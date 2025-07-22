#!/bin/bash

# Script to compile JDBC Tutorial examples

# Make sure we're in the project root directory
cd "$(dirname "$0")"

# Create build directory if it doesn't exist
mkdir -p build

# Compile the code
echo "Compiling Java files..."
javac -d build -cp "lib/*" src/examples/*.java src/advanced/*.java

if [ $? -eq 0 ]; then
  echo "Compilation successful!"
else
  echo "Compilation failed!"
  exit 1
fi

echo "Done!"
