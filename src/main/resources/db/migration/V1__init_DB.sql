-- create sequence bucket_seq start with 1 increment by 1;
-- create sequence category_seq start with 1 increment by 1;
-- create sequence order_details_seq start with 1 increment by 1;
-- create sequence order_seq start with 1 increment by 1;
-- create sequence product_seq start with 1 increment by 1;
-- create sequence user_seq start with 1 increment by 1;
--
-- create table users
-- (
--     archive     boolean not null,
--     id          bigint  not null,
--     email       varchar(255) UNIQUE ,
--     extra_phone varchar(255),
--     name        varchar(255),
--     password    varchar(255),
--     phone       varchar(255) UNIQUE ,
--     role        varchar(255) check (role in ('CLIENT', 'ADMIN', 'MANAGER')),
--     primary key (id)
-- );

create table products (
    opt_price numeric(38,2),
    price numeric(38,2),
    id bigint not null,
    country_producer varchar(255),
    on_stock varchar(255),
    photo_url varchar(255),
    producer varchar(255),
    product_description text,
    title varchar(255),
    primary key (id));
-- create table categories
-- (
--     id    bigint not null,
--     title varchar(255),
--     primary key (id)
-- );
-- create table products_categories
-- (
--     category_id bigint not null,
--     product_id  bigint not null,
--     constraint products_categories_categories_fk foreign key (category_id) references categories,
--     constraint products_categories_products_fk foreign key (product_id) references products
-- );
-- create table buckets
-- (
--     id      bigint not null,
--     user_id bigint unique,
--     primary key (id),
--     constraint user_bucket_fk foreign key (user_id) references users
-- );
-- create table orders
-- (
--     sum     numeric(38, 2),
--     closed  timestamp(6),
--     created timestamp(6),
--     id      bigint not null,
--     updated timestamp(6),
--     user_id bigint,
--     status  varchar(255) check (status in ('NEW', 'APPROVED', 'CANCELLED', 'PAID', 'CLOSED')),
--     primary key (id),
--     constraint orders_users_fk foreign key (user_id) references users
-- );
-- create table buckets_products
-- (
--     bucket_id   bigint not null,
--     products_id bigint not null,
--     constraint buckets_products_fk foreign key (products_id) references products,
--     constraint buckets_buckets_fk foreign key (bucket_id) references buckets
-- );
-- create table order_details
-- (
--     amount     numeric(38, 2),
--     price      numeric(38, 2),
--     id         bigint not null,
--     order_id   bigint,
--     product_id bigint,
--     primary key (id),
--     constraint order_details_orders_fk foreign key (order_id) references orders,
--     constraint order_details_products_fk foreign key (product_id) references products
-- );
-- create table orders_details
-- (
--     details_id bigint not null unique,
--     order_id   bigint not null,
--     constraint orders_details_to_order_detail foreign key (details_id) references order_details,
--     constraint orders_details_orders foreign key (order_id) references orders
-- );
