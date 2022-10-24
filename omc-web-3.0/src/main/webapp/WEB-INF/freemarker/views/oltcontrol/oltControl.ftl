<@bs.layout [
"jquery/jquery.js",
"bootstrap/bootstrap.min.css",
"bootstrap/bootstrap.min.js",
"multiple/bootstrap-multiselect.css",
"multiple/bootstrap-multiselect.js",
"echarts/echarts.js",
"lcims/oltcontrol/oltControl.js",
"dpl.css",
"layer/layer.css",
"laydate/laydate.js"
]  true true>

<!--main-->
<div class="omc_main">
    <div class="omc-scroll" id="mainhistory" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span6">
                            <label class="control-label" style="padding-top: 0px;">olt地址:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="oltAddress" name="oltAddress" type="text" maxlength="20">
                            </div>
                        </div>

                        <div class="span6">
                            <label class="control-label" style="padding-top: 0px;">olt地址:</label>
                            <div class="controls">
                                <select id="oltAddressType" name="oltAddressType" multiple="multiple">
                                    <#list paramlist as param>
                                        <option value="${param.oltIp}">${param.oltIp}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span4">
                            <a href="#" id="querybutton" class="button button-primary">查询</a>
                            <a href="#" id="resetbutton" class="button ml10">重置</a>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span8">
                            <label class="control-label" style="padding-top: 0px;">时间:</label>
                            <div id="endDate_div" class="controls">
                                <input size="30" readonly="readonly" class=" input-medium" id="date" name="date" type="text">
                            </div>
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
                                <th style="width:15%">olt地址</th>
                                <th style="width:15%">告警级别</th>
                                <th style="width:40%">告警内容</th>
                                <th style="width:30%">告警时间</th>
                            </tr>
                            </thead>
                            <tbody id="alarmhistorydiv"></tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                    <!--搜索结果-END-->
                </div>

                <#--折线图-->
                <div id="chartArea" style="width: 100%;"></div>
            </div>
        </div>
    </div>
    </@bs.layout>
