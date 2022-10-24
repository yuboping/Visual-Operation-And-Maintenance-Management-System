require.config({
    baseUrl: '/js',
    paths: {
        "password": "lcims/tool/password",
        "jquery": "jquery/jquery.min",
        "jcookie": "jquery/jquery.cookie",
        "des": "lcims/des/des",
        "vcode": "lcims/tool/verifycode",
    },
    shim:{
        'jcookie': ['jquery'],
    },
});

require(["jquery", "password","des","vcode","jcookie"], function ($, password,des,vcode) {
    "use strict";
    var usernameObj = $('#username');
    var passwordObj = $('#password');
    var verifycodeObj = $('#verifycode');
    var errmsgObj = $('#errors');
    var loginsubmitObj = $('#loginsubmit');
    rmbUserSet();
    //用户名输入框获取焦点
    usernameObj.focus(function () {
        errmsgObj.hide();
    }).keydown(function(event){
        if (event.keyCode == 13) {
                passwordObj.focus();
        }
    });
    passwordObj.focus(function () {
        errmsgObj.hide();
    }).keydown(function(event){
        if (event.keyCode == 13) {
                verifycodeObj.focus();
        }
    });
    verifycodeObj.focus(function () {
        errmsgObj.hide();
    }).keydown(function(event){
        if (event.keyCode == 13) {
                loginsubmitObj.click();
        }
    });
    
    function rmbUserSet() {
        if ($.cookie("rmbUser") == "true") {
            $("#rmbUser").attr("checked", true);
            usernameObj.val($.cookie("userName"));
            var pwd = $.cookie("passWord");
            pwd = des.decStr(pwd);
            passwordObj.val(pwd);
        }
        refreshvcode();
    };

        $("#code").click(function(){
                refreshvcode();
        });
    
        function refreshvcode(){
        //刷新验证码
                var v=Math.random();
        document.getElementById("code").src="/login/createverify?random='"+v+"'";
    }

    /**
     * 提示信息
     * @param {String} message 提示信息
     * @param {Object} errObj 提示对象 默认为$("#usererror")
     */
    function notify(message,obj) {
        errmsgObj.text("").append(message).show();
    }
    
    //点击登录按钮时验证和记住密码
    loginsubmitObj.click(function () {
        errmsgObj.hide();
        var username = usernameObj.val();
        var pwd = passwordObj.val();
        var verifycode = verifycodeObj.val();
        if (username == "") {
            notify("用户名不能为空！",usernameObj);
            return;
        }
        var pwdValid = password.valid(pwd);
        if (!pwdValid.pass) {
            notify(pwdValid.message,passwordObj);
            return;
        }
//        误删测试用
        var vcodeValid = vcode.valid(verifycode);
        if (!vcodeValid.pass) {
                refreshvcode();
            notify(vcodeValid.message,verifycodeObj);
            return;
        }
        pwd = des.des(pwd);
        $.ajax({
            type: "post",
            url: "/login/getvcode"+"?random=" + Math.random(),
            data: [],
            dataType: "text",
            async: true,
            cache: false,
            success: function (data) {
                var oldCode = data.toUpperCase();
                var newCode = verifycode.toUpperCase();
                if(oldCode != newCode ) {
                    notify("验证码错误！",$('#verifycode'));
                    refreshvcode();
                } else {
                  //保存用户信息
                    if ($("#rmbUser").is(":checked") == true) {
                        $.cookie("rmbUser", "true", { expires: 7 }); // 存储一个带7天期限的 cookie
                        $.cookie("userName", username, { expires: 7 }); // 存储一个带7天期限的 cookie
                        $.cookie("passWord", pwd, { expires: 7 }); // 存储一个带7天期限的 cookie
                    }
                    else {
                        $.cookie("rmbUser", "false", { expires: -1 });
                        $.cookie("userName", '', { expires: -1 });
                        $.cookie("passWord", '', { expires: -1 });
                    }
                    
                    var params = "username=" + username + "&password=" + pwd+"&verifycode=" + verifycode;
                    $.ajax({
                        url: "/login",
                        data: params,
                        dataType: "json",
                        async: false,
                        cache: false,
                        success: function (data) {
                            var loginresult = data.opSucc;
                            //success
                            if (loginresult) {
                                //调用后台跳转页面程序
                                var homepage = $.ajax({url:"/loginsuccess",dataType: "text",async:false});
                                $("#loginform").attr("action", homepage.responseText);
                                $("#loginform").submit();
                            }
                            else {
                                var loginmessage = data.message;
                                if(loginmessage.indexOf("vcodeerror") >=0){
                                    notify("验证码错误！",$('#verifycode'));
                                }else {
                                    notify(loginmessage);
                                }
                                refreshvcode();
                            }
                        },
                        error: function (data) {
                            console.log(data);
                        }
                    });
                }
                
            }
        });
    });
});