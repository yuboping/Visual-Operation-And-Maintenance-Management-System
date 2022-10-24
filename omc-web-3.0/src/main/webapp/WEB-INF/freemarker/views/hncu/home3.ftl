<@bs.layout [
	"echarts/echarts.js",
	"echarts3/echarts.min.js",
    "echarts/echarts.css",
    "lcims/jscm/layui/layui.all.js",
    "lcims/jscm/home.js",
    "lcims/jscm/jtopo.js",
     "dpl.css",
    "global.css", 
    "jscm/layui/css/layui.css",
    "jscm/iconfont/iconfont.css",
    "jscm/global.css"
] true>
<div class="main clearfix" style="background: #050a32 url(/images/jscm/bg.png) no-repeat center;width: 100%;height: 850px;">
    <div class="w75 fl">
        <div class="mian_middle">
            <div id="serverwidth" class="block" style="height:828px">
                <div class="block_title">服务器列表</div>
                <div class="ltalics_bg"><img src="/images/jscm/1.png"></div>
                <!-- border:1px solid #444; -->
                <canvas width="100%" height="780px" id="canvas" style="margin-top: 10px;">
                </canvas>
                <div class="top_left"></div>
                <div class="top_right"></div>
                <div class="bottom_left"></div>
                <div class="bottom_right"></div>
                
            </div>

        </div>
    </div>
    <div class="w25 fl">
     	<div class="block">
            <div class="block_title">设备状态</div>
            <div class="clearfix">
                <div class="purple fl"  ><p id="hostnumber" class=" timer" data-to="123" data-speed="1500" style="color:#ef6a8b;"></p>设备总数</div>
                <div class="purple fl"><p style="color: #44b0e2; " id="alarmhost" class=" timer" data-to="123" data-speed="1500">0</p>故障数</div>
                <!-- <div class="purple fl"><p style="color: #6f85fe; " class=" timer" data-to="123" data-speed="1500">42</p>离线数</div> -->
            </div>
            <div class="ltalics_bg"><img src="/images/jscm/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div>
        <div class="block">
            <div class="block_title">告警数</div>
            <div class="circle_box clearfix ">
                <div class="circle fl" style="width: 33%;"><div id="alarmlevel3" class="nub timer" data-to="123" data-speed="1500" >22</div>严重</div>
                <div class="circle  fl cc" style="width: 33%;"><div id="alarmlevel2" class="nub timer" data-to="123" data-speed="1500" >22</div>警告</div>
                <div class="circle  fl " style="width: 33%;"><div id="alarmlevel1" class="nub timer" data-to="123" data-speed="1500" >22</div>普通</div>
            </div>
            <div class="ltalics_bg"><img src="/images/jscm/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div>
        <div class="block">
            <div class="block_title">告警信息列表</div>
           <div style="height: 448px;padding:0px 5px;overflow: auto;;" id="alarmlist">
               <table class="main_tab" id="maintable" cellpadding="0" cellspacing="0" style="font-size:16px">
               </table>
           </div>
            <div class="ltalics_bg"><img src="/images/jscm/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div>
       <!--  <div class="block">
            <div class="block_title">告警趋势图</div>
            <div id="main4" style="height: 238px; " ></div>
            <div class="ltalics_bg"><img src="/images/jscm/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div> -->
    </div>
</div>

<!--end-omc_main-->
</@bs.layout>