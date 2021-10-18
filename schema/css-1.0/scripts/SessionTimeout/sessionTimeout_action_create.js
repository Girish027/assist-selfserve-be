function translate(pageData, entityId, clientId, accountId, env) {
    pageData = JSON.parse(pageData).createSessionTimeout_p0;
    var profileId = "timeout_";
    var apiData = {};
    apiData.key = pageData.key;
    apiData.expression = pageData.expression;
    apiData.profileId = profileId.concat(pageData.timeout);
    return JSON.stringify(apiData);
}
