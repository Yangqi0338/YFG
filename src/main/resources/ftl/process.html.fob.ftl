<html>
<head>
    <meta charset="UTF-8">
</head>
<style>
    * {
        font-family: 'Source Han Serif SC';
        font-size: 12px;

    }

    html {
        width: 277mm;
        margin: 50px auto;
    }

    * {
        margin: 0;
        padding: 0;
    }

    body {
        padding: 0 10px !important;
    }

    tr {
        page-break-inside: avoid;
    }

    th {
        position: sticky;
        top: 0;
        z-index: 1;
    }

    td {
        padding-left: 4px;
        padding-right: 4px;

    }

    .item_th {
        width: 8em;
    }
    .item_td {
        text-align: left;width: 8.05em;box-sizing: border-box;
        padding-left: 0.5em;
    }

    .flex_td {
        padding-left: 0;
        padding-right: 0;
        display: table-row;
        height: 100%;
        vertical-align: top;
        border: 0.5px solid #000;
    }
    .flex_td>div {
        display: table-cell;
        height: 100%;
    }
    .flex_td_box {
        height: 100%;
        display: table-cell;
        border: 0.5px solid #000;
    }

    .flex_td_box>div{
        border-bottom:0.5px solid #000;
        box-sizing: border-box;
        font-size: 0;
        vertical-align: middle;
        width: 100%;
        display: table;
        /*background: black;*/
    }
    .flex_td_box>div>div {
        width: 100%;
        vertical-align: top;
        display: table-row;
    }

    .flex_td_box>div>div>div{
        padding: 4px;
        box-sizing: border-box;
        align-items: center;
        display: table-cell;
        /*vertical-align: middle;*/
        margin: 0;
    }
    .flex_td_box>div>div>div:nth-child(2) {
        border-left:0.5px solid #000;
    }

    .table_border {
        width: 100%;
        border-collapse: collapse;
        padding-bottom: 5px;
    }

    .table_border tbody tr, td {
        height: 25px;
        font-family: 'Source Han Serif SC Medium';
    }

    .table_no_border {
        width: 100%;
        border-collapse: collapse;
        border: none;
    }

    .table_no_border tr, td, th {
        border: none;
    }

    .table_border td, th {
        border: 1px solid black;
    }

    .mt {
        margin-bottom: 5px;
    }

    .one_imgs_xbj {
        display: table-cell;
        vertical-align: center;
        text-align: center;
        padding: 10px;
        max-width: 85mm;
    }
    .one_imgs_xbj img {
        max-height: 65mm;
        min-height: 65mm;
        /*max-width: 75mm;*/
    }

    .jcgy_img_box {
        display: table-cell;
        vertical-align: middle;
        text-align: center;
        padding: 10px;
        max-width: 85mm;
    }

    .jcgy_img_box img {
        height: ${jcgyImgHeight}mm;
        max-width: 85mm;

    }

    .text_color {
        font-weight: bold;
        margin: 2mm 0;
    }


    .size_table {
        width: auto;
    }

    .size_tr {
        text-align: center;
        height: 35px;
    }

    .bold {
        font-weight: bold;
    }

    .gb,.size_gb {
        background-color: #b8b7b7;
    }

    .dgb,.size_dgb{
        background-color: #b8b7b7;
    }

    .table_border .gb {
        background-color: #b8b7b7;
    }


    .fg {
        min-width: 30px;
        height: 28px;
    }

    .wb {
        background-color: white;
    }
    .size_wb {
        background-color: white;
        border-left: 2.5px solid #000000;
        border-right: 2.5px solid #000000;
    }

    .size_table_border .gb {
        background-color: #e8e8e8;
    }

    .size_table_border .dgb {
        background-color: #e8e8e8;
    }

    .td_lt {
        text-align: left;
        vertical-align: top;
        padding-top: 4px;
    }

    hr {
        border: 1px solid black;
        margin: 8px 0px;
    }

    .th_title {
        border: none;
        text-align: left;

    }

    .th_title p {
        font-size: 14px;
    }

    .ql-size-small {
        font-size: 0.75em;
    }

    .ql-size-huge {
        font-size: 2.5em;
    }

    .ql-size-large {
        font-size: 1.5em;
    }
    table {
        /*table-layout: fixed;*/
    }

    .table_border, .info_table {
        margin-top: 10px;
    }

    .partNameClass {
        width: auto;
    }

    .methodClass div {
        max-width: 110px;
        word-break: break-all;
    }

    .one_page_img {
        height: 166mm;
        width: 280mm;
        padding: 0mm;
        vertical-align: middle;
        text-align: center;
        display: table-cell;

    }

    .one_page_img img {
        max-height: 166mm;
        max-width: 284mm;
    }

