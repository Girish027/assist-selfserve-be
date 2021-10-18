use `self_serve`;

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try { var responseIntermediate = JSON.parse(responseData); var response = JSON.parse(responseIntermediate); if (response.status === \\\"completed\\\") { return new ApiResponse(\\\"SUCCESS\\\", \\\"Successfully processed \\\" + response.processedRecords + \\\" rows out of \\\" + response.totalRecords + \\\" rows\\\"); } else if(response.status === \\\"empty\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"No records found to add agents\\\"); } else if(response.status === \\\"upload_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"There was  an error while uploading rows, Successfully processed \\\" + response.processedRecords + \\\" rows\\\"); } else if(response.status === \\\"validation_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"There was  a validation error while uploading in records, Successfully processed \\\" + response.processedRecords + \\\" rows\\\"); } else return new ApiResponse(\\\"ERROR\\\", JSON.stringify(response)) } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\"}'
WHERE (`id` = '53');
UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try { var responseIntermediate = JSON.parse(responseData); var response = JSON.parse(responseIntermediate); if (response.status === \\\"completed\\\") { return new ApiResponse(\\\"SUCCESS\\\", \\\"Successfully processed \\\" + response.processedRecords + \\\" rows out of \\\" + response.totalRecords + \\\" rows\\\"); } else if(response.status === \\\"empty\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"No records found to add agents\\\"); } else if(response.status === \\\"upload_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"There was  an error while uploading rows, Successfully processed \\\" + response.processedRecords + \\\" rows\\\"); } else if(response.status === \\\"validation_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"There was  a validation error while uploading in records, Successfully processed \\\" + response.processedRecords + \\\" rows\\\"); } else return new ApiResponse(\\\"ERROR\\\", JSON.stringify(response)) } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\"}'
WHERE (`id` = '55');

UPDATE `self_serve`.`config` SET `value` = '{\"refreshIntervalInSeconds\": 30, \"showRefreshButton\": false, \"autoRefreshHelpType\": \"NEXT_AUTO_REFRESH_VISUALIZATION_TIMER\"}'
WHERE (`code` = 'promotion_monitor_configs');