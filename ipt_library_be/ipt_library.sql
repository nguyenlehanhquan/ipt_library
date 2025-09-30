drop database if exists ipt_library;
create database if not exists ipt_library;

use ipt_library;

create table if not exists users (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    username varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255)
);

insert into users (username, password, role) values
('hanhquan', '123456', 'admin');

create table if not exists books (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    isbn varchar(15) unique,
    description varchar(255) not null,
    author varchar(255)
);

insert into books (isbn, description, author) values
('9780062245946', 'Pinkalicious: Cherry Blossom', 'Victoria Kann'),
('9781443429184', 'Finding Winnie: The True Story of the World''s Most Famous Bear', 'Lindsay Mattick'),
('9780061915109', 'My Heart Is Like a Zoo', 'Michael Hall'),
('9780061152597', 'Yes Day!', 'Amy Krouse Rosenthal'),
('9780061873287', 'Fancy Nancy''s Perfectly Posh Paper Doll Book', 'Jane O''Connor'),
('9780358238393', 'Amazon Adventure', 'Sy Montgomery'),
('9780544813243', 'Perrazo y Perrito / Big Dog and Little Dog', 'Dav Pilkey');

create table if not exists contracts (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    contract_code varchar(255) unique
);

insert into contracts (contract_code) values
('ipt-rrd-12-2023'),
('ipt-rrd-11-2024'),
('ipt-rrd-12-2024'),
('ipt-rrd-sgp-03-2024'),
('ipt-rrd-sgp-07-2025');

create table if not exists archives (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp default CURRENT_TIMESTAMP,
    updater     varchar(50),
    archive_type enum('SAMPLE', 'RECEIVED_ITEM', 'SIGNED_SHEET'),
    shelf varchar(127) not null unique,
    location varchar(127) not null unique
);

insert into archives (archive_type, shelf, location) values
('SAMPLE', 'S.011', '215'),
('SAMPLE', 'S.023', '512'),
('SAMPLE', 'S.035', '613'),
('SAMPLE', 'S.038', '616'),
('SAMPLE', 'S.008', '212'),
('RECEIVED_ITEM', 'K.013', '237'),
('RECEIVED_ITEM', 'TCV.2024.21', '2024.21'),
('RECEIVED_ITEM', 'TCV.2024.22', '2024.22'),
('RECEIVED_ITEM', 'TCV.2025.03', '2025.03'),
('RECEIVED_ITEM', 'TCV.2024.28', '2024.28'),
('SIGNED_SHEET', 'I.003', '123'),
('SIGNED_SHEET', 'I.031', '525'),
('SIGNED_SHEET', 'I.030', '524'),
('SIGNED_SHEET', 'I.036', '624'),
('SIGNED_SHEET', 'I.041', '723');


create table if not exists book_contract (
    id int auto_increment primary key,
    created     timestamp default CURRENT_TIMESTAMP,
    creator     varchar(50),
    updated     timestamp,
    updater     varchar(50),
    book_id int not null,
    contract_id int not null,
    order_number varchar(255) not null,
    customer_code varchar(255) not null,

    sample_quantity int not null,
    sample_archive_location_id int,
    sample_remark varchar(255),

    received_item_quantity int not null,
    received_item_archive_location_id int,
    received_item_remark varchar(255),

    signed_sheet_quantity int not null,
    signed_sheet_archive_location_id int,
    signed_sheet_remark varchar(255),

    delivery_date date,

    foreign key (sample_archive_location_id) references archives(id) on delete cascade,
    foreign key (received_item_archive_location_id) references archives(id) on delete cascade,
    foreign key (signed_sheet_archive_location_id) references archives(id) on delete cascade,

    foreign key (book_id) references books(id) on delete cascade,
    foreign key (contract_id) references contracts(id) on delete cascade
);

insert into book_contract (book_id, contract_id, order_number, customer_code, sample_quantity, sample_archive_location_id, sample_remark, received_item_quantity, received_item_archive_location_id, received_item_remark, signed_sheet_quantity, signed_sheet_archive_location_id, signed_sheet_remark, delivery_date)
values
(1, 1, '903A39093', 'HCP', 2, 1, '', 1, 6, '', 2, 11, '', '2024-01-02'),
(2, 2, '903A69519', 'HCP', 2, 2, '', 1, 7, '', 2, 12, '', '2024-08-19'),
(3, 3, '903A71108', 'HCP', 2, 3, '', 1, 8, '', 2, 13, '', '2024-09-02'),
(4, 4, '903A76042', 'HCP', 2, 4, '', 1, 9, '', 2, 14, '', '2024-10-07'),
(5, 5, '903A89524', 'HCP', 2, 5, '', 1, 10, '', 2, 15, '', '2025-02-17');

alter table users auto_increment = 1;
alter table books auto_increment = 1;
alter table contracts auto_increment = 1;
alter table archives auto_increment = 1;
alter table book_contract auto_increment = 1;


