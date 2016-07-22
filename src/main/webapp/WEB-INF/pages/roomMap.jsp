<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>小区平面图</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="${ctx}/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">

    </style>
    <link href="${ctx}/static/css/roomMap.css" rel="stylesheet" />
    <link href="${ctx}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/static/scripts/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.js"></script>

    <link href="${ctx}/layer/skin/layer.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/layer/layer.js"></script>

    <script type="text/javascript">
        function approve(id) {
            $.get("${ctx}/approve.do", {"id":id}, function (data) {
                if (data != undefined && data.status == 'success') {
                    layer.msg('操作成功', {icon: 1});
                } else {
                    layer.msg('操作失败');
                }
            });
        }

        function editArea(id, oldArea) {
            layer.prompt({
                title: '输入新的面积，并确认',
                formType: 0, //prompt风格，支持0-2,
                value: oldArea
            }, function(pass){
                layer.prompt({title: '随便写点啥，并确认', formType: 2}, function(text){
                    layer.msg('演示完毕！您的口令：'+ pass +' 您最后写下了：'+ text);
                });
            });
        }

        function delRoom() {
            layer.confirm('删除此房间？', {
                btn: ['删除','取消'] //按钮
            }, function(){
                layer.msg('删除了。', {icon: 1});
            }, function(){
                layer.msg('放弃了。', {icon: 1});
            });
        }

        function addRoom() {
            //页面层
            layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['420px', '240px'], //宽高
                content: 'html内容'
            });
        }
    </script>
</head>

