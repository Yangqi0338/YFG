-- 数据权限

INSERT INTO `c_amc_data`.`t_data_permissions_module` (`id`,
                                                      `company_code`,
                                                      `create_id`,
                                                      `create_name`,
                                                      `create_date`,
                                                      `update_id`,
                                                      `update_name`,
                                                      `update_date`,
                                                      `remarks`,
                                                      `del_flag`,
                                                      `function_module`,
                                                      `function_module_code`,
                                                      `business_name`,
                                                      `business_type`)
VALUES (md5(uuid()),-- 看看表里数据+1
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        '报表中心',
        'report',
        '电商充绒量报表',-- 菜单名称
        'hangTagReport' -- tableCode
       );


INSERT INTO `c_amc_data`.`t_data_permissions_field` (`id`,
                                                     `company_code`,
                                                     `create_id`,
                                                     `create_name`,
                                                     `create_date`,
                                                     `update_id`,
                                                     `update_name`,
                                                     `update_date`,
                                                     `remarks`,
                                                     `del_flag`,
                                                     `business_type`,
                                                     `field_remarks`,
                                                     `field_name`,
                                                     `field_value_source`,
                                                     `field_source_default_value`)
VALUES (REPLACE(UUID(), '-', ''),
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        'hangTagReport', -- tableCode
        '品牌',
        'brand', -- 这里直接带上表里的别名
        'dict',
        'C8_Brand');



INSERT INTO `c_amc_data`.`t_data_permissions_module` (`id`,
                                                      `company_code`,
                                                      `create_id`,
                                                      `create_name`,
                                                      `create_date`,
                                                      `update_id`,
                                                      `update_name`,
                                                      `update_date`,
                                                      `remarks`,
                                                      `del_flag`,
                                                      `function_module`,
                                                      `function_module_code`,
                                                      `business_name`,
                                                      `business_type`)
VALUES (md5(uuid()),-- 看看表里数据+1
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        '报表中心',
        'report',
        '设计下单进度明细报表',-- 菜单名称
        'designOrderScheduleDetailsReport' -- tableCode
       );


INSERT INTO `c_amc_data`.`t_data_permissions_field` (`id`,
                                                     `company_code`,
                                                     `create_id`,
                                                     `create_name`,
                                                     `create_date`,
                                                     `update_id`,
                                                     `update_name`,
                                                     `update_date`,
                                                     `remarks`,
                                                     `del_flag`,
                                                     `business_type`,
                                                     `field_remarks`,
                                                     `field_name`,
                                                     `field_value_source`,
                                                     `field_source_default_value`)
VALUES (REPLACE(UUID(), '-', ''),
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        'designOrderScheduleDetailsReport', -- tableCode
        '品牌',
        'brand', -- 这里直接带上表里的别名
        'dict',
        'C8_Brand');


INSERT INTO `c_amc_data`.`t_data_permissions_module` (`id`,
                                                      `company_code`,
                                                      `create_id`,
                                                      `create_name`,
                                                      `create_date`,
                                                      `update_id`,
                                                      `update_name`,
                                                      `update_date`,
                                                      `remarks`,
                                                      `del_flag`,
                                                      `function_module`,
                                                      `function_module_code`,
                                                      `business_name`,
                                                      `business_type`)
VALUES (md5(uuid()),-- 看看表里数据+1
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        '报表中心',
        'report',
        '尺寸查询报表',-- 菜单名称
        'styleSizeReport' -- tableCode
       );


INSERT INTO `c_amc_data`.`t_data_permissions_field` (`id`,
                                                     `company_code`,
                                                     `create_id`,
                                                     `create_name`,
                                                     `create_date`,
                                                     `update_id`,
                                                     `update_name`,
                                                     `update_date`,
                                                     `remarks`,
                                                     `del_flag`,
                                                     `business_type`,
                                                     `field_remarks`,
                                                     `field_name`,
                                                     `field_value_source`,
                                                     `field_source_default_value`)
VALUES (REPLACE(UUID(), '-', ''),
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        'styleSizeReport', -- tableCode
        '品牌',
        'brand', -- 这里直接带上表里的别名
        'dict',
        'C8_Brand');



INSERT INTO `c_amc_data`.`t_data_permissions_module` (`id`,
                                                      `company_code`,
                                                      `create_id`,
                                                      `create_name`,
                                                      `create_date`,
                                                      `update_id`,
                                                      `update_name`,
                                                      `update_date`,
                                                      `remarks`,
                                                      `del_flag`,
                                                      `function_module`,
                                                      `function_module_code`,
                                                      `business_name`,
                                                      `business_type`)
VALUES (md5(uuid()),-- 看看表里数据+1
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        '报表中心',
        'report',
        'BOM清单查询报表',-- 菜单名称
        'stylePackBomMaterialReport' -- tableCode
       );


INSERT INTO `c_amc_data`.`t_data_permissions_field` (`id`,
                                                     `company_code`,
                                                     `create_id`,
                                                     `create_name`,
                                                     `create_date`,
                                                     `update_id`,
                                                     `update_name`,
                                                     `update_date`,
                                                     `remarks`,
                                                     `del_flag`,
                                                     `business_type`,
                                                     `field_remarks`,
                                                     `field_name`,
                                                     `field_value_source`,
                                                     `field_source_default_value`)
VALUES (REPLACE(UUID(), '-', ''),
        '677447590605750272',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        '0',
        'stylePackBomMaterialReport', -- tableCode
        '品牌',
        'brand', -- 这里直接带上表里的别名
        'dict',
        'C8_Brand');