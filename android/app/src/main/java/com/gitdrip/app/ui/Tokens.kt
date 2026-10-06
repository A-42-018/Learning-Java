package com.gitdrip.app.ui

/** Design tokens (U0). Pure Kotlin (no Compose imports) so palette/contrast logic stays JVM-testable. Colors are 0xAARRGGBB. */
object Palette {
    // Brand
    const val Indigo = 0xFF6366F1
    const val Cyan = 0xFF22D3EE
    const val Magenta = 0xFFEC4899
    // Status (always paired with an icon + text, never colour-only)
    const val Success = 0xFF34D399
    const val Warning = 0xFFFBBF24
    const val Danger = 0xFFF87171
    const val SuccessLight = 0xFF047857
    const val WarningLight = 0xFF92400E
    const val DangerLight = 0xFFB91C1C
    // Status as TEXT on dark glass (the plain Success/Warning/Danger above are for icons and fills)
    const val DarkSuccessText = 0xFF6EE7B7
    const val DarkWarningText = 0xFFFCD34D
    // Primary gradient button (same in dark + light): dark label on a light indigo->cyan fill
    const val ButtonStart = 0xFF818CF8
    const val ButtonEnd = 0xFF22D3EE
    const val OnButton = 0xFF0B1020

    // Dark
    const val DarkBgTop = 0xFF0B1020
    const val DarkBgBottom = 0xFF121B3A
    const val DarkOnBg = 0xFFE8ECF8
    const val DarkOnVariant = 0xFFC9D0E6
    const val DarkPrimary = 0xFFC7D2FE
    const val DarkOnPrimary = 0xFF0B1020
    const val DarkSecondary = 0xFF67E8F9
    const val DarkTertiary = 0xFFF472B6
    const val DarkSurface = 0xFF0F1630
    const val DarkSurfaceVariant = 0xFF1A2347
    const val DarkOutline = 0xFFA3AED6
    const val DarkOutlineVariant = 0xFF2A3560
    const val DarkContainerLowest = 0xFF0B1020
    const val DarkContainerLow = 0xFF111A38
    const val DarkContainer = 0xFF151F42
    const val DarkContainerHigh = 0xFF1B2650
    const val DarkContainerHighest = 0xFF222E5E
    const val DarkError = 0xFFFECACA

    // Light
    const val LightBgTop = 0xFFEEF2FF
    const val LightBgBottom = 0xFFF8FAFC
    const val LightOnBg = 0xFF0F172A
    const val LightOnVariant = 0xFF475569
    const val LightPrimary = 0xFF4F46E5
    const val LightOnPrimary = 0xFFFFFFFF
    const val LightSecondary = 0xFF0E7490
    const val LightTertiary = 0xFFBE185D
    const val LightSurface = 0xFFF8FAFC
    const val LightSurfaceVariant = 0xFFE2E8F8
    const val LightOutline = 0xFF64748B
    const val LightOutlineVariant = 0xFFCBD5E1
    const val LightContainerLowest = 0xFFFFFFFF
    const val LightContainerLow = 0xFFF4F6FD
    const val LightContainer = 0xFFEEF1FB
    const val LightContainerHigh = 0xFFE8ECF8
    const val LightContainerHighest = 0xFFE2E7F5
    const val LightError = 0xFFB91C1C
}

/** Glass surface alphas (0..1); used from U1. */
object GlassAlpha {
    const val L1 = 0.08f
    const val L2 = 0.14f
    const val L3 = 0.20f
    const val LightL1 = 0.62f
    const val LightL2 = 0.74f
    const val LightL3 = 0.84f
    const val BorderStart = 0.28f
    const val BorderEnd = 0.04f
    const val OrbDark = 0.20f
    const val OrbLight = 0.14f
    const val SolidFloor = 0.85f   // "solid surfaces" / API < 31 fallback for blurred layers
}

/** Radii in dp. */
object Radius {
    const val Control = 14
    const val Card = 20
    const val Sheet = 28
}

/** Minimum touch target in dp (WCAG / Material). Every interactive glass component uses it. */
object Touch { const val Min = 48 }

/** Spacing in dp (4dp grid). */
object Space {
    const val Xs = 4
    const val S = 8
    const val M = 12
    const val L = 16
    const val Xl = 24
    const val Screen = 16
}

/** WCAG relative luminance / contrast ratio for 0xAARRGGBB colors (alpha ignored). */
object Contrast {
    /** Source-over composite of [fg] at [alpha] onto opaque [bg] (0xAARRGGBB, result opaque). */
    fun over(fg: Long, alpha: Float, bg: Long): Long {
        fun mix(sh: Int) = Math.round(((fg shr sh) and 0xFFL) * alpha + ((bg shr sh) and 0xFFL) * (1 - alpha)).toLong()
        return 0xFF000000L or (mix(16) shl 16) or (mix(8) shl 8) or mix(0)
    }
    private fun chan(v: Int): Double { val c = v / 255.0; return if (c <= 0.03928) c / 12.92 else Math.pow((c + 0.055) / 1.055, 2.4) }
    fun luminance(argb: Long): Double {
        val r = ((argb shr 16) and 0xFFL).toInt(); val g = ((argb shr 8) and 0xFFL).toInt(); val b = (argb and 0xFFL).toInt()
        return 0.2126 * chan(r) + 0.7152 * chan(g) + 0.0722 * chan(b)
    }
    fun ratio(a: Long, b: Long): Double {
        val la = luminance(a); val lb = luminance(b)
        return (maxOf(la, lb) + 0.05) / (minOf(la, lb) + 0.05)
    }
}
