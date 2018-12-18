#!/bin/bash
set -e
set +x
mvn clean
mvn install
java -jar target/experiments-*.jar
set -x
