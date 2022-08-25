Write-Host "[1] gym_acc"
Write-Host "[2] gym_acc_vue"

$selectProject = Read-Host "Please enter project number:"

Write-Host "Please enter the version number:"

$majorVersion = Read-Host "Major Version:"
$minorVersion = Read-Host "Minor Version:"
$patchVersion = Read-Host "Patch Version:"

function Build-Java {
    .\mvnw.cmd package "-Dmaven.test.skip=true" -U -e -X -B
}

function Build-Npm {
    npm run build:prod
}

function Build-Docker($tag) {
    docker build -t "${tag}:${majorVersion}.${minorVersion}.${patchVersion}" .
}

function Push-Docker($tag) {
    docker push "${tag}:${majorVersion}.${minorVersion}.${patchVersion}"
}

function Notify-To-WeChat($tag) {
    $params = @{
        "msgtype" = "markdown";
        "markdown" = @{
            "content" = "new version image published
            >tag: ${tag}
            >version: ${majorVersion}.${minorVersion}.${patchVersion}";
        };
    } | ConvertTo-Json

    Write-Host $params

    Invoke-WebRequest -Uri "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=11dd1e5d-7935-421f-8681-de58dadb7032" -Method Post -Body $params  -ContentType "application/json"
}

$gymAccTag = "ascr.asun.cloud/acc/gym_acc"
$gymAccVueTag = "ascr.asun.cloud/acc/gym_acc_vue"

switch ($selectProject) {
    1 { 
        Build-Java
        Build-Docker($gymAccTag)
        Push-Docker($gymAccTag)
        Notify-To-WeChat($gymAccTag)
     }
     2 {
         Build-Npm
         Build-Docker($gymAccVueTag)
         Push-Docker($gymAccVueTag)
         Notify-To-WeChat($gymAccVueTag)
      }
    Defult {
        Write-Host "💥"
    }
}

Write-Output "$majorVersion.$minorVersion.$patchVersion"
# Build-Java
# Build-Docker