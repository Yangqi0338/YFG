SELECT

    tsc.style_no as 大货款号,
    ts.size_range                                                                              AS 尺寸表名称,
       ts.size_range                                                                              AS 号型类型,
       ts.size_range_name                                                                         AS 子尺码范围,
       ts.default_size                                                                            AS 基础尺码,
       tpz.code_error_setting                                                                     AS 档差,
       ts.prod_category_name                                                                      AS 品类测量组,
       IF(tpsd.size LIKE '%(%', SUBSTRING_INDEX(SUBSTRING_INDEX(tpsd.size, '(', -1), ')', 1), '') as 尺码类型值,
       SUBSTRING_INDEX(tpsd.size, '(', 1)                                                         as 尺码号型,
       'DRAFT'                                                                                    as 状态,
       tpsd.create_name                                                                           as 创建人,
       tpsd.create_date                                                                           as 创建时间,

       tpsd.update_name                                                                           as 更新人,
       GREATEST(ts.update_date,tpsd.update_date, tpi.update_date, tpz.update_date, tpsd.update_date,tsc.update_date)             as 更新时间,
       tpz.part_name                                                                              as 测量点名称,
       tpz.method                                                                                 as 测量点描述,
       tpsd.size                                                                                  as 尺码名称,
       IF(ts.default_size = tpsd.size, '是', '否')                                                as 是否为基础码的标识,
       tpsd.template                                                                              AS 样板尺寸,
       tpsd.garment                                                                               AS 成衣尺寸,
       tpsd.washing                                                                               AS 洗后尺寸,
       tpz.part_code                                                                              AS 测量点ID,
       ts.design_no                                                                               AS 设计款号,
       ts.historical_data                                                                               as 历史数据,
       if(ts.del_flag = '0', '存在', '删除')                                                      as 删除标识,
        tsc.id                                                                                               as 大货款ID

FROM t_style_color tsc
         LEFT JOIN t_style as ts ON tsc.style_id = ts.id and tsc.del_flag = '0'
         LEFT JOIN t_pack_info as tpi ON tpi.style_color_id = tsc.id and tpi.del_flag = '0'
         LEFT JOIN t_pack_size as tpz ON tpz.foreign_id = tpi.id and tpz.del_flag = '0'
         LEFT JOIN t_pack_size_detail as tpsd
                   ON tpsd.foreign_id = tpi.id and pack_size_id = tpz.id and tpsd.del_flag = '0'
