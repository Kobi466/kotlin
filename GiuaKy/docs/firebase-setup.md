# Firebase Setup Cho De Giua Ky

App nay dang dung 3 dich vu Firebase:

1. `Authentication`
2. `Cloud Firestore`
3. `Firebase Storage`

## 1. Authentication

- Bat `Email/Password` trong Firebase Authentication.
- Tao it nhat 2 tai khoan:
  - `admin@gmail.com`
  - `user@gmail.com`

## 2. Firestore

Tao collection `users` de luu role:

```text
users/{uid}
  role: "admin" | "user"
```

Vi du:

```json
{
  "role": "admin"
}
```

Collection note phai giu dung 3 field theo de:

```text
notes/{noteId}
  title: string
  description: string
  file: string
```

Khong them field timestamp, owner, category...

## 3. Firebase Storage

File duoc upload vao duong dan:

```text
notes/{noteId}/{fileName}
```

Field `file` trong Firestore se luu download URL cua file da upload.

## 4. Security Rules

- Firestore rules: [firestore.rules](/d:/MyFiles/MyApplication/GiuaKy/firestore.rules)
- Storage rules: [storage.rules](/d:/MyFiles/MyApplication/GiuaKy/storage.rules)

Y nghia:

- User da dang nhap thi duoc xem note.
- Admin moi duoc them, sua, xoa note.
- Note chi duoc phep co 3 field: `title`, `description`, `file`.

## 5. Mapping Flutter -> Kotlin Compose

- `Widget` -> `@Composable`
- `setState()` -> `MutableStateFlow` + `collectAsState()`
- `Navigator.push()` -> doi `editor` state de chuyen man
- `Firebase service class` -> [FirebaseNoteRepository](/d:/MyFiles/MyApplication/GiuaKy/app/src/main/java/com/example/giuaky/FirebaseNoteRepository.kt)
- `Provider/Bloc/ViewModel` -> [CrudViewModel](/d:/MyFiles/MyApplication/GiuaKy/app/src/main/java/com/example/giuaky/CrudViewModel.kt)

## 6. File Chinh

- UI: [CrudApp.kt](/d:/MyFiles/MyApplication/GiuaKy/app/src/main/java/com/example/giuaky/CrudApp.kt)
- Logic nho: [CrudLogic.kt](/d:/MyFiles/MyApplication/GiuaKy/app/src/main/java/com/example/giuaky/CrudLogic.kt)
- ViewModel: [CrudViewModel.kt](/d:/MyFiles/MyApplication/GiuaKy/app/src/main/java/com/example/giuaky/CrudViewModel.kt)
- Firebase repo: [FirebaseNoteRepository.kt](/d:/MyFiles/MyApplication/GiuaKy/app/src/main/java/com/example/giuaky/FirebaseNoteRepository.kt)
