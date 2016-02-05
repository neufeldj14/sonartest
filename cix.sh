#!/bin/bash
#

set -euo pipefail

if [ -z "${RUN_ACTIVITY:-}" ]; then
  echo "missing RUN_ACTIVITY"
  exit 1
fi

if [ -z "${SLAVE_TYPE:-}" ]; then
  echo "missing SLAVE_TYPE"
  exit 1
fi


case "$RUN_ACTIVITY" in

  run-db-unit-tests-*)
    DB_ENGINE=`echo $RUN_ACTIVITY | sed "s/run-db-unit-tests-//g"`

    echo "./run-db-unit-tests.sh $DB_ENGINE $SLAVE_TYPE"
    ;;

  run-db-integration-tests-*)
    DB_ENGINE=`echo $RUN_ACTIVITY | sed "s/run-db-integration-tests-//g" | cut -d \- -f 1`
    CATEGORY=`echo $RUN_ACTIVITY | sed "s/run-db-integration-tests-//g" | cut -d \- -f 2`

    echo "./run-db-integration-tests.sh $DB_ENGINE $CATEGORY $SLAVE_TYPE"
    ;;

  run-upgrade-tests-*)
    DB_ENGINE=`echo $RUN_ACTIVITY | sed "s/run-upgrade-tests-//g"`

    echo "./run-upgrade-tests.sh $DB_ENGINE  $SLAVE_TYPE"
    ;;

  run-perf-tests)
    ./run-perf-tests.sh
    ;;

  *)
    echo "unknown RUN_ACTIVITY = $RUN_ACTIVITY"
    exit 1
    ;;

esac
