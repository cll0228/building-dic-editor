<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>楼盘字典概况</title>
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


    <script>
        function refresh() {
            $.get("${ctx}/update.do");
            alert("请求已提交");
            return false;
        }
    </script>
    <style type="text/css">
        .simple-title {
            background-color: #2f96b4;
            color: white
        }

    </style>

</head>

<body>

<div class="container">
    <jsp:include page="/WEB-INF/pages/include/header.jsp" flush="true"/>

    <div style="text-align: left; color: #000000;margin-top: 100px;">
        楼栋数:${buildingCount}<br>
        房屋总数:${countRoomDeal + countRoomOthers}<br>
        交易记录获取房屋数:${countRoomDeal}<br>
        其它来源获取房屋数:${countRoomOthers}<br>
        已经锁定的房屋数:${countRoomLocked} (来源于交易记录，解析规则严谨)<br>
        <br>
    </div>

    <h1 style="font-size: 14px;">
        更多功能建设中...
    </h1>
</div> <!-- /container -->

</body>
</html>
