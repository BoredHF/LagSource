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
