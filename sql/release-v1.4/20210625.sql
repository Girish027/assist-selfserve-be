use `self_serve`;

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values, entity, context) { try { context  = JSON.parse(context); var date = new Date(); var dateString = (\\\"0\\\" + date.getDate()).slice(-2) + (\\\"0\\\" + (date.getMonth() + 1)).slice(-2) + (\\\"\\\" + date.getYear()).slice(-2); var timeString = (\\\"0\\\" + date.getHours()).slice(-2) + \\\"\\\" + (\\\"0\\\" + date.getMinutes()).slice(-2)+ \\\"\\\" +  (\\\"0\\\" + date.getSeconds()).slice(-2); var accountId = \\\"BulkLiveAgents\\\" + \\\"-\\\" + dateString + \\\"-\\\" + timeString; var uiData = { \\\"accountId\\\": accountId }; } catch (e) {} return JSON.stringify(uiData); }\"}'
WHERE (`id` = '69');
UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values, entity, context) { try { context  = JSON.parse(context); var date = new Date(); var dateString = (\\\"0\\\" + date.getDate()).slice(-2) + (\\\"0\\\" + (date.getMonth() + 1)).slice(-2) + (\\\"\\\" + date.getYear()).slice(-2); var timeString = (\\\"0\\\" + date.getHours()).slice(-2) + \\\"\\\" + (\\\"0\\\" + date.getMinutes()).slice(-2)+ \\\"\\\" +  (\\\"0\\\" + date.getSeconds()).slice(-2); var accountId = \\\"BulkTestAgents\\\" + \\\"-\\\" + dateString + \\\"-\\\" + timeString; var uiData = { \\\"accountId\\\": accountId }; } catch (e) {} return JSON.stringify(uiData); }\"}'
WHERE (`id` = '68');
UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values, entity, context) { try { context  = JSON.parse(context); var date = new Date(); var dateString = (\\\"0\\\" + date.getDate()).slice(-2) + (\\\"0\\\" + (date.getMonth() + 1)).slice(-2) + (\\\"\\\" + date.getYear()).slice(-2); var timeString = (\\\"0\\\" + date.getHours()).slice(-2) + \\\"\\\" + (\\\"0\\\" + date.getMinutes()).slice(-2)+ \\\"\\\" +  (\\\"0\\\" + date.getSeconds()).slice(-2); var accountId = \\\"SmartResponses\\\" + \\\"-\\\" + dateString + \\\"-\\\" + timeString; var uiData = { \\\"accountId\\\": accountId }; } catch (e) {} return JSON.stringify(uiData); }\"}'
WHERE (`id` = '52');
