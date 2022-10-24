<@bs.useCSS ["dpl.css","global.css","iconfont/iconfont.css","laypage/laypage.css"]/>
<!--头部-->

<!--top menu   style="background-color:#203E7C"-->
<div class="omc_wrap clearfix" style="background-color:#203E7C">
	<div class="omc_header">
		<div class="omc_w clearfix">
			<div class="omc_logo fl">
				<img src="/images/${province}/logo.png">
				<span class="logo_name">${systemname}</span>
			</div>
			<div class="header_menu fl">
                <ul id="firstLevelMenu">
                </ul>
            </div>
            <div class="omc_user fr">
                <div class="user-img">
                    <p>
                        <#if province == "gdcu11">
                            <span id="show"></span>
                         </#if>
                    	<i class="iconfont icon-yonghu"></i>
                    	<span>${Session["username"]}</span>
                    	<a href="#" data-toggle="message" id="alarmmessage_but" >
                    		<i class="iconfont icon-lingdanghover"></i>
                    		消息<span class="user-messages" id="alarm_message_num"></span>
                		</a>
                    	<a href="/logout" class="border-none">
                    		<i class="iconfont icon-tuichu"></i>
                    		退出
                		</a>
                    </p>
                </div>
            </div>
		</div>
  	</div>
</div>
<!--end-top menu-->
<input type="hidden" id="alarmsound" value="${alarmsound}"  />
<input type="hidden" id="alarm_sound_type" value="${alarm_sound_type}"  />

<#if alarmsound == "true">
<audio id="alarm_music" preload="preload" >
   <source src="/resources/music/${alarmsoundname}" type="audio/mpeg">
</audio>
</#if>

<!--clickalarmmessage-->
<div class="message_outer"  id="alarmmessage">
    <div class="message_list">
        <div id="messageMain" class="omc-scroll">
        	<div>
        		<p class="menu_secttl">消息<a href="#" class="dap-close" data-toggle="message">&times;</a></p>
       			<ul></ul>
   			</div>
        </div>
    </div>
</div>
<!--end clickalarmmessage -->

<!--实时告警-->
<ul class="omc_info_container" id="info-container"></ul>
<!--实时告警END-->

<!--需要在页面最后加载JS-->
<@bs.useJS ["echarts/echarts.js","lcims/topmenu.js"]/>