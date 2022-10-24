<@bs.layout [
"echarts/echarts.js",
"lcims/gscm5G/thirdparty.js",
"layer/layer.css",
"dpl.css"
]  true true>
    <!--main-->
<#--第三方接口设备管理-->
    <div class="omc_main">
        <div class="omc-scroll" id="mainbas" >
            <div>
                <!--查询条件-->
                <div class="omc_main_tab" >
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">设备名称:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="thirdparty_name" name="thirdparty_name" type="text">
                                </div>
                            </div>
                            <div class="span10">
                                <label class="control-label">设备IP:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="thirdparty_ip" name="thirdparty_ip" type="text">
                                </div>
                            </div>
                        </div>
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">IP类型:</label>
                                <div class="controls">
                                    <select class="input-medium" id="ip_type" name="ip_type" >
                                        <option value="">全部</option>
                                        <#list mdParamList as mdParam>
                                            <option value=${mdParam.code}>${mdParam.description}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="span10">
                                <label class="control-label">属地名称:</label>
                                <div class="controls">
                                    <select class="input-medium" id="area_no" name="area_no" >
                                        <option value="">全部</option>
                                        <#list mdAreaList as mdParam>
                                            <option value=${mdParam.areano}>${mdParam.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="span4">
                                <a id="querybutton" class="button button-primary">查询</a>
                                <a id="resetbutton" class="button ml10">重置</a>
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
                                <div id = "operate_menu" class="fr" >
                                </div>
                            </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                            <tr>
                                <th style="width:10%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                <th style="width:15%">设备名称</th>
                                <th style="width:15%">设备IP</th>
                                <th style="width:15%">设备型号</th>
                                <th style="width:15%">厂家名称</th>
                                <th style="width:15%">属地名称</th>
                                <th style="width:15%">端口</th>
                                <th style="width:15%">用户名</th>
                                <th style="width:15%">密码</th>
                                <th style="width:15%">文件路径</th>
                                <th style="width:15%">操作</th>
                            </tr>
                            </thead>
                            <tbody id="thirdpartydiv"></tbody>
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
            <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">设备名称:</td>
                    <td style="padding-left:10px;"><input id="add_thirdparty_name" size="20" maxlength="20" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">设备IP:</td>
                    <td style="padding-left:10px;"><input id="add_thirdparty_ip" size="30" maxlength="30" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">厂家名称:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_factory_name"></select> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">设备型号:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_equip_name"></select> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">属地名称：</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_area_name"></select> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">IP类型:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="add_ip_type"></select> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">端口:</td>
                    <td style="padding-left:10px;"><input id="add_port" size="20" maxlength="20" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">用户名:</td>
                    <td style="padding-left:10px;"><input id="add_username" size="30" maxlength="30" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">密码:</td>
                    <td style="padding-left:10px;"><input id="add_password" size="20" maxlength="20" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">文件路径:</td>
                    <td style="padding-left:10px;"><input id="add_file_path" size="30" maxlength="30" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">描述:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="add_description" size="50" maxlength="100" style="width: 380px;" class="" type="text"></td>
                </tr>
            </table>
            <div class="row row-fluid mb10">
                <br/>
                <a href="#" id="add_ok" class="button button-primary" style="margin-left: 220px;">确认</a>
                <a href="#" id="add_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-新增-END-->

    <!--弹层-修改-->
    <div class="mt10 display_none" id="modify_div">
        <div class="form-horizontal onlineTools">
            <input id="modify_basid" type="hidden">
            <table style="margin-left:0px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">设备名称:</td>
                    <td style="padding-left:10px;"><input id="modify_thirdparty_name" size="20" maxlength="20" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">设备IP:</td>
                    <td style="padding-left:10px;"><input id="modify_thirdparty_ip" size="30" maxlength="30" disabled="disabled" style="width: 150px;" class="" type="text"> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">厂家名称:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_factory_name"></select> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">设备型号:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_equip_name"></select> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">属地名称：</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_area_name"></select> <span class="text-danger">*</span></td>
                    <td style="text-align:right;padding-left:10px;">IP类型:</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="modify_ip_type"></select> <span class="text-danger">*</span></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">端口:</td>
                    <td style="padding-left:10px;"><input id="modify_port" size="20" maxlength="20" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">用户名:</td>
                    <td style="padding-left:10px;"><input id="modify_username" size="30" maxlength="30" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">密码:</td>
                    <td style="padding-left:10px;"><input id="modify_password" size="20" maxlength="20" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">文件路径:</td>
                    <td style="padding-left:10px;"><input id="modify_file_path" size="30" maxlength="30" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">描述:</td>
                    <td style="padding-left:10px;" colspan="3"><input id="modify_description" size="50" maxlength="100" style="width: 380px;" class="" type="text"> </td>
                </tr>
            </table>
            <div class="row row-fluid mb10">
                <br/>
                <a href="#" id="modify_ok" class="button button-primary" style="margin-left: 220px;">确认</a>
                <a href="#" id="modify_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!--弹层-修改-END-->

    <!--弹层-详情-->
    <div class="mt10 display_none" id="detail_div">
        <div class="form-horizontal onlineTools">
            <input id="detail_basid" type="hidden">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">设备名称:</td>
                    <td style="padding-left:10px;"><input id="detail_thirdparty_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">设备IP:</td>
                    <td style="padding-left:10px;"><input id="detail_thirdparty_ip" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">设备型号:</td>
                    <td style="padding-left:10px;"><input id="detail_equip_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">厂家名称:</td>
                    <td style="padding-left:10px;"><input id="detail_factory_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">属地名称:</td>
                    <td style="padding-left:10px;"><input id="detail_area_name" size="30" readonly="readonly" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">IP类型:</td>
                    <td style="padding-left:10px;"><input id="detail_ip_type" class="" style="width: 150px;" size="30" readonly="readonly" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">端口:</td>
                    <td style="padding-left:10px;"><input id="detail_port" size="20" maxlength="20" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">用户名:</td>
                    <td style="padding-left:10px;"><input id="detail_username" size="30" maxlength="30" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">密码:</td>
                    <td style="padding-left:10px;"><input id="detail_password" size="20" maxlength="20" style="width: 150px;" class="" type="text"></td>
                    <td style="text-align:right;padding-left:10px;">文件路径:</td>
                    <td style="padding-left:10px;"><input id="detail_file_path" size="30" maxlength="30" style="width: 150px;" class="" type="text"></td>
                </tr>
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">描述:</td>
                    <td style="padding-left:10px;" colspan="3"><textarea id="detail_description" readonly="readonly" size="50" style="width: 380px;resize:none;" class="" type="text"></textarea></td>
                </tr>
            </table>
            <div class="row row-fluid mb10">
                <br/>
                <a href="#" id="detail_ok" style="margin-left: 265px;" class="button button-primary ml200">关闭</a>
            </div>
        </div>
    </div>
    <!--弹层-详情-END-->

    <!-- 弹出--设置属地--START-->
    <div class="mt10 display_none" id="set_area_div">
        <div class="form-horizontal onlineTools">
            <table style="margin-left:40px;border-collapse:separate;border-spacing:0px 5px;">
                <tr style="">
                    <td style="text-align:right;padding-left:10px;">属地名称：</td>
                    <td style="padding-left:10px;"><select class="" style="width: 150px;" id="set_area_name"></select> <span class="text-danger">*</span></td>
                </tr>
            </table>
            <div class="row row-fluid mb10">
                <br/>
                <a href="#" id="setArea_ok" class="button button-primary" style="margin-left: 125px;">确认</a>
                <a href="#" id="setArea_cancle" class="button ml10">取消</a>
            </div>
        </div>
    </div>
    <!-- 弹出--设置属地--END-->

</@bs.layout>