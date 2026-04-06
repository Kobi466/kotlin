# Auth Register Role Design

**Goal:** Add in-app user registration while keeping admin accounts created manually in Firebase Console.

## Product Rules

- Admin accounts are created manually in Firebase Authentication.
- Each admin account must also have `users/{uid}` in Firestore with:

```json
{
  "role": "admin"
}
```

- Users created from the app always get:

```json
{
  "role": "user"
}
```

- The app must not expose any path that can create an admin account.

## Architecture

- Keep Firebase auth and Firestore access in `FirebaseNoteRepository`.
- Keep UI state and auth mode switching in `CrudViewModel`.
- Keep field validation helpers in `CrudLogic`.
- Extend the existing auth screen so it can switch between `Login` and `Register`.

## Data Flow

1. User switches auth mode to `Register`.
2. User enters `email`, `password`, and `confirmPassword`.
3. App validates the form locally.
4. App calls Firebase Auth `createUserWithEmailAndPassword`.
5. App writes `users/{uid}` with `role = "user"`.
6. App loads the session as a normal signed-in user.

## Security

- Firestore rules must allow a signed-in user to create exactly one self profile at `users/{uid}` with only `role = "user"`.
- Firestore rules must keep admin-only write access for notes.
- Admin elevation remains manual in Firebase Console only.

## Testing

- Add unit tests for register validation.
- Verify unit tests pass.
- Verify Android debug build succeeds.
