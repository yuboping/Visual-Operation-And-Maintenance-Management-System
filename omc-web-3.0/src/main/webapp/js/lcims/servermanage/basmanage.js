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
        
        resizewh.resizeBodyH($("#mainbas"));
        butBindFunction();
        $("#checkboxAll").click(function() {
        	var flag = $(this).prop('checked');
        	checkbox.checkboxAll(flag,'checkboxAll','basdiv');
        });
        reset("mainbas");
        initChildrenMenu();
//        loadingwait();
//        loadOptRecord();
        
//----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainbas");
        });
        $("#exportbutton").click(function() {
            exportExcel();
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
        
        //设置属地
        $("#setArea_ok").click(function() {
            setBrasArea();
        });
        $("#setArea_cancle").click(function() {
            layer.closeAll();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','basdiv');
        
    }
    //重置页面标签内容
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
 					//新增、修改、删除绑定事件
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
 					}else if(result[i].url=='export'){
 					    $("#"+result[i].name).click(function() {
 					        exportShow();
 				            });
 					}else if(result[i].url=='refresh'){
 					   $("#"+result[i].name).click(function() {
                                               refreshBras();
                                           });
 					}else if(result[i].url=='setArea'){
                                            $("#"+result[i].name).click(function() {
                                                setBrasAreaShow();
                                            });
                                         }
     			}
     		}
     		
     	});
    }
    
    function bindCheckBox(){
    	$("#basdiv :checkbox").click(function(){  
        	var flag = $(this).prop('checked');
        	var value = $(this).val();
        	checkbox.checkboxSingle(flag,value);
        });
    }
    
    //新增按钮事件
    function addShow(){
    	reset("add_div");
        $.getJSON("/view/class/system/basmanage/query/mdparamlist?type=14&random=" + Math.random(), function(result) {
            $("#add_ip_type").empty();
            $.each(result,function(i,data){
                $("#add_ip_type").append("<option value=\""+data.code+"\">"+data.description+"</option>");
            });
        });
        $.getJSON("/view/class/system/basmanage/query/mdfactorylist?random=" + Math.random(), function(result) {
            $("#add_factory_name").empty();
            $("#add_equip_name").empty();
            $("#add_factory_name").append("<option value=\"\">请选择</option>");
            $.each(result,function(i,data){
            	$("#add_factory_name").append("<option value=\""+data.id+"\">"+data.factory_name+"</option>");
            });
        });
        $("#add_factory_name").change(function() {
        	var factory_name = $("#add_factory_name").val();
        	if(factory_name == "") {
        		$("#add_equip_name").empty();
            	$("#add_equip_name").append("<option value=\"\"></option>");
        	}else{
        		$.getJSON("/view/class/system/basmanage/query/equiplist?factory_id="+factory_name+"&random=" + Math.random(), function(result) {
					$("#add_equip_name").empty();
					$.each(result,function(i,data){
						$("#add_equip_name").append("<option value=\""+data.id+"\">"+data.model_name+"</option>");
					});
				});
        	}
	    });
        $.getJSON("/view/class/system/basmanage/query/mdarealist?random=" + Math.random(), function(result) {
            $("#add_area_name").empty();
            $.each(result,function(i,data){
            	$("#add_area_name").append("<option value=\""+data.areano+"\">"+data.name+"</option>");
            });
        });
        showLayer("add_div",'新增BRAS');
    }
    
    //新增确认
    function addInfo(){
        var bas_name = stringutil.Trim($("#add_bas_name").val());
        var bas_ip = stringutil.Trim($("#add_bas_ip").val());
        var equip_name = $("#add_equip_name").val();
        var factory_name = $("#add_factory_name").val();
        var area_name = stringutil.Trim($("#add_area_name").val());
        var ip_type = stringutil.Trim($("#add_ip_type").val());
        var description = stringutil.Trim($("#add_description").val());
        if(stringutil.checkString("add_bas_name",bas_name," 设备名称不能为空!")|| 
        		stringutil.checkString('add_bas_name',bas_name," 设备名称不能超过50位!",50) ||
        		stringutil.checkString('add_bas_ip',bas_ip," 设备IP不能为空!") || 
        		stringutil.checkString('add_bas_ip',bas_ip," 设备IP不能超过100位!",100) ||
        		stringutil.checkString('add_equip_name',equip_name,"设备型号不能为空!") ||
        		stringutil.checkString('add_equip_name',equip_name,"设备型号不能超过35位!",35) ||
        		stringutil.checkString('add_factory_name',factory_name,"厂家名称不能为空!") ||
        		stringutil.checkString('add_factory_name',factory_name,"厂家名称不能超过35位!",35) ||
        		stringutil.checkString('add_area_name',area_name,"属地名称不能为空!") ||
        		stringutil.checkString('add_area_name',area_name,"属地名称不能超过50位!",50) ||
        		stringutil.checkString('add_ip_type',ip_type,"IP类型不能为空!") ||
        		stringutil.checkString('add_ip_type',ip_type,"IP类型不能超过35位!",35) ||
        		stringutil.checkString('add_description',description," BRAS描述不能超过100位!",100)){
            return;
        }
        if(validAddr('add',bas_ip)) {
        	return;
        }
        //校验设备名称是否重复事件
        $.ajax({
            type: "post",
            url: "/view/class/system/basmanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {"bas_name": bas_name},
            success: function (result) {
            	var length = result.length;
            	var state = true;
            	for (var i=0;i<length;i++){
            		if(result[i].nas_name==bas_name){
        				state = false;
        			}
        		}
        		if(state) {
        			$.ajax({
						type: "post",
						url: "/view/class/system/basmanage/query?random=" + Math.random(),
						cache: false,
						async: false, 
						data: {"bas_ip": bas_ip},
						success: function (result) {
							var length = result.length;
							var state = true;
							for (var i=0;i<length;i++){
								if(result[i].nas_ip==bas_ip){
									state = false;
								}
							}
							if(state) {
								loadingwait();
								var data= {'nas_name':bas_name,'nas_ip':bas_ip,'equip_id':equip_name,'area_no':area_name,'ip_type':ip_type,'description':description};
								$.getJSON("/view/class/system/basmanage/add?random=" + Math.random(),data, function(result) {
									layer.closeAll();
									layerResultAndReload(result);
								});
							} else {
								$("#add_bas_ip").focus();
								layer.tips("设备IP不能重复", '#add_bas_ip',{ tips: [2, '#EE1A23'] });
								return;
							}
						}
					});
        		} else {
        			$("#add_bas_name").focus();
                    layer.tips("设备名称不能重复", '#add_bas_name',{ tips: [2, '#EE1A23'] });
            		return;
        		}
            }
         });
    }
    
    //修改按钮事件
    function modifyShow(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个BRAS!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var basid = checkboxArray[0];
            reset("modify_div");
            
            loadingwait();
            var data = {id : basid};
            $.ajax({
				type: "post",
				url: "/view/class/system/basmanage/query/mdfactorylist?random=" + Math.random(),
				cache: false,
				async: false, 
				dataType: "json",
				success: function (result) {
					$("#modify_factory_name").empty();
					$.each(result,function(i,data){
		            	$("#modify_factory_name").append("<option value=\""+data.id+"\">"+data.factory_name+"</option>");
		            });
				}
			});
			$.ajax({
				type: "post",
				url: "/view/class/system/basmanage/query/mdparamlist?type=14&random=" + Math.random(),
				cache: false,
				async: false, 
				dataType: "json",
				success: function (result) {
					$("#modify_ip_type").empty();
                    $.each(result,function(i,data){
                        $("#modify_ip_type").append("<option value=\""+data.code+"\">"+data.description+"</option>");
                    });
				}
			});
			$.ajax({
				type: "post",
				url: "/view/class/system/basmanage/query/mdarealist?random=" + Math.random(),
				cache: false,
				async: false, 
				dataType: "json",
				success: function (result) {
					$("#modify_area_name").empty();
					$.each(result,function(i,data){
						$("#modify_area_name").append("<option value=\""+data.areano+"\">"+data.name+"</option>");
					});
				}
			});
            $.ajax({
                type: "post",
                url: "/view/class/system/basmanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                	if(result.length>0){
                        var bas = result[0];
                        $("#modify_bas_name").val(bas.nas_name);
                        $("#modify_bas_ip").val(bas.nas_ip);
                        $("#modify_description").val(bas.description);
                        $("#modify_factory_name option").each(function(){
							if($(this).attr('value')==bas.factory_id){
								$(this).prop("selected", "true");
							}
						});
						var fact_id = $("#modify_factory_name").val();
						$.ajax({
							type: "post",
							url: "/view/class/system/basmanage/query/equiplist?factory_id="+fact_id+"&random=" + Math.random(),
							cache: false,
							async: false, 
							dataType: "json",
							success: function (result) {
								$("#modify_equip_name").empty();
								$.each(result,function(i,data){
									$("#modify_equip_name").append("<option value=\""+data.id+"\">"+data.model_name+"</option>");
								});
							}
						});
						$("#modify_equip_name option").each(function(){
							if($(this).attr('value')==bas.equip_id){
								$(this).prop("selected", "true");
							}
						});
						$("#modify_ip_type option").each(function(){
							if($(this).attr('value')==bas.ip_type){
								$(this).prop("selected", "true");
							}
						});
						$("#modify_area_name option").each(function(){
							if($(this).attr('value')==bas.area_no){
								$(this).prop("selected", "true");
							}
						});
						$("#modify_factory_name").change(function() {
							var factory_name = $("#modify_factory_name").val();
							$.getJSON("/view/class/system/basmanage/query/equiplist?factory_id="+factory_name+"&random=" + Math.random(), function(result) {
								$("#modify_equip_name").empty();
								$.each(result,function(i,data){
									$("#modify_equip_name").append("<option value=\""+data.id+"\">"+data.model_name+"</option>");
								});
							});
						});
                        showLayer("modify_div",'修改BRAS');
                    }
                }
             });
    	}
   }
   
   //修改确认
    function modifyInfo(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个BRAS!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var basid = checkboxArray[0];
        	var bas_name = stringutil.Trim($("#modify_bas_name").val());
			var bas_ip = stringutil.Trim($("#modify_bas_ip").val());
			var equip_name = $("#modify_equip_name").val();
			var area_name = stringutil.Trim($("#modify_area_name").val());
			var ip_type = stringutil.Trim($("#modify_ip_type").val());
			var description = stringutil.Trim($("#modify_description").val());
            if(stringutil.checkString("modify_bas_name",bas_name," 设备名称不能为空!")|| 
        		stringutil.checkString('modify_bas_name',bas_name," 设备名称不能超过50位!",50) ||
        		stringutil.checkString('modify_bas_ip',bas_ip," 设备IP不能为空!") || 
        		stringutil.checkString('modify_bas_ip',bas_ip," 设备IP不能超过100位!",100) ||
        		stringutil.checkString('modify_equip_name',equip_name,"设备型号不能为空!") ||
        		stringutil.checkString('modify_equip_name',equip_name,"设备型号不能超过35位!",35) ||
        		stringutil.checkString('modify_area_name',area_name,"属地名称不能为空!") ||
        		stringutil.checkString('modify_area_name',area_name,"属地名称不能超过50位!",50) ||
        		stringutil.checkString('modify_ip_type',ip_type,"IP类型不能为空!") ||
        		stringutil.checkString('modify_ip_type',ip_type,"IP类型不能超过35位!",35) ||
        		stringutil.checkString('modify_description',description," BRAS描述不能超过100位!",100)){
	            return;
	        }
	        if(validAddr('modify',bas_ip)) {
	        	return;
	        }
            //校验设备名称是否重复事件
	        $.ajax({
	            type: "post",
	            url: "/view/class/system/basmanage/query?random=" + Math.random(),
	            cache: false,
	            async: false, 
	            data: {"bas_name": bas_name},
	            success: function (result) {
	            	var length = result.length;
	            	var state = true;
	            	for (var i=0;i<length;i++){
	            		if(result[i].nas_name==bas_name && result[i].id!=basid){
	        				state = false;
	        			}
	        		}
	        		if(state) {
	        			$.ajax({
							type: "post",
							url: "/view/class/system/basmanage/query?random=" + Math.random(),
							cache: false,
							async: false, 
							data: {"bas_ip": bas_ip},
							success: function (result) {
								var length = result.length;
								var state = true;
								for (var i=0;i<length;i++){
									if(result[i].nas_ip==bas_ip && result[i].id!=basid){
										state = false;
									}
								}
								if(state) {
									loadingwait();
									var data= {'id':basid,'nas_name':bas_name,'nas_ip':bas_ip,'equip_id':equip_name,'area_no':area_name,'ip_type':ip_type,'description':description};
									$.getJSON("/view/class/system/basmanage/modify?random=" + Math.random(),data, function(result) {
										layer.closeAll();
			                            layerResultAndReload(result);
									});
								} else {
									$("#modify_bas_ip").focus();
									layer.tips("设备IP不能重复", '#modify_bas_ip',{ tips: [2, '#EE1A23'] });
									return;
								}
							}
						});
	        		} else {
	        			$("#modify_bas_name").focus();
	                    layer.tips("设备名称不能重复", '#modify_bas_name',{ tips: [2, '#EE1A23'] });
	            		return;
	        		}
	           }
	        });
    	}
    }
    
  	//详情按钮事件
    function detailShow(basid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/basmanage/queryDetail?id="+basid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	if(result.length>0){
                    var bas = result[0];
                    $("#detail_bas_name").val(bas.nas_name);
                    $("#detail_bas_ip").val(bas.nas_ip);
                    $("#detail_equip_name").val(bas.modelname);
                    $("#detail_factory_name").val(bas.factoryname);
                    $("#detail_area_name").val(bas.areaname);
                    $("#detail_ip_type").val(bas.iptype);
                    $("#detail_description").val(bas.description);
                    showLayerDetail("detail_div",' bas详情');
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
        layer.confirm('是否需要跳转到主机 bas配置管理页面？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function(){
            window.location.href = "/view/class/system/hostbasmanage?key=hostbasmanage&operatetype=1&operateid="+operateid+"&random=" + Math.random();
        });
    }
    
    //查询按钮事件
    function queryOpt(){
    	var addr = $("#nas_ip").val();
        if(addr != null && addr != ""){
        	var addrress = stringutil.Trim(addr);
            if (!checkIp(addrress) && addrress != "") {
                layer.tips('请输入正确格式的设备IP!', '#nas_ip',{ tips: [2, '#EE1A23'] });
                $("#nas_ip").focus();
                return false;
            }
        }
        loadingwait();
        loadOptRecord();
    }
    
    //导出Excel
    function exportShow(){
		downloadExcel();
    }
    
    function downloadExcel(){
		window.open("/view/class/system/basmanage/export?random=" + Math.random(),"_blank");
    }
    
    //加载查询内容
    function loadOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();
        
        var nas_name = stringutil.Trim($("#nas_name").val());
        var nas_ip = stringutil.Trim($("#nas_ip").val());
        var ip_type = stringutil.Trim($("#nas_type").val());
        var area_no = stringutil.Trim($("#area_no").val());
        var data = {'nas_name':nas_name,'ip_type':ip_type,'nas_ip':nas_ip,'area_no':area_no};
        $.getJSON("/view/class/system/basmanage/query?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#querynum").text(total);
            laypage({
                cont: 'pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: false, //是否开启跳页
                jump: function(obj, first){ //触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showTable(result,startnum,endnum);
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#mainbas"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接tr
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
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"+"</td>"
            + "<td>"
            +rowninfo.nas_name+"</td><td>"+rowninfo.nas_ip+"</td><td style='max-width:120px'>"
            +stringutil.isNull(rowninfo.modelname)+"</td><td style='max-width:120px'>"
            +stringutil.isNull(rowninfo.factoryname)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.areaname)+"\">"+stringutil.isNull(rowninfo.areaname)+"</td><td>"
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
        $("#basdiv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("basdiv");
    }
    
    function deleteShow() {
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length == 0){
    		layer.msg("请选择一个BRAS!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		layer.confirm('是否确认删除该批次数据？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] //按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {basidArray:checkboxArray};
                var url = "/view/class/system/basmanage/delete/?random=" + Math.random();
                $.getJSON(url,data,function(result){
                	layer.close(layer_load);
                	loadOptRecord();
                	layer.alert(result.message);
                })
            });
    	}
    }
    function refreshBras(){
        layer.confirm('是否确认同步Bras数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function(){
            layer.closeAll();
            loadingwait();
            var data = null;
            var url = "/view/class/system/basmanage/refreshBras/?random=" + Math.random();
            $.getJSON(url,data,function(result){
                    layer.close(layer_load);
                    loadOptRecord();
                    layer.alert(result.message);
            })
        });
    }
    
    //打开设置属地页面
    function setBrasAreaShow(){
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
                layer.msg("请选择一个BRAS!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            reset("set_area_div");
            $.getJSON("/view/class/system/basmanage/query/mdarealist?random=" + Math.random(), function(result) {
                $("#set_area_name").empty();
                $.each(result,function(i,data){
                    $("#set_area_name").append("<option value=\""+data.areano+"\">"+data.name+"</option>");
                });
            });
            showLayerSetBrasArea("set_area_div","批量修改属地");
        }
    }
    
    function showLayerSetBrasArea(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '350px', '200px' ],
            content : $("#"+divid)
        });
    }
    
    //确认属地修改
    function setBrasArea(){
        var arr = checkbox.getReturnArray();
        var brasids=arr.toString();
        var areano = $("#set_area_name").val();
        var data = {brasids:brasids,areano:areano};
        var url = "/view/class/system/basmanage/setBrasArea";
        loadingwait();
        $.ajax({
            type: "post",
            url: url+"?random=" + Math.random(),
            data: data,
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
                layer.close(layer_load);
                checkbox.cleanArray();
                layer.closeAll();
                layer.msg(result.message,{
                    time:2000,
                    skin: 'layer_msg_color_success'
                });
                loadOptRecord();
            }
         });
    }
    
    function validAddr(type,addr){
        if (!checkIp(addr)) {
            $("#"+type+"_bas_ip").focus();
            layer.tips('请输入正确格式的设备IP!', '#'+type+'_bas_ip',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    
    //校验IP
    function checkIp(ip) {
        var regex1 = /^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
        var regex2 = '0.0.0.0';
        return regex1.test(ip) && regex2 != ip;
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
        	queryOpt();
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
            area : [ '620px', '360px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '500px', '360px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
});