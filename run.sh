#!/bin/bash

# Turf Management System - Run Script

echo "Starting Turf Management System..."

# Check if compiled classes exist
if [ ! -d "out" ]; then
    echo "Compiled classes not found. Please run ./compile.sh first"
    exit 1
fi

# Run the application
java -cp "out:lib/*" com.turf.App
