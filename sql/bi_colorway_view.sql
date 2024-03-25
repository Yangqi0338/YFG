select tsc.style_no                                                                                        as 大货款号,
       ts.design_no                                                                                        as 设计款号,
       tsc.color_specification                                                                             as 颜色规格,
       tsc.color_code                                                                                      as 颜色代码,
       tsc.color_name                                                                                      as 颜色名称,
       tsc.band_name                                                                                       as 波段,
       tsc.tag_price                                                                                       as 吊牌价,
       tsc.new_date                                                                                        as 上新时间,
       ts.style_flavour_name                                                                               as 款式风格,
       ts.style_flavour                                                                                    as 款式风格编码,
       ts.pattern_design_name                                                                              as 版式名称,
       JSON_EXTRACT(tpc.calc_item_val, '$."物料费"')                                                       as 物料费,
       JSON_EXTRACT(tpc.calc_item_val, '$."车缝加工费"')                                                   as 车缝加工费,
       JSON_EXTRACT(tpc.calc_item_val, '$."包装费"')                                                       as 包装费,
       JSON_EXTRACT(tpc.calc_item_val, '$."倍价"')                                                         as 倍价,
       JSON_EXTRACT(tpc.calc_item_val, '$."除毛领成本"')                                                   as 除毛领成本,
       JSON_EXTRACT(tpc.calc_item_val, '$."检测费"')                                                       as 检测费,
       JSON_EXTRACT(tpc.calc_item_val, '$."毛领成本"')                                                     as 毛领成本,
       JSON_EXTRACT(tpc.calc_item_val, '$."毛纱加工费"')                                                   as 毛纱加工费,
       JSON_EXTRACT(tpc.calc_item_val, '$."去毛领倍率"')                                                   as 去毛领倍率,
       JSON_EXTRACT(tpc.calc_item_val, '$."外协加工费"')                                                   as 外协加工费,
       if(JSON_EXTRACT(tpc.calc_item_val, '$."物料费"') + JSON_EXTRACT(tpc.calc_item_val, '$."包装费"') = 0, null,
          tsc.tag_price /
          (JSON_EXTRACT(tpc.calc_item_val, '$."物料费"') + JSON_EXTRACT(tpc.calc_item_val, '$."包装费"'))) as 实际倍率,
       if('1' = tsp.product_hangtag_confirm, '是', '否')                                                   as 吊牌确认,
       tpb.bulk_unit_use * tpb.bulk_price * (1 + tpb.loss_rate) /
       tpb.price_tax                                                                                       as 计控实际成本,
       tsc.tag_price / (tpb.bulk_unit_use * tpb.bulk_price * (1 + tpb.loss_rate) /
                        tpb.price_tax)                                                                     as 计控实际倍率,
       tsp.series_name                                                                                     as 系列名称,
       tht.down_content                                                                                    as 充绒量,
       tsc.product_name                                                                                    as 品名,
       tsc.ware_code                                                                                       as 唯一码,
       if('0' = tsc.status, '是', '否')                                                                    as 启用,
       if('1' = tsc.bom_status, '样品', '大货')                                                            as BOM阶段,
       case tsc.scm_send_flag
           when '0' then '未发送'
           when '1' then '发送成功'
           when '2' then '发送失败'
           when '3' then '重新打开'
           else '未知状态'
           end                                                                                             as 发送状态,
       tuf.url                                                                                             as 款式图,
       6.5                                                                                                 as 倍率,
       tsc.subdivide_name                                                                                  as 产品细分,
       tsc.defective_no                                                                                    as 次品编号,
       if('0' = tsp.control_confirm, '是', '否')                                                           as 计控成本确认,
       if('0' = tsp.control_hangtag_confirm, '是', '否')                                                   as 计控吊牌价确认,
       ts.devt_type_name                                                                                   as 模式,
       tsc.is_trim                                                                                         as 是否是内配饰,
       tsc.accessory_no                                                                                    as 配饰款号,
       if('1' = tsc.is_luxury, '是', '否')                                                                 as 轻奢款,
       tsc.design_correct_date                                                                             as 设计下明细单,
       if('1' = tsc.order_flag, '未上会', '已上会')                                                        as 上会状态,
       tsc.send_batching_date1                                                                             as 下配料1,
       tsc.send_batching_date2                                                                             as 下配料2,
       tsc.send_main_fabric_date                                                                           as 下主面料单,
       tsc.send_single_date                                                                                as 下里布单,
       tsc.create_date                                                                                     as 创建时间,
       GREATEST(IFNULL(tsc.update_date, 0), IFNULL(ts.update_date, 0), IFNULL(tpi.update_date, 0),
                IFNULL(tsp.update_date, 0), IFNULL(tpb.update_date, 0), IFNULL(pbv.update_date, 0),
                IFNULL(tht.update_date, 0), IFNULL(tuf.update_date, 0), IFNULL(tpts.update_date, 0)
       )                                                                                                   as 修改时间,
       tsc.sales_type_name                                                                                 as 销售类型,
       tsc.sales_type                                                                                      as 销售类型编码,
       tsc.principal_style_no                                                                              as 主款款号,
       tsc.bom_status                                                                                      as BOM发送状态,
       tsc.remarks                                                                                         as 备注,
       tsc.supplier_abbreviation                                                                           as 厂家,
       tsc.supplier_no                                                                                     as 厂家款号,
       tsc.supplier_color                                                                                  as 厂家款颜色,
       ts.id                                                                                              as StyleURL,
       if('0' = tsc.is_mainly, '否', '是')                                                                 as 是否主推,
       tsc.create_name                                                                                     as 创建人,
       tsc.update_name                                                                                     as 修改者,
       ts.technician_name                                                                                  as 后技术工艺师,
       ts.designer                                                                                         as 后技术下单员,
       tht.place_order_date                                                                                as 下单时间,
       if(tpts.foreign_id > '0', '是', '否')                                                               as 含外辅工艺,
       CONCAT(tsc.ware_code, tsc.color_code, ts.default_size)                                              as 默认条形码,
       ts.historical_data                                                                                  as 历史数据,
       if(tsc.del_flag = '0', '存在', '删除')                                                              as 删除标识,
       tsc.id                                                                                               as 大货款ID
