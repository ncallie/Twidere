insert into usr (id, username, password, email, active)
values (1, 'admin', '123456789', 'admin@gmail.com', true);

insert into user_role (user_id, roles)
values (1, 'ADMIN'),
       (1, 'USER');