# Auth Register Role Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add user registration in the app while keeping admin creation manual in Firebase.

**Architecture:** Extend the current auth screen to support two modes, put registration in the existing repository, and keep local validation in `CrudLogic`. Firestore rules will allow self-creation of `users/{uid}` with `role = "user"` only.

**Tech Stack:** Kotlin, Jetpack Compose, Firebase Authentication, Cloud Firestore, Firebase Storage, JUnit

---

### Task 1: Add register validation helpers

**Files:**
- Modify: `app/src/test/java/com/example/giuaky/ExampleUnitTest.kt`
- Modify: `app/src/main/java/com/example/giuaky/CrudLogic.kt`

- [ ] Step 1: Write failing tests for register validation and default role value.
- [ ] Step 2: Run `./gradlew.bat testDebugUnitTest` and confirm the new tests fail.
- [ ] Step 3: Add minimal register helper functions to `CrudLogic.kt`.
- [ ] Step 4: Run `./gradlew.bat testDebugUnitTest` and confirm tests pass.

### Task 2: Add register state and Firebase flow

**Files:**
- Modify: `app/src/main/java/com/example/giuaky/CrudModels.kt`
- Modify: `app/src/main/java/com/example/giuaky/FirebaseNoteRepository.kt`
- Modify: `app/src/main/java/com/example/giuaky/CrudViewModel.kt`

- [ ] Step 1: Add auth mode and confirm password state.
- [ ] Step 2: Add repository registration that writes `users/{uid}` with `role = "user"`.
- [ ] Step 3: Add ViewModel events for mode switching and register submit.
- [ ] Step 4: Run unit tests again.

### Task 3: Extend the auth UI

**Files:**
- Modify: `app/src/main/java/com/example/giuaky/CrudApp.kt`

- [ ] Step 1: Show login/register mode toggle.
- [ ] Step 2: Show confirm password field in register mode.
- [ ] Step 3: Wire the primary button to login or register.
- [ ] Step 4: Keep the notes and editor screens unchanged.

### Task 4: Update security rules and docs

**Files:**
- Modify: `firestore.rules`
- Modify: `docs/firebase-setup.md`

- [ ] Step 1: Allow self-create of `users/{uid}` with `role = "user"`.
- [ ] Step 2: Document the manual admin setup steps and the new register flow.

### Task 5: Verify

**Files:**
- No code changes required.

- [ ] Step 1: Run `./gradlew.bat testDebugUnitTest`.
- [ ] Step 2: Run `./gradlew.bat assembleDebug`.
- [ ] Step 3: Report the exact verification results.
