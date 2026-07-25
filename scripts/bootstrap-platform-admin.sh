#!/usr/bin/env bash
# Run once per environment, after the backend is up, to create the PLATFORM_ADMIN account.
# Idempotent: safe to run on every deploy — the endpoint no-ops if the account already exists.
#
# Required env vars (must already be set on the server the backend runs on,
# e.g. exported in the systemd unit / docker-compose / CI secrets — NOT committed to git):
#   PLATFORM_ADMIN_EMAIL
#   PLATFORM_ADMIN_PASSWORD
#   PLATFORM_ADMIN_BOOTSTRAP_TOKEN   must match the same value the backend was started with
#
# Optional:
#   API_BASE   Base URL of the backend (default: http://localhost:8081)
#
# Usage:
#   PLATFORM_ADMIN_EMAIL=platform@yourcompany.com \
#   PLATFORM_ADMIN_PASSWORD="$(openssl rand -base64 24)" \
#   PLATFORM_ADMIN_BOOTSTRAP_TOKEN="$(openssl rand -hex 32)" \
#   API_BASE=https://your-prod-host \
#   ./bootstrap-platform-admin.sh

set -euo pipefail

: "${PLATFORM_ADMIN_EMAIL:?PLATFORM_ADMIN_EMAIL must be set}"
: "${PLATFORM_ADMIN_PASSWORD:?PLATFORM_ADMIN_PASSWORD must be set}"
: "${PLATFORM_ADMIN_BOOTSTRAP_TOKEN:?PLATFORM_ADMIN_BOOTSTRAP_TOKEN must be set}"

API_BASE="${API_BASE:-http://localhost:8081}"

# Wait for the app to be reachable (useful right after a fresh deploy/restart).
for i in $(seq 1 30); do
  if curl -sf "${API_BASE}/auth/helloWorld" > /dev/null 2>&1; then
    break
  fi
  echo "Waiting for backend at ${API_BASE}... (${i}/30)"
  sleep 2
done

response=$(curl -sf -X POST "${API_BASE}/auth/register-platform-admin" \
  -H "X-Bootstrap-Token: ${PLATFORM_ADMIN_BOOTSTRAP_TOKEN}")
echo "${response}"
