use self_serve;

ALTER TABLE `activity_instance` ADD COLUMN `processed_response` json DEFAULT NULL;

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\" : \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.status == \'SUCCESS\' && uiData.data != null){ return new ApiResponse(uiData.status, uiData.data.entityId); } else { return new ApiResponse(uiData.status, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}'
WHERE `id` in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,26,27,28,29);
