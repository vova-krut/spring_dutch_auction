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
                sh 'docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=dutch_auction -d -p 5432:5432 postgres'
                withCredentials([file(credentialsId: 'dutch_auction_properties', variable: 'YAML_FILE')]) {
                    sh 'cp $YAML_FILE src/main/resources/application.yml'
                }
                sh './mvnw install'
                sh 'docker stop $(docker ps -a -q)'
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