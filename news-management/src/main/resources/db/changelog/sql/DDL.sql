-- quibase formatted sql

--changeset Sergey:1.6
create table news
(
    id       bigserial PRIMARY KEY not null,
    time     time                  not null,
    username varchar(30)           not null,
    title    varchar(50),
    text     varchar(300)
);

create table comment
(
    id       bigserial PRIMARY KEY not null,
    time     time                  not null,
    text     varchar(300)          not null,
    username varchar(30)           not null,
    news_id  bigint,
    CONSTRAINT comment_news_id_fk FOREIGN KEY (news_id)
        REFERENCES news (id)
        ON UPDATE cascade
        ON DELETE cascade
);