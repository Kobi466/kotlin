package com.example.giuaky.ui.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.giuaky.CrudLogic
import com.example.giuaky.NoteItem
import com.example.giuaky.SessionUser
import com.example.giuaky.UserRole
import com.example.giuaky.ui.theme.AccentOrange
import com.example.giuaky.ui.theme.AppSurface
import com.example.giuaky.ui.theme.AppSurfaceSoft
import com.example.giuaky.ui.theme.TextPrimary
import com.example.giuaky.ui.theme.TextSecondary

@Composable
fun NotesScreen(
    currentUser: SessionUser,
    notes: List<NoteItem>,
    onLogoutClick: () -> Unit,
    onNoteClick: (NoteItem) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentUser.email,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            TextButton(onClick = onLogoutClick) {
                Text("Dang xuat", color = AccentOrange, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AssistChip(
            onClick = {},
            enabled = false,
            label = {
                Text(
                    text = if (currentUser.role == UserRole.ADMIN) {
                        "Admin: co the them, sua, xoa"
                    } else {
                        "User: chi xem"
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (notes.isEmpty()) {
            EmptyNotesState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        canEdit = currentUser.role == UserRole.ADMIN,
                        onOpenFile = { uriHandler.openUri(note.file) },
                        onEditClick = { onNoteClick(note) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyNotesState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Chua co ghi chu nao",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Admin co the nhan nut cong de tao note dau tien.",
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun NoteCard(
    note: NoteItem,
    canEdit: Boolean,
    onOpenFile: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (canEdit) Modifier.clickable(onClick = onEditClick) else Modifier),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = note.title,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = note.description,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            if (note.file.isNotBlank() && CrudLogic.isImageFile(note.file)) {
                AsyncImage(
                    model = note.file,
                    contentDescription = "Anh cua ${note.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (canEdit) "Cham vao the de sua" else "Chi co the xem",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                TextButton(onClick = onOpenFile) {
                    Text("Mo file", color = AccentOrange)
                }
            }
        }
    }
}
