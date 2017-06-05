#!/bin/bash

set -ev


if [[ $TRAVIS_PULL_REQUEST == “false” ]] && [[ $TRAVIS_BRANCH == “master” ]]; then
    PUBLISH="artifactoryPublish"
fi

./gradlew clean build connectedCheck -PdisablePreDex $PUBLISH --stacktrace --info
