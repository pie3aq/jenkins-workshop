package com.linuxpolska.jenkins

class MavenCmd {

    static void buildMvn(script, String options = '') {
        if (options) {
            script.sh "mvn compile ${options}"
        } else {
            script.sh "mvn compile"
        }
    }

    static void packageMvn(script, String options = '') {
        if (options) {
            script.sh "mvn package ${options}"
        } else {
            script.sh "mvn package"
        }
    }

    static void testMvn(script, String options = '') {
        if (options) {
            script.sh "mvn test ${options}"
        } else {
            script.sh "mvn test"
        }
    }

    static void sonarMvn(script, String options = '') {
        if (options) {
            script.sh "mvn sonar:sonar ${options}"
        } else {
            script.sh "mvn sonar:sonar"
        }
    }
}

