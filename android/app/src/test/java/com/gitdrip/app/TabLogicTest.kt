package com.gitdrip.app

import com.gitdrip.app.ui.TabLogic
import org.junit.Assert.*
import org.junit.Test

/** U4: Files / Batches / Schedule tab text and rules (pure JVM). */
class TabLogicTest {
    @Test fun sizes() {
        assertEquals("0 B", TabLogic.formatSize(0)); assertEquals("1023 B", TabLogic.formatSize(1023))
        assertEquals("1.0 KB", TabLogic.formatSize(1024)); assertEquals("1.5 KB", TabLogic.formatSize(1536))
        assertEquals("2.0 MB", TabLogic.formatSize(2L * 1024 * 1024))
        assertEquals("12 B · abcdef0123", TabLogic.fileSubtitle(12, "abcdef0123456789"))
    }

    @Test fun filesAndBatches() {
        assertEquals("1 file · secrets (.env, keys) are never copied", TabLogic.filesSummary(1))
        assertEquals("3 files · secrets (.env, keys) are never copied", TabLogic.filesSummary(3))
        assertEquals("c.kt", TabLogic.fileName("a/b/c.kt")); assertEquals("c.kt", TabLogic.fileName("c.kt"))
        assertEquals("#4", TabLogic.batchTag(4)); assertEquals("Unassigned", TabLogic.batchTag(null))
        assertEquals("#2 feat: x", TabLogic.batchTitle(2, "feat: x")); assertEquals("#2", TabLogic.batchTitle(2, ""))
        assertEquals("1 file", TabLogic.batchFiles(1)); assertEquals("0 files", TabLogic.batchFiles(0))
        assertEquals("Unassigned files: 5", TabLogic.unassignedLabel(5))
    }

    @Test fun runAndMove() {
        for (s in listOf("PENDING", "committed", "FAILED")) assertTrue(s, TabLogic.canRun(s))
        for (s in listOf("SUCCESS", "NOCHANGE", "RUNNING", "PENDING_RETRY")) assertFalse(s, TabLogic.canRun(s))
        assertFalse(TabLogic.canMoveUp(0)); assertTrue(TabLogic.canMoveUp(1))
        assertTrue(TabLogic.canMoveDown(0, 2)); assertFalse(TabLogic.canMoveDown(1, 2)); assertFalse(TabLogic.canMoveDown(0, 0))
    }

    @Test fun days() {
        assertEquals(7, TabLogic.DAY_NAMES.size)
        var d = 0b1111111
        d = TabLogic.toggleDay(d, 6)
        assertFalse(TabLogic.dayOn(d, 6)); assertTrue(TabLogic.dayOn(d, 0))
        assertEquals(0b1111111, TabLogic.toggleDay(d, 6))
    }

    @Test fun scheduleText() {
        assertEquals("09:00 · Every day", TabLogic.scheduleTitle("09:00", "Every day"))
        assertEquals("Device zone · Skip missed slots", TabLogic.scheduleSubtitle("", "skip"))
        assertEquals("Asia/Dhaka · Run when back", TabLogic.scheduleSubtitle("Asia/Dhaka", "run-now"))
        assertEquals("Off", TabLogic.nextLine(false, "x")); assertEquals("No upcoming run", TabLogic.nextLine(true, null))
        assertEquals("Next: 1/2/26", TabLogic.nextLine(true, "1/2/26"))
        assertTrue(TabLogic.scheduleHelp(3).contains("Cap 3/day"))
    }

    @Test fun scheduleErrorOrder() {
        assertEquals("Time must be HH:MM (24h)", TabLogic.scheduleError(false, false, 0))
        assertTrue(TabLogic.scheduleError(true, false, 0)!!.startsWith("Unknown zone"))
        assertEquals("Pick at least one day", TabLogic.scheduleError(true, true, 0))
        assertNull(TabLogic.scheduleError(true, true, 1))
    }
    @Test fun prDialogTexts() {
        assertEquals("Run next as pull request", TabLogic.prTitle(null))
        assertEquals("Run batch #4 as pull request", TabLogic.prTitle(4))
        assertTrue(TabLogic.PR_OPEN.contains("review") && TabLogic.PR_MERGE.contains("merge"))
    }
    @Test fun watchTexts() {
        assertEquals("Off", TabLogic.tickLabel("off")); assertEquals("Off", TabLogic.tickLabel("x"))
        assertEquals("Scan only (queue batches)", TabLogic.tickLabel("scan")); assertEquals("Scan + commit one batch", TabLogic.tickLabel("commit"))
        assertEquals("2 new batches", TabLogic.watchSummary("SUCCESS", "2 new batches")); assertEquals("Scan done", TabLogic.watchSummary("SUCCESS", " "))
        assertTrue(TabLogic.watchSummary("PENDING", "").contains("busy"))
        assertEquals("Watch: no watch dir configured", TabLogic.watchSummary("ERROR", "no watch dir configured")); assertEquals("Watch: no answer", TabLogic.watchSummary("", ""))
    }
}
