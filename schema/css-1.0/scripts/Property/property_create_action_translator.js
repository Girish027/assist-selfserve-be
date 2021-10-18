function translate(pageData, entityId, clientId, accountId, env, prefetch) {
    var pageData = JSON.parse(pageData).createPropertyConfig_p0;
    var apiData = {};
    if (pageData.scope.scopeType === 'account') {
        apiData.scopeId = pageData.accountId;
    } else {
        apiData.scopeId = pageData.scope.queueScope;
    }
    apiData.scopeType = pageData.scope.scopeType;
    apiData.contextVariableBaseId = "";
    apiData.varTypeLabel = "";
    apiData.varName = pageData.varName;
    apiData.key = pageData.key;
    apiData.order = pageData.order;
    apiData.varType = pageData.varType;
    apiData.displayName = pageData.displayName;
    apiData.validatorId = pageData.validatorId;
    apiData.editableFlag = pageData.editableFlag;
    apiData.maskableFlag = pageData.maskableFlag;
    apiData.mandatoryFlag = pageData.mandatoryFlag;
    apiData.varDescription = pageData.varDescription;
    if (env == "LIVE") {
        var liveValidatorIds = JSON.parse(prefetch).liveValidators;
        var idList = [];
        for (var i = 0; i < liveValidatorIds.length; i++) {
            idList.push(liveValidatorIds[i].name)
        }
        if (idList.indexOf(apiData.validatorId) == -1) {
            apiData.validatorId = ""
        }
    }
    return JSON.stringify(apiData)
}
