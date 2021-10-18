function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiDataB = JSON.parse(pageData).updateQueues_p0.tabsField[0];
    var uiDataA = JSON.parse(pageData).updateQueues_p0.tabsField[1].assistConfig;
    var apiData = [{
        "entityAttributes": {
            "accountId": componentAccountId,
            "queueName": uiDataB.queueName,
            "queueDesc": uiDataB.queueDesc,
            "queueType": uiDataA.queueType
        },
        "entityBaseData": {
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityDisplayName": uiDataB.queueName,
            "entityType": "queue",
            "entityId": uiDataB.queueId,
        }
    }
];
    if (uiDataB.queueTags.length > 0) {
        apiData[0].entityAttributes.tags = uiDataB.queueTags.join(",")
    };
    return JSON.stringify(apiData);
}
