<@bs.layout [
    "lcims/analogdialup/analogdialupresultmanage.js",
    "layer/layer.css",
    "ais/icon-style.css",
    "ais/ais.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainanalogdialup" >
       <div>
            <div class="omc_main_tab" >
                <!--查询条件-->
                    <div class="form-horizontal onlineTools">
	                    <div class="row row-fluid">
	                        <div class="span10">
	                            <label class="control-label" style="padding-top: 0px;">开始日期:</label>
	                            <div id="startDate_div" class="controls">
	                                <input size="30" readonly="readonly" class="input-medium" id="startdate" name="startdate" type="text">
	                            </div>
	                        </div>
	                        <div class="span10">
	                            <label class="control-label" style="padding-top: 0px;">结束日期:</label>
	                            <div id="endDate_div" class="controls">
	                                <input size="30" readonly="readonly" class=" input-medium" id="enddate" name="enddate" type="text">
	                            </div>
	                        </div>
	                    </div>
	                    <div class="row row-fluid">
	                        <div class="span10">
	                            <label class="control-label">主机名:</label>
	                            <div class="controls">
	                                <input size="30" class=" input-medium" id="host_name" name="host_name" type="text" maxlength="20">
	                            </div>
	                        </div>
	                        <div class="span10">
	                            <label class="control-label">主机IP:</label>
	                            <div class="controls">
	                                <input size="30" class=" input-medium" id="host_ip" name="host_ip" type="text" maxlength="20">
	                            </div>
	                        </div>
	                    </div>
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">账号名称:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="username" name="username" type="text">
                                </div>
                            </div>
                            <div class="span10">
                                <label class="control-label">执行结果:</label>
                                <select class="input-medium" style = "float: left; margin-left: 10px;" id="returncode"" >
				            		<#list mapList as map>
				            			<option value=${map.CODE}>${map.DESCRIPTION}</option>
				                    </#list>
                 				</select>
                            </div>
                            <div class="span4" style="margin-left: 530px;">
                                <a  id="querybutton" class="button button-primary">查询</a>
                                <a  id="resetbutton" class="button ml10">重置</a>
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
                            <div id = "operate_menu" class="fr" ></div>
                         </div>
                    </div>
                    <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                        <thead>
                            <tr>
                                <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                <th style="width:15%">主机名</th>
                                <th style="width:10%">主机IP</th>
                                <th style="width:15%">账号名称</th>
                                <th style="width:15%">拨测时间</th>
                                <th style="width:10%">执行结果</th>
                                <th style="width:20%">执行结果详情</th>
                                <th style="width:10%">操作</th>
                            </tr>
                        </thead>
                        <tbody id="analogdialupdiv"></tbody>
                    </table>
                    <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                    <div class="ad-page-outer clearfix "></div>
                </div>
                <!--搜索结果-END-->
            </div>
        </div>
    </div>
</div>
<!--end-main-->

<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <input id="detail_metricid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">主机名:</td>
				<td style="padding-left:10px;"><input id="detail_host_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">主机IP:</td>
				<td style="padding-left:10px;"><input id="detail_host_ip" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">拨测频率:</td>
				<td style="padding-left:10px;"><input id="detail_min_rate_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">账号名称:</td>
				<td style="padding-left:10px;"><input id="detail_username" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">接入服务器端口:</td>
				<td style="padding-left:10px;"><input id="detail_nas_port" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">主叫号码:</td>
				<td style="padding-left:10px;"><input id="detail_call_from_id" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">被叫号码:</td>
				<td style="padding-left:10px;"><input id="detail_call_to_id" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">扩展:</td>
				<td style="padding-left:10px;"><input id="detail_ext" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">命令名:</td>
				<td style="padding-left:10px;"><input id="detail_command_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">流水号:</td>
				<td style="padding-left:10px;"><input id="detail_serialno" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">返回码:</td>
				<td style="padding-left:10px;"><input id="detail_returncode" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">描述信息:</td>
				<td style="padding-left:10px;"><input id="detail_errordesc" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">执行结果:</td>
				<td style="padding-left:10px;"><input id="detail_returncode_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">拨测耗时(秒):</td>
				<td style="padding-left:10px;"><input id="detail_calltime" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
       </table>
       <div class="row row-fluid mb10">
           <br/>
           <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
       </div>
    </div>
</div>
<!--弹层-详情-END-->
</@bs.layout>