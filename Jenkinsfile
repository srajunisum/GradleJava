pipeline {

environment {
       JAR_DIR = "${env.WORKSPACE}\\build\\libs"
       TARGET_DIR = "C:\\Users\\nisum\\Documents\\Sterling\\bin"
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
                    echo "${JAR_DIR} "
                    echo "${TARGET_DIR}"
                 }
        }

        stage('Copy Jar') {
             steps {
               fileOperations([fileCopyOperation(excludes: '', flattenFiles: true, includes:"${JAR_DIR}" , targetLocation:"${TARGET_DIR}")])
             }
           }




    }

  }
