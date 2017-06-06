#!/bin/bash

set -ev

echo "IS PULL REQUEST : "$TRAVIS_PULL_REQUEST
echo "IS BRANCH : "$TRAVIS_BRANCH

if [[ $TRAVIS_PULL_REQUEST == "false" ]] && [[ $TRAVIS_BRANCH == "master" ]]; then
    PUBLISH="artifactoryPublish"
fi

echo "PUBLISH : "$PUBLISH

./gradlew clean build connectedCheck -PdisablePreDex $PUBLISH --stacktrace --info
