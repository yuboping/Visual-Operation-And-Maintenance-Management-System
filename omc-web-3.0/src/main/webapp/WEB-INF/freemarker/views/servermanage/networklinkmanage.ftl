<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/networklinkmanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainnetworklinkmanage" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="linktype" name="linktype" >
                                    <#list paramlist as param>
                                        <option value=${param.code}>${param.description}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">名称:</label>
                            <div class="controls">
                                <select class="input-medium" id="source" name="source" >
                                	<#list hostlist as host>
                                		<option value=${host.hostid}>${host.hostname}</option>
                                		<!-- 
                                	    <#if host_index=0> 
                                       		<option value=${host.hostid}>${host.hostname}</option>
                                       </#if>
                                		<#if host_has_next>
                                			<option value=${host.hostid}>${host.hostname}</option>
                                		</#if>
                                		-->
                                    </#list>
                                    <#list deviceinfos as device>
                                    	<option value=${device.device_id}>${device.device_name}</option>
                                    	<!-- 
                                       <#if device_index=0> 
                                       		<option value=${device.device_id}>${device.device_name}</option>
                                       </#if>
                                    	<#if device_has_next>
                                    		<option value=${device.device_id}>${device.device_name}</option>
                                    	</#if>
                                    	-->
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span4">
                            <a href="#" id="querybutton" class="button button-primary">查询</a>
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
                                    <a href="#role_add" id="addbutton" class="button button-small button-primary">新增</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:15%">本机名称</th>
                                    <th style="width:10%">本机端口</th>
                                    <th style="width:15%">对端设备</th>
                                    <th style="width:10%">对端端口</th>
                                    <th style="width:38%">描述</th>
                                    <th style="width:12%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="hostdiv"></tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--end-main-->

<!--弹层-新增-->
<div class="mt10 display_none" id="add_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">类型:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_type">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">本机名称:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_source">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">本机端口:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="add_port" name="add_port" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">对端设备:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_target">
                	
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">对端端口:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="add_to_port" name="add_to_port" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">描述:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="add_description" name="add_description" type="text">
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
           <label class="control-label" style="padding-top: 0px;">类型:</label>
            <div class="controls">
            	<input id="modify_id"  type="hidden">
            	<select class="input-large" style="width: 150px;" id="modify_type" disabled="disabled" >
                </select>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">本机名称:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" autocomplete="off" id="modify_source">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">本机端口:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="modify_port" name="modify_port" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">对端设备:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" autocomplete="off" id="modify_target">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">对端端口:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="modify_to_port" name="modify_to_port" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">描述:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="modify_description" name="modify_description" type="text">
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