
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

<!-- Left column -->
<div class="templatemo-flex-row">
    <jsp:include page="../sidebar.jsp"></jsp:include>
    <!-- Main content -->
    <div class="templatemo-content col-1 light-gray-bg">
        <div class="templatemo-content-container">
            <div class="templatemo-content-widget no-padding">
                <div class="panel panel-default table-responsive">
                    <!-- 数据列表 -->
                    <table class="table table-striped table-bordered table-hover templatemo-user-table">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>deviceId</th>
                            <th>groupId</th>
                            <th>groupName</th>
                        </tr>
                        </thead>
                        <c:forEach items="${groupList}" var="item" varStatus="row">
                            <tbody>
                            <tr>
                                <td width="50">${row.count}</td>
                                <td >${item.deviceId}</td>
                                <td >${item.groupId}</td>
                                <td >${item.groupName}</td>
                            </tr>
                            </tbody>
                        </c:forEach>
                    </table>
                </div>
            </div>

            <div class="pagination-wrap" id="page-div-nav">
                <div class="page-info" id="page-info-area">
                    <%--当前第${pageInfo.pageNum}页，总共${pageInfo.pages}页，总共${pageInfo.total}记录--%>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
