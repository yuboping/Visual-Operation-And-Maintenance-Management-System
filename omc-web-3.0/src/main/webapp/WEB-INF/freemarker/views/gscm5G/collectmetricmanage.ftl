<@bs.layout [
    "echarts/echarts.js",
    "lcims/gscm5G/collectmetric.js",
    "layer/layer.css",
    "dpl.css"
]  true true>

<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="collectmetricmanage" >
	   <input type="hidden" value="${operateid}" id="operateid" />
	   <input type="hidden" value="${operatetype}" id="operatetype"/>
       <div>
            <div class="omc_main_tab" >
            <!--查询条件-->
            <div class="form-horizontal onlineTools">
            	<div class=" mb10  clearfix">
                        <div class="tag_box fl" id="operate_menu_1">
                        </div>
                    </div>
            
             <div id="query_div" class="form-horizontal onlineTools display_none">
            	<div class=" mb10  clearfix">
                   <div class="tag_box fl">
                        <div class="span10">
                            <label class="control-label">主机:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_hostid" name="query_hostid">
                                    <option value="">全部</option>
                                    <#list hostlist as host>
                                    	<option value="${host.hostid}">${host.hostname}</option>
                                    </#list>
                                </select>
                                <input type="hidden" value="" id="query_hostid_page"/>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_metricid" name="query_metricid">
                                    <#list metriclist as metric>
                                    	<option value="${metric.id}">${metric.metric_name}</option>
                                    </#list>
                                </select>
                                <input type="hidden" value="" id="query_metricid_page"/>
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
                                <div id="operate_menu_2" class="fr">
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:15%">主机名称</th>
                                    <th style="width:10%">ip</th>
                                    <th style="width:10%">指标</th>
                                    <th style="width:10%">采集周期</th>
                                    <th style="width:30%">脚本</th>
                                    <th style="width:8%">状态</th>
                                    <th style="width:8%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="hostmetricdiv">
                            </tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                 <div/>
            </div>
        </div>
        
        <div id="bind_div" class="form-horizontal onlineTools">
        	
            <div class=" mb10  clearfix">
            	<!--查询条件-START-->
                   <div class="tag_box fl">
                        <div class="span10">
                            <label class="control-label">主机:</label>
                            <div class="controls">
                                <select class="input-medium" id="bind_operateid" name="bind_operateid">
                                    <option value="">请选择</option>
                                    <#list hostlist as host>
                                    	<option value="${host.hostid}">${host.hostname}</option>
                                    </#list>
                                </select>
                                <input type="hidden" value="" id="query_hostid_page"/>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="bind_operatetype" name="bind_operatetype">
                                    <#list metriclist as metric>
                                    	<option value="${metric.id}">${metric.metric_name}</option>
                                    </#list>
                                </select>
                                <input type="hidden" value="" id="query_metricid_page"/>
                            </div>
                        </div>
                        <div class="span4">
                        </div>
                        </div>
                    </div>
                  <!--查询条件-END-->
                  <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div id="operate_menu_2" class="fr">
                                	<a href="#" id="bindbutton" class="button button-small button-primary">提交</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAddAll" name="checkboxAddAll" /> </th>
                                    <th style="width:10%">主机名称</th>
                                    <th style="width:8%">ip</th>
                                    <th style="width:10%">指标</th>
                                    <th style="width:10%">采集周期</th>
                                    <th style="width:16%">脚本</th>
                                    <th style="width:16%">脚本参数</th>
                                    <th style="width:16%">脚本返回类型</th>
                                    <th style="width:12%">状态</th>
                                </tr>
                            </thead>
                            <tbody id="hostmetricadddiv">
                            </tbody>
                        </table>
                    </div>
        </div>
        
    </div>
</div>
<!--end-main-->



<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <input id="detail_hostid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
        	<tr style="">
				<td style="text-align:right;padding-left:10px;">主机名称:</td>
				<td style="padding-left:10px;"><input id="detail_hostname" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">ip:</td>
				<td style="padding-left:10px;"><input id="detail_addr" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标名称:</td>
				<td style="padding-left:10px;"><input id="detail_metric_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">状态:</td>
				<td style="padding-left:10px;"><input id="detail_statename" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集周期:</td>
				<td style="padding-left:10px;"><input id="detail_cyclename" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">脚本返回类型:</td>
				<td style="padding-left:10px;"><input id="detail_returntypename" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集脚本:</td>
				<td colspan="3" style="padding-left:10px;"><input id="detail_script" size="30" readonly="readonly" style="width: 400px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本参数:</td>
				<td colspan="3" style="padding-left:10px;"><input id="detail_script_param" readonly="readonly" style="width: 400px;" class="" type="text"></td>
			</tr>
        </table>
        <div class="row row-fluid mb10">
        	<br/>
           <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
       </div>
    </div>
</div>

<!--弹层-编辑-->
<div class="mt10 display_none" id="modify_div">
    <div class="form-horizontal onlineTools">
        <input id="modify_id" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
        	<tr style="">
				<td style="text-align:right;padding-left:10px;">主机名称:</td>
				<td style="padding-left:10px;"><input id="modify_hostname" disabled="disabled" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">ip:</td>
				<td style="padding-left:10px;"><input id="modify_addr" disabled="disabled" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标名称:</td>
				<td style="padding-left:10px;"><input id="modify_metric_name" disabled="disabled" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">状态:</td>
				<td style="padding-left:10px;">
					<select id="modify_state" disabled="disabled" style="width: 150px;" >
						<#list statelist as param>
                        	<option value="${param.code}">${param.description}</option>
                        </#list>
					</select>
				</td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集周期:</td>
				<td style="padding-left:10px;">
					<select id="modify_cycle_id" disabled="disabled" style="width: 150px;">
						<#list collcyclelist as cycle>
                        	<option value="${cycle.cycleid}">${cycle.cyclename}</option>
                        </#list>
					</select>
				</td>
				<td style="text-align:right;padding-left:10px;">脚本返回类型:</td>
				<td style="padding-left:10px;">
					<select id="modify_script_return_type" disabled="disabled" style="width: 150px;">
						<#list scriptReturnTypelist as param>
                        	<option value="${param.code}">${param.description}</option>
                        </#list>
					</select>
				</td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">采集脚本:</td>
				<td colspan="3" style="padding-left:10px;"><input id="modify_script" size="100"  style="width: 400px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">脚本参数:</td>
				<td colspan="3" style="padding-left:10px;"><input id="modify_script_param" size="256" style="width: 400px;" class="" type="text"></td>
			</tr>
        </table>
        <div class="row row-fluid mb10">
           <br/>
           <a href="#" id="modify_ok" class="button button-primary ml200">确认</a>
           <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>

</@bs.layout>