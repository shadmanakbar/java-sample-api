#!/bin/bash
# ══════════════════════════════════════════════════════════════════════════════
# scripts/start.sh — Starts the java-sample-api Spring Boot application
# ══════════════════════════════════════════════════════════════════════════════
# This file is already configured for the Java Spring Boot fat JAR produced by:
#   mvn -B package -DskipTests
# The workflow runs that command before calling this script.
# ══════════════════════════════════════════════════════════════════════════════

set -euo pipefail

mkdir -p logs

JAR=$(ls target/*.jar 2>/dev/null | head -1)

if [ -z "$JAR" ]; then
  echo "❌ No JAR found in target/. Run 'mvn package -DskipTests' first."
  exit 1
fi

echo "🚀 Starting $JAR on port 8080 ..."
java -jar "$JAR" \
  --server.port=8080 \
  --spring.jpa.show-sql=false \
  > logs/app.log 2>&1 &

echo "   PID $! — tail logs/app.log to watch startup"
