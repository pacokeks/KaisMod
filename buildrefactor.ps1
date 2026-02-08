Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$progressActivity = "Tetra Refactor Build"
$copyBuildToMinecraftMods = $true
$appDataDir = [Environment]::GetFolderPath("ApplicationData")
if ([string]::IsNullOrWhiteSpace($appDataDir)) {
    throw "Konnte den aktuellen AppData-Pfad nicht ermitteln."
}
$minecraftModsDir = Join-Path (Join-Path $appDataDir ".minecraft") "mods"

function Update-BuildProgress {
    param(
        [int]$Percent,
        [string]$Status
    )
    Write-Progress -Activity $progressActivity -Status "$Status ($Percent%)" -PercentComplete $Percent
    Write-Host "[$Percent%] $Status"
}

function Test-JavaHome {
    param([string]$PathValue)
    if ([string]::IsNullOrWhiteSpace($PathValue)) {
        return $false
    }
    return (Test-Path (Join-Path $PathValue "bin\java.exe"))
}

function Get-JavaHomeCandidate {
    if (Test-JavaHome $env:JAVA_HOME) {
        return $env:JAVA_HOME
    }

    try {
        $jdkRoot = Get-ItemProperty "HKLM:\SOFTWARE\JavaSoft\JDK" -ErrorAction Stop
        $currentVersion = $jdkRoot.CurrentVersion
        if (-not [string]::IsNullOrWhiteSpace($currentVersion)) {
            $jdkVersionKey = "HKLM:\SOFTWARE\JavaSoft\JDK\$currentVersion"
            $jdkVersionInfo = Get-ItemProperty $jdkVersionKey -ErrorAction Stop
            if (Test-JavaHome $jdkVersionInfo.JavaHome) {
                return $jdkVersionInfo.JavaHome
            }
        }
    } catch {
    }

    $candidates = @(
        "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot",
        "C:\Program Files\Eclipse Adoptium\jdk-21*",
        "C:\Program Files\Java\jdk-21*",
        "C:\Program Files\Microsoft\jdk-21*"
    )

    foreach ($pattern in $candidates) {
        $dirs = Get-ChildItem -Path $pattern -Directory -ErrorAction SilentlyContinue | Sort-Object Name -Descending
        foreach ($dir in $dirs) {
            if (Test-JavaHome $dir.FullName) {
                return $dir.FullName
            }
        }
    }

    return $null
}

$projectRoot = Join-Path $PSScriptRoot "refactored\tetra-fabric"
$gradleWrapper = Join-Path $projectRoot "gradlew.bat"

Update-BuildProgress -Percent 5 -Status "Initialisiere Buildskript"

if (-not (Test-Path $gradleWrapper)) {
    throw "Gradle Wrapper nicht gefunden: $gradleWrapper"
}

Update-BuildProgress -Percent 15 -Status "Pruefe Java-Umgebung"

$javaHome = Get-JavaHomeCandidate
if (-not $javaHome) {
    throw "Kein gueltiges JDK gefunden. Bitte einmalig JAVA_HOME und PATH (%JAVA_HOME%\bin) setzen."
}

$javaInPath = Get-Command java -ErrorAction SilentlyContinue
$javaHomeInEnv = Test-JavaHome $env:JAVA_HOME

if (-not $javaInPath -and -not $javaHomeInEnv) {
    Write-Warning "JAVA_HOME/PATH fehlen in dieser Session. Nutze Fallback mit erkanntem JDK."
    $env:JAVA_HOME = $javaHome.TrimEnd('\')
    $javaBin = Join-Path $env:JAVA_HOME "bin"
    if (($env:Path -split ";") -notcontains $javaBin) {
        $env:Path = "$javaBin;$env:Path"
    }
    $javaInPath = Get-Command java -ErrorAction SilentlyContinue
    $javaHomeInEnv = Test-JavaHome $env:JAVA_HOME
}

if (-not $javaInPath -and -not $javaHomeInEnv) {
    throw "Weder JAVA_HOME noch java im PATH verfuegbar. Bitte JAVA_HOME und %JAVA_HOME%\\bin setzen."
}

if ($javaHomeInEnv) {
    Write-Host "JAVA_HOME: $env:JAVA_HOME"
    & (Join-Path $env:JAVA_HOME "bin\java.exe") -version
} else {
    Write-Host "JAVA_HOME: (nicht gesetzt)"
    & java -version
}

Update-BuildProgress -Percent 30 -Status "Bereinige alte Build-Artefakte"

$libsDir = Join-Path $projectRoot "build\libs"
if (Test-Path $libsDir) {
    Remove-Item -Path (Join-Path $libsDir "*.jar") -Force -ErrorAction SilentlyContinue
}

Update-BuildProgress -Percent 45 -Status "Starte Gradle Build (clean build)"

Push-Location $projectRoot
try {
    & $gradleWrapper clean build
    if ($LASTEXITCODE -ne 0) {
        throw "Gradle Build fehlgeschlagen."
    }
} finally {
    Pop-Location
}

Update-BuildProgress -Percent 85 -Status "Pruefe erzeugte JAR-Dateien"

$builtJars = Get-ChildItem -Path $libsDir -Filter "*.jar" -File -ErrorAction SilentlyContinue |
    Sort-Object LastWriteTime -Descending

if (-not $builtJars) {
    throw "Build war erfolgreich, aber keine JAR in $libsDir gefunden."
}

$mainJar = $builtJars | Where-Object { $_.Name -notmatch "(sources|dev|javadoc)" } | Select-Object -First 1
if (-not $mainJar) {
    $mainJar = $builtJars[0]
}

if ($copyBuildToMinecraftMods) {
    Update-BuildProgress -Percent 95 -Status "Kopiere Build nach .minecraft\mods"
    if (-not (Test-Path $minecraftModsDir)) {
        New-Item -Path $minecraftModsDir -ItemType Directory -Force | Out-Null
    }

    $targetJarPath = Join-Path $minecraftModsDir $mainJar.Name
    Copy-Item -Path $mainJar.FullName -Destination $targetJarPath -Force
    Write-Host "In Mods-Ordner kopiert: $targetJarPath"
}

Update-BuildProgress -Percent 100 -Status "Build abgeschlossen"
Write-Host "Build erfolgreich."
Write-Host "Aktuelle Build-Datei: $($mainJar.FullName)"
Write-Progress -Activity $progressActivity -Completed
