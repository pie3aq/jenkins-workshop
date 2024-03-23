// vars/status.groovy
def call(String status, String comment = '') {
    if (comment) {
        echo "${comment}"
    }
}

