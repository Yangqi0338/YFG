use s_pdm_data;
alter table t_order_book_detail add column online_material varchar(50) default null comment '线上备料';
update t_order_book_detail set online_material = material;

alter table t_order_book_detail add column online_braiding varchar(50) default null comment '线上备胚';
update t_order_book_detail set online_braiding = braiding;

update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'75CM(XS)','XS');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'110CM(XXS)','XXS');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'105CM(XXXL)','XXXL');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'80CM(S)','S');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'85CM(M)','M');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'90CM(L)','L');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'100CM(XXL)','XXL');
update t_order_book_detail set commissioning_size = REPLACE(commissioning_size,'95CM(XL)','XL');