<html>
<style>
    html {

        width: 277mm;
        margin: 50px auto;
        background-color: #fff;
        padding: 16px;
    }

    * {
        margin: 0;
        padding: 0;
    }

    body {
        background-color: #f3f3f3;
    }

    tr {
        page-break-inside: avoid;
    }

    .page_height {
        height: 210px;
    }

    .table_border {
        width: 100%;
        border: 1px solid black;
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
        height: 32px;
    }

    .info_table tr, td {
        border: none;
    }

    .info_table_b {
        border-left: 12px solid white !important;
    }

    .table_border td {
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

    .div_img_list {

        display: flex;
        flex-wrap: wrap;
        gap: 16px;
        justify-content: space-between;
        align-items: center;
        border: 1px solid black;
    }

    .one_imgs {
        text-align: center;
    }

    .one_imgs_item {
        margin: 0 auto;
        width: 80%;
    }

    .div_img_list .title {
        width: 100%;
        text-align: center;
        font-weight: 700;
        font-size: 18px;
    }

    .div_img_list .border {
        border-bottom: 1px solid black;

    }

    .sizeItemWidtd {
        min-widtd: 65px;
    }

    .sizeWidtd {
        min-widtd: ${65*sizeColspan}px;
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
        width: 30%;
    }

    .info_tr3:nth-child(even) {
        background-color: #e7e6e6;
    }

    .info_tr4 {
        background-color: #e7e6e6;
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
</style>
<body>
<!-- 页眉 -->
<table class="table_no_border page_start">
    <tr>
        <td>品牌22</td>
        <td>${brandName}</td>
        <td>设计款号</td>
        <td>${designNo}</td>
        <td>大货号</td>
        <td>${styleNo}</td>
        <td>制单日期</td>
        <td>${createDate}</td>
        <td>${createTime}</td>
        <td style="width: 120px;"></td>
    </tr>
</table>


<!--基础信息-->

<table class="info_table">
    <tr>
        <td colspan="7" class="table_header">${companyName}</td>
    </tr>
    <tr class="info_tr4">
        <td style="width: 25%;" rowspan="9">
            <img style="width: 100%" src="${stylePic}">
        </td>
        <td class="info_table_b">大货款号</td>
        <td>${styleNo}</td>
        <td class="info_table_b">注意事项</td>
        <td>${mattersAttention}</td>
        <td class="info_table_b">设计师</td>
        <td>${designer}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">品名</td>
        <td>${prodCategoryName}</td>
        <td class="info_table_b">外辅工艺</td>
        <td>${extAuxiliaryTechnics}</td>
        <td class="info_table_b">后技术放码师</td>
        <td>${gradingName}</td>
    </tr>

    <tr class="info_tr3">
        <td class="info_table_b">执行标准</td>
        <td>${executeStandardCode}</td>
        <td class="info_table_b">洗唛材质备注</td>
        <td>${washingMaterialRemarks}</td>
        <td class="info_table_b">后技术放码师</td>
        <td>${gradingName}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">质量等级</td>
        <td>${qualityGrade}</td>
        <td class="info_table_b">包装形式</td>
        <td>${packagingForm}</td>
        <td class="info_table_b">后技术样衣工</td>
        <td>${sampleMakerName}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">安全标题</td>
        <td>${saftyTitle}</td>
        <td class="info_table_b">包装袋标准</td>
        <td>${packagingBagStandard}</td>
        <td class="info_table_b">后技术工艺师</td>
        <td>${technologistName}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">安全类别</td>
        <td>${saftyType}</td>
        <td class="info_table_b">特殊规格</td>
        <td></td>
        <td class="info_table_b">模板部件</td>
        <td>${templatePart}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">号型类型</td>
        <td>${sizeRangeName}</td>
        <td class="info_table_b" colspan="2">${specialSpec}</td>
        <td class="info_table_b">下单时间</td>
        <td>${placeOrderDateStr}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">成分信息</td>
        <td colspan="2">${composition}</td>
        <td colspan="2" class="info_table_b">充绒量</td>
        <td>${downContent}</td>
    </tr>
    <tr class="info_tr3">
        <td class="info_table_b">贮藏要求</td>
        <td colspan="2">${storageDemand}</td>
        <td colspan="2" class="info_table_b">面料详情</td>
        <td>${fabricDetails}</td>
    </tr>
    <tr class="info_tr3">
        <td>产地:${producer}</td>
        <td colspan="3" style="padding-left: 6px;">温馨提示:</td>
        <td colspan="3" style="padding-left: 6px;">洗标:</td>
    </tr>
    <tr class="info_tr3">
        <td>
            生产日期:${produceDateStr}
            <hr/>
            <p>如果品类为鞋且非陈列品，请维护属性</p>
        </td>
        <td colspan="3">${warmTips}</td>
        <td colspan="3">
            <#if  washingLabel != '' >
                <img style="width:120px" src="${washingLabel}">
            </#if>
        </td>
    </tr>
</table>
<!--    裁剪工艺-->
<table class="table_border mt" style="page-break-before: always;">
    <tr>
        <td colspan="2" class="table_header">裁剪工艺描述</td>
    </tr>
    <tr>
        <td class="item_td">工艺项目</td>
        <td class="content_tr">
            描述
        </td>
    </tr>
    <#list cjgyDataList as item>
        <tr>
            <td>${item.item}</td>
            <td>
                ${item.content}
            </td>
        </tr>
    </#list>
</table>
<!-- 朴条位置 归拔位置 -->
<table class="table_border mt">
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
<table class="table_border mt" style="page-break-before: always;">
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

<#--尺寸表-->
<table class="table_border small_table mt" style="page-break-before: always; ">
    <tr>
        <td colspan="${sizeTitleColspan}" class="table_header">尺寸表</td>
    </tr>
    <tr>
        <td rowspan="2">部位</td>
        <td rowspan="2">描述</td>
        <#list sizeList as size>
            <td colspan="${sizeColspan}" class="sizeWidtd">${size}</td>
        </#list>
        <td rowspan="2">公差(- )</td>
        <td rowspan="2">公差(+)</td>
    </tr>
    <tr>
        <#list sizeList as size>
            <td class="sizeItemWidtd">样板尺寸</td>
            <td class="sizeItemWidtd">成衣尺寸</td>
            <#if washSkippingFlag>
                <td class="sizeItemWidtd">洗后尺寸</td>
            </#if>
        </#list>
    </tr>
    <#list sizeDataList as item>
        <tr>
            <#list item as c>
                <td>${c}</td>
            </#list>
        </tr>
    </#list>

</table>
<#--    小部件-->
<table class="table_border mt" style="page-break-before: always;">
    <tr>
        <td colspan="4" class="table_header">小部件</td>
    </tr>
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

<#--    基础工艺 -->
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

<#--    整烫包装 -->
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
