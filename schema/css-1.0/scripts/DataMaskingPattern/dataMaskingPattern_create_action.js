function translate(pageData, entityId, clientId, accountId, env) {
    pageData = JSON.parse(pageData).createMaskingPatterns_p0;
    if (pageData.scopeType === 'account') {
        pageData.scopeId = pageData.accountScope;
    } else {
        pageData.scopeId = pageData.queueScope;
    }
    var pageDataString = JSON.stringify(pageData);
    return encodeURIComponent(pageDataString);
}
