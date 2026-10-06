package com.gitdrip.app.ui

/** Pure mapping of engine/Room state strings to a visual kind (U1). Colour is never the only cue: each kind has an icon + the state text. */
enum class StateKind { Ok, Failed, Retry, Running, Neutral }

object StateStyle {
    fun kind(state: String): StateKind = when (state.uppercase()) {
        "SUCCESS" -> StateKind.Ok
        "FAILED", "ERROR" -> StateKind.Failed
        "PENDING_RETRY" -> StateKind.Retry
        "RUNNING" -> StateKind.Running
        else -> StateKind.Neutral   // PENDING, COMMITTED, SKIPPED, NOCHANGE, SCHEDULED, unknown
    }
    fun label(state: String): String = state.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }
}
