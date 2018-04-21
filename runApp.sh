#!/bin/bash

## set -xv


SCRIPT=$(basename ${BASH_SOURCE[0]} .sh) 

error() { echo >&2 "$SCRIPT: $*"; exit 1; } 
info()  { echo >&2 "$SCRIPT: $*"; } 


info $*

CYGWIN=$(uname -o)
info ${CYGWIN}

info "${JAVA_HOME}"


set -x
find ./*/target -name "$1"*-executable.jar -exec "${JAVA_HOME}"/bin/javaw.exe -jar {} \;
set +x

