# Refactored Test Steps (Tetra Fabric 1.21.11)

Diese Schritte pruefen, ob der Refactor wirklich laeuft.

## 0) Vorbedingungen
- Minecraft-Version: **1.21.11**
- Fabric Loader: kompatibel zu 1.21.11
- Java: mindestens 21
- Nutze **nur** den Refactor-JAR, nicht die alte Forge-Tetra-JAR.

Wichtig im Mods-Ordner (`%AppData%\.minecraft\mods`):
- `tetra-refactored-6.11.0-fabric-1.21.11.jar` vorhanden
- `tetra-1.20.1-6.11.0.jar` (Forge) **nicht aktiv**

## 1) Build ausfuehren
Im Projekt-Root:
```powershell
.\buildrefactor.ps1
```

Erwartung:
- `BUILD SUCCESSFUL`
- Ausgabe zeigt Kopie nach `%AppData%\.minecraft\mods\tetra-refactored-6.11.0-fabric-1.21.11.jar`

## 2) Loader-Test (Start ohne Welt)
1. Minecraft mit Fabric-Profil starten.
2. Im Mod-Menue pruefen:
   - Mod-ID/Name `tetra` bzw. `Tetra Refactored` sichtbar.
3. `latest.log` pruefen (`%AppData%\.minecraft\logs\latest.log`):
   - kein Crash
   - kein Duplicate-Mod-ID-Fehler
   - keine Forge-only Fehler von alter Tetra

## 3) Schnelltest mit Commands (empfohlen)
In eine Testwelt (Creative) gehen und folgende Items geben:
```mcfunction
/give @s tetra:hammer
/give @s tetra:basic_workbench
/give @s tetra:forged_workbench
/give @s tetra:upgrade_workbench
/give @s tetra:modular_sword
/give @s tetra:modular_axe
/give @s tetra:modular_pickaxe
/give @s tetra:modular_spear
/give @s tetra:modular_mace
/give @s tetra:core_placeholder 16
/give @s minecraft:copper_ingot 16
/give @s minecraft:iron_ingot 16
/give @s minecraft:gold_ingot 16
/give @s minecraft:diamond 16
/give @s minecraft:netherite_ingot 16
```

Erwartung:
- Alle `tetra:*` Items existieren (kein "Unknown item" Fehler)

## 4) Block + GUI-Test
1. Eine normale Crafting Table platzieren (`minecraft:crafting_table`).
2. `tetra:hammer` in die Main-Hand nehmen und Rechtsklick auf die Crafting Table.

Erwartung:
- Die Crafting Table wird zu `tetra:basic_workbench`.
- Kein Crash/kein Fehler-Spam.

3. `tetra:basic_workbench` oder `tetra:forged_workbench` platzieren (optional auch `tetra:upgrade_workbench` als Legacy-ID).
4. Rechtsklick auf den Block.

Erwartung:
- GUI oeffnet ohne Crash.
- Hintergrund/Overlay wird gezeichnet.
- Keine Fehler-Spam im `latest.log`.

## 5) Kern-Installations-Test
1. In den Tool-Slot ein modulares Tool legen (z. B. `tetra:modular_sword`).
2. In Material-Slot `tetra:core_placeholder` legen.
3. Ergebnis-Slot entnehmen.

Erwartung:
- Neuer Tool-Stack wird erzeugt.
- Tool hat gesetzten Core.

Pruefen per Datenabfrage (Item in Main Hand halten):
```mcfunction
data get entity @s SelectedItem
```

Im Output soll unter den Custom-Daten `tetra_tool_state` erscheinen, inkl.:
- `core_id: "tetra:core_placeholder"`
- `core_upgrade_level >= 1`

## 6) Head-Upgrade-Test (gueltiger Pfad)
Der Pfad ist strikt sequentiell:
- `stone -> copper -> iron -> gold -> diamond -> netherite`

Test:
1. Tool mit `stone`-Startzustand in den Tool-Slot.
2. `copper_ingot` einlegen, Ergebnis nehmen.
3. Danach mit dem neuen Tool `iron_ingot`, dann `gold_ingot`, dann `diamond`, dann `netherite_ingot`.

Erwartung:
- Jeder naechste Schritt funktioniert.
- `head_upgrade_level` steigt pro erfolgreichem Schritt um 1.

## 7) Head-Upgrade-Test (ungueltiger Pfad)
1. Frisches Tool (`stone`) in den Tool-Slot.
2. Direkt `iron_ingot` statt `copper_ingot` einlegen.

Erwartung:
- Kein gueltiges Ergebnis im Confirm-/Output-Slot.
- Kein Crash.

## 8) Persistenz-Test
1. Ein bereits modifiziertes Tool in Inventar behalten.
2. Welt verlassen und neu laden.
3. Item erneut mit `data get entity @s SelectedItem` pruefen.

Erwartung:
- `tetra_tool_state` bleibt erhalten (core/head/levels unveraendert).

## 9) Regression/Robustheit
- Shift-Click im GUI mehrfach testen (Tool/Material rein und raus).
- Mehrfaches Oeffnen/Schliessen der Werkbank.
- Block abbauen und neu setzen.

Erwartung:
- Kein Dupe, kein Freeze, kein Client- oder Server-Crash.

## 10) Wenn etwas fehlschlaegt
Bitte mir direkt schicken:
1. Genaue Schritt-Nummer aus diesem Dokument
2. `latest.log` Ausschnitt mit Stacktrace
3. Falls GUI-bezogen: Screenshot
4. `data get entity @s SelectedItem` Ausgabe des betroffenen Items

---

## Aktueller Scope-Hinweis
Dieser Refactor ist ein lauffaehiger Fabric-Startpunkt mit Build-success und Kernfunktionen (Block, GUI, modulare Tools, ToolState). Es ist **noch kein vollstaendiger 1:1-Feature-Port** der kompletten Forge-Tetra-Originallogik.
