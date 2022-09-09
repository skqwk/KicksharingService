create sequence if not exists scooter_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence scooter_sequence owner to root;

create sequence if not exists subscription_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence subscription_sequence owner to root;

create sequence if not exists model_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence model_sequence owner to root;

create sequence if not exists tariff_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence tariff_sequence owner to root;

create sequence if not exists user_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence user_sequence owner to root;

create sequence if not exists rent_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence rent_sequence owner to root;

create sequence if not exists rent_point_sequence
    increment 1
    minvalue 1
    maxvalue 9223372036854775807
    start 1
    cache 1;
alter sequence rent_point_sequence owner to root;

create table if not exists model
(
    id           bigint not null
        primary key default nextval('model_sequence'),
    manufacturer varchar(255),
    name         varchar(255)
);

alter table model
    owner to root;

alter sequence model_sequence
    owned by model.id;

create table if not exists scooter
(
    id          bigint not null
        primary key default nextval('scooter_sequence'),
    status      integer,
    time_in_use bigint,
    model_id    bigint
        constraint fk_scooter_model
            references model (id)
            on delete cascade
);

alter table scooter
    owner to root;

alter sequence scooter_sequence
    owned by scooter.id;

create table if not exists tariff
(
    id                bigint not null
        primary key default nextval('tariff_sequence'),
    activation_cost   double precision,
    description       varchar(255),
    discount          double precision,
    duration_in_hours integer,
    name              varchar(255),
    settlement_cost   double precision,
    settlement_for    integer,
    tariff_cost       double precision,
    type              integer
);

alter table tariff
    owner to root;

alter sequence tariff_sequence
    owned by tariff.id;

create table if not exists user_account
(
    id       bigint not null
        primary key default nextval('user_sequence'),
    age      integer,
    email    varchar(255)
        constraint uk_email
            unique,
    password varchar(255),
    role     integer
);

alter table user_account
    owner to root;

alter sequence user_sequence
    owned by user_account.id;

create table if not exists rent_history
(
    id          bigint not null
        primary key default nextval('rent_sequence'),
    cost        double precision,
    finished_in timestamp,
    started_in  timestamp,
    status      integer,
    scooter_id  bigint
        constraint fk_rent_scooter
            references scooter (id) on delete set null,
    tariff_id   bigint
        constraint fk_rent_tariff
            references tariff (id) on delete set null,
    user_id     bigint
        constraint fk_rent_user
            references user_account (id) on delete set null
);

alter table rent_history
    owner to root;

alter sequence rent_sequence
    owned by rent_history.id;

create table if not exists rent_point
(
    id        bigint not null
        primary key default nextval('rent_point_sequence'),
    latitude  double precision,
    longitude double precision
);

alter table rent_point
    owner to root;

alter sequence rent_point_sequence
    owned by rent_point.id;

create table if not exists rent_point_scooters
(
    rent_point_id bigint not null
        constraint fk_rent_point
            references rent_point (id),
    scooters_id   bigint not null
        constraint uk_scooter
            unique
        constraint fk_scooter
            references scooter (id)
);

alter table rent_point_scooters
    owner to root;


create table if not exists subscription
(
    id         bigint not null
        primary key default nextval('subscription_sequence'),
    expired_in timestamp,
    tariff_id  bigint
        constraint fk_subscription_tariff
            references tariff (id) on delete cascade,
    user_id    bigint
        constraint fk_subscription_user
            references user_account (id) on delete cascade
);

alter table subscription
    owner to root;

alter sequence subscription_sequence
    owned by subscription.id;



