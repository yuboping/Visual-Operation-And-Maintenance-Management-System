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
    	$.getJSON("/view/class/system/equipmentmanage/queryNames?random=" + Math.random(), function(result) {
			$("#add_factory_name").empty();
            $.each(result,function(i,data){
                $("#add_factory_name").append("<option value=\""+data.id+"\">"+data.factory_name+"</option>");
            });
		});
    	showLayer("add_div",'新增设备型号');
    }
    
    // 修改按钮事件
    function modifyShow(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个设备!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var factoryid = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {id : factoryid};
            $.ajax({
                type: "post",
                url: "/view/class/system/equipmentmanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
	            	$("#modify_factory_name").empty();
	                $.each(result,function(i,data){
	                    $("#modify_factory_name").append("<option value=\""+data.factory_id+"\">"+data.factory_name+"</option>");
	                });
	            }
            });
            loadingwait();
            $.ajax({
				type: "post",
				url: "/view/class/system/equipmentmanage/query?random=" + Math.random(),
				data: data,
				cache: false,
				async: false, 
				dataType: "json",
				success: function (result) {
					if(result.length>0){
						var factory = result[0];
						$("#modify_factory_name option").each(function(){
							if($(this).attr('value')==factory.factory_id){
								$(this).prop("selected", "true");
							}
						});
						$("#modify_equipment").val(factory.model_name);
						showLayer("modify_div",'修改设备');
					}
				}
			});
    	}
   }
    
    
    // 详情按钮事件
    function detailShow(equipmentid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/equipmentmanage/query?id="+equipmentid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	if(result.length>0){
                    var equipment = result[0];
                    $("#detail_equipment_name").val(equipment.model_name);
                    $("#detail_factory_name").val(equipment.factory_name);
                    showLayerDetail("detail_div",'厂商详情');
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
    
    // 修改确认
    function modifyInfo(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个设备!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var factoryid = checkboxArray[0];
            var factory_name = stringutil.Trim($("#modify_factory_name").val()); 
            var equipment = stringutil.Trim($("#modify_equipment").val());
            if(stringutil.checkString("modify_equipment",equipment,"设备型号不能为空!") 
            		|| stringutil.checkString("modify_equipment",equipment,"设备型号不能超过50位!",50) 
            		|| stringutil.checkString("modify_factory_name",factory_name,"厂家名称不能超过100位!",100)){
                return;
            }
            // 校验设备型号是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/equipmentmanage/query?random=" + Math.random(),
                cache: false,
                async: false, 
                data: {'model_name':equipment},
                success: function (result) {
                	var length = result.length;
                	var state = true;
                	if(length == 0 ){
                		state = true;
                	}else{
                		for(var i=0; i<length;i++){
                			if(result[i].model_name == equipment){
                				if(result[i].id!=factoryid) {
                					state = false;
                    				break;
                				}
                    		}
                		}
                	}
                	if(state){
                		loadingwait();
                        var data= {id:factoryid,model_name:equipment,factory_id:factory_name};
                        $.getJSON("/view/class/system/equipmentmanage/modify?random=" + Math.random(),data, function(result) {
                            layer.closeAll();
                            layerResultAndReload(result);
                        });
                	}else{
                		$("#modify_equipment").focus();
                        layer.tips("设备型号不能重复", '#modify_equipment',{ tips: [2, '#EE1A23'] });
                		return;
                	}
                }
             });
    	}
    }
    
    // 新增确认
    function addInfo(){
        var factory_name = stringutil.Trim($("#add_factory_name").val()); 
        var equipment = stringutil.Trim($("#add_equipment").val());
        if(stringutil.checkString("add_equipment",equipment,"设备型号不能为空!") 
        		|| stringutil.checkString("add_equipment",equipment,"设备型号不能超过50位!",50) 
        		|| stringutil.checkString("add_factory_name",factory_name,"厂商名称不能超过100位!",100)){
            return;
        }
        // 校验设备型号是否重复事件
        $.ajax({
            type: "post",
            url: "/view/class/system/equipmentmanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {"model_name": equipment},
            success: function (result) {
            	var length = result.length;
            	var state = true;
            	for (var i=0;i<length;i++){
            		if(result[i].model_name==equipment){
        				state = false;
        			}
        		}
            	if(state){
            		loadingwait();
                    var data= {'factory_id':factory_name,'model_name':equipment};
                    $.getJSON("/view/class/system/equipmentmanage/add?random=" + Math.random(),data, function(result) {
                        layer.closeAll();
                        layerResultAndReload(result);
                    });
            	}else{
            		$("#add_equipment").focus();
                    layer.tips("设备型号不能重复", '#add_equipment',{ tips: [2, '#EE1A23'] });
            		return;
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
        var factory_id = stringutil.Trim($("#factoryid").val());
        var data = {'model_name':model_name,'factory_id':factory_id};
        $.getJSON("/view/class/system/equipmentmanage/query?random=" + Math.random(),data, function(result) {
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
            var id = rowninfo.id;
            var checked = "";
            if(checkbox.isExitArray(id)){
            	checked = "checked=\"checked\"";
            	k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"  
            +"</td><td style='max-width:120px'>"+rowninfo.model_name+"</td><td style='max-width:120px'>"
            +rowninfo.factory_name+"</td><td>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\">详情</a>"
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
    		layer.msg("请选择一个设备!",{
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
                var data = {'equipmentidArray':checkboxArray};
                var url = "/view/class/system/equipmentmanage/delete?random=" + Math.random();
                $.getJSON(url,data,function(result){
                	layer.close(layer_load);
                	loadOptRecord();
                	layer.alert(result.message);
                })
            });
    	}
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
            area : [ '600px', '220px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '530px', '220px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] // 0.1透明度的白色背景
          });
    }
});