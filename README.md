# LagSource

LagSource is a lightweight, read-only diagnostics plugin for Paper/Spigot that provides an instant snapshot of entity and chunk hotspots.

LagSource is not a profiler. It provides point-in-time snapshots only and does not store historical data.

## Requirements
- Minecraft: 1.19.4 - 1.21+
- Java: 17
- Platform: Paper or Spigot

## Install
1. Download the latest jar from the GitHub Actions artifact.
2. Place the jar in your server's `plugins` folder.
3. Start the server to generate `config.yml`.
4. Run `/lagsource` as an operator.

## Commands
- `/lagsource` - Snapshot of total entities, top entity types, and hot chunks.
- `/lagsource chunk` - Chunk-only hotspot snapshot.

## Permissions
- `lagsource.use` - Required to use LagSource commands.
- `lagsource.admin` - Reserved for future features.

## Configuration
```yaml
chunk-entity-warning: 100
max-entity-report: 5
include-players: false
```

- `chunk-entity-warning`: Threshold used for chunk warning severity.
- `max-entity-report`: Maximum number of top entries to display.
- `include-players`: Whether to include players in entity counts.

## Output Notes
- Human-readable, compact output.
- Color-coded severity for chunk density.
- LagSource is not a profiler.

## Manual Test Checklist
1. Start a Paper server with the plugin installed.
2. Run `/lagsource` as OP:
   - You see totals, top entity types, and hot chunks.
3. Spawn many mobs in one chunk:
   - That chunk appears above the warning threshold.
4. Run `/lagsource chunk`:
   - Chunk-only output appears.
5. Run as a non-permitted player:
   - Access is denied cleanly.

## Version
- Current: 0.1.0-SNAPSHOT
- Changelog: initial MVP build in progress.
