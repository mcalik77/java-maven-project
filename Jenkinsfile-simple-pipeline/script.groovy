def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t mcalik77/demo-app:jma-2.1 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push mcalik77/demo-app:jma-2.1'
    }
}

def deployApp() {
    sshagent(['docker-server']) {
     def dockerCmd = 'docker run -d -p 3080:8080 mcalik77/demo-app:jma-2.1'
     sh "ssh -o StrictHostKeyChecking=no ec2-user@52.53.152.194 ${dockerCmd}"
    }

}

return this
