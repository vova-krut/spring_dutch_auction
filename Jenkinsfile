pipeline {
    agent {
        docker {
            image 'vovakrut/java-docker'
            args '--user=root -v /var/run/docker.sock:/var/run/docker.sock --net=host'
        }
    }
    triggers { pollSCM '* * * * *' }
    stages {
        stage('Copy configuration file') {
            steps {
                echo 'Copying application.yml...'
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    sh 'cp $YAML_FILE src/main/resources/application.yml'
                }
            }
        }
        stage('Scan') {
            steps {
                withSonarQubeEnv(installationName: 'sq1') {
                    sh './mvnw clean verify sonar:sonar'
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Build') {
            steps {
                echo "Building.."
                sh './mvnw install -DskipTests'
            }
        }
        stage('Run') {
            steps {
                echo "Running.."
                sh 'docker compose up'
            }
        }
    }
    post {
        always {
            sh 'docker compose down'
            sh 'docker compose rm -f'
        }
    }
}