<%--
  Created by IntelliJ IDEA.
  User: 文辉
  Date: 2017/7/19
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="templatemo-sidebar">
    <header class="templatemo-site-header">
        <div class="square"></div>
        <h1>Draper</h1>
    </header>
    <%--<div class="mobile-menu-icon">--%>
        <%--<i class="fa fa-bars"></i>--%>
    <%--</div>--%>
    <nav class="templatemo-left-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/v1/shades"></i>Shades</a></li>
            <li><a href="${pageContext.request.contextPath}/v1/groups"></i>Groups</a></li>
            <li><a href="${pageContext.request.contextPath}/v1/groups">Log</a></li>
            <%--<li><a href="${pageContext.request.contextPath}/admin/activity/show"><i class="fa fa-database fa-fw"></i>活动管理</a></li>--%>
        <%--<li><a href="${pageContext.request.contextPath}/admin/goods/show"><i class="fa fa-map-marker fa-fw"></i>活动管理</a></li>--%>
            <%--<li><a href="${pageContext.request.contextPath}/admin/chat"><i class="fa fa-sliders fa-fw"></i>客服管理</a></li>--%>
            <%--<li><a href="${pageContext.request.contextPath}/admin/logout"><i class="fa fa-eject fa-fw"></i>退出系统</a></li>--%>
        </ul>
    </nav>
</div>
