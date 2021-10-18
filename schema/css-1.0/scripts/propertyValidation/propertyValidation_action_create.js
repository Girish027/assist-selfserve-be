function translate(pageData, entityId, clientId, accountId, env, prefetch) {
    var apiData = JSON.parse(pageData).createPropertyValidation_p0;
    apiData.key = "";
    if (!apiData.validatorRule) {
        apiData.validatorRule = ""
    }
    if (!apiData.enumValues || apiData.enumValues.length == 0) {
        apiData.enumValues = [""]
    }
    return JSON.stringify(apiData);
}