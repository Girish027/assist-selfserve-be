use self_serve;

DROP TABLE IF EXISTS `clients_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients_config` (
  `id` int(11) NOT NULL,
  `scope_type` varchar(45) NOT NULL,
  `scope_id` varchar(45) NOT NULL,
  `config_name` varchar(45) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `values` mediumtext,
  `disable` tinyint(4) NOT NULL DEFAULT '0',
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlqcwt9b7oyvvbsvr02xttjoui` (`scope_id`,`scope_type`,`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
