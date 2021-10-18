-- remove admin menu
use self_serve;

delete from node where id in (5, 11, 13);
delete from menu where id = 5;
delete from node_group where id = 5;