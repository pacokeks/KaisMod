# complete.md - KaisMod Umsetzungsleitfaden (Fabric 1.21.11)

Stand: 2026-02-08
Arbeitsmodus: Schrittweise, jeder Schritt endet mit Build + Ingame-Test durch den Nutzer.

## Aktueller Fokus (2026-02-08, UI/Icon-Hotfix)

Ziel dieses Schritts:
- Upgrade-Workbench-Inventar-Icon wie Tetra-Blockmodell darstellen.
- Workbench-UI nicht mehr als komplettes `workbench.png`-Sheet rendern (keine schwarzen Vollflaechen).
- Zusatzslots nur sichtbar machen, wenn sie im aktuellen Zustand gebraucht werden.

Umsetzung in diesem Schritt:
- `models/item/upgrade_workbench.json` auf Block-Parent umgestellt (`kaismod:block/upgrade_workbench`).
- `UpgradeWorkbenchScreen` auf Tetra-Basisabmessungen (`320x240`) + gezielten Teilbereich aus `workbench.png` umgestellt.
- `UpgradeWorkbenchScreenHandler` Material-/Confirm-Slots auf zustandsabhaengige Sichtbarkeit umgestellt.

Dein Test fuer diesen Schritt:
1. Minecraft mit neuem Build starten.
2. Inventar pruefen:
   - Upgrade-Workbench-Item zeigt ein 3D-Blockmodell statt schwarzem/flachem Platzhalter.
3. Block platzieren:
   - Weltmodell bleibt sichtbar (Unterteil vorhanden).
4. Rechtsklick auf Upgrade-Workbench:
   - Keine grossen schwarzen GUI-Rechtecke.
   - Kein altes Container-Layout.
5. Tool in den linken Slot legen:
   - Zusatzslots erscheinen nur zustandsabhaengig (nicht sofort alle dauerhaft).

## 0) Strategische Entscheidung (verbindlich)

### Warum wir Tetra nicht direkt "anpassen"
- Technisch: `MinecraftMods/tetra-1.20.1-6.11.0/META-INF/mods.toml` ist Forge-basiert (`modLoader="javafml"`, Forge-Abhaengigkeiten).
- Version: Ziel von Tetra ist 1.20.1, unser Ziel ist Fabric 1.21.11.
- Lizenz: `license="ARR"` (All Rights Reserved). Direkte Code-Uebernahme/Weitergabe ist rechtlich problematisch.

### Was wir stattdessen machen
- Tetra als Referenz fuer UX, Datenstruktur und Verhalten nutzen.
- Alle Systeme in `KaisMod` Fabric-nativ neu implementieren.
- Bereits bestaetigt: GUI-Texturen (`workbench.png`, `forged-container.png`, `player-inventory.png`) sind byte-identisch uebernommen und korrekt eingebunden.

## 1) Projektziel (fixiert)
- Modulare Tools/Waffen: Schwert, Axt, Pickaxe, Speer, Mace.
- Toolstruktur: Kopf, Griff, exakt ein Kern.
- Upgrade-Werkbank als zentrale Station mit Vorschau + Bestaetigung.
- Serverautoritativ: Entscheidungen/Validierung serverseitig, Client ist Darstellung.

## 2) Arbeitsregeln (Do/Don’t)
- Build immer mit `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1`.
- Agent startet Minecraft niemals selbst.
- Nach jedem Implementierungsschritt: erst Build, dann dein Ingame-Test, dann erst naechster Schritt.
- Kein 1:1 Port von Tetra-Klassen/Forge-Code.

## 3) Status heute (Ist-Zustand)
Bereits vorhanden in `KaisMod`:
- Mod laedt (Fabric-Setup vorhanden).
- Upgrade-Workbench Block + ScreenHandler + Client-Screen.
- Tetra-GUI-Layout-Basis umgesetzt (Slotabstand 17, Positionen passend).
- Modulare Item-Basisklassen + `ToolState`-Persistenz.
- Reentrancy-Schutz in `UpgradeWorkbenchScreenHandler` vorhanden.

Noch offen fuer "wie original Tetra-Gefuehl":
- Datengetriebenes Core-/Upgrade-Register.
- Echte Validierungsregeln und Kostenmodell.
- Werkzeugtyp-spezifische Effekte und Stat-Recalc.
- Vollstaendige UX-Fehlertexte/Vorschau-Logik.

## 4) Schrittplan (verbindlich)

### Schritt 1 - Core- und Upgrade-Domain stabilisieren
Implementierung:
- `ToolCore` von Placeholder zu echter Definition erweitern (id, kompatible Tooltypen, Kosten, Tooltip, Effektprofil).
- Core-Registry (Start mit wenigen Cores) einfuehren.
- Head-Material-Regeln als zentrale Domain statt hardcoded if/else im ScreenHandler.
- `ToolState`-Operationen zentralisieren (installCore, switchCore, upgradeHead) inkl. Validierung.

Akzeptanz:
- Kein ungueltiger Toolzustand mehr durch direkte Slot-Interaktion.
- Fehler werden als maschinenlesbare Result-Typen geliefert (fuer UI-Feedback).

