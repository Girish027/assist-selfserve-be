function translate(values, entity) {
    values = JSON.parse(values).data.items[entity];
    var uiData = {
        "id": values.smartResponseId,
        "scope": values.scope.toLowerCase(),
        "scopeId": values.scopeId,
        "tag": values.tags,
        "responseText": values.smartresponseText,
    }; return JSON.stringify(uiData);
}