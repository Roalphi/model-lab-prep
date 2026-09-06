#!/bin/bash
mvn clean package
pkill -f "java -cp target/age-calculator-1.0.jar AgeApp" || true
nohup java -cp target/age-calculator-1.0.jar AgeApp > app.log 2>&1 &