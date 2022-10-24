<@bs.layout [
"echarts/echarts.js",
"lcims/gscm5G/collectormanage.js",
"layer/layer.css",
"dpl.css"
]  true true>
    <!--main-->
    <div class="omc_main">
        <div class="omc-scroll" id="mainhost" >
            <div>
                <!--查询条件-->
                <div class="omc_main_tab" >
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">主机名:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="hostname" name="hostname" type="text" maxlength="20">
                                </div>
                            </div>
                            <div class="span10">
                                <label class="control-label">主机IP:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="addr" name="addr" type="text" maxlength="20">
                                </div>
                            </div>
                        </div>
                        <div class="row row-fluid">
                            <div class="span20" id="nodeids">
                                <label class="control-label" >节点:</label>
                                <div class="controls">
                                    <select class="input-medium" id="nodeid" name="nodeid">
                                        <option value="">全部</option>
                                        <#list nodeList as node>
                                            <option value=${node.id}>${node.node_name}</option>
                                        </#list>
                                    </select>
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
                                    <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span>条数据 | </div>
                                    <div class="fl ml10">第 <span id="currnum" >1-10</span>条数据 </div>
                                    <div id="operate_menu" class="fr"></div>
                                </div>
                            </div>
                            <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                                <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:9%">IP地址</th>
                                    <th style="width:15%">主机名</th>
<#--                                    <th style="width:14%">主机类型</th>-->
<#--                                    <th style="width:15%">型号</th>-->
                                    <th style="width:20%">操作系统</th>
                                    <th style="width:10%">节点</th>
                                    <th style="width:12%">操作</th>
                                </tr>
                                </thead>
                                <tbody id="hostdiv"></tbody>
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
    <div class="mt10 display_none" id="add_div">
        <div class="form-horizontal onlineTools">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr>
                    <td style="text-align:right;padding-left:10px;">
                        <div>
                            <label><input type="checkbox" name="collect" id="add_isCollect" value="0">自动补采</label>
                        </div>
                    </td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">主机名称:</td>
                    <td style="padding-left:10px;"><input id="add_hostname" size="30" style="width: 150px;" class="" type="text" maxlength="100"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">主机IP:</td>
                    <td style="padding-left:10px;"><input id="add_addr" size="30" style="width: 150px;" class="" type="text" maxlength="100"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">所属节点:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_nodeid"></select> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">主机类型:</td>
                    <td style="padding-left:10px;"><span id="add_hosttype"></span></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">位置:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="add_host_room" size="30" style="width: 380px;" class="" type="text" maxlength="100"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">机柜号:</td>-->
<#--                    <td style="padding-left:10px;"><input id="add_rack_num" size="30" style="width: 150px;" class="" type="text" maxlength="40"></td>-->
<#--                    <td style="text-align:right;padding-left:10px;">主机型号:</td>-->
<#--                    <td style="padding-left:10px;"><input id="add_productname" size="30" style="width: 150px;" class="" type="text" maxlength="64"> <span class="text-danger">*</span></td>-->
<#--                </tr>-->
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">操作系统:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="add_os" size="50" style="width: 380px;" class="" type="text" maxlength="100"> <span class="text-danger">*</span></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">主机CPU:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="add_cpu" size="30" style="width: 380px;" class="" type="text" maxlength="64"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">主机内存:</td>-->
<#--                    <td style="padding-left:10px;"><input id="add_memory" size="30" style="width: 150px;" class="" type="text" maxlength="64"></td>-->
<#--                    <td style="text-align:right;padding-left:10px;">主机磁盘:</td>-->
<#--                    <td style="padding-left:10px;"><input id="add_diskspace" size="30" style="width: 150px;" class="" type="text" maxlength="64"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">序列号:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="add_serialnumber" size="30" style="width: 380px;" class="" type="text" maxlength="40"></td>-->
<#--                </tr>-->
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">IPV4:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="add_ipv4" size="30" style="width: 380px;" class="" type="text" maxlength="100"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">IPV6:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="add_ipv6" size="30" style="width: 380px;" class="" type="text" maxlength="100"></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">图位置:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="add_location" size="20" style="width: 380px;" value="0" class="" type="text" maxlength="20"></td>-->
<#--                </tr>-->
            </table>
            <div class="row row-fluid mb10">
                <a href="#" id="add_ok" class="button button-primary ml200">确认</a>
                <a href="#" id="add_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-新增-END-->

    <!--弹层-修改-->
    <div class="mt10 display_none" id="modify_div">
        <div class="form-horizontal onlineTools">
            <input id="modify_hostid" type="hidden">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr>
                    <td style="text-align:right;padding-left:10px;">
                        <div>
                            <label><input type="checkbox" name="collect" id="modify_isCollect" value="0">自动补采</label>
                        </div>
                    </td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">主机名称:</td>
                    <td style="padding-left:10px;"><input id="modify_hostname" size="30" style="width: 150px;" class="" type="text" maxlength="100"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">主机IP:</td>
                    <td style="padding-left:10px;"><input id="modify_addr" disabled="disabled" size="30" style="width: 150px;" class="" type="text" maxlength="100"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">所属节点:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_nodeid"></select> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">主机类型:</td>
                    <td style="padding-left:10px;"><span id="modify_hosttype">采集机主机</span></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">位置:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="modify_host_room" size="30" style="width: 380px;" class="" type="text" maxlength="100"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">机柜号:</td>-->
