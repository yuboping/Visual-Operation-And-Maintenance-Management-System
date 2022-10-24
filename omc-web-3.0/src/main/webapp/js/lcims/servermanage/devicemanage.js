require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage"
    }
});

require(['jquery','layer','laypage','resizewh'],
        function($,layer,laypage,resizewh) {
	resizewh.resizeWH($("#maindevicemanage"));
	
	$("#querybutton").click(function() {
        queryOpt();
    });
    $("#addbutton").click(function() {
        addShow();
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
    
    //查询按钮事件
    function queryOpt(){
    	var source = $("#source").val();
        if(source == null && source == ""){
        	 layer.tips('请选择名称!', '#source',{ tips: [2, '#EE1A23'] });
             $("#source").focus();
             return false;
        }
        loadingwait();
        loadOptRecord();
    }
    //加载查询内容
    function loadOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 8;
        
        var nodeid = $("#nodeid").val();
        var type = $("#type").val();
        var networktype = $("#networktype").val();
        var device_name = $("#device_name").val();
        var data = {'nodeid':nodeid,'type':type,'networktype':networktype,'device_name':device_name};
        $.getJSON("/view/class/system/device/query/infolist?random=" + Math.random(),data, function(result) {
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
                    resizewh.resizeWH($("#mainhost"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    
    //拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            rowdata = rowdata + "<tr><td>"+rowninfo.device_name+"</td>"
            +"<td>"+isNull(rowninfo.location)+"</td>"
            +"<td>"
            +isNull(rowninfo.nodename)+"</td><td>"+rowninfo.typename+"</td><td>"
            +isNull(rowninfo.networktypename)+"</td><td>"+isNull(rowninfo.device_room)+"</td>"
            +"<td>"
            +"<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""+rowninfo.device_id+"\" >修改</a>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""+rowninfo.device_id+"\">删除</a>"
            +"</td></tr>";
        }
        $("#devicediv").empty().append(rowdata);
        $("[name=modify]").each(function(){
            $(this).on('click',function(){
                modifyShow($(this).attr('id'));
            });
        });
        $("[name=delete]").each(function(){
            $(this).on('click',function(){
                deleteShow($(this).attr('id'));
            });
        });
    }
    
    function isNull(data){
    	if(data==null || data ==''){
    		return '';
    	}else
    		return data;
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
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
    
    
    function getParamData(operate){
    	$.ajax({
            type: "post",
            url: "/view/class/system/device/query/initData?random=" + Math.random(),
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
            	$("#"+operate+"_nodeid").empty();
            	$("#"+operate+"_type").empty();
            	$("#"+operate+"_networktype").empty();
        		var nodelist = result[0];
        		var typelist = result[1];
        		var networkTypelist = result[2];
        		for(var i=0; i<nodelist.length;i++){
        			$("#"+operate+"_nodeid").append("<option value=\""+nodelist[i].nodeid+"\">"+nodelist[i].name+"</option>");
        		}
        		for(var i=0; i<typelist.length;i++){
        			$("#"+operate+"_type").append("<option value=\""+typelist[i].code+"\">"+typelist[i].description+"</option>");
        		}
        		for(var i=0; i<networkTypelist.length;i++){
        			$("#"+operate+"_networktype").append("<option value=\""+networkTypelist[i].code+"\">"+networkTypelist[i].description+"</option>");
        		}
            }
         });
    }
    
    //新增按钮事件
    function addShow(){
		reset("add_div");
		getParamData("add");
		showLayer("add_div",'新增网络关系');
    }
    
    function addInfo(){
    	var type = $("#add_type").val();
    	var device_name = $("#add_device_name").val();
    	var nodeid = $("#add_nodeid").val();
    	var networktype = $("#add_networktype").val();
    	var device_room = $("#add_device_room").val();
    	var location = $("#add_location").val();
    	if(validDeviceName('add',device_name)|| validDeviceRoom('add',device_room)){
            return;
        }
    	var data = {type:type,device_name:device_name,nodeid:nodeid,networktype:networktype,device_room:device_room,location:location};
    	loadingwait();
    	$.getJSON("/view/class/system/device/mamage/add/?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }
    
    function validDeviceName(type,value){
        if(null == value || value==""){
            $("#"+type+"_device_name").focus();
            layer.tips('设备名称不能为空!', '#'+type+'_device_name',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    function validDeviceRoom(type,value){
        if(null == value || value==""){
            $("#"+type+"_device_room").focus();
            layer.tips('设备位置不能为空!', '#'+type+'_device_room',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '480px', '360px' ],
            content : $("#"+divid)
        });
    }
    
    function layerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
            loadOptRecord();
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
    
    
    function deleteShow(id) {
        layer.confirm('是否确认删除该条数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function(){
            layer.closeAll();
            loadingwait();
            var data = {device_id:id};
            var url = "/view/class/system/device/mamage/delete/?random=" + Math.random();
            $.getJSON(url,data,function(result){
                layerResultAndReload(result);
            })
        });
    }
    
    
    //修改按钮事件
    function modifyShow(id){
        reset("modify_div");
        getParamData("modify");
        var data = {device_id:id};
        //查询网络关系信息
        loadingwait();
        $.getJSON("/view/class/system/device/querySingleinfo?random=" + Math.random(),data,function(result){
        	$("#modify_device_id").val(id);
        	$("#modify_device_name").val(result.device_name);
        	$("#modify_device_room").val(result.device_room);
        	$("#modify_location").val(result.location);
            $("#modify_nodeid option").each(function(){
        		if($(this).attr('value')==result.nodeid){
        			$(this).prop("selected", "true");
        		}
        	});
            $("#modify_type option").each(function(){
        		if($(this).attr('value')==result.type){
        			$(this).prop("selected", "true");
        		}
        	});
            $("#modify_networktype option").each(function(){
        		if($(this).attr('value')==result.networktype){
        			$(this).prop("selected", "true");
        		}
        	});
            
            showLayer("modify_div",'修改网络关系');
        });
    }
    
    //修改确认
    function modifyInfo(){
    	var device_id = $("#modify_device_id").val();
    	var type = $("#modify_type").val();
    	var device_name = $("#modify_device_name").val();
    	var nodeid = $("#modify_nodeid").val();
    	var networktype = $("#modify_networktype").val();
    	var device_room = $("#modify_device_room").val();
    	var location = $("#modify_location").val();
    	if(validDeviceName('modify',device_name)|| validDeviceRoom('modify',device_room)){
            return;
        }
    	var data = {device_id:device_id,type:type,device_name:device_name,nodeid:nodeid,networktype:networktype,device_room:device_room,location:location};
    	loadingwait();
    	$.getJSON("/view/class/system/device/mamage/modify/?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }
    
});