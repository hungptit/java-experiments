#!/bin/bash
mvn clean package && chmod u+x target/experiments-*.jar -*.jar && java -jar target/experiments-*.jar
