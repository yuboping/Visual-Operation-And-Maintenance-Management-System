<@bs.layout [
"dpl.css",
"global.css",
"bootstrap/bootstrap.min.css",
"bootstrap/font-awesome.min.css",
"ais/font-awesome.min.css",
"ais/icon-style.css",
"ais/ais.css",
"layer/layer.css",
"require/require.js",
"lcims/ais/plan.js"
] false false>
<!--内容区域-->
<div class="omc_main_plan omc_ais" >
    <div id="mainplan">
        <div class="dap-ais-mainarea_plan">
            <div class="dap-ais-mainarea-bg">
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
                                        <#--<option value="year" selected="selected">每年</option>-->
                                        <#--<option value="month">每月</option>-->
                                        <#--<option value="week">每周</option>-->
                                        <#--<option value="day">每天</option>-->
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
            </div>
        </div>
    </div>
</div>
</@bs.layout>