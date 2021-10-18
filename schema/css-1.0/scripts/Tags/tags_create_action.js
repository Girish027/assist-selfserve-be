function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).createTags_p0;
    var apiData = [{
        "entityBaseData": {
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityType": "tag",
            "entityDisplayName": uiData.tagName
        },
        "entityAttributes": {
            "tagName": uiData.tagName,
            "tagType": uiData.tagType
        }
    }];
    return JSON.stringify(apiData);
}