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
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    echo "Building.."
                    sh 'cp $YAML_FILE src/main/resources/application.yml'
                    sh './mvnw install'
                }
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