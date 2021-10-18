use self_serve;

DELETE FROM `fetch_config_template` WHERE (`id` = '64');

INSERT INTO `fetch_config_template` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `fetch_for`, `headers`, `params`, `relativeurl`, `resp_api_to_resp_ui`, `translator_type`, `type`, `service_id`) 
VALUES ('64', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'getSmartResponseExcel', '{\"GenerateAuthToken\": \"Bearer \"}', '{\"contexts\": [\"accountId\", \"clientId\"], \"constants\": {}}', '/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/smartresponse/downloadexcel', '{\"objectTranslator\": \"function translate(apiData) { return apiData; }\"}', 'NONE', 'PAGE_DATA_VALUE', 'assist');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{ \"objectTranslator\": \"function translate(values, entity) { values = JSON.parse(values).data.items[entity]; var uiData = { \\\"id\\\": values.smartResponseId, \\\"scope\\\": values.scope.toLowerCase(), \\\"scopeId\\\": values.scopeId, \\\"tag\\\": values.tags, \\\"responseText\\\": values.smartresponseText, }; return JSON.stringify(uiData); }\" }' 
WHERE (`id` = '54');

INSERT INTO `action` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `definition`, `definition_type`, `description`, `type`, `response_definition`, `configs`) 
VALUES ('47', 'default.user', '2020-10-16 13:13:15.527428', 'Gangeyula.Chandra@247-inc.com', '2021-03-04 23:52:09.792912', '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"uploadFile\": \"true\", \"Content-Type\": \"multipart/form-data\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": true, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/smartresponse/uploadfile\", \"bodyDefinition\": {}, \"requestBodyKey\": \"file\", \"restClientType\": \"MULTIPART_FILE\", \"idLookupRequired\": false, \"objectTranslator\": \"\", \"preFetchDefinition\": null}', 'CREATE', 'Create Smart Response', 'REST', '{\"entityResponseTranslator\": \"var ApiResponse = function (_status, _message) { this.status = _status; this.message = _message; }; function translate(responseData) { try { var uiData = JSON.stringify(responseData); if (uiData.indexOf(\\\"true\\\") > -1) { return new ApiResponse(\\\"SUCCESS\\\", \\\"Smart Responses updated successfully\\\"); } else { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\"}', '{}');

INSERT INTO `action_workflow` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `action_id`, `activity_template_id`) 
VALUES ('50', 'default.user', '2019-08-27 17:44:15.136579', 'default.user', '2019-08-27 17:44:15.136579', '47', 'smartResponse');
