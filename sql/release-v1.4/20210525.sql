use `self_serve`;
UPDATE `self_serve`.`fetch_config_template` SET `params` = '{ \"contexts\": [ \"clientId\", \"accountId\", \"componentClientId\", \"componentAccountId\" ], \"constants\": {}, \"queryParams\": {\"queryCriteria\": {\"pageSize\": 25000}} }'
WHERE (`id` = '55');
