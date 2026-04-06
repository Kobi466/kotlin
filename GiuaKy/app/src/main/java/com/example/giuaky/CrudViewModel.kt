package com.example.giuaky

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.example.giuaky.ui.state.AppUiState
import com.example.giuaky.ui.state.AuthMode
import com.example.giuaky.ui.state.NoteEditorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CrudViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseNoteRepository()
    private var notesListener: ListenerRegistration? = null

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        restoreSession()
    }

    fun onAuthModeChange(mode: AuthMode) {
        _uiState.update {
            it.copy(
                authMode = mode,
                password = "",
                confirmPassword = ""
            )
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun login() {
        val currentState = _uiState.value
        if (!currentState.canLogin) {
            showMessage("Enter email and password before login.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true) }

            runCatching {
                repository.signIn(
                    email = CrudLogic.normalizeEmail(currentState.email),
                    password = currentState.password
                )
            }.onSuccess { sessionUser ->
                startListeningNotes()
                _uiState.update {
                    it.copy(
                        isBusy = false,
                        password = "",
                        confirmPassword = "",
                        currentUser = sessionUser,
                        editor = null,
                        snackbarMessage = "Login success. Role: ${sessionUser.role.name}."
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isBusy = false) }
                showMessage(error.message ?: "Login failed.")
            }
        }
    }

    fun register() {
        val currentState = _uiState.value
        if (!currentState.canRegister) {
            showMessage("Register needs email, password >= 6, and matching confirm password.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true) }

            runCatching {
                repository.signUp(
                    email = CrudLogic.normalizeEmail(currentState.email),
                    password = currentState.password.trim()
                )
            }.onSuccess { sessionUser ->
                startListeningNotes()
                _uiState.update {
                    it.copy(
                        isBusy = false,
                        authMode = AuthMode.LOGIN,
                        password = "",
                        confirmPassword = "",
                        currentUser = sessionUser,
                        editor = null,
                        snackbarMessage = "Register success. New account is USER by default."
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isBusy = false) }
                showMessage(error.message ?: "Register failed.")
            }
        }
    }

    fun logout() {
        notesListener?.remove()
        notesListener = null
        repository.signOut()
        _uiState.update {
            it.copy(
                currentUser = null,
                password = "",
                confirmPassword = "",
                notes = emptyList(),
                editor = null,
                snackbarMessage = "Signed out."
            )
        }
    }

    fun openAddEditor() {
        if (_uiState.value.canEditNotes.not()) {
            showMessage("Only admin can add notes.")
            return
        }

        _uiState.update { it.copy(editor = NoteEditorState()) }
    }

    fun openEditEditor(note: NoteItem) {
        if (_uiState.value.canEditNotes.not()) {
            return
        }

        _uiState.update {
            it.copy(
                editor = NoteEditorState(
                    noteId = note.id,
                    title = note.title,
                    description = note.description,
                    currentFileUrl = note.file
                )
            )
        }
    }

    fun closeEditor() {
        _uiState.update { it.copy(editor = null) }
    }

    fun onTitleChange(value: String) {
        _uiState.update { state ->
            state.copy(editor = state.editor?.copy(title = value))
        }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { state ->
            state.copy(editor = state.editor?.copy(description = value))
        }
    }

    fun onFileSelected(uri: Uri) {
        val fileName = resolveFileName(uri)
        _uiState.update { state ->
            state.copy(
                editor = state.editor?.copy(
                    selectedFile = LocalFile(uri = uri, name = fileName)
                )
            )
        }
    }

    fun saveNote() {
        val editor = _uiState.value.editor ?: return
        val hasFile = editor.selectedFile != null || editor.currentFileUrl.isNotBlank()

        if (!CrudLogic.canSaveNote(editor.title, editor.description, hasFile)) {
            showMessage("Enter title, description, and a file before saving.")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true) }

            runCatching {
                if (editor.isEditing) {
                    repository.updateNote(
                        noteId = editor.noteId.orEmpty(),
                        title = editor.title,
                        description = editor.description,
                        currentFileUrl = editor.currentFileUrl,
                        selectedFile = editor.selectedFile
                    )
                } else {
                    repository.addNote(
                        title = editor.title,
                        description = editor.description,
                        file = editor.selectedFile ?: error("File is required.")
                    )
                }
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isBusy = false,
                        editor = null,
                        snackbarMessage = if (editor.isEditing) {
                            "Note updated."
                        } else {
                            "Note created."
                        }
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isBusy = false) }
                showMessage(error.message ?: "Save note failed.")
            }
        }
    }

    fun deleteCurrentNote() {
        val editor = _uiState.value.editor ?: return
        val noteId = editor.noteId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true) }

            runCatching {
                repository.deleteNote(
                    NoteItem(
                        id = noteId,
                        title = editor.title,
                        description = editor.description,
                        file = editor.currentFileUrl
                    )
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isBusy = false,
                        editor = null,
                        snackbarMessage = "Note deleted."
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isBusy = false) }
                showMessage(error.message ?: "Delete note failed.")
            }
        }
    }

    fun consumeSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    override fun onCleared() {
        notesListener?.remove()
        notesListener = null
        super.onCleared()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            runCatching {
                repository.restoreSession()
            }.onSuccess { sessionUser ->
                if (sessionUser != null) {
                    startListeningNotes()
                }

                _uiState.update {
                    it.copy(
                        isInitializing = false,
                        currentUser = sessionUser
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isInitializing = false) }
                showMessage(error.message ?: "Could not restore session.")
            }
        }
    }

    private fun startListeningNotes() {
        notesListener?.remove()
        notesListener = repository.listenNotes(
            onChanged = { notes ->
                _uiState.update {
                    it.copy(
                        isInitializing = false,
                        notes = notes
                    )
                }
            },
            onError = { error ->
                showMessage(error.message ?: "Could not load notes.")
            }
        )
    }

    private fun resolveFileName(uri: Uri): String {
        val resolver = getApplication<Application>().contentResolver

        if (uri.scheme == "content") {
            resolver.query(uri, null, null, null, null)?.use { cursor ->
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0 && cursor.moveToFirst()) {
                    return cursor.getString(index)
                }
            }
        }

        return uri.lastPathSegment
            ?.substringAfterLast('/')
            .orEmpty()
            .ifBlank { "attachment" }
    }

    private fun showMessage(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }
}
