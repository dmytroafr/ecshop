alter table if exists buckets drop constraint if exists FKnl0ltaj67xhydcrfbq8401nvj;
alter table if exists buckets_products drop constraint if exists FKl4aj2nxk7wbu3s95bc0wq1elx;
alter table if exists buckets_products drop constraint if exists FKc49ah45o66gy2f2f4c3os3149;
alter table if exists order_details drop constraint if exists FKjyu2qbqt8gnvno9oe9j2s2ldk;
alter table if exists order_details drop constraint if exists FK4q98utpd73imf4yhttm3w0eax;
alter table if exists orders drop constraint if exists FK32ql8ubntj5uh44ph9659tiih;
alter table if exists orders_details drop constraint if exists FKbblrq2kcscnyr9fsv8ptp98wy;
alter table if exists orders_details drop constraint if exists FK5o977kj2vptwo70fu7w7so9fe;
alter table if exists products_categories drop constraint if exists FKqt6m2o5dly3luqcm00f5t4h2p;
alter table if exists products_categories drop constraint if exists FKtj1vdea8qwerbjqie4xldl1el;
alter table if exists users drop constraint if exists FK8l2qc4c6gihjdyoch727guci;

drop table if exists buckets cascade;
drop table if exists buckets_products cascade;
drop table if exists categories cascade;
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
drop sequence if exists user_seq;

create table buckets (id bigint not null, user_id bigint unique, primary key (id));
create table buckets_products (bucket_id bigint not null, products_id bigint not null);
create table categories (id bigint not null, tittle varchar(255), primary key (id));
create table order_details (amount numeric(38,2), price numeric(38,2), id bigint not null, order_id bigint, product_id bigint, primary key (id));
create table orders (sum numeric(38,2), created timestamp(6), id bigint not null, updated timestamp(6), user_id bigint, address varchar(255), status varchar(255) check (status in ('NEW','APPROVED','CANCELLED','PAID','CLOSED')), primary key (id));
create table orders_details (details_id bigint not null unique, order_id bigint not null);
create table products (price numeric(38,2), id bigint not null, tittle varchar(255), primary key (id));
create table products_categories (category_id bigint not null, product_id bigint not null);
create table users (archive boolean not null, bucket_id bigint unique, id bigint not null, email varchar(255), name varchar(255), password varchar(255), phone varchar(255), role varchar(255) check (role in ('CLIENT','ADMIN','MANAGER')), primary key (id));

create sequence bucket_seq start with 1 increment by 1;
create sequence category_seq start with 1 increment by 1;
create sequence order_details_seq start with 1 increment by 1;
create sequence order_seq start with 1 increment by 1;
create sequence product_seq start with 1 increment by 1;
create sequence user_seq start with 1 increment by 1;

alter table if exists buckets add constraint FKnl0ltaj67xhydcrfbq8401nvj foreign key (user_id) references users;
alter table if exists buckets_products add constraint FKl4aj2nxk7wbu3s95bc0wq1elx foreign key (products_id) references products;
alter table if exists buckets_products add constraint FKc49ah45o66gy2f2f4c3os3149 foreign key (bucket_id) references buckets;
alter table if exists order_details add constraint FKjyu2qbqt8gnvno9oe9j2s2ldk foreign key (order_id) references orders;
alter table if exists order_details add constraint FK4q98utpd73imf4yhttm3w0eax foreign key (product_id) references products;
alter table if exists orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references users;
alter table if exists orders_details add constraint FKbblrq2kcscnyr9fsv8ptp98wy foreign key (details_id) references order_details;
alter table if exists orders_details add constraint FK5o977kj2vptwo70fu7w7so9fe foreign key (order_id) references orders;
alter table if exists products_categories add constraint FKqt6m2o5dly3luqcm00f5t4h2p foreign key (category_id) references categories;
alter table if exists products_categories add constraint FKtj1vdea8qwerbjqie4xldl1el foreign key (product_id) references products;
alter table if exists users add constraint FK8l2qc4c6gihjdyoch727guci foreign key (bucket_id) references buckets;