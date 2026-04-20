package com.example.bluromatic.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bluromatic.R

data class BlurOption(val label: String, val value: Int)

private val blurOptions = listOf(
    BlurOption("A little blurred", 1),
    BlurOption("More blurred",     2),
    BlurOption("The most blurred", 3)
)


@Composable
fun BluromaticScreen(
    blurViewModel: BlurViewModel = viewModel(factory = BlurViewModel.Factory)
) {
    val uiState by blurViewModel.blurUiState.collectAsStateWithLifecycle()
    BluromaticScreenContent(
        uiState     = uiState,
        onStartBlur = { level -> blurViewModel.applyBlur(level) },
        onCancel    = { blurViewModel.cancelWork() }
    )
}

@Composable
fun BluromaticScreenContent(
    uiState     : BlurUiState,
    onStartBlur : (Int) -> Unit,
    onCancel    : () -> Unit
) {
    var selectedBlur by remember { mutableStateOf(blurOptions[0]) }
    val bgColor = Color(0xFF2ABFBF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Blur-O-Matic", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            val imageData: Any = when (uiState) {
                is BlurUiState.Complete -> uiState.outputUri
                else -> R.drawable.android_cupcake
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageData)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (uiState is BlurUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 4.dp)
                }
            }
        }

        Card(
            modifier  = Modifier.fillMaxWidth().padding(16.dp),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("Select Blur Amount", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))

                Column(modifier = Modifier.selectableGroup()) {
                    blurOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = option == selectedBlur,
                                    onClick  = { selectedBlur = option },
                                    role     = Role.RadioButton,
                                    enabled  = uiState !is BlurUiState.Loading
                                )
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = option == selectedBlur,
                                onClick  = null,
                                colors   = RadioButtonDefaults.colors(selectedColor = bgColor),
                                enabled  = uiState !is BlurUiState.Loading
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(option.label, fontSize = 15.sp)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                when (uiState) {
                    is BlurUiState.Complete ->
                        Text(" Image saved to gallery!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                    is BlurUiState.Error ->
                        Text(" Something went wrong.", color = Color(0xFFC62828), fontWeight = FontWeight.Medium)
                    is BlurUiState.Loading ->
                        Text(" Working in background…", color = Color(0xFF1565C0), fontWeight = FontWeight.Medium)
                    else -> {}
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (uiState is BlurUiState.Loading) {
                        OutlinedButton(
                            onClick  = onCancel,
                            modifier = Modifier.weight(1f),
                            colors   = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828))
                        ) { Text("Cancel") }
                    }
                    Button(
                        onClick  = { onStartBlur(selectedBlur.value) },
                        enabled  = uiState !is BlurUiState.Loading,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.buttonColors(containerColor = bgColor),
                        shape    = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            if (uiState is BlurUiState.Loading) "Processing…" else "Start",
                            color = Color.White, fontSize = 16.sp
                        )
                    }
                }
            }
        }

        // Info box
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape    = RoundedCornerShape(12.dp),
            colors   = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(" WorkManager Chain", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0D47A1))
                Spacer(Modifier.height(4.dp))
                Text("CleanupWorker → BlurWorker × level → SaveImageToFileWorker", fontSize = 12.sp, color = Color(0xFF1565C0))
                Text("Constraint: Battery not low", fontSize = 12.sp, color = Color(0xFF1565C0))
            }
        }
    }
}
