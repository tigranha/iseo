create table users
(
    id         serial primary key,
    first_name text                not null,
    last_name  text                not null,
    username   varchar(255) unique not null,
    password   varchar(255)        not null
);
