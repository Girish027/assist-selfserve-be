/*update for entity Template Names*/
UPDATE entity_template SET name = 'Account' WHERE (id = 1);
UPDATE entity_template SET name = 'Queues' WHERE (id = 2);
UPDATE entity_template SET name = 'Teams' WHERE (id = 3);
UPDATE entity_template SET name = 'Teams' WHERE (id = 4);
UPDATE entity_template SET name = 'Skills' WHERE (id = 5);
UPDATE entity_template SET name = 'Tags' WHERE (id = 7);

/*rearranging dashboard tiles*/ 
UPDATE node_group SET id = 14 WHERE (id = 4);
UPDATE node_group SET id = 13 WHERE (id = 3);
UPDATE node_group SET id = 11 WHERE (id = 1);
UPDATE node_group SET id = 12 WHERE (id = 2);
UPDATE node_group SET id = 1 WHERE (id = 14);
UPDATE node_group SET id = 2 WHERE (id = 13);
UPDATE node_group SET id = 3 WHERE (id = 11);
UPDATE node_group SET id = 4 WHERE (id = 12);

/*Rearranging activities among the node_GROUPS*/
UPDATE node SET parent_id = 1 WHERE (id = 12);
UPDATE node SET parent_id = 4 WHERE (id = 14);
UPDATE node SET parent_id = 3 WHERE (id = 15);
UPDATE node SET parent_id = 1 WHERE (id = 17);
UPDATE node SET parent_id = 2 WHERE (id = 18);
UPDATE node SET parent_id = 2 WHERE (id = 19);
UPDATE node SET parent_id = 2 WHERE (id = 20);
