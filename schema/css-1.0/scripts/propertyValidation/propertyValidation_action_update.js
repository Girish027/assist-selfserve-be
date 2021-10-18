function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap) {
    var apiData = JSON.parse(pageData).updatePropertyValidation_p0;
    if (!apiData.validatorRule) {
        apiData.validatorRule = "";
    }
    if (!apiData.enumValues || apiData.enumValues.length == 0) {
        apiData.enumValues = [""];
    }
    if (env === "LIVE") {
        var key = getLiveKey(prefetch, liveEntityIdMap, {
            label: pageData.validatorName
        }, "updatePropertyValidation_p0.key");
        if (key !== "") {
            apiData.key = key;
        }
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
