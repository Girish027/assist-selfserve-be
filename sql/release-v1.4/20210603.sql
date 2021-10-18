use `self_serve`;
UPDATE `config` SET `value` = '{\"refreshIntervalInSeconds\": 30, \"showRefreshButton\": true, \"autoRefreshHelpType\": \"NEXT_AUTO_REFRESH_VISUALIZATION_TIMER\"}'
WHERE (`code` = 'promotion_monitor_configs');
