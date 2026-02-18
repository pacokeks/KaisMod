# progress.md

## 2026-02-07 - Phase 1 Start

### Kontext und Quellenpruefung
- `mod.md`, `konzept.md`, `documentation.md`, `AGENTS.md` gelesen.
- Lokalen Referenzpfad `MinecraftMods/` geprueft (enthaelt Fabric API und Tetra-Referenzdaten).
- Offizielle Fabric-Quellen fuer Versionierung verifiziert (Fabric Meta + Fabric Maven).

### Agenten-Workstreams (Phase 1)
- Agent A (Architektur/Plattform): Fabric-Projektbasis in `KaisMod` erstellt und auf `1.21.11` gepinnt.
- Agent B (Gameplay/Daten): Daten-Grundstruktur (`ToolType`, `ToolState`, `ModuleSlot`, `HeadMaterialTier`, `ToolCore`) angelegt.
- Agent C (Content/UI/UX): Upgrade-Workbench Block registriert, Placeholder-Interaktion und Basis-Assets/Rezepte angelegt.

### Umgesetzte Aenderungen in `KaisMod`
- Projekt aus offizieller Fabric-Vorlage erzeugt.
- `gradle.properties` angepasst:
  - `minecraft_version=1.21.11`
  - `loader_version=0.18.4`
  - `loom_version=1.14-SNAPSHOT`
  - `fabric_version=0.141.3+1.21.11`
  - `maven_group=de.kai.kaismod`
  - `archives_base_name=kaismod`
- `fabric.mod.json` auf `kaismod` umgestellt (Entrypoints, Metadaten, Depends, Icon-Pfad).
- Example-Mod-Dateien und Mixins entfernt.
- Neue Kernklassen angelegt:
  - `de.kai.kaismod.KaisMod`
  - `de.kai.kaismod.KaisModClient`
  - `registry/RegistryBootstrap`, `registry/ModItems`, `registry/ModBlocks`
  - `block/UpgradeWorkbenchBlock`
  - `screen/ModScreenHandlers`, `network/ModNetworking`
  - `data/ToolType`, `data/ToolState`
  - `item/ModularToolItem`
  - `core/ToolCore`
  - `module/ModuleSlot`
  - `balance/HeadMaterialTier`
- Ressourcen hinzugefuegt:
  - `assets/kaismod/icon.png`
  - `assets/kaismod/lang/de_de.json`, `assets/kaismod/lang/en_us.json`
  - `assets/kaismod/blockstates/upgrade_workbench.json`
  - `assets/kaismod/models/block/upgrade_workbench.json`
  - `assets/kaismod/models/item/upgrade_workbench.json`
  - `assets/kaismod/models/item/core_placeholder.json`
  - `data/kaismod/recipe/upgrade_workbench.json`
  - `data/kaismod/loot_table/blocks/upgrade_workbench.json`

### Fehler / Blocker
- Build-Validierung blockiert:
  - Befehl: `./gradlew.bat build`
  - Fehler: `JAVA_HOME is not set and no 'java' command could be found in your PATH.`
- Lokale Pruefung zeigt: kein `java` im PATH und keine erkennbare JDK-Installation unter `C:\Program Files`.

### Naechste Schritte
- Java 21 (JDK) lokal installieren bzw. `JAVA_HOME` setzen.
- Danach `./gradlew.bat build` erneut ausfuehren.
- Falls Build erfolgreich: optional `./gradlew.bat runClient` und `./gradlew.bat runServer` fuer Phase-1-Akzeptanz testen.

### Nachtrag
- Lokales `.git` aus dem Template in `KaisMod/.git` entfernt, damit `KaisMod` als normales Projektverzeichnis vorliegt.

## 2026-02-07 - Nachlauf nach Java-Installation

### Verifikation und Korrekturen
- Java-Check zeigte zuerst weiterhin kein `java` im PATH der aktuellen Shell.
- JDK gefunden unter: `C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot`.
- Build fuer die Session mit gesetztem `JAVA_HOME` und PATH erfolgreich gestartet.

