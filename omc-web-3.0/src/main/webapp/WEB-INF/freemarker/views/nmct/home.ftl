<@bs.layout [
	"echarts/echarts.js",
	"echarts3/echarts.min.js",
    "lcims/nmct/home.js",
    "echarts/echarts.css",
    "dpl.css",
    "global.css"
] true>

<!--内容区域-->
<!--omc_main-->
<div class="omc_main omc_city clearfix" >
	<input type="hidden" id="province" name="province" value="${province}"/>
	<div class="omc-scroll" id="homemain">
	<!--需要添加一层div,修复google浏览器首页滚动的时候显示问题-->
	<div>
		<div class="scroller-box clearfix">
		    <div class="omc_home_Big fl" >
		        <div class="home_bigsingle">
		          <div class="home_bigsingle" id="area1"></div>
		          <div class="home_smallsingle" id="area6"></div>
		        </div>
		        <div class="home_bigsingle2 mt10">
		        	<div class="home_bigsingle2" id="area2"></div>
				</div>
		    </div>
		    <div class="omc_home_small fl">
		        <div class="home_single">
		        	<div class="home_single" id="area3"></div>
				</div>
				<div class="home_single mt10">
		        	<div class="home_single" id="area4"></div>
				</div>
				<div class="home_single mt10">
		        	<div class="home_single" id="area5"></div>
				</div>
		    </div>
	    </div>
	</div>
	<div>
</div>
<!--end-omc_main-->
</@bs.layout>
