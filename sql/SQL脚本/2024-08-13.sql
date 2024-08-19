ALTER TABLE `s_pdm_data`.`t_basicsdatum_material_price`
MODIFY COLUMN `order_day` varchar(19) NULL DEFAULT NULL COMMENT '订货周期' AFTER `currency_name`,
MODIFY COLUMN `production_day` varchar(19) NULL DEFAULT NULL COMMENT '生产周期' AFTER `order_day`;