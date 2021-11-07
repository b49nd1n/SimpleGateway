#!/bin/bash

cd $1

mvn clean package

java -jar target/SimpleGateway-1.0-SNAPSHOT-jar-with-dependencies.jar 2>&1 >> log &

disown
