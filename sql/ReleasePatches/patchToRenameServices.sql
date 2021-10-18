
SET SQL_SAFE_UPDATES = 0;

delete FROM fetch_config_template;
delete FROM service_urls;
delete FROM service;

INSERT INTO `service` VALUES ('cobp','default.user','2019-06-03 19:42:21.000000','default.user','2019-06-03 19:42:21.000000','COBP','COBP'),('tie','default.user','2019-06-13 12:11:09.000000','default.user','2019-06-13 12:11:09.000000','TIE','TIE');
INSERT INTO `fetch_config_template` VALUES (1,'default.user','2019-06-03 19:45:44.000000','default.user','2019-06-03 19:45:44.000000','p0','{}','{\"contexts\": [\"clientId\", \"accountId\", \"productName\"], \"constants\": {}}','/getClientMetaData?clientName=${clientId}&accountName=${accountId}&productNames=${productName}','[{\"name\": \"cobp.accountName\", \"uiRespPath\": \"accountDetails.accountName\", \"apiRespPath\": \"name\"}, {\"name\": \"cobp.accountVertical\", \"uiRespPath\": \"accountDetails.accountVertical\", \"apiRespPath\": \"accounts[0].basicInfo.vertical\"}, {\"name\": \"cobp.timezone\", \"uiRespPath\": \"accountDetails.timezone\", \"apiRespPath\": \"accounts[0].basicInfo.timeZone\"}, {\"name\": \"cobp.language\", \"uiRespPath\": \"accountDetails.language\", \"apiRespPath\": \"accounts[0].basicInfo.locales\"}, {\"name\": \"cobp.currency\", \"uiRespPath\": \"accountDetails.currency\", \"apiRespPath\": \"\"}, {\"name\": \"cobp.adminName\", \"uiRespPath\": \"accountDetails.adminName\", \"apiRespPath\": \"accounts[0].adminDetails.adminName\"}, {\"name\": \"cobp.adminEmailId\", \"uiRespPath\": \"accountDetails.adminEmailId\", \"apiRespPath\": \"accounts[0].adminDetails.adminEmail\"}, {\"name\": \"cobp.products\", \"uiRespPath\": \"productDetails.products\", \"apiRespPath\": \"accounts[0].package.products[*].name\"}]','PAGE_DATA_VALUE','cobp'),(2,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','p1','{}','{\"contexts\": [\"clientId\", \"accountId\"], \"constants\": {\"env\": \"DEV\"}}','/adminconsole/rest/tagsfile?client=${clientId}&account=${accountId}&environment=${env}','[{\"name\": \"tie.stageTag\", \"uiRespPath\": \"stageTag\", \"apiRespPath\": \"jsContent\"}]','PAGE_DATA_VALUE','tie'),(3,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','p1','{}','{\"contexts\": [\"clientId\", \"accountId\"], \"constants\": {\"env\": \"PRODUCTION\"}}','/adminconsole/rest/tagsfile?client=${clientId}&account=${accountId}&environment=${env}','[{\"name\": \"tie.prodTag\", \"uiRespPath\": \"productionTag\", \"apiRespPath\": \"jsContent\"}]','PAGE_DATA_VALUE','tie');
INSERT INTO `service_urls` VALUES (1,'default.user','2019-06-06 12:26:36.000000','default.user','2019-06-06 12:26:36.000000','https://stable.api.sv2.247-inc.net/cobp-service','TEST','cobp'),(2,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','http://dev-predictioncon02.app.shared.int.sv2.247-inc.net','TEST','tie');
