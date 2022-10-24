<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/adminlog.js",
    "layer/layer.css",
    "bootstrap/bootstrap.min.css",
    "daterangepicker/daterangepicker.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainadminlog" >
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
                                <select class="input-medium" id="funcid" name="funcid" >
                                    <option value="">所有</option>
                                    <#list params as param>
                                        <option value=${param.code}>${param.description}</option>
                                    </#list>
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
                                <div class="fr">
                                   
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:10%">时间</th>
                                    <th style="width:15%">管理员</th>
                                    <th style="width:15%">角色</th>
                                    <th style="width:15%">属地</th>
                                    <th style="width:15%">登录IP</th>
                                    <th style="width:30%">操作内容</th>
                                </tr>
                            </thead>
                            <tbody id="adminlogdiv"></tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                </div>
            </div>
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>
