use `self_serve`;

-- property validation
UPDATE `entity_template` SET `name` = 'Property Validations'
WHERE (`id` = '19');

UPDATE `activity_template` SET
`description` = 'Property Validations',
`title` = 'Property Validations',
`ui_schema` = '{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true, \"uiOptions\": {\"entityList\": {\"entityHeader\": {\"label\": \"Name\", \"secondaryLabel\": \"Type\"}}}}'
WHERE (`id` = 'propertyValidation');


UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{ \"objectTranslator\": \"function translate(values) { values = JSON.parse(values); var uiData = values.items; if(Array.isArray(uiData.enumValues) && uiData.enumValues.length==1 && uiData.enumValues[0]==\'\'){uiData.enumValues=[]} return JSON.stringify(uiData); }\"}'
WHERE (`id` = '59');

UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/validator/update?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var apiData = JSON.parse(pageData).updatePropertyValidation_p0; if(!apiData.validatorRule) {apiData.validatorRule=\\\"\\\"} if(!apiData.enumValues || apiData.enumValues.length==0) {apiData.enumValues=[\\\"\\\"]}  return JSON.stringify(apiData); }\", \"preFetchDefinition\": {}}'
WHERE (`id` = '42');

UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/validator?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env, prefetch) { var apiData = JSON.parse(pageData).createPropertyValidation_p0; apiData.key=\\\"\\\"; if(!apiData.validatorRule) {apiData.validatorRule=\\\"\\\"} if(!apiData.enumValues || apiData.enumValues.length==0) {apiData.enumValues=[\\\"\\\"]}  return JSON.stringify(apiData); }\", \"preFetchDefinition\": {}}'
WHERE (`id` = '43');

-- password policy
UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.policyId){ return new ApiResponse(uiData.status, \'\'); } else { return new ApiResponse(\'ERROR\', uiData.errorMessage); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}'
WHERE `id` in ('19', '20', '21', '22');

-- account config
UPDATE `activity_template` SET
`description` = 'Account Configurations',
`title` = 'Account Configurations'
WHERE (`id` = 'accountConfig');

UPDATE `entity_template` SET `name` = 'Account Configurations'
WHERE (`id` = '18');

-- authorised Urls
UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.key){ return new ApiResponse(uiData.status, \'\'); } else { return new ApiResponse(\'ERROR\', uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}'
WHERE (`id` = '44');
