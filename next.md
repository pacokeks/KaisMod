So testest du jetzt genau

  1. Minecraft mit Fabric starten.
  2. In eine Testwelt gehen (/gamemode creative ist ok).
  3. Upgrade-Werkbank geben und platzieren:
      - /give @p kaismod:upgrade_workbench
  4. Ein modulares Tool geben:
      - /give @p kaismod:modular_sword
  5. Kern + Upgrade-Material geben:
      - /give @p kaismod:core_placeholder 4
      - /give @p minecraft:diamond 8
  6. Rechtsklick auf die Upgrade-Werkbank:
      - GUI muss aufgehen.
  7. Tool in Slot 1, Core in Slot 2, Diamond in Slot 3 legen:
      - In Slot 4 (Confirm) muss ein Ergebnis-Item erscheinen (Preview).
  8. Confirm-Ergebnis aus Slot 4 nehmen:
      - Tool aus Slot 1 wird verbraucht/ersetzt.
      - Core und Diamond werden jeweils um 1 reduziert.
  9. Negative Tests:
      - Falsches Item in Tool-Slot (z. B. Dirt) -> darf nicht rein.
      - Falsches Item in Core-Slot -> darf nicht rein.
      - Falsches Item in Upgrade-Slot -> darf nicht rein.
  10. Optional Logcheck:

  - KaisMod/run/logs/latest.log auf Fehler beim Öffnen/Nutzen der Werkbank prüfen.

  Wenn du mir das Ergebnis gibst, mache ich danach direkt Phase 4 (Core-System-Regeln + Wechselkosten +      
  serverseitige Validierung).