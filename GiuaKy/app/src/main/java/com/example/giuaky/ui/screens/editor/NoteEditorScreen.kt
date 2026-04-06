package com.example.giuaky.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.giuaky.CrudLogic
import com.example.giuaky.UserRole
import com.example.giuaky.ui.components.AppPrimaryButton
import com.example.giuaky.ui.components.AppSectionCard
import com.example.giuaky.ui.components.AppTextField
import com.example.giuaky.ui.state.NoteEditorState
import com.example.giuaky.ui.theme.AccentOrange
import com.example.giuaky.ui.theme.AppSurfaceSoft
import com.example.giuaky.ui.theme.DangerRed
import com.example.giuaky.ui.theme.TextPrimary
import com.example.giuaky.ui.theme.TextSecondary

@Composable
fun NoteEditorScreen(
    userRole: UserRole,
    editor: NoteEditorState,
    isBusy: Boolean,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFileSelected: (Uri) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onFileSelected(uri)
        }
    }
    val uriHandler = LocalUriHandler.current
    val hasFile = editor.selectedFile != null || editor.currentFileUrl.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Text(
                text = if (editor.isEditing) "Sua note" else "Them note",
                color = TextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            if (editor.isEditing && userRole == UserRole.ADMIN) {
                IconButton(onClick = onDeleteClick, enabled = !isBusy) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = DangerRed
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        AppSectionCard {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(
                    value = editor.title,
                    onValueChange = onTitleChange,
                    label = "Title",
                    singleLine = true
                )

                AppTextField(
                    value = editor.description,
                    onValueChange = onDescriptionChange,
                    label = "Description",
                    minLines = 5
                )

                Text(
                    text = "File hoac hinh anh",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = { filePicker.launch("*/*") },
                    enabled = !isBusy,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppSurfaceSoft,
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = if (editor.selectedFile == null) "Chon file" else "Doi file"
                    )
                }

                if (hasFile) {
                    EditorPreview(
                        editor = editor,
                        onOpenRemoteFile = {
                            if (editor.currentFileUrl.isNotBlank()) {
                                uriHandler.openUri(editor.currentFileUrl)
                            }
                        }
                    )
                }

                AppPrimaryButton(
                    text = if (editor.isEditing) "UPDATE NOTE" else "ADD NOTE",
                    onClick = onSaveClick,
                    enabled = !isBusy
                )
            }
        }
    }
}

@Composable
private fun EditorPreview(
    editor: NoteEditorState,
    onOpenRemoteFile: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        editor.selectedFile?.let { localFile ->
            Text(
                text = "File moi: ${localFile.name}",
                color = TextSecondary
            )

            if (CrudLogic.isImageFile(localFile.name)) {
                AsyncImage(
                    model = localFile.uri,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        if (editor.currentFileUrl.isNotBlank() && editor.selectedFile == null) {
            Text(
                text = "File hien tai da luu tren Firebase Storage",
                color = TextSecondary
            )

            if (CrudLogic.isImageFile(editor.currentFileUrl)) {
                AsyncImage(
                    model = editor.currentFileUrl,
                    contentDescription = "Current image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            TextButton(onClick = onOpenRemoteFile) {
                Text("Mo file hien tai", color = AccentOrange)
            }
        }
    }
}
