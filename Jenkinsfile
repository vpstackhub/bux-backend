pipeline {
    agent any

    environment {
        IMAGE_NAME = 'vpstackhub/bux-backend'
        IMAGE_TAG = '1.0'
        REMOTE_USER = 'ec2-user'
        REMOTE_HOST = 'your.ec2.ip.here'
        DOCKER_HUB_CREDENTIALS_ID = 'docker-hub-creds'
        SSH_KEY_CREDENTIALS_ID = 'ec2-ssh-key'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push $IMAGE_NAME:$IMAGE_TAG'
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent([SSH_KEY_CREDENTIALS_ID]) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $REMOTE_USER@$REMOTE_HOST '
                        docker pull $IMAGE_NAME:$IMAGE_TAG &&
                        docker stop bux-backend || true &&
                        docker rm bux-backend || true &&
                        docker run -d --name bux-backend -p 8080:8080 $IMAGE_NAME:$IMAGE_TAG
                    '
                    '''
                }
            }
        }
    }
}