param(
    [string]$MinecraftVersion = "1.21.8",
    [string]$Loader = "fabric",
    [string]$OutputDir = ".\\run\\mods",
    [switch]$IncludeOptional,
    [switch]$AllowVersionFallback
)

$ErrorActionPreference = "Stop"

$essentialProjects = @(
    "fabric-api",
    "sodium",
    "lithium",
    "ferrite-core",
    "entityculling",
    "moreculling",
    "immediatelyfast",
    "modernfix",
    "c2me-fabric",
    "memoryleakfix",
    "debugify"
)

$optionalProjects = @(
    "sodium-extra",
    "reeses-sodium-options",
    "iris",
    "zoomify",
    "cull-leaves",
    "lambdynamiclights",
    "betterf3",
    "continuity"
)

$projects = @($essentialProjects)
if ($IncludeOptional) {
    $projects += $optionalProjects
}

New-Item -ItemType Directory -Force $OutputDir | Out-Null

$loaderJson = [Uri]::EscapeDataString("[`"$Loader`"]")
$versionJson = [Uri]::EscapeDataString("[`"$MinecraftVersion`"]")

function Get-LatestProjectVersion {
    param([string]$ProjectSlug)

    $uri = "https://api.modrinth.com/v2/project/$ProjectSlug/version?loaders=$loaderJson&game_versions=$versionJson"
    $versions = Invoke-RestMethod -Uri $uri -Headers @{"User-Agent" = "pvpfps-client-installer/1.0"}

    if (-not $versions -or $versions.Count -eq 0) {
        if (-not $AllowVersionFallback) {
            return $null
        }

        $fallbackUri = "https://api.modrinth.com/v2/project/$ProjectSlug/version?loaders=$loaderJson"
        $fallbackVersions = Invoke-RestMethod -Uri $fallbackUri -Headers @{"User-Agent" = "pvpfps-client-installer/1.0"}
        if (-not $fallbackVersions -or $fallbackVersions.Count -eq 0) {
            return $null
        }

        $mcMinor = ($MinecraftVersion -split "\\.")[0..1] -join "."
        $exactFallback = $fallbackVersions | Where-Object { $_.game_versions -contains $MinecraftVersion } | Select-Object -First 1
        if ($null -ne $exactFallback) {
            return $exactFallback
        }

        $nearestSameMinor = $fallbackVersions | Where-Object {
            ($_.game_versions | Where-Object { $_ -like "$mcMinor.*" }).Count -gt 0
        } | Select-Object -First 1

        if ($null -ne $nearestSameMinor) {
            Write-Warning ("Fallback usado para {0}: versao mais proxima de {1}." -f $ProjectSlug, $MinecraftVersion)
            return $nearestSameMinor
        }

        Write-Warning ("Fallback generico usado para {0}: sem match direto para {1}." -f $ProjectSlug, $MinecraftVersion)
        return $fallbackVersions[0]
    }

    return $versions[0]
}

foreach ($project in $projects) {
    try {
        Write-Host "[INFO] Buscando $project ..."
        $version = Get-LatestProjectVersion -ProjectSlug $project
        if ($null -eq $version) {
            Write-Warning "Sem versao compativel para $project em $MinecraftVersion/$Loader"
            continue
        }

        $file = $version.files | Where-Object { $_.primary -eq $true } | Select-Object -First 1
        if ($null -eq $file) {
            $file = $version.files | Select-Object -First 1
        }

        if ($null -eq $file) {
            Write-Warning "Sem arquivo baixavel em $project ($($version.version_number))"
            continue
        }

        $destination = Join-Path $OutputDir $file.filename
        Write-Host "[DOWNLOAD] $project -> $($file.filename)"
        Invoke-WebRequest -Uri $file.url -OutFile $destination -Headers @{"User-Agent" = "pvpfps-client-installer/1.0"}
    }
    catch {
        Write-Warning ("Falha em {0}: {1}" -f $project, $_.Exception.Message)
    }
}

Write-Host "Concluido. Mods salvos em: $OutputDir"
