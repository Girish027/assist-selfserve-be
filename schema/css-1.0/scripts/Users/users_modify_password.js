function translate(pageData, entityId, clientId, accountId, env, prefetch) {
  var uiData = JSON.parse(pageData).tabsField[0];
  var apiData = {
    "clientId": "nemo-client-" + clientId,
    "key": uiData.key,
    "firstName": uiData.firstName,
    "userName": uiData.userName,
    "lastName": uiData.lastName,
    "displayName": uiData.displayName,
    "screenName": uiData.displayName,
    "email": uiData.email,
    "activeChatLimits": uiData.activeChatLimits,
    "autoAcceptChats": uiData.autoAcceptChats,
    "status": uiData.status,
    "teamId": uiData.teamId,
    "newPassword": uiData.password.newPassword,
    "confirmPassword": uiData.password.confirmPassword,
    "roleIds": [uiData.roleId],
    "skillLevels": [],
    "monitoringTeamIds": [],
    "authenticationType": uiData.authenticationType,
    "corruptUserMessage": "",
    "samlUserName": "",
    "metadataMap": { employeeId: uiData.employeeId }
  };
  return JSON.stringify(apiData);
}
