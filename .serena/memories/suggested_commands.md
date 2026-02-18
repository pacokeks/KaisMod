# Empfohlene Kommandos (Windows)
- Standard-Build (verbindlich laut AGENTS): `C:\Users\pasca\Desktop\Kai\MinecraftMod\build.ps1`
- Direkter Gradle-Build im Projekt: `KaisMod\gradlew.bat clean build`
- Schnelle Dateisuche: `rg --files`
- Textsuche: `rg "<pattern>" KaisMod/src`

# Abschluss-Checks nach Aenderungen
- Build ueber `build.ps1` erfolgreich.
- Falls relevant: Nutzer fuehrt Ingame-Test manuell aus (Agent startet Minecraft nicht).
- Bei UI/Assets: pruefen, dass Texturpfade und Groessen konsistent sind.