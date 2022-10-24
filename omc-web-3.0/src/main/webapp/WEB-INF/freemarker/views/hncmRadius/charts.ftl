<@bs.layout [
"echarts/echarts.css",
"bootstrap/bootstrap.min.css",
"echarts3/echarts.min.js",
"daterangepicker/daterangepicker.css",
"jquery.mCustomScrollbar.css",
"layer/layer.css",
"scroll/jquery.mousewheel.min.js",
"lcims/tool/iscroll.js",
"lcims/radius/radius.js"
] true true>
    <!--内容区域-->
    <div class="omc_main" >
        <div id="mainarea">
            <input type='hidden' value=${host_id} name="hostId" id="hostId" />
            <div class="scroller-box">
                <div class="omc_main_title">
                    <span style="font-size:15px;"><span class="c666"> ${titlename_1}</span><i class="iconfont icon-jiantouyou c666"></i>${titlename_2}</span>
                </div>
                <div class="omc_main_tab clearfix">
                    <#if chartDetailList?exists>
                        <!--业务情况-->
                        <#list chartDetailList as info>
                            <div class="half_page fl ">
                                <div id="serviceinfo_${info.chart_name}" name="serviceinfo" class="omc_city_tu boxh265">
                                    <input type='hidden' value=${info} name="chart_in_maindiv" id="chart_in_maindiv_${info_index}"></input>
                                    <@bs.metricChart info.getChart_title() info.getChart_name() "dap-monitor-chartCon" info.isAlarm() info_index info.getAlarmmsg()></@bs.metricChart>
                                </div>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div style="margin-left: 200px;">
                <div id="chartArea" style="width: 99%;"></div>
            </div>
        </div>
    </div>
    <!--end 内容区域-->
</@bs.layout>
