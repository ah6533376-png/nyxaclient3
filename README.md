# Nyxa Client 3.0

Glassmorphic client mod for **Minecraft 1.20.1 (Fabric, Java 17)**.

## Features
- **Glass ClickGUI** — translucent rounded panels, smooth toggle animations, Nyxa yellow accents
- **Search bar with proper focus behavior** — click outside the box and it stops capturing keys until you click it again; Enter toggles the first result
- **HUD editor** — drag anything, scroll to scale, edge/center snapping guides
- **Custom crosshairs** — 6 hand-drawn 15x15 pixel-art styles (bitmap-based) with outline, size, RGB + rainbow color
- **Glass keystrokes** — WASD / mouse buttons (with CPS counters) / spacebar
- **Info HUDs** — FPS, coordinates, CPS
- Modules: Sprint, Fullbright, Zoom, Crosshair, Keystrokes, FPS, Coordinates, CPS
- JSON config saved to `config/nyxa.json`

## Build
Requires JDK 17.
```
./gradlew build        # jar in build/libs/
./gradlew runClient    # test in dev environment
```

## Controls
- Right Shift — ClickGUI
- G — HUD editor
