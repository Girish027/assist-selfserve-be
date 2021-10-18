withEnv(['MAIL_TO=prasada@247.ai',
         'MAIL_FROM=central-jenkins@247.ai',
         'NEXUS_UPLOAD_STATUS=SUCCESS',
         'PRESENT_STATUS=DEV_DEPLOY_SUCCESS',
         'JAVA_HOME=/opt/jdk-11.0.3.7-x86_64',
         'GRADLE_HOME=/opt/gradle-5.4.1',
         'MVN_HOME=/opt/apache-maven-3.5.3',
         'timeout_value=24']) {
    echo "${JAVA_HOME}"
    echo "Current branch <${env.BRANCH_NAME}>"
    def buildSuccess = false
    node("build_slave") {
        stage('Preparation') {
            echo '\u26AB Delete workspace'
            sh "cd ${WORKSPACE}"
            deleteDir()
            sh "${getEnvSetCommand()}"
            sh "env"
            echo "Get code from github for the latest commit in this PR"
            executeCheckout()
        }
        if(env.CHANGE_ID) {
            stage('PR Commit') {
                try {
                    echo "Pull request detected"
                    getEnvSetCommand()
                    buildSuccess = executeBuild()
                    sh "ls -altR ${WORKSPACE}/"
                    if (buildSuccess) {
                      currentBuild.result = 'SUCCESS'
                    }
                    else {
                     currentBuild.result = 'FAILURE'
                    }
                } catch(Exception ex) {
                    echo "PR Commit Failed, below are the exception details...."
                    echo "ex.toString() - ${ex.toString()}"
                    echo "ex.getMessage() - ${ex.getMessage()}"
                    echo "ex.getStackTrace() - ${ex.getStackTrace()}"
                    PRESENT_STATUS = 'COMMIT_FAILURE'
                    currentBuild.result = "FAILURE"
                }
            }
        }
        if(!env.CHANGE_ID) {
            stage('PR Merge') {
                try {
                    echo "PR merge detected"
                    echo '\u26AB Delete workspace'
                    sh "cd ${WORKSPACE}"
                    deleteDir()
                    getEnvSetCommand()
                    executeCheckout()
                    executeBuild()
                    sh '''
                        cd ${WORKSPACE}
                        ls -altR ${WORKSPACE}/
                    '''
                    PRESENT_STATUS = 'MERGE_SUCCESS'
                    currentBuild.result = "SUCCESS"
                } catch(Exception ex) {
                    echo "PR Merge Failed, below are the exception details...."
                    echo "ex.toString() - ${ex.toString()}"
                    echo "ex.getMessage() - ${ex.getMessage()}"
                    echo "ex.getStackTrace() - ${ex.getStackTrace()}"
                    PRESENT_STATUS = 'MERGE_FAILURE'
                    currentBuild.result = "FAILURE"
                }
            }
        }
        stage('Code coverage and SonarQube Analysis upload') {
            try {
                withSonarQubeEnv('SonarServer') {
                    // Parameter required for sonar-scanner are read from sonar-project.properties file.
                    sh '''
                        cd ${WORKSPACE}
                        ${GRADLE_HOME}/bin/gradle sonarqube
                    '''
                    sh '''
                    /var/tellme/opt/sonar-scanner-3.2.0.1227/bin/sonar-scanner -e '-Dproject.settings=sonar-project.properties' -e '-Dsonar.branch=${env.BRANCH_NAME}' 
                    '''
                    
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/tests/test', reportFiles: 'index.html', reportName: 'Self-serve portal Code Coverage Report', reportTitles: 'Self-serve Code Coverage report'])
                }
            } catch(Exception ex) {
                echo "SonarQube step Failed, below are the exception details...."
                echo "ex.toString() - ${ex.toString()}"
                echo "ex.getMessage() - ${ex.getMessage()}"
                echo "ex.getStackTrace() - ${ex.getStackTrace()}"
            }
        }
        stage("Quality Gate") {
            // Uncomment below line for handling timeout of sonarserver slow response
            // timeout(time: 1, unit: 'HOURS') {N
            //def qualityGateRes = waitForQualityGate()
            //if (qualityGateRes.status == 'ERROR') {
            //    error "Pipeline aborted due to quality gate failure: ${qualityGateRes.status}"
            //    currentBuild.result = 'FAILURE'
            //}
            //}
        }
        if((!env.CHANGE_ID) && (PRESENT_STATUS == "MERGE_SUCCESS")) {
            stage('Upload to Nexus - dev Repo') {
                try {
                    currentBuild.result = uploadArtifactToNexus("commit")
                    PRESENT_STATUS = 'NEXUS_UPLOAD_DEV_REPO_SUCCESS'
                } catch(Exception ex) {
                    echo "Upload to Nexus - dev Repo (actual repo name is commit) Failed...."
                    echo "ex.toString() - ${ex.toString()}"
                    echo "ex.getMessage() - ${ex.getMessage()}"
                    echo "ex.getStackTrace() - ${ex.getStackTrace()}"
                    PRESENT_STATUS = 'NEXUS_UPLOAD_DEV_REPO_FAILURE'
                    currentBuild.result = "FAILURE"
                }
            }
        }
        if((!env.CHANGE_ID) && (PRESENT_STATUS == "NEXUS_UPLOAD_DEV_REPO_SUCCESS")) {
            stage('Deploy to Dev env') {
                try {
                    sh "touch ${WORKSPACE}/result"
                    sh "chmod 777 ${WORKSPACE}/result"
                    sh '''
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa dev-client-prov01.web.shared.int.sv2.247-inc.net "sudo chef-client -o recipe[Self-Serve-BE] ; echo $?" > ${WORKSPACE}/result
                    cd ${WORKSPACE}
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa dev-client-prov01.web.shared.int.sv2.247-inc.net "sudo cat /var/log/chef-client.log"
                     '''
                    env.WORKSPACE = pwd()
                    def output = readFile "${env.WORKSPACE}/result"
                    echo "output of chef client run =$output"
                    echo "${output.trim()}"
                    if( output.trim() == 1 ) {
                        echo "COMMAND RUN FAILED FOR SERVER dev-client-prov01.web.shared.int.sv2.247-inc.net!!"
                    } else {
                        echo "COMMAND RAN SUCCESSFULLY FOR SERVER dev-client-prov01.web.shared.int.sv2.247-inc.net!!!"
                        PRESENT_STATUS = 'DEV_DEPLOY_SUCCESS'
                        currentBuild.result = "SUCCESS"
                        sh "exit 0 "
                    }
                    sh "touch ${WORKSPACE}/servertwo"
                    sh "chmod 777 ${WORKSPACE}/servertwo"
                    sh '''
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa dev-client-prov02.web.shared.int.sv2.247-inc.net "sudo chef-client -o recipe[Self-Serve-BE] ; echo $?" > ${WORKSPACE}/servertwo
                    cd ${WORKSPACE}
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa dev-client-prov02.web.shared.int.sv2.247-inc.net "sudo cat /var/log/chef-client.log"
                     '''
                    env.WORKSPACE = pwd()
                    def outputtwo = readFile "${env.WORKSPACE}/servertwo"
                    echo "outputtwo=$outputtwo"
                    echo "${outputtwo.trim()}"
                    if( outputtwo.trim() == 1 ) {
                        echo "COMMAND RUN FAILED FOR SERVER dev-client-prov02.web.shared.int.sv2.247-inc.net  !!"
                    } else {
                        echo "COMMAND RAN SUCCESSFULLY FOR SERVER dev-client-prov02.web.shared.int.sv2.247-inc.net!!!"
                        PRESENT_STATUS = 'DEV_DEPLOY_SUCCESS'
                        currentBuild.result = "SUCCESS"
                        sh "exit 0 "
                    }
                } catch(Exception ex) {
                    echo "Deploy to Dev Failed, below are the exception details...."
                    echo "ex.toString() - ${ex.toString()}"
                    echo "ex.getMessage() - ${ex.getMessage()}"
                    echo "ex.getStackTrace() - ${ex.getStackTrace()}"
                    //PRESENT_STATUS = 'DEV_DEPLOY_FAILURE'
                    //currentBuild.result = "FAILURE"
                    echo "Skiping dev deployment step in failure case, till dev envs issues are fixed...."
                    PRESENT_STATUS = 'DEV_DEPLOY_SUCCESS'
                    currentBuild.result = "SUCCESS"
                    sh "exit 0 "
                    
                }
            }
        }
        stash includes: '**/*', name: 'SelfSvrBuildStore'
    }
    if((!env.CHANGE_ID) && (PRESENT_STATUS == "DEV_DEPLOY_SUCCESS")) {
        stage('Promote to QA repo') {
            try {
                mail (from: "${MAIL_FROM}",
                    to: "${MAIL_TO}",
                    subject: "Self-serve portal app - Waiting on approval for ${env.JOB_NAME} (Commit# ${env.BUILD_NUMBER})",
                    body: "Please go to below URL to approve: ${env.BUILD_URL}input/",
                    charset: 'UTF-8',
                    mimeType: 'text/html');
                def PROMOTE_TO_QA_CONFIRM = input(message: 'Do you like to promote to QA nexus repo?', ok: 'Yes',
                        parameters: [booleanParam(defaultValue: true,
                        description: 'If you like to promote to QA nexus repo, just click on Yes button below',name: 'Yes?')])
                echo "User response:" + PROMOTE_TO_QA_CONFIRM
                if (PROMOTE_TO_QA_CONFIRM == true){
                    node("build_slave") {
                        unstash 'SelfSvrBuildStore'
                        currentBuild.result = uploadArtifactToNexus("qa")
                        PRESENT_STATUS = "PROMOTED_TO_QA_YES"
                        currentBuild.result = "SUCCESS"
                        stash includes: '**/*', name: 'SelfSvrBuildStore'
                    }
                } else {
                    PRESENT_STATUS = 'PROMOTED_TO_QA_NO'
                    currentBuild.result = "ABORTED"
                }
            } catch(Exception ex) {
                echo "Promote to QA Failed or manually aborted with below exception. If you have aborted, then ignore below exception details."
                echo "ex.toString() - ${ex.toString()}"
                echo "ex.getMessage() - ${ex.getMessage()}"
                echo "ex.getStackTrace() - ${ex.getStackTrace()}"
                PRESENT_STATUS = 'PROMOTED_TO_QA_NO'
                currentBuild.result = "ABORTED"
            }
        }
    }
    node("build_slave") {
        if((!env.CHANGE_ID) && (PRESENT_STATUS == "PROMOTED_TO_QA_YES")) {
            stage('Deploy to QA env') {
                unstash 'SelfSvrBuildStore'
                try {
                    sh "touch ${WORKSPACE}/qaServerOne"
                    sh "chmod 777 ${WORKSPACE}/qaServerOne"
                    sh '''
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa qa-client-prov01.web.shared.int.sv2.247-inc.net "sudo chef-client -o recipe[Self-Serve-BE] ; echo $?" > ${WORKSPACE}/qaServerOne
                    cd ${WORKSPACE}
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa qa-client-prov01.web.shared.int.sv2.247-inc.net "sudo cat /var/log/chef-client.log"
                     '''
                    env.WORKSPACE = pwd()
                    def output = readFile "${env.WORKSPACE}/qaServerOne"
                    echo "output=$output"
                    echo "${output.trim()}"
                    if( output.trim() == 1 ) {
                            echo "COMMAND RUN FAILED FOR SERVER qa-client-prov01.web.shared.int.sv2.247-inc.net!!"
                    } else {
                        echo "COMMAND RAN SUCCESSFULLY FOR SERVER qa-client-prov01.web.shared.int.sv2.247-inc.net!!!"
                        PRESENT_STATUS = 'QA_DEPLOY_SUCCESS'
                        currentBuild.result = "SUCCESS"
                        sh "exit 0 "
                    }
                    sh "touch ${WORKSPACE}/qaServerTwo"
                    sh "chmod 777 ${WORKSPACE}/qaServerTwo"
                    sh '''
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa qa-client-prov02.web.shared.int.sv2.247-inc.net "sudo chef-client -o recipe[Self-Serve-BE] ; echo $?" > ${WORKSPACE}/qaServerTwo
                    cd ${WORKSPACE}
                    ssh -o StrictHostKeyChecking=no -l cicddev -i ~/.ssh/id_cicddev_rsa qa-client-prov02.web.shared.int.sv2.247-inc.net "sudo cat /var/log/chef-client.log"
                     '''
                    env.WORKSPACE = pwd()
                    def outputtwo = readFile "${env.WORKSPACE}/qaServerTwo"
                    echo "outputtwo=$outputtwo"
                    echo "${outputtwo.trim()}"
                    if( outputtwo.trim() == 1 ) {
                            echo "COMMAND RUN FAILED FOR SERVER qa-client-prov02.web.shared.int.sv2.247-inc.net  !!"
                    } else {
                        echo "COMMAND RAN SUCCESSFULLY FOR SERVER qa-client-prov02.web.shared.int.sv2.247-inc.net!!!"
                        PRESENT_STATUS = 'QA_DEPLOY_SUCCESS'
                        currentBuild.result = "SUCCESS"
                        sh "exit 0 "
                    }
                } catch(Exception ex) {
                    echo "Deploy to QA Failed, below are the exception details...."
                    echo "ex.toString() - ${ex.toString()}"
                    echo "ex.getMessage() - ${ex.getMessage()}"
                    echo "ex.getStackTrace() - ${ex.getStackTrace()}"
                    //PRESENT_STATUS = 'QA_DEPLOY_FAILURE'
                    //currentBuild.result = "FAILURE"
                    echo "Skiping QA deployment step in failure case, till QA envs issues are fixed...."
                    PRESENT_STATUS = 'QA_DEPLOY_SUCCESS'
                    currentBuild.result = "SUCCESS"
                    sh "exit 0 "
                }
            }
        }
        stage('Send Notification') {
            sendBuildStatusNotification()
        }
    }
}
def executeCheckout() {
    checkout scm
}
def boolean executeBuild() {
    def result = false
    try {
        sh '''
            cd ${WORKSPACE}
cat > gradle.properties <<EOF
systemProp.http.proxyHost=proxy-grp1.lb-priv.sv2.247-inc.net
systemProp.http.proxyPort=3128
systemProp.http.nonProxyHosts=*.sv2.247-inc.net|localhost
systemProp.https.proxyHost=proxy-grp1.lb-priv.sv2.247-inc.net
systemProp.https.proxyPort=3128
systemProp.https.nonProxyHosts=*.sv2.247-inc.net|localhost
        '''
        sh '''
            cat gradle.properties
            export _JAVA_OPTIONS=-Djava.io.tmpdir=/tmp
            ${GRADLE_HOME}/bin/gradle clean build test
        '''
        result = true
    } catch(Exception ex) {
        echo "Build Failed, below are the exception details...."
        echo "ex.toString() - ${ex.toString()}"
        echo "ex.getMessage() - ${ex.getMessage()}"
        echo "ex.getStackTrace() - ${ex.getStackTrace()}"
        result = false
    }
    return result
}
def uploadArtifactToNexus(REPO_ID) {
    sh "cd ${WORKSPACE}"
    def REPO_URL="http://nexus.cicd.sv2.247-inc.net/nexus/content/repositories/${REPO_ID}/"
    def GRP_ID="com.tfsc.ilabs"
    def ART_ID="self-service"
    def PACKAGING="jar"
    def VERSION_NAME
    //sh ''' cat application.properties | grep "app.version=" | awk -F '=' -v OFS="" '{$1=""; print}' > VERSION_NAME_VALUE1 '''
    sh ''' cat build.gradle | grep "version = " | awk -F '=' -v OFS="" '{$1=""; print}' | sed -e 's|[" ]||g' > VERSION_NAME_VALUE1 '''
    VERSION_NAME = readFile('VERSION_NAME_VALUE1').trim()
    def statuscommitnum = sh(returnStatus: true, script: "git rev-list HEAD --count > GIT_COMMIT_NUM")
    def GIT_HEAD = readFile('GIT_COMMIT_NUM').trim()
    def ARTIFACT_VERSION = "1.0.0-${BRANCH_NAME}-${GIT_HEAD}"
    def JAR_FILE = "${WORKSPACE}/build/libs/${ART_ID}-${VERSION_NAME}.${PACKAGING}"
    def status1 = sh(returnStatus: true, script: "${MVN_HOME}/bin/mvn -B deploy:deploy-file -Durl=${REPO_URL} -DrepositoryId=${REPO_ID} -DgroupId=${GRP_ID} -DartifactId=${ART_ID} -Dversion=${ARTIFACT_VERSION} -Dpackaging=${PACKAGING} -Dfile=${JAR_FILE} -DgeneratePom=true -e > upload_output1.txt")
    if (status1 != 0) {
        NEXUS_UPLOAD_STATUS = 'FAILURE'
        def output1 = readFile('upload_output1.txt').trim()
        echo output1
    }
    def status2 = sh(returnStatus: true, script: "${MVN_HOME}/bin/mvn -B deploy:deploy-file -Durl=${REPO_URL} -DrepositoryId=${REPO_ID} -DgroupId=${GRP_ID} -DartifactId=${ART_ID} -Dversion=promoted -Dpackaging=${PACKAGING} -Dfile=${JAR_FILE} -DgeneratePom=true -e > upload_output2.txt")
    if (status2 != 0) {
        NEXUS_UPLOAD_STATUS = 'FAILURE'
        def output2 = readFile('upload_output2.txt').trim()
        echo output2
    }
    return "${NEXUS_UPLOAD_STATUS}"
}
def sendBuildStatusNotification(subject="") {
    echo 'Sending notification...'
    def isCurrentBuildSuccessful = (currentBuild.result != 'UNSTABLE' && currentBuild.result != 'FAILURE')
    def prevBuildResult = currentBuild?.getPreviousBuild()?.result
    def mailSubject = "${subject} (${env.BRANCH_NAME} $BUILD_DISPLAY_NAME)"
    if (currentBuild.result == 'ABORTED') {
        mailSubject = "${subject} ABORTED, (${env.BRANCH_NAME} $BUILD_DISPLAY_NAME)"
    } else {
        if (!isCurrentBuildSuccessful) {
            mailSubject = "${subject} FAILED, (${env.BRANCH_NAME} $BUILD_DISPLAY_NAME)"
        } else {
            if(prevBuildResult != 'SUCCESS') {
                mailSubject = "${subject} (${env.BRANCH_NAME} $BUILD_DISPLAY_NAME) is back to normal"
            }
        }
    }
    def consoleLink = "${BUILD_URL}consoleFull"
    def changesLink = "${JOB_URL}changes"
    def scriptsWithErrors
    def mailbody = "<p>${BUILD_URL}</p>"+
                "<p><a href='${consoleLink}'>Console Output</a></p>"+
                "<p><a href='${changesLink}'>Changes</a></p>"
    if(scriptsWithErrors?.trim()) {
        mailbody = "$mailbody<p>Scripts with errors:<br><pre>$scriptsWithErrors</pre></p>"
    }
    mail bcc: '', body: "${mailbody}", cc: '', charset: 'UTF-8', from: "${MAIL_FROM}", mimeType: 'text/html', replyTo: '', subject:  "Self-serve portal app - ${mailSubject}", to: "${MAIL_TO}"
}
/* set ENV and PATH variables */
def getEnvSetCommand(){
    def setProxyCommand =  ''' export no_proxy='127.0.0.1, localhost, 10.*,*.sv2.247-inc.net'
        export http_proxy=http://proxy-grp1.lb-priv.sv2.247-inc.net:3128
        export https_proxy=http://proxy-grp1.lb-priv.sv2.247-inc.net:3128
    '''
    env.PATH = sh (script:"echo \$PATH:${MVN_HOME}/bin:/bin:/usr/local/bin:/bin:/usr/bin:/usr/sbin", returnStdout: true)
    setPathCommand = "export PATH=\$PATH"
    def initEnvCommand = setProxyCommand + "\n" + setPathCommand
    return initEnvCommand
}
