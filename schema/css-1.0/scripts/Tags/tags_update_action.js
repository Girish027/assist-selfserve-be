function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).updateTags_p0;
    var apiData = [{
        "entityBaseData": {
            "accountId": componentAccountId,
            "clientId": componentClientId,
            "entityType": "tag",
            "entityId": entityId,
            "entityDisplayName": uiData.tagName
        },
        "entityAttributes": {
            "tagName": uiData.tagName,
            "tagType": uiData.tagType
        }
    }];
    if (env == "LIVE") {
        var pfd = JSON.parse(prefetch);
        var liveTags = pfd.liveTags;
        for (var i = 0; i < liveTags.length; i++) {
            if (liveTags[i].label == uiData.tagName) {
                apiData[0].entityBaseData.entityId = liveTags[i].name;
                break;
            }
        }
    }
    return JSON.stringify(apiData);
}