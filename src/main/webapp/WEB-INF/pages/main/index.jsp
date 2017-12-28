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
    <%@include file="/static/include/head.jsp" %>
</head>
<body>
<script type="text/javascript">
//    function jump(page) {
//        $.ajax({
//            url: "http://192.168.20.17:8080/ApiTest/shades?pageSize=10&pageStartIndex="+page,
////            url: "http://192.168.20.63:9090/shades?pageSize=10&pageStartIndex="+page,
////            url:'/v1/device/device',
//            type: "get",
//            dataType: "json",
//            success: function (data) {
//                $("#data_table").empty();
////                $("#data_table").append("<tr> hello</tr>");
////                for (var i;i<3;i++){
////                    $("#data_table").append("<tr> hello</tr>");
////                }
//                for( var i = 0; i < data.length; i++ ) {
//                    //动态创建一个tr行标签,并且转换成jQuery对象
//                    var $trTemp = $("<tr></tr>");
//
//                    //往行里面追加 td单元格
//                    $trTemp.append("<td>"+ (i+1) +"</td>");
//                    $trTemp.append("<td>"+ data[i].shadeId +"</td>");
//                    $trTemp.append("<td>"+ data[i].shadeName +"</td>");
//                    $trTemp.append("<td>"+ data[i].shadePosition +"</td>");
//                    $trTemp.append("<td>"+ data[i].shadePriority +"</td>");
//                    $trTemp.append("<td>"+ data[i].shadeStatus +"</td>");
//                    // $("#J_TbData").append($trTemp);
//                    $trTemp.appendTo("#data_table");
//                }
//
//            },
//            error: function (result) {
//                window.alert("error" + page);
//            }
//        });
//    }

</script>
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
                            <th>num</th>
                            <th>shadeId</th>
                            <th>shadeName</th>
                            <th>shadePosition</th>
                            <th>shadePriority</th>
                            <th>shadeStatus</th>
                            <%--<th>deviceId</th>--%>
                            <%--<th>deviceName</th>--%>
                            <%--<th>mac</th>--%>
                            <%--<th>modelName</th>--%>
                            <%--<th>version</th>--%>
                            <%--<th>remarks</th>--%>
                        </tr>
                        </thead>
                        <tbody id="data_table">
                        <c:forEach items="${shadeList}" var="item" varStatus="row">
                            <tr>
                                <td width="50">${row.count}</td>
                                <td>${item.shadeId}</td>
                                <td>${item.shadeName}</td>
                                <td>${item.shadePosition}</td>
                                <td>${item.shadePriority}</td>
                                <td>${item.shadeStatus}</td>
                                <td>${item.remarks}</td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>

            <div class="pagination-wrap" id="page-div-nav">
                <ul class="pagination">
                    <li class="disabled">
                        <a aria-label="Next" href="${pageContext.request.contextPath}/v1/shades?pageStartIndex=1">
                            <span aria-hidden="true" >首页</span>
                        </a>
                    </li>
                    <c:if test="${pageInfo.hasPreviousPage}">
                        <li>
                            <a href="${pageContext.request.contextPath}/v1/shades?pageStartIndex=${pageInfo.pageNum - 1}"
                               aria-label="Previous">
                                <span aria-hidden="true"><i class="fa fa-backward"></i></span>
                            </a>
                        </li>
                    </c:if>
                    <c:forEach items="${pageInfo.navigatepageNums}" var="pageNums">
                        <c:if test="${pageNums == pageInfo.pageNum}">
                            <li class="active"><a
                                    href="${pageContext.request.contextPath}/v1/shades?pageStartIndex=${pageNums}">${pageNums}</a>
                            </li>
                        </c:if>
                        <c:if test="${pageNums != pageInfo.pageNum}">
                            <li>
                                <a href="${pageContext.request.contextPath}/v1/shades?pageStartIndex=${pageNums}">${pageNums}</a>
                            </li>
                        </c:if>
                    </c:forEach>
                    <c:if test="${pageInfo.hasNextPage}">
                        <li class="disabled">
                            <a href="${pageContext.request.contextPath}/v1/shades?pageStartIndex=${pageInfo.pageNum + 1}"
                               aria-label="Next">
                                <span aria-hidden="true"><i class="fa fa-forward"></i></span>
                            </a>
                        </li>
                    </c:if>

                    <li>
                        <a href="${pageContext.request.contextPath}/v1/shades?pageStartIndex=${pageInfo.pages}"
                           aria-label="Next">
                            <span aria-hidden="true">末页</span>
                        </a>
                    </li>

                </ul>
                <div class="page-info" id="page-info-area">
                    当前第${pageInfo.pageNum}页，总共${pageInfo.pages}页，总共${pageInfo.total}记录
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
