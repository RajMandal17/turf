#!/bin/bash

# Script to run JDBC Tutorial examples

# Make sure we're in the project root directory
cd "$(dirname "$0")"

# Check if the example name was provided
if [ -z "$1" ]; then
  echo "Usage: $0 <example_class_name>"
  echo "Example: $0 BasicJdbcExample"
  echo "Available examples:"
  echo "  Basic Examples:"
  ls -1 src/examples | grep -v "DatabaseUtil.java" | sed 's/\.java$//'
  echo "  Advanced Examples:"
  ls -1 src/advanced | sed 's/\.java$//'
  exit 1
fi

EXAMPLE=$1

# Check if the example exists
if [ -f "src/examples/$EXAMPLE.java" ]; then
  PACKAGE="examples"
elif [ -f "src/advanced/$EXAMPLE.java" ]; then
  PACKAGE="advanced"
else
  echo "Error: Example $EXAMPLE not found!"
  exit 1
fi

# Make sure the code is compiled
if [ ! -d "build/$PACKAGE" ]; then
  echo "You need to compile the code first. Run ./compile.sh"
  exit 1
fi

# Run the example
echo "Running $PACKAGE.$EXAMPLE..."
java -cp "build:lib/*" $PACKAGE.$EXAMPLE

echo "Done!"
