package com.example.giuaky.ui.state

import com.example.giuaky.CrudLogic
import com.example.giuaky.LocalFile
import com.example.giuaky.NoteItem
import com.example.giuaky.SessionUser
import com.example.giuaky.UserRole

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class NoteEditorState(
    val noteId: String? = null,
    val title: String = "",
    val description: String = "",
    val currentFileUrl: String = "",
    val selectedFile: LocalFile? = null
) {
    val isEditing: Boolean
        get() = noteId != null
}

data class AppUiState(
    val isInitializing: Boolean = true,
    val isBusy: Boolean = false,
    val authMode: AuthMode = AuthMode.LOGIN,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val currentUser: SessionUser? = null,
    val notes: List<NoteItem> = emptyList(),
    val editor: NoteEditorState? = null,
    val snackbarMessage: String? = null
) {
    val canLogin: Boolean
        get() = CrudLogic.canLogin(email, password)

    val canRegister: Boolean
        get() = CrudLogic.canRegister(email, password, confirmPassword)

    val canEditNotes: Boolean
        get() = currentUser?.role == UserRole.ADMIN
}
