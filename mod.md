# mod.md - Umsetzungsplan (Fabric 1.21.11)

## 1) Ziel und Scope
Wir bauen eine neue Minecraft Mod fuer **Fabric 1.21.11** mit einem modularen Upgrade-System.
Alle Ziel-Items bestehen aus 3 Teilen:
- Teil 1: Kopf
- Teil 2: Griff
- Teil 3: Kern

Betroffene Item-Typen:
- Schwert
- Axt
- Spitzhacke
- Speer (custom)
- Mace

Spezial-Block:
- Neue Werkbank (Mischung aus Workbench + Schmiedetisch)
- Oeffnet per **Rechtsklick**
- Hat ein eigenes GUI (crafting-table-aehnlicher Fluss)

Wichtige Regel:
- Beim normalen Crafting hat das Tool noch **keinen Kern**.
- Der Kern wird erst spaeter in der neuen Upgrade-Werkbank eingesetzt.
- Der Kopf kann ueber Upgrade-Materialien verbessert werden.

## 2) Verbindlicher Arbeitsmodus
Dieser Plan ist der Arbeitsvertrag fuer die Umsetzung. Wir arbeiten in Phasen und schliessen jede Phase mit messbaren Kriterien ab. Keine Feature-Ausweitung vor Abschluss der vorherigen Phase.

## 3) 3-Agenten-Setup (Arbeitsaufteilung)
Wir nutzen 3 spezialisierte Agenten-Workstreams:

### Agent 1 - Architektur & Plattform
Verantwortung:
- Fabric 1.21.11 Projektbasis
- Registries, Datenmodell, Component/NBT-Persistenz
- Netzwerkpakete, Events, Server-Authoritaet

Deliverables:
- Build laeuft
- Basisklassen + Registrierungen stehen
- Save/Load der 3 Teile stabil

### Agent 2 - Gameplay & Balancing
Verantwortung:
- Stat-System fuer Kopf/Griff/Kern
- Upgrade-Regeln, Kostenlogik, Progression
- Tool-spezifische Werte fuer Schwert/Axt/Pickaxe/Speer/Mace

Deliverables:
- Material-Tiers mit differenzierten Stats
- Upgrade-Validierung und Regeln im Code erzwungen
- Kein ungueltiger Zustand moeglich

### Agent 3 - Content, UI & UX
Verantwortung:
- Upgrade-Werkbank Block + Screen + Slots
- Rechtsklick-Interaktion
- Texturen/Modelle/Language/Rezepte
- Nutzerfluss, Tooltip, Kostenvorschau

Deliverables:
- GUI komplett nutzbar
- Rechtsklick oeffnet zuverlaessig
- Assets/Rezepte/Localization konsistent

Optional spaeter (Sub-Agenten bei Bedarf):
- Testing-Agent
- Data-Pack-Agent
- Render-Agent

## 4) Basisannahmen und Versionen
Stand heute (2026-02-07):
- Ziel bleibt **Minecraft 1.21.11**
- Fabric Blogpost fuer 1.21.11 datiert auf **2025-12-05**
- Fuer Dev wird dort Loom 1.14 genannt und Loader 0.18.1 als aktueller Stable zum Zeitpunkt des Posts

Umsetzungshinweis:
- Endgueltige Versionspins werden beim Setup gegen aktuelle maven/modrinth Versionen geprueft.

## 5) Technisches Design

### 5.1 Datenmodell pro Tool
Jedes modulare Tool speichert:
- `tool_type` (sword, axe, pickaxe, spear, mace)
- `head_material` (wood/stone/copper/iron/gold/diamond/netherite)
- `handle_material` (start z. B. wood/leather/metal, erweiterbar)
- `core_id` (optional, exakt 0 oder 1)
- `upgrade_level_head` (int)
- `upgrade_level_core` (int oder core-intern)

Regel:
- `core_id` darf nie mehrere Werte haben.

### 5.2 Upgrade-Werkbank GUI (Slots)
Minimaler Slot-Plan:
- Slot A: Tool-Slot (nur modulare Ziel-Items)
- Slot B: Kern-Slot (Core-Items)
- Slot C: Upgrade-Material-Slot (erz/ingot/gem je nach Tier)
- Slot D: Ergebnis/Preview oder Bestätigungs-Trigger

Zusatz:
- Kosten- und Effektvorschau sichtbar vor Bestaetigung.
- Bestaetigungsschritt zwingend bei Kernwechsel.

