function translate(apiData) {
    var val = JSON.parse(JSON.parse(apiData).data.entity);
    var configMapper = JSON.parse(val.entityAttributes.users);
    var users = [];
    users = configMapper.map(function (item) {
        var keywords = item.skillLevelId.split("-");
        var key = keywords[keywords.length - 1].toLowerCase();
        return ({
            id: item.userId, name: item.userId, key: key
        })
    });
    var uiData = {
        skillName: val.entityBaseData.entityDisplayName,
        skillId: val.entityBaseData.entityId,
        skillDesc: val.entityAttributes.skillDesc,
        ConfigMapper: users
    };
    return JSON.stringify(uiData)
}
