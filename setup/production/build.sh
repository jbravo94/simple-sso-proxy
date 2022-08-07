#!/bin/bash
set -e

echo "Building Backend..."
(cd ../../backend && ./build.sh)

echo "Building Frontend..."
(cd ../../frontend && ./build.sh)

echo "Success!"
