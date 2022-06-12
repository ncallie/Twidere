create table user_subscribers
(
    channel_id    int NOT NULL references usr,
    subscriber_id int NOT NULL references usr,
    primary key (channel_id, subscriber_id)
)