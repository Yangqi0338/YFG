alter table t_style_country_status modify column check_detail_json text comment '检查详情Json';
alter table t_style_country_status add column type varchar(10) default 'tag' not null comment '标准类型';
alter table t_style_country_status add column standard_column_code varchar(300) not null default '' comment '冗余一份标准列编码';