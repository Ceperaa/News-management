insert INTO users(username, password)
VALUES ('username', '{noop}password'),
       ('username1', '{noop}password');

insert INTO users_roles(user_id, roles_id)
VALUES (1, 2);