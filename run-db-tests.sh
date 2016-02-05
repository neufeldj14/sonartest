#!/bin/bash
set -euo pipefail

mvn verify -pl :sonar-db -am -Dorchestrator.configUrl=$1 -B -e -V
