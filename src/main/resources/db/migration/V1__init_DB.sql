drop table if exists buckets_products cascade;
drop table if exists categories cascade;
drop table if exists confirmation_token cascade;
drop table if exists order_details cascade;
drop table if exists orders cascade;
drop table if exists orders_details cascade;
drop table if exists products cascade;
drop table if exists products_categories cascade;
drop table if exists users cascade;
drop sequence if exists bucket_seq;
drop sequence if exists category_seq;
drop sequence if exists order_details_seq;
drop sequence if exists order_seq;
drop sequence if exists product_seq;
drop sequence if exists token_seq;
drop sequence if exists user_seq;

create sequence bucket_seq start with 1 increment by 1;
create sequence category_seq start with 1 increment by 1;
create sequence order_details_seq start with 1 increment by 1;
create sequence order_seq start with 1 increment by 1;
create sequence product_seq start with 1 increment by 1;
create sequence token_seq start with 1 increment by 1;
create sequence user_seq start with 1 increment by 1;

create table buckets (
    id bigint not null,
    user_id bigint unique,
    primary key (id),
    FOREIGN KEY (id) REFERENCES users(id));

create table buckets_products (
    bucket_id bigint not null,
    product_id bigint not null,
    PRIMARY KEY (bucket_id, product_id));

create table categories (
    id bigint not null,
    title varchar(255),
    primary key (id));

create table confirmation_token (
    confirmed_at timestamp(6),
    created_at timestamp(6) not null,
    expires_at timestamp(6) not null,
    id bigint not null,
    user_id bigint not null,
    token varchar(255) not null,
    primary key (id));

create table order_details (
    amount numeric(38,2),
    price numeric(38,2),
    id bigint not null,
    order_id bigint,
    product_id bigint,
    primary key (id));

create table orders (
    sum numeric(38,2),
    closed timestamp(6),
    created timestamp(6),
    id bigint not null,
    updated timestamp(6),
    user_id bigint,
    delivery varchar(255),
    payment varchar(255),
    status varchar(255) check (status in ('NEW','APPROVED','CANCELLED','PAID','CLOSED')),
    primary key (id));

create table orders_details (
    details_id bigint not null unique,
    order_id bigint not null);

create table products (
    opt_price numeric(38,2),
    price numeric(38,2),
    id bigint not null,
    country_producer varchar(255),
    on_stock varchar(255) check (on_stock in ('ON_STOCK','AVAILABLE','WAITING','ABSENT')),
    photo_url varchar(255),
    producer varchar(255),
    product_description text,
    title varchar(255),
    primary key (id));

create table products_categories (
    category_id bigint not null,
    product_id bigint not null);

create table users (
    id bigint not null,
    is_enable boolean,
    is_locked boolean,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255) not null,
    phone varchar(255),
    role varchar(255) check (role in ('CLIENT','ADMIN','MANAGER')),
    username varchar(255) not null unique ,
    created timestamp(6),
    primary key (id));

alter table if exists buckets add constraint user_id_fk foreign key (user_id) references users(id);
alter table if exists buckets_products add constraint product_id_fk foreign key (product_id) references products(id);
alter table if exists buckets_products add constraint bucket_id_fk foreign key (bucket_id) references buckets(id);


alter table if exists confirmation_token add constraint user_id_fk foreign key (user_id) references users;
alter table if exists order_details add constraint order_id_fk foreign key (order_id) references orders;
alter table if exists order_details add constraint product_id_fk foreign key (product_id) references products;
alter table if exists orders add constraint user_id_fk foreign key (user_id) references users;
alter table if exists orders_details add constraint details_id_fk foreign key (details_id) references order_details;
alter table if exists orders_details add constraint order_id_fk foreign key (order_id) references orders;
alter table if exists products_categories add constraint category_id_fk foreign key (category_id) references categories;
alter table if exists products_categories add constraint product_id_fk foreign key (product_id) references products;