</style>
<body>
<!-- 页眉 -->
<table class="table_no_border page_start bold">
    <tr>
        <td style="width: 20%;">Eifini</td>
        <td style="width: 20%;">${designNo}</td>
        <td style="width: 20%;">${styleNo} </td>
        <td style="width: 40%;text-align: right;">${createDate}${createTime}</td>
    </tr>
</table>


<!--基础信息 1 -->

<table class="table_no_border info_table">
    <tr>
        <td style="width: 15%;vertical-align: top;text-align: center;" rowspan="5">
            <table style="height: 156mm;">
                <tr>
                    <td style="height: 80mm;vertical-align: top;">
                        <div style="display: table-cell;text-align: center;vertical-align: middle;">
                            <img style="max-width: 58mm;max-height: 80mm;" src="${stylePic}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div style="display: table-cell;text-align: center;vertical-align: middle;">
                            <div class="bold" style="font-size: 16px; margin-bottom: 20px"> 扫码查看工艺单/视频</div>
                            <img style="width: 45mm;" src="${qrCodeUrl}">
                        </div>
                    </td>
                </tr>
            </table>


        </td>
        <td class="fg" rowspan="5"></td>
        <td style="width: 40%;vertical-align: top;">
            <table style="width: 100%;">
                <tr style="width: 100%;">
                    <td class="gb bold" style="width:40%;">大货款号</td>
                    <td class="gb" style="width:60%;">${styleNo}</td>
                </tr>
                <tr>
                    <td class=" bold">品名*</td>
                    <td>${productName}</td>
                </tr>
                <tr>
                    <td class="gb bold">执行标准*</td>
                    <td class="gb bold">${executeStandard}</td>
                </tr>
                <tr>
                    <td class="bold">质量等级</td>
                    <td>${qualityGrade}</td>
                </tr>
                <tr>
                    <td class="gb bold">安全标题</td>
                    <td class="gb">${saftyTitle}</td>
                </tr>
                <tr>
                    <td class="bold">安全类别</td>
                    <td>${saftyType}</td>
                </tr>
                <tr>
                    <td class="gb bold">包装形式*</td>
                    <td class="gb">${packagingForm}</td>
                </tr>
                <tr>
                    <td class="bold">包装袋标准*</td>
                    <td>${packagingBagStandard}</td>
                </tr>
                <tr>
                    <td class="gb bold">后技术工艺师</td>
                    <td class="gb">${technologistName}</td>
                </tr>
                <tr>
                    <td class="bold">后技术放码师</td>
                    <td>${gradingName}</td>
                </tr>
                <tr>

                    <td class="gb bold">后技术样衣工</td>
                    <td class="gb">${sampleMakerName}</td>
                </tr>
                <tr>
                    <td class="bold">设计师*</td>
                    <td>${designer}</td>
                </tr>
                <tr>
                    <td class="gb bold">版师*</td>
                    <td class="gb">${patternDesignName}</td>
                </tr>
                <tr>
                    <td class="td_lt bold">成分信息*</td>
                    <td class="td_lt" style="font-size: 11px">
                        <pre>${ingredient}</pre>
                    </td>
                </tr>
            </table>
        </td>
        <td class="fg"></td>
        <td style="width: 40%;vertical-align: top;">
            <table style="width: 100%;">
                <tr style="width: 100%;">
                    <td class="gb bold" style="width:40%;">外辅工艺</td>
                    <td class="gb" style="width:60%;">${extAuxiliaryTechnics}</td>
                </tr>
                <tr>
                    <td class="bold">★★注意事项</td>
                    <td>${mattersAttention}</td>
                </tr>
                <tr>
                    <td class="gb bold">洗唛材质备注</td>
                    <td class="gb">${washingMaterialRemarksName}</td>
                </tr>
                <tr>
                    <td class="bold">充绒量</td>
                    <td style="font-size: 11px">${downContent}</td>
                </tr>
                <tr>
                    <td class="gb bold">特殊规格</td>
                    <td class="gb" style="font-size: 11px">${specialSpec}</td>
                </tr>
                <tr>
                    <td class="bold">面料详情</td>
                    <td class="" style="font-size: 11px"><pre>${fabricDetails}</pre></td>
                </tr>
                <tr>
                    <td class="gb bold">描述</td>
                    <td class="gb">${remarks}</td>
                </tr>
                <tr>
                    <td class=" bold">号型类型*</td>
                    <td>${sizeRangeName}</td>
                </tr>
                <tr>
                    <td class=" gb bold">模板部件</td>
                    <td class="gb">${templatePart}</td>
                </tr>
                <tr>
                    <td class="bold">后技术下单员</td>
                    <td>${placeOrderStaffName}</td>
                </tr>
                <tr>
                    <td class="gb bold">下单时间</td>
                    <td class="gb">${placeOrderDateStr}</td>
                </tr>

            </table>
        </td>
    </tr>
    <tr>
        <td colspan="5">
            <p class="bold">提示信息</p>
            <hr>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width:100%">
                <tr class="gb" style="width:100%">
                    <td class="td_lt gb bold" style="width:40%;">温馨提示*</td>
                    <td class="td_lt gb" style="width:60%;">
                        <pre>${warmTips}</pre>
                    </td>
                </tr>
            </table>
        </td>
        <td class="fg"></td>
        <td>
            <table style="width:100%">
                <tr style="width:100%">
                    <td class="td_lt gb bold" style="width:40%">贮藏要求</td>
                    <td class="td_lt gb" style="width:60%">${storageDemandName}</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="5">
            <p class="bold" style="padding-top: 4px;">如果品类为鞋且非陈列品,请维护属性</p>
            <hr>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width:100%">
                <tr class="gb" style="width:100%">
                    <td class="td_lt gb bold" style="width:40%;">产地</td>
                    <td class="td_lt gb" style="width:60%;">
                        <pre>${producer}</pre>
                    </td>
                </tr>
            </table>
        </td>
        <td class="fg"></td>
        <td>
            <table style="width:100%">
                <tr style="width:100%">
                    <td class="td_lt gb bold" style="width:40%">生产日期</td>
                    <td class="td_lt gb" style="width:60%">${produceDateStr}</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="7">
            <p class="bold">洗标</p>
            <hr>
            <#if  washingLabel != '' >
                <img style="height:18px" src="${washingLabel}">
            </#if>
        </td>
    </tr>
