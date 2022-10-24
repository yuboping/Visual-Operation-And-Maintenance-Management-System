<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/admin.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainadmin" >
        <div>
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <!--查询条件-->
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">登录名:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="admin" name="admin" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">角色:</label>
                            <div class="controls">
                                <select class="input-medium" id="roleid" name="roleid">
                                    <option value="">全部</option>
                                    <#list roleList as role>
                                        <option value=${role.roleid}>${role.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">属地:</label>
                            <div class="controls">
                                <select class="input-medium" id="areano" name="areano">
                                    <option value="">全部</option>
                                    <#list areaList as area>
                                        <option value=${area.areano}>${area.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">节点:</label>
                            <div class="controls">
                                <select class="input-medium" id="nodeid" name="nodeid">
                                	<option value="">无</option>
                                    <#list nodeList as node>
                                        <option value=${node.nodeid}>${node.name}</option>
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
                                <div class="fr">
                                    <a href="#add" id="addbutton" class="button button-small button-primary">新增</a>
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:12%">登录名</th>
                                    <th style="width:13%">角色</th>
                                    <th style="width:20%">授权地市</th>
                                    <th style="width:15%">授权节点</th>
                                    <th style="width:10%">联系人</th>
                                    <th style="width:10%">联系电话</th>
                                    <th style="width:10%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="queryinfodiv"></tbody>
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
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">管理员:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_admin" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">联系人:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_name" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">登录密码:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_pwd" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">再次确认:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_pwd_s" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">所在属地:</label>
                <div class="controls">
                    <select class="input-medium" id="add_areaid"></select>
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">联系电话:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_contactphone" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">角色:</label>
                <div class="controls">
                    <select class="input-medium" id="add_roleid"></select>
                    <span class="text-danger">*</span>
                </div>
        </div>
        <div class="row row-fluid mb10" id="add_nodelist_div">
           <label class="control-label">可管理节点:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="add_nodelist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="add_areanolist_div">
           <label class="control-label">可管理地市:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="add_areanolist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="add_modulelist_div">
           <label class="control-label">可管理模块:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="add_modulelist"></div>
            </div>
        </div>
        <div class="row row-fluid">
           <a href="#" id="add_ok" class="button button-primary ml400">确认</a>
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
                <label class="control-label">管理员:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_admin" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">联系人:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_name" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">登录密码:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_pwd" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">再次确认:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_pwd_s" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">所在属地:</label>
                <div class="controls">
                    <select class="input-medium" id="modify_areaid"></select>
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">联系电话:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_contactphone" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">角色:</label>
                <div class="controls">
                    <select class="input-medium" id="modify_roleid"></select>
                    <span class="text-danger">*</span>
                </div>
        </div>
        <div class="row row-fluid mb10" id="modify_nodelist_div">
           <label class="control-label">可管理节点:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="modify_nodelist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="modify_areanolist_div">
           <label class="control-label">可管理地市:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="modify_areanolist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10" id="modify_modulelist_div">
           <label class="control-label">可管理模块:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_admin" id="modify_modulelist"></div>
            </div>
        </div>
        <div class="row row-fluid">
           <a href="#" id="modify_ok" class="button button-primary ml400">确认</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->
</@bs.layout>