create schema if not exists self_serve;

use self_serve;

--
-- Table structure for table `service`
--
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `id` varchar(255) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `service_urls`
--
DROP TABLE IF EXISTS `service_urls`;
CREATE TABLE `service_urls` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `baseurl` varchar(255) NOT NULL,
  `env` varchar(45) NOT NULL,
  `headers` json DEFAULT NULL,
  `service_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfth4fb7ffwo7emlle3cjp5ld3` (`service_id`),
  CONSTRAINT `FKfth4fb7ffwo7emlle3cjp5ld3` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `entity_template`
--
DROP TABLE IF EXISTS `entity_template`;
CREATE TABLE `entity_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `fetch_for` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Table structure for table `activity_template`
--
DROP TABLE IF EXISTS `activity_template`;
CREATE TABLE `activity_template` (
  `id` varchar(255) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `entity_location` json NOT NULL,
  `model` varchar(45) NOT NULL,
  `promotion_approval` bit(1) NOT NULL,
  `title` varchar(255) NOT NULL,
  `type` varchar(45) NOT NULL,
  `ui_schema` mediumtext,
  `entity_template_id` int(11) NOT NULL,
  `configs` json NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo6aryvmfiyxb97fkeuuun9wls` (`entity_template_id`),
  CONSTRAINT `FKo6aryvmfiyxb97fkeuuun9wls` FOREIGN KEY (`entity_template_id`) REFERENCES `entity_template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `bookmark`
--
DROP TABLE IF EXISTS `bookmark`;
CREATE TABLE `bookmark` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `display_label` varchar(255) NOT NULL,
  `relative_url` varchar(255) NOT NULL,
  `ui_schema` mediumtext,
  `service_url_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcmu448jwmhydl2h54hjhsnhj8` (`service_url_id`),
  CONSTRAINT `FKcmu448jwmhydl2h54hjhsnhj8` FOREIGN KEY (`service_url_id`) REFERENCES `service_urls` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `fetch_config_template`
--
DROP TABLE IF EXISTS `fetch_config_template`;
CREATE TABLE `fetch_config_template` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `fetch_for` varchar(255) NOT NULL,
  `headers` json DEFAULT NULL,
  `params` json DEFAULT NULL,
  `relativeurl` varchar(255) NOT NULL,
  `resp_api_to_resp_ui` json DEFAULT NULL,
  `translator_type` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `service_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK98astysfxby8u3812ebtn1rwl` (`service_id`),
  CONSTRAINT `FK98astysfxby8u3812ebtn1rwl` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `action`
--
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `definition` json DEFAULT NULL,
  `definition_type` varchar(45) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `action_dependencies`
--
DROP TABLE IF EXISTS `action_dependencies`;
CREATE TABLE `action_dependencies` (
  `action_id` int(11) NOT NULL,
  `dependent_action_id` int(11) NOT NULL,
  PRIMARY KEY (`action_id`,`dependent_action_id`),
  KEY `FKtmr869uw3qcxxlx22a8td9qyw` (`dependent_action_id`),
  CONSTRAINT `FKplb9xfj4cpsgvnkm0vwcqebqj` FOREIGN KEY (`action_id`) REFERENCES `action` (`id`),
  CONSTRAINT `FKtmr869uw3qcxxlx22a8td9qyw` FOREIGN KEY (`dependent_action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `node`
--
DROP TABLE IF EXISTS `node`;
CREATE TABLE `node` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `node_id` varchar(255) NOT NULL,
  `node_type` varchar(255) NOT NULL,
  `parent_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `node_group`
--
DROP TABLE IF EXISTS `node_group`;
CREATE TABLE `node_group` (
  `id` varchar(255) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `ui_schema` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `page_template`
--
DROP TABLE IF EXISTS `page_template`;
CREATE TABLE `page_template` (
  `id` varchar(255) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `definition` mediumtext NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `menu`
--
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `menu_group_name` varchar(255) NOT NULL,
  `seq` int(11) NOT NULL,
  `node_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqv8m9sm4gr8545ri1dicr0hic` (`node_id`),
  CONSTRAINT `FKqv8m9sm4gr8545ri1dicr0hic` FOREIGN KEY (`node_id`) REFERENCES `node` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `activity_instance`
--
DROP TABLE IF EXISTS `activity_instance`;
CREATE TABLE `activity_instance` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `account_id` varchar(255) NOT NULL,
  `client_id` varchar(255) NOT NULL,
  `status` varchar(45) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `current_page_template_id` varchar(255) DEFAULT NULL,
  `activity_template_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhmpvacy0u9xmol2qrnubvej0x` (`current_page_template_id`),
  KEY `FK1p7svw33mvhs47y2bhd178rdu` (`activity_template_id`),
  CONSTRAINT `FK1p7svw33mvhs47y2bhd178rdu` FOREIGN KEY (`activity_template_id`) REFERENCES `activity_template` (`id`),
  CONSTRAINT `FKhmpvacy0u9xmol2qrnubvej0x` FOREIGN KEY (`current_page_template_id`) REFERENCES `page_template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `activity_page`
