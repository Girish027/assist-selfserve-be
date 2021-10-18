function translate(apiData, entity, context, auxResponse) {
  var uiData = { "tabsField": [{}, {}, {}] };
  var u = JSON.parse(apiData).items;
  var auxResp = JSON.parse(auxResponse);
  var t0 = uiData.tabsField[0];
  t0.key = u.key;
  t0.userName = u.userName;
  t0.employeeId = u.metadataMap && u.metadataMap.employeeId;
  t0.firstName = u.firstName;
  t0.lastName = u.lastName;
  t0.displayName = u.displayName;
  t0.email = u.email;
  t0.activeChatLimits = u.activeChatLimits != undefined ? u.activeChatLimits.toString() : 1;
  t0.autoAcceptChats = u.autoAcceptChats != undefined ? u.autoAcceptChats.toString() : "true";
  t0.teamId = u.teamId;
  if (u.roleIds.length > 1) {
    t0.roleId = "superlead_role_id";
  } else {
    t0.roleId = u.roleIds.toString();
  }
  t0.authenticationType = u.authenticationType;
  if (u.authenticationType == "REMOTE_AUTH") {
    t0.samlUserName = u.samlUserName !== null ? u.samlUserName : "";
  }
  t0.status = u.status;
  var t1 = uiData.tabsField[1];
  t1.ConfigMapper = {};
  t1.ConfigMapper = u.skillLevels.map(function (sklvl) {
    var key;
    var skillLevelKey = { "1": "LOW", "2": "MEDIUM", "3": "HIGH" };
    for(var i = 0; i< auxResp.length ; i++) {
      var item = auxResp[i];
      if(item){
        var skillLevel = JSON.parse(item.skillLevel);
        if (sklvl.skillId === item.skillId) {
          for(var j = 0; j< skillLevel.length ; j++) {
            var skills = skillLevel[j];
            if (skills.skillLevelId === sklvl.skillLevelId) {
              key = skills.skillValue;
              break;
            }
          }
        }
      }
      if(key){
        break;
      }
    }
    return ({ id: sklvl.skillId, name: sklvl.skillId, key: skillLevelKey[key] })
  });
  var t2 = uiData.tabsField[2];
  t2.ConfigMapper = {};
  t2.ConfigMapper = u.monitoringTeamIds ? u.monitoringTeamIds.map(function (tId) {
    return ({ id: tId.teamId, name: tId.teamId, key: "" })
  }) : [];
  return JSON.stringify(uiData);
}
