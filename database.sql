create database if not exists personal_project2;
use personal_project2;

drop table if exists user;
create table user (
     id int primary key auto_increment,
    username text not null,
    email text not null,
    password text not null,
    isSeller boolean,
    totalSpent float
);

insert into user 
(username, email, password, isSeller, totalSpent) values
("admin", "admin@gmail.com", "admin", true, 0),
("jennifer", "jennifer@gmail.com", "jennifer", false, 378.86),
("henry", "henry@gmail.com", "henry", false, 1499),
("gloria", "gloria@gmail.com", "gloria", false, 0),
("erica", "erica@gmail.com", "erica", false, 1743.5);

drop table if exists product;
create table product (
     id int primary key auto_increment,
    name text,
    description text,
    retailPrice float,
    wholesalePrice float,
    stockQuantity integer,
    soldQuantity integer,
    profit float
);

insert into product 
(name, description, retailPrice, wholesalePrice, stockQuantity, soldQuantity, profit) values
("Super B Energy Complex", "Dietary Supplement", 10.98, 7.98, 100, 0, 0),
("Electric Power Toothbrush", "Philips Sonicare", 100.98, 80.98, 196, 4, 80),
("Bagel", "Bread Roll", 4.98, 3.98, 192, 8, 8),
("Milk", "Strawberry Flavored Milk", 10.98, 8.98, 480, 20, 40),
("Macbook Pro", "13-inch MacBook Pro - Silver", 1499, 1299, 98, 2, 400);

drop table if exists orders;
create table orders (
     id int primary key auto_increment,
    user_id int,
    foreign key (user_id) references user(id),
    orderStatus int not null,
    datePlaced timestamp
);

insert into orders
(user_id, orderStatus, datePlaced) values
(2, 0, "2023-02-01 00:00:01"),
(2, 1, "2023-02-02 00:00:01"),
(2, 1, "2023-02-03 00:00:01"),
(3, 0, "2023-02-04 00:00:01"),
(3, 1, "2023-02-05 00:00:01"),
(4, 0, "2023-02-06 00:00:01"),
(5, 2, "2023-02-07 00:00:01"),
(5, 1, "2023-02-08 00:00:01"),
(5, 1, "2023-02-09 00:00:01"),
(5, 1, "2023-02-10 00:00:01");

drop table if exists orderProduct;
create table orderProduct (
     id int primary key auto_increment,
    order_id int,
    foreign key (order_id) references orders(id),
    product_id int,
    foreign key (product_id) references product(id),
    purchasedQuantity integer,
    executionRetailPrice float,
    executionWholesalePrice float
);

insert into orderProduct
(order_id, product_id, purchasedQuantity, executionRetailPrice, executionWholesalePrice) values
(1, 1, 1, 9.98, 6.98),
(2, 2, 4, 90.98, 70.98),
(3, 3, 3, 4.98, 3.98),
(4, 4, 10, 10.98, 8.98),
(5, 5, 1, 1499, 1299),
(6, 1, 2, 10.98, 7.98),
(7, 2, 1, 100.98, 80.98),
(8, 3, 5, 4.98, 3.98),
(9, 4, 20, 10.98, 8.98),
(10, 5, 1, 1499, 1299);

drop table if exists user_product;
create table user_product (
     user_id int,
     foreign key (user_id) references user(id),
     product_id int,
     foreign key (product_id) references product(id)
);

insert into user_product
(user_id, product_id) values
(2, 1),
(2, 2),
(2, 3),
(3, 1),
(4, 2); 