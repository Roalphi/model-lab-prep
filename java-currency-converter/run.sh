#!/bin/bash
mvn clean package
pkill -f "java -cp target/currency-converter-1.0.jar CurrencyApp" || true
nohup java -cp target/currency-converter-1.0.jar CurrencyApp > app.log 2>&1 &