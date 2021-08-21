def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t mcalik77/demo-app:jma-2.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push mcalik77/demo-app:jma-2.0'
    }
}

def deployApp() {
    sshagent(['docker-server']) {
     withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
     sh "echo $PASS | docker login -u $USER --password-stdin"
     sh 'docker pull mcalik77/demo-app:jma-2.0'
     sh 'docker run -d -p 8081:8081 mcalik77/demo-app:jma-2.0'
     }
}
}

return this
