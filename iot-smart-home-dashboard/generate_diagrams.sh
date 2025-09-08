#!/bin/bash

echo "==============================================="
echo "PlantUML Diagram Generator for IoT Smart Home Dashboard"
echo "==============================================="

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 8+ to run PlantUML"
    exit 1
fi

# Check if PlantUML jar exists
if [ ! -f "plantuml.jar" ]; then
    echo "PlantUML jar not found. Downloading..."
    echo "Please download plantuml.jar from: https://plantuml.com/download"
    echo "Place it in this directory and run this script again."
    exit 1
fi

echo
echo "Generating PlantUML diagrams..."
echo

# Generate PNG diagrams
echo "[1/5] Generating Architecture diagram..."
java -jar plantuml.jar -tpng architecture.puml
if [ $? -eq 0 ]; then echo "     ✓ architecture.png created"; fi

echo "[2/5] Generating Sequence diagrams..."
java -jar plantuml.jar -tpng sequence-diagrams.puml
if [ $? -eq 0 ]; then echo "     ✓ sequence-diagrams.png created"; fi

echo "[3/5] Generating Component diagram..."
java -jar plantuml.jar -tpng component-diagram.puml
if [ $? -eq 0 ]; then echo "     ✓ component-diagram.png created"; fi

echo "[4/5] Generating Use Case diagram..."
java -jar plantuml.jar -tpng use-case-diagram.puml
if [ $? -eq 0 ]; then echo "     ✓ use-case-diagram.png created"; fi

echo "[5/5] Generating Data Model diagram..."
java -jar plantuml.jar -tpng data-model.puml
if [ $? -eq 0 ]; then echo "     ✓ data-model.png created"; fi

echo
echo "==============================================="
echo "All diagrams generated successfully!"
echo "==============================================="
echo "Generated files:"
ls -1 *.png 2>/dev/null
echo
echo "You can now open the PNG files to view the diagrams."