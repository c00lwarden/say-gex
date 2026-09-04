# Portfolio Client — Fabric 26.2

A harmless, client-only educational Fabric mod for a programming portfolio.
It adds a toggleable diagnostic HUD and demonstrates key bindings, client tick
events, lightweight performance sampling, and safe HUD rendering. It does not
send packets, alter movement, inspect other players, or change server gameplay.

## What it demonstrates

- **UI enhancement:** an in-game overlay with frame rate, memory use, session
  time, and tick count.
- **Performance awareness:** metrics are sampled once per second instead of on
  every frame, keeping the HUD work deliberately small.
- **Diagnostics:** the overlay reports JVM memory, FPS, and client uptime.
- **Events:** Fabric's client tick event handles the user-configurable keybind.

## Controls

- `F8` toggles the diagnostics HUD. Change it under **Options → Controls → Key
  Binds → Portfolio Client**.

## Build and run

1. Install **JDK 25** and **Gradle 9.5.1**. Minecraft 26.2's Fabric toolchain
   uses Java 25 and Loom 1.17.
2. Open a terminal in this folder.
3. Run `gradle wrapper --gradle-version 9.5.1` once. This creates the standard
   Gradle wrapper files for the project.
4. Run `./gradlew runClient` on macOS/Linux, or `gradlew.bat runClient` on
   Windows, to launch a separate development Minecraft instance.
5. Run `./gradlew build` (or `gradlew.bat build`) to create the distributable
   JAR in `build/libs/`.
6. Put the non-`-sources` JAR into the `mods` folder of a Fabric 26.2 profile
   that has Fabric API installed.

The first run downloads Minecraft, Fabric, and development dependencies.

## Build it on a website — no admin needed

1. Make a free GitHub account at https://github.com.
2. Create a new empty repository, then use **Add file → Upload files** to upload
   everything inside this project folder, including the hidden `.github` folder.
3. Open the repository's **Actions** tab, choose **Build Portfolio Client**, and
   press **Run workflow**.
4. When the green check appears, open that run and download the
   `portfolio-client-26.2` artifact. Inside is the JAR.
5. Put the non-`-sources` JAR in your CurseForge profile's `mods` folder.

## Project layout

```
src/main/java/io/warden/portfolio/PortfolioClient.java  Mod entry point and events
src/main/java/io/warden/portfolio/DiagnosticHud.java    HUD rendering and metrics
src/main/resources/fabric.mod.json                       Loader metadata
src/main/resources/assets/.../en_us.json                 Keybind text
```

## Portfolio talking points

The code keeps all functionality local to the client. `ClientTickEvents` is
used only to process the F8 key. `DiagnosticHud` reads local JVM and FPS values,
then renders text through Minecraft/Fabric's supported HUD API. No raw OpenGL
calls are used, which matters because Minecraft 26.2 can run with Vulkan.
