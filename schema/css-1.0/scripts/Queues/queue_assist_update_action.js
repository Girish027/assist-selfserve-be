function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).updateQueues_p0.tabsField;
    var uiDataB = uiData[0];
    var uiDataA = uiData[1].assistConfig;
    var apiData = [{
            "entityBaseData": {
                "accountId": componentAccountId,
                "entityType": "chatQueue",
                "entityId": uiDataB.queueId,
                "entityDisplayName": uiDataB.queueName,
                "clientId": clientId
            },
            "entityAttributes": {
                "queueLength": uiDataA.queueLength,
                "queueTimeOut": uiDataA.queueTimeOut,
                "resourceFree": uiDataA.routeOn,
                "conversationalInterval": uiDataA.conversationalInterval,
                "agentAffinityPeriod": uiDataA.agentAffinityPeriod,
                "visitorInactivityPeriod": (uiDataA.VisitorInactivityObj.visitorInactivityEnabled == "true" && uiDataA.VisitorInactivityObj.visitorInactivity != 0) ? uiDataA.VisitorInactivityObj.visitorInactivity : -1,
                "agentResponseTime": (uiDataA.AgentResponseObj.agentResponseEnabled == "true" && uiDataA.AgentResponseObj.agentResponse != 0) ? uiDataA.AgentResponseObj.agentResponse : -1,
                "typingEnabled": uiDataA.typingEnabled == "true",
                "typingTimeout": uiDataA.typingTimeout,
                "isAccountQueue": uiDataA.isAccountQueue == "true",
                "agentFTShare": uiDataA.fileTransfer.FTVisitor,
                "visitorMailerConfigKey": uiDataA.mailer.visitorConfig,
                "agentMailerConfigKey": uiDataA.mailer.agentConfig,
                "coViewEnabled": uiDataA.coBrowse.coViewEnabled == "true",
                "coBrowseEnabled": uiDataA.coBrowse.coBrowseEnabled == "true",
                "interLOBEnabled": uiDataA.interLob.acceptInterLob == "true",
                "isOutboundLOBQueueTransferEnabled": uiDataA.interLob.outboundLob == "true",
                "agentAffinityPeriod": uiDataA.agentAffinityPeriod,
                "conversationalInterval": uiDataA.conversationalInterval,
                "dispositionFormContent": uiDataA.queueType === "MESSAGING" ? "" : uiDataA.dispositionForm,
                "crmScriptContent": uiDataA.crmIntg.crmScriptContent,
                "crmDisplayText": uiDataA.crmIntg.crmLaunchBtn,
                "crmAppId": uiDataA.crmIntg.crmAppId,
                "isStandardWrapUpRequired": uiDataA.queueType === "MESSAGING" ? true : (uiDataA.useStandardWrapUp == "true"),
                "avgChatDuration": "10",
                "waitTimeBufferFactor": "1.0",
                "chatDurationRefreshFactor": "3.0"
            }
        }
    ];
    if (uiDataA.skill && uiDataA.skill.join(",") !== "") {
        apiData[0].entityAttributes.skill = uiDataA.skill.join(",")
    };
    return JSON.stringify(apiData);
}