<#--                    <td style="padding-left:10px;"><input id="modify_rack_num" size="30" style="width: 150px;" class="" type="text" maxlength="40"></td>-->
<#--                    <td style="text-align:right;padding-left:10px;">主机型号:</td>-->
<#--                    <td style="padding-left:10px;"><input id="modify_productname" size="30" style="width: 150px;" class="" type="text" maxlength="64"> <span class="text-danger">*</span></td>-->
<#--                </tr>-->
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">操作系统:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="modify_os" size="50" style="width: 380px;" class="" type="text" maxlength="100"> <span class="text-danger">*</span></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">主机CPU:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="modify_cpu" size="30" style="width: 380px;" class="" type="text" maxlength="64"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">主机内存:</td>-->
<#--                    <td style="padding-left:10px;"><input id="modify_memory" size="30" style="width: 150px;" class="" type="text" maxlength="64"></td>-->
<#--                    <td style="text-align:right;padding-left:10px;">主机磁盘:</td>-->
<#--                    <td style="padding-left:10px;"><input id="modify_diskspace" size="30" style="width: 150px;" class="" type="text" maxlength="64"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">序列号:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="modify_serialnumber" size="30" style="width: 380px;" class="" type="text" maxlength="40"></td>-->
<#--                </tr>-->
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">IPV4:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="modify_ipv4" size="30" style="width: 380px;" class="" type="text" maxlength="100"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">IPV6:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="modify_ipv6" size="30" style="width: 380px;" class="" type="text" maxlength="100"></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">图位置:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="modify_location" size="20" style="width: 380px;" class="" type="text" maxlength="20"></td>-->
<#--                </tr>-->
            </table>
            <div class="row row-fluid mb10">
                <a href="#" id="modify_ok" class="button button-primary ml200">确认</a>
                <a href="#" id="modify_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-修改-END-->

    <!--弹层-详情-->
    <div class="mt10 display_none" id="detail_div">
        <div class="form-horizontal onlineTools">
            <input id="detail_hostid" type="hidden">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr>
                    <td style="text-align:right;padding-left:10px;">
                        <div>
                            <label><input type="checkbox" name="collect" id="detail_isCollect" value="0">自动补采</label>
                        </div>
                    </td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">主机名称:</td>
                    <td style="padding-left:10px;"><input id="detail_hostname" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">主机IP:</td>
                    <td style="padding-left:10px;"><input id="detail_addr" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">所属节点:</td>
                    <td style="padding-left:10px;"><input class="" style="width: 150px;" readonly="readonly" id="detail_nodeid" type="text"></input></td>
                    <td style="text-align:right;padding-left:10px;">主机类型:</td>
                    <td style="padding-left:10px;"><input class="" size="30" style="width: 150px;" readonly="readonly" id="detail_hosttype" type="text"></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">位置:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="detail_host_room" size="30" readonly="readonly" style="width: 380px;" class="" type="text"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">机柜号:</td>-->
<#--                    <td style="padding-left:10px;"><input id="detail_rack_num" readonly="readonly" size="30" style="width: 150px;" class="" type="text"></td>-->
<#--                    <td style="text-align:right;padding-left:10px;">主机型号:</td>-->
<#--                    <td style="padding-left:10px;"><input id="detail_productname" readonly="readonly" size="30" style="width: 150px;" class="" type="text"></td>-->
<#--                </tr>-->
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">操作系统:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="detail_os" readonly="readonly" size="50" style="width: 380px;" class="" type="text"></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">主机CPU:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="detail_cpu" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">主机内存:</td>-->
<#--                    <td style="padding-left:10px;"><input id="detail_memory" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>-->
<#--                    <td style="text-align:right;padding-left:10px;">主机磁盘:</td>-->
<#--                    <td style="padding-left:10px;"><input id="detail_diskspace" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>-->
<#--                </tr>-->
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">序列号:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="detail_serialnumber" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>-->
<#--                </tr>-->
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">IPV4:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="detail_ipv4" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">IPV6:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="detail_ipv6" readonly="readonly" size="30" style="width: 380px;" class="" type="text"></td>
                </tr>
<#--                <tr style="">-->
<#--                    <td style="text-align:right;padding-left:10px;">图位置:</td>-->
<#--                    <td style="padding-left:10px;" colspan="3"><input id="detail_location" readonly="readonly" size="20" style="width: 380px;" class="" type="text"></td>-->
<#--                </tr>-->
            </table>
            <div class="row row-fluid mb10" style="margin-top: 22px;">
                <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
            </div>
        </div>
    </div>
    <!--弹层-修改-END-->

</@bs.layout>