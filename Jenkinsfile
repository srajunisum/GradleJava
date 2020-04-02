pipeline {

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
              script {
                  dir ("C:\\Users\\nisum\\Documents\\Sterling\\bin")
                  bat 'install3rdParty.cmd MSN jar -j C:\\GradleJava-1.jar -targetJVM EVERY'
              }
            }
         }


    }

  }
