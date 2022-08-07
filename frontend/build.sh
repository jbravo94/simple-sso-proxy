#!/bin/bash
set -e

docker run --rm -v "$(pwd):/root" node:16-alpine sh -c "cd /root && npm ci && npm run build"
docker build -t simple-sso-proxy-frontend .
