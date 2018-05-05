#!/bin/bash

## set -xv


SCRIPT=$(basename ${BASH_SOURCE[0]} .sh)

error() { echo >&2 "${SCRIPT}: $*"; exit 1; }
info()  { echo >&2 "${SCRIPT}: $*"; }


info $*

OPERATING_SYSTEM=$(uname -o)
info "${OPERATING_SYSTEM}"

JAVA_APP=$(which java)
info "${JAVA_APP}"

set -x
find ./*/target -name "$1"*-executable.jar -exec "${JAVA_APP}" -jar {} \;
set +x

