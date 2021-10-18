use self_serve;

UPDATE `entity_template` SET `name` = 'Reset Times'
WHERE (`id` = '9');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{ \"objectTranslator\": \"function translate(values){var entities=[]; var list=[]; try{values=JSON.parse(values); list=values.data.items;}catch(e){return e.toString();} list.forEach(function(item){ entities.push({name:item.key, label: item.validatorName, secondaryLabel: item.validatorTypeLabel === \\\"Regular Expression\\\" ? \\\"RegEx\\\" : item.validatorTypeLabel}); });return JSON.stringify(entities);}\" }'
WHERE (`id` = '58');

UPDATE `node` SET `node_id` = 'accountConfig' WHERE (`id` = '21');
UPDATE `node` SET `node_id` = 'activeCards' WHERE (`id` = '22');
UPDATE `node` SET `node_id` = 'authorisedUrls' WHERE (`id` = '23');
UPDATE `node` SET `node_id` = 'maskingPatterns' WHERE (`id` = '24');
UPDATE `node` SET `node_id` = 'keywordAlerts' WHERE (`id` = '25');
UPDATE `node` SET `node_id` = 'leadConsole' WHERE (`id` = '26');
UPDATE `node` SET `node_id` = 'mailer' WHERE (`id` = '27');
UPDATE `node` SET `node_id` = 'passwordPolicy' WHERE (`id` = '28');
UPDATE `node` SET `node_id` = 'propertyConfig' WHERE (`id` = '31');
UPDATE `node` SET `node_id` = 'sessiontimeout' WHERE (`id` = '33');
UPDATE `node` SET `node_id` = 'smartResponse' WHERE (`id` = '34');

UPDATE `config` SET `value` = 'listSkill,updateSmartResponse_p0' WHERE (`code` = 'exclude_cache_fetchfor');

DELETE FROM `action_workflow` WHERE (`id` = '16');
DELETE FROM `action_workflow` WHERE (`id` = '19');

INSERT INTO `action` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `definition`, `definition_type`, `description`, `type`, `response_definition`, `configs`)
VALUES ('50', 'default.user', '2020-10-16 13:13:15.527428', 'default.user', '2021-02-26 04:07:32.029417', '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/invitemanagement/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { var uiData = JSON.parse(pageData).createQueues_p0.tabsField; var uiDataB = uiData[0]; var uiDataA = uiData[1].assistConfig; var apiData = [{ queueName : uiDataB.queueName, queueDescription : uiDataB.queueDesc, acceptanceRate : uiDataA.inviteMgmt.invites.toString(), safetyfactor : uiDataA.inviteMgmt.safetyfactor, expiryInterval : uiDataA.inviteMgmt.expiryInterval, caModelType : uiDataA.inviteMgmt.caModelType, key : uiDataA.inviteMgmt.key }]; return JSON.stringify(apiData); }\", \"preFetchDefinition\": null}', 'CREATE', 'Update Invite Management', 'REST', '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null ){ return new ApiResponse(\'SUCCESS\', \'\'); } else { return new ApiResponse(uiData.status, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}', '{}');

INSERT INTO `action_workflow` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `action_id`, `activity_template_id`)
VALUES ('55', 'default.user', '2019-08-27 17:44:15.136579', 'default.user', '2019-08-27 17:44:15.136579', '50', 'queues');

INSERT INTO `action` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `definition`, `definition_type`, `description`, `type`, `response_definition`, `configs`) 
VALUES ('51', 'default.user', '2019-08-26 13:13:15.527428', 'Sowjanya.R@247-inc.com', '2021-03-12 08:47:22.965880', '{\"type\": \"REST\", \"method\": \"GET\", \"headers\": {}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/admin/rest/ohs/get?entityId=${entity}&entityType=chatQueue\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) {  return \\\"\\\"; }\", \"preFetchDefinition\": null}', 'CREATE', 'assist queue GET action', 'REST', '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(JSON.parse(responseData).data.entity);  if(uiData != null && uiData.entityAttributes.id){ return new ApiResponse(uiData.status, uiData.entityAttributes.id); } else { return new ApiResponse(uiData.status, uiData.errorMessage); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}', '{\"POLL\": {\"type\": \"POLL\", \"retryCount\": 3000, \"retryInterval\": 30000}}');

INSERT INTO `action_workflow` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `action_id`, `activity_template_id`) 
VALUES ('56', 'default.user', '2019-08-27 17:44:15.136579', 'default.user', '2019-08-27 17:44:15.136579', '51', 'queues');

UPDATE `action_dependencies` SET `action_id` = '51' 
WHERE (`action_id` = '13') and (`dependent_action_id` = '12');

UPDATE `action_dependencies` SET `dependent_action_id` = '51' 
WHERE (`action_id` = '35') and (`dependent_action_id` = '13');

INSERT INTO `action_dependencies` (`action_id`, `dependent_action_id`) 
VALUES ('50', '35');

