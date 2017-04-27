<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/3/13
  Time: 16:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
</head>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String socketPath=request.getServerName()+":"+request.getServerPort()+path;
%>
<body>
<script src="<%=basePath%>static/sockjs/sockjs.js"></script>
<script src="<%=basePath%>js/jquery.min.js"></script>
<%--<script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>--%>
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
//        alert(openEvt.Data);
    }

    function onMessage(evt) {
        alert(evt.data);
    }
    function onError(evt) {
        alert(evt.data)
    }
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

</script>
请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>

<form action="/websocket/send.do">
    姓名：<input type="text" name="username"/>
    <input type="submit" value="发送"/>
</form>
</body>
</html>
