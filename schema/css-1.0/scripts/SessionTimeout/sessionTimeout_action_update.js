function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap) {
    pageData = JSON.parse(pageData).updateSessionTimeout_p0;
    var profileId = "timeout_";
    var apiData = {};
    apiData.expression = pageData.expression;
    apiData.profileId = profileId.concat(pageData.timeout);
    if (env === "LIVE") {
        var key = getLiveKey(prefetch, liveEntityIdMap, {
            label: pageData.expression,
            secondaryLabel: pageData.timeout
        }, "updateSessionTimeout_p0.key");
        if (key !== "") {
            apiData.key = key;
        } else {
            apiData.key = pageData.key;
        }
    } else {
        apiData.key = pageData.key;
    }
    return JSON.stringify(apiData);
}

function getLiveKey(prefetch, liveEntityIdMap, testEntity, liveEntityIdKey) {
    if (liveEntityIdMap !== null && liveEntityIdMap !== undefined) {
        var liveEntityId = JSON.parse(liveEntityIdMap);
        if (Object.keys(liveEntityId).length !== 0) {
            return liveEntityId[liveEntityIdKey];
        } else {
            return getLiveKeyFromPrefetch(prefetch, testEntity);
        }
    } else {
        return getLiveKeyFromPrefetch(prefetch, testEntity);
    }
}

function getLiveKeyFromPrefetch(prefetch, testEntity) {
    var listLiveData = JSON.parse(prefetch).listLiveData;
    var key = "";
    for (var i = 0; i < listLiveData.length; i++) {
        if (testEntity.secondaryLabel !== undefined) {
            if (listLiveData[i].label === testEntity.label && listLiveData[i].secondaryLabel === testEntity.secondaryLabel) {
                key = listLiveData[i].name;
                break;
            }
        } else {
            if (listLiveData[i].label === testEntity.label) {
                key = listLiveData[i].name;
                break;
            }
        }
    }
    return key;
}
