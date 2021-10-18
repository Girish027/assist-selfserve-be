function translate(pageData, entityId, clientId, accountId, env, prefetch) {
  function getSkillLevelsFromConfigMapper(configMapper, skillLevels) {
      var skillLevelMap = [];
      if (configMapper) {
          if (skillLevels && skillLevels.length > 0) {
              skillLevelMap = configMapper.map(function (skill) {
                  var skillLevelId;
                  var key = (skill.key && skillLevelKey[skill.key]) ? skillLevelKey[skill.key] : 3;
                  for (var i = 0; i < skillLevels.length; i++) {
                      var item = skillLevels[i];
                      if (item) {
                          var skillLevel = JSON.parse(item.skillLevel);
                          if (item.skillId === skill.id) {
                              for (var j = 0; j < skillLevel.length; j++) {
                                  var skills = skillLevel[j];
                                  if (skills.skillValue === key) {
                                      skillLevelId = skills.skillLevelId;
                                      break;
                                  }
                              }
                          }
                      }
                      if (skillLevelId) {
                          break;
                      }
                  }
                  var skLvl = skill ? {
                      skillId: skill.id,
                      skillLevelId: skillLevelId
                  }
                   : [];
                  return skLvl;
              })
          } else {
              /* This code is a fallback feature where skill levels are not stored in pageData - old saved activities before this fix and this workes for skill which are following the common format*/
              skillLevelMap = configMapper.map(function (skill) {
                  var skLvl = skill ? {
                      skillId: skill.id,
                      skillLevelId: skill.id + "-" + (skill.key && isValidSkillKey(skill.key) ? skill.key.toUpperCase() : "HIGH")
                  }
                   : [];
                  return skLvl;
              }) || [];
          }
      }
      return skillLevelMap;
  }
  function isValidSkillKey(skill) {
      if (skill.toUpperCase() == "HIGH" || skill.toUpperCase() == "MEDIUM" || skill.toUpperCase() == "LOW") {
          return true;
      }
      return false;
  }
  var uiData = JSON.parse(pageData).createUserTest_p0;
  var t0 = uiData.tabsField[0];
  var t1 = uiData.tabsField[1];
  var t2 = uiData.tabsField[2];
  var roles = [];
  if (t0.roleId == "superlead_role_id") {
      roles.push("lead_role_id", "agent_role_id");
  } else {
      roles.push(t0.roleId);
  }
  var skillLevels = t1.skillLevels;
  var skillLevelKey = {
      "LOW": 1,
      "MEDIUM": 2,
      "HIGH": 3
  };
  var apiData = {
      "key": "",
      "userName": t0.userName,
      "firstName": t0.firstName.trim(),
      "lastName": t0.lastName.trim(),
      "displayName": t0.displayName.trim(),
      "email": t0.email,
      "activeChatLimits": t0.activeChatLimits,
      "autoAcceptChats": t0.autoAcceptChats,
      "teamId": t0.teamId,
      "timezone": "GMT0",
      "roleIds": roles,
      "authenticationType": t0.authenticationType || "LOCAL_AUTH",
      "status": t0.status,
      "samlUserName": t0.authenticationType == "REMOTE_AUTH" ? (t0.samlUserName || "") : "",
      "skillLevels": getSkillLevelsFromConfigMapper(t1.ConfigMapper, skillLevels),
      "monitoringTeamIds": t2.ConfigMapper && t2.ConfigMapper.map(function (team) {
          return ({
              teamId: team.id
          })
      }) || [],
      "metadataMap": {
          "employeeId": t0.employeeId
      }
  };
  if (t0.password !== undefined && t0.password.newPassword !== "" && t0.authenticationType === "LOCAL_AUTH" && t0.setPassword === "oneTime") {
      apiData.newPassword = t0.password.newPassword;
      apiData.confirmPassword = t0.password.confirmPassword;
  }
  return JSON.stringify(apiData);
}
