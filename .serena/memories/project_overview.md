# Projektueberblick
- Name: MinecraftMod (Fabric Mod fuer Minecraft 1.21.11)
- Zweck: Modulares Werkzeug-/Waffensystem mit Upgrade-Werkbank (Kopf/Griff/Kern, genau ein Kern pro Tool)
- Stack: Java, Fabric Loader/API, Gradle (Loom)
- Wichtige Quellen laut AGENTS.md: `documentation.md`, `konzept.md`, `mod.md`, und lokale Referenzen in `MinecraftMods`.
- Wichtige Regel: Agent startet Minecraft nie selbst; Ingame-Tests/Screenshots nur durch Nutzer.

# Struktur (high level)
- `KaisMod/`: eigentliche Mod (Gradle-Projekt)
- `KaisMod/src/main/java`: Common/Server-Logik
- `KaisMod/src/client/java`: Client-UI/Screen
- `KaisMod/src/main/resources`: Assets/JSON/Textures
- `build.ps1`: bevorzugtes Build-Skript fuer Gesamtworkflow inkl. Kopie nach `.minecraft/mods`.