<@bs.layout [
"echarts/echarts.js",
"lcims/hostprocessmanage/hostprocessmanage.js",
"layer/layer.css",
"dpl.css"
]  true true>

<!--main-->
<div class="omc_main">
    <div class="omc-scroll" id="hostprocessmanage">
        <input type="hidden" value="${operateid}" id="operateid"/>
        <input type="hidden" value="${operatetype}" id="operatetype"/>
        <input type="hidden" value="" id="idlist" name = "idlist"/>
        <div>
            <div class="omc_main_tab">
                <!--查询条件-->
                <div class="form-horizontal onlineTools">
                    <div class=" mb10  clearfix">
                        <div class="tag_box fl" id="operate_menu_1">
                        </div>
                    </div>

                    <div id="query_div" class="form-horizontal onlineTools display_none">
                        <div class=" mb10  clearfix">
                            <div class="tag_box fl">
                                <div class="span10">
                                    <label class="control-label">主机:</label>
                                    <div class="controls">
                                        <select class="input-medium" id="query_hostid" name="query_hostid">
                                            <option value="">全部</option>
                                            <#list hostlist as host>
                                    	        <option value="${host.hostid}">${host.hostname}</option>
                                            </#list>
                                        </select>
                                        <input type="hidden" value="" id="query_hostid_page"/>
                                    </div>
                                </div>
                                <div class="span10">
                                    <label class="control-label">进程:</label>
                                    <div class="controls">
                                        <select class="input-medium" id="query_metricid" name="query_metricid">
                                            <option value="">全部</option>
                                            <#list processlist as process>
                                    	        <option value="${process.process_id}">${process.process_name}</option>
                                            </#list>
                                        </select>
                                        <input type="hidden" value="" id="query_metricid_page"/>
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
                                    <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span>条数据 |</div>
                                    <div class="fl ml10">第 <span id="currnum">1-10</span>条数据</div>
                                    <input type="hidden" value="" id="page_curr"/>
                                    <div id="operate_menu_2" class="fr">
                                    </div>
                                </div>
                            </div>
                            <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                                <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll"/>
                                    </th>
                                    <th style="width:10%">主机名称</th>
                                    <th style="width:10%">主机IP</th>
                                    <th style="width:8%">进程名称</th>
                                    <th style="width:8%">进程标识</th>
                                    <th style="width:20%">启动脚本</th>
                                    <th style="width:20%">停止脚本</th>
                                    <th style="width:16%">描述</th>
                                    <th style="width:8%">执行结果</th>
                                </tr>
                                </thead>
                                <tbody id="hostprocessdiv">
                                </tbody>
                            </table>
                            <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                            <div class="ad-page-outer clearfix "></div>
                        </div>
                        <!--搜索结果-END-->
                        <div/>
                    </div>
                </div>

                <div id="bind_div" class="form-horizontal onlineTools">

                    <div class=" mb10  clearfix">
                        <!--查询条件-START-->
                        <div class="tag_box fl">
                            <div class="span10">
                                <label class="control-label">类型:</label>
                                <div class="controls">
                                    <select class="input-medium" id="bind_operatetype" name="bind_operatetype">
                                        <option value="">请选择</option>
                                        <#list sourcetypelist as param>
                                            <option value="${param.code}">${param.description}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="span10">
                                <label class="control-label">项:</label>
                                <div class="controls">
                                    <select class="input-medium" id="bind_operateid" name="bind_operateid">

                                    </select>
                                </div>
                            </div>
                            <div class="span4">
                            </div>
                        </div>
                    </div>
                    <!--查询条件-END-->
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                            <div class="mb10 clearfix">
                                <div id="operate_menu_2" class="fr">
                                    <a href="#" id="bindbutton" class="button button-small button-primary">提交</a>
                                </div>
                            </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                            <tr>
                                <th style="width:5%"><input type="checkbox" id="checkboxAddAll" name="checkboxAddAll"/>
                                </th>
                                <th style="width:10%">主机名称</th>
                                <th style="width:10%">主机ip</th>
                                <th style="width:8%">进程名称</th>
                                <th style="width:10%">进程标识</th>
                                <th style="width:20%">启动脚本</th>
                                <th style="width:20%">停止脚本</th>
                                <th style="width:25%">描述</th>
                            </tr>
                            </thead>
                            <tbody id="hostprocessrepdiv">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!--end-main-->

        <!--弹层-编辑-->
        <div class="mt10 display_none" id="modify_div">
            <div class="form-horizontal onlineTools">
                <input id="modify_id" type="hidden">
                <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">主机IP:</td>
                        <td style="padding-left:10px;"><input id="modify_host_id" disabled="disabled"
                                                              readonly="readonly" style="width: 150px;" class=""
                                                              type="text"></td>
                        <td style="text-align:right;padding-left:10px;">进程ID:</td>
                        <td style="padding-left:10px;"><input id="modify_process_id" disabled="disabled" readonly="readonly"
                                                              style="width: 150px;" class="" type="text"></td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">进程标识:</td>
                        <td style="padding-left:10px;"><input id="modify_process_key" disabled="disabled" readonly="readonly"
                                                              style="width: 150px;" class="" maxlength="40"
                                                              type="text"></td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">启动脚本:</td>
                        <td colspan="3" style="padding-left:10px;"><input id="modify_start_script" size="100" maxlength="100"
                                                                          style="width: 400px;" class="" type="text">
                        </td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">停止脚本:</td>
                        <td colspan="3" style="padding-left:10px;"><input id="modify_stop_script" size="100" maxlength="100"
                                                                          style="width: 400px;" class="" type="text">
                        </td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">描述:</td>
                        <td colspan="3" style="padding-left:10px;"><input id="modify_description" size="100" maxlength="100"
                                                                          style="width: 400px;" class="" type="text">
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

        <!--弹层-详情-->
        <div class="mt10 display_none" id="detail_div">
            <div class="form-horizontal onlineTools">
                <input id="detail_nodeid" type="hidden">
                <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">主机IP:</td>
                        <td style="padding-left:10px;"><input id="host_ip" readonly="readonly" style="width: 150px;" class=""
                                                              type="text"></td>
                        <td style="text-align:right;padding-left:10px;">进程名称:</td>
                        <td style="padding-left:10px;"><input id="process_name" readonly="readonly"
                                                              style="width: 150px;" class="" type="text"></td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">执行类型:</td>
                        <td style="padding-left:10px;"><select id="script_type" disabled="disabled" style="width: 150px;">
                                <#list scriptTypelist as param>
                                    <option value="${param.code}">${param.description}</option>
                                </#list>
                        </select>
                        </td>
                        <td style="text-align:right;padding-left:10px;">执行状态:</td>
                        <td style="padding-left:10px;"><select id="operate_state" disabled="disabled" style="width: 150px;">
                                <#list statetypelist as param>
                                    <option value="${param.code}">${param.description}</option>
                                </#list>
                        </select>
                        </td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">执行脚本:</td>
                        <td colspan="3" style="padding-left:10px;"><input id="show_script" size="100" readonly="readonly"
                                                                          style="width: 400px;" class="" type="text">
                        </td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">执行结果:</td>
                        <td style="padding-left:10px;"><input id="operate_result" style="width: 150px;" readonly="readonly" class=""
                                                              type="text"></td>
                    </tr>
                    <tr style="">
                        <td style="text-align:right;padding-left:10px;">执行时间:</td>
                        <td colspan="3" style="padding-left:10px;"><input id="create_time" style="width: 400px;" readonly="readonly" class=""
                                                              type="text"></td>
                    </tr>
                </table>
                <div class="row row-fluid mb10">
                    <br/>
                    <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
                </div>
            </div>
        </div>
        
    <!--弹层-自动启动-->
    <div class="mt10 display_none" id="autostartex_div">
        <div class="form-horizontal onlineTools">
            <input id="autostartex_metricid" type="hidden">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">启动时间:</td>
                    <td colspan="3" style="padding-left:10px;"><input id="autostartex_date" style="width: 400px;" readonly="readonly" class=""
                                                              type="text"></td>
            </table>
            <div class="row row-fluid mb10">
                <br/>
                <a href="#" id="autostartex_ok" style="margin-left: 265px;" class="button button-primary ml200">确定</a>
            </div>
        </div>
    </div>
    <!--弹层-自动启动-END-->

</@bs.layout>