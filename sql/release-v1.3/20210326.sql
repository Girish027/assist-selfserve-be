/* Queues Add notify call*/
use self_serve;

INSERT INTO `action_workflow`
(`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `action_id`, `activity_template_id`) VALUES
('57', 'default.user', '2021-03-25 22:44:15.136579', 'default.user', '2021-03-25 22:44:15.136579', '13', 'queues');

UPDATE `action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/invitemanagement/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { var uiData = JSON.parse(pageData).createQueues_p0.tabsField; var uiDataB = uiData[0]; var uiDataA = uiData[1].assistConfig; var apiData = [{ queueName : uiDataB.queueName, queueDescription : uiDataB.queueDesc, acceptanceRate : uiDataA.inviteMgmt.invites.toString(), safetyfactor : uiDataA.inviteMgmt.safetyfactor, expiryInterval : uiDataA.inviteMgmt.expiryInterval, caModelType : uiDataA.inviteMgmt.caModelType, key : entityId}]; return JSON.stringify(apiData); }\", \"preFetchDefinition\": null}' WHERE (`id` = '50');

INSERT INTO `action_dependencies` (`action_id`, `dependent_action_id`) VALUES ('13', '12');
INSERT INTO `action_dependencies` (`action_id`, `dependent_action_id`) VALUES ('51', '13');
DELETE FROM `action_dependencies` WHERE (`action_id` = '51') and (`dependent_action_id` = '12');

--- response definition - fix

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null ){ return new ApiResponse(\'SUCCESS\', \'\'); } else { return new ApiResponse(uiData.status, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } }\"}'
WHERE (`id` = '25');

UPDATE `action` SET `response_definition` = '{ \"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null ){ return new ApiResponse(\'SUCCESS\', \'\'); } else { return new ApiResponse(uiData.status, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \" }' 
WHERE (`id` = '34');
 
UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values){values = JSON.parse(values);values.notificationStatus = values.notificationStatus.toString();return JSON.stringify(values);}\"}'
WHERE (`id` = '49');