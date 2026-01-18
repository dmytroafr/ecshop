create sequence site_statistics_seq start with 1 increment by 1;

create table site_statistics (
    id bigint not null,
    visit_date date not null unique,
    daily_visits bigint not null default 0,
    total_visits bigint not null default 0,
    primary key (id)
);

-- Початкова статистика
insert into site_statistics (id, visit_date, daily_visits, total_visits)
values (nextval('site_statistics_seq'), current_date, 0, 0);
