# documentation.md - Fabric Wissensdatenbank (Stand: 2026-02-07)

## 1) Zweck
Dieses Dokument buendelt die wichtigsten Informationen aus:
- Fabric Wiki (`wiki.fabricmc.net/start`)
- Offizieller Fabric-Dokumentation (`docs.fabricmc.net`)
- Spielerbereich (`docs.fabricmc.net/players`)
- Entwicklerbereich (`docs.fabricmc.net/develop`)
- Lokaler Analyse deiner heruntergeladenen Fabric-API

Ziel:
- Sauberer Projektstart mit minimalen Integrationsfehlern
- Fruehe Vermeidung typischer Build-/Versions-/API-Probleme

## 2) 5-Agenten-Auswertung (durchgefuehrt)

### Agent 1 - Wiki-Startanalyse
Ergebnis:
- `wiki.fabricmc.net/start` ist Einstiegspunkt, aber der Hinweis ist klar: Inhalte koennen unvollstaendig/veraltet sein.
- Die Seite verweist auf die neue Dokumentation unter `docs.fabricmc.net`.

Konsequenz:
- Wiki nur orientierend nutzen, Implementierungsentscheidungen immer mit aktueller Doku pruefen.

### Agent 2 - Doku-Architektur (Hauptseite)
Ergebnis:
- `docs.fabricmc.net` ist die primaere, strukturierte Quelle fuer Players/Develop/API-Themen.
- Es gibt klar getrennte Einstiegspfade fuer Spieler und Entwickler.

Konsequenz:
- Fuer unser Projekt wird die Entwicklerdoku als primaeres Entscheidungsfundament genutzt.

### Agent 3 - Entwicklerdoku (Develop)
Kernpunkte fuer unsere Mod:
- **Projektsetup**: sauberer Workspace, kein Sync-Ordner, stabile Toolchain.
- **Events**: Interaktionen werden ueber Event-Callbacks/Block-Use sauber abgefangen.
- **Networking**: Server bleibt autoritativ, Client zeigt nur Zustand/Preview.
- **Screen/ScreenHandler**: Logik serverseitig im Handler, UI clientseitig.
- **Block Entities & Daten**: strukturierte Persistenz, klarer Datentransfer.
- **Data Components**: moderne Datenspeicherung fuer Itemdaten, wo sinnvoll.
- **GameTest/Tests**: fruehe automatisierte Tests reduzieren Regressions.
- **Porting/Updates**: vor Versionswechseln zwingend Porting-Notes pruefen.

Konsequenz:
- Unser Upgrade-System wird serverautoritativ und testgetrieben aufgebaut.

### Agent 4 - Spielerbereich (Players)
Kernpunkte:
- Installation aufgeteilt nach Launcher/Installer/Java-Setup.
- Bei Fehlverhalten sind Loader-/Java-/Profilkonflikte typische Ursachen.
- Mod-Kompatibilitaet und saubere Modquellen sind zentral.

Konsequenz:
- Wir liefern spaeter klare Nutzerhinweise (Java, Loader, kompatible Versionen, bekannte Konflikte).

### Agent 5 - Lokale Fabric-API-Analyse
Analysierte Dateien:
- `MinecraftMod/fabric-api-0.143.2+26.1/fabric.mod.json`
- `MinecraftMod/fabric-api-0.143.2+26.1.jar`

Ergebnis:
- Fabric API Version: `0.143.2+26.1`
- Abhaengigkeit: `minecraft ~26.1-`
- Abhaengigkeit: `fabricloader >=0.18.4`
- Abhaengigkeit: `java >=25`
- Enthaltene Submodule: **42** JAR-Module (u. a. Events, Networking, Screen, Transfer, Tags, Renderer)

Wichtige Bewertung:
- Diese lokale API wirkt auf die `26.1`-Schiene ausgerichtet.
- Fuer ein stabiles 1.21.11-Projekt muss zuerst die exakt kompatible API-Version gegen Fabric-Entwicklerseite/Maven verifiziert werden.

## 3) Kritische Erkenntnisse fuer unser Projekt

