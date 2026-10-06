package com.gitdrip.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/** Glass depth: L1 card, L2 raised / dialog, L3 pressed / selected. */
enum class GlassLevel { L1, L2, L3 }

/** "Solid surfaces" switch (Settings > Appearance, persisted by [Appearance]). When true every glass surface is opaque. */
val LocalSolidSurfaces = compositionLocalOf { false }

@Composable
fun isDarkTheme(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

val CardShape = RoundedCornerShape(Radius.Card.dp)
val ControlShape = RoundedCornerShape(Radius.Control.dp)
val SheetShape = RoundedCornerShape(Radius.Sheet.dp)

private fun c(v: Long) = Color(v)

/** Aurora backdrop: vertical gradient + 3 static orbs. Drawn once at the root, never per card (cheap on every API level). */
@Composable
fun GlassBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val dark = isDarkTheme()
    val top = c(if (dark) Palette.DarkBgTop else Palette.LightBgTop)
    val bottom = c(if (dark) Palette.DarkBgBottom else Palette.LightBgBottom)
    val a = if (dark) GlassAlpha.OrbDark else GlassAlpha.OrbLight
    Box(
        modifier.fillMaxSize().drawBehind {
            drawRect(Brush.verticalGradient(listOf(top, bottom)))
            fun orb(col: Long, cx: Float, cy: Float, r: Float) = drawCircle(
                Brush.radialGradient(listOf(c(col).copy(alpha = a), Color.Transparent), Offset(cx * size.width, cy * size.height), r * size.width),
                radius = r * size.width, center = Offset(cx * size.width, cy * size.height),
            )
            orb(Palette.Indigo, 0.15f, 0.10f, 0.75f)
            orb(Palette.Cyan, 0.95f, 0.45f, 0.65f)
            orb(Palette.Magenta, 0.20f, 0.90f, 0.70f)
        },
        content = content,
    )
}

/** Fill colour of one glass level for the current theme; [solid] swaps in an opaque container colour. */
@Composable
fun glassFill(level: GlassLevel): Color {
    if (LocalSolidSurfaces.current) {
        val s = MaterialTheme.colorScheme
        return when (level) { GlassLevel.L1 -> s.surfaceContainerLow; GlassLevel.L2 -> s.surfaceContainerHigh; GlassLevel.L3 -> s.surfaceContainerHighest }
    }
    return if (isDarkTheme()) Color.White.copy(alpha = when (level) { GlassLevel.L1 -> GlassAlpha.L1; GlassLevel.L2 -> GlassAlpha.L2; GlassLevel.L3 -> GlassAlpha.L3 })
    else Color.White.copy(alpha = when (level) { GlassLevel.L1 -> GlassAlpha.LightL1; GlassLevel.L2 -> GlassAlpha.LightL2; GlassLevel.L3 -> GlassAlpha.LightL3 })
}

/** Translucent fill + 1dp gradient border + top highlight. No shadows, no RenderEffect. */
@Composable
fun Modifier.glass(level: GlassLevel = GlassLevel.L1, shape: Shape = CardShape): Modifier {
    val dark = isDarkTheme()
    val fill = glassFill(level)
    val edge = if (dark) Color.White else Color(0xFF475569)
    val border = Brush.verticalGradient(listOf(edge.copy(alpha = if (dark) GlassAlpha.BorderStart else 0.30f), edge.copy(alpha = if (dark) GlassAlpha.BorderEnd else 0.08f)))
    val highlight = Color.White.copy(alpha = if (dark) 0.22f else 0.90f)
    return this.clip(shape).background(fill, shape).border(1.dp, border, shape).drawBehind {
        val inset = 14.dp.toPx()
        if (size.width > inset * 2) drawLine(
            Brush.horizontalGradient(listOf(Color.Transparent, highlight, Color.Transparent), startX = inset, endX = size.width - inset),
            Offset(inset, 1.dp.toPx()), Offset(size.width - inset, 1.dp.toPx()), strokeWidth = 1.dp.toPx(),
        )
    }
}

/** Convenience for previews: force dark or light below this point. */
@Composable
fun ThemeOverride(dark: Boolean, solid: Boolean = false, content: @Composable () -> Unit) =
    GitDripTheme(darkTheme = dark) { CompositionLocalProvider(LocalSolidSurfaces provides solid) { content() } }