### 5.3 Rechtsklick-Oeffnung
Geplante Umsetzung:
- Rechtsklick ueber `UseBlockCallback` oder Block-`onUse`/`useItemOn` behandeln
- Wenn Zielblock Upgrade-Werkbank ist: Use-Interaktion priorisieren, Screen oeffnen
- Schutz gegen Desync/Spam via Cooldown + Server-Validation

## 6) Material-Tiers fuer Kopf-Upgrade (alle Tools/Waffen)
Diese Tiers gelten als gemeinsame Basis fuer Kopfwerte. Agent 2 tuned final pro Tool.

| Material    | Rarity      | Damage Mod | Mining Speed Mod | Durability Mod | Enchant Mod | Knockback/Impact |
|-------------|-------------|------------|------------------|----------------|-------------|------------------|
| wood        | common      | 0.85       | 0.90             | 0.70           | 1.00        | 0.90             |
| stone       | common      | 1.00       | 1.00             | 1.00           | 0.95        | 1.00             |
| copper      | uncommon    | 1.05       | 1.10             | 1.05           | 1.05        | 1.05             |
| iron        | uncommon    | 1.15       | 1.15             | 1.25           | 1.00        | 1.10             |
| gold        | rare        | 1.00       | 1.35             | 0.60           | 1.35        | 0.95             |
| diamond     | epic        | 1.35       | 1.25             | 1.80           | 1.20        | 1.20             |
| netherite   | legendary   | 1.50       | 1.20             | 2.40           | 1.10        | 1.35             |

Tool-spezifische Auspraegung:
- Pickaxe priorisiert Mining Speed + Durability
- Schwert priorisiert Damage + Attack Speed Balance
- Axt priorisiert Damage + Impact
- Speer priorisiert Reach/Throw-Utility + Precision
- Mace priorisiert Impact + Heavy Damage

## 7) Mehrschrittiger Durchfuehrungsplan

### Phase 0 - Analyse und Uebernahme aus Referenzmod
- Sichtung von `MinecraftMod/tetra-1.20.1-6.11.0` (nur als konzeptionelle Hilfe)
- Extraktion von UX-Ideen, NICHT Forge-Code 1:1 kopieren

Akzeptanz:
- Liste von wiederverwendbaren Konzepten erstellt
- Risiken bei Forge -> Fabric dokumentiert

### Phase 1 - Fabric Projektgeruest
- Neues Fabric-Projekt fuer 1.21.11 aufsetzen
- Pakete anlegen: `item`, `core`, `module`, `block`, `screen`, `registry`, `network`, `data`, `balance`
- Basale Registry-Helper bauen

Akzeptanz:
- Client + Dedicated Server starten ohne Fehler

### Phase 2 - Modulares Tool-Framework
- `ModularToolItem` abstrakt erstellen
- Konkrete Klassen fuer 5 Tooltypen erstellen
- Persistenz fuer Kopf/Griff/Kern ueber Components/NBT

Akzeptanz:
- Tool erstellt, gespeichert, geladen ohne Datenverlust

### Phase 3 - Upgrade-Werkbank Block + ScreenHandler
- Neuen Block registrieren
- Rechtsklick-Open implementieren
- GUI mit Slot-Validierung + Preview + Confirm bauen

Akzeptanz:
- Block laesst sich platzieren
- Rechtsklick oeffnet GUI stabil
- Ungueltige Items werden abgelehnt

### Phase 4 - Kern-System
- Core-Definition (id, kompatible Tooltypen, Effekte, Tooltip)
- Kern-Installation einmalig
- Kernwechsel nur mit Extra-Kosten + Confirm

Akzeptanz:
- Pro Tool genau 1 Kern aktiv
- Wechselregel serverseitig erzwungen

### Phase 5 - Kopf-Upgrade-System
- Materialtiers als Datenquelle (json + code fallback)
- Upgrade-Checks: naechstes Tier nur wenn gueltig und Kosten vorhanden
- Stat-Recalculation fuer Toolattribute

Akzeptanz:
- Kopf-Upgrades funktionieren fuer alle 5 Tooltypen
- Werte aendern sich nachvollziehbar nach Tier

### Phase 6 - Spezialsysteme pro Tool
- Pickaxe: Mining-Logik + optionale Kernhooks
- Schwert/Axt/Mace: Combat-Hooks
- Speer: Reichweite/Wurf-Logik

