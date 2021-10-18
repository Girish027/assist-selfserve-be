function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).createQueues_p0.tabsField;
    var uiDataB = uiData[0];
    var uiDataA = uiData[1].assistConfig;
    var visitorInactivity = uiDataA.VisitorInactivityObj.visitorInactivity || 180;
    var agentResponse = uiDataA.AgentResponseObj.agentResponse || 120;
    var queueId = getQueueId(componentAccountId, uiDataB.queueName);
    var apiData = [{
        "entityBaseData": {
            "accountId": componentAccountId,
            "entityType": "chatQueue",
            "entityId": queueId,
            "entityDisplayName": uiDataB.queueName,
            "clientId": clientId
        },
        "entityAttributes": {
            "queueLength": uiDataA.queueLength || 30,
            "queueTimeOut": uiDataA.queueTimeOut || 600,
            "resourceFree": uiDataA.routeOn || "DISPOSE",
            "visitorInactivityPeriod": (uiDataA.VisitorInactivityObj.visitorInactivityEnabled == "true" && visitorInactivity != 0) ? visitorInactivity : -1,
            "agentResponseTime": (uiDataA.AgentResponseObj.agentResponseEnabled == "true" && agentResponse != 0) ? agentResponse : -1,
            "typingEnabled": uiDataA.typingEnabled == "true",
            "typingTimeout": uiDataA.typingTimeout,
            "isAccountQueue": uiDataA.isAccountQueue == "true",
            "agentFTShare": uiDataA.fileTransfer.FTVisitor,
            "visitorMailerConfigKey": uiDataA.mailer.visitorConfig,
            "agentMailerConfigKey": uiDataA.mailer.agentConfig,
            "coViewEnabled": uiDataA.coBrowse ? uiDataA.coBrowse.coViewEnabled == "true" : false,
            "coBrowseEnabled": uiDataA.coBrowse ? uiDataA.coBrowse.coBrowseEnabled == "true" : false,
            "interLOBEnabled": uiDataA.interLob.acceptInterLob == "true",
            "isOutboundLOBQueueTransferEnabled": uiDataA.interLob.outboundLob == "true",
            "agentAffinityPeriod": uiDataA.agentAffinityPeriod || 180,
            "conversationalInterval": uiDataA.conversationalInterval || 72,
            "dispositionFormContent": uiDataA.queueType === "MESSAGING" ? "" : uiDataA.dispositionForm,
            "crmScriptContent": uiDataA.crmIntg.crmAppId ? uiDataA.crmIntg.crmScriptContent : undefined,
            "crmDisplayText": uiDataA.crmIntg.crmAppId ? uiDataA.crmIntg.crmLaunchBtn : undefined,
            "crmAppId": uiDataA.crmIntg.crmAppId ? uiDataA.crmIntg.crmAppId : undefined,
            "isStandardWrapUpRequired": uiDataA.queueType === "MESSAGING" ? true : (uiDataA.useStandardWrapUp == "true"),
            "avgChatDuration": "10",
            "waitTimeBufferFactor": "1.0",
            "chatDurationRefreshFactor": "3.0"
        }
    }];
    if (uiDataA.skill && uiDataA.skill.join(",") !== "") {
        apiData[0].entityAttributes.skill = uiDataA.skill.join(",")
    };
    return JSON.stringify(apiData);
}

function getQueueId(componentAccountId, queueName) {
    var arr = queueName.toLowerCase().split(" ");
    var idArr = [];
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] != "") {
            idArr.push(arr[i]);
        }
    }
    return componentAccountId + "-queue-" + idArr.join("-");
}