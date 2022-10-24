<@bs.layout [
    "echarts/echarts.js",
    "lcims/mainttool/hostresourcesstatistics.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainhostresourcesstatistics" >
        <div>
        	
            <div class="omc_main_tab" >
            	<div class="form-horizontal onlineTools">
                    <div class=" mb10  clearfix">
                        <div class="tag_box fl"  id="report_menu">
                        </div>
                </div>
                
                
                <!--日报表开始-->
                <div id="dayreport_div" class="form-horizontal onlineTools hide">
                    <!--查询条件-->
                    <div class="row row-fluid">
                    	<div class="span10">
                            <label class="control-label">主机IP:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="dayreportaddr" name="dayreportaddr" type="text" maxlength="20">
                                <input type="hidden" value="" id="dayreportaddr_page"/>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">日期:</label>
                            <div class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="dayreportdate" name="dayreportdate" type="text">
                                <input type="hidden" value="" id="dayreportdate_page"/>
                            </div>
                        </div>
	                    <div class="span4">
	                    	<a  id="dayreport_querybutton" class="button button-primary">查询</a>
	                    	<a  id="dayreport_resetbutton" class="button ml10">重置</a>
	                    </div>
	                </div>
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div class="fl"> 共查询到 <span id="dayreport_querynum" class="text-danger"></span>条数据 | </div>
                                <div class="fl ml10">第 <span id="dayreport_currnum" >1-10</span>条数据 </div>
                                <div id = "dayreport_operate_menu" class="fr" >
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:20%">时间</th>
                                    <th style="width:20%">cpu占用率</th>
                                    <th style="width:20%">内存占用率</th>
                                </tr>
                            </thead>
                            <tbody id="dayreport_hostresourcesstatisticsdiv"></tbody>
                        </table>
                        <div id="dayreport_pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                </div>
                <!--日报表结束-->
                
                <!--周报表开始-->
                <div id="weekreport_div" class="form-horizontal onlineTools hide">
                    <!--查询条件-->
                    <div class="row row-fluid">
                    	<div class="span10">
                            <label class="control-label">主机IP:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="weekreportaddr" name="weekreportaddr" type="text" maxlength="20">
                                <input type="hidden" value="" id="weekreportaddr_page"/>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">日期:</label>
                            <div class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="weekreportdate" name="weekreportdate" type="text">
                                <input type="hidden" value="" id="weekreportdate_page"/>
                            </div>
                        </div>
	                    <div class="span4">
	                    	<a  id="weekreport_querybutton" class="button button-primary">查询</a>
	                    	<a  id="weekreport_resetbutton" class="button ml10">重置</a>
	                    </div>
	                </div>
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div class="fl"> 共查询到 <span id="weekreport_querynum" class="text-danger"></span>条数据 | </div>
                                <div class="fl ml10">第 <span id="weekreport_currnum" >1-10</span>条数据 </div>
                                <div id = "weekreport_operate_menu" class="fr" >
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:10%">设备名称</th>
                                    <th style="width:10%">IP</th>
                                    <th style="width:10%">cpu占用率(平均)</th>
                                    <th style="width:10%">cpu占用率(最小)</th>
                                    <th style="width:10%">cpu占用率(最大)</th>
                                    <th style="width:10%">内存占用率(平均)</th>
                                    <th style="width:10%">内存占用率(最小)</th>
                                    <th style="width:10%">内存占用率(最大)</th>
                                </tr>
                            </thead>
                            <tbody id="weekreport_hostresourcesstatisticsdiv"></tbody>
                        </table>
                        <div id="weekreport_pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                </div>
                <!--周报表结束-->
                
                <!--月报表开始-->
                <div id="monthreport_div" class="form-horizontal onlineTools hide">
                    <!--查询条件-->
                    <div class="row row-fluid">
                    	<div class="span10">
                            <label class="control-label">主机IP:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="monthreportaddr" name="monthreportaddr" type="text" maxlength="20">
                                <input type="hidden" value="" id="monthreportaddr_page"/>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">日期:</label>
                            <div class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="monthreportdate" name="monthreportdate" type="text">
                                <input type="hidden" value="" id="monthreportdate_page"/>
                            </div>
                        </div>
	                    <div class="span4">
	                    	<a  id="monthreport_querybutton" class="button button-primary">查询</a>
	                    	<a  id="monthreport_resetbutton" class="button ml10">重置</a>
	                    </div>
	                </div>
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div class="fl"> 共查询到 <span id="monthreport_querynum" class="text-danger"></span>条数据 | </div>
                                <div class="fl ml10">第 <span id="monthreport_currnum" >1-10</span>条数据 </div>
                                <div id = "monthreport_operate_menu" class="fr" >
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:10%">设备名称</th>
                                    <th style="width:10%">IP</th>
                                    <th style="width:10%">cpu占用率(平均)</th>
                                    <th style="width:10%">cpu占用率(最小)</th>
                                    <th style="width:10%">cpu占用率(最大)</th>
                                    <th style="width:10%">内存占用率(平均)</th>
                                    <th style="width:10%">内存占用率(最小)</th>
                                    <th style="width:10%">内存占用率(最大)</th>
                                </tr>
                            </thead>
                            <tbody id="monthreport_hostresourcesstatisticsdiv"></tbody>
                        </table>
                        <div id="monthreport_pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                </div>
                <!--月报表结束-->
                
            </div>
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>