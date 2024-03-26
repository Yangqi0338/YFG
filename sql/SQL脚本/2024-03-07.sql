-- 用于mango品牌  状态：未上生产
ALTER TABLE `s_pdm_data`.`t_style`
ADD COLUMN `gender_type` varchar(64) NULL COMMENT '产品季性别类型' AFTER `design_marking_status`,
ADD COLUMN `gender_name` varchar(64) NULL COMMENT '产品季性别名称' AFTER `gender_type`;

