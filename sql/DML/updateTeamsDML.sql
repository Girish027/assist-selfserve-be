INSERT INTO `action` VALUES 
(5,'default.user','2019-09-19 17:37:31.168947','default.user','2019-09-19 17:37:31.168947','{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\", \"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"ohs\", \"relativePath\": \"/v1/ohs/adminEntity/update?entity=${body}\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate( pageData, entityId,clientId , accountId, env){ var uiData= JSON.parse(pageData).updateTeams_p0; var apiData=[{\\\"entityBaseData\\\":{\\\"accountId\\\": accountId+\\\"-account-default\\\", \\\"clientId\\\": \\\"nemo-client-\\\"+clientId, \\\"entityType\\\": \\\"team\\\", \\\"entityId\\\": entityId },\\\"entityAttributes\\\": {\\\"teamName\\\": uiData.teamName, \\\"accountId\\\": accountId+\\\"-account-default\\\" }}]; if(uiData.teamTags.join(\\\",\\\")!==\\\"\\\"){ apiData[0].entityAttributes.tags=uiData.teamTags.join(\\\",\\\") }; return JSON.stringify(apiData); }\"}','Update team action','REST');

INSERT INTO `action_workflow` VALUES 
(5,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',5,'updateTeams');

INSERT INTO `fetch_config_template` VALUES
(8,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','listTeam','{\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\"}','{\"contexts\": [\"accountId\"], \"constants\": {}}','/v1/ohs/adminEntity/listByAccount?entityType=team&accountId=${accountId}-account-default','{\"objectTranslator\": \"function translate(values){var queues={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ queues.enum.push(item.entityBaseData.entityId); queues.enumNames.push(item.entityBaseData.entityDisplayName); });return JSON.stringify(queues);}\"}','JS','FIELD_OPTIONS','ohs'),
(9,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','updateTeams_p0','{\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\"}','{\"contexts\": [\"entity\"], \"constants\": {}}','/v1/ohs/adminEntity/get?entityId=${entity}&entityType=team','{\"objectTranslator\": \"function translate(values){values=JSON.parse(JSON.parse(values).data.entity); var uiData = {}; uiData.teamId=values.entityBaseData.entityId; uiData.teamName=values.entityBaseData.entityDisplayName; uiData.teamTags=values.entityAttributes.tags.split(\\\",\\\"); return JSON.stringify(uiData);}\"}','JS','PAGE_DATA_VALUE','ohs');

INSERT INTO `page_template` VALUES
('updateTeams_p0','default.user','2019-08-07 14:06:42.713766','jonathan.paul@247-inc.com','2019-10-07 11:23:10.848661','{\"schema\":{\"type\":\"object\",\"title\":\"Update Team\",\"properties\":{\"team\":{\"enum\":[],\"type\":\"string\",\"title\":\"Team\",\"enumNames\":[]},\"teamId\":{\"type\":\"string\",\"title\":\"Team Id\"},\"teamName\":{\"type\":\"string\",\"title\":\"Team Name\",\"default\":\"\"},\"teamTags\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"anyOf\":[{\"enum\":[],\"type\":\"string\",\"enumNames\":[]}],\"title\":\"Not Selected\"},\"title\":\"Select Tags\",\"default\":[]}},\"description\":\"Update teams for your client\"},\"uiSchema\":{\"team\":{\"ui:field\":\"select\"},\"teamId\":{\"ui:field\":\"plaintext\"},\"teamName\":{\"ui:field\":\"text\",\"ui:options\":{\"inputType\":\"text\"}},\"teamTags\":{\"ui:field\":\"multiselect\",\"ui:options\":{\"inputType\":\"text\"}}},\"fetch\":{\"list\":{\"listTag\":\"teamTags\",\"listTeam\":\"team\"},\"page\":\"updateTeams_p0\"}}','Team updation for Chat','Update Teams');

INSERT INTO `service` VALUES
('ohs','default.user','2019-06-03 19:42:21.000000','default.user','2019-06-03 19:42:21.000000','Org Hierarchy Service','Org Hierarchy Service');

INSERT INTO `service_urls` VALUES
(3,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://dev.api.sv2.247-inc.net','TEST','ohs'),
(6,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://dev.api.sv2.247-inc.net','LIVE','ohs');


INSERT INTO `workflow_page` VALUES
(6,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','updateTeams_p0','updateTeams');

INSERT INTO `workflow_template` VALUES
('updateTeams','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Update Teams','updateTeams_p0.team','MENU',_binary '','Update Teams','NON_SINGLETON',3);
