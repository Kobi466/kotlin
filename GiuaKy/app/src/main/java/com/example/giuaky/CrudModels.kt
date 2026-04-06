package com.example.giuaky

import android.net.Uri

data class NoteDto(
    val title: String = "",
    val description: String = "",
    val file: String = ""
)

data class UserProfileDto(
    val role: String = CrudLogic.defaultUserRoleValue()
)

data class NoteItem(
    val id: String,
    val title: String,
    val description: String,
    val file: String
)

enum class UserRole {
    ADMIN,
    USER
}

data class SessionUser(
    val uid: String,
    val email: String,
    val role: UserRole
)

data class LocalFile(
    val uri: Uri,
    val name: String
)
