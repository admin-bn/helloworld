#!/usr/bin/groovy


pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '2'))
    timeout(time: 10, unit: 'MINUTES')
    disableConcurrentBuilds()
  }

  agent {
    kubernetes {
      label 'bdop-lapp'
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
labels:
  component: ci
spec:
  # Use service account that can deploy to all namespaces
  serviceAccountName: bdop-jk-admin
  containers:
  - name: maven
    image: maven:3.8-openjdk-17-slim
    command:
    - cat
    tty: true
    volumeMounts:
      - mountPath: "/root/.m2"
        name: bdop-jk-pv-claim
  - name: helm
    image: alpine/helm:3.10.2
    command:
    - cat
    tty: true      
    volumeMounts:
      - mountPath: /usr/local/bin/helm
        name: bdop-jk-pv-claim
  - name: docker
    image: docker:19.03.6
    command:
    - cat
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  volumes:
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
    - name: bdop-jk-pv-claim
      persistentVolumeClaim:
        claimName: bdop-jk-pv-claim
"""
}
   }
	  stages {
	    stage('Run maven') {
	      steps {
	        container('maven') {
	        configFileProvider([configFile(fileId: '31fb2403-392c-41f7-9028-9f154c610d59', variable: 'MAVEN_SETTINGS_XML')]) {
	            sh 'mvn -s $MAVEN_SETTINGS_XML clean install -DskipTests'
	        }
	         //sh "mvn clean install -DskipTests"
	        }
	      }
	    }

	    stage('Push') {
	      steps {
	        container('docker') {
	                withCredentials([
	                usernamePassword(
	                  credentialsId: "bdop-docker-repo",
	                  usernameVariable: 'REGISTRY_USER', passwordVariable: 'REGISTRY_PASS'
	                )
	              ]){
	                    echo "env.BDOP_DOCKER_REPO is '${BDOP_DOCKER_REPO}'"
	                    echo "${REGISTRY_PASS}" 
	                    echo "${REGISTRY_USER}" 
	                    sh "docker login -u ${REGISTRY_USER} -p ${REGISTRY_PASS} "
	                    sh "docker build -t hello-world ."
	                    sh "docker tag hello-world:latest bosenet/hello-world:latest"
	                    sh "docker push  bosenet/hello-world:latest"
	              }
	        }
	      }
	    }
    
	     stage('helm') {
	      steps {
	        container('helm') {
	                withCredentials([
	                usernamePassword(
	                  credentialsId: "bdop_nexus",
	                  usernameVariable: 'NX_USER', passwordVariable: 'NX_PASS'
	                )
	              ]){
	                    sh "helm version"
	                    sh "helm lint ./hello-world"
	                    sh "helm list -n bdop"
	                    //sh "helm upgrade --dry-run --install hw121 /home/jenkins/agent/workspace/HelloWorld/hello-world --namespace bdop"
	                    
	                    sh "helm upgrade hello-world ./hello-world --namespace=bdop"
	                    //sh "helm repo add hw-chart http://ae183eab50180425691974eb08081fd2-1806456043.us-east-1.elb.amazonaws.com:8081/repository/bdop-charts/ --username ${NX_USER} --password ${NX_PASS}"
	                    //sh "helm repo add https://admin-bn.github.io/bn-test-helm"
	                    //sh "echo ${REGISTRY_PASS} | helm registry login registry-1.docker.io  -u ${REGISTRY_USER} --password-stdin"
	                    //sh "helm create hw"
	                    //sh "helm package hw"
	                    //sh "curl -u ${NX_USER}:${NX_PASS} http://ae183eab50180425691974eb08081fd2-1806456043.us-east-1.elb.amazonaws.com:8081/repository/bdop-charts/ --upload-file hw-0.1.0.tgz -v"
	                    //sh "helm install hw bdop-charts/hw -n bdop"
	                    //sh "helm upgrade --install --namespace bdop hw"
	                    //sh "helm list | grep ${HELM_APP_NAME}"
	                    //sh "helm upgrade --install hw ${args.chart_dir} --set imageTag=${args.version_tag},replicas=${args.replicas},cpu=${args.cpu},memory=${args.memory},ingress.hostname=${args.hostname} --namespace=${namespace}"
	              }
	        }
	      }
	    }
	    
	    
  }
  
      post { 
        always { 
            echo 'I will always run!'
            //office365ConnectorSend status: currentBuild.currentResult, webhookUrl: 'https://bosenetltd.webhook.office.com/webhookb2/9e3e3e8a-8b5d-4587-a54f-6bf461785d5c@f129bd96-bdd0-437a-a6c0-bcce2c450aae/JenkinsCI/a08c7dcc8f374099a01b60e77d60964d/1bd1d700-0b69-4cda-aa0b-5175d904c8e2'
        }
    }
}