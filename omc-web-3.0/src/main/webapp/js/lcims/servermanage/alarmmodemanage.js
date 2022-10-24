require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','stringutil'],
    function($,layer,laypage,resizewh,checkbox,stringutil) {
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
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','nodediv');
    }
    // 重置页面标签内容
    function reset(divid){
        $("#"+divid+" input[type='text']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" textarea").each(function(){
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
 		$("#operate_menu").empty();
 		var url = "/view/class/querychildrenmdmenu";
     	$.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
     		if(result!=null && result.length >0 ){
     			for(var i=0;i<result.length;i++){
     				$("#operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
 					// 新增、修改、删除绑定事件
 					console.log(result[i].url);
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
    	showLayer("add_div",'新增告警方式');
    }
    
    // 新增确认
    function addInfo(){
        var modename = stringutil.Trim($("#add_modename").val());
        var modetype = $("#add_modetype").val();
        var modeattr = stringutil.Trim($("#add_modeattr").val());
        if(stringutil.checkString("add_modename",modename,"告警方式名称不能为空!") 
        		|| stringutil.checkString("add_modename",modename,"告警方式名称不能超过40位!",40) 
        		|| stringutil.checkString("add_modetype",modetype,"告警方式类型不能为空!") 
        		|| stringutil.checkString("add_modetype",modetype,"告警方式类型不能超过11位!",11) 
        		|| stringutil.checkString("add_modeattr",modeattr,"告警方式属性不能为空!") 
        		|| stringutil.checkString("add_modeattr",modeattr,"告警方式属性不能超过256位!",256)){
            return;
        }
        // 校验告警方式名称是否重复事件
        var state = true;
        $.ajax({
            type: "post",
            url: "/view/class/system/alarmmodemanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {"modename":modename},
            success: function (result) {
            	var length = result.length;
            	for (var i=0;i<length;i++){
            		if(result[i].modename==modename){
        				state = false;
        				$("#add_modename").focus();
	                    layer.tips("告警方式名称不能重复", '#add_modename',{ tips: [2, '#EE1A23'] });
	            		return;
        			}
        		}
			}
		});
		if(state){
			loadingwait();
            var data= {'modename':modename,'modetype':modetype,'modeattr':modeattr};
            $.getJSON("/view/class/system/alarmmodemanage/add?random=" + Math.random(),data, function(result) {
                layer.closeAll();
				layerResultAndReload(result);
			});
		}
    }
    
    // 修改按钮事件
    function modifyShow(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个告警方式!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var modeid = checkboxArray[0];
            reset("modify_div");
            var data = {'modeid':modeid};
            $.ajax({
	            type: "post",
	            url: "/view/class/system/alarmmodemanage/querytype?random=" + Math.random(),
	            cache: false,
	            async: false, 
	            dataType: "json",
	            success: function (result) {
	            	$("#modify_modetype").empty();
	                $.each(result,function(i,data){
	                    $("#modify_modetype").append("<option value=\""+data.code+"\">"+data.description+"</option>");
	                });
	            }
	        });
	        loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/alarmmodemanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                	if(result.length>0){
                        var alarmmode = result[0];
                        $("#modify_modename").val(alarmmode.modename);
                        $("#modify_modeattr").val(alarmmode.modeattr);
                        $("#modify_modetype option").each(function(){
	                		if($(this).attr('value')==alarmmode.modetype){
	                			$(this).prop("selected", "true");
	                		}
	                	});
                        showLayer("modify_div",'修改告警方式');
                    }
                }
             });
    	}
	}
	
	// 修改确认
    function modifyInfo(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个告警方式!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var modeid = checkboxArray[0];
            var modename = stringutil.Trim($("#modify_modename").val());
	        var modetype = $("#modify_modetype").val();
	        var modeattr = stringutil.Trim($("#modify_modeattr").val());
	        if(stringutil.checkString("modify_modename",modename,"告警方式名称不能为空!") 
	        		|| stringutil.checkString("modify_modename",modename,"告警方式名称不能超过40位!",40) 
	        		|| stringutil.checkString("modify_modetype",modetype,"告警方式类型不能为空!") 
	        		|| stringutil.checkString("modify_modetype",modetype,"告警方式类型不能超过11位!",11) 
	        		|| stringutil.checkString("modify_modeattr",modeattr,"告警方式属性不能为空!") 
	        		|| stringutil.checkString("modify_modeattr",modeattr,"告警方式属性不能超过256位!",256)){
	            return;
	        }
            // 校验告警方式名称是否重复事件
            var state = true;
            $.ajax({
                type: "post",
                url: "/view/class/system/alarmmodemanage/query?random=" + Math.random(),
                cache: false,
                async: false, 
                data: {'modename':modename},
                success: function (result) {
                	var length = result.length;
                	if(length == 0){
                		state = true;
                	}else{
                		for(var i=0; i<length;i++){
                			if(result[i].modename==modename && result[i].modeid!=modeid){
                				state = false;
                				$("#modify_modename").focus();
		                        layer.tips("告警方式名称不能重复", '#modify_modename',{ tips: [2, '#EE1A23'] });
		                		return;
                    		}
                		}
                	}
                	
                }
			});
			if(state){
        		loadingwait();
                var data= {'modeid':modeid,'modename':modename,'modetype':modetype,'modeattr':modeattr};
                $.getJSON("/view/class/system/alarmmodemanage/modify?random=" + Math.random(),data, function(result) {
                    layer.closeAll();
                    layerResultAndReload(result);
                });
        	}
    	}
    }
    
    //删除
    function deleteShow() {
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length == 0){
    		layer.msg("请选择一个告警方式!",{
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
                var data = {'alarmmodeArray':checkboxArray};
                var url = "/view/class/system/alarmmodemanage/delete/?random=" + Math.random();
                $.getJSON(url,data,function(result){
                	layer.close(layer_load);
                	loadOptRecord();
                	layer.alert(result.message);
                })
            });
    	}
    }
    
    // 详情按钮事件
    function detailShow(modeid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/alarmmodemanage/query?modeid="+modeid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	if(result.length>0){
                    var alarmmode = result[0];
                    $("#detail_modename").val(alarmmode.modename);
                    $("#detail_modetype").val(alarmmode.description);
                    $("#detail_modeattr").val(alarmmode.modeattr);
                    showLayerDetail("detail_div",'告警方式详情');
                }
            }
         });
    }
    
    function isNull(data){
    	if(data==null || data ==''){
    		return '';
    	}else
    		return data;
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
        
        var modename = stringutil.Trim($("#modename").val());
        var modetype = stringutil.Trim($("#modetype").val());
        var data = {'modename':modename,'modetype':modetype};
        $.getJSON("/view/class/system/alarmmodemanage/query?random=" + Math.random(),data, function(result) {
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
            var id = rowninfo.modeid;
            var checked = "";
            if(checkbox.isExitArray(id)){
            	checked = "checked=\"checked\"";
            	k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.modeid+"\" id=\""+rowninfo.modeid+"\" "+checked+" />"+"</td>"
            +"<td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.modename+"\">"+rowninfo.modename+"</td><td>"
            +rowninfo.description
            +"</td><td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.modeattr+"\">"+rowninfo.modeattr+"</td><td>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.modeid+"\">详情</a>"
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
            area : [ '600px', '300px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '530px', '350px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] // 0.1透明度的白色背景
          });
    }
});