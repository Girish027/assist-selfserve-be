-- bugfix invite management fetch (CENTRAL-7712)

use self_serve;

UPDATE `fetch_config_template` SET `params` = '{\"contexts\": [\"clientId\", \"accountId\", \"componentClientId\", \"componentAccountId\", \"entity\"], \"constants\": {}}', `relativeurl` = '/en/hierarchy/rest/clients/${componentClientId}/accounts/${componentAccountId}/invitemanagement/${entity}' 
WHERE (`id` = '42');
