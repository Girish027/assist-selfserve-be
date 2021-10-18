function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).updateTeams_p0;
    var apiData = [{
        "entityBaseData": {
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityType": "team",
            "entityId": entityId
        },
        "entityAttributes": {
            "teamName": uiData.teamName,
            "accountId": componentAccountId
        }
    }];
    if (uiData.teamTags.length > 0) { 
        apiData[0].entityAttributes.tags = uiData.teamTags.join(",") 
    };
    return JSON.stringify(apiData);
}
