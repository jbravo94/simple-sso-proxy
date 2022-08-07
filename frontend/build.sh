#!/bin/bash
set -e

npm ci
npm run build
docker build -t simple-sso-proxy-frontend .
