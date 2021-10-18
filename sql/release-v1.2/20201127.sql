use `self_serve`;

ALTER TABLE activity_instance
ADD COLUMN `hidden` bit(1) NOT NULL DEFAULT b'0';
