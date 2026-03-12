#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

cleanup() {
  if [[ -n "${BACK_PID:-}" ]]; then kill "$BACK_PID" >/dev/null 2>&1 || true; fi
  if [[ -n "${FRONT_PID:-}" ]]; then kill "$FRONT_PID" >/dev/null 2>&1 || true; fi
}
trap cleanup EXIT INT TERM

if command -v brew >/dev/null 2>&1; then
  brew services start mysql >/dev/null 2>&1 || true
fi

bash "$ROOT_DIR/scripts/dev-backend.sh" &
BACK_PID=$!

bash "$ROOT_DIR/scripts/dev-frontend.sh" &
FRONT_PID=$!

wait