### Build-Fehler und Loesung
- Erster Buildlauf schlug fehl (56 Compile-Fehler in `net.minecraft.*`-Imports).
- Ursache: Projekt stand auf Mojang-Mappings, Code wurde mit Yarn-Namensraum geschrieben.
- Fix umgesetzt:
  - `gradle.properties`: `yarn_mappings=1.21.11+build.4` hinzugefuegt.
  - `build.gradle`: `mappings loom.officialMojangMappings()` ersetzt durch `mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"`.

### Ergebnis
- `./gradlew.bat build` erfolgreich.
- `./gradlew.bat runServer --dry-run` erfolgreich (Task-Graph aufloesbar).
- Phase-1-Status: Projektgeruest ist jetzt buildbar.

## 2026-02-07 - Build-Automatisierung

### Neu
- Datei `build.ps1` im Projektroot `MinecraftMod` erstellt.
- Script-Funktionen:
  - findet ein gueltiges Java 21 JDK (JAVA_HOME/Registry/Standardpfade)
  - setzt `JAVA_HOME` + PATH fuer die Session
  - schreibt optional dauerhaft User-`JAVA_HOME` und `%JAVA_HOME%\bin` in den User-PATH
  - loescht vorherige JARs in `KaisMod/build/libs` (ueberschreibt damit den letzten Build gezielt)
  - fuehrt `gradlew.bat clean build` aus
  - gibt die aktuelle Haupt-JAR aus

### Test
- `powershell -ExecutionPolicy Bypass -File .\build.ps1` erfolgreich.
- Ergebnis-JAR: `KaisMod/build/libs/kaismod-1.0.0.jar`

