-- 物料编码流水字段
ALTER TABLE `s_pdm_data`.`t_basicsdatum_material`
ADD COLUMN `material_code_flow` varchar(18) NULL COMMENT '物料编码流水' AFTER `quality_level_name`;