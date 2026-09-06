#!/bin/bash
mvn clean package
pkill -f "java -cp target/quiz-app-1.0.jar" || true
nohup java -cp target/quiz-app-1.0.jar QuizApp > app.log 2>&1 &