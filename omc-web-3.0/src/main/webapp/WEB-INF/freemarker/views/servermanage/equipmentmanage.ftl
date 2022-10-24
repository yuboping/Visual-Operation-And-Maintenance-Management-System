<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/equipmentmanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainnode" >
        <div>
        	
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">设备型号:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="model_name" name="model_name" type="text">
                            </div>
                        </div>
                        <div class="span10" id="factoryids">
                            <label class="control-label" >厂家名称:</label>
                            <div class="controls">
                                <select class="input-medium" id="factoryid" name="factoryid">
                                	<option value="">全部</option>
                                    <#list factoryList as factory>
                                        <option value=${factory.id}>${factory.factory_name}</option>
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
                                    <th style="width:10%">设备型号</th>
                                    <th style="width:10%">厂家名称</th>
                                    <th style="width:10%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="nodediv"></tbody>
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
				<td style="text-align:right;padding-left:10px;">厂家名称:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_factory_name"></select><span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">设备型号:</td>
				<td style="padding-left:10px;" colspan="3"><input id="add_equipment" size="50" maxlength="100" style="width: 380px;" class="" type="text"><span class="text-danger">*</span></td>
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
        <input id="modify_factoryid" type="hidden">
        <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">厂家名称:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_factory_name" disabled="disabled"></select><span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">设备型号:</td>
				<td style="padding-left:10px;" colspan="3"><input id="modify_equipment" size="50" maxlength="100" style="width: 380px;" class="" type="text"><span class="text-danger">*</span></td>
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
        <input id="detail_nodeid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">设备型号:</td>
				<td style="padding-left:10px;"><input id="detail_equipment_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">厂家名称:</td>
				<td style="padding-left:10px;" colspan="3"><input id="detail_factory_name" readonly="readonly" size="50" style="width: 380px;" class="" type="text"></td>
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