<@bs.layout [
    "global.css",
    "bootstrap/bootstrap.min.css",
    "bootstrap/font-awesome.min.css",
    "ais/font-awesome.min.css",
    "ais/icon-style.css",
    "ais/ais.css",
    "require/require.js",
    "echarts/echarts.js",
    "lcims/ais/aisschedulemanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainaisschedule" >
       <div>
            <div class="omc_main_tab" >
                <!--查询条件-->
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">巡检计划标题:</label>
                                <div class="controls">
                                    <input type="hidden" name="savetype" id="savetype" value=""/>
                                    <input size="30" class=" input-medium" id="schedule_name" name="schedule_name" type="text">
                                </div>
                            </div>
                            <div class="span4">
                                <a id="querybutton" class="button button-primary">查询</a>
                                <a id="resetbutton" class="button ml10">重置</a>
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
                            <div id = "operate_menu" class="fr" ></div>
                         </div>
                    </div>
                    <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                        <thead>
                            <tr>
                                <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                <th style="width:30%">标题</th>
                                <th style="width:30%">巡检组</th>
                                <th style="width:20%">巡检时间</th>
                                <#--<th style="width:15%">邮箱</th>-->
                                <th style="width:15%">操作</th>
                            </tr>
                        </thead>
                        <tbody id="aisschedulediv"></tbody>
                    </table>
                    <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                    <div class="ad-page-outer clearfix "></div>
                </div>
                <!--搜索结果-END-->
            </div>
        </div>
    </div>
</div>
<!--end-main-->

<!--弹层-新增-->
<div class="mt10 display_none" id="add_div">
    <!--内容区域-->
    <div class="omc_main_plan omc_ais" >
        <div id="mainplan">
            <#--<div class="dap-ais-mainarea_plan">-->
                <#--<div class="dap-ais-mainarea-bg">-->
                    <!--新计划-->
                    <div class="panel dap-ais-newpanel">
                        <div class="panel-heading"><i class="dap-ais-newplan-ico"></i> 制定巡检计划</div>
                        <div class="panel-body">
                            <form class="form-horizontal dap-ais-plan-form" role="form" id="scheduleform">
                                <input type="hidden" name="id" id="id" value=""/>
                                <input type="hidden" name="timer" id="timer" value=""/>
                                <input type="hidden" name="group_ids" id="group_ids" value=""/>
                                <div class="form-group">
                                    <label for="ttl" class="col-sm-2 control-label">报告标题</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control input-lg" style="width:70%" id="title" name="title"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="time" class="col-sm-2 control-label">巡检时间</label>
                                    <div class="col-sm-10 form-inline">
                                        <select id="schedule_type" name="schedule_type" class="form-control" >
                                            <option value="1" selected="selected">每年</option>
                                            <option value="2">每月</option>
                                            <option value="3">每周</option>
                                            <option value="4">每天</option>
                                        </select>
                                        <select class="form-control" id="monthSel">
                                        <#list 1..12 as month>
                                            <option>${month}</option>
                                        </#list>
                                        </select>
                                        <label class="dap-ais-label-inline" id="monthLabel">月</label>
                                        <select class="form-control" id="daySel">
                                        <#list 1..31 as day>
                                            <option>${day}</option>
                                        </#list>
                                        </select>
                                        <label class="dap-ais-label-inline" id="dayLabel">日</label>
                                        <select class="form-control" id="weekdaySel" style="display:none">
                                            <option value="1">周日</option>
                                            <option value="2">周一</option>
                                            <option value="3">周二</option>
                                            <option value="4">周三</option>
                                            <option value="5">周四</option>
                                            <option value="6">周五</option>
                                            <option value="7">周六</option>
                                        </select>
                                        <select class="form-control" id="hourSel">
                                        <#list 0..23 as hour>
                                            <option>${hour}</option>
                                        </#list>
                                        </select>
                                        <label class="dap-ais-label-inline" id="hourLabel">点</label>
                                        <select class="form-control" id="minSel">
                                        <#list 0..59 as minute>
                                            <option>${minute}</option>
                                        </#list>
                                        </select>
                                        <label class="dap-ais-label-inline" id="minLabel">分</label>
                                    </div>
                                </div>
                                <!--巡检类型START-->
                                <div class="form-group">
                                    <div class="dap-ais-ttl">
                                        <label class="col-xs-2 control-label">巡检类别</label>
                                        <span class="col-xs-2 pull-right"></span>

                                        <div class="clearfix"></div>

                                        <div class="dap-ais-chklist" id="ais-chklist">
                                        </div>
                                    </div>
                                <#include "chkgroupwithschedule.ftl"/>
                                </div>
                                <!--巡检类型END-->
                            <#if sendEmail>
                            <div class="form-group">
                                <label for="ttl" class="col-sm-2 control-label">邮件发送</label>

                                <div class="col-sm-10">
                                    <input type="text" class="form-control input-lg" maxlength="512" style="width:70%" id="emails" name="emails"/>
                                    <font color="red">多个收件人邮箱地址请用英文逗号分隔</font>
                                </div>
                            </div>
                            </#if>
                            <#if sendSms>
                            <div class="form-group">
                                <label for="ttl" class="col-sm-2 control-label">短信提醒</label>

                                <div class="col-sm-10">
                                    <input type="text" class="form-control input-lg" maxlength="512" style="width:70%" id="phones" name="phones"/>
                                    <font color="red">多个手机号请用英文分号分隔</font>
                                </div>
                            </div>
                            </#if>
                                <div class="">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <button class="dap-ais-btn big" id="savebutton" type="button">保存计划</button>
                                        <button class="dap-ais-btn big" id="closebutton" type="button">关闭</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                <#--</div>-->
            <#--</div>-->
        </div>
    </div>
</div>
<!--弹层-新增-END-->
</@bs.layout>