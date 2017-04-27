<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/3/13
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page isELIgnored="false" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String socketPath=request.getServerName()+":"+request.getServerPort()+path;
%>
<head>
    <title>Title</title>
    <base href="<%=basePath%>"></base>
</head>

<%--<script src="./jquery.etalage.min.js"></script>--%>
<script src="<%=basePath%>js/jquery.min.js"></script>
<script src="<%=basePath%>js/jquery.form.js"></script>
<link rel="stylesheet" href="<%=basePath%>static/bootstrap/css/bootstrap.min.css">
<script src="<%=basePath%>static/bootstrap/js/bootstrap.min.js"></script>

<%--<link rel="stylesheet" href="https://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">--%>
<script type="text/javascript">
    var websocket = null;
    if ('WebSocket' in window) {
//        websocket = new WebSocket("ws://192.168.20.86:8080/websocket/socketServer.do");
        websocket = new WebSocket("ws://"+"<%=socketPath%>"+"/websocket/socketServer.do");
    }
    else if ('MozWebSocket' in window) {
//        websocket = new MozWebSocket("ws://192.168.20.86:8080/websocket/socketServer.do");
        websocket = new MozWebSocket("ws://"+"<%=socketPath%>"+"/websocket/socketServer.do");
    }
    else {
//        websocket = new SockJS("http://192.168.20.86:8080/sockjs/socketServer.do");
        websocket = new SockJS("http://"+"<%=socketPath%>"+"/sockjs/socketServer.do");
    }
    websocket.onopen = onOpen;
    websocket.onmessage = onMessage;
    websocket.onerror = onError;
    websocket.onclose = onClose;

    function onOpen(openEvt) {
        //alert(openEvt.Data);
    }

    function onMessage(evt) {
        var result=evt.data;
        var results=result.split(",");
        if(results[0]=="pre"){
            $("#show-upgrade-tip").text("已找到"+results[1]+"台");
        }else if(results[0]=="percent"){
            $("#show-upgrade-tip").text("已完成"+results[1]+"%");
            $("#progress").css("width",results[1]+"%")
        }else if(results[0]=="origin"){
            $("#draperOriginal").append("<li class='list-group-item'>"+results[1]+"</li>");
        }else if(results[0]=="upgradeBefore"){
            $("#draperUpgradeBefore").append("<li class='list-group-item'>"+results[1]+"</li>");
        }else if(results[0]=="upgradeAfter"){
            $("#draperUpgradeAfter").append("<li class='list-group-item'>"+results[1]+"</li>");

        }

    }
    function onError() {}
    function onClose() {}

    function doSend() {
        if (websocket.readyState == websocket.OPEN) {
            var msg = document.getElementById("inputMsg").value;
            websocket.send(msg);//调用后台handleTextMessage方法
            alert("发送成功!");
        } else {
            alert("连接失败!");
        }
    }
    window.close=function()
    {
        websocket.onclose();
    }

    $.ajax({
        <%--url:"/"+"<%=path%>"+"/websocket/login",--%>
        url:"websocket/login",
        type:"POST",
        dataType:"json",
        data:{
            username:"cyf"
        }
    })
</script>
<script type="text/javascript">

//    $.get('http://localhost:8080/data/data/0', $("btn_get").serialize(), function(data) {});
    //获取端口
//    function getUser() {
        $.ajax({
            <%--url:"<%=basePath%>"+'v1/init/ports',--%>
            url:'v1/init/ports',
            type:'GET',
            dateType:'JSON',
            success:function (data) {
                data.forEach(function (port) {
                    $("#ports").append("<option>"+port+"</option>")
                })
            },
            error:function () {

            }
        });
