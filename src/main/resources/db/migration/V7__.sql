alter table t_country_language add column model_language_code varchar(10) default 'ZH' not null comment '号型语言编码';
alter table t_country_language drop column country_code;
alter table t_country_language drop column language_name;
alter table t_country_language change column country_name name varchar(255) default null comment '名称';
