#!/bin/bash
set -euo pipefail

ORCHESTRATOR_CONFIG_URL=$1
shift

mvn org.sonarsource.orchestrator:orchestrator-maven-plugin::create-db verify \
  -pl :sonar-db \
  -Dorchestrator.configUrl=$ORCHESTRATOR_CONFIG_URL \
  -B -e -V $*
