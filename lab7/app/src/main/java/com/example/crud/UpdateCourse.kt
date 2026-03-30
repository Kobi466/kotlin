package com.example.crud

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.crud.ui.theme.CRUDTheme

class UpdateCourse : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val courseID = intent.getStringExtra("courseID") ?: ""
        
        if (courseID.isBlank()) {
            Toast.makeText(this, "Error: Missing ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val initialName = intent.getStringExtra("courseName") ?: ""
        val initialDuration = intent.getStringExtra("courseDuration") ?: ""
        val initialDescription = intent.getStringExtra("courseDescription") ?: ""

        // Senior Mentor Note: Showing a verification toast to confirm data loading
        Toast.makeText(this, "Editing: $initialName", Toast.LENGTH_SHORT).show()

        setContent {
            CRUDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Edit Course Details", fontWeight = FontWeight.ExtraBold) },
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                    }
                                },
                                // Change colors to be DIFFERENT from MainActivity
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    ) { innerPadding ->
                        UpdateCourseScreen(
                            modifier = Modifier.padding(innerPadding),
                            context = LocalContext.current,
                            id = courseID,
                            name = initialName,
                            duration = initialDuration,
                            description = initialDescription,
                            onFinish = { finish() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateCourseScreen(
    modifier: Modifier = Modifier,
    context: Context,
    id: String,
    name: String,
    duration: String,
    description: String,
    onFinish: () -> Unit
) {
    var courseName by remember { mutableStateOf(name) }
    var courseDuration by remember { mutableStateOf(duration) }
    var courseDescription by remember { mutableStateOf(description) }
    var isLoading by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val repository = remember { FirestoreRepository() }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete This Course?") },
            text = { Text("Are you sure you want to permanently remove \"$courseName\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        isLoading = true
                        repository.deleteCourse(
                            courseID = id,
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                                onFinish()
                            },
                            onFailure = { e ->
                                isLoading = false
                                Toast.makeText(context, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Visual indicator that this is an EDIT mode
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Document ID: ${id.take(8)}...", 
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        OutlinedTextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = { Text("Course Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        OutlinedTextField(
            value = courseDuration,
            onValueChange = { courseDuration = it },
            label = { Text("Duration") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        OutlinedTextField(
            value = courseDescription,
            onValueChange = { courseDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.weight(1f))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (courseName.isBlank() || courseDuration.isBlank() || courseDescription.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        val updatedCourse = Course(
                            courseID = id,
                            courseName = courseName,
                            courseDuration = courseDuration,
                            courseDescription = courseDescription
                        )
                        repository.updateCourse(
                            course = updatedCourse,
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(context, "Changes saved!", Toast.LENGTH_SHORT).show()
                                onFinish()
                            },
                            onFailure = { e ->
                                isLoading = false
                                Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("SAVE CHANGES")
            }

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("DELETE COURSE")
            }
        }
    }
}