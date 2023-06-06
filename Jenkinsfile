pipeline {
    agent {
        docker {
            image 'vovakrut/java-docker'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    triggers { pollSCM '* * * * *' }
    stages {
        stage('Build') {
            steps {
                echo "Building.."
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    sh 'sudo cp $YAML_FILE src/main/resources/application.yml'
                }
                sh 'sudo docker compose build'
            }
        }
        stage('Run') {
            steps {
                echo "Running.."
                sh 'sudo docker compose up'
            }
        }
    }
    post {
        always {
            sh 'docker volume rm $(docker volume ls -q --filter dangling=true)'
        }
    }
}