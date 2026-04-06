package com.example.giuaky

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    suspend fun restoreSession(): SessionUser? {
        val firebaseUser = auth.currentUser ?: return null
        return SessionUser(
            uid = firebaseUser.uid,
            email = firebaseUser.email.orEmpty(),
            role = resolveRole(firebaseUser.uid)
        )
    }

    suspend fun signIn(email: String, password: String): SessionUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: error("Firebase user is missing.")

        return SessionUser(
            uid = firebaseUser.uid,
            email = firebaseUser.email.orEmpty(),
            role = resolveRole(firebaseUser.uid)
        )
    }

    suspend fun signUp(email: String, password: String): SessionUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: error("Firebase user is missing.")

        try {
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(UserProfileDto(role = CrudLogic.defaultUserRoleValue()))
                .await()
        } catch (error: Throwable) {
            runCatching { firebaseUser.delete().await() }
            throw error
        }

        return SessionUser(
            uid = firebaseUser.uid,
            email = firebaseUser.email.orEmpty(),
            role = UserRole.USER
        )
    }

    fun signOut() {
        auth.signOut()
    }

    fun listenNotes(
        onChanged: (List<NoteItem>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        return firestore.collection("notes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                val notes = snapshot?.documents.orEmpty()
                    .map { document ->
                        val dto = document.toObject(NoteDto::class.java) ?: NoteDto()
                        NoteItem(
                            id = document.id,
                            title = dto.title,
                            description = dto.description,
                            file = dto.file
                        )
                    }
                    .sortedBy { it.title.lowercase() }

                onChanged(notes)
            }
    }

    suspend fun addNote(title: String, description: String, file: LocalFile) {
        val noteRef = firestore.collection("notes").document()
        val fileUrl = uploadFile(noteRef.id, file)

        noteRef.set(
            NoteDto(
                title = title.trim(),
                description = description.trim(),
                file = fileUrl
            )
        ).await()
    }

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        currentFileUrl: String,
        selectedFile: LocalFile?
    ) {
        val finalFileUrl = if (selectedFile != null) {
            deleteFileIfNeeded(currentFileUrl)
            uploadFile(noteId, selectedFile)
        } else {
            currentFileUrl
        }

        firestore.collection("notes")
            .document(noteId)
            .set(
                NoteDto(
                    title = title.trim(),
                    description = description.trim(),
                    file = finalFileUrl
                )
            )
            .await()
    }

    suspend fun deleteNote(note: NoteItem) {
        deleteFileIfNeeded(note.file)
        firestore.collection("notes").document(note.id).delete().await()
    }

    private suspend fun resolveRole(uid: String): UserRole {
        val roleValue = firestore.collection("users")
            .document(uid)
            .get()
            .await()
            .getString("role")
            .orEmpty()
            .lowercase()

        return if (roleValue == "admin") {
            UserRole.ADMIN
        } else {
            UserRole.USER
        }
    }

    private suspend fun uploadFile(noteId: String, file: LocalFile): String {
        val storageRef = storage.reference.child(CrudLogic.storagePath(noteId, file.name))
        storageRef.putFile(file.uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    private suspend fun deleteFileIfNeeded(fileUrl: String) {
        if (fileUrl.isBlank()) {
            return
        }

        runCatching {
            storage.getReferenceFromUrl(fileUrl).delete().await()
        }
    }
}
