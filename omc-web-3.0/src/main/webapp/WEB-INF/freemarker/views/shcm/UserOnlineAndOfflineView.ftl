<@bs.layout [
    "lcims/shcm/UserOnlineAndOfflineView.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css"
]  true true>

<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainhistory" >
        <div class="omc_main_tab" style="height:770px">
        	<!-- 查询条件 -->
	        <div class="form-horizontal onlineTools">
	               <div class="row row-fluid">
	                   <div class="span10">
	                       <label class="control-label" style="padding-top: 0px;">查询类型:</label>
	                       <div class="controls">
	                           <select class="input-medium" id="query_type">
	                           	    <option value="1">按天</option>
	                                <option value="2">按月</option>
	                           </select>
	                       </div>
	                   </div>
	                   <div class="span10">
	                       <label class="control-label" style="padding-top: 0px;">日期:</label>
	                       <div id="startDate_div" class="controls">
	                           <input type="text" class="layui-input input-medium" id="timeCheck" placeholder=" - " style="display:none">
	                           <input type="text" class="layui-input input-medium" id="monthCheck" placeholder=" - " style="display:none">
	                       </div>
	                   </div>
	                   <div class="span4">
	                       <a href="#" id="querybutton" class="button button-primary">查询</a>
	                       
	                   </div>
	               </div>
	           </div>
	       
	        <!-- 表格展示 -->
	        <div class="omc_table_box">
                    <div class="mb10 clearfix">
                         <div class="mb10 clearfix">
                             <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span> 条数据</div>
                             <!-- <div class="fl ml10">第 <span id="currnum" >1-10</span>条数据 </div> -->
                             <div id="operate_menu" class="fr">
                            <!--  <a href="#" id="showchartdiv" style="margin-right:5px" class="button button-primary">显示图表</a> -->
                             </div>
                             
                        </div>
                        <div style="max-height:270px;overflow:auto" id="showtablediv">
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:25%">时间</th>
                                    <th style="width:50%">下线原因</th>
                                    <th style="width:25%">次数</th>
                                </tr>
                            </thead>
                            <tbody id="alarmhistorydiv"></tbody>
                        </table>
                        </div>
                        <div id="pageinfo" class="fr parts_down_page clearfix">
                        </div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                <!--搜索结果-END-->
             </div>
              <!--图表展示-->
			<div  style="width:100%;height:280px;margin-right:10px;padding:15px 0;padding-right:9px;" id="chartdiv">
				<div style="width:100%;height:100%;background: #fff;border:1px solid #ebebea;" id="userChartShow">
				
				</div>
			</div>
        </div>
  </div>
</div>
<!--end-main-->
</@bs.layout>
