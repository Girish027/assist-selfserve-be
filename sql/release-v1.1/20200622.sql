/*CENTRAL-5243 - Generate JWT token for Assist API call : 22-jul-2020*/

use self_serve;

CREATE TABLE `external_service_auth_config` (
  `id` int(11) NOT NULL,
  `name` varchar(255)  NOT NULL,
  `value` varchar(255)  DEFAULT NULL,
  `service_id` varchar(255)  NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255)  DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfth4fb7ffwo7emlle3cjp5ld3` (`service_id`),
  CONSTRAINT `FKq3w2rj7helof5drd9w5rnsi2j` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`) VALUES (1,'JWT_CLAIM_ISS','247-chat-self-serve','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`) VALUES (2,'JWT_CLAIM_EXP','900','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`) VALUES (3,'JWT_CLAIM_ALG','RSA','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`) VALUES (4,'JWT_CLAIM_PRIVATE_KEY','/var/tellme/css/conf/.key','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`) VALUES (5,'JWT_CLAIM_PUBLIC_KEY','/var/tellme/css/conf/.pub','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`) VALUES (6,'JWT_TOKEN_HEADER_KEY','Authorization','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
