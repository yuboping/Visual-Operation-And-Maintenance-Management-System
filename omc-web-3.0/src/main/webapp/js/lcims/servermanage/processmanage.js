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
        
        resizewh.resizeBodyH($("#mainprocess"));
        butBindFunction();
        $("#checkboxAll").click(function() {
        	var flag = $(this).prop('checked');
        	checkbox.checkboxAll(flag,'checkboxAll','processdiv');
        });
        reset("mainprocess");
        initChildrenMenu();
        // loadingwait();
        // queryOpt();
        
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainprocess");
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
        checkbox.bindAllCheckbox('checkboxAll','processdiv');
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
    	$("#processdiv :checkbox").click(function(){  
        	var flag = $(this).prop('checked');
        	var value = $(this).val();
        	checkbox.checkboxSingle(flag,value);
        });
    }
    
    // 新增按钮事件
    function addShow(){
    	reset("add_div");
    	showLayer("add_div",'新增进程');
    }
    
    // 修改按钮事件
    function modifyShow(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个进程!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var processid = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {process_id : processid};
            $.ajax({
                type: "post",
                url: "/view/class/system/processmanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                	var pageList = result.pageList;
                	if(pageList.length>0){
                        var process = pageList[0];
                        $("#modify_process_name").val(process.process_name);
                        $("#modify_process_key").val(process.process_key);
                        $("#modify_description").val(process.description);
                        showLayer("modify_div",'修改进程');
                    }
                }
             });
    	}
   }
    
    // 详情按钮事件
    function detailShow(processid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/processmanage/query?process_id="+processid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	var pageList = result.pageList;
            	if(pageList.length>0){
                    var process = pageList[0];
                    $("#detail_process_name").val(process.process_name);
                    $("#detail_process_key").val(process.process_key);
                    $("#detail_description").val(process.description);
                    detailHostProcess(1, processid);
                    showLayerDetail("detail_div",'进程详情');
                }
            }
         });
    }
    
    //查询主机进程管理信息
    function detailHostProcess(pageNumber,processid){
        var linkData = {'process_id':processid,pageNumber:pageNumber};
        $("#detaildiv").empty()
        $.getJSON("/view/class/system/hostprocessmanage/query?random=" + Math.random(),linkData, function(result) {
        	layer.close(layer_load);
        	if(result!=null && result!=''){
        		detailResetPage(result,processid);
        	}
        });
    }
    
    //详情重置分页(跳转分页)
    function detailResetPage(result,processid) {
    	var totalCount = result.totalCount;
    	var pageList = result.pageList;
    	var start = result.start;
    	var end = result.end;
    	var pageNumber = result.pageNumber;
    	var totalPages = result.totalPages;
        laypage({
            cont: "detailpageinfo", //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: totalPages, //通过后台拿到的总页数
            curr: pageNumber, //当前页
            jump: function (obj, first) { //触发分页后的回调
            	detailShowTable(pageList,start,end);
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                	detailHostProcess(obj.curr,processid);
                }
            }
        });
    }
    
    // 详情拼接tr
    function detailShowTable(data,startnum,endnum){
        var rowdata = "";
        for(var i=0;i<=endnum-startnum;i++){
            var rowninfo = data[i];
            var id = rowninfo.id;
                rowdata = rowdata + "<tr>"
                +"<td class='over_ellipsis' style='max-width:80px' title=\""+stringutil.isNull(rowninfo.host_ip)+"\">"+stringutil.isNull(rowninfo.host_ip)+"</td>"
                +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.start_script)+"\">"+stringutil.isNull(rowninfo.start_script)+"</td>"
                +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.stop_script)+"\">"+stringutil.isNull(rowninfo.stop_script)+"</td>"
                +"<td class='over_ellipsis' style='max-width:200px' title=\""+stringutil.isNull(rowninfo.description)+"\">"+stringutil.isNull(rowninfo.description)+"</td>"
                +"</tr>";
        }
        $("#detaildiv").empty().append(rowdata);
    }
    
    // 修改确认
    function modifyInfo(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个进程!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var processid = checkboxArray[0];
            var process_name = stringutil.Trim($("#modify_process_name").val());
            var process_key = stringutil.Trim($("#modify_process_key").val()); 
            var description = stringutil.Trim($("#modify_description").val());
            if(stringutil.checkString("modify_process_name",process_name,"进程名称不能为空!") ||
            		stringutil.checkString("modify_process_key",process_key,"进程关键字不能为空!") ||
            		stringutil.checkString("modify_process_name",process_name,"进程名称不能超过50位!",50) || 
            		stringutil.checkString("modify_process_key",process_key,"进程关键字不能超过40位!",40) ||
            		stringutil.checkString("modify_description",description,"进程描述不能超过100位!",100)){
                return;
            }
            var state = true;
            // 校验进程名称是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/processmanage/query?random=" + Math.random(),
                cache: false,
                async: false, 
                data: {process_name: process_name},
                success: function (result) {
                	var pageList = result.pageList;
                	var length = pageList.length;
                	for (var i=0;i<length;i++){
                		if(pageList[i].process_name==process_name){
                			if(pageList[i].process_id != processid){
                				state = false;
                				$("#modify_process_name").focus();
                                layer.tips("进程名称不能重复", '#modify_process_name',{ tips: [2, '#EE1A23'] });
                        		return;
                			}
            			}
            		}
                }
             });
            if(state){
            	//校验进程关键字是否重复事件
                $.ajax({
                    type: "post",
                    url: "/view/class/system/processmanage/query?random=" + Math.random(),
                    cache: false,
                    async: false, 
                    data: {process_key:process_key},
                    success: function (result) {
                    	var pageList = result.pageList;
                    	var length = pageList.length;
                    	for (var i=0;i<length;i++){
                    		if(pageList[i].process_key==process_key){
                    			if(pageList[i].process_id != processid){
	                				state = false;
	                				$("#modify_process_key").focus();
	                                layer.tips("进程关键字不能重复", '#modify_process_key',{ tips: [2, '#EE1A23'] });
	                        		return;
                    			}
                			}
                		}
                    }
                 });
            }
            if(state){
        		loadingwait();
                var data= {process_id:processid,process_name:process_name,process_key:process_key,description:description};
                $.getJSON("/view/class/system/processmanage/modify?random=" + Math.random(),data, function(result) {
                    layer.closeAll();
                    layerResultAndReload(result);
                });
        	}
    	}
    }
    
    // 新增确认
    function addInfo(){
        var process_name = stringutil.Trim($("#add_process_name").val()); 
        var process_key = stringutil.Trim($("#add_process_key").val());
        var description = stringutil.Trim($("#add_description").val());
        if(stringutil.checkString("add_process_name",process_name,"进程名称不能为空!") ||
        		stringutil.checkString("add_process_key",process_key,"进程关键字不能为空!") ||
        		stringutil.checkString("add_process_name",process_name,"进程名称不能超过50位!",50) || 
        		stringutil.checkString("add_process_key",process_key,"进程关键字不能超过40位!",40) ||
        		stringutil.checkString("add_description",description,"进程描述不能超过100位!",100)){
            return;
        }
        var state = true;
        // 校验进程名称是否重复事件
        $.ajax({
            type: "post",
            url: "/view/class/system/processmanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {process_name: process_name},
            success: function (result) {
            	var pageList = result.pageList;
            	var length = pageList.length;
            	for (var i=0;i<length;i++){
            		if(pageList[i].process_name==process_name){
        				state = false;
        				$("#add_process_name").focus();
                        layer.tips("进程名称不能重复", '#add_process_name',{ tips: [2, '#EE1A23'] });
                		return;
        			}
        		}
            }
         });
        if(state){
        	//校验进程关键字是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/processmanage/query?random=" + Math.random(),
                cache: false,
                async: false, 
                data: {process_key:process_key},
                success: function (result) {
                	var pageList = result.pageList;
                	var length = pageList.length;
                	for (var i=0;i<length;i++){
                		if(pageList[i].process_key==process_key){
            				state = false;
            				$("#add_process_key").focus();
                            layer.tips("进程关键字不能重复", '#add_process_key',{ tips: [2, '#EE1A23'] });
                    		return;
            			}
            		}
                }
             });
        }
    	if(state){
    		loadingwait();
            var data= {process_name:process_name,process_key:process_key,description:description};
            $.getJSON("/view/class/system/processmanage/add?random=" + Math.random(),data, function(result) {
                layer.closeAll();
                addLayerResultAndReload(result);
            });
    	}
    }
    
    function addJumpShow(operateid) {
        layer.confirm('是否需要跳转到主机进程关系管理页面？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            window.location.href = "/view/class/system/hostprocessmanage?key=hostprocessmanage&operatetype=2&operateid="+operateid+"&random=" + Math.random();
        });
    }
    
    // 查询按钮事件
    function queryOpt(){
        var process_name = $("#process_name").val();
        $("#process_name_page").val(process_name);
        var process_key = $("#process_key").val();
        $("#process_key_page").val(process_name);
        loadOptRecord(1);
    }
    
    // 分页加载查询内容
    function loadOptRecord(pageNumber){
    	loadingwait();
        checkbox.cleanArray();
        $("#checkboxAll").prop("checked", false);
        var process_name = stringutil.Trim($("#process_name_page").val());
        var process_key = stringutil.Trim($("#process_key_page").val());
        var data = {process_name:process_name,process_key:process_key,pageNumber:pageNumber};
        var process_detail_show = $("#process_detail_show").val();
        $.getJSON("/view/class/system/processmanage/query?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            resetPage(result);
        });
    }
   
    //重置分页(跳转分页)
    function resetPage(result) {
    	var totalCount = result.totalCount;
    	var pageList = result.pageList;
    	var start = result.start;
    	var end = result.end;
    	var pageNumber = result.pageNumber;
    	var totalPages = result.totalPages;
        $("#querynum").text(totalCount);
        laypage({
            cont: "pageinfo", //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: totalPages, //通过后台拿到的总页数
            curr: pageNumber, //当前页
            jump: function (obj, first) { //触发分页后的回调
            	showTable(pageList,start,end);
                $("#currnum").text( start + "-" + end);
                if(totalCount==0){
              	  $("#currnum").empty().text("0 ");
                }
                $("#page_curr").val(obj.curr);
                resizewh.resizeBodyH($("#mainprocess"));
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    loadOptRecord(obj.curr);
                }
            }
        });
    }
    
    // 拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        var k = 0;
        for(var i=0;i<=endnum-startnum;i++){
            var rowninfo = data[i];
            var id = rowninfo.id;
            var checked = "";
            if(checkbox.isExitArray(id)){
            	checked = "checked=\"checked\"";
            	k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.process_id+"\" id=\""+rowninfo.process_id+"\" "+checked+" />"
            +"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.process_name)+"\">"+stringutil.isNull(rowninfo.process_name)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.process_key)+"\">"+stringutil.isNull(rowninfo.process_key)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.description)+"\">"
            +stringutil.isNull(rowninfo.description)+"</td><td>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.process_id+"\">详情</a>"
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
        $("#processdiv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("processdiv");
    }
    
    function deleteShow() {
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length == 0){
    		layer.msg("请选择一个进程!",{
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
                var data = {processidArray:checkboxArray};
                var url = "/view/class/system/processmanage/delete/?random=" + Math.random();
                $.getJSON(url,data,function(result){
                	layer.close(layer_load);
                	queryOpt();
                	layer.alert(result.message);
                })
            });
    	}
    }
    
    function layerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
        	var page_curr = $("#page_curr").val();
        	loadOptRecord(page_curr);
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
//        	addJumpShow(result.data);
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
    
    function showLayerDetail(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '720px', '460px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '530px', '250px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] // 0.1透明度的白色背景
          });
    }
});