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

    <link href="${ctx}/layer/skin/layer.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/layer/layer.js"></script>
    <script>
        function refresh() {
            $("#theone").attr("src", "${ctx}/statistics/data.do?refresh=true");
            $("#theone").hide();
            layer.load();
            return false;
        }

        $(function(){
            //加载层-默认风格
            layer.load();

            $("#theone").load(function(){
                layer.closeAll('loading');
                $("#theone").show();
            });
        });
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

    <iframe id="theone" frameborder="0" src="${ctx}/statistics/data.do" scrolling="no" width="100%" height="1800"></iframe>

</div> <!-- /container -->

</body>
</html>
