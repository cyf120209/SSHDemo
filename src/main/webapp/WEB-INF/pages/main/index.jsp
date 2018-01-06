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

    paginator=function(page) {
        oper_list();
    }

    function oper_list(page) {
        $("#pageNum").val(page);
        form1.action="/v1/shades";
        form1.submit();
    }

    /************************** form处理 *****************************/
    /**
     * 重置表单
     */
    function resetForm(){
        $(".tableSearch input:not(.btn1)").val("");
        $(".tableSearch select").val("-1");
    }
</script>
<!-- Left column -->
<div class="templatemo-flex-row">
    <jsp:include page="../sidebar.jsp"></jsp:include>
    <!-- Main content -->
    <div class="templatemo-content col-1 light-gray-bg">

        <form class="form-inline" name="form1" method="post">
            <div class="tableSearch">
                <%  //查询列表 %>
                <div class="form-group" >
                    <input class="form-control" type="text" name="shadeId" value="${attr.shadeId}"
                           placeholder="input shadId please" style="width: 200px" required="true"/>
                </div>
                <div class="form-group" >
                    <input class="form-control" type="text" name="shadeName" value="${attr.shadeName}"
                           placeholder="input shadeName please" style="width: 200px" />
                </div>
                <div class="form-group" >
                    <input class="form-control" type="text" name="shadePosition" value="${attr.shadePosition}"
                           placeholder="input shadePosition please" style="width: 210px" />
                </div>
                <br >
                <div class="form-group">
                    <input class="form-control" type="text" name="shadePriority" value="${attr.shadePriority}"
                           placeholder="input shadePriority please" style="width: 200px" />
                </div>
                <div class="form-group">
                    <input class="form-control" type="text" name="shadeStatus" value="${attr.shadeStatus}"
                           placeholder="input shadeStatus please"  style="width: 200px"/>
                </div>


                <button type="button" class="btn btn-default" onclick="oper_list();" name="search">
                    <span class="glyphicon glyphicon-search"></span> 查 询
                </button>
                <button type="button" class="btn btn-default" onclick="resetForm();">
                    <span class="glyphicon glyphicon-refresh"></span> 重 置
                </button>
            </div>

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
                                <%--<td>${item.remarks}</td>--%>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="pagination-wrap" id="paginator">
                <ul class="pagination">
                    <li>
                        <a id="1" aria-label="Next" href="javascript:void(0);" onclick="oper_list(1)">
                            <span aria-hidden="true">首页</span>
                        </a>
                    </li>
                    <c:if test="${pageInfo.hasPreviousPage}">
                        <li>
                            <a href="javascript:void(0);" onclick="oper_list(${pageInfo.pageNum}-1)"
                               aria-label="Previous">
                                <%--<span aria-hidden="true"><i class="fa fa-backward"></i></span>--%>
                                    <span aria-hidden="true"> < </span>
                            </a>
                        </li>
                    </c:if>
                    <c:forEach items="${pageInfo.navigatepageNums}" var="pageNums">
                        <c:if test="${pageNums == pageInfo.pageNum}">
                            <li class="active"><a
                                    href="javascript:void(0);" onclick="oper_list(${pageNums})">${pageNums}</a>
                            </li>
                        </c:if>
                        <c:if test="${pageNums != pageInfo.pageNum}">
                            <li>
                                <a href="javascript:void(0);" onclick="oper_list(${pageNums})">${pageNums}</a>
                            </li>
                        </c:if>
                    </c:forEach>
                    <c:if test="${pageInfo.hasNextPage}">
                        <li>
                            <a href="javascript:void(0);" onclick="oper_list(${pageInfo.pageNum}+1)"
                               aria-label="Next">
                                <span aria-hidden="true"> > </span>
                            </a>
                        </li>
                    </c:if>

                    <li>
                        <a id="2" href="javascript:void(0);" onclick="oper_list(${pageInfo.pages})"
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
            <input type="hidden" id="pageNum" name="pageNum" value="${pageInfo.pageNum}"/>
        </form>
    </div>
</div>

</body>
</html>