</table>
<#if sizeDataList??>
    <#if sizeList?size gt 5>
        <table class="table_border size_table_border mt" style="page-break-before: always; ">
            <thead>
            <tr>
                <th colspan="${sizeTitleColspan}" class="th_title">
                    <p>测量点</p>
                    <hr>
                </th>
            </tr>
            <tr class="size_tr gb">
                <th rowspan="2" style="text-align: center;width: 120px;" class="partNameClass">部位</th>
                <th rowspan="2" style="text-align: center;">描述</th>
                <#if sizeList??>
                    <#list sizeList as size>
                        <th colspan="${sizeColspan}" class="size_${sizeClass[(size_index)*sizeColspan+2]} sizeWidth" style="border-left: 2.5px solid #000000; border-right: 2.5px solid #000000">
                            ${size}
                        </th>
                    </#list>
                </#if>
                <th rowspan="2" class="gc" style="text-align: center; padding: 0 10px;">公差<br/>(-)</th>
                <th rowspan="2" class="gc" style="text-align: center; padding: 0 10px;">公差<br/>(+)</th>
            </tr>

            <tr>
                <#list sizeList as size>
                    <td class="sizeItemWidth size_${sizeClass[(size_index+1)*sizeColspan-sizeColspan+2]}"  style="text-align: center;border-left: 2.5px solid #000000;<#if washSkippingFlag><#else>border-right: 2.5px solid #000000;</#if>">成衣<br>尺寸</td>
                    <#if washSkippingFlag>
                        <td class="sizeItemWidth size_${sizeClass[(size_index+1)*sizeColspan-sizeColspan+1+2]}" style="text-align: center;border-right: 2.5px solid #000000;">洗后<br>尺寸</td>
                    </#if>
                </#list>
            </tr>
            </thead>
            <tbody>
            <#if sizeDataList??>
                <#list sizeDataList as item>
                    <tr class="size_tr">
                        <#if item.rowType=="1">
                            <td style="text-align: left;" colspan="${sizeTitleColspan}"> ${item.remark}</td>
                        <#else>
                            <#list item.rowData as c>
                                <td class="${c.className} ${sizeClass[c_index]} "
                                    style="<#if sizeClass[c_index]?string == "wb" && (c_index+1) <= item.rowData?size && sizeClass[c_index+1]?string != "wb">
                                            border-right: 2.5px solid #000000;
                                    </#if>
                                    <#if (c_index-1) gt -1 && sizeClass[c_index]?string == "wb" && sizeClass[c_index-1]?string != "wb">
                                            border-left: 2.5px solid #000000;
                                    </#if>
                                    <#if c_index == item.rowData?size-3>
                                            border-right: 2.5px solid #000000;
                                    </#if>
                                    <#if c_index == 2>
                                            border-left: 2.5px solid #000000;
                                    </#if>">
                                    <div style="">
                                        <#if c_index gt 1>
                                            <p style="font-weight: bold;word-break: break-all;">${c.text}</p>
                                        <#else>
                                            ${c.text}
                                        </#if>

                                    </div>
                                </td>
                            </#list>
                        </#if>
                    </tr>
                </#list>
            </#if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="${sizeTitleColspan-2}" style="height: 32px;">测量点-${sizeDataList?size}</td>
                <td colspan="2" style="height: 32px;">单位:CM</td>
            </tr>
            </tfoot>
        </table>
    <#else>
        <table class="table_border mt size_table size_table_border" style="page-break-before: always; ">
            <thead>
            <tr>
                <th colspan="${sizeTitleColspan}" class="th_title">
                    <p>测量点</p>
                    <hr>
                </th>
            </tr>
            <tr class="size_tr gb">
                <th rowspan="2" style="text-align: center; width: 120px;" class="partNameClass">部位</th>
                <th rowspan="2" style="text-align: center;">描述</th>
                <#if sizeList??>
                    <#list sizeList as size>
                        <th colspan="${sizeColspan}" class="sizeWidth ${sizeClass[(size_index)*sizeColspan+2]} partNameClass" style="border-left: 2.5px solid #000000; border-right: 2.5px solid #000000">
                            ${size}
                        </th>
                    </#list>
                </#if>
                <th rowspan="2" class="gc" style="text-align: center; padding: 0 10px;">公差<br/>(-)</th>
                <th rowspan="2" class="gc" style="text-align: center; padding: 0 10px;">公差<br/>(+)</th>
            </tr>

            <tr>
                <#list sizeList as size>
                    <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+2]}" style="text-align: center;border-left: 2.5px solid #000000; <#if washSkippingFlag>padding: 0 10px;<#else>padding: 0 30px;border-right: 2.5px solid #000000;</#if>">成衣尺寸</td>
                    <#if washSkippingFlag>
                        <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+1+2]}" style="text-align: center; padding: 0 10px;border-right: 2.5px solid #000000">洗后尺寸</td>
                    </#if>
                </#list>
            </tr>
            </thead>
            <tbody>
            <#if sizeDataList??>
                <#list sizeDataList as item>
                    <tr class="size_tr">
                        <#if item.rowType=="1">
                            <td style="text-align: left;" colspan="${sizeTitleColspan}"> ${item.remark}</td>
                        <#else>
                            <#list item.rowData as c>
                                <td class="${c.className} ${sizeClass[c_index]} "
                                    style="<#if sizeClass[c_index]?string == "wb" && (c_index+1) <= item.rowData?size && sizeClass[c_index+1]?string != "wb">
                                            border-right: 2.5px solid #000000;
                                    </#if>
                                    <#if (c_index-1) gt -1 && sizeClass[c_index]?string == "wb" && sizeClass[c_index-1]?string != "wb">
                                            border-left: 2.5px solid #000000;
                                    </#if>
                                    <#if c_index == item.rowData?size-3>
                                            border-right: 2.5px solid #000000;
                                    </#if>
                                    <#if c_index == 2>
                                            border-left: 2.5px solid #000000;
                                    </#if>">
                                    <div style="">
                                        <#if c_index gt 1>
                                            <p style="font-weight: bold;word-break: break-all;">${c.text}</p>
                                        <#else>
                                            ${c.text}
                                        </#if>

                                    </div>
                                </td>
                            </#list>
                        </#if>
                    </tr>
                </#list>
            </#if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="${sizeTitleColspan-2}" style="height: 32px;">测量点-${sizeDataList?size}</td>
                <td colspan="2" style="height: 32px;">单位:CM</td>
            </tr>
            </tfoot>
        </table>
    </#if>
