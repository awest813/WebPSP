# JPCSP Modernization & Performance Roadmap

This document outlines the strategic plan for modernizing the JPCSP codebase, improving performance, and enhancing maintainability.

## 1. Build System & Dependency Management

The current project uses Apache Ant with manual dependency management (jars in `lib/`). To improve reproducibility and dependency management, we should migrate to a modern build system.

*   **Migrate to Maven or Gradle**:
    *   Standardize project structure (`src/main/java`, `src/test/java`, `src/main/resources`).
    *   Manage dependencies declaratively (e.g., `pom.xml` or `build.gradle`).
    *   Simplify CI/CD configuration.
*   **Dependency Updates**:
    *   **Log4j 1.x -> Log4j 2.x / SLF4J**: Upgrade from the outdated and potentially vulnerable Log4j 1.2.15.
    *   **JUnit 4 -> JUnit 5**: Modernize the testing framework.
    *   **LWJGL**: Ensure we are on the latest stable version of LWJGL 3.
    *   **Xuggler**: Evaluate alternatives or update if possible, as Xuggler is quite old. Consider generic FFmpeg wrappers (e.g., JavaCV).

## 2. Java Version & Code Modernization

The project currently targets Java 1.8. Modernizing the language level brings performance benefits and developer productivity.

*   **Upgrade to Java 17 LTS (or 21 LTS)**:
    *   Take advantage of new GC implementations (ZGC, Shenandoah) for lower latency.
    *   Use improved JIT compiler features.
*   **Language Features**:
    *   **Try-with-resources**: Ensure all IO streams and native resources are closed properly.
    *   **Var**: Use type inference for local variables to reduce verbosity.
    *   **Functional Programming**: Utilize Streams API and Lambdas where appropriate for cleaner collection processing.
    *   **Switch Expressions**: Simplify control flow.
*   **Code Quality**:
    *   **Fix Warnings**: SYSTEMATICALLY address compiler warnings (Raw types, Unchecked casts, Deprecated APIs).
    *   **Static Analysis**: Integrate tools like SonarQube, Checkstyle, or SpotBugs.
    *   **Formatting**: Enforce a consistent code style (e.g., Google Java Format).

## 3. Performance Improvements

*   **Profiling**:
    *   Use modern profilers (VisualVM, JProfiler, async-profiler) to identify current bottlenecks during emulation.
*   **Memory Management**:
    *   Optimize object allocation to reduce GC pressure.
    *   Review usage of `ByteBuffer` vs `byte[]` for large data chunks.
    *   Investigate `Unsafe` usage and consider migration to `VarHandle` or `MemorySegment` (Project Panama) in newer Java versions.
*   **JIT / Dynarec**:
    *   Review `jpcsp.Allegrex.compiler` for optimization opportunities.
    *   Update ASM usage to leverage latest bytecode features.
*   **Parallelism**:
    *   Review threading model. Ensure thread safety and maximize multi-core usage for independent tasks (Audio, GPU, CPU).

## 4. Architecture & Refactoring

*   **Decoupling**:
    *   Reduce coupling between core emulation components (CPU, Memory, HLE modules).
    *   Interface-based design to allow swapping implementations (e.g., different GPU backends).
*   **Refactoring Large Classes**:
    *   Break down monolithic classes (e.g., `KirkEngine`, `Emulator`, HLE Modules) into smaller, testable units.

## 5. UI/UX

*   **Swing Modernization**:
    *   Adopt a modern Look and Feel (e.g., FlatLaf) for an immediate visual upgrade.
    *   Improve High-DPI support.
*   **JavaFX Migration (Long-term)**:
    *   Consider migrating the UI to JavaFX for better hardware acceleration and modern UI controls.

## 6. Testing

*   **Unit Tests**: Increase coverage for core logic (CPU instructions, Crypto, format parsers).
*   **Integration Tests**: Automated testing of ROM loading and boot sequences.
*   **Regression Testing**: Maintain a suite of test ROMs/Homebrew to catch regressions.
