use self_serve;
INSERT INTO `config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`) VALUES ('promotion_monitor_configs', 'default', '2021-05-13 03:15:55.000000', '1', 'UI_APP', '{\"refreshIntervalInSeconds\": 30, \"showRefreshButton\": false, autoRefreshHelpType: \"NEXT_AUTO_REFRESH_VISUALIZATION_LAST_REFRESHED\"}');
INSERT INTO `config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`) VALUES ('max_instances_refresh_minutes_limit', 'default.user', '2021-05-13 12:02:55.000000', '1', 'BACKEND', '6');

ALTER TABLE `config`
ADD COLUMN `class_type` VARCHAR(255) NULL AFTER `value`;

UPDATE `config` SET `class_type` = 'java.util.Map'
WHERE (`code` = 'promotion_monitor_configs');
