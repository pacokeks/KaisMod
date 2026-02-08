# AGENTS.md

## Projektregeln
- Kommunikation und Dokumentation erfolgen auf Deutsch.
- Nutze **Serena-Tools wann immer moeglich**, insbesondere fuer Codebasis-Navigation, Symbolsuche und strukturierte Aenderungen.
- Shell-Kommandos nur verwenden, wenn Serena fuer den konkreten Schritt nicht geeignet ist.
- Bei Konflikt zwischen Quellen gilt: offizielle Fabric-Dokumentation vor Drittquellen.
- Dieses Projekt ist ein Mod fuer **Fabric** fuer **Minecraft 1.21.11**.
- Fuer Builds immer `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` verwenden.
- Der Agent darf Minecraft (Launcher/Client/`runClient`) **niemals selbst starten**; Ingame-Tests und Screenshots werden ausschliesslich vom Nutzer ausgefuehrt.
- Wenn Informationen benoetigt werden, nutze immer auch diese lokalen Dateien:
  - `C:\Users\pasca\Desktop\Kai\MinecraftMod\documentation.md`
  - `C:\Users\pasca\Desktop\Kai\MinecraftMod\konzept.md`
  - `C:\Users\pasca\Desktop\Kai\MinecraftMod\mod.md`
- Nutze bei Informationsbedarf immer auch diesen lokalen Pfad: `C:\Users\pasca\Desktop\Kai\MinecraftMod\MinecraftMods`
- Nutze bei Informationsbedarf immer auch diese offiziellen Fabric-Quellen:
  - `https://wiki.fabricmc.net/start`
  - `https://docs.fabricmc.net/develop/`
  - `https://docs.fabricmc.net/players/`

## Learnings (Stand: 2026-02-08)
- UI-/Asset-Referenz fuer die Werkbank ist Tetra `MinecraftMods/tetra-1.20.1-6.11.0`.
- Die GUI-Texturen `workbench.png`, `forged-container.png` und `player-inventory.png` muessen byte-identisch aus Tetra uebernommen werden (Pfad: `assets/tetra/textures/gui/`).
- Screen-Rendering fuer die Upgrade-Werkbank nutzt direkte Texturzeichnung (kein alternatives Sprite-Mapping):
  - Hintergrund: `179x176`
  - `forged-container`: Position relativ zum Screen `x+0, y-13`, Groesse `179x128`
  - `player-inventory`: Position relativ zum Screen `x+0, y+103`, Groesse `179x106`
- Slot-Raster muss zum Tetra-Grid passen:
  - Slot-Abstand `17` (nicht `18`)
  - Spielerinventar Start `x=12, y=116`
  - Hotbar `y=171`
  - Obere Werkbank-Slots: `x=46/63/80/97`, `y=51`
- Crash-Fix: `UpgradeWorkbenchScreenHandler` braucht Reentrancy-Schutz bei `onContentChanged`/`updateResult`, damit kein rekursives `markDirty`-Loop entsteht.
- Build-Skript-Learning:
  - Mods-Zielordner darf nicht hart auf einen Usernamen gesetzt sein; `.minecraft/mods` wird ueber aktuellen `ApplicationData`-Pfad aufgeloest.
  - Wenn der absolute AGENTS-Buildpfad auf einem Rechner nicht existiert, im Repo mit `./build.ps1` bauen.
- Testprozess-Learning: Agent startet Minecraft niemals selbst; Ingame-Validierung und Screenshots ausschliesslich durch den Nutzer.
