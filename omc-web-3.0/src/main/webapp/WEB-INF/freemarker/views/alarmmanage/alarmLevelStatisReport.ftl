<@bs.layout [
    "echarts/echarts.js",
    "lcims/alarmmanage/alarmlevelstatisreport.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css"

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
                                    <option value="2">月表</option>
                                    <option value="1">周表</option>
                                    <option value="0">日表</option>
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
                             <div id="operate_menu" class="fr"></div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:10%">告警时间</th>
                                    <th style="width:20%">普通</th>
                                    <th style="width:25%">警告</th>
                                    <th style="width:20%">严重</th>
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
<!--end-main-->
</@bs.layout>
