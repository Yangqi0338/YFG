alter table t_order_book_detail add column designer_distribute VARCHAR(1) default '0' comment '设计师分配';
alter table t_order_book_detail add column offline_business_distribute VARCHAR(1) default '0' comment '线下商企分配';
alter table t_order_book_detail add column online_business_distribute VARCHAR(1) default '0' comment '线上商企分配';

update t_column_define set hidden = '1', column_width = '80',sort_order = '9', del_flag = '0' where id = 'orderBook42';
update t_column_define set hidden = '1', column_width = '100',sort_order = '14', del_flag = '0' where id = '1742859895962791938';