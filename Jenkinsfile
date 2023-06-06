pipeline {
    agent {
        docker {
            image 'vovakrut/java-docker'
            args '--user=root -v /var/run/docker.sock:/var/run/docker.sock'
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
        stage('Build') {
            steps {
                echo "Building.."
                sh './mvnw install -DskipTests'
            }
        }
        stage('Run') {
            steps {
                echo "Running.."
                sh 'docker compose up --rm'
            }
        }
    }
    post {
        always {
            sh 'docker compose down'
            sh 'docker compose rm -f'
            sh 'docker volume rm $(docker volume ls -q --filter dangling=true)'
        }
    }
}