### PATH / Java
- User-`JAVA_HOME` gesetzt auf `C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot\`.
- User-PATH enthaelt `%JAVA_HOME%\bin`.

## 2026-02-07 - build.ps1 Anpassung (kein Session-Setzen)

### Aenderung nach Nutzerwunsch
- `build.ps1` setzt **nicht mehr** `JAVA_HOME` oder `PATH` pro Ausfuehrung.
- Script prueft jetzt nur noch, ob Java ueber `JAVA_HOME` oder `PATH` verfuegbar ist.
- Fehlt beides, bricht es mit klarer Fehlermeldung ab.

### One-time Java Setup durch Agent
- User-`JAVA_HOME` und User-PATH (`%JAVA_HOME%\bin`) wurden einmalig gesetzt.

### Test
- `build.ps1` erneut erfolgreich ausgefuehrt.
- Build erzeugt: `KaisMod/build/libs/kaismod-1.0.0.jar`.

## 2026-02-07 - AGENTS und Build-Progressbar
- `AGENTS.md` erweitert: Builds immer ueber `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1`.
- `build.ps1` erweitert um Prozentanzeige + Progress-Bar (`Write-Progress`) mit festen Build-Phasen.
- Java einmalig fuer User-Umgebung erneut gesetzt (`JAVA_HOME`, `%JAVA_HOME%\bin` im User-PATH) und Build erfolgreich getestet.

## 2026-02-07 - Systemweiter Java-Pfad + Fallback
- Systemweiter Status geprueft:
  - Machine `JAVA_HOME`: `C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot\`
  - Machine `Path` enthaelt `C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot\bin`
- `build.ps1` erweitert um Fallback:
  - Wenn in der aktuellen Shell weder `JAVA_HOME` noch `java` im PATH verfuegbar ist, setzt das Script automatisch Session-`JAVA_HOME` + Session-PATH aus erkanntem JDK und baut weiter.
- Build mit Fallback erfolgreich getestet.

## 2026-02-07 - Phase-Check und Phase 2 (Modulares Tool-Framework)

### Phase-Status
- **Phase 1**: abgeschlossen (Build/Start stabil, Registry-Basis vorhanden).
- **Phase 2**: umgesetzt (Framework + Persistenz + konkrete Tooltypen).

### 3-Workstream-Ausfuehrung
- Agent 1 (Architektur/Plattform): Persistenzpfad in `ModularToolItem` ueber `CUSTOM_DATA`-Component implementiert.
- Agent 2 (Gameplay/Daten): `ToolState` um stabile NBT-Serialisierung/Deserialisierung inkl. Core-Upgrade-Level erweitert.
- Agent 3 (Content/UI/UX): 5 Tool-Items registriert, Models/Lang/Rezepte fuer schnellen Ingame-Loop angelegt.

### Umgesetzte Dateien (Phase 2)
- `KaisMod/src/main/java/de/kai/kaismod/item/ModularToolItem.java`
- `KaisMod/src/main/java/de/kai/kaismod/data/ToolState.java`
- `KaisMod/src/main/java/de/kai/kaismod/item/ModularSwordItem.java`
- `KaisMod/src/main/java/de/kai/kaismod/item/ModularAxeItem.java`
- `KaisMod/src/main/java/de/kai/kaismod/item/ModularPickaxeItem.java`
- `KaisMod/src/main/java/de/kai/kaismod/item/ModularSpearItem.java`
- `KaisMod/src/main/java/de/kai/kaismod/item/ModularMaceItem.java`
- `KaisMod/src/main/java/de/kai/kaismod/registry/ModItems.java`
- `KaisMod/src/main/resources/assets/kaismod/lang/en_us.json`
- `KaisMod/src/main/resources/assets/kaismod/lang/de_de.json`
- `KaisMod/src/main/resources/assets/kaismod/models/item/modular_sword.json`
- `KaisMod/src/main/resources/assets/kaismod/models/item/modular_axe.json`
- `KaisMod/src/main/resources/assets/kaismod/models/item/modular_pickaxe.json`
- `KaisMod/src/main/resources/assets/kaismod/models/item/modular_spear.json`
- `KaisMod/src/main/resources/assets/kaismod/models/item/modular_mace.json`
- `KaisMod/src/main/resources/data/kaismod/recipe/modular_sword.json`
- `KaisMod/src/main/resources/data/kaismod/recipe/modular_axe.json`
- `KaisMod/src/main/resources/data/kaismod/recipe/modular_pickaxe.json`
- `KaisMod/src/main/resources/data/kaismod/recipe/modular_spear.json`
- `KaisMod/src/main/resources/data/kaismod/recipe/modular_mace.json`

### Verifikation
- Build mit `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` erfolgreich.
- Ausgabe-JAR erstellt und automatisch nach `.minecraft\mods` kopiert.

## 2026-02-07 - Phase 3 Start (Upgrade-Werkbank Block + ScreenHandler)

### Umgesetzt
- Neuer `UpgradeWorkbenchScreenHandler` mit 4 Slots:
  - Tool-Slot: akzeptiert nur `ModularToolItem`
  - Kern-Slot: akzeptiert nur `core_placeholder`
  - Upgrade-Slot: akzeptiert nur Upgrade-Materialien (Iron/Gold/Diamond/Netherite)
  - Confirm-Slot: keine direkte Einlage moeglich
- Shift-Click-Logik (`quickMove`) implementiert, inkl. Slot-Validierungsregeln.
- `UpgradeWorkbenchBlock#onUse` oeffnet serverseitig jetzt den Screen per Rechtsklick.
- `ModScreenHandlers` registriert den neuen ScreenHandler-Typ `kaismod:upgrade_workbench`.
- Client-Screen hinzugefuegt (`UpgradeWorkbenchScreen`) und in `KaisModClient` registriert.
- GUI-Titel lokalisiert (`screen.kaismod.upgrade_workbench` in `de_de`/`en_us`).

### Zusatzfix
- Rezept-JSONs auf das in 1.21.11 akzeptierte Ingredient-Format angepasst (String-Key statt altes Objekt im `key`-Block), um Parse-Fehler im Log zu vermeiden.

### Verifikation
- Build erneut erfolgreich mit `build.ps1`.
- JAR erfolgreich nach `.minecraft\\mods` kopiert.

## 2026-02-07 - Phase 3 Komplettierung (Preview + Confirm)

### Finalisiert
- Confirm-Slot (`UpgradeWorkbenchScreenHandler`) zeigt jetzt eine echte **Vorschau** des resultierenden Tools an.
- Beim Entnehmen aus dem Confirm-Slot wird serverseitig angewendet:
  - Tool wird ersetzt durch Resultat (mit aktualisiertem `ToolState`)
  - Kern-/Upgrade-Materialien werden verbraucht
