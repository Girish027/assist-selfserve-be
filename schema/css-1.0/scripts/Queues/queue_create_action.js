function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).createQueues_p0.tabsField;
    var uiDataB = uiData[0];
    var uiDataA = uiData[1];
    var apiData = [{
        "entityAttributes": {
            "accountId": componentAccountId,
            "queueName": uiDataB.queueName,
            "queueDesc": uiDataB.queueDesc,
            "queueType": uiDataA.assistConfig.queueType
        },
        "entityBaseData": {
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityDisplayName": uiDataB.queueName,
            "entityType": "queue"
        }
    }];
    return JSON.stringify(apiData);
}