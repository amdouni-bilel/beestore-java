pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_IMAGE   = 'beestore'
        DOCKER_TAG     = "${BUILD_NUMBER}"
        SONAR_HOST_URL = 'https://sonarcloud.io'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Couverture & Qualité (SonarCloud)') {
            environment {
                SONAR_TOKEN = credentials('SONAR_TOKEN')
            }
            steps {
                sh '''
                    mvn verify sonar:sonar \
                      -Dsonar.projectKey=beedigital_beestore \
                      -Dsonar.organization=beedigital \
                      -Dsonar.host.url=$SONAR_HOST_URL \
                      -Dsonar.token=$SONAR_TOKEN \
                      -Dsonar.qualitygate.wait=true
                '''
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                    docker build -f Dockerfile.multistage \
                      -t ${DOCKER_IMAGE}:${DOCKER_TAG} \
                      -t ${DOCKER_IMAGE}:latest .
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} \$DOCKER_USER/${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker tag ${DOCKER_IMAGE}:latest \$DOCKER_USER/${DOCKER_IMAGE}:latest"
                    sh "docker push \$DOCKER_USER/${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker push \$DOCKER_USER/${DOCKER_IMAGE}:latest"
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline SUCCESS : BeeStore construit, testé, analysé et publié.'
        }
        failure {
            echo 'Pipeline FAILED : voir Console Output pour le détail.'
        }
    }
}
