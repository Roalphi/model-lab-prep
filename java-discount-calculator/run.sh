#!/bin/bash
mvn clean package
pkill -f "java -cp target/discount-calculator-1.0.jar DiscountApp" || true
nohup java -cp target/discount-calculator-1.0.jar DiscountApp > app.log 2>&1 &