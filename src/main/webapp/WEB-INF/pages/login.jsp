<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>欢迎登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="${ctx}/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #f5f5f5;
        }

        .form-signin {
            max-width: 300px;
            padding: 19px 29px 29px;
            margin: 0 auto 20px;
            background-color: #fff;
            border: 1px solid #e5e5e5;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            box-shadow: 0 1px 2px rgba(0,0,0,.05);
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }

    </style>
    <link href="${ctx}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

    <script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.js"></script>
</head>

<body>

<div class="container">
<h1>请直接点击登录</h1>
    <c:set var="goToSomeUrl" value="${empty param['goTo'] ? goTo : param['goTo']}" />
    <form class="form-signin" role="form" action="${ctx}/login.do" method="post" style="text-align: center">
        <h2 class="form-signin-heading">登录系统</h2>
        <input type="text" name="username" readonly class="form-control" placeholder="User Name" value="success" required autofocus>
        <input type="password" readonly name="password" class="form-control" value="success" placeholder="Password" required>
        <input type="hidden" name="goTo" value="${goToSomeUrl}" />
        <button class="btn btn-primary" type="submit" style="width: 100px">Sign in</button>
    </form>

</div> <!-- /container -->

</body>
</html>
