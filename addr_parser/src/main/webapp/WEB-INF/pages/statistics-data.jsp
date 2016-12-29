<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="${ctx}/static/css/signin.css" rel="stylesheet"/>
    <!-- Le styles -->
    <link href="${ctx}/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
        table tr td {
            text-align: center;
        }
    </style>
    <link href="${ctx}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx}/static/scripts/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.js"></script>


    <link href="${ctx}/layer/skin/layer.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/layer/layer.js"></script>
    <script type="text/javascript" src="${ctx}/static/scripts/echarts.min.js"></script>

    <style type="text/css">
        .simple-title {
            background-color: #2f96b4;
            color: white
        }
    </style>

</head>

<body>

<div style="text-align: left; color: #000000;margin-top: 100px;">
    <table>
        <tr>
            <td style="text-align:right">缓存更新时间</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left">${lastUpdateTime} <a onclick="window.parent.refresh();return false;" href="${ctx}/statistics/data.do?refresh=true">刷新</a> </td>
        </tr>
        <tr>
            <td style="text-align:right"></td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"></td>
        </tr>
        <tr>
            <td style="text-align:right">总小区数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${residenceCount}" pattern="#,#00"/></td>
        </tr>
        <tr>
            <td style="text-align:right">覆盖小区数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${residenceCoverdCount}" pattern="#,#00"/>，小区覆盖率${residenceCoverdRate}</td>
        </tr>
        <tr>
            <td style="text-align:right"></td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"></td>
        </tr>
        <tr>
            <td style="text-align:right">楼栋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${buildingCount}" pattern="#,#00"/></td>
        </tr>
        <tr>
            <td style="text-align:right">楼栋数(有坐标)</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${buildingWithCoordinate}" pattern="#,#00"/></td>
        </tr>
        <tr>
            <td style="text-align:right"></td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"></td>
        </tr>
        <tr>
            <td style="text-align:right">房屋总数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${countRoom}" pattern="#,#00"/></td>
        </tr>
        <%-- <tr>
            <td style="text-align:right">交易/复评获取房屋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${countRoomDeal}" pattern="#,#00"/></td>
        </tr>
        <tr>
            <td style="text-align:right">其它来源获取房屋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${countRoomOthers}" pattern="#,#00"/></td>
        </tr> --%>
        <tr>
            <td style="text-align:right">已经锁定的房屋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${countRoomLocked}" pattern="#,#00"/>(来源于交易记录/复评，解析规则相对严谨)</td>
        </tr>
        <tr>
            <td style="text-align:right"></td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"></td>
        </tr>
        <tr>
            <td style="text-align:right">有面积房屋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${houseWithArea}" pattern="#,#00"/>，占比${houseWithAreaRate}</td>
        </tr>
        <tr>
            <td style="text-align:right">有朝向房屋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${houseWithTowards}" pattern="#,#00" /></td>
        </tr>
        <%-- <tr>
            <td style="text-align:right">有户型房屋数</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${houseWithHuxing}" pattern="#,#00" /></td>
        </tr> --%>
        <tr>
            <td style="text-align:right"></td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"></td>
        </tr>
        <%-- <tr>
            <td style="text-align:right">房屋中地址解析规则90分以上数量</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${parseConfirmedHouseCount}" pattern="#,#00" /></td>
        </tr>
        <tr>
            <td style="text-align:right">房屋中地址解析规则90分以下数量</td>
            <td>&nbsp;&nbsp;</td>
            <td style="text-align:left"><fmt:formatNumber value="${parseUnconfirmedHouseCount}" pattern="#,#00" /></td>
        </tr> --%>
    </table>

    <div style="margin: 0; padding: 80px;">
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main1" style="width: 450px;height:350px;float: left"></div>
    <div id="main2" style="width: 450px;height:350px;float: left"></div>
    <div id="main3" style="width: 450px;height:350px;float: left"></div>
    <div id="main4" style="width: 450px;height:350px;float: left"></div>
    <div id="main5" style="width: 450px;height:350px;float: left"></div>
    </div>
    <script type="text/javascript">

        echarts.init(document.getElementById('main1')).setOption({
            title : {
                text: '小区覆盖率',
                subtext: '小区总数<fmt:formatNumber value="${residenceCount}" pattern="#,#00"/>',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['楼盘字典已覆盖','楼盘字典未覆盖']
            },
            series : [
                {
                    name: '占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:${residenceCoverdCount}, name:'已覆盖(<fmt:formatNumber value="${residenceCoverdCount}" pattern="#,#00"/>)'},
                        {value:${residenceCount - residenceCoverdCount}, name:'未覆盖(<fmt:formatNumber value="${residenceCount - residenceCoverdCount}" pattern="#,#00"/>)'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        });

        echarts.init(document.getElementById('main2')).setOption({
            title : {
                text: '楼栋',
                subtext: '楼栋总数<fmt:formatNumber value="${buildingWithCoordinate}" pattern="#,#00"/>',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['有坐标','无坐标']
            },
            series : [
                {
                    name: '占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:${buildingWithCoordinate}, name:'有坐标(<fmt:formatNumber value="${buildingWithCoordinate}" pattern="#,#00"/>)'},
                        {value:${buildingCount - buildingWithCoordinate}, name:'无坐标(${buildingCount - buildingWithCoordinate})'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        });

        echarts.init(document.getElementById('main3')).setOption({
            title : {
                text: '房屋面积填充率',
                subtext: '房屋总数<fmt:formatNumber value="${countRoom}" pattern="#,#00"/>',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['有面积','无面积']
            },
            series : [
                {
                    name: '占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:${houseWithArea}, name:'有面积(<fmt:formatNumber value="${houseWithArea}" pattern="#,#00"/>)'},
                        {value:${countRoom - houseWithArea}, name:'无面积(<fmt:formatNumber value="${countRoom - houseWithArea}" pattern="#,#00"/>)'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        });
        echarts.init(document.getElementById('main4')).setOption({
            title : {
                text: '有面积房屋锁定率',
                subtext: '有面积房屋总数<fmt:formatNumber value="${houseWithArea}" pattern="#,#00"/>',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['已锁定','未锁定']
            },
            series : [
                {
                    name: '占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:${countRoomLocked}, name:'已锁定(<fmt:formatNumber value="${countRoomLocked}" pattern="#,#00"/>)'},
                        {value:${houseWithArea - countRoomLocked}, name:'未锁定(<fmt:formatNumber value="${houseWithArea - countRoomLocked}" pattern="#,#00"/>)'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        });
        /* echarts.init(document.getElementById('main5')).setOption({
            title : {
                text: '地址解析准确度',
                subtext: '参与统计的地址数量<fmt:formatNumber value="${parseConfirmedHouseCount + parseUnconfirmedHouseCount}" pattern="#,#00"/>',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['可信度高','可信度低']
            },
            series : [
                {
                    name: '占比',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:${parseConfirmedHouseCount}, name:'可信度高(<fmt:formatNumber value="${parseConfirmedHouseCount}" pattern="#,#00"/>)'},
                        {value:${parseUnconfirmedHouseCount}, name:'可信度低(<fmt:formatNumber value="${parseUnconfirmedHouseCount}" pattern="#,#00"/>)'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        }); */
    </script>

</div>

</body>
</html>
