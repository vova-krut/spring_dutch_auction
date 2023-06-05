pipeline {
    agent {
        node {
            label 'docker-agent-java'
        }
    }
    triggers { pollSCM '* * * * *' }
    stages {
        stage('Build') {
            steps {
                echo "Building.."
                sh 'sudo dockerd'
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    sh 'cp $YAML_FILE src/main/resources/application.yml'
                }
                sh 'docker-compose build'
            }
        }
        stage('Run') {
            steps {
                echo "Running.."
                sh 'docker-compose up'
            }
        }
    }
}