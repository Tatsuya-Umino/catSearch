# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table AdminUsers (
  admin_user_id             bigint not null,
  create_ts                 date,
  update_ts                 date,
  versions                  bigint,
  admin_user_name           varchar(255),
  admin_user_password       varchar(255),
  registerd_date            date,
  last_login_date           date,
  constraint pk_AdminUsers primary key (admin_user_id))
;

create table Cats (
  cat_id                    bigint not null,
  create_ts                 date,
  update_ts                 date,
  versions                  bigint,
  cat_name                  varchar(255),
  city_name                 varchar(255),
  address                   varchar(255),
  last_address              varchar(255),
  micro_num                 bigint,
  standout_feature          varchar(255),
  body_color                varchar(255),
  eyes_color                varchar(255),
  body_size                 varchar(255),
  missing_date              varchar(255),
  message                   varchar(255),
  remarks                   varchar(255),
  latitude                  bigint,
  longitude                 bigint,
  cat_image                 varchar(255),
  flg                       integer,
  pref_pref_id              bigint,
  token                     varchar(255),
  contributor_contributor_id bigint,
  constraint pk_Cats primary key (cat_id))
;

create table Comments (
  comment_id                bigint not null,
  create_ts                 date,
  update_ts                 date,
  versions                  bigint,
  cat_cat_id                bigint,
  comment_title             varchar(255),
  comment_name              varchar(255),
  mail_address              varchar(255),
  comment_message           varchar(255),
  comment_image             varchar(255),
  constraint pk_Comments primary key (comment_id))
;

create table Contributors (
  contributor_id            bigint not null,
  create_ts                 date,
  update_ts                 date,
  versions                  bigint,
  contributor_name          varchar(255),
  contributor_nick_name     varchar(255),
  birth_year                varchar(255),
  birth_month               varchar(255),
  birth_day                 varchar(255),
  mail_address              varchar(255),
  sex                       integer,
  how_to_arrive_id          integer,
  hash_id                   varchar(255),
  constraint pk_Contributors primary key (contributor_id))
;

create table Knows (
  how_to_arrive_id          bigint not null,
  create_ts                 date,
  update_ts                 date,
  versions                  bigint,
  how_to_arrive_name        varchar(255),
  constraint pk_Knows primary key (how_to_arrive_id))
;

create table Prefs (
  pref_id                   bigint not null,
  create_ts                 date,
  update_ts                 date,
  versions                  bigint,
  pref_name                 varchar(255),
  constraint pk_Prefs primary key (pref_id))
;

create sequence AdminUsers_seq;

create sequence Cats_seq;

create sequence Comments_seq;

create sequence Contributors_seq;

create sequence Knows_seq;

create sequence Prefs_seq;

alter table Cats add constraint fk_Cats_pref_1 foreign key (pref_pref_id) references Prefs (pref_id) on delete restrict on update restrict;
create index ix_Cats_pref_1 on Cats (pref_pref_id);
alter table Cats add constraint fk_Cats_contributor_2 foreign key (contributor_contributor_id) references Contributors (contributor_id) on delete restrict on update restrict;
create index ix_Cats_contributor_2 on Cats (contributor_contributor_id);
alter table Comments add constraint fk_Comments_cat_3 foreign key (cat_cat_id) references Cats (cat_id) on delete restrict on update restrict;
create index ix_Comments_cat_3 on Comments (cat_cat_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists AdminUsers;

drop table if exists Cats;

drop table if exists Comments;

drop table if exists Contributors;

drop table if exists Knows;

drop table if exists Prefs;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists AdminUsers_seq;

drop sequence if exists Cats_seq;

drop sequence if exists Comments_seq;

drop sequence if exists Contributors_seq;

drop sequence if exists Knows_seq;

drop sequence if exists Prefs_seq;

