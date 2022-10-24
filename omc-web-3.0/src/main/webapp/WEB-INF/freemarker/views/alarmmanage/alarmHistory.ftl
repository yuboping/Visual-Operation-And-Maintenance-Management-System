<@bs.layout [
    "echarts/echarts.js",
    "lcims/alarmmanage/alarmhistory.js",
    "layer/layer.css",
    "laydate/laydate.js",
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
                            <label class="control-label" style="padding-top: 0px;">开始日期:</label>
                            <div id="startDate_div" class="controls">
                                <input size="30" readonly="readonly" class="input-medium" id="startdate" name="startdate" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">结束日期:</label>
                            <div id="endDate_div" class="controls">
                                <input size="30" readonly="readonly" class=" input-medium" id="enddate" name="enddate" type="text">
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">是否确认:</label>
                            <div class="controls">
                                <select class="input-medium" id="confirm_type">
                                    <option value="">请选择</option>
                                    <option value="1">已确认</option>
                                    <option value="0">未确认</option>
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
                            <label class="control-label" style="padding-top: 0px;">告警级别:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_alarmlevel">
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
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
                                    <th style="width:10%">告警目标</th>
                                    <th style="width:20%">告警信息</th>
                                    <#--<th style="width:7%">告警数量</th>-->
                                    <th style="width:12%">初次告警时间</th>
                                    <th style="width:12%">最后告警时间</th>
                                    <th style="width:5%">确认人</th>
                                    <th style="width:12%">确认时间</th>
                                    <th style="width:12%">清除时间</th>
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

<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <input id="detail_alarmid" type="hidden">
        <!--搜索结果-->
        <div class="omc_table_box">
            <div class="mb10 clearfix">
                <div class="mb10 clearfix">
                    <div class="fl"> 共查询到 <span id="hisquerynum" class="text-danger"></span>条数据 | </div>
                    <div class="fl ml10">第 <span id="hiscurrnum" >1-10</span>条数据 </div>
                    <div id="operate_menu_his" class="fr">
                        <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
                    </div>
                </div>
                <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                    <thead>
                    <tr>
                        <th style="width:16%">模块</th>
                        <th style="width:18%">指标</th>
                        <th style="width:16%">告警目标</th>
                        <th style="width:14%">告警级别</th>
                        <th style="width:18%">告警信息</th>
                        <th style="width:20%">告警周期时间</th>
                    </tr>
                    </thead>
                    <tbody id="hisalarmdiv"></tbody>
                </table>
                <div id="hispageinfo" class="fr parts_down_page clearfix"></div>
                <div class="ad-page-outer clearfix "></div>
            </div>
            <!--搜索结果-END-->
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>