Akzeptanz:
- Jeder Tooltyp hat klar unterscheidbares Verhalten

### Phase 7 - Content und Data
- Rezepte fuer Basistools ohne Kern
- Rezept fuer Upgrade-Werkbank
- Core-Items + Lang-Dateien + Models/Textures Platzhalter

Akzeptanz:
- Voller Test-Loop in Survival moeglich

### Phase 8 - Balancing + Gating
- Progression (z. B. Diamond/Netherite Upgrades erst spaeter)
- Wirtschaftskosten fuer Upgrade/Kernwechsel
- Feintuning mit Testprotokollen

Akzeptanz:
- Keine offensichtliche OP-Kombination
- Jede Tier-Stufe hat nachvollziehbaren Nutzen

### Phase 9 - QA und Release-Vorbereitung
- Unit/Integration-Tests fuer Validierung und Persistenz
- Multiplayer-Checks (Server autoritativ)
- Changelog, known issues, release build

Akzeptanz:
- Keine kritischen Crashes im Core-Loop
- Release-kandidat laeuft client+server

## 8) Risiken und Gegenmassnahmen
- Risiko: Rechtsklick kollidiert mit Vanilla-Interaktionen (Item-Nutzung, anderer Block-Use)
  - Gegenmassnahme: `ActionResult` sauber steuern (`SUCCESS`/`PASS`), serverseitige Validierung + Cooldown
- Risiko: Forge-Referenz nicht direkt portierbar
  - Gegenmassnahme: nur Design uebernehmen, Code neu in Fabric schreiben
- Risiko: Stat-Explosion durch Kern + Kopf
  - Gegenmassnahme: harte Caps, Servervalidierung, zentraler Stat-Rechner
- Risiko: Multiplayer-Desync
  - Gegenmassnahme: Server-only Autoritaet, klare Sync-Pakete

## 9) Definition of Done (gesamt)
Die Mod ist fertig, wenn:
- Fabric 1.21.11 Build stabil laeuft
- Neue Upgrade-Werkbank per Rechtsklick nutzbar ist
- Schwert, Axt, Spitzhacke, Speer, Mace modular sind
- Kopf/Griff/Kern korrekt gespeichert und angewendet werden
- Kopf-Upgrades ueber Holz/Stein/Kupfer/Eisen/Gold/Diamant/Netherite funktionieren
- Kernsystem mit 1-Kern-Regel und Wechselkosten aktiv ist
- Basis-Balancing spielbar und dokumentiert ist

## 10) Nächster konkreter Schritt
Direkt nach diesem Dokument startet **Phase 1**: Fabric-Projekt fuer 1.21.11 anlegen und die Paketstruktur + Basisklassen erzeugen.

---

## 11) Phase 0 - Detailplan und Durchfuehrungsprotokoll (abgeschlossen)

Status: **abgeschlossen am 2026-02-07**

### 11.1 Ziel von Phase 0
Vor dem Coding wurde die Referenzmod `MinecraftMod/tetra-1.20.1-6.11.0` analysiert, um:
- Datenstruktur und Upgrade-Architektur zu verstehen
- direkte Porting-Risiken von Forge 1.20.1 zu Fabric 1.21.11 zu dokumentieren
- einen belastbaren technischen Startpunkt fuer Phase 1 zu definieren

### 11.2 Genaue Schrittfolge (Phase-0-Plan)
1. **Inventarisierung der Referenzmod**
   - Struktur, Dateitypen, Datenumfang, Kernordner erfassen
2. **Datenmodell-Analyse**
   - Module, Improvements, Materials, Repairs, Recipes, Lang-Schluessel vergleichen
3. **Interaktions- und UI-Analyse**
   - Workbench/Forged-Workbench Begrifflichkeiten, Slot- und Schematics-Fluss ableiten
4. **Migrationsanalyse Forge -> Fabric**
   - Loader/Metadata/Mixin/Events/API-Risiken erfassen
5. **Ableitung fuer Zielmod**
   - Welche Konzepte werden uebernommen, vereinfacht oder bewusst verworfen
6. **Abschlussartefakt**
   - Erkenntnisse als Wissensdatenbank in `mod.md` dokumentieren

### 11.3 Durchgefuehrt mit 3 Agenten (Workstreams)

