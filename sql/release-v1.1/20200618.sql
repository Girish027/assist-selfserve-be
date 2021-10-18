/*Add icons for Teams, Tags and Skills activities*/
use self_serve;

UPDATE node_group SET ui_schema = '{\"menuGroupName\": \"nav\", \"icon\":\"teamsIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}' WHERE (id = 1);
UPDATE node_group SET ui_schema = '{\"menuGroupName\": \"nav\", \"icon\":\"tagsIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}' WHERE (id = 2);
UPDATE node_group SET ui_schema = '{\"menuGroupName\": \"nav\", \"icon\":\"skillsIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}' WHERE (id = 3);
UPDATE node_group SET ui_schema = '{\"menuGroupName\": \"nav\", \"icon\":\"queueManagementIcon\", \"toolTip\":\"\", \"dashboard\":true, \"display_order\":1, \"isActive\":true}' WHERE (id = 4);

/*update for entity Template Names*/
UPDATE entity_template SET name = 'Account' WHERE (id = 1);
UPDATE entity_template SET name = 'Queue' WHERE (id = 2);
UPDATE entity_template SET name = 'Team' WHERE (id = 3);
UPDATE entity_template SET name = 'Team' WHERE (id = 4);
UPDATE entity_template SET name = 'Skill' WHERE (id = 5);
UPDATE entity_template SET name = 'Tag' WHERE (id = 7);