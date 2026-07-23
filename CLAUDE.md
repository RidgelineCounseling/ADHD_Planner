# ADHD Planner

An Android app that helps people with ADHD plan, organize, and stay on task. The
design intentionally favors calm, low-friction, glanceable UI (a "Right Now"
focus, gentle rewards/elevation feedback, brain-dump capture, and simple
schedule/lists/goals views).

## Design rules (do not drift from these)

These are explicit owner preferences. Match the existing look and feel — never
introduce a new visual language.

- **Colors:** Reuse the named color constants defined at the top of
  `MainActivity.kt` (e.g. `RidgelineBlue`, `MidnightSlate`, `SuccessGreen`,
  `PureWhite`, `CardInset`, `NeutralFill`, `BorderGray`, `TextMuted`,
  `OverdueRed`, `MetricOrange`, `Navy`). Do **not** invent new hex colors or add
  new palette entries for a one-off; pick the closest existing constant.
- **Fonts / type:** Use `MaterialTheme.typography.*` styles with the default
  system font (`FontFamily.Default`). Weight/emphasis is done with
  `FontWeight` (e.g. `Bold`, `Black`) inline. Do not add custom font families.
- **Icons:** Use `Icons.Outlined.*` for essentially all icons (this is the house
  style). `Icons.Filled.*` is reserved for "done/active" states — most commonly
  `Icons.Filled.CheckCircle` for a completed task/step. Icons come from
  `androidx.compose.material:material-icons-extended`.
- **Sizing/consistency:** When a UI element exists in more than one screen (e.g.
  subtask rows on Lists vs. step rows on Goals), keep them visually consistent —
  same checkbox sizes, spacing, and text styles.

## Architecture

- Single-module Jetpack Compose app, Material 3. Package `com.example.adhdplanner`.
- **Almost all UI and logic lives in `app/src/main/java/com/example/adhdplanner/MainActivity.kt`**
  (~10k+ lines). It is large but organized; search within it rather than
  expecting many small files.
- The main composable is `MainApp(...)`. It hosts a `Scaffold` whose body is a
  `when (currentScreen)` block. The screens are:
  - `"Home"` — the schedule / main page (day view, "Right Now", week pager, the
    add FAB with its speed-dial menu).
  - `"Lists"` — priorities and the **Other To-Do** section (items with subtasks;
    inline add, drag reordering, brain-dump wizard entry).
  - `"Goals"` — weekly/monthly goals made of `GoalStep`s with deadlines/rewards.
  - `"LogInsight"` — activity log / heatmap / stats.
- Reusable composables (cards, scoreboard, chips, etc.) are defined as top-level
  `@Composable` functions earlier in the same file.

## State & persistence

- UI state uses `remember { mutableStateOf(...) }` and `mutableStateListOf(...)`.
- Data is persisted to **SharedPreferences** as JSON. Each data class has
  `toJson()` / `fromJson()` helpers (e.g. `OtherTodoItem`, `GoalStep`,
  `GoalEntry`), saved via `snapshotFlow { ... }` collectors that write back to
  prefs on change. When you add a field to a persisted data class, update its
  `toJson`/`fromJson` too.
- Key data types: `OtherTodoItem`, `TodoSubtask`, `GoalEntry`, `GoalStep`,
  schedule entries, `FocusSession`, wizard items.

## Dialogs / overlays (a known gotcha)

Avoid `Dialog(properties = DialogProperties(usePlatformDefaultWidth = false))`
with a root `Modifier.fillMaxSize()` scrim — on some devices that dialog window
measures to zero and renders nothing (this caused a stuck FAB "×" bug). For a
full-screen scrim/overlay, render it **inline** in the Scaffold content (a
`Box(Modifier.fillMaxSize())` sibling) where constraints are real, and use a
`BackHandler` for back-dismiss. Centered-card dialogs sized with
`fillMaxWidth(fraction)` / `fillMaxHeight(fraction)` (like the Brain Dump wizard)
are fine.

## Build & tooling

- Gradle (Kotlin DSL). `minSdk = 24`, `targetSdk = 36`, `compileSdk = 36`,
  Java 11. Compose via BOM.
- Lint: **detekt** (`./gradlew detekt`), config at `app/config/detekt.yml`.
- Build: `./gradlew assembleDebug` (or build/run from Android Studio).
- **Note:** Claude Code web sessions do not have the Android SDK installed, so
  the app cannot be compiled or run here — changes are verified by code review
  and must be built/tested in Android Studio.

## Git workflow

- Development happens on a feature branch; changes are merged into `main` so the
  owner can pull into Android Studio to test. Confirm the current designated
  branch before pushing.
