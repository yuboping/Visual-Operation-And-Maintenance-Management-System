<@bs.layout [
     "dpl.css",
    "layer/layer.css"
] true true>

<div class="omc_main">
   <div class="omc-scroll" id="main" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">报表名称:</label>
                            <div class="controls">
                                <input size="30" class="input-medium" id="reportname" name="reportname" type="text">
                            </div>
                        </div>
                        <div class="span4">
                            <a href="#" id="querybutton" class="button button-primary">查询</a>
                            <a href="#" id="resetbutton" class="button ml10">重置</a>
                        </div>
                    </div>
                    <!--查询条件-END-->
                    
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div class="fl"> 共查询到 <span id="querynum" class="text-danger">0</span>条数据 | </div>
                                <div class="fl ml10">第 <span id="currnum" >1-10</span>条数据 </div>
                                <div class="fr">
                                    <a href="#" id="addbutton" class="button button-small button-primary">新增</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center " style="table-layout:fixed;word-wrap:break-word;">
                            <thead>
                                <tr>
                                    <th style="width:15%">报表名称</th>
                                    <th style="width:13%">数据源</th>
                                    <th style="width:10%">报表周期</th>
                                    <th style="width:26%">日报表SQL</th>
                                    <th style="width:26%">月报表SQL</th>
                                    <th style="width:10%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="infodiv"></tbody>
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
<div class="display_none" id="add_div">
    <div class="pb5" >
       <div class="form-horizontal onlineTools clearfix" id="add_data_info_div">
            <div class="row row-fluid mt20">
                <div class="span12">
                    <label class="control-label">报表名称：</label>
                    <div class="controls">
                        <input  class="input-medium" id="add_report_name" name="add_report_name" type="text">
                        <span class="text-danger">*</span>
                    </div>
                </div>
                <div class="span12">
                    <label class="control-label">数据源：</label>
                    <div class="controls">
                         <select class="input-medium" id="add_datasourceid" name="add_datasourceid"></select>
                         <span class="text-danger">*</span>
                    </div>
                </div>
            </div>
            <div class="row row-fluid">
               <label class="control-label">日报表查询SQL：</label>
                <div class="controls">
                    <textarea class="report_sql_textarea" type="text" id="add_report_sql_day" placeholder="select * from table" name="report_sql"></textarea>
                    <span class="text-danger">*</span>
                </div>
            </div>
            <div class="row row-fluid">
               <label class="control-label">月报表查询SQL：</label>
                <div class="controls">
                    <textarea class="report_sql_textarea" type="text" id="add_report_sql_mon" placeholder="select * from table" name="report_sql"></textarea>
                    <span class="text-danger">*</span>
                </div>
            </div>
            <div class="row row-fluid">
                    <label class="control-label">时间字段列名：</label>
                    <div class="controls">
                        <input  class="input-medium" id="add_report_timefield" placeholder="timefield" name="add_report_timefield" type="text">
                        <span class="text-danger"> 例：</span>
                        <span class="report_sql_text_italic"> 
                            <strong>select * from table</strong> where 
                            <strong>timefield</strong>='2017-01-02';
                        </span>
                    </div>
            </div>
            <div class="row row-fluid">
                <a href="#" id="add_but_matchsql" name="add_but_matchsql" class="button button-primary ml325">查询</a>
                <a href="#" id="add_but_reset" name="add_but_reset" class="button ml10">重置</a>
            </div>
        </div>
        <div class="popup_box" style="">
            <div class="form-horizontal onlineTools mt20" id="add_fieldinfo_div">
            </div>
        </div>
     </div>
</div>
<!--弹层-新增-END-->

<!--弹层-修改-->
<div class="display_none" id="modify_div">
    <div class="pb5" >
       <div class="form-horizontal onlineTools clearfix" id="modify_data_info_div">
            <div class="row row-fluid mt20">
                <div class="span12">
                    <label class="control-label">报表名称：</label>
                    <div class="controls">
                        <input  class="input-medium" id="modify_report_name" name="modify_report_name" type="text">
                        <input  class="input-medium" id="modify_report_id" name="modify_report_id" type="hidden">
                        <span class="text-danger">*</span>
                    </div>
                </div>
                <div class="span12">
                    <label class="control-label">数据源：</label>
                    <div class="controls">
                         <select class="input-medium" id="modify_datasourceid" name="modify_datasourceid"></select>
                         <span class="text-danger">*</span>
                    </div>
                </div>
            </div>
            <div class="row row-fluid">
               <label class="control-label">日报表查询SQL：</label>
                <div class="controls">
                    <textarea class="report_sql_textarea" type="text" id="modify_report_sql_day" placeholder="select * from table" name="report_sql"></textarea>
                    <span class="text-danger">*</span>
                </div>
            </div>
            <div class="row row-fluid">
               <label class="control-label">月报表查询SQL：</label>
                <div class="controls">
                    <textarea class="report_sql_textarea" type="text" id="modify_report_sql_mon" placeholder="select * from table" name="report_sql"></textarea>
                    <span class="text-danger">*</span>
                </div>
            </div>
            <div class="row row-fluid">
                    <label class="control-label">时间字段列名：</label>
                    <div class="controls">
                        <input  class="input-medium" id="modify_report_timefield" placeholder="timefield" name="modify_report_timefield" type="text">
                        <span class="text-danger"> 例：</span>
                        <span class="report_sql_text_italic"> 
                            <strong>select * from table</strong> where 
                            <strong>timefield</strong>='2017-01-02';
                        </span>
                    </div>
            </div>
            <div class="row row-fluid mb10">
                <a href="#" id="modify_but_matchsql" name="modify_but_matchsql" class="button button-primary ml325">查询</a>
                <a href="#" id="modify_but_reset" name="modify_but_reset" class="button ml10">重置</a>
            </div>
        </div>
        <div class="popup_box" style="">
            <div class="form-horizontal onlineTools mt20" id="modify_fieldinfo_div">
            </div>
        </div>
     </div>
</div>
<!--弹层-修改-END-->

</@bs.layout>

<#-- 需要把JS放在下面,滚动条样式修改的function需要在页面渲染之后调用 -->
<@bs.useJS ["echarts/echarts.js","lcims/report/report.js"]/>
