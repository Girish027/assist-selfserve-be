function translate(apiData) {
  try {
    var val = JSON.parse(JSON.parse(apiData).data.entity);
    var configMapper = JSON.parse(val.entityAttributes.users);
    var skillLevel = JSON.parse(val.entityAttributes.skillLevel);
    var users = [];
    var key;
    var skillLevelKey = { "1": "LOW", "2": "MEDIUM", "3": "HIGH" };
    users = configMapper.map(function (item) {
      var userSkillValue;
      var skillLevelId;
      skillLevel.forEach(function (levelId) {
        if (levelId.skillLevelId === item.skillLevelId) {
          skillLevelId = levelId.skillLevelId;
          userSkillValue = parseInt(levelId.skillValue);
          key = skillLevelKey[userSkillValue];
        }
      });
      return ({ id: item.userId, name: item.userId, key: key, skillLevelId: skillLevelId, skillValue: userSkillValue })
    });
    var uiData = {
      "skillId": val.entityBaseData.entityId,
      "skillName": val.entityBaseData.entityDisplayName,
      "skillDesc": val.entityAttributes.skillDesc,
      "ConfigMapper": users,
      "skillLevels": skillLevel
    };
    return JSON.stringify(uiData);
  }
  catch (e) { }
  return null;
}
