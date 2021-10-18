use `self_serve`;

UPDATE `action`
SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null ){ return new ApiResponse(\'SUCCESS\', \'\'); } else { return new ApiResponse(uiData.status, uiData.errorMessage||\'System error while publishing\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } } \"}'
WHERE (`id` = '25');