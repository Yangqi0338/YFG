<html>
<head>
    <meta charset="UTF-8">
</head>
<style>
    * {
        font-family: 'Source Han Serif CN', 'Noto Serif SC', SimSun;
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

    .table_border {
        width: 100%;
        border-collapse: collapse;
        padding-bottom: 5px;
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
        background-color: #dadada;
    }

    .fg {
        min-width: 30px;
    }

    .wb {
        background-color: white;
    }

    .td_lt {
        text-align: left;
        vertical-align: top;
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
<table class="table_no_border page_start font_bold">
    <tr>
        <td style="width: 20%;">Eifini</td>
        <td style="width: 20%;">${designNo}</td>
        <td style="width: 20%;">${styleNo} </td>
        <td style="width: 40%;text-align: right;">${createDate}${createTime}</td>
    </tr>
</table>


<!--基础信息-->

<table class="table_no_border">
    <tr>
        <!-- 图片-->
        <td style="width: 20%;" rowspan="13">
            <img style="width: 100%" src="${stylePic}">
        </td>
        <td class="fg"></td>
        <td class="gb" style="width:15%;">大货款号</td>
        <td class="gb" style="width:25%;">${styleNo}</td>
        <td class="fg"></td>
        <td class="gb" style="width:15%;">外辅工艺</td>
        <td class="gb" style="width:25%;">${extAuxiliaryTechnics}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td>品名*</td>
        <td>${productName}</td>
        <td class="fg"></td>
        <td class="td_lt" rowspan="3">★★注意事项</td>
        <td class="td_lt" rowspan="3">${mattersAttention}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="gb">执行标准*</td>
        <td class="gb font_bold">${executeStandard}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td>质量等级</td>
        <td>${qualityGrade}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="gb">安全标题</td>
        <td class="gb">${saftyTitle}</td>
        <td class="fg"></td>
        <td class="gb">洗唛材质备注</td>
        <td class="gb">${washingMaterialRemarks}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td>安全类别</td>
        <td>${saftyType}</td>
        <td class="fg"></td>
        <td>充绒量</td>
        <td>${downContent}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="gb">包装形式*</td>
        <td class="gb">${packagingForm}</td>
        <td class="fg"></td>
        <td class="gb">特殊规格</td>
        <td class="gb">2</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td>包装袋标准*</td>
        <td>${packagingBagStandard}</td>
        <td class="fg"></td>
        <td rowspan="3" class="td_lt">面料详情</td>
        <td rowspan="3" class="td_lt">${fabricDetails}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="gb">后技术工艺师</td>
        <td class="gb">${technologistName}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td>后技术放码师</td>
        <td>${gradingName}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="gb">后技术样衣工</td>
        <td class="gb">${sampleMakerName}</td>
        <td class="fg"></td>
        <td class="gb">号型类型*</td>
        <td class="gb">${sizeRangeName}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td>设计师*</td>
        <td>${designer}</td>
        <td class="fg"></td>
        <td>模板部件</td>
        <td>${templatePart}</td>
    </tr>
    <!-- 13 -->
    <tr>
        <td class="fg"></td>
        <td class="gb">版师*</td>
        <td class="gb">${patternDesignName}</td>
        <td class="fg"></td>
        <td>后技术下单员</td>
        <td>${placeOrderStaffName}</td>
    </tr>
    <tr>
        <!-- 14 -->
        <td style="text-align: center;font-weight: bold;">
            扫码查看工艺单
        </td>
        <td class="fg"></td>
        <td class="td_lt">成分信息*</td>
        <td class="td_lt">${ingredient}</td>
        <td class="fg"></td>
        <td colspan="2" style="vertical-align: top;">
            <table class="table_no_border gb">
                <tr>
                    <td style="width: 145px;">下单时间</td>
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
        <td class="fg"></td>
        <td colspan="5">
            <p>提示信息</p>
            <hr>
        </td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="td_lt gb">温馨提示*</td>
        <td class="td_lt gb">${warmTips}</td>
        <td class="fg"></td>
        <td class="td_lt gb">贮藏要求</td>
        <td class="td_lt gb">${storageDemand}</td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td colspan="5">
            <p>如果品类为鞋且非陈列品,请维护属性</p>
            <hr>
        </td>
    </tr>
    <tr>
        <td class="fg"></td>
        <td class="gb">产地</td>
        <td class="gb">${producer}</td>
        <td class="fg"></td>
        <td class="gb">生产日期</td>
        <td class="gb">${produceDateStr}</td>
    </tr>
    <tr>
        <td colspan="7">
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
        <th class="item_td gb">工艺项目</th>
        <th class="content_tr gb">
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
            <#list cjgyImgList as item>
                <img class="img_item" src="${item.url}"/>
            </#list>
        </td>
    </tr>
    </tbody>
</table>
<#--注意事项-->
<table class="table_border mt" style="page-break-before: always;page-break-inside: avoid;">
    <thead>
    <tr>
        <th class="th_title">
            <p>注意事项</p>
            <hr>
        </th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
            <#list zysxImgList as item>
                <img class="img_item" src="${item.url}"/>
            </#list>
        </td>
    </tr>
    </tbody>
</table>

<!--测量点 -->

<table class="table_border small_table mt size_table" style="page-break-before: always; ">
    <thead>
    <tr>
        <th colspan="${sizeTitleColspan}" class="th_title">
            <p>测量点</p>
            <hr>
        </th>
    </tr>
    <tr class="size_tr gb">
        <th rowspan="2" style="text-align: left;">部位</th>
        <th rowspan="2" style="text-align: left;">描述</th>
        <#list sizeList as size>
            <th colspan="${sizeColspan}" class="sizeWidth ${sizeClass[(size_index)*sizeColspan+2]}">
                <#if size==defaultSize>★</#if>
                ${size}
                <#if size==defaultSize>★</#if>
            </th>
        </#list>
        <th rowspan="2" class="gc">公差(-)</th>
        <th rowspan="2" class="gc">公差(+)</th>
    </tr>

    <tr>
        <#list sizeList as size>
            <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+2]}">样板<br>尺寸</td>
            <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+1+2]}">成衣<br>尺寸</td>
            <#if washSkippingFlag>
                <td class="sizeItemWidth ${sizeClass[(size_index+1)*sizeColspan-sizeColspan+2+2]}">洗后<br>尺寸</td>
            </#if>
        </#list>
    </tr>
    </thead>
    <tbody>
    <#list sizeDataList as item>
        <tr class="size_tr">
            <#if item.rowType=="1">
                <td style="height: 32px;text-align: left;" colspan="${sizeTitleColspan}"> ${item.remark}</td>
            <#else>
                <#list item.rowData as c>
                    <td style="height: 32px" class="${sizeClass[c_index]}"> ${c.text}</td>
                </#list>
            </#if>
        </tr>
    </#list>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="${sizeTitleColspan}" style="height: 32px;">测量点-${sizeDataList?size}</td>
    </tr>
    </tfoot>
