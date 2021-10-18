function translate(pageData, entityId, clientId, accountId, env, prefetch) {
    prefetch = JSON.parse(prefetch);
    var uiData = JSON.parse(pageData).passwordPolicy_p0.configs;
    var apiData = getConfig(prefetch.listPasswordPolicy, "passwordLength");
    apiData.configValue = uiData.pwdLength;
    return JSON.stringify(apiData);
}

function getConfig(items, key) {
    var config = null;
    for (var i = 0; i < items.length; i++) {
        var item = items[i];
        if (item.configKey == key) {
            config = item;
        }
    }
    return config;
}
