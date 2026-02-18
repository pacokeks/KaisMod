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
- Item-Placeholder-Root-Cause (1.21.11): Jedes Item braucht zusaetzlich `assets/<modid>/items/<item>.json` (Item-Definition). Wenn nur `models/item/*.json` vorhanden ist, erscheinen Custom-Items als Missing-Texture-Placeholder.
- API-Learning (lokale Fabric-Basis): `APIs/Fabric/fabric-api-0.141.3+1.21.11 (1)` ist die passende lokale API fuer dieses Projekt (`minecraft >=1.21.11- <1.21.12-`, `java >=21`, `fabricloader >=0.17.3`).
- API-Diff-Learning (historisch): `fabric-api-0.143.2+26.1` gehoert zur `26.1`-Schiene (`java >=25`) und darf fuer dieses 1.21.11-Projekt nicht verwendet werden.
- API-Diff-Learning (Forge-Quelle): Forge-/NeoForge-Artefakte mit `mods.toml`/`javafml` (z. B. unter `APIs/Forge/...`) sind nicht direkt in Fabric uebernehmbar; nur Konzepte uebernehmen, Implementierung Fabric-nativ bauen.
- UI-Porting-Learning: `forged-container.png` nicht blind zusaetzlich ueber die stabile Basis legen; ohne exakte UV-/Layout-Portierung zerreisst die Werkbank-GUI. Zunaechst den stabilen Basis-Stand halten und fehlende Layer nur gezielt integrieren.
- Getting-Started-Learning: Die Tetra-Werkbank-Progression umfasst den Hammer-Flow (`Hammer` + Rechtsklick auf `minecraft:crafting_table` => `tetra:basic_workbench`), nicht nur das Platzieren eines separaten Upgrade-Blocks.
- Forge-vs-Fabric-Workbench-Learning: Die originale Tetra-Workbench-GUI besteht nicht nur aus Texturen, sondern aus vielen zusaetzlichen GUI-Elementen/Animationen (Mutil-Widgets). Beim Fabric-Port fehlen diese Elemente zunaechst; reine Texturzeichnung kann daher nur eine Teilrekonstruktion sein.
- Screen-Scope-Learning (Originalcode): `forged-container.png` wird in Tetra fuer `ForgedContainerScreen` verwendet, nicht fuer `WorkbenchScreen`. Die Workbench-Basis zeichnet im Original nur `workbench.png` (Ausschnitt `48x48` bei `x+136,y+42`) plus `player-inventory.png` (`x+72,y+153`).
- Asset-Learning: Der `latest.log`-Fehler `PNG header missing` kam von einer 0-Byte-Datei (`textures/item/module/sword/blade/stonecutter/held.png`); solche Dateien beim Port immer auf Dateilaenge > 0 pruefen.
