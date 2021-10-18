use `self_serve`;

ALTER TABLE `action` ADD COLUMN `configs` json DEFAULT NULL;
ALTER TABLE `action_execution_monitor` ADD COLUMN `execution_meta` json DEFAULT NULL;

UPDATE `action` SET `configs` = "{\"POLL\": {\"type\": \"POLL\", \"retryCount\": 5, \"retryInterval\": 30000}}"
WHERE id = 14;
