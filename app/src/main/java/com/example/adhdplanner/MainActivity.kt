package com.example.adhdplanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle as DateTimeTextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import java.util.UUID
import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import android.provider.CalendarContract
import org.json.JSONArray
import org.json.JSONObject
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector

// App color palette. Brand names are preserved so the ~400 existing references keep working;
// the hex values are repointed to the refreshed palette. New role colors (completion teal,
// overdue red, neutral text/border tiers) are added below for task-state styling.
val Navy = Color(0xFF1C2F45)            // Logo / nav structure (unselected nav icons)
val RidgelineBlue = Color(0xFF3366FF)   // Buttons / links / interactive (screen-tuned blue)
val SkyMid = Color(0xFF185FA5)          // Button hover / text on ice
val SkyDark = Color(0xFF0C447C)         // Text on ice (darker)
val MidnightSlate = Color(0xFF000033)   // Primary text (deep navy) + elevation visuals
val MistBlue = Color(0xFFA3B8C9)        // Kept: muted blue-gray (external events, outline)
val SkyGray = Color(0xFFEFF2F1)         // App background (Platinum)
val CardInset = Color(0xFFF7F9F8)       // Between Platinum and white — gentle inset fills (text boxes, subcards)
val PureWhite = Color(0xFFFFFFFF)       // Card surfaces
val PureBlack = Color(0xFF000000)
val SuccessGreen = Color(0xFF00A878)    // Completed tasks / streaks (Jungle Green)
val IceBlueAccent = Color(0xFFE6F1FB)   // Selected states / highlights (ice)
val SoftYellow = Color(0xFFFDE68A)      // Kept: priority placement blocks

// Completion (green) + overdue (red) role colors — for task-state chips/text.
val TealLight = Color(0xFFE1F5EE)       // Completion chip background
val TealDark = Color(0xFF085041)        // Text on completion chip
val OverdueRed = Color(0xFFEF233C)      // Overdue tasks (cooler, more vivid red)
val OverdueRedLight = Color(0xFFFCEBEB) // Overdue chip background
val OverdueRedDark = Color(0xFFA32D2D)  // Text on red light

// Neutral text + border tiers.
val BorderGray = Color(0xFFD3D1C7)      // Dividers / card borders
val TextMuted = Color(0xFF5F5E5A)       // Secondary / label text
val TextHint = Color(0xFF888780)        // Placeholders / tertiary
val NeutralFill = Color(0xFFF1EFE8)     // Neutral chip / inset row fill (replaces pale-blue fills)

// Custom Metric Colors (kept distinct for the elevation/scoreboard climbing theme).
val MetricGold = Color(0xFFD4AF37)
val MetricGreen = Color(0xFF10B981)
val MetricOrange = Color(0xFFF97316)
val MetricBlue = Color(0xFF2563EB)

// Standard Event Palette
// Event block colors. A curated, harmonized set tuned to the brand vibe — soft, slightly
// desaturated mid-tones at similar lightness so they read as a designed family rather than
// generic pastels. They echo the brand (periwinkle ↔ electric blue, jade ↔ jungle green) while
// adding warm tones (sand, clay, rose) that pick up the warm neutral undertones in the palette.
// Users can also mix their own via the custom color picker.
val EventColors = listOf(
    PureWhite,            // None / default (no fill)
    Color(0xFF9DB4F2),    // Periwinkle  — brand-blue family
    Color(0xFF8ECFB6),    // Jade        — jungle-green family
    Color(0xFF8FC7D2),    // Soft teal
    Color(0xFFE8CF92),    // Warm sand
    Color(0xFFE8AC93),    // Soft clay
    Color(0xFFDBA6BF),    // Dusty rose
    Color(0xFFB8ABDD),    // Heather
    Color(0xFFB5C2CF)     // Cool stone
)

// ===== Backup / restore =====
// Exports the ENTIRE app SharedPreferences store to a JSON string. Dumping the whole prefs map
// (rather than enumerating individual keys) means every piece of saved data is captured
// automatically — all current keys and any added later — so a backup can never silently miss
// something. Each value records its type ("t") so it round-trips back to the right type on restore.
fun exportPrefsToJson(prefs: SharedPreferences): String {
    val data = JSONObject()
    for ((key, value) in prefs.all) {
        val entry = JSONObject()
        when (value) {
            is Boolean -> { entry.put("t", "b"); entry.put("v", value) }
            is Int -> { entry.put("t", "i"); entry.put("v", value) }
            is Long -> { entry.put("t", "l"); entry.put("v", value) }
            is Float -> { entry.put("t", "f"); entry.put("v", value.toDouble()) }
            is String -> { entry.put("t", "s"); entry.put("v", value) }
            is Set<*> -> { entry.put("t", "ss"); entry.put("v", JSONArray().apply { value.forEach { put(it.toString()) } }) }
            else -> { /* unknown type — skip */ }
        }
        if (entry.has("t")) data.put(key, entry)
    }
    return JSONObject().apply {
        put("app", "Ascent")
        put("schemaVersion", 1)
        put("exportedAt", System.currentTimeMillis())
        put("data", data)
    }.toString(2)
}

// Restores prefs from a backup JSON. Returns true on success. This REPLACES existing data
// (clear + write) so the restore is a faithful snapshot, not a merge. The caller should recreate
// the Activity afterward so the in-memory Compose state re-reads the restored values.
fun importPrefsFromJson(prefs: SharedPreferences, json: String): Boolean {
    return try {
        val root = JSONObject(json)
        val data = root.getJSONObject("data")
        val editor = prefs.edit()
        editor.clear()
        val keys = data.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val entry = data.getJSONObject(key)
            when (entry.getString("t")) {
                "b" -> editor.putBoolean(key, entry.getBoolean("v"))
                "i" -> editor.putInt(key, entry.getInt("v"))
                "l" -> editor.putLong(key, entry.getLong("v"))
                "f" -> editor.putFloat(key, entry.getDouble("v").toFloat())
                "s" -> editor.putString(key, entry.getString("v"))
                "ss" -> {
                    val arr = entry.getJSONArray("v")
                    val set = LinkedHashSet<String>()
                    for (i in 0 until arr.length()) set.add(arr.getString(i))
                    editor.putStringSet(key, set)
                }
            }
        }
        editor.apply()
        true
    } catch (e: Exception) {
        false
    }
}

// A self-contained custom color picker. Uses HSV: a decorative rainbow reference bar over a Hue
// slider, plus Saturation and Brightness sliders, with a live preview swatch and hex readout.
// Kept slider-based (no canvas drag math) for reliability. The chosen color is returned as a
// Compose Color, which the event stores as an ARGB int like the preset colors.
@Composable
fun CustomColorPickerDialog(
    initial: Color,
    onConfirm: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val startHsv = remember {
        FloatArray(3).also { android.graphics.Color.colorToHSV(initial.toArgb(), it) }
    }
    var hue by remember { mutableStateOf(startHsv[0]) }
    var sat by remember { mutableStateOf(startHsv[1].coerceAtLeast(0.05f)) }
    var bright by remember { mutableStateOf(startHsv[2].coerceAtLeast(0.15f)) }
    val preview = Color.hsv(hue, sat, bright)
    val rainbow = remember { (0..12).map { Color.hsv(it * 30f, 1f, 1f) } }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        title = { Text("Custom color", style = MaterialTheme.typography.titleMedium, color = MidnightSlate) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Live preview + hex.
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(preview)
                            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                    )
                    Text(
                        text = "#%06X".format(0xFFFFFF and preview.toArgb()),
                        style = MaterialTheme.typography.titleSmall,
                        color = MidnightSlate
                    )
                }
                // Decorative rainbow reference bar (non-interactive) over the hue slider.
                Text("Hue", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50))
                ) {
                    drawRect(brush = Brush.horizontalGradient(rainbow))
                }
                Slider(
                    value = hue, onValueChange = { hue = it }, valueRange = 0f..360f,
                    colors = SliderDefaults.colors(thumbColor = RidgelineBlue, activeTrackColor = RidgelineBlue, inactiveTrackColor = BorderGray)
                )
                Text("Saturation", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Slider(
                    value = sat, onValueChange = { sat = it }, valueRange = 0.05f..1f,
                    colors = SliderDefaults.colors(thumbColor = RidgelineBlue, activeTrackColor = RidgelineBlue, inactiveTrackColor = BorderGray)
                )
                Text("Brightness", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Slider(
                    value = bright, onValueChange = { bright = it }, valueRange = 0.15f..1f,
                    colors = SliderDefaults.colors(thumbColor = RidgelineBlue, activeTrackColor = RidgelineBlue, inactiveTrackColor = BorderGray)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(preview) },
                colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                shape = RoundedCornerShape(50)
            ) { Text("Use this color", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        }
    )
}

// ===== Task composer =====
// A single "new/edit item" window shared by priorities and Other To-Dos, so both cards behave the
// same way and the Lists page matches the Schedule page's tap-to-open-a-window feel.
//
// The one place the two types genuinely differ is "when":
//   - a to-do's when is a DEADLINE (dueDate + optional dueTime)
//   - a priority's when is a SCHEDULE BLOCK (its time on the timeline)
// So the composer shows a due-date field for to-dos and a time/placement field for priorities.
// "Place on schedule" is not a third concept — it's the same "when", answered by tapping a slot
// instead of typing.

data class TaskDraft(
    val editingId: String? = null,          // null = creating something new
    val isPriority: Boolean = false,
    val ownerDate: LocalDate = LocalDate.now(),  // the day the item belongs to
    val title: String = "",
    val notes: String = "",
    val subtasks: List<TodoSubtask> = emptyList(),
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val color: Color = PureWhite,
    val reward: String = "",
    val triggerCue: String = "",
    val reminderNightBefore: Boolean = false,
    val reminderMorningOf: Boolean = false,
    val reminderAtDueTime: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskComposerDialog(
    draft: TaskDraft,
    onDismiss: () -> Unit,
    onPlaceOnSchedule: (TaskDraft) -> Unit,
    onSave: (TaskDraft) -> Unit
) {
    // Local field state, re-seeded whenever a different draft comes in (e.g. after returning from
    // schedule placement with a time filled in).
    var title by remember(draft) { mutableStateOf(draft.title) }
    var notes by remember(draft) { mutableStateOf(draft.notes) }
    val subtasks = remember(draft) { mutableStateListOf<TodoSubtask>().apply { addAll(draft.subtasks) } }
    var dueDate by remember(draft) { mutableStateOf(draft.dueDate) }
    var dueTimeEnabled by remember(draft) { mutableStateOf(draft.dueTime != null) }
    var hourText by remember(draft) {
        mutableStateOf(
            (draft.dueTime?.hour ?: 9).let { if (it == 0 || it == 12) 12 else if (it > 12) it - 12 else it }.toString()
        )
    }
    var minuteText by remember(draft) { mutableStateOf(String.format(Locale.US, "%02d", draft.dueTime?.minute ?: 0)) }
    var isPm by remember(draft) { mutableStateOf((draft.dueTime?.hour ?: 9) >= 12) }
    var color by remember(draft) { mutableStateOf(draft.color) }
    var reward by remember(draft) { mutableStateOf(draft.reward) }
    var triggerCue by remember(draft) { mutableStateOf(draft.triggerCue) }
    var remindNightBefore by remember(draft) { mutableStateOf(draft.reminderNightBefore) }
    var remindMorningOf by remember(draft) { mutableStateOf(draft.reminderMorningOf) }
    var remindAtDue by remember(draft) { mutableStateOf(draft.reminderAtDueTime) }
    var showColorPicker by remember(draft) { mutableStateOf(false) }
    var showDatePicker by remember(draft) { mutableStateOf(false) }
    var expandedSubtaskId by remember(draft) { mutableStateOf<String?>(null) }
    var subtaskInput by remember(draft) { mutableStateOf("") }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = draft.dueDate?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
    )

    // Assemble the current field values into a draft (used for both Save and the placement trip).
    fun currentDraft(): TaskDraft {
        val t: LocalTime? = if (dueTimeEnabled) {
            val h12 = hourText.toIntOrNull()?.coerceIn(1, 12) ?: 9
            val m = minuteText.toIntOrNull()?.coerceIn(0, 59) ?: 0
            var h24 = if (h12 == 12) 0 else h12
            if (isPm) h24 += 12
            LocalTime.of(h24, m)
        } else null
        return draft.copy(
            title = title.trim(),
            notes = notes.trim(),
            subtasks = subtasks.toList(),
            dueDate = dueDate,
            dueTime = t,
            color = color,
            reward = reward.trim(),
            triggerCue = triggerCue.trim(),
            reminderNightBefore = remindNightBefore,
            reminderMorningOf = remindMorningOf,
            reminderAtDueTime = remindAtDue && t != null
        )
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier.fillMaxWidth(0.95f).heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.9f).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when {
                            draft.editingId != null && draft.isPriority -> "Edit priority"
                            draft.editingId != null -> "Edit to-do"
                            draft.isPriority -> "New priority"
                            else -> "New to-do"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        color = MidnightSlate
                    )
                    Icon(
                        imageVector = Icons.Outlined.Close, contentDescription = "Close",
                        tint = MidnightSlate.copy(alpha = 0.6f),
                        modifier = Modifier.size(22.dp).clickable { onDismiss() }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = title, onValueChange = { title = it },
                        label = { Text("Title") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = notes, onValueChange = { notes = it },
                        label = { Text("Details (optional)") },
                        modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    )

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                    // --- Subtasks (each can carry its own details) ---
                    Text("Subtasks (optional)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                    subtasks.forEachIndexed { idx, st ->
                        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(CardInset).padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = st.text,
                                    onValueChange = { subtasks[idx] = st.copy(text = it) },
                                    placeholder = { Text("Subtask", fontSize = 13.sp) },
                                    singleLine = true,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                    shape = RoundedCornerShape(50)
                                )
                                Icon(
                                    imageVector = if (expandedSubtaskId == st.id) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = "Subtask details",
                                    tint = MidnightSlate.copy(alpha = 0.55f),
                                    modifier = Modifier.size(20.dp).clickable {
                                        expandedSubtaskId = if (expandedSubtaskId == st.id) null else st.id
                                    }
                                )
                                Icon(
                                    imageVector = Icons.Outlined.Close, contentDescription = "Remove subtask",
                                    tint = Color.Gray.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp).clickable { subtasks.removeAt(idx) }
                                )
                            }
                            if (expandedSubtaskId == st.id) {
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = st.details,
                                    onValueChange = { subtasks[idx] = st.copy(details = it) },
                                    placeholder = { Text("Details for this subtask", fontSize = 12.sp) },
                                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                                    modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 3,
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = subtaskInput, onValueChange = { subtaskInput = it },
                            placeholder = { Text("Add a subtask…", fontSize = 13.sp) }, singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(50)
                        )
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(RidgelineBlue).clickable {
                                val t = subtaskInput.trim()
                                if (t.isNotBlank()) { subtasks.add(TodoSubtask(text = t)); subtaskInput = "" }
                            },
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Outlined.Add, contentDescription = "Add subtask", tint = PureWhite, modifier = Modifier.size(18.dp)) }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                    // --- When ---
                    // For a to-do this is a deadline; for a priority it's the block on the timeline.
                    Text(if (draft.isPriority) "When (optional)" else "Due date (optional)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                    if (!draft.isPriority) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(50)).background(IceBlueAccent).clickable { showDatePicker = true }.padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = dueDate?.let { "${it.monthValue}/${it.dayOfMonth}/${it.year}" } ?: "Set a date",
                                    style = MaterialTheme.typography.labelLarge, color = SkyDark
                                )
                            }
                            if (dueDate != null) {
                                Text("Clear", style = MaterialTheme.typography.labelMedium, color = TextMuted,
                                    modifier = Modifier.clickable { dueDate = null; dueTimeEnabled = false }.padding(4.dp))
                            }
                        }
                    }
                    // Time is offered once there's a day to hang it on (priorities always have one).
                    if (draft.isPriority || dueDate != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { dueTimeEnabled = !dueTimeEnabled },
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Checkbox(checked = dueTimeEnabled, onCheckedChange = { dueTimeEnabled = it }, colors = CheckboxDefaults.colors(checkedColor = RidgelineBlue))
                            Text(if (draft.isPriority) "Give it a time" else "Add a time", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                        }
                        if (dueTimeEnabled) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = hourText,
                                    onValueChange = { r -> val d = r.filter { it.isDigit() }; if (d.length <= 2) hourText = d },
                                    label = { Text("Hr") }, singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(80.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Text(":", style = MaterialTheme.typography.titleMedium, color = MidnightSlate)
                                OutlinedTextField(
                                    value = minuteText,
                                    onValueChange = { r -> val d = r.filter { it.isDigit() }; if (d.length <= 2) minuteText = d },
                                    label = { Text("Min") }, singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(80.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                Row(
                                    modifier = Modifier.clip(RoundedCornerShape(50)).background(IceBlueAccent).clickable { isPm = !isPm }.padding(horizontal = 14.dp, vertical = 12.dp)
                                ) { Text(if (isPm) "PM" else "AM", style = MaterialTheme.typography.labelLarge, color = SkyDark, fontWeight = FontWeight.Bold) }
                            }
                        }
                    }
                    // Same "when", answered by tapping the day instead of typing it.
                    OutlinedButton(
                        onClick = { onPlaceOnSchedule(currentDraft()) },
                        enabled = title.isNotBlank(),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, RidgelineBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.CalendarToday, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(15.dp))
                            Text("Pick a time on the schedule", color = RidgelineBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                    // --- Reminders ---
                    // Layered cues beat a single alarm for prospective memory — different
                    // attentional states need different triggers.
                    Text("Reminders (optional)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { remindNightBefore = !remindNightBefore },
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Checkbox(checked = remindNightBefore, onCheckedChange = { remindNightBefore = it }, colors = CheckboxDefaults.colors(checkedColor = RidgelineBlue))
                        Text("Night before (8 PM)", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { remindMorningOf = !remindMorningOf },
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Checkbox(checked = remindMorningOf, onCheckedChange = { remindMorningOf = it }, colors = CheckboxDefaults.colors(checkedColor = RidgelineBlue))
                        Text("Morning of (9 AM)", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                    }
                    if (dueTimeEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { remindAtDue = !remindAtDue },
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Checkbox(checked = remindAtDue, onCheckedChange = { remindAtDue = it }, colors = CheckboxDefaults.colors(checkedColor = RidgelineBlue))
                            Text("At the time above", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                    // --- Reward + starter cue ---
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Reward (optional)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        WhyChip(WHY_REWARD_TITLE, WHY_REWARD_BODY)
                    }
                    OutlinedTextField(
                        value = reward, onValueChange = { reward = it },
                        placeholder = { Text("e.g. 5 mins of phone time", fontSize = 13.sp) }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(50)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Starter cue (optional)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        WhyChip(WHY_TRIGGER_CUE_TITLE, WHY_TRIGGER_CUE_BODY)
                    }
                    OutlinedTextField(
                        value = triggerCue, onValueChange = { triggerCue = it },
                        label = { Text("When I…") },
                        placeholder = { Text("e.g. finish my morning coffee", fontSize = 13.sp) }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(50)
                    )

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                    // --- Color ---
                    Text("Color", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EventColors.forEach { c ->
                            Box(
                                modifier = Modifier.size(34.dp).clip(CircleShape).background(c)
                                    .border(
                                        width = if (color == c) 3.dp else 1.dp,
                                        color = if (color == c) MidnightSlate else Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .clickable { color = c }
                            )
                        }
                        if (color !in EventColors) {
                            Box(
                                modifier = Modifier.size(34.dp).clip(CircleShape).background(color)
                                    .border(3.dp, MidnightSlate, CircleShape)
                                    .clickable { showColorPicker = true }
                            )
                        }
                        Box(
                            modifier = Modifier.size(34.dp).clip(CircleShape).background(NeutralFill)
                                .border(1.dp, BorderGray, CircleShape)
                                .clickable { showColorPicker = true },
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Outlined.Add, contentDescription = "Custom color", tint = MidnightSlate, modifier = Modifier.size(18.dp)) }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onSave(currentDraft()) },
                    enabled = title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Save", fontWeight = FontWeight.Bold) }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { ms ->
                        dueDate = Instant.ofEpochMilli(ms).atZone(ZoneId.of("UTC")).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK", fontWeight = FontWeight.Bold, color = RidgelineBlue) }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = MidnightSlate) } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showColorPicker) {
        CustomColorPickerDialog(
            initial = if (color == PureWhite) RidgelineBlue else color,
            onConfirm = { picked -> color = picked; showColorPicker = false },
            onDismiss = { showColorPicker = false }
        )
    }
}

/**
 * The "tap an item to inspect it" window, shared by priorities and to-dos. Mirrors the Schedule
 * page's event-inspect dialog so the whole app reads the same way: tap a thing → a window about
 * that thing → Edit / Delete. Subtasks stay tickable here so it doubles as a working view.
 */
@Composable
fun TaskDetailDialog(
    title: String,
    notes: String,
    subtasks: List<TodoSubtask>,
    whenLabel: String?,
    color: Color,
    reward: String,
    triggerCue: String,
    isCompleted: Boolean,
    isPriority: Boolean,
    onToggleSubtask: (String) -> Unit,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val maxH = (LocalConfiguration.current.screenHeightDp * 0.85f).dp
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier.fillMaxWidth(0.95f).wrapContentHeight().heightIn(max = maxH),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // A dot of the item's own color, so the composer's color choice means something.
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color).border(1.dp, BorderGray, CircleShape))
                        Text(
                            text = if (isPriority) "Priority" else "To-do",
                            style = MaterialTheme.typography.labelMedium,
                            color = RidgelineBlue
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.Close, contentDescription = "Close",
                        tint = MidnightSlate.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp).clickable { onDismiss() }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MidnightSlate,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                )

                Column(
                    modifier = Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(notes, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate.copy(alpha = 0.8f))
                    }
                    if (whenLabel != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.Schedule, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(14.dp))
                            Text(whenLabel, style = MaterialTheme.typography.labelLarge, color = RidgelineBlue)
                        }
                    }
                    if (triggerCue.isNotBlank()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(NeutralFill).padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text("When I ${triggerCue}…", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        }
                    }
                    if (reward.isNotBlank()) {
                        Text("🎁 Reward: $reward", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }

                    if (subtasks.isNotEmpty()) {
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                        val done = subtasks.count { it.isCompleted }
                        Text(
                            "Subtasks · $done/${subtasks.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (done == subtasks.size) SuccessGreen else TextMuted
                        )
                        subtasks.forEach { st ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (st.isCompleted) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle, contentDescription = "Mark incomplete",
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(18.dp).clickable { onToggleSubtask(st.id) }
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier.size(18.dp).clip(CircleShape)
                                                .border(1.5.dp, MidnightSlate.copy(alpha = 0.35f), CircleShape)
                                                .clickable { onToggleSubtask(st.id) }
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = st.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (st.isCompleted) TextMuted else MidnightSlate,
                                        textDecoration = if (st.isCompleted) TextDecoration.LineThrough else null
                                    )
                                }
                                if (st.details.isNotBlank()) {
                                    Text(
                                        text = st.details,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextMuted,
                                        modifier = Modifier.padding(start = 26.dp, top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onToggleComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCompleted) NeutralFill else SuccessGreen),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text(
                        if (isCompleted) "Mark not done" else "Mark complete",
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) MidnightSlate else PureWhite
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, RidgelineBlue),
                        modifier = Modifier.weight(1f)
                    ) { Text("Edit", color = RidgelineBlue, fontWeight = FontWeight.Bold) }
                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, OverdueRed),
                        modifier = Modifier.weight(1f)
                    ) { Text("Delete", color = OverdueRed, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

// ===== Material 3 Theme =====
// Maps the brand color palette into M3 semantic roles. Most existing widgets still reference the
// brand colors by name (RidgelineBlue, MidnightSlate, etc.) — that keeps working. But anywhere
// `MaterialTheme.colorScheme.primary` is read, it now resolves to the brand palette automatically.
// As widgets are progressively migrated to read from the scheme, swapping the palette becomes a
// one-place change instead of a 412-site find/replace.
//
// Surface containers in M3 give a layered look (background -> surface -> surfaceContainer ->
// surfaceContainerHigh) using subtle tonal shifts. The old SkyGray (background) / PureWhite (cards)
// split maps cleanly to background / surface, with IceBlueAccent serving as a higher-elevation tint.

private val RidgelineColorScheme = lightColorScheme(
    primary = RidgelineBlue,
    onPrimary = PureWhite,
    primaryContainer = IceBlueAccent,
    onPrimaryContainer = MidnightSlate,

    secondary = MidnightSlate,
    onSecondary = PureWhite,
    secondaryContainer = MistBlue,
    onSecondaryContainer = MidnightSlate,

    tertiary = SuccessGreen,
    onTertiary = PureWhite,

    background = SkyGray,
    onBackground = MidnightSlate,
    surface = PureWhite,
    onSurface = MidnightSlate,
    surfaceVariant = IceBlueAccent,
    onSurfaceVariant = RidgelineBlue,
    // Cards/sheets read as bright white to pop against the Platinum (#EFF2F1) background.
    // surfaceContainer matches the background for subtle inset fills; Highest stays ice for
    // selected-state highlights.
    surfaceContainerLowest = PureWhite,
    surfaceContainerLow = PureWhite,
    surfaceContainer = CardInset,
    surfaceContainerHigh = PureWhite,
    surfaceContainerHighest = IceBlueAccent,

    error = OverdueRed,
    onError = PureWhite,

    outline = MistBlue,
    outlineVariant = BorderGray
)

// Typography: the M3 type scale with weights tuned for a productivity app. The display* roles
// are for hero text (rare in this app), headline* for screen titles, title* for section/card
// headers, body* for prose, label* for buttons / chips / metadata.
//
// FontWeight.Black is reserved for emphasis (the scoreboard numerals, dialog titles) — most
// text uses Bold/SemiBold/Medium for a less heavy feel than the previous everything-is-Black look.
//
// IMPORTANT: do NOT set `color` on these styles. M3 expects text color to flow from
// LocalContentColor, which Surface / Card / Button set automatically based on their container
// (e.g. inside a primary-colored Button, content color is onPrimary). Hard-coding a color here
// would make dark text win over a Button's white content color, breaking contrast on filled buttons.

// ===== Fonts =====
// Plus Jakarta Sans for display/headings/titles; Inter for body/labels/UI — per the brand guide.
// Font files live in res/font/ (lowercase, underscored names). Available weights: Jakarta has
// 400/500/600, Inter has 400/500. Requests for heavier weights are synthesized from the nearest.
val JakartaFamily = FontFamily(
    Font(R.font.jakarta_regular, FontWeight.Normal),
    Font(R.font.jakarta_medium, FontWeight.Medium),
    Font(R.font.jakarta_semibold, FontWeight.SemiBold)
)
val InterFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium)
)

// Typography: display/headline/title roles use Plus Jakarta Sans; body/label use Inter. Weights
// are capped to what the font files provide (Jakarta max 600, Inter max 500) — heavier requests
// synthesize. Sizes preserved from the prior scale.
private val RidgelineTypography = Typography(
    displayLarge = TextStyle(fontFamily = JakartaFamily, fontSize = 36.sp, fontWeight = FontWeight.SemiBold),
    displayMedium = TextStyle(fontFamily = JakartaFamily, fontSize = 30.sp, fontWeight = FontWeight.SemiBold),
    displaySmall = TextStyle(fontFamily = JakartaFamily, fontSize = 26.sp, fontWeight = FontWeight.SemiBold),

    headlineLarge = TextStyle(fontFamily = JakartaFamily, fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = TextStyle(fontFamily = JakartaFamily, fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    headlineSmall = TextStyle(fontFamily = JakartaFamily, fontSize = 20.sp, fontWeight = FontWeight.SemiBold),

    titleLarge = TextStyle(fontFamily = JakartaFamily, fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontFamily = JakartaFamily, fontSize = 16.sp, fontWeight = FontWeight.Medium),
    titleSmall = TextStyle(fontFamily = JakartaFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium),

    bodyLarge = TextStyle(fontFamily = InterFamily, fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontFamily = InterFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontFamily = InterFamily, fontSize = 12.sp, fontWeight = FontWeight.Normal),

    labelLarge = TextStyle(fontFamily = InterFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontFamily = InterFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium),
    labelSmall = TextStyle(fontFamily = InterFamily, fontSize = 11.sp, fontWeight = FontWeight.Medium)
)

// Shape language: M3 recommends a small/medium/large/extraLarge progression. Default Compose
// shapes are 4/12/16/28dp — we use 8/12/16/24 which fits this app's existing 8-16dp range better.
private val RidgelineShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun RidgelineTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RidgelineColorScheme,
        typography = RidgelineTypography,
        shapes = RidgelineShapes
    ) {
        // Make Inter the default for any Text that doesn't reference a typography style. Without
        // this, bare Text() calls fall back to the system font; the typography roles above only
        // cover Text that explicitly uses MaterialTheme.typography.* . Headings authored via the
        // title/headline styles still get Plus Jakarta Sans.
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = InterFamily),
            content = content
        )
    }
}

// Core Rigid Data Models
data class ScheduleEntry(val defaultSlotLabel: String, var task: String, var isTopPriority: Boolean, var notes: String = "", var blockColor: Color = PureWhite, var isCompleted: Boolean = false, var startHour: Int = 0, var startMinute: Int = 0, var durationMins: Int = 60, var hasCustomTime: Boolean = false, val date: LocalDate = LocalDate.now(), var reward: String = "", var isAllDay: Boolean = false, var reminderEpochMillis: Long? = null, var reminderNightBefore: Boolean = false, var reminderMorningOf: Boolean = false, var triggerCue: String = "", var subtasks: List<TodoSubtask> = emptyList(), val id: String = UUID.randomUUID().toString())
data class WizardItem(val text: String, val id: Long = System.currentTimeMillis())
data class TodoSubtask(val id: String = UUID.randomUUID().toString(), var text: String, var isCompleted: Boolean = false, var dueDate: LocalDate? = null, var dueTime: LocalTime? = null, var details: String = "")
data class OtherTodoItem(val id: String, val text: String, var isCompleted: Boolean = false, val date: LocalDate, var completedDate: LocalDate? = null, var reward: String = "", var reminderEpochMillis: Long? = null, var reminderNightBefore: Boolean = false, var reminderMorningOf: Boolean = false, var triggerCue: String = "", var subtasks: List<TodoSubtask> = emptyList(), val fromBrainDump: Boolean = false, var dueDate: LocalDate? = null, var dueTime: LocalTime? = null, var notes: String = "", var blockColor: Color = PureWhite)
data class InsightLog(val id: Long = System.currentTimeMillis(), val behavior: String, val trigger: String, val timeOfDay: String, val date: LocalDate = LocalDate.now())

// A saved answer to a daily guided-reflection prompt. This replaces the old manual behavior log
// as the Insights page's self-awareness tool — qualitative, low-effort, done at a calm moment.
data class ReflectionEntry(val id: Long = System.currentTimeMillis(), val date: LocalDate = LocalDate.now(), val prompt: String, val response: String)
// A reflection prompt. linkedNoteId optionally ties it to a Trail Note lesson (learning loop).
data class ReflectionPrompt(val text: String, val linkedNoteId: String? = null)
data class GoalStep(val id: String = UUID.randomUUID().toString(), var text: String, var deadlineDate: LocalDate? = null, var isCompleted: Boolean = false)
data class GoalEntry(val id: String = UUID.randomUUID().toString(), var title: String, var steps: List<GoalStep> = emptyList(), var deadlineDate: LocalDate? = null, var reward: String = "", var isCompleted: Boolean = false, val weekAnchor: LocalDate? = null, val monthAnchor: YearMonth? = null, val type: String = "WEEKLY", var futureVision: String = "")

// ===== Elevation & Milestones =====
// The motivation model: every completion adds "elevation" (never lost). Crossing accelerating
// thresholds earns named milestones (XP-style, never un-earned). Replaces points/wins/streak.
//
// Elevation awarded per completion (weighted by effort):
//   - to-do / insight log:      +10
//   - priority / weekly goal:   +20
//   - goal step / monthly goal: +30 (the bigger commitments)

const val ELEVATION_TODO = 10
const val ELEVATION_PRIORITY = 20
const val ELEVATION_STEP = 30

data class Milestone(val threshold: Int, val name: String)

// Accelerating ladder: early milestones come fast (quick early reinforcement), then space out.
// After the named summit, generative peaks continue indefinitely so there's always a next climb.
val MILESTONE_LADDER: List<Milestone> = listOf(
    Milestone(0, "Trailhead"),
    Milestone(50, "Base Camp"),
    Milestone(120, "Foothills"),
    Milestone(220, "Tree Line"),
    Milestone(360, "The Ridge"),
    Milestone(560, "Alpine Zone"),
    Milestone(840, "High Camp"),
    Milestone(1220, "The Summit"),
    Milestone(1720, "Skyward"),
    Milestone(2360, "Cloudbreaker"),
    Milestone(3160, "Stratosphere")
)

/** The highest milestone whose threshold the elevation has reached or passed. */
fun currentMilestone(elevation: Int): Milestone =
    MILESTONE_LADDER.last { elevation >= it.threshold }

/** The next milestone above the current elevation, or null if past the top of the named ladder. */
fun nextMilestone(elevation: Int): Milestone? =
    MILESTONE_LADDER.firstOrNull { elevation < it.threshold }

/**
 * Today's "climb intensity" tier, 0-3, based on how many completions happened today.
 * 0 = no climb yet, 1 = a step, 2 = solid climb, 3 = peak day (3+).
 * Pure escalation — a light day is never penalized, you just don't climb as high.
 */
fun todaysClimbTier(completionsToday: Int): Int = completionsToday.coerceIn(0, 3)

// ===== Right Now selection =====
// A unified "what's the user's most pressing thing" abstraction across the various item types.
// Used to populate the Right Now card on the Home screen.

sealed class RightNowItem(val task: String, val reward: String) {
    // A top-priority schedule entry that's uncompleted. The main thing the card surfaces.
    class Priority(val entry: ScheduleEntry) : RightNowItem(entry.task, entry.reward)

    // An uncompleted to-do for today.
    class Todo(val todo: OtherTodoItem) : RightNowItem(todo.text, todo.reward)

    // A non-priority scheduled event — only surfaced when there are no tasks left.
    class Event(val entry: ScheduleEntry, val statusLabel: String) : RightNowItem(entry.task, entry.reward)

    // Everything's done — show an encouraging message instead of a task.
    object AllClear : RightNowItem("", "")
}

/**
 * Pick what the Right Now card should show.
 *
 * Ordering:
 *   1. Uncompleted top-priority schedule entries (the explicit "top 3"), soonest start first.
 *   2. Uncompleted to-dos for today, in list order.
 *   3. Only if no tasks remain: the next uncompleted non-priority event (in progress, then later).
 *   4. If nothing is left anywhere: AllClear (encouraging empty state).
 *
 * Priorities rank above to-dos because they're the user's explicitly-elevated focus items.
 * Scheduled (non-priority) events are intentionally demoted — they're context, not the
 * action the user most needs to take.
 */
fun pickRightNowItem(
    scheduleEntries: List<ScheduleEntry>,
    otherTodoEntries: List<OtherTodoItem>,
    now: LocalDateTime = LocalDateTime.now()
): RightNowItem {
    val today = now.toLocalDate()
    val nowMins = now.hour * 60 + now.minute

    val todaysEntries = scheduleEntries.filter { it.date == today && !it.isCompleted && !it.isAllDay }

    // Tier 1: uncompleted priorities, soonest start time first.
    val nextPriority = todaysEntries
        .filter { it.isTopPriority }
        .minByOrNull { it.startHour * 60 + it.startMinute }
    if (nextPriority != null) {
        return RightNowItem.Priority(nextPriority)
    }

    // Tier 2: uncompleted to-dos due today or carried over from an earlier day.
    // A carried-over to-do keeps its original (past) date — it's "carried" only as a display
    // concept on the Lists screen — so we include anything dated today or earlier that's still
    // open. Oldest first, so the most overdue item surfaces ahead of today's fresh ones.
    val nextTodo = otherTodoEntries
        .filter { !it.isCompleted && !it.date.isAfter(today) }
        .minByOrNull { it.date }
    if (nextTodo != null) {
        return RightNowItem.Todo(nextTodo)
    }

    // Tier 3: no tasks left — surface the next non-priority event for context.
    val nonPriorityEvents = todaysEntries.filter { !it.isTopPriority }
    // In progress first.
    val inProgress = nonPriorityEvents
        .map { it to (it.startHour * 60 + it.startMinute) }
        .filter { (e, startMins) -> startMins <= nowMins && nowMins < startMins + e.durationMins }
        .minByOrNull { (_, startMins) -> nowMins - startMins }
    if (inProgress != null) {
        val (e, startMins) = inProgress
        val minutesUntilEnd = (startMins + e.durationMins) - nowMins
        return RightNowItem.Event(e, "Happening now · $minutesUntilEnd min left")
    }
    // Then the next upcoming one today.
    val upcoming = nonPriorityEvents
        .map { it to (it.startHour * 60 + it.startMinute) }
        .filter { (_, startMins) -> startMins > nowMins }
        .minByOrNull { (_, startMins) -> startMins }
    if (upcoming != null) {
        val (e, _) = upcoming
        return RightNowItem.Event(e, "Up next · ${formatTimeLabel(e.startHour, e.startMinute)}")
    }

    // Tier 4: nothing left.
    return RightNowItem.AllClear
}

// Custom Notification Model
data class CustomNotificationData(val title: String, val message: String, val bgColor: Color, val icon: String)

/**
 * An active focus (Pomodoro-style) session — the delay-aversion chunking mechanic. ADHD brains
 * find a distant payoff hard to work toward (Sonuga-Barke delay aversion), so a big task is
 * broken into short timed chunks, each its own near-term win with a micro-reward.
 *
 *   taskLabel       — what the user is focusing on (may be blank for a general session)
 *   workMinutes     — length of each work chunk (user-picked: 10/15/25)
 *   phase           — WORK (counting down a work chunk) or BREAK (counting down a 5-min break)
 *   secondsLeft     — remaining seconds in the current phase
 *   chunksCompleted — how many work chunks have finished this session (drives the counter + rewards)
 *   isPaused        — whether the countdown is currently halted
 */
data class FocusSession(
    val taskLabel: String,
    val workMinutes: Int,
    val phase: String = "WORK",
    val secondsLeft: Int = workMinutes * 60,
    val chunksCompleted: Int = 0,
    val isPaused: Boolean = false
)

const val FOCUS_PHASE_WORK = "WORK"
const val FOCUS_PHASE_BREAK = "BREAK"
const val FOCUS_BREAK_MINUTES = 5

// ===== Persistence (JSON via android.org.json) =====

private fun parseLocalDate(s: String?): LocalDate? =
    if (s.isNullOrBlank()) null else runCatching { LocalDate.parse(s) }.getOrNull()

private fun parseYearMonth(s: String?): YearMonth? =
    if (s.isNullOrBlank()) null else runCatching { YearMonth.parse(s) }.getOrNull()

private fun JSONObject.optStringOrNull(key: String): String? =
    if (!has(key) || isNull(key)) null else optString(key, "").ifBlank { null }

private fun ScheduleEntry.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("defaultSlotLabel", defaultSlotLabel)
    put("task", task)
    put("isTopPriority", isTopPriority)
    put("notes", notes)
    put("blockColor", blockColor.toArgb())
    put("isCompleted", isCompleted)
    put("startHour", startHour)
    put("startMinute", startMinute)
    put("durationMins", durationMins)
    put("hasCustomTime", hasCustomTime)
    put("date", date.toString())
    put("reward", reward)
    put("isAllDay", isAllDay)
    putOpt("reminderEpochMillis", reminderEpochMillis)
    put("reminderNightBefore", reminderNightBefore)
    put("reminderMorningOf", reminderMorningOf)
    put("triggerCue", triggerCue)
    put("subtasks", subtasksToJsonArray(subtasks))
}

private fun scheduleEntryFromJson(o: JSONObject): ScheduleEntry = ScheduleEntry(
    defaultSlotLabel = o.optString("defaultSlotLabel", ""),
    task = o.optString("task", ""),
    isTopPriority = o.optBoolean("isTopPriority", false),
    notes = o.optString("notes", ""),
    blockColor = Color(o.optInt("blockColor", PureWhite.toArgb())),
    isCompleted = o.optBoolean("isCompleted", false),
    startHour = o.optInt("startHour", 0),
    startMinute = o.optInt("startMinute", 0),
    durationMins = o.optInt("durationMins", 60),
    hasCustomTime = o.optBoolean("hasCustomTime", false),
    date = parseLocalDate(o.optStringOrNull("date")) ?: LocalDate.now(),
    reward = o.optString("reward", ""),
    isAllDay = o.optBoolean("isAllDay", false),
    reminderEpochMillis = if (o.has("reminderEpochMillis") && !o.isNull("reminderEpochMillis")) o.optLong("reminderEpochMillis") else null,
    reminderNightBefore = o.optBoolean("reminderNightBefore", false),
    reminderMorningOf = o.optBoolean("reminderMorningOf", false),
    triggerCue = o.optString("triggerCue", ""),
    subtasks = subtasksFromJsonArray(o.optJSONArray("subtasks")),
    id = o.optStringOrNull("id") ?: UUID.randomUUID().toString()
)

private fun OtherTodoItem.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("text", text)
    put("isCompleted", isCompleted)
    put("date", date.toString())
    putOpt("completedDate", completedDate?.toString())
    put("reward", reward)
    putOpt("reminderEpochMillis", reminderEpochMillis)
    put("reminderNightBefore", reminderNightBefore)
    put("reminderMorningOf", reminderMorningOf)
    put("triggerCue", triggerCue)
    put("fromBrainDump", fromBrainDump)
    putOpt("dueDate", dueDate?.toString())
    putOpt("dueTime", dueTime?.toString())
    put("notes", notes)
    put("blockColor", blockColor.toArgb())
    put("subtasks", subtasksToJsonArray(subtasks))
}

// Subtask JSON is shared by to-dos and priorities (ScheduleEntry), so it lives in one place.
private fun subtasksToJsonArray(subtasks: List<TodoSubtask>): JSONArray = JSONArray().apply {
    subtasks.forEach { st ->
        put(JSONObject().apply {
            put("id", st.id)
            put("text", st.text)
            put("isCompleted", st.isCompleted)
            putOpt("dueDate", st.dueDate?.toString())
            putOpt("dueTime", st.dueTime?.toString())
            put("details", st.details)
        })
    }
}

private fun subtasksFromJsonArray(arr: JSONArray?): List<TodoSubtask> {
    if (arr == null) return emptyList()
    return buildList {
        for (i in 0 until arr.length()) {
            val so = arr.optJSONObject(i) ?: continue
            add(
                TodoSubtask(
                    id = so.optStringOrNull("id") ?: UUID.randomUUID().toString(),
                    text = so.optString("text", ""),
                    isCompleted = so.optBoolean("isCompleted", false),
                    dueDate = parseLocalDate(so.optStringOrNull("dueDate")),
                    dueTime = so.optStringOrNull("dueTime")?.let { runCatching { LocalTime.parse(it) }.getOrNull() },
                    details = so.optString("details", "")
                )
            )
        }
    }
}

private fun otherTodoItemFromJson(o: JSONObject): OtherTodoItem = OtherTodoItem(
    id = o.optStringOrNull("id") ?: UUID.randomUUID().toString(),
    text = o.optString("text", ""),
    isCompleted = o.optBoolean("isCompleted", false),
    date = parseLocalDate(o.optStringOrNull("date")) ?: LocalDate.now(),
    completedDate = parseLocalDate(o.optStringOrNull("completedDate")),
    reward = o.optString("reward", ""),
    reminderEpochMillis = if (o.has("reminderEpochMillis") && !o.isNull("reminderEpochMillis")) o.optLong("reminderEpochMillis") else null,
    reminderNightBefore = o.optBoolean("reminderNightBefore", false),
    reminderMorningOf = o.optBoolean("reminderMorningOf", false),
    triggerCue = o.optString("triggerCue", ""),
    fromBrainDump = o.optBoolean("fromBrainDump", false),
    dueDate = parseLocalDate(o.optStringOrNull("dueDate")),
    dueTime = o.optStringOrNull("dueTime")?.let { runCatching { LocalTime.parse(it) }.getOrNull() },
    notes = o.optString("notes", ""),
    blockColor = Color(o.optInt("blockColor", PureWhite.toArgb())),
    subtasks = subtasksFromJsonArray(o.optJSONArray("subtasks"))
)

private fun InsightLog.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("behavior", behavior)
    put("trigger", trigger)
    put("timeOfDay", timeOfDay)
    put("date", date.toString())
}

private fun insightLogFromJson(o: JSONObject): InsightLog = InsightLog(
    id = o.optLong("id", System.currentTimeMillis()),
    behavior = o.optString("behavior", ""),
    trigger = o.optString("trigger", ""),
    timeOfDay = o.optString("timeOfDay", ""),
    date = parseLocalDate(o.optStringOrNull("date")) ?: LocalDate.now()
)

private fun ReflectionEntry.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("date", date.toString())
    put("prompt", prompt)
    put("response", response)
}

private fun reflectionEntryFromJson(o: JSONObject): ReflectionEntry = ReflectionEntry(
    id = o.optLong("id", System.currentTimeMillis()),
    date = parseLocalDate(o.optStringOrNull("date")) ?: LocalDate.now(),
    prompt = o.optString("prompt", ""),
    response = o.optString("response", "")
)

// Rotating daily reflection prompts. A mix of general pattern-noticing prompts and ones tied to a
// Trail Note lesson (linkedNoteId) so reading and reflecting reinforce each other. Framed around
// curiosity and wins rather than confession. One is shown per day (deterministic rotation).
val REFLECTION_PROMPTS = listOf(
    ReflectionPrompt("What made starting something hard today — and did anything help?"),
    ReflectionPrompt("What's one thing that went better than you expected today?"),
    ReflectionPrompt("When did you feel most focused or in-flow today?"),
    ReflectionPrompt("What's one small win worth noticing from today?"),
    ReflectionPrompt("Did you get to be kind to yourself when something slipped today?", "tn_self_compassion"),
    ReflectionPrompt("Did breaking something into smaller pieces help you start today?", "tn_delay_aversion"),
    ReflectionPrompt("Did having someone nearby — even virtually — help you get going?", "tn_body_doubling"),
    ReflectionPrompt("Did getting something out of your head and onto paper help today?", "tn_working_memory"),
    ReflectionPrompt("Did a reward, big or small, help you follow through on anything?", "tn_chosen_rewards"),
    ReflectionPrompt("Where did time slip away from you today — what were you doing?", "tn_time_blindness"),
    ReflectionPrompt("What's one thing you're carrying into tomorrow?"),
    ReflectionPrompt("What drained your energy today, and what restored it?"),
    ReflectionPrompt("Did a strong feeling show up today? What was underneath it?", "tn_rsd"),
    ReflectionPrompt("What would make tomorrow one percent easier?"),
    ReflectionPrompt("Did a warning before switching tasks help you today?", "tn_task_switching")
)

// The prompt for a given day — deterministic rotation so it's stable within a day.
fun reflectionPromptForDate(date: LocalDate): ReflectionPrompt =
    REFLECTION_PROMPTS[(date.toEpochDay().mod(REFLECTION_PROMPTS.size.toLong())).toInt()]

private fun GoalStep.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("text", text)
    putOpt("deadlineDate", deadlineDate?.toString())
    put("isCompleted", isCompleted)
}

private fun goalStepFromJson(o: JSONObject): GoalStep = GoalStep(
    id = o.optStringOrNull("id") ?: UUID.randomUUID().toString(),
    text = o.optString("text", ""),
    deadlineDate = parseLocalDate(o.optStringOrNull("deadlineDate")),
    isCompleted = o.optBoolean("isCompleted", false)
)

private fun GoalEntry.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("title", title)
    put("steps", JSONArray().apply { steps.forEach { put(it.toJson()) } })
    putOpt("deadlineDate", deadlineDate?.toString())
    put("reward", reward)
    put("isCompleted", isCompleted)
    putOpt("weekAnchor", weekAnchor?.toString())
    putOpt("monthAnchor", monthAnchor?.toString())
    put("type", type)
    put("futureVision", futureVision)
}

private fun goalEntryFromJson(o: JSONObject): GoalEntry {
    val stepsArr = o.optJSONArray("steps") ?: JSONArray()
    val steps = (0 until stepsArr.length()).map { goalStepFromJson(stepsArr.getJSONObject(it)) }
    return GoalEntry(
        id = o.optStringOrNull("id") ?: UUID.randomUUID().toString(),
        title = o.optString("title", ""),
        steps = steps,
        deadlineDate = parseLocalDate(o.optStringOrNull("deadlineDate")),
        reward = o.optString("reward", ""),
        isCompleted = o.optBoolean("isCompleted", false),
        weekAnchor = parseLocalDate(o.optStringOrNull("weekAnchor")),
        monthAnchor = parseYearMonth(o.optStringOrNull("monthAnchor")),
        type = o.optString("type", "WEEKLY"),
        futureVision = o.optString("futureVision", "")
    )
}

private fun <T> loadList(json: String?, fromJson: (JSONObject) -> T): List<T> {
    if (json.isNullOrBlank()) return emptyList()
    return runCatching {
        val arr = JSONArray(json)
        (0 until arr.length()).map { fromJson(arr.getJSONObject(it)) }
    }.getOrDefault(emptyList())
}

private fun <T> serializeList(items: List<T>, toJson: (T) -> JSONObject): String {
    val arr = JSONArray()
    items.forEach { arr.put(toJson(it)) }
    return arr.toString()
}

private fun grandRewardsToJsonString(map: Map<LocalDate, String>): String =
    JSONObject().apply { map.forEach { (date, reward) -> put(date.toString(), reward) } }.toString()

private fun grandRewardsFromJsonString(json: String?): Map<LocalDate, String> {
    if (json.isNullOrBlank()) return emptyMap()
    return runCatching {
        val obj = JSONObject(json)
        val out = mutableMapOf<LocalDate, String>()
        obj.keys().forEach { key ->
            parseLocalDate(key)?.let { out[it] = obj.optString(key, "") }
        }
        out.toMap()
    }.getOrDefault(emptyMap())
}

// Activity dates for the 7-day rhythm view. Stored as a JSON array of epoch-day longs.
// Replaces the streak counter's loss-aversion model with a non-punitive "which of the last
// 7 days had activity" visualization that recovers naturally on return.
private fun activityDatesToJsonString(days: Set<Long>): String =
    JSONArray().apply { days.sorted().forEach { put(it) } }.toString()

private fun activityDatesFromJsonString(json: String?): Set<Long> {
    if (json.isNullOrBlank()) return emptySet()
    return runCatching {
        val arr = JSONArray(json)
        (0 until arr.length()).map { arr.getLong(it) }.toSet()
    }.getOrDefault(emptySet())
}

/**
 * The user's personal reward bank — small rewards (under 5 min) and medium rewards (15+ min).
 * Collected at first launch via the onboarding wizard and surfaced as tappable chips above
 * every reward input field, so the user converts a blank-field problem into a one-tap selection.
 *
 * Schatz 2024: chosen rewards are higher-value than imposed ones, and reward novelty matters —
 * which is why the bank can be refreshed (handled separately by the refresh prompt).
 */
data class RewardBank(val small: List<String>, val medium: List<String>)

private fun rewardBankToJsonString(bank: RewardBank): String =
    JSONObject().apply {
        put("small", JSONArray().apply { bank.small.forEach { put(it) } })
        put("medium", JSONArray().apply { bank.medium.forEach { put(it) } })
    }.toString()

private fun rewardBankFromJsonString(json: String?): RewardBank {
    if (json.isNullOrBlank()) return RewardBank(emptyList(), emptyList())
    return runCatching {
        val o = JSONObject(json)
        val small = o.optJSONArray("small")?.let { a -> (0 until a.length()).map { a.getString(it) } } ?: emptyList()
        val medium = o.optJSONArray("medium")?.let { a -> (0 until a.length()).map { a.getString(it) } } ?: emptyList()
        RewardBank(small, medium)
    }.getOrDefault(RewardBank(emptyList(), emptyList()))
}

/**
 * Default tracking-behavior catalog presented in the first-launch behavior picker. Each entry is
 * "<emoji> <label>" — same format as historical insight logs so changes to the catalog don't
 * orphan past data. Intentionally excludes Adult Content (out by user request — sensitive enough
 * that users who want it can add via "Custom" rather than seeing it on a default list).
 */
val DEFAULT_BEHAVIORS = listOf(
    "📱 Social Media",
    "🍩 Snacks/Sugar",
    "🎮 Video Games",
    "📺 Binge Watching",
    "🛍️ Impulse Buying",
    "🌀 Doomscrolling",
    "☕ Caffeine",
    "🐢 Procrastinating",
    "🍽️ Skipping Meals",
    "🌙 Sleep Avoidance",
    "📲 Phone Pickups",
    "💅 Nail Biting"
)

private fun behaviorsToJsonString(items: List<String>): String =
    JSONArray().apply { items.forEach { put(it) } }.toString()

private fun behaviorsFromJsonString(json: String?): List<String> {
    if (json.isNullOrBlank()) return emptyList()
    return runCatching {
        val a = JSONArray(json)
        (0 until a.length()).map { a.getString(it) }
    }.getOrDefault(emptyList())
}

// ===== Why-chip explanation copy =====
// In-the-moment psychoeducation text for the "?" chips next to key fields. Plain, warm,
// de-stigmatized language. Each is one short idea. The clinical disclaimer is appended to
// the reward one since it's the most-seen field; a standalone disclaimer also lives on the
// Insight screen header.
const val WHY_TRIGGER_CUE_TITLE = "Why \"When I…\"?"
const val WHY_TRIGGER_CUE_BODY = "Linking a task to something you already do — \"when I finish my coffee, I'll…\" — gives your brain a concrete cue to start. ADHD makes task initiation hard, and an if-then plan like this takes the decision out of the moment, so starting feels more automatic."
const val WHY_FUTURE_VISION_TITLE = "Why picture finishing?"
const val WHY_FUTURE_VISION_BODY = "ADHD brains tend to discount future rewards steeply — a payoff that's far away feels less real now. Vividly imagining the moment you finish (where you are, how it feels) makes that future reward feel closer and more motivating today."
const val WHY_REWARD_TITLE = "Why a reward?"
const val WHY_REWARD_BODY = "Reward works better than pressure for ADHD motivation, and a reward you chose yourself works best of all. Picking something small and immediate gives your brain a clear, near-term reason to follow through."

// ===== Trail Notes (micro-lesson psychoeducation) =====
// Short single-idea cards surfaced over time, each ending with a one-tap retrieval or reflection
// prompt and (optionally) a link into the relevant feature. Grounded in: micro-lessons + spaced
// retrieval practice drive retention (Martinengo 2024); interactivity beats static text
// (Selaskowski 2023); segment by journey stage (Seery 2025). Delivery is self-paced — the next
// note unlocks when the current one is finished.
//
// Prompt types:
//   "QUIZ"    — a retrieval question; one option is correct (correctOption index). Tapping shows
//               whether it was right, reinforcing the idea.
//   "REFLECT" — a reflection prompt; any tapped option is valid (no right answer). Used to make
//               the idea personally relevant rather than test recall.
//
// Journey stage:
//   "FOUNDATION" — explainers for newly-diagnosed users (what ADHD does, why these tools help)
//   "INMOMENT"   — in-the-moment tactics for users already familiar with their ADHD
//   "ANY"        — relevant to everyone; always in the sequence
//
// linkAction is an optional CTA target the UI maps to a navigation/dialog action.

const val TRAIL_PROMPT_QUIZ = "QUIZ"
const val TRAIL_PROMPT_REFLECT = "REFLECT"
const val TRAIL_STAGE_FOUNDATION = "FOUNDATION"
const val TRAIL_STAGE_INMOMENT = "INMOMENT"
const val TRAIL_STAGE_ANY = "ANY"

data class TrailNote(
    val id: String,
    val title: String,
    val body: String,
    val promptType: String,
    val promptQuestion: String,
    val promptOptions: List<String>,
    val correctOption: Int? = null,   // QUIZ only; index into promptOptions
    val linkAction: String? = null,   // optional CTA target, e.g. "ADD_CUE"
    val linkLabel: String? = null,    // CTA button text
    val stage: String = TRAIL_STAGE_ANY,
    val order: Int = 0,
    val sources: List<String> = emptyList()  // short citations shown in the reader for credibility
)

/**
 * Seed Trail Notes catalog. These are STARTER cards grounded in concepts already in the app,
 * written in the same plain, de-stigmatized voice as the Why chips. They are intentionally
 * revisable — the plan is to sharpen this content with co-design sessions before treating it as
 * final. The delivery system below is content-agnostic, so swapping/extending this list is the
 * only change needed when the real library is ready.
 *
 * Ordering: FOUNDATION cards carry lower order numbers (surfaced first for newly-diagnosed users);
 * INMOMENT cards are weighted earlier for familiar users (handled by the surfacing logic, not the
 * raw order). ANY cards thread throughout.
 */
val SEED_TRAIL_NOTES = listOf(
    TrailNote(
        id = "tn_what_is_dopamine",
        title = "Your brain isn't lazy — it's under-rewarded",
        body = "ADHD brains tend to under-respond to ordinary rewards, so everyday tasks can feel flat and hard to start. This isn't a willpower problem. It's why building in clear, immediate rewards — like the ones in this app — actually helps your brain engage.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why can ordinary tasks feel so hard to start?",
        promptOptions = listOf("You're lazy", "ADHD brains under-respond to ordinary rewards", "You don't care enough"),
        correctOption = 1,
        stage = TRAIL_STAGE_FOUNDATION,
        order = 1,
        sources = listOf(
            "Volkow et al. (2010), Molecular Psychiatry — dopamine reward-pathway dysfunction in ADHD",
            "Aarts et al. (2015), Frontiers in Human Neuroscience — striatal reward response in adult ADHD"
        )
    ),
    TrailNote(
        id = "tn_implementation_intentions",
        title = "Glue a task to something you already do",
        body = "Starting is the hardest part. An if-then plan — \"when I finish my coffee, I'll start the report\" — borrows momentum from a habit you already have, so the start happens almost on autopilot instead of needing a fresh decision.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "What makes an if-then \"starter cue\" work?",
        promptOptions = listOf("It borrows momentum from an existing habit", "It makes the task shorter", "It adds pressure"),
        correctOption = 0,
        linkAction = "ADD_CUE",
        linkLabel = "Add a starter cue",
        stage = TRAIL_STAGE_ANY,
        order = 2,
        sources = listOf(
            "Gollwitzer (1999), American Psychologist — implementation intentions",
            "Gawrilow, Gollwitzer & Oettingen (2011), J. of Social & Clinical Psychology — if-then plans in children with ADHD"
        )
    ),
    TrailNote(
        id = "tn_chosen_rewards",
        title = "Pick rewards you actually want",
        body = "A reward you chose yourself motivates more than one handed to you — and it works best when it's small and comes soon after the effort. That's the whole point of your reward bank: ready-made, self-chosen treats you can attach to a task in one tap.",
        promptType = TRAIL_PROMPT_REFLECT,
        promptQuestion = "When did you last reward yourself for finishing something?",
        promptOptions = listOf("Today", "This week", "Can't remember"),
        linkAction = "OPEN_REWARD_BANK",
        linkLabel = "Edit my reward bank",
        stage = TRAIL_STAGE_ANY,
        order = 3,
        sources = listOf(
            "Volkow et al. (2010), Molecular Psychiatry — why immediate reward matters in ADHD",
            "Deci & Ryan — self-determination theory: self-chosen rewards protect motivation"
        )
    ),
    TrailNote(
        id = "tn_delay_aversion",
        title = "Shrink the mountain into footholds",
        body = "A big task with a far-off payoff is exactly what an ADHD brain wants to avoid. Breaking it into short timed chunks — each with its own small win — turns one aversive mountain into a series of doable footholds. That's what a focus session does.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why break a task into short timed chunks?",
        promptOptions = listOf("To finish faster", "Each chunk is a near-term win, which is easier to start", "To track time better"),
        correctOption = 1,
        linkAction = "START_FOCUS",
        linkLabel = "Start a focus session",
        stage = TRAIL_STAGE_INMOMENT,
        order = 4,
        sources = listOf(
            "Marx et al. (2021), J. of Attention Disorders — delay-discounting meta-analysis (37 comparisons)",
            "Demurie et al. (2012), Developmental Science — steeper reward discounting in ADHD"
        )
    ),
    TrailNote(
        id = "tn_future_self",
        title = "Make the finish line feel real",
        body = "ADHD brains discount future rewards steeply — a payoff that's days away barely registers now. Vividly picturing the moment you finish (where you are, how it feels) pulls that future closer and makes it more motivating today.",
        promptType = TRAIL_PROMPT_REFLECT,
        promptQuestion = "Picture finishing a goal you're working on. How clearly can you see it?",
        promptOptions = listOf("Crystal clear", "A little fuzzy", "Haven't tried"),
        stage = TRAIL_STAGE_ANY,
        order = 5,
        sources = listOf(
            "Sonuga-Barke & Fairchild (2012), Biological Psychiatry — delay aversion in ADHD",
            "Barkley (2012), Executive Functions — \"temporal myopia\" (future rewards lack present salience)"
        )
    ),
    TrailNote(
        id = "tn_reward_novelty",
        title = "Rewards go stale — refresh them",
        body = "The same reward loses its pull over time as your brain habituates. Swapping in new rewards every few weeks keeps the novelty — and the motivation — alive. The app nudges you to refresh your bank about once a month for exactly this reason.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why refresh your rewards every few weeks?",
        promptOptions = listOf("The brain habituates and they lose their pull", "Old rewards stop being allowed", "It's required to keep the app working"),
        correctOption = 0,
        stage = TRAIL_STAGE_INMOMENT,
        order = 6,
        sources = listOf(
            "Schultz (1998), J. of Neurophysiology — dopamine reward signal & habituation",
            "Volkow et al. (2010), Molecular Psychiatry — ADHD reward system responds to novelty"
        )
    ),
    TrailNote(
        id = "tn_time_blindness",
        title = "Your internal clock runs on a different battery",
        body = "Time blindness isn't carelessness — it's a documented difference in how ADHD brains process time. When the brain's timing systems are dysregulated, the future can feel distant and vague even when it's minutes away, which is why deadlines that are \"soon\" can feel unreal until they're urgent. Making time visible — clocks, timers, visual countdowns — works because it externalizes what your brain struggles to generate internally.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why does an ADHD brain often lose track of time?",
        promptOptions = listOf("Time just moves faster for some people", "Differences in how the brain tracks duration", "ADHD brains don't care about being on time"),
        correctOption = 1,
        stage = TRAIL_STAGE_FOUNDATION,
        order = 7,
        sources = listOf(
            "Barkley (2012), Executive Functions — temporal processing as a core ADHD feature",
            "Doyle (2006), review — time perception among ADHD executive-function impairments"
        )
    ),
    TrailNote(
        id = "tn_task_switching",
        title = "Give yourself a 5-minute warning before you switch",
        body = "Every time you shift from one task to another, your brain pays a \"switch cost\" — a measurable dip in speed and accuracy while it unloads the old task and loads the new one. Research suggests ADHD brains tend to pay a larger switch cost. A simple 5-minute wind-down warning — a timer, a note, a spoken cue — gives your brain time to start disengaging before the switch lands, cutting the cost and reducing transition paralysis.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why does switching tasks feel harder than it should?",
        promptOptions = listOf("You're avoiding the next task", "Your brain needs time to unload one task and load the next", "You haven't practiced switching enough"),
        correctOption = 1,
        linkAction = "START_FOCUS",
        linkLabel = "Start a focus session",
        stage = TRAIL_STAGE_INMOMENT,
        order = 8,
        sources = listOf(
            "Cepeda, Kramer & Gonzalez de Sather (2001), Developmental Psychology — task-switch costs",
            "Marx et al. (2015), Neuropsychologia — attentional set-shifting in adults with ADHD"
        )
    ),
    TrailNote(
        id = "tn_wall_of_awful",
        title = "That feeling before a hard task has a name",
        body = "ADHD coach Brendan Mahan calls it the \"Wall of Awful\" — the emotional barrier built up from years of struggling with things that were hard because of your wiring. When you go to start a task you've struggled with before, your brain doesn't just see the task — it replays the past difficulty around it. The wall isn't weakness; it's an understandable response to a history of difficulty. Naming it is the first step to finding the door through it.",
        promptType = TRAIL_PROMPT_REFLECT,
        promptQuestion = "Think of a task you've been avoiding. What comes up when you picture starting it?",
        promptOptions = listOf("Dread or anxiety", "Shame or embarrassment", "Numbness — I just go blank"),
        stage = TRAIL_STAGE_FOUNDATION,
        order = 9,
        sources = listOf(
            "Brendan Mahan — the \"Wall of Awful\" (an ADHD coaching framework, not a clinical diagnosis)",
            "Psychology Today (2025) — clinical application of the concept (Mutti-Driscoll)"
        )
    ),
    TrailNote(
        id = "tn_rsd",
        title = "Strong feelings about mistakes aren't a personality flaw",
        body = "Many people with ADHD feel criticism, failure, or rejection intensely and suddenly — the reaction can seem out of proportion because it's tied to how the ADHD brain regulates emotion, not to weak character. A 2023 systematic review found emotional dysregulation is common across the ADHD lifespan and a real source of difficulty. Knowing the reaction is wired in, not a flaw, makes it easier to respond rather than react.",
        promptType = TRAIL_PROMPT_REFLECT,
        promptQuestion = "When you make a mistake or get criticized, how does it usually land?",
        promptOptions = listOf("It stings but passes quickly", "It hits hard and lingers for hours", "It can derail my whole day"),
        stage = TRAIL_STAGE_FOUNDATION,
        order = 10,
        sources = listOf(
            "Soler-Gutiérrez et al. (2023), systematic review — emotional dysregulation across the ADHD lifespan",
            "Shaw et al. (2014), American J. of Psychiatry — emotional dysregulation as a core ADHD feature",
            "Note: \"rejection sensitive dysphoria\" is a clinical-popular term (Dodson), not a formal DSM diagnosis"
        )
    ),
    TrailNote(
        id = "tn_body_doubling",
        title = "Someone nearby can do what willpower can't",
        body = "Body doubling — working alongside another person, even silently, even on video — is one of the most widely reported ADHD strategies for a reason. Another person's presence provides subtle external activation: light accountability, a shared attention field, and a pacing cue your brain can borrow. It draws on a well-established effect called social facilitation, where the presence of others raises arousal and helps you engage. In-person, virtual, or even a \"study with me\" video can all work.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why does having someone nearby help with getting started?",
        promptOptions = listOf("They can help if you get stuck", "Their presence externalizes the activation your brain struggles to self-generate", "It makes the work go faster"),
        correctOption = 1,
        linkAction = "START_FOCUS",
        linkLabel = "Start a focus session",
        stage = TRAIL_STAGE_INMOMENT,
        order = 11,
        sources = listOf(
            "Social facilitation research (well-established) — presence of others raises task arousal",
            "Ara et al. (2025), preliminary VR study (preprint, n=12) — body double aided ADHD task completion"
        )
    ),
    TrailNote(
        id = "tn_working_memory",
        title = "Your brain can't hold it all — so don't make it",
        body = "ADHD often involves working-memory limits — the ability to hold and use information while doing something else. When working memory is stretched, details slip, you lose track of steps, and everything takes more effort. The fix isn't trying harder to remember: it's getting the information out of your head. Checklists, written steps, voice memos, and reminders act as external memory, freeing up your attention for the task itself.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "What's the most effective way to handle working-memory gaps?",
        promptOptions = listOf("Practice memorizing things more", "Externalize info into lists, notes, or reminders", "Avoid complex tasks"),
        correctOption = 1,
        stage = TRAIL_STAGE_ANY,
        order = 12,
        sources = listOf(
            "Gilbert et al. (2022), Psychonomic Bulletin & Review — \"intention offloading\" is highly effective",
            "Barkley — externalizing information as a core ADHD strategy"
        )
    ),
    TrailNote(
        id = "tn_self_compassion",
        title = "Missing a day doesn't erase your progress",
        body = "ADHD brains are prone to all-or-nothing thinking: one missed day can feel like the whole streak — and the whole system — is broken. But research on habit formation shows a single lapse has no meaningful long-term effect if you restart promptly. What matters more is self-compassion: adults with ADHD who practice self-kindness tend to report better follow-through and lower emotional reactivity. It's not just a nice idea — it's a functional strategy.",
        promptType = TRAIL_PROMPT_REFLECT,
        promptQuestion = "When you miss a day on something you're building, what's your first reaction?",
        promptOptions = listOf("I restart the next day, no drama", "I feel guilty but come back eventually", "I write off the whole effort"),
        stage = TRAIL_STAGE_ANY,
        order = 13,
        sources = listOf(
            "Lally et al. (2010), European J. of Social Psychology — one missed day doesn't break habit formation",
            "Beaton, Sirois & Milne (2022), J. of Clinical Psychology — self-compassion & ADHD wellbeing"
        )
    ),
    TrailNote(
        id = "tn_sleep",
        title = "Sleep isn't optional — it's executive-function fuel",
        body = "ADHD already taxes executive function — attention, impulse control, working memory, emotional regulation. Sleep deprivation taxes the very same systems, so the two compound each other. Research shows people with ADHD traits are especially vulnerable to cognitive impairment from poor sleep, and sleep problems are common in ADHD (by some estimates affecting a majority of kids with ADHD). Protecting sleep is one of the highest-leverage things you can do for the functions ADHD already makes hard.",
        promptType = TRAIL_PROMPT_QUIZ,
        promptQuestion = "Why does poor sleep hit especially hard with ADHD?",
        promptOptions = listOf("ADHD brains need more sleep than other brains", "Sleep loss and ADHD tax the same executive systems, compounding the effect", "Sleep problems are unrelated to ADHD"),
        correctOption = 1,
        stage = TRAIL_STAGE_FOUNDATION,
        order = 14,
        sources = listOf(
            "Liang et al. (2021), Frontiers in Pediatrics — sleep mediates physical activity → executive function in ADHD",
            "Åkerstedt et al. (2020), Biological Psychiatry: CNNI — ADHD traits predict greater impairment after sleep loss"
        )
    ),
    TrailNote(
        id = "tn_hyperfocus",
        title = "Hyperfocus is a feature, not just a bug",
        body = "Hyperfocus — getting so absorbed that hours vanish — is one of ADHD's paradoxes: the same brain that can't stay on a boring task can lock onto an interesting one with extraordinary intensity. It appears linked to the same dopamine-and-interest mechanisms behind distraction: when a task is intrinsically rewarding, the system engages fully. The key is steering it on purpose. A focus session on the right task can become hyperfocus — but without an exit plan it can swallow your afternoon. Set a timer and a stopping cue before you dive in.",
        promptType = TRAIL_PROMPT_REFLECT,
        promptQuestion = "Last time you hyperfocused — was it on something useful, or did it eat your time?",
        promptOptions = listOf("Useful — I got a ton done", "Both — started useful, drifted", "Mostly time-eating"),
        linkAction = "START_FOCUS",
        linkLabel = "Start a focus session",
        stage = TRAIL_STAGE_ANY,
        order = 15,
        sources = listOf(
            "Hyperfocus in ADHD (2025), European Psychiatry — 50-adult study; recommends timers & structured breaks",
            "ADDitude — hyperfocus linked to dopamine and high-interest tasks"
        )
    )
)

private fun trailProgressToJsonString(seenIds: Set<String>): String =
    JSONArray().apply { seenIds.forEach { put(it) } }.toString()

private fun trailProgressFromJsonString(json: String?): Set<String> {
    if (json.isNullOrBlank()) return emptySet()
    return runCatching {
        val a = JSONArray(json)
        (0 until a.length()).map { a.getString(it) }.toSet()
    }.getOrDefault(emptySet())
}

/**
 * The ordered sequence of notes for a given journey stage. FOUNDATION users get foundational
 * cards first; INMOMENT users get in-the-moment cards weighted earlier. ANY cards always thread
 * through. Within a weight group, raw `order` decides sequence.
 */
fun trailSequenceForStage(stage: String): List<TrailNote> {
    val weight: (TrailNote) -> Int = { note ->
        when {
            note.stage == TRAIL_STAGE_ANY -> 1
            note.stage == stage -> 0          // matching-stage cards first
            else -> 2                          // off-stage cards last
        }
    }
    return SEED_TRAIL_NOTES.sortedWith(compareBy(weight, { it.order }))
}

// ===== Notifications =====
// Reminder flow:
//   1. When user sets a reminder on an item, scheduleReminder() registers an AlarmManager alarm
//      whose PendingIntent targets ReminderReceiver, with the item's stable id used as the
//      request code (so cancellations target the same alarm).
//   2. When the alarm fires, ReminderReceiver builds and posts a notification to the
//      "ridgeline_reminders" channel.
//   3. cancelReminder() removes the alarm using the same request code.

const val REMINDER_CHANNEL_ID = "ridgeline_reminders"
const val REMINDER_EXTRA_TITLE = "reminder_title"
const val REMINDER_EXTRA_BODY = "reminder_body"
const val REMINDER_EXTRA_NOTIF_ID = "reminder_notif_id"

// Sentinel values for the reminder-preset picker. Use negative numbers because they can't
// collide with any real "minutes before" value. Stored in dialogReminderMinutesBefore so we
// don't have to introduce a new sealed type just for two options.
const val REMINDER_PRESET_MORNING_OF = -1     // 9 AM same day as the event
const val REMINDER_PRESET_NIGHT_BEFORE = -2   // 8 PM the day before

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(REMINDER_EXTRA_TITLE) ?: "Reminder"
        val body = intent.getStringExtra(REMINDER_EXTRA_BODY) ?: ""
        val notifId = intent.getIntExtra(REMINDER_EXTRA_NOTIF_ID, 0)

        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_ascent)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Tapping the notification opens the app.
        val openIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (openIntent != null) {
            val pi = PendingIntent.getActivity(
                context, notifId, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pi)
        }

        // POST_NOTIFICATIONS is runtime-permission gated on API 33+. If the user denied it,
        // notify() throws SecurityException. Wrap in try/catch so the receiver doesn't crash.
        runCatching {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(notifId, builder.build())
            }
        }
    }
}

private fun ensureReminderChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            REMINDER_CHANNEL_ID,
            "Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Scheduled reminders for events, priorities, and to-dos" }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

// ===== Daily reflection reminder =====
// The Insights reflection only works if something external cues it — expecting the user to
// remember to open the app and reflect is the same self-initiation demand that made the old
// behavior log fail. So: one gentle nudge a day, at a time the user picks.
//
// Deliberate design choices:
//   - Its OWN notification channel, so muting reflection nudges doesn't also kill task reminders.
//   - The day's actual prompt goes in the body — it plants the question even if never tapped.
//   - Silent if the user already reflected today (nothing sours a feature like being nagged
//     for something you did).
//   - No streak pressure. Missing days is fine; that's the whole point of the self-compassion note.

const val REFLECTION_CHANNEL_ID = "ridgeline_reflection"
const val EXTRA_OPEN_REFLECTION = "open_reflection"
private const val REFLECTION_REQUEST_CODE = 990001
private const val REFLECTION_NOTIF_ID = 990002

private fun ensureReflectionChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            REFLECTION_CHANNEL_ID,
            "Daily reflection",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "A once-a-day nudge to reflect. Quiet on days you've already written." }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

/** The next occurrence of hour:minute — today if still ahead, otherwise tomorrow. */
fun nextReflectionTriggerMillis(hour: Int, minute: Int): Long {
    val now = LocalDateTime.now()
    var target = now.toLocalDate().atTime(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
    if (!target.isAfter(now)) target = target.plusDays(1)
    return target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun scheduleReflectionReminder(context: Context, hour: Int, minute: Int) {
    val intent = Intent(context, ReflectionReminderReceiver::class.java)
    val pi = PendingIntent.getBroadcast(
        context, REFLECTION_REQUEST_CODE, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAt = nextReflectionTriggerMillis(hour, minute)
    runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        } else {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        }
    }
}

fun cancelReflectionReminder(context: Context) {
    val intent = Intent(context, ReflectionReminderReceiver::class.java)
    val pi = PendingIntent.getBroadcast(
        context, REFLECTION_REQUEST_CODE, intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pi != null) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
        pi.cancel()
    }
}

/**
 * Whether a reflection already exists for [date], read straight from the stored JSON. The receiver
 * runs outside Compose, so it can't read the in-memory list — it goes to SharedPreferences.
 */
private fun hasReflectionForDate(prefsJson: String?, date: LocalDate): Boolean {
    if (prefsJson.isNullOrBlank()) return false
    return runCatching {
        val arr = JSONArray(prefsJson)
        (0 until arr.length()).any { i ->
            arr.optJSONObject(i)?.optString("date") == date.toString()
        }
    }.getOrDefault(false)
}

class ReflectionReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("ridgeline_storage_v7", Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("reflectionReminderEnabled", true)
        val hour = prefs.getInt("reflectionReminderHour", 20)
        val minute = prefs.getInt("reflectionReminderMinute", 0)

        // Always chain the next day's alarm first, so the daily loop is self-sustaining even if
        // we skip tonight's notification below.
        if (enabled) scheduleReflectionReminder(context, hour, minute) else return

        // Already reflected today? Stay quiet.
        val today = LocalDate.now()
        if (hasReflectionForDate(prefs.getString("reflectionEntries", null), today)) return

        ensureReflectionChannel(context)
        val prompt = reflectionPromptForDate(today)

        // Tapping routes straight into the reflection rather than just opening the app.
        val openIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            putExtra(EXTRA_OPEN_REFLECTION, true)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val builder = NotificationCompat.Builder(context, REFLECTION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_ascent)
            .setContentTitle("Today's reflection")
            // The prompt itself is the body — it does the work even unopened.
            .setContentText(prompt.text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(prompt.text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (openIntent != null) {
            builder.setContentIntent(
                PendingIntent.getActivity(
                    context, REFLECTION_NOTIF_ID, openIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        runCatching {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(REFLECTION_NOTIF_ID, builder.build())
            }
        }
    }
}

/** Stable, deterministic request code from an item id. Same id → same code → cancellable. */
/**
 * Reminder slots — each item can have up to 3 independent reminders (Schatz / prospective memory:
 * a single alarm fires once and is gone, but layered cues catch the user in different states).
 *   slot 0 = specific time (existing user-picked picker)
 *   slot 1 = night before at 8pm
 *   slot 2 = morning of at 9am
 * The slot is XOR'd into the request code so each reminder gets a unique AlarmManager PendingIntent.
 */
private const val REMINDER_SLOT_SPECIFIC = 0
private const val REMINDER_SLOT_NIGHT_BEFORE = 1
private const val REMINDER_SLOT_MORNING_OF = 2
private const val REMINDER_SLOT_DUE = 3

private fun reminderRequestCode(itemId: String, slot: Int = REMINDER_SLOT_SPECIFIC): Int =
    itemId.hashCode() xor slot

private fun buildReminderPendingIntent(
    context: Context,
    itemId: String,
    title: String,
    body: String,
    slot: Int = REMINDER_SLOT_SPECIFIC,
    flags: Int = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
): PendingIntent {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra(REMINDER_EXTRA_TITLE, title)
        putExtra(REMINDER_EXTRA_BODY, body)
        putExtra(REMINDER_EXTRA_NOTIF_ID, reminderRequestCode(itemId, slot))
    }
    return PendingIntent.getBroadcast(context, reminderRequestCode(itemId, slot), intent, flags)
}

fun scheduleReminder(context: Context, itemId: String, title: String, body: String, triggerAtMillis: Long, slot: Int = REMINDER_SLOT_SPECIFIC) {
    if (triggerAtMillis <= System.currentTimeMillis()) return // past, skip
    val pi = buildReminderPendingIntent(context, itemId, title, body, slot)
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            // No exact-alarm permission: use inexact which is still reasonably accurate (±15 min).
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        } else {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        }
    }
}

fun cancelReminder(context: Context, itemId: String, slot: Int = REMINDER_SLOT_SPECIFIC) {
    // Build a matching PendingIntent (FLAG_NO_CREATE returns existing one if any) and cancel.
    val intent = Intent(context, ReminderReceiver::class.java)
    val pi = PendingIntent.getBroadcast(
        context, reminderRequestCode(itemId, slot), intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pi != null) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
        pi.cancel()
    }
}

/** Cancel ALL reminder slots for an item (specific time, night before, morning of, due time). */
fun cancelAllReminders(context: Context, itemId: String) {
    cancelReminder(context, itemId, REMINDER_SLOT_SPECIFIC)
    cancelReminder(context, itemId, REMINDER_SLOT_NIGHT_BEFORE)
    cancelReminder(context, itemId, REMINDER_SLOT_MORNING_OF)
    cancelReminder(context, itemId, REMINDER_SLOT_DUE)
}

/**
 * Night-before reminder timestamp — 8pm the day before the item's date.
 * Returns null if that moment is already in the past (so caller skips scheduling).
 */
fun nightBeforeEpochMillis(itemDate: LocalDate): Long? {
    val moment = itemDate.minusDays(1).atTime(20, 0)
    val millis = moment.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return if (millis > System.currentTimeMillis()) millis else null
}

/**
 * Morning-of reminder timestamp — 9am on the item's date.
 * Returns null if that moment is already in the past.
 */
fun morningOfEpochMillis(itemDate: LocalDate): Long? {
    val moment = itemDate.atTime(9, 0)
    val millis = moment.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return if (millis > System.currentTimeMillis()) millis else null
}

/** Human-readable label for a reminder time, e.g. "Tomorrow at 9:00 AM" or "Today at 3:30 PM". */
fun formatReminderLabel(epochMillis: Long): String {
    val zone = ZoneId.systemDefault()
    val dt = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(epochMillis), zone)
    val today = LocalDate.now()
    val datePart = when (dt.toLocalDate()) {
        today -> "Today"
        today.plusDays(1) -> "Tomorrow"
        today.minusDays(1) -> "Yesterday"
        else -> "${dt.toLocalDate().month.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault())} ${dt.dayOfMonth}"
    }
    val hour12 = if (dt.hour == 0 || dt.hour == 12) 12 else if (dt.hour > 12) dt.hour - 12 else dt.hour
    val ampm = if (dt.hour < 12) "AM" else "PM"
    return "$datePart at $hour12:${String.format(Locale.US, "%02d", dt.minute)} $ampm"
}

fun formatTimeLabel(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val h12 = if (hour == 0 || hour == 12) 12 else if (hour > 12) hour - 12 else hour
    return String.format(Locale.US, "%d:%02d %s", h12, minute, amPm)
}

// ===== Device calendar (read-only) =====
// Reads events the user already has synced on their phone (Google, Outlook, etc.) via Android's
// CalendarContract content provider. No Google API, no backend, no account — just the
// READ_CALENDAR permission. Events are display-only: they appear in the schedule alongside
// app-created entries but can't be edited, completed, or deleted from here, and they never enter
// the persisted scheduleEntries list (so they can't be mistaken for app data or get serialized).

data class ExternalCalendarEvent(
    val id: String,            // "ext_<eventId>_<begin>" — stable, prefixed so it's identifiable
    val title: String,
    val startMillis: Long,
    val endMillis: Long,
    val isAllDay: Boolean,
    val calendarName: String,
    val location: String
)

// One calendar/account in the device's shared calendar store (e.g. a Gmail account, a work
// Outlook account, a "Holidays" calendar). Used to let the user choose which to display.
data class DeviceCalendar(
    val id: Long,
    val displayName: String,
    val accountName: String
)

/**
 * Lists the calendars available in the device's shared calendar store. Each event belongs to one
 * of these. Returns an empty list on any failure (missing permission, provider error).
 */
fun queryDeviceCalendars(context: Context): List<DeviceCalendar> {
    val out = mutableListOf<DeviceCalendar>()
    val projection = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        CalendarContract.Calendars.ACCOUNT_NAME
    )
    runCatching {
        context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI, projection, null, null,
            "${CalendarContract.Calendars.CALENDAR_DISPLAY_NAME} ASC"
        )?.use { c ->
            val idIdx = c.getColumnIndex(CalendarContract.Calendars._ID)
            val nameIdx = c.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            val acctIdx = c.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
            while (c.moveToNext()) {
                val id = if (idIdx >= 0) c.getLong(idIdx) else continue
                val name = (if (nameIdx >= 0) c.getString(nameIdx) else null)?.takeIf { it.isNotBlank() } ?: "(Unnamed calendar)"
                val acct = (if (acctIdx >= 0) c.getString(acctIdx) else null) ?: ""
                out.add(DeviceCalendar(id, name, acct))
            }
        }
    }
    return out
}

/**
 * Query the device calendar for events overlapping [date]. Uses CalendarContract.Instances, which
 * expands recurring events into concrete instances within the time window. Returns an empty list
 * on any failure (missing permission, provider error) rather than throwing.
 *
 * All-day handling: all-day events are anchored to UTC midnight by the provider, so their date is
 * derived in UTC; timed events use the system zone. This avoids the classic all-day off-by-one.
 */
fun queryDeviceCalendarEvents(context: Context, date: LocalDate, hiddenCalendarIds: Set<Long> = emptySet()): List<ExternalCalendarEvent> {
    val out = mutableListOf<ExternalCalendarEvent>()
    val zone = ZoneId.systemDefault()
    val dayStart = date.atStartOfDay(zone).toInstant().toEpochMilli()
    val dayEnd = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

    val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
    android.content.ContentUris.appendId(builder, dayStart)
    android.content.ContentUris.appendId(builder, dayEnd)
    val uri = builder.build()

    val projection = arrayOf(
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.TITLE,
        CalendarContract.Instances.BEGIN,
        CalendarContract.Instances.END,
        CalendarContract.Instances.ALL_DAY,
        CalendarContract.Instances.CALENDAR_DISPLAY_NAME,
        CalendarContract.Instances.EVENT_LOCATION,
        CalendarContract.Instances.CALENDAR_ID
    )

    runCatching {
        context.contentResolver.query(uri, projection, null, null, "${CalendarContract.Instances.BEGIN} ASC")?.use { c ->
            val idIdx = c.getColumnIndex(CalendarContract.Instances.EVENT_ID)
            val titleIdx = c.getColumnIndex(CalendarContract.Instances.TITLE)
            val beginIdx = c.getColumnIndex(CalendarContract.Instances.BEGIN)
            val endIdx = c.getColumnIndex(CalendarContract.Instances.END)
            val allDayIdx = c.getColumnIndex(CalendarContract.Instances.ALL_DAY)
            val calIdx = c.getColumnIndex(CalendarContract.Instances.CALENDAR_DISPLAY_NAME)
            val locIdx = c.getColumnIndex(CalendarContract.Instances.EVENT_LOCATION)
            val calIdIdx = c.getColumnIndex(CalendarContract.Instances.CALENDAR_ID)
            while (c.moveToNext()) {
                // Skip events from calendars the user has hidden.
                val calId = if (calIdIdx >= 0) c.getLong(calIdIdx) else -1L
                if (calId in hiddenCalendarIds) continue
                val begin = if (beginIdx >= 0) c.getLong(beginIdx) else continue
                val end = if (endIdx >= 0) c.getLong(endIdx) else begin
                val allDay = allDayIdx >= 0 && c.getInt(allDayIdx) == 1
                val title = (if (titleIdx >= 0) c.getString(titleIdx) else null)?.takeIf { it.isNotBlank() } ?: "(Untitled event)"

                // Decide whether this instance actually belongs to `date`.
                val belongs = if (allDay) {
                    val startDate = Instant.ofEpochMilli(begin).atZone(java.time.ZoneOffset.UTC).toLocalDate()
                    val endDateExclusive = Instant.ofEpochMilli(end).atZone(java.time.ZoneOffset.UTC).toLocalDate()
                    !date.isBefore(startDate) && date.isBefore(endDateExclusive)
                } else {
                    // Timed: include if it starts on this date (events spanning midnight show on start day).
                    Instant.ofEpochMilli(begin).atZone(zone).toLocalDate() == date
                }
                if (!belongs) continue

                val eventId = if (idIdx >= 0) c.getLong(idIdx) else begin
                out.add(
                    ExternalCalendarEvent(
                        id = "ext_${eventId}_$begin",
                        title = title,
                        startMillis = begin,
                        endMillis = end,
                        isAllDay = allDay,
                        calendarName = (if (calIdx >= 0) c.getString(calIdx) else null) ?: "",
                        location = (if (locIdx >= 0) c.getString(locIdx) else null) ?: ""
                    )
                )
            }
        }
    }
    return out
}

class MainActivity : ComponentActivity() {
    // Set when the app is opened by tapping the daily reflection notification, so MainApp can
    // land the user on the reflection instead of the default screen. Backed by mutableStateOf so
    // onNewIntent (app already running) also routes correctly.
    private val openReflectionRequest = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        openReflectionRequest.value = intent?.getBooleanExtra(EXTRA_OPEN_REFLECTION, false) == true
        setContent {
            RidgelineTheme {
                MainApp(
                    openReflection = openReflectionRequest.value,
                    onReflectionOpened = { openReflectionRequest.value = false }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.getBooleanExtra(EXTRA_OPEN_REFLECTION, false)) openReflectionRequest.value = true
    }
}

@Composable
fun StatDisplayItem(icon: String, label: String, value: String, valueColor: Color, isCondensed: Boolean, onClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .let { if (onClick != null) it.clickable { onClick() } else it }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = icon, fontSize = 14.sp)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = valueColor)
        }
        if (!isCondensed) {
            Text(text = label, fontSize = 11.sp, color = RidgelineBlue, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ScoreboardSection(
    elevation: Int,
    todayCompletions: Int,
    activityDates: Set<Long>,
    isCondensed: Boolean,
    onToggle: () -> Unit
) {
    val milestone = currentMilestone(elevation)
    val next = nextMilestone(elevation)
    val climbTier = todaysClimbTier(todayCompletions)

    val fraction = if (next != null) {
        val span = (next.threshold - milestone.threshold).coerceAtLeast(1)
        ((elevation - milestone.threshold).coerceIn(0, span)).toFloat() / span.toFloat()
    } else 1f

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable { onToggle() }
    ) {
        // AnimatedContent smooths the transition between condensed and expanded states.
        // SizeTransform tweens the card's height so it grows/shrinks gracefully instead of
        // snapping; fadeIn/fadeOut crossfades the two distinct subtrees on top of that.
        AnimatedContent(
            targetState = isCondensed,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220, delayMillis = 90)) togetherWith
                        fadeOut(animationSpec = tween(90)))
                    .using(SizeTransform(clip = false) { _, _ -> tween(240) })
            },
            label = "ScoreboardCondense"
        ) { condensed ->
            if (condensed) {
                // === Condensed: a richer compact strip ===
                // Milestone chip (filled blue pill, white text) · elevation · mini progress bar · climb badge.
                // Tighter than the old condensed header but more colorful and informative.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Milestone pill — small but vivid.
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(RidgelineBlue)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = milestone.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = PureWhite
                        )
                    }
                    // Elevation.
                    Text(
                        text = "$elevation ft",
                        style = MaterialTheme.typography.titleMedium,
                        color = MidnightSlate
                    )
                    // Mini progress bar — fills the remaining space, shows current span progress.
                    if (next != null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MistBlue.copy(alpha = 0.35f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction.coerceIn(0.02f, 1f))
                                    .clip(RoundedCornerShape(50))
                                    .background(MetricGreen)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    ClimbTierBadge(climbTier)
                }
            } else {
                // === Expanded: two-pane row with milestone+elevation inside the left pane ===
                // The climb-tier badge floats top-right above the panes; the panes themselves are
                // [vertical bar | milestone name → elevation → next stop info] · divider · [7-day rhythm].
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                    // Top-right climb-tier badge.
                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                        ClimbTierBadge(climbTier)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().height(104.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // --- Left pane: vertical bar + (milestone name · elevation · next stop block) ---
                        Row(
                            modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VerticalClimbBar(
                                elevation = elevation,
                                milestone = milestone,
                                next = next,
                                modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(verticalArrangement = Arrangement.Center) {
                                // Current milestone + elevation sit at the top of the text column,
                                // immediately above the "Next stop" info. This is the change from
                                // the previous full-width header — saves a row, ties name to bar.
                                Text(
                                    text = milestone.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = RidgelineBlue
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = elevation.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MidnightSlate
                                    )
                                    Text(
                                        text = " ft",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MidnightSlate.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                if (next != null) {
                                    Text(
                                        text = "Next stop",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MidnightSlate.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = next.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MidnightSlate
                                    )
                                    Text(
                                        text = "${next.threshold - elevation} ft to go",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = RidgelineBlue
                                    )
                                } else {
                                    Text(
                                        text = "Peak of the named trail!",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = RidgelineBlue
                                    )
                                    Text(
                                        text = "Keep climbing",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MidnightSlate.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }

                        // Divider between the two panes.
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .padding(vertical = 8.dp)
                                .background(MistBlue.copy(alpha = 0.4f))
                        )

                        // --- Right pane: 7-day rhythm ---
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Last 7 days",
                                style = MaterialTheme.typography.labelSmall,
                                color = MidnightSlate.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val today = LocalDate.now()
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (offset in 6 downTo 0) {
                                    val day = today.minusDays(offset.toLong())
                                    val active = activityDates.contains(day.toEpochDay())
                                    val isToday = offset == 0
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 3.dp)
                                    ) {
                                        Text(
                                            text = day.dayOfWeek.getDisplayName(DateTimeTextStyle.NARROW, Locale.getDefault()),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isToday) RidgelineBlue else MidnightSlate.copy(alpha = 0.4f)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(if (active) MetricGreen else Color.Transparent)
                                                .border(
                                                    width = if (active) 0.dp else 1.5.dp,
                                                    color = if (isToday) RidgelineBlue else MistBlue,
                                                    shape = RoundedCornerShape(50)
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Vertical climb bar: a tall track that fills bottom-up toward the next milestone, with small
 * dots marking the current milestone (bottom) and next milestone (top). Reinforces the
 * ascending-a-ridgeline metaphor within the scoreboard card.
 */
@Composable
fun VerticalClimbBar(
    elevation: Int,
    milestone: Milestone,
    next: Milestone?,
    modifier: Modifier = Modifier
) {
    val fraction = if (next != null) {
        val span = (next.threshold - milestone.threshold).coerceAtLeast(1)
        ((elevation - milestone.threshold).coerceIn(0, span)).toFloat() / span.toFloat()
    } else 1f

    Box(
        modifier = modifier.width(14.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Track.
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MistBlue.copy(alpha = 0.3f))
        )
        // Fill, growing from the bottom.
        Box(
            modifier = Modifier
                .fillMaxHeight(fraction.coerceIn(0.02f, 1f))
                .width(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MetricGreen)
        )
        // Next-milestone dot at the very top.
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(14.dp)
                .clip(RoundedCornerShape(50))
                .background(if (next == null) MetricGreen else MistBlue)
                .border(2.dp, MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(50))
        )
        // Current-milestone dot at the bottom (always reached → filled green).
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(14.dp)
                .clip(RoundedCornerShape(50))
                .background(MetricGreen)
                .border(2.dp, MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(50))
        )
    }
}

/**
 * Today's climb-intensity badge. Tiers map to how much was completed today:
 *   0 → faint "Not climbing yet"
 *   1 → a single chevron (a step)
 *   2 → double chevron (solid climb)
 *   3 → triple chevron + "Peak day" (3+)
 * Pure positive escalation — a light day shows the resting state, never a penalty.
 */
@Composable
fun ClimbTierBadge(tier: Int) {
    val (label, color) = when (tier) {
        0 -> "Rested" to MistBlue
        1 -> "Climbing" to MetricGreen
        2 -> "Strong climb" to MetricOrange
        else -> "Peak day" to MetricBlue
    }
    Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Chevron stack grows with tier (▲ count). Tier 0 shows a single faint chevron.
            val chevrons = if (tier == 0) 1 else tier
            repeat(chevrons) {
                Text(
                    text = "▲",
                    fontSize = 13.sp,
                    color = if (tier == 0) MistBlue else color
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (tier == 0) MidnightSlate.copy(alpha = 0.5f) else color
        )
    }
}

/**
 * One cell in the week strip on Schedule/Lists. Text-only, no card border — just the weekday
 * letter and date number, with a circular highlight ring around the number for the selected day.
 * Today (when not selected) gets a subtle ring too so it remains locatable at a glance.
 */
@Composable
fun RowScope.WeekStripDay(date: LocalDate, isSelected: Boolean, isToday: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // Single-letter weekday (M/T/W…) above the date number. The narrow form fits the
        // tighter inline-with-month layout while still giving the user the day-of-week cue.
        Text(
            text = date.dayOfWeek.getDisplayName(DateTimeTextStyle.NARROW, Locale.getDefault()),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) RidgelineBlue else MidnightSlate.copy(alpha = 0.5f)
        )
        // The number sits inside a 28dp circle (down from 32dp) to keep the row compact.
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isSelected) RidgelineBlue else Color.Transparent)
                .border(
                    width = if (!isSelected && isToday) 1.5.dp else 0.dp,
                    color = if (!isSelected && isToday) RidgelineBlue else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = if (isSelected) PureWhite else MidnightSlate
            )
        }
    }
}

/**
 * Dropdown calendar: a month grid that appears inline below the month label. Horizontal
 * swiping moves between months; tapping a day fires onDayPick. Replaces the previous
 * modal Dialog pop-up so the calendar feels like part of the page, not an overlay.
 */
@Composable
fun DropdownCalendar(
    monthState: YearMonth,
    selectedDate: LocalDate,
    onMonthChange: (YearMonth) -> Unit,
    onDayPick: (LocalDate) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .pointerInput(monthState) {
                    var accumulated = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { accumulated = 0f },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            accumulated += dragAmount
                        },
                        onDragEnd = {
                            val threshold = 140f
                            if (accumulated < -threshold) onMonthChange(monthState.plusMonths(1))
                            else if (accumulated > threshold) onMonthChange(monthState.minusMonths(1))
                        }
                    )
                },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onMonthChange(monthState.minusMonths(1)) }) {
                    Text("◀", color = RidgelineBlue, fontWeight = FontWeight.Bold)
                }
                AnimatedContent(
                    targetState = monthState,
                    transitionSpec = {
                        if (targetState.isAfter(initialState)) {
                            slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                        } else {
                            slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                        }
                    },
                    label = "MonthHeaderSwap"
                ) { month ->
                    Text(
                        text = "${month.month.getDisplayName(DateTimeTextStyle.FULL, Locale.getDefault())} ${month.year}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MidnightSlate
                    )
                }
                IconButton(onClick = { onMonthChange(monthState.plusMonths(1)) }) {
                    Text("▶", color = RidgelineBlue, fontWeight = FontWeight.Bold)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach { d ->
                    Text(
                        text = d,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = MidnightSlate.copy(alpha = 0.5f)
                    )
                }
            }

            AnimatedContent(
                targetState = monthState,
                transitionSpec = {
                    if (targetState.isAfter(initialState)) {
                        slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                    } else {
                        slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                    }
                },
                label = "MonthGridSwap"
            ) { month ->
                val firstOfMonth = month.atDay(1)
                val daysInMonth = month.lengthOfMonth()
                val prefixEmpty = (firstOfMonth.dayOfWeek.value - 1) % 7
                val totalSlots = prefixEmpty + daysInMonth
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    var slot = 0
                    while (slot < totalSlots) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            repeat(7) {
                                val dayNum = slot - prefixEmpty + 1
                                if (slot < prefixEmpty || dayNum > daysInMonth) {
                                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                                } else {
                                    val cellDate = month.atDay(dayNum)
                                    val isSelected = cellDate == selectedDate
                                    val isToday = cellDate == LocalDate.now()
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) RidgelineBlue else Color.Transparent)
                                            .border(
                                                width = if (!isSelected && isToday) 1.5.dp else 0.dp,
                                                color = if (!isSelected && isToday) RidgelineBlue else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable { onDayPick(cellDate) }
                                    ) {
                                        Text(
                                            text = dayNum.toString(),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (isSelected) PureWhite else MidnightSlate
                                        )
                                    }
                                }
                                slot++
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A small tappable "?" chip that opens a lightweight info popup explaining *why* a feature
 * exists — in-the-moment psychoeducation (Seery 2025: experienced users want explanation at
 * the point of challenge, not in a manual). Grounded in the research but written in plain,
 * de-stigmatized language (Flobak 2021).
 *
 * Sits inline next to a field label or section header. The popup is a simple AlertDialog with
 * a title, the explanation body, and a single dismiss button. Intentionally tiny and low-risk:
 * it touches no existing flow and renders only when tapped.
 */
@Composable
fun WhyChip(title: String, explanation: String) {
    var showInfo by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(RidgelineBlue.copy(alpha = 0.12f))
            .clickable { showInfo = true },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RidgelineBlue)
    }
    if (showInfo) {
        AlertDialog(
            onDismissRequest = { showInfo = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(16.dp),
            title = { Text(title, fontWeight = FontWeight.Black, color = MidnightSlate, fontSize = 17.sp) },
            text = {
                Text(explanation, color = MidnightSlate.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(onClick = { showInfo = false }) {
                    Text("Got it", color = RidgelineBlue, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/**
 * A horizontal scrollable row of pill chips showing the user's reward bank. Tapping a chip
 * fills the associated reward input with that string. Sits above any reward TextField to
 * convert a blank-field problem into a one-tap selection.
 *
 * Renders nothing when the bank is empty (e.g., the user hasn't completed onboarding yet).
 * Use `mediumOnly = true` for the grand reward field (since grand rewards are end-of-day,
 * higher-value moments). Default shows both pools concatenated.
 */
@Composable
fun RewardChipsRow(
    bank: RewardBank,
    onPick: (String) -> Unit,
    mediumOnly: Boolean = false,
    smallOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    val items = when {
        mediumOnly -> bank.medium
        smallOnly -> bank.small
        else -> bank.small + bank.medium
    }
    if (items.isEmpty()) return
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { reward ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(IceBlueAccent)
                    .clickable { onPick(reward) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = reward,
                    style = MaterialTheme.typography.labelMedium,
                    color = MidnightSlate,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Compact priorities card — slim header (pill + M/N + tiny progress bar) plus each priority as a
 * tight pill row. Used both inline (when the user is near the top of Lists) and pinned above the
 * scrolling area (when the user scrolls down — so priorities stay visible while reviewing to-dos).
 */
@Composable
fun CondensedPrioritiesCard(
    priorities: List<ScheduleEntry>,
    onPriorityClick: (ScheduleEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val total = priorities.size
    val done = priorities.count { it.isCompleted }
    val fraction = if (total > 0) done.toFloat() / total.toFloat() else 0f
    val allDone = total > 0 && done == total
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // Once every priority is done, this pinned strip stops listing them. Scrolling to your
        // to-dos shouldn't mean scrolling past a wall of finished work — collapse to a single
        // line of credit and give the space back.
        if (allDone) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                Text(
                    text = "Priorities complete for today",
                    style = MaterialTheme.typography.titleSmall,
                    color = SuccessGreen
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(RidgelineBlue)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Priorities", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                    }
                    if (total > 0) {
                        Text(text = "$done / $total", style = MaterialTheme.typography.labelMedium, color = MidnightSlate.copy(alpha = 0.7f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (total > 0) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MistBlue.copy(alpha = 0.35f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction.coerceIn(0.02f, 1f))
                                    .clip(RoundedCornerShape(50))
                                    .background(MetricGreen)
                            )
                        }
                    }
                }
                if (priorities.isEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "None set",
                        style = MaterialTheme.typography.labelMedium,
                        color = MidnightSlate.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        priorities.forEach { entry ->
                            val isDone = entry.isCompleted
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isDone) RidgelineBlue.copy(alpha = 0.5f) else RidgelineBlue)
                                    .clickable { onPriorityClick(entry) }
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = entry.task,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PureWhite,
                                    maxLines = 1,
                                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isDone) {
                                    Text("✓", color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp(openReflection: Boolean = false, onReflectionOpened: () -> Unit = {}) {
    var currentScreen by remember { mutableStateOf("Home") }
    val context = LocalContext.current
    // Haptic feedback handle — used by milestone celebrations. Unlike the Vibrator service, this
    // doesn't require the VIBRATE manifest permission, so it works as a guaranteed minimum buzz
    // even if the permission isn't granted. The longer waveform via Vibrator is an upgrade.
    val haptics = LocalHapticFeedback.current
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    // Set up the reminder notification channel once on first composition.
    // On API 33+ (Tiramisu), POST_NOTIFICATIONS is a runtime permission — request it if missing.
    LaunchedEffect(Unit) {
        ensureReminderChannel(context)
        ensureReflectionChannel(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                (context as? Activity)?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001
                    )
                }
            }
        }
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // Shared Storage Repository
    val sharedPrefs = remember { context.getSharedPreferences("ridgeline_storage_v7", Context.MODE_PRIVATE) }
    // Elevation: the never-decreasing motivation accumulator (replaces points/wins).
    var elevation by remember { mutableStateOf(sharedPrefs.getInt("elevation", 0)) }
    // Full-screen milestone celebration. When elevation crosses a milestone threshold,
    // awardElevation sets this and triggers haptic feedback. The overlay shows until tapped.
    var celebrationMilestone by remember { mutableStateOf<Milestone?>(null) }
    // Ascent celebration: drops a big card showing the climb just completed. The bar animates
    // from `before` up to `after` and a "+X ft" badge pops out. Auto-dismisses.
    data class AscentEvent(val before: Int, val after: Int, val taskLabel: String)
    var ascentCelebration by remember { mutableStateOf<AscentEvent?>(null) }
    // Goal pending completion confirmation. Set when the user taps the goal flag/title to mark
    // the whole goal complete; the confirmation dialog reads this and either completes the goal
    // or cancels. Unchecking still happens immediately (no confirm — reversal shouldn't be gated).
    var goalPendingCompletion by remember { mutableStateOf<GoalEntry?>(null) }
    // Today's completion count, for the daily climb-intensity tier. Persisted with the date it
    // belongs to so it resets automatically when a new day begins.
    var todayCompletions by remember {
        val storedDay = sharedPrefs.getLong("todayCompletionsDay", 0L)
        val today = LocalDate.now().toEpochDay()
        mutableStateOf(if (storedDay == today) sharedPrefs.getInt("todayCompletions", 0) else 0)
    }
    // Preserved (no longer surfaced) — kept so a future opt-in could revive streaks.
    var points by remember { mutableStateOf(sharedPrefs.getInt("points", 0)) }
    var wins by remember { mutableStateOf(sharedPrefs.getInt("wins", 0)) }
    var streak by remember { mutableStateOf(sharedPrefs.getInt("streak", 0)) }
    var freezes by remember { mutableStateOf(sharedPrefs.getInt("freezes", 0)) }
    // 7-day rhythm: set of epoch-days on which the user had any activity. Drives the rhythm dots.
    val activityDates = remember {
        mutableStateListOf<Long>().apply {
            addAll(activityDatesFromJsonString(sharedPrefs.getString("activityDates", null)))
        }
    }

    // --- USER REWARD BANK ---
    // The user's chosen list of small/medium rewards. Surfaced as tappable chips above every
    // reward input throughout the app. Persisted as JSON.
    var rewardBank by remember {
        mutableStateOf(rewardBankFromJsonString(sharedPrefs.getString("rewardBank", null)))
    }
    LaunchedEffect(Unit) {
        snapshotFlow { rewardBank }.drop(1).collect {
            sharedPrefs.edit().putString("rewardBank", rewardBankToJsonString(it)).apply()
        }
    }
    // First-launch detection. Read once; flips to true when the onboarding wizard completes.
    // Subsequent launches read the persisted flag and skip the wizard.
    var hasCompletedSetup by remember { mutableStateOf(sharedPrefs.getBoolean("hasCompletedSetup", false)) }
    // Show the onboarding wizard on first launch (when no setup flag is stored).
    var showOnboardingWizard by remember { mutableStateOf(!hasCompletedSetup) }
    // Editor dialog — reachable from the Insight page gear. Lets the user re-edit their bank
    // after onboarding is complete.
    var showRewardBankEditor by remember { mutableStateOf(false) }
    // Last-refresh timestamp for the bank. Used by the 4-week refresh prompt (Schatz novelty).
    // Initialized to the first time the user completes onboarding; updated when they edit.
    var rewardBankLastRefreshMs by remember {
        mutableStateOf(sharedPrefs.getLong("rewardBankLastRefreshMs", 0L))
    }
    // Whether the user has dismissed the refresh prompt for the current cycle. Resets when
    // rewardBankLastRefreshMs is updated (i.e., user actually edited the bank).
    var refreshPromptDismissedMs by remember {
        mutableStateOf(sharedPrefs.getLong("refreshPromptDismissedMs", 0L))
    }

    // The manual behavior log is retired — the Insights page is now daily reflection + patterns +
    // Trail Notes. Historical insightLogs still deserialize and stay in backups; they're simply no
    // longer written to or displayed.
    // Activity grid date window. "30" (last 30 days), "90", or "ALL". Persisted so the user's
    // preference survives relaunch.
    var heatmapWindow by remember { mutableStateOf(sharedPrefs.getString("heatmapWindow", "30") ?: "30") }
    LaunchedEffect(Unit) {
        snapshotFlow { heatmapWindow }.drop(1).collect {
            sharedPrefs.edit().putString("heatmapWindow", it).apply()
        }
    }

    // --- TRAIL NOTES (micro-lesson psychoeducation) ---
    // Journey stage drives which notes surface first. Set by a one-tap onboarding question;
    // defaults to FOUNDATION until answered (newly-diagnosed-friendly default).
    var journeyStage by remember { mutableStateOf(sharedPrefs.getString("journeyStage", TRAIL_STAGE_FOUNDATION) ?: TRAIL_STAGE_FOUNDATION) }
    LaunchedEffect(Unit) {
        snapshotFlow { journeyStage }.drop(1).collect {
            sharedPrefs.edit().putString("journeyStage", it).apply()
        }
    }
    // Set of note ids the user has finished. Self-paced unlock: the next note is the first one
    // in the stage sequence not in this set.
    var trailNotesSeen by remember { mutableStateOf(trailProgressFromJsonString(sharedPrefs.getString("trailNotesSeen", null))) }
    LaunchedEffect(Unit) {
        snapshotFlow { trailNotesSeen }.drop(1).collect {
            sharedPrefs.edit().putString("trailNotesSeen", trailProgressToJsonString(it)).apply()
        }
    }
    // Whether the journey-stage question has been answered. Triggers a one-time prompt on the
    // Insight screen (same pattern as the behavior picker).
    var hasSetJourneyStage by remember { mutableStateOf(sharedPrefs.getBoolean("hasSetJourneyStage", false)) }
    var showJourneyStagePrompt by remember { mutableStateOf(false) }
    // The note currently open in the reader (null = reader closed). Browsing the library or
    // tapping the "next note" card sets this.
    var openTrailNote by remember { mutableStateOf<TrailNote?>(null) }
    // Whether the Trail Notes library (browse past + upcoming) is open.
    var showTrailLibrary by remember { mutableStateOf(false) }
    // Ask the journey-stage question once, after behavior setup, when landing on Insight.
    LaunchedEffect(currentScreen, hasSetJourneyStage) {
        if (currentScreen == "LogInsight" && !hasSetJourneyStage) {
            showJourneyStagePrompt = true
        }
    }

    // --- CUSTOM NOTIFICATION SYSTEM ---
    var customNotification by remember { mutableStateOf<CustomNotificationData?>(null) }

    fun showNotification(title: String, msg: String, color: Color, icon: String) {
        customNotification = CustomNotificationData(title, msg, color, icon)
    }

    LaunchedEffect(customNotification) {
        if (customNotification != null) {
            delay(3500)
            customNotification = null
        }
    }

    // --- GAMIFICATION ENGINE ---
    fun processStreak(isActivity: Boolean) {
        val today = LocalDate.now().toEpochDay()
        val lastActive = sharedPrefs.getLong("lastActivityDate", 0L)

        if (lastActive == 0L && isActivity) {
            streak = 1
            sharedPrefs.edit().putLong("lastActivityDate", today).putInt("streak", streak).apply()
        } else if (lastActive != 0L && today > lastActive) {
            val daysMissed = (today - lastActive - 1).toInt()
            var tempFreezes = freezes
            var tempPoints = points
            var streakKept = true
            var autoBought = 0
            var usedFreezes = 0

            if (daysMissed > 0) {
                for (i in 1..daysMissed) {
                    if (tempFreezes > 0) {
                        tempFreezes--
                        usedFreezes++
                    } else if (tempPoints >= 50) {
                        tempPoints -= 50
                        autoBought++
                        usedFreezes++
                    } else {
                        streakKept = false
                        break
                    }
                }
            }

            if (isActivity) {
                if (streakKept) {
                    streak++
                    if (usedFreezes > 0) {
                        val freezeMsg = if (autoBought > 0) "Auto-bought $autoBought freeze(s) to protect your streak!" else "Used $usedFreezes freeze(s) to protect your streak!"
                        showNotification("Streak Saved!", freezeMsg, MetricBlue, "❄️")
                    }
                } else {
                    streak = 1
                    showNotification("Streak Reset", "You missed a few days, but you're back at it!", MetricOrange, "🔄")
                }
                freezes = tempFreezes
                points = tempPoints
                sharedPrefs.edit().putLong("lastActivityDate", today).putInt("streak", streak).putInt("freezes", freezes).putInt("points", points).apply()
            } else {
                if (!streakKept && streak > 0) {
                    streak = 0
                    freezes = tempFreezes
                    points = tempPoints
                    sharedPrefs.edit().putInt("streak", 0).putInt("freezes", freezes).putInt("points", points).apply()
                } else if (streakKept && (freezes != tempFreezes || points != tempPoints)) {
                    freezes = tempFreezes
                    points = tempPoints
                    sharedPrefs.edit().putInt("freezes", freezes).putInt("points", points).apply()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        processStreak(false) // Passive check on app open
    }

    fun recordActivity() {
        processStreak(true)
        // Record today for the 7-day rhythm view (idempotent — only add once per day).
        val today = LocalDate.now().toEpochDay()
        if (!activityDates.contains(today)) {
            activityDates.add(today)
        }
    }

    // Increment today's completion counter, resetting it first if the stored day is stale.
    fun bumpTodayCompletions() {
        val today = LocalDate.now().toEpochDay()
        val storedDay = sharedPrefs.getLong("todayCompletionsDay", 0L)
        todayCompletions = if (storedDay == today) todayCompletions + 1 else 1
        sharedPrefs.edit().putLong("todayCompletionsDay", today).putInt("todayCompletions", todayCompletions).apply()
    }

    /**
     * Award elevation for a completion. Adds the (effort-weighted) amount, records activity for
     * the rhythm view, bumps today's climb tier, fires a "Well Done!" banner, and — if this push
     * crossed a milestone threshold — triggers the full-screen milestone celebration with haptics.
     *
     * When `silent` is true, the elevation is still awarded (and milestone crossings still fire),
     * but the routine ascent celebration overlay is skipped. Used when another UI element (like
     * the grand reward celebration AlertDialog) will display the achievement instead — avoids
     * stacking two overlays for the same moment.
     */
    fun awardElevation(amount: Int, customMsg: String, silent: Boolean = false) {
        val before = elevation
        elevation += amount
        sharedPrefs.edit().putInt("elevation", elevation).apply()
        recordActivity()
        bumpTodayCompletions()

        // Milestone crossing: if the new elevation reached a threshold the old one hadn't,
        // pop the big celebration overlay instead of a small banner. The "Well Done!" banner
        // is suppressed in that case so the celebration owns the moment. Milestone celebrations
        // run even when silent=true — they're too significant to skip.
        val crossed = MILESTONE_LADDER.firstOrNull { it.threshold in (before + 1)..elevation }
        if (crossed != null) {
            celebrationMilestone = crossed
            // Compose haptic feedback — always available, no permission needed. This is the
            // floor: even on devices without VIBRATE permission, the user feels something.
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            // Vibrator system service — the upgrade path. Longer, stronger triple-pulse pattern.
            // Requires <uses-permission android:name="android.permission.VIBRATE" /> in manifest.
            // Wrapped in try/catch so a missing permission just skips this and leaves the haptic.
            try {
                val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager).defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
                }
                // Triple-pulse: tap-tap-TAP. Stronger amplitude on the final hit (where supported).
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 100, 80, 100, 80, 300)
                    val amplitudes = intArrayOf(0, 180, 0, 180, 0, 255)
                    val effect = if (vib.hasAmplitudeControl()) {
                        android.os.VibrationEffect.createWaveform(timings, amplitudes, -1)
                    } else {
                        android.os.VibrationEffect.createWaveform(timings, -1)
                    }
                    vib.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vib.vibrate(longArrayOf(0, 100, 80, 100, 80, 300), -1)
                }
            } catch (_: Throwable) { /* vibration is decoration — never fail an award over it */ }
        } else if (!silent) {
            // No milestone — fire the ascent celebration instead of a small banner.
            // The overlay shows the bar climbing from `before` to `after` with a "+X ft" pop.
            ascentCelebration = AscentEvent(before = before, after = elevation, taskLabel = customMsg)
            // Light haptic tap for the per-completion celebration (much softer than milestones).
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    /**
     * Reverse the elevation award when an item is unchecked. Decrements elevation (floor 0) and
     * today's climb-tier counter. Does NOT un-earn milestones or remove the day from the rhythm
     * view (the user still engaged with the app that day; the act of unchecking is itself activity).
     */
    fun removeElevation(amount: Int) {
        elevation = (elevation - amount).coerceAtLeast(0)
        sharedPrefs.edit().putInt("elevation", elevation).apply()
        if (todayCompletions > 0) {
            todayCompletions--
            sharedPrefs.edit().putInt("todayCompletions", todayCompletions).apply()
        }
    }

    // Persistent Storage State Containers (loaded from SharedPreferences on first composition)
    val scheduleEntries = remember {
        mutableStateListOf<ScheduleEntry>().apply {
            addAll(loadList(sharedPrefs.getString("scheduleEntries", null), ::scheduleEntryFromJson))
        }
    }
    val otherTodoEntries = remember {
        mutableStateListOf<OtherTodoItem>().apply {
            addAll(loadList(sharedPrefs.getString("otherTodoEntries", null), ::otherTodoItemFromJson))
        }
    }
    val insightLogs = remember {
        mutableStateListOf<InsightLog>().apply {
            addAll(loadList(sharedPrefs.getString("insightLogs", null), ::insightLogFromJson))
        }
    }
    val goalEntries = remember {
        mutableStateListOf<GoalEntry>().apply {
            addAll(loadList(sharedPrefs.getString("goalEntries", null), ::goalEntryFromJson))
        }
    }
    // Daily guided-reflection entries (the Insights journaling feature).
    val reflectionEntries = remember {
        mutableStateListOf<ReflectionEntry>().apply {
            addAll(loadList(sharedPrefs.getString("reflectionEntries", null), ::reflectionEntryFromJson))
        }
    }
    // --- Daily reflection reminder settings ---
    // On by default at 8pm. Kept in prefs so the BroadcastReceiver (which runs outside Compose)
    // can read them when the alarm fires.
    var reflectionReminderEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("reflectionReminderEnabled", true)) }
    var reflectionReminderHour by remember { mutableStateOf(sharedPrefs.getInt("reflectionReminderHour", 20)) }
    var reflectionReminderMinute by remember { mutableStateOf(sharedPrefs.getInt("reflectionReminderMinute", 0)) }
    // (Re)schedule whenever the setting changes — and on launch, so the default-on reminder is
    // armed for existing users too. Turning it off cancels the pending alarm.
    LaunchedEffect(reflectionReminderEnabled, reflectionReminderHour, reflectionReminderMinute) {
        if (reflectionReminderEnabled) scheduleReflectionReminder(context, reflectionReminderHour, reflectionReminderMinute)
        else cancelReflectionReminder(context)
    }
    // Tapping the reflection notification lands the user on Insights, where the reflection card is
    // the first thing on the page.
    LaunchedEffect(openReflection) {
        if (openReflection) {
            currentScreen = "LogInsight"
            onReflectionOpened()
        }
    }

    val dailyGrandRewards = remember {
        mutableStateMapOf<LocalDate, String>().apply {
            putAll(grandRewardsFromJsonString(sharedPrefs.getString("dailyGrandRewards", null)))
        }
    }

    // Auto-persist any change to the collections above. snapshotFlow re-emits whenever
    // the list/map contents change; drop(1) skips the initial emission (already on disk).
    LaunchedEffect(Unit) {
        snapshotFlow { scheduleEntries.toList() }.drop(1).collect {
            sharedPrefs.edit().putString("scheduleEntries", serializeList(it) { e -> e.toJson() }).apply()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { otherTodoEntries.toList() }.drop(1).collect {
            sharedPrefs.edit().putString("otherTodoEntries", serializeList(it) { e -> e.toJson() }).apply()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { insightLogs.toList() }.drop(1).collect {
            sharedPrefs.edit().putString("insightLogs", serializeList(it) { e -> e.toJson() }).apply()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { reflectionEntries.toList() }.drop(1).collect {
            sharedPrefs.edit().putString("reflectionEntries", serializeList(it) { e -> e.toJson() }).apply()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { goalEntries.toList() }.drop(1).collect {
            sharedPrefs.edit().putString("goalEntries", serializeList(it) { e -> e.toJson() }).apply()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { dailyGrandRewards.toMap() }.drop(1).collect {
            sharedPrefs.edit().putString("dailyGrandRewards", grandRewardsToJsonString(it)).apply()
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { activityDates.toList() }.drop(1).collect {
            sharedPrefs.edit().putString("activityDates", activityDatesToJsonString(it.toSet())).apply()
        }
    }

    // Scalar values — same pattern. snapshotFlow on a captured State var emits on every change.
    LaunchedEffect(Unit) {
        snapshotFlow { elevation }.drop(1).collect { sharedPrefs.edit().putInt("elevation", it).apply() }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { points }.drop(1).collect { sharedPrefs.edit().putInt("points", it).apply() }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { wins }.drop(1).collect { sharedPrefs.edit().putInt("wins", it).apply() }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { streak }.drop(1).collect { sharedPrefs.edit().putInt("streak", it).apply() }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { freezes }.drop(1).collect { sharedPrefs.edit().putInt("freezes", it).apply() }
    }

    var showRewardSettingDialog by remember { mutableStateOf(false) }
    var showGrandRewardCelebration by remember { mutableStateOf(false) }
    var grandRewardInput by remember { mutableStateOf("") }

    // Brain Dump Wizard Reactive State
    var showBrainDumpWizard by remember { mutableStateOf(false) }
    // Chooser sheet shown by the Home FAB. Lets the user pick which kind of thing to add.
    var showAddSheet by remember { mutableStateOf(false) }
    // Drives the rotation of the + icon on the FAB and the same icon on the chooser's close button,
    // so that opening the chooser looks like a single button rotating from + to ×. Both buttons sit
    // in the same screen position; the dialog covers the FAB but uses the same icon and rotation,
    // making the cross-fade between them imperceptible.
    val fabIconRotation by animateFloatAsState(
        targetValue = if (showAddSheet) 45f else 0f,
        animationSpec = tween(240),
        label = "FabIconRotation"
    )
    // Lightweight inline to-do dialog launched from the chooser sheet (the Lists screen has its
    // own inline input; this is for adding from anywhere without navigating).
    var showQuickTodoDialog by remember { mutableStateOf(false) }
    var quickTodoText by remember { mutableStateOf("") }

    // --- FOCUS SESSION (delay-aversion chunking) ---
    // The active session, or null when none is running. The setup dialog (duration picker)
    // is gated on showFocusSetup; once started, focusSession is non-null and the full-screen
    // focus overlay takes over.
    var focusSession by remember { mutableStateOf<FocusSession?>(null) }
    var showFocusSetup by remember { mutableStateOf(false) }
    // Pre-filled task label when launching focus from a specific task (Right Now, priority, to-do).
    // Blank for a general session started from the FAB chooser.
    var focusSetupTaskLabel by remember { mutableStateOf("") }
    var focusSetupMinutes by remember { mutableStateOf(25) }
    // A micro-reward suggestion surfaced at the end of each work chunk (pulled from the small
    // reward bank). Null when no suggestion is showing.
    var focusChunkReward by remember { mutableStateOf<String?>(null) }
    var wizardStep by remember { mutableStateOf(1) }
    var wizardRawInputText by remember { mutableStateOf("") }
    val wizardBrainDumpList = remember { mutableStateListOf<WizardItem>() }
    val wizardSelectedPriorities = remember { mutableStateListOf<WizardItem>() }
    var showCarryOverActionDialog by remember { mutableStateOf(false) }
    var carryOverItemTarget by remember { mutableStateOf<OtherTodoItem?>(null) }
    var pendingWizardOtherTodoRemoval by remember { mutableStateOf<OtherTodoItem?>(null) }
    var showCompletionConfirmDialog by remember { mutableStateOf(false) }
    var priorityItemToComplete by remember { mutableStateOf<ScheduleEntry?>(null) }
    var isWizardPlacementMode by remember { mutableStateOf(false) }
    var wizardCurrentSchedulingIndex by remember { mutableStateOf(0) }
    // Set when placement mode was entered to give an EXISTING unscheduled priority a time (by
    // tapping its all-day chip). Tells the save path to update that entry rather than create a
    // second copy. Null during a normal brain-dump placement run.
    var priorityBeingScheduledId by remember { mutableStateOf<String?>(null) }
    // --- Task composer ---
    // Non-null while the new/edit window is open. Held across a trip to the schedule (see
    // composerPlacementPending) so the user's typing survives picking a time.
    var taskComposerDraft by remember { mutableStateOf<TaskDraft?>(null) }
    var composerPlacementPending by remember { mutableStateOf(false) }
    // --- Task detail window ---
    // Stored as an id (not the object) so the open window always reflects live data — ticking a
    // subtask inside it re-reads from the list rather than showing a stale snapshot.
    var taskDetailId by remember { mutableStateOf<String?>(null) }
    var taskDetailIsPriority by remember { mutableStateOf(false) }

    // Goals Builder State
    var showGoalWizardDialog by remember { mutableStateOf(false) }
    var draftGoalType by remember { mutableStateOf("WEEKLY") }
    var editingGoalId by remember { mutableStateOf<String?>(null) }
    var draftGoalTitle by remember { mutableStateOf("") }
    var draftGoalDeadline by remember { mutableStateOf<LocalDate?>(null) }
    var draftGoalReward by remember { mutableStateOf("") }
    // Episodic future thinking: a vivid sensory picture of finishing the goal. Re-surfaced when
    // the user engages with the goal, to reduce delay discounting (Bickel 2019).
    var draftGoalVision by remember { mutableStateOf("") }
    val draftGoalSteps = remember { mutableStateListOf<GoalStep>() }
    var draftNewStepText by remember { mutableStateOf("") }
    var activeDeadlineSelectionId by remember { mutableStateOf<String?>("goal_root") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Navigation and Calendar Tracking
    // todayTick increments at each midnight boundary so anything derived from LocalDate.now()
    // (the "return to today" button, isToday highlight, which page is "today") refreshes when
    // the date rolls over while the app stays in memory. Without it, values captured at launch
    // go stale after midnight — which caused the "June 1 reads as last week of May" bug.
    var todayTick by remember { mutableStateOf(0) }
    val today = run { todayTick; LocalDate.now() }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedGoalWeek by remember { mutableStateOf(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))) }
    var selectedGoalMonth by remember { mutableStateOf(YearMonth.now()) }
    var isWeeklyGoalsExpanded by remember { mutableStateOf(true) }
    var isMonthlyGoalsExpanded by remember { mutableStateOf(true) }
    var goalWeekSwipeAccumulator by remember { mutableStateOf(0f) }
    var goalMonthSwipeAccumulator by remember { mutableStateOf(0f) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    var pickerMonthState by remember { mutableStateOf(YearMonth.now()) }

    // Fixed reference Monday — a constant, NOT derived from today, so week math never goes stale.
    // Page 5000 is arbitrary headroom; the page for any given week is computed relative to this
    // fixed anchor. "Today's page" is therefore always recomputed from the live date.
    val baseMondayAnchor = remember { LocalDate.of(2020, 1, 6) } // a Monday
    // Helper: which pager page corresponds to the week containing the given date.
    fun pageForDate(d: LocalDate): Int {
        val mon = d.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return 5000 + ChronoUnit.WEEKS.between(baseMondayAnchor, mon).toInt()
    }
    // The page representing the current week. Recomputed live (depends on `today`), so it
    // tracks date rollovers. Replaces the old fixed `initialPageConstant` as "today's page".
    val initialPageConstant = run { todayTick; pageForDate(LocalDate.now()) }
    val pagerState = rememberPagerState(initialPage = pageForDate(LocalDate.now())) { 10000 }

    // Midnight roller — sleeps until just past the next midnight, then bumps todayTick so all
    // date-derived UI refreshes. Keeps a long-lived process correct across day boundaries.
    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalDateTime.now()
            val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
            val millisUntil = java.time.Duration.between(now, nextMidnight).toMillis().coerceAtLeast(1000L)
            delay(millisUntil + 1000L)
            todayTick++
        }
    }

    // When the date rolls over, snap the goal views to the new current week/month so the user
    // always lands on "now" rather than a stale week/month captured at launch. (Manual swiping
    // during the same day still works — this only fires on an actual day change.)
    LaunchedEffect(todayTick) {
        if (todayTick > 0) {
            selectedGoalWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            selectedGoalMonth = YearMonth.now()
        }
    }

    // --- DEVICE CALENDAR (read-only) ---
    // showDeviceCalendar is the user's opt-in toggle (persisted). calendarPermissionGranted is
    // re-checked live (not persisted — the user can revoke it in system settings). externalEvents
    // holds the queried events for the currently-shown day; it's transient and never persisted, so
    // device events can't leak into the app's own data.
    var showDeviceCalendar by remember { mutableStateOf(sharedPrefs.getBoolean("showDeviceCalendar", false)) }
    var calendarPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
        )
    }
    var externalEvents by remember { mutableStateOf<List<ExternalCalendarEvent>>(emptyList()) }
    var externalEventToInspect by remember { mutableStateOf<ExternalCalendarEvent?>(null) }
    // Which calendars the user has hidden (by ID, stored as strings). Empty = show all — so the
    // default is unchanged for anyone who never opens the picker. availableCalendars is the live
    // list of calendars on the device (transient, re-queried when the calendar feature is on).
    var hiddenCalendarIds by remember {
        mutableStateOf(sharedPrefs.getStringSet("hiddenCalendarIds", emptySet())?.toSet() ?: emptySet())
    }
    var availableCalendars by remember { mutableStateOf<List<DeviceCalendar>>(emptyList()) }
    // Bumping this key forces the calendar to re-read. The manual refresh button increments it, and
    // it's also incremented when the app returns to the foreground (below), so calendar edits made
    // in another app show up when you come back.
    var calendarRefreshKey by remember { mutableStateOf(0) }
    val lifecycleOwner = context as? LifecycleOwner
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) calendarRefreshKey++
        }
        lifecycleOwner?.lifecycle?.addObserver(observer)
        onDispose { lifecycleOwner?.lifecycle?.removeObserver(observer) }
    }
    // Permission launcher — toggling the setting on requests READ_CALENDAR; the callback records
    // the result so the query effect can run (or the setting can fall back to off if denied).
    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        calendarPermissionGranted = granted
        if (granted) {
            showDeviceCalendar = true
            sharedPrefs.edit().putBoolean("showDeviceCalendar", true).apply()
        } else {
            showDeviceCalendar = false
            sharedPrefs.edit().putBoolean("showDeviceCalendar", false).apply()
        }
    }
    // Query the device calendar for the selected day whenever the toggle, permission, date, or the
    // set of hidden calendars changes. Runs the content-provider read off the main thread.
    LaunchedEffect(selectedDate, showDeviceCalendar, calendarPermissionGranted, todayTick, hiddenCalendarIds, calendarRefreshKey) {
        externalEvents = if (showDeviceCalendar && calendarPermissionGranted) {
            val hidden = hiddenCalendarIds.mapNotNull { it.toLongOrNull() }.toSet()
            withContext(Dispatchers.IO) { queryDeviceCalendarEvents(context, selectedDate, hidden) }
        } else {
            emptyList()
        }
    }

    // Load the list of calendars on the device whenever the feature is on and permitted, so the
    // settings picker can show them.
    LaunchedEffect(showDeviceCalendar, calendarPermissionGranted, calendarRefreshKey) {
        availableCalendars = if (showDeviceCalendar && calendarPermissionGranted) {
            withContext(Dispatchers.IO) { queryDeviceCalendars(context) }
        } else {
            emptyList()
        }
    }

    // --- Backup / restore launchers (Storage Access Framework — no storage permissions needed) ---
    // Export: stash the snapshot JSON, then let the user pick where to save it; write on return.
    var pendingBackupJson by remember { mutableStateOf<String?>(null) }
    val backupExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        val json = pendingBackupJson
        if (uri != null && json != null) {
            val ok = try {
                context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                true
            } catch (e: Exception) { false }
            Toast.makeText(context, if (ok) "Backup saved" else "Couldn't save the backup", Toast.LENGTH_LONG).show()
        }
        pendingBackupJson = null
    }
    // Restore: read the chosen file, then hold it pending an explicit confirm before overwriting.
    var pendingRestoreJson by remember { mutableStateOf<String?>(null) }
    val backupImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val text = try {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes().toString(Charsets.UTF_8) }
            } catch (e: Exception) { null }
            if (text != null) pendingRestoreJson = text
            else Toast.makeText(context, "Couldn't read that file", Toast.LENGTH_LONG).show()
        }
    }


    val hourRowHeightDp = 67.5f
    val minuteScaleFactor = 1.125f
    val timelineScrollState = rememberScrollState()
    val density = LocalDensity.current

    // Each top card is condensed by default and expands only on an explicit tap. Scrolling to
    // later times auto-condenses both; scrolling to earlier times no longer expands them.
    var priorityExpanded by remember { mutableStateOf(false) }
    var scoreboardExpanded by remember { mutableStateOf(false) }
    var accumulatedDelta by remember { mutableStateOf(0f) }
    var horizontalSwipeAccumulator by remember { mutableStateOf(0f) }

    val twoHoursPx = with(density) { (2 * hourRowHeightDp).dp.toPx() }
    // Auto-condense after ~1h of downward (toward-later) scroll.
    val condenseDistancePx = twoHoursPx / 2f

    // Scroll the daily schedule to the current time on app open,
    // leaving ~1 hour of context above so the user sees recent events too.
    LaunchedEffect(Unit) {
        val now = LocalTime.now()
        val minutesFromMidnight = now.hour * 60 + now.minute
        val targetDp = ((minutesFromMidnight - 60) * minuteScaleFactor).coerceAtLeast(0f)
        val targetPx = with(density) { targetDp.dp.toPx() }
        timelineScrollState.scrollTo(targetPx.toInt())
    }

    // Drive auto-condense from schedule scroll. Scrolling toward LATER times accumulates toward a
    // condense; scrolling toward EARLIER times bleeds the accumulator (clamped at 0) but never
    // expands. The cards only ever open via an explicit tap now.
    LaunchedEffect(Unit) {
        var prevScroll: Int? = null
        snapshotFlow { timelineScrollState.value }.collect { newPos ->
            val prev = prevScroll
            prevScroll = newPos
            if (prev == null) return@collect // first emission: just record and wait
            val delta = (newPos - prev).toFloat() // positive when scrolling toward later time
            accumulatedDelta = (accumulatedDelta + delta).coerceAtLeast(0f)
            if (accumulatedDelta >= condenseDistancePx) {
                if (priorityExpanded) priorityExpanded = false
                if (scoreboardExpanded) scoreboardExpanded = false
                accumulatedDelta = 0f
            }
        }
    }

    var showScheduleDialog by remember { mutableStateOf(false) }
    var activeInspectEntry by remember { mutableStateOf<ScheduleEntry?>(null) }
    // Holds an entry the user has tapped "Delete Event" on, pending confirmation.
    // Separate from activeInspectEntry so the inspect dialog stays open behind the
    // confirmation, restoring on Cancel without losing the user's place.
    var entryPendingDelete by remember { mutableStateOf<ScheduleEntry?>(null) }
    // To-do and goal delete confirmations. Same pattern as entryPendingDelete: setting the
    // value pops an AlertDialog; the dialog's confirm button performs the actual removal.
    var todoPendingDelete by remember { mutableStateOf<OtherTodoItem?>(null) }
    // Confirm before marking a to-do complete (prevents accidental one-tap completion).
    var todoPendingComplete by remember { mutableStateOf<OtherTodoItem?>(null) }
    // Which to-dos are expanded to show their subtask list (by id).
    val expandedTodoIds = remember { mutableStateListOf<String>() }
    // When you tap a to-do on the schedule, we jump to Lists and briefly highlight + scroll to it.
    var highlightedTodoId by remember { mutableStateOf<String?>(null) }
    val todoBringIntoView = remember { BringIntoViewRequester() }
    LaunchedEffect(highlightedTodoId) {
        if (highlightedTodoId != null) {
            delay(180) // let the Lists screen compose and lay out the target row
            runCatching { todoBringIntoView.bringIntoView() }
            delay(2500)
            highlightedTodoId = null
        }
    }
    // When the last subtask of a to-do gets checked, offer to complete the parent too.
    var subtaskCompletionPrompt by remember { mutableStateOf<OtherTodoItem?>(null) }
    var goalPendingDelete by remember { mutableStateOf<GoalEntry?>(null) }
    var editingEntry by remember { mutableStateOf<ScheduleEntry?>(null) }

    var dialogEventName by remember { mutableStateOf("") }
    var dialogEventNotes by remember { mutableStateOf("") }
    var dialogStartHour by remember { mutableStateOf("9") }
    var dialogStartMin by remember { mutableStateOf("00") }
    var dialogStartAmPm by remember { mutableStateOf("AM") }
    var dialogTimeMode by remember { mutableStateOf("DURATION") }
    var dialogDurationMins by remember { mutableStateOf("60") }
    var dialogEndHour by remember { mutableStateOf("10") }
    var dialogEndMin by remember { mutableStateOf("00") }
    var dialogEndAmPm by remember { mutableStateOf("AM") }
    var dialogSelectedColor by remember { mutableStateOf(PureWhite) }
    var showCustomColorPicker by remember { mutableStateOf(false) }
    var dialogIsAllDay by remember { mutableStateOf(false) }
    // Reminder offset (minutes before start). null = no reminder. 0 = at start time.
    var dialogReminderMinutesBefore by remember { mutableStateOf<Int?>(null) }
    // "When I [cue]..." implementation intention. Optional. Helps with task initiation by
    // attaching the task to a contextual cue (Gawrilow research on if-then planning).
    var dialogTriggerCue by remember { mutableStateOf("") }

    // Focus-session ticker. Runs once; whenever a session is active and not paused, it
    // decrements secondsLeft each second. On hitting zero it transitions phases:
    //   WORK done   → award elevation + a chunk counter bump, surface a micro-reward suggestion
    //                 from the small bank, then start a 5-minute BREAK.
    //   BREAK done  → start the next WORK chunk.
    // The session keeps going until the user explicitly stops it (no fixed chunk cap — the user
    // decides when they're done, which respects autonomy and avoids an arbitrary endpoint).
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val s = focusSession ?: continue
            if (s.isPaused) continue
            if (s.secondsLeft > 1) {
                focusSession = s.copy(secondsLeft = s.secondsLeft - 1)
            } else {
                // Phase boundary reached.
                if (s.phase == FOCUS_PHASE_WORK) {
                    // Chunk complete: reward + advance to break.
                    awardElevation(ELEVATION_TODO, "Focus chunk done · +$ELEVATION_TODO ft", silent = true)
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    // Surface a small-reward suggestion for the break, if the bank has any.
                    focusChunkReward = rewardBank.small.randomOrNull()
                    focusSession = s.copy(
                        phase = FOCUS_PHASE_BREAK,
                        secondsLeft = FOCUS_BREAK_MINUTES * 60,
                        chunksCompleted = s.chunksCompleted + 1
                    )
                } else {
                    // Break complete: clear the reward suggestion, start the next work chunk.
                    focusChunkReward = null
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    focusSession = s.copy(
                        phase = FOCUS_PHASE_WORK,
                        secondsLeft = s.workMinutes * 60
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        bottomBar = {
            // M3 NavigationBar — handles ripple, selection animation (the indicator pill fills
            // in smoothly when tapped), system-gesture inset padding, and semantics natively.
            // The dark-bar look is preserved by overriding containerColor + itemColors instead
            // of styling each tab manually.
            val navigationDestinations = listOf(
                Triple("Home", Icons.Outlined.CalendarToday, "Schedule"),
                Triple("Lists", Icons.Outlined.Checklist, "Lists"),
                Triple("Goals", Icons.Outlined.Flag, "Goals"),
                Triple("LogInsight", Icons.Outlined.Lightbulb, "Insight")
            )
            NavigationBar(
                containerColor = PureWhite,
                contentColor = Navy,
                tonalElevation = 0.dp
            ) {
                navigationDestinations.forEach { (screen, icon, label) ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                // Selected items have no label, so the icon grows to fill the
                                // freed vertical space — keeps the item visually balanced
                                // instead of leaving an empty spot below the icon.
                                modifier = Modifier.size(if (isSelected) 30.dp else 22.dp)
                            )
                        },
                        // Passing label = null on selected items tells NavigationBarItem to
                        // center the icon vertically. Unselected items still get their label.
                        label = if (isSelected) null else {
                            {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            // When selected: white icon on a sky-blue pill (the interactive color
                            // marks the active tab).
                            selectedIconColor = PureWhite,
                            selectedTextColor = PureWhite,
                            indicatorColor = RidgelineBlue,
                            // When unselected: navy icon/label on the light ice bar.
                            unselectedIconColor = Navy,
                            unselectedTextColor = Navy
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            // Use AnimatedContent so only one FAB is in the slot at a time. With two
            // sibling AnimatedVisibility blocks, the slot's layout briefly measured both
            // during the transition, which caused the "+" to flash centered before snapping
            // to the right. A single AnimatedContent child keeps measurement stable.
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "FabSwitch"
            ) { screen ->
                when (screen) {
                    "Home" -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Today return chip — visible only when not on today's date OR the week
                            // pager has been swiped away from the current week. Tapping resets both.
                            AnimatedVisibility(
                                visible = selectedDate != today || pagerState.currentPage != initialPageConstant,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                Button(
                                    onClick = {
                                        selectedDate = today
                                        coroutineScope.launch { pagerState.animateScrollToPage(initialPageConstant) }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    // Date number is more glanceable than the word "Today" at this size.
                                    Text(
                                        text = LocalDate.now().dayOfMonth.toString(),
                                        color = PureWhite,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            Button(
                                onClick = { showAddSheet = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Open add menu",
                                    tint = PureWhite,
                                    modifier = Modifier.size(28.dp).rotate(fabIconRotation)
                                )
                            }
                        }
                    }
                    "Lists" -> {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AnimatedVisibility(
                                visible = selectedDate != today || pagerState.currentPage != initialPageConstant,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                Button(
                                    onClick = {
                                        selectedDate = today
                                        coroutineScope.launch { pagerState.animateScrollToPage(initialPageConstant) }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Text(
                                        text = LocalDate.now().dayOfMonth.toString(),
                                        color = PureWhite,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    wizardStep = 1
                                    wizardRawInputText = ""
                                    wizardBrainDumpList.clear()
                                    wizardSelectedPriorities.clear()
                                    wizardCurrentSchedulingIndex = 0
                                    isWizardPlacementMode = false
                                    showBrainDumpWizard = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                                modifier = Modifier.height(54.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Outlined.Psychology, contentDescription = null, tint = PureWhite, modifier = Modifier.size(20.dp))
                                    Text(
                                        text = "Brain Dump",
                                        color = PureWhite,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // Other screens: no FAB. Spacer keeps the slot's intrinsic size
                        // consistent across screens so the bottom bar layout doesn't shift.
                        Spacer(modifier = Modifier.size(54.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(SkyGray)
        ) {
            when (currentScreen) {
                "Home" -> {
                    // Minute ticker — drives a recomposition every 60 seconds so the Right Now
                    // card recomputes which item is "in progress" vs "next up". Without this,
                    // pickRightNowItem only re-runs when its inputs change, so a stale value of
                    // LocalDateTime.now() can leave the card showing "next up · 2:30 PM" long after
                    // 2:30 has passed. The ticker increments a counter we feed into remember(...).
                    var minuteTick by remember { mutableStateOf(0) }
                    LaunchedEffect(Unit) {
                        while (true) {
                            // Sleep until the start of the next minute (so transitions happen
                            // close to the actual minute boundary, not at an arbitrary offset).
                            val now = LocalDateTime.now()
                            val secondsUntilNextMinute = 60 - now.second
                            kotlinx.coroutines.delay(secondsUntilNextMinute * 1000L)
                            minuteTick++
                        }
                    }
                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 4.dp)) {
                        AnimatedVisibility(visible = isWizardPlacementMode) {
                            val activePlacementItem = wizardSelectedPriorities.getOrNull(wizardCurrentSchedulingIndex)
                            if (activePlacementItem != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = IceBlueAccent),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).border(2.dp, RidgelineBlue, RoundedCornerShape(12.dp))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (composerPlacementPending) "Pick a time" else "Scheduling Priority ${wizardCurrentSchedulingIndex + 1} of ${wizardSelectedPriorities.size}",
                                                style = MaterialTheme.typography.labelMedium, color = RidgelineBlue
                                            )
                                            Text(text = "Placing: \"${activePlacementItem.text}\"", fontSize = 15.sp, fontWeight = FontWeight.Black, color = MidnightSlate)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Tap any time slot below — or skip if you don't know when yet.", fontSize = 12.sp, color = MidnightSlate.copy(alpha = 0.7f))
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            // Skip = keep the item, drop the "when".
                                            TextButton(onClick = {
                                                val label = activePlacementItem.text
                                                if (composerPlacementPending) {
                                                    // Return to the composer with no time set.
                                                    composerPlacementPending = false
                                                    isWizardPlacementMode = false
                                                    wizardSelectedPriorities.clear()
                                                    currentScreen = "Lists"
                                                } else if (priorityBeingScheduledId != null) {
                                                    // Was placing an existing priority — leave it unscheduled.
                                                    isWizardPlacementMode = false
                                                    wizardCurrentSchedulingIndex = 0
                                                    wizardSelectedPriorities.clear()
                                                    priorityBeingScheduledId = null
                                                } else {
                                                    scheduleEntries.add(
                                                        ScheduleEntry(
                                                            defaultSlotLabel = "",
                                                            task = label,
                                                            isTopPriority = true,
                                                            hasCustomTime = false,
                                                            date = selectedDate
                                                        )
                                                    )
                                                    if (pendingWizardOtherTodoRemoval != null) {
                                                        otherTodoEntries.removeAll { it.id == pendingWizardOtherTodoRemoval!!.id }
                                                        pendingWizardOtherTodoRemoval = null
                                                    }
                                                    if (wizardCurrentSchedulingIndex + 1 < wizardSelectedPriorities.size) {
                                                        wizardCurrentSchedulingIndex++
                                                    } else {
                                                        isWizardPlacementMode = false
                                                        wizardCurrentSchedulingIndex = 0
                                                        val cnt = scheduleEntries.count { it.date == selectedDate && it.isTopPriority }
                                                        if (cnt > 0) showRewardSettingDialog = true else currentScreen = "Lists"
                                                    }
                                                    recordActivity()
                                                }
                                            }) {
                                                Text("Skip", color = RidgelineBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                            TextButton(onClick = {
                                                if (composerPlacementPending) {
                                                    composerPlacementPending = false
                                                    isWizardPlacementMode = false
                                                    wizardSelectedPriorities.clear()
                                                    currentScreen = "Lists"
                                                } else {
                                                    isWizardPlacementMode = false
                                                    pendingWizardOtherTodoRemoval = null
                                                    priorityBeingScheduledId = null
                                                    wizardSelectedPriorities.clear()
                                                }
                                            }) {
                                                Text("Cancel", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // "Right Now" card: surfaces the next priority or to-do so the user doesn't
                        // have to scan the day to decide what to do. Compact, single-row layout —
                        // stays present even while scrolling the schedule (the card itself doesn't
                        // condense; only the scoreboard below does). Hidden in wizard mode only.
                        if (!isWizardPlacementMode) {
                            val rightNow = remember(scheduleEntries.toList(), otherTodoEntries.toList(), minuteTick) {
                                pickRightNowItem(scheduleEntries, otherTodoEntries)
                            }
                            // The date the surfaced item lives on — used to sync Lists when tapped.
                            val today = LocalDate.now()
                            val rightNowDate: LocalDate? = when (rightNow) {
                                is RightNowItem.Priority -> rightNow.entry.date
                                is RightNowItem.Event -> rightNow.entry.date
                                // A carried-over to-do has a past date but appears in today's
                                // carried-over section — send the user to today, not the old date.
                                is RightNowItem.Todo -> if (rightNow.todo.date.isBefore(today)) today else rightNow.todo.date
                                is RightNowItem.AllClear -> null
                            }
                            // Compact sub-line that combines status + days-over for to-dos,
                            // or status label for events. Empty if there's nothing to add.
                            val subLine: String = when (rightNow) {
                                is RightNowItem.Event -> rightNow.statusLabel
                                is RightNowItem.Priority -> if (rightNow.entry.hasCustomTime) formatTimeLabel(rightNow.entry.startHour, rightNow.entry.startMinute) else ""
                                is RightNowItem.Todo -> {
                                    val daysOver = ChronoUnit.DAYS.between(rightNow.todo.date, today)
                                    when {
                                        daysOver <= 0L -> ""
                                        daysOver == 1L -> "From yesterday"
                                        else -> "From $daysOver days ago"
                                    }
                                }
                                else -> ""
                            }
                            val eyebrow: String = when (rightNow) {
                                is RightNowItem.Priority -> "TOP PRIORITY"
                                is RightNowItem.Todo -> if (rightNow.todo.date.isBefore(today)) "CARRIED OVER" else "NEXT TO-DO"
                                is RightNowItem.Event -> "ON YOUR SCHEDULE"
                                is RightNowItem.AllClear -> "ALL CLEAR"
                            }
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(24.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { priorityExpanded = !priorityExpanded }
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = if (!priorityExpanded) 6.dp else 8.dp)) {
                                    // Title row: an inline eyebrow pill in front of the task text,
                                    // both on the same line. Saves the previous standalone eyebrow row.
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(PureWhite.copy(alpha = 0.18f))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = eyebrow,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = PureWhite
                                            )
                                        }
                                        Text(
                                            text = if (rightNow is RightNowItem.AllClear) "🎉 Deck cleared!" else rightNow.task,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = PureWhite,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        // Chevron signals the card expands/condenses on tap.
                                        if (rightNow !is RightNowItem.AllClear) {
                                            Icon(
                                                imageVector = if (priorityExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                                contentDescription = if (priorityExpanded) "Collapse" else "Expand",
                                                tint = PureWhite.copy(alpha = 0.9f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    // Expanded-only detail: the supplemental sub-row plus a button
                                    // to open this item on the Lists page. Condensed by default —
                                    // the eyebrow + task line above always shows so the card still
                                    // tells you what's next at a glance.
                                    if (rightNow !is RightNowItem.AllClear && priorityExpanded) {
                                        // Supplemental sub-row, joined by middots — same as before.
                                        val cue = when (rightNow) {
                                            is RightNowItem.Priority -> rightNow.entry.triggerCue
                                            is RightNowItem.Event -> rightNow.entry.triggerCue
                                            is RightNowItem.Todo -> rightNow.todo.triggerCue
                                            else -> ""
                                        }
                                        val pieces = buildList {
                                            if (subLine.isNotBlank()) add(subLine)
                                            if (rightNow.reward.isNotBlank()) add("🎁 ${rightNow.reward}")
                                            if (cue.isNotBlank()) add("When I ${cue}…")
                                        }
                                        if (pieces.isNotEmpty()) {
                                            Text(
                                                text = pieces.joinToString("  ·  "),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = PureWhite.copy(alpha = 0.85f),
                                                maxLines = 1,
                                                modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                                            )
                                        }
                                        // "View in Lists" — jumps to the Lists page for this item's
                                        // date. Its own clickable, so it navigates instead of just
                                        // toggling the card.
                                        if (rightNowDate != null) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(50))
                                                    .background(PureWhite.copy(alpha = 0.18f))
                                                    .clickable {
                                                        rightNowDate?.let { date ->
                                                            selectedDate = date
                                                            val targetMon = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                                            val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                                            coroutineScope.launch { pagerState.animateScrollToPage(5000 + weeksDiff) }
                                                            currentScreen = "Lists"
                                                        }
                                                    }
                                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(imageVector = Icons.Outlined.Checklist, contentDescription = null, tint = PureWhite, modifier = Modifier.size(15.dp))
                                                Text("View in Lists", style = MaterialTheme.typography.labelMedium, color = PureWhite, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        ScoreboardSection(
                            elevation = elevation, todayCompletions = todayCompletions, activityDates = activityDates.toSet(),
                            isCondensed = !scoreboardExpanded,
                            onToggle = { scoreboardExpanded = !scoreboardExpanded }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Single row: tappable month label on the left, the week-day strip
                        // flowing to its right inside a HorizontalPager. The 3-letter month
                        // abbreviation keeps the left side width predictable across months.
                        // The Today return button has moved next to the FAB (see below).
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            val showYear = selectedDate.year != LocalDate.now().year
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable {
                                        pickerMonthState = YearMonth.from(selectedDate)
                                        showMonthDropdown = !showMonthDropdown
                                    }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = selectedDate.month.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault()) + (if (showYear) " ${selectedDate.year}" else ""),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MidnightSlate
                                )
                                Icon(
                                    imageVector = Icons.Outlined.ArrowDropDown,
                                    contentDescription = "Open calendar",
                                    tint = MidnightSlate,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            // Manual calendar refresh — only shown when the device-calendar feature
                            // is active. Re-reads immediately for events added/changed in another app.
                            if (showDeviceCalendar && calendarPermissionGranted) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            calendarRefreshKey++
                                            Toast.makeText(context, "Calendar refreshed", Toast.LENGTH_SHORT).show()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Refresh,
                                        contentDescription = "Refresh calendar",
                                        tint = RidgelineBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            // Week-day strip: HorizontalPager takes the remaining width.
                            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                                val weekMonday = baseMondayAnchor.plusWeeks((page - 5000).toLong())
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                    for (dayOffset in 0..6) {
                                        val iteratingDate = weekMonday.plusDays(dayOffset.toLong())
                                        WeekStripDay(
                                            date = iteratingDate,
                                            isSelected = iteratingDate == selectedDate,
                                            isToday = iteratingDate == LocalDate.now(),
                                            onClick = { selectedDate = iteratingDate }
                                        )
                                    }
                                }
                            }
                        }

                        // Inline dropdown calendar — drops down below the row above when opened.
                        AnimatedVisibility(visible = showMonthDropdown) {
                            DropdownCalendar(
                                monthState = pickerMonthState,
                                selectedDate = selectedDate,
                                onMonthChange = { pickerMonthState = it },
                                onDayPick = { picked ->
                                    selectedDate = picked
                                    val targetMon = picked.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                    val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                    coroutineScope.launch { pagerState.scrollToPage(5000 + weeksDiff) }
                                    showMonthDropdown = false
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Spacer(modifier = Modifier.height(10.dp))

                        // All-Day events bar. Shows entries flagged isAllDay for the selected date
                        // as small chips that tap to open the standard inspect dialog. Only renders
                        // when at least one all-day item exists, to avoid wasted vertical space.
                        val allDayForSelected = scheduleEntries.filter { it.date == selectedDate && it.isAllDay }
                        val externalAllDay = externalEvents.filter { it.isAllDay }
                        // To-dos due on this day with no specific time show as all-day chips.
                        val todoAllDay = otherTodoEntries.filter { !it.isCompleted && it.dueDate == selectedDate && it.dueTime == null }
                        // Priorities with no time yet — they matter today but aren't placed on the
                        // timeline. Tapping one starts placement so it can be scheduled.
                        val unscheduledPriorities = scheduleEntries.filter { it.date == selectedDate && !it.isAllDay && !it.hasCustomTime && it.isTopPriority }
                        if (allDayForSelected.isNotEmpty() || externalAllDay.isNotEmpty() || todoAllDay.isNotEmpty() || unscheduledPriorities.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp)
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Unscheduled priorities lead the bar — they're the day's headline items.
                                // Tapping one drops into placement mode so it can get a time.
                                unscheduledPriorities.forEach { p ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = if (p.isCompleted) RidgelineBlue.copy(alpha = 0.45f) else RidgelineBlue),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.clickable {
                                            // Reuse the wizard's tap-a-slot placement for this one item.
                                            wizardSelectedPriorities.clear()
                                            wizardSelectedPriorities.add(WizardItem(p.task))
                                            wizardCurrentSchedulingIndex = 0
                                            priorityBeingScheduledId = p.id
                                            isWizardPlacementMode = true
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (p.isCompleted) {
                                                Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Priority", tint = PureWhite, modifier = Modifier.size(13.dp))
                                            } else {
                                                Box(modifier = Modifier.size(12.dp).clip(CircleShape).border(1.5.dp, PureWhite, CircleShape))
                                            }
                                            Text(
                                                text = p.task,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PureWhite,
                                                textDecoration = if (p.isCompleted) TextDecoration.LineThrough else null
                                            )
                                        }
                                    }
                                }
                                allDayForSelected.forEach { entry ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = entry.blockColor),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.clickable { activeInspectEntry = entry }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(imageVector = Icons.Outlined.PushPin, contentDescription = "All-day", tint = MidnightSlate, modifier = Modifier.size(13.dp))
                                            Text(
                                                text = entry.task,
                                                fontSize = 12.sp,
                                                fontWeight = if (entry.isCompleted) FontWeight.Normal else FontWeight.Bold,
                                                color = MidnightSlate,
                                                textDecoration = if (entry.isCompleted) TextDecoration.LineThrough else null
                                            )
                                        }
                                    }
                                }
                                // External all-day events — read-only, calendar-icon chips.
                                externalAllDay.forEach { ext ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MistBlue.copy(alpha = 0.3f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.clickable { externalEventToInspect = ext }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(imageVector = Icons.Outlined.Event, contentDescription = "From your calendar", tint = RidgelineBlue, modifier = Modifier.size(13.dp))
                                            Text(
                                                text = ext.title,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = MidnightSlate
                                            )
                                        }
                                    }
                                }
                                // To-do items due today (no time) — on-brand blue chips that jump to Lists.
                                todoAllDay.forEach { td ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = RidgelineBlue),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.clickable {
                                            highlightedTodoId = td.id
                                            selectedDate = td.dueDate ?: selectedDate
                                            currentScreen = "Lists"
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(imageVector = Icons.Outlined.Checklist, contentDescription = "To-do due today", tint = PureWhite, modifier = Modifier.size(13.dp))
                                            Text(
                                                text = td.text,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PureWhite
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f).fillMaxWidth()
                                .pointerInput(selectedDate) {
                                    detectHorizontalDragGestures(
                                        onDragStart = { _: Offset -> horizontalSwipeAccumulator = 0f },
                                        onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                                            change.consume()
                                            horizontalSwipeAccumulator += dragAmount
                                        },
                                        onDragEnd = {
                                            val minimumSwipeThresholdPx = 140f
                                            if (horizontalSwipeAccumulator < -minimumSwipeThresholdPx) {
                                                val nextDay = selectedDate.plusDays(1)
                                                selectedDate = nextDay
                                                val targetMon = nextDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                                val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                                coroutineScope.launch { pagerState.animateScrollToPage(5000 + weeksDiff) }
                                            } else if (horizontalSwipeAccumulator > minimumSwipeThresholdPx) {
                                                val previousDay = selectedDate.minusDays(1)
                                                selectedDate = previousDay
                                                val targetMon = previousDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                                val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                                coroutineScope.launch { pagerState.animateScrollToPage(5000 + weeksDiff) }
                                            }
                                        }
                                    )
                                }
                                .verticalScroll(timelineScrollState)
                        ) {
                            AnimatedContent(
                                targetState = selectedDate,
                                transitionSpec = {
                                    if (targetState.isAfter(initialState)) {
                                        slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                                    } else {
                                        slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                                    }
                                },
                                label = "ScheduleDayTransition"
                            ) { targetDate ->
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        for (hour in 0..23) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().height(hourRowHeightDp.dp).pointerInput(hour) {
                                                    detectTapGestures { offset ->
                                                        // Snap to :00 (upper half) or :30 (lower half) of the row.
                                                        val rowHeightPx = hourRowHeightDp.dp.toPx()
                                                        val snappedMinute = if (offset.y < rowHeightPx / 2f) 0 else 30

                                                        // Composer is waiting on a time: write it back into the
                                                        // draft and return to the window rather than opening the
                                                        // event dialog.
                                                        if (composerPlacementPending) {
                                                            taskComposerDraft?.let { d ->
                                                                taskComposerDraft = d.copy(
                                                                    dueDate = targetDate,
                                                                    dueTime = LocalTime.of(hour, snappedMinute)
                                                                )
                                                            }
                                                            composerPlacementPending = false
                                                            isWizardPlacementMode = false
                                                            wizardSelectedPriorities.clear()
                                                            currentScreen = "Lists"
                                                            return@detectTapGestures
                                                        }

                                                        editingEntry = null
                                                        val h12 = if (hour == 0 || hour == 12) 12 else if (hour > 12) hour - 12 else hour
                                                        dialogStartHour = h12.toString()
                                                        dialogStartMin = String.format(Locale.US, "%02d", snappedMinute)
                                                        dialogStartAmPm = if (hour < 12) "AM" else "PM"

                                                        if (isWizardPlacementMode && wizardSelectedPriorities.isNotEmpty()) {
                                                            val activeItem = wizardSelectedPriorities.getOrNull(wizardCurrentSchedulingIndex)
                                                            dialogEventName = activeItem?.text ?: ""
                                                            dialogEventNotes = "Brain Dump Priority Goal Block"
                                                            dialogSelectedColor = SkyMid
                                                            dialogDurationMins = "45"
                                                        } else {
                                                            dialogEventName = ""
                                                            dialogEventNotes = ""
                                                            dialogSelectedColor = PureWhite
                                                            dialogDurationMins = "60"
                                                        }
                                                        dialogIsAllDay = false
                                                        dialogReminderMinutesBefore = null

                                                        dialogTriggerCue = ""

                                                        dialogTimeMode = "DURATION"
                                                        // End-time defaults to one hour after the snapped start; rolls over at midnight.
                                                        val endTotalMin = hour * 60 + snappedMinute + 60
                                                        val endHourCalc = (endTotalMin / 60) % 24
                                                        val endMinCalc = endTotalMin % 60
                                                        val eh12 = if (endHourCalc == 0 || endHourCalc == 12) 12 else if (endHourCalc > 12) endHourCalc - 12 else endHourCalc
                                                        dialogEndHour = eh12.toString()
                                                        dialogEndMin = String.format(Locale.US, "%02d", endMinCalc)
                                                        dialogEndAmPm = if (endHourCalc < 12) "AM" else "PM"

                                                        showScheduleDialog = true
                                                    }
                                                },
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(text = formatTimeLabel(hour, 0), style = MaterialTheme.typography.labelMedium, color = MidnightSlate.copy(alpha = 0.5f), modifier = Modifier.width(65.dp).padding(top = 4.dp), textAlign = TextAlign.End)
                                                Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(start = 16.dp)) { HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 1.dp, modifier = Modifier.align(Alignment.TopStart)) }
                                            }
                                        }
                                    }

                                    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(start = 81.dp, end = 4.dp)) {
                                        val fullWidth = maxWidth
                                        // App entries for the day, plus device-calendar events converted to
                                        // display-only ScheduleEntry objects (id prefixed "ext_"). The external
                                        // ones are folded into the same layout so overlap column-packing handles
                                        // them, but they never touch the persisted scheduleEntries list. Only
                                        // merged when the shown day matches the queried day.
                                        val externalTimedAsEntries = if (targetDate == selectedDate) {
                                            externalEvents.filter { !it.isAllDay }.map { ext ->
                                                val zdt = Instant.ofEpochMilli(ext.startMillis).atZone(ZoneId.systemDefault())
                                                val dur = ((ext.endMillis - ext.startMillis) / 60000L).toInt().coerceIn(15, 1440)
                                                ScheduleEntry(
                                                    defaultSlotLabel = "",
                                                    task = ext.title,
                                                    isTopPriority = false,
                                                    blockColor = MistBlue.copy(alpha = 0.35f),
                                                    startHour = zdt.hour,
                                                    startMinute = zdt.minute,
                                                    durationMins = dur,
                                                    date = targetDate,
                                                    id = ext.id
                                                )
                                            }
                                        } else emptyList()
                                        // To-do items with a due date + time on this day appear as
                                        // 15-minute, display-only blocks in the timeline (id "todo_"),
                                        // in the on-brand to-do blue. They never touch scheduleEntries.
                                        val todoTimedAsEntries = otherTodoEntries
                                            .filter { !it.isCompleted && it.dueDate == targetDate && it.dueTime != null }
                                            .map { td ->
                                                ScheduleEntry(
                                                    defaultSlotLabel = "",
                                                    task = td.text,
                                                    isTopPriority = false,
                                                    blockColor = RidgelineBlue,
                                                    startHour = td.dueTime!!.hour,
                                                    startMinute = td.dueTime!!.minute,
                                                    durationMins = 15,
                                                    date = targetDate,
                                                    id = "todo_${td.id}"
                                                )
                                            }
                                        val dayEntries = remember(scheduleEntries.toList(), targetDate, externalTimedAsEntries, todoTimedAsEntries) {
                                            // Only entries with a real time land on the timeline. An unscheduled
                                            // priority (hasCustomTime = false) is "what matters today" without a
                                            // "when" yet — it shows as an all-day chip instead.
                                            (scheduleEntries.filter { it.date == targetDate && !it.isAllDay && it.hasCustomTime } + externalTimedAsEntries + todoTimedAsEntries)
                                                .sortedBy { it.startHour * 60 + it.startMinute }
                                        }
                                        val entryLayouts = remember(dayEntries) {
                                            val clusters = mutableListOf<MutableList<ScheduleEntry>>()
                                            var currentCluster = mutableListOf<ScheduleEntry>()
                                            var clusterEnd = -1
                                            for (entry in dayEntries) {
                                                val start = entry.startHour * 60 + entry.startMinute
                                                val end = start + entry.durationMins
                                                if (currentCluster.isEmpty() || start < clusterEnd) {
                                                    currentCluster.add(entry)
                                                    if (end > clusterEnd) clusterEnd = end
                                                } else {
                                                    clusters.add(currentCluster)
                                                    currentCluster = mutableListOf(entry)
                                                    clusterEnd = end
                                                }
                                            }
                                            if (currentCluster.isNotEmpty()) clusters.add(currentCluster)

                                            val layoutMap = mutableMapOf<String, Pair<Int, Int>>()
                                            for (cluster in clusters) {
                                                val cols = mutableListOf<Int>()
                                                for (entry in cluster) {
                                                    val start = entry.startHour * 60 + entry.startMinute
                                                    val end = start + entry.durationMins
                                                    var placed = false
                                                    for (i in cols.indices) {
                                                        if (cols[i] <= start) {
                                                            cols[i] = end
                                                            layoutMap[entry.id] = Pair(i, 1)
                                                            placed = true
                                                            break
                                                        }
                                                    }
                                                    if (!placed) {
                                                        cols.add(end)
                                                        layoutMap[entry.id] = Pair(cols.size - 1, 1)
                                                    }
                                                }
                                                val totalCols = cols.size
                                                for (entry in cluster) {
                                                    val colIdx = layoutMap[entry.id]?.first ?: 0
                                                    layoutMap[entry.id] = Pair(colIdx, totalCols)
                                                }
                                            }
                                            layoutMap
                                        }

                                        dayEntries.forEach { entry ->
                                            val isExternal = entry.id.startsWith("ext_")
                                            val isTodo = entry.id.startsWith("todo_")
                                            val layoutInfo = entryLayouts[entry.id] ?: Pair(0, 1)
                                            val colIdx = layoutInfo.first
                                            val totalCols = layoutInfo.second
                                            val itemWidth = fullWidth / totalCols
                                            val itemOffsetX = itemWidth * colIdx

                                            val startMinsTotal = entry.startHour * 60 + entry.startMinute
                                            val cardTopOffset = startMinsTotal * minuteScaleFactor
                                            val rawCardHeight = entry.durationMins * minuteScaleFactor
                                            val safeCardHeight = if (rawCardHeight < 38f) 38f else rawCardHeight
                                            val textContrastColor = if (entry.blockColor == PureWhite || entry.blockColor == SoftYellow || isExternal) PureBlack else PureWhite

                                            Card(
                                                colors = CardColors(containerColor = entry.blockColor, contentColor = textContrastColor, disabledContainerColor = entry.blockColor, disabledContentColor = textContrastColor),
                                                shape = RoundedCornerShape(14.dp),
                                                modifier = Modifier
                                                    .offset(x = itemOffsetX, y = cardTopOffset.dp)
                                                    .width(itemWidth)
                                                    .padding(end = if (totalCols > 1) 2.dp else 0.dp)
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                                    .height(safeCardHeight.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isExternal) RidgelineBlue.copy(alpha = 0.4f) else Color.LightGray.copy(alpha = 0.6f),
                                                        shape = RoundedCornerShape(14.dp)
                                                    )
                                                    .clickable {
                                                        if (isExternal) externalEventToInspect = externalEvents.find { it.id == entry.id }
                                                        else if (isTodo) {
                                                            highlightedTodoId = entry.id.removePrefix("todo_")
                                                            selectedDate = entry.date
                                                            val targetMon = entry.date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                                            val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                                            coroutineScope.launch { pagerState.animateScrollToPage(5000 + weeksDiff) }
                                                            currentScreen = "Lists"
                                                        }
                                                        else activeInspectEntry = entry
                                                    }
                                            ) {
                                                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalArrangement = Arrangement.Center) {
                                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                        if (isExternal) {
                                                            Icon(imageVector = Icons.Outlined.Event, contentDescription = "From your calendar", tint = RidgelineBlue, modifier = Modifier.size(12.dp))
                                                        } else if (isTodo) {
                                                            Icon(imageVector = Icons.Outlined.Checklist, contentDescription = "To-do item", tint = textContrastColor, modifier = Modifier.size(12.dp))
                                                        }
                                                        Text(text = entry.task, fontWeight = if (isExternal) FontWeight.Medium else FontWeight.Bold, fontSize = if (entry.durationMins <= 15) 12.sp else 14.sp, color = textContrastColor, maxLines = 1)
                                                    }
                                                    if (safeCardHeight > 48f && entry.notes.isNotBlank()) {
                                                        Text(text = entry.notes, fontSize = 11.sp, color = textContrastColor.copy(alpha = 0.8f), maxLines = 1)
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (targetDate == LocalDate.now()) {
                                        Box(modifier = Modifier.fillMaxSize().padding(start = 81.dp)) {
                                            val timeNow = LocalTime.now()
                                            val totalMinsNow = timeNow.hour * 60 + timeNow.minute
                                            val liveYOffsetDp = totalMinsNow * minuteScaleFactor
                                            Row(modifier = Modifier.fillMaxWidth().offset(y = liveYOffsetDp.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(RidgelineBlue))
                                                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(RidgelineBlue.copy(alpha = 0.8f)))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "Lists" -> {
                    val dayPrioritiesCount = scheduleEntries.count { it.date == selectedDate && it.isTopPriority }
                    // Scroll state hoisted so we can derive a condense flag from it. Below a small
                    // threshold (≈ first 40dp of scroll), the page shows its full header; once the
                    // user has scrolled past it, the "Lists" title hides and the priorities card
                    // shrinks to a compact strip so the Other-To-Do section has more breathing room.
                    val listsScrollState = rememberScrollState()
                    val density = LocalDensity.current
                    val listsCondenseAtPx = with(density) { 40.dp.toPx() }
                    val listsExpandAtPx = with(density) { 8.dp.toPx() }
                    var isListsCondensed by remember { mutableStateOf(false) }
                    LaunchedEffect(listsScrollState) {
                        snapshotFlow { listsScrollState.value }.collect { v ->
                            isListsCondensed = when {
                                !isListsCondensed && v > listsCondenseAtPx -> true
                                isListsCondensed && v < listsExpandAtPx -> false
                                else -> isListsCondensed
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize().pointerInput(selectedDate) {
                            detectHorizontalDragGestures(
                                onDragStart = { _: Offset -> horizontalSwipeAccumulator = 0f },
                                onHorizontalDrag = { change: PointerInputChange, dragAmount: Float -> change.consume(); horizontalSwipeAccumulator += dragAmount },
                                onDragEnd = {
                                    val minimumSwipeThresholdPx = 140f
                                    if (horizontalSwipeAccumulator < -minimumSwipeThresholdPx) {
                                        val nextDay = selectedDate.plusDays(1)
                                        selectedDate = nextDay
                                        val targetMon = nextDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                        val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                        coroutineScope.launch { pagerState.animateScrollToPage(5000 + weeksDiff) }
                                    } else if (horizontalSwipeAccumulator > minimumSwipeThresholdPx) {
                                        val previousDay = selectedDate.minusDays(1)
                                        selectedDate = previousDay
                                        val targetMon = previousDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                        val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                        coroutineScope.launch { pagerState.animateScrollToPage(5000 + weeksDiff) }
                                    }
                                }
                            )
                        }
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 4.dp)) {
                            // Pinned condensed priorities card. Appears above the scrolling area
                            // when the user has scrolled past the condense threshold. Reads
                            // priorities for `selectedDate` (which always matches the active
                            // pager page). Stays visible no matter how far the user scrolls.
                            AnimatedVisibility(visible = isListsCondensed) {
                                Column {
                                    CondensedPrioritiesCard(
                                        priorities = scheduleEntries.filter { it.date == selectedDate && it.isTopPriority },
                                        onPriorityClick = { activeInspectEntry = it }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(listsScrollState)) {
                                // Page title hides as the user scrolls into the content. The
                                // month/days row stays visible so date navigation remains accessible.
                                AnimatedVisibility(visible = !isListsCondensed) {
                                    Text(
                                        text = "Lists",
                                        style = MaterialTheme.typography.displaySmall,
                                        color = RidgelineBlue,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, top = 12.dp, bottom = 2.dp)
                                    )
                                }

                                // 4-week reward bank refresh prompt (Schatz 2024 novelty). Passive
                                // banner — appears once 28+ days have passed since the user last
                                // edited their bank, dismissable for the cycle. Resets when the user
                                // actually opens the editor and saves (handled in the editor's Save).
                                val refreshIntervalMs = 28L * 24 * 60 * 60 * 1000
                                val nowMs = System.currentTimeMillis()
                                val shouldShowRefresh = hasCompletedSetup &&
                                        rewardBankLastRefreshMs > 0 &&
                                        (nowMs - rewardBankLastRefreshMs) >= refreshIntervalMs &&
                                        refreshPromptDismissedMs < rewardBankLastRefreshMs
                                AnimatedVisibility(visible = shouldShowRefresh) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp, vertical = 6.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(IceBlueAccent)
                                            .clickable { showRewardBankEditor = true }
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text("✨", fontSize = 18.sp)
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Refresh your reward bank?",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MidnightSlate,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "It's been 4 weeks. New rewards work better.",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MidnightSlate.copy(alpha = 0.7f)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Dismiss",
                                            tint = MidnightSlate.copy(alpha = 0.5f),
                                            modifier = Modifier.size(20.dp).clickable {
                                                val now = System.currentTimeMillis()
                                                sharedPrefs.edit().putLong("refreshPromptDismissedMs", now).apply()
                                                refreshPromptDismissedMs = now
                                            }
                                        )
                                    }
                                }

                                // Month label + Today button + week pager. Mirrors the Schedule screen,
                                // sharing the same selectedDate/pagerState so navigation stays in sync
                                // when the user switches tabs.
                                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    val showYear = selectedDate.year != LocalDate.now().year
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .clickable {
                                                pickerMonthState = YearMonth.from(selectedDate)
                                                showMonthDropdown = !showMonthDropdown
                                            }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = selectedDate.month.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault()) + (if (showYear) " ${selectedDate.year}" else ""),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MidnightSlate
                                        )
                                        Icon(
                                            imageVector = Icons.Outlined.ArrowDropDown,
                                            contentDescription = "Open calendar",
                                            tint = MidnightSlate,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                                        val weekMonday = baseMondayAnchor.plusWeeks((page - 5000).toLong())
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                            for (dayOffset in 0..6) {
                                                val iteratingDate = weekMonday.plusDays(dayOffset.toLong())
                                                WeekStripDay(
                                                    date = iteratingDate,
                                                    isSelected = iteratingDate == selectedDate,
                                                    isToday = iteratingDate == LocalDate.now(),
                                                    onClick = { selectedDate = iteratingDate }
                                                )
                                            }
                                        }
                                    }
                                }
                                AnimatedVisibility(visible = showMonthDropdown) {
                                    DropdownCalendar(
                                        monthState = pickerMonthState,
                                        selectedDate = selectedDate,
                                        onMonthChange = { pickerMonthState = it },
                                        onDayPick = { picked ->
                                            selectedDate = picked
                                            val targetMon = picked.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                            val weeksDiff = ChronoUnit.WEEKS.between(baseMondayAnchor, targetMon).toInt()
                                            coroutineScope.launch { pagerState.scrollToPage(5000 + weeksDiff) }
                                            showMonthDropdown = false
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))

                                AnimatedContent(
                                    targetState = selectedDate,
                                    transitionSpec = {
                                        if (targetState.isAfter(initialState)) {
                                            slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                                        } else {
                                            slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                                        }
                                    },
                                    label = "ListsDayTransition"
                                ) { targetDate ->
                                    val dayPrioritiesCountInternal = scheduleEntries.count { it.date == targetDate && it.isTopPriority }
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "${targetDate.dayOfWeek.getDisplayName(DateTimeTextStyle.FULL, Locale.getDefault())}, ${targetDate.month.getDisplayName(DateTimeTextStyle.FULL, Locale.getDefault())} ${targetDate.dayOfMonth}, ${targetDate.year}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = RidgelineBlue, modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, bottom = 12.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        // Priority data computed up here so both the condensed strip
                                        // and the full Card can read it without duplication.
                                        val dayPrioritiesForTarget = scheduleEntries.filter { it.date == targetDate && it.isTopPriority }
                                        val priorityTotal = dayPrioritiesForTarget.size
                                        val priorityDone = dayPrioritiesForTarget.count { it.isCompleted }

                                        // Crossfade between the condensed strip and full Card driven by
                                        // scroll position. Same animation pattern used by the scoreboard.
                                        AnimatedContent(
                                            targetState = isListsCondensed,
                                            transitionSpec = {
                                                fadeIn(animationSpec = tween(220, delayMillis = 90)) togetherWith
                                                        fadeOut(animationSpec = tween(90))
                                            },
                                            label = "ListsPrioritiesCondense"
                                        ) { condensed ->
                                            if (condensed) {
                                                // Empty when condensed. The pinned condensed card above
                                                // the scrolling area handles display so priorities stay
                                                // visible no matter how far the user scrolls.
                                                Spacer(modifier = Modifier.height(0.dp))
                                            } else {
                                                Card(
                                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                                    shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                                        Text(text = "Today's Priorities", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                                                        Spacer(modifier = Modifier.height(12.dp))
                                                        val dayPriorities = scheduleEntries.filter { it.date == targetDate && it.isTopPriority }

                                                        // Opens the shared composer. Looks like a text box so it reads as
                                                        // "type here", but gives you the full window — details, subtasks,
                                                        // timing, color — in one place.
                                                        if (dayPriorities.size < 3) {
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .clip(RoundedCornerShape(50))
                                                                    .background(CardInset)
                                                                    .border(1.dp, BorderGray.copy(alpha = 0.6f), RoundedCornerShape(50))
                                                                    .clickable {
                                                                        taskComposerDraft = TaskDraft(isPriority = true, ownerDate = targetDate)
                                                                    }
                                                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                            ) {
                                                                Icon(Icons.Outlined.Add, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(18.dp))
                                                                Text("Add a priority…", style = MaterialTheme.typography.titleLarge, color = TextHint)
                                                            }
                                                            Spacer(modifier = Modifier.height(12.dp))
                                                        }

                                                        if (dayPriorities.isEmpty()) {
                                                            Text(text = "Nothing set yet. Add up to three above — or use Brain Dump below if your head's full and you want help picking.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                                                        } else {
                                                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                                                dayPriorities.forEach { priority ->
                                                                    val isDone = priority.isCompleted
                                                                    // Blue card keeps priorities visually weighty — they're the
                                                                    // day's headline items — while matching the goal card's shape,
                                                                    // spacing and typography.
                                                                    Card(
                                                                        colors = CardDefaults.cardColors(containerColor = if (isDone) RidgelineBlue.copy(alpha = 0.5f) else RidgelineBlue),
                                                                        shape = RoundedCornerShape(14.dp),
                                                                        modifier = Modifier.fillMaxWidth()
                                                                    ) {
                                                                        Column(modifier = Modifier.padding(12.dp)) {
                                                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                                                                // Circle checkbox, same as to-dos and goals.
                                                                                if (isDone) {
                                                                                    Icon(
                                                                                        imageVector = Icons.Filled.CheckCircle,
                                                                                        contentDescription = "Mark not done",
                                                                                        tint = PureWhite,
                                                                                        modifier = Modifier.size(26.dp).clickable {
                                                                                            val i = scheduleEntries.indexOfFirst { it.id == priority.id }
                                                                                            if (i != -1) {
                                                                                                scheduleEntries[i] = scheduleEntries[i].copy(isCompleted = false)
                                                                                                removeElevation(ELEVATION_PRIORITY); recordActivity()
                                                                                            }
                                                                                        }
                                                                                    )
                                                                                } else {
                                                                                    Box(
                                                                                        modifier = Modifier.size(26.dp).clip(CircleShape)
                                                                                            .border(2.dp, PureWhite, CircleShape)
                                                                                            .clickable { priorityItemToComplete = priority; showCompletionConfirmDialog = true }
                                                                                    )
                                                                                }
                                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                                Column(
                                                                                    modifier = Modifier.weight(1f).clickable {
                                                                                        taskDetailId = priority.id; taskDetailIsPriority = true
                                                                                    }
                                                                                ) {
                                                                                    Text(
                                                                                        text = priority.task,
                                                                                        style = MaterialTheme.typography.titleLarge,
                                                                                        color = PureWhite,
                                                                                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                                                                                    )
                                                                                    if (priority.hasCustomTime) {
                                                                                        Text(
                                                                                            text = formatTimeLabel(priority.startHour, priority.startMinute),
                                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                                            color = IceBlueAccent
                                                                                        )
                                                                                    }
                                                                                    val stepTotal = priority.subtasks.size
                                                                                    if (stepTotal > 0) {
                                                                                        val stepDone = priority.subtasks.count { it.isCompleted }
                                                                                        Text(
                                                                                            text = "☑ $stepDone/$stepTotal subtasks",
                                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                                            color = IceBlueAccent
                                                                                        )
                                                                                    }
                                                                                    if (priority.reward.isNotBlank()) {
                                                                                        Text(
                                                                                            text = "🎁 Reward: ${priority.reward}",
                                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                                            color = IceBlueAccent,
                                                                                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                                                                                        )
                                                                                    }
                                                                                }
                                                                                // Unscheduled priorities get a one-tap way to pick a time.
                                                                                if (!priority.hasCustomTime && !isDone) {
                                                                                    Row(
                                                                                        modifier = Modifier
                                                                                            .clip(RoundedCornerShape(50))
                                                                                            .background(PureWhite.copy(alpha = 0.2f))
                                                                                            .clickable {
                                                                                                wizardSelectedPriorities.clear()
                                                                                                wizardSelectedPriorities.add(WizardItem(priority.task))
                                                                                                wizardCurrentSchedulingIndex = 0
                                                                                                priorityBeingScheduledId = priority.id
                                                                                                isWizardPlacementMode = true
                                                                                                selectedDate = targetDate
                                                                                                currentScreen = "Home"
                                                                                            }
                                                                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                                                                        verticalAlignment = Alignment.CenterVertically,
                                                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                                                    ) {
                                                                                        Icon(imageVector = Icons.Outlined.Schedule, contentDescription = null, tint = PureWhite, modifier = Modifier.size(13.dp))
                                                                                        Text("Set time", style = MaterialTheme.typography.bodyMedium, color = PureWhite, fontWeight = FontWeight.Bold)
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(16.dp))

                                                            val grandReward = dailyGrandRewards[targetDate] ?: ""
                                                            val totalP = dayPriorities.size.coerceAtLeast(1)
                                                            val completedP = dayPriorities.count { it.isCompleted }
                                                            val progressAmt = completedP.toFloat() / totalP.toFloat()
                                                            val allDone = (completedP == totalP && totalP > 0)

                                                            if (grandReward.isBlank()) {
                                                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                                    // Reward bank chips — medium-only here since grand rewards are
                                                                    // end-of-day, higher-value moments.
                                                                    RewardChipsRow(
                                                                        bank = rewardBank,
                                                                        mediumOnly = true,
                                                                        onPick = { grandRewardInput = it }
                                                                    )
                                                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                                        OutlinedTextField(
                                                                            value = grandRewardInput,
                                                                            onValueChange = { grandRewardInput = it },
                                                                            placeholder = { Text("Reward for all 3...") },
                                                                            modifier = Modifier.weight(1f).height(56.dp),
                                                                            singleLine = true,
                                                                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardInset, unfocusedContainerColor = CardInset, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                                                            shape = RoundedCornerShape(50)
                                                                        )
                                                                        Button(
                                                                            onClick = {
                                                                                if(grandRewardInput.isNotBlank()) {
                                                                                    dailyGrandRewards[targetDate] = grandRewardInput
                                                                                    grandRewardInput = ""
                                                                                    showNotification("Grand Reward Saved", "Keep up the momentum to claim it!", MetricGold, "🏆")
                                                                                    recordActivity()
                                                                                }
                                                                            },
                                                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                                                            shape = RoundedCornerShape(50),
                                                                            modifier = Modifier.height(56.dp),
                                                                            contentPadding = PaddingValues(horizontal = 16.dp)
                                                                        ) { Text("Save", fontWeight = FontWeight.Bold) }
                                                                    }
                                                                }
                                                            } else {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .height(50.dp)
                                                                        .clip(RoundedCornerShape(50))
                                                                        .background(SkyGray)
                                                                ) {
                                                                    Box(
                                                                        modifier = Modifier
                                                                            .fillMaxHeight()
                                                                            .fillMaxWidth(progressAmt.coerceAtLeast(0.01f))
                                                                            .background(if (allDone) MetricGold.copy(alpha = 0.5f) else MetricGold.copy(alpha = 0.25f))
                                                                    )
                                                                    Row(
                                                                        modifier = Modifier
                                                                            .fillMaxSize()
                                                                            .padding(horizontal = 12.dp),
                                                                        verticalAlignment = Alignment.CenterVertically,
                                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                                    ) {
                                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                                            Text(if(allDone) "🏆 EARNED: " else "🏆 REWARD: ", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium, color = MetricGold)
                                                                            Text(grandReward, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, textDecoration = if (allDone) TextDecoration.LineThrough else TextDecoration.None)
                                                                        }
                                                                        Text(
                                                                            text = "$completedP/$totalP",
                                                                            fontWeight = FontWeight.Black,
                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                            color = if (allDone) MetricGold else MidnightSlate.copy(alpha = 0.5f)
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(text = "Other To-Do", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(50))
                                                        .background(CardInset)
                                                        .border(1.dp, BorderGray.copy(alpha = 0.6f), RoundedCornerShape(50))
                                                        .clickable {
                                                            taskComposerDraft = TaskDraft(isPriority = false, ownerDate = targetDate)
                                                        }
                                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Icon(Icons.Outlined.Add, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(18.dp))
                                                    Text("Add a to-do…", style = MaterialTheme.typography.titleLarge, color = TextHint)
                                                }
                                                Spacer(modifier = Modifier.height(12.dp))
                                                val activeTodos = otherTodoEntries.filter { item -> !item.isCompleted && item.date <= targetDate }
                                                val completedTodos = otherTodoEntries.filter { item -> item.isCompleted && item.completedDate == targetDate }
                                                if (activeTodos.isEmpty() && completedTodos.isEmpty()) {
                                                    Text(text = "No additional tasks written down for today.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 8.dp))
                                                } else {
                                                    // Hand-rolled drag-and-drop for the active sublist. The reorderable Compose
                                                    // libraries all require a LazyColumn, which doesn't play nicely inside this
                                                    // page's outer verticalScroll Column. Tracking drag state by hand avoids the
                                                    // nested-scroll problem and gives us full control over the UX.
                                                    //
                                                    // Approach: long-press anywhere on a row to start dragging. While dragging,
                                                    // the dragged item follows the finger (Y-offset), and other items animate
                                                    // upward/downward to make space at the drop target. On release, the items
                                                    // are permuted in otherTodoEntries (preserving completed items' positions).
                                                    var draggedItemId by remember(targetDate) { mutableStateOf<String?>(null) }
                                                    var dragOffsetY by remember(targetDate) { mutableStateOf(0f) }
                                                    // Approximate per-row height (including the 8dp inter-item spacing) used
                                                    // for slot calculations during drag. Items have minor height differences
                                                    // (rewards/cues add lines) but a fixed slot size is acceptable here — the
                                                    // user's perception of "drop target" cares more about slot count than pixels.
                                                    val rowSlotPx = with(LocalDensity.current) { 64.dp.toPx() }

                                                    if (activeTodos.isNotEmpty()) {
                                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                            activeTodos.forEachIndexed { idx, todoItem ->
                                                                val isDragging = draggedItemId == todoItem.id
                                                                val draggedIdx = if (draggedItemId != null) activeTodos.indexOfFirst { it.id == draggedItemId } else -1
                                                                // Current "slot" the dragged item is hovering over — derived from
                                                                // the original index plus the drag offset, snapped to the nearest slot.
                                                                val currentSlot = if (draggedIdx != -1) (draggedIdx + (dragOffsetY / rowSlotPx).roundToInt()).coerceIn(0, activeTodos.size - 1) else -1
                                                                // For non-dragged items: shift up if dragged item passed us going down,
                                                                // shift down if dragged item passed us going up.
                                                                val targetShift = when {
                                                                    isDragging -> 0f
                                                                    draggedIdx == -1 -> 0f
                                                                    currentSlot > draggedIdx && idx > draggedIdx && idx <= currentSlot -> -rowSlotPx
                                                                    currentSlot < draggedIdx && idx < draggedIdx && idx >= currentSlot -> rowSlotPx
                                                                    else -> 0f
                                                                }
                                                                val animatedShift by animateFloatAsState(
                                                                    targetValue = targetShift,
                                                                    animationSpec = tween(120),
                                                                    label = "TodoShift"
                                                                )

                                                                val isCarriedOver = todoItem.date.isBefore(targetDate)
                                                                val canHaveSubtasks = !todoItem.fromBrainDump
                                                                val isExpanded = todoItem.id in expandedTodoIds
                                                                val subDone = todoItem.subtasks.count { it.isCompleted }
                                                                val subTotal = todoItem.subtasks.size
                                                                val isHighlighted = todoItem.id == highlightedTodoId
                                                                val highlightBorder by animateColorAsState(if (isHighlighted) RidgelineBlue else Color.Transparent, label = "todoHighlight")
                                                                // Replace this to-do (by id) with a copy whose subtasks are transformed.
                                                                val updateSubtasks: ((List<TodoSubtask>) -> List<TodoSubtask>) -> Unit = { transform ->
                                                                    val baseIdx = otherTodoEntries.indexOfFirst { it.id == todoItem.id }
                                                                    if (baseIdx != -1) {
                                                                        val cur = otherTodoEntries[baseIdx]
                                                                        otherTodoEntries[baseIdx] = cur.copy(subtasks = transform(cur.subtasks))
                                                                    }
                                                                }
                                                                Card(
                                                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                                                    shape = RoundedCornerShape(14.dp),
                                                                    elevation = CardDefaults.cardElevation(defaultElevation = if (isDragging) 8.dp else 0.dp),
                                                                    modifier = Modifier
                                                                        .offset { IntOffset(0, if (isDragging) dragOffsetY.roundToInt() else animatedShift.roundToInt()) }
                                                                        .zIndex(if (isDragging) 1f else 0f)
                                                                        .pointerInput(activeTodos.size, todoItem.id) {
                                                                            detectDragGesturesAfterLongPress(
                                                                                onDragStart = {
                                                                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                                    draggedItemId = todoItem.id
                                                                                    dragOffsetY = 0f
                                                                                },
                                                                                onDrag = { _, drag -> dragOffsetY += drag.y },
                                                                                onDragEnd = {
                                                                                    val activeIdx = activeTodos.indexOfFirst { it.id == todoItem.id }
                                                                                    val target = if (activeIdx != -1) (activeIdx + (dragOffsetY / rowSlotPx).roundToInt()).coerceIn(0, activeTodos.size - 1) else -1
                                                                                    if (activeIdx != -1 && target != activeIdx) {
                                                                                        val activeIndicesInBase = otherTodoEntries.withIndex()
                                                                                            .filter { (_, item) -> !item.isCompleted && item.date <= targetDate }
                                                                                            .map { it.index }
                                                                                        if (activeIdx in activeIndicesInBase.indices && target in activeIndicesInBase.indices) {
                                                                                            val sourceBaseIdx = activeIndicesInBase[activeIdx]
                                                                                            val targetBaseIdx = activeIndicesInBase[target]
                                                                                            val moved = otherTodoEntries.removeAt(sourceBaseIdx)
                                                                                            otherTodoEntries.add(targetBaseIdx, moved)
                                                                                        }
                                                                                    }
                                                                                    draggedItemId = null
                                                                                    dragOffsetY = 0f
                                                                                },
                                                                                onDragCancel = {
                                                                                    draggedItemId = null
                                                                                    dragOffsetY = 0f
                                                                                }
                                                                            )
                                                                        }
                                                                        .fillMaxWidth()
                                                                        // Carried-over items look identical to today's — the
                                                                        // "Carried over" label carries the meaning. Recoloring a
                                                                        // task because it moved days reads as a mark against you.
                                                                        .border(
                                                                            width = if (isHighlighted) 2.dp else 1.dp,
                                                                            color = if (isHighlighted) highlightBorder else Color.LightGray.copy(alpha = 0.5f),
                                                                            shape = RoundedCornerShape(14.dp)
                                                                        )
                                                                        .then(if (isHighlighted) Modifier.bringIntoViewRequester(todoBringIntoView) else Modifier)
                                                                ) {
                                                                    Column(modifier = Modifier.padding(12.dp)) {
                                                                        Row(
                                                                            modifier = Modifier.fillMaxWidth(),
                                                                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                                                                        ) {
                                                                            Row(
                                                                                verticalAlignment = Alignment.CenterVertically,
                                                                                modifier = Modifier.weight(1f)
                                                                            ) {
                                                                                // Drag handle (≡) — visual affordance. Long-pressing
                                                                                // anywhere on the row activates drag.
                                                                                Icon(
                                                                                    imageVector = Icons.Outlined.DragIndicator,
                                                                                    contentDescription = "Drag to reorder",
                                                                                    tint = MidnightSlate.copy(alpha = 0.4f),
                                                                                    modifier = Modifier.size(18.dp)
                                                                                )
                                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                                // Checkbox completes the item. Tapping the text opens
                                                                                // the detail window instead — two distinct targets, so
                                                                                // a stray tap can't finish something by accident.
                                                                                Box(
                                                                                    modifier = Modifier.size(26.dp).clip(CircleShape)
                                                                                        .border(2.dp, RidgelineBlue.copy(alpha = 0.6f), CircleShape)
                                                                                        .clickable {
                                                                                            if (dayPrioritiesCountInternal < 3) { carryOverItemTarget = todoItem; showCarryOverActionDialog = true }
                                                                                            else { todoPendingComplete = todoItem }
                                                                                        }
                                                                                )
                                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                                Column(
                                                                                    modifier = Modifier.weight(1f).clickable {
                                                                                        taskDetailId = todoItem.id; taskDetailIsPriority = false
                                                                                    }
                                                                                ) {
                                                                                    Text(text = todoItem.text, style = MaterialTheme.typography.titleLarge, color = MidnightSlate)
                                                                                    if (subTotal > 0) {
                                                                                        Text(text = "☑ $subDone/$subTotal subtasks", style = MaterialTheme.typography.bodyMedium, color = if (subDone == subTotal) SuccessGreen else TextMuted)
                                                                                    }
                                                                                    if (todoItem.reward.isNotBlank()) {
                                                                                        Text(text = "🎁 Reward: ${todoItem.reward}", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                                                                                    }
                                                                                    if (todoItem.triggerCue.isNotBlank()) {
                                                                                        Text(
                                                                                            text = "When I ${todoItem.triggerCue}…",
                                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                                            fontStyle = FontStyle.Italic,
                                                                                            color = TextMuted
                                                                                        )
                                                                                    }
                                                                                    todoItem.reminderEpochMillis?.let { epoch ->
                                                                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                                                            Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                                                                                            Text(text = formatReminderLabel(epoch), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                                                                                        }
                                                                                    }
                                                                                    if (todoItem.dueDate != null) {
                                                                                        val due = todoItem.dueDate!!
                                                                                        val dueOverdue = due.isBefore(LocalDate.now()) || (due.isEqual(LocalDate.now()) && todoItem.dueTime?.isBefore(LocalTime.now()) == true)
                                                                                        val dueColor = if (dueOverdue) OverdueRed else if (due.isEqual(LocalDate.now()) || due.isEqual(LocalDate.now().plusDays(1))) MetricOrange else RidgelineBlue
                                                                                        val dueTimeStr = todoItem.dueTime?.let { t ->
                                                                                            val h = if (t.hour == 0) 12 else if (t.hour > 12) t.hour - 12 else t.hour
                                                                                            " ${h}:${String.format(Locale.US, "%02d", t.minute)} ${if (t.hour < 12) "AM" else "PM"}"
                                                                                        } ?: ""
                                                                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                                                            Icon(imageVector = Icons.Outlined.Event, contentDescription = null, tint = dueColor, modifier = Modifier.size(12.dp))
                                                                                            Text(text = "Due ${due.monthValue}/${due.dayOfMonth}$dueTimeStr", style = MaterialTheme.typography.bodyMedium, color = dueColor)
                                                                                        }
                                                                                    }
                                                                                    if (isCarriedOver) Text("Carried over", style = MaterialTheme.typography.bodyMedium, color = RidgelineBlue)
                                                                                }
                                                                            }

                                                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                                                                // Expand/collapse subtasks. Brain-dump items stay single, so no chevron.
                                                                                // Edit/Delete now live in the detail window, which keeps the row clean.
                                                                                if (canHaveSubtasks) {
                                                                                    Icon(
                                                                                        imageVector = if (isExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                                                                        contentDescription = if (isExpanded) "Hide subtasks" else "Show subtasks",
                                                                                        tint = MidnightSlate.copy(alpha = 0.55f),
                                                                                        modifier = Modifier.clickable {
                                                                                            if (isExpanded) expandedTodoIds.remove(todoItem.id) else expandedTodoIds.add(todoItem.id)
                                                                                        }.padding(4.dp).size(20.dp)
                                                                                    )
                                                                                }
                                                                            }
                                                                        }

                                                                        // Expanded subtask list + inline add — indented like goal steps.
                                                                        if (isExpanded && canHaveSubtasks) {
                                                                            Spacer(modifier = Modifier.height(8.dp))
                                                                            Column(modifier = Modifier.fillMaxWidth().padding(start = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                                                todoItem.subtasks.forEach { st ->
                                                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                                                        if (st.isCompleted) {
                                                                                            Icon(
                                                                                                imageVector = Icons.Filled.CheckCircle,
                                                                                                contentDescription = "Mark subtask incomplete",
                                                                                                tint = SuccessGreen,
                                                                                                modifier = Modifier.size(26.dp).clickable {
                                                                                                    updateSubtasks { list -> list.map { if (it.id == st.id) it.copy(isCompleted = false) else it } }
                                                                                                }
                                                                                            )
                                                                                        } else {
                                                                                            Box(
                                                                                                modifier = Modifier
                                                                                                    .size(26.dp)
                                                                                                    .clip(CircleShape)
                                                                                                    .border(2.dp, MidnightSlate.copy(alpha = 0.35f), CircleShape)
                                                                                                    .clickable {
                                                                                                        updateSubtasks { list -> list.map { if (it.id == st.id) it.copy(isCompleted = true) else it } }
                                                                                                        // If this was the last unchecked subtask, offer to complete the parent.
                                                                                                        val allDoneNow = todoItem.subtasks.all { it.id == st.id || it.isCompleted }
                                                                                                        if (allDoneNow && !todoItem.isCompleted) subtaskCompletionPrompt = todoItem
                                                                                                    }
                                                                                            )
                                                                                        }
                                                                                        Spacer(modifier = Modifier.width(10.dp))
                                                                                        Text(
                                                                                            text = st.text,
                                                                                            style = MaterialTheme.typography.bodyLarge,
                                                                                            color = if (st.isCompleted) TextMuted else MidnightSlate,
                                                                                            textDecoration = if (st.isCompleted) TextDecoration.LineThrough else null,
                                                                                            modifier = Modifier.weight(1f)
                                                                                        )
                                                                                        Icon(
                                                                                            imageVector = Icons.Outlined.Close,
                                                                                            contentDescription = "Delete subtask",
                                                                                            tint = Color.Gray.copy(alpha = 0.5f),
                                                                                            modifier = Modifier.size(16.dp).clickable {
                                                                                                updateSubtasks { list -> list.filterNot { it.id == st.id } }
                                                                                            }
                                                                                        )
                                                                                    }
                                                                                }
                                                                                // Add-subtask inline input.
                                                                                var subtaskDraft by remember(todoItem.id, subTotal) { mutableStateOf("") }
                                                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                                                    OutlinedTextField(
                                                                                        value = subtaskDraft, onValueChange = { subtaskDraft = it },
                                                                                        placeholder = { Text("Add a subtask…") }, singleLine = true,
                                                                                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                                                                                        modifier = Modifier.weight(1f),
                                                                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardInset, unfocusedContainerColor = CardInset, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                                                                        shape = RoundedCornerShape(50)
                                                                                    )
                                                                                    Box(
                                                                                        modifier = Modifier.size(36.dp).clip(CircleShape).background(RidgelineBlue).clickable {
                                                                                            val t = subtaskDraft.trim()
                                                                                            if (t.isNotBlank()) { updateSubtasks { list -> list + TodoSubtask(text = t) }; subtaskDraft = "" }
                                                                                        }, contentAlignment = Alignment.Center
                                                                                    ) { Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add subtask", tint = PureWhite, modifier = Modifier.size(18.dp)) }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    // Completed items — same card language, greyed like a done goal.
                                                    if (completedTodos.isNotEmpty()) {
                                                        Column(
                                                            modifier = Modifier.padding(top = if (activeTodos.isNotEmpty()) 10.dp else 0.dp),
                                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                                        ) {
                                                            completedTodos.forEach { todoItem ->
                                                                Card(
                                                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                                                    shape = RoundedCornerShape(14.dp),
                                                                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                                                ) {
                                                                    Row(
                                                                        verticalAlignment = Alignment.CenterVertically,
                                                                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                                                                    ) {
                                                                        Icon(
                                                                            imageVector = Icons.Filled.CheckCircle, contentDescription = "Mark not done",
                                                                            tint = SuccessGreen,
                                                                            modifier = Modifier.size(26.dp).clickable {
                                                                                val baseIdx = otherTodoEntries.indexOfFirst { it.id == todoItem.id }
                                                                                if (baseIdx != -1) {
                                                                                    otherTodoEntries[baseIdx] = otherTodoEntries[baseIdx].copy(isCompleted = false, completedDate = null)
                                                                                    removeElevation(ELEVATION_TODO)
                                                                                    recordActivity()
                                                                                }
                                                                            }
                                                                        )
                                                                        Spacer(modifier = Modifier.width(8.dp))
                                                                        Column(
                                                                            modifier = Modifier.weight(1f).clickable {
                                                                                taskDetailId = todoItem.id; taskDetailIsPriority = false
                                                                            }
                                                                        ) {
                                                                            Text(text = todoItem.text, style = MaterialTheme.typography.titleLarge, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
                                                                            if (todoItem.reward.isNotBlank()) {
                                                                                Text(text = "🎁 Reward: ${todoItem.reward}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(100.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                "Goals" -> {
                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Text(
                            text = "Goals",
                            style = MaterialTheme.typography.displaySmall,
                            color = RidgelineBlue,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, top = 12.dp, bottom = 2.dp)
                        )

                        Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {

                            // WEEKLY GOALS SECTION
                            val weekEnd = selectedGoalWeek.plusDays(6)
                            val weeklyGoalsForWeek = goalEntries.filter { it.type == "WEEKLY" && it.weekAnchor == selectedGoalWeek }
                            val weeklyDone = weeklyGoalsForWeek.count { it.isCompleted }
                            val weeklyTotal = weeklyGoalsForWeek.size
                            val weeklyFraction = if (weeklyTotal > 0) weeklyDone.toFloat() / weeklyTotal.toFloat() else 0f

                            // The Weekly card has its own internal expand/collapse toggle
                            // (▼/▲ on the header row) which lets the user shrink it for more
                            // Monthly Goals room — no scroll-driven auto-condense.
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (isWeeklyGoalsExpanded) {
                                        // Expanded header: standard card title row with title and ▲ toggle.
                                        Row(
                                            modifier = Modifier.fillMaxWidth().clickable { isWeeklyGoalsExpanded = !isWeeklyGoalsExpanded }.padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Weekly Goals", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                                            Text(text = "▲", fontSize = 18.sp, color = RidgelineBlue)
                                        }
                                    } else {
                                        // Collapsed: slim header matching the condensed Priorities
                                        // card style, plus each weekly goal as a pill row below.
                                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
                                            // Slim header row.
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { isWeeklyGoalsExpanded = !isWeeklyGoalsExpanded },
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(50))
                                                        .background(RidgelineBlue)
                                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Text(text = "Weekly", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                                                }
                                                if (weeklyTotal > 0) {
                                                    Text(
                                                        text = "$weeklyDone / $weeklyTotal",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MidnightSlate.copy(alpha = 0.7f)
                                                    )
                                                } else {
                                                    Text(
                                                        text = "None set",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MidnightSlate.copy(alpha = 0.5f)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                                if (weeklyTotal > 0) {
                                                    Box(
                                                        modifier = Modifier
                                                            .width(48.dp)
                                                            .height(4.dp)
                                                            .clip(RoundedCornerShape(50))
                                                            .background(MistBlue.copy(alpha = 0.35f))
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxHeight()
                                                                .fillMaxWidth(weeklyFraction.coerceIn(0.02f, 1f))
                                                                .clip(RoundedCornerShape(50))
                                                                .background(MetricGreen)
                                                        )
                                                    }
                                                }
                                                Text(text = "▼", fontSize = 16.sp, color = RidgelineBlue)
                                            }
                                            // Pill rows for each weekly goal — matches the condensed
                                            // Priorities card. Tap a row to expand the card and bring
                                            // the user back to the full goal list (and the user can
                                            // then act on the specific goal there).
                                            if (weeklyGoalsForWeek.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    weeklyGoalsForWeek.forEach { goal ->
                                                        val isDone = goal.isCompleted
                                                        val stepTotal = goal.steps.size
                                                        val stepDone = goal.steps.count { it.isCompleted }
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .clip(RoundedCornerShape(50))
                                                                .background(if (isDone) RidgelineBlue.copy(alpha = 0.5f) else RidgelineBlue)
                                                                .clickable { isWeeklyGoalsExpanded = true }
                                                                .padding(horizontal = 14.dp, vertical = 6.dp),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = goal.title,
                                                                style = MaterialTheme.typography.labelMedium,
                                                                color = PureWhite,
                                                                maxLines = 1,
                                                                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                            if (stepTotal > 0 && !isDone) {
                                                                Text(
                                                                    text = "$stepDone/$stepTotal",
                                                                    style = MaterialTheme.typography.labelSmall,
                                                                    color = PureWhite.copy(alpha = 0.85f)
                                                                )
                                                            } else if (isDone) {
                                                                Text("✓", color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = isWeeklyGoalsExpanded) {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().pointerInput(selectedGoalWeek) {
                                                detectHorizontalDragGestures(
                                                    onDragStart = { _: Offset -> goalWeekSwipeAccumulator = 0f },
                                                    onHorizontalDrag = { change: PointerInputChange, dragAmount: Float -> change.consume(); goalWeekSwipeAccumulator += dragAmount },
                                                    onDragEnd = {
                                                        val minimumSwipeThresholdPx = 140f
                                                        if (goalWeekSwipeAccumulator < -minimumSwipeThresholdPx) {
                                                            selectedGoalWeek = selectedGoalWeek.plusWeeks(1)
                                                        } else if (goalWeekSwipeAccumulator > minimumSwipeThresholdPx) {
                                                            selectedGoalWeek = selectedGoalWeek.minusWeeks(1)
                                                        }
                                                    }
                                                )
                                            }
                                        ) {
                                            AnimatedContent(
                                                targetState = selectedGoalWeek,
                                                transitionSpec = {
                                                    if (targetState.isAfter(initialState)) {
                                                        slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                                                    } else {
                                                        slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                                                    }
                                                },
                                                label = "WeeklyGoalsTransition"
                                            ) { targetWeek ->
                                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                                                    val targetEnd = targetWeek.plusDays(6)
                                                    val tWeekLabel = "Week of ${targetWeek.month.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault())} ${targetWeek.dayOfMonth} - ${targetEnd.month.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault())} ${targetEnd.dayOfMonth}"
                                                    Text(text = tWeekLabel, color = RidgelineBlue, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 12.dp))

                                                    // Matches the Lists page: a button that looks like a text box,
                                                    // opening the full builder rather than capturing a bare title.
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clip(RoundedCornerShape(50))
                                                            .background(CardInset)
                                                            .border(1.dp, BorderGray.copy(alpha = 0.6f), RoundedCornerShape(50))
                                                            .clickable {
                                                                draftGoalType = "WEEKLY"
                                                                draftGoalTitle = ""
                                                                draftGoalDeadline = targetEnd
                                                                draftGoalReward = ""
                                                                draftGoalVision = ""
                                                                draftGoalSteps.clear()
                                                                draftNewStepText = ""
                                                                editingGoalId = null
                                                                showGoalWizardDialog = true
                                                            }
                                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Icon(Icons.Outlined.Add, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(18.dp))
                                                        Text("Set a weekly goal…", style = MaterialTheme.typography.titleLarge, color = TextHint)
                                                    }

                                                    Spacer(modifier = Modifier.height(16.dp))

                                                    val currentGoals = goalEntries.filter { it.type == "WEEKLY" && it.weekAnchor == targetWeek }.sortedBy { goal ->
                                                        if (goal.isCompleted) 1 else 0
                                                    }

                                                    if (currentGoals.isEmpty()) {
                                                        Text(text = "No weekly goals mapped out yet.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 16.dp))
                                                    } else {
                                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                                                            currentGoals.forEach { goal ->
                                                                val now = LocalDate.now()
                                                                val isGoalLate = goal.deadlineDate?.isBefore(now) == true && !goal.isCompleted
                                                                val isGoalSoon = goal.deadlineDate?.isEqual(now) == true || goal.deadlineDate?.isEqual(now.plusDays(1)) == true

                                                                Card(
                                                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                                                    shape = RoundedCornerShape(14.dp),
                                                                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray.copy(alpha=0.5f), RoundedCornerShape(14.dp))
                                                                ) {
                                                                    Column(modifier = Modifier.padding(12.dp)) {
                                                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f).clickable {
                                                                                val idx = goalEntries.indexOfFirst { it.id == goal.id }
                                                                                if (idx != -1) {
                                                                                    if (!goalEntries[idx].isCompleted) {
                                                                                        // Completing the goal is a meaningful moment — confirm before doing it.
                                                                                        goalPendingCompletion = goalEntries[idx]
                                                                                    } else {
                                                                                        // Unchecking is reversal cleanup — apply immediately.
                                                                                        goalEntries[idx] = goalEntries[idx].copy(isCompleted = false)
                                                                                        removeElevation(ELEVATION_PRIORITY); recordActivity()
                                                                                    }
                                                                                }
                                                                            }) {
                                                                                // Circle checkbox, matching priorities and to-dos on the Lists page.
                                                                                if (goal.isCompleted) {
                                                                                    Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Completed", tint = SuccessGreen, modifier = Modifier.size(26.dp))
                                                                                } else {
                                                                                    Box(modifier = Modifier.size(26.dp).clip(CircleShape).border(2.dp, RidgelineBlue.copy(alpha = 0.6f), CircleShape))
                                                                                }
                                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                                Column {
                                                                                    Text(text = goal.title, style = MaterialTheme.typography.titleLarge, color = if (goal.isCompleted) Color.Gray else MidnightSlate, textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None)
                                                                                    if (goal.deadlineDate != null) {
                                                                                        Text("Due: ${goal.deadlineDate!!.monthValue}/${goal.deadlineDate!!.dayOfMonth}", style = MaterialTheme.typography.bodyMedium, color = if (goal.isCompleted) Color.Gray else if (isGoalLate) Color.Red else if (isGoalSoon) MetricOrange else RidgelineBlue)
                                                                                    }
                                                                                }
                                                                            }

                                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit goal", tint = RidgelineBlue, modifier = Modifier.clickable {
                                                                                    editingGoalId = goal.id
                                                                                    draftGoalType = "WEEKLY"
                                                                                    draftGoalTitle = goal.title
                                                                                    draftGoalDeadline = goal.deadlineDate
                                                                                    draftGoalReward = goal.reward
                                                                                    draftGoalVision = goal.futureVision
                                                                                    draftGoalSteps.clear()
                                                                                    draftGoalSteps.addAll(goal.steps)
                                                                                    draftNewStepText = ""
                                                                                    showGoalWizardDialog = true
                                                                                }.padding(8.dp).size(22.dp))
                                                                                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Delete goal", tint = Color.Gray.copy(alpha = 0.6f), modifier = Modifier.clickable { goalPendingDelete = goal }.padding(8.dp).size(22.dp))
                                                                            }
                                                                        }

                                                                        // Episodic future thinking: re-surface the vivid finish picture above the
                                                                        // steps, so the user re-reads it right before engaging with the work.
                                                                        if (goal.futureVision.isNotBlank()) {
                                                                            Spacer(modifier = Modifier.height(8.dp))
                                                                            Box(
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth()
                                                                                    .clip(RoundedCornerShape(14.dp))
                                                                                    .background(NeutralFill)
                                                                                    .padding(12.dp)
                                                                            ) {
                                                                                Column {
                                                                                    Text("✨ When this is done", style = MaterialTheme.typography.bodyMedium, color = RidgelineBlue)
                                                                                    Spacer(modifier = Modifier.height(2.dp))
                                                                                    Text(goal.futureVision, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                                                                                }
                                                                            }
                                                                        }

                                                                        if (goal.steps.isNotEmpty()) {
                                                                            Spacer(modifier = Modifier.height(8.dp))
                                                                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = 24.dp)) {
                                                                                goal.steps.forEach { step ->
                                                                                    val isStepLate = step.deadlineDate?.isBefore(now) == true && !step.isCompleted
                                                                                    val isStepSoon = step.deadlineDate?.isEqual(now) == true || step.deadlineDate?.isEqual(now.plusDays(1)) == true
                                                                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable {
                                                                                        val gIdx = goalEntries.indexOfFirst { it.id == goal.id }
                                                                                        if (gIdx != -1) {
                                                                                            val sIdx = goalEntries[gIdx].steps.indexOfFirst { it.id == step.id }
                                                                                            if (sIdx != -1) {
                                                                                                val updatedSteps = goalEntries[gIdx].steps.toMutableList()
                                                                                                val sToggled = !updatedSteps[sIdx].isCompleted
                                                                                                updatedSteps[sIdx] = updatedSteps[sIdx].copy(isCompleted = sToggled)
                                                                                                goalEntries[gIdx] = goalEntries[gIdx].copy(steps = updatedSteps)
                                                                                                if (sToggled) { awardElevation(ELEVATION_STEP, "✨ Step done · +$ELEVATION_STEP ft") } else { removeElevation(ELEVATION_STEP); recordActivity() }
                                                                                            }
                                                                                        }
                                                                                    }.padding(vertical = 4.dp)) {
                                                                                        if (step.isCompleted) {
                                                                                            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Completed", tint = SuccessGreen, modifier = Modifier.size(26.dp))
                                                                                        } else {
                                                                                            Box(modifier = Modifier.size(26.dp).clip(CircleShape).border(2.dp, MidnightSlate.copy(alpha = 0.35f), CircleShape))
                                                                                        }
                                                                                        Spacer(modifier = Modifier.width(10.dp))
                                                                                        Text(text = step.text, style = MaterialTheme.typography.bodyLarge, color = if (step.isCompleted) Color.Gray else MidnightSlate, textDecoration = if (step.isCompleted) TextDecoration.LineThrough else TextDecoration.None, modifier = Modifier.weight(1f))
                                                                                        if (step.deadlineDate != null) {
                                                                                            Text("${step.deadlineDate!!.monthValue}/${step.deadlineDate!!.dayOfMonth}", style = MaterialTheme.typography.bodyMedium, color = if (step.isCompleted) Color.Gray else if (isStepLate) Color.Red else if (isStepSoon) MetricOrange else RidgelineBlue)
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        if (goal.reward.isNotBlank()) {
                                                                            Spacer(modifier = Modifier.height(10.dp))
                                                                            val totalSteps = goal.steps.size
                                                                            val completedSteps = goal.steps.count { it.isCompleted }
                                                                            val isFullyDone = goal.isCompleted
                                                                            val progressAmt = if (isFullyDone) 1f else if (totalSteps > 0) completedSteps.toFloat() / totalSteps.toFloat() else 0f

                                                                            Box(
                                                                                modifier = Modifier.fillMaxWidth().height(36.dp).clip(RoundedCornerShape(50)).background(Color.LightGray.copy(alpha = 0.3f))
                                                                            ) {
                                                                                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progressAmt.coerceAtLeast(0.01f)).background(if (isFullyDone) RidgelineBlue.copy(alpha = 0.6f) else MistBlue.copy(alpha = 0.6f)))
                                                                                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                                                        Text(if(isFullyDone) "🏆 EARNED: " else "🎁 REWARD: ", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium, color = if(isFullyDone) RidgelineBlue else MidnightSlate)
                                                                                        Text(goal.reward, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, textDecoration = if (isFullyDone) TextDecoration.LineThrough else TextDecoration.None)
                                                                                    }
                                                                                    if (totalSteps > 0) {
                                                                                        Text(text = "$completedSteps/$totalSteps", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium, color = if (isFullyDone) RidgelineBlue else MidnightSlate.copy(alpha = 0.6f))
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            var inlineRewardText by remember(goal.id) { mutableStateOf("") }
                                                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                                                RewardChipsRow(
                                                                                    bank = rewardBank,
                                                                                    mediumOnly = true,
                                                                                    onPick = { inlineRewardText = it }
                                                                                )
                                                                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                                                    OutlinedTextField(
                                                                                        value = inlineRewardText,
                                                                                        onValueChange = { inlineRewardText = it },
                                                                                        placeholder = { Text("Set a reward for this goal...") },
                                                                                        singleLine = true,
                                                                                        modifier = Modifier.weight(1f).height(48.dp),
                                                                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                                                                        shape = RoundedCornerShape(50)
                                                                                    )
                                                                                    Spacer(modifier = Modifier.width(8.dp))
                                                                                    Button(
                                                                                        onClick = {
                                                                                            if (inlineRewardText.isNotBlank()) {
                                                                                                val gIdx = goalEntries.indexOfFirst { it.id == goal.id }
                                                                                                if (gIdx != -1) {
                                                                                                    goalEntries[gIdx] = goalEntries[gIdx].copy(reward = inlineRewardText.trim())
                                                                                                    recordActivity()
                                                                                                    showNotification("Reward Saved!", "Keep up the momentum!", RidgelineBlue, "🎁")
                                                                                                }
                                                                                            }
                                                                                        },
                                                                                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                                                                        shape = RoundedCornerShape(50),
                                                                                        modifier = Modifier.height(48.dp)
                                                                                    ) { Text("Save") }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // MONTHLY GOALS SECTION
                            val monthlyGoalsForMonth = goalEntries.filter { it.type == "MONTHLY" && it.monthAnchor == selectedGoalMonth }
                            val monthlyDone = monthlyGoalsForMonth.count { it.isCompleted }
                            val monthlyTotal = monthlyGoalsForMonth.size
                            val monthlyFraction = if (monthlyTotal > 0) monthlyDone.toFloat() / monthlyTotal.toFloat() else 0f

                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (isMonthlyGoalsExpanded) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().clickable { isMonthlyGoalsExpanded = !isMonthlyGoalsExpanded }.padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Monthly Goals", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                                            Text(text = "▲", fontSize = 18.sp, color = RidgelineBlue)
                                        }
                                    } else {
                                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { isMonthlyGoalsExpanded = !isMonthlyGoalsExpanded },
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(50))
                                                        .background(RidgelineBlue)
                                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Text(text = "Monthly", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                                                }
                                                if (monthlyTotal > 0) {
                                                    Text(
                                                        text = "$monthlyDone / $monthlyTotal",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MidnightSlate.copy(alpha = 0.7f)
                                                    )
                                                } else {
                                                    Text(
                                                        text = "None set",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MidnightSlate.copy(alpha = 0.5f)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                                if (monthlyTotal > 0) {
                                                    Box(
                                                        modifier = Modifier
                                                            .width(48.dp)
                                                            .height(4.dp)
                                                            .clip(RoundedCornerShape(50))
                                                            .background(MistBlue.copy(alpha = 0.35f))
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxHeight()
                                                                .fillMaxWidth(monthlyFraction.coerceIn(0.02f, 1f))
                                                                .clip(RoundedCornerShape(50))
                                                                .background(MetricGreen)
                                                        )
                                                    }
                                                }
                                                Text(text = "▼", fontSize = 16.sp, color = RidgelineBlue)
                                            }
                                            // Goal pill rows beneath the slim header.
                                            if (monthlyGoalsForMonth.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    monthlyGoalsForMonth.forEach { goal ->
                                                        val isDone = goal.isCompleted
                                                        val stepTotal = goal.steps.size
                                                        val stepDone = goal.steps.count { it.isCompleted }
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .clip(RoundedCornerShape(50))
                                                                .background(if (isDone) RidgelineBlue.copy(alpha = 0.5f) else RidgelineBlue)
                                                                .clickable { isMonthlyGoalsExpanded = true }
                                                                .padding(horizontal = 14.dp, vertical = 6.dp),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = goal.title,
                                                                style = MaterialTheme.typography.labelMedium,
                                                                color = PureWhite,
                                                                maxLines = 1,
                                                                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                            if (stepTotal > 0 && !isDone) {
                                                                Text(
                                                                    text = "$stepDone/$stepTotal",
                                                                    style = MaterialTheme.typography.labelSmall,
                                                                    color = PureWhite.copy(alpha = 0.85f)
                                                                )
                                                            } else if (isDone) {
                                                                Text("✓", color = PureWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = isMonthlyGoalsExpanded) {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().pointerInput(selectedGoalMonth) {
                                                detectHorizontalDragGestures(
                                                    onDragStart = { _: Offset -> goalMonthSwipeAccumulator = 0f },
                                                    onHorizontalDrag = { change: PointerInputChange, dragAmount: Float -> change.consume(); goalMonthSwipeAccumulator += dragAmount },
                                                    onDragEnd = {
                                                        val minimumSwipeThresholdPx = 140f
                                                        if (goalMonthSwipeAccumulator < -minimumSwipeThresholdPx) {
                                                            selectedGoalMonth = selectedGoalMonth.plusMonths(1)
                                                        } else if (goalMonthSwipeAccumulator > minimumSwipeThresholdPx) {
                                                            selectedGoalMonth = selectedGoalMonth.minusMonths(1)
                                                        }
                                                    }
                                                )
                                            }
                                        ) {
                                            AnimatedContent(
                                                targetState = selectedGoalMonth,
                                                transitionSpec = {
                                                    if (targetState.isAfter(initialState)) {
                                                        slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                                                    } else {
                                                        slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                                                    }
                                                },
                                                label = "MonthlyGoalsTransition"
                                            ) { targetMonth ->
                                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                                                    val tMonthLabel = "${targetMonth.month.getDisplayName(DateTimeTextStyle.FULL, Locale.getDefault())} ${targetMonth.year}"
                                                    Text(text = tMonthLabel, color = RidgelineBlue, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 12.dp))

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clip(RoundedCornerShape(50))
                                                            .background(CardInset)
                                                            .border(1.dp, BorderGray.copy(alpha = 0.6f), RoundedCornerShape(50))
                                                            .clickable {
                                                                draftGoalType = "MONTHLY"
                                                                draftGoalTitle = ""
                                                                draftGoalDeadline = targetMonth.atEndOfMonth()
                                                                draftGoalReward = ""
                                                                draftGoalVision = ""
                                                                draftGoalSteps.clear()
                                                                draftNewStepText = ""
                                                                editingGoalId = null
                                                                showGoalWizardDialog = true
                                                            }
                                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Icon(Icons.Outlined.Add, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(18.dp))
                                                        Text("Set a monthly goal…", style = MaterialTheme.typography.titleLarge, color = TextHint)
                                                    }

                                                    Spacer(modifier = Modifier.height(16.dp))

                                                    val currentGoals = goalEntries.filter { it.type == "MONTHLY" && it.monthAnchor == targetMonth }.sortedBy { goal ->
                                                        if (goal.isCompleted) 1 else 0
                                                    }

                                                    if (currentGoals.isEmpty()) {
                                                        Text(text = "No monthly goals mapped out yet.", color = Color.Gray, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 16.dp))
                                                    } else {
                                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                                                            currentGoals.forEach { goal ->
                                                                val now = LocalDate.now()
                                                                val isGoalLate = goal.deadlineDate?.isBefore(now) == true && !goal.isCompleted
                                                                val isGoalSoon = goal.deadlineDate?.isEqual(now) == true || goal.deadlineDate?.isEqual(now.plusDays(1)) == true

                                                                Card(
                                                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                                                    shape = RoundedCornerShape(14.dp),
                                                                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray.copy(alpha=0.5f), RoundedCornerShape(14.dp))
                                                                ) {
                                                                    Column(modifier = Modifier.padding(12.dp)) {
                                                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f).clickable {
                                                                                val idx = goalEntries.indexOfFirst { it.id == goal.id }
                                                                                if (idx != -1) {
                                                                                    if (!goalEntries[idx].isCompleted) {
                                                                                        goalPendingCompletion = goalEntries[idx]
                                                                                    } else {
                                                                                        goalEntries[idx] = goalEntries[idx].copy(isCompleted = false)
                                                                                        removeElevation(ELEVATION_STEP); recordActivity()
                                                                                    }
                                                                                }
                                                                            }) {
                                                                                // Circle checkbox, matching priorities and to-dos on the Lists page.
                                                                                if (goal.isCompleted) {
                                                                                    Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Completed", tint = SuccessGreen, modifier = Modifier.size(24.dp))
                                                                                } else {
                                                                                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).border(2.dp, RidgelineBlue.copy(alpha = 0.6f), CircleShape))
                                                                                }
                                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                                Column {
                                                                                    Text(text = goal.title, style = MaterialTheme.typography.titleLarge, color = if (goal.isCompleted) Color.Gray else MidnightSlate, textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None)
                                                                                    if (goal.deadlineDate != null) {
                                                                                        Text("Due: ${goal.deadlineDate!!.monthValue}/${goal.deadlineDate!!.dayOfMonth}", style = MaterialTheme.typography.bodyMedium, color = if (goal.isCompleted) Color.Gray else if (isGoalLate) Color.Red else if (isGoalSoon) MetricOrange else RidgelineBlue)
                                                                                    }
                                                                                }
                                                                            }

                                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit goal", tint = RidgelineBlue, modifier = Modifier.clickable {
                                                                                    editingGoalId = goal.id
                                                                                    draftGoalType = "MONTHLY"
                                                                                    draftGoalTitle = goal.title
                                                                                    draftGoalDeadline = goal.deadlineDate
                                                                                    draftGoalReward = goal.reward
                                                                                    draftGoalVision = goal.futureVision
                                                                                    draftGoalSteps.clear()
                                                                                    draftGoalSteps.addAll(goal.steps)
                                                                                    draftNewStepText = ""
                                                                                    showGoalWizardDialog = true
                                                                                }.padding(8.dp).size(20.dp))
                                                                                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Delete goal", tint = Color.Gray.copy(alpha = 0.6f), modifier = Modifier.clickable { goalPendingDelete = goal }.padding(8.dp).size(20.dp))
                                                                            }
                                                                        }

                                                                        // Episodic future thinking: re-surface the vivid finish picture above the
                                                                        // steps, so the user re-reads it right before engaging with the work.
                                                                        if (goal.futureVision.isNotBlank()) {
                                                                            Spacer(modifier = Modifier.height(8.dp))
                                                                            Box(
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth()
                                                                                    .clip(RoundedCornerShape(14.dp))
                                                                                    .background(NeutralFill)
                                                                                    .padding(12.dp)
                                                                            ) {
                                                                                Column {
                                                                                    Text("✨ When this is done", style = MaterialTheme.typography.bodyMedium, color = RidgelineBlue)
                                                                                    Spacer(modifier = Modifier.height(2.dp))
                                                                                    Text(goal.futureVision, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                                                                                }
                                                                            }
                                                                        }

                                                                        if (goal.steps.isNotEmpty()) {
                                                                            Spacer(modifier = Modifier.height(8.dp))
                                                                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = 24.dp)) {
                                                                                goal.steps.forEach { step ->
                                                                                    val isStepLate = step.deadlineDate?.isBefore(now) == true && !step.isCompleted
                                                                                    val isStepSoon = step.deadlineDate?.isEqual(now) == true || step.deadlineDate?.isEqual(now.plusDays(1)) == true
                                                                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable {
                                                                                        val gIdx = goalEntries.indexOfFirst { it.id == goal.id }
                                                                                        if (gIdx != -1) {
                                                                                            val sIdx = goalEntries[gIdx].steps.indexOfFirst { it.id == step.id }
                                                                                            if (sIdx != -1) {
                                                                                                val updatedSteps = goalEntries[gIdx].steps.toMutableList()
                                                                                                val sToggled = !updatedSteps[sIdx].isCompleted
                                                                                                updatedSteps[sIdx] = updatedSteps[sIdx].copy(isCompleted = sToggled)
                                                                                                goalEntries[gIdx] = goalEntries[gIdx].copy(steps = updatedSteps)
                                                                                                if (sToggled) { awardElevation(ELEVATION_STEP, "✨ Step done · +$ELEVATION_STEP ft") } else { removeElevation(ELEVATION_STEP); recordActivity() }
                                                                                            }
                                                                                        }
                                                                                    }.padding(vertical = 4.dp)) {
                                                                                        if (step.isCompleted) {
                                                                                            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Completed", tint = SuccessGreen, modifier = Modifier.size(24.dp))
                                                                                        } else {
                                                                                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).border(2.dp, MidnightSlate.copy(alpha = 0.35f), CircleShape))
                                                                                        }
                                                                                        Spacer(modifier = Modifier.width(10.dp))
                                                                                        Text(text = step.text, style = MaterialTheme.typography.bodyLarge, color = if (step.isCompleted) Color.Gray else MidnightSlate, textDecoration = if (step.isCompleted) TextDecoration.LineThrough else TextDecoration.None, modifier = Modifier.weight(1f))
                                                                                        if (step.deadlineDate != null) {
                                                                                            Text("${step.deadlineDate!!.monthValue}/${step.deadlineDate!!.dayOfMonth}", style = MaterialTheme.typography.bodyMedium, color = if (step.isCompleted) Color.Gray else if (isStepLate) Color.Red else if (isStepSoon) MetricOrange else RidgelineBlue)
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        if (goal.reward.isNotBlank()) {
                                                                            Spacer(modifier = Modifier.height(10.dp))
                                                                            val totalSteps = goal.steps.size
                                                                            val completedSteps = goal.steps.count { it.isCompleted }
                                                                            val isFullyDone = goal.isCompleted
                                                                            val progressAmt = if (isFullyDone) 1f else if (totalSteps > 0) completedSteps.toFloat() / totalSteps.toFloat() else 0f

                                                                            Box(
                                                                                modifier = Modifier.fillMaxWidth().height(36.dp).clip(RoundedCornerShape(50)).background(Color.LightGray.copy(alpha = 0.3f))
                                                                            ) {
                                                                                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progressAmt.coerceAtLeast(0.01f)).background(if (isFullyDone) RidgelineBlue.copy(alpha = 0.6f) else MistBlue.copy(alpha = 0.6f)))
                                                                                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                                                        Text(if(isFullyDone) "🏆 EARNED: " else "🎁 REWARD: ", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium, color = if(isFullyDone) RidgelineBlue else MidnightSlate)
                                                                                        Text(goal.reward, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, textDecoration = if (isFullyDone) TextDecoration.LineThrough else TextDecoration.None)
                                                                                    }
                                                                                    if (totalSteps > 0) {
                                                                                        Text(text = "$completedSteps/$totalSteps", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium, color = if (isFullyDone) RidgelineBlue else MidnightSlate.copy(alpha = 0.6f))
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            var inlineRewardText by remember(goal.id) { mutableStateOf("") }
                                                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                                                RewardChipsRow(
                                                                                    bank = rewardBank,
                                                                                    mediumOnly = true,
                                                                                    onPick = { inlineRewardText = it }
                                                                                )
                                                                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                                                    OutlinedTextField(
                                                                                        value = inlineRewardText,
                                                                                        onValueChange = { inlineRewardText = it },
                                                                                        placeholder = { Text("Set a reward for this goal...") },
                                                                                        singleLine = true,
                                                                                        modifier = Modifier.weight(1f).height(48.dp),
                                                                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                                                                        shape = RoundedCornerShape(50)
                                                                                    )
                                                                                    Spacer(modifier = Modifier.width(8.dp))
                                                                                    Button(
                                                                                        onClick = {
                                                                                            if (inlineRewardText.isNotBlank()) {
                                                                                                val gIdx = goalEntries.indexOfFirst { it.id == goal.id }
                                                                                                if (gIdx != -1) {
                                                                                                    goalEntries[gIdx] = goalEntries[gIdx].copy(reward = inlineRewardText.trim())
                                                                                                    recordActivity()
                                                                                                    showNotification("Reward Saved!", "Keep up the momentum!", RidgelineBlue, "🎁")
                                                                                                }
                                                                                            }
                                                                                        },
                                                                                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                                                                        shape = RoundedCornerShape(50),
                                                                                        modifier = Modifier.height(48.dp)
                                                                                    ) { Text("Save") }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "LogInsight" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Insights",
                                style = MaterialTheme.typography.displaySmall,
                                color = RidgelineBlue,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f)
                            )
                            // Reward bank editor entry — gear icon. The app has no settings screen,
                            // so the bank editor is anchored here as the least-cramped page header.
                            // Tap opens the same wizard the user saw on first launch, pre-populated.
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable { showRewardBankEditor = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Settings",
                                    tint = RidgelineBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // --- DAILY REFLECTION card ---
                        // One rotating prompt per day, answered in a sentence at a calm moment. This
                        // replaces the old manual behavior log. If already answered today, the saved
                        // response shows with an edit affordance.
                        run {
                            val todayDate = LocalDate.now()
                            val todaysPrompt = reflectionPromptForDate(todayDate)
                            val existingToday = reflectionEntries.firstOrNull { it.date == todayDate }
                            var reflectionDraft by remember(existingToday?.id, todaysPrompt.text) { mutableStateOf(existingToday?.response ?: "") }
                            var isEditingReflection by remember(existingToday?.id) { mutableStateOf(existingToday == null) }

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                                Icon(imageVector = Icons.Outlined.EditNote, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(22.dp))
                                Text("Today's reflection", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                            }
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(todaysPrompt.text, style = MaterialTheme.typography.titleMedium, color = MidnightSlate)
                                    // Optional link to the Trail Note this prompt draws on (learning loop).
                                    if (todaysPrompt.linkedNoteId != null) {
                                        val linkedNote = SEED_TRAIL_NOTES.firstOrNull { it.id == todaysPrompt.linkedNoteId }
                                        if (linkedNote != null) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.clip(RoundedCornerShape(50)).clickable { openTrailNote = linkedNote }.padding(vertical = 2.dp)) {
                                                Icon(Icons.Outlined.MenuBook, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(14.dp))
                                                Text("Read the lesson behind this", style = MaterialTheme.typography.labelMedium, color = RidgelineBlue, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    if (isEditingReflection) {
                                        OutlinedTextField(
                                            value = reflectionDraft, onValueChange = { reflectionDraft = it },
                                            placeholder = { Text("A sentence is plenty…") },
                                            modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 5,
                                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardInset, unfocusedContainerColor = CardInset, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Button(
                                                onClick = {
                                                    val resp = reflectionDraft.trim()
                                                    if (resp.isNotBlank()) {
                                                        val existingIdx = reflectionEntries.indexOfFirst { it.date == todayDate }
                                                        if (existingIdx != -1) reflectionEntries[existingIdx] = reflectionEntries[existingIdx].copy(response = resp, prompt = todaysPrompt.text)
                                                        else reflectionEntries.add(ReflectionEntry(date = todayDate, prompt = todaysPrompt.text, response = resp))
                                                        recordActivity()
                                                        isEditingReflection = false
                                                    }
                                                },
                                                enabled = reflectionDraft.isNotBlank(),
                                                colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                                shape = RoundedCornerShape(50)
                                            ) { Text("Save", fontWeight = FontWeight.Bold) }
                                            if (existingToday != null) {
                                                TextButton(onClick = { reflectionDraft = existingToday.response; isEditingReflection = false }) { Text("Cancel", color = TextMuted) }
                                            }
                                        }
                                    } else {
                                        Text(existingToday?.response ?: "", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                            Text("Saved for today", style = MaterialTheme.typography.labelSmall, color = SuccessGreen)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Edit", style = MaterialTheme.typography.labelMedium, color = RidgelineBlue, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { isEditingReflection = true }.padding(4.dp))
                                        }
                                    }
                                }
                            }
                            // Recent reflections — a light peek at the last few entries.
                            val recentReflections = reflectionEntries.filter { it.date != todayDate }.sortedByDescending { it.date }.take(3)
                            if (recentReflections.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text("Recent reflections", style = MaterialTheme.typography.labelMedium, color = MidnightSlate.copy(alpha = 0.6f), modifier = Modifier.padding(start = 4.dp, bottom = 6.dp))
                                recentReflections.forEach { entry ->
                                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clip(RoundedCornerShape(12.dp)).background(CardInset).padding(12.dp)) {
                                        Text("${entry.date.monthValue}/${entry.date.dayOfMonth}  ·  ${entry.prompt}", style = MaterialTheme.typography.labelSmall, color = MidnightSlate.copy(alpha = 0.55f))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(entry.response, style = MaterialTheme.typography.bodySmall, color = MidnightSlate)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // --- TRAIL NOTES card ---
                        // Surfaces the next unlocked micro-lesson (self-paced). Shows progress as
                        // "trail markers" tied to the mountain theme. Tapping opens the reader; a
                        // small link opens the full library to re-read finished notes.
                        run {
                            val sequence = trailSequenceForStage(journeyStage)
                            val nextNote = sequence.firstOrNull { it.id !in trailNotesSeen }
                            val doneCount = sequence.count { it.id in trailNotesSeen }
                            val totalCount = sequence.size
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(22.dp))
                                    Text("Trail Notes", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                                }
                                if (totalCount > 0) {
                                    Text(
                                        text = "$doneCount / $totalCount",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MidnightSlate.copy(alpha = 0.5f)
                                    )
                                }
                            }
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    if (nextNote != null) {
                                        Text(
                                            text = if (doneCount == 0) "Start here" else "Next note",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = RidgelineBlue
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = nextNote.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MidnightSlate
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = { openTrailNote = nextNote },
                                            colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                            shape = RoundedCornerShape(50),
                                            modifier = Modifier.fillMaxWidth().height(46.dp)
                                        ) { Text("Read", fontWeight = FontWeight.Bold) }
                                    } else {
                                        // All caught up.
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Icon(imageVector = Icons.Outlined.Terrain, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(18.dp))
                                            Text(
                                                text = "You've read every Trail Note",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MidnightSlate
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "More are on the way. Revisit any from the library.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MidnightSlate.copy(alpha = 0.6f)
                                        )
                                    }
                                    if (doneCount > 0) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Browse all notes",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = RidgelineBlue,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .clickable { showTrailLibrary = true }
                                                .padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Your patterns", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Header row: title on the left, window-toggle pills on the right.
                                // The title updates to match the window so the user always knows
                                // what date range the heatmap is showing.
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Activity", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MidnightSlate)
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf("30" to "30d", "90" to "90d", "ALL" to "All").forEach { (value, label) ->
                                            val selected = heatmapWindow == value
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(50))
                                                    .background(if (selected) RidgelineBlue else Color.LightGray.copy(alpha = 0.3f))
                                                    .clickable { heatmapWindow = value }
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = label,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = if (selected) PureWhite else MidnightSlate
                                                )
                                            }
                                        }
                                    }
                                }
                                Text(
                                    text = when (heatmapWindow) {
                                        "30" -> "Last 30 days"
                                        "90" -> "Last 90 days"
                                        else -> "All-time"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MidnightSlate.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
                                val today = LocalDate.now()
                                val activitySet = activityDates.toSet()
                                val weeksToShow = when (heatmapWindow) { "30" -> 6; "90" -> 13; else -> 20 }
                                val thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                val startMonday = thisMonday.minusWeeks((weeksToShow - 1).toLong())
                                // Week × day activity grid (contribution-graph style). Rows = days of
                                // week, columns = weeks. A cell is filled blue if there was any
                                // activity (a completion or reflection) that day. Future days are blank.
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Column(modifier = Modifier.width(30.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        daysOfWeek.forEach { day ->
                                            Box(modifier = Modifier.height(16.dp), contentAlignment = Alignment.CenterStart) {
                                                Text(day.name.take(1), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                                            }
                                        }
                                    }
                                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                        for (w in 0 until weeksToShow) {
                                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                                for (d in 0 until 7) {
                                                    val cellDate = startMonday.plusWeeks(w.toLong()).plusDays(d.toLong())
                                                    val isFuture = cellDate.isAfter(today)
                                                    val active = activitySet.contains(cellDate.toEpochDay())
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(16.dp)
                                                            .clip(RoundedCornerShape(3.dp))
                                                            .background(
                                                                when {
                                                                    isFuture -> Color.Transparent
                                                                    active -> RidgelineBlue
                                                                    else -> NeutralFill
                                                                }
                                                            )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Pattern stat cards — auto-derived from activity data (no manual input):
                        // current streak, active days this month, and most active day of the week.
                        run {
                            val today = LocalDate.now()
                            val activitySet = activityDates.toSet()
                            val activeLast30 = (0 until 30).count { activitySet.contains(today.minusDays(it.toLong()).toEpochDay()) }
                            // Most active day of the week over the last ~12 weeks.
                            val dayCounts = mutableMapOf<DayOfWeek, Int>()
                            (0 until 84).forEach { offset ->
                                val d = today.minusDays(offset.toLong())
                                if (activitySet.contains(d.toEpochDay())) dayCounts[d.dayOfWeek] = (dayCounts[d.dayOfWeek] ?: 0) + 1
                            }
                            val topDay = dayCounts.maxByOrNull { it.value }?.key
                            val topDayLabel = topDay?.name?.take(3)?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "—"
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Column(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(vertical = 14.dp, horizontal = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("$streak", style = MaterialTheme.typography.headlineMedium, color = RidgelineBlue)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("day streak", style = MaterialTheme.typography.labelSmall, color = MidnightSlate.copy(alpha = 0.65f), textAlign = TextAlign.Center)
                                }
                                Column(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(vertical = 14.dp, horizontal = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("$activeLast30", style = MaterialTheme.typography.headlineMedium, color = RidgelineBlue)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("active days (30d)", style = MaterialTheme.typography.labelSmall, color = MidnightSlate.copy(alpha = 0.65f), textAlign = TextAlign.Center)
                                }
                                Column(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).padding(vertical = 14.dp, horizontal = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(topDayLabel, style = MaterialTheme.typography.headlineMedium, color = RidgelineBlue)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("top day", style = MaterialTheme.typography.labelSmall, color = MidnightSlate.copy(alpha = 0.65f), textAlign = TextAlign.Center)
                                }
                            }
                        }

                        // Clinical disclaimer. This is a Ridgeline Counseling product positioned as
                        // an education-and-organization adjunct to care, not a treatment or a
                        // diagnostic tool. Kept low-key (small, muted) so it informs without alarming.
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Ridgeline is a tool for planning and self-reflection, not a substitute for professional care, diagnosis, or treatment. If you're struggling, reach out to a licensed provider.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MidnightSlate.copy(alpha = 0.45f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            } // END OF currentScreen WHEN BLOCK

            // ==========================================
            // --- CUSTOM NOTIFICATION OVERLAY ---
            // ==========================================
            AnimatedVisibility(
                visible = customNotification != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .zIndex(100f)
            ) {
                customNotification?.let { notif ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = notif.bgColor),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().clickable { customNotification = null }
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = notif.icon, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = notif.title, fontWeight = FontWeight.Black, fontSize = 16.sp, color = PureWhite)
                                Text(text = notif.message, fontSize = 14.sp, color = PureWhite.copy(alpha = 0.9f))
                            }
                        }
                    }
                }
            }
        } // END OF INNER PADDING BOX

        // =========================================================================
        // --- GLOBAL OVERLAYS & DIALOGS SECTION ---
        // =========================================================================

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedCalDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                            if (activeDeadlineSelectionId == "goal_root") {
                                draftGoalDeadline = selectedCalDate
                            } else {
                                val sIdx = draftGoalSteps.indexOfFirst { it.id == activeDeadlineSelectionId }
                                if (sIdx != -1) draftGoalSteps[sIdx] = draftGoalSteps[sIdx].copy(deadlineDate = selectedCalDate)
                            }
                        }
                        showDatePicker = false
                        activeDeadlineSelectionId = null
                    }) { Text("OK", fontWeight = FontWeight.Bold, color = RidgelineBlue) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = MidnightSlate) }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showGoalWizardDialog) {
            Dialog(onDismissRequest = { showGoalWizardDialog = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.90f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(if (editingGoalId == null) "New Goal Builder" else "Edit Goal", style = MaterialTheme.typography.headlineMedium)
                            IconButton(onClick = { showGoalWizardDialog = false }) { Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = MidnightSlate) }
                        }

                        Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            OutlinedTextField(
                                value = draftGoalTitle, onValueChange = { draftGoalTitle = it }, label = { Text("Core Objective") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                            )

                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Text("Goal Deadline: ", style = MaterialTheme.typography.titleSmall, color = MidnightSlate)
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.clip(RoundedCornerShape(50)).background(RidgelineBlue).clickable { activeDeadlineSelectionId = if (activeDeadlineSelectionId == "goal_root") null else "goal_root" }.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    if (draftGoalDeadline != null) {
                                        Text(text = "${draftGoalDeadline!!.monthValue}/${draftGoalDeadline!!.dayOfMonth}", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null, tint = PureWhite, modifier = Modifier.size(14.dp))
                                            Text("Set Date", style = MaterialTheme.typography.labelMedium, color = PureWhite)
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(visible = activeDeadlineSelectionId == "goal_root") {
                                if (draftGoalType == "WEEKLY") {
                                    val today = LocalDate.now()
                                    val dateOptions = mutableListOf<Pair<String, LocalDate?>>()
                                    for (i in 0..6) {
                                        val d = selectedGoalWeek.plusDays(i.toLong())
                                        val dayName = d.dayOfWeek.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault())
                                        dateOptions.add(Pair("$dayName ${d.monthValue}/${d.dayOfMonth}", d))
                                    }
                                    dateOptions.add(Pair("Clear", null))

                                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        dateOptions.forEach { (label, dateVal) ->
                                            val isSelected = draftGoalDeadline == dateVal && dateVal != null
                                            val isToday = dateVal == today

                                            val bgColor = when {
                                                isSelected -> RidgelineBlue
                                                isToday -> IceBlueAccent
                                                else -> Color.LightGray.copy(alpha=0.4f)
                                            }
                                            val textColor = if (isSelected) PureWhite else MidnightSlate

                                            Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(bgColor).clickable { draftGoalDeadline = dateVal; activeDeadlineSelectionId = null }.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                                Text(label, style = MaterialTheme.typography.labelMedium, color = textColor)
                                            }
                                        }
                                    }
                                } else {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(IceBlueAccent).clickable { showDatePicker = true }.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) { Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(14.dp)); Text("Open Calendar", style = MaterialTheme.typography.labelMedium, color = RidgelineBlue) }
                                        }
                                        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.LightGray.copy(alpha=0.4f)).clickable { draftGoalDeadline = null; activeDeadlineSelectionId = null }.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                            Text("Clear", style = MaterialTheme.typography.labelMedium, color = MidnightSlate)
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                            Text("What steps need to be accomplished?", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)

                            draftGoalSteps.forEach { step ->
                                Column(modifier = Modifier.fillMaxWidth().background(PureWhite, RoundedCornerShape(14.dp)).border(1.dp, Color.LightGray.copy(alpha=0.4f), RoundedCornerShape(14.dp)).padding(10.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Text(step.text, fontSize = 13.sp, color = MidnightSlate, modifier = Modifier.weight(1f))
                                        Box(modifier = Modifier.clip(RoundedCornerShape(50)).background(IceBlueAccent).clickable { activeDeadlineSelectionId = if (activeDeadlineSelectionId == step.id) null else step.id }.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                            if (step.deadlineDate != null) {
                                                Text(text = "${step.deadlineDate!!.monthValue}/${step.deadlineDate!!.dayOfMonth}", style = MaterialTheme.typography.labelSmall, color = RidgelineBlue)
                                            } else {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                                    Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(12.dp))
                                                    Text("Date", style = MaterialTheme.typography.labelSmall, color = RidgelineBlue)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Remove step", tint = Color.LightGray, modifier = Modifier.clickable { draftGoalSteps.removeAll { it.id == step.id }; if (activeDeadlineSelectionId == step.id) activeDeadlineSelectionId = null }.padding(4.dp).size(18.dp))
                                    }

                                    AnimatedVisibility(visible = activeDeadlineSelectionId == step.id) {
                                        if (draftGoalType == "WEEKLY") {
                                            val today = LocalDate.now()
                                            val dateOptions = mutableListOf<Pair<String, LocalDate?>>()
                                            for (i in 0..6) {
                                                val d = selectedGoalWeek.plusDays(i.toLong())
                                                val dayName = d.dayOfWeek.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault())
                                                dateOptions.add(Pair("$dayName ${d.monthValue}/${d.dayOfMonth}", d))
                                            }
                                            dateOptions.add(Pair("Clear", null))

                                            Row(modifier = Modifier.fillMaxWidth().padding(top=8.dp).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                dateOptions.forEach { (label, dateVal) ->
                                                    val isSelected = step.deadlineDate == dateVal && dateVal != null
                                                    val isToday = dateVal == today

                                                    val bgColor = when {
                                                        isSelected -> RidgelineBlue
                                                        isToday -> IceBlueAccent
                                                        else -> Color.LightGray.copy(alpha=0.4f)
                                                    }
                                                    val textColor = if (isSelected) PureWhite else MidnightSlate

                                                    Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(bgColor).clickable {
                                                        val sIdx = draftGoalSteps.indexOfFirst { it.id == step.id }
                                                        if (sIdx != -1) draftGoalSteps[sIdx] = draftGoalSteps[sIdx].copy(deadlineDate = dateVal)
                                                        activeDeadlineSelectionId = null
                                                    }.padding(horizontal = 10.dp, vertical = 4.dp)) {
                                                        Text(label, style = MaterialTheme.typography.labelSmall, color = textColor)
                                                    }
                                                }
                                            }
                                        } else {
                                            Row(modifier = Modifier.fillMaxWidth().padding(top=8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(IceBlueAccent).clickable { showDatePicker = true }.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) { Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(14.dp)); Text("Open Calendar", style = MaterialTheme.typography.labelMedium, color = RidgelineBlue) }
                                                }
                                                Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.LightGray.copy(alpha=0.4f)).clickable {
                                                    val sIdx = draftGoalSteps.indexOfFirst { it.id == step.id }
                                                    if (sIdx != -1) draftGoalSteps[sIdx] = draftGoalSteps[sIdx].copy(deadlineDate = null)
                                                    activeDeadlineSelectionId = null
                                                }.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                                    Text("Clear", style = MaterialTheme.typography.labelMedium, color = MidnightSlate)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = draftNewStepText, onValueChange = { draftNewStepText = it }, placeholder = { Text("Add task step item...", fontSize = 13.sp) }, singleLine = true, modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = CardInset, unfocusedContainerColor = CardInset, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.Transparent), shape = RoundedCornerShape(50)
                                )
                                Box(
                                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(50)).background(RidgelineBlue).clickable {
                                        if (draftNewStepText.isNotBlank()) {
                                            draftGoalSteps.add(GoalStep(text = draftNewStepText.trim()))
                                            draftNewStepText = ""
                                        }
                                    }, contentAlignment = Alignment.Center
                                ) { Text("+", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                            }

                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Set a Reward", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                                WhyChip(WHY_REWARD_TITLE, WHY_REWARD_BODY)
                            }
                            OutlinedTextField(
                                value = draftGoalReward, onValueChange = { draftGoalReward = it }, placeholder = { Text("What's the prize for finishing?") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            // Episodic future thinking prompt — encourage a vivid, sensory picture
                            // of the finished goal. Multi-line; re-surfaced when engaging the goal.
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Picture finishing this", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                                WhyChip(WHY_FUTURE_VISION_TITLE, WHY_FUTURE_VISION_BODY)
                            }
                            OutlinedTextField(
                                value = draftGoalVision, onValueChange = { draftGoalVision = it },
                                placeholder = { Text("What does that moment look like? Where are you, who's there, how does it feel?") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (draftGoalTitle.isNotBlank()) {
                                    val finalGoal = GoalEntry(
                                        id = editingGoalId ?: java.util.UUID.randomUUID().toString(),
                                        title = draftGoalTitle.trim(),
                                        steps = draftGoalSteps.toList(),
                                        deadlineDate = draftGoalDeadline,
                                        reward = draftGoalReward.trim(),
                                        weekAnchor = if (draftGoalType == "WEEKLY") selectedGoalWeek else null,
                                        monthAnchor = if (draftGoalType == "MONTHLY") selectedGoalMonth else null,
                                        type = draftGoalType,
                                        futureVision = draftGoalVision.trim()
                                    )
                                    if (editingGoalId != null) {
                                        val gIdx = goalEntries.indexOfFirst { it.id == editingGoalId }
                                        if (gIdx != -1) goalEntries[gIdx] = finalGoal
                                    } else {
                                        goalEntries.add(finalGoal)
                                    }
                                    showGoalWizardDialog = false
                                    recordActivity()
                                    showNotification("Goal Saved", "Your objective has been locked in.", RidgelineBlue, "🎯")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) { Text("Save Goal", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }



        // Read-only inspect for a device-calendar event. No edit/delete/complete — these live in
        // the user's actual calendar app, not here.
        externalEventToInspect?.let { ext ->
            val zone = ZoneId.systemDefault()
            val startDt = Instant.ofEpochMilli(ext.startMillis).atZone(zone)
            val endDt = Instant.ofEpochMilli(ext.endMillis).atZone(zone)
            Dialog(onDismissRequest = { externalEventToInspect = null }) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Outlined.Event, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (ext.calendarName.isNotBlank()) ext.calendarName else "Calendar event",
                                style = MaterialTheme.typography.labelMedium,
                                color = RidgelineBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(ext.title, style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                        Spacer(modifier = Modifier.height(12.dp))
                        if (ext.isAllDay) {
                            Text("All day", fontWeight = FontWeight.Bold, color = RidgelineBlue, fontSize = 13.sp)
                        } else {
                            Text(
                                "${formatTimeLabel(startDt.hour, startDt.minute)} – ${formatTimeLabel(endDt.hour, endDt.minute)}",
                                fontWeight = FontWeight.Bold, color = RidgelineBlue, fontSize = 13.sp
                            )
                        }
                        if (ext.location.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(ext.location, fontSize = 13.sp, color = MidnightSlate.copy(alpha = 0.7f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "From your device calendar — edit it in your calendar app.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MidnightSlate.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { externalEventToInspect = null },
                            colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.align(Alignment.End)
                        ) { Text("Close", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }

        if (activeInspectEntry != null) {
            val inspectTarget = activeInspectEntry!!
            val totalStartMins = inspectTarget.startHour * 60 + inspectTarget.startMinute
            val totalEndMins = (totalStartMins + inspectTarget.durationMins) % 1440
            val computedEndHour = totalEndMins / 60
            val computedEndMin = totalEndMins % 60

            // Cap the inspect dialog at 80% of screen height (the previous fixed height).
            // The Card wraps content vertically below that ceiling, so a short event renders compactly
            // and a long description grows until the cap, then becomes scrollable.
            val maxDialogHeightDp = (LocalConfiguration.current.screenHeightDp * 0.80f).dp

            Dialog(onDismissRequest = { activeInspectEntry = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .wrapContentHeight()
                        .heightIn(max = maxDialogHeightDp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                        IconButton(
                            onClick = { activeInspectEntry = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Close",
                                tint = MidnightSlate
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(24.dp)
                        ) {
                            Text(
                                text = inspectTarget.task,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 36.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Description region. Uses weight(1f, fill = false) so it takes up
                            // only what it needs when short, but expands to fill remaining height
                            // (and becomes scrollable) when long. Without fill = false the column
                            // would always be stretched to maxDialogHeightDp.
                            if (inspectTarget.notes.isNotBlank()) {
                                Text(
                                    text = inspectTarget.notes,
                                    fontSize = 15.sp,
                                    color = MidnightSlate.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .weight(1f, fill = false)
                                        .verticalScroll(rememberScrollState())
                                )
                            } else {
                                Text(
                                    text = "No description added.",
                                    fontSize = 15.sp,
                                    color = MidnightSlate.copy(alpha = 0.4f)
                                )
                            }

                            // Implementation-intention prompt. Shown only if the user set one,
                            // styled as a quoted block so it reads as a rehearsable phrase
                            // rather than another data field.
                            if (inspectTarget.triggerCue.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(NeutralFill)
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "When I ${inspectTarget.triggerCue}…",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = RidgelineBlue
                                    )
                                }
                            }

                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Start: ${formatTimeLabel(inspectTarget.startHour, inspectTarget.startMinute)}",
                                    fontWeight = FontWeight.Bold,
                                    color = RidgelineBlue,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "End: ${formatTimeLabel(computedEndHour, computedEndMin)}",
                                    fontWeight = FontWeight.Bold,
                                    color = RidgelineBlue,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { entryPendingDelete = inspectTarget }
                                ) {
                                    Text(
                                        text = "Delete Event",
                                        color = RidgelineBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }

                                Button(
                                    onClick = {
                                        editingEntry = inspectTarget
                                        dialogEventName = inspectTarget.task
                                        dialogEventNotes = inspectTarget.notes

                                        val sh12 = if (inspectTarget.startHour == 0 || inspectTarget.startHour == 12) 12 else if (inspectTarget.startHour > 12) inspectTarget.startHour - 12 else inspectTarget.startHour
                                        dialogStartHour = sh12.toString()
                                        dialogStartMin = String.format(Locale.US, "%02d", inspectTarget.startMinute)
                                        dialogStartAmPm = if (inspectTarget.startHour < 12) "AM" else "PM"

                                        dialogTimeMode = "DURATION"
                                        dialogDurationMins = inspectTarget.durationMins.toString()

                                        val eh12 = if (computedEndHour == 0 || computedEndHour == 12) 12 else if (computedEndHour > 12) computedEndHour - 12 else computedEndHour
                                        dialogEndHour = eh12.toString()
                                        dialogEndMin = String.format(Locale.US, "%02d", computedEndMin)
                                        dialogEndAmPm = if (computedEndHour < 12) "AM" else "PM"

                                        dialogSelectedColor = inspectTarget.blockColor
                                        dialogIsAllDay = inspectTarget.isAllDay
                                        // Reverse-derive the reminder preset from the stored absolute timestamp.
                                        // Check Morning of (9 AM same day) and Night before (8 PM previous day)
                                        // first; otherwise look for a minutes-before match against the preset list.
                                        // If none match (e.g. set via a future custom picker), null it so the user
                                        // can re-pick rather than seeing nothing selected.
                                        dialogReminderMinutesBefore = inspectTarget.reminderEpochMillis?.let { epoch ->
                                            val morningOfMillis = LocalDateTime.of(inspectTarget.date, LocalTime.of(9, 0))
                                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                            val nightBeforeMillis = LocalDateTime.of(inspectTarget.date.minusDays(1), LocalTime.of(20, 0))
                                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                            when {
                                                epoch == morningOfMillis -> REMINDER_PRESET_MORNING_OF
                                                epoch == nightBeforeMillis -> REMINDER_PRESET_NIGHT_BEFORE
                                                else -> {
                                                    val startMillis = LocalDateTime.of(
                                                        inspectTarget.date,
                                                        LocalTime.of(inspectTarget.startHour, inspectTarget.startMinute)
                                                    ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                                    val diffMins = ((startMillis - epoch) / 60000L).toInt()
                                                    if (diffMins in listOf(0, 5, 15, 30, 60)) diffMins else null
                                                }
                                            }
                                        }

                                        // triggerCue (implementation intention)
                                        dialogTriggerCue = inspectTarget.triggerCue

                                        activeInspectEntry = null
                                        showScheduleDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text("Edit", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // FAB chooser sheet. Opened from the Home "+" button. Each option resets the appropriate
        // state and launches the matching flow. Uses a full-screen Dialog with translucent scrim
        // and a vertically-stacked column of pill buttons positioned above where the FAB sits.
        if (showAddSheet) {
            // Rendered inline as a full-screen overlay rather than inside a Dialog window.
            // A Dialog(usePlatformDefaultWidth = false) whose root was a fillMaxSize scrim
            // failed to lay out on some devices — the window measured to zero, so nothing
            // appeared and the menu could not be dismissed (there was no way to flip
            // showAddSheet back to false). Inline, it measures against real screen
            // constraints and always draws. Back, the scrim, or the FAB all close it.
            BackHandler { showAddSheet = false }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // Tappable scrim — clicking anywhere on the dim area dismisses. Using
                        // clickable with indication = null suppresses the ripple so it feels like
                        // pure background, not an interactable surface.
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showAddSheet = false }
                        )
                        .background(Color.Black.copy(alpha = 0.25f))
                ) {
                    // Pill column anchored bottom-right. Padding accounts for the nav bar and
                    // the standard FAB position so the × button lines up where the + button sat.
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .navigationBarsPadding()
                            // 80dp NavigationBar + 16dp FAB padding; then 16dp end padding.
                            .padding(end = 16.dp, bottom = 96.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
                    ) {
                        // Helper: a pill-shaped button with icon + label on a primary background.
                        // Defined locally so it captures the dismiss-and-fire pattern in one place.
                        @Composable
                        fun PillOption(icon: ImageVector, label: String, onPick: () -> Unit) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        showAddSheet = false
                                        onPick()
                                    }
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(imageVector = icon, contentDescription = null, tint = PureWhite, modifier = Modifier.size(22.dp))
                                Text(label, style = MaterialTheme.typography.titleMedium, color = PureWhite)
                            }
                        }

                        PillOption(Icons.Outlined.Event, "Schedule an event") {
                            editingEntry = null
                            dialogEventName = ""
                            dialogEventNotes = ""
                            dialogSelectedColor = PureWhite
                            dialogIsAllDay = false
                            dialogReminderMinutesBefore = null

                            dialogTriggerCue = ""
                            dialogStartHour = ""
                            dialogStartMin = ""
                            dialogStartAmPm = "AM"
                            dialogTimeMode = "DURATION"
                            dialogDurationMins = "60"
                            dialogEndHour = ""
                            dialogEndMin = ""
                            dialogEndAmPm = "AM"
                            showScheduleDialog = true
                        }

                        PillOption(Icons.Outlined.Psychology, "Brain dump") {
                            wizardStep = 1
                            wizardRawInputText = ""
                            wizardBrainDumpList.clear()
                            wizardSelectedPriorities.clear()
                            wizardCurrentSchedulingIndex = 0
                            isWizardPlacementMode = false
                            showBrainDumpWizard = true
                        }

                        PillOption(Icons.Outlined.Description, "Add a to-do") {
                            quickTodoText = ""
                            showQuickTodoDialog = true
                        }

                        PillOption(Icons.Outlined.Timer, "Focus session") {
                            // General session — no specific task attached.
                            focusSetupTaskLabel = ""
                            focusSetupMinutes = 25
                            showFocusSetup = true
                        }

                        PillOption(Icons.Outlined.Flag, "New goal") {
                            editingGoalId = null
                            // Default to WEEKLY; the wizard has its own type toggle if the user wants monthly.
                            draftGoalType = "WEEKLY"
                            draftGoalTitle = ""
                            draftGoalDeadline = null
                            draftGoalReward = ""
                            draftGoalVision = ""
                            draftGoalSteps.clear()
                            draftNewStepText = ""
                            showGoalWizardDialog = true
                        }

                        // The real Scaffold FAB sits directly over this spot showing the "×"
                        // (its "+" rotated 45°) and closes the menu when tapped; this spacer
                        // reserves the same footprint so the pill stack lines up above it.
                        Spacer(modifier = Modifier.size(54.dp))
                    }
                }
        }

        // Quick "add to-do" dialog launched from the FAB chooser. Adds a single OtherTodoItem to
        // the currently-selected date and dismisses. For multiple to-dos, the Lists screen has
        // an inline input that's better suited.
        if (showQuickTodoDialog) {
            AlertDialog(
                onDismissRequest = { showQuickTodoDialog = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Add a to-do", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "For ${selectedDate.month.getDisplayName(DateTimeTextStyle.SHORT, Locale.getDefault())} ${selectedDate.dayOfMonth}",
                            style = MaterialTheme.typography.labelMedium,
                            color = RidgelineBlue
                        )
                        OutlinedTextField(
                            value = quickTodoText,
                            onValueChange = { quickTodoText = it },
                            placeholder = { Text("What needs doing?") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(50)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (quickTodoText.isNotBlank()) {
                                otherTodoEntries.add(
                                    OtherTodoItem(
                                        id = UUID.randomUUID().toString(),
                                        text = quickTodoText.trim(),
                                        date = selectedDate
                                    )
                                )
                                recordActivity()
                            }
                            showQuickTodoDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Add", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showQuickTodoDialog = false }) {
                        Text("Cancel", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Focus setup dialog — pick the work-chunk length, then start. The task label is
        // pre-filled when launched from a task; editable so a general session can be named.
        if (showFocusSetup) {
            AlertDialog(
                onDismissRequest = { showFocusSetup = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Outlined.Timer, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(22.dp))
                        Text("Focus session", fontWeight = FontWeight.Black, color = MidnightSlate)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Work in short bursts. Each chunk earns elevation, with a 5-minute break after.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MidnightSlate.copy(alpha = 0.75f)
                        )
                        OutlinedTextField(
                            value = focusSetupTaskLabel,
                            onValueChange = { focusSetupTaskLabel = it },
                            label = { Text("Focusing on (optional)") },
                            placeholder = { Text("e.g. write the report") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(50)
                        )
                        Text("Chunk length", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(10, 15, 25).forEach { mins ->
                                val selected = focusSetupMinutes == mins
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(if (selected) RidgelineBlue else Color.LightGray.copy(alpha = 0.3f))
                                        .clickable { focusSetupMinutes = mins }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${mins}m",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (selected) PureWhite else MidnightSlate,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            focusSession = FocusSession(
                                taskLabel = focusSetupTaskLabel.trim(),
                                workMinutes = focusSetupMinutes
                            )
                            focusChunkReward = null
                            showFocusSetup = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                        shape = RoundedCornerShape(50)
                    ) { Text("Start", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showFocusSetup = false }) {
                        Text("Cancel", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Full-screen focus overlay. Takes over while a session runs: a big countdown ring,
        // the task label, the phase (Focus / Break), a chunk counter, and pause/stop controls.
        // Minimal and calm by design — a busy screen during focus would defeat the purpose.
        focusSession?.let { session ->
            val totalSeconds = if (session.phase == FOCUS_PHASE_WORK) session.workMinutes * 60 else FOCUS_BREAK_MINUTES * 60
            val fraction = if (totalSeconds > 0) session.secondsLeft.toFloat() / totalSeconds.toFloat() else 0f
            val isWork = session.phase == FOCUS_PHASE_WORK
            val mins = session.secondsLeft / 60
            val secs = session.secondsLeft % 60
            Dialog(
                onDismissRequest = { /* Stop is explicit via the button — avoid accidental dismiss */ },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                // Extend the dialog window edge-to-edge so the focus background color fills behind
                // the status bar and navigation bar (rather than leaving them system-colored).
                // We grab the dialog's own Window via DialogWindowProvider and clear the
                // decor-fits-system-windows flag, then let the Box draw into the full area.
                val dialogView = LocalView.current
                LaunchedEffect(dialogView) {
                    val dialogWindow = (dialogView.parent as? DialogWindowProvider)?.window
                    if (dialogWindow != null) {
                        dialogWindow.setLayout(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        WindowCompat.setDecorFitsSystemWindows(dialogWindow, false)
                        // Transparent system bars so the focus color shows through behind them.
                        dialogWindow.statusBarColor = android.graphics.Color.TRANSPARENT
                        dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (isWork) RidgelineBlue else MidnightSlate),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        // Phase label.
                        Text(
                            text = if (isWork) "FOCUS" else "BREAK",
                            style = MaterialTheme.typography.labelLarge,
                            color = PureWhite.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Task label (if any).
                        if (session.taskLabel.isNotBlank()) {
                            Text(
                                text = session.taskLabel,
                                style = MaterialTheme.typography.titleLarge,
                                color = PureWhite,
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Countdown ring — Canvas arc that depletes as the phase progresses.
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val stroke = 16.dp.toPx()
                                val inset = stroke / 2f
                                // Track.
                                drawArc(
                                    color = PureWhite.copy(alpha = 0.18f),
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    topLeft = Offset(inset, inset),
                                    size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                                )
                                // Remaining-time arc.
                                drawArc(
                                    color = if (isWork) IceBlueAccent else MistBlue,
                                    startAngle = -90f,
                                    sweepAngle = 360f * fraction.coerceIn(0f, 1f),
                                    useCenter = false,
                                    topLeft = Offset(inset, inset),
                                    size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke),
                                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = String.format(Locale.US, "%d:%02d", mins, secs),
                                    style = MaterialTheme.typography.displayMedium,
                                    color = PureWhite
                                )
                                if (session.chunksCompleted > 0) {
                                    Text(
                                        text = "${session.chunksCompleted} chunk${if (session.chunksCompleted == 1) "" else "s"} done",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = PureWhite.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }

                        // Break-time micro-reward suggestion.
                        if (!isWork && focusChunkReward != null) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(PureWhite.copy(alpha = 0.15f))
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "🎁 Take your break: ${focusChunkReward}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PureWhite,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Controls: Pause/Resume + Stop.
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(
                                onClick = { focusSession = session.copy(isPaused = !session.isPaused) },
                                colors = ButtonDefaults.buttonColors(containerColor = PureWhite.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = if (session.isPaused) "Resume" else "Pause",
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = {
                                    // Stop the session. Chunks already completed keep their elevation
                                    // (awarded as they happened) — stopping is not a penalty.
                                    val done = session.chunksCompleted
                                    focusSession = null
                                    focusChunkReward = null
                                    if (done > 0) {
                                        showNotification("Focus done", "$done chunk${if (done == 1) "" else "s"} completed. Nice work.", RidgelineBlue, "⛰️")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PureWhite),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                            ) {
                                Text("Stop", color = RidgelineBlue, fontWeight = FontWeight.Bold)
                            }
                        }

                        // During a break, let the user skip ahead to the next work chunk.
                        if (!isWork) {
                            Spacer(modifier = Modifier.height(12.dp))
                            TextButton(onClick = {
                                focusChunkReward = null
                                focusSession = session.copy(phase = FOCUS_PHASE_WORK, secondsLeft = session.workMinutes * 60)
                            }) {
                                Text("Skip break", color = PureWhite.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Delete-event confirmation. Shows on top of the inspect dialog without dismissing it,
        // so Cancel returns the user to the same view they were already looking at.
        if (entryPendingDelete != null) {
            val target = entryPendingDelete!!
            AlertDialog(
                onDismissRequest = { entryPendingDelete = null },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Delete event?", fontWeight = FontWeight.Black, color = MidnightSlate) },
                text = {
                    Text(
                        "\"${target.task}\" will be permanently removed. This can't be undone.",
                        color = MidnightSlate,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            cancelAllReminders(context, target.id)
                            scheduleEntries.removeAll { it.id == target.id }
                            entryPendingDelete = null
                            activeInspectEntry = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(50)
                    ) { Text("Delete", fontWeight = FontWeight.Bold, color = PureWhite) }
                },
                dismissButton = {
                    TextButton(onClick = { entryPendingDelete = null }) {
                        Text("Cancel", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // To-do delete confirmation. Used for both active and completed to-dos.
        // Offer to complete the parent to-do once all its subtasks are checked off.
        if (subtaskCompletionPrompt != null) {
            val promptItem = subtaskCompletionPrompt!!
            AlertDialog(
                onDismissRequest = { subtaskCompletionPrompt = null },
                containerColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                title = { Text("All subtasks done! 🎉", style = MaterialTheme.typography.titleMedium, color = MidnightSlate) },
                text = {
                    Text(
                        "You've checked off every subtask for \"${promptItem.text}\". Mark the whole task complete?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MidnightSlate.copy(alpha = 0.8f)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val baseIdx = otherTodoEntries.indexOfFirst { it.id == promptItem.id }
                            if (baseIdx != -1 && !otherTodoEntries[baseIdx].isCompleted) {
                                cancelAllReminders(context, promptItem.id)
                                otherTodoEntries[baseIdx] = otherTodoEntries[baseIdx].copy(isCompleted = true, completedDate = selectedDate)
                                awardElevation(ELEVATION_TODO, "Task done · +$ELEVATION_TODO ft")
                            }
                            subtaskCompletionPrompt = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                        shape = RoundedCornerShape(50)
                    ) { Text("Complete it", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { subtaskCompletionPrompt = null }) {
                        Text("Not yet", color = TextMuted)
                    }
                }
            )
        }

        // Confirm completing a to-do. Matches the confirm-before-complete pattern used by
        // priorities and goals, and guards against accidental taps.
        // Task composer — the shared new/edit window for priorities and to-dos. Hidden (but kept
        // alive) while the user is off picking a time on the schedule.
        taskComposerDraft?.let { draft ->
            if (!composerPlacementPending) {
                TaskComposerDialog(
                    draft = draft,
                    onDismiss = { taskComposerDraft = null },
                    onPlaceOnSchedule = { d ->
                        // Stash the in-progress draft, hop to the schedule, and let the user tap a
                        // slot. The slot handler writes the time back into the draft and returns.
                        taskComposerDraft = d
                        composerPlacementPending = true
                        wizardSelectedPriorities.clear()
                        wizardSelectedPriorities.add(WizardItem(d.title.ifBlank { "New item" }))
                        wizardCurrentSchedulingIndex = 0
                        isWizardPlacementMode = true
                        selectedDate = d.dueDate ?: d.ownerDate
                        currentScreen = "Home"
                    },
                    onSave = { d ->
                        if (d.isPriority) {
                            // A priority's "when" is its block on the timeline.
                            val existingIdx = d.editingId?.let { id -> scheduleEntries.indexOfFirst { it.id == id } } ?: -1
                            if (existingIdx != -1) {
                                val prior = scheduleEntries[existingIdx]
                                scheduleEntries[existingIdx] = prior.copy(
                                    task = d.title,
                                    notes = d.notes,
                                    subtasks = d.subtasks,
                                    blockColor = d.color,
                                    reward = d.reward,
                                    triggerCue = d.triggerCue,
                                    hasCustomTime = d.dueTime != null,
                                    startHour = d.dueTime?.hour ?: 0,
                                    startMinute = d.dueTime?.minute ?: 0,
                                    date = d.dueDate ?: prior.date
                                )
                            } else {
                                scheduleEntries.add(
                                    ScheduleEntry(
                                        defaultSlotLabel = "",
                                        task = d.title,
                                        isTopPriority = true,
                                        notes = d.notes,
                                        subtasks = d.subtasks,
                                        blockColor = d.color,
                                        reward = d.reward,
                                        triggerCue = d.triggerCue,
                                        hasCustomTime = d.dueTime != null,
                                        startHour = d.dueTime?.hour ?: 0,
                                        startMinute = d.dueTime?.minute ?: 0,
                                        date = d.dueDate ?: d.ownerDate
                                    )
                                )
                            }
                        } else {
                            // A to-do's "when" is a deadline.
                            val existingIdx = d.editingId?.let { id -> otherTodoEntries.indexOfFirst { it.id == id } } ?: -1
                            val targetId = d.editingId ?: UUID.randomUUID().toString()
                            if (existingIdx != -1) {
                                otherTodoEntries[existingIdx] = otherTodoEntries[existingIdx].copy(
                                    text = d.title,
                                    notes = d.notes,
                                    subtasks = d.subtasks,
                                    blockColor = d.color,
                                    dueDate = d.dueDate,
                                    dueTime = d.dueTime,
                                    reward = d.reward,
                                    triggerCue = d.triggerCue,
                                    reminderNightBefore = d.reminderNightBefore,
                                    reminderMorningOf = d.reminderMorningOf
                                )
                            } else {
                                otherTodoEntries.add(
                                    OtherTodoItem(
                                        id = targetId,
                                        text = d.title,
                                        date = d.ownerDate,
                                        notes = d.notes,
                                        subtasks = d.subtasks,
                                        blockColor = d.color,
                                        dueDate = d.dueDate,
                                        dueTime = d.dueTime,
                                        reward = d.reward,
                                        triggerCue = d.triggerCue,
                                        reminderNightBefore = d.reminderNightBefore,
                                        reminderMorningOf = d.reminderMorningOf
                                    )
                                )
                            }
                            // Re-arm reminders from scratch so toggling any of them off actually
                            // cancels the old alarm rather than leaving it orphaned.
                            cancelAllReminders(context, targetId)
                            val body = if (d.triggerCue.isNotBlank()) "When you ${d.triggerCue}, it's time to ${d.title}" else "To-do reminder"
                            val anchorDate = d.dueDate ?: d.ownerDate
                            if (d.reminderNightBefore) {
                                nightBeforeEpochMillis(anchorDate)?.let { ts ->
                                    scheduleReminder(context, targetId, "Tomorrow: ${d.title}", body, ts, REMINDER_SLOT_NIGHT_BEFORE)
                                }
                            }
                            if (d.reminderMorningOf) {
                                morningOfEpochMillis(anchorDate)?.let { ts ->
                                    scheduleReminder(context, targetId, "Today: ${d.title}", body, ts, REMINDER_SLOT_MORNING_OF)
                                }
                            }
                            if (d.reminderAtDueTime && d.dueDate != null && d.dueTime != null) {
                                val dueMs = LocalDateTime.of(d.dueDate, d.dueTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                scheduleReminder(context, targetId, "Due: ${d.title}", "\"${d.title}\" is due now", dueMs, REMINDER_SLOT_DUE)
                            }
                        }
                        recordActivity()
                        taskComposerDraft = null
                    }
                )
            }
        }

        // Task detail window — tap any priority or to-do to inspect it. Reads live from the lists
        // so subtask ticks update in place.
        taskDetailId?.let { detailId ->
            val todo = if (!taskDetailIsPriority) otherTodoEntries.firstOrNull { it.id == detailId } else null
            val prio = if (taskDetailIsPriority) scheduleEntries.firstOrNull { it.id == detailId } else null
            if (todo != null) {
                val whenLabel = todo.dueDate?.let { d ->
                    val t = todo.dueTime
                    "Due ${d.monthValue}/${d.dayOfMonth}" + (t?.let { " at ${formatTimeLabel(it.hour, it.minute)}" } ?: "")
                }
                TaskDetailDialog(
                    title = todo.text, notes = todo.notes, subtasks = todo.subtasks,
                    whenLabel = whenLabel, color = todo.blockColor, reward = todo.reward,
                    triggerCue = todo.triggerCue, isCompleted = todo.isCompleted, isPriority = false,
                    onToggleSubtask = { stId ->
                        val i = otherTodoEntries.indexOfFirst { it.id == detailId }
                        if (i != -1) {
                            val cur = otherTodoEntries[i]
                            otherTodoEntries[i] = cur.copy(subtasks = cur.subtasks.map {
                                if (it.id == stId) it.copy(isCompleted = !it.isCompleted) else it
                            })
                        }
                    },
                    onToggleComplete = {
                        val i = otherTodoEntries.indexOfFirst { it.id == detailId }
                        if (i != -1) {
                            if (otherTodoEntries[i].isCompleted) {
                                otherTodoEntries[i] = otherTodoEntries[i].copy(isCompleted = false, completedDate = null)
                                removeElevation(ELEVATION_TODO); recordActivity()
                            } else {
                                cancelAllReminders(context, detailId)
                                otherTodoEntries[i] = otherTodoEntries[i].copy(isCompleted = true, completedDate = selectedDate)
                                awardElevation(ELEVATION_TODO, "Task done · +$ELEVATION_TODO ft")
                            }
                        }
                        taskDetailId = null
                    },
                    onEdit = {
                        taskComposerDraft = TaskDraft(
                            editingId = todo.id, isPriority = false, ownerDate = todo.date,
                            title = todo.text, notes = todo.notes, subtasks = todo.subtasks,
                            dueDate = todo.dueDate, dueTime = todo.dueTime, color = todo.blockColor,
                            reward = todo.reward, triggerCue = todo.triggerCue,
                            reminderNightBefore = todo.reminderNightBefore,
                            reminderMorningOf = todo.reminderMorningOf,
                            reminderAtDueTime = todo.dueTime != null
                        )
                        taskDetailId = null
                    },
                    onDelete = { todoPendingDelete = todo; taskDetailId = null },
                    onDismiss = { taskDetailId = null }
                )
            } else if (prio != null) {
                val whenLabel = if (prio.hasCustomTime) formatTimeLabel(prio.startHour, prio.startMinute) else "No time set"
                TaskDetailDialog(
                    title = prio.task, notes = prio.notes, subtasks = prio.subtasks,
                    whenLabel = whenLabel, color = prio.blockColor, reward = prio.reward,
                    triggerCue = prio.triggerCue, isCompleted = prio.isCompleted, isPriority = true,
                    onToggleSubtask = { stId ->
                        val i = scheduleEntries.indexOfFirst { it.id == detailId }
                        if (i != -1) {
                            val cur = scheduleEntries[i]
                            scheduleEntries[i] = cur.copy(subtasks = cur.subtasks.map {
                                if (it.id == stId) it.copy(isCompleted = !it.isCompleted) else it
                            })
                        }
                    },
                    onToggleComplete = {
                        val i = scheduleEntries.indexOfFirst { it.id == detailId }
                        if (i != -1) {
                            if (scheduleEntries[i].isCompleted) {
                                scheduleEntries[i] = scheduleEntries[i].copy(isCompleted = false)
                                removeElevation(ELEVATION_PRIORITY); recordActivity()
                                taskDetailId = null
                            } else {
                                // Route through the existing confirm so priorities behave consistently.
                                priorityItemToComplete = scheduleEntries[i]
                                showCompletionConfirmDialog = true
                                taskDetailId = null
                            }
                        }
                    },
                    onEdit = {
                        taskComposerDraft = TaskDraft(
                            editingId = prio.id, isPriority = true, ownerDate = prio.date,
                            title = prio.task, notes = prio.notes, subtasks = prio.subtasks,
                            dueDate = prio.date,
                            dueTime = if (prio.hasCustomTime) LocalTime.of(prio.startHour, prio.startMinute) else null,
                            color = prio.blockColor,
                            reward = prio.reward, triggerCue = prio.triggerCue,
                            reminderNightBefore = prio.reminderNightBefore,
                            reminderMorningOf = prio.reminderMorningOf
                        )
                        taskDetailId = null
                    },
                    onDelete = { entryPendingDelete = prio; taskDetailId = null },
                    onDismiss = { taskDetailId = null }
                )
            }
        }

        if (todoPendingComplete != null) {
            val target = todoPendingComplete!!
            AlertDialog(
                onDismissRequest = { todoPendingComplete = null },
                containerColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Mark complete?", style = MaterialTheme.typography.titleMedium, color = MidnightSlate) },
                text = {
                    Text(
                        "Mark \"${target.text}\" as done? You'll earn +$ELEVATION_TODO ft of elevation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MidnightSlate.copy(alpha = 0.8f)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val baseIdx = otherTodoEntries.indexOfFirst { it.id == target.id }
                            if (baseIdx != -1 && !otherTodoEntries[baseIdx].isCompleted) {
                                cancelAllReminders(context, target.id)
                                otherTodoEntries[baseIdx] = otherTodoEntries[baseIdx].copy(isCompleted = true, completedDate = selectedDate)
                                awardElevation(ELEVATION_TODO, "Task done · +$ELEVATION_TODO ft")
                            }
                            todoPendingComplete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                        shape = RoundedCornerShape(50)
                    ) { Text("Complete", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { todoPendingComplete = null }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }

        if (todoPendingDelete != null) {
            val target = todoPendingDelete!!
            AlertDialog(
                onDismissRequest = { todoPendingDelete = null },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Delete to-do?", fontWeight = FontWeight.Black, color = MidnightSlate) },
                text = {
                    Text(
                        "\"${target.text}\" will be permanently removed. This can't be undone.",
                        color = MidnightSlate,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            cancelAllReminders(context, target.id)
                            otherTodoEntries.removeAll { it.id == target.id }
                            recordActivity()
                            todoPendingDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(50)
                    ) { Text("Delete", fontWeight = FontWeight.Bold, color = PureWhite) }
                },
                dismissButton = {
                    TextButton(onClick = { todoPendingDelete = null }) {
                        Text("Cancel", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Goal delete confirmation. Used for both weekly and monthly goals.
        // Includes a heads-up about step count so the user knows what they're
        // discarding — goals tend to represent more user investment than to-dos.
        if (goalPendingDelete != null) {
            val target = goalPendingDelete!!
            val totalSteps = target.steps.size
            AlertDialog(
                onDismissRequest = { goalPendingDelete = null },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Delete goal?", fontWeight = FontWeight.Black, color = MidnightSlate) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "\"${target.title}\" will be permanently removed. This can't be undone.",
                            color = MidnightSlate,
                            fontSize = 14.sp
                        )
                        if (totalSteps > 0) {
                            Text(
                                "Heads up: this goal has $totalSteps step${if (totalSteps == 1) "" else "s"} — they'll be deleted too.",
                                color = MetricOrange,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            goalEntries.removeAll { it.id == target.id }
                            recordActivity()
                            goalPendingDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(50)
                    ) { Text("Delete", fontWeight = FontWeight.Bold, color = PureWhite) }
                },
                dismissButton = {
                    TextButton(onClick = { goalPendingDelete = null }) {
                        Text("Cancel", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showScheduleDialog) {
            AlertDialog(
                onDismissRequest = { showScheduleDialog = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = dialogEventName,
                            onValueChange = { dialogEventName = it },
                            label = { Text("Event Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = MidnightSlate
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite,
                                focusedBorderColor = RidgelineBlue,
                                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(50)
                        )

                        OutlinedTextField(
                            value = dialogEventNotes,
                            onValueChange = { dialogEventNotes = it },
                            label = { Text("Details or Notes") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite,
                                focusedBorderColor = RidgelineBlue,
                                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(50)
                        )

                        // Implementation-intention cue ("When I [cue], I'll [task]"). Optional.
                        // Helps with task initiation — if the user fills this in, the reminder
                        // notification text rephrases the task as a contextual prompt.
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Starter cue (optional)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                            WhyChip(WHY_TRIGGER_CUE_TITLE, WHY_TRIGGER_CUE_BODY)
                        }
                        OutlinedTextField(
                            value = dialogTriggerCue,
                            onValueChange = { dialogTriggerCue = it },
                            label = { Text("When I…") },
                            placeholder = { Text("e.g. sit down with coffee, finish my 10 am meeting") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = PureWhite,
                                unfocusedContainerColor = PureWhite,
                                focusedBorderColor = RidgelineBlue,
                                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(50)
                        )

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                        Text("Start Time Parameters", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = dialogStartHour,
                                onValueChange = { dialogStartHour = it },
                                label = { Text("Hour") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                            )
                            OutlinedTextField(
                                value = dialogStartMin,
                                onValueChange = { dialogStartMin = it },
                                label = { Text("Min") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                            )
                            Button(
                                onClick = { dialogStartAmPm = if (dialogStartAmPm == "AM") "PM" else "AM" },
                                colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.padding(top = 4.dp).height(52.dp)
                            ) { Text(dialogStartAmPm, fontWeight = FontWeight.Bold) }
                        }

                        Row(modifier = Modifier.fillMaxWidth().background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(50)).padding(4.dp)) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (dialogTimeMode == "DURATION") PureWhite else Color.Transparent)
                                    .clickable { dialogTimeMode = "DURATION" }
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Outlined.Timer, contentDescription = null, tint = MidnightSlate, modifier = Modifier.size(14.dp))
                                    Text("Duration", style = MaterialTheme.typography.labelMedium, color = MidnightSlate)
                                }
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (dialogTimeMode == "END_TIME") PureWhite else Color.Transparent)
                                    .clickable { dialogTimeMode = "END_TIME" }
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Outlined.Schedule, contentDescription = null, tint = MidnightSlate, modifier = Modifier.size(14.dp))
                                    Text("End Time", style = MaterialTheme.typography.labelMedium, color = MidnightSlate)
                                }
                            }
                        }

                        if (dialogTimeMode == "DURATION") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = dialogDurationMins,
                                    onValueChange = { dialogDurationMins = it },
                                    label = { Text("Duration (Minutes)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = PureWhite,
                                        unfocusedContainerColor = PureWhite,
                                        focusedBorderColor = RidgelineBlue,
                                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(50)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf(15, 30, 45, 60, 75, 90, 105, 120).forEach { mins ->
                                        val isSelectedPreset = dialogDurationMins == mins.toString()
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(if (isSelectedPreset) RidgelineBlue else Color.LightGray.copy(alpha = 0.3f))
                                                .clickable { dialogDurationMins = mins.toString() }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "${mins}m", style = MaterialTheme.typography.labelSmall,
                                                color = if (isSelectedPreset) PureWhite else MidnightSlate)
                                        }
                                    }
                                }
                            }
                        } else {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = dialogEndHour,
                                    onValueChange = { dialogEndHour = it },
                                    label = { Text("End Hour") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                                )
                                OutlinedTextField(
                                    value = dialogEndMin,
                                    onValueChange = { dialogEndMin = it },
                                    label = { Text("End Min") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                                )
                                Button(
                                    onClick = { dialogEndAmPm = if (dialogEndAmPm == "AM") "PM" else "AM" },
                                    colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.padding(top = 4.dp).height(52.dp)
                                ) { Text(dialogEndAmPm, fontWeight = FontWeight.Bold) }
                            }
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                        // All-day toggle. When ON, hides time/duration fields below; entry will
                        // render in the All-Day bar above the schedule rather than in the timeline.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("All-day event", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                            Switch(
                                checked = dialogIsAllDay,
                                onCheckedChange = { dialogIsAllDay = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = RidgelineBlue)
                            )
                        }

                        // Reminder picker. Hidden for all-day items to keep the data model
                        // unambiguous about offsets — we'd otherwise need a separate UI for them.
                        if (!dialogIsAllDay) {
                            Text("Reminder", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                            // Labels intentionally short to match the duration chips ("15m", "30m" etc).
                            // The two non-numeric options stay verbose so their meaning is unambiguous.
                            val reminderOptions = listOf(
                                null to "None",
                                REMINDER_PRESET_NIGHT_BEFORE to "Night before",
                                REMINDER_PRESET_MORNING_OF to "Morning of",
                                0 to "At start",
                                5 to "5m",
                                15 to "15m",
                                30 to "30m",
                                60 to "1h"
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                reminderOptions.forEach { (mins, label) ->
                                    val selected = dialogReminderMinutesBefore == mins
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (selected) RidgelineBlue else Color.LightGray.copy(alpha = 0.3f))
                                            .clickable { dialogReminderMinutesBefore = mins }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = label, style = MaterialTheme.typography.labelSmall,
                                            color = if (selected) PureWhite else MidnightSlate)
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                        Text("Event Theme Color Palette", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EventColors.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (dialogSelectedColor == color) 3.dp else 1.dp,
                                            color = if (dialogSelectedColor == color) MidnightSlate else Color.LightGray,
                                            shape = CircleShape
                                        )
                                        .clickable { dialogSelectedColor = color }
                                )
                            }
                            // If the current selection is a custom color (not one of the presets),
                            // show it as its own selected swatch so the choice stays visible.
                            if (dialogSelectedColor !in EventColors) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(dialogSelectedColor)
                                        .border(3.dp, MidnightSlate, CircleShape)
                                        .clickable { showCustomColorPicker = true }
                                )
                            }
                            // "+" — opens the custom color picker.
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NeutralFill)
                                    .border(1.dp, BorderGray, CircleShape)
                                    .clickable { showCustomColorPicker = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Custom color", tint = MidnightSlate, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (dialogEventName.isNotBlank()) {
                                val h12S = dialogStartHour.toIntOrNull() ?: 9
                                var h24S = if (h12S == 12) 0 else h12S
                                if (dialogStartAmPm == "PM") h24S += 12
                                val minS = dialogStartMin.toIntOrNull() ?: 0

                                val computedDuration = if (dialogIsAllDay) {
                                    // All-day items have no timeline duration; store 0 as a sentinel.
                                    0
                                } else if (dialogTimeMode == "DURATION") {
                                    dialogDurationMins.toIntOrNull() ?: 60
                                } else {
                                    val h12E = dialogEndHour.toIntOrNull() ?: 10
                                    var h24E = if (h12E == 12) 0 else h12E
                                    if (dialogEndAmPm == "PM") h24E += 12
                                    val minE = dialogEndMin.toIntOrNull() ?: 0

                                    val startAbsoluteMins = h24S * 60 + minS
                                    val endAbsoluteMins = h24E * 60 + minE
                                    val calculatedDiff = endAbsoluteMins - startAbsoluteMins
                                    if (calculatedDiff > 0) calculatedDiff else calculatedDiff + 1440
                                }

                                val targetDate = if (editingEntry != null) editingEntry!!.date else selectedDate

                                // Compute absolute reminder timestamp from picker selection.
                                // - Positive/zero values are "minutes before start": subtract from start time.
                                // - REMINDER_PRESET_MORNING_OF / NIGHT_BEFORE map to absolute times of day
                                //   (9 AM same day / 8 PM previous day) regardless of the event's start time.
                                // - All-day items skip reminders entirely (no time of day to anchor to).
                                val computedReminderMillis: Long? = when {
                                    dialogIsAllDay -> null
                                    dialogReminderMinutesBefore == null -> null
                                    dialogReminderMinutesBefore == REMINDER_PRESET_MORNING_OF -> {
                                        LocalDateTime.of(targetDate, LocalTime.of(9, 0))
                                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                    }
                                    dialogReminderMinutesBefore == REMINDER_PRESET_NIGHT_BEFORE -> {
                                        LocalDateTime.of(targetDate.minusDays(1), LocalTime.of(20, 0))
                                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                    }
                                    else -> {
                                        val startInstant = LocalDateTime.of(targetDate, LocalTime.of(h24S, minS))
                                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                        startInstant - dialogReminderMinutesBefore!! * 60000L
                                    }
                                }

                                val updatedEntry = ScheduleEntry(
                                    defaultSlotLabel = formatTimeLabel(h24S, 0),
                                    task = dialogEventName,
                                    isTopPriority = isWizardPlacementMode || (editingEntry?.isTopPriority ?: false),
                                    notes = dialogEventNotes,
                                    blockColor = dialogSelectedColor,
                                    startHour = h24S,
                                    startMinute = minS,
                                    durationMins = computedDuration,
                                    hasCustomTime = true,
                                    date = targetDate,
                                    isAllDay = dialogIsAllDay,
                                    reminderEpochMillis = computedReminderMillis,
                                    triggerCue = dialogTriggerCue.trim()
                                )

                                val savedId: String
                                val schedulingExistingId = priorityBeingScheduledId
                                if (editingEntry != null) {
                                    val index = scheduleEntries.indexOfFirst { it.id == editingEntry!!.id }
                                    if (index != -1) {
                                        scheduleEntries[index] = updatedEntry.copy(id = editingEntry!!.id)
                                    }
                                    savedId = editingEntry!!.id
                                    // Cancel any prior reminder on this id before scheduling a new one.
                                    cancelAllReminders(context, savedId)
                                } else if (schedulingExistingId != null) {
                                    // Giving an existing unscheduled priority a time — update it in place
                                    // (and keep its completion state) rather than adding a duplicate.
                                    val index = scheduleEntries.indexOfFirst { it.id == schedulingExistingId }
                                    if (index != -1) {
                                        val prior = scheduleEntries[index]
                                        scheduleEntries[index] = updatedEntry.copy(
                                            id = schedulingExistingId,
                                            isTopPriority = true,
                                            isCompleted = prior.isCompleted,
                                            reward = prior.reward
                                        )
                                        savedId = schedulingExistingId
                                    } else {
                                        scheduleEntries.add(updatedEntry)
                                        savedId = updatedEntry.id
                                    }
                                    cancelAllReminders(context, savedId)
                                } else {
                                    scheduleEntries.add(updatedEntry)
                                    savedId = updatedEntry.id
                                }

                                // Schedule the new reminder if applicable. If the user set a
                                // "When I..." cue, build the body as a contextual prompt that
                                // rehearses the implementation intention. Otherwise fall back to
                                // the plain "Starts at..." line.
                                if (computedReminderMillis != null) {
                                    val whenText = formatTimeLabel(h24S, minS)
                                    val cue = dialogTriggerCue.trim()
                                    val body = if (cue.isNotEmpty()) {
                                        "When you ${cue}, it's time to ${dialogEventName}"
                                    } else {
                                        "Starts at $whenText"
                                    }
                                    scheduleReminder(
                                        context,
                                        savedId,
                                        dialogEventName,
                                        body,
                                        computedReminderMillis
                                    )
                                }

                                if (isWizardPlacementMode) {
                                    if (schedulingExistingId != null) {
                                        // We were only giving one existing priority a time — exit cleanly.
                                        // No reward prompt: that belongs to the brain-dump run, not this.
                                        isWizardPlacementMode = false
                                        wizardCurrentSchedulingIndex = 0
                                        wizardSelectedPriorities.clear()
                                        priorityBeingScheduledId = null
                                    } else {
                                        if (pendingWizardOtherTodoRemoval != null) {
                                            otherTodoEntries.removeAll { it.id == pendingWizardOtherTodoRemoval!!.id }
                                            pendingWizardOtherTodoRemoval = null
                                        }

                                        if (wizardCurrentSchedulingIndex + 1 < wizardSelectedPriorities.size) {
                                            wizardCurrentSchedulingIndex++
                                        } else {
                                            isWizardPlacementMode = false
                                            wizardCurrentSchedulingIndex = 0

                                            val dayPrioritiesCount = scheduleEntries.count { it.date == selectedDate && it.isTopPriority }
                                            if (dayPrioritiesCount > 0) {
                                                showRewardSettingDialog = true
                                            } else {
                                                currentScreen = "Lists"
                                            }
                                        }
                                    }
                                }

                                showScheduleDialog = false
                                editingEntry = null
                                recordActivity()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                        shape = RoundedCornerShape(50)
                    ) { Text("Save", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showScheduleDialog = false
                            editingEntry = null
                        }
                    ) { Text("Cancel", color = MidnightSlate, fontWeight = FontWeight.Bold) }
                }
            )

            // Custom color picker, layered over the event dialog when the user taps "+".
            if (showCustomColorPicker) {
                CustomColorPickerDialog(
                    initial = if (dialogSelectedColor == PureWhite) RidgelineBlue else dialogSelectedColor,
                    onConfirm = { picked ->
                        dialogSelectedColor = picked
                        showCustomColorPicker = false
                    },
                    onDismiss = { showCustomColorPicker = false }
                )
            }
        }

        if (showBrainDumpWizard) {
            Dialog(
                onDismissRequest = { showBrainDumpWizard = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        IconButton(
                            onClick = { showBrainDumpWizard = false },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) { Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = MidnightSlate) }

                        Column(modifier = Modifier.fillMaxSize()) {
                            when (wizardStep) {
                                1 -> {
                                    Text("Brain Dump", style = MaterialTheme.typography.headlineMedium)
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = wizardRawInputText,
                                            onValueChange = { wizardRawInputText = it },
                                            placeholder = { Text("List your ideas or tasks...") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f).height(54.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedContainerColor = PureWhite,
                                                unfocusedContainerColor = PureWhite,
                                                focusedBorderColor = RidgelineBlue,
                                                unfocusedBorderColor = Color.Transparent
                                            ),
                                            shape = RoundedCornerShape(50)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(CircleShape)
                                                .background(RidgelineBlue)
                                                .clickable {
                                                    if (wizardRawInputText.isNotBlank()) {
                                                        wizardBrainDumpList.add(WizardItem(wizardRawInputText.trim()))
                                                        wizardRawInputText = ""
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("+", color = PureWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                            .verticalScroll(rememberScrollState()),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (wizardBrainDumpList.isEmpty()) {
                                            Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                                                Text("Your mind palace is clear.", color = Color.Gray, fontSize = 14.sp)
                                            }
                                        } else {
                                            wizardBrainDumpList.forEachIndexed { index, item ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(PureWhite, RoundedCornerShape(50))
                                                        .padding(horizontal = 14.dp, vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    // Inline editable text. Replacing the list item with a copy() on
                                                    // each keystroke keeps the snapshot-based persistence (which watches
                                                    // wizardBrainDumpList) and recomposition wiring trivial.
                                                    OutlinedTextField(
                                                        value = item.text,
                                                        onValueChange = { newText ->
                                                            // Guard against stale index after a delete during typing.
                                                            if (index < wizardBrainDumpList.size && wizardBrainDumpList[index].id == item.id) {
                                                                wizardBrainDumpList[index] = item.copy(text = newText)
                                                            }
                                                        },
                                                        singleLine = true,
                                                        textStyle = TextStyle(fontSize = 14.sp, color = MidnightSlate),
                                                        colors = OutlinedTextFieldDefaults.colors(
                                                            focusedContainerColor = PureWhite,
                                                            unfocusedContainerColor = PureWhite,
                                                            focusedBorderColor = RidgelineBlue,
                                                            unfocusedBorderColor = Color.Transparent
                                                        ),
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Outlined.Close,
                                                        contentDescription = "Remove item",
                                                        tint = Color.LightGray,
                                                        modifier = Modifier.clickable { wizardBrainDumpList.remove(item) }.padding(horizontal = 8.dp).size(20.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            // Drop any entries the user emptied while editing — they can't be
                                            // meaningfully selected as priorities or carried to to-dos.
                                            val blanks = wizardBrainDumpList.filter { it.text.isBlank() }
                                            blanks.forEach { wizardBrainDumpList.remove(it) }
                                            if (wizardBrainDumpList.isNotEmpty()) wizardStep = 2
                                        },
                                        enabled = wizardBrainDumpList.any { it.text.isNotBlank() },
                                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.fillMaxWidth().height(48.dp)
                                    ) { Text("Next", fontWeight = FontWeight.Bold) }
                                }
                                2 -> {
                                    val currentPriorityCount = scheduleEntries.count { it.date == selectedDate && it.isTopPriority }
                                    val availableSlotsCount = (3 - currentPriorityCount).coerceAtLeast(0)

                                    Text("Select Today's Focus Blocks", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)

                                    if (availableSlotsCount > 0) {
                                        Text("Select up to $availableSlotsCount items to elevate to Priorities. Remaining items move to 'Other To-Do'.", fontSize = 13.sp, color = Color.Gray)
                                    } else {
                                        Text("You have already scheduled 3 Priorities for today! All brain-dumped items will safely convert into standard to-dos.", fontSize = 13.sp, color = RidgelineBlue, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                            .verticalScroll(rememberScrollState()),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        wizardBrainDumpList.forEach { item ->
                                            val isChosen = wizardSelectedPriorities.contains(item)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (isChosen) IceBlueAccent else PureWhite, RoundedCornerShape(50))
                                                    .border(
                                                        width = if (isChosen) 2.dp else 0.dp,
                                                        color = if (isChosen) RidgelineBlue else Color.Transparent,
                                                        shape = RoundedCornerShape(50)
                                                    )
                                                    .clickable(enabled = availableSlotsCount > 0 || isChosen) {
                                                        if (isChosen) {
                                                            wizardSelectedPriorities.remove(item)
                                                        } else if (wizardSelectedPriorities.size < availableSlotsCount) {
                                                            wizardSelectedPriorities.add(item)
                                                        }
                                                    }
                                                    .padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = isChosen,
                                                    onCheckedChange = null,
                                                    enabled = availableSlotsCount > 0 || isChosen,
                                                    colors = CheckboxDefaults.colors(checkedColor = RidgelineBlue)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(text = item.text, style = MaterialTheme.typography.titleSmall, color = if (availableSlotsCount == 0 && !isChosen) Color.Gray else MidnightSlate)
                                            }
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            wizardBrainDumpList.forEach { dumpItem ->
                                                if (!wizardSelectedPriorities.contains(dumpItem)) {
                                                    otherTodoEntries.add(
                                                        OtherTodoItem(
                                                            id = java.util.UUID.randomUUID().toString(),
                                                            text = dumpItem.text,
                                                            date = selectedDate,
                                                            fromBrainDump = true
                                                        )
                                                    )
                                                }
                                            }

                                            if (wizardSelectedPriorities.isNotEmpty()) {
                                                showBrainDumpWizard = false
                                                wizardCurrentSchedulingIndex = 0
                                                isWizardPlacementMode = true
                                                currentScreen = "Home"
                                            } else {
                                                showBrainDumpWizard = false
                                                recordActivity()
                                                showNotification("Brain Dump Saved", "Items added to your to-do list.", RidgelineBlue, "🧠")
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.fillMaxWidth().height(48.dp)
                                    ) {
                                        Text(
                                            text = if (wizardSelectedPriorities.isNotEmpty()) "Next (${wizardSelectedPriorities.size} Selected)" else "Save to Other To-Do",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showRewardSettingDialog) {
            AlertDialog(
                onDismissRequest = {
                    showRewardSettingDialog = false
                    currentScreen = "Lists"
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Set Priority Rewards 🎁", fontWeight = FontWeight.Black, color = MidnightSlate) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("You've scheduled your priorities! Set a small reward for completing each one.", fontSize = 14.sp, color = MidnightSlate)
                        val dayPriorities = scheduleEntries.filter { it.date == selectedDate && it.isTopPriority }
                        dayPriorities.forEach { priority ->
                            OutlinedTextField(
                                value = priority.reward,
                                onValueChange = { newVal ->
                                    val idx = scheduleEntries.indexOfFirst { it.id == priority.id }
                                    if(idx != -1) scheduleEntries[idx] = scheduleEntries[idx].copy(reward = newVal)
                                },
                                label = { Text(priority.task, maxLines = 1) },
                                placeholder = { Text("e.g. 10 mins reading, a snack...") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)), shape = RoundedCornerShape(50)
                            )
                            // Reward bank chips — small-only since per-priority rewards are
                            // individual-task reinforcement (under-5-min tier).
                            RewardChipsRow(
                                bank = rewardBank,
                                smallOnly = true,
                                onPick = { picked ->
                                    val idx = scheduleEntries.indexOfFirst { it.id == priority.id }
                                    if (idx != -1) scheduleEntries[idx] = scheduleEntries[idx].copy(reward = picked)
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showRewardSettingDialog = false
                            currentScreen = "Lists"
                            recordActivity()
                            Toast.makeText(context, "Rewards Set! Head to the Lists page to set your Grand Reward.", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue)
                    ) { Text("Save & Continue", fontWeight = FontWeight.Bold) }
                }
            )
        }


        if (showCarryOverActionDialog && carryOverItemTarget != null) {
            val currentTargetItem = carryOverItemTarget!!
            AlertDialog(
                onDismissRequest = { showCarryOverActionDialog = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Task Options", fontWeight = FontWeight.Black, color = MidnightSlate) },
                text = { Text("What would you like to do with:\n\"${currentTargetItem.text}\"?", color = MidnightSlate, fontSize = 15.sp) },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                showCarryOverActionDialog = false
                                wizardSelectedPriorities.clear()
                                wizardSelectedPriorities.add(WizardItem(currentTargetItem.text))
                                wizardCurrentSchedulingIndex = 0
                                pendingWizardOtherTodoRemoval = currentTargetItem
                                isWizardPlacementMode = true
                                currentScreen = "Home"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.weight(1f)
                        ) { Text("Make Priority", style = MaterialTheme.typography.labelMedium) }

                        Button(
                            onClick = {
                                val idx = otherTodoEntries.indexOfFirst { it.id == currentTargetItem.id }
                                if (idx != -1) {
                                    otherTodoEntries[idx] = currentTargetItem.copy(isCompleted = true, completedDate = selectedDate)
                                    awardElevation(ELEVATION_TODO, "Task done · +$ELEVATION_TODO ft")
                                }
                                showCarryOverActionDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.weight(1f)
                        ) { Text("Mark Completed", style = MaterialTheme.typography.labelMedium) }
                    }
                }
            )
        }

        if (showCompletionConfirmDialog && priorityItemToComplete != null) {
            AlertDialog(
                onDismissRequest = { showCompletionConfirmDialog = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Priority Checklist", fontWeight = FontWeight.Black, color = MidnightSlate) },
                text = { Text("Have you completed your top priority:\n\"${priorityItemToComplete?.task}\"?", color = MidnightSlate, fontSize = 15.sp) },
                confirmButton = {
                    Button(
                        onClick = {
                            val item = priorityItemToComplete
                            if (item != null) {
                                val idx = scheduleEntries.indexOfFirst { it.id == item.id }
                                if (idx != -1) {
                                    val updated = scheduleEntries[idx].copy(isCompleted = true)
                                    scheduleEntries[idx] = updated
                                }

                                // Determine up front whether this completion clears the day with a
                                // grand reward set. If so, suppress the per-priority ascent overlay
                                // (silent award) so it doesn't stack behind the grand reward dialog —
                                // the grand reward celebration owns this moment.
                                val dayPriorities = scheduleEntries.filter { it.date == item.date && it.isTopPriority }
                                val allDoneNow = dayPriorities.isNotEmpty() && dayPriorities.all { it.isCompleted }
                                val hasGrandReward = dailyGrandRewards[item.date]?.isNotBlank() == true
                                val triggersGrandReward = allDoneNow && hasGrandReward

                                awardElevation(ELEVATION_PRIORITY, "Priority done · +$ELEVATION_PRIORITY ft", silent = triggersGrandReward)

                                showCompletionConfirmDialog = false

                                if (triggersGrandReward) {
                                    // Grand-reward bonus, also silent — the dialog displays it.
                                    awardElevation(30, "🏆 All priorities cleared · +30 ft", silent = true)
                                    showGrandRewardCelebration = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(50)
                    ) { Text("Yes, I did it!", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showCompletionConfirmDialog = false }) {
                        Text("Not yet", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Full-screen milestone celebration. Shown when awardElevation detects that the user
        // just crossed a threshold. Visually distinct from the small banner system: covers the
        // whole screen with a brand-gradient backdrop, a giant mountain emoji, the milestone
        // name and elevation as the centerpiece. Tap anywhere to dismiss.
        // Goal completion confirmation. Fires when the user taps the goal's flag/title to mark
        // it complete. The dialog reads `goalPendingCompletion` to know which goal; on confirm,
        // it sets isCompleted and awards the (weekly vs monthly) elevation. Cancel just clears
        // the pending state — no side effects. Unchecking a completed goal does NOT route through
        // here (reversal applies immediately, see the click handler in each goal row).
        goalPendingCompletion?.let { pendingGoal ->
            val isMonthly = pendingGoal.type == "MONTHLY"
            val elevationAmount = if (isMonthly) ELEVATION_STEP else ELEVATION_PRIORITY
            val kindLabel = if (isMonthly) "monthly" else "weekly"
            AlertDialog(
                onDismissRequest = { goalPendingCompletion = null },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Complete this $kindLabel goal?", style = MaterialTheme.typography.titleLarge, color = MidnightSlate) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(pendingGoal.title, style = MaterialTheme.typography.titleMedium, color = RidgelineBlue)
                        if (pendingGoal.reward.isNotBlank()) {
                            Text("🎁 Reward: ${pendingGoal.reward}", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate.copy(alpha = 0.8f))
                        }
                        val totalSteps = pendingGoal.steps.size
                        val doneSteps = pendingGoal.steps.count { it.isCompleted }
                        if (totalSteps > 0 && doneSteps < totalSteps) {
                            Text(
                                "Heads up: $doneSteps of $totalSteps steps are checked off.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MetricOrange
                            )
                        }
                        Text("Marking this complete adds +$elevationAmount ft of elevation.", style = MaterialTheme.typography.bodySmall, color = MidnightSlate.copy(alpha = 0.6f))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val idx = goalEntries.indexOfFirst { it.id == pendingGoal.id }
                            if (idx != -1) {
                                goalEntries[idx] = goalEntries[idx].copy(isCompleted = true)
                                val msg = if (isMonthly) "🏔️ Monthly goal complete · +$ELEVATION_STEP ft" else "🏔️ Weekly goal complete · +$ELEVATION_PRIORITY ft"
                                awardElevation(elevationAmount, msg)
                            }
                            goalPendingCompletion = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Yes, complete it", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { goalPendingCompletion = null }) {
                        Text("Not yet", color = MidnightSlate, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Ascent celebration: drops a tall, hero-styled card from the top showing the climb
        // that just happened. The vertical bar animates from the previous fraction to the new
        // fraction; a "+X ft" badge pops out with spring physics; a glowing dot rides the leading
        // edge of the fill while it's still climbing. Auto-dismisses after a short hold.
        ascentCelebration?.let { ev ->
            // Recompute milestone context against the celebration's snapshot. We use the milestone
            // the user is in *after* the gain — the visualization makes the most sense when the
            // bar reflects current progress (and if a milestone was crossed, the milestone overlay
            // handles that case; we won't be here at all in that case).
            val milestoneAt = currentMilestone(ev.after)
            val nextAt = nextMilestone(ev.after)
            val span = if (nextAt != null) (nextAt.threshold - milestoneAt.threshold).coerceAtLeast(1) else 1
            val beforeFraction = if (nextAt != null)
                ((ev.before - milestoneAt.threshold).coerceIn(0, span)).toFloat() / span.toFloat()
            else 1f
            val afterFraction = if (nextAt != null)
                ((ev.after - milestoneAt.threshold).coerceIn(0, span)).toFloat() / span.toFloat()
            else 1f
            val gained = ev.after - ev.before

            // Trigger flags toggled in a LaunchedEffect so animations sequence properly:
            //   t=0       — overlay drops in (Dialog mounts), bar starts at beforeFraction
            //   t=120ms   — fraction target switches to afterFraction (climb begins)
            //   t=380ms   — "+X ft" badge pops out (spring scale)
            //   t=2200ms  — auto-dismiss
            var fillTarget by remember(ev) { mutableStateOf(beforeFraction) }
            var showBadge by remember(ev) { mutableStateOf(false) }
            LaunchedEffect(ev) {
                kotlinx.coroutines.delay(120)
                fillTarget = afterFraction
                kotlinx.coroutines.delay(260)
                showBadge = true
                kotlinx.coroutines.delay(1820)
                ascentCelebration = null
            }
            // Animated fraction tween. The 700ms duration gives the climb a satisfying weight
            // without lingering — short enough to support rapid completions, long enough to read.
            val animatedFraction by animateFloatAsState(
                targetValue = fillTarget,
                animationSpec = tween(durationMillis = 700),
                label = "AscentFill"
            )
            // Spring-scale for the "+X ft" badge: starts at 0, overshoots to ~1.15, settles to 1.
            val badgeScale by animateFloatAsState(
                targetValue = if (showBadge) 1f else 0f,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
                ),
                label = "AscentBadgeScale"
            )
            // Infinite shimmer on the leading-edge spark while the fill is still moving.
            val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "AscentShimmer")
            val sparkAlpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                    animation = tween(700, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                ),
                label = "SparkAlpha"
            )

            Dialog(
                onDismissRequest = { ascentCelebration = null },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true, dismissOnBackPress = true)
            ) {
                // Full-screen dim scrim that's tap-to-dismiss. The card slides down from the top
                // edge inside it. The scrim is semi-transparent so the underlying screen peeks
                // through faintly — keeps the celebration feeling like a moment, not a context switch.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { ascentCelebration = null }
                        ),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // The card itself slides down from above. AnimatedVisibility with a remembered
                    // `visible=true` trick mounts the slide-in on first composition.
                    val configuration = LocalConfiguration.current
                    val cardHeight = (configuration.screenHeightDp * 0.62f).dp.coerceAtLeast(420.dp)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 32.dp)
                            .height(cardHeight),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp, topStart = 24.dp, topEnd = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                            // Header at top: small "WELL DONE" eyebrow + the task message.
                            Column(
                                modifier = Modifier.align(Alignment.TopStart).fillMaxWidth(0.6f)
                            ) {
                                Text(
                                    text = "WELL DONE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RidgelineBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ev.taskLabel,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MidnightSlate,
                                    maxLines = 3
                                )
                            }

                            // The hero — a tall vertical climb bar on the right.
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight(0.85f)
                                    .width(38.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                // Track.
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(14.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(MistBlue.copy(alpha = 0.3f))
                                )
                                // Animated fill, grows from the bottom up.
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight(animatedFraction.coerceIn(0.02f, 1f))
                                        .width(14.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(MetricGreen)
                                )
                                // Leading-edge spark — a soft glow circle riding the top of the fill.
                                // Uses fillMaxHeight on a transparent column so the spark's vertical
                                // position is driven by the same fraction as the fill below it.
                                if (animatedFraction > 0.02f && animatedFraction < 0.99f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight(animatedFraction.coerceIn(0.02f, 1f)),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        // Outer glow halo.
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(MetricGreen.copy(alpha = 0.35f * sparkAlpha))
                                        )
                                        // Inner bright core.
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(PureWhite.copy(alpha = sparkAlpha))
                                        )
                                    }
                                }
                                // Top dot (next milestone or summit).
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(if (nextAt == null) MetricGreen else MistBlue)
                                        .border(3.dp, MaterialTheme.colorScheme.surfaceContainerLow, CircleShape)
                                )
                                // Bottom dot (current milestone, always reached).
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(MetricGreen)
                                        .border(3.dp, MaterialTheme.colorScheme.surfaceContainerLow, CircleShape)
                                )
                            }

                            // The "+X ft" pop-out badge — sits beside the bar, springs in after
                            // the fill animation has gotten underway.
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(end = 64.dp)
                                    .graphicsLayer {
                                        scaleX = badgeScale
                                        scaleY = badgeScale
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(MetricGold)
                                        .padding(horizontal = 20.dp, vertical = 12.dp)
                                ) {
                                    Text(
                                        text = "+$gained ft",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = PureBlack
                                    )
                                }
                            }

                            // Bottom info: current milestone & target. Mirrors the scoreboard layout.
                            Column(
                                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(0.7f)
                            ) {
                                Text(
                                    text = milestoneAt.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = RidgelineBlue
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = ev.after.toString(),
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MidnightSlate
                                    )
                                    Text(
                                        text = " ft",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MidnightSlate.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                if (nextAt != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Next stop",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MidnightSlate.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = nextAt.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MidnightSlate
                                    )
                                    Text(
                                        text = "${nextAt.threshold - ev.after} ft to go",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = RidgelineBlue
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        celebrationMilestone?.let { crossed ->
            Dialog(
                onDismissRequest = { celebrationMilestone = null },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true, dismissOnBackPress = true)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(RidgelineBlue)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { celebrationMilestone = null }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "⛰️",
                            fontSize = 96.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "MILESTONE REACHED",
                            style = MaterialTheme.typography.labelMedium,
                            color = PureWhite.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = crossed.name,
                            style = MaterialTheme.typography.displayMedium,
                            color = PureWhite,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(PureWhite.copy(alpha = 0.18f))
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "$elevation ft of elevation",
                                style = MaterialTheme.typography.titleMedium,
                                color = PureWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = "Keep climbing.",
                            style = MaterialTheme.typography.titleLarge,
                            color = PureWhite.copy(alpha = 0.95f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        // Encouraging dismiss button — pill shape, high-contrast white on blue.
                        // Backed by the whole-screen scrim click as a secondary dismiss path,
                        // so users who tap anywhere outside the button still close the celebration.
                        Button(
                            onClick = { celebrationMilestone = null },
                            colors = ButtonDefaults.buttonColors(containerColor = PureWhite),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Let's keep it going!",
                                style = MaterialTheme.typography.titleMedium,
                                color = RidgelineBlue
                            )
                        }
                    }
                }
            }
        }

        // First-launch onboarding wizard. Asks the user to write out their personal reward bank
        // — 5+ small rewards (under 5 min) and 3+ medium rewards (15+ min) — which then powers
        // tappable chips above every reward field throughout the app. Schatz 2024: chosen
        // rewards are higher-value reinforcement than imposed ones, so collecting the user's
        // own list is the foundation.
        if (showOnboardingWizard) {
            var onboardingStep by remember { mutableStateOf(0) }
            val draftSmall = remember { mutableStateListOf<String>() }
            val draftMedium = remember { mutableStateListOf<String>() }
            var draftSmallInput by remember { mutableStateOf("") }
            var draftMediumInput by remember { mutableStateOf("") }
            Dialog(
                onDismissRequest = { /* Force the wizard to be completed or explicitly skipped via the buttons */ },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        when (onboardingStep) {
                            0 -> {
                                // Welcome screen — kept short on purpose. The next step is where the
                                // actual work happens; this page is just a friendly door.
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "⛰️",
                                    fontSize = 80.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Welcome to your climb",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MidnightSlate,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "First, let's pick some rewards you actually want.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MidnightSlate.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    onClick = { onboardingStep = 1 },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.fillMaxWidth().height(54.dp)
                                ) { Text("Let's go", fontWeight = FontWeight.Bold) }
                            }
                            1 -> {
                                // Small rewards step.
                                Text(
                                    text = "Small rewards",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MidnightSlate
                                )
                                Text(
                                    text = "Quick treats — under 5 minutes. Add at least 5 things you'd enjoy.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MidnightSlate.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Examples: a square of chocolate, 5 min of stretching, one funny YouTube short, a hot drink, step outside for fresh air…",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MidnightSlate.copy(alpha = 0.5f),
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = draftSmallInput,
                                        onValueChange = { draftSmallInput = it },
                                        placeholder = { Text("e.g. a piece of chocolate") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = PureWhite,
                                            unfocusedContainerColor = PureWhite,
                                            focusedBorderColor = RidgelineBlue,
                                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                                        ),
                                        shape = RoundedCornerShape(50)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(RidgelineBlue)
                                            .clickable {
                                                if (draftSmallInput.isNotBlank()) {
                                                    draftSmall.add(draftSmallInput.trim())
                                                    draftSmallInput = ""
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) { Text("+", color = PureWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${draftSmall.size} / 5+ added",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (draftSmall.size >= 5) MetricGreen else MidnightSlate.copy(alpha = 0.5f)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .verticalScroll(rememberScrollState())
                                        .padding(vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    draftSmall.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(50))
                                                .background(NeutralFill)
                                                .padding(horizontal = 14.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = item,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MidnightSlate,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = "Remove",
                                                tint = MidnightSlate.copy(alpha = 0.6f),
                                                modifier = Modifier.size(18.dp).clickable {
                                                    draftSmall.removeAt(index)
                                                }
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = { onboardingStep = 2 },
                                    enabled = draftSmall.size >= 5,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.fillMaxWidth().height(54.dp)
                                ) { Text("Next", fontWeight = FontWeight.Bold) }
                            }
                            2 -> {
                                // Medium rewards step.
                                Text(
                                    text = "Medium rewards",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MidnightSlate
                                )
                                Text(
                                    text = "Bigger treats — 15 minutes or more. Add at least 3 things worth working toward.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MidnightSlate.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Examples: an episode of a show, a walk in the park, a hot bath, gaming session, calling a friend…",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MidnightSlate.copy(alpha = 0.5f),
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = draftMediumInput,
                                        onValueChange = { draftMediumInput = it },
                                        placeholder = { Text("e.g. an episode of my show") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = PureWhite,
                                            unfocusedContainerColor = PureWhite,
                                            focusedBorderColor = RidgelineBlue,
                                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                                        ),
                                        shape = RoundedCornerShape(50)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(RidgelineBlue)
                                            .clickable {
                                                if (draftMediumInput.isNotBlank()) {
                                                    draftMedium.add(draftMediumInput.trim())
                                                    draftMediumInput = ""
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) { Text("+", color = PureWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${draftMedium.size} / 3+ added",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (draftMedium.size >= 3) MetricGreen else MidnightSlate.copy(alpha = 0.5f)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .verticalScroll(rememberScrollState())
                                        .padding(vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    draftMedium.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(50))
                                                .background(NeutralFill)
                                                .padding(horizontal = 14.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = item,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MidnightSlate,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = "Remove",
                                                tint = MidnightSlate.copy(alpha = 0.6f),
                                                modifier = Modifier.size(18.dp).clickable {
                                                    draftMedium.removeAt(index)
                                                }
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        // Commit the bank, mark setup complete, dismiss wizard.
                                        rewardBank = RewardBank(small = draftSmall.toList(), medium = draftMedium.toList())
                                        sharedPrefs.edit().putBoolean("hasCompletedSetup", true).apply()
                                        val now = System.currentTimeMillis()
                                        sharedPrefs.edit().putLong("rewardBankLastRefreshMs", now).apply()
                                        rewardBankLastRefreshMs = now
                                        hasCompletedSetup = true
                                        showOnboardingWizard = false
                                    },
                                    enabled = draftMedium.size >= 3,
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.fillMaxWidth().height(54.dp)
                                ) { Text("Start climbing", fontWeight = FontWeight.Bold) }
                            }
                        }
                    }
                }
            }
        }

        // Journey-stage prompt — one-tap question that weights which Trail Notes surface first.
        // Asked once, after behavior setup, when landing on Insight. Newly-diagnosed users get
        // foundational explainers first; familiar users get in-the-moment tactics weighted earlier.
        if (showJourneyStagePrompt) {
            Dialog(
                onDismissRequest = { /* one-tap choice required */ },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.fillMaxWidth().size(40.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Where are you on the journey?",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MidnightSlate,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This just tweaks which Trail Notes you see first. You'll get them all either way.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MidnightSlate.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                journeyStage = TRAIL_STAGE_FOUNDATION
                                sharedPrefs.edit().putBoolean("hasSetJourneyStage", true).apply()
                                hasSetJourneyStage = true
                                showJourneyStagePrompt = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth().height(54.dp)
                        ) { Text("Newly diagnosed — start with the basics", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) }
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {
                                journeyStage = TRAIL_STAGE_INMOMENT
                                sharedPrefs.edit().putBoolean("hasSetJourneyStage", true).apply()
                                hasSetJourneyStage = true
                                showJourneyStagePrompt = false
                            },
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, RidgelineBlue),
                            modifier = Modifier.fillMaxWidth().height(54.dp)
                        ) { Text("I know my ADHD — give me tactics", color = RidgelineBlue, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) }
                    }
                }
            }
        }

        // Trail Note reader. Shows the note body and a one-tap prompt (quiz or reflection). For a
        // quiz, tapping reveals whether the choice was right. Marking done adds the id to
        // trailNotesSeen, which unlocks the next note. Optional CTA links into a feature.
        openTrailNote?.let { note ->
            var answered by remember(note.id) { mutableStateOf<Int?>(null) }
            var sourcesExpanded by remember(note.id) { mutableStateOf(false) }
            Dialog(onDismissRequest = { openTrailNote = null }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.95f).heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.85f).dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Text("Trail Note", style = MaterialTheme.typography.labelMedium, color = RidgelineBlue)
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Close",
                                tint = MidnightSlate.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp).clickable { openTrailNote = null }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(note.title, style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(note.body, style = MaterialTheme.typography.bodyLarge, color = MidnightSlate.copy(alpha = 0.85f))

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Prompt.
                        Text(note.promptQuestion, style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            note.promptOptions.forEachIndexed { idx, option ->
                                val isQuiz = note.promptType == TRAIL_PROMPT_QUIZ
                                val picked = answered == idx
                                // Color logic: before answering, neutral. After answering a quiz,
                                // correct option goes green, a wrong pick goes muted red, others stay
                                // neutral. Reflection: any pick just highlights in brand blue.
                                val bg = when {
                                    answered == null -> IceBlueAccent
                                    isQuiz && idx == note.correctOption -> SuccessGreen.copy(alpha = 0.25f)
                                    isQuiz && picked -> Color.Red.copy(alpha = 0.15f)
                                    picked -> RidgelineBlue.copy(alpha = 0.2f)
                                    else -> IceBlueAccent.copy(alpha = 0.5f)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(50))
                                        .background(bg)
                                        .clickable(enabled = answered == null) { answered = idx }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(option, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, modifier = Modifier.weight(1f))
                                    if (answered != null && isQuiz && idx == note.correctOption) {
                                        Text("✓", color = SuccessGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Post-answer feedback line.
                        if (answered != null && note.promptType == TRAIL_PROMPT_QUIZ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            val gotIt = answered == note.correctOption
                            Text(
                                text = if (gotIt) "That's it." else "Not quite — the highlighted one is the key idea.",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (gotIt) SuccessGreen else MidnightSlate.copy(alpha = 0.7f)
                            )
                        }

                        // Optional in-app action link.
                        if (note.linkAction != null && note.linkLabel != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {
                                    // Mark read, close reader, then route to the feature.
                                    trailNotesSeen = trailNotesSeen + note.id
                                    openTrailNote = null
                                    when (note.linkAction) {
                                        "OPEN_REWARD_BANK" -> showRewardBankEditor = true
                                        "START_FOCUS" -> { focusSetupTaskLabel = ""; focusSetupMinutes = 25; showFocusSetup = true }
                                        "ADD_CUE" -> currentScreen = "Lists"   // the cue lives in the to-do edit dialog on Lists
                                        else -> {}
                                    }
                                },
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, RidgelineBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text(note.linkLabel, color = RidgelineBlue, fontWeight = FontWeight.Bold) }
                        }

                        // Sources — collapsible, so citations add credibility without crowding the
                        // lesson. Tapping the row toggles the reference list. Kept visually quiet
                        // (muted text) since it's supporting material, not the main content.
                        if (note.sources.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = BorderGray.copy(alpha = 0.5f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { sourcesExpanded = !sourcesExpanded }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
                                    Text(
                                        text = if (note.sources.size == 1) "Source" else "Sources (${note.sources.size})",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = TextMuted
                                    )
                                }
                                Icon(
                                    imageVector = if (sourcesExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            if (sourcesExpanded) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 4.dp)) {
                                    note.sources.forEach { src ->
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text("•", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                            Text(src, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                        }
                                    }
                                    Text(
                                        text = "Educational summary, not medical advice. This app supports — and doesn't replace — care from a qualified professional.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextHint,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                trailNotesSeen = trailNotesSeen + note.id
                                openTrailNote = null
                            },
                            enabled = answered != null,
                            colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) { Text(if (answered != null) "Done" else "Pick an answer to finish", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }

        // Trail Notes library — browse finished notes (re-readable) and see what's still locked.
        if (showTrailLibrary) {
            val sequence = trailSequenceForStage(journeyStage)
            Dialog(onDismissRequest = { showTrailLibrary = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = RidgelineBlue, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Trail Notes", style = MaterialTheme.typography.headlineSmall, color = MidnightSlate)
                            }
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Close",
                                tint = MidnightSlate.copy(alpha = 0.6f),
                                modifier = Modifier.size(22.dp).clickable { showTrailLibrary = false }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Walk the sequence; a note is locked once we hit the first unread one
                            // (self-paced gating — you can't skip ahead).
                            var reachedLocked = false
                            sequence.forEach { note ->
                                val isRead = note.id in trailNotesSeen
                                val isNext = !isRead && !reachedLocked
                                if (!isRead && !isNext) reachedLocked = true
                                val locked = !isRead && !isNext
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (locked) Color.LightGray.copy(alpha = 0.18f) else IceBlueAccent)
                                        .clickable(enabled = !locked) { openTrailNote = note; showTrailLibrary = false }
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = if (isRead) "✓" else if (isNext) "→" else "🔒",
                                        color = if (isRead) SuccessGreen else if (isNext) RidgelineBlue else MidnightSlate.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (locked) "Locked" else note.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = if (locked) MidnightSlate.copy(alpha = 0.4f) else MidnightSlate
                                        )
                                        if (!locked) {
                                            Text(
                                                text = if (isRead) "Read · tap to revisit" else "Next up",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MidnightSlate.copy(alpha = 0.55f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Behavior picker — first-launch (and any-time-after-reset) prompt for the user to
        // pick the 3-6 dopamine-seeking behaviors they want to track. Triggered automatically
        // when the user lands on the Insight page without having completed setup. Force-modal
        // (no outside-tap dismiss) so the user can't accidentally skip and end up with an
        // empty tile grid.

        // Settings dialog — opened from the gear icon on Insight page. Two tabs:
        // Behaviors (the tracking catalog from the first-launch picker) and Rewards
        // (the small/medium reward bank). Single Save button at the bottom commits any
        // changes from either tab atomically.
        if (showRewardBankEditor) {
            // Tab selection state. 0 = Behaviors, 1 = Rewards. Persists only for the
            // lifetime of the dialog.
            var settingsTab by remember(showRewardBankEditor) { mutableStateOf(0) }
            // Behavior tab draft state.
            // Reward tab draft state.
            val draftSmall = remember(showRewardBankEditor) { mutableStateListOf<String>().apply { addAll(rewardBank.small) } }
            val draftMedium = remember(showRewardBankEditor) { mutableStateListOf<String>().apply { addAll(rewardBank.medium) } }
            var draftSmallInput by remember(showRewardBankEditor) { mutableStateOf("") }
            var draftMediumInput by remember(showRewardBankEditor) { mutableStateOf("") }
            Dialog(
                onDismissRequest = { showRewardBankEditor = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.9f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MidnightSlate
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tab selector — two pill chips. Tapping switches the visible tab content.
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(Color.LightGray.copy(alpha = 0.3f)).padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("Rewards" to 0, "Calendar" to 1, "Reflection" to 2, "Backup" to 3).forEach { (label, idx) ->
                                val selected = settingsTab == idx
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(if (selected) PureWhite else Color.Transparent)
                                        .clickable { settingsTab = idx }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (selected) MidnightSlate else MidnightSlate.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tab content area — scrollable.
                        Column(
                            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (settingsTab == 0) {
                                // === REWARDS TAB ===
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                                    Text(
                                        text = "Tap × to remove. Add new ones below.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MidnightSlate.copy(alpha = 0.6f)
                                    )
                                    WhyChip(WHY_REWARD_TITLE, WHY_REWARD_BODY)
                                }
                                Text("Small (under 5 min)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue, fontWeight = FontWeight.Bold)
                                draftSmall.forEachIndexed { index, item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(50))
                                            .background(NeutralFill)
                                            .padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = item, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, modifier = Modifier.weight(1f))
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Remove",
                                            tint = MidnightSlate.copy(alpha = 0.6f),
                                            modifier = Modifier.size(18.dp).clickable { draftSmall.removeAt(index) }
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = draftSmallInput,
                                        onValueChange = { draftSmallInput = it },
                                        placeholder = { Text("Add a small reward") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(50)
                                    )
                                    Box(
                                        modifier = Modifier.size(44.dp).clip(CircleShape).background(RidgelineBlue).clickable {
                                            if (draftSmallInput.isNotBlank()) {
                                                draftSmall.add(draftSmallInput.trim())
                                                draftSmallInput = ""
                                            }
                                        },
                                        contentAlignment = Alignment.Center
                                    ) { Text("+", color = PureWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Medium (15+ min)", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue, fontWeight = FontWeight.Bold)
                                draftMedium.forEachIndexed { index, item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(50))
                                            .background(NeutralFill)
                                            .padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = item, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, modifier = Modifier.weight(1f))
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Remove",
                                            tint = MidnightSlate.copy(alpha = 0.6f),
                                            modifier = Modifier.size(18.dp).clickable { draftMedium.removeAt(index) }
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = draftMediumInput,
                                        onValueChange = { draftMediumInput = it },
                                        placeholder = { Text("Add a medium reward") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(50)
                                    )
                                    Box(
                                        modifier = Modifier.size(44.dp).clip(CircleShape).background(RidgelineBlue).clickable {
                                            if (draftMediumInput.isNotBlank()) {
                                                draftMedium.add(draftMediumInput.trim())
                                                draftMediumInput = ""
                                            }
                                        },
                                        contentAlignment = Alignment.Center
                                    ) { Text("+", color = PureWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                                }
                            } else if (settingsTab == 1) {
                                // === CALENDAR TAB ===
                                Text(
                                    text = "Show events from your phone's calendar (Google, Outlook, etc.) right in your schedule.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MidnightSlate.copy(alpha = 0.75f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(NeutralFill)
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Show my calendar", style = MaterialTheme.typography.titleSmall, color = MidnightSlate)
                                        Text(
                                            text = "Read-only. Events stay in your calendar app.",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MidnightSlate.copy(alpha = 0.6f)
                                        )
                                    }
                                    Switch(
                                        checked = showDeviceCalendar && calendarPermissionGranted,
                                        onCheckedChange = { wantOn ->
                                            if (wantOn) {
                                                if (calendarPermissionGranted) {
                                                    showDeviceCalendar = true
                                                    sharedPrefs.edit().putBoolean("showDeviceCalendar", true).apply()
                                                } else {
                                                    // Triggers the system permission prompt; the launcher
                                                    // callback flips the toggle based on the result.
                                                    calendarPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)
                                                }
                                            } else {
                                                showDeviceCalendar = false
                                                sharedPrefs.edit().putBoolean("showDeviceCalendar", false).apply()
                                            }
                                        },
                                        colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = RidgelineBlue)
                                    )
                                }
                                // If the user turned it on but permission isn't granted, explain.
                                if (showDeviceCalendar && !calendarPermissionGranted) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Calendar permission is needed. Toggle on to grant it, or enable it for this app in your phone's Settings.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MetricOrange
                                    )
                                }
                                // Per-calendar picker — choose which of the device's calendars
                                // (personal, work, holidays, etc.) appear on the schedule. Turning
                                // one off hides its events; the setting persists.
                                if (showDeviceCalendar && calendarPermissionGranted && availableCalendars.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Calendars to show", style = MaterialTheme.typography.titleSmall, color = MidnightSlate)
                                    Text(
                                        text = "Pick which calendars appear on your schedule.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MidnightSlate.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    availableCalendars.forEach { cal ->
                                        val visible = cal.id.toString() !in hiddenCalendarIds
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                                Text(cal.displayName, style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, maxLines = 1)
                                                if (cal.accountName.isNotBlank() && cal.accountName != cal.displayName) {
                                                    Text(cal.accountName, style = MaterialTheme.typography.labelSmall, color = MidnightSlate.copy(alpha = 0.55f), maxLines = 1)
                                                }
                                            }
                                            Switch(
                                                checked = visible,
                                                onCheckedChange = { show ->
                                                    val idStr = cal.id.toString()
                                                    hiddenCalendarIds = if (show) hiddenCalendarIds - idStr else hiddenCalendarIds + idStr
                                                    sharedPrefs.edit().putStringSet("hiddenCalendarIds", hiddenCalendarIds).apply()
                                                },
                                                colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = RidgelineBlue)
                                            )
                                        }
                                    }
                                }
                            } else if (settingsTab == 2) {
                                // === REFLECTION TAB ===
                                Text(
                                    text = "A once-a-day nudge to reflect. It stays quiet on days you've already written, and it never counts a streak against you.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MidnightSlate.copy(alpha = 0.75f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(NeutralFill)
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Daily reflection reminder", style = MaterialTheme.typography.titleSmall, color = MidnightSlate)
                                        Text(
                                            text = "Today's prompt arrives as a notification.",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MidnightSlate.copy(alpha = 0.6f)
                                        )
                                    }
                                    Switch(
                                        checked = reflectionReminderEnabled,
                                        onCheckedChange = { on ->
                                            reflectionReminderEnabled = on
                                            sharedPrefs.edit().putBoolean("reflectionReminderEnabled", on).apply()
                                        },
                                        colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = RidgelineBlue)
                                    )
                                }
                                if (reflectionReminderEnabled) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text("Remind me at", style = MaterialTheme.typography.titleSmall, color = RidgelineBlue)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    // Typed time entry. The fields hold text (so a half-typed value like
                                    // "1" isn't fought by the UI); each keystroke tries to commit, and only
                                    // a valid 1-12 / 0-59 pair is written to state+prefs. Anything invalid
                                    // just isn't committed, and a hint appears rather than silently
                                    // snapping the number to something the user didn't type.
                                    var hourText by remember(showRewardBankEditor) {
                                        mutableStateOf(
                                            (if (reflectionReminderHour == 0 || reflectionReminderHour == 12) 12
                                            else if (reflectionReminderHour > 12) reflectionReminderHour - 12
                                            else reflectionReminderHour).toString()
                                        )
                                    }
                                    var minuteText by remember(showRewardBankEditor) {
                                        mutableStateOf(String.format(Locale.US, "%02d", reflectionReminderMinute))
                                    }
                                    var isPmState by remember(showRewardBankEditor) { mutableStateOf(reflectionReminderHour >= 12) }

                                    val hourValid = hourText.toIntOrNull()?.let { it in 1..12 } == true
                                    val minuteValid = minuteText.toIntOrNull()?.let { it in 0..59 } == true

                                    val commitTime: () -> Unit = {
                                        val h12v = hourText.toIntOrNull()
                                        val mv = minuteText.toIntOrNull()
                                        if (h12v != null && h12v in 1..12 && mv != null && mv in 0..59) {
                                            var h24 = if (h12v == 12) 0 else h12v
                                            if (isPmState) h24 += 12
                                            reflectionReminderHour = h24
                                            reflectionReminderMinute = mv
                                            sharedPrefs.edit()
                                                .putInt("reflectionReminderHour", h24)
                                                .putInt("reflectionReminderMinute", mv)
                                                .apply()
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = hourText,
                                            onValueChange = { raw ->
                                                val digits = raw.filter { it.isDigit() }
                                                if (digits.length <= 2) { hourText = digits; commitTime() }
                                            },
                                            label = { Text("Hour") },
                                            singleLine = true,
                                            isError = hourText.isNotEmpty() && !hourValid,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.width(88.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        Text(":", style = MaterialTheme.typography.titleMedium, color = MidnightSlate)
                                        OutlinedTextField(
                                            value = minuteText,
                                            onValueChange = { raw ->
                                                val digits = raw.filter { it.isDigit() }
                                                if (digits.length <= 2) { minuteText = digits; commitTime() }
                                            },
                                            label = { Text("Min") },
                                            singleLine = true,
                                            isError = minuteText.isNotEmpty() && !minuteValid,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.width(88.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = PureWhite, unfocusedContainerColor = PureWhite, focusedBorderColor = RidgelineBlue, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        Row(
                                            modifier = Modifier.clip(RoundedCornerShape(50)).background(IceBlueAccent).clickable {
                                                isPmState = !isPmState
                                                commitTime()
                                            }.padding(horizontal = 14.dp, vertical = 12.dp)
                                        ) {
                                            Text(if (isPmState) "PM" else "AM", style = MaterialTheme.typography.labelLarge, color = SkyDark, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    if ((hourText.isNotEmpty() && !hourValid) || (minuteText.isNotEmpty() && !minuteValid)) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Hour 1-12, minutes 0-59.",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = OverdueRed
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val previewH = if (reflectionReminderHour == 0 || reflectionReminderHour == 12) 12 else if (reflectionReminderHour > 12) reflectionReminderHour - 12 else reflectionReminderHour
                                        Text(
                                            text = "Saved: $previewH:${String.format(Locale.US, "%02d", reflectionReminderMinute)} ${if (reflectionReminderHour >= 12) "PM" else "AM"}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MidnightSlate.copy(alpha = 0.55f)
                                        )
                                    }
                                }
                            } else {
                                // === BACKUP TAB ===
                                Text(
                                    text = "Your tasks, goals, logs, and progress live only on this device. Back them up to a file so nothing is lost if you switch phones, reinstall, or clear the app.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MidnightSlate.copy(alpha = 0.75f)
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        pendingBackupJson = exportPrefsToJson(sharedPrefs)
                                        val stamp = LocalDate.now().toString()
                                        backupExportLauncher.launch("ascent-backup-$stamp.json")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) { Text("Back up my data", fontWeight = FontWeight.Bold) }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { backupImportLauncher.launch(arrayOf("application/json", "*/*")) },
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(1.dp, RidgelineBlue),
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) { Text("Restore from a backup", color = RidgelineBlue, fontWeight = FontWeight.Bold) }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Restoring replaces everything currently in the app with the backup's contents, then restarts the app. Your backup file stays where you saved it.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MidnightSlate.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                // Commit reward bank changes; treat as a refresh (resets novelty clock).
                                rewardBank = RewardBank(small = draftSmall.toList(), medium = draftMedium.toList())
                                val now = System.currentTimeMillis()
                                sharedPrefs.edit().putLong("rewardBankLastRefreshMs", now).apply()
                                sharedPrefs.edit().putLong("refreshPromptDismissedMs", 0L).apply()
                                rewardBankLastRefreshMs = now
                                refreshPromptDismissedMs = 0L
                                showRewardBankEditor = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth().height(54.dp)
                        ) { Text("Save", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }

        // Restore confirmation — restoring overwrites everything, so it's gated behind an explicit
        // confirm. On success the Activity is recreated so the in-memory state re-reads the file.
        if (pendingRestoreJson != null) {
            AlertDialog(
                onDismissRequest = { pendingRestoreJson = null },
                containerColor = PureWhite,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Restore this backup?", style = MaterialTheme.typography.titleMedium, color = MidnightSlate) },
                text = {
                    Text(
                        "This replaces everything currently in Ascent with the contents of the backup, then restarts the app. This can't be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MidnightSlate.copy(alpha = 0.8f)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val json = pendingRestoreJson
                            pendingRestoreJson = null
                            val ok = json != null && importPrefsFromJson(sharedPrefs, json)
                            if (ok) {
                                (context as? Activity)?.recreate()
                            } else {
                                Toast.makeText(context, "That file isn't a valid Ascent backup", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RidgelineBlue),
                        shape = RoundedCornerShape(50)
                    ) { Text("Replace & restore", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { pendingRestoreJson = null }) { Text("Cancel", color = TextMuted) }
                }
            )
        }


        if (showGrandRewardCelebration) {
            AlertDialog(
                onDismissRequest = { showGrandRewardCelebration = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp),
                title = { Text("🏆 ALL PRIORITIES COMPLETE 🏆", style = MaterialTheme.typography.titleMedium, color = MetricGold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        // The chosen reward is the centerpiece — it's the higher-value reinforcement
                        // (Schatz 2024), so it gets the largest type and visual weight. The +30 ft
                        // elevation is secondary information shown smaller below.
                        Text("Your reward:", style = MaterialTheme.typography.labelMedium, color = MidnightSlate.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${dailyGrandRewards[selectedDate]}",
                            style = MaterialTheme.typography.displaySmall,
                            color = MetricGold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Go enjoy it — you earned it today.", style = MaterialTheme.typography.bodyMedium, color = MidnightSlate, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("+30 ft added to your elevation", style = MaterialTheme.typography.labelMedium, color = MidnightSlate.copy(alpha = 0.6f))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Pure acknowledgment now — the +30 ft has already been awarded when
                            // the third priority completed. Tapping Claim is just the user saying
                            // "I'm going to take the reward." Dismissing the dialog (back/outside
                            // tap) loses no elevation; the achievement stands either way.
                            showGrandRewardCelebration = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MetricGold),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Claim Reward", color = PureBlack, fontWeight = FontWeight.Bold) }
                }
            )
        }
    }

}