use `self_serve`;

INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`)
VALUES (4,'JWT_CLAIM_PRIVATE_KEY_TEST','/var/tellme/css/conf/.key','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);

INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`)
VALUES (5,'JWT_CLAIM_PUBLIC_KEY_TEST','/var/tellme/css/conf/.pub','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);

INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`)
VALUES (6,'JWT_TOKEN_HEADER_KEY','Authorization','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);

INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`)
VALUES (7,'JWT_CLAIM_PRIVATE_KEY_LIVE','/var/tellme/css/conf/.key','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);

INSERT INTO `external_service_auth_config` (`id`,`name`,`value`,`service_id`,`created_by`,`created_on`,`last_updated_by`,`last_updated_on`)
VALUES (8,'JWT_CLAIM_PUBLIC_KEY_LIVE','/var/tellme/css/conf/.pub','assist','default.user','2020-07-12 00:34:55.000000',NULL,NULL);
