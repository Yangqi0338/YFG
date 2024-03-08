use s_pdm_data;
alter table t_basicsdatum_material_ingredient add column code varchar(50) default null comment '成分编码';
alter table t_basicsdatum_material_ingredient add column say_code varchar(50) default null comment '说明编码';
