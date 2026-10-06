package com.gitdrip.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gitdrip.app.asHeading

/** Icon-only action: >= 48dp target, spoken [description], dimmed (and inert) when disabled. */
@Composable
fun GlassIconButton(icon: ImageVector, description: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) =
    IconButton(onClick, modifier.alpha(if (enabled) 1f else 0.4f), enabled = enabled) { Icon(icon, description) }

/** Warning-style card with optional action; icon + text, never colour only. Text is announced as one node. */
@Composable
fun NoticeCard(text: String, modifier: Modifier = Modifier, actionLabel: String? = null, onAction: () -> Unit = {}) =
    GlassCard(modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(Space.M.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        }
        if (actionLabel != null) GlassButton(actionLabel, onAction, style = GlassButtonStyle.Text)
    }

/** Dialog that only offers choices (no confirm button): each option is a full-width >= 48dp row. */
@Composable
fun GlassChoiceDialog(title: String, options: List<Pair<String, () -> Unit>>, onDismiss: () -> Unit) = AlertDialog(
    onDismissRequest = onDismiss, shape = SheetShape, containerColor = MaterialTheme.colorScheme.surfaceContainerHigh, tonalElevation = 0.dp,
    title = { Text(title, Modifier.asHeading()) },
    text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Space.S.dp)) {
            items(options) { o -> GlassButton(o.first, o.second, Modifier.fillMaxWidth(), style = GlassButtonStyle.Secondary) }
        }
    },
    confirmButton = {},
    dismissButton = { GlassButton("Cancel", onDismiss, style = GlassButtonStyle.Text) },
)
