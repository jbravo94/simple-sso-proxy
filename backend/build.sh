#!/bin/bash
set -e

./mvnw clean package
docker build -t simple-sso-proxy-backend .