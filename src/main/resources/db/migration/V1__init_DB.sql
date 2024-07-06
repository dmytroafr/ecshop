create sequence bucket_seq start with 1 increment by 1;
create sequence category_seq start with 1 increment by 1;
create sequence order_details_seq start with 1 increment by 1;
create sequence order_seq start with 1 increment by 1;
create sequence product_seq start with 1 increment by 1;
create sequence token_seq start with 1 increment by 1;
create sequence user_seq start with 1 increment by 1;

create table users (
    id bigint not null,
    enabled boolean not null ,
    account_non_locked boolean not null,
    account_non_expired boolean not null,
    credentials_non_expired boolean not null,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255) not null,
    phone varchar(255),
    role varchar(255) check (role in ('ROLE_CLIENT','ROLE_ADMIN','ROLE_MANAGER')),
    username varchar(255) not null unique ,
    created timestamp(6),
    primary key (id));

create table buckets (
    id bigint not null,
    user_id bigint unique,
    primary key (id),
    constraint user_id_fk foreign key (id) references users(id));

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

create table buckets_products (
    bucket_id bigint not null,
    product_id bigint not null,
    constraint product_id_fk foreign key (product_id) references products(id),
    constraint bucket_id_fk foreign key (bucket_id) references buckets(id));

create table categories (
    id bigint not null,
    title varchar(255),
    primary key (id));

create table products_categories (
    category_id bigint not null,
    product_id bigint not null,
    constraint category_id_fk foreign key (category_id) references categories,
    constraint product_id_fk foreign key (product_id) references products);

create table confirmation_token (
    confirmed_at timestamp(6),
    created_at timestamp(6) not null,
    expires_at timestamp(6) not null,
    id bigint not null,
    user_id bigint not null,
    token varchar(255) not null,
    primary key (id),
    constraint user_id_fk foreign key (user_id) references users);

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
    primary key (id),
    constraint user_id_fk foreign key (user_id) references users);

create table order_details (
    amount numeric(38,2),
    price numeric(38,2),
    id bigint not null,
    order_id bigint,
    product_id bigint,
    primary key (id),
    constraint order_id_fk foreign key (order_id) references orders,
    constraint product_id_fk foreign key (product_id) references products);

create table orders_details (
    details_id bigint not null unique,
    order_id bigint not null,
    constraint details_id_fk foreign key (details_id) references order_details,
    constraint order_id_fk foreign key (order_id) references orders);

insert into users (id, enabled, account_non_locked, account_non_expired, credentials_non_expired, email, first_name,
                   last_name, password, phone, role, username, created)
values (1,true, true, true, true, 'dima@e-chem.com.ua', 'Afrosin', 'Dmytro',
        '$2a$12$g4Vi5h.Jo/Lyp4gWuuIU0u37RaLhmTzCAq0vUWMOY.H5Ntw6QnePG',
        '+380957913429', 'ROLE_ADMIN', 'dima', now());
alter sequence user_seq restart with 2;
insert into buckets (id, user_id) VALUES (1,1);