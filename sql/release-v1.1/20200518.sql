use self_serve;

/*label changes (nav items)*/
UPDATE node_group SET title = 'Agent Management' WHERE (id = '1');
UPDATE activity_template SET title = 'Queues' WHERE (id = 'queues');
UPDATE activity_template SET title = 'Hours of Operation' WHERE (id = 'chathours');

/*disable visitors*/
DELETE FROM node_group WHERE (id = '3');
DELETE FROM menu WHERE (id = '3');
DELETE FROM node WHERE (id = '16');
DELETE FROM node WHERE (id = '3');

/*disable adf*/
DELETE FROM node WHERE (id = '14');

/*moving queues above chathours*/
UPDATE node SET id = '30' WHERE (id = '12');
UPDATE node SET id = '12' WHERE (id = '17');
UPDATE node SET id = '17' WHERE (id = '30');

/*update queues action fix*/
UPDATE action SET definition = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\"}, \"serviceId\": \"assist\", \"relativePath\": \"/en/admin/rest/ohs/update?entity=${body}\", \"bodyDefinition\": {}, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { var uiData = JSON.parse(pageData).updateQueues_p0.basicSection; var apiData = [ { \\\"entityBaseData\\\": { \\\"accountId\\\": accountId + \\\"-account-default\\\", \\\"entityType\\\": \\\"chatQueue\\\", \\\"entityId\\\": uiData.queueId, \\\"entityDisplayName\\\": uiData.queueName }, \\\"entityAttributes\\\": { \\\"queueLength\\\": uiData.assistSection.queueLength, \\\"queueTimeOut\\\": uiData.assistSection.queueTimeOut, \\\"avgChatDuration\\\": uiData.assistSection.avgChatDuration, \\\"chatDurationRefreshFactor\\\": uiData.assistSection.chatDurationRefreshFactor, \\\"waitTimeBufferFactor\\\": uiData.assistSection.waitTimeBufferFactor, \\\"typingTimeout\\\": uiData.assistSection.typingTimeout, \\\"resourceFree\\\": uiData.assistSection.resourceFree, \\\"typingEnabled\\\": uiData.assistSection.typingEnabled, \\\"interLOBEnabled\\\": uiData.assistSection.interLOBEnabled, \\\"coBrowseEnabled\\\": uiData.assistSection.coBrowseEnabled, \\\"coViewEnabled\\\": uiData.assistSection.coViewEnabled, \\\"agentFTShare\\\": uiData.assistSection.agentFTShare, \\\"isAccountQueue\\\": uiData.assistSection.isAccountQueue, \\\"isOutboundLOBQueueTransferEnabled\\\": uiData.assistSection.isOutboundLOBQueueTransferEnabled, \\\"visitorMailerConfigKey\\\": uiData.assistSection.visitorMailerConfigKey, \\\"agentMailerConfigKey\\\": uiData.assistSection.agentMailerConfigKey, \\\"visitorInactivityPeriod\\\": uiData.assistSection.visitorInactivityPeriod, \\\"agentResponseTime\\\": uiData.assistSection.agentResponseTime, \\\"agentAffinityPeriod\\\": uiData.assistSection.agentAffinityPeriod, \\\"conversationalInterval\\\": uiData.assistSection.conversationalInterval } } ]; if (uiData.assistSection.skill !== undefined && uiData.assistSection.skill.join(\\\",\\\") !== \\\"\\\") { apiData[ 0 ].entityAttributes.skill = uiData.assistSection.skill.join(\\\",\\\") }; for (var i = 0; i < 999999999; i++) { } return JSON.stringify(apiData); }\"}' WHERE (id = '14');

/*update fetch config template for updateQueues_p0*/
UPDATE fetch_config_template SET resp_api_to_resp_ui = '{\"objectTranslator\":\"function translate(apiData) { var val = JSON.parse(JSON.parse(apiData).data.entity); var uiData = {\\\"basicSection\\\": {\\\"assistSection\\\": {\\\"queueLength\\\": val.entityAttributes.queueLength,\\\"queueTimeOut\\\": val.entityAttributes.queueTimeOut,\\\"avgChatDuration\\\": val.entityAttributes.avgChatDuration,\\\"chatDurationRefreshFactor\\\": val.entityAttributes.chatDurationRefreshFactor,\\\"waitTimeBufferFactor\\\": val.entityAttributes.waitTimeBufferFactor,\\\"typingTimeout\\\": val.entityAttributes.typingTimeout,\\\"resourceFree\\\": val.entityAttributes.resourceFree,\\\"typingEnabled\\\": val.entityAttributes.typingEnabled,\\\"interLOBEnabled\\\": val.entityAttributes.interLOBEnabled,\\\"coBrowseEnabled\\\": val.entityAttributes.coBrowseEnabled,\\\"coViewEnabled\\\": val.entityAttributes.coViewEnabled,\\\"agentFTShare\\\": val.entityAttributes.agentFTShare,\\\"isAccountQueue\\\": val.entityAttributes.isAccountQueue,\\\"crmScriptContent\\\": val.entityAttributes.crmScriptContent,\\\"crmDisplayText\\\": val.entityAttributes.crmDisplayText,\\\"isOutboundLOBQueueTransferEnabled\\\": val.entityAttributes.isOutboundLOBQueueTransferEnabled,\\\"visitorMailerConfigKey\\\": val.entityAttributes.visitorMailerConfigKey,\\\"agentMailerConfigKey\\\": val.entityAttributes.agentMailerConfigKey,\\\"visitorInactivityPeriod\\\": val.entityAttributes.visitorInactivityPeriod,\\\"agentResponseTime\\\": val.entityAttributes.agentResponseTime,\\\"agentAffinityPeriod\\\": val.entityAttributes.agentAffinityPeriod,\\\"crmAppId\\\": val.entityAttributes.crmAppId,\\\"conversationalInterval\\\": val.entityAttributes.conversationalInterval } } }; if (val.entityAttributes.skill.length > 0) { uiData.basicSection.assistSection.skill = val.entityAttributes.skill.split(\\\",\\\") } return JSON.stringify(uiData); }\"}' WHERE (id = '11');
UPDATE fetch_config_template SET resp_api_to_resp_ui = '{\"objectTranslator\":\"function translate(apiData) { var val = JSON.parse(JSON.parse(apiData).data.entity); var uiData = { basicSection: { queueName: val.entityBaseData.entityDisplayName, queueId: val.entityAttributes.id, queueDesc: val.entityAttributes.queueDesc, queueType: val.entityAttributes.queueType } }; if (val.entityAttributes.tags.length > 0) { uiData.basicSection.queueTags = val.entityAttributes.tags.split(); } return JSON.stringify(uiData); }\"}' WHERE (id = '12');

-- 5713 config update

CREATE TABLE config (
  code varchar(255) NOT NULL,
  created_by varchar(255) NOT NULL,
  created_on datetime(6) NOT NULL,
  last_updated_by varchar(255) DEFAULT NULL,
  last_updated_on datetime(6) DEFAULT NULL,
  status tinyint(1) NOT NULL DEFAULT '1',
  type varchar(255) NOT NULL,
  value varchar(255) NOT NULL,
  PRIMARY KEY (code)
) ;

INSERT INTO config (code, status, created_by, created_on, type, value)
 VALUES ('max_instances_days_limit', 1, 'default.user', '2018-07-12 00:34:55', 'UI_APP', '90');


