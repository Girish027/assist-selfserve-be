function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityId) {
    pageData = JSON.parse(pageData).updateMsgFilter_p0;
    if (env === "LIVE") {
        pageData.key = getLiveKey(prefetch, liveEntityIdMap, {
            label: pageData.notificationType
        }, liveEntityIdKey);
    }
    return JSON.stringify(pageData)
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
