INSERT INTO `action` VALUES
(6,'default.user','2019-09-19 17:37:31.168947','default.user','2019-09-19 17:37:31.168947','{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\", \"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"ohs\", \"relativePath\": \"/v1/ohs/adminEntity/create?entity=${body}\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate( pageData, entityId,clientId , accountId, env){ var uiData= JSON.parse(pageData).createQueues_p0.basicSection; var apiData=[{\\\"entityBaseData\\\":{\\\"accountId\\\": accountId+\\\"-account-default\\\", \\\"clientId\\\": \\\"nemo-client-\\\"+clientId, \\\"entityType\\\": \\\"queue\\\" },\\\"entityAttributes\\\": {\\\"queueName\\\": uiData.queueName, \\\"accountId\\\": accountId+\\\"-account-default\\\" }}]; if(uiData.queueTags.join(\\\",\\\")!==\\\"\\\"){ apiData[0].entityAttributes.tags=uiData.queueTags.join(\\\",\\\") }; return JSON.stringify(apiData); }\"}','create queue action','REST'),
(7,'default.user','2019-08-26 13:13:15.527428','default.user','2019-08-26 16:46:57.805806','{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/json\"}, \"serviceId\": \"assist\", \"relativePath\": \"/en/admin/rest/ohs/notify\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate(pageData) { return JSON.stringify(\\\"\\\"); }\"}','Assist notify action','REST'),
(8,'default.user','2019-08-26 13:13:15.527428','default.user','2019-08-26 16:46:57.805806','{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"assist\", \"relativePath\": \"/en/admin/rest/ohs/update\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate( pageData, entityId,clientId , accountId, env){ var uiData= JSON.parse(pageData).createQueues_p0; var apiData=[{\\\"entityBaseData\\\":{\\\"accountId\\\": accountId+\\\"-account-default\\\", \\\"clientId\\\": \\\"nemo-client-\\\"+clientId, \\\"entityType\\\": \\\"chatQueue\\\", \\\"entityDisplayName\\\": entityId,\\\"entityId\\\": accountId+\\\"-account-default-queue-\\\"+entityId },\\\"entityAttributes\\\": {\\\"agentFTShare\\\": uiData.assistSection.agentFTShare,\\\"agentMailerConfigKey\\\": uiData.assistSection.agentMailerConfigKey,\\\"avgChatDuration\\\": uiData.assistSection.avgChatDuration,\\\"chatDurationRefreshFactor\\\": uiData.assistSection.chatDurationRefreshFactor,\\\"coBrowseEnabled\\\": uiData.assistSection.coBrowseEnabled,\\\"coViewEnabled\\\": uiData.assistSection.coViewEnabled,\\\"crmDisplayText\\\": uiData.assistSection.crmDisplayText,\\\"crmScriptContent\\\": uiData.assistSection.crmScriptContent,\\\"interLOBEnabled\\\": uiData.assistSection.interLOBEnabled,\\\"isAccountQueue\\\": uiData.assistSection.isAccountQueue,\\\"isOutboundLOBQueueTransferEnabled\\\": uiData.assistSection.isOutboundLOBQueueTransferEnabled,\\\"queueLength\\\": uiData.assistSection.queueLength, \\\"queueTimeOut\\\": uiData.assistSection.queueTimeout,\\\"resourceFree\\\": uiData.assistSection.resourceFree,\\\"typingEnabled\\\": uiData.assistSection.typingEnabled,\\\"typingTimeout\\\": uiData.assistSection.typingTimeout,\\\"visitorMailerConfigKey\\\": uiData.assistSection.visitorMailerConfigKey}}]; return JSON.stringify(apiData); }\"}','Assist config push action','REST');

INSERT INTO `action_dependencies` VALUES 
(7,6),
(8,7);

