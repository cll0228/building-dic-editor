<%--@elvariable id="ctx" type="java" value="/"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>地址解析</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="${ctx}/static/css/signin.css" rel="stylesheet"/>
    <!-- Le styles -->
    <link href="${ctx}/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">

    </style>
    <link href="${ctx}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx}/static/scripts/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.js"></script>

    <script>
        function check() {
            if ($("#addressList").attr("disabled") == "disabled") {
                if ($("#addressFile").val() == '') {
                    alert("请上传文件");
                    return false;
                }
            } else {
                if ($("#addressList").val() == '') {
                    alert("请输入地址");
                    return false;
                }
            }
        }
    </script>
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/pages/include/header.jsp" flush="true" />

    <h1 style="font-size: 14px;">此页面提供测试功能，检验解析规则。提交数据后，以文件形式返回结果。<br/>
    解析正式数据并存数据库请提交后台任务，并在<a href="${ctx}/result.do" target="_blank">任务解析结果</a>页面查看。
    </h1>
    <div style="width: 1px;height: 30px;"></div>
    <p>
        输入地址或上传地址文件
    </p>
    <form action="${ctx}/parse.do" method="post" enctype="multipart/form-data" target="_blank">
        <label><input type="radio" value="1" name="inputtype" id="inputtype1">输入地址，每行一个地址，最多输入1000条数据</label>
        <label>
            <textarea disabled class="form-control" name="addressList" id="addressList" cols="200" rows="10"
                      style="width: 100%"></textarea>
        </label>

        <label><input type="radio" value="2" checked name="inputtype" id="inputtype2">上传文件，必须使用utf-8编码，不可超过10M，大文件请执行后台任务</label>
        <input type="file" name="addressFile" id="addressFile"/>

        <div style="text-align: center">
            <button class="btn btn-success" type="submit" onclick="return check()">解析地址</button>
        </div>
    </form>


</div>


<script type="text/javascript">
    $(function () {
        $("#inputtype1").click(function () {
            $("#addressList").removeAttr("disabled");
            $("#addressFile").attr("disabled","disabled");
        });
        $("#inputtype2").click(function () {
            $("#addressList").attr("disabled","disabled");
            $("#addressFile").removeAttr("disabled");
        });
    })
</script>
</body>
</html>