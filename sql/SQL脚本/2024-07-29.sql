-- mango品牌添加国际号型，国际尺码
ALTER TABLE `s_pdm_data`.`t_hang_tag`
ADD COLUMN `ext_shape` varchar(64) NULL COMMENT 'mango国际号型' AFTER `color_code_translate`,
ADD COLUMN `ext_size` varchar(64) NULL COMMENT 'mango国际尺码' AFTER `ext_shape`;