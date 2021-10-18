function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap) {
    pageData = JSON.parse(pageData).updatePropertyConfig_p0;
    var apiData = {};
    if (pageData.scope.scopeType === "account") {
        apiData.scopeId = pageData.scope.accountId;
    } else {
        apiData.scopeId = pageData.scope.queueScope;
    }
    apiData.scopeType = pageData.scope.scopeType;
    apiData.contextVariableBaseId = "";
    apiData.varTypeLabel = "";
    apiData.varName = pageData.varName;
    apiData.order = pageData.order;
    apiData.varType = pageData.varType;
    apiData.displayName = pageData.displayName;
    apiData.validatorId = pageData.validatorId;
    apiData.editableFlag = pageData.editableFlag;
    apiData.maskableFlag = pageData.maskableFlag;
    apiData.mandatoryFlag = pageData.mandatoryFlag;
    apiData.varDescription = pageData.varDescription;
    if (env === "LIVE") {
        var key = getLiveKey(prefetch, liveEntityIdMap, {
            label: pageData.varName,
            secondaryLabel: pageData.varType
        }, "updatePropertyConfig_p0.key");
        if (key !== "") {
            apiData.key = key;
        } else {
            apiData.key = pageData.key;
        }
    } else {
        apiData.key = pageData.key;
    }
    return JSON.stringify(apiData)
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
