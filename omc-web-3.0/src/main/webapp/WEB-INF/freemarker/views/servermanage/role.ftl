<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/role.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainrole" >
        <div>
            <div class="omc_main_tab" >
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
                                <th style="width:30%">角色</th>
                                <th style="width:50%">权限</th>
                                <th style="width:20%">操作</th>
                            </tr>
                        </thead>
                        <tbody id="nodediv"></tbody>
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
           <label class="control-label">角色:</label>
            <div class="controls">
                <input size="30" class="input-large" id="add_name" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">权限:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_role" id="add_menulist"></div>
            </div>
        </div>
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
        <div class="row row-fluid mb10">
           <label class="control-label">角色:</label>
            <div class="controls">
                <input id="modify_roleid" type="hidden">
                <input size="30" class="input-large" id="modify_name" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">权限:</label>
            <div class="controls">
                <div class="omc_checkboxdiv_role" id="modify_menulist"></div>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <a href="#" id="modify_ok" class="button button-primary ml200">确认</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->
</@bs.layout>