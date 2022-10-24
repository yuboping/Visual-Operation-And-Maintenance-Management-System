<@bs.layout [
    "lcims/gdcu/queryOnlineUser.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainOnlineUser" >
        <div>
        <!--查询条件-->
        <div class="omc_main_tab" >
            <div class="form-horizontal onlineTools">
            <div class="row row-fluid">
                <div class="span20">
                	<!--<label class="control-label">用户</label>-->
                    <div class="controls">
                        <input size="16" class=" input-medium" id="queryvalue" type="text" style="padding-left: 70px; width: 300px;">
                        <div class="btn-select">
                            <select class="input-medium" id="querytype">
                                <option value="1">用户</option>
                                <option value="2">用户IP</option>
                            </select>
                        </div>
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
                </div>
                <table class="table table-bordered table-striped table-head-bordered table-hover center ">
                    <thead>
	                    <tr>
	                        <th></th>
	                        <th>用户账号</th>
	                        <th width="100">上线时间</th>
	                        <th width="100">上次活跃时间</th>
	                        <th>在线时长</th>
	                        <th>session状态</th>
	                        <th>接入设备IP</th>
	                        <th>用户IP</th>
	                        <th>用户mac</th>
	                        <th width="140">在线会话ID</th>
	                        <th>接入端口信息</th>
	                    </tr>
                    </thead>
                    <tbody id="queryOnlineUser" > </tbody>
                </table>
                <div class="mb10 mt10 text-center clearfix">
	                <div >
	                    <a href="#batch" id="batchbutton" class="button button-small button-primary">踢用户下线</a>
	                    <a href="#delete" id="deletebutton" class="button button-small button-primary">删除在线信息</a>
	                </div>
                </div>
                <div id="pageinfo" class="parts_down_page clearfix">
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