#!/bin/bash
set -e

docker run --rm -v "$(pwd):/root" openjdk:17-jdk-alpine sh -c "cd /root && ./mvnw clean package -Dmaven.test.skip=true"
docker build -t simple-sso-proxy-backend .