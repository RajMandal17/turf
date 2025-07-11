#!/bin/bash

# Turf Management System - Compile Script

echo "Compiling Turf Management System..."

# Create output directory
mkdir -p out

# Compile all Java files
javac -d out -cp ".:lib/*" src/com/turf/*.java src/com/turf/model/*.java src/com/turf/dao/*.java src/com/turf/service/*.java src/com/turf/util/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "To run the application, use: ./run.sh"
else
    echo "Compilation failed!"
    exit 1
fi
