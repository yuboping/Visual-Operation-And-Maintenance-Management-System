<@bs.layout [
    "lcims/ais/aisgroupmanage.js",
    "layer/layer.css",
    "ais/icon-style.css",
    "ais/ais.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainaisgroup" >
       <div>
            <div class="omc_main_tab" >
                <!--查询条件-->
                    <div class="form-horizontal onlineTools">
                        <div class="row row-fluid">
                            <div class="span10">
                                <label class="control-label">巡检组名称:</label>
                                <div class="controls">
                                    <input size="30" class=" input-medium" id="group_name" name="group_name" type="text">
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
                                <th style="width:30%">巡检组名称</th>
                                <th style="width:45%">描述</th>
                                <th style="width:30%">图标</th>
                            </tr>
                        </thead>
                        <tbody id="aisgroupdiv"></tbody>
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
            <label class="control-label">巡检组名称:</label>
            <div class="controls">
                <input size="64" maxlength="64" class="input-medium" id="add_name" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>

        <div class="row row-fluid mb10">
            <label class="control-label">巡检组描述:</label>
            <div class="controls">
                <input size="128" maxlength="128" class="input-large" id="add_description" type="text">
            </div>
        </div>

        <div class="row row-fluid mb10">
            <label class="control-label">图标:</label>
            <div class="controls">
                <select id="add_icon" style="width: 150px;">
                    <#list paramList as param>
                        <option value="${param.code}">${param.description}</option>
                    </#list>
                </select>
            </div>
        </div>

        <!-- 增加样式预览 -->
        <div class="row row-fluid mb10">
            <label class="control-label">样式预览:</label>
            <div class="controls">
                <div class="dap-ais-circle-box" style="width: 150px;">
                   <div class="dap-ais-circle-scale"></div>
                   <div class="dap-ais-circle">
                       <span id="add_show_class" class="icon-wlsbqk"></span>
                       <span class="fa fa-check-square-o dap-ais-check"></span>
                       <span class="dap-ais-nochoose-txt">不检查</span>
                   </div>
               </div>
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
            <label class="control-label">巡检组名称:</label>
            <div class="controls">
                <input id="modify_group_id" type="hidden">
                <input size="64" maxlength="64" class="input-medium" id="modify_name" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>

        <div class="row row-fluid mb10">
            <label class="control-label">巡检组描述:</label>
            <div class="controls">
                <input size="128" maxlength="128" class="input-large" id="modify_description" type="text">
            </div>
        </div>

        <div class="row row-fluid mb10">
            <label class="control-label">图标:</label>
            <div class="controls">
            <#--<input size="64" maxlength="64" class="input-medium" id="modify_icon" type="text">-->
                <select id="modify_icon" style="width: 150px;">
                    <#list paramList as param>
                        <option value="${param.code}">${param.description}</option>
                    </#list>
                </select>
            </div>
        </div>

        <!-- 增加样式预览 -->
        <div class="row row-fluid mb10">
            <label class="control-label">样式预览:</label>
            <div class="controls">
                <div class="dap-ais-circle-box" style="width: 150px;">
                    <div class="dap-ais-circle-scale"></div>
                    <div class="dap-ais-circle">
                        <span id="modify_show_class" class="icon-wlsbqk"></span>
                        <span class="fa fa-check-square-o dap-ais-check"></span>
                        <span class="dap-ais-nochoose-txt">不检查</span>
                    </div>
                </div>
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