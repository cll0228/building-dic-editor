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
        function approve(id, tid) {
            $.get("${ctx}/approve.do", {"id":id}, function (data) {
                if (data != undefined && data.status == 'success') {
                    layer.msg('房屋已锁定！', {icon: 1});
                    $("."+tid).find(".btn-group").css("display","none");
                    $("."+tid).find(".btn-group").before("<span title='房屋已经锁定' class='glyphicon glyphicon glyphicon-lock' aria-hidden='true'></span>");
                    $("#"+id+"_c").css("color","");
                } else {
                    layer.msg('锁定失败');
                }
            });
        }

        function editArea(id, newArea, liclass) {
            layer.prompt({
                title: '输入新的面积，并确认',
                formType: 0, //prompt风格，支持0-2,
                value: newArea
            }, function(val){
            	$.post("${ctx}/newArea.do", {"id" : id, "newArea": val}, function (data) {
            		if(data.status=="success") {
            			layer.msg('修改成功', {icon: 1});
            			$("."+liclass)[0].innerText = val;
            		} else {
            			layer.msg('修改失败', {icon: 2});
            		}
					
                });
                
            }, function(){
                layer.msg('放弃了。', {icon: 1});
            });
        }

        function addBuilding(rid, no,title) {
            layer.prompt({
                title: title ? title : '输入楼栋号，并确认',
                formType: 0, //prompt风格，支持0-2,
                value: no ? no : ''
            }, function(){
                layer.msg('OK..。', {icon: 1});
                $.post("${ctx}/addBuilding.do", {"rid" : rid, "no": no}, function (data) {

                });
            }, function(){
                //layer.msg('放弃了。', {icon: 1});
            });
        }

        function editTotalFloor(bid, oldTotalFloor) {
            var plusid = bid+(oldTotalFloor+1);
            var data = "<tr><td id="+'"nf_'+bid+'"'+ ' class="unreal"><span style="color: #e9322d;">12</span><a class="plus" class="'+plusid+'"' +' style="font-size: large" href="#" title="在此楼层添加房屋">+</a></td></tr>';
            layer.prompt({
                title: '输入新的总层高，并确认',
                formType: 0, //prompt风格，支持0-2,
                value: '7'
            }, function(text){
//                $(".table1 tbody").find("#_s"+id).before(data);
                $("#s_"+bid).before(data);
                $("#nf_"+bid).attr('colspan',100);
                $("#plusid").click(function(){
                    addNewRoom(bid);
                })
                layer.msg('总层高已经更新。', {icon: 1});
            }, function(){
                layer.msg('放弃了。', {icon: 1});
            });
        }
        function delRoom(rid) {
            var td_id = "#"+rid;
            layer.confirm('删除此房间？', {
                btn: ['删除','取消'] //按钮
            }, function(){
                $.post("${ctx}/deleteRoom.do", {"rid" : rid}, function (data) {
                    if (data != undefined && data.status == 'success') {
                        layer.msg('房屋已删除！', {icon: 1});
                        $("#"+rid+"_t").find(".btn-group").css("display","none");
                        $("#"+rid+"_t").find(".cellBottom").css("display","none");
                        $("#"+rid+"_t").find(".plus").css("display","");
                        $("#"+rid+"_c").css("color","red");
                        $("#"+rid+"_t").unbind("dblclick")
                    } else {
                        layer.msg('删除失败！');
                    }
                });
            }, function(){
                layer.msg('放弃了。', {icon: 1});
            });
        }
        function delBuilding(buildingId) {
            layer.confirm('删除此楼？', {
                btn: ['删除','取消'] //按钮
            }, function(){
            	$.post("${ctx}/deleteBuilding.do", {"buildingId" : buildingId}, function (data) {
                    if (data != undefined && data.status == 'success') {
                        layer.msg('楼栋已删除！', {icon: 1});
                        $("#b_"+buildingId).css("display","none");
                    } else {
                        layer.msg('删除失败！', {icon: 2});
                    }
                });
            }, function(){
                layer.msg('放弃了。', {icon: 1});
            });
        }
        function addRoom(bid,rname) {

//            $(function(){
//
//                $(window).scroll(function(e){window.scrollTo(0,0);});
//
//            });
            var area_class = "." + bid + "_" + rname + "_3";
            var span_class = "." + bid + "_" + rname + "_1";
            var td_class = "." + bid + "_" + rname;
            var plus_id = "#" + bid + "_" + rname + "_2";
            var a1 = "." + bid + "_" + rname + "_01";
            var a2 = "." + bid + "_" + rname + "_02";
            var a3 = "." + bid + "_" + rname + "_03";
//            window.scrollTo(10,0);
            layer.prompt({
                title: '输入房间'+rname+'室的面积，并确认',
                formType: 0 //prompt风格，支持0-2,
//                scrollbar: false
            }, function(text){
                $.post("${ctx}/addRoom.do", {"bid" : bid, "rname": rname,"rarea":text}, function (data) {

                    if (data != undefined && data.status == 'success') {
                        $(area_class).attr('id',data.rid+"_a");
                        layer.msg('房屋已添加！', {icon: 1});
                        $(area_class).html(text);
                        $(span_class).css("color","#57a957");
                        $(plus_id).css("display","none");
                        $(td_class).find(".btn-group").css("display","");
                        $(td_class).find(".cellBottom").css("display","");
                        $(td_class).attr('id',data.rid+"_t");
                        $(span_class).attr('id',data.rid+"_c");
                        $(a1).click(function(){
                            editArea(data.rid,$(area_class)[0].innerText,bid + "_" + rname + "_3");
                        })
                        $(a2).click(function(){
                            delRoom(data.rid);
                        })
                        $(a3).click(function(){
                            approve(data.rid, bid+"_"+rname);
                        })
                        $(td_class).dblclick(function(){
                            viewDetail(data.rid);
                        })
                    } else {
                        layer.msg('添加失败！');
                    }
                });
            }, function(){
                layer.msg('放弃了。', {icon: 1});
            });
        }

        function addNewRoom(bid) {
            var data = '<div style="padding:20px;"><table><tr><td> 请输入房间号：</td><td><input id="rm" type="text" /></td></tr><tr><td>请输入面积：</td><td><input id="ra" type="text"/></td></tr></table></div>';
            //页面层
            layer.open({
                type: 1,
                area: ['600px', '300px'],
                shadeClose: true,
                content: data,
                btn: ['确定', '关闭'],
                yes:function(index){
                    $.post("${ctx}/addNewRoom.do", {"bid" : bid, "rname": $('#rm').val(),"rarea":$('#ra').val()}, function (data) {

                        if (data != undefined) {
                            if(data.status === 'exists'){
                                layer.msg('该房屋已存在！', {icon: 1});
                                layer.close();
                            }
                            if(data.status == 'success'){
                                layer.msg('房屋已添加！', {icon: 1});
                                window.location.reload();//刷新当前页面.
                            }
                        } else {
                            layer.msg('添加失败！');
                        }
                    })
                },
                cancel:function(index){
                    layer.close();
                }
            });
        }
        function viewDetail(id,oriAddress,src) {
            layer.alert('当前房屋的ID是' + id + '<br/>原始地址是:'+oriAddress+'<br/>数据来源:'+src, {icon: 1, title: "房屋详情"});
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

    <div class="tips1" style="text-align: center; padding: 10px;">
        <div style="width: 30px;height: 15px;background-color: red;float: left;margin-top: 2px;"></div>
        <div style="width: 300px;height: 15px;float: left;text-align: left;padding-top: 0; padding-left: 10px">房屋不存在</div>
        <div style="clear: both;width: 1px;height: 6px;"></div>
        <div style="width: 30px;height: 15px;background-color: green;float: left;;margin-top: 2px;"></div>
        <div style="width: 300px;height: 15px;float: left;text-align: left; padding-left: 10px">从地址库中解析到的房屋</div>
        <div style="clear: both;width: 1px;height: 6px;"></div>
        <div style="width: 30px;height: 15px;background-color: #985f0d;float: left;;margin-top: 2px;"></div>
        <div style="width: 300px;height: 15px;float: left;text-align: left; padding-left:10px">机器自动补全的房屋</div>
        <div style="clear: both;width: 1px;height: 6px;"></div>
        <div style="width: 30px;height: 15px;background-color: #2f96b4;float: left;;margin-top: 2px;"></div>
        <div style="width: 300px;height: 15px;float: left;text-align: left; padding-left: 10px">人工补全的房屋</div>
        <div style="clear: both;width: 1px;height: 6px;"></div>
        <div style="width: 30px;height: 15px;background-color: #000;float: left;margin-top: 2px;;"></div>
        <div style="width: 300px;height: 15px;float: left;text-align: left; padding-left: 10px">已经锁定的的房屋</div>
        <div style="clear: both;width: 1px;height: 6px;"></div>
        <br/>
        <span>开发数据，随意编辑</span>
    </div>
    </c:if>
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
        <p><span>楼栋位图</span>&nbsp;&nbsp;<input onclick="addBuilding(${r.residenceId});" type="button" class="btn btn-sm" value="添加楼栋"> </p>
        <div style="clear: both">
            <c:forEach items="${r.buildingBitMaps}" var="be" varStatus="s">
                <div class="building-map" style="color: ${be.exists ? 'green' : 'red'};">
                    <c:if test="${not be.exists}">
                        <a  class="addbuilding" href="#" onclick="addBuilding(${r.residenceId}, '${be.buildingName}','添加此楼');return false">${be.buildingName}</a>
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
                                <span style="text-indent: 4px" class="glyphicon glyphicon glyphicon-th" aria-hidden="true">${b.name}号&nbsp;${b.totalFloor}F</span>
                            </div>
                            <div class="cellRight">
                                <a class="plus" href="#" onclick="layer.alert('以列表的形式，详细地显示此楼栋的全部信息，一目了然。');return false">数据列表</a>
                                <div class="btn-group">
                                    <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <span class="glyphicon glyphicon glyphicon-wrench"
                                              aria-hidden="true"></span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a href="#" onclick="layer.alert('开发中');return false;">自动补全所有</a></li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#" onclick="layer.alert('开发中');return false;">全部锁定</a></li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#" onclick="editTotalFloor(${b.id},'9');return false;">修改总层高</a></li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#" onclick="delBuilding(${b.id});return false;">删除此楼</a></li>
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
                                    <td ondblclick="viewDetail(${r.id},'${r.oriAddress}','${r.src}');" class="${b.id}_${r.name}" id="${r.id}_t">
                                        <c:choose>
                                            <c:when test="${r.status eq 10 or r.status eq 20 or r.status eq 30}">
                                                <div class="cell">
                                                    <div class="cellTop" style="height: 60%;">
                                                        <c:choose>
                                                            <c:when test="${r.status eq 10}">
                                                                <c:set var="clr1" value="#57a957" />
                                                            </c:when>
                                                            <c:when test="${r.status eq 20}">
                                                                <c:set var="clr1" value="#985f0d" />
                                                            </c:when>
                                                            <c:when test="${r.status eq 30}">
                                                                <c:set var="clr1" value="#2f96b4" />
                                                            </c:when>
                                                        </c:choose>

                                                        <span style="color: ${clr1}; " class="${b.id}_${r.name}_1" id="${r.id}_c">${r.name}</span>
                                                        <a class="plus" id="${b.id}_${r.name}_2" style="font-size: large;display: none;" href="#" onclick="addRoom(${b.id},${r.name});" title="添加此房屋">+</a>
                                                        <div class="btn-group">
                                                            <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                                                    data-toggle="dropdown" aria-haspopup="true"
                                                                    aria-expanded="false">
                                                        <span class="glyphicon glyphicon glyphicon-pencil"
                                                              aria-hidden="true"></span>
                                                            </button>
                                                            <ul class="dropdown-menu">
                                                                <li><a href="#" class="${b.id}_${r.name}_01" onclick="editArea(${r.id},$('.${b.id}_${r.name}_3')[0].innerText,'${b.id}_${r.name}_3');return false;">修改面积</a>
                                                                </li>
                                                                <li><a href="#" class="${b.id}_${r.name}_02" onclick="delRoom(${r.id});return false;">删除</a>
                                                                </li>
                                                                <li role="separator" class="divider"></li>
                                                                <li><a href="#" class="${b.id}_${r.name}_03" onclick="approve(${r.id},'${b.id}_${r.name}');return false;">锁定</a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="cellBottom" style="height: 40%;">
                                                        <span style="color: #1b1b1b;font-size: 8px"><i class="${b.id}_${r.name}_3" id="${r.id}_a">${r.area}</i></span>
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
                                                        <span style="color: #1b1b1b;font-size: 8px"><i class="${b.id}_${r.name}_3" id="${r.id}">${r.area}</i></span>
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
                                    <td class="${b.id}_${r.name}">
                                        <div class="cell">
                                            <div class="cellTop" style="height: 60%;">
                                                <span class="${b.id}_${r.name}_1" style="color: red; ">${r.name}</span>
                                                <a class="plus" id="${b.id}_${r.name}_2" style="font-size: large" href="#" onclick="addRoom(${b.id},${r.name});" title="添加此房屋">+</a>

                                                <div class="btn-group" style="display:none;">
                                                    <button class="btn btn-default btn-xs dropdown-toggle" type="button"
                                                            data-toggle="dropdown" aria-haspopup="true"
                                                            aria-expanded="false">
                                                        <span class="glyphicon glyphicon glyphicon-pencil"
                                                              aria-hidden="true"></span>
                                                    </button>
                                                    <ul class="dropdown-menu">
                                                        <li><a href="#" class="${b.id}_${r.name}_01">修改面积</a>
                                                        </li>
                                                        <li><a href="#" class="${b.id}_${r.name}_02">删除</a>
                                                        </li>
                                                        <li role="separator" class="divider"></li>
                                                        <li><a href="#" class="${b.id}_${r.name}_03">锁定</a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="cellBottom" style="height: 40%;">
                                                <span style="color: #1b1b1b;font-size: 8px"><i class="${b.id}_${r.name}_3"></i></span>
                                            </div>
                                        </div>
                                    </td>
                                </c:if>
                            </c:forEach>
                            <td style="text-align: center;font-size: 30px; width: 40px;min-width: 40px;">
                                <a class="plus" href="#" onclick="addNewRoom(${b.id});" title="在此楼层添加房屋">+</a>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${not f.real}">
                        <tr>
                            <td colspan="100" class="unreal">
                                <span style="color: #e9322d;">${f.name}</span>
                                <a class="plus" style="font-size: large" href="#" onclick="addNewRoom(${b.id});" title="在此楼层添加房屋">+</a>
                                <!--
                                <a class="plus" href="#" onclick="addRoom();return false;">在此楼层创建房屋</a>-->
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
                <tr id="s_${b.id}">
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
