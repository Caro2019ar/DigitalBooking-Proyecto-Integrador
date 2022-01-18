pipeline {

    agent any

    post {
        failure {
            updateGitlabCommitStatus name: 'Build & Deploy', state: 'failed'
        }
        success {
            updateGitlabCommitStatus name: 'Build & Deploy', state: 'success'
        }
    }

    stages {
         
        stage("Build...") {
            steps {
                dir("frontend/booking-hotels") {
                        sh 'sudo rm yarn.lock'
                        sh 'sudo rm package-lock.json'
                        sh 'yarn install'
                        sh 'yarn run build'
                }
                dir("Backend/Integrador") {
                        sh 'sudo kill -9 $(lsof -t -i:8080) || true'
                        sh 'mvn -B -DskipTests clean package'
                }
            }
        }

        stage("Deploy...") {
            steps {
                sh 'sudo rm -rf /var/www/html'
                sh 'sudo cp -a ${WORKSPACE}/frontend/booking-hotels/build/. /var/www/html/'
                sh 'JENKINS_NODE_COOKIE=dontKillMe nohup java -jar ${WORKSPACE}/Backend/Integrador/target/Integrador-0.0.1-SNAPSHOT.jar &'
                sh 'sudo systemctl restart httpd'
            }
        }

    }

}