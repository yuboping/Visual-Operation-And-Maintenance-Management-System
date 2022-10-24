<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/alarmmodemanage.js",
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
                            <label class="control-label">告警方式名称</label>
                            <div class="controls">
                            	<input size="30" class="input-medium" id="modename" name="modename" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">告警方式类型</label>
                            <div class="controls">
                                <select class="input-medium" id="modetype" name="modetype" >
                                    <option value="">全部</option>
                                    <#list params as param>
                                        <option value=${param.code}>${param.description}</option>
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
                                    <th style="width:15%">告警方式名称</th>
                                    <th style="width:15%">告警方式类型</th>
                                    <th style="width:60%">告警方式属性</th>
                                    <th style="width:5%">操作</th>
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
        <div class="row row-fluid mb10">
           <label class="control-label" style="padding-top: 0px;" >告警方式名称</label>
            <div class="controls">
                <input size="30" class="input-large" id="add_modename" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label" style="padding-top: 0px;" >告警方式类型</label>
            <div class="controls">
                <select class="input-large" id="add_modetype">
                	<#list params as param>
                        <option value=${param.code}>${param.description}</option>
                    </#list>
                </select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label" style="padding-top: 0px;" >告警方式属性</label>
            <div class="controls">
            	<textarea  class="input-large" id="add_modeattr" ></textarea>
            	<span class="text-danger">*</span>
            	<div style="font-size: 11px;line-height: normal;">
					多个值用英文分号';'隔开
				</div>
            </div>
        </div>
        <div class="row row-fluid mb10">
			<a href="#" id="add_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="add_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-新增-END-->

<!--弹层-修改-->
<div class="mt10 display_none" id="modify_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid mb10">
			<label class="control-label" style="padding-top: 0px;">告警方式名称</label>
            <div class="controls">
                <input size="30" class="input-large" id="modify_modename" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label" style="padding-top: 0px;">告警方式类型</label>
            <div class="controls">
                <select class="input-large" id="modify_modetype"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label" style="padding-top: 0px;">告警方式属性</label>
            <div class="controls">
                <textarea  class="input-large" id="modify_modeattr" ></textarea>
                <span class="text-danger">*</span>
            	<div style="font-size: 11px;line-height: normal;">
					多个值用英文分号';'隔开
				</div>
            </div>
        </div>
        <div class="row row-fluid mb10">
            <a href="#" id="modify_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->

<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <input id="detail_basid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">告警方式名称:</td>
				<td style="padding-left:10px;"><input id="detail_modename" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
				<td style="text-align:right;padding-left:10px;">告警方式类型:</td>
				<td style="padding-left:10px;"><input id="detail_modetype" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">告警方式属性:</td>
				<td style="padding-left:10px;" colspan="3"><textarea id="detail_modeattr" readonly="readonly" size="50" style="width: 400px;resize:none;" class="" type="text"></textarea></td>
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