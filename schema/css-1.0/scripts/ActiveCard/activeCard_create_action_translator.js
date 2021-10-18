function translate(pageData, entityId, clientId, accountId, env) {
    pageData = JSON.parse(pageData).createActiveCards_p0;
    var payload = {};
    payload.commandName = '/f ' + pageData.commandName;
    payload.commandDescription = pageData.commandDescription;
    payload.numberOfParamters = pageData.numberOfParamters;
    payload.enabled = pageData.enabled;
    payload.commandActionName = pageData.commandActionName;
    payload.key = pageData.key;
    return JSON.stringify(payload);
}
