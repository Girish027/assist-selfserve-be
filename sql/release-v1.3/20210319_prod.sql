USE self_serve;

CREATE TABLE `clients_config` (
  `id` int NOT NULL,
  `scope_type` varchar(45) NOT NULL,
  `scope_id` varchar(45) NOT NULL,
  `config_name` varchar(45) DEFAULT NULL,
  `description` varchar(200),
  `values` mediumtext DEFAULT NULL,
  `disable` tinyint NOT NULL DEFAULT '0',
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('1', 'client', 'test-default', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('2', 'client', 'test-default', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('3', 'client', 'salesdemo', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('4', 'client', 'salesdemo', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('5', 'client', 'all', 'BLACKLISTED_GROUPS', 'Default UI access for clients to groups ', '[\"1\", \"7\"]', '0', 'default.user', '2021-03-22 11:18:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('6', 'client', 'all', 'BLACKLISTED_ACTIVITIES', 'Default UI access for clients to activities', '[]', '0', 'default.user', '2021-03-22 11:08:14.129995');

INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('7', 'client', 'ectraining', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('8', 'client', 'ectraining', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('9', 'client', 'pedemo', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('10', 'client', 'pedemo', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('11', 'client', 'tfsinc', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('12', 'client', 'tfsinc', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('13', 'client', 'tfscorp', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('14', 'client', 'tfscorp', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('15', 'client', 'tfscustomer', 'BLACKLISTED_GROUPS', 'Disabling UI access to particular groups ', '[\"5\"]', '0', 'default.user', '2021-03-22 11:30:15.129995');
INSERT INTO `clients_config` (`id`, `scope_type`, `scope_id`, `config_name`, `description`, `values`, `disable`, `created_by`, `created_on`) VALUES ('16', 'client', 'tfscustomer', 'BLACKLISTED_ACTIVITIES', 'Disabling UI access to particular activities ', '[]', '0', 'default.user', '2021-03-22 11:10:14.129995');
