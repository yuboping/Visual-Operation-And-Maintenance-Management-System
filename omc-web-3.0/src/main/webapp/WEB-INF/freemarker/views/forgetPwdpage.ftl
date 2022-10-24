<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><@bs.header ["iconfont/iconfont.css","login.css","dpl.css","layer/layer.css"] />
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
				<input id="countdownnum" type="hidden" value="${countdown}"/>
				<div class="login_right">
					<div class="login_title" >用户密码修改</div>
					<div class="row-fluid mb10" style="margin-top: 10px"">
		                <div class="controls">
		                    <label class="control-label">用 户 名:</label>
		                    <input size="30" class="input-medium" id="admin" name="admin" type="text">
		                    <span class="text-danger">*</span><br>
		                </div>
		                 
		            </div>
		            <div class="row-fluid mb10">
		            	<div class="controls"  style="margin-top: 10px">
		                    <label class="control-label">验证方式:</label>
		                     <select class="input-medium" id="checkType" name="checkType">
		                     		<option value="0">请选择</option>
		                     		<#list codetype as m>
                                        <option value=${m.code}>${m.description}</option>
                                    </#list>
                              </select>
		                    <span class="text-danger">*</span><br>
		                </div>
		            </div>
		            <div class="row-fluid mb10">
		            	<div id="isp" class="controls"  style="margin-top: 10px;display:none">
		            		<div style="text-align:left;padding-left: 85px;"><span style="color: red;">请输入用户创建时手机号</span></div>
		                    <div><label class="control-label">手 机 号:<input size="30" class="input-medium" id="contactphone" type="text"></label></div>
		                    <input  value="点击发送验证码" type="button" class="btn-submit" style="height:26px;width: 150px;margin-left:50px" id="loginsubmit1">
		                </div>
		                <div id="ism" class="controls"  style="margin-top: 10px;display:none">
		                	<div style="text-align:left;padding-left: 85px;"><span style="color: red;">请输入用户创建时邮箱地址</span></div>
		                    <div><label class="control-label">邮箱地址:<input size="30" class="input-medium" id="email" type="text"></label></div>
		                    <input  value="点击发送验证码" type="button" class="btn-submit" style="height:26px;width: 150px;margin-left:50px" id="loginsubmit2">
		                </div>
		                <div id="ist" class="controls"  style="margin-top: 10px;display:none"></div>
		            </div>
		            <div class="row-fluid mb10" style="margin-top: 10px">
		                <div class="controls">
		                    <label id="verificationcodename" class="control-label">验 证 码:</label>
		                    <input size="30" class="input-medium" id="checkCode" type="text">
		                    <span class="text-danger">*</span><br>
		                </div>
		            </div>
					<div class="row-fluid mb10" style="margin-top: 10px">
	                	<div class="controls">
	                	<div style="text-align:left;padding-left: 85px;"><span style="color: red;">密码长度为8~12位</span></div>
	                    <div style="text-align:left;padding-left: 85px;"><span style="color: red;">必须包含大小写字母、数字、特殊符号任意两者组合</span></div>
	                    <label class="control-label">新 密 码:</label>
	                    <input size="30" class="input-medium" id="password" type="password">
	                    <span class="text-danger">*</span><br> 
	                </div>
		            </div>
		            <div class="row-fluid mb10">
		                <div class="controls">
		                    <label class="control-label">确认密码:</label>
		                    <input size="30" class="input-medium" id="oncePassword" type="password">
		                    <span class="text-danger">*</span>
		                </div>
		        	</div>
                    <div class="row-fluid mb10">
                        <a href="/" > <label style="cursor:pointer;margin-left:45px;margin-top: 10px;font-size: 16px;line-height: 30px;display:inline-block">返回登陆</label></a>
                        <input  value="提交修改" type="button" class="btn-submit" style="width: 100px;margin-left:100px" id="submitForget">
                    </div>
                </div>
			</div>
		</div>	
    </body>
    <script data-main="js/lcims/forgetPwd" type="text/javascript" src="/js/require/require.js"></script>
</html>