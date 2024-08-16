-- 添加备注字段
ALTER TABLE `s_pdm_data`.`t_pack_info_status`
ADD COLUMN `remark` varchar(500) NULL COMMENT '备注' AFTER `special_spec_comments`;