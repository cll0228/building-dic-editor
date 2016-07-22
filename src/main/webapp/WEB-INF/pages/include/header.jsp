<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt " %>

<script type="text/javascript" src="${ctx}/static/scripts/jquery.js"></script>
<div style="text-align: right">

    Menu: <a href="${ctx}/"><b>地址解析工具</b></a>&nbsp;|&nbsp;
    <a href="${ctx}/result.do"><b>任务解析结果</b></a>&nbsp;|&nbsp;
    <a href="${ctx}/roomMap.do"><b>小区平面图</b></a>&nbsp;|&nbsp;
    <a href="${ctx}/statistics.do"><b>楼盘字典概况</b></a>

    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    你好,${username} <a href="${ctx}/logout.do">退出</a>
</div>
