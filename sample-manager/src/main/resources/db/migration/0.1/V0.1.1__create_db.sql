create table DINOSAUR
(
    id                 varchar(255) NOT NULL PRIMARY KEY,
    customer_id        varchar(255) NOT NULL,
    name               varchar(255) NOT NULL,
    submitted_at       timestamp    NOT NULL,
    modified_at       timestamp,
    status             varchar(255),
    desiredStatus      varchar(255),
    endpoint           varchar(255),
    unique (customer_id, name)
);

create table SHARD
(
    id      varchar(255) NOT NULL PRIMARY KEY
);

ALTER TABLE DINOSAUR ADD shard_id varchar(255);
ALTER TABLE DINOSAUR ADD CONSTRAINT fk_shard FOREIGN KEY (shard_id) REFERENCES SHARD (id);

INSERT INTO SHARD(id) VALUES ('${shard-id}');
