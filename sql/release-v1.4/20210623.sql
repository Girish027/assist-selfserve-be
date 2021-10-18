use `self_serve`;
DELETE FROM `activity_page` WHERE (`id` = '1');
DELETE FROM `activity_page` WHERE (`id` = '4');

DELETE FROM `page_data` where page_template_id ='createTags_p0';
DELETE FROM `page_data` where page_template_id ='updateTags_p0';

DELETE FROM `action_workflow` WHERE (`id` = '2');
DELETE FROM `action_workflow` WHERE (`id` = '4');

DELETE FROM `action_execution_monitor` WHERE action_id='2';
DELETE FROM `action_execution_monitor` WHERE action_id='6';

DELETE FROM `action` WHERE (`id` = '2');
DELETE FROM `action` WHERE (`id` = '6');


DELETE FROM `entity_instance` where entity_template_id=7;
DELETE FROM `entity_id_lookup` where entity_template_id=7;

DELETE FROM `activity_instance` where activity_template_id='tags';
DELETE FROM `activity_template` WHERE (`id` = 'tags');

DELETE FROM `node_group` WHERE (`id` = '6');
DELETE FROM `menu` WHERE (`id` = '5');
DELETE FROM `node` WHERE (`id` = '14');
DELETE FROM `node` WHERE (`id` = '5');

use self_serve;
-- Updating the clients configs to blacklist tags
UPDATE `clients_config` SET `values` = '[\"5\"]' WHERE (`id` = '1');
UPDATE `clients_config` SET `values` = '[\"5\"]' WHERE (`id` = '3');
UPDATE `clients_config` SET `values` = '[\"5\"]' WHERE (`id` = '5');
UPDATE `clients_config` SET `values` = '[\"5\"]' WHERE (`id` = '9');
UPDATE `clients_config` SET `values` = '[\"5\"]' WHERE (`id` = '11');
UPDATE `clients_config` SET `values` = '[\"5\"]' WHERE (`id` = '23');
UPDATE `clients_config` SET `values` = '[\"1\", \"7\"]' WHERE (`id` = '27');
UPDATE `clients_config` SET `values` = '[\"1\",\"7\"]' WHERE (`id` = '29');
UPDATE `clients_config` SET `values` = '[\"7\"]' WHERE (`id` = '31');

UPDATE `action` SET `response_definition` = '{ \"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request, Please retry again or contact Administrator, if issue persistst\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.status == \'SUCCESS\' ){ return new ApiResponse(uiData.status); } else { return new ApiResponse(\'ERROR\',\'Error while processing your request, Please retry again or contact Administrator, if issue persists\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request, Please retry again or contact Administrator, if issue persists\'); } }\" }'
WHERE (`id` = '13');
UPDATE `action` SET `response_definition` = '{\"entityResponseTranslator\": \"var ApiResponse = function(_status, _message){ this.status = _status; this.message = _message; }; function translate(responseData) { try{ if(!responseData){ return new ApiResponse(\'ERROR\', \'Error while processing your request\'); } var uiData = JSON.parse(responseData); if(uiData != null && uiData.status == \'SUCCESS\' ){ return new ApiResponse(uiData.status); } else { return new ApiResponse(\'ERROR\', \'Error while processing your request, Please retry again or contact Administrator, if issue persists\'); } }catch(e){ return new ApiResponse(\'ERROR\', \'Error while processing your request, Please retry again or contact Administrator, if issue persists\'); } }\"}' 
WHERE (`id` = '15');


UPDATE `entity_template` SET `name` = 'Context Validations' WHERE (`id` = '19');
UPDATE `entity_template` SET `name` = 'Customer Context' WHERE (`id` = '13');

DELETE FROM `fetch_config_template` WHERE (`id` = '1');
DELETE FROM `fetch_config_template` WHERE (`id` = '2');
DELETE FROM `fetch_config_template` WHERE (`id` = '7');
DELETE FROM `fetch_config_template` WHERE (`id` = '22');
DELETE FROM `fetch_config_template` WHERE (`id` = '23');
