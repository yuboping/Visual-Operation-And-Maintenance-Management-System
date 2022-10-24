<@bs.layout [
    "echarts/echarts.js",
    "lcims/servermanage/adminmanage.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainnode" >
        <div>
        	
            <!--查询条件-->
            <div class="omc_main_tab" >
                <div class="form-horizontal onlineTools">
                    <div class="row row-fluid">
                        <div class="span10">
                            <label class="control-label">用户名:</label>
                            <div class="controls">
                                <input size="30" class=" input-medium" id="model_name" name="model_name" type="text">
                            </div>
                        </div>
                        <div class="span10" id="factoryids">
                            <label class="control-label" >角色:</label>
                            <div class="controls">
                                <select class="input-medium" id="role_id" name="role_id">
                                	<option value="">全部</option>
                                    <#list mdRoleList as mdRole>
                                        <option value=${mdRole.roleid}>${mdRole.name}</option>
                                    </#list>
                                </select>
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
                                <div id = "oprate_menu" class="fr" >
                                </div>
                             </div>
                        </div>
                        <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                            <thead>
                                <tr>
                                    <th style="width:5%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
                                    <th style="width:10%">用户名</th>
                                    <th style="width:10%">角色</th>
                                    <th style="width:10%">管理员姓名</th>
                                    <th style="width:10%">联系电话</th>
                                    <th style="width:10%">操作</th>
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
                <label class="control-label">管理员姓名:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_name" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">登录密码:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_pwd" type="password">
                    <span class="text-danger">*</span><br>
                    <span style="color: red;">密码长度为8~12位，且必须包含大小写字母、数字、特殊符号任意两者组合!</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">再次确认:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_pwd_s" type="password">
                    <span class="text-danger">*</span>
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
			 <label class="control-label">角色:</label>
			 <div class="controls">
				 <select class="input-medium" id="add_roleid"></select>
				 <span class="text-danger">*</span>
			 </div>
			 </span>
			 <span class="span12 ml15">
				<label class="control-label">联系电话:</label>
				<div class="controls">
					<input size="30" class="input-medium" id="add_contactphone" type="text">
					<span class="text-danger">*</span>
				</div>
			</span>
		</div>
		<div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">单位名称:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_corpname" type="text">
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">家庭住址:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_coaddr" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">邮件地址:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="add_email" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">备注信息:</label>
                <div class="controls">
                	<input size="30" class="input-medium" id="add_description" type="text" style="width: 700px;">
                </div>
            </span>
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
                    <input size="30" class="input-medium" id="modify_admin" type="text" disabled="disabled">
                    <span class="text-danger">*</span>
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">管理员姓名:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_name" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
        	 <span class="span12">
        	 	<label class="control-label">角色:</label>
                <div class="controls">
                    <select class="input-medium" id="modify_roleid"></select>
                    <span class="text-danger">*</span>
                </div>
        	 </span>
             <span class="span12 ml15">
                <label class="control-label">联系电话:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_contactphone" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">单位名称:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_corpname" type="text">
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">家庭住址:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_coaddr" type="text">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">邮件地址:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="modify_email" type="text">
                    <span class="text-danger">*</span>
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">备注信息:</label>
                <div class="controls">
                	<input size="30" class="input-medium" id="modify_description" type="text" style="width: 700px;">
                </div>
            </span>
        </div>
        <div class="row row-fluid">
           <a href="#" id="modify_ok" class="button button-primary ml400">确认</a>
            <a href="#" id="modify_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改-END-->

<!--弹层-详情--->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">管理员:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_admin" type="text" readonly="readonly">
                </div>
            </span>
             <span class="span12 ml15">
                <label class="control-label">管理员姓名:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_name" type="text" readonly="readonly">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
            <span class="span12">
                <label class="control-label">角色:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_roleid" type="text" readonly="readonly">
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">联系电话:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_contactphone" type="text" readonly="readonly">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">单位名称:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_corpname" type="text" readonly="readonly">
                </div>
            </span>
            <span class="span12 ml15">
                <label class="control-label">家庭住址:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_coaddr" type="text" readonly="readonly">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">邮件地址:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="detail_email" type="text" readonly="readonly">
                </div>
            </span>
        </div>
        <div class="row row-fluid mb10">
			<span class="span12">
                <label class="control-label">备注信息:</label>
                <div class="controls">
                	<input size="30" class="input-medium" id="detail_description" type="text" readonly="readonly" style="width: 700px;">
                </div>
            </span>
        </div>
        <div class="row row-fluid">
           <br/>
           <a href="#" id="detail_ok" class="button button-primary ml400">关闭</a>
       </div>
    </div>
</div>
<!--弹层-详情-END-->

<!--弹层-修改密码--->
<div class="mt10 display_none" id="reset_div">
    <div class="form-horizontal onlineTools">
        <div style="margin-left: 24%;">
            <div class="row row-fluid mb10">
            <span>
                <label class="control-label">旧密码:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="old_pwd" type="password">
                    <span class="text-danger">*</span>
                </div>
            </span>
            </div>
            <div class="row row-fluid mb10">
            <span>
                <label class="control-label">新密码:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="reset_pwd" type="password">
                    <span class="text-danger">*</span><br>
                    <span style="color: red;">密码长度为8~12位，且必须包含大小写字母、数字、特殊符号任意两者组合!</span>
                </div>
            </span>
            </div>
            <div class="row row-fluid mb10">
             <span>
                <label class="control-label">再次确认:</label>
                <div class="controls">
                    <input size="30" class="input-medium" id="reset_pwd_s" type="password">
                    <span class="text-danger">*</span>
                </div>
            </span>
            </div>
        </div>
		<div class="row row-fluid">
			<a href="#" id="reset_ok" class="button button-primary ml400">确认</a>
			<a href="#" id="reset_cancle" class="button ml10">取消</a>
        </div>
    </div>
</div>
<!--弹层-修改密码-END-->

</@bs.layout>