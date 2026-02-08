# Tetra Fabric Refactor (Minecraft 1.21.11)

## Ziel
Dieser Ordner enthaelt:
- `refactored/tetra-1.20.1-6.11.0`: unveraenderte Original-Kopie von Tetra (Forge 1.20.1)
- `refactored/tetra-fabric`: lauffaehiger Fabric-Refactor fuer 1.21.11

## Warum ein Refactor statt 1:1-Port
Die Originalversion ist Forge-basiert (`mods.toml`, Forge-Container/Capabilities, Forge-Netzwerk/Events) und kann nicht direkt unter Fabric 1.21.11 geladen werden.

## Konkrete Porting-Schritte
1. **Projektbasis**
- Fabric/Loom-Projekt auf 1.21.11 erstellt (an `KaisMod`-Setup angelehnt)
- Java 21 Target, Loader/Fabric API Pins gesetzt

2. **Namespace & Loader-Metadaten**
- Mod-ID auf `tetra` gesetzt
- Fabric Entrypoints:
  - `se.mickelus.tetra.TetraFabricMod`
  - `se.mickelus.tetra.TetraFabricModClient`

3. **Code-Struktur**
- Paketstruktur von `de.kai.kaismod` nach `se.mickelus.tetra` umgestellt
- Registry-/Screen-/Block-/Item-IDs auf `tetra` normalisiert

4. **Assets & Daten**
- Originale Tetra-Assets und Datapacks aus `tetra-1.20.1-6.11.0` in das Fabric-Projekt uebernommen
- GUI-Texturen `workbench.png`, `forged-container.png`, `player-inventory.png` sind im Fabric-Projekt unter `assets/tetra/textures/gui/` vorhanden

5. **Build-Status**
- Build von `refactored/tetra-fabric` erfolgreich:
  - `build/libs/tetra-refactored-6.11.0-fabric-1.21.11.jar`
  - `build/libs/tetra-refactored-6.11.0-fabric-1.21.11-sources.jar`

## Wichtiger Hinweis
Dieser Stand ist ein **technisch lauffaehiger Fabric-Refactor-Startpunkt** (Mod laedt, Build ok, Referenzassets/-daten integriert), aber kein vollstaendiger Feature-gleichwertiger Forge->Fabric-Port der kompletten Original-Tetra-Logik.
