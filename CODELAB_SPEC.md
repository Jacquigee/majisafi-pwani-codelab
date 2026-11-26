# Codelab Repository Specification: MajiSafi Pwani
**Session Title:** Beyond Autocomplete: Building Real-World Android Apps with Google Antigravity  
**Duration:** 60 Minutes  
**Audience:** Beginner & Intermediate Android Developers (Mombasa, Kenya)  
**Tech Stack:** Kotlin, Jetpack Compose (Material 3), Android CLI, Device MCP (`mobile-mcp`), Clean Architecture + MVI, Room, Koin, Coroutines/Flow, WorkManager, JUnit4 + Turbine

---

## 1. Domain & Problem Context (Mombasa Case Study)
Mombasa Island and nearby mainland estates (Tudor, Nyali, Bamburi, Kisauni, Likoni, Changamwe) rely heavily on private boreholes due to municipal water rationing. Over-extraction draws seawater into coastal coral aquifers, causing fluctuating **Total Dissolved Solids (TDS)** salinity levels.

**MajiSafi Pwani** is an offline-first, crowdsourced borehole salinity and water finder app helping Mombasa residents, tenants, and water-bowser buyers check nearby water quality before purchasing or signing a lease.

### Salinity Classification Rules (Domain Truth)
- **Fresh (`< 500 ppm`):** Safe for drinking and cooking without restriction.
- **Mildly Brackish (`500..1500 ppm`):** Safe for cooking and bathing; slight mineral taste.
- **Brackish (`1501..3000 ppm`):** Laundry, cleaning, and flushing only (corrodes kettles/heaters).
- **Saline (`> 3000 ppm`):** Non-potable unless filtered via Reverse Osmosis (RO).

---

## 2. 60-Minute Workshop Schedule & Milestones

| Timestamp | Phase | Topic & Focus | Git Tag |
|---|---|---|---|
| **00:00 - 00:10** | Step 0 | Android CLI & Antigravity Agent Setup | `step-0-init` |
| **00:10 - 00:25** | Step 1 | Domain Rules & Offline Room Database | `step-1-domain-room` |
| **00:25 - 00:40** | Step 2 | MVI Architecture & Koin Dependency Injection | `step-2-koin-mvi` |
| **00:40 - 00:50** | Step 3 | Jetpack Compose Material 3 UI & Badges | `step-3-compose-ui` |
| **00:50 - 00:55** | Step 4 | WorkManager Offline Sync & Device MCP | `step-4-workmanager` |
| **00:55 - 01:00** | Step 5 | Turbine Flow Testing & Agent Verification | `step-5-testing` |

---

## 3. Step-by-Step Curriculum

### Step 0: Project Initialization via Android CLI (00-10m)
```bash
# 1. Verify Android CLI
android --version

# 2. Scaffold empty Compose project
android create empty-activity --name="MajiSafi Pwani" -o=.

# 3. Initialize agent environment
android init
```
**Antigravity Agent Prompt:**
> "Set up our Gradle version catalog for Room, Koin 4, WorkManager, and Turbine. Ensure Java 17 toolchain is configured."

---

### Step 1: Domain Modeling & Room Database (10-25m)
- Domain entities: `Borehole`, `Estate`, `SalinityCategory`, `SalinityReport`
- Pure business logic: `SalinityClassifier.classify(tdsPpm)`
- Room entities: `BoreholeEntity`, `SalinityReportEntity`
- DAO: `BoreholeDao` with Flow queries and pre-populated Mombasa borehole seeds (`MombasaSeedData`).
- Repository: `OfflineFirstBoreholeRepository`.

**Antigravity Agent Prompt:**
> "Write the pure domain `SalinityClassifier` object enforcing Mombasa salinity thresholds (<500 ppm Fresh, 500-1500 Mildly Brackish, 1501-3000 Brackish, >3000 Saline). Implement a Room database with pre-populated seed data for Nyali, Tudor, Bamburi, and Likoni."

---

### Step 2: MVI Architecture & Koin DI (25-40m)
- Contract: `BoreholeUiState`, `BoreholeUiIntent`, `BoreholeUiEffect`
- Container: `BoreholeViewModel` processing user intents (Estate selection, search queries, salinity reporting) and driving immutable `StateFlow`.
- Dependency injection: `appModule` registering Room, Repository, and ViewModel.

**Antigravity Agent Prompt:**
> "Create an MVI architecture for `BoreholeScreen` with unidirectional data flow. Register all dependencies in Koin's `appModule` using `viewModel { ... }`."

---

### Step 3: Jetpack Compose Material 3 UI (40-50m)
- Theme: Indian ocean deep greens, warning ambers, and coral alerts (`Color.kt`, `Theme.kt`).
- Components:
  - `SalinityBadge`: Visual indicator with distinct color coding per TDS range.
  - `EstateFilterChips`: Horizontal scroll chip group for Mombasa estates.
  - `BoreholeCard`: TDS readout, pump status, and usage recommendations.
  - `AddReadingBottomSheet`: Bottom sheet for logging crowdsourced readings offline.
  - `BoreholeScreen`: Composite screen with search, filtering, and snackbar notifications.

**Antigravity Agent Prompt:**
> "Build the Jetpack Compose Material 3 UI for MajiSafi Pwani. Display custom `SalinityBadge` components matching domain categories and include a modal bottom sheet to enter new TDS readings."

---

### Step 4: Background Sync & Device MCP (50-55m)
- WorkManager: `SalinitySyncWorker` triggered periodically or on network reconnect.
- Device MCP: Connect Antigravity to physical device via `mobile-mcp` or simulate via `scripts/mock_device_mcp.py`.

**Antigravity Agent Prompt:**
> "Implement a CoroutineWorker that checks for unsynced local reports and syncs them to cloud storage. Use Device MCP to tap the estate chips and verify UI responsiveness."

---

### Step 5: Unit Testing & Verification with Turbine (55-60m)
- `SalinityClassifierTest`: Validates boundary values (499, 500, 1500, 1501, 3000, 3001).
- `BoreholeViewModelTest`: Tests MVI StateFlow emissions using Cash App's `Turbine`.

```bash
./gradlew testDebugUnitTest
```

---

## 4. Git Checkpoints
Attendees can jump to any stage of the codelab:
```bash
git checkout tags/step-0-init
git checkout tags/step-1-domain-room
git checkout tags/step-2-koin-mvi
git checkout tags/step-3-compose-ui
git checkout tags/step-4-workmanager
git checkout tags/step-5-testing
git checkout master
```
