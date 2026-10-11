```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/YOUR-USERNAME/hotel-booking.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t hotel-booking .'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker rm -f hotel-app || exit 0'
                bat 'docker run -d --name hotel-app -p 8085:8085 hotel-booking'
            }
        }
    }
}
```
