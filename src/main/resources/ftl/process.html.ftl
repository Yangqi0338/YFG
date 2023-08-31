<html>
<head>
    <meta charset="UTF-8">
</head>
<style>
    * {
        font-family: '思源宋体', 'Noto Serif SC', SimSun;
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

    .tbody {
        display: block;
        max-height: 300px; /* 设置最大高度以启用滚动 */
        overflow-y: scroll; /* 垂直滚动 */
    }

    .page_height {
        height: 210px;
    }

    .table_border {
        width: 100%;
        border-collapse: collapse;
    }

    .table_no_border {
        width: 100%;
    }

    .info_table {
        width: 100%;
        border: none;
        border-collapse: collapse;
    }

    .info_table td {
        height: 20px;
    }

    .info_table tr, td {
        border: none;
    }


    .table_border td, th {
        border: 1px solid black;
    }

    .table_header {
        font-size: 20px;
        text-align: center;
        font-weight: 700;
    }

    .mt {
        margin-top: 16px;
    }


    .one_imgs {
        text-align: center;
    }

    .one_imgs_item {
        margin: 0 auto;
        width: 80%;
    }


    .sizeItemWidth {
        width: 36px;
        text-align: center;
    }

    .sizeWidth {
        width: 100px;
        text-align: center;
    }

    .item_td {
        width: 20%;
    }

    .content_tr {
        width: 80%;
    }

    .small_table {
        font-size: 12px;
    }

    .img_item {
        margin: 5px;
        width: 45%;
    }


    .info_tr4 td:nth-child(1) {
        background-color: white;
    }

    .info_tr3 td:nth-child(odd) {
        width: 10%;
    }

    .info_tr3 td:nth-child(even) {
        width: 15%;
    }

    .page_start {
        background-color: #e7e6e6;
    }

    .gc {
        width: 44px;
        text-align: center !important;
    }

    .size_table {
        width: auto;
    }

    .size_tr {
        text-align: center;
        height: 35px;
    }

    .font_bold {
        font-weight: bold;
    }

    .gb {
        background-color: #999999;
    }

    .td_lt {
        text-align: left;
        vertical-align: top;
    }

    .info_table .l {
        border-left: 16px solid white;
    }

    .info_table .r {
        border-right: 16px solid white;
    }

    hr {
        border: 1px solid black;
        margin: 8px 0px;

    }

    .th_title {
        border: none;
        text-align: left;
    }
</style>
<body>
<!-- 页眉 -->
<table data-name="pageStart" class="table_no_border page_start font_bold">
    <tr>
        <td>Eifini</td>
        <td>${designNo}</td>
        <td>${designNo} BOM</td>
        <td>DRAFT-大货生产${createDate}${createTime}</td>
        <td style="width: 120px;"></td>
    </tr>
</table>


<!--基础信息-->

<table class="info_table">
    <tr>
        <!-- 图片-->
        <td style="width: 20%;" rowspan="13">
            <img style="width: 100%" src="${stylePic}">
        </td>
        <td class="l gb" style="width:15%;">大货款号</td>
        <td class="r gb" style="width:25%;">${styleNo}</td>
        <td class="l gb" style="width:15%;">外辅工艺</td>
        <td class="r gb" style="width:25%;">${extAuxiliaryTechnics}</td>
    </tr>
    <tr>
        <td class="l">品名*</td>
        <td class="r">${productName}</td>
        <td class="l td_lt" rowspan="3">★★注意事项</td>
        <td class="r td_lt" rowspan="3">${mattersAttention}</td>
    </tr>
    <tr>
        <td class="l gb">执行标准*</td>
        <td class="r gb font_bold">${executeStandard}</td>
    </tr>
    <tr>
        <td class="l">质量等级</td>
        <td class="r">${qualityGrade}</td>
    </tr>
    <tr>
        <td class="l gb">安全标题</td>
        <td class="r gb">${saftyTitle}</td>
        <td class="l gb">洗唛材质备注</td>
        <td class="r gb">${washingMaterialRemarks}</td>
    </tr>
    <tr>
        <td class="l">安全类别</td>
        <td class="r">${saftyType}</td>
        <td class="l">充绒量</td>
        <td class="r">${downContent}</td>
    </tr>
    <tr>
        <td class="l gb">包装形式*</td>
        <td class="r gb">${packagingForm}</td>
        <td class="l gb">特殊规格</td>
        <td class="r gb">2</td>
    </tr>
    <tr>
        <td class="l">包装袋标准*</td>
        <td class="r">${packagingBagStandard}</td>
        <td rowspan="3" class="l td_lt">面料详情</td>
        <td rowspan="3" class="r td_lt">${fabricDetails}</td>
    </tr>
    <tr>
        <td class="l  gb">后技术工艺师</td>
        <td class="r  gb">${technologistName}</td>
    </tr>
    <tr>
        <td class="l">后技术放码师</td>
        <td class="r">${gradingName}</td>
    </tr>
    <tr>
        <td class="l gb">后技术样衣工</td>
        <td class="r gb">${sampleMakerName}</td>
        <td class="l gb">号型类型*</td>
        <td class="r gb">${sizeRangeName}</td>
    </tr>
    <tr>
        <td class="l">设计师*</td>
        <td class="r">${designer}</td>
        <td class="l">模板部件</td>
        <td class="r">${templatePart}</td>
    </tr>
    <!-- 13 -->
    <tr>
        <td class="l gb">版师*</td>
        <td class="r gb">${patternDesignName}</td>
        <td class="l ">后技术下单员</td>
        <td class="r ">${placeOrderStaffName}</td>
    </tr>
    <tr>
        <!-- 14 -->
        <td style="text-align: center;font-weight: bold;">
            扫码查看工艺单
        </td>
        <td class="l td_lt">成分信息*</td>
        <td class="r td_lt">
            ${ingredient}</td>
        <td class="l r " colspan="2" style="vertical-align: top;">
            <table class="table_no_border gb">
                <tr>
                    <td>下单时间</td>
                    <td>${placeOrderDateStr}</td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <!-- 二维码 -->
        <td rowspan="4">
            <#if  qrCodeUrl != '' >
                <img style="width: 100%" src="${qrCodeUrl}">
            </#if>
        </td>
        <td class="l r" colspan="4">
            <p>提示信息</p>
            <hr>
        </td>
    </tr>
    <tr>

        <td class="td_lt l gb">温馨提示*</td>
        <td class="td_lt r  gb">${warmTips}</td>

        <td class="td_lt l gb">贮藏要求</td>
        <td class="td_lt r gb">${storageDemand}</td>
    </tr>
    <tr>
        <td class="l r" colspan="4">
            <p>如果品类为鞋且非陈列品,请维护属性</p>
            <hr>
        </td>
    </tr>
    <tr>
        <td class="l gb">产地</td>
        <td class="r gb">${producer}</td>
        <td class="l gb">生产日期</td>
        <td class="r gb">${produceDateStr}</td>
    </tr>
    <tr>
        <td class="l r" colspan="5">
            <p>洗标</p>
            <hr>
            <#if  washingLabel != '' >
                <img style="height:50px" src="${washingLabel}">
            </#if>
        </td>
    </tr>
</table>
<!--    裁剪工艺-->
<table class="table_border mt" style="page-break-before: always;">
    <thead>
    <tr>
        <th class="th_title" colspan="2">
            <p>裁剪工艺</p>
            <hr>
        </th>
    </tr>
    <tr>
        <th class="item_td">工艺项目</th>
        <th class="content_tr">
            描述
        </th>
    </tr>
    </thead>
    <tbody>
    <#list cjgyDataList as item>
        <tr>
            <td>${item.item}</td>
            <td>
                ${item.content}
            </td>
        </tr>
    </#list>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="2">裁剪工艺-${cjgyDataList?size} </td>
    </tr>
    </tfoot>
</table>
<!-- 朴条位置 归拔位置 -->
<table class="table_border mt" style="page-break-inside: avoid;">
    <tr>
        <td class="table_header">朴条位置 归拔位置</td>
    </tr>
    <tr>
        <td>
            <#list cjgyImgList as item>
                <img class="img_item" src="${item.url}"/>
            </#list>
        </td>
    </tr>
</table>
<#--注意事项-->
<table class="table_border mt" style="page-break-before: always;page-break-inside: avoid;">
    <tr>
        <td class="table_header">注意事项</td>
    </tr>
    <tr>
        <td>
            <#list zysxImgList as item>
                <img class="img_item" src="${item.url}"/>
            </#list>
        </td>
    </tr>
</table>

<!--尺寸表 -->

<table class="table_border small_table mt size_table" style="page-break-before: always; ">
    <thead>
    <tr>
        <th colspan="${sizeTitleColspan}" class="th_title">
            <p>尺寸表</p>
            <hr>
        </th>
    </tr>
    <tr class="size_tr">
        <th rowspan="2" style="text-align: left;">部位</th>
        <th rowspan="2" style="text-align: left;">描述</th>
        <#list sizeList as size>
            <#if defaultSize==size>
                <th colspan="${sizeColspan}" class="sizeWidth gb">${size}</th>
            <#else>
                <th colspan="${sizeColspan}" class="sizeWidth">${size}</th>
            </#if>
        </#list>
        <th rowspan="2" class="gc">公差(-)</th>
        <th rowspan="2" class="gc">公差(+)</th>
    </tr>

    <tr>
        <#list sizeList as size>
            <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan]}">样板<br>尺寸</td>
            <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+1]}">成衣<br>尺寸</td>
            <#if washSkippingFlag>
                <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+2]}">洗后<br>尺寸</td>
            </#if>
        </#list>
    </tr>
    </thead>
    <tbody>
    <#list sizeDataList as item>
        <tr class="size_tr">
            <#list item as c>
                <td style="height: 32px" class="${c.className}"> ${c.text}</td>
            </#list>
        </tr>
    </#list>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="${sizeTitleColspan}" style="height: 32px;">尺寸表-${sizeDataList?size}</td>
    </tr>
    </tfoot>
