use s_pdm_data;
alter table t_style_country_status add column type varchar(10) default 'tag' not null comment '标准类型';