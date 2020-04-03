pipeline {

environment {
       JAR_DIR = "${env.WORKSPACE}/build/libs/GradleJava-1.jar"
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
              dir("C:\\Program Files (x86)\\Jenkins\\workspace\\test\\build\\libs") {
                fileOperations([fileCopyOperation(excludes: '', flattenFiles: true, includes: '*.jar', targetLocation: "C:\\Users\\nisum\\Documents\\Sterling\\bin")])
            }

             }
           }




    }

  }
