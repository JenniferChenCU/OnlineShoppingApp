create database if not exists personal_project2;

use personal_project2;

drop table if exists user;
create table user (
	id int primary key auto_increment,
    username text not null,
    email text not null,
    password text not null,
    isSeller boolean
);

insert into User 
(username, email, password, isSeller) values
("jenniferchen", "jenniferchen@gmail.com", "1234", false),
("Henry", "henrywang@icloud.com", "1234", false),
("admin", "admin@gmail.com", "1234", true);

drop table if exists product;
create table product (
	id int primary key auto_increment,
    name text not null,
    description text not null,
    retailPrice float,
    wholesalePrice float,
    stockQuantity integer
);

insert into product 
(name, description, retailPrice, wholesalePrice, stockQuantity) values
("Super B Energy Complex", "Dietary Supplement", 10.98, 7.98, 50),
("Electric Power Toothbrush", "Philips Sonicare", 179.96, 139.96, 25),
("Bagel", "Breakfast", 4.98, 2.98, 200);

drop table if exists orders;
create table orders (
	id int primary key auto_increment,
    userId int,
    foreign key (userId) references user(id),
    orderStatus text not null,
    datePlaced timestamp
);

insert into orders 
(userId, orderStatus, datePlaced) values
(1, "Completed", "2022-12-01 00:00:01"),
(1, "Canceled", "2023-01-10 00:00:01"),
(2, "Processing", "2023-02-07 00:00:01"),
(3, "Processing", "2023-02-10 00:00:01");

drop table if exists orderProduct;
create table orderProduct (
	id int primary key auto_increment,
    orderId int,
    foreign key (orderId) references orders(id),
    productId int,
    foreign key (productId) references product(id),
    purchasedQuantity integer,
    executionRetailPrice float,
    executionWholesalePrice float
);

insert into orderProduct
(orderId, productId, purchasedQuantity, executionRetailPrice, executionWholesalePrice) values
(1, 1, 2, 9.98, 6.98),
(2, 2, 1, 179.96, 139.96),
(3, 1, 1, 10.98, 7.98),
(4, 3, 1, 4.98, 2.98);

drop table if exists productWatchList;
create table productWatchList (
     userId int,
     foreign key (userId) references user(id),
     productId int,
     foreign key (productId) references product(id)
);

insert into productWatchList 
(userId, productId) values
(1, 2),
(2, 1); 