
select * from users;
select * from role;
select * from project;
select * from role_user;

insert into role(id, title, status, project_id, date_created, created_by_id)
values('role1001', 'projectadmin', 1, 'proj1001', now(), 'user1001')

insert into project(id, title, description, date_created, created_by_id)
values('proj1001', 'ADOPT', 'reporting application', now(), 'user1001')

insert into role_user(id, user_id, role_id, project_id)
values('rluser1001', 'user1001', 'role1001', 'proj1001')

ALTER TABLE role_user
  DROP FOREIGN KEY fk_role_user_1

ALTER TABLE role_user
ADD CONSTRAINT fk_role_user_1
FOREIGN KEY (role_id)
REFERENCES role(id)