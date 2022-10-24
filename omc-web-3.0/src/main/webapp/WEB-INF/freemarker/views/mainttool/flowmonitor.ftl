<@bs.layout [
    "layer/layer.css",
    "lcims/mainttool/flowmonitor.js"
]  true true>
<!--main-->
<div class="omc_main" >
   <div id="mainarea" >
        <div>
            <div class="omc_main_tab" >
            <div class="omc_table_box">
                <div class="mb10 clearfix">
                    <div class="fr"> 共查询到 <span class="text-danger">${flowlist?size}</span>条数据 </div>
                </div>
                <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                    <thead>
                    <tr>
                        <th>流程名称</th>
                        <th>执行时间</th>
                        <th>执行结果</th>
                        <th width="250">操作</th>
                    </thead>
                    <tbody>
                    <#if flowlist?exists>
                        <#list flowlist as flow>
                        <tr>
                            <td>${flow.task_name}</td>
                            <td>${flow.create_time!"暂无记录"}</td>
                            <td>${flow.result_flag!"暂无记录"}</td>
                            <td>
                            <#if buttons?exists>
                                <#list buttons as button>
                                    <#if button.name == "flowmonitor_excute">
                                        <a class="J_execute pr10" href="#" task_id=${flow.task_id!} task_name=${flow.task_name!}>${button.show_name}</a>
                                    <#elseif button.name == "flowmonitor_edit">
                                        <a class="J_edit pr10" href="#" task_id=${flow.task_id!}>${button.show_name}</a>
                                    <#else>
                                        <a class="J_detail pr10" href="#" task_id=${flow.task_id!} task_name=${flow.task_name!} serial_num=${flow.serial_num!}>${button.show_name}</a>
                                    </#if>
                                </#list>
                            </#if>
                            </td>
                        </tr>
                        </#list>
                    <#else>
                        <tr>
                            <td colspan="4">暂无数据</td>
                        </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
            </div>
        </div>
    </div>
</div>
<!--end-main-->

<!--弹层-配置-->
<div class="mt40" id="data_edit" style="display: none;">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid mb10">
            <label class="control-label">流程名称</label>
            <div class="controls">
                <div id="task_name" style="margin-top: 8px;">流程名称</div>
            </div>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label">执行频率</label>
            <div class="controls">
                <input id="cron" size="16" class=" input-large" type="text">
                <span class="text-danger">*</span>
                <div class="note-error text-danger"></div>
            </div>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label">结果发送方式</label>
            <div class="controls">
                <div class="mt5" >
                    <select class="input-medium" id="send_type" name="type" >
                        <option value="1">邮件</option>
                        <option value="2">短信</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row row-fluid mb10">
            <label id="receive_label" class="control-label">邮件地址</label>
            <div class="controls">
                <input id="receiver" size="16" class=" input-large" type="text">
            </div>
        </div>
    </div>
</div>

</@bs.layout>  