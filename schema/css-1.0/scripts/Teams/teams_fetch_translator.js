function translate(values) {
    values = JSON.parse(JSON.parse(values).data.entity);
    var uiData = {};
    uiData.teamId = values.entityBaseData.entityId;
    uiData.teamName = values.entityBaseData.entityDisplayName;
    var tags = values.entityAttributes.tags;
    uiData.teamTags = tags.length > 0 ? values.entityAttributes.tags.split(",") : [];
    return JSON.stringify(uiData);
}