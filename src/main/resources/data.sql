delete from role;
delete from user;
delete from hibernate_sequence;
delete from user_roles;

insert into role(id,name) values (1, "Admin");
insert into role(id, name) values (2 , "User");

insert into user(id, name, username, password) values (1, "raffay hussain","raffayhusayn", "password" );
insert into user(id, name, username, password) values (2, "ayesha syeda","ayyesha", "hmhmhm" );

insert into user_roles(user_id, roles_id) values(2, 1);
insert into user_roles(user_id, roles_id) values(2, 2);
insert into user_roles(user_id, roles_id) values(1, 2);


