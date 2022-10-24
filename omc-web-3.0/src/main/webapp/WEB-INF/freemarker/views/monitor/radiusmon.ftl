<@bs.layout [
    "echarts/echarts.css",
    "bootstrap/bootstrap.min.css",
    "echarts3/echarts.min.js",
    "daterangepicker/daterangepicker.css",
    "jquery.mCustomScrollbar.css",
    "layer/layer.css",
    "scroll/jquery.mousewheel.min.js",
    "lcims/tool/iscroll.js",
    "lcims/monitor/radiusmon.js"
] true true>
<!--内容区域-->
<div class="omc_main" >
    <div id="mainarea">
        <div class="scroller-box">
        	<div class="omc_main_title">
        		<span style="font-size:15px;"><span class="c666"> ${titlename_1}</span><i class="iconfont icon-jiantouyou c666"></i>${titlename_2}
        		<#if titlename_3?exists>
        		  <i class="iconfont icon-jiantouyou c666"></i>${titlename_3}
        		</#if>
        		</span>
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
    </div>
</div>
<!--end 内容区域-->

<!--弹层-查看-->
<div id="layer_div" class="display_none">
	<a class="button  button-primary fr mr10" id="layer_but_serach">查&nbsp;&nbsp;询</a>
    <!-- <a class="button  button-primary fr mr10" id="layer_but_export">导&nbsp;&nbsp;出</a> -->
	<input type='hidden' id="layer_hidden_chartinfo"></input>
	
	<div class="form-horizontal onlineTools">
        <div class="row row-fluid">
            <div class="span10" style="width:25%;">
            	<div class="controls" style="margin-left:40px;">
            		<select class="input-medium" id="dataStyle" name="dataStyle" >
            			
            		</select>
            	</div>
            </div>
            <div id="start_div" class="span10" style="width:30%;">
                <label id="startDate_label" class="control-label" style="padding-top: 0px;">开始日期&nbsp;</label>
            	<div id="startDate_div" class="controls">
            		<input size="30" readonly="readonly" class=" input-medium" id="startdate" name="startdate" type="text">
            	</div>
            </div>
            <div id="end_div" class="span10" style="width:30%;">
                <label class="control-label" style="padding-top: 0px;">结束日期&nbsp;</label>
            	<div id="endDate_div" class="controls">
            		<input size="30" readonly="readonly" class=" input-medium" id="enddate" name="enddate" type="text">
            	</div>
            </div>
        </div>
    </div>
    <div id="layer_date_div">
	</div>
   <div>
   		<div id="layer_chartinfo_div"></div>
   </div>
</div>
<!--弹层-结束-->
</@bs.layout>
