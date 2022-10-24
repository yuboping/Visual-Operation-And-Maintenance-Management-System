<@bs.layout [
    "echarts/echarts.js",
    "select2/select2.min.css",
    "lcims/alarmmanage/alarmhisreportforms.js",
    "laydate/laydate.js",
    "layer/layer.css",
    "dpl.css"
]  true true>

<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainhistory" >
        <div>
        <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">查询类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_type">
                                    <option value="1">月表</option>
                                    <option value="0">周表</option>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">日期:</label>
                            <div id="startDate_div" class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="startdate" name="startdate" type="text">
                                <input size="30" readonly="readonly" class=" input-medium" id="enddate" name="enddate" type="text">
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">告警级别:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_alarmlevel">
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">指标:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_metricid">
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">模块:</label>
                            <div class="controls">
                                <select class="input-medium" id="querymodule" name="querymodule">
                                    <option value="" selected="true" >请选择</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row row-fluid">
                        <div class="span20">
                            <label class="control-label" style="padding-top: 0px;">监控目标:</label>
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
                             <div id="operate_menu" class="fr"></div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:8%">模块</th>
                                    <th style="width:8%">指标</th>
                                    <th style="width:8%">告警级别</th>
                                    <th style="width:15%">告警目标</th>
                                    <th style="width:22%">告警信息</th>
                                    <th style="width:15%">告警周期时间</th>
                                    <th style="width:8%">指标原始值</th>
                                    <th style="width:8%">确认时间</th>
                                    <th style="width:5%">确认人</th>
                                    <th style="width:15%">清除时间</th>
                                </tr>
                            </thead>
                            <tbody id="alarmhistorydiv"></tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                <!--搜索结果-END-->
                </div>
            </div>
        </div>
</div>

    <!--弹层-新增-->
    <div class="mt10 display_none" id="add_div">
        <div class="form-horizontal onlineTools">
            <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">APN名称:</td>
                    <td style="padding-left:10px;">
                        <select class="input-medium js-example-basic-single" id="add_apn_id" name="add_apn_id" style="width: 150px;" ></select>
                    </td>
                    <td style="text-align:right;padding-left:10px;">限流周期:</td>
                    <td style="padding-left:10px;"><input id="add_limit_cycle" size="50" maxlength="50" style="width: 150px;" type="text" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">认证限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_auth_value" size="30" maxlength="100" style="width: 150px;" type="text" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">日志限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_log_value" size="30" maxlength="100" style="width: 150px;" type="text" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">记录次数限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_record_value" size="30" maxlength="100" style="width: 150px;" type="text" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">每日限流阀值:</td>
                    <td style="padding-left:10px;"><input id="add_day_value" size="30" maxlength="100" style="width: 150px;" type="text" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
            </table>
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
                        <select class="input-medium" id="modify_apn_id" name="modify_apn_id" style="width: 150px;" disabled></select>
                    </td>
                    <td style="text-align:right;padding-left:10px;">限流周期:</td>
                    <td style="padding-left:10px;"><input id="modify_limit_cycle" size="50" maxlength="50" style="width: 150px;" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">认证限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_auth_value" size="30" maxlength="100" style="width: 150px;" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">日志限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_log_value" size="30" maxlength="100" style="width: 150px;" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">记录次数限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_record_value" size="30" maxlength="100" style="width: 150px;" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">每日限流阀值:</td>
                    <td style="padding-left:10px;"><input id="modify_day_value" size="30" maxlength="100" style="width: 150px;" oninput="value=value.replace(/[^\d]/g,'')"> <span class="text-danger">*</span></td>
                </tr>
            </table>
            <div class="row row-fluid mb10" style="text-align: center;">
                <br/>
                <a href="#" id="modify_ok" class="button button-primary">确认</a>
                <a href="#" id="modify_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-修改-END-->
<!--end-main-->
</@bs.layout>
