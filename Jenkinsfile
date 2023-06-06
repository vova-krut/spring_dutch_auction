pipeline {
    agent {
        docker {
            image 'vovakrut/java-docker'
        }
    }
    triggers { pollSCM '* * * * *' }
    stages {
        stage('Clone repo') {
            steps {
                echo 'Cloning...'
                sh 'git clone https://github.com/vova-krut/spring_dutch_auction'
                sh 'cd spring_dutch_auction'
            }
        }
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