// vars/prepareJava.groovy
def call(String version) {
    if (version == '17') {
        env.JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-17.0.10.0.7-2.el9.x86_64'
    } else {
        env.JAVA_HOME = '/usr/lib/jvm/java-11-openjdk-11.0.22.0.7-2.el9.x86_64'
    }
}

