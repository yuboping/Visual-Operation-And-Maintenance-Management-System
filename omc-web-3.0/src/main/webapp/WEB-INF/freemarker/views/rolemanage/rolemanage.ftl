<@bs.layout [
    "echarts/echarts.js",
    "jstree/themes/default/style.min.css",
    "jstree/jstree.min.js",
    "lcims/rolemanage/rolemanage.js",
    "layer/layer.css",
    "jquery/jquery.min.js",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainrole" >
       <div>
            <div class="omc_main_tab" >
                <!--查询条件-->
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">角色名称:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="name" name="name" type="text">
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
                                <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                <th style="width:80%">角色名称</th>
                                <th style="width:15%">操作</th>
                            </tr>
                        </thead>
                        <tbody id="rolediv"></tbody>
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
<!--弹层-新增-->
<div class="mt10 display_none" id="add_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">角色名称:</label>
                <div class="controls">
                    <input size="30" maxlength="40" class="input-medium" id="add_rolename" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">角色描述:</label>
                <div class="controls">
                    <input size="30" maxlength="100" class="input-medium" id="add_description" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label">参数:</label>
            <div class="controls">
                <#--<select class="input-medium" id="add_roleid"></select>-->
                <div class="omc_checkboxdiv_admin" id="add_roleid"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="add_areanolist_div">
            <label class="control-label">可管理属地:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="add_areanolist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="add_nodelist_div">
            <label class="control-label">可管理节点:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="add_nodelist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="add_jstree_div">
            <label class="control-label">可管理模块:</label>
            <div class="controls">
                <div id="add_jstree"></div>
            </div>
        </div>
        <#--<div id="jstree_add_div"></div>-->
        <div class="row row-fluid">
            <a href="#" id="add_ok" class="button button-primary ml400">保存</a>
            <a href="#" id="add_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-新增-END-->

<!--弹层-修改-->
<div class="mt10 display_none" id="modify_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">角色名称:</label>
                <div class="controls">
                    <input id="modify_id" type="hidden">
                    <input size="30" maxlength="40" class="input-medium" id="modify_rolename" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">角色描述:</label>
                <div class="controls">
                    <input size="30" maxlength="100" class="input-medium" id="modify_description" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label">参数:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="modify_roleid"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="modify_areanolist_div">
            <label class="control-label">可管理属地:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="modify_areanolist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="modify_nodelist_div">
            <label class="control-label">可管理节点:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="modify_nodelist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="modify_jstree_div">
            <label class="control-label">可管理模块:</label>
            <div class="controls">
                <div id="modify_jstree"></div>
            </div>
        </div>
        <div class="row row-fluid">
            <a href="#" id="modify_ok" class="button button-primary ml400">保存</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->

<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">角色名称:</label>
                <div class="controls">
                    <input id="detail_id" type="hidden">
                    <input size="30" class="input-medium" id="detail_rolename" readonly="readonly" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">角色描述:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_description" readonly="readonly" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <label class="control-label">参数:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" disabled="disabled" id="detail_roleid"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="detail_areanolist_div">
            <label class="control-label">可管理属地:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="detail_areanolist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="detail_nodelist_div">
            <label class="control-label">可管理节点:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" disabled="disabled" id="detail_nodelist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="detail_jstree_div">
            <label class="control-label">可管理模块:</label>
            <div class="controls">
                <div id="detail_jstree"></div>
            </div>
        </div>
        <div class="row row-fluid">
            <a href="#" id="detail_ok" class="button button-primary ml400">关闭</a>
        </div>
    </div>
</div>
<!--弹层-详情-END-->
</@bs.layout>