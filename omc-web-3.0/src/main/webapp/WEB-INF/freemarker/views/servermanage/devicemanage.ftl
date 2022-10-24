<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/devicemanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="maindevicemanage" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                	<div class="row row-fluid">
                		<div class="span10">
                            <label class="control-label">节点:</label>
                            <div class="controls">
                                <select class="input-medium" id="nodeid" name="nodeid" >
                                    <option value="">全部</option>
                                    <#list nodelist as node>
                                        <option value=${node.nodeid}>${node.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="type" name="type" >
                                	<option value="">全部</option>
                                	<#list typelist as type>
                                		<option value=${type.code}>${type.description}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                	</div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">设备名称</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="device_name" name="device_name" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">网络类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="networktype" name="networktype" >
                                	<option value="">全部</option>
                                	<#list networkTypelist as networktype>
                                		<option value=${networktype.code}>${networktype.description}</option>
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
                                    <a href="#device_add" id="addbutton" class="button button-small button-primary">新增</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:15%">设备名称</th>
                                    <th style="width:8%">图位置</th>
                                    <th style="width:10%">节点</th>
                                    <th style="width:15%">类型</th>
                                    <th style="width:10%">网络类型</th>
                                    <th style="width:30%">位置</th>
                                    <th style="width:12%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="devicediv"></tbody>
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
           <label class="control-label" style="padding-top: 0px;">设备名称:</label>
            <div class="controls">
                <input size="40"  style="width: 306px;" input-medium" id="add_device_name" name="add_device_name" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">图位置:</label>
            <div class="controls">
                <input size="20"  style="width: 150px;" input-medium" id="add_location" name="add_location" type="text">
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">节点:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_nodeid">
                	
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">类型:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_type">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">网络类型:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" id="add_networktype">
                	
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">设备位置:</label>
            <div class="controls">
                <input size="60"  style="width: 306px;" input-medium" id="add_device_room" name="add_device_room" type="text">
                <span class="text-danger">*</span>
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
    	   <input id="modify_device_id"  type="hidden">
           <label class="control-label" style="padding-top: 0px;">设备名称:</label>
            <div class="controls">
                <input size="30"  style="width: 306px;" input-medium" id="modify_device_name" name="modify_device_name" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
    	   <input id="modify_device_id"  type="hidden">
           <label class="control-label" style="padding-top: 0px;">图位置:</label>
            <div class="controls">
            	<input size="20"  style="width: 150px;" input-medium" id="modify_location" name="modify_location" type="text">
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">节点:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" autocomplete="off" id="modify_nodeid">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">类型:</label>
            <div class="controls">
            	<input id="modify_id"  type="hidden">
            	<select class="input-large" style="width: 150px;" autocomplete="off" id="modify_type">
                </select>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">网络类型:</label>
            <div class="controls">
                <select class="input-large" style="width: 150px;" autocomplete="off" id="modify_networktype">
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid">
           <label class="control-label" style="padding-top: 0px;">设备位置:</label>
            <div class="controls">
                <input size="250"  style="width: 306px;" input-medium" id="modify_device_room" name="modify_device_room" type="text">
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