</#if>

<!-- 朴条位置(不需要) 归拔位置 4 2 -->
<#if false>
    <table class="table_border mt ptwz" style="page-break-inside: avoid;">
        <thead>
        <tr>
            <th class="th_title">
                <p>朴条位置 归拔位置</p>
                <hr>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <div class="one_page_img">
                    <#if cjgyImgList??>
                        <#list cjgyImgList as item>
                            <img src="${item.url}"/>
                        </#list>
                    </#if>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</#if>

<!--    基础工艺 4 3-->
<#if jcgyDataList?size gt 0>
    <table class="table_border mt"  style="page-break-before: always; ">
        <thead>
        <tr>
            <th colspan="3" class="th_title">
                <p>基础工艺</p>
                <hr>
            </th>
        </tr>
        <tr>
            <th class="gb item_th">工艺类型</th>
            <th class="gb">工艺描述</th>
        </tr>
        </thead>
        <tbody>
        <#if jcgyDataList??>
            <tr style="border-right: 0.5px solid #000">
                <td class="flex_td" colspan="2" rowspan="${jcgyRowsPan}">
                    <div>
                        <div class="flex_td_box">
                            <#list jcgyDataList as item>
                                <div>
                                    <div>
                                        <div class="item_td">
                                            <div style="height: 100%">
                                                ${item.processTypeName}
                                            </div>
                                        </div>
                                        <div>
                                            <div>
                                                ${item.content}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>

                    </div>
                </td>
            </tr>
        </#if>
        </tbody>

        <tfoot>
        <tr>
            <td colspan="3" style="height: 32px;">基础工艺-${jcgyDataList?size}</td>
        </tr>
        </tfoot>
    </table>
