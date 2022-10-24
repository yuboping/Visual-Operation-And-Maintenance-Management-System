<@bs.layout [
    "echarts/echarts.js",
    "lcims/hncu/loginlog.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css"

]  true true>

<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainhistory" >
        <div>
        <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">登陆时间:</label>
                            <div id="startDate_div" class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="startdate" name="startdate" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">退出时间:</label>
                            <div id="endDate_div" class="controls">
                                <input size="30" readonly="readonly" class=" input-medium" id="enddate" name="enddate" type="text">
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">管理员账号:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="adminAccount" name="adminAccount" type="text" maxlength="64">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">登陆源IP:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="loginIp" name="loginIp" type="text" maxlength="32">
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">事件发生主机名:</label>
                            <div class="controls">
                                <select class="input-medium" id="eventOccurHost" name="eventOccurHost">
                                    <option value="">全部</option>
                                    <#list allHostList as allHost>
                                        <option value="${allHost.addr}">${allHost.addr}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10" id="visible_div">
                        	<label class="control-label" style="padding-top: 10px;visibility:none;"></label>
                        	<div class="controls">
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
                             <input type="hidden" value="" id="page_curr"/>
                             <div id="operate_menu" class="fr"></div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:10%">事件发生主机名</th>
                                    <th style="width:15%">管理员账号</th>
                                    <th style="width:15%">登陆源IP</th>
                                    <th style="width:15%">登陆时间</th>
                                    <th style="width:15%">退出时间</th>
                                    <th style="width:15%">在线时间(分)</th>
                                    <th style="width:15%">是否成功登陆</th>
                                </tr>
                            </thead>
                            <tbody id="alarmhistorydiv"></tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                <!--搜索结果-END-->
                </div>
            </div>
        </div>
</div>
</@bs.layout>