Dein Test nach Schritt 1:
1. `build.ps1` ausfuehren.
2. Ingame: Upgrade-Werkbank oeffnen.
3. Tool + Core + Material in verschiedenen Kombinationen testen.
4. Erwartung: Es gibt keine stillen Fehlschlaege und keine Dupe/State-Korruption.

### Schritt 2 - ScreenHandler auf regelgetriebene Transaktionen umbauen
Implementierung:
- Handler verwendet nur noch zentrale Domain-Services fuer Vorschau/Apply.
- Expliziter Confirm-Flow fuer Kernwechsel (teurer als Erstinstallation).
- Input-Verbrauch erst bei erfolgreicher Apply-Transaktion.
- Saubere Trennung: Preview-Result vs. Apply-Result.

Akzeptanz:
- Kein Materialverbrauch bei ungueltigem Zustand.
- Kernwechsel braucht bestaetigten Flow.

Dein Test nach Schritt 2:
1. `build.ps1` ausfuehren.
2. Ingame: Erstinstallation Kern durchfuehren.
3. Danach Kernwechsel probieren (ohne und mit gueltiger Bestaetigung/Kosten).
4. Erwartung: Regeln werden strikt erzwungen, kein Bypass per Shift-Klick.

### Schritt 3 - Datengetriebene Inhalte (JSON) einfuehren
Implementierung:
- JSON-Loader fuer Kern- und Materialdefinitionen.
- Fallback auf interne Defaults, falls JSON fehlt/fehlerhaft ist.
- Struktur orientiert an Tetra-Idee (data-driven), aber vereinfacht fuer KaisMod.

Akzeptanz:
- Neue Core-/Materialeintraege ohne Java-Refactor moeglich.
- Fehlerhafte JSON crasht nicht hart, sondern faellt kontrolliert zurueck.

Dein Test nach Schritt 3:
1. `build.ps1` ausfuehren.
2. Ein JSON-Wert absichtlich aendern (z. B. Kosten), neu starten.
3. Ingame pruefen, ob geaendertes Verhalten sichtbar ist.
4. Erwartung: Daten greifen, Mod bleibt stabil.

### Schritt 4 - Tool-spezifische Basismechaniken aktivieren
Implementierung:
- Jede Toolklasse bekommt differenzierte Basiswerte.
- Core-Hooks fuer Tooltypen (Pickaxe, Sword, Axe, Spear, Mace) anbinden.
- Serverseitige Stat-Recalculation aus `ToolState` + Material + Core.

Akzeptanz:
- Die 5 Tooltypen fuehlen sich mechanisch unterscheidbar an.
- Stats sind reproduzierbar aus dem Zustand berechenbar.

Dein Test nach Schritt 4:
1. `build.ps1` ausfuehren.
2. Je Tooltyp ein Exemplar bauen und je einen Kern anwenden.
3. Ingame: Mining/Kampf/Funktionsverhalten vergleichen.
4. Erwartung: klare Unterschiede pro Tooltyp, keine Null-Effekte.

### Schritt 5 - UX auf Tetra-Niveau bringen (Fabric-nativ)
Implementierung:
- Praezise Fehlertexte (Material fehlt, Kosten fehlen, inkompatibler Kern, Progression gesperrt).
- Kosten-/Effektvorschau sichtbar vor Confirm.
- Optional: einfache Stat-Vorschau im Screen.

Akzeptanz:
- Nutzer versteht immer, warum etwas geht/nicht geht.
- Keine Blackbox-Interaktionen.

Dein Test nach Schritt 5:
1. `build.ps1` ausfuehren.
2. Alle Fehlerfaelle bewusst provozieren.
3. Erwartung: Jede Ablehnung hat klare, korrekte Rueckmeldung.

### Schritt 6 - Balancing + Gating + Stabilitaet
Implementierung:
- Endgame-Kerne/Upgrades progression-gated.
- Kostenkurven und Limits feinjustieren.
- Automatisierte Tests fuer Kernregeln, Persistenz und Slot-Validierung.

Akzeptanz:
- Kein offensichtlicher Exploit/OP-Pfad.
- Keine Regressions in Kernlogik.

Dein Test nach Schritt 6:
1. `build.ps1` ausfuehren.
2. Survival-Testlauf mit fruehem bis spaetem Progressionsstand.
3. Erwartung: Gating wirkt, aber fuehlt sich fair an.

## 5) Reihenfolge fuer unsere naechsten Aktionen
1. Jetzt direkt mit Schritt 1 starten (Domain + Registry + Validierung).
2. Danach baue ich und gebe dir exakte Test-Checkliste fuer genau diesen Schritt.
3. Erst nach deinem Testergebnis gehen wir zu Schritt 2.

## 6) Fokusregel gegen Kontextverlust
Vor jeder Aenderung pruefen wir:
- Dient die Aenderung direkt dem aktuellen Schritt?
- Ist sie Fabric-1.21.11-kompatibel?
- Ist sie serverautoritativ und testbar?
Wenn nein: in Backlog, nicht jetzt implementieren.
