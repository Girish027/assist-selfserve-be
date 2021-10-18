function translate(pageData, entityId, clientId, accountId, env, prefetch) {
    pageData = JSON.parse(pageData).authorisedUrls_p0;
    prefetch = JSON.parse(prefetch);
    var apiData = {};
    apiData.accountId = pageData.accountId;
    apiData.scopeType = pageData.scopeType;
    apiData.scopeValue = pageData.scopeValue;
    apiData.urlFilterListingType = pageData.filterType;
    apiData.enabled = pageData.enabled == "true";
    if (env === "TEST") {
        apiData.filterConfig = prefetch.enabledUrlsTest.filterConfig;
        apiData.key = prefetch.enabledUrlsTest.key;
    } else if (env === "LIVE") {
        apiData.filterConfig = prefetch.enabledUrlsLive.filterConfig;
        apiData.key = prefetch.enabledUrlsLive.key;
    }
    pageData.filterConfig.forEach(function(pageDataFilter) {
        urlId = pageDataFilter.urlFilterConfigId;
        if (urlId != undefined && urlId != "") {
            apiData.filterConfig.forEach(function(apiFilter) {
                if (urlId === apiFilter.urlFilterConfigId) {
                    apiFilter.filterText = pageDataFilter.filterText;
                    apiFilter.filterOptions = pageDataFilter.filterOptions;
                    apiFilter.urlConfigDeleted = true;
                }
            });
        } else {
            apiData.filterConfig.push({
                urlFilterConfigId: pageData.key,
                filterText: pageDataFilter.filterText,
                filterOptions: pageDataFilter.filterOptions,
                urlConfigDeleted: true
            });
        }
    });
    return JSON.stringify(apiData)
}