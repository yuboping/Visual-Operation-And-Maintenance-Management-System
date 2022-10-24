<@bs.layout [
    "echarts/echarts.js",
    "lcims/gdcu/offlineBatchuser.js",
    "mainttool.css",
    "layer/layer.css",
    "laypage/laypage.css",
    "bootstrap/bootstrap.min.css",
    "daterangepicker/daterangepicker.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainarea" >
        <div>
            <div class="omc_main_tab" >
                <div class="form-horizontal ">
                    <div class="row row-fluid">
                        <div class="span6">
                            <label class="control-label">Brasip</label>
                            <div class="controls">
                                <input size="16" id="brasip" class="input-medium" type="text">
                            </div>
                        </div>
                        <div class="span4">
                            <div class="controls">
                                <div class="input-position">
                                    <a id="offlineuser" href="#" class="J_info button button-primary">提交命令</a>
                                </div>
                            </div>
                        </div>
                        <div class="span4">
                            <div class="controls">
                                <div class="input-position">
                                    <a id="query" href="#" class="J_info button button-primary">查询历史操作</a>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

            <div class="omc_table_box">
                <div class="mb10 clearfix">
                    <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span>条数据 </div>
                    <div class="fr"> 第 <span id="currnum" >1-10</span>条数据</div>
                </div>
                <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                    <thead>
                    <tr>
                        <th>Brasip</th>
                        <th>批量下线原因</th>
                        <th>操作提交时间</th>
                    </tr>
                    </thead>
                    <tbody id="recordtable">
                    </tbody>
                </table>
                <div id="pageinfo" class="parts_down_page clearfix">
                    
                </div>
            </div>
            </div>
        </div>
    </div>
</div>
<!--end-main-->
<!--弹层-提出指令-->
<div  id="tool_info">
    <div class="omc_tool_open" id="omcOpen" style="display: none;">
        <div class="f18">该BAS共有 <span id="totalnum" class="text-danger f24">0</span> 个用户在线,确认下线所有用户吗?</div>
        <div>
            <p class="pt10">请记录下线所有用户的原因：</p>
            <div>
                <textarea id="offlineReason"></textarea>
            </div>
            <div class="mt20 fr"><a id="submitCommand" href="#" class="button button-primary">确&nbsp;&nbsp;认</a><a href="#" class="omc-loadclose button ml10">取&nbsp;&nbsp;消</a></div>
        </div>
    </div>
    <!--加载-->
    <div class="openloading"  id="openLoading" style="display: none;">
            <span><img src="/images/mainttool/loading-0.gif">正在发送指令,请稍等...</span>
    </div>
    <!--加载结果-->
    <div class="loadingresult" id="loadingresult" style="display: none;">
        <div class="clearfix">
            <i class="iconfont icon-gou text-success "></i>
            <div class="loadingresult_title ">指令发送成功!</div>
            <!--<i class="iconfont icon-cha text-danger  "></i>-->
            <!--<div class="loadingresult_title">指令发送失败:调用obs接口查询basip用户在线数失败!</div>-->
        </div>
        <div class="mt10"><a href="#" class="omc-loadclose button button-primary">确&nbsp;&nbsp;认</a></div>
    </div>
</div>
</@bs.layout>