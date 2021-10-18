use self_serve;

alter table config modify column value LONGTEXT;

INSERT INTO `config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`) VALUES
('cache_apikey', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', 'e2d1acc451f85921d7d6d6ba6e046d9b'),
('cache_clear_maxtime', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', '60000'),
('cache_evict_on_instance_api', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', 'true');
