# MajiSafi Pwani 🌊

> **Beyond Autocomplete: Building Real-World Android Apps with Google Antigravity**  
> *A 60-Minute Android Developer Codelab for Mombasa, Kenya*

MajiSafi Pwani is an offline-first, crowdsourced borehole salinity and water quality finder for residents, tenants, and water bowser operators in Mombasa County.

---

## 🌊 Domain Context: Mombasa Coastal Salinity
Due to municipal water rationing, residents across Mombasa (Tudor, Nyali, Bamburi, Kisauni, Likoni, Changamwe) rely heavily on private borehole water. Seawater intrusion causes fluctuating **Total Dissolved Solids (TDS)**:
- **Fresh (`< 500 ppm`):** Safe for drinking and cooking.
- **Mildly Brackish (`500..1500 ppm`):** Safe for cooking and bathing; mineral taste.
- **Brackish (`1501..3000 ppm`):** Laundry and cleaning only (corrodes piping and appliances).
- **Saline (`> 3000 ppm`):** Non-potable without Reverse Osmosis (RO).

---

## 🛠️ Tech Stack
- **UI Toolkit:** Jetpack Compose (Material 3) + Edge-to-Edge
- **Architecture:** Clean Architecture + MVI (Model-View-Intent)
- **Local Storage:** Room Database (Offline-first with pre-populated Mombasa seeds)
- **Dependency Injection:** Koin 4
- **Background Work:** WorkManager
- **Testing:** JUnit4 + Cash App Turbine + Coroutines Test
- **Tooling & AI Agents:** Google Android CLI (`android`), Device MCP (`mobile-mcp`), Google Antigravity

---

## 🚀 Quickstart

### 1. Build & Run Tests
```bash
# Run unit tests (Domain classifier + ViewModel Turbine tests)
./gradlew testDebugUnitTest

# Assemble debug APK
./gradlew assembleDebug
```

### 2. Checkpoint Tags
Jump to any stage of the 60-minute workshop:
```bash
git checkout tags/step-0-init           # Initial project scaffolding
git checkout tags/step-1-domain-room     # Domain models & Room database
git checkout tags/step-2-koin-mvi        # MVI contracts & Koin DI
git checkout tags/step-3-compose-ui      # Jetpack Compose M3 UI & Badges
git checkout tags/step-4-workmanager     # WorkManager sync worker
git checkout tags/step-5-testing         # Turbine unit tests
git checkout master                      # Complete reference solution
```

### 3. Device MCP (AI Agent Interaction)
See [docs/device_mcp_guide.md](docs/device_mcp_guide.md) to control the application via Antigravity using either real hardware (`@mobilenext/mobile-mcp`) or the bundled lightweight mock server:
```bash
python3 scripts/mock_device_mcp.py
```

---

## 📖 Codelab Guide
Read [CODELAB_SPEC.md](CODELAB_SPEC.md) for the detailed 60-minute lesson plan, speaker timeline, and Antigravity prompting recipes.
