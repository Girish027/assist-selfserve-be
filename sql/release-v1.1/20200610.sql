USE self_serve;
/*Update page template for skills*/
UPDATE page_template SET definition = '{ \"schema\": { \"type\": \"object\", \"properties\": { \"skillId\": { \"type\": \"string\", \"title\": \"Skill Id\", \"default\": \"\" }, \"skillName\": { \"type\": \"string\", \"title\": \"Skill Name\", \"default\": \"\" }, \"skillDesc\": { \"type\": \"string\", \"title\": \"Skill Description\", \"default\": \"\" } } }, \"uiSchema\": { \"skillId\": { \"ui:field\": \"plaintext\", \"ui:options\": { \"fieldSize\": [ 6, 8 ] } }, \"skillName\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"fieldSize\": [ 6, 8 ] } }, \"skillDesc\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"fieldSize\": [ 6, 8 ] } } }, \"viewUiSchema\": { \"skillId\": { \"ui:field\": \"plaintext\", \"ui:options\": { \"isDisabled\": true, \"fieldSize\": [ 6, 8 ] } }, \"skillName\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": true, \"fieldSize\": [ 6, 8 ] } }, \"skillDesc\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": true, \"fieldSize\": [ 6, 8 ] } } }, \"fetch\": { \"page\": \"updateSkill_p0\" } }' WHERE id = 'updateSkill_p0';
/*Skill to agents TEST and LIVE activities*/
INSERT INTO activity_template (id, created_by, created_on, last_updated_by, last_updated_on, description, entity_location, model, promotion_approval, title, type, ui_schema, entity_template_id, configs) VALUES ('STALive', 'default.user', '2019-08-07 13:50:29.862585', 'default.user', '2019-08-07 13:50:29.862585', 'Skill to Agents Live', '{\"UPDATE\": \"STALive_p0.skillId\"}', 'MENU', 1, 'Live - Skill to Agents', 'NON_SINGLETON', '{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}', '5', '{\"publishType\": \"LIVE_ONLY\"}');
INSERT INTO activity_template (id, created_by, created_on, last_updated_by, last_updated_on, description, entity_location, model, promotion_approval, title, type, ui_schema, entity_template_id, configs) VALUES ('STATest', 'default.user', '2019-08-07 13:50:29.862585', 'default.user', '2019-08-07 13:50:29.862585', 'Skill to Agents Test', '{\"UPDATE\": \"STATest_p0.skillId\"}', 'MENU', 1, 'Test - Skill to Agents', 'NON_SINGLETON', '{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}', '5', '{\"publishType\": \"TEST_ONLY\"}');
INSERT INTO page_template (id, created_by, created_on, last_updated_by, last_updated_on, definition, description, title, type) VALUES ('STALive_p0', 'default.user', '2019-08-07 14:06:42.713766', 'Gangeyula.Chandra@247.ai', '2020-06-12 21:12:43.442175', '{\"schema\":{\"type\":\"object\",\"properties\":{\"skillId\":{\"type\":\"string\",\"title\":\"Skill ID\"},\"ConfigMapper\":{\"type\":\"array\",\"title\":\"skill selector\",\"items\":{\"users\":{\"type\":\"array\",\"items\":{\"enum\":[],\"type\":\"string\",\"enumNames\":[]},\"default\":\"\"},\"key\":{\"type\":\"array\",\"items\":{\"enum\":[\"low\",\"medium\",\"high\"],\"type\":\"string\",\"title\":\"\",\"enumNames\":[\"low\",\"medium\",\"high\"]}}}}}},\"uiSchema\":{\"skillId\":{\"ui:field\":\"plaintext\",\"ui:options\":{\"fieldSize\":[6,8]}},\"ConfigMapper\":{\"ui:field\":\"entitymapper\",\"ui:options\":{\"baseListDataPath\":\"users\",\"baseKeysDataPath\":\"key\",\"readOnly\":false,\"unassignedListLabel\":\"CONFIG_MAPPER_LABEL_UNASSIGNED\",\"assignedListLabel\":\"CONFIG_MAPPER_LABEL_ASSIGNED\",\"keyListLabel\":\"CONFIG_MAPPER_LABEL_SKILL_LEVEL\",\"fieldSize\":[3,5]}}},\"viewUiSchema\":{\"skillId\":{\"ui:field\":\"plaintext\",\"ui:options\":{\"isDisabled\":true,\"fieldSize\":[6,8]}},\"ConfigMapper\":{\"ui:field\":\"entitymapper\",\"ui:options\":{\"isDisabled\":true,\"baseListDataPath\":\"users\",\"baseKeysDataPath\":\"key\",\"readOnly\":true,\"unassignedListLabel\":\"CONFIG_MAPPER_LABEL_UNASSIGNED\",\"assignedListLabel\":\"CONFIG_MAPPER_LABEL_ASSIGNED\",\"keyListLabel\":\"CONFIG_MAPPER_LABEL_SKILL_LEVEL\",\"fieldSize\":[3,5]}}},\"fetch\":{\"list\":{\"listUser\":\"ConfigMapper.users\"},\"page\":\"STALive_p0\"}}', 'skill to Agents Live', 'Skill to Agents Live', 'UPDATE');
INSERT INTO page_template (id, created_by, created_on, last_updated_by, last_updated_on, definition, description, title, type) VALUES ('STATest_p0', 'default.user', '2019-08-07 14:06:42.713766', 'Gangeyula.Chandra@247.ai', '2020-06-12 21:12:34.689193', '{\"schema\":{\"type\":\"object\",\"properties\":{\"skillId\":{\"type\":\"string\",\"title\":\"Skill ID\"},\"ConfigMapper\":{\"type\":\"array\",\"title\":\"skill selector\",\"items\":{\"users\":{\"type\":\"array\",\"items\":{\"enum\":[],\"type\":\"string\",\"enumNames\":[]},\"default\":\"\"},\"key\":{\"type\":\"array\",\"items\":{\"enum\":[\"low\",\"medium\",\"high\"],\"type\":\"string\",\"title\":\"\",\"enumNames\":[\"low\",\"medium\",\"high\"]}}}}}},\"uiSchema\":{\"skillId\":{\"ui:field\":\"plaintext\",\"ui:options\":{\"fieldSize\":[6,8]}},\"ConfigMapper\":{\"ui:field\":\"entitymapper\",\"ui:options\":{\"baseListDataPath\":\"users\",\"baseKeysDataPath\":\"key\",\"readOnly\":false,\"unassignedListLabel\":\"CONFIG_MAPPER_LABEL_UNASSIGNED\",\"assignedListLabel\":\"CONFIG_MAPPER_LABEL_ASSIGNED\",\"keyListLabel\":\"CONFIG_MAPPER_LABEL_SKILL_LEVEL\",\"fieldSize\":[3,5]}}},\"viewUiSchema\":{\"skillId\":{\"ui:field\":\"plaintext\",\"ui:options\":{\"isDisabled\":true,\"fieldSize\":[6,8]}},\"ConfigMapper\":{\"ui:field\":\"entitymapper\",\"ui:options\":{\"isDisabled\":true,\"baseListDataPath\":\"users\",\"baseKeysDataPath\":\"key\",\"readOnly\":true,\"unassignedListLabel\":\"CONFIG_MAPPER_LABEL_UNASSIGNED\",\"assignedListLabel\":\"CONFIG_MAPPER_LABEL_ASSIGNED\",\"keyListLabel\":\"CONFIG_MAPPER_LABEL_SKILL_LEVEL\",\"fieldSize\":[3,5]}}},\"fetch\":{\"list\":{\"listUser\":\"ConfigMapper.users\"},\"page\":\"STATest_p0\"}}', 'skill to Agents Test', 'Skill to Agents Test', 'UPDATE');
INSERT INTO activity_page (id, created_by, created_on, last_updated_by, last_updated_on, page_order, section_name, page_template_id, activity_template_id) VALUES ('12', 'default.user', '2019-08-08 14:24:45.621475', 'default.user', '2019-08-08 14:24:45.621475', '1', '', 'STATest_p0', 'STATest');
INSERT INTO activity_page (id, created_by, created_on, last_updated_by, last_updated_on, page_order, section_name, page_template_id, activity_template_id) VALUES ('13', 'default.user', '2019-08-08 14:24:45.621475', 'default.user', '2019-08-08 14:24:45.621475', '1', '', 'STALive_p0', 'STALive');
INSERT INTO fetch_config_template (id, created_by, created_on, last_updated_by, last_updated_on, fetch_for, headers, params, relativeurl, resp_api_to_resp_ui, translator_type, type, service_id) VALUES ('24', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'STATest_p0', '{}', '{\"contexts\": [\"entity\"], \"constants\": {}}', '/en/admin/rest/ohs/get?entityId=${entity}&entityType=skill', '{\"objectTranslator\": \"function translate(apiData) { var val = JSON.parse(JSON.parse(apiData).data.entity); var ebd = val.entityBaseData; var ea = val.entityAttributes; var users = JSON.parse(ea.users); ConfigMapper = users.map(function (item) { var skillLevelId = item.skillLevelId.split(\\"-\\"); var userId = item.userId.split(\\"-\\"); return ({ id: item.userId, name: userId[userId.length - 1 ], key: skillLevelId[skillLevelId.length - 1 ].toLowerCase() }); }); var uiData = { \\\"skillId\\\": ebd.entityId, \\\"skillName\\\": ea.skillName, \\\"skillDesc\\\": ea.skillDesc, \\\"ConfigMapper\\\": ConfigMapper }; return JSON.stringify(uiData); }\"}', 'JS', 'PAGE_DATA_VALUE', 'assist');
INSERT INTO fetch_config_template (id, created_by, created_on, last_updated_by, last_updated_on, fetch_for, headers, params, relativeurl, resp_api_to_resp_ui, translator_type, type, service_id) VALUES ('25', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'STALive_p0', '{}', '{\"contexts\": [\"entity\"], \"constants\": {}}', '/en/admin/rest/ohs/get?entityId=${entity}&entityType=skill', '{\"objectTranslator\": \"function translate(apiData) { var val = JSON.parse(JSON.parse(apiData).data.entity); var ebd = val.entityBaseData; var ea = val.entityAttributes; var users = JSON.parse(ea.users); ConfigMapper = users.map(function (item) { var skillLevelId = item.skillLevelId.split(\\"-\\"); var userId = item.userId.split(\\"-\\"); return ({ id: item.userId, name: userId[userId.length - 1 ], key: skillLevelId[skillLevelId.length - 1 ].toLowerCase() }); }); var uiData = { \\\"skillId\\\": ebd.entityId, \\\"skillName\\\": ea.skillName, \\\"skillDesc\\\": ea.skillDesc, \\\"ConfigMapper\\\": ConfigMapper }; return JSON.stringify(uiData); }\"}', 'JS', 'PAGE_DATA_VALUE', 'assist');
INSERT INTO node (id, created_by, created_on, last_updated_by, last_updated_on, node_id, node_type, parent_id) VALUES ('19', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'STATest', 'WORKFLOW', '3');
INSERT INTO node (id, created_by, created_on, last_updated_by, last_updated_on, node_id, node_type, parent_id) VALUES ('20', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'STALive', 'WORKFLOW', '3');
/*update actions for skill to agents activities*/
UPDATE action SET definition = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"assist\", \"relativePath\": \"/en/admin/rest/ohs/update?entity=${body}\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { var uiData = JSON.parse(pageData).updateSkill_p0; var apiData = [ { \\\"entityBaseData\\\": { \\\"entityDisplayName\\\": uiData.skillName, \\\"entityId\\\": uiData.skillId, \\\"accountId\\\": accountId + \\\"-account-default\\\", \\\"clientId\\\": \\\"nemo-client\\\" + clientId, \\\"entityType\\\": \\\"skill\\\" }, \\\"entityAttributes\\\": { \\\"skillName\\\": uiData.skillName, \\\"skillDesc\\\": uiData.skillDesc, \\\"account\\\": accountId + \\\"-account-default\\\", \\\"users\\\": \\\"[]\\\" } } ]; return JSON.stringify(apiData); }\"}' WHERE id = '8';
INSERT INTO action (id, created_by, created_on, last_updated_by, last_updated_on, definition, definition_type, description, type) VALUES ('17', 'default.user', '2019-08-26 13:13:15.527428', 'default.user', '2019-08-26 16:46:57.805806', '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"assist\", \"relativePath\": \"/en/admin/rest/ohs/update?entity=${body}\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { var uiData = JSON.parse(pageData).STALive_p0; var ConfigMapper = uiData.ConfigMapper; var users = []; users = ConfigMapper.map(function (item) { return ({ userId: item.id, skillLevelId: uiData.skillId + \\\"-\\\" + item.key.toUpperCase() }); }); var apiData = [ { \\\"entityBaseData\\\": { \\\"entityId\\\": uiData.skillId, \\\"accountId\\\": accountId + \\\"-account-default\\\", \\\"clientId\\\": \\\"nemo-client-\\\" + clientId, \\\"entityType\\\": \\\"skill\\\" }, \\\"entityAttributes\\\": { \\\"account\\\": accountId + \\\"-account-default\\\", \\\"skillName\\\": uiData.skillName, \\\"skillDesc\\\": uiData.skillDesc, \\\"users\\\": JSON.stringify(users) } } ]; return JSON.stringify(apiData); }\"}', 'UPDATE', 'Skill to agents LIVE Update ', 'REST');
INSERT INTO action (id, created_by, created_on, last_updated_by, last_updated_on, definition, definition_type, description, type) VALUES ('18', 'default.user', '2019-08-26 13:13:15.527428', 'default.user', '2019-08-26 16:46:57.805806', '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"assist\", \"relativePath\": \"/en/admin/rest/ohs/update?entity=${body}\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { var uiData = JSON.parse(pageData).STATest_p0; var ConfigMapper = uiData.ConfigMapper; var users = []; users = ConfigMapper.map(function (item) { return ({ userId: item.id, skillLevelId: uiData.skillId + \\\"-\\\" + item.key.toUpperCase() }); }); var apiData = [ { \\\"entityBaseData\\\": { \\\"entityId\\\": uiData.skillId, \\\"accountId\\\": accountId + \\\"-account-default\\\", \\\"clientId\\\": \\\"nemo-client-\\\" + clientId, \\\"entityType\\\": \\\"skill\\\" }, \\\"entityAttributes\\\": { \\\"account\\\": accountId + \\\"-account-default\\\", \\\"skillName\\\": uiData.skillName, \\\"skillDesc\\\": uiData.skillDesc, \\\"users\\\": JSON.stringify(users) } } ]; return JSON.stringify(apiData); }\"}', 'UPDATE', 'Skill to agents TEST Update ', 'REST');
INSERT INTO action_workflow (id, created_by, created_on, last_updated_by, last_updated_on, action_id, activity_template_id) VALUES ('21', 'default.user', '2019-08-27 17:44:15.136579', 'default.user', '2019-08-27 17:44:15.136579', '17', 'STALive');
INSERT INTO action_workflow (id, created_by, created_on, last_updated_by, last_updated_on, action_id, activity_template_id) VALUES ('22', 'default.user', '2019-08-27 17:44:15.136579', 'default.user', '2019-08-27 17:44:15.136579', '18', 'STATest');