from t_style_color as tsc
         left join t_style as ts on ts.id = tsc.style_id
    and ts.del_flag = '0'
         left join t_pack_info as tpi on tpi.style_color_id = tsc.id
    and tpi.del_flag = '0'
         left join t_pack_pricing as tpc on tpc.foreign_id = tpi.id
    and tpc.pack_type = 'packBigGoods'
    and tpc.del_flag = '0'
         left join t_style_pricing as tsp on tsp.pack_id = tpi.id
    and tsp.del_flag = '0'
         left join t_pack_bom_version as pbv on pbv.status = '0'
    and pbv.foreign_id = tpi.id
    and pbv.del_flag = '0'
    and pbv.pack_type = 'packBigGoods'
         left join t_pack_bom as tpb on tpb.foreign_id = tpi.id
    and pbv.id = tpb.bom_version_id
    and tpb.pack_type = 'packBigGoods'
    and tpb.del_flag = '0'

         left join t_hang_tag as tht on tht.bulk_style_no = tsc.style_no
    and tht.del_flag = '0'
         left join t_upload_file as tuf on tuf.id = ts.style_pic
    and tuf.del_flag = '0'
         LEFT JOIN (SELECT DISTINCT tpts.foreign_id, tpts.update_date
                    FROM t_pack_tech_spec AS tpts
                    WHERE tpts.del_flag = '0'
                      AND tpts.spec_type = '外辅工艺'
                      AND tpts.pack_type = 'packBigGoods') as tpts on tpts.foreign_id = tpi.id

where tsc.del_flag = '0'
  and tsc.`status` = '0'
group by tsc.id