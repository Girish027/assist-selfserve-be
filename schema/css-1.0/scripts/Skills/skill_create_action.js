function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).createSkills_p0;
    var apiData = [{
        "entityBaseData": {
            "entityDisplayName": uiData.skillName,
            "entityId": "",
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityType": "skill"
        },
        "entityAttributes": {
            "skillName": uiData.skillName,
            "skillDesc": uiData.skillDesc,
            "account": componentAccountId,
            "users": "[]"
        }
    }];
    return JSON.stringify(apiData);
}