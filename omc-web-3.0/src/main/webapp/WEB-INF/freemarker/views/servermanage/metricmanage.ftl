<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/metricmanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainmetric" >
        <div>
        	<input type="hidden" value="${province}" id="provincename">
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">指标名称:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="metric_name" name="metric_name" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">指标类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="metric_type" name="metric_type" >
                                    <option value="">全部</option>
                                    <#list mdMetricTypeList as mdMetricType>
                                        <option value=${mdMetricType.id}>${mdMetricType.metric_type_name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
	                        <div class="span4">
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
                                <div id = "operate_menu" class="fr" >
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:6%">指标标识</th>
                                    <th style="width:6%">指标名称</th>
                                    <th style="width:6%">采集周期</th>
                                    <th style="width:20%">采集脚本</th>
                                    <th style="width:16%">脚本参数</th>
                                    <th style="width:10%">脚本返回类型</th>
                                    <th style="width:5%">指标类型</th>
                                    <#if province == "jscm">
                                    <th style="width:5%">服务类型</th>
                                    </#if>
                                    <th style="width:7%">指标描述</th>
                                    <th style="width:7%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="metricdiv"></tbody>
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
<!--弹层-新增-->
<div class="mt10 display_none" id="add_div">
    <div class="form-horizontal onlineTools">
        <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标标识:</td>
				<td style="padding-left:10px;"><input id="add_metric_identity" size="30" maxlength="30" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
				<td style="text-align:right;padding-left:10px;">指标名称:</td>
				<td style="padding-left:10px;"><input id="add_metric_name" size="50" maxlength="50" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集周期:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_cycle_id"></select> <span class="text-danger">*</span></td>
				<td style="text-align:right;padding-left:10px;">采集脚本:</td>
				<td style="padding-left:10px;"><input id="add_script" size="30" maxlength="100" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本参数:</td>
				<td style="padding-left:10px;" colspan="3"><input id="add_script_param" size="30" maxlength="256" style="width: 380px;" class="" type="text"> </td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本返回类型:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_script_return_type"></select> <span class="text-danger">*</span></td>
				<td style="text-align:right;padding-left:10px;">指标类型:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_metric_type"></select> <span class="text-danger">*</span></td>
			</tr>
			<#if province == "jscm">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">服务类型:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_server_type"></select></td>
			</tr>
			</#if>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标描述:</td>
				<td style="padding-left:10px;" colspan="3"><input id="add_description" size="50" maxlength="100" style="width: 380px;" class="" type="text"> </td>
			</tr>
       </table>
       <div class="row row-fluid mb10">
           <br/>
           <a href="#" id="add_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="add_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-新增-END-->

<!--弹层-修改-->
<div class="mt10 display_none" id="modify_div">
    <div class="form-horizontal onlineTools">
        <input id="modify_metricid" type="hidden">
        <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标标识:</td>
				<td style="padding-left:10px;"><input id="modify_metric_identity" size="30" maxlength="20" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
				<td style="text-align:right;padding-left:10px;">指标名称:</td>
				<td style="padding-left:10px;"><input id="modify_metric_name" size="30" maxlength="30" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集周期:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_cycle_id"></select> <span class="text-danger">*</span></td>
				<td style="text-align:right;padding-left:10px;">采集脚本:</td>
				<td style="padding-left:10px;"><input id="modify_script" size="30" maxlength="100" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本参数:</td>
				<td style="padding-left:10px;" colspan="3"><input id="modify_script_param" size="30" maxlength="256" style="width: 380px;" class="" type="text"> </td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本返回类型:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_script_return_type"></select> <span class="text-danger">*</span></td>
				<td style="text-align:right;padding-left:10px;">指标类型:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_metric_type"></select> <span class="text-danger">*</span></td>
			</tr>
			<#if province == "jscm">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">服务类型:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_server_type"></select></td>
			</tr>
			</#if>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标描述:</td>
				<td style="padding-left:10px;" colspan="3"><input id="modify_description" size="50" maxlength="100" style="width: 380px;" class="" type="text"> </td>
			</tr>
       </table>
       <div class="row row-fluid mb10">
           <br/>
           <a href="#" id="modify_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->

<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <input id="detail_metricid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标标识:</td>
				<td style="padding-left:10px;"><input id="detail_metric_identity" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">指标名称:</td>
				<td style="padding-left:10px;"><input id="detail_metric_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集周期:</td>
				<td style="padding-left:10px;"><input id="detail_cycle_id" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">采集脚本:</td>
				<td style="padding-left:10px;"><input id="detail_script" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本参数:</td>
				<td style="padding-left:10px;" colspan="3"><input id="detail_script_param" size="30" readonly="readonly" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本返回类型:</td>
				<td style="padding-left:10px;"><input id="detail_script_return_type" readonly="readonly" size="30" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">指标类型:</td>
				<td style="padding-left:10px;"><input id="detail_metric_type" readonly="readonly" size="30" style="width: 150px;" class="" type="text"></td>
			</tr>
			<#if province == "jscm">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">服务类型:</td>
				<td style="padding-left:10px;"><input id="detail_server_type" readonly="readonly" size="30" style="width: 150px;" class="" type="text"></td>
			</tr>
			</#if>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标描述:</td>
				<td style="padding-left:10px;" colspan="3"><textarea id="detail_description" readonly="readonly" size="50" style="width: 380px;resize:none;" class="" type="text"></textarea></td>
			</tr>
       </table>
       <div class="row row-fluid mb10">
           <br/>
           <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
       </div>
    </div>
</div>
<!--弹层-修改-END-->

</@bs.layout>