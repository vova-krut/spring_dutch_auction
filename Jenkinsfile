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
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    sh 'cp $YAML_FILE src/main/resources/application.yml'
                }
                sh './mvnw install -DskipTests'
            }
        }
        stage('Run') {
            steps {
                echo "Running.."
                sh 'docker-compose up -v /var/run/docker.sock:/var/run/docker.sock'
            }
        }
    }
}