</#if>
<#if zysxDataList?size gt 0>
    <table class="table_border mt" <#if ztbzDataList?size gt 0><#else>style="page-break-before: always;"</#if>>
        <thead>
        <tr>
            <th colspan="2" class="th_title">
                <p>注意事项</p>
                <hr>
            </th>
        </tr>
        <tr>
            <th class="gb item_th">工艺项目</th>
            <th class="gb">描述</th>
        </tr>
        </thead>
        <tbody>
        <#if zysxDataList??>
            <#list zysxDataList as item>
                <tr>
                    <td class="item_td">${item.item}</td>
                    <td>
                        ${item.content}
                    </td>
                </tr>
            </#list>
        </#if>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="3" style="height: 30px;">注意事项-${zysxDataList?size}</td>
        </tr>
        </tfoot>
    </table>
</#if>
<!--    整烫包装 6-->
<#if ztbzDataList?size gt 0>
    <table class="table_border mt" <#if jcgyDataList?size gt 0><#else>style="page-break-before: always;"</#if>  >
        <thead>
        <tr>
            <th colspan="2" class="th_title">
                <p>整烫包装</p>
                <hr>
            </th>
        </tr>
        <tr>
            <th class="gb item_td">工艺项目</th>
            <th class="gb" >描述</th>
        </tr>
        </thead>
        <tbody>
        <#if ztbzDataList??>
            <#list ztbzDataList as item>
                <tr>
                    <td class="item_td">${item.item}</td>
                    <td>
                        ${item.content}
                    </td>
                </tr>
            </#list>
        </#if>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="3" style="height: 32px;">整烫包装-${ztbzDataList?size}</td>
        </tr>
        </tfoot>
    </table>
</#if>

</body>
</html>
