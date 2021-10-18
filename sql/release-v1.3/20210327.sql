
/* Fix - Hoop publish fail */
UPDATE `self_serve`.`action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.status == \\\"SUCCESS\\\"){ return new ApiResponse(uiData.status, \'\'); } else { return new ApiResponse(\\\"ERROR\\\", parseMsg(uiData.errorMessage)); } }catch(e){ return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } } function parseMsg(msg) { var retVal=msg||\\\"\\\"; if(msg.indexOf(\\\"Heuristic\\\")!=-1){retVal= \\\"Entity that you are trying to map is not available in the target environment. Please ensure the mapped entities are published to the target environment.\\\";} else if(msg.indexOf(\\\"entity.invalid.id\\\")!=-1){ retVal=\\\"Entity that you are trying to update is not available in the target environment. Contact [24]7.ai support team.\\\";} return retVal;} \"}'
WHERE (`id` = '1');