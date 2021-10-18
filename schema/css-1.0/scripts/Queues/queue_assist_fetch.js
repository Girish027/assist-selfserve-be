function translate(apiData, entity) {
    var val = JSON.parse(JSON.parse(apiData).data.entity);
    var ea = val.entityAttributes;
    var ebd = val.entityBaseData;
    var uiData = {
        "tabsField": [
            {},
            {
                "assistConfig": {
                    "useStandardWrapUp": ea.isStandardWrapUpRequired ? ea.isStandardWrapUpRequired.toString(): "false",
                    "skill": [],
                    "queueTimeOut": Number(ea.queueTimeOut),
                    "queueLength": Number(ea.queueLength),
                    "routeOn": ea.resourceFree,
                    "agentAffinityPeriod": Number(ea.agentAffinityPeriod),
                    "conversationalInterval": Number(ea.conversationalInterval),
                    "VisitorInactivityObj": {
                        "visitorInactivityEnabled": ea.visitorInactivityPeriod == "-1" ? "false" : "true",
                        "visitorInactivity": ea.visitorInactivityPeriod == "-1" ? 180 : Number(ea.visitorInactivityPeriod)
                    },
                    "AgentResponseObj": {
                        "agentResponseEnabled": ea.agentResponseTime == "-1" ? "false" : "true",
                        "agentResponse": ea.agentResponseTime == "-1" ? 120 : Number(ea.agentResponseTime),
                    },
                    "typingEnabled": ea.typingEnabled ? ea.typingEnabled.toString() : "false",
                    "typingTimeout": Number(ea.typingTimeout),
                    "isAccountQueue": ea.isAccountQueue ? ea.isAccountQueue.toString(): "false",
                    "fileTransfer": {
                        "FTVisitor": ea.agentFTShare
                    },
                    "mailer": {
                        "visitorConfig": isUndefinedOrNull(ea.visitorMailerConfigKey),
                        "agentConfig": isUndefinedOrNull(ea.agentMailerConfigKey)
                    },
                    "coBrowse": {
                        "coBrowseEnabled": ea.coBrowseEnabled ? ea.coBrowseEnabled.toString() : "false",
                        "coViewEnabled": ea.coViewEnabled ? ea.coViewEnabled.toString() : "false"
                    },
                    "crmIntg": {
                        "crmScriptContent": ea.crmScriptContent,
                        "crmLaunchBtn": ea.crmDisplayText,
                        "crmAppId": ea.crmAppId
                    },
                    "interLob": {
                        "outboundLob": ea.isOutboundLOBQueueTransferEnabled ? ea.isOutboundLOBQueueTransferEnabled.toString() : "false",
                        "acceptInterLob": ea.interLOBEnabled ? ea.interLOBEnabled.toString() : "false"
                    }
                }
            }
        ]
    };
    if (ea.skill && ea.skill.split(",") !== "") {
        uiData.tabsField[1].assistConfig.skill = ea.skill.split(",");
    };
    return JSON.stringify(uiData);
}

function isUndefinedOrNull(obj) {
    if (obj === undefined || obj === null || obj === "null" || obj === NaN) {
        return undefined;
    } else return obj;
}