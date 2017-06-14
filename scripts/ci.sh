#!/bin/bash

set -ev

./gradlew clean
./gradlew build --info -PdisablePreDex
./gradlew connectedCheck --info -PdisablePreDex

if [[ $TRAVIS_PULL_REQUEST == "false" ]] && [[ $TRAVIS_BRANCH == "master" ]]; then
    ./gradlew artifactoryPublish --info
fi
