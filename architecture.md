
---

## 📄 `architecture.md`

```md
# LagSource — Architecture

## High-Level Design
LagSource is a command-driven snapshot analyzer.

It performs short, synchronous reads of the server state and formats the data for human consumption.

There are:
- No background threads
- No persistent storage
- No scheduled tasks

## Core Components

### Command Layer
**LagSourceCommand**
- Permission validation
- Snapshot execution
- Output formatting

### Snapshot Services

#### EntitySnapshotService
- Iterates:
  - World#getEntities()
- Aggregates:
  - EntityType → count
- Optional player exclusion

#### ChunkSnapshotService
- Iterates:
  - World#getLoadedChunks()
- For each chunk:
  - Chunk#getEntities()
- Collects:
  - Chunk → entity count

## Analysis Layer
- Sorts results
- Applies thresholds
- Limits output size
- Flags severity

## Output Formatter
- Plain text
- Color-coded severity
- Compact and copy-friendly

Example:
