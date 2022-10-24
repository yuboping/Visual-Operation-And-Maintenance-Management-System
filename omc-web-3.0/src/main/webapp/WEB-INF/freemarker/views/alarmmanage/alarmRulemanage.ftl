<@bs.layout [
    "echarts/echarts.js",
    "lcims/alarmmanage/alarmrule.js",
    "layer/layer.css",
    "dpl.css"
]  true true>

<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainalarmrule" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
             <div class="form-horizontal onlineTools">
            	<div class=" mb10  clearfix">
                        <div class="tag_box fl">
                        	<#list menulist as menu>
                        		<#if menu_index=0> 
                        		<input type="hidden" value="${menu.key}" id="classtype_param"/>
								  <a href="#" id="${menu.key}" class="tags border-left" onclick="">${menu.name}</a>
								<#else> 
								  	<#if menu_has_next>
								  		<a href="#" id="${menu.key}" class="tags" onclick="">${menu.name}</a>
								  	 <#else>
								  	 	<a href="#" id="${menu.key}" class="tags border-right" onclick="">${menu.name}</a>
								  	 </#if>
								</#if>
                			</#list>
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
                                    <a href="#alarmRule_add" id="addbutton" class="button button-small button-primary">新增</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:10%">业务模块</th>
                                    <th style="width:15%">指标</th>
                                    <th style="width:15%">监控目标</th>
                                    <th style="width:15%">告警规则</th>
                                    <th style="width:25%">告警信息</th>
                                    <th style="width:10%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="alarmdiv"></tbody>
                        </table>
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
<!--弹层-新增-->
<div class="mt10 display_none" id="add_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">模块:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_moduleid"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">监控目标:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_monitortarget1"></select>
                <select class="input-large" style="width: 150px;display:none;"  id="add_monitortarget_area"></select>
                <select class="input-large" style="width: 150px;display:none;"  id="add_monitortarget2"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">指标:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_metricid"></select>
                <select class="input-large" style="width: 150px;display:none;" id="add_metricid_attr"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">告警规则:</label>
            <div class="controls">
                <input size="250" style="width: 306px;" class=" input-medium" id="add_alarmexpr" name="add_alarmexpr" type="text">
                <span class="text-danger">*</span>
<div style="font-size: 11px;line-height: normal;">
规则说明：支持表达式书写。语法支持JS函数与运算符。
<br/>
关键字：newest - 当前值; avgindays - 平均值;pre5min - 上一周期值; yescurrent - 昨日同一时段;<br/>
allin12hour - 12个小时数据总量 ;collect - 采集值<br/>
示例：
"当前值>1000": newest>1000<br/>
"未采集到值": collect<0<br/>
"当前值超过平均值的50%": Math.abs(newest-avgindays)>avgindays*0.5
<br/>&nbsp;
</div>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">告警级别:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_alarmlevel">
                	<#list alarmlevels as alarmlevel>
                		<option value="${alarmlevel.alarmlevel}">${alarmlevel.alarmlable}</option>
                	</#list>
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">告警信息:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="add_alarmmsg" name="add_alarmmsg" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        
        <div class="row row-fluid" id="add_alarmmode">
           <label class="control-label" style="padding-top: 0px;">告警方式:</label>
            <div class="controls" id="add_alarmmode_controls">
            </div>
        </div>
        
        <div class="row row-fluid">
           <a href="#" id="add_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="add_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-新增-END-->

<!--弹层-修改-->
<div class="mt10 display_none" id="modify_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">模块:</label>
            <div class="controls">
            	<input id="modify_ruleid"  type="hidden">
             	<input style="width: 150px;" class=" input-medium" id="modify_moduleid" readonly="readonly" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">监控目标:</label>
            <div class="controls">
            	<input style="width: 150px;" class=" input-medium" id="modify_monitortarget2" readonly="readonly" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">指标:</label>
            <div class="controls">
            	<input style="width: 150px;" class=" input-medium" id="modify_metricid" readonly="readonly" type="text">
            	<input style="width: 150px;" class=" input-medium" id="modify_attribute" readonly="readonly" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">告警规则:</label>
            <div class="controls">
                <input size="250" style="width: 306px;" class=" input-medium" id="modify_alarmexpr" name="modify_alarmexpr" type="text">
                <span class="text-danger">*</span>
<div style="font-size: 11px;line-height: normal;">
规则说明：支持表达式书写。语法支持JS函数与运算符。
<br/>
关键字：newest - 当前值; avgindays - 平均值;pre5min - 上一周期值; yescurrent - 昨日同一时段;<br/>
allin12hour - 12个小时数据总量;collect - 采集值<br/>
示例：
"当前值>1000": newest>1000<br/>
"未采集到值": collect<0<br/>
"当前值超过平均值的50%": Math.abs(newest-avgindays)>avgindays*0.5
<br/>&nbsp;
</div>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">告警级别:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" autocomplete="off" id="modify_alarmlevel">
                	<#list alarmlevels as alarmlevel>
                		<option value="${alarmlevel.alarmlevel}">${alarmlevel.alarmlable}</option>
                	</#list>
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">告警信息:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="modify_alarmmsg" name="modify_alarmmsg" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        
        <div class="row row-fluid" id="modify_alarmmode">
           <label class="control-label" style="padding-top: 0px;">告警方式:</label>
            <div class="controls" id="modify_alarmmode_controls">
            </div>
        </div>
        
        <div class="row row-fluid">
           <a href="#" id="modify_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->
</@bs.layout>