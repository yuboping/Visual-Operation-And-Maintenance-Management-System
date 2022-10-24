<@bs.layout [
    "echarts/echarts.js",
    "lcims/mainttool/hostcapability.js",
    "layer/layer.css",
    "dpl.css",
    "bootstrap/bootstrap.min.css"

]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainhostcap" >
       <div>
            <div class="omc_main_tab" >
                <!--查询条件-->
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">主机ip:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="name" name="name" type="text" maxlength="20">
                                </div>
                            </div>
                            <div class="span4">
                                <a  id="querybutton" class="button button-primary">查询</a>
                                <a  id="resetbutton" class="button ml10">重置</a>
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
                                <th style="width:17%">主机名称</th>
                                <th style="width:15%">ip</th>
                                <th style="width:12%">cpu</th>
                                <th style="width:12%">cpu占用率</th>
                                <th style="width:12%">内存</th>
                                <th style="width:12%">内存占用率</th>
                                <th style="width:20%">统计时间</th>
                            </tr>
                        </thead>
                        <tbody id="hostcapdiv"></tbody>
                    </table>
                    <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                    <div class="ad-page-outer clearfix "></div>
                </div>
                <!--搜索结果-END-->
            </div>
        </div>
    </div>
</div>
</@bs.layout>