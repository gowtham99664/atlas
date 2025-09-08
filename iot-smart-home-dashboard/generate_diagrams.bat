@echo off
echo ===============================================
echo PlantUML Diagram Generator for IoT Smart Home Dashboard
echo ===============================================

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 8+ to run PlantUML
    pause
    exit /b 1
)

REM Check if PlantUML jar exists
if not exist "plantuml.jar" (
    echo PlantUML jar not found. Downloading...
    echo Please download plantuml.jar from: https://plantuml.com/download
    echo Place it in this directory and run this script again.
    pause
    exit /b 1
)

echo.
echo Generating PlantUML diagrams...
echo.

REM Generate PNG diagrams
echo [1/5] Generating Architecture diagram...
java -jar plantuml.jar -tpng architecture.puml
if %errorlevel% equ 0 echo     ✓ architecture.png created

echo [2/5] Generating Sequence diagrams...
java -jar plantuml.jar -tpng sequence-diagrams.puml
if %errorlevel% equ 0 echo     ✓ sequence-diagrams.png created

echo [3/5] Generating Component diagram...
java -jar plantuml.jar -tpng component-diagram.puml
if %errorlevel% equ 0 echo     ✓ component-diagram.png created

echo [4/5] Generating Use Case diagram...
java -jar plantuml.jar -tpng use-case-diagram.puml
if %errorlevel% equ 0 echo     ✓ use-case-diagram.png created

echo [5/5] Generating Data Model diagram...
java -jar plantuml.jar -tpng data-model.puml
if %errorlevel% equ 0 echo     ✓ data-model.png created

echo.
echo ===============================================
echo All diagrams generated successfully!
echo ===============================================
echo Generated files:
dir /b *.png 2>nul
echo.
echo You can now open the PNG files to view the diagrams.
pause