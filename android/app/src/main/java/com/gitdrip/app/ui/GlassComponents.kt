@file:OptIn(ExperimentalMaterial3Api::class)

package com.gitdrip.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitdrip.app.LabeledSwitch
import com.gitdrip.app.asHeading

private val MinTouch = Touch.Min.dp

/** Root of every screen: aurora background + transparent Scaffold (system-bar insets handled by Scaffold). */
@Composable
fun GlassScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) = GlassBackground(modifier) {
    Scaffold(
        containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = topBar, floatingActionButton = floatingActionButton, content = content,
    )
}

/** Top bar keeping the heading semantics + labelled Back button (>= 48dp via IconButton). */
@Composable
fun GlassTopBar(title: String, onBack: (() -> Unit)? = null, actions: @Composable RowScope.() -> Unit = {}) = TopAppBar(
    title = { Text(title, Modifier.asHeading()) },
    navigationIcon = { if (onBack != null) IconButton(onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
    actions = actions,
    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, scrolledContainerColor = Color.Transparent),
)

/** Glass container. With [onClick] the whole card is one >= 48dp button target and darkens to L3 while pressed. */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    level: GlassLevel = GlassLevel.L1,
    shape: androidx.compose.ui.graphics.Shape = CardShape,
    content: @Composable ColumnScope.() -> Unit,
) {
    val src = remember { MutableInteractionSource() }
    val pressed by src.collectIsPressedAsState()
    var m = modifier.let { if (onClick != null) it.pressScale(src) else it }.glass(if (pressed) GlassLevel.L3 else level, shape)
    if (onClick != null) m = m.heightIn(min = MinTouch).clickable(src, LocalIndication.current, role = Role.Button, onClickLabel = onClickLabel, onClick = onClick)
    Column(m.padding(Space.L.dp), content = content)
}

/** One number + label. Merged into a single TalkBack node "<label>: <value>" (same as the old Stat). */
@Composable
fun StatTile(label: String, value: Int, modifier: Modifier = Modifier, icon: ImageVector? = null, accent: Color = MaterialTheme.colorScheme.primary) =
    GlassCard(modifier.semantics(mergeDescendants = true) { contentDescription = "$label: $value" }) {
        if (icon != null) Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp))
        Text("$value", style = MaterialTheme.typography.headlineMedium)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

enum class GlassButtonStyle { Primary, Secondary, Text }

