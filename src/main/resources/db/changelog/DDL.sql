-- quibase formatted sql

--changeset Sergey:1.6
create table news
(
    id       bigserial PRIMARY KEY not null,
    time     time                  not null,
    username varchar(30)           not null,
    title    varchar(30),
    text     varchar(30)
);

create table comment
(
    id       bigserial PRIMARY KEY not null,
    time     time                  not null,
    text     varchar(100)          not null,
    username varchar(30)           not null,
    news_id  bigint,
    CONSTRAINT comment_news_id_fk FOREIGN KEY (news_id)
        REFERENCES public.news (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

create table users
(
    id       bigserial PRIMARY KEY,
    username varchar(30),
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
        foreign key (user_id) references users (id),
    constraint users_roles_user_id_id_fk
        foreign key (roles_id) references role (id)
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