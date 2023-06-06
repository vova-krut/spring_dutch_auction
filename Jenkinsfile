pipeline {
    agent {
        docker {
            image 'vovakrut/java-docker'
            args '--user=root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    triggers { pollSCM '* * * * *' }
    stages {
        stage('Build') {
            steps {
                echo "Building.."
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    sh 'cp $YAML_FILE src/main/resources/application.yml'
                }
                sh 'docker compose build'
            }
        }
        stage('Run') {
            steps {
                echo "Running.."
                sh 'docker compose up'
            }
        }
    }
}