#### Agent A - Architektur/Plattform
Untersuchte Punkte:
- `META-INF/mods.toml` (Forge-Abhaengigkeit)
- Mixins und Paketstruktur
- Registry-/Block-/Screen-relevante Klassennamen

Erkenntnisse:
- Referenz ist klar Forge-basiert (`modLoader=javafml`, Forge 47, MC 1.20.1)
- Klassenstruktur bestaetigt modulare Tool-Architektur und Workbench-zentrierten Flow
- Direkte Code-Uebernahme ist **nicht** sinnvoll; nur Architekturideen portieren

#### Agent B - Gameplay/Daten
Untersuchte Punkte:
- `data/tetra/modules`, `improvements`, `materials`, `repairs`, `schematics`
- Beispiel: `modules/single/spearhead.json`, `materials/metal/iron.json`

Erkenntnisse:
- Stark datengetriebenes Design (JSON-first) mit klaren Domänen:
  - Modul-Definition
  - Material-Statistik
  - mehrstufige Verbesserungen
  - reparatur-/socket-basierte Upgrades
- Dieses Muster passt sehr gut zu unserem Kopf/Griff/Kern-System
- Fuer unsere Mod sollten wir ein schlankeres JSON-Schema nutzen (weniger Komplexitaet als Tetra)

#### Agent C - UX/Content
Untersuchte Punkte:
- `assets/tetra/lang/en_us.json`
- Workbench-Texte, Schematic-/Kosten-/Fehler-Hinweise
- Blockstate/Model der `forged_workbench`

Erkenntnisse:
- Gute UX-Idee: klare Fehltexte bei ungueltigem Material/Kosten/Integritaet
- UI-Fluss basiert auf Vorschau + Bestaetigung (sehr relevant fuer Kernwechsel)
- Visuelles Muster der Werkbank kann als Inspiration fuer unseren Block dienen

### 11.4 Konkrete Referenzdateien (wissensrelevant)
- `MinecraftMod/tetra-1.20.1-6.11.0/META-INF/mods.toml`
- `MinecraftMod/tetra-1.20.1-6.11.0/tetra.mixins.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/modules/single/spearhead.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/modules/single/basic_handle.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/modules/single/perk_socket_0.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/improvements/single/spearhead/hone_damage.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/improvements/single/spearhead/tempered.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/materials/metal/iron.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/repairs/sockets/single/perk_0/pristine_diamond.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/data/tetra/recipes/stonecutter.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/assets/tetra/lang/en_us.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/assets/tetra/blockstates/forged_workbench.json`
- `MinecraftMod/tetra-1.20.1-6.11.0/assets/tetra/models/block/forged_workbench.json`

### 11.5 Uebernahmeentscheidung fuer unsere Mod

Direkt uebernehmen (Konzept):
- datengetriebene Module/Materialien/Upgrades
- klarer Upgrade-UI-Flow mit Vorschau + Bestaetigung
- trennscharfe Toolteile und eindeutige Slot-Zustaende

Anpassen (vereinfacht):
- statt voller Tetra-Schematic-Komplexitaet ein gezieltes Kopf/Griff/Kern-System
- statt sehr vieler Materialkategorien zunaechst Fokus auf Kopf-Tiers (wood -> netherite)

Bewusst verwerfen:
- Forge-spezifische Implementierungen
- Tetra-spezifische Sondermechaniken, die nicht Teil unseres Scopes sind

### 11.6 Migrationsrisiken und Gegenmassnahmen (Phase-0-Ergebnis)
1. **Loader/API-Risiko** (Forge vs Fabric)
   - Massnahme: komplette Neuumsetzung auf Fabric API + Loom
2. **Interaktionsrisiko Rechtsklick** (Blockabbau kollidiert mit GUI-Oeffnen)
   - Massnahme: serverseitige Interception + Break-Cancel + Cooldown
3. **Datenkonsistenz** (Itemdaten, Core-Regeln)
   - Massnahme: zentraler Validator fuer ToolState (1 Kern max)
4. **Balancing-Risiko** (Material + Kern stapelt zu stark)
   - Massnahme: Stat-Caps und Tool-spezifische Gewichtungen
5. **Netzwerk/Desync-Risiko**
   - Massnahme: Server autoritativ, Client nur Preview

### 11.7 Output-Artefakte aus Phase 0
- Referenzstruktur inventarisiert (Assets + Data + Class-Packages)
- Kerndateien fuer Architektur, Data-Design und UX identifiziert
- Risiko- und Migrationsmatrix dokumentiert
- Genaue Uebergabe in Phase 1 vorbereitet