UPDATE `action_dependencies` SET `dependent_action_id` = '16'
WHERE (`action_id` = '41') and (`dependent_action_id` = '15');

DELETE FROM `action_dependencies` 
WHERE (`action_id` = '15') and (`dependent_action_id` = '16');


UPDATE `action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/agents/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).updateUserTest_p0; var t0 = uiData.tabsField[0]; var t1 = uiData.tabsField[1]; var t2 = uiData.tabsField[2]; var roles = []; if(t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); }else { roles.push(t0.roleId); } var apiData = {\\\"clientId\\\": \\\"nemo-client-\\\"+clientId,\\\"userName\\\": t0.userName,\\\"firstName\\\":t0.firstName,\\\"lastName\\\":t0.lastName,\\\"displayName\\\":t0.displayName,\\\"email\\\":t0.email,\\\"activeChatLimits\\\":t0.activeChatLimits,\\\"autoAcceptChats\\\":t0.autoAcceptChats,\\\"teamId\\\":t0.teamId,\\\"timezone\\\":\\\"GMT0\\\",\\\"roleIds\\\":roles,\\\"authenticationType\\\":t0.authenticationType||\\\"LOCAL_AUTH\\\",\\\"status\\\":t0.status,\\\"samlUserName\\\":\\\"\\\",\\\"key\\\":t0.key,\\\"skillLevels\\\": t1.ConfigMapper&&t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId:skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; })||[],\\\"monitoringTeamIds\\\":t2.ConfigMapper&&t2.ConfigMapper.map(function(team){return ({teamId: team.id})})||[],\\\"metadataMap\\\":{\\\"employeeId\\\":t0.employeeId}}; return JSON.stringify(apiData); }\", \"preFetchDefinition\": {}}'
 WHERE (`id` = '37');

UPDATE `action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/agents/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var uiData = JSON.parse(pageData).updateUserLive_p0; var t0 = uiData.tabsField[0]; var t1 = uiData.tabsField[1]; var t2 = uiData.tabsField[2]; var roles = []; if(t0.roleId == \\\"superlead_role_id\\\") { roles.push(\\\"lead_role_id\\\", \\\"agent_role_id\\\"); }else { roles.push(t0.roleId); } var apiData = {\\\"clientId\\\": \\\"nemo-client-\\\"+clientId,\\\"userName\\\": t0.userName,\\\"firstName\\\":t0.firstName,\\\"lastName\\\":t0.lastName,\\\"displayName\\\":t0.displayName,\\\"email\\\":t0.email,\\\"activeChatLimits\\\":t0.activeChatLimits,\\\"autoAcceptChats\\\":t0.autoAcceptChats,\\\"teamId\\\":t0.teamId,\\\"timezone\\\":\\\"GMT0\\\",\\\"roleIds\\\":roles,\\\"authenticationType\\\":t0.authenticationType||\\\"LOCAL_AUTH\\\",\\\"status\\\":t0.status,\\\"samlUserName\\\":\\\"\\\",\\\"key\\\":t0.key,\\\"skillLevels\\\": t1.ConfigMapper&&t1.ConfigMapper.map(function (skill) { var skLvl = skill ? { skillId: skill.id, skillLevelId:skill.id + \\\"-\\\" + (skill.key ? skill.key.toUpperCase() : \\\"HIGH\\\") } : []; return skLvl; })||[],\\\"monitoringTeamIds\\\":t2.ConfigMapper&&t2.ConfigMapper.map(function(team){return ({teamId: team.id})})||[],\\\"metadataMap\\\":{\\\"employeeId\\\":t0.employeeId}}; return JSON.stringify(apiData); }\", \"preFetchDefinition\": {}}' 
WHERE (`id` = '39');

--adding advance admin node back
INSERT INTO `node_group` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `title`, `ui_schema`) VALUES 
('5','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','Advanced Admin','{\"menuGroupName\": \"nav\", \"icon\":\"adminConsoleIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}');


INSERT INTO `node` (id, created_by, created_on, last_updated_by, last_updated_on, node_id, node_type, parent_id) VALUES
('5', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', '5', 'GROUP', '-1'),
('11','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','2','BOOKMARK',5),
('13','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','1','BOOKMARK',5);

INSERT INTO menu (id, created_by, created_on, last_updated_by, last_updated_on, menu_group_name, seq, node_id) 
VALUES ('5', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'nav', '1', '5');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values){values = JSON.parse(values); values.notificationStatus =  values.notificationStatus.toString();return JSON.stringify(values);}\"}' WHERE (`id` = '49');

UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/notificationconfig/update?notificationConfigObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { pageData = JSON.parse(pageData).updateMsgFilter_p0;return JSON.stringify(pageData)}\", \"preFetchDefinition\": null }' 
WHERE (`id` = '34');

UPDATE `config` SET `value` = '{\"value\": \"e2d1acc451f85921d7d6d6ba6e046d9b\" }' 
WHERE (`code` = 'cache_apikey');
