use self_serve;

INSERT INTO `action_dependencies` VALUES
(13,12),(14,15),(15,16);


INSERT INTO `action_workflow` VALUES
(1,'default.user','2019-08-27 17:44:01.596456','default.user','2019-08-27 17:44:01.596456',1,'chathours'),
(2,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',2,'tags'),
(4,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',6,'tags'),
(9,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',5,'teams'),
(10,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',4,'teams'),
(13,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',8,'skills'),
(14,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',9,'skills'),
(15,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',12,'queues'),
(16,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',13,'queues'),
(18,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',14,'queues'),
(19,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',15,'queues'),
(20,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',16,'queues'),
(21,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',17,'STALive'),
(22,'default.user','2019-08-27 17:44:15.136579','default.user','2019-08-27 17:44:15.136579',18,'STATest');


INSERT INTO `activity_page` VALUES
(1,'default.user','2019-08-07 14:08:14.129995','default.user','2019-08-07 14:08:14.129995',1,'','createTags_p0','tags',NULL),
(2,'default.user','2019-08-07 16:35:09.859460','default.user','2019-08-07 16:35:09.859460',1,'','chathours_p0','chathours',NULL),
(4,'default.user','2019-08-07 14:08:14.129995','default.user','2019-08-07 14:08:14.129995',1,'','updateTags_p0','tags',NULL),
(6,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','createQueues_p0','queues',NULL),
(7,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','updateQueues_p0','queues',NULL),
(8,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','createTeams_p0','teams',NULL),
(9,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','updateTeams_p0','teams',NULL),
(10,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','updateSkill_p0','skills',NULL),
(11,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','createSkills_p0','skills',NULL),
(12,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','STATest_p0','STATest',NULL),
(13,'default.user','2019-08-08 14:24:45.621475','default.user','2019-08-08 14:24:45.621475',1,'','STALive_p0','STALive',NULL);


INSERT INTO `activity_template` VALUES
('chathours','default.user','2019-08-07 16:23:27.677031','default.user','2019-08-07 16:23:27.677031','Chat Hours and Calendar','{\"UPDATE\": \"chathours_p0.queues\"}','MENU',_binary '\0','Hours of Operation','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',8,'{\"publishType\": \"DEFAULT\"}'),
('queues','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Queue Activity','{\"CREATE\": \"createQueues_p0.tabsField.0.queueName\", \"UPDATE\": \"updateQueues_p0.tabsField.0.queueId\"}','MENU',_binary '\0','Queues Configuration','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',2,'{\"publishType\": \"DEFAULT\"}'),
('skills','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Skill Activity','{\"CREATE\": \"createSkills_p0.skillName\", \"UPDATE\": \"updateSkill_p0.skillId\"}','MENU',_binary '\0','Skills Configuration','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',5,'{\"publishType\": \"DEFAULT\"}'),
('STALive','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Skill to Agents Live','{\"UPDATE\": \"STALive_p0.skillId\"}','MENU',_binary '','Live - Skill to Agents','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',5,'{\"publishType\": \"LIVE_ONLY\"}'),
('STATest','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Skill to Agents Test','{\"UPDATE\": \"STATest_p0.skillId\"}','MENU',_binary '','Test - Skill to Agents','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',5,'{\"publishType\": \"TEST_ONLY\"}'),
('tags','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Tags Activity','{\"CREATE\": \"createTags_p0.tagName\", \"UPDATE\": \"updateTags_p0.tagId\"}','MENU',_binary '\0','Tags Configuration','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',7,'{\"publishType\": \"DEFAULT\"}'),
('teams','default.user','2019-08-07 13:50:29.862585','default.user','2019-08-07 13:50:29.862585','Team Activity','{\"CREATE\": \"createTeams_p0.teamName\", \"UPDATE\": \"updateTeams_p0.teamId\"}','MENU',_binary '\0','Teams Configuration','NON_SINGLETON','{\"menuGroupName\": \"nav\", \"icon\":\"\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}',3,'{\"publishType\": \"DEFAULT\"}');


INSERT INTO `config` VALUES
('max_instances_days_limit','default.user','2018-07-12 00:34:55.000000',NULL,NULL,1,'UI_APP','90');


INSERT INTO `entity_template` VALUES
(1,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294',NULL,'Account'),
(2,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294','listQueue','Queues'),
(3,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294','listTeam','Teams'),
(4,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294',NULL,'Teams'),
(5,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294','listSkill','Skills'),
(7,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294','listTag','Tags'),
(8,'default.user','2019-08-08 14:17:12.404294','default.user','2019-08-08 14:17:12.404294','msgQueue','Messaging Queues');


INSERT INTO `hibernate_sequence` VALUES
(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556),(1556);


INSERT INTO `menu` VALUES
(1,'default.user','2019-06-13 12:24:46.000000','defaullt.user','2019-06-13 12:24:46.000000','nav',1,1),
(2,'default.user','2019-06-13 12:24:46.000000','defaullt.user','2019-06-13 12:24:46.000000','nav',1,2),
(3,'default.user','2019-06-13 12:24:46.000000','defaullt.user','2019-06-13 12:24:46.000000','nav',1,3),
(4,'default.user','2019-06-13 12:24:46.000000','defaullt.user','2019-06-13 12:24:46.000000','nav',1,4),
(5,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','nav',1,5);


INSERT INTO `node` VALUES
(1,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','1','GROUP',-1),
(2,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','2','GROUP',-1),
(3,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','3','GROUP',-1),
(4,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','4','GROUP',-1),
(5,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','5','GROUP',-1),
(11,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','2','BOOKMARK',5),
(12,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','queues','WORKFLOW',1),
(13,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','1','BOOKMARK',5),
(14,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','tags','WORKFLOW',4),
(15,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','teams','WORKFLOW',3),
(17,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','chathours','WORKFLOW',1),
(18,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','skills','WORKFLOW',2),
(19,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','STATest','WORKFLOW',2),
(20,'default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','STALive','WORKFLOW',2);


INSERT INTO `node_group` VALUES
('1','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','Queues','{\"menuGroupName\": \"nav\", \"icon\":\"queueManagementIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}'),
('2','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','Skills','{\"menuGroupName\": \"nav\", \"icon\":\"skillsIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}'),
('3','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','Teams','{\"menuGroupName\": \"nav\", \"icon\":\"teamsIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}'),
('4','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','Tags','{\"menuGroupName\": \"nav\", \"icon\":\"tagsIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}'),
('5','default.user','2019-06-13 12:24:46.000000','default.user','2019-06-13 12:24:46.000000','Advanced Admin','{\"menuGroupName\": \"nav\", \"icon\":\"adminConsoleIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}');


INSERT INTO `permission` VALUES
(1,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','PORTAL_PROMOTION_MONITOR_VIEW'),
(2,'default.user','2020-05-28 16:56:30.966717','default.user','2020-05-28 16:56:30.966717','PORTAL_CLIENT_PICKER_EDIT'),
(3,'default.user','2020-05-28 16:57:26.021872','default.user','2020-05-28 16:57:26.021872','ACTIVITY_DRAFT_CREATE'),
(4,'default.user','2020-05-28 16:57:37.092017','default.user','2020-05-28 16:57:37.092017','ACTIVITY_DRAFT_EDIT'),
(5,'default.user','2020-05-28 16:57:47.494907','default.user','2020-05-28 16:57:47.494907','ACTIVITY_TEST_PUBLISH'),
(6,'default.user','2020-05-28 16:58:13.149272','default.user','2020-05-28 16:58:13.149272','ACTIVITY_LIVE_PUBLISH'),
(7,'default.user','2020-05-28 16:58:25.762003','default.user','2020-05-28 16:58:25.762003','ACTIVITY_DRAFT_DISCARD'),
(8,'default.user','2020-05-28 16:58:37.136295','default.user','2020-05-28 16:58:37.136295','ACTIVITY_TEST_DISCARD');


INSERT INTO `role` VALUES
(1,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','reader','viewer'),
(2,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','tester','tester'),
(3,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','configurator','developer'),
(4,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','approver','operator'),
(5,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','useradmin','admin'),
(6,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','sysadmin','partneradmin'),
(7,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623','247admin','247admin');


INSERT INTO `role_permission` VALUES
(1,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',3,3),
(2,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',3,4),
(3,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',4,3),
(4,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',4,4),
(5,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',5,3),
(6,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',5,4),
(7,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',6,4),
(8,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',7,3),
(9,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',7,4),
(10,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',8,3),
(11,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',8,4),
(12,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',1,1),
(13,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',1,3),
(14,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',1,4),
(15,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',3,5),
(16,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',4,5),
(17,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',5,5),
(18,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',6,5),
(19,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',7,5),
(20,'default.user','2020-05-28 16:54:35.191623','default.user','2020-05-28 16:54:35.191623',8,5);


INSERT INTO `service` VALUES
('adminConsole','default.user','2019-06-13 12:11:09.000000','default.user','2019-06-13 12:11:09.000000','Admin Console','Admin Console'),
('assist','default.user','2019-06-13 12:11:09.000000','default.user','2019-06-13 12:11:09.000000','Assist call','Assist'),
('cobp','default.user','2019-06-03 19:42:21.000000','default.user','2019-06-03 19:42:21.000000','COBP','COBP'),
('ohs','default.user','2019-06-03 19:42:21.000000','default.user','2019-06-03 19:42:21.000000','Org Hierarchy Service','Org Hierarchy Service'),
('tie','default.user','2019-06-13 12:11:09.000000','default.user','2019-06-13 12:11:09.000000','TIE Tag download','TIE Tags'),
('tieAction','default.user','2019-06-13 12:11:09.000000','default.user','2019-06-13 12:11:09.000000','TIE Action calls','TIE Action');


INSERT INTO `service_urls` VALUES
(1,'default.user','2019-06-06 12:26:36.000000','default.user','2019-06-06 12:26:36.000000','http://localhost/cobp-service','TEST','{}','cobp'),
(2,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://central.247-inc.net','TEST','{}','tie'),
(3,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://staging.api.247-inc.net','TEST','{\"apikey\": \"8UjizUNm8jcN4lZgsReIgUGXeqbctjYT\"}','ohs'),
(4,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://central.247-inc.net/adminconsole/rest/domainwhitelist/test','TEST','{}','tieAction'),
(5,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://${clientId}.portal.assist.staging.247-inc.net','TEST','{}','assist'),
(6,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://staging.api.247-inc.net','LIVE','{\"apikey\": \"8UjizUNm8jcN4lZgsReIgUGXeqbctjYT\"}','ohs'),
(7,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://central.247-inc.net/adminconsole/rest/domainwhitelist/live','LIVE','{}','tieAction'),
(8,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://${clientId}.portal.assist.staging.247-inc.net','LIVE','{}','assist'),
(9,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://{clientId}.portal.assist.staging.247-inc.net','TEST','{}','adminConsole'),
(10,'default.user','2019-06-13 12:13:02.000000','default.user','2019-06-13 12:13:02.000000','https://{clientId}.portal.assist.staging.247-inc.net','LIVE','{}','adminConsole');
