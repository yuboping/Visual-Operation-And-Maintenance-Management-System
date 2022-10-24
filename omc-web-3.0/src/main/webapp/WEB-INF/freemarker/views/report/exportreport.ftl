<@bs.layout [
    "layer/layer.css",
    "daterangepicker/daterangepicker.css",
    "bootstrap/bootstrap.min.css",
     "dpl.css"
] true true>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<div class="omc_main">
   <div class="omc-scroll" id="main" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">周期类型：</label>
                            <div class="controls">
                                 <select class="input-medium" id="report_type" name="report_type">
                                     <#if reporttypelist ??>
                                        <#list reporttypelist as type>
                                            <option value=${type.key} name="reporttype">${type.value}</option>
                                        </#list>
                                     </#if>
                                 </select>
                            </div>
                        </div>
                        <div class="span10">
                            <input  id="startnum" name="startnum" type="hidden" value="0">
                            <input  id="endnum" name="endnum" type="hidden" value="0">
                            <input  id="reportid" name="reportid" type="hidden" value="${reportid}">
                            <label class="control-label">时间:</label>
                            <div id="startDate_div" class="controls">
                                <input size="30" readonly="readonly" class=" input-medium" id="startdate" name="startdate" type="text">
                            </div>
                        </div>
                        <div class="span4">
                            <a href="#" id="querybutton" class="button button-primary">查询</a>
                        </div>
                    </div>
                    <!--查询条件-END-->
                    
                    <!--搜索结果-->
                    <div class="omc_table_box">
                        <div class="mb10 clearfix">
                             <div class="mb10 clearfix">
                                <div class="fl"> 共查询到 <span id="querynum" class="text-danger">0</span>条数据 </div>
                                <div class="fr">
                                    <a href="#" id="exportbut" class="button button-small button-primary">导出</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center " style="table-layout:fixed;">
                            <thead id="tb_head">
                                <tr>
                                    <th style="width:100%"></th>
                                </tr>
                            </thead>
                            <tbody id="tb_body"></tbody>
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
</@bs.layout>

<#-- 需要把JS放在下面,滚动条样式修改的function需要在页面渲染之后调用 -->
<@bs.useJS ["echarts/echarts.js","lcims/report/exportreport.js"]/>
