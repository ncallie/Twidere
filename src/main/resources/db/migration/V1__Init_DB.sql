create sequence hibernate_sequence start 2 increment 1;

create table usr (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    email varchar(255) UNIQUE,
    active boolean NOT NULL
);

create table message (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    filename varchar(255),
    tag varchar(200) NOT NULL,
    text varchar(255) NOT NULL,
    user_id int REFERENCES usr(id) ON DELETE CASCADE
);

create table user_role (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id int REFERENCES usr(id) ON DELETE CASCADE,
    roles varchar(255) NOT NULL
);

