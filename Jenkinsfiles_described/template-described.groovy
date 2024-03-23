@Library('sharedlib@3.0.0')
//definicja bibliotek uzywanych w procesie

import sharedlib.command.gitlab.*
//import poszczegolnych klas z biblioteki

def status(String status, String comment) {
    if (comment) {
        echo "${comment}"
    }
}
//miejsce na definicje wlasnych funkcji, uzytych potem wewnatrz pipeline


pipeline { //poczatek wlasciwego procesu
    agent any //okreslenie definicji agenta (etykiety, rodzaje, uzycie pluginow ssh lub docker) dla calego procesu
    options { disableConcurrentBuilds() } //dodatkowe opcje pozwalajace na niestandardowe zachowanie procesu 
    environment {
        TEST_ENV = "Test" //miejsce na definicje zmiennych srodowiskowych dzialajacych w zakresie calego procesu
    }
    parameters { //parametry uruchomieniowe procesu
        string(name: 'GITLAB_OBJECT_KIND', defaultValue: '')
        string(name: 'GITLAB_MR_STATE', defaultValue: '')
    }
    triggers { //okreslenie wyzwalaczy procesu 
        GenericTrigger( //uzycie i definicja modulu GenericTrigger pozwalajacego na wyzwolenie budowania za pomoca curla autoryzowanego tokenem
            genericVariables: [ //przypisanie do parametrow wartosci z payloadu przekazanego przez wyzwalacza (np sygnal z gitlab) przy uruchamianiu GenericTrigger
                [key: 'GITLAB_OBJECT_KIND', value: '$.object_kind'],
                [key: 'GITLAB_MR_STATE', value: '$.object_attributes.state']
            ],
            causeString: 'Triggered by GitLab. Action type: $GITLAB_OBJECT_KIND $GITLAB_MR_STATE', //metadana okreslajaca opis powodu uruchomienia joba, gdy dzieje sie to przez GenericTrigger
            token: "WklejTuDowolnyWygenerowanyToken" //specjalny token, wymagany zeby uruchomic proces przez GenericTrigger
        )                                            //przyklad: przy takiej wartosci URL potrzebny do uruchomienia procesu to:
    }                                                //http://10.158.18.179:8080/generic-webhook-trigger/invoke?token=WklejTuDowolnyWygenerowanyToken
    stages { //klamra rozpoczynajaca definicje etapow pipeline widzianych w GUI jako osobne kwadraty
        stage('Test stage') { //definicja prierwszego stage - nazwa
            agent { //mozliwosc ustawienia osobnej definicji agenta dla poszczegolnych stage'y
                label 'linux_slaves1'
            }
            when { //instrukcje warunkowe na podstawie ktorych podejmowana jest decyzja czy stage ma byc uruchamiany czy pomijany
                beforeAgent true
                allOf {
                    expression { return GITLAB_OBJECT_KIND == 'merge_request' }
                    expression { return !(GITLAB_MR_STATE == 'closed' ) }
                }
            }
            steps { //klamra rozpoczynajaca definicje krokow (poszczegolnych polecen jenkinsowych), ktore maja zostac wykonane w danym stage
                script { //krok bedacy poleceniem wykonania kodu grooviego
                    status("running")
                    echo "testing"
                }
            }
            post { //kroki do wykonania po zakonczeniu wykonywania wlasciwego stage'a
                success { //w przypadku sukcesu
                    script {
                        status("success", "Test stage - success!")
                    }
                }
                failure { //w przypadku niepowodzenia
                    script {
                        status("failed", "Test stage - failed!")
                    }
                }
                always { //zawsze :)
                    cleanWs()
                }
            }
        }
    }
}