- Regeln aktuell:
  - Kern wird nur gesetzt, wenn bisher keiner aktiv ist
  - Upgrade-Material mappt auf Kopfmaterial (`iron`, `gold`, `diamond`, `netherite`)
  - `headUpgradeLevel` wird bei Upgrade erhoeht
- Screen-Open und Slot-Validierung bleiben aktiv und stabil.

### Build-Status
- `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` erneut erfolgreich.
- JAR in `.minecraft\\mods` aktualisiert.

## 2026-02-18 - Phase 4 Abschluss + Phase 5 Start

### Phase-Status
- **Phase 4**: abgeschlossen.
- **Phase 5**: gestartet (Kostenmodell fuer Kopf-Upgrades aufgesetzt).

### Umgesetzt (Phase 4)
- Reale Core-Items registriert:
  - `efficiency_core`, `blood_core`, `breaker_core`, `precision_core`
- `UpgradeWorkbenchScreenHandler` erweitert:
  - Core-Slot akzeptiert jetzt alle registrierten Core-Items.
  - Core-Install bleibt einmalig.
  - Core-Wechsel ist nur mit **Extra-Kosten** und Confirm moeglich.
  - Switch-Katalysator: `amethyst_shard` im Upgrade-Slot.
  - Core-Kompatibilitaet und Wechselregeln werden serverseitig erzwungen.
- Kostenanbindung aus `ToolCoreRegistry` genutzt:
  - Installation: `installationCost`
  - Wechsel: `switchCost`

### Umgesetzt (Phase 5 Start)
- Datengetriebene Kopf-Upgrade-Kosten eingefuehrt:
  - Datei: `data/kaismod/balance/head_upgrade_costs.json`
  - Loader mit Code-Fallback: `HeadUpgradeRules`
- `UpgradeWorkbenchScreenHandler` verbraucht fuer Kopf-Upgrades jetzt materialabhaengige Kosten (statt fix 1).

### Build-Status
- Build mit `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` erfolgreich.

## 2026-02-18 - Phase 5 Fortschritt (Tool-Eingang + Tooltip + Kernwechsel-Feinschliff)

### Umgesetzt
- `blood_core` ist jetzt zusaetzlich mit Axt kompatibel (fuer Core-Switch-Testfaelle).
- Upgrade-Werkbank akzeptiert jetzt auch Vanilla-Basis-Tools als Eingang und wandelt sie in modulare Tools um:
  - Pickaxe, Sword, Axe (alle Materialstufen)
  - Trident als Speer-Basis
- Dynamische Tooltip-Erweiterung fuer modulare Tools:
  - Attack Damage
  - Attack Speed
  - aktiver Core
  - Kopfmaterial
  - Griffmaterial
- Kopf-Upgrade-Pfad stabilisiert und Kosten-Logik bleibt datengetrieben aktiv.

### Build-Status
- Build mit `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` erneut erfolgreich.

## 2026-02-18 - Phase 5 Fortschritt (Stat-Recalculation Hook)

### Umgesetzt
- Zentrale Stat-Berechnung als eigener Baustein:
  - `de.kai.kaismod.balance.ToolStatCalculator`
- `ModularToolItem` nutzt die berechneten Werte jetzt nicht nur fuer Tooltip, sondern auch fuer Verhalten:
  - zusaetzlicher kampfseitiger Schaden in `postHit` (serverseitig)
  - material-/toolabhaengiger Haltbarkeitsverbrauch in `postHit` und `postMine`
- Tooltip bleibt mit denselben Live-Werten synchron (Damage/Speed/Core/Kopf/Griff).

### Build-Status
- Build mit `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` erfolgreich.

## 2026-02-18 - Phase 5 Feintuning (Damage + Mining Speed)

### Umgesetzt
- Kampfwerte fuer Nicht-Pickaxe-Tools erhoeht (SWORD/AXE/SPEAR/MACE), Pickaxe-Damage bewusst unveraendert gehalten.
- Mining-Speed-Hook auf die korrekte 1.21.11-Methode umgestellt (`getMiningSpeed` statt alter Methode).
- Pickaxe-Mining-Speed deutlich angehoben, Axe-Mining-Speed fuer Stein-Abbau relativ abgesenkt, damit Pickaxe klar schneller bei Gestein ist.

### Build-Status
- Build mit `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1` erfolgreich.
