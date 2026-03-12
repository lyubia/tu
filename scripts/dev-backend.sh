#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

JAVA_17_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
if [[ -d "$JAVA_17_HOME" ]]; then
  export JAVA_HOME="$JAVA_17_HOME"
  export PATH="/usr/local/opt/openjdk@17/bin:$PATH"
fi

cd "$ROOT_DIR/backend"

exec mvn spring-boot:run

