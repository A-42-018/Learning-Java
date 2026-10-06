package com.gitdrip.app.ui

/** Pure text/rule helpers for the Files / Batches / Schedule tabs (U4). No Compose/Android imports so they run in plain JVM tests. */
object TabLogic {
    fun formatSize(bytes: Long): String = when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024L * 1024 -> "%.1f KB".format(java.util.Locale.US, bytes / 1024.0)
        else -> "%.1f MB".format(java.util.Locale.US, bytes / 1048576.0)
    }

    fun fileSubtitle(size: Long, sha: String): String = "${formatSize(size)} · ${sha.take(10)}"

    fun filesSummary(n: Int): String = (if (n == 1) "1 file" else "$n files") + " · secrets (.env, keys) are never copied"

    fun fileName(path: String): String = path.substringAfterLast('/')

    /** Batch tag on a file row: "#3" or "Unassigned". */
    fun batchTag(seq: Int?): String = if (seq == null) "Unassigned" else "#$seq"

    fun unassignedLabel(n: Int): String = "Unassigned files: $n"

    fun batchTitle(seq: Int, message: String, origin: String = ""): String = ("#$seq $message" + if (origin == "watch") " (from watcher)" else "").trim()

    fun batchFiles(n: Int): String = if (n == 1) "1 file" else "$n files"

    /** Same states the engine can retry/run: not yet pushed. */
    fun canRun(status: String): Boolean = status.uppercase().let { it == "PENDING" || it == "COMMITTED" || it == "FAILED" }

    /** P7.6 "Run as pull request" dialog texts (stored behaviour: engine `pr-flow` with `--issue`, optional `--merge`). */
    fun prTitle(seq: Int?): String = if (seq == null) "Run next as pull request" else "Run batch #$seq as pull request"
    const val PR_OPEN = "Open pull request (review later)"
    const val PR_MERGE = "Open pull request and squash-merge"

    /** P7.9 watch card (work-dir watcher; the dir itself is set in Termux: `gitdrip watch <p> --dir D`). */
    const val WATCH_TITLE = "Watch work folder"
    const val WATCH_HELP = "Scans the folder you set in Termux (gitdrip watch <project> --dir <folder>) for settled edits and queues them as new batches."
    const val WATCH_SCAN = "Scan now"
    const val WATCH_COMMIT = "Scan + commit 1"
    const val WATCH_TICK = "Auto mode"
    fun tickTitle(): String = "Run the watch scan every 5 minutes (Termux cron)"
    fun tickLabel(arg: String): String = when (arg) {
        "scan" -> "Scan only (queue batches)"
        "commit" -> "Scan + commit one batch"
        else -> "Off"
    }
    fun watchSummary(state: String, reason: String): String = when (state) {
        "SUCCESS" -> reason.ifBlank { "Scan done" }
        "PENDING" -> "Project is busy, try again in a minute"
        else -> "Watch: " + reason.ifBlank { state.ifBlank { "no answer" } }
    }

    fun canMoveUp(index: Int): Boolean = index > 0
    fun canMoveDown(index: Int, size: Int): Boolean = index in 0 until size - 1

    val DAY_NAMES: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    fun dayOn(days: Int, i: Int): Boolean = (days shr i) and 1 == 1
    fun toggleDay(days: Int, i: Int): Int = days xor (1 shl i)

    const val POLICY_SKIP = "skip"
    const val POLICY_RUN_NOW = "run-now"
    val POLICIES: List<String> = listOf(POLICY_SKIP, POLICY_RUN_NOW)
    fun policyLabel(p: String): String = when (p) {
        POLICY_SKIP -> "Skip missed slots"
        POLICY_RUN_NOW -> "Run when back"
        else -> p
    }

    fun scheduleTitle(time: String, daysLabel: String): String = "$time · $daysLabel"

    fun scheduleSubtitle(zone: String, policy: String): String = "${zone.ifBlank { "Device zone" }} · ${policyLabel(policy)}"

    fun nextLine(enabled: Boolean, nextText: String?): String = when {
        !enabled -> "Off"
        nextText == null -> "No upcoming run"
        else -> "Next: $nextText"
    }

    fun scheduleError(timeValid: Boolean, zoneValid: Boolean, days: Int): String? = when {
        !timeValid -> "Time must be HH:MM (24h)"
        !zoneValid -> "Unknown zone (e.g. Asia/Dhaka, UTC). Empty = device zone"
        days == 0 -> "Pick at least one day"
        else -> null
    }

    const val EXACT_ALARM_NOTICE = "Exact alarms are off: runs may be late. Allow \"Alarms & reminders\"."
    const val SHARED_ACCESS_NOTICE = "Termux can't read app-private storage. Grant \"All files access\" so files go to /storage/emulated/0/GitDrip."
    fun scheduleHelp(cap: Int) = "Disable battery optimization for GitDrip and Termux. Missed slots: skip, or run on boot/time change. Cap $cap/day."
}
