# Device MCP Guide: MajiSafi Pwani

This codelab integrates **Model Context Protocol (MCP)** to allow AI agent workflows (such as Google Antigravity) to observe and control the Android app on physical devices, emulators, or simulated mock environments.

---

## 1. Real Device / Emulator Setup (`mobile-mcp`)
The project includes support for `@mobilenext/mobile-mcp`. This server interacts directly with ADB and UIAutomator.

### Prerequisites
- Node.js & npm installed
- ADB installed and configured in your `PATH`
- Connected Android device with USB Debugging enabled, or a running Android Virtual Device (AVD)

### Configuration
The `mcp_config.json` file in the root of the project defines the server:
```json
{
  "mcpServers": {
    "mobile-mcp": {
      "command": "npx",
      "args": ["-y", "@mobilenext/mobile-mcp@latest"]
    }
  }
}
```

### Agent Prompting Recipes with Device MCP
When using Antigravity with `mobile-mcp` enabled:
- *"Take a screenshot of the current device screen and tell me if the Nyali borehole card shows the Fresh badge."*
- *"Tap the Tudor filter chip and inspect the list to ensure only Tudor boreholes are visible."*
- *"Open the Log Salinity Reading sheet, input 950 ppm, set reporter to 'Mombasa Tech Lead', and submit."*

---

## 2. Simulated Environment (`mock-device-mcp`)
For attendees without physical devices, USB cables, or high-spec laptops able to run emulators, we have provided a bundled **Mock Device MCP Server**: `scripts/mock_device_mcp.py`.

### Running the Mock MCP Server
Test the mock server directly in bash:
```bash
python3 scripts/mock_device_mcp.py
```

### Tools Provided
1. `device_inspect_screen`: Returns simulated UI hierarchy with top bar, estate chips, and borehole list.
2. `device_tap_filter_chip`: Simulates filtering by estate (`estate: "Nyali"`).
3. `device_submit_salinity_reading`: Simulates submitting a crowdsourced reading to the Room DB.
4. `device_trigger_sync`: Simulates triggering the WorkManager sync worker.
