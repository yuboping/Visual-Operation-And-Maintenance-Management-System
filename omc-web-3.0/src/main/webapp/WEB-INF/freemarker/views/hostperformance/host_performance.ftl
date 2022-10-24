<@bs.layout [
    "echarts/echarts.js",
    "lcims/hostperformance/hostperformance.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainhostperformance" >
        <div>
            <input id="menuId" value="${menuId}" type="hidden"/>
            <div class="omc_main_tab" >
                <!--搜索结果-->
                <div class="omc_table_box">
                    <div class="mb10 clearfix">
                         <div class="mb10 clearfix">
                            <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span> 条数据 | </div>
                            <div class="fl ml10">第 <span id="currnum" >1-10</span> 条数据 </div>
                            <div class="fr">
                            </div>
                         </div>
                    </div>
                    <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                        <thead>
                            <tr>
                                <th style="width:10%">主机名</th>
                                <th style="width:8%">主机IP</th>
                                <th style="width:8%">节点</th>
                                <th style="width:10%">cpu使用率(%)</th>
                                <th style="width:10%">内存使用率(%)</th>
                                <th style="width:8%">连通性</th>
                                <th style="width:45%">进程信息</th>
                            </tr>
                        </thead>
                        <tbody id="hostperformancediv"></tbody>
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