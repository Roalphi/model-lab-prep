#!/bin/bash
mvn clean package
pkill -f "java -cp target/theme-switch-1.0.jar ThemeApp" || true
nohup java -cp target/theme-switch-1.0.jar ThemeApp > app.log 2>&1 &