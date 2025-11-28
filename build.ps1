<#
.SYNOPSIS
    Build the mod for one or all Minecraft versions, matching the CI/CD pipeline behavior.

.DESCRIPTION
    This script builds the mod locally using the same process as the GitHub Actions pipeline.
    It can build for a specific Minecraft version or all supported versions.

.PARAMETER MinecraftVersion
    Optional. The Minecraft version to build for (e.g., "1.21.8").
    If not specified, builds for all versions in versions.json.
    Valid values: 1.21.6, 1.21.8, 1.21.9, 1.21.10

.PARAMETER Clean
    Optional. Clean the build directory before building.

.EXAMPLE
    .\build.ps1
    Builds for all Minecraft versions.

.EXAMPLE
    .\build.ps1 -MinecraftVersion "1.21.8"
    Builds only for Minecraft 1.21.8.

.EXAMPLE
    .\build.ps1 -MinecraftVersion "1.21.8" -Clean
    Cleans and builds for Minecraft 1.21.8.
#>

Param(
  [Parameter(Mandatory=$false)]
  [ValidateSet("1.21.6", "1.21.8", "1.21.9", "1.21.10")]
  [string]$MinecraftVersion = "",
  
  [Parameter(Mandatory=$false)]
  [switch]$Clean
)

# Check if versions.json exists
if (-not (Test-Path "versions.json")) {
    Write-Host "Error: versions.json not found!" -ForegroundColor Red
    exit 1
}

# Check if jq is available (for parsing JSON)
$jqAvailable = $false
try {
    $null = Get-Command jq -ErrorAction Stop
    $jqAvailable = $true
} catch {
    Write-Host "Warning: jq not found. Using PowerShell JSON parsing instead." -ForegroundColor Yellow
}

# Function to get Gradle version from versions.json
function Get-GradleVersion {
    param([string]$McVersion)
    
    if ($jqAvailable) {
        # Use proper JSON escaping for jq
        $escapedVersion = $McVersion -replace '"', '\"'
        $gradleVersion = jq -r ".[`"$escapedVersion`"].gradle_version" versions.json
    } else {
        $versions = Get-Content "versions.json" | ConvertFrom-Json
        $gradleVersion = $versions.$McVersion.gradle_version
    }
    
    return $gradleVersion
}

# Function to update Gradle wrapper distribution URL
function Update-GradleWrapper {
    param(
        [string]$McVersion,
        [string]$GradleVersion
    )
    
    $wrapperFile = "gradle\wrapper\gradle-wrapper.properties"
    
    if (-not (Test-Path $wrapperFile)) {
        Write-Host "Error: gradle-wrapper.properties not found!" -ForegroundColor Red
        return $false
    }
    
    $gradleUrl = "https\://services.gradle.org/distributions/gradle-${GradleVersion}-bin.zip"
    $content = Get-Content $wrapperFile -Raw
    $content = $content -replace "distributionUrl=.*", "distributionUrl=$gradleUrl"
    Set-Content -Path $wrapperFile -Value $content -NoNewline
    
    Write-Host "Updated Gradle distribution URL to: https://services.gradle.org/distributions/gradle-${GradleVersion}-bin.zip" -ForegroundColor Cyan
    return $true
}

# Function to build for a specific Minecraft version
function Build-ForVersion {
    param([string]$McVersion)
    
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "Building for Minecraft $McVersion" -ForegroundColor Cyan
    Write-Host "========================================`n" -ForegroundColor Cyan
    
    # Get Gradle version from versions.json
    $gradleVersion = Get-GradleVersion -McVersion $McVersion
    
    if (-not $gradleVersion -or $gradleVersion -eq "null") {
        Write-Host "Error: Could not find gradle_version for Minecraft $McVersion" -ForegroundColor Red
        return $false
    }
    
    # Update Gradle wrapper
    if (-not (Update-GradleWrapper -McVersion $McVersion -GradleVersion $gradleVersion)) {
        return $false
    }
    
    # Clean if requested
    if ($Clean) {
        Write-Host "Cleaning build directory..." -ForegroundColor Yellow
        .\gradlew.bat clean
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: Clean failed!" -ForegroundColor Red
            return $false
        }
    }
    
    # Build
    Write-Host "Building mod for Minecraft $McVersion..." -ForegroundColor Yellow
    .\gradlew.bat build --no-daemon -Pminecraft_version=$McVersion
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n✅ Build successful for Minecraft $McVersion!" -ForegroundColor Green
        Write-Host "Artifacts are in: build\libs\" -ForegroundColor Green
        return $true
    } else {
        Write-Host "`n❌ Build failed for Minecraft $McVersion!" -ForegroundColor Red
        return $false
    }
}

# Main execution
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Mod Build Script" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$versionsToBuild = @()

if ($MinecraftVersion) {
    # Build for specific version
    $versionsToBuild = @($MinecraftVersion)
    Write-Host "Building for Minecraft version: $MinecraftVersion" -ForegroundColor Cyan
} else {
    # Build for all versions
    if ($jqAvailable) {
        $allVersions = jq -r 'keys[]' versions.json
        $versionsToBuild = $allVersions | ForEach-Object { $_.Trim() } | Where-Object { $_ -ne "" }
    } else {
        $versions = Get-Content "versions.json" | ConvertFrom-Json
        $versionsToBuild = $versions.PSObject.Properties.Name
    }
    Write-Host "Building for all Minecraft versions: $($versionsToBuild -join ', ')" -ForegroundColor Cyan
}

$successCount = 0
$failCount = 0

foreach ($version in $versionsToBuild) {
    if (Build-ForVersion -McVersion $version) {
        $successCount++
    } else {
        $failCount++
    }
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Build Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Successful: $successCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor $(if ($failCount -eq 0) { "Green" } else { "Red" })
Write-Host ""

if ($failCount -gt 0) {
    exit 1
}

