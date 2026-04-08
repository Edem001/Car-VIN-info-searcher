pipeline {
    agent any
    
    stages {
        stage('Pull Project') {
            steps {
                echo 'Cleaning workspace and pulling latest code...'
                checkout scm
            }
        }

        stage('Prepare Environment') {
            steps {
                echo 'Setting execution permissions for Gradle...'
                sh "chmod +x gradlew"
            }
        }

        stage('Build Android APK') {
            steps {
                echo 'Starting Gradle Build...'
                sh "./gradlew assembleDebug"
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving the generated APK...'
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', allowEmptyArchive: false
            }
        }
    }
    
    post {
        always {
            echo 'Build process finished.'
        }
        success {
            echo 'APK is ready for download!'
        }
        failure {
            echo 'Build failed. Check the console output above.'
        }
    }
}
