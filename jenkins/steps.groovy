pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }

        stage('Test') {
            steps {
                // Add your test commands here
                echo 'Testing...'
            }
        }

        stage('Deploy') {
            steps {
                // Add your deployment commands here
                echo 'Deploying...'
            }
        }
    }

    post {
        always {
            // Clean up workspace after pipeline completes
            cleanWs()
        }
        success {
            // Actions to perform on success
            echo 'Build, Test and Deployment completed successfully.'
        }
        failure {
            // Actions to perform if the pipeline fails
            echo 'Error occurred during the pipeline execution.'
        }
    }
}
