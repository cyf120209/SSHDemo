<%--
  Created by IntelliJ IDEA.
  User: padmate
  Date: 2017/11/13
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<base href="<%=basePath%>">
<title>管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- jquery --%>
<script type="text/javascript" src="js/common/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="js/common/jquery.jsonp-2.4.0.min.js"></script>
<%-- bootstrap --%>
<link rel="stylesheet" href="static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="static/bootstrap/css/bootstrap-theme.min.css">
<link href="static/css/font-awesome.min.css" rel="stylesheet">
<link href="static/css/templatemo-style.css" rel="stylesheet">
<script src="static/bootstrap/js/bootstrap.min.js"></script>
<script src="static/bootstrap/bootstrap-common.js"></script>

<link rel="stylesheet" href="static/bootstrap3-dialog/css/bootstrap-dialog.min.css">
<script src="static/bootstrap3-dialog/js/bootstrap-dialog.min.js"></script>
