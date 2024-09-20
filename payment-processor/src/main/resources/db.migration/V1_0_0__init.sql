DROP TABLE IF EXISTS transaction;
create table transaction(
    "id"                  varchar(60) primary key,
    "opt_lock"            bigint,
    "created_by"          varchar(50),
    "last_modified_by"    varchar(50),
    "created_date"        timestamp not null,
    "last_modified_date"  timestamp not null,
    "amount"              decimal,
    "txn_status"          varchar(150)
);