--
DROP TABLE IF EXISTS `activity_page`;
CREATE TABLE `activity_page` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `page_order` int(11) NOT NULL,
  `section_name` varchar(255) DEFAULT NULL,
  `page_template_id` varchar(255) NOT NULL,
  `activity_template_id` varchar(255) NOT NULL,
  `entity_location` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3movkssgh64qpkgj8vqmet2g` (`page_template_id`,`activity_template_id`),
  KEY `FK1co4vsdmk9su3tjqou6kbacn` (`activity_template_id`),
  CONSTRAINT `FK1co4vsdmk9su3tjqou6kbacn` FOREIGN KEY (`activity_template_id`) REFERENCES `activity_template` (`id`),
  CONSTRAINT `FKnanuukv8kij2cekcp8n7oci50` FOREIGN KEY (`page_template_id`) REFERENCES `page_template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `action_workflow`
--
DROP TABLE IF EXISTS `action_workflow`;
CREATE TABLE `action_workflow` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `action_id` int(11) NOT NULL,
  `activity_template_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKth33dheyjuq8pxxhyhg0ia8ak` (`action_id`,`activity_template_id`),
  KEY `FK5wmsagvu71q51i56ry9b8jr9d` (`activity_template_id`),
  CONSTRAINT `FK5wmsagvu71q51i56ry9b8jr9d` FOREIGN KEY (`activity_template_id`) REFERENCES `activity_template` (`id`),
  CONSTRAINT `FKngng8rfpe6mbcc3pay1gcxq74` FOREIGN KEY (`action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `entity_instance`
--
DROP TABLE IF EXISTS `entity_instance`;
CREATE TABLE `entity_instance` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `label` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` varchar(60) NOT NULL,
  `entity_template_id` int(11) DEFAULT NULL,
  `activity_instance_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpnu7st9rtm43loji50326r7sn` (`name`,`activity_instance_id`,`entity_template_id`),
  KEY `FK4cgdi6k575f7og8rl85ot8e7m` (`entity_template_id`),
  KEY `FKn4eysf0mau3jdj1ek4xvdb55n` (`activity_instance_id`),
  CONSTRAINT `FK4cgdi6k575f7og8rl85ot8e7m` FOREIGN KEY (`entity_template_id`) REFERENCES `entity_template` (`id`),
  CONSTRAINT `FKn4eysf0mau3jdj1ek4xvdb55n` FOREIGN KEY (`activity_instance_id`) REFERENCES `activity_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `action_execution_monitor`
--
DROP TABLE IF EXISTS `action_execution_monitor`;
CREATE TABLE `action_execution_monitor` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `env` varchar(45) NOT NULL,
  `request` json DEFAULT NULL,
  `response` json DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `action_id` int(11) NOT NULL,
  `entity_instance_id` int(11) NOT NULL,
  `activity_instance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKawdnywvyk7quur0x45kyagiat` (`action_id`,`activity_instance_id`,`env`,`entity_instance_id`),
  UNIQUE KEY `UK9mw3kv8tyi1qprwer0txcg164` (`action_id`,`env`,`entity_instance_id`),
  KEY `FK3xxfg8osmfh5jy5slhweelt20` (`entity_instance_id`),
  KEY `FKcgv9ybmbia5u56fntag5icoh4` (`activity_instance_id`),
  CONSTRAINT `FK3xxfg8osmfh5jy5slhweelt20` FOREIGN KEY (`entity_instance_id`) REFERENCES `entity_instance` (`id`),
  CONSTRAINT `FKcgv9ybmbia5u56fntag5icoh4` FOREIGN KEY (`activity_instance_id`) REFERENCES `activity_instance` (`id`),
  CONSTRAINT `FKlty3mvf29ca6nwnbjqt85n1ta` FOREIGN KEY (`action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `page_data`
--
DROP TABLE IF EXISTS `page_data`;
CREATE TABLE `page_data` (
  `id` int(11) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `created_on` datetime(6) NOT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `data` json NOT NULL,
  `status` varchar(45) NOT NULL,
  `page_template_id` varchar(255) NOT NULL,
  `activity_instance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc6t25arsayu84oky0nvaijxpi` (`page_template_id`),
  KEY `FK23yuxkwgwdlyak0bd6vijtm4c` (`activity_instance_id`),
  CONSTRAINT `FK23yuxkwgwdlyak0bd6vijtm4c` FOREIGN KEY (`activity_instance_id`) REFERENCES `activity_instance` (`id`),
  CONSTRAINT `FKc6t25arsayu84oky0nvaijxpi` FOREIGN KEY (`page_template_id`) REFERENCES `page_template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
