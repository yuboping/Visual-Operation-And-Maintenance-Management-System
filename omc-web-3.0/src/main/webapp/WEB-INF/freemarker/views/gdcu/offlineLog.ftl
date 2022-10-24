<@bs.layout [
    "echarts/echarts.js",
    "lcims/gdcu/offlineLog.js",
    "mainttool.css",
    "layer/layer.css",
    "laypage/laypage.css",
    "bootstrap/bootstrap.min.css",
    "daterangepicker/daterangepicker.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="offlineLogmain" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">开始日期</label>
                        	<div id="startDate_div" class="controls">
                        		<input size="30" readonly="readonly" class=" input-medium" id="startdate" name="startdate" type="text">
                        	</div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">结束日期</label>
                        	<div id="endDate_div" class="controls">
                        		<input size="30" readonly="readonly" class=" input-medium" id="enddate" name="enddate" type="text">
                        	</div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                    	<div class="span10">
                            <label class="control-label" style="padding-top: 0px;">登录名</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="admin" name="admin" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">操作内容</label>
                            <div class="controls">
                                <select class="input-medium" id="opttype" name="opttype" >
                                    <option value="">所有</option>
                                    <option value='DumpUserbyIp'>批量踢用户下线</option>
                                    <option value='KickLMUser'>批量删除在线用户</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                    	<div class="span20">
                            <label class="control-label" style="padding-top: 0px;">Brasip</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="brasip" name="brasip" type="text">
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
                            <div class="fr">
                               
                            </div>
                         </div>
	                </div>
	                <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
	                    <thead>
	                    <tr>
	                        <th>操作时间</th>
	                        <th>管理员</th>
	                        <th>角色</th>
	                        <th>属地</th>
	                        <th>登录IP</th>
	                        <th>Brasip</th>
	                        <th>批量下线原因</th>
	                        <th>操作内容</th>
	                    </tr>
	                    </thead>
	                    <tbody id="offlineLogmaindiv" >
	                    </tbody>
	                </table>
	                <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                    <div class="ad-page-outer clearfix "></div>    
	                </div>
	            </div>
	            <!--搜索结果-END-->
            </div>
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>