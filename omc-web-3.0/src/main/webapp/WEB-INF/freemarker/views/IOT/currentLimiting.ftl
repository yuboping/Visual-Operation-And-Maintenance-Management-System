<@bs.layout [
"echarts/echarts.js",
"select2/select2.min.css",
"lcims/IOT/currentLimiting.js",
"layer/layer.css",
"dpl.css"
]  true true>
    <!--main-->
    <div class="omc_main">
        <div class="omc-scroll" id="main" >
            <div>
                <input type="hidden" value="${province}" id="provincename">
                <!--查询条件-->
                <div class="omc_main_tab" >
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">APN名称:</label>
                                <div class="controls">
                                    <input size="30" class="input-medium" id="apn_name" name="apn_name" type="text">
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
                                <th style="width:6%">APN名称</th>
                                <th style="width:6%">限流周期</th>
                                <th style="width:6%">认证限流阀值</th>
                                <th style="width:20%">日志限流阀值</th>
                                <th style="width:16%">记录次数限流阀值</th>
                                <th style="width:7%">操作</th>
                            </tr>
                            </thead>
                            <tbody id="apndiv"></tbody>
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
                    <td style="text-align:right;padding-left:10px;">APN名称:</td>
                    <td style="padding-left:10px;">
                            <select class="input-medium js-example-basic-single" id="add_apn_id" name="add_apn_id" style="width: 150px;" >
<#--                                <option value="" selected="true" >请选择</option>-->
                            </select>
                    </td>
                    <td style="text-align:right;padding-left:10px;">限流周期:</td>
                    <td style="padding-left:10px;"><input id="add_limit_cycle" size="50" maxlength="50" style="width: 150px;" type="text" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">认证限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_auth_value" size="30" maxlength="100" style="width: 150px;" type="text"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">日志限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_log_value" size="30" maxlength="100" style="width: 150px;" type="text"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">记录次数限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_record_value" size="30" maxlength="100" style="width: 150px;" type="text"> <span class="text-danger">*</span></td>
                </tr>
                
            </table>
            <div class="row row-fluid mb10" style="text-align: center;">
               	<br/> 
               	 认证限流阀值、日志限流阀值和记录次数限流阀值需满足递增关系，
               	 <br/> 
               	 即认证限流阀值<日志限流阀值<记录次数限流阀值。
            </div>
            <div class="row row-fluid mb10" style="text-align: center;">
                <br/>
                <a href="#" id="add_ok" class="button button-primary">确认</a>
                <a href="#" id="add_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-新增-END-->

    <!--弹层-修改-->
    <div class="mt10 display_none" id="modify_div">
        <div class="form-horizontal onlineTools">
            <input id="modify_apnid" type="hidden">
            <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">APN名称:</td>
                    <td style="padding-left:10px;">
                        <select class="input-medium" id="modify_apn_id" name="modify_apn_id" style="width: 150px;" disabled ></select>
                    </td>
                    <td style="text-align:right;padding-left:10px;">限流周期:</td>
                    <td style="padding-left:10px;"><input id="modify_limit_cycle" size="50" maxlength="50" style="width: 150px;" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">认证限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_auth_value" size="30" maxlength="100" style="width: 150px;"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">日志限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_log_value" size="30" maxlength="100" style="width: 150px;"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">记录次数限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_record_value" size="30" maxlength="100" style="width: 150px;"> <span class="text-danger">*</span></td>
                </tr>
            </table>
            <div class="row row-fluid mb10" style="text-align: center;">
               	<br/> 
               	 认证限流阀值、日志限流阀值和记录次数限流阀值需满足递增关系，
               	 <br/> 
               	 即认证限流阀值<日志限流阀值<记录次数限流阀值。
            </div>
            <div class="row row-fluid mb10" style="text-align: center;">
                <br/>
                <a href="#" id="modify_ok" class="button button-primary">确认</a>
                <a href="#" id="modify_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-修改-END-->

    <!--弹层-详情-->
    <div class="mt10 display_none" id="detail_div">
        <div class="form-horizontal onlineTools">
            <input id="detail_apnid" type="hidden">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">APN名称:</td>
                    <td style="padding-left:10px;"><input id="detail_apn_id" size="30" maxlength="30" readonly="readonly" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">限流周期:</td>
                    <td style="padding-left:10px;"><input id="detail_limit_cycle" size="50" maxlength="50" readonly="readonly" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">认证限流阀值:</td>
                    <td style="padding-left:10px;"><input id="detail_auth_value" size="30" maxlength="100" readonly="readonly" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">日志限流阀值:</td>
                    <td style="padding-left:10px;"><input id="detail_log_value" size="30" maxlength="100" readonly="readonly" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">记录次数限流阀值:</td>
                    <td style="padding-left:10px;"><input id="detail_record_value" size="30" maxlength="100" readonly="readonly" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                </tr>
            </table>
            <div class="row row-fluid mb10" style="text-align: center;">
                <br/>
                <a href="#" id="detail_ok" class="button button-primary">关闭</a>
            </div>
        </div>
    </div>
    <!--弹层-详情-END-->

</@bs.layout>