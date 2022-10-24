<@bs.layout [
    "echarts/echarts.js",
    "lcims/report/reportpage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainreportpage" >
        <div>
            <div class="omc_main_tab" >
                <!--报表开始-->
                <div id="report_div" class="form-horizontal onlineTools">
                    <!--查询条件-->
                    <div class="row row-fluid">
                    	<div class="span6">
                    		<label class="control-label" style="padding-top: 0px;">报表类型</label>
	                    	<div class="controls" style="width: 20px;">
		                        <select id="reporttype"></select>
	                    	</div>
	                    	<input type="hidden" value="" id="reporttype_page"/>
                		</div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">日期:</label>
                            <div class="controls">
                                <div id = "reportdateinput"></div>
                                <input type="hidden" value="" id="reportdate_page"/>
                            </div>
                        </div>
	                    <div class="span4">
	                    	<a  id="report_querybutton" class="button button-primary">查询</a>
	                    	<a  id="report_resetbutton" class="button ml10">重置</a>
	                    </div>
	                </div>
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div class="fl"> 共查询到 <span id="report_querynum" class="text-danger"></span>条数据 | </div>
                                <div class="fl ml10">第 <span id="report_currnum" >1-10</span>条数据 </div>
                                <div id = "report_operate_menu" class="fr"></div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover center">
                            <thead id="report_reportpagethead"></thead>
                            <tbody id="report_reportpagetbody"></tbody>
                        </table>
                        <div id="report_pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                </div>
                <!--报表结束-->
            </div>
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>