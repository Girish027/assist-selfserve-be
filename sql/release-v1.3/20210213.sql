use self_serve;

INSERT INTO `action` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `definition`, `definition_type`, `description`, `type`, `response_definition`, `configs`) VALUES
('41', 'default.user', '2019-08-26 13:13:15.527428', 'jonathan.paul@247-inc.com', '2021-02-12 13:26:09.734790', '{\"type\": \"REST\", \"method\": \"GET\", \"headers\": {}, \"serviceId\": \"assist\", \"idLookupKeys\": null, \"relativePath\": \"/en/admin/rest/ohs/get?entityId=${entity}&entityType=chatQueue\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) {  return \\\"\\\"; }\", \"preFetchDefinition\": null}', 'UPDATE', 'assist queue GET action', 'REST', '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(JSON.parse(responseData).data.entity);  if(uiData != null && uiData.entityAttributes.id){ return new ApiResponse(uiData.status, uiData.entityAttributes.id); } else { return new ApiResponse(uiData.status, uiData.errorMessage); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}', '{\"POLL\": {\"type\": \"POLL\", \"retryCount\": 3000, \"retryInterval\": 30000}}');

INSERT INTO `action_dependencies` (`action_id`, `dependent_action_id`)
VALUES ('41', '15');

UPDATE `action_dependencies`
SET `dependent_action_id` = '41'
WHERE (`action_id` = '14') and (`dependent_action_id` = '15');
