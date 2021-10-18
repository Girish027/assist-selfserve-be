function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
  try {
    var uiData = JSON.parse(pageData).STATest_p0;
    var ConfigMapper = uiData.ConfigMapper;
    var skillLevels = uiData.skillLevels;
    var users = [];
    var skillLevelKey = { "LOW": 1, "MEDIUM": 2, "HIGH": 3 };
    var key; users = ConfigMapper.map(function (item) {
      key = item.key ? skillLevelKey[item.key] : 3;
      var skillLevelId;
      skillLevels.forEach(function (skillLevel) {
        if (skillLevel.skillValue === key) {
          skillLevelId = skillLevel.skillLevelId;
        }
      });
      var skillUser = item ? { userId: item.id, skillLevelId: skillLevelId } : null;
      return skillUser;
    });
    var apiData = [
      {
        "entityBaseData": {
          "entityId": uiData.skillId,
          "accountId": componentAccountId,
          "clientId": componentClientId,
          "entityType": "skill"
        },
        "entityAttributes": {
          "account": componentAccountId,
          "skillName": uiData.skillName,
          "skillDesc": uiData.skillDesc,
          "users": JSON.stringify(users)
        }
      }
    ];
    return JSON.stringify(apiData);
  }
  catch (e) { }
  return null;
}
