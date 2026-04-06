package com.example.giuaky

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.giuaky.ui.components.AppBusyOverlay
import com.example.giuaky.ui.components.AppLoadingScreen
import com.example.giuaky.ui.screens.auth.AuthScreen
import com.example.giuaky.ui.screens.editor.NoteEditorScreen
import com.example.giuaky.ui.screens.notes.NotesScreen
import com.example.giuaky.ui.theme.AccentOrange
import com.example.giuaky.ui.theme.AppBackground
import com.example.giuaky.ui.theme.TextPrimary

@Composable
fun CrudNoteApp(viewModel: CrudViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser = uiState.currentUser
    val editorState = uiState.editor

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        viewModel.consumeSnackbar()
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            val showAddButton = currentUser != null &&
                uiState.editor == null &&
                uiState.canEditNotes

            if (showAddButton) {
                FloatingActionButton(
                    onClick = viewModel::openAddEditor,
                    containerColor = AccentOrange,
                    contentColor = TextPrimary,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add note")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isInitializing -> AppLoadingScreen()
                currentUser == null -> AuthScreen(
                    authMode = uiState.authMode,
                    email = uiState.email,
                    password = uiState.password,
                    confirmPassword = uiState.confirmPassword,
                    isBusy = uiState.isBusy,
                    onAuthModeChange = viewModel::onAuthModeChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                    onLoginClick = viewModel::login,
                    onRegisterClick = viewModel::register
                )

                editorState != null -> NoteEditorScreen(
                    userRole = currentUser.role,
                    editor = editorState,
                    isBusy = uiState.isBusy,
                    onBackClick = viewModel::closeEditor,
                    onTitleChange = viewModel::onTitleChange,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    onFileSelected = viewModel::onFileSelected,
                    onSaveClick = viewModel::saveNote,
                    onDeleteClick = viewModel::deleteCurrentNote
                )

                else -> NotesScreen(
                    currentUser = currentUser,
                    notes = uiState.notes,
                    onLogoutClick = viewModel::logout,
                    onNoteClick = viewModel::openEditEditor
                )
            }

            if (uiState.isBusy) {
                AppBusyOverlay()
            }
        }
    }
}
