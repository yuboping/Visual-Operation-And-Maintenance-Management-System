<@bs.layout [
    "echarts/echarts.js",
    "lcims/gscm5G/collectfilemanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="maincollect" >
        <div>
        	<input type="hidden" value="${province}" id="provincename">
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">采集主机:</label>
                            <div class="controls">
                                <select class="input-medium" id="host_ip" name="host_ip" >
                                    <#list hostList as host>
                                        <option value=${host.addr}>${host.addr}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">采集日期:</label>
                            <div id="collectDate_div" class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="collect_time" name="collect_time" type="text">
                            </div>
                        </div>
	                        <div class="span4">
	                            <a  id="querybutton" class="button button-primary">查询</a>
	                            <a  id="contrastbutton" class="button button-primary ml10">稽核对比</a>
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
                                    <th style="width:4%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:20%">防火墙日志</th>
                                    <th style="width:8%">文件大小</th>
                                    <th style="width:10%">采集状态</th>
                                    <th style="width:10%">采集状态下载</th>
                                    <th style="width:20%">第三方接口输出文件</th>
                                    <th style="width:8%">文件大小</th>
                                    <th style="width:10%">输出状态</th>
                                    <th style="width:10%">输出状态下载</th>
                                </tr>
                            </thead>
                            <tbody id="collectdiv"></tbody>
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
<!--弹层-稽核对比-->
<div class="mt10 display_none" id="contrast_div">
    <div class="form-horizontal onlineTools">
        <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
			<tr style="">
				<td style="text-align:right;padding-left:10px;">日期:</td>
				<td style="padding-left:10px;"><input id="contrast_collect_time" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">主机:</td>
				<td style="padding-left:10px;"><input id="contrast_host_ip" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">防火墙日志采集总数:</td>
				<td style="padding-left:10px;"><input id="contrast_firewall_log_num" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">第三方接口输出文件总数:</td>
				<td style="padding-left:10px;"><input id="contrast_thirdparty_log_num" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
			</tr>
			<tr style="">
				<td style="text-align:right;padding-left:10px;">差异文件清单:</td>
				<td style="padding-left:10px;"><textarea id="contrast_diff_log_name" size="30" readonly="readonly" style="width: 150px; hight: 150px;" class="" type="text"></textarea></td>
			</tr>
       </table>
       <div class="row row-fluid mb10">
           <br/>
           <a href="#" id="contrast_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
       </div>
    </div>
</div>
<!--弹层-稽核对比-END-->
</@bs.layout>