//    }
    //
    function startInit() {
        $.ajax({
            url:'v1/init/init',
            type:'POST',
            dateType:'json',
            data:{
                port:$("#ports").val()
            }
        }).done(function (data) {
            alert("初始化"+data);
        }).fail();
    }

    function getDrapers() {
        $.ajax({
            url:'v1/draper/drapers',
            type:'GET',
            dateType:'json',
        }).done(function (data) {
            data.forEach(function (draper) {
                $("#drapers").append("<option>"+draper+"</option>")
            })
        }).fail();
    }
    function draperOperator(cmd) {
        $.ajax({
            url:'v1/draper/operation',
            type:'POST',
            dateType:'json',
            data:{
                draperID:$("#drapers").val(),
                cmd:cmd
            }
        }).done().fail();
    }

    function getGroup(){
        $.ajax({
            url:"v1/group/map",
            type:"GET",
            dateType:"json"
            }
        ).done(function (data) {

        }).fail();
    }

    function addGroup(){
        var d=[];
        var draper=$("#drapers").val();
        if(draper!=null && ""!=draper){
            d.push(draper);
        }else {
            d.push(3);
            d.push(4);
            d.push(5);
        }
        $.ajax({
            url:"v1/group/add/"+$("#deviceID").val()+"/"+$("#groupID").val(),
            type:"POST",
            dateType:"json",
            traditional:true,
            data:{
                draperList:d
            }
//            data:{draperList:JSON.stringify(d)}
//            headers : {
//                'Accept' : 'application/json',
//                'Content-Type' : 'application/json'
//            }
        }).done(function (data) {}).fail();
    }

    function delSelectedGroup(){
        var deviceID=$("#deviceID").val();
        var groupID=$("#groupID").val();
        var draper=$("#drapers").val();
        if(deviceID=="" || groupID=="" || draper==null){
            alert("deviceID,groupID,draperID不能为空");
            return;
        }
        $.ajax({
            url:"v1/group/delete/"+deviceID+"/"+groupID+"/"+draper,
            type:"POST",
            dateType:"json",
            traditional:true,
            data:{
                _method:"DELETE",
            }
        }).done(function (data) {}).fail();
    }

    function delAllGroup(){
        var deviceID=$("#deviceID").val();
        var groupID=$("#groupID").val();
        if(groupID=="" || deviceID==""){
            alert("deviceID,groupID不能为空");
            return;
        }
        $.ajax({
            url:"v1/group/delete/"+deviceID+"/"+groupID,
            type:"POST",
            dateType:"json",
            traditional:true,
            data:{
                _method:"DELETE",
            }
        }).done(function (data) {}).fail();
    }

    function updateGroup() {
        $.ajax({
            url:"v1/group/update/2",
            type:"POST",
            dataType:"json",
            data:{
                _method:"PUT",
                groupName:$("#groupName").val()
            }
        }).done().fail();
    }

    function groupOperator(cmd) {
        $.ajax({
            url:'v1/group/operation',
            type:'POST',
            dateType:'json',
            data:{
                deviceID:$("#deviceID").val(),
                groupID:$("#groupID").val(),
                cmd:cmd
            }
        }).done().fail();
    }

    function uploadFile() {
//        $.ajax({
//            url:"http://localhost:8080/v1/upgrade/upload",
//            type:"POST",
//            enctype: 'multipart/form-data',
//            async:true,
//            data:$("#upload").serialize()
//        }).done(function (data) {
//            alert(data);
//        }).fail(function (d) {
//            alert(d);
//        });
        $("#upload").ajaxSubmit({
            success: function (data) {
                alert(data.corrupt);
                $("#type").val(data.type);
                $("#typeNum").val(data.typeNum);
                $("#majorNum").val(data.majorNum);
                $("#minorNum").val(data.minorNum);
                $("#patchNum").val(data.patchNum);
            },
            error: function (error) {
                alert("error"+error);
            },
            url: 'v1/upgrade/upload', /*设置post提交到的页面*/
            type: "post", /*设置表单以post方法提交*/
            async:true,
            dataType: "json" /*设置返回值类型为文本*/
        });
    }

    function upgradeSelected() {
        $("#show-upgrade-tip").text("升级准备中...");
//        $("#mymodal").modal({backdrop:'static',keyboard:'false'});
        $.ajax({
            url:'v1/upgrade/upgrade',
            type:"POST",
            dataType:"json",
            data:{
                draperID:$("#drapers").val()
            },
            success:function () {
                $("#progress").css("width",0+"%")
                ref=setInterval("getPercent()",1000);
            },

        }).done.fail();
    }

    //-1 代表更新所有
    function upgrade() {
        $("#show-upgrade-tip").text("升级准备中...");
//        $("#mymodal").modal({backdrop:'static',keyboard:'false'});
        $.ajax({
            url:'v1/upgrade/upgrade',
            type:"POST",
            dataType:"json",
            data:{
                draperID:-1
            },
            success:function () {
                $("#progress").css("width",0+"%")
//                ref=setInterval("getPercent()",1000);
//                $( "#dialog-confirm" ).dialog({
//                    resizable: false,
//                    height:140,
//                    modal: true,
//                    buttons: {
//                        "确认": function() {
//                            $( this ).dialog( "close" );
//                        },
//                        "Cancel": function() {
//                            $( this ).dialog( "close" );
//                        }
//                    }
//                });
            }
        });
    }


    var ref;
    function readVersion() {
        var draper=$("#drapers").val();
        if(draper==null){
            draper=1;
        }
        $.ajax({
            url:'v1/upgrade/version/'+draper,
            type:'GET',
            dateType:'json',
            success:function (data) {
                $("#version").text(data);
            },
            error:function (data) {
                $("#version").text(data);
            }
        });
//        $("#dialog-confirm").dialog({
//                    resizable: false,
//                    height:140,
//                    modal: true,
//                    buttons: {
//                        "确认": function() {
//                            $( this ).dialog( "close" );
//                        },
//                        "Cancel": function() {
//                            $( this ).dialog( "close" );
//                        }
//                    }
//                });
    }

