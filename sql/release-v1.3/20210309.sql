UPDATE `action` SET `response_definition` = '{\n    \"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null ){ return new ApiResponse(\'SUCCESS\', \'SUCCESS\'); } else { return new ApiResponse(uiData.status, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"\n}' 
WHERE (`id` = '34');

UPDATE `activity_template` SET `entity_location` = '{\"UPDATE\": {\"entityIdLocation\": \"updateMsgFilter_p0.key\", \"entityLabelLocation\": \"updateMsgFilter_p0.notificationType\"}}' 
WHERE (`id` = 'keywordAlerts');

UPDATE `activity_template` SET `entity_location` = '{ \"CREATE\": { \"entityIdLocation\": \"createMailer_p0.mailConfigAccordionTabs.0.configKey\", \"entityLabelLocation\": \"createMailer_p0.mailConfigAccordionTabs.0.configKey\" }, \"UPDATE\": { \"entityIdLocation\": \"updateMailer_p0.mailConfigAccordionTabs.0.key\", \"entityLabelLocation\": \"updateMailer_p0.mailConfigAccordionTabs.0.configKey\" } }' 
WHERE (`id` = 'mailer');

UPDATE `activity_template` SET `entity_location` = '{\"UPDATE\": {\"entityIdLocation\": \"updateAccountConfig_p0.key\", \"entityLabelLocation\": \"updateAccountConfig_p0.description\"}}' 
WHERE (`id` = 'accountConfig');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.description){ return new ApiResponse(\'SUCCESS\', uiData.description); } else { return new ApiResponse(\'ERROR\', uiData.errorMessage); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request \'); } } \"}'
 WHERE (`id` = '40');

UPDATE `activity_template` SET `entity_location` = '{\"CREATE\": {\"entityIdLocation\": \"createPropertyValidation_p0.validatorName\", \"entityLabelLocation\": \"createPropertyValidation_p0.validatorName\"}, \"UPDATE\": {\"entityIdLocation\":\"updatePropertyValidation_p0.key\", \"entityLabelLocation\": \"updatePropertyValidation_p0.validatorName\"}}'
WHERE (`id` = 'propertyValidation');

UPDATE `action` SET `definition` = '{\"type\": \"REST\", \"method\": \"POST\", \"headers\": {\"Content-Type\": \"application/x-www-form-urlencoded\", \"GenerateAuthToken\": \"Bearer \"}, \"serviceId\": \"assist\", \"uploadFile\": false, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/nemo-client-${clientId}/accounts/${accountId}-account-default/propertyconfiguration?entityObj=${body}\", \"bodyDefinition\": {}, \"requestBodyKey\": null, \"restClientType\": null, \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData, entityId, clientId, accountId, env) { pageData = JSON.parse(pageData).createPropertyConfig_p0; var apiData = {};if(pageData.scope.scopeType === \'account\'){apiData.scopeId =pageData.accountId;}else{apiData.scopeId = pageData.scope.queueScope;} apiData.scopeType =pageData.scope.scopeType;apiData.contextVariableBaseId = \\\"\\\"; apiData.varTypeLabel=\\\"\\\"; apiData.varName = pageData.varName; apiData.key=pageData.key; apiData.order = pageData.order;apiData.varType =pageData.varType;apiData.displayName = pageData.displayName;apiData.validatorId = pageData.validatorId; apiData.editableFlag = pageData.editableFlag; apiData.maskableFlag = pageData.maskableFlag; apiData.mandatoryFlag= pageData.mandatoryFlag; apiData.varDescription=pageData.varDescription; return JSON.stringify(apiData)}\", \"preFetchDefinition\": null}'
 WHERE (`id` = '27');

 UPDATE `activity_template` SET `ui_schema` = '{ \"menuGroupName\": \"nav\", \"icon\": \"\", \"toolTip\": \"\", \"dashboard\": true, \"display_order\": 1, \"isActive\": true, \"uiOptions\": { \"entityList\": { \"entityHeader\": { \"label\": \"Name\", \"secondaryLabel\": \"Property Panel\" } } } }' 
 WHERE (`id` = 'propertyConfig');

UPDATE `activity_template` SET `title` = 'Mailer Configurations'
WHERE (`id` = 'mailer');
UPDATE `activity_template` SET `title` = 'Property Configurations' 
WHERE (`id` = 'propertyConfig');

UPDATE `entity_template` SET `name` = 'Property Configurations'
WHERE (`id` = '13');

UPDATE `entity_template` SET `name` = 'Session Timeouts' 
WHERE (`id` = '20');

UPDATE `activity_template` SET `title` = 'Session Timeouts' 
WHERE (`id` = 'sessiontimeout');