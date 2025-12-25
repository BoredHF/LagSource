# AGENT_START.md — LagSource (Auto-Build Prompt)

## Role
You are **Codex**, an autonomous senior Minecraft plugin engineer building a sellable plugin called **LagSource**.

You will implement the project end-to-end without asking questions unless something is genuinely blocking.  
You must follow the workflow below exactly.

---

## Source of truth
Use these files as the only scope authority:
- `product.md`
- `mvp.md`
- `architecture.md`

Do **not** invent features outside these files.

---

## Hard constraints
- Build time target: **1–3 days** scope
- Platform: **Paper/Spigot**
- Versions: **1.19.4 – 1.21+**
- **Read-only diagnostics only** (no killing entities, no unloading chunks, no “fix” features)
- Prefer **Paper API** where available, but must run on Spigot too
- No NMS, no reflection

---

## Required workflow (must follow)
1) Create a deep plan mapping tasks to `mvp.md` and `architecture.md`.
2) For each slice:
   - Create a new branch
   - Implement the change
   - Open a PR
   - Perform a code review of your own PR (write review notes)
   - Apply fixes based on review
   - Ensure CI/tests pass
   - Merge only when green

---

## Repository requirements
### Project layout
Single plugin project (simple), Gradle preferred.

Minimum files:
- `README.md` (install + commands + permissions + “not a profiler” disclaimer)
- `LICENSE` (MIT)
- `plugin.yml`
- `config.yml` default
- `src/main/java/...`

### Build
- Gradle build that produces a versioned jar.
- Target Java 17 (compatible with modern Paper).
- Add a GitHub Actions workflow:
  - Build on push + PR
  - Upload artifact (jar)

### Quality
- Use clean architecture as described in `architecture.md`:
  - Command layer
  - Snapshot services
  - Analysis + formatter
- Keep the code defensive:
  - Null checks for worlds
  - Handle unloaded/empty cases
  - Avoid heavy allocation where possible

---

## Feature requirements (MVP)
Implement exactly the MVP:

### Commands
- `/lagsource`
  - Total entity count (optionally excluding players)
  - Top entity types by count (max N from config)
  - Top chunks by entity count (max N from config)
  - Threshold warnings for:
    - total entity count (optional)
    - entities per chunk

- `/lagsource chunk`
  - Focus on chunk hotspot detection output (same underlying scan)

### Config
Provide `config.yml` with:
- `chunk-entity-warning` (default: 100)
- `max-entity-report` (default: 5)
- `include-players` (default: false)

Optional (allowed if you keep it small):
- `total-entity-warning` (default: 2000)  
If you add this, document it in README and keep it consistent.

### Permissions
- `lagsource.use` (required)
- `lagsource.admin` (reserved; not required for MVP unless used)

### Output
- Human-readable, compact output.
- Color coded severity:
  - Green for normal, Yellow for warning, Red for high density
- Must clearly state:
  - “LagSource is not a profiler.”

---

## Implementation details (do this)
### Snapshot algorithm
- For each world:
  - Get entities list
  - Count totals and per entity type
  - For chunk hotspots:
    - Iterate `world.getLoadedChunks()`
    - For each chunk: `chunk.getEntities().length`
- Combine across worlds:
  - Top entity types globally (or per world—pick one, but document it; global is fine)
  - Top chunks list includes world name + chunk X/Z
- Sorting:
  - Use descending sort by counts
  - Limit output using `max-entity-report`

### Performance rules
- This is invoked manually by command; still avoid obviously wasteful behavior.
- Avoid nested scans across worlds where possible.
- Don’t store large intermediate structures unnecessarily.

---

## Deliverables checklist
You must finish with:
- [ ] Working plugin jar builds successfully
- [ ] Commands function and permission-guarded
- [ ] Output matches MVP requirements
- [ ] Config loads with defaults
- [ ] README includes:
  - Install instructions
  - Commands
  - Permissions
  - Config keys
  - Disclaimer: “not a profiler”, “no historical trends”
- [ ] GitHub Actions workflow included
- [ ] Version + changelog note in README

---

## PR plan (mandatory)
Use these PRs (or fewer if truly tiny, but keep slices clean):

### PR1 — Project skeleton
- Gradle setup
- plugin.yml + config.yml
- main plugin class bootstrap
- GitHub Actions build

### PR2 — Command + permissions
- `/lagsource` command registered
- Permission checks + friendly denial message
- Basic output header

### PR3 — Entity snapshot service
- Entity counting by type
- Config options (include-players, max-entity-report)
- Output formatting

### PR4 — Chunk hotspot service
- Loaded chunk scan
- Threshold warnings
- `/lagsource chunk` subcommand

### PR5 — Docs polish + edge cases
- README complete
- Edge-case handling (no worlds? no loaded chunks?)
- Final formatting improvements

Each PR must include a short “How to test” section.

---

## Acceptance test (run locally)
You must provide manual steps in README and ensure these work:

1) Start Paper server with plugin installed.
2) Run `/lagsource` as OP:
   - You see totals, top entity types, top chunks
3) Spawn many mobs in one chunk:
   - That chunk appears as red/warn above threshold
4) Run `/lagsource chunk`:
   - Chunk-only hotspot view prints correctly
5) Run as non-permitted player:
   - Denied message shows, no crash.

---

## Start now
Proceed with PR1 immediately.
