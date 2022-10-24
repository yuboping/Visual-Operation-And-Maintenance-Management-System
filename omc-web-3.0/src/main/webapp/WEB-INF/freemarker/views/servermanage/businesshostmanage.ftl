<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/businesshostmanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainbusinesshost" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">业务链路:</label>
                            <div class="controls">
                                <select class="input-medium" id="business_name" name="business_name" style="width: 350px;" >
                                    <option value="">全部</option>
                                    <#list mdMenuHostList as mdMenuHost>
                                        <option value=${mdMenuHost.name}>${mdMenuHost.business_link}</option>
                                    </#list>
                                </select>
                                <input type="hidden" value="" id="business_name_page"/>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">主机IP:</label>
                            <div class="controls">
                                <select class="input-medium" id="host_ip" name="host_ip" >
                                    <option value="">全部</option>
                                    <#list hostlist as host>
                                        <option value=${host.hostid}>${host.addr}</option>
                                    </#list>
                                </select>
                                <input type="hidden" value="" id="host_id_page"/>
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
                                <input type="hidden" value="" id="page_curr"/>
                                <div id = "operate_menu" class="fr" >
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:10%">业务链路</th>
                                    <th style="width:10%">主机IP</th>
                                    <th style="width:10%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="businesshostdiv"></tbody>
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
				<td style="text-align:right;padding-left:10px;">业务链路:</td>
				<td style="padding-left:10px;"><select class="" style="width: 350px;" id="add_business_name"></select> <span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">主机IP:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_host_ip"></select> <span class="text-danger">*</span></td>
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
        <input id="modify_businesshostid" type="hidden">
        <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">业务链路:</td>
				<td style="padding-left:10px;"><select class="" style="width: 350px;" id="modify_business_name"></select> <span class="text-danger">*</span></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">主机IP:</td>
				<td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_host_ip"></select> <span class="text-danger">*</span></td>
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
        <input id="detail_businesshostid" type="hidden">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">业务链路:</td>
				<td style="padding-left:10px;"><input id="detail_business_name" size="30" readonly="readonly" style="width: 350px;" class="" type="text"></td>
		    </tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">主机IP:</td>
				<td style="padding-left:10px;"><input id="detail_host_ip" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
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