Param(
  [Parameter(Mandatory=$true)]
  [string]$Version
)

# Ensure we're on main branch
git checkout master
git pull

# Check if tag already exists and clean it up if needed
$tagName = "v$Version"
$tagExists = git tag -l $tagName

if ($tagExists) {
    Write-Host "Tag $tagName already exists. Cleaning up..." -ForegroundColor Yellow
    
    # Delete local tag
    git tag -d $tagName
    
    # Delete remote tag
    git push origin ":refs/tags/$tagName" 2>&1 | Out-Null
    
    Write-Host "✅ Removed existing tag $tagName" -ForegroundColor Green
}

# Create and push tag (this will trigger release workflow)
Write-Host "Creating release tag: $tagName" -ForegroundColor Cyan
git tag -a $tagName -m "Release $tagName"
git push origin $tagName

Write-Host "✅ Tag $tagName pushed successfully" -ForegroundColor Green
Write-Host "The release workflow will automatically create the GitHub Release" -ForegroundColor Yellow
