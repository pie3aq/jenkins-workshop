@Library('sharedlib@3.0.0')

import sharedlib.command.gitlab.*


def status(String status, String comment) {
    if (comment) {
        echo "${comment}"
    }
}



pipeline {
    agent any
    options { disableConcurrentBuilds() }
    environment {
        TEST_ENV = "Test"
    }
    parameters {
        string(name: 'GITLAB_OBJECT_KIND', defaultValue: '')
        string(name: 'GITLAB_MR_STATE', defaultValue: '')
    }
    triggers {
        GenericTrigger(
            genericVariables: [
                [key: 'GITLAB_OBJECT_KIND', value: '$.object_kind'],
                [key: 'GITLAB_MR_STATE', value: '$.object_attributes.state']
            ],
            causeString: 'Triggered by GitLab. Action type: $GITLAB_OBJECT_KIND $GITLAB_MR_STATE',
            token: "${gitlab_token}"
        )
    }
    stages {
        stage('Test stage') {
            agent {
                label 'linux_slaves1'
            }
            when {
                beforeAgent true
                allOf {
                    expression { return GITLAB_OBJECT_KIND == 'merge_request' }
                    expression { return !(GITLAB_MR_STATE == 'closed' ) }
                }
            }
            steps {
                script {
                    status("running")
                    echo "testing"
                }
            }
            post {
                success {
                    script {
                        status("success", "Test stage - success!")
                    }
                }
                failure {
                    script {
                        status("failed", "Test stage - failed!")
                    }
                }
                always {
                    cleanWs()
                }
            }
        }
    }
}