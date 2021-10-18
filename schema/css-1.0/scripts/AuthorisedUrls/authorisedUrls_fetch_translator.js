function translate(apiData) {
    values = JSON.parse(apiData).data.items[0];
    items = values.filterConfig;
    filtersList = [];
    items.forEach(function (item) {
        if (item.urlConfigDeleted) {
            filtersList.push({
                urlFilterConfigId: item.urlFilterConfigId,
                filterText: item.filterText,
                filterOptions: item.filterOptions,
                urlConfigDeleted: false
            });
        }
    });
    var uiData = {
        "key": values.key,
        "accountId": values.accountId,
        "scopeType": values.scopeType,
        "scopeValue": values.scopeValue,
        "enabled": values.enabled.toString(),
        "filterType": values.urlFilterListingType,
        "filterConfig": filtersList
    };
    return JSON.stringify(uiData);
}