#!/bin/bash
mvn clean package
pkill -f "java -cp target/book-finder-1.0.jar BookApp" || true
nohup java -cp target/book-finder-1.0.jar BookApp > app.log 2>&1 &