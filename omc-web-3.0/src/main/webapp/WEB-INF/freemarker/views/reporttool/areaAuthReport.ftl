<@bs.layout [
    "echarts/echarts.js",
    "lcims/reporttool/areaAuthReport.js",
    "mainttool.css",
    "layer/layer.css",
    "laypage/laypage.css",
    "bootstrap/bootstrap.min.css",
    "daterangepicker/daterangepicker.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainqryRadiusAreaAuth" >
        <div>
        <!--查询条件-->
        <div class="omc_main_tab" >
           <div class="form-horizontal onlineTools">
           <input type='hidden' value=${reportId} name="reportId" id="reportId"></input>
           <div class="row row-fluid">
           		<div class="span6">
                    <label class="control-label" style="padding-top: 0px;">报表类型</label>
                    <div class="controls" style="width: 20px;">
                        <select id="querytype">
	                        <option value="1">日报</option>
	                	 	<option value="2">月报</option>
                        </select>
                    </div>
                </div>
	            <div class="span10" id="day_div">
	                <label class="control-label" style="padding-top: 0px;">查询日期</label>
	            	<div id="queryDate_div" class="controls">
	            		<input size="30" readonly="readonly" class=" input-medium" id="querydate" name="querydate" type="text">
	            	</div>
	            </div>
	            <div class="span10" id="month_div">
	                <label class="control-label" style="padding-top: 0px;">年月</label>
	            	<div id="queryDate_div" class="controls">
	            		<select id="queryYear" style="width: 70px;">
	                        
                        </select>
                        <select id="queryMonth" style="width: 70px;">
	                        <option value="01">1月</option>
	                	 	<option value="02">2月</option>
	                	 	<option value="03">3月</option>
	                	 	<option value="04">4月</option>
	                	 	<option value="05">5月</option>
	                	 	<option value="06">6月</option>
	                	 	<option value="07">7月</option>
	                	 	<option value="08">8月</option>
	                	 	<option value="09">9月</option>
	                	 	<option value="10">10月</option>
	                	 	<option value="11">11月</option>
	                	 	<option value="12">12月</option>
                	 	</select>
	            	</div>
	            </div>
                <div class="span6">
                    <a href="#" id="querybutton" class="button button-primary">查询</a>
                    <a href="#" id="resetbutton" class="button ml10">重置</a>
                    <a href="#" id="exportbutton" class="button ml10">导出</a>
                </div>
        	</div>

        	</div>
			<!--查询条件-END-->
            
            <!--搜索结果-->
            <div class="omc_table_box">
                <div class="mb10 clearfix">
                    <div class="mb10 clearfix">
                    	<div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span>条数据 | </div>
                        <div class="fl ml10">第 <span id="currnum" >1-10</span>条数据 </div>
                     </div>
                </div>
                <table class="table table-bordered table-striped table-head-bordered table-hover center " style="table-layout: fixed;">
                    <thead>
	                    <tr>
	                        <th>地市名称</th>
	                        <th width="85">认证量</th>
	                        <th>认证成功量</th>
	                        <th>计费开始包数</th>
	                        <th>计费中间包数</th>
	                        <th>计费结束包数</th>
	                        <th>未知请求量</th>
	                        <th>请求总包数</th>
	                        <th>在线用户数量</th>
	                        <th>认证成功率(%)</th>
	                    </tr>
                    </thead>
                   <tbody id="qryRadiusAreaAuth" ></tbody>
                </table>
                <div id="pageinfo" class="parts_down_page clearfix">
                    <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                    <div class="ad-page-outer clearfix "></div>
                </div>
                <!--搜索结果-END-->
            <div/>
            </div>
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>