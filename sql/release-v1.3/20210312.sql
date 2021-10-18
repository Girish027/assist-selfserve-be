-- bug fix reset password action

use self_serve;

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function (_status, _message) { this.status = _status; this.message = _message; }; function translate(responseData, responseCode) { try { if (responseCode == 200) { return new ApiResponse(\\\"SUCCESS\\\", \\\"Reset password is successful\\\"); } else { return new ApiResponse(\\\"ERROR\\\", \\\"Reset password unsuccessful, Error: \\\"+ JSON.parse(responseData).errorMessage); } } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\"}' 
WHERE (`id` = '48');

UPDATE `action` SET `description` = 'agents reset password (NON-ORC)' 
WHERE (`id` = '48');

UPDATE `action` SET `description` = 'agents modify password (NON-ORC)' 
WHERE (`id` = '49');

-- smart response hardening

UPDATE `fetch_config_template`
SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values, entity, context) { try { context  = JSON.parse(context); var timestamp = new Date().getTime(); var uiData = { \\\"accountId\\\": context.accountId+timestamp}; } catch (e) {} return JSON.stringify(uiData); }\"}'
WHERE (`id` = '52');

UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function (_status, _message) { this.status = _status; this.message = _message; }; function translate(responseData) { try { var uiData = JSON.stringify(responseData); if (uiData.indexOf(\\\"true\\\") > -1) { return new ApiResponse(\\\"SUCCESS\\\", \\\"\\\"); } else { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } } catch (e) { return new ApiResponse(\\\"ERROR\\\", \\\"Error while processing your request\\\"); } }\" }'
WHERE (`id` = '47');
