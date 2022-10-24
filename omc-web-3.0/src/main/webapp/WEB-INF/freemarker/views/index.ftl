<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><@bs.header ["iconfont/iconfont.css","login.css"] />
    <input type="hidden" id="homepage" name="homepage" value="${homepage}"/>
    <body>
        <div class="bg_box"  style="position:relative; width:100%; height:100%; z-index:-1"> 
     		<img style="position:fixed;" src="images/login_bg.jpg" height="100%" width="100%" />
	    </div>
	    <div class="main">
	    	<div  class="box clearfix">
	    		<div class="logo_box ">
	    			<img src="/images/${province}/logo.png" >
	    			<span class="logo_name ">${systemname}</span>
    			</div>
				<form id="loginform"></form>
				<div class="login_right">
					<div class="login_title" >用户登录</div>
					<div class="errors" style="display:none;" id="errors">
                    </div>
                    <div class="row mt15" >
                        <label class="login-label"><span class="iconfont icon-loginyonghu"></span></label>
                        <input type="text" size="25" placeholder="账号" class="required" name="username" id="username"/>
                    </div>
                     <div class="row">
                        <label class="login-label"><span class="iconfont icon-mima"></span></label>
                        <input type="password" size="25" class="required password" name="password" id="password" placeholder="密码"/>
                        <input type="hidden" size="2"  name="pwd_write_flag" id="pwd_write_flag" value="1"/>
                    </div>
                    <div class="row clearfix">
                        <div class="yzm_input fl " >
                          <label class="login-label "><span class="iconfont icon-yanzhengma"></span></label>
                          <input type="text" id="verifycode" name="verifycode" class="required yzm" maxlength="4" placeholder="验证码">
                          <i class="iconfont icon-gou green" style="display:none"></i>
                          <i class="iconfont icon-gou red" style="display:none"></i>
                        </div>
                        <div class="yzm_img fl" id="verifycodeerror" title="看不清,换一张">
                        	<img id="code" name="code" />
                        </div>
                    </div>
                    <div class="row clearfix">
                        <div class="remember-pwd fl"><label><input id="rmbUser" type="checkbox"> 记住密码</label></div>
                        <#if forgetFlag>
                       <div class="forget-pwd fr" style="text-align:right;" >
                        <a href="/forgetPwd" > <label style="cursor:pointer;">忘记密码</label></a></div>
                        </#if>
                    </div>
                    <div class="row btn-row"  >
                        <input  value="登 录" type="button" class="btn-submit"  id="loginsubmit">
                    </div>
                </div>
			</div>
		</div>	
    </body>
    <div class="foot">版权所有©亚信安全科技股份有限公司 </div>
    <script data-main="js/lcims/login" type="text/javascript" src="/js/require/require.js"></script>
</html>