#!/bin/bash
mvn clean package
pkill -f "java -cp target/contact-book-1.0.jar ContactApp" || true
nohup java -cp target/contact-book-1.0.jar ContactApp > app.log 2>&1 &