### 11.8 Genaue Uebergabe an Phase 1 (sofort umsetzbar)
Phase 1 startet mit diesen verbindlichen Aufgaben:
1. Fabric-1.21.11 Projektgeruest erzeugen (Loom/Loader/API pins)
2. Paketstruktur laut Kapitel 7 Phase 1 anlegen
3. `ToolState` als zentrale Datenstruktur definieren
4. Registries fuer:
   - modulare Tools (5 Typen)
   - Upgrade-Werkbank-Block
   - ScreenHandler + Netzwerkkanal
5. Minimal lauffaehiger End-to-End-Flow:
   - Welt startet
   - Upgrade-Block platzierbar
   - GUI testweise oeffnbar

### 11.9 Akzeptanzkriterien fuer "Phase 0 abgeschlossen"
- Referenzmod wurde in Struktur, Daten, UX und Plattform ausgewertet
- Porting-Risiken wurden klar benannt
- Uebernahme- und Nicht-Uebernahme-Regeln festgelegt
- Naechste Phase hat konkrete, umsetzbare Startaufgaben

Ergebnis: **Phase 0 ist abgeschlossen.**

## 12) Fabric-Wiki/Doku-Ergaenzungen (2026-02-07)

### 12.1 Verbindliche Plattformregeln fuer Phase 1+
- `wiki.fabricmc.net/start` nur als Einstieg nutzen; die Seite verweist selbst auf neue Doku und kann unvollstaendig/veraltet sein.
- Primärquelle fuer Implementierung: `docs.fabricmc.net` + offizielle FabricMC-Seiten.
- Versionen immer gegen `fabricmc.net/develop` pinnen (Minecraft, Yarn, Loader, Loom, Fabric API).

### 12.2 Setup-Qualitaetsregeln (Fehlervermeidung)
- Mod-Projekt nicht in Cloud-/Sync-Ordnern (OneDrive/Dropbox) entwickeln.
- Kein Pfad mit Sonderzeichen/Leerzeichen fuer den Build-Workspace.
- Java-Version strikt passend zur Zielversion setzen (laut Fabric-Entwicklerseite fuer 1.21.11: Java 21).

### 12.3 Interaktions- und UI-Regeln (fuer unsere Werkbank)
- Werkbank-Oeffnung per Rechtsklick technisch ueber `UseBlockCallback`/`onUse`, nicht ueber Attack-Events.
- Screen-Architektur: Serverlogik in `ScreenHandler`, Client-UI getrennt im Screen.
- Bei zusaetzlichen Positions-/Kontextdaten `ExtendedScreenHandlerFactory` nutzen.

### 12.4 Daten- und Persistenzregeln
- Itemdaten auf modernen Fabric-Pfaden halten (Data Components + NBT nur wo erforderlich).
- Tool-Zustand zentral validieren (`ToolState`), insbesondere 1-Kern-Regel serverseitig erzwingen.
- JSON-/Datapack-getriebene Material- und Upgrade-Daten als Primärmodell beibehalten.

### 12.5 Netzwerk- und Testregeln
- Alle Gameplay-Entscheidungen serverautoritativ; Client nur Darstellung/Preview.
- Frueh GameTest/automatisierte Tests fuer Kernregeln, Slot-Validierung und Persistenz einbauen.
- Vor jedem Versionssprung Porting-Seiten in der Fabric-Doku lesen (Breaking/API-Changes).

### 12.6 Lokale Fabric-API-Pruefung (wichtig)
Analyse der lokalen Datei `MinecraftMod/fabric-api-0.143.2+26.1/fabric.mod.json`:
- Version: `0.143.2+26.1`
- Abhaengigkeit: `minecraft ~26.1-`
- Abhaengigkeit: `fabricloader >=0.18.4`
- Abhaengigkeit: `java >=25`
- Enthaltene Untermodule: 42 JARs

Bewertung:
- Diese lokale Fabric-API wirkt wie ein Build fuer die `26.1`-Schiene und **passt wahrscheinlich nicht direkt** zu einem stabilen 1.21.11-Setup (das laut Fabric-Entwicklerseite mit Java 21 gefuehrt wird).
- Phase 1 muss daher zuerst die **genau kompatible** Fabric-API fuer 1.21.11 pinnen, statt blind die lokale `0.143.2+26.1` zu uebernehmen.

