-- 用于打版任务 待接收提示是否参考样衣字段
ALTER TABLE `s_pdm_data`.`t_pattern_making`
ADD COLUMN `refer_sample` int(255) NULL COMMENT '是否参考样衣(0：否，1：是)' AFTER `receive_reason`;