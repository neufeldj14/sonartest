#!/bin/bash

set -euo pipefail

function configureTravis {
  mkdir ~/.local
  curl -sSL https://github.com/SonarSource/travis-utils/tarball/v25 | tar zx --strip-components 1 -C ~/.local
  source ~/.local/bin/install
}
configureTravis

function strongEcho {
  echo ""
  echo "================ $1 ================="
}

strongEcho 'Build and deploy'

# Do not deploy a SNAPSHOT version but the release version related to this build
set_maven_build_version $TRAVIS_BUILD_NUMBER

# analysis is currently executed by SonarSource internal infrastructure
mvn deploy \
    -Pdeploy-sonarsource \
    -DskipTests \
    -B -e -V
