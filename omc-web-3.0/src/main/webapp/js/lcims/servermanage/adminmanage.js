require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil',
        "des": "/js/lcims/des/des"
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','stringutil','des'],
    function($,layer,laypage,resizewh,checkbox,stringutil,des) {
        var layer_load;
        
        resizewh.resizeBodyH($("#mainnode"));
        butBindFunction();
        $("#checkboxAll").click(function() {
        	var flag = $(this).prop('checked');
        	checkbox.checkboxAll(flag,'checkboxAll','nodediv');
        });
        reset("mainnode");
        initChildrenMenu();
//        loadingwait();
//        loadOptRecord();
        
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainnode");
        });
        $("#add_ok").click(function() {
            addInfo();
        });
        $("#add_cancle").click(function() {
            layer.closeAll();
        });
        $("#modify_ok").click(function() {
            modifyInfo();
        });
        $("#modify_cancle").click(function() {
            layer.closeAll();
        });
        $("#detail_ok").click(function() {
            layer.closeAll();
        });
        $("#reset_ok").click(function() {
            resetInfo();
        });
        $("#reset_cancle").click(function() {
            layer.closeAll();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','nodediv');
    }
    // 重置页面标签内容
    function reset(divid){
        $("#"+divid+" input[type='text']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" input[type='password']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" select").each(function(){
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
    }
    
    function initChildrenMenu(){
    	var pageUrl=window.location.pathname;
 		$("#oprate_menu").empty();
 		var url = "/view/class/querychildrenmdmenu";
     	$.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
     		if(result!=null && result.length >0 ){
     			for(var i=0;i<result.length;i++){
     				$("#oprate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
 					// 新增、修改、删除绑定事件
 					if(result[i].url=='add'){
 						$("#"+result[i].name).click(function() {
 				            addShow();
 				        });
 					}else if(result[i].url=='edit'){
 						$("#"+result[i].name).click(function() {
 				        	modifyShow();
 				        });
 					}else if(result[i].url=='delete'){
 						$("#"+result[i].name).click(function() {
 				        	deleteShow();
 				        });
 					}else if(result[i].url=='reset'){
 						$("#"+result[i].name).click(function() {
 				        	resetShow();
 				        });
 					}
     			}
     		}
     		
     	});
    }
    
    function bindCheckBox(){
    	$("#nodediv :checkbox").click(function(){  
        	var flag = $(this).prop('checked');
        	var value = $(this).val();
        	checkbox.checkboxSingle(flag,value);
        });
    }
    
    // 新增按钮事件
    function addShow(){
    	reset("add_div");
    	$.getJSON("/view/class/system/adminmanage/rolelist?random=" + Math.random(), function(result) {
			$("#add_roleid").empty().append("<option value=\"\">请选择</option>");
            $.each(result,function(i,data){
                $("#add_roleid").append("<option value=\""+data.roleid+"\">"+data.name+"</option>");
            });
		});
    	showLayer("add_div",'新增管理员');
    }
    
    // 新增确认
    function addInfo(){
    	var admin = stringutil.Trim($("#add_admin").val());
    	var name = stringutil.Trim($("#add_name").val());
        var pwd = stringutil.Trim($("#add_pwd").val());
        var pwd_s = stringutil.Trim($("#add_pwd_s").val());
        var roleid = $("#add_roleid").val();
        var contactphone = stringutil.Trim($("#add_contactphone").val());
        var corpname = stringutil.Trim($("#add_corpname").val());
        var coaddr = stringutil.Trim($("#add_coaddr").val());
        var email = stringutil.Trim($("#add_email").val());
        var description = stringutil.Trim($("#add_description").val());

        if(stringutil.checkString("add_admin",admin,"管理员不能为空!")
        		|| stringutil.checkString("add_admin",admin,"管理员不能超过40位!",40)
        		|| stringutil.checkString("add_pwd",pwd,"登录密码不能为空!")
        		|| stringutil.checkString("add_pwd",pwd,"登录密码不能超过18位!",18)
        		|| stringutil.checkString("add_pwd_s",pwd_s,"再次确认不能为空!")
        		|| stringutil.checkString("add_pwd_s",pwd_s,"再次确认不能超过18位!",18)
        		|| stringutil.checkString("add_roleid",roleid,"角色不能为空!")
        		|| stringutil.checkString("add_roleid",roleid,"角色不能超过40位!",40)
        		|| stringutil.checkString("add_contactphone",contactphone,"手机号码不能为空!")
        		|| stringutil.checkString("add_email",email,"邮箱不能为空!")
        		|| stringutil.checkString("add_name",name,"管理员姓名不能超过20位!",20)
        		|| stringutil.checkString("add_contactphone",contactphone,"联系电话不能超过20位!",20)
        		|| stringutil.checkString("add_corpname",corpname,"单位名称不能超过64位!",64)
        		|| stringutil.checkString("add_coaddr",coaddr,"家庭地址不能超过64位!",64)
        		|| stringutil.checkString("add_email",email,"电子邮件地址不能超过60位!",60)
        		|| stringutil.checkString("add_description",description,"备注不能超过100位!",100)){
            return;
        }
        if(validPwd('add',pwd,pwd_s)) {
        	return;
        }
        if(validEmail('add',email)) {
        	return;
        }
        if(validPhone('add',contactphone)) {
        	return;
        }
        //校验用户名和密码不能一致
        if (checkAdminAndPwd('add',admin,pwd)){
            return;
        }

        //密码加密
        pwd = des.des(pwd);
        pwd_s = des.des(pwd_s);

        // 校验设备名称是否重复事件
        $.ajax({
            type: "post",
            url: "/view/class/system/adminmanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {"admin": admin},
            success: function (result) {
            	var length = result.length;
            	var state = true;
            	for (var i=0;i<length;i++){
            		if(result[i].admin==admin){
        				state = false;
        			}
        		}
            	if(state){
            		loadingwait();
                    var data= {'admin':admin,'name':name,'password':pwd,'pwd_s':pwd_s,'roleid':roleid,'contactphone':contactphone,'corpname':corpname,'coaddr':coaddr,'email':email,'description':description};
                    // $.getJSON("/view/class/system/adminmanage/add?random=" + Math.random(),data, function(result) {
                    //     layer.closeAll();
                    //     layerResultAndReload(result);
                    // });
                    $.ajax({
                        type: "post",
                        url: "/view/class/system/adminmanage/add?random=" + Math.random(),
                        data: data,
                        cache: false,
                        async: false,
                        dataType: "json",
                        success: function (result) {
                            layer.closeAll();
                            layerResultAndReload(result);
                        }
                    });
            	}else{
            		$("#add_admin").focus();
                    layer.tips("管理员不能重复", '#add_admin',{ tips: [2, '#EE1A23'] });
            		return;
            	}
            }
         });
    }
    
    // 修改按钮事件
	function modifyShow(){
		var checkboxArray = checkbox.getReturnArray();
		if(checkboxArray.length != 1){
			layer.msg("修改请只选择一个管理员!",{
				time:2000,
				skin: 'layer_msg_color_alert'
			});
		}else{
			var adminid = checkboxArray[0];
			reset("modify_div");
			loadingwait();
			var data = {admin : adminid};				
			$.ajax({
				type: "post",
				url: "/view/class/system/adminmanage/singleinfo?random=" + Math.random(),
				data: data,
				cache: false,
				async: false, 
				dataType: "json",
				success: function (result) {
					if (result.length > 0) {
						var info = result[0];
						$("#modify_admin").val(info.admin);
						$("#modify_contactphone").val(info.contactphone);
						//角色
						if (info.admin=='admin') {
							$("#modify_roleid").attr("disabled",true);
							$("#modify_roleid").empty().append("<option value=\"\">请选择</option>");
							$.each(info.rolelistforcheck,function(i,info){
								var temp = "<option value=\""+info.key + "\"";
								if(Boolean(info.checkflag)){
									temp = temp + " selected = \"selected\"";
								}
								temp = temp + ">"+info.value+"</option>";
								$("#modify_roleid").append(temp);
							});
						} else {
							$("#modify_roleid").attr("disabled",false);
							$("#modify_roleid").empty().append("<option value=\"\">请选择</option>");
							$.each(info.rolelistforcheck,function(i,info){
								var temp = "<option value=\""+info.key + "\"";
								if(Boolean(info.checkflag)){
									temp = temp + " selected = \"selected\"";
								}
								temp = temp + ">"+info.value+"</option>";
								$("#modify_roleid").append(temp);
							});
						}
						
						$("#modify_name").val(info.name);
						$("#modify_corpname").val(info.corpname);
						$("#modify_coaddr").val(info.coaddr);
						$("#modify_email").val(info.email);
						$("#modify_description").val(info.description);
						showLayer("modify_div",'修改管理员');
					}
				}
			});			
		}
	}
    
    function isNull(data){
    	if(data==null || data ==''){
    		return '';
    	}else
    		return data;
    }
    
    // 修改确认
    function modifyInfo(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个管理员!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var adminid = checkboxArray[0];
    		var admin = stringutil.Trim($("#modify_admin").val());
	    	var name = stringutil.Trim($("#modify_name").val());
	        var roleid = $("#modify_roleid").val();
	        var contactphone = stringutil.Trim($("#modify_contactphone").val());
	        var corpname = stringutil.Trim($("#modify_corpname").val());
	        var coaddr = stringutil.Trim($("#modify_coaddr").val());
	        var email = stringutil.Trim($("#modify_email").val());
	        var description = stringutil.Trim($("#modify_description").val());
	        if(stringutil.checkString("modify_admin",admin,"管理员不能为空!") 
	        		|| stringutil.checkString("modify_admin",admin,"管理员不能超过40位!",40)
	        		|| stringutil.checkString("modify_roleid",roleid,"角色不能为空!")
	        		|| stringutil.checkString("modify_contactphone",contactphone,"手机号码不能为空!")
	        		|| stringutil.checkString("modify_email",email,"邮箱不能为空!")
	        		|| stringutil.checkString("modify_roleid",roleid,"角色不能超过40位!",40)
	        		|| stringutil.checkString("modify_name",name,"管理员姓名不能超过20位!",20)
	        		|| stringutil.checkString("modify_contactphone",contactphone,"联系电话不能超过20位!",20)
	        		|| stringutil.checkString("modify_corpname",corpname,"单位名称不能超过64位!",64)
	        		|| stringutil.checkString("modify_coaddr",coaddr,"家庭地址不能超过64位!",64)
	        		|| stringutil.checkString("modify_email",email,"电子邮件地址不能超过60位!",60)
	        		|| stringutil.checkString("modify_description",description,"备注不能超过100位!",100)){
	            return;
	        }
	        if(validEmail('modify',email)) {
	        	return;
	        }
	        if(validPhone('modify',contactphone)) {
	        	return;
	        }
            var data= {'admin':adminid,'name':name,'roleid':roleid,'contactphone':contactphone,'corpname':corpname,'coaddr':coaddr,'email':email,'description':description};
            $.getJSON("/view/class/system/adminmanage/modify?random=" + Math.random(),data, function(result) {
                layer.closeAll();
                layerResultAndReload(result);
            });
    	}
    }
    
    // 修改密码按钮事件
	function resetShow(){
		var checkboxArray = checkbox.getReturnArray();
		if(checkboxArray.length != 1){
			layer.msg("修改密码请只选择一个管理员!",{
				time:2000,
				skin: 'layer_msg_color_alert'
			});
		}else{
			var adminid = checkboxArray[0];
			reset("reset_div");
			// loadingwait();
            var data = {admin : adminid};
            $.ajax({
                type: "post",
                url: "/view/class/system/adminmanage/getSession?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if (result == "1"){
                        showLayer("reset_div",'修改密码');
                    }else {
                        layer.msg("只能修改本人登录密码！",{
                            time:2000,
                            skin: 'layer_msg_color_alert'
                        });
                    }
                }
            });
        }
	}
	
	// 修改密码确认
	function resetInfo(){
		var checkboxArray = checkbox.getReturnArray();
		if(checkboxArray.length != 1){
			layer.msg("修改密码请只选择一个管理员!",{
				time:2000,
				skin: 'layer_msg_color_alert'
			});
		}else{
			var adminid = checkboxArray[0];
            var old_pwd = stringutil.Trim($("#old_pwd").val());
			var pwd = stringutil.Trim($("#reset_pwd").val());
			var pwd_s = stringutil.Trim($("#reset_pwd_s").val());
			if(stringutil.checkString("reset_old_pwd",pwd,"旧密码不能为空!")
                    || stringutil.checkString("reset_pwd",pwd,"新密码不能为空!")
					|| stringutil.checkString("reset_pwd",pwd,"新密码不能超过18位!",18)
					|| stringutil.checkString("reset_pwd_s",pwd_s,"再次确认不能为空!")
					|| stringutil.checkString("reset_pwd_s",pwd_s,"再次确认不能超过18位!",18)){
				return;
			}
			if (validPwd('reset',pwd,pwd_s)) {
	            return;
	        }

            //校验用户名和密码不能一致
            if (adminid == pwd){
                $("#reset_pwd").focus();
                layer.tips('用户名和密码不能一致!', '#reset_pwd',{ tips: [2, '#EE1A23'] });
                return true;
            }

            //密码加密
            pwd = des.des(pwd);
            old_pwd = des.des(old_pwd);

			var data= {'admin':adminid,'oldPwd':old_pwd,'password':pwd};
            $.ajax({
                type: "post",
                url: "/view/class/system/adminmanage/chgpasswd?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if (result.data != null && result.data == "1"){
                        $("#old_pwd").focus();
                        layer.tips("原密码错误，请重新输入！", '#old_pwd',{ tips: [2, '#EE1A23'] });
                        return;
                    }
                    layer.closeAll();
                    layerResultAndReload(result);
                    loadOptRecord();
                }
            });
		}
	}
    
    // 详情按钮事件
    function detailShow(admin){
        reset("detail_div");
        loadingwait();
        var data = {admin : admin};
        $.ajax({
            type: "post",
            url: "/view/class/system/adminmanage/queryDetail?random=" + Math.random(),
            data: data,
            cache: false,
            async: false,
			dataType: "json",
            success: function (result) {
            	if(result.length>0){
                    var admin = result[0];
                    $("#detail_admin").val(admin.admin);
                    $("#detail_name").val(admin.name);
                    $("#detail_contactphone").val(admin.contactphone);
                    $("#detail_roleid").val(admin.rolename);
                    $("#detail_corpname").val(admin.corpname);
                    $("#detail_coaddr").val(admin.coaddr);
                    $("#detail_email").val(admin.email);
                    $("#detail_description").val(admin.description);
                    showLayerDetail("detail_div",'管理员详情');
                }
            }
         });
    }
    
    function addJumpShow(operateid) {
        layer.confirm('是否需要跳转到服务器管理页面？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            window.location.href = "/view/class/system/servermanage?key=servermanage&random=" + Math.random();
        });
    }
    
    // 查询按钮事件
    function queryOpt(){
        loadingwait();
        loadOptRecord();
    }
    // 加载查询内容
    function loadOptRecord(){
        // 分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();
        
        var model_name = stringutil.Trim($("#model_name").val());
        var role_id = stringutil.Trim($("#role_id").val());
        var data = {'admin':model_name,'roleid':role_id};
        $.getJSON("/view/class/system/adminmanage/query?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#querynum").text(total);
            laypage({
                cont: 'pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: false, // 是否开启跳页
                jump: function(obj, first){ // 触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showTable(result,startnum,endnum);
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#mainnode"));
                },
                groups: page_count // 连续显示分页数
            });
        });
    }
    // 拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.admin;
            var checked = "";
            if(checkbox.isExitArray(id)){
            	checked = "checked=\"checked\"";
            	k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.admin+"\" id=\""+rowninfo.admin+"\" "+checked+" />"
            +"</td><td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.admin+"\">"+rowninfo.admin+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.rolename+"\">"
            +rowninfo.rolename+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.name)+"\">" + stringutil.isNull(rowninfo.name) + "</td><td style='max-width:120px'>" + stringutil.isNull(rowninfo.contactphone) + "</td><td>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.admin+"\">详情</a>"
            +"</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
        	//全选框选中
        	$("#checkboxAll").prop("checked", true);
        }else{
        	$("#checkboxAll").prop("checked", false);
        }
        $("#nodediv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("nodediv");
    }
    
    function deleteShow() {
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length == 0){
    		layer.msg("请选择至少一个管理员!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		layer.confirm('是否确认删除该批次数据？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] // 按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {'adminArray':checkboxArray};
                var url = "/view/class/system/adminmanage/delete?random=" + Math.random();
                $.getJSON(url,data,function(result){
                	layer.close(layer_load);
                	loadOptRecord();
                	layer.alert(result.message);
                })
            });
    	}
    }
    
    function validEmail(type, email) {
    	if(null == email || email==""){
            return false;
        }
    	if(!checkEmail(email)) {
    		$("#"+type+"_email").focus();
            layer.tips('请输入正确的邮件格式!', '#'+type+'_email',{ tips: [2, '#EE1A23'] });
            return true;
    	}
    	return false;
    }
    
    function checkEmail(email) {
        var regex = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
        return regex.test(email);
    }

    //校验用户名和密码不能一致
    function checkAdminAndPwd(type,admin,pwd) {
        if (admin == pwd){
            $("#"+type+"_admin").focus();
            layer.tips('用户名和密码不能一致!', '#'+type+'_admin',{ tips: [2, '#EE1A23'] });
            return true;
        }
    }

    //密码正则表达式
    function checkPwd(pwd) {
        var regPwd =/^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[\(\)])+$)([^(0-9a-zA-Z)]|[\(\)]|[a-z]|[A-Z]|[0-9]){8,12}$/;
        return regPwd.test(pwd);
    }

    function validPwd(type, pwd,pwd_s) {
        if(!checkPwd(pwd)) {
            $("#" + type + "_pwd").focus();
            layer.tips('密码长度为8~12位，且必须包含大小写字母、数字、特殊符号任意两者组合!', '#' + type + '_pwd', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        if(pwd!=pwd_s){
            $("#" + type + "_pwd").focus();
            layer.tips('密码和确认密码不一致!', '#' + type + '_pwd', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function checkPhone(contactphone) {
        var regex = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
        return regex.test(contactphone);
    }
    
    function validPhone(type, contactphone) {
    	if(null == contactphone || contactphone==""){
            return false;
        }
    	if(contactphone.length!=11){
            $("#" + type + "_contactphone").focus();
            layer.tips('联系电话必须是11位!', '#' + type + '_contactphone', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        if(!checkPhone(contactphone)){
            $("#" + type + "_contactphone").focus();
            layer.tips('请输入正确的联系电话格式!', '#' + type + '_contactphone', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function layerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
            queryOpt();
            layer.msg(result.message,{
                time:2000,
                skin: 'layer_msg_color_success'
            });
        }else{
            layer.msg(result.message,{
                time:2000,
                skin: 'layer_msg_color_error'
            });
        }
    }
    
    function addLayerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
        	addJumpShow(result.id);
        }else{
            layer.msg(result.message,{
                time:2000,
                skin: 'layer_msg_color_error'
            });
        }
    }
    
    function showLayerDetail(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '900px', '460px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '900px', '460px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] // 0.1透明度的白色背景
          });
    }
});