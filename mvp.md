# LagSource — MVP Specification

## MVP Goal
Deliver a fast, safe, read-only lag inspection tool that provides immediate insight during TPS drops.

## Supported Platforms
- Paper / Spigot
- Target versions: 1.19.4 – 1.21+

## Commands

### /lagsource
Displays a live snapshot of:
- Total loaded entities
- Top entity types by count
- Top lag-heavy chunks
- Optional tile entity counts

### /lagsource chunk
- Shows top N chunks by entity density
- Displays chunk coordinates and world
- Flags chunks exceeding configurable thresholds

## Core MVP Features

### 1. Live Entity Snapshot
- Count all loaded entities
- Group by entity type
- Sorted descending

Example output:
Top Entities:

Villager: 412

Cow: 287

Item: 201


### 2. Chunk Hotspot Detection
- Iterate loaded chunks
- Count entities per chunk
- Identify top offenders

Example:


Hot Chunks:
world (123, -45): 189 entities
world_nether (22, 8): 142 entities


### 3. Threshold Warnings
- Configurable limits:
  - Entities per chunk
  - Total entity count
- Highlight problematic areas

### 4. Read-Only Safety
- No entity removal
- No chunk unloading
- No async world mutation

## Configuration
```yaml
chunk-entity-warning: 100
max-entity-report: 5
include-players: false
```

Permissions

lagsource.use

lagsource.admin (future-proofed)

Explicit MVP Exclusions

No TPS sampling

No async profiling

No graphs or charts

No historical storage

No web UI
