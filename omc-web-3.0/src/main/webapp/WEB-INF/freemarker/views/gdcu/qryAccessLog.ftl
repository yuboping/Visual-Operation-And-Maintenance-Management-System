<@bs.layout [
    "lcims/gdcu/qryAccessLog.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css",
    "daterangepicker/daterangepicker.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainqryAccessLog" >
        <div>
        <!--查询条件-->
        <div class="omc_main_tab" >
           <div class="form-horizontal onlineTools">
           <div class="row row-fluid">
	            <div class="span10">
	                <label class="control-label">用户账号</label>
                    <div class="controls">
                        <input size="16" id="account" class="input-medium" type="text">
                    </div>
	            </div>
	            <div class="span10">
	                <label class="control-label" style="padding-top: 0px;">查询日期</label>
	            	<div id="startDate_div" class="controls">
	            	   <select id="queryaccType" style="width: 85px;">
                            <option value="1">精确:天</option>
                            <option value="2">精确:分钟</option>
                        </select>
                        
	            	</div>
           		</div>
                <div class="span4">
                    <a href="#" id="querybutton" class="button button-primary">查询</a>
                    <a href="#" id="resetbutton" class="button ml10">重置</a>
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
	                        <th>用户名</th>
	                        <th>域名</th>
	                        <th width="85">接入时间</th>
	                        <th>服务类型</th>
	                        <th>包类型</th>
	                        <th width="160">认证结果</th>
	                        <th width="100">NAS_IP</th>
	                        <th>NAS_PORT</th>
	                        <th>端口类型</th>
	                        <th>用户IP</th>
	                        <th>Mac地址</th>
	                        <th>Callee_id</th>
	                        <th>Radius来源</th>
	                    </tr>
                    </thead>
                   <tbody id="qryAccessLogdiv" ></tbody>
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