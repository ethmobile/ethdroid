#!/bin/bash

datenow=$(date +"%Y%m%d")
sed -i "s,__datenow__,$datenow,g" ./bintray_descriptor.json
