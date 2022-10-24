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
        
        resizewh.resizeBodyH($("#mainmetrictype"));
        butBindFunction();
        $("#checkboxAll").click(function() {
        	var flag = $(this).prop('checked');
        	checkbox.checkboxAll(flag,'checkboxAll','metrictypediv');
        });
        reset("mainmetrictype");
        initChildrenMenu();
        // loadingwait();
        // queryOpt();
        
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainmetrictype");
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
        checkbox.bindAllCheckbox('checkboxAll','metrictypediv');
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
    	$("#metrictypediv :checkbox").click(function(){  
        	var flag = $(this).prop('checked');
        	var value = $(this).val();
        	checkbox.checkboxSingle(flag,value);
        });
    }
    
    // 新增按钮事件
    function addShow(){
    	reset("add_div");
    	showLayer("add_div",'新增指标类型');
    }
    
    // 修改按钮事件
    function modifyShow(){
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个指标类型!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var metrictypeid = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {id : metrictypeid};
            $.ajax({
                type: "post",
                url: "/view/class/system/metrictypemanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                	var pageList = result.pageList;
                	if(pageList.length>0){
                        var metrictype = pageList[0];
                        $("#modify_metric_type_name").val(metrictype.metric_type_name);
                        $("#modify_description").val(metrictype.description);
                        showLayer("modify_div",'修改指标类型');
                    }
                }
             });
    	}
   }
    
    
    // 详情按钮事件
    function detailShow(metrictypeid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/metrictypemanage/query?id="+metrictypeid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	var pageList = result.pageList;
            	if(pageList.length>0){
                    var metrictype = pageList[0];
                    $("#detail_metric_type_name").val(metrictype.metric_type_name);
                    $("#detail_description").val(metrictype.description);
                    showLayerDetail("detail_div",'指标类型详情');
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
    		layer.msg("修改请只选择一个指标类型!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var metrictypeid = checkboxArray[0];
            var metric_type_name = stringutil.Trim($("#modify_metric_type_name").val()); 
            var description = stringutil.Trim($("#modify_description").val());
            if(stringutil.checkString("modify_metric_type_name",metric_type_name,"指标类型名称不能为空!") 
            		|| stringutil.checkString("modify_metric_type_name",metric_type_name,"指标类型名称不能超过50位!",50) 
            		|| stringutil.checkString("modify_description",description,"指标类型描述能超过100位!",100)){
                return;
            }
            var state = true;
            // 校验指标类型名称是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/metrictypemanage/query?random=" + Math.random(),
                cache: false,
                async: false,
                data: {metric_type_name:metric_type_name},
                success: function (result) {
                	var pageList = result.pageList;
                	var length = pageList.length;
                	for (var i=0;i<length;i++){
                		if(pageList[i].metric_type_name==metric_type_name){
                			if(pageList[i].id != metrictypeid){
                				state = false;
                				$("#modify_metric_type_name").focus();
                                layer.tips("指标类型名称不能重复", '#modify_metric_type_name',{ tips: [2, '#EE1A23'] });
                        		return;
                			}
            			}
            		}
                }
             });
            if(state){
        		loadingwait();
                var data= {id:metrictypeid,metric_type_name:metric_type_name,description:description};
                $.getJSON("/view/class/system/metrictypemanage/modify?random=" + Math.random(),data, function(result) {
                    layer.closeAll();
                    layerResultAndReload(result);
                });
        	}
    	}
    }
    
    // 新增确认
    function addInfo(){
        var metric_type_name = stringutil.Trim($("#add_metric_type_name").val()); 
        var description = stringutil.Trim($("#add_description").val());
        if(stringutil.checkString("add_metric_type_name",metric_type_name,"指标类型名称不能为空!") 
        		|| stringutil.checkString("add_metric_type_name",metric_type_name,"指标类型名称不能超过50位!",50) 
        		|| stringutil.checkString("add_description",description,"指标类型描述不能超过100位!",100)){
            return;
        }
        // 校验指标标识是否重复事件
        $.ajax({
            type: "post",
            url: "/view/class/system/metrictypemanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {"metric_type_name": metric_type_name},
            success: function (result) {
            	var pageList = result.pageList;
            	var length = pageList.length;
            	var state = true;
            	for (var i=0;i<length;i++){
            		if(pageList[i].metric_type_name==metric_type_name){
        				state = false;
        			}
        		}
            	if(state){
            		loadingwait();
                    var data= {metric_type_name:metric_type_name,description:description};
                    $.getJSON("/view/class/system/metrictypemanage/add?random=" + Math.random(),data, function(result) {
                        layer.closeAll();
                        addLayerResultAndReload(result);
                    });
            	}else{
            		$("#add_metric_type_name").focus();
                    layer.tips("指标类型名称不能重复", '#add_metric_type_name',{ tips: [2, '#EE1A23'] });
            		return;
            	}
            }
         });
    }
    
    function addJumpShow(operateid) {
        layer.confirm('是否需要跳转到指标管理页面？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            window.location.href = "/view/class/system/metricmanage?key=metricmanage&random=" + Math.random();
        });
    }
    
    // 查询按钮事件
    function queryOpt(){
        var metric_type_name = $("#metric_type_name").val();
        $("#metric_type_name_page").val(metric_type_name);
        loadOptRecord(1);
    }
    
    // 分页加载查询内容
    function loadOptRecord(pageNumber){
    	loadingwait();
        checkbox.cleanArray();
        $("#checkboxAll").prop("checked", false);
        var metric_type_name = stringutil.Trim($("#metric_type_name_page").val());
        var data = {metric_type_name:metric_type_name,pageNumber:pageNumber};
        $.getJSON("/view/class/system/metrictypemanage/query?random=" + Math.random(),data, function(result) {
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
                resizewh.resizeBodyH($("#mainmetrictype"));
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
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"
            +"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.metric_type_name)+"\">"+stringutil.isNull(rowninfo.metric_type_name)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.description)+"\">"
            +stringutil.isNull(rowninfo.description)+"</td><td>"
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
        $("#metrictypediv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("metrictypediv");
    }
    
    function deleteShow() {
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length == 0){
    		layer.msg("请选择一个指标类型!",{
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
                var data = {metricTypeidArray:checkboxArray};
                var url = "/view/class/system/metrictypemanage/delete/?random=" + Math.random();
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
            area : [ '600px', '270px' ],
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