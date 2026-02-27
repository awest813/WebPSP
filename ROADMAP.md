# Jpcsp Modernization Roadmap

This roadmap proposes practical, incremental changes to modernize Jpcsp with a focus on:
- better performance on low-spec devices (especially Chromebook-class hardware),
- improved reliability and startup experience,
- quality-of-life (QoL) upgrades for everyday use.

## Guiding goals

1. **Performance first on weak CPUs/GPUs**: stable frame pacing over peak FPS.
2. **Battery-aware defaults**: avoid unnecessary background work.
3. **Faster first-run and safer updates**: simpler setup, fewer regressions.
4. **Clear diagnostics**: easy to understand what setting improves what.

---

## Phase 1 (0-3 months): Low-risk wins

### Performance and compatibility
- Add a **"Low-Spec" preset** that automatically selects:
  - native PSP resolution,
  - conservative post-processing,
  - shader simplification,
  - reduced texture cache memory.
- Add **dynamic frame skip** with frame-time budget targeting (instead of static skip).
- Implement **GPU capability probing** at startup to auto-disable expensive features on weak iGPUs.
- Add a **lightweight software rendering fallback profile** tuned for very old or unsupported OpenGL stacks.

### Chromebook-focused improvements
- Add a **"Chromebook profile"** (Intel UHD/Celeron style defaults) prioritizing low CPU overhead.
- Improve handling of **high-latency storage** (eMMC) by reducing synchronous file scanning and caching metadata.
- Add a **single-thread-safe mode** for thermally constrained devices to avoid aggressive thread contention.

### QoL
- Improve settings UX with clear labels: **"Performance cost: Low/Medium/High"**.
- Add **"Reset to recommended"** and **"Explain this option"** helper text in configuration panels.
- Add first-run wizard: choose device class (desktop/laptop/chromebook/legacy PC) and apply profile.

### Delivery and quality
- Add baseline telemetry logs (local, opt-in) for frame-time, shader compile time, and stutter events.
- Add CI checks for startup regression and key menus loading successfully.

---

## Phase 2 (3-6 months): Core optimization work

### Rendering pipeline modernization
- Reduce driver overhead by batching small draw calls and minimizing state changes.
- Add **shader cache persistence** (compile once, reuse across sessions).
- Move expensive texture conversion paths to asynchronous workers where safe.
- Add adaptive internal resolution scaling tied to frame-time (with hysteresis to avoid oscillation).

### CPU and JIT improvements
- Improve JIT hot-path caching and reduce deoptimization churn.
- Add targeted micro-optimizations for common syscall/module paths observed in popular games.
- Profile lock contention and reduce coarse-grained synchronization in hot emulation loops.

### Memory and I/O
- Add smarter cache eviction policies for low-memory devices.
- Improve UMD/ISO read-ahead and block cache strategy for slow storage.
- Add compact-memory mode for devices with 4 GB RAM or less.

### QoL
- Game-specific auto-profiles with safe defaults and per-title override UI.
- Better controller mapping presets and deadzone calibration wizard.
- Searchable settings with tags: "performance", "graphics", "audio", "latency".

---

## Phase 3 (6-12 months): Platform and UX modernization

### Platform reach
- Validate support for modern Java runtimes (e.g., 17/21) while preserving Java 8 compatibility path if required.
- Improve Linux/ChromeOS packaging guidance (AppImage/flatpak-style distribution docs).
- Harden startup and runtime behavior on Mesa/iGPU stacks common on Chromebooks.

### Stability and maintainability
- Expand automated regression suite:
  - boot smoke tests for known titles,
  - save/load stability checks,
  - networking and media playback sanity tests.
- Introduce performance guardrails in CI (no >X% regressions on tracked scenes).
- Improve logging and crash-report bundles for easier issue triage.

### QoL
- In-app compatibility hints sourced from community profiles (offline cache).
- Better pause/resume handling for laptop/chromebook sleep cycles.
- Faster startup path by lazy-loading non-critical plugins/tools.

---

## Candidate "quick-fix" backlog (can start immediately)

- Add "Low-Spec" toggle in UI that flips a curated set of existing options.
- Cache game library metadata to avoid full rescans on every launch.
- Add "disable expensive effects" one-click switch in graphics settings.
- Add frame-time graph overlay for debugging stutter.
- Improve default key bindings and provide import/export profiles.
- Add "portable mode" with self-contained config folder.

---

## Success metrics

Track these per release:
- Median and 1% low FPS on low-end test devices.
- Shader compilation stutter events per 10 minutes of gameplay.
- Cold start to "first frame" time.
- Memory usage after 30 minutes in-game.
- Number of user reports requiring manual tuning to become playable.

---

## Suggested low-spec test matrix

- Intel Celeron/Pentium Chromebook-class CPU, 4 GB RAM, integrated graphics.
- Older Intel UHD iGPU Windows laptop.
- Mid-range desktop baseline for regression comparison.

For each build, test:
- one 2D-heavy title,
- one 3D action title,
- one FMV-heavy title,
- one networking-enabled title.