<body>
<div class="container">
    <jsp:include page="/WEB-INF/pages/include/header.jsp" flush="true"/>

    <h1>输入关键字时避免只输数字、分词等字符，多个关键字可以使用逗号分开。 </h1>
    <form id="form1" action="${ctx}/roomMap.do" method="post">
        <label style="display: inline"><input type="radio" value="1" checked/>关键字匹配系统小区(速度快)</label>
        <label style="display: inline"><input type="radio" value="1" disabled/>关键字匹配原始地址</label>
        <div style="width: 1px;height: 5px;"></div>
        <label style="display: inline" for="keywords">关键字</label>
        <input type="text" name="keywords" style="width:360px" id="keywords" value="${keywords}"/>
        <input type="submit" class="btn btn-primary" onclick="return check();" value="开始查找"/>
        &nbsp;&nbsp;
    </form>
    <div class="block10"></div>
    <c:if test="${not empty residences}">
        <div class="residenceList">
            <span class="simpleTitle">匹配关键字的全部小区:</span>
            <c:forEach items="${residences}" var="r" varStatus="s">
                <c:if test="${r.exists}">
                    <span><a href="#rid_${r.id}">${r.name}</a></span>
                </c:if>
                <c:if test="${not r.exists}">
                    <span style="color: grey">${r.name}</span>
                </c:if>
                <c:if test="${not s.last}">
                    |
                </c:if>
            </c:forEach>
        </div>
    </c:if>
    <div class="tips1" style="display: none">
        <span style="color:red;">红色室号表示房屋不存在</span> <br/>
        <span style="color:green;">绿色室号是从地址库中解析到的房屋</span> <br/>
        <span style="color:#985f0d;">暗黄色室号是机器自动补全的房屋</span> <br/>
        <span style="color:#2f96b4;">淡蓝色室号是人工补全的房屋</span> <br/>
        <span style="color:#000;">黑色室号是已经确认的的房屋</span> <br/>
    </div>
    <c:if test="${empty keywords}">
        <div style="text-align: center;padding-top: 180px;">
            <img src="${ctx}/static/img/viewmag-256.png">
        </div>
    </c:if>

    <c:if test="${not empty keywords and empty residenceModels}">
        <div style="text-align: center;padding-top: 180px;">
            <!--    <img src="${ctx}/static/img/nothing.gif">-->
            <span>( 「 「 ) ~~~→ 什么都没找到，换个关键词试试吧，可以使用多个关键字用逗号分开。</span>
        </div>
    </c:if>

    <c:forEach items="${residenceModels}" var="r">
        <div class="myhr"></div>
        <p name="rid_${r.residenceId}" id="rid_${r.residenceId}"><span class="simpleTitle">小区:</span>:${r.name}&nbsp;
        </p>
        <p><span>楼栋位图</span>&nbsp;&nbsp;<input type="button" class="btn btn-sm" value="添加楼栋"> </p>
        <div style="clear: both">
            <c:forEach items="${r.buildingBitMaps}" var="be" varStatus="s">
                <div class="building-map" style="color: ${be.exists ? 'green' : 'red'};">
                    <c:if test="${not be.exists}">
                        <span onclick="javascript:alert('添加')">${s.index + r.minBuilding}</span>
                    </c:if>
                    <c:if test="${be.exists}">
                        <a href="#b_${be.buildingId}">${s.index + r.minBuilding}</a>
                    </c:if>
                </div>
            </c:forEach>
            <div style="clear:both;"></div>
        </div>
        <c:forEach items="${r.buildings}" var="b">
            <table class="table1" border="1" cellpadding="1" cellspacing="1" name="b_${b.id}" id="b_${b.id}">
                <thead>
                <tr>
                    <td colspan="100">
                        <div class="cell">
                            <div class="cellLeft" style="width: 60%;">
                                    ${b.name}号&nbsp;总楼层:${b.totalFloor}
                            </div>
                            <div class="cellRight">
                                <div class="btn-group">
                                    <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span class="glyphicon glyphicon glyphicon-pencil"
                                              aria-hidden="true">Edit</span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a href="#" onclick="alert('开发中');return false;">自动补全所有</a></li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#" onclick="alert('开发中');return false;">全部锁定</a></li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#" onclick="alert('开发中');return false;">修改总层高</a></li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#" onclick="alert('开发中');return false;">删除此楼</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${b.floors}" var="f">
                    <c:if test="${f.real}">
                        <tr>
                            <c:forEach items="${f.rooms}" var="r">
                                <c:if test="${r.real}">
                                    <td ondblclick="alert('当前房屋的ID是${r.id}');">
                                        <c:choose>
                                            <c:when test="${r.status == 10}">
                                                <div class="cell">
                                                    <div class="cellTop" style="height: 60%;">
                                                        <span style="color: #57a957; ">${r.name}</span>
                                                        <div class="btn-group">
                                                            <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                                                    data-toggle="dropdown" aria-haspopup="true"
                                                                    aria-expanded="false">
                                                        <span class="glyphicon glyphicon glyphicon-pencil"
                                                              aria-hidden="true">Edit</span>
                                                            </button>
                                                            <ul class="dropdown-menu">
                                                                <li><a href="#" onclick="editArea(${r.id},'${r.area}');return false;">修改面积</a>
                                                                </li>
                                                                    <li><a href="#" onclick="delRoom('开发中');return false;">删除</a>
                                                                    </li>
                                                                <li role="separator" class="divider"></li>
                                                                <li><a href="#" onclick="approve(${r.id});return false;">锁定</a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="cellBottom" style="height: 40%;">
                                                        <span style="color: #1b1b1b;font-size: 8px"><i>${r.area}</i></span>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${r.status == 20}">
                                                <div class="cell">
                                                    <div class="cellTop" style="height: 60%;">
                                                        <span style="color:#985f0d;">${r.name}</span>
                                                        <div class="btn-group">
                                                            <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                                                    data-toggle="dropdown" aria-haspopup="true"
                                                                    aria-expanded="false">
                                                        <span class="glyphicon glyphicon glyphicon-pencil"
                                                              aria-hidden="true">Edit</span>
                                                            </button>
                                                            <ul class="dropdown-menu">
                                                                <li><a href="#" onclick="editArea(${r.id},'${r.area}');return false;">修改面积</a>
                                                                </li>
                                                                <li role="separator" class="divider"></li>
                                                                <li><a href="#" onclick="approve(${r.id});return false;">锁定</a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="cellBottom" style="height: 40%;">
                                                        <span style="color: #1b1b1b;font-size: 8px"><i>${r.area}</i></span>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${r.status == 30}">
                                                <div class="cell">
                                                    <div class="cellTop" style="height: 60%;">
                                                        <span style="color:#2f96b4;">${r.name}</span>
                                                        <div class="btn-group">
                                                            <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                                                    data-toggle="dropdown" aria-haspopup="true"
                                                                    aria-expanded="false">
                                                        <span class="glyphicon glyphicon glyphicon-pencil"
                                                              aria-hidden="true">Edit</span>
                                                            </button>
                                                            <ul class="dropdown-menu">
                                                                <li><a href="#" onclick="editArea(${r.id},'${r.area}');return false;">修改面积</a>
                                                                </li>
                                                                <li role="separator" class="divider"></li>
                                                                <li><a href="#" onclick="approve(${r.id});return false;">锁定</a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="cellBottom" style="height: 40%;">
                                                        <span style="color: #1b1b1b;font-size: 8px"><i>${r.area}</i></span>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${r.status == 40}">
                                                <div class="cell">
                                                    <div class="cellTop" style="height: 60%;">
                                                        <span style="color:#000;">${r.name}</span>
                                                        <span title="房屋已经锁定" class="glyphicon glyphicon glyphicon-lock" aria-hidden="true"></span>
                                                    </div>
                                                    <div class="cellBottom" style="height: 40%;">
                                                        <span style="color: #1b1b1b;font-size: 8px"><i>${r.area}</i></span>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                [[!数据错误!]]
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>
                                <c:if test="${not r.real}">
                                    <td>
                                        <div class="cell">
                                            <div class="cellTop" style="height: 60%;">
                                                <span style="color: red; ">${r.name}</span>
                                                <button class="btn btn-default btn-xs dropdown-toggle" type="button" onclick="addRoom();">
                                                    <span class="glyphicon glyphicon glyphicon-plus" aria-hidden="true">New</span>
                                                </button>
                                            </div>
                                            <div class="cellBottom" style="height: 40%;">
                                                &nbsp;
                                            </div>
                                        </div>
                                    </td>
                                </c:if>
                            </c:forEach>
                            <td style="text-align: center;font-size: 30px; width: 40px;min-width: 40px;">
                                <a href="javascript:addRoom(0);" title="在此楼层添加房屋">+</a>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${not f.real}">
                        <tr>
                            <td colspan="100" class="unreal">
                                <span style="color: #e9322d;">${f.name}</span>
                                <a href="#" onclick="return false;">在此楼层创建房屋</a>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
                <tr>
                    <td colspan="100" class="suggestion">
                        建议:无
                            <%--
                            <s>建议:补全3楼 <a href="#">接受</a></s>
                            --%>
                    </td>
                </tr>
                </tbody>
            </table>
        </c:forEach>
    </c:forEach>
    <a class="backtop" title="返回顶部">
        <h6>返回顶部</h6>
    </a>
</div> <!-- /container -->

<script>
    $(window).scroll(function () {
        if ($(window).scrollTop() >= 100) {
            $(".backtop").fadeIn();
        } else {

            $(".backtop").fadeOut();
        }
    });

    $(".backtop").click(function (event) {
        $("html,body").animate({scrollTop: 0}, 500);
        return false;
    });
</script>

</body>
</html>
