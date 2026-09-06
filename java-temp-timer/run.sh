#!/bin/bash
mvn clean package
pkill -f "java -cp target/temp-timer-app-1.0.jar App" || true
nohup java -cp target/temp-timer-app-1.0.jar App > app.log 2>&1 &