use self_serve;

INSERT INTO `config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`) VALUES
('cache_expire_config', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', '[{"url":"http://stable-client-prov01.web.shared.int.sv2.247-inc.net/self-serve/api/api-cache/expire", "method": "POST", "headers": {"Authorization": "Bearer e2d1acc451f85921d7d6d6ba6e046d9b"}, "body": {}},{"url":"http://stable-client-prov02.web.shared.int.sv2.247-inc.net/self-serve/api/api-cache/expire", "method": "POST", "headers": {"Authorization": "Bearer e2d1acc451f85921d7d6d6ba6e046d9b"}, "body": {}}]');

