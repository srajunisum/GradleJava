pipeline {
  environment {
         WORK_DIR      = "${env.WORKSPACE}/build/libs"
         STEERLING_DIR = "C:\\Users\\nisum\\Documents\\Sterling\\bin"
    }
  agent any

   stages {
      stage('Cloning Git') {
          steps {
             git credentialsId: '6a3bf6ef-5144-4e85-9ce9-c05020be6a4a', url: 'https://github.com/srajunisum/GradleJava.git'
           }
       }

      stage('Gradle Build') {
          steps {
              bat 'gradlew.bat clean build'
            }
        }

      stage('Copy Jar') {
          steps {
            dir("${WORK_DIR}") {
             fileOperations([fileCopyOperation(excludes: '', flattenFiles: true, includes: '*.jar', targetLocation: "${STEERLING_DIR}")])
             }
          }
       }

      stage('Install3rdParty') {
         steps {
            script {
               dir("${STEERLING_DIR}"){
                bat 'install3rdParty.cmd MSN jar -j *.jar -targetJVM EVERY'
                }
            }
          }
       }

      stage('Buildear') {
         steps {
            script {
              dir("${STEERLING_DIR}"){
              bat 'buildear.cmd -Dappserver=jboss -Dwarfiles=smcfs,sbc -Dearfile=smcfs.ear -Dnowebservice=true -Dnoejb=true -Ddevmode=true create-ear'
              }
            }
          }
      }

    }

  }
