function translate(values) {
    values = JSON.parse(JSON.parse(values).data.entity);
    var uiData = {};
    uiData.tagId = values.entityBaseData.entityId;
    uiData.tagName = values.entityAttributes.tagName;
    uiData.tagType = values.entityAttributes.tagType;
    return JSON.stringify(uiData);
}