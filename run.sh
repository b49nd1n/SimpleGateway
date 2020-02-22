#!/bin/bash

mvn exec:java -Dexec.mainClass=ru.mgvk.simplegateway.Main >> log &


disown
