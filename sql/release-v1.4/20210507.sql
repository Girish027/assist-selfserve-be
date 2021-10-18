-- Pre fetch validation for agents (https://247inc.atlassian.net/browse/CENTRAL-7678)

use self_serve;

UPDATE `activity_template` SET `configs` = '{\"publishType\": \"LIVE_ONLY\", \"preFetchInputValidation\": \"[^A-Za-z0-9-_ . ,:@]\"}' 
WHERE (`id` = 'userLive');
UPDATE `activity_template` SET `configs` = '{\"publishType\": \"TEST_ONLY\", \"preFetchInputValidation\": \"[^A-Za-z0-9-_ . ,:@]\"}' 
WHERE (`id` = 'userTest');

INSERT INTO `self_serve`.`config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`) VALUES ('component_list', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', '\"ohs,assist,vcc\"');

