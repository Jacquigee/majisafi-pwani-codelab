#!/usr/bin/env python3
"""
Mock Mobile Device MCP Server for MajiSafi Pwani Codelab.
Provides standard MCP tools to simulate interacting with an Android device
running the MajiSafi Pwani application.
"""

import sys
import json

TOOLS = [
    {
        "name": "device_inspect_screen",
        "description": "Inspects currently visible UI elements on the Android device running MajiSafi Pwani.",
        "inputSchema": {
            "type": "object",
            "properties": {
                "screen_name": {
                    "type": "string",
                    "description": "Optional name of the target screen to verify (e.g., 'BoreholeScreen')"
                }
            }
        }
    },
    {
        "name": "device_tap_filter_chip",
        "description": "Simulates tapping an Estate filter chip (e.g. Nyali, Tudor, Bamburi).",
        "inputSchema": {
            "type": "object",
            "properties": {
                "estate": {
                    "type": "string",
                    "description": "Name of the estate (e.g., 'Nyali', 'Tudor', 'Likoni')"
                }
            },
            "required": ["estate"]
        }
    },
    {
        "name": "device_submit_salinity_reading",
        "description": "Simulates opening the bottom sheet and submitting a crowdsourced TDS reading for a borehole.",
        "inputSchema": {
            "type": "object",
            "properties": {
                "borehole_id": {"type": "integer", "description": "ID of the target borehole"},
                "tds_ppm": {"type": "integer", "description": "TDS reading in ppm"},
                "reporter_name": {"type": "string", "description": "Name of person or organisation reporting"}
            },
            "required": ["borehole_id", "tds_ppm", "reporter_name"]
        }
    },
    {
        "name": "device_trigger_sync",
        "description": "Taps the cloud sync icon in the top app bar to execute WorkManager background sync.",
        "inputSchema": {
            "type": "object",
            "properties": {}
        }
    }
]

def handle_request(req):
    method = req.get("method")
    req_id = req.get("id")

    if method == "initialize":
        return {
            "jsonrpc": "2.0",
            "id": req_id,
            "result": {
                "protocolVersion": "2024-11-05",
                "capabilities": {
                    "tools": {}
                },
                "serverInfo": {
                    "name": "majisafi-mock-device-mcp",
                    "version": "1.0.0"
                }
            }
        }

    if method == "notifications/initialized":
        return None

    if method == "tools/list":
        return {
            "jsonrpc": "2.0",
            "id": req_id,
            "result": {
                "tools": TOOLS
            }
        }

    if method == "tools/call":
        params = req.get("params", {})
        tool_name = params.get("name")
        args = params.get("arguments", {})

        if tool_name == "device_inspect_screen":
            content = (
                "📱 Screen: BoreholeScreen\n"
                "TopBar: 'MajiSafi Pwani' [CloudSync icon]\n"
                "Search: 'Search by borehole name or estate...'\n"
                "Chips: [All Estates (selected), Nyali, Tudor, Bamburi, Kisauni, Likoni, Changamwe, Mombasa Island]\n"
                "List: 8 boreholes loaded (Nyali Links: 420 ppm [Fresh], Tudor Kiosk: 880 ppm [Mildly Brackish], Bamburi: 2150 ppm [Brackish], Likoni: 3450 ppm [Saline])"
            )
            return {
                "jsonrpc": "2.0",
                "id": req_id,
                "result": {"content": [{"type": "text", "text": content}]}
            }

        elif tool_name == "device_tap_filter_chip":
            estate = args.get("estate", "All Estates")
            content = f"✅ Tapped chip '{estate}'. Borehole list updated to show boreholes in {estate}."
            return {
                "jsonrpc": "2.0",
                "id": req_id,
                "result": {"content": [{"type": "text", "text": content}]}
            }

        elif tool_name == "device_submit_salinity_reading":
            bid = args.get("borehole_id")
            tds = args.get("tds_ppm")
            reporter = args.get("reporter_name")
            content = f"✅ Successfully logged reading: Borehole #{bid} = {tds} ppm by {reporter}. Queued locally in Room DB (isSynced=false)."
            return {
                "jsonrpc": "2.0",
                "id": req_id,
                "result": {"content": [{"type": "text", "text": content}]}
            }

        elif tool_name == "device_trigger_sync":
            content = "☁️ WorkManager SalinitySyncWorker triggered. All pending offline readings marked as synced."
            return {
                "jsonrpc": "2.0",
                "id": req_id,
                "result": {"content": [{"type": "text", "text": content}]}
            }

        else:
            return {
                "jsonrpc": "2.0",
                "id": req_id,
                "error": {"code": -32601, "message": f"Unknown tool: {tool_name}"}
            }

    return {
        "jsonrpc": "2.0",
        "id": req_id,
        "error": {"code": -32601, "message": f"Unhandled method: {method}"}
    }

def main():
    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue
        try:
            req = json.loads(line)
            res = handle_request(req)
            if res:
                sys.stdout.write(json.dumps(res) + "\n")
                sys.stdout.flush()
        except Exception as e:
            err = {
                "jsonrpc": "2.0",
                "id": None,
                "error": {"code": -32603, "message": str(e)}
            }
            sys.stdout.write(json.dumps(err) + "\n")
            sys.stdout.flush()

if __name__ == "__main__":
    main()
