<@bs.layout [
    "echarts/echarts.js",
    "lcims/ais/aisgroupmetricmanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainaisgroupmetric" >
        <div>
        	
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                    	<div class="span11">
                            <label class="control-label">巡检组:</label>
                            <div class="controls">
                                <select class="input-medium" id="queryaisgroup" name="queryaisgroup" style="width: 250px;" >
                                    <option value="" selected="true" >请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="span9">
                            <label class="control-label">模块:</label>
                            <div class="controls">
                                <select class="input-medium" id="querymodule" name="querymodule" style="width: 250px;" >
                                    <option value="" selected="true" >请选择</option>
                                </select>
                            </div>
                        </div>
                        
	                </div>
	                <div class="row row-fluid">
                        <div class="span11">
                            <label class="control-label">监控目标:</label>
                            <div class="controls">
                                <select class="input-medium" id="querymonitortarget1" name="querymonitortarget1" style="width: 100px;float:left;" >
                                    <option value="" selected="true" >请选择</option>
                                </select>
                                <select class="input-medium" id="querymonitortarget2" name="querymonitortarget2" style="width: 100px;float:left;margin-left:10px;display: none;" >
                                    <option value="" selected="true" >请选择</option>
                                </select>
                                <select class="input-medium" id="querymonitortarget3" name="querymonitortarget3" style="width: 100px;float:left;margin-left:10px;display: none;" >
                                    <option value="" selected="true" >请选择</option>
                                </select>
                                <select class="input-medium" id="querymonitortarget4" name="querymonitortarget4" style="width: 100px;float:left;margin-left:10px;display: none;" >
                                    <option value="" selected="true" >请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="span9">
                            <label class="control-label">指标:</label>
                            <div class="controls">
                                <select class="input-medium" id="querymetric" name="querymetric" style="width: 250px;" >
                                    <option value="" selected="true" >请选择</option>
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
                                    <th style="width:10%">巡检组</th>
                                    <th style="width:10%">模块</th>
                                    <th style="width:6%">维度1</th>
                                    <th style="width:6%">维度2</th>
                                    <th style="width:10%">告警类别</th>
                                    <th style="width:8%">图表名称</th>
                                    <th style="width:8%">指标名称</th>
                                    <th style="width:6%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="aisgroupmetricdiv"></tbody>
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
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
            <tr>
			    <td style="text-align:right;padding-left:10px;">巡检组:</td>
				<td style="padding-left:10px;">
					<select id="addaisgroup" name="addaisgroup" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">模块:</td>
				<td style="padding-left:10px;">
					<select id="addmodule" name="addmodule" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">监控目标:</td>
				<td style="padding-left:10px;">
					<select id="addmonitortarget1" name="addmonitortarget1" style="width: 100px;float:left;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
                    <select id="addmonitortarget2" name="addmonitortarget2" style="width: 100px;float:left;margin-left:10px;display: none;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
                    <select id="addmonitortarget3" name="addmonitortarget3" style="width: 100px;float:left;margin-left:10px;display: none;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
                    <select id="addmonitortarget4" name="addmonitortarget4" style="width: 100px;float:left;margin-left:10px;display: none;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">图表:</td>
				<td style="padding-left:10px;">
					<select id="addcharts" name="addcharts" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">指标:</td>
				<td style="padding-left:10px;">
					<select id="addmetric" name="addmetric" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">指标维度:</td>
				<td style="padding-left:10px;">
					<select id="addmetricattr" name="addmetricattr" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
				</td>
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
        <input id="modify_aisgroupmetricid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
            <tr>
			    <td style="text-align:right;padding-left:10px;">巡检组:</td>
				<td style="padding-left:10px;">
					<select id="modifyaisgroup" name="modifyaisgroup" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">模块:</td>
				<td style="padding-left:10px;">
					<select id="modifymodule" name="modifymodule" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">监控目标:</td>
				<td style="padding-left:10px;">
					<select id="modifymonitortarget1" name="modifymonitortarget1" style="width: 100px;float:left;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
                    <select id="modifymonitortarget2" name="modifymonitortarget2" style="width: 100px;float:left;margin-left:10px;display: none;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
                    <select id="modifymonitortarget3" name="modifymonitortarget3" style="width: 100px;float:left;margin-left:10px;display: none;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
                    <select id="modifymonitortarget4" name="modifymonitortarget4" style="width: 100px;float:left;margin-left:10px;display: none;" >
                    	<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">图表:</td>
				<td style="padding-left:10px;">
					<select id="modifycharts" name="modifycharts" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">指标:</td>
				<td style="padding-left:10px;">
					<select id="modifymetric" name="modifymetric" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
					<span class="text-danger">*</span>
				</td>
			</tr>
			<tr>
			    <td style="text-align:right;padding-left:10px;">指标维度:</td>
				<td style="padding-left:10px;">
					<select id="modifymetricattr" name="modifymetricattr" style="width: 250px;" >
						<option value="" selected="true" >请选择</option>
                    </select>
				</td>
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
        <input id="detail_aisgroupmetricid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
			    <td style="text-align:right;padding-left:10px;">巡检组:</td>
			    <td style="padding-left:10px;"><input id="detail_aisgroup_name" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">模块:</td>
				<td style="padding-left:10px;"><input id="detail_module_name" size="30" readonly="readonly" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
			    <td style="text-align:right;padding-left:10px;">维度1:</td>
			    <td style="padding-left:10px;"><input id="detail_dimension1_name" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">维度2:</td>
				<td style="padding-left:10px;"><input id="detail_dimension2_name" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">图表名称:</td>
				<td style="padding-left:10px;"><input id="detail_charts_title" size="30" readonly="readonly" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">指标名称:</td>
				<td style="padding-left:10px;"><input id="detail_metric_name" size="30" readonly="readonly" style="width: 380px;" class="" type="text"></td>
			</tr>
			<tr style="">
			    <td style="text-align:right;padding-left:10px;">指标下维度:</td>
				<td style="padding-left:10px;"><input id="detail_attr" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>
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