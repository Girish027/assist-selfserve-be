function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).createTeams_p0;
    var apiData = [{
        "entityBaseData": {
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityType": "team"
        },
        "entityAttributes": {
            "teamName": uiData.teamName,
            "accountId": componentAccountId
        }
        }];
    return JSON.stringify(apiData);
}

