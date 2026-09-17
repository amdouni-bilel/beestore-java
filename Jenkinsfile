pipeline {

    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_IMAGE = 'amdounibilel/beestore'
        DOCKER_TAG = "${BUILD_NUMBER}"
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

        stage('Couverture & Qualite (SonarCloud)') {

            environment {
                SONAR_TOKEN = credentials('SONAR_TOKEN')
            }

            steps {

                sh '''
                mvn verify sonar:sonar \
                -Dsonar.projectKey=amdouni-bilel_beestore-java \
                -Dsonar.organization=amdouni-bilel \
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
                docker build \
                -f Dockerfile.multistage \
                -t ${DOCKER_IMAGE}:${DOCKER_TAG} \
                -t ${DOCKER_IMAGE}:latest .
                """

            }
        }

        stage('Docker Push') {

            steps {

                withCredentials([
                        usernamePassword(
                                credentialsId: '83c3aab9-a1c3-4e58-afb1-dcb5eb957d8c',
                                usernameVariable: 'DOCKER_USER',
                                passwordVariable: 'DOCKER_PASS'
                        )
                ]) {

                    sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    '''

                    sh """
                    docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """

                    sh """
                    docker push ${DOCKER_IMAGE}:latest
                    """

                }
            }
        }
    }

    post {

        success {
            echo 'Pipeline SUCCESS : BeeStore construit, teste, analyse et publie.'
        }

        failure {
            echo 'Pipeline FAILED : voir Console Output pour le detail.'
        }
    }
}