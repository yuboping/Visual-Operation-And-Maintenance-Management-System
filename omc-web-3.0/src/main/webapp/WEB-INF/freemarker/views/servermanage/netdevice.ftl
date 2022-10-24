<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/netdevice.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainnetdevice" >
        <div>
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">IP地址:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="nasip" name="nasip" type="text">
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">设备类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="accesstype" name="accesstype" >
                                    <option value="">全部</option>
                                    <#list accessTypeList as accessType>
                                        <option value=${accessType.code}>${accessType.description}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">设备类型:</label>
                            <div class="controls">
                                <select class="input-medium" id="nastype" name="nastype" >
                                    <option value="">全部</option>
                                    <#list nasTypeList as nasType>
                                        <option value=${nasType.nastype}>${nasType.nastype}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="span10">
                            <label class="control-label">属地:</label>
                            <div class="controls">
                                <select class="input-medium" id="areano" name="areano" >
                                    <#list areaList as area>
                                        <option value=${area.areano}>${area.name}</option>
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
                                    <th style="width:20%">IP地址</th>
                                    <th style="width:20%">设备类型</th>
                                    <th style="width:20%">型号</th>
                                    <th style="width:20%">属地</th>
                                    <th style="width:20%">操作</th>
                                </tr>
                            </thead>
                            <tbody id="querydiv"></tbody>
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
           <label class="control-label">NASIP:</label>
            <div class="controls">
                <input size="30" class="input-large" id="add_nasip" type="text">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">设备类型:</label>
            <div class="controls">
                <select class="input-large" id="add_accesstype"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">设备型号:</label>
            <div class="controls">
                <select class="input-large" id="add_nastype"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">属地:</label>
            <div class="controls">
                <select class="input-large" id="add_areano"></select>
                <span class="text-danger">*</span>
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
           <label class="control-label">NASIP:</label>
            <div class="controls">
                <input size="30" class="input-large" id="modify_nasip" type="text">
                <input size="30" class="input-large" id="modify_nasid" type="hidden">
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">设备类型:</label>
            <div class="controls">
                <select class="input-large" id="modify_accesstype"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">设备型号:</label>
            <div class="controls">
                <select class="input-large" id="modify_nastype"></select>
                <span class="text-danger">*</span>
            </div>
        </div>
        <div class="row row-fluid mb10">
           <label class="control-label">属地:</label>
            <div class="controls">
                <select class="input-large" id="modify_areano"></select>
                <span class="text-danger">*</span>
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