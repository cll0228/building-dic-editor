<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>任务解析结果</title>
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
        var keywords = '${keywords}';
        var building = '${building}';
        var room = '${room}';
        var ctx = '${ctx}';
        var cur_page = parseInt('${cur_page}');
        function check() {
            if ($("#keywords").val() == '') {
                alert("请输入合适的关键字");
                return false;
            }
        }
        function toPage(n) {
            if (keywords == '') {
                alert("没有关键字");
                return false;
            }
            $("#keywords").val(keywords);
            $("#building").val(building);
            $("#room").val(room);
            $("#page").val(n);
            $("#form1").submit();
        }

        $(function () {
        });
    </script>
</head>

<body>

<div class="container">
    <jsp:include page="/WEB-INF/pages/include/header.jsp" flush="true"/>

    <h1 style="font-size: 14px;">
        输入关键字时避免只输数字、分词等字符，多个关键字可以使用逗号分开。
    </h1>
    <div style="width: 1px;height: 30px;"></div>
    <p>

    <form id="form1" action="${ctx}/result.do" method="post">

        <label style="display: inline"><input type="radio" value="1" checked/>关键字匹配系统小区(速度快)</label>
        <label style="display: inline"><input type="radio" value="1" disabled/>关键字匹配原始地址</label>
        <div style="width: 1px;height: 5px;"></div>
        <label style="display: inline">关键字</label>

        <input type="text" name="keywords" style="width:360px" id="keywords" value="${keywords}"/>
        楼栋<input type="text" name="building" id="building" value="${building}" style="width:50px" size="10" max="10"/>
        室号<input type="text" name="room" id="room" style="width:50px" value="${room}"/>
        <input type="submit" class="btn btn-success" onclick="return check();" value="开始查找"/>
        &nbsp;&nbsp;
        <input type="hidden" name="page" id="page" value="${cur_page}"/>
        <a href="#" onclick="alert('受限于时间，暂不支持导出');return false">导出全部结果</a>&nbsp;&nbsp;
    </form>

    <div>
        <c:if test="${not (empty keywords)}">
            符合查询条件且被成功解析的地址集&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="alert('此功能未开放');return false">查看匹配关键字但未解析成功的记录</a>
        </c:if>
    </div>
    <div style="margin-top: 6px; margin-bottom: 6px;">
        <c:if test="${totalPage > 1}">
            总记录数:${count}&nbsp;&nbsp;页码: <a href="javascript:toPage(cur_page - 1);">下一页</a> | <a
                href="javascript:toPage(cur_page + 1);">下一页</a>
            <c:forEach begin="1" end="${totalPage}" step="1" var="n" varStatus="s">
                <c:if test="${cur_page eq n}">
                    &nbsp;${n}&nbsp;
                </c:if>
                <c:if test="${not (cur_page eq n)}">
                    <a href="javascript:toPage(${n});">&nbsp;${n}&nbsp;</a>
                </c:if>
                <c:if test="${not s.last}">
                    |
                </c:if>
            </c:forEach>
        </c:if>
    </div>
    <c:if test="${empty keywords}">
        <div style="color: #57a957">
        欢迎使用</div>
    </c:if>
    <c:if test="${not empty keywords}">
        <div style="border: 1px solid #1f6377;margin-top: 6px;margin-bottom: 6px; background-color: #e3e3e3">
            <span style="background-color: #2f96b4; color: white">当前页匹配关键字的全部小区:</span>
            <c:forEach items="${residences}" var="r" varStatus="s">
                <c:if test="${r.exists}">
                    <span>${r.name}</span>
                </c:if>
                <c:if test="${not r.exists}">
                    <span style="color: grey">${r.name}</span>
                </c:if>
                <c:if test="${not s.last}">
                    |
                </c:if>
            </c:forEach>
        </div>
        <table class="table" border="1" cellpadding="1" cellspacing="1" width="100%">
            <tr style="background-color: #4bb1cf">
                <td>序号</td>
                <td>ID</td>
                <td>引用ID</td>
                <td>原始地址</td>
                <td>小区</td>
                <td>楼栋</td>
                <td>室号</td>
                <td>面积</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${list}" var="i" varStatus="s">
                <tr>
                    <td>${s.count}</td>
                    <td>${i.id}</td>
                    <td>${i.refId}</td>
                    <td>${i.oriAddress}</td>
                    <td>${i.residence}</td>
                    <td>${i.building}</td>
                    <td>${i.room}</td>
                    <td>${i.area}</td>
                    <td style="text-align: center"><a target="_blank"
                                                      href="http://map.baidu.com/?newmap=1&ie=utf-8&s=s%26wd%3D${i.residence}">GoTo百度地图</a>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty list}">
                <tr>
                    <td colspan="10">
                        没有符合条件的数据
                    </td>
                </tr>
            </c:if>
        </table>
    </c:if>
    <div>
        <c:if test="${totalPage > 1}">
            页码:
            <c:forEach begin="1" end="${totalPage}" step="1" var="n" varStatus="s">
                <c:if test="${cur_page eq n}">
                    &nbsp;${n}&nbsp;
                </c:if>
                <c:if test="${not (cur_page eq n)}">
                    <a href="javascript:toPage(${n});">&nbsp;${n}&nbsp;</a>
                </c:if>
                <c:if test="${not s.last}">
                    |
                </c:if>
            </c:forEach>
        </c:if>
    </div>
    <div style="width: 1px;height: 50px;">
    </div>
</div> <!-- /container -->

</body>
</html>
