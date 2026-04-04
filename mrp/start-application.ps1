Write-Host "Checking environment..."

# Check if Java is installed
Write-Host "Checking Java..."
try {
    $javaVersion = java -version 2>&1
    Write-Host "Java version: $javaVersion"
} catch {
    Write-Host "Java is not installed or not in PATH"
    exit 1
}

# Check if Maven is installed
Write-Host "Checking Maven..."
try {
    $mavenVersion = mvn -version 2>&1
    Write-Host "Maven version: $mavenVersion"
} catch {
    Write-Host "Maven is not installed or not in PATH"
    exit 1
}

# Check if MySQL is running
Write-Host "Checking MySQL..."
try {
    $mysqlOutput = mysql -h localhost -P 3306 -u root -proot -e "SHOW DATABASES;" 2>&1
    Write-Host "MySQL is running"
} catch {
    Write-Host "MySQL is not running or not accessible"
    exit 1
}

# Create database if it doesn't exist
Write-Host "Creating database if it doesn't exist..."
try {
    mysql -h localhost -P 3306 -u root -proot -e "CREATE DATABASE IF NOT EXISTS sparkit_mrp;" 2>&1
    Write-Host "Database created or already exists"
} catch {
    Write-Host "Failed to create database"
    exit 1
}

# Create Maven local repository if it doesn't exist
Write-Host "Creating Maven local repository if it doesn't exist..."
$mavenRepo = "C:\Users\qiyu\.m2\repository"
if (-not (Test-Path $mavenRepo)) {
    New-Item -ItemType Directory -Path $mavenRepo -Force
    Write-Host "Maven local repository created"
} else {
    Write-Host "Maven local repository already exists"
}

# Run the application
Write-Host "Running application..."
try {
    mvn spring-boot:run
} catch {
    Write-Host "Failed to run application: $_"
    exit 1
}
