#!/bin/bash

mvn clean package

java -jar target/SimpleGateway-1.0-SNAPSHOT-jar-with-dependencies.jar &

disown