</table>


<!--    小部件-->
<table class="table_border mt" style="page-break-before: always;">

    <tr>
        <td rowspan="${xbjRowsPan}" style="width: 30%">
            <div class="one_imgs">
                <#list xbjImgList as item>
                    <img class="one_imgs_item" src="${item.url}"/>
                </#list>
            </div>
        </td>
        <td style="width: 10%">编码</td>
        <td style="width: 10%">工艺项目</td>
        <td style="width: 50%">工艺描述</td>
    </tr>
    <#list xbjDataList as item>
        <tr>
            <td>${item.itemCode}</td>
            <td>${item.item}</td>
            <td>
                ${item.content}
            </td>
        </tr>
    </#list>
</table>

<!--    基础工艺 -->
<table class="table_border mt" style="page-break-before: always">
    <tr>
        <td colspan="3" class="table_header">基础工艺</td>
    </tr>
    <tr>

        <td style="width: 10%">工艺项目</td>
        <td style="width: 50%">描述</td>
        <td style="width: 50%">图片</td>

    </tr>
    <#list jcgyDataList as item>
        <tr>
            <td>${item.item}</td>
            <td>
                ${item.content}
            </td>
            <#if  item_index==0 >
                <td rowspan="${jcgyRowsPan}" style="width: 30%">
                    <div class="one_imgs">
                        <#list jcgyImgList as item>
                            <img class="one_imgs_item" src="${item.url}"/>
                        </#list>
                    </div>
                </td>
            </#if>
        </tr>
    </#list>
</table>

<!--    整烫包装 -->
<table class="table_border mt">
    <tr>
        <td colspan="3" class="table_header">整烫包装</td>
    </tr>
    <tr>

        <td style="width: 10%">工艺项目</td>
        <td style="width: 50%">描述</td>

    </tr>
    <#list ztbzDataList as item>
        <tr>
            <td>${item.item}</td>
            <td>
                ${item.content}
            </td>
        </tr>
    </#list>
</table>

</body>
</html>