@Composable
fun GlassButton(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    style: GlassButtonStyle = GlassButtonStyle.Primary, enabled: Boolean = true, icon: ImageVector? = null,
) {
    val label = when (style) {
        GlassButtonStyle.Primary -> Color(Palette.OnButton)
        GlassButtonStyle.Secondary -> MaterialTheme.colorScheme.onSurface
        GlassButtonStyle.Text -> MaterialTheme.colorScheme.primary
    }
    val src = remember { MutableInteractionSource() }
    val base = modifier.pressScale(src).heightIn(min = MinTouch).alpha(if (enabled) 1f else 0.45f)
    val shaped = when (style) {
        GlassButtonStyle.Primary -> base.clip(ControlShape).background(Brush.horizontalGradient(listOf(Color(Palette.ButtonStart), Color(Palette.ButtonEnd))))
        GlassButtonStyle.Secondary -> base.glass(GlassLevel.L1, ControlShape)
        GlassButtonStyle.Text -> base.clip(ControlShape)
    }
    Row(
        shaped.clickable(src, LocalIndication.current, enabled = enabled, role = Role.Button, onClick = onClick).padding(horizontal = Space.L.dp, vertical = Space.S.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
    ) {
        if (icon != null) { Icon(icon, null, tint = label, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(Space.S.dp)) }
        Text(text, color = label, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}

@Composable
fun GlassTextField(
    value: String, onChange: (String) -> Unit, label: String, modifier: Modifier = Modifier,
    singleLine: Boolean = true, error: String? = null, hint: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val fill = glassFill(GlassLevel.L1)
    OutlinedTextField(
        value, onChange, modifier.fillMaxWidth(), label = { Text(label) }, singleLine = singleLine, isError = error != null,
        supportingText = (error ?: hint)?.let { { Text(it) } }, keyboardOptions = keyboardOptions, visualTransformation = visualTransformation, shape = ControlShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fill, unfocusedContainerColor = fill, errorContainerColor = fill,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

/** FilterChip in glass colours; M3 keeps the 48dp touch target around the 32dp chip. */
@Composable
fun GlassChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector? = null) {
    val s = MaterialTheme.colorScheme
    FilterChip(
        selected, onClick, label = { Text(label) }, modifier = modifier, shape = RoundedCornerShape(50),
        leadingIcon = icon?.let { { Icon(it, null, Modifier.size(FilterChipDefaults.IconSize)) } },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = glassFill(GlassLevel.L1), labelColor = s.onSurface,
            selectedContainerColor = s.primary.copy(alpha = 0.30f), selectedLabelColor = s.onSurface, selectedLeadingIconColor = s.onSurface,
        ),
        border = BorderStroke(1.dp, if (selected) s.primary else s.outline),
    )
}

@Composable
private fun kindColor(k: StateKind): Color {
    val dark = isDarkTheme(); val s = MaterialTheme.colorScheme
    return when (k) {
        StateKind.Ok -> Color(if (dark) Palette.DarkSuccessText else Palette.SuccessLight)
        StateKind.Failed -> Color(if (dark) Palette.DarkError else Palette.DangerLight)
        StateKind.Retry -> Color(if (dark) Palette.DarkWarningText else Palette.WarningLight)
        StateKind.Running -> s.secondary
        StateKind.Neutral -> s.onSurfaceVariant
    }
}

private fun kindIcon(k: StateKind): ImageVector = when (k) {
    StateKind.Ok -> Icons.Default.Check
    StateKind.Failed -> Icons.Default.Warning
    StateKind.Retry -> Icons.Default.Refresh
    StateKind.Running -> Icons.Default.PlayArrow
    StateKind.Neutral -> Icons.Default.Info
}

/** Status pill: icon + text + colour (never colour-only). Read as "State: <label>". */
@Composable
fun StateBadge(state: String, modifier: Modifier = Modifier) {
    val k = StateStyle.kind(state); val col = kindColor(k); val label = StateStyle.label(state)
    Row(
        modifier.semantics(mergeDescendants = true) { contentDescription = "State: $label" }
            .clip(RoundedCornerShape(50)).background(col.copy(alpha = 0.16f)).padding(horizontal = Space.M.dp, vertical = Space.Xs.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Space.Xs.dp),
    ) {
        Icon(kindIcon(k), null, tint = col, modifier = Modifier.size(14.dp))
        Text(label, color = col, style = MaterialTheme.typography.labelMedium)
    }
}

/** Dialogs sit in their own window (nothing to see through), so L2 = opaque surfaceContainerHigh + sheet radius. */
@Composable
fun GlassDialog(
    onDismiss: () -> Unit, title: String, confirmText: String, onConfirm: () -> Unit,
    dismissText: String? = "Cancel", confirmEnabled: Boolean = true, content: @Composable () -> Unit,
) = AlertDialog(
    onDismissRequest = onDismiss, shape = SheetShape, containerColor = MaterialTheme.colorScheme.surfaceContainerHigh, tonalElevation = 0.dp,
    title = { Text(title, Modifier.asHeading()) }, text = content,
    confirmButton = { GlassButton(confirmText, onConfirm, style = GlassButtonStyle.Text, enabled = confirmEnabled) },
    dismissButton = dismissText?.let { { GlassButton(it, onDismiss, style = GlassButtonStyle.Text) } },
)

/** Glass row around the existing [LabeledSwitch] (whole row toggles, Role.Switch, >= 48dp). */
@Composable
fun GlassSwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) =
    Box(modifier.fillMaxWidth().glass(GlassLevel.L1, ControlShape).padding(horizontal = Space.L.dp)) { LabeledSwitch(label, checked, onChange) }

@Composable
fun EmptyState(title: String, body: String, modifier: Modifier = Modifier, action: (@Composable () -> Unit)? = null) =
    GlassCard(modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Space.S.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            action?.invoke()
        }
    }