### 3.1 Versionierung und Toolchain
- Ziel bleibt `Minecraft 1.21.11`.
- Laut Fabric-Entwicklerseite gibt es fuer 1.21.11 konkrete Versionsvorgaben (Minecraft, Yarn, Loader, Loom, API, Java).
- Diese Pins muessen in Phase 1 **explizit** gesetzt werden (keine implizite "latest"-Annahme).

### 3.2 Rechtsklick-Interaktion (Upgrade-Werkbank)
Fuer unsere Werkbank ist technisch korrekt:
- Rechtsklick via Block-Use/Event (`UseBlockCallback`/`onUse`) behandeln
- Kein Missbrauch von Attack-Events fuer Rechtsklick-Funktion
- Rueckgabewerte (`ActionResult`) sauber steuern, um Konflikte mit Vanilla-Use zu vermeiden

### 3.3 UI- und Netzwerkarchitektur
- ScreenHandler auf dem Server als authoritative Logikschicht
- Client-Screen nur Darstellung, Vorschau, Eingabe
- Bei Zusatzdaten fuer Screen-Erstellung: `ExtendedScreenHandlerFactory`
- Alle finalen Upgradeentscheidungen serverseitig validieren

### 3.4 Datenmodell-Strategie
- Tooldaten (Kopf/Griff/Kern) zentral als `ToolState` modellieren
- 1-Kern-Regel hard-enforced
- Kopf-Upgrades datengetrieben (JSON + Validierung)
- Data Components nutzen, NBT nur wo erforderlich

### 3.5 Teststrategie (frueh)
- GameTests fuer:
  - Slotvalidierung
  - Kerninstallation/Kernwechsel
  - Persistenz Save/Load
  - Materialtier-Upgrades
- Reproduzierbare Server/Client-Tests vor jeder grossen Erweiterung

## 4) Spieler-Dokumentation: relevante Punkte fuer spaeteren Release
- Installationspfad klar dokumentieren (Java + Fabric Loader + Mod-Dateien)
- Versionkompatibilitaet klar nennen (Minecraft/Fabric Loader/Fabric API)
- Fehlerhilfe anbieten:
  - falsche Java-Version
  - inkompatibler Loader
  - falsche API-Version
  - Konflikt mit anderen Mods

## 5) Entwickler-Dokumentation: konkrete Start-Checkliste
1. Versionen aus offizieller Fabric-Quelle fuer 1.21.11 pinnen.
2. Java-Version entsprechend den offiziellen Pins setzen.
3. Neues Fabric-Projekt in sauberem lokalen Pfad aufsetzen.
4. Basismodule anlegen: registries, block, screen, networking, data, items.
5. Rechtsklick-Workbench als erster End-to-End-Flow implementieren.
6. ToolState + Kernregel + Kopf-Tierdaten als Kernlogik implementieren.
7. Tests ab frueher Phase einbauen (mindestens Persistenz + Regeln).
8. Porting-Notes vor jedem Versionssprung pruefen.

## 6) Hinweise zu Vollstaendigkeit
Die Fabric-Dokumentation ist gross und entwickelt sich laufend weiter. Dieses Dokument deckt die fuer unser Projekt kritischen Bereiche ab und wird bei Phasewechseln aktualisiert.

## 7) Quellen
- Wiki Start: https://wiki.fabricmc.net/start
- Hauptdoku: https://docs.fabricmc.net/
- Spielerbereich: https://docs.fabricmc.net/players/
- Entwicklerbereich: https://docs.fabricmc.net/develop/
- Fabric Version/Pins: https://fabricmc.net/develop/
- Rechtsklick-Event (API-Quelle): https://github.com/FabricMC/fabric/blob/1.21.10/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/player/UseBlockCallback.java
- Screen-Architektur (Javadocs): https://maven.fabricmc.net/docs/fabric-api-0.110.5+1.21.4/net/fabricmc/fabric/api/screenhandler/v1/ExtendedScreenHandlerFactory.html
- Lokale API-Dateien:
  - `MinecraftMod/fabric-api-0.143.2+26.1/fabric.mod.json`
  - `MinecraftMod/fabric-api-0.143.2+26.1.jar`

