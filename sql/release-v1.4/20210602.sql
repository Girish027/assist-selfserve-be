/* CENTRAL-7565: Rename Property Configurations fields */
use `self_serve`;
UPDATE `activity_template` SET `description` = 'Context Validation', `title` = 'Context Validation'
WHERE (`id` = 'propertyValidation');

UPDATE `activity_template` SET `description` = 'Customer Context', `title` = 'Customer Context', `ui_schema` = '{ \"menuGroupName\": \"nav\", \"icon\": \"\", \"toolTip\": \"\", \"dashboard\": true, \"display_order\": 1, \"isActive\": true, \"uiOptions\": { \"entityList\": { \"entityHeader\": { \"label\": \"Name\", \"secondaryLabel\": \"Context Panel\" } } } }'
WHERE (`id` = 'propertyConfig');


/* Password field validation changes */

-- modify password

UPDATE `self_serve`.`action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Referer\": \"https://test-default.portal.assist.staging.247-inc.net/en/console\", \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/${componentClientId}/accounts/${componentAccountId}/users/${entity}/password/modify?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).tabsField[0]; var apiData = { \\\"clientId\\\": \\\"nemo-client\\\" + clientId, \\\"key\\\": uiData.key, \\\"firstName\\\": uiData.firstName, \\\"userName\\\": uiData.userName, \\\"lastName\\\": uiData.lastName, \\\"displayName\\\": uiData.displayName, \\\"screenName\\\":uiData.displayName, \\\"email\\\": uiData.email, \\\"activeChatLimits\\\": uiData.activeChatLimits, \\\"autoAcceptChats\\\":uiData.autoAcceptChats, \\\"status\\\": uiData.status, \\\"teamId\\\": uiData.teamId, \\\"newPassword\\\": uiData.password.newPassword, \\\"confirmPassword\\\": uiData.password.confirmPassword, \\\"roleIds\\\": [ uiData.roleId ], \\\"skillLevels\\\": [], \\\"monitoringTeamIds\\\": [], \\\"authenticationType\\\": uiData.authenticationType, \\\"corruptUserMessage\\\": \\\"\\\", \\\"samlUserName\\\": \\\"\\\", \\\"metadataMap\\\": { \\\"employeeId\\\": uiData.employeeId } }; return JSON.stringify(apiData); }\", \"preFetchDefinition\": null, \"prefetchRequiredForLiveOnly\": false}'
WHERE (`id` = '49');

-- userTest create

UPDATE `self_serve`.`action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Referer\": \"https://test-default.portal.assist.staging.247-inc.net/en/console\", \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/${componentClientId}/accounts/${componentAccountId}/agents?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).createUserTest_p0; var t0 = uiData.tabsField[ 0 ]; var t1 = uiData.tabsField[ 1 ]; var t2 = uiData.tabsField[ 2 ]; var roles = []; if (t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); } else { roles.push(t0.roleId); } var apiData = { \\\"key\\\": \\\"\\\", \\\"userName\\\": t0.userName, \\\"firstName\\\": t0.firstName, \\\"lastName\\\": t0.lastName, \\\"displayName\\\": t0.displayName, \\\"email\\\": t0.email, \\\"activeChatLimits\\\": t0.activeChatLimits, \\\"autoAcceptChats\\\": t0.autoAcceptChats, \\\"teamId\\\": t0.teamId, \\\"timezone\\\": \\\"GMT0\\\", \\\"roleIds\\\": roles, \\\"authenticationType\\\": t0.authenticationType || \\\"LOCAL_AUTH\\\", \\\"status\\\": t0.status, \\\"samlUserName\\\": \\\"\\\", \\\"skillLevels\\\": t1.ConfigMapper && t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId: skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; }) || [], \\\"monitoringTeamIds\\\": t2.ConfigMapper && t2.ConfigMapper.map(function (team) { return ({ teamId: team.id }) }) || [], \\\"metadataMap\\\": { \\\"employeeId\\\": t0.employeeId } }; if (t0.password.newPassword !== \\\"\\\") { apiData.newPassword = t0.password.newPassword; apiData.confirmPassword = t0.password.confirmPassword; } return JSON.stringify(apiData); }\", \"preFetchDefinition\": {}}'
WHERE (`id` = '36');

-- userLive create

UPDATE `self_serve`.`action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Referer\": \"https://test-default.portal.assist.staging.247-inc.net/en/console\", \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/${componentClientId}/accounts/${componentAccountId}/agents?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).createUserLive_p0; var t0 = uiData.tabsField[ 0 ]; var t1 = uiData.tabsField[ 1 ]; var t2 = uiData.tabsField[ 2 ]; var roles = []; if (t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); } else { roles.push(t0.roleId); } var apiData = { \\\"key\\\": \\\"\\\", \\\"userName\\\": t0.userName, \\\"firstName\\\": t0.firstName, \\\"lastName\\\": t0.lastName, \\\"displayName\\\": t0.displayName, \\\"email\\\": t0.email, \\\"activeChatLimits\\\": t0.activeChatLimits, \\\"autoAcceptChats\\\": t0.autoAcceptChats, \\\"teamId\\\": t0.teamId, \\\"timezone\\\": \\\"GMT0\\\", \\\"roleIds\\\": roles, \\\"authenticationType\\\": t0.authenticationType || \\\"LOCAL_AUTH\\\", \\\"status\\\": t0.status, \\\"samlUserName\\\": \\\"\\\", \\\"skillLevels\\\": t1.ConfigMapper && t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId: skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; }) || [], \\\"monitoringTeamIds\\\": t2.ConfigMapper && t2.ConfigMapper.map(function (team) { return ({ teamId: team.id }) }) || [], \\\"metadataMap\\\": { \\\"employeeId\\\": t0.employeeId } }; if (t0.password.newPassword !== \\\"\\\") { apiData.newPassword = t0.password.newPassword; apiData.confirmPassword = t0.password.confirmPassword; } return JSON.stringify(apiData); }\", \"preFetchDefinition\": {}}'
WHERE (`id` = '38');