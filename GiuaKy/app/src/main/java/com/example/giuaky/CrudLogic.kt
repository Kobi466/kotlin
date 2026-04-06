package com.example.giuaky

import com.example.giuaky.ui.state.AuthMode

object CrudLogic {
    fun normalizeEmail(raw: String): String = raw.trim()

    fun authTitle(mode: AuthMode): String {
        return if (mode == AuthMode.LOGIN) {
            "Welcome back"
        } else {
            "Create account"
        }
    }

    fun authPrimaryLabel(mode: AuthMode): String {
        return if (mode == AuthMode.LOGIN) {
            "LOGIN"
        } else {
            "REGISTER"
        }
    }

    fun canLogin(email: String, password: String): Boolean {
        return normalizeEmail(email).isNotEmpty() && password.trim().isNotEmpty()
    }

    fun canRegister(email: String, password: String, confirmPassword: String): Boolean {
        val cleanEmail = normalizeEmail(email)
        val cleanPassword = password.trim()
        val cleanConfirmPassword = confirmPassword.trim()

        return cleanEmail.isNotEmpty() &&
            cleanPassword.length >= 6 &&
            cleanPassword == cleanConfirmPassword
    }

    fun defaultUserRoleValue(): String = "user"

    fun canSaveNote(title: String, description: String, hasFile: Boolean): Boolean {
        return title.trim().isNotEmpty() &&
            description.trim().isNotEmpty() &&
            hasFile
    }

    fun storagePath(noteId: String, fileName: String): String {
        val safeName = fileName
            .trim()
            .ifEmpty { "attachment" }
            .replace(Regex("[^A-Za-z0-9._-]"), "_")

        return "notes/$noteId/$safeName"
    }

    fun isImageFile(value: String): Boolean {
        val lowerValue = value
            .substringBefore('?')
            .lowercase()

        return lowerValue.endsWith(".png") ||
            lowerValue.endsWith(".jpg") ||
            lowerValue.endsWith(".jpeg") ||
            lowerValue.endsWith(".webp") ||
            lowerValue.endsWith(".gif")
    }
}
