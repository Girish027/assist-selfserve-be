use `self_serve`;

UPDATE `config` SET `value` = '{ \"chathours_p0\": { \"fetchFor\": \"chathours_p0\", \"status\": [ 404 ] }, \"updateQueues_p0\": { \"fetchFor\": \"updateQueues_p0\", \"status\": [ 404 ] } }' 
WHERE (`code` = 'excluded_fetchfor_list');

UPDATE `fetch_config_template` SET `resp_api_to_resp_ui` = '{\"objectTranslator\": \"function translate(values, entity) { var uiData = { \\\"tabsField\\\": [{}, { \\\"assistConfig\\\": {} }] }; var apiData = { \\\"dispostionFormContent\\\": \\\"\\\" }; try { apiData = JSON.parse(values).data; uiData.tabsField[1].assistConfig.dispositionForm = apiData.dispostionFormContent; } catch (e) { uiData.tabsField[1].assistConfig.dispositionForm = \\\"\\\"; } return JSON.stringify(uiData); }\" }'
WHERE (`id` = '28');
