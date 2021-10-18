function translate(apiData) {
  try {
    var uiData = {
      "tabsField": [
        {
          "activeChatLimits": "1",
          "authenticationType": "LOCAL_AUTH",
          "autoAcceptChats": "true",
          "roleId": "agent_role_id",
          "status": "ACTIVE"
        },
        {},
        {}
      ]
    };
    var values = JSON.parse(apiData);
    var list = JSON.parse(values.data.list);
    var t1 = uiData.tabsField[1];
    var data = [];
    list.forEach(function (items) {
      var skillLevels = {
        skillId: items.entityBaseData.entityId,
        skillLevel: items.entityAttributes.skillLevel
      };
      data.push(skillLevels);
    });
    t1.skillLevels = data;
    return JSON.stringify(uiData);
  }
  catch (e) { }
  return null;
}
