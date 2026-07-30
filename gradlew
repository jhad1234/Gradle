#!/bin/sh

set -e

if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
  echo "ERROR: gradle-wrapper.jar missing"
  exit 1
fi

exec java -jar gradle/wrapper/gradle-wrapper.jar "$@"
