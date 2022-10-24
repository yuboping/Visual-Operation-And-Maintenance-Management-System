require.config({
    baseUrl: '/js',
    paths: {
        "password": "lcims/tool/password",
        "jquery": "jquery/jquery.min",
        "jcookie": "jquery/jquery.cookie",
        "des": "lcims/des/des",
        'stringutil': '/js/lcims/tool/stringutil',
        'layer':'/js/layer/layer',
        "vcode": "lcims/tool/verifycode",
    },
    shim:{
        'jcookie': ['jquery'],
    },
});

require(["jquery", "password","des","vcode","stringutil","layer","jcookie"], function ($, password,des,vcode,stringutil,layer) {
    "use strict";
    
   
    var adminis=false
    var countdownnum=$("#countdownnum").val()
    var wait=countdownnum;
    var waitTime=0;

    //密码验证
    function checkPwd(pwd) {
        var regPwd =/^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[\(\)])+$)([^(0-9a-zA-Z)]|[\(\)]|[a-z]|[A-Z]|[0-9]){8,12}$/;
        return regPwd.test(pwd);
    }
    //密码校验
  function validPwd(type, pwd,pwd_s) {
    if(!checkPwd(pwd)) {
        $("#" + type).focus();
        layer.tips('密码长度为8~12位，且必须包含大小写字母、数字、特殊符号任意两者组合!', '#' + type, {
            tips : [ 2, '#EE1A23' ]
        });
        return true;
    }
    if(pwd!=pwd_s){
        $("#" + type).focus();
        layer.tips('密码和确认密码不一致!', '#' + type, {
            tips : [ 2, '#EE1A23' ]
        });
        return true;
    }
    return false;
}
   //手机号码验证
  function checkPhone(contactphone) {
          var regex = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
          return regex.test(contactphone);
      }
   //手机号码校验   
  function validPhone(type, contactphone) {
  	if(null == contactphone || contactphone==""){
          return false;
      }
  	if(contactphone.length!=11){
          $("#" + type).focus();
          layer.tips('联系电话必须是11位!', '#' + type, {
              tips : [ 2, '#EE1A23' ]
          });
          return true;
      }
      if(!checkPhone(contactphone)){
          $("#" + type).focus();
          layer.tips('请输入正确的联系电话格式!', '#' + type, {
              tips : [ 2, '#EE1A23' ]
          });
          return true;
      }
      return false;
  }
  //邮箱验证
  function checkEmail(email) {
      var regex = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
      return regex.test(email);
  }
  //邮箱校验
  function validEmail(type, email) {
  	if(null == email || email==""){
          return false;
      }
  	if(!checkEmail(email)) {
  		$("#"+type).focus();
          layer.tips('请输入正确的邮件格式!', '#'+type,{ tips: [2, '#EE1A23'] });
          return true;
  	}
  	return false;
  }
  //用户名校验
  function adminCheck(){
  	var admin=stringutil.Trim($("#admin").val())
  	 if(stringutil.checkString("admin",admin,"用户名不能为空!")){
	            return false;
	     }
  	 //查看用户名是否存在
  	 $.ajax({
         type: "post",
         url: "/forgetPwd/checkAdmin?random=" + Math.random(),
         cache: false,
         async: false, 
         data: {"admin": admin},
         success: function (result) {
        	
         	if(result != 0){ //存在
         		adminis=true
         	}else{  //不存在
         		adminis=false
         		layer.tips("用户名不存在!", '#' + 'admin', {
          			tips : [ 2, '#EE1A23' ]
          		});
         	}
        	return adminis;
         }
      });  	
  }
  //倒计时
  function countdown(){
	  var o=$("#loginsubmit"+$("#checkType").val())
	  if (wait == 0) {
		   o.removeAttr("disabled");
		   o.val("点击发送验证码");
		   wait = waitTime;
		  } else { 
		 
		   o.attr("disabled","true"); 
		   o.val("重新发送(" + wait + ")");
		   wait--;
		   setTimeout(function() {
			   countdown()
		   },
		   1000)
		  }
  }
  //提交发送验证码
    function sendCheckCode(){
    	adminCheck()
    
    	if(!adminis){
    		return
    	}
    	var checkType=$("#checkType").val()
    	var phoneType=$("#contactphone")
    	var emailType=$("#email")
    	wait=countdownnum
    	waitTime=countdownnum
		//先校验手机号,在发送验证码
		
		//验证参数   
		var b=checkTypeIs(checkType,phoneType,emailType)
		if(b){
			//验证手机号/邮箱是否是创建用户时的
			checkPhoneOREmail(checkType,checkType=="1"? phoneType.val():emailType.val());
		}
		
	}
    //验证手机号码/邮箱地址
	function checkPhoneOREmail(checkType,checkValue){
		//去后台验证手机号/邮箱地址 是否是创建用户时留下的
		var admin=stringutil.Trim($("#admin").val())
		if(!adminis){
			layer.tips("用户名错误!", '#' + 'admin', {
    			tips : [ 2, '#EE1A23' ]
    		});
    		return
    	}
		 $.ajax({
	         type: "post",
	         url: "/forgetPwd/checkType?random=" + Math.random(),
	         cache: false,
	         async: false, 
	         data: {"admin": admin,"checkType":checkType,"checkValue":checkValue},
	         success: function (result) {
	        	
	         	if(result.opSucc){
	         		//是 发送验证码成功
	         		result.message
	         		layer.msg(result.message, {
	         		    time: 2000//20s后自动关闭
	         		  });
					countdown()
	         	}else{
	         		//不是/验证码发送失败 提示
	         		var messages=result.message.split('_');
	         		var menum=messages[0]
	         		var message=messages[1]
	         		if(menum == "1"){
	         			var checkType=$("#checkType").val()
						var mag=""
						if(checkType == "1"){
							layer.tips(message, '#' + 'contactphone', {
				    			tips : [ 2, '#EE1A23' ]
				    		});
						}else if(checkType == "2"){
							layer.tips(message, '#' + 'email', {
				    			tips : [ 2, '#EE1A23' ]
				    		});
						}
	         		}else if(menum == "5"){
	         			wait=messages[1]
	         	    	waitTime=messages[1]
	         			countdown()
	         			layer.msg(messages[2], {
		         		    time: 2000//20s后自动关闭
		         		  });
	         		}else{
	         			layer.msg(message, {
		         		    time: 2000//20s后自动关闭
		         		  });
	         		}
					
	         	}
	         }
	      });
	}
	
    //发送验证码时 校验手机号码/邮箱地址
    function checkTypeIs(checkType,phoneType,emailType){
		//先获取验证方式 1:手机短信验证 2:邮箱验证
    	var checckValue="";
    	switch(checkType) {
	     case "0":
	    	 if(stringutil.checkString("checkType",checkType,"请选择验证方式!")){
	    		 return false;
	    	 }
	        break;
	     case "1":
	    	 if(stringutil.checkString("contactphone",stringutil.Trim(phoneType.val()),"手机号不能为空!")){
	    		 return false;
	    	 }
	    	  if(validPhone('contactphone',stringutil.Trim(phoneType.val()))) {
	          	return false;
	          }
	    	 checckValue=phoneType.val()
	        break;
	     case "2":
	    	 if(stringutil.checkString("email",stringutil.Trim(emailType.val()),"邮箱地址不能为空!")){
	    		 return false;
	    	 }
	    	 if(validEmail('email',stringutil.Trim(emailType.val()))) {
	         	return false;
	         }
	    	 checckValue=emailType.val()
	    	 break;
	     case "3":
	    	 break;
	     default:
	        alert("验证方式配置错误")
	        return false;
	        break;
    	}
    	return true;
	}
    
    //用户名输入后 验证
    $("#admin").blur(function(){
    	adminCheck();	
    })
    //验证码发送提交
    $("#loginsubmit1").click(function(){
    	sendCheckCode();
    })
    //验证码发送提交
     $("#loginsubmit2").click(function(){
    	 sendCheckCode();
    })
    
    $("#loginsubmit3").click(function(){
    	 sendCheckCode();
    })
    
    //验证方式改变
    $("#checkType").change(function(){
    	
    	var checkType=$("#checkType").val()
    	var phoneType=$("#isp")
    	var emailType=$("#ism")
    	var tokenType=$("#ist")
    	switch(checkType) {
	     case "0":
	    	 phoneType.css("display","none")
	    	 emailType.css("display","none")
	         tokenType.css("display","none")
	         $("#verificationcodename").html("验 证 码:");
	        break;
	     case "1":
	    	 phoneType.css("display","inline")
	    	 emailType.css("display","none")
	    	 tokenType.css("display","none")
	    	 $("#verificationcodename").html("验 证 码:");
	        break;
	     case "2":
	    	 phoneType.css("display","none")
	    	 emailType.css("display","inline")
	    	 tokenType.css("display","none")
	    	 $("#verificationcodename").html("验 证 码:");
	    	 break;
	     case "3":
	    	 phoneType.css("display","none")
	    	 emailType.css("display","none")
	    	 tokenType.css("display","inline")
	    	 $("#verificationcodename").html("令牌动态密码:");
	    	 break;
	     default:
	    	 phoneType.css("display","none")
	    	 emailType.css("display","none")
	    	 tokenType.css("display","none")
	    	 $("#verificationcodename").html("验 证 码:");
	        alert("验证方式配置错误")
	        break;
    	}
    })
    	//修改密码提交
    	$("#submitForget").click(function(){
    		if(!adminis){
    			$("#" + "admin").focus();
    			layer.tips("用户名不存在!", '#' + 'admin', {
        			tips : [ 2, '#EE1A23' ]
        		});
        		return;
    		}
    		var admin=stringutil.Trim($("#admin").val())
    		var checkCode=stringutil.Trim($("#checkCode").val())
    		var password=stringutil.Trim($("#password").val())
    		var oncePassword=stringutil.Trim($("#oncePassword").val())
    		if(stringutil.checkString("checkCode",checkCode,"验证码不能为空!")
    				||stringutil.checkString("password",password,"密码不能为空!")
    				||stringutil.checkString("oncePassword",oncePassword,"确认密码不能为空!")){
    			return
    		}
    		//验证密码
		 if(validPwd('password',password,oncePassword)) {
	        	return;
	     }
		 //密码加密
		 var oncePwd = des.des(oncePassword);
	     var pwd = des.des(password);
	     var checkType=$("#checkType").val()
		 //验证都通过,发送后台修改密码
	     $.ajax({
	         type: "post",
	         url: "/forgetPwd/changePossword?random=" + Math.random(),
	         cache: false,
	         async: false, 
	         data: {"admin": admin,"checkCode":checkCode,"password":pwd,"oncePassword":oncePwd,"checkType":checkType},
	         success: function (result) {
	        	 layer.msg(result.message, {
	         		    time: 2000//20s后自动关闭
	         		  });
	         }
	      });
	     return false;
	     //event.stopPropagation();    //  阻止事件冒泡
    	})
    	
    
});