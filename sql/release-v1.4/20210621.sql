use `self_serve`;
UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"referer\": \"https://salesdemo.portal.assist.staging.247-inc.net/en/\", \"uploadFile\": \"true\", \"Content-Type\": \"multipart/form-data\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": true, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/${componentClientId}/accounts/${componentAccountId}/agents/upload/${uploadOption}\", \"bodyDefinition\": {}, \"requestBodyKey\": \"uploadfile\", \"restClientType\": \"MULTIPART_FILE\", \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData) { var uiData = JSON.parse(pageData).createBulkAgentsTest_p0 ; var uploadOption = uiData.stepTwo.selectType; return uploadOption; }\", \"preFetchDefinition\": null, \"getPathParamFromPageData\": true, \"prefetchRequiredForLiveOnly\": false }' 
WHERE (`id` = '52');

UPDATE `action` SET `definition` = '{ \"type\": \"REST\", \"method\": \"POST\", \"headers\": { \"referer\": \"https://salesdemo.portal.assist.staging.247-inc.net/en/\", \"uploadFile\": \"true\", \"Content-Type\": \"multipart/form-data\", \"GenerateAuthToken\": \"Bearer \" }, \"serviceId\": \"assist\", \"uploadFile\": true, \"idLookupKeys\": null, \"relativePath\": \"/en/hierarchy/rest/clients/${componentClientId}/accounts/${componentAccountId}/agents/upload/${uploadOption}\", \"bodyDefinition\": {}, \"requestBodyKey\": \"uploadfile\", \"restClientType\": \"MULTIPART_FILE\", \"idLookupRequired\": false, \"objectTranslator\": \"function translate(pageData) { var uiData = JSON.parse(pageData).createBulkAgentsLive_p0 ; var uploadOption = uiData.stepTwo.selectType; return uploadOption; }\", \"preFetchDefinition\": null, \"getPathParamFromPageData\": true, \"prefetchRequiredForLiveOnly\": false }' 
WHERE (`id` = '54');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message) { this.status = _status; this.message = _message; }; function translate(responseData) { try { var responseIntermediate = JSON.parse(responseData); var response = JSON.parse(responseIntermediate); if (response.status === \\\"completed\\\") { return new ApiResponse(\\\"SUCCESS\\\", response.uploadId); } else if (response.status === \\\"empty\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"No records found to add agents\\\"); } else if (response.status === \\\"upload_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"Some erroneous records were identified during the last operation.Please click on View Configurations, download excel file & check Errors column to review failed records.\\\"); } else if (response.status === \\\"validation_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"Validation failed for some records during the last operation. Please click on View Configurations, download excel file & check Errors column to review failed records.\\\"); } else return new ApiResponse(\\\"ERROR\\\", JSON.stringify(response)) } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\"}' 
WHERE (`id` = '53');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message) { this.status = _status; this.message = _message; }; function translate(responseData) { try { var responseIntermediate = JSON.parse(responseData); var response = JSON.parse(responseIntermediate); if (response.status === \\\"completed\\\") { return new ApiResponse(\\\"SUCCESS\\\", response.uploadId); } else if (response.status === \\\"empty\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"No records found to add agents\\\"); } else if (response.status === \\\"upload_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"Some erroneous records were identified during the last operation. Please click on View Configurations, download excel file & check Errors column to review failed records.\\\"); } else if (response.status === \\\"validation_error\\\") { return new ApiResponse(\\\"ERROR_STOP\\\", \\\"Validation failed for some records during the last operation. Please click on View Configurations, download excel file & check Errors column to review failed records.\\\"); } else return new ApiResponse(\\\"ERROR\\\", JSON.stringify(response)) } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\"}'
WHERE (`id` = '55');
