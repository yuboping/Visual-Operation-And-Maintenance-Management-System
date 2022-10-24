<@bs.layout [
    "echarts/echarts.js",
    "lcims/shcm/authensituation.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css"

]  true true>

<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="authFailReasonReportDiv" >
        <div>
            <input type="hidden" id="dayDate" name="dayDate" value="${dayDate}"/>
            <input type="hidden" id="monthDate" name="monthDate" value="${monthDate}"/>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">统计维度:</label>
                            <div class="controls">
                                <select class="input-medium" id="query_type">
                                    <option value="1">按天</option>
                                    <option value="2">按月</option>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label" style="padding-top: 0px;">日期:</label>
                            <div id="startDate_div" class="controls">
                                <input size="30" readonly="readonly" class=" input-medium" id="daydate" name="daydate" type="text">
                                <input size="30" readonly="readonly" class=" input-medium" id="monthdate" name="monthdate" type="text">
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">BRSIP:</label>
                            <div class="controls">
                                <select class="input-medium" id="brasip" name="brasip" >
                                    <option value="">全部</option>
                                    <#list naslist as nas>
                                        <option value=${nas.nas_ip}>${nas.nas_ip}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <div class="controls">
                                <a href="#" id="querybutton" class="button button-primary">查询</a>
                                <a href="#" id="resetbutton" class="button ml10">重置</a>
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
                             <div id="operate_menu" class="fr"></div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:25%">统计时间</th>
                                    <th style="width:25%">认证成功数</th>
                                    <th style="width:25%">认证失败数</th>
                                    <th style="width:25%">总认证数</th>
                                </tr>
                            </thead>
                            <tbody id="alarmhistorydiv"></tbody>
                        </table>
                        <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                        <div class="ad-page-outer clearfix "></div>
                    </div>
                <!--搜索结果-END-->
                </div>
                <!--折线图-->
                <div  style="width:100%;height:370px;margin-right:10px;padding:15px 0;padding-right:9px;" id="chartdiv">
                    <div style="width:95%;height:90%;background: #fff;border:1px solid #ebebea;" id="authFailReasonChartShow">
                    
                    </div>
                </div>
            </div>
        </div>
</div>
<!--end-main-->
</@bs.layout>
