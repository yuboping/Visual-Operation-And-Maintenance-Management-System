<@bs.layout [
    "dpl.css",
    "global.css"
] true>

<!--内容区域-->
<div class="omc_main omc_city  clearfix">
	<div class="omc-scroll" id="busmodulediv" >
		<div class="scroller-box clearfix">
			<#list mdMenuList as mdMenu>
				<div class="omc_city_single fadescale01 fl" onclick="javascript:window.location.href='${mdMenu.url}';">
					<div class="omc_city_box">
						<div class="omc_city_icon">
							<i class="iconfont ${mdMenu.largeicon}"></i>
							<#if mdMenu.alarmcount &gt; 0 >
								<div class="omc_city_messgae"></div>
							</#if>
						</div>
						<div class="omc_city_iconname mt10"><span>${mdMenu.show_name}</span></div>
					</div>
				</div>
			</#list>
		</div>
	</div>
</div>
</@bs.layout>

<#-- 需要把JS放在下面,滚动条样式修改的function需要在页面渲染之后调用 -->
<@bs.useJS ["echarts/echarts.js","lcims/monitor/busmodule.js"]/>
