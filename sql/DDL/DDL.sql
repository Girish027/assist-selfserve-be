-- -----------------------------------------------------
-- Schema self_serve
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `self_serve` ;

-- -----------------------------------------------------
-- Schema self_serve
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `self_serve` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `self_serve` ;

-- -----------------------------------------------------
-- Table `self_serve`.`action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`action` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`action` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `definition` JSON NULL DEFAULT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`action_dependencies`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`action_dependencies` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`action_dependencies` (
  `action_id` INT(11) NOT NULL,
  `dependent_action_id` INT(11) NOT NULL,
  PRIMARY KEY (`action_id`, `dependent_action_id`),
  INDEX `FKtmr869uw3qcxxlx22a8td9qyw` (`dependent_action_id` ASC),
  CONSTRAINT `FKplb9xfj4cpsgvnkm0vwcqebqj`
    FOREIGN KEY (`action_id`)
    REFERENCES `self_serve`.`action` (`id`),
  CONSTRAINT `FKtmr869uw3qcxxlx22a8td9qyw`
    FOREIGN KEY (`dependent_action_id`)
    REFERENCES `self_serve`.`action` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`workflow_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`workflow_template` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`workflow_template` (
  `id` VARCHAR(255) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `model` VARCHAR(45) NOT NULL,
  `promotion_approval` BIT(1) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`page_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`page_template` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`page_template` (
  `id` VARCHAR(255) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `definition` JSON NOT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `title` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`workflow_instance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`workflow_instance` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`workflow_instance` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `account_id` VARCHAR(255) NOT NULL,
  `client_id` VARCHAR(255) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `current_page_template_id` VARCHAR(255) NULL DEFAULT NULL,
  `workflow_template_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKfilu3eiwd43x07l31kiirvdi3` (`current_page_template_id` ASC),
  INDEX `FK3hyxkxkegbjjhaoat9iuj2fgs` (`workflow_template_id` ASC),
  CONSTRAINT `FK3hyxkxkegbjjhaoat9iuj2fgs`
    FOREIGN KEY (`workflow_template_id`)
    REFERENCES `self_serve`.`workflow_template` (`id`),
  CONSTRAINT `FKfilu3eiwd43x07l31kiirvdi3`
    FOREIGN KEY (`current_page_template_id`)
    REFERENCES `self_serve`.`page_template` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`action_execution_monitor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`action_execution_monitor` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`action_execution_monitor` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `env` VARCHAR(45) NOT NULL,
  `response` LONGTEXT NULL DEFAULT NULL,
  `status` VARCHAR(45) NOT NULL,
  `action_id` INT(11) NOT NULL,
  `workflow_instance_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK3skkhmt3parsimx97qnt0ss8l` (`action_id` ASC, `workflow_instance_id` ASC, `env` ASC),
  INDEX `FK4x68wvm12loprv2m4x1o0kv2s` (`workflow_instance_id` ASC),
  CONSTRAINT `FK4x68wvm12loprv2m4x1o0kv2s`
    FOREIGN KEY (`workflow_instance_id`)
    REFERENCES `self_serve`.`workflow_instance` (`id`),
  CONSTRAINT `FKlty3mvf29ca6nwnbjqt85n1ta`
    FOREIGN KEY (`action_id`)
    REFERENCES `self_serve`.`action` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`action_field_lookup`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`action_field_lookup` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`action_field_lookup` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `field` VARCHAR(255) NOT NULL,
  `path` VARCHAR(255) NOT NULL,
  `page_template_id` VARCHAR(255) NOT NULL,
  `workflow_template_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKjdjtwoi4bqd8han5qj3hlhefm` (`field` ASC, `workflow_template_id` ASC, `page_template_id` ASC),
  INDEX `FKadwakhhlovwshpt8glp16119r` (`page_template_id` ASC),
  INDEX `FKdr80k4guxsf499p4yij70f5xr` (`workflow_template_id` ASC),
  CONSTRAINT `FKadwakhhlovwshpt8glp16119r`
    FOREIGN KEY (`page_template_id`)
    REFERENCES `self_serve`.`page_template` (`id`),
  CONSTRAINT `FKdr80k4guxsf499p4yij70f5xr`
    FOREIGN KEY (`workflow_template_id`)
    REFERENCES `self_serve`.`workflow_template` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`action_workflow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`action_workflow` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`action_workflow` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `action_id` INT(11) NOT NULL,
  `workflow_template_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKj46tj3ee2qt3kqirk3mipsknt` (`action_id` ASC, `workflow_template_id` ASC),
  INDEX `FKt65ycjwvry0ayr8en0d7n7cn5` (`workflow_template_id` ASC),
  CONSTRAINT `FKngng8rfpe6mbcc3pay1gcxq74`
    FOREIGN KEY (`action_id`)
    REFERENCES `self_serve`.`action` (`id`),
  CONSTRAINT `FKt65ycjwvry0ayr8en0d7n7cn5`
    FOREIGN KEY (`workflow_template_id`)
    REFERENCES `self_serve`.`workflow_template` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`audit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`audit` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`audit` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `event_data` JSON NOT NULL,
  `event_type` VARCHAR(45) NOT NULL,
  `host_name` VARCHAR(255) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`service`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`service` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`service` (
  `id` VARCHAR(255) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `description` VARCHAR(1024) NULL DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`fetch_config_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`fetch_config_template` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`fetch_config_template` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `fetch_for` VARCHAR(255) NOT NULL,
  `headers` JSON NULL DEFAULT NULL,
  `params` JSON NULL DEFAULT NULL,
  `relativeurl` VARCHAR(255) NOT NULL,
  `resp_api_to_resp_ui` JSON NULL DEFAULT NULL,
  `type` VARCHAR(45) NOT NULL,
  `service_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK98astysfxby8u3812ebtn1rwl` (`service_id` ASC),
  CONSTRAINT `FK98astysfxby8u3812ebtn1rwl`
    FOREIGN KEY (`service_id`)
    REFERENCES `self_serve`.`service` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`hibernate_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`hibernate_sequence` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`hibernate_sequence` (
  `next_val` BIGINT(20) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`page_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`page_data` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`page_data` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `data` JSON NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `page_template_id` VARCHAR(255) NOT NULL,
  `workflow_instance_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKc6t25arsayu84oky0nvaijxpi` (`page_template_id` ASC),
  INDEX `FKl9xie1hf7r4iedxjhoh3bsseh` (`workflow_instance_id` ASC),
  CONSTRAINT `FKc6t25arsayu84oky0nvaijxpi`
    FOREIGN KEY (`page_template_id`)
    REFERENCES `self_serve`.`page_template` (`id`),
  CONSTRAINT `FKl9xie1hf7r4iedxjhoh3bsseh`
    FOREIGN KEY (`workflow_instance_id`)
    REFERENCES `self_serve`.`workflow_instance` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`service_urls`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`service_urls` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`service_urls` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `baseurl` VARCHAR(255) NOT NULL,
  `env` VARCHAR(45) NOT NULL,
  `service_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKfth4fb7ffwo7emlle3cjp5ld3` (`service_id` ASC),
  CONSTRAINT `FKfth4fb7ffwo7emlle3cjp5ld3`
    FOREIGN KEY (`service_id`)
    REFERENCES `self_serve`.`service` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`workflow_access`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`workflow_access` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`workflow_access` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `access_role` VARCHAR(255) NOT NULL,
  `workflow_template_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKtepj3rf2bowtrb9pfsocn18dq` (`workflow_template_id` ASC),
  CONSTRAINT `FKtepj3rf2bowtrb9pfsocn18dq`
    FOREIGN KEY (`workflow_template_id`)
    REFERENCES `self_serve`.`workflow_template` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `self_serve`.`workflow_page`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `self_serve`.`workflow_page` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `self_serve`.`workflow_page` (
  `id` INT(11) NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,
  `created_on` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(255) NULL DEFAULT NULL,
  `last_updated_on` DATETIME NULL DEFAULT NULL,
  `page_order` INT(11) NOT NULL,
  `section_name` VARCHAR(255) NULL DEFAULT NULL,
  `page_template_id` VARCHAR(255) NOT NULL,
  `workflow_template_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKg615bov2kfhlqrjqr3cx0tdwj` (`page_template_id` ASC, `workflow_template_id` ASC),
  INDEX `FKcv0rejdr78epkkl3qb6lfjybr` (`workflow_template_id` ASC),
  CONSTRAINT `FK55dfi391jxkuk29xnh7m11bab`
    FOREIGN KEY (`page_template_id`)
    REFERENCES `self_serve`.`page_template` (`id`),
  CONSTRAINT `FKcv0rejdr78epkkl3qb6lfjybr`
    FOREIGN KEY (`workflow_template_id`)
    REFERENCES `self_serve`.`workflow_template` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
