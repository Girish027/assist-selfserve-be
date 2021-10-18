function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap) {
    pageData = JSON.parse(pageData).updateLeadConsole_p0;
    var apiData = {};
    if (pageData.propertyType === "Reset Time") {
        apiData.propertyType = "RESET_TIME";
    } else {
        apiData.propertyType = pageData.propertyType;
    }
    if (env === "LIVE") {
        var key = getLiveKey(prefetch, liveEntityIdMap, {
            label: pageData.applicationValue
        }, "updateLeadConsole_p0.key");
        if (key !== "") {
            apiData.key = key;
        } else {
            apiData.key = pageData.key;
        }
    } else {
        apiData.key = pageData.key;
    }
    apiData.hours = pageData.time.hours;
    apiData.minutes = pageData.time.minutes;
    apiData.meridiem = pageData.time.meridian;
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
