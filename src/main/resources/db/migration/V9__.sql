update t_order_book_detail set is_order = 0 where is_order is null;
alter table t_order_book_detail change column is_order order_status varchar(2) not null default "0" comment '下单状态 0未下单 1投产失败 2下单中 3投产中 4已下单';
update t_order_book_detail set order_status = '2' where order_status = '1';
alter table t_order_book_detail drop column designer_confirm;
alter table t_order_book_detail drop column business_confirm;
alter table t_order_book_detail add column order_person varchar(50) default null comment '下单人';
alter table t_order_book_detail add column order_person_name varchar(50) default null comment '下单人名字';
alter table t_order_book_detail add column order_date datetime default null comment '下单时间';
alter table t_order_book_detail add column order_no varchar(50) default null comment '投产编码';
alter table t_order_book_detail add column order_send_status varchar(2) default null comment '投产下发状态';
alter table t_order_book_detail add column order_send_warn_msg varchar(500) default null comment '投产下发错误信息';
alter table t_order_book_detail add column delivery_at datetime default null comment '商品要求货期';
alter table t_order_book_detail add column sale_type_id varchar(10) default null comment '销售分类';
alter table t_order_book_detail add column budget_no varchar(50) default null comment '预算号';
alter table t_order_book_detail add column budget_no_id varchar(50) default null comment '预算号id';
alter table t_order_book_detail add column fac_merge varchar(2) default null comment '合并投产';
alter table t_order_book_detail add column place_order_type varchar(2) default null comment '下单类型';
alter table t_order_book_detail add column devt_type varchar(10) default null comment '投产类型';
alter table t_order_book_detail modify column unit_dosage_ids varchar(500) default null comment '单件用量/里';
alter table t_order_book_detail modify column unit_fabric_dosage_ids varchar(500) default null comment '单件用量/里';