//    function getPercent() {
//        $.ajax({
//            url:'v1/upgrade/percent',
//            type:'GET',
//            dataType:'json',
//            success:function (data) {
//                $("#progress").css("width",data+"%")
//                if(data==100){
//                    clearInterval(ref);
//                }
//            },
//            error:function () {
//
//            }
//        })
//    }

//$(function(){
//    $(".btn").click(function(){
//        $("#mymodal").modal("toggle");
//    })});

    function test1() {
        $("#show-upgrade-tip").text("已找到台");
    }
</script>

</head>
<body>
<h5>request:${requestScope.message}</h5>
<h5>${message}</h5>
<select id="ports" name="ports" class="form-control-static"></select>
<button type="button" class="btn btn-default" onclick="startInit()">start</button>
<br/>
<br/>
<select id="drapers" name="darpers" class="form-control-static"></select>
<button type="button" class="btn btn-default" onclick="getDrapers()">get</button>
<div>
    <button type="button" class="btn btn-default" onclick="draperOperator(3)">up</button>
    <button type="button" class="btn btn-default" onclick="draperOperator(1)">stop</button>
    <button type="button" class="btn btn-default" onclick="draperOperator(4)">down</button>
</div><br/>

<%--<select multiple class="form-control-static">--%>
    <%--<option>14564687123</option>--%>
    <%--<option>24564687123</option>--%>
    <%--<option>34564687123</option>--%>
    <%--<option>44564687123</option>--%>
    <%--<option>54564687123</option>--%>
<%--</select>--%>
<div>
    <button type="button" class="btn btn-default" onclick="getGroup()">getGroup</button><br/>
    <label>deviceID:</label>  <input type="text" placeholder="请输入设备ID" id="deviceID"><br/>
    <label>groupID:</label>   <input type="text" placeholder="请输入组ID" id="groupID"><br/>
    <button type="button" class="btn btn-default" onclick="addGroup()">addGroup</button>
    <button type="button" class="btn btn-default" onclick="delSelectedGroup()">delSelectGroup</button>
    <button type="button" class="btn btn-default" onclick="delAllGroup()">delAllGroup</button><br/>
    <input type="text" id="groupName" placeholder="请输入组名称">
    <button type="button" class="btn btn-default" onclick="updateGroup()">updateGroup</button>
    <div>
        <button type="button" class="btn btn-default" onclick="groupOperator(3)">up</button>
        <button type="button" class="btn btn-default" onclick="groupOperator(1)">stop</button>
        <button type="button" class="btn btn-default" onclick="groupOperator(4)">down</button>
    </div><br/>
</div>
<br/>
<div>
    <form name="upload" id="upload" enctype="multipart/form-data">
        <input type="file" name="file">
        <button type="button"  class="btn btn-default" onclick="uploadFile()">upload</button>
    </form>

    <input type="text" id="type">
    <input type="text" id="typeNum">
    <input type="text" id="majorNum">
    <input type="text" id="minorNum">
    <input type="text" id="patchNum"><br/>
    <button type="button" class="btn btn-default" onclick="upgradeSelected()">upgrade selected</button>
    <button type="button" class="btn btn-default" onclick="upgrade()">upgrade</button><br/>
    <button type="button" class="btn btn-default" onclick="readVersion()">readVersion</button>
    <Label id="version"></Label>
</div><br/>

<Label id="show-upgrade-tip"></Label> <br/>

<div class="progress progress-striped active">
    <div id="progress" name="progress" class="progress-bar progress-bar-success" role="progressbar"
         aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
         style="width: 0%;">
        <span class="sr-only">90% 完成（成功）</span>
    </div>
</div><br/>

<div style="width: 20%;float: left">
    所以设备<br/>
<ul class="list-group" id="draperOriginal"></ul>
</div>

<div style="width: 20%;float: left">
    升级前<br/>
<ul class="list-group" id="draperUpgradeBefore"></ul>
</div>

<div style="width: 20%;float: left">
    升级后<br/>
<ul class="list-group" id="draperUpgradeAfter"></ul>
</div>
<br/>


<div class="modal fade" id="mymodal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">提示</h4>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default" onclick="test1()">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</body>
</html>
