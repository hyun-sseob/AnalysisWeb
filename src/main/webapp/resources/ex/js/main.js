$("#login").on("click", function(){
    $("#layer").css("display", "block");
    $("#container").css("opacity", "0.5");
    $("#login-box").css("display", "block");
    
    $("#loginBtn").on("click", function(e){
		e.preventDefault();
		    layerOut();
		    var email = $("#email").val();
	    	var password = $("#password").val();
		
	    	var params = { 
					"email" : email,
					"password" : password
				}
	    	console.log(params);
	    	
	    	$.ajax({
	    		type:"post",
				url:"/shs/userLogin",
				data : params
			}).done(function(data){
				d = JSON.parse(data);
		
			if(d.status == 1){
				alert("로그인 완료");
				$("#login").hide();
				$("#logout").show();
				$("#myinfo").css("display","block");
				$("#write-btn").show();
			}else if(d.status == 0){
				alert("로그인 정보를 다시 확인해 주세요.");
			}
		});
	});
});

$("#write-btn").on("click", function(){
	
	$.ajax({
		url:"/shs/userLevel"
	}).done(function(data){
		d = JSON.parse(data);
		console.log(d);
		
		var userNo = d.userNo; 
		
		if(d.status == "1"){
			location.href="write.html?userNo=" + userNo;
		}else{
			alert("관리자만 글을 작성할 수 있습니다.");
		}
	})
});

$("#send").on("click", function(){
	var msgName = $("#usrName").val();
	var msgEmail = $("#usrMail").val();
	var msgContents = $("#usrMessage").val();
	
	$.ajax({
		url:"/shs/sendMessage",
		data:{
			"msgName" : msgName,
			"msgEmail" : msgEmail,
			"msgContents" : msgContents
		}
	}).done(function(data){
		
		d = JSON.parse(data);
		console.log(d);
		
		if(d.status == 1){
			alert("메세지 전송 완료");
		}else{
			alert("실패 (sendMessage)");
		}
	});
});

$("#regBtn").on("click", function(){
	location.href = "join.html";
});

$("#logout").on("click", function(){
	location.href = "/shs/logout";
});

$("#layer").on("click", function(){
    layerOut();
});
/*글쓰기 버튼 클릭*/


/* 공통부분 */
function layerOut(){
    $(".pw-change-box").hide();
    $(".message-contents").hide();
    $("#layer").css("display", "none");
    $("#container").css("opacity", "1");
    $("#login-box").css("display", "none");
    $(".join-box").css("display", "none");
    $(".send-mail").css("display", "none");
}

$(".arc-box-img").on("click" , function(){
    location.href = "detail.html";
});

$("#detail-list").on("click" , function(){
    location.href = "index.html";
});


$("#leftMenu").on("click" , function(e){
    $("#left-copy").css("display","block").css("z-index","5005").css("width","200px").css("height","auto").css("padding-bottom", "15px");
    e.stopPropagation();
});
$("#right:not(#leftMenu1)").on("click", function(){
            $("#left-copy").hide(300);
});

$(window).on("resize", function(){
    var width = $(window).width();
    if(width < 768){
        $("#left-copy").hide();
        $("#right:not(#leftMenu1)").on("click", function(){
            console.log($(this));
            console.log("띠로리리리리");
            $("#left-copy").hide(300);
        });
    }
});
