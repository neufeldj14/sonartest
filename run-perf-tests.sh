#!/bin/bash
set -euo pipefail

echo "YeAH"
cd it/perf-tests
mvn verify -B -e -V
