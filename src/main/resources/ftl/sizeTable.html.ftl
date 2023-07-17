<style>
    table {
        border-top: 1px solid #999;
        border-left: 1px solid #999;
        border-spacing: 0;

    }

    td,
    th {

        border-bottom: 1px solid #999;
        border-right: 1px solid #999;
    }

    .sizeItemWidth {
        min-width: 65px;
    }

    .sizeWidth {
        min-width: ${65*colspan}px;
    }
</style>
<div>
    <p>测量点·</p>
    <hr>

    <table>
        <thead>
        <tr>
            <th rowspan="2">部位</th>
            <th rowspan="2">描述</th>
            <#list sizeList as size>
                <th colspan="${colspan}" class="sizeWidth">${size}</th>
            </#list>
            <th rowspan="2">公差(- )</th>
            <th rowspan="2">公差(+)</th>
        </tr>
        <tr>
            <#list sizeList as size>
                <td class="sizeItemWidth">样板尺寸</td>
                <td class="sizeItemWidth">成衣尺寸</td>
                <#if washSkippingFlag>
                    <td class="sizeItemWidth">洗后尺寸</td>
                </#if>
            </#list>
        </tr>
        </thead>
        <tbody>
        <#list dataList as item>
            <tr>
                <#list item as c>
                    <td>${c}</td>
                </#list>
            </tr>
        </#list>
        </tbody>
    </table>
</div>