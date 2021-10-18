function translate(apiData) {
    var uiData = { "configs": {} };
    var configs = { "prevPwd": 0, "pwdLength": 0, "pwdExpiry": 0, "pwdAttempts": 0 };
    var items = JSON.parse(apiData).data.items;
    configs.prevPwd = getConfig(items, "historyLength");
    configs.pwdLength = getConfig(items, "passwordLength");
    configs.pwdExpiry = getConfig(items, "expiryDays");
    configs.pwdAttempts = getConfig(items, "retryCount");
    uiData.configs = configs;
    return JSON.stringify(uiData);
}

function getConfig(items, key) {
    var config = null;
    items.forEach(function (item) {
        if (item.configKey == key) {
            config = item;
        }
    });
    return config.configValue;
}