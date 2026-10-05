@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.gitdrip.app

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gitdrip.app.data.*
import com.gitdrip.app.ui.*

@Composable
fun ScheduleTab(vm: MainViewModel, p: ProjectEntity) {
    val ctx = LocalContext.current
    val list by vm.schedules(p.id).collectAsStateWithLifecycle(emptyList())
    val msg by vm.schedMsg.collectAsStateWithLifecycle()
    var editing by remember { mutableStateOf<ScheduleEntity?>(null) }
    var adding by remember { mutableStateOf(false) }
    var exact by remember { mutableStateOf(Scheduler.canExact(ctx)) }
    LaunchedEffect(list) { exact = Scheduler.canExact(ctx) }
    val fmt = remember { java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT) }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = Space.Screen.dp, end = Space.Screen.dp, top = Space.S.dp, bottom = Space.Xl.dp),
        verticalArrangement = Arrangement.spacedBy(Space.S.dp),
    ) {
        if (!exact) item {
            NoticeCard(TabLogic.EXACT_ALARM_NOTICE, actionLabel = "Allow exact alarms") {
                ctx.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${ctx.packageName}")))
            }
        }
        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Space.S.dp), verticalArrangement = Arrangement.spacedBy(Space.S.dp)) {
                GlassButton("Add time", { adding = true }, icon = Icons.Default.Add)
                GlassButton("Battery", { ctx.startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)) }, style = GlassButtonStyle.Secondary)
            }
        }
        item { Text(TabLogic.scheduleHelp(DAILY_CAP), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        msg?.let { m ->
            item {
                Row(Modifier.semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Assertive }, horizontalArrangement = Arrangement.spacedBy(Space.S.dp)) {
                    Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    Text(m, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        if (list.isEmpty()) item { EmptyState("No schedules", "Each slot runs the next pending batch.") }
        items(list, key = { it.id }) { s ->
            val at = nextFire(System.currentTimeMillis(), s.time, zoneOf(s.zone), s.days)
            GlassCard(Modifier.fillMaxWidth()) {
                // Only the text area is the edit button, so the Switch / Delete below stay separate TalkBack nodes.
                Column(
                    Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        .clickable(onClickLabel = "Edit schedule ${s.time}", role = Role.Button) { editing = s },
                    verticalArrangement = Arrangement.spacedBy(Space.Xs.dp),
                ) {
                    Text(TabLogic.scheduleTitle(s.time, daysLabel(s.days)), style = MaterialTheme.typography.titleMedium)
                    Text(TabLogic.scheduleSubtitle(s.zone, s.policy), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(TabLogic.nextLine(s.enabled, at?.let { fmt.format(it) }), style = MaterialTheme.typography.bodySmall)
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Switch(s.enabled, { vm.setEnabled(s, it) }, Modifier.semantics { contentDescription = "Schedule ${s.time} enabled" })
                    GlassIconButton(Icons.Default.Delete, "Delete schedule ${s.time}", { vm.deleteSchedule(s) })
                }
            }
        }
    }
    if (adding || editing != null) ScheduleDialog(editing, onDismiss = { adding = false; editing = null }) { time, zone, days, policy ->
        vm.saveSchedule(p, editing, time, zone, days, policy); adding = false; editing = null
    }
}

private val AsciiKeys = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = false, keyboardType = KeyboardType.Ascii)

@Composable
private fun ScheduleDialog(s: ScheduleEntity?, onDismiss: () -> Unit, onSave: (String, String, Int, String) -> Unit) {
    var time by remember(s) { mutableStateOf(s?.time ?: "09:00") }
    var zone by remember(s) { mutableStateOf(s?.zone ?: "") }
    var days by remember(s) { mutableIntStateOf(s?.days ?: ALL_DAYS) }
    var policy by remember(s) { mutableStateOf(s?.policy ?: TabLogic.POLICY_SKIP) }
    val err = TabLogic.scheduleError(validTime(time), validZone(zone.trim()), days)
    GlassDialog(
        onDismiss = onDismiss, title = if (s == null) "New schedule" else "Edit schedule", confirmText = "Save",
        onConfirm = { onSave(time, zone.trim(), days, policy) }, confirmEnabled = err == null,
    ) {
        Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(Space.S.dp)) {
            GlassTextField(time, { time = it.trim() }, "Time HH:MM (24h)", keyboardOptions = AsciiKeys)
            GlassTextField(zone, { zone = it }, "Time zone (empty = device)", keyboardOptions = AsciiKeys)
            Text("Days", style = MaterialTheme.typography.labelMedium, modifier = Modifier.asHeading())
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Space.S.dp), verticalArrangement = Arrangement.spacedBy(Space.S.dp)) {
                TabLogic.DAY_NAMES.forEachIndexed { i, n -> GlassChip(n, TabLogic.dayOn(days, i), { days = TabLogic.toggleDay(days, i) }) }
            }
            Text("If a slot is missed", style = MaterialTheme.typography.labelMedium, modifier = Modifier.asHeading())
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Space.S.dp), verticalArrangement = Arrangement.spacedBy(Space.S.dp)) {
                TabLogic.POLICIES.forEach { o -> GlassChip(TabLogic.policyLabel(o), policy == o, { policy = o }) }
            }
            err?.let {
                Row(Modifier.semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Assertive }, horizontalArrangement = Arrangement.spacedBy(Space.S.dp)) {
                    Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
