<@bs.layout [
	"echarts/echarts.js",
	"echarts3/echarts.min.js",
    "echarts/echarts.css",
    "lcims/gscm5G/layui/layui.all.js",
    "lcims/gscm5G/home.js",
    "lcims/gscm5G/jtopo.js",
     "dpl.css",
    "global.css", 
    "gscm5G/layui/css/layui.css",
    "gscm5G/iconfont/iconfont.css",
    "gscm5G/global.css"
] true>
<div class="main clearfix" style="width: 100%;height: 100%;">
    <div class="w65 fl">
        <div class="mian_middle">
            <div id="serverwidth" class="block" style="margin-left: 100px;width: 85%;height:600px">
                <div class="block_title_gscm5G">服务器列表</div>
                <div class="ltalics_bg"><img src="/images/gscm5G/1.png"></div>
                <!-- border:1px solid #444; -->
                <!--<canvas width="100%" height="780px" id="canvas" style="margin-top: 10px;"> </canvas>-->
                <img width="100%" height="570px" src="/images/${province}/index_host.png">
                <div class="top_left"></div>
                <div class="top_right"></div>
                <div class="bottom_left"></div>
                <div class="bottom_right"></div>
                
            </div>

        </div>
    </div>
    <div class="w35 fl">
     	<div class="block" style="margin-right: 100px;">
            <div class="block_title_gscm5G" >设备状态</div>
            <div class="clearfix">
                <div class="purple fl"  ><p id="hostnumber" class=" timer" data-to="123" data-speed="1500" style="color:#ef6a8b;"></p>设备总数</div>
                <div class="purple fl"><p style="color: #44b0e2; " id="alarmhost" class=" timer" data-to="123" data-speed="1500">0</p>故障数</div>
                <!-- <div class="purple fl"><p style="color: #6f85fe; " class=" timer" data-to="123" data-speed="1500">42</p>离线数</div> -->
            </div>
            <div class="ltalics_bg"><img src="/images/gscm5G/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div>
        <div class="block" style="margin-right: 100px;">
            <div class="block_title_gscm5G">告警数</div>
            <div class="circle_box clearfix ">
                <div class="circle fl" style="width: 33%;"><div id="alarmlevel3" class="nub timer" data-to="123" data-speed="1500" >0</div>严重</div>
                <div class="circle  fl cc" style="width: 33%;"><div id="alarmlevel2" class="nub timer" data-to="123" data-speed="1500" >0</div>警告</div>
                <div class="circle  fl " style="width: 33%;"><div id="alarmlevel1" class="nub timer" data-to="123" data-speed="1500" >0</div>普通</div>
            </div>
            <div class="ltalics_bg"><img src="/images/gscm5G/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div>
        <div class="block" style="margin-right: 100px;">
            <div class="block_title_gscm5G">告警信息列表</div>
           <div style="height: 220px;padding:0px 5px;overflow: auto;;" id="alarmlist">
               <table class="main_tab" id="maintable" cellpadding="0" cellspacing="0" style="font-size:16px">
               </table>
           </div>
            <div class="ltalics_bg"><img src="/images/gscm5G/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div>
       <!--  <div class="block">
            <div class="block_title_gscm5G">告警趋势图</div>
            <div id="main4" style="height: 238px; " ></div>
            <div class="ltalics_bg"><img src="/images/gscm5G/1.png"></div>
            <div class="top_left"></div>
            <div class="top_right"></div>
            <div class="bottom_left"></div>
            <div class="bottom_right"></div>
        </div> -->
    </div>
</div>

<!--end-omc_main-->
</@bs.layout>