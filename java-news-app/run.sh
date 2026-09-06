#!/bin/bash
mvn clean package
pkill -f "java -cp target/news-app-1.0.jar NewsApp" || true
nohup java -cp target/news-app-1.0.jar NewsApp > app.log 2>&1 &