INSERT INTO `action_workflow` VALUES 
(6,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',6,'createQueues'),
(7,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',7,'createQueues'),
(8,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',8,'createQueues');

INSERT INTO `fetch_config_template` VALUES
(8,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','listTeam','{\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\"}','{\"contexts\": [\"accountId\"], \"constants\": {}}','/v1/ohs/adminEntity/listByAccount?entityType=team&accountId=${accountId}-account-default','{\"objectTranslator\": \"function translate(values){var queues={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ queues.enum.push(item.entityBaseData.entityId); queues.enumNames.push(item.entityBaseData.entityDisplayName); });return JSON.stringify(queues);}\"}','JS','FIELD_OPTIONS','ohs'),
(10,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','listSkill','{}','{\"contexts\": [\"accountId\"], \"constants\": {}}','/en/admin/rest/ohs/listByAccount?entityType=skill&accountId=${accountId}-account-default','{\"objectTranslator\": \"function translate(values){var queues={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ queues.enum.push(item.entityBaseData.entityId); queues.enumNames.push(item.entityBaseData.entityDisplayName); });return JSON.stringify(queues);}\"}','JS','FIELD_OPTIONS','assist'),
(11,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','mailerConfigKey','{}','{\"contexts\": [\"accountId\"], \"constants\": {}}','/en/admin/rest/ohs/listByAccount?entityType=mailerConfigKey&accountId=${accountId}-account-default','{\"objectTranslator\": \"function translate(values){var queues={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ queues.enum.push(item.entityBaseData.entityId); queues.enumNames.push(item.entityBaseData.entityDisplayName); });return JSON.stringify(queues);}\"}','JS','FIELD_OPTIONS','assist'),
(12,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','resourceFree','{}','{\"contexts\": [\"accountId\"], \"constants\": {}}','/en/admin/rest/ohs/listByAccount?entityType=resourceFree&accountId=${accountId}-account-default','{\"objectTranslator\": \"function translate(values){var queues={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ queues.enum.push(item.entityBaseData.entityId); queues.enumNames.push(item.entityBaseData.entityDisplayName); });return JSON.stringify(queues);}\"}','JS','FIELD_OPTIONS','assist'),
(13,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','mailerConfigKeyTemp','{}','{\"contexts\": [\"accountId\"], \"constants\": {}}','/en/admin/rest/ohs/listByAccount?entityType=mailerConfigKey&accountId=${accountId}-account-default','{\"objectTranslator\": \"function translate(values){var queues={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ queues.enum.push(item.entityBaseData.entityId); queues.enumNames.push(item.entityBaseData.entityDisplayName); });return JSON.stringify(queues);}\"}','JS','FIELD_OPTIONS','assist');

INSERT INTO `page_template` VALUES
('createQueues_p0','default.user','2019-08-07 14:06:42.713766','jonathan.paul@247-inc.com','2019-10-07 11:23:10.848661','{\"schema\":{\"type\":\"object\",\"title\":\"Create Queue\",\"properties\":{\"basicSection\":{\"type\":\"object\",\"title\":\"Basic Section\",\"properties\":{\"queueName\":{\"type\":\"string\",\"title\":\"Queue Name\",\"default\":\"\"},\"queueDesc\":{\"type\":\"string\",\"title\":\"Description\",\"default\":\"\"},\"queueTags\":{\"title\":\"Tags\",\"type\":\"array\",\"items\":{\"type\":\"string\",\"anyOf\":[{\"enum\":[],\"type\":\"string\",\"enumNames\":[]}],\"title\":\"Not Selected\"}}}},\"assistSection\":{\"title\":\"Assist Section\",\"type\":\"object\",\"properties\":{\"queueLength\":{\"title\":\"Queue Length\",\"type\":\"string\",\"default\":\"0\"},\"queueTimeOut\":{\"title\":\"Queue Timeout (seconds)\",\"type\":\"string\",\"default\":\"0\"},\"avgChatDuration\":{\"title\":\"Average Chat Duration (minutes)\",\"type\":\"string\",\"default\":\"10\"},\"chatDurationRefreshFactor\":{\"title\":\"Chat Duration Refresh Factor\",\"type\":\"string\",\"default\":\"3.0\"},\"waitTimeBufferFactor\":{\"title\":\"Wait Time Buffer Factor\",\"type\":\"string\",\"default\":\"1.0\"},\"typingTimeout\":{\"title\":\"Typing Timeout (seconds)\",\"type\":\"string\",\"default\":\"5\"},\"resourceFree\":{\"title\":\"Route On\",\"type\":\"string\",\"enum\":[],\"enumNames\":[],\"default\":\"DISPOSE\"},\"typingEnabled\":{\"title\":\"Typing Feature Enabled\",\"type\":\"string\",\"enum\":[\"True\",\"False\"],\"enumNames\":[\"True\",\"False\"],\"default\":\"True\"},\"interLOBEnabled\":{\"title\":\"Accept InterLOB chats\",\"type\":\"string\",\"enum\":[\"True\",\"False\"],\"enumNames\":[\"True\",\"False\"],\"default\":\"False\"},\"coBrowseEnabled\":{\"title\":\"Co-Browsing Feature Enabled\",\"type\":\"string\",\"enum\":[\"True\",\"False\"],\"enumNames\":[\"True\",\"False\"],\"default\":\"False\"},\"coViewEnabled\":{\"title\":\"Co-View Feature Enabled\",\"type\":\"string\",\"enum\":[\"True\",\"False\"],\"enumNames\":[\"True\",\"False\"],\"default\":\"False\"},\"agentFTShare\":{\"title\":\"File Transfer for Visitors\",\"type\":\"string\",\"enum\":[\"Deny\",\"Auto Accept\"],\"enumNames\":[\"Deny\",\"Auto Accept\"],\"default\":\"Deny\"},\"isAccountQueue\":{\"title\":\"Account Level Queue\",\"type\":\"string\",\"enum\":[\"True\",\"False\"],\"enumNames\":[\"True\",\"False\"],\"default\":\"False\"},\"skill\":{\"title\":\"Choose skills\",\"type\":\"array\",\"items\":{\"type\":\"string\",\"anyOf\":[{\"enum\":[],\"type\":\"string\",\"enumNames\":[]}],\"title\":\"Not Selected\"}},\"dispositionFormContent\":{\"title\":\"Upload disposition form\",\"type\":\"string\",\"default\":\"\"},\"crmScriptContent\":{\"title\":\"CRM Script Content\",\"type\":\"string\",\"default\":\"\"},\"crmDisplayText\":{\"title\":\"CRM Launch Button Label\",\"type\":\"string\",\"default\":\"\"},\"isOutboundLOBQueueTransferEnabled\":{\"title\":\"Outbound LOB Transfer Enabled\",\"type\":\"string\",\"enum\":[\"True\",\"False\"],\"enumNames\":[\"True\",\"False\"],\"default\":\"False\"},\"visitorMailerConfigKey\":{\"title\":\"Visitor Mailer Configuration\",\"type\":\"string\",\"enum\":[],\"enumNames\":[],\"default\":\"\"},\"agentMailerConfigKey\":{\"title\":\"Agent Mailer Configuration\",\"type\":\"string\",\"enum\":[],\"enumNames\":[],\"default\":\"\"}}}},\"description\":\"Create queues for your client\"},\"uiSchema\":{\"basicSection\":{\"queueName\":{\"ui:field\":\"text\",\"ui:options\":{\"inputType\":\"text\"}},\"queueDesc\":{\"ui:field\":\"text\",\"ui:options\":{\"inputType\":\"text\"}},\"queueTags\":{\"ui:field\":\"multiselect\"}},\"assistSection\":{\"queueLength\":{\"ui:field\":\"text\"},\"queueTimeOut\":{\"ui:field\":\"text\"},\"avgChatDuration\":{\"ui:field\":\"text\"},\"chatDurationRefreshFactor\":{\"ui:field\":\"text\"},\"waitTimeBufferFactor\":{\"ui:field\":\"text\"},\"typingTimeout\":{\"ui:field\":\"text\"},\"resourceFree\":{\"ui:field\":\"select\"},\"typingEnabled\":{\"ui:field\":\"select\"},\"interLOBEnabled\":{\"ui:field\":\"select\"},\"coBrowseEnabled\":{\"ui:field\":\"select\"},\"coViewEnabled\":{\"ui:field\":\"select\"},\"agentFTShare\":{\"ui:field\":\"select\"},\"isAccountQueue\":{\"ui:field\":\"select\"},\"skill\":{\"ui:field\":\"multiselect\"},\"dispositionFormContent\":{\"ui:field\":\"text\",\"ui:options\":{\"inputType\":\"file\"}},\"crmScriptContent\":{\"ui:field\":\"textarea\"},\"crmDisplayText\":{\"ui:field\":\"textarea\"},\"isOutboundLOBQueueTransferEnabled\":{\"ui:field\":\"select\"},\"visitorMailerConfigKey\":{\"ui:field\":\"select\"},\"agentMailerConfigKey\":{\"ui:field\":\"select\"}}},\"fetch\":{\"list\":{\"listTag\":\"basicSection.queueTags\",\"listSkill\":\"assistSection.skill\",\"mailerConfigKey\":\"assistSection.agentMailerConfigKey\",\"mailerConfigKeyTemp\":\"assistSection.visitorMailerConfigKey\",\"resourceFree\":\"assistSection.resourceFree\"},\"page\":\"createQueues_p0\"}}','Queue Creation for Chat','Create Queues');

INSERT INTO `service` VALUES
('assist','default.user','2019-06-13 12:11:09.000000','default.user','2019-06-13 12:11:09.000000','Assist call','Assist'),
('ohs','default.user','2019-06-03 19:42:21.000000','default.user','2019-06-03 19:42:21.000000','Org Hierarchy Service','Org Hierarchy Service');

INSERT INTO `service_urls` VALUES
(3,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://dev.api.sv2.247-inc.net','TEST','ohs'),
(5,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://appglobal.qacore1.assist.int.lb-priv.sv2.247-inc.net','TEST','assist'),
(6,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://dev.api.sv2.247-inc.net','LIVE','ohs'),
(8,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://appglobal.qacore1.assist.int.lb-priv.sv2.247-inc.net','LIVE','assist');

INSERT INTO `workflow_page` VALUES
(7,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','createQueues_p0','createQueues');

INSERT INTO `workflow_template` VALUES
('createQueues','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Create Queues','createQueues_p0.basicSection.queueName','MENU',_binary '','Create Queues','NON_SINGLETON',3);
