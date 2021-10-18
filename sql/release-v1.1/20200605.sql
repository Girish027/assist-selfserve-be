/*CENTRAL-5778: UI fixes (Re-organize left nav)*/
/*left nav skeleton change and label changes

Teams
	Teams Configuration

Skills
	Skills Configuration
	
Queues
	Queues Configuration
	Hours of Operation
	
Advanced Amdin
    Live - Advanced Admin
    Test - Advanced Admin

*/
USE self_serve;
/*insert a new node_group*/
INSERT INTO node_group (id, created_by, created_on, last_updated_by, last_updated_on, title, ui_schema) VALUES ('3', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', 'Skills', '{\"menuGroupName\": \"nav\", \"icon\":\"agentIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}');
INSERT INTO node (id, created_by, created_on, last_updated_by, last_updated_on, node_id, node_type, parent_id) VALUES ('3', 'default.user', '2019-06-13 12:24:46.000000', 'default.user', '2019-06-13 12:24:46.000000', '3', 'GROUP', '-1');
INSERT INTO self_serve.menu (id, created_by, created_on, last_updated_by, last_updated_on, menu_group_name, seq, node_id) VALUES ('3', 'default.user', '2019-06-13 12:24:46.000000', 'defaullt.user', '2019-06-13 12:24:46.000000', 'nav', '1', '3');
/* change of left nav order, label changes*/
UPDATE node_group SET title = 'Teams' WHERE (id = 1);
UPDATE node_group SET title = 'Queues' WHERE (id = 3);
UPDATE node_group SET title = 'Skills' WHERE (id = 2);
UPDATE node_group SET title = 'Advanced Admin' WHERE (id = 4);
UPDATE bookmark SET display_label = 'Test - Advanced Admin' WHERE (id = 1);
UPDATE bookmark SET display_label = 'Live - Advanced Admin' WHERE (id = 2);
/*moving the activities to right places*/
UPDATE node SET parent_id = 2 WHERE (id = 18);
UPDATE node SET parent_id = 2 WHERE (id = 6);
UPDATE node SET parent_id = 2 WHERE (id = 7);
UPDATE node SET parent_id = 3 WHERE (id = 12);
UPDATE node SET parent_id = 3 WHERE (id = 17);
/*label changes*/
UPDATE activity_template SET title = 'Queues Configuration' WHERE (id = 'queues');
UPDATE activity_template SET title = 'Skills Configuration' WHERE (id = 'skills');
UPDATE activity_template SET title = 'Teams Configuration' WHERE (id = 'teams');
