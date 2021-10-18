function translate(pageData, entityId, clientId, accountId, env, prefetch) {
    var apiData = JSON.parse(pageData).updateAccountConfig_p0;
    return JSON.stringify(apiData);
}