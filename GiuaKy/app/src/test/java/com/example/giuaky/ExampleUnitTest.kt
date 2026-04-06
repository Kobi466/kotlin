package com.example.giuaky

import com.example.giuaky.ui.state.AuthMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun normalizeEmail_trimsSpaces() {
        assertEquals("admin@gmail.com", CrudLogic.normalizeEmail("  admin@gmail.com  "))
    }

    @Test
    fun canLogin_requiresEmailAndPassword() {
        assertTrue(CrudLogic.canLogin("admin@gmail.com", "123456"))
        assertFalse(CrudLogic.canLogin("   ", "123456"))
        assertFalse(CrudLogic.canLogin("admin@gmail.com", "   "))
    }

    @Test
    fun canRegister_requiresEmailPasswordAndMatchingConfirmPassword() {
        assertTrue(CrudLogic.canRegister("user@gmail.com", "123456", "123456"))
        assertFalse(CrudLogic.canRegister("   ", "123456", "123456"))
        assertFalse(CrudLogic.canRegister("user@gmail.com", "123", "123"))
        assertFalse(CrudLogic.canRegister("user@gmail.com", "123456", "654321"))
    }

    @Test
    fun defaultUserRoleValue_isUser() {
        assertEquals("user", CrudLogic.defaultUserRoleValue())
    }

    @Test
    fun authPrimaryLabel_changesWithMode() {
        assertEquals("LOGIN", CrudLogic.authPrimaryLabel(AuthMode.LOGIN))
        assertEquals("REGISTER", CrudLogic.authPrimaryLabel(AuthMode.REGISTER))
    }

    @Test
    fun authTitle_changesWithMode() {
        assertEquals("Welcome back", CrudLogic.authTitle(AuthMode.LOGIN))
        assertEquals("Create account", CrudLogic.authTitle(AuthMode.REGISTER))
    }

    @Test
    fun storagePath_replacesUnsafeCharacters() {
        assertEquals(
            "notes/note-1/de_thi.pdf",
            CrudLogic.storagePath("note-1", "de thi.pdf")
        )
    }

    @Test
    fun canSaveNote_requiresTitleDescriptionAndFile() {
        assertTrue(CrudLogic.canSaveNote("Hello", "World", hasFile = true))
        assertFalse(CrudLogic.canSaveNote("", "World", hasFile = true))
        assertFalse(CrudLogic.canSaveNote("Hello", "", hasFile = true))
        assertFalse(CrudLogic.canSaveNote("Hello", "World", hasFile = false))
    }
}
