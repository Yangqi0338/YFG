use s_pdm_data;
alter table t_order_book_detail add column online_material varchar(50) default null comment '线上备料';
update t_order_book_detail set online_material = material;

alter table t_order_book_detail add column online_braiding varchar(50) default null comment '线上备胚';
update t_order_book_detail set online_braiding = braiding;