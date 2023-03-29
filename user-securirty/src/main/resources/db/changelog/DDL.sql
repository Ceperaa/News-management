-- quibase formatted sql

--changeset Sergey:1.6
create table users
(
    id       bigserial PRIMARY KEY,
    username varchar(30) unique not null,
    password varchar(228) default '{noop}123'
);

create table role
(
    id   bigserial PRIMARY KEY,
    role varchar(20)
);

create table users_roles
(
    user_id  int,
    roles_id int,
    constraint users_roles_role_id_id_fk
        foreign key (user_id) references users (id)
            ON UPDATE cascade
            ON DELETE cascade,
    constraint users_roles_user_id_id_fk
        foreign key (roles_id) references role (id)
            ON UPDATE cascade
            ON DELETE cascade
);

insert INTO role(role)
VALUES ('ROLE_JOURNALIST'),
       ('ROLE_SUBSCRIBER'),
       ('ROLE_ADMIN');

insert INTO users(username, password)
VALUES ('username', '{noop}password'),
       ('username1', '{noop}password');

insert INTO users_roles(user_id, roles_id)
VALUES (1, 2);