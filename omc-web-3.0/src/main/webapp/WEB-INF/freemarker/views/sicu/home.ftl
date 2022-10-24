<@bs.layout [
    "jquery/jquery.js",
    "echarts/echarts.css",
    "bootstrap/bootstrap.min.css",
    "jquery.mCustomScrollbar.css",
    "layer/layer.css",
    "lcims/home/jquery.mCustomScrollbar.concat.min.js",
    "scroll/jquery.mousewheel.min.js",
    "lcims/sicu/home.js"
] true>

<!--内容区域-->
<!--omc_main-->
<div class="omc_main omc_city  clearfix ">
    <!-- 
    <div class="omc-scroll" id="homemain">
    <div>
        <div class="scroller-box clearfix">
    -->
            <div class="home_new">
                <p class="title">主机监控报表<i class="iconfont icon-iconfontyuan"></i><span class="f14"> 每5分钟更新数据</span></p>
                <div class="home_new_tab">
                    <table cellpadding="0" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>主机名</th>
                            <th>主机IP</th>
                            <th>主机类型</th>
                            <th>节点</th>
                            <th>CPU利用率(%)</th>
                            <th>内存使用率(%)</th>
                            <th>连通性</th>
                            <th>进程状态</th>
                        </tr>
                        </thead>
                        <tbody id="hostperformance">
                        
                        </tbody>
                    </table>
                </div>
        
                <p class="title mt20">告警事件报表<i class="iconfont icon-iconfontyuan"></i><span class="f14"> 每5分钟更新数据</span></p>
                <div class="home_new_tab">
                    <table cellpadding="0" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>监控目标</th>
                            <th>指标</th>
                            <th>告警级别</th>
                            <th>告警信息</th>
                            <th>告警时间</th>
                            <!-- <th>确认状态</th> -->
                            <th>确认状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="alarminfoData">
                        
                        </tbody>
                        
                    </table>
                </div>
            </div>
            
          <!--   
           </div>
         </div>
       </div>
       -->
</div>

<!--end-omc_main-->
</@bs.layout>
