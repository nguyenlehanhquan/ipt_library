drop database if exists ipt_library;
create database if not exists ipt_library;

use ipt_library;

drop table if exists users;
create table if not exists users (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    full_name varchar(255),
    username varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255) not null
);

drop trigger add_creator_on_change;
DELIMITER $$ CREATE Trigger add_creator_on_change  BEFORE INSERT ON users  FOR EACH ROW BEGIN IF NEW.creator = '' or NEW.creator is null THEN SET NEW.creator = NEW.username; END IF;  end $$ DELIMITER ;

drop table if exists books;
create table if not exists books (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    isbn varchar(15) unique,
    description varchar(255) not null
);

drop table if exists contracts;
create table if not exists contracts (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    contract_code varchar(255) unique
);

drop table if exists archives;
create table if not exists archives (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    archive_type enum('SAMPLE', 'RECEIVED_ITEM', 'SIGNED_SHEET'),
    shelf varchar(127) not null unique,
    location varchar(127) unique
);

drop table if exists book_contract;
create table if not exists book_contract (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp,
    updater     varchar(50),
    book_id int not null,
    contract_id int not null,
    order_number varchar(255) not null,
    customer_code varchar(255),

    sample_archive_location_id int,
    sample_quantity int,
    sample_remark varchar(255),

    received_item_archive_location_id int,
    received_item_quantity int,
    received_item_remark varchar(255),

    signed_sheet_archive_location_id int,
    signed_sheet_quantity int,
    signed_sheet_remark varchar(255),

    delivery_date date,

    foreign key (sample_archive_location_id) references archives(id) on delete cascade,
    foreign key (received_item_archive_location_id) references archives(id) on delete cascade,
    foreign key (signed_sheet_archive_location_id) references archives(id) on delete cascade,

    foreign key (book_id) references books(id) on delete cascade,
    foreign key (contract_id) references contracts(id) on delete cascade
);

alter table users auto_increment = 1;
alter table books auto_increment = 1;
alter table contracts auto_increment = 1;
alter table archives auto_increment = 1;
alter table book_contract auto_increment = 1;


