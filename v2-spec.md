# LagSource v2 Spec (Proposed)

## Scope Statement
This spec defines an optional v2 feature set for LagSource. It is separate from the MVP and is not implied by the current `product.md`, `mvp.md`, or `architecture.md`. It introduces GUI workflows, nearby scans, and snapshot history.

## Goals
- Provide local, actionable diagnostics for administrators near a suspected lag source.
- Provide a GUI dashboard for quick inspection and navigation.
- Provide automatic, read-only snapshots triggered by low TPS events.

## Non-Goals
- Automated fixes (entity removal, chunk unloads, etc.).
- NMS or reflection.
- Asynchronous world modification.
- Persistent storage beyond optional future extension.

## Commands
- `/lagsource` (unchanged): live snapshot of current state.
- `/lagsource chunk` (unchanged): hot chunk-only output.
- `/lagsource nearby [radius]`: scan chunks within a radius of the executor.
- `/lagsource gui`: open main dashboard.
- `/lagsource snapshots`: open snapshot browser GUI.
- `/lagsource snapshot now`: manual capture (admin/testing).

## Permissions
- `lagsource.use` (existing)
- `lagsource.admin` (required for snapshot capture and clear)
- `lagsource.tp` (optional, if later allowing teleport; default OFF)

## Configuration (Additive)
```yaml
nearby-radius: 3
snapshot-enabled: true
snapshot-tps-threshold: 18.0
snapshot-below-threshold-seconds: 10
snapshot-cooldown-seconds: 120
snapshot-buffer-size: 10
```

## GUI: Main Dashboard
Inventory size: 27 slots.

Display:
- TPS indicator
- Total entities
- World breakdown (one item per world)
- Top entity types (top N)
- Top chunks (top N)

Buttons:
- Refresh (re-run snapshot)
- Settings (read-only config view)
- Help

Click actions:
- World item -> World Detail Menu
- Chunk item -> Chat message with chunk info + suggested /tp
- Entity type -> “Where are they?” menu

## GUI: World Detail Menu
Shows:
- Entities in that world
- Top entity types in that world
- Top chunks in that world

## GUI: Chunk Click
Sends chat message:
- Chunk coords + world
- Suggested `/tp x y z` to chunk center
- Entity count

Teleport behavior:
- Suggest-only for v2 (no teleport by default).
- Optional future: `lagsource.tp` permission gate.

## GUI: Entity Type -> “Where are they?”
Shows top chunks where a selected entity type is concentrated (top 5).

Implementation options:
- Preferred: collect per-entity-type chunk counts while scanning, but only for top entity types to control memory.
- Alternative: recompute on click (slower, but on-demand).

## Snapshot System
### Triggering
- Every 1s: read TPS.
- Maintain counter of seconds TPS < threshold.
- If counter >= below-threshold-seconds AND cooldown passed -> capture snapshot.

### TPS Provider
- Paper: use `Bukkit.getTPS()`.
- Spigot: show “TPS unavailable” and disable auto-capture.

### Snapshot Contents
Each snapshot stores:
- timestamp
- TPS value at capture
- total entities (optionally excluding players)
- entities by world
- top entity types (global)
- top chunks (global)

### Snapshot Store
- In-memory ring buffer (ArrayDeque)
- Size limited by `snapshot-buffer-size`
- Optional future disk store (explicitly not included in v2)

## Snapshot GUI Flow
### Snapshot Browser Menu
Each item shows:
- time
- TPS
- total entities
- worst chunk entity count

Buttons:
- Refresh
- Close
- Clear (admin)

### Snapshot Detail Menu
Tabs/buttons:
- Worlds
- Entity Types
- Hot Chunks

### World Detail Menu
Top entity types in that world
Top chunks in that world

### Chunk click behavior
As above (suggest /tp in chat)

## Architecture Changes (Additions)
### New Core Classes
- `TpsProvider`
  - `PaperTpsProvider`
  - `FallbackTpsProvider`
- `SnapshotScheduler` (repeating task, 1s)
- `SnapshotService` (builds LagSnapshot using collectors)
- `SnapshotStore` (ring buffer)

### GUI Layer
- `SnapshotBrowserMenu`
- `SnapshotDetailMenu`
- `MenuRouter` (inventory click events + state)

### Data Model
- `LagSnapshot { id, timestamp, tps, totals, perWorld, topTypes, topChunks }`
- `ChunkKey { worldName, x, z }`

## Performance Constraints
- Snapshot is read-only.
- No async world access.
- Avoid heavy allocations and recompute only when needed.
- Cap output and GUI lists by config limits.

