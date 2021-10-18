use `self_serve`;
INSERT INTO `config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`)
VALUES ('skip_live_promotions', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', 'true');

INSERT INTO `config` (`code`, `created_by`, `created_on`, `status`, `type`, `value`)
VALUES ('mock_success_response', 'default.user', '2018-07-12 00:34:55.000000', '1', 'BACKEND', '{\"response\": \"success\"}');
