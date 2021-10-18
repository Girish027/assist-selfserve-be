-- update agents fetch and action with saml username

use self_serve;

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(apiData) { var uiData = { \\\"tabsField\\\": [{}, {}, {}] }; var u = JSON.parse(apiData).items; var t0 = uiData.tabsField[0]; t0.key = u.key; t0.userName = u.userName; t0.employeeId = u.metadataMap && u.metadataMap.employeeId; t0.firstName = u.firstName; t0.lastName = u.lastName; t0.displayName = u.displayName; t0.email = u.email; t0.activeChatLimits = u.activeChatLimits != undefined ? u.activeChatLimits.toString() : 1; t0.autoAcceptChats = u.autoAcceptChats != undefined ? u.autoAcceptChats.toString() : \\\"true\\\"; t0.teamId = u.teamId; if (u.roleIds.length > 1) { t0.roleId = \\\"superlead_role_id\\\"; } else { t0.roleId = u.roleIds.toString(); } t0.authenticationType = u.authenticationType; if (u.authenticationType == \\\"REMOTE_AUTH\\\") { t0.samlUserName = u.samlUserName; } t0.status = u.status; var t1 = uiData.tabsField[1]; t1.ConfigMapper = {}; t1.ConfigMapper = u.skillLevels.map(function (sklvl) { var keywords = sklvl.skillLevelId.split(\\\"-\\\"); var key = keywords[keywords.length - 1].toUpperCase(); return ({ id: sklvl.skillId, name: sklvl.skillId, key: key }) }); var t2 = uiData.tabsField[2]; t2.ConfigMapper = {}; t2.ConfigMapper = u.monitoringTeamIds ? u.monitoringTeamIds.map(function (tId) { return ({ id: tId.teamId, name: tId.teamId, key: \\\"\\\" }) }) : []; return JSON.stringify(uiData); } \"}'
WHERE (`id` = '50');
UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(apiData) { var uiData = { \\\"tabsField\\\": [{}, {}, {}] }; var u = JSON.parse(apiData).items; var t0 = uiData.tabsField[0]; t0.key = u.key; t0.userName = u.userName; t0.employeeId = u.metadataMap && u.metadataMap.employeeId; t0.firstName = u.firstName; t0.lastName = u.lastName; t0.displayName = u.displayName; t0.email = u.email; t0.activeChatLimits = u.activeChatLimits != undefined ? u.activeChatLimits.toString() : 1; t0.autoAcceptChats = u.autoAcceptChats != undefined ? u.autoAcceptChats.toString() : \\\"true\\\"; t0.teamId = u.teamId; if (u.roleIds.length > 1) { t0.roleId = \\\"superlead_role_id\\\"; } else { t0.roleId = u.roleIds.toString(); } t0.authenticationType = u.authenticationType; if (u.authenticationType == \\\"REMOTE_AUTH\\\") { t0.samlUserName = u.samlUserName; } t0.status = u.status; var t1 = uiData.tabsField[1]; t1.ConfigMapper = {}; t1.ConfigMapper = u.skillLevels.map(function (sklvl) { var keywords = sklvl.skillLevelId.split(\\\"-\\\"); var key = keywords[keywords.length - 1].toUpperCase(); return ({ id: sklvl.skillId, name: sklvl.skillId, key: key }) }); var t2 = uiData.tabsField[2]; t2.ConfigMapper = {}; t2.ConfigMapper = u.monitoringTeamIds ? u.monitoringTeamIds.map(function (tId) { return ({ id: tId.teamId, name: tId.teamId, key: \\\"\\\" }) }) : []; return JSON.stringify(uiData); } \"}'
WHERE (`id` = '51');

UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/agents/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).updateUserTest_p0; var t0 = uiData.tabsField[0]; var t1 = uiData.tabsField[1]; var t2 = uiData.tabsField[2]; var roles = []; if (t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); } else { roles.push(t0.roleId); } var apiData = { \\\"clientId\\\": \\\"nemo-client-\\\" + clientId, \\\"userName\\\": t0.userName, \\\"firstName\\\": t0.firstName, \\\"lastName\\\": t0.lastName, \\\"displayName\\\": t0.displayName, \\\"email\\\": t0.email, \\\"activeChatLimits\\\": t0.activeChatLimits, \\\"autoAcceptChats\\\": t0.autoAcceptChats, \\\"teamId\\\": t0.teamId, \\\"timezone\\\": \\\"GMT0\\\", \\\"roleIds\\\": roles, \\\"authenticationType\\\": t0.authenticationType || \\\"LOCAL_AUTH\\\", \\\"status\\\": t0.status, \\\"samlUserName\\\": t0.authenticationType == \\\"REMOTE_AUTH\\\" ? t0.samlUserName : \\\"\\\", \\\"key\\\": t0.key, \\\"skillLevels\\\": t1.ConfigMapper && t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId: skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; }) || [], \\\"monitoringTeamIds\\\": t2.ConfigMapper && t2.ConfigMapper.map(function (team) { return ({ teamId: team.id }) }) || [], \\\"metadataMap\\\": { \\\"employeeId\\\": t0.employeeId } }; return JSON.stringify(apiData); }\", \"preFetchDefinition\": {} }'
WHERE (`id` = '37');
UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/agents/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).updateUserLive_p0; var t0 = uiData.tabsField[0]; var t1 = uiData.tabsField[1]; var t2 = uiData.tabsField[2]; var roles = []; if (t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); } else { roles.push(t0.roleId); } var apiData = { \\\"clientId\\\": \\\"nemo-client-\\\" + clientId, \\\"userName\\\": t0.userName, \\\"firstName\\\": t0.firstName, \\\"lastName\\\": t0.lastName, \\\"displayName\\\": t0.displayName, \\\"email\\\": t0.email, \\\"activeChatLimits\\\": t0.activeChatLimits, \\\"autoAcceptChats\\\": t0.autoAcceptChats, \\\"teamId\\\": t0.teamId, \\\"timezone\\\": \\\"GMT0\\\", \\\"roleIds\\\": roles, \\\"authenticationType\\\": t0.authenticationType || \\\"LOCAL_AUTH\\\", \\\"status\\\": t0.status, \\\"samlUserName\\\": t0.authenticationType == \\\"REMOTE_AUTH\\\" ? t0.samlUserName : \\\"\\\", \\\"key\\\": t0.key, \\\"skillLevels\\\": t1.ConfigMapper && t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId: skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; }) || [], \\\"monitoringTeamIds\\\": t2.ConfigMapper && t2.ConfigMapper.map(function (team) { return ({ teamId: team.id }) }) || [], \\\"metadataMap\\\": { \\\"employeeId\\\": t0.employeeId } }; return JSON.stringify(apiData); }\", \"preFetchDefinition\": {} }'
WHERE (`id` = '39');

UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"Referer\": \"https://test-default.portal.assist.staging.247-inc.net/en/console\", \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/agents?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).createUserTest_p0; var t0 = uiData.tabsField[0]; var t1 = uiData.tabsField[1]; var t2 = uiData.tabsField[2]; var roles = []; if (t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); } else { roles.push(t0.roleId); } var apiData = { \\\"key\\\": \\\"\\\", \\\"userName\\\": t0.userName, \\\"firstName\\\": t0.firstName, \\\"lastName\\\": t0.lastName, \\\"displayName\\\": t0.displayName, \\\"email\\\": t0.email, \\\"activeChatLimits\\\": t0.activeChatLimits, \\\"autoAcceptChats\\\": t0.autoAcceptChats, \\\"teamId\\\": t0.teamId, \\\"timezone\\\": \\\"GMT0\\\", \\\"roleIds\\\": roles, \\\"authenticationType\\\": t0.authenticationType || \\\"LOCAL_AUTH\\\", \\\"status\\\": t0.status, \\\"samlUserName\\\": t0.authenticationType == \\\"REMOTE_AUTH\\\" ? t0.samlUserName : \\\"\\\", \\\"skillLevels\\\": t1.ConfigMapper && t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId: skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; }) || [], \\\"monitoringTeamIds\\\": t2.ConfigMapper && t2.ConfigMapper.map(function (team) { return ({ teamId: team.id }) }) || [], \\\"metadataMap\\\": { \\\"employeeId\\\": t0.employeeId } }; if (t0.newPassword !== \\\"\\\") { apiData.newPassword = t0.newPassword; apiData.confirmPassword = t0.confirmPassword; } return JSON.stringify(apiData); }\", \"preFetchDefinition\": {} }'
WHERE (`id` = '36');
UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"Referer\": \"https://test-default.portal.assist.staging.247-inc.net/en/console\", \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/agents?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).createUserLive_p0; var t0 = uiData.tabsField[0]; var t1 = uiData.tabsField[1]; var t2 = uiData.tabsField[2]; var roles = []; if (t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); } else { roles.push(t0.roleId); } var apiData = { \\\"key\\\": \\\"\\\", \\\"userName\\\": t0.userName, \\\"firstName\\\": t0.firstName, \\\"lastName\\\": t0.lastName, \\\"displayName\\\": t0.displayName, \\\"email\\\": t0.email, \\\"activeChatLimits\\\": t0.activeChatLimits, \\\"autoAcceptChats\\\": t0.autoAcceptChats, \\\"teamId\\\": t0.teamId, \\\"timezone\\\": \\\"GMT0\\\", \\\"roleIds\\\": roles, \\\"authenticationType\\\": t0.authenticationType || \\\"LOCAL_AUTH\\\", \\\"status\\\": t0.status, \\\"samlUserName\\\": t0.authenticationType == \\\"REMOTE_AUTH\\\" ? t0.samlUserName : \\\"\\\", \\\"skillLevels\\\": t1.ConfigMapper && t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId: skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; }) || [], \\\"monitoringTeamIds\\\": t2.ConfigMapper && t2.ConfigMapper.map(function (team) { return ({ teamId: team.id }) }) || [], \\\"metadataMap\\\": { \\\"employeeId\\\": t0.employeeId } }; if (t0.newPassword !== \\\"\\\") { apiData.newPassword = t0.newPassword; apiData.confirmPassword = t0.confirmPassword; } return JSON.stringify(apiData); }\", \"preFetchDefinition\": {} }'
WHERE (`id` = '38');