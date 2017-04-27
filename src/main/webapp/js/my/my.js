function checkLogin(){
	var account = $("#login_account").val();
	var password = $("#login_password").val();
}
function CheckForm(){

	var flag = true;
	var newpas = $("#register_newpas").val();
	var checkpas = $("#register_checkpas").val();
	var account = $("#register_account").val();
	var tele = $("#register_tele").val();
	var address = $("#register_address").val();
	
	if(newpas != checkpas){
		flag = false;
		alert("验证密码和新密码不相同");
	}else if(account == "" || tele == "" || address == "" || newpas == "" || checkpas==""){
		flag = false;
		alert("信息不能为空")
	}else if(!checkAccount()){
		flag = false;
	}
	
	return flag;
}
function addList(node){
	
	var id = node.id;
	$.ajax({
		url:"addListAndDelCar.do",
		async: false,
		data:{
		"id":id
	},
		dataType:"json",
		type:"post",
		success: function(data){
			var map = data.data;
			var name = map.p_name;
			var price = map.p_price;
			var num = map.c_num;
			addListTr(name,price,num);
			$("#price").val(map.total);
	}
		
	});
}
function addListTr(name,price,num){
	
	//得到table对象
    var mytable = document.getElementById("order");
  
    //向table中插入一行
    var mytr=mytable.insertRow();
    mytr.setAttribute("class", "listtr");
  
    //创建一个新的td对象
    var mytd=document.createElement("td");
    var mytd1=document.createElement("td");
    var mytd2=document.createElement("td");
    
    
    //创建一个新的span对象
    var myspan=document.createElement("span");
    var myspan1=document.createElement("span");
    var myspan2=document.createElement("span");
    myspan.setAttribute("class", "detail");
    myspan1.setAttribute("class", "detail");
    myspan2.setAttribute("class", "detail");
    myspan.innerHTML=name;
    myspan1.innerHTML=price;
    myspan2.innerHTML=num;
  
    //添加td中span对象
    mytd.appendChild(myspan);
    mytd1.appendChild(myspan1);
    mytd2.appendChild(myspan2);
  
    //向tr中加入td对象
    mytr.appendChild(mytd);
    mytr.appendChild(mytd1);
    mytr.appendChild(mytd2);
}

function checkAccount(){
	var account = $("#register_account").val();
	var flag = false;

	$.ajax({
		url:"checkAccount.do",
		async: false,
		data:{
		"account":account
	},
		dataType:"json",
		type:"post",
		success: function(data){
		
			flag = data.success;		
			if(data.success){
				alert("账号可用");
			}else{
				alert("账号已被注册");
			}
	},
	error: function(){
		alert("errir");
	}
		
	});
	
	return flag;
}