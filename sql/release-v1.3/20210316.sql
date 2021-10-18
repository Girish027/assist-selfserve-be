use self_serve;

UPDATE `activity_template` SET `entity_location` = '{"CREATE": {"entityIdLocation": "createTeams_p0.teamName", "entityLabelLocation": "createTeams_p0.teamName"}, "UPDATE": {"entityIdLocation": "updateTeams_p0.teamId", "entityLabelLocation": "updateTeams_p0.teamName"}}' 
WHERE (id = 'teams');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.key){ return new ApiResponse(\'SUCCESS\', \'\'); } else { return new ApiResponse(\'ERROR\', uiData.errorMsg); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } }\"}' 
WHERE `id` in ('37','39');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.key){ return new ApiResponse(\'SUCCESS\', uiData.key); } else { return new ApiResponse(\'ERROR\', uiData.errorMsg); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } }\"}' 
WHERE `id` in ('36','38');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values){var teams={enum:[\\\"\\\"], enumNames:[\\\"Select Team\\\"]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list); list.forEach(function(item){ teams.enum.push(item.entityBaseData.entityId);teams.enumNames.push(item.entityBaseData.entityDisplayName); }); }catch(e){} return JSON.stringify(teams);}\"}' 
WHERE (`id` = '8');

INSERT INTO `fetch_config_template` (`id`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `fetch_for`, `headers`, `params`, `relativeurl`, `resp_api_to_resp_ui`, `translator_type`, `type`, `service_id`) 
VALUES ('65', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'listTeamForConfigMapper', '{\"apikey\": \"KtbDtdYYb7EUD4oajYCM8l9Nh7L0AgEc\"}', '{\"contexts\": [\"accountId\"], \"constants\": {}}', '/v1/ohs/adminEntity/listByAccount?entityType=team&accountId=${accountId}-account-default', '{\"objectTranslator\": \"function translate(values){var teams={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list); list.forEach(function(item){ teams.enum.push(item.entityBaseData.entityId);teams.enumNames.push(item.entityBaseData.entityDisplayName); }); }catch(e){} return JSON.stringify(teams);}\"}', 'JS', 'FIELD_OPTIONS', 'ohs');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values){values = JSON.parse(values); values.scope = {} ;if(values.scopeType === \'queue\'){values.scope.queueScope = values.scopeId;}else{values.scope.accountId=values.scopeId;} values.scope.scopeType = values.scopeType;values.propertyType = values.varGroup;values.order = values.order.toString(); values.editableFlag = values.editableFlag.toString(); values.mandatoryFlag = values.mandatoryFlag.toString(); values.maskableFlag =values.maskableFlag.toString();if( values.validatorId !== undefined){values.propertyType = values.validatorId;}else{values.propertyType = \'\';}; return JSON.stringify(values)}\"}' 
WHERE (`id` = '37');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null ){ return new ApiResponse(\'SUCCESS\', \'\' ); } else { return new ApiResponse(uiData.errorCode, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}' 
WHERE (`id` = '26');
