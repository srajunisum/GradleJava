pipeline {
  environment {
         WORK_DIR      = "${env.WORKSPACE}/build/libs"
         STERLING_DIR = "C:\\Users\\nisum\\Documents\\Sterling\\bin"
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
             fileOperations([fileCopyOperation(excludes: '', flattenFiles: true, includes: '*.jar', targetLocation: "${STERLING_DIR}")])
             }
          }
       }

      stage('Install3rdParty') {
         steps {
            script {
               dir("${STERLING_DIR}"){
                bat 'install3rdParty.cmd MSN jar -j *.jar -targetJVM EVERY'
                }
            }
          }
       }

       stage('Resource deployer') {
         steps {
           script {
             dir("${STERLING_DIR}"){
              bat 'deployer.cmd -t resourcejar'
              }
            }
          }
       }

      stage('Entity deployer') {
        steps {
          script {
             dir("${STERLING_DIR}"){
              bat 'deployer.cmd -t entitydeployer'
              }
           }
        }
       }

    stage('Create folder') {
            steps {
              script {
                 dir("${STERLING_DIR}"){
                  bat 'mkdir testsmcfs.ear'
                  }
               }
            }
           }


    }

  }
