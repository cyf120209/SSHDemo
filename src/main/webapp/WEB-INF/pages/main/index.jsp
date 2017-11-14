
<%--
  Created by IntelliJ IDEA.
  User: padmate
  Date: 2017/11/13
  Time: 13:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/static/include/head.jsp"%>
</head>
<body>
    <!-- 数据列表 -->
    <table class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
            <th>num</th>
            <%  // 列表头部 %>
            <th>deviceId</th>
            <th>deviceName</th>
            <th>mac</th>
            <th>modelName</th>
            <th>version</th>
            <th>remarks</th>
        </tr>
        </thead>
        <c:forEach items="${deviceList}" var="item" varStatus="row">
            <tbody>
                <tr>
                    <td width="50">${row.count}</td>
                    <td >${item.deviceId}</td>
                    <td >${item.deviceName}</td>
                    <td >${item.mac}</td>
                    <td >${item.modelName}</td>
                    <td >${item.version}</td>
                    <td >${item.remarks}</td>
                </tr>
            </tbody>
        </c:forEach>
    </table>
</body>
</html>