</table>


<!--    小部件-->
<table class="table_border mt" style="page-break-before: always;">
    <thead>
    <tr>
        <th colspan="4" class="th_title">
            <p>基础工艺</p>
            <hr>
        </th>
    </tr>
    <tr>

        <th class="gb" style="width: 10%">图片</th>
        <th class="gb" style="width: 10%">编码</th>
        <th class="gb" style="width: 10%">工艺项目</th>
        <th class="gb" style="width: 50%">工艺描述</th>
    </tr>
    </thead>
    <tbody>
    <#list xbjDataList as item>
        <tr>
            <#if item_index==0>
                <td rowspan="${xbjRowsPan}" style="width: 30%">
                    <div class="one_imgs">
                        <#list xbjImgList as item>
                            <img class="one_imgs_item" src="${item.url}"/>
                        </#list>
                    </div>
                </td>
            </#if>
            <td>${item.itemCode}</td>
            <td>${item.item}</td>
            <td>
                ${item.content}
            </td>
        </tr>
    </#list>
    </tbody>
</table>

<!--    裁剪工艺 -->
<table class="table_border mt" style="page-break-before: always">
    <thead>
    <tr>
        <th colspan="3" class="th_title">
            <p>裁剪工艺</p>
            <hr>
        </th>
    </tr>
    <tr>
        <th class="gb" style="width: 10%">工艺项目</th>
        <th class="gb" style="width: 50%">描述</th>
        <th class="gb" style="width: 50%">图片</th>

    </tr>
    </thead>
    <tbody>
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
    </tbody>

    <tfoot>
    <tr>
        <td colspan="3" style="height: 32px;">裁剪工艺-${jcgyDataList?size}</td>
    </tr>
    </tfoot>
</table>

<!--    整烫包装 -->
<table class="table_border mt">
    <thead>
    <tr>
        <th colspan="2" class="th_title">
            <p>整烫包装</p>
            <hr>
        </th>
    </tr>
    <tr>

        <th class="gb" style="width: 10%">工艺项目</th>
        <th class="gb" style="width: 50%">描述</th>

    </tr>
    </thead>
    <tbody>
    <#list ztbzDataList as item>
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
        <td colspan="3" style="height: 32px;">整烫包装-${ztbzDataList?size}</td>
    </tr>
    </tfoot>
</table>

</body>
</html>
