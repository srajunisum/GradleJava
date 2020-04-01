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

            bat 'xcopy C:\\Program Files (x86)\Jenkins\workspace\test\build\libs C:\\Users\nisum\Documents\Docs /O /X /E /H /K'
        }


    }

  }
