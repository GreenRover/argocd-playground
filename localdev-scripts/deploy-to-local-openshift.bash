#!/bin/bash

mvn clean package

crdPath="${BASH_SOURCE%/*}/../target/classes/META-INF/fabric8/*-v1.yml"

for filename in $crdPath; do
  oc apply -f "$filename"
done
