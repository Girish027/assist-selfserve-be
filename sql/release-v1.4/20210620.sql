use self_serve;

UPDATE `activity_template` SET `configs` = '{ \"pageSize\": 100, \"subHeader\": { \"BULK\": { \"subHeaderTitle\": \"Create or Update existing Assist users in this screen.\", \"subHeaderDescription\": \"To create or update one user, switch the bulk upload toggle to off.\", \"allowedSubHeaderActions\": [ \"BULK_UPLOAD\" ] }, \"CREATE\": { \"subHeaderTitle\": \"Create or Update existing Assist users in this screen.\", \"subHeaderDescription\": \"To create or update multiple users, switch the bulk upload toggle to on.\", \"allowedSubHeaderActions\": [ \"BULK_UPLOAD\" ] }, \"UPDATE\": {} }, \"publishType\": \"TEST_ONLY\", \"allowedActions\": { \"BULK\": [ \"PUBLISH_TO_TEST\", \"BULK_UPLOAD\" ], \"CREATE\": [ \"PUBLISH_TO_TEST\", \"SAVE_AND_CLOSE\", \"EDIT\", \"BULK_UPLOAD\" ], \"UPDATE\": [ \"PUBLISH_TO_TEST\", \"EDIT\", \"SAVE_AND_CLOSE\" ] }, \"isPaginationEnabled\": true, \"preFetchInputValidation\": \"[^A-Za-z0-9-_ . ,:@]\" }' 
WHERE (`id` = 'userTest');

UPDATE `activity_template` SET `configs` = '{ \"pageSize\": 100, \"subHeader\": { \"BULK\": { \"subHeaderTitle\": \"Create or Update existing Assist users in this screen.\", \"subHeaderDescription\": \"To create or update one user, switch the bulk upload toggle to off.\", \"allowedSubHeaderActions\": [ \"BULK_UPLOAD\" ] }, \"CREATE\": { \"subHeaderTitle\": \"Create or Update existing Assist users in this screen.\", \"subHeaderDescription\": \"To create or update multiple users, switch the bulk upload toggle to on.\", \"allowedSubHeaderActions\": [ \"BULK_UPLOAD\" ] }, \"UPDATE\": {} }, \"publishType\": \"LIVE_ONLY\", \"allowedActions\": { \"BULK\": [ \"PUBLISH_TO_LIVE\", \"BULK_UPLOAD\" ], \"CREATE\": [ \"PUBLISH_TO_LIVE\", \"SAVE_AND_CLOSE\", \"EDIT\", \"BULK_UPLOAD\" ], \"UPDATE\": [ \"PUBLISH_TO_LIVE\", \"EDIT\", \"SAVE_AND_CLOSE\" ] }, \"isPaginationEnabled\": true, \"preFetchInputValidation\": \"[^A-Za-z0-9-_ . ,:@]\" }' 
WHERE (`id` = 'userLive');

UPDATE `fetch_config_template` SET `params` = '{\"contexts\": [\"clientId\", \"accountId\", \"componentClientId\", \"componentAccountId\", \"pageNumber\"], \"constants\": {\"pageNumber\": \"0\", \"pageSize\": 100}, \"queryParams\": {\"queryCriteria\": {\"pageNumber\": \"${pageNumber}\", \"pageSize\": \"${pageSize}\"}}}'
WHERE (`id` = '55');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values) { var entities=[]; var list=[]; try{ values=JSON.parse(values); list=values.data.items; } catch(e){ return e.toString(); } list.forEach(function(item){ entities.push({ name:item.key, label: item.userName, labelState: item.status }); }); var metaData={\\\"totalCount\\\":values.data.count}; return JSON.stringify({\\\"data\\\":entities, \\\"metaData\\\":metaData});}\"}'
WHERE (`id` = '55');

UPDATE `config` SET `value` = '\"updateSmartResponse_p0,listSkill,listUser\"' 
WHERE (`code` = 'exclude_cache_fetchfor');
