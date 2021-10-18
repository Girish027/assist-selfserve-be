function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap, componentClientId, componentAccountId) {
  var uiData = JSON.parse(pageData).updateSkill_p0;
  var apiData = [{
    "entityBaseData": {
      "entityDisplayName": uiData.skillName,
      "entityId": uiData.skillId,
      "accountId": componentAccountId,
      "clientId": componentClientId,
      "entityType": "skill"
    },
    "entityAttributes": {
      "skillName": uiData.skillName,
      "skillDesc": uiData.skillDesc,
      "account": componentAccountId,
      "users": "[]"
    }
  }];
  return JSON.stringify(apiData);
}
