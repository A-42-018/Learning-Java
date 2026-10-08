package com.gitdrip.app

import com.gitdrip.app.ui.*
import org.junit.Assert.*
import org.junit.Test

/** U1: worst-case WCAG contrast of text tokens on glass over the gradient + each orb (stationary levels L1/L2), plus touch/state rules. */
class ContrastTest {
    private val orbs = listOf(Palette.Indigo, Palette.Cyan, Palette.Magenta)
    private val white = 0xFFFFFFFFL
    private fun darkBgs() = listOf(Palette.DarkBgTop, Palette.DarkBgBottom).flatMap { bg ->
        orbs.flatMap { o -> listOf(GlassAlpha.L1, GlassAlpha.L2).map { a -> Contrast.over(white, a, Contrast.over(o, GlassAlpha.OrbDark, bg)) } }
    }
    private fun lightBgs() = listOf(Palette.LightBgTop, Palette.LightBgBottom).flatMap { bg ->
        orbs.flatMap { o -> listOf(GlassAlpha.LightL1, GlassAlpha.LightL2).map { a -> Contrast.over(white, a, Contrast.over(o, GlassAlpha.OrbLight, bg)) } }
    }
    private fun worst(fg: Long, bgs: List<Long>) = bgs.minOf { Contrast.ratio(fg, it) }
    private fun atLeast(name: String, min: Double, fg: Long, bgs: List<Long>) {
        val w = worst(fg, bgs); assertTrue("$name worst=${"%.2f".format(w)} < $min", w >= min)
    }

    @Test fun darkTextTokens() {
        val d = darkBgs()
        atLeast("onBg", 4.5, Palette.DarkOnBg, d); atLeast("onVariant", 4.5, Palette.DarkOnVariant, d)
        atLeast("primary", 4.5, Palette.DarkPrimary, d); atLeast("secondary", 4.5, Palette.DarkSecondary, d)
        atLeast("success", 4.5, Palette.DarkSuccessText, d); atLeast("warning", 4.5, Palette.DarkWarningText, d)
        atLeast("error", 4.5, Palette.DarkError, d)
    }
    @Test fun darkOutlineIsNonTextContrast() = atLeast("outline", 3.0, Palette.DarkOutline, darkBgs())

    @Test fun lightTextTokens() {
        val l = lightBgs()
        atLeast("onBg", 4.5, Palette.LightOnBg, l); atLeast("onVariant", 4.5, Palette.LightOnVariant, l)
        atLeast("primary", 4.5, Palette.LightPrimary, l); atLeast("secondary", 4.5, Palette.LightSecondary, l)
        atLeast("success", 4.5, Palette.SuccessLight, l); atLeast("warning", 4.5, Palette.WarningLight, l)
        atLeast("danger", 4.5, Palette.DangerLight, l)
    }
    @Test fun lightOutlineIsNonTextContrast() = atLeast("outline", 3.0, Palette.LightOutline, lightBgs())

    @Test fun primaryButtonLabelOnBothGradientEnds() {
        assertTrue(Contrast.ratio(Palette.OnButton, Palette.ButtonStart) >= 4.5)
        assertTrue(Contrast.ratio(Palette.OnButton, Palette.ButtonEnd) >= 4.5)
    }
    @Test fun overCompositing() {
        assertEquals(0xFF808080L, Contrast.over(0xFFFFFFFFL, 0.5f, 0xFF000000L))
        assertEquals(0xFF0B1020L, Contrast.over(0xFFFFFFFFL, 0f, 0xFF0B1020L))
    }
    @Test fun touchTargetIs48() = assertEquals(48, Touch.Min)

    @Test fun stateKinds() {
        assertEquals(StateKind.Ok, StateStyle.kind("SUCCESS")); assertEquals(StateKind.Failed, StateStyle.kind("FAILED"))
        assertEquals(StateKind.Failed, StateStyle.kind("ERROR")); assertEquals(StateKind.Retry, StateStyle.kind("PENDING_RETRY"))
        assertEquals(StateKind.Running, StateStyle.kind("running")); assertEquals(StateKind.Neutral, StateStyle.kind("COMMITTED"))
        assertEquals(StateKind.Neutral, StateStyle.kind("whatever"))
        assertEquals("Pending retry", StateStyle.label("PENDING_RETRY"))
    }
}
