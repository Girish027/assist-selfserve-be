use self_serve;

CREATE TABLE `permission` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `app_role` varchar(255) NOT NULL,
  `standard_role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `role_permission` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `permission_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf8yllw1ecvwqy3ehyxawqa1qp` (`permission_id`),
  KEY `FKa6jx8n8xkesmjmv6jqug6bg68` (`role_id`),
  CONSTRAINT `FKa6jx8n8xkesmjmv6jqug6bg68` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKf8yllw1ecvwqy3ehyxawqa1qp` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `permission` values
(1, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'PORTAL_PROMOTION_MONITOR_VIEW'),
(2, 'default.user', '2020-05-28 16:56:30.966717', 'default.user', '2020-05-28 16:56:30.966717', 'PORTAL_CLIENT_PICKER_EDIT'),
(3, 'default.user', '2020-05-28 16:57:26.021872', 'default.user', '2020-05-28 16:57:26.021872', 'ACTIVITY_DRAFT_CREATE'),
(4, 'default.user', '2020-05-28 16:57:37.092017', 'default.user', '2020-05-28 16:57:37.092017', 'ACTIVITY_DRAFT_EDIT'),
(5, 'default.user', '2020-05-28 16:57:47.494907', 'default.user', '2020-05-28 16:57:47.494907', 'ACTIVITY_TEST_PUBLISH'),
(6, 'default.user', '2020-05-28 16:58:13.149272', 'default.user', '2020-05-28 16:58:13.149272', 'ACTIVITY_LIVE_PUBLISH'),
(7, 'default.user', '2020-05-28 16:58:25.762003', 'default.user', '2020-05-28 16:58:25.762003', 'ACTIVITY_DRAFT_DISCARD'),
(8, 'default.user', '2020-05-28 16:58:37.136295', 'default.user', '2020-05-28 16:58:37.136295', 'ACTIVITY_TEST_DISCARD');

insert into role values
(1, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'reader', 'viewer'),
(2, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'tester', 'tester'),
(3, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'configurator', 'developer'),
(4, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'approver', 'operator'),
(5, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'useradmin', 'admin'),
(6, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 'sysadmin', 'partneradmin'),
(7, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', '247admin', '247admin');

insert into role_permission values
(1, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 3, 3),
(2, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 3, 4),
(3, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 4, 3),
(4, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 4, 4),
(5, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 5, 3),
(6, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 5, 4),
(7, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 6, 4),
(8, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 7, 3),
(9, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 7, 4),
(10, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 8, 3),
(11, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 8, 4),
(12, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 1, 1),
(13, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 1, 3),
(14, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 1, 4),
(15, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 3, 5),
(16, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 4, 5),
(17, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 5, 5),
(18, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 6, 5),
(19, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 7, 5),
(20, 'default.user', '2020-05-28 16:54:35.191623', 'default.user', '2020-05-28 16:54:35.191623', 8, 5);
