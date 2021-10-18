use `self_serve`;

UPDATE `fetch_config_template` 
SET `resp_api_to_resp_ui` = '{\"objectTranslator\" : \"function translate(values){var entities={enum:[], enumNames:[]}; var list=[]; try{values=JSON.parse(values); list=JSON.parse(values.data.list);}catch(e){return e.toString();} list.forEach(function(item){ entities.enum.push(item.entityBaseData.entityId); entities.enumNames.push(item.entityBaseData.entityDisplayName||item.entityBaseData.entityId); });return JSON.stringify(entities);}\"}'
WHERE (`id` = '10');