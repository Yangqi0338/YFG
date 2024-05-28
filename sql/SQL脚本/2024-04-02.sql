ALTER TABLE `s_pdm_data`.`t_fabric_ingredients_info`
    ADD COLUMN `brand` varchar(64) NOT NULL COMMENT '品牌' AFTER `company_code`,
    ADD COLUMN `brand_name` varchar(64) NOT NULL COMMENT '品牌名称' AFTER `brand`;