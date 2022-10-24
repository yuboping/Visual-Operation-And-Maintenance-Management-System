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
	resizewh.resizeWH($("#mainnetworklinkmanage"));
	
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
    
    $("#linktype").bind("change",function(){
    	queryLinkTypeInfo();
    });
    
    $("#add_type").bind("change",function(){
    	addLinkTypeInfo();
    });
    
    function queryLinkTypeInfo(){
    	var linktype = $("#linktype").val();
    	var suorce = "source";
    	linkTypeInfo(linktype,suorce);
    }
    
    function addLinkTypeInfo(){
    	var linktype = $("#add_type").val();
    	var source = "add_source";
    	linkTypeInfo(linktype,source);
    }
    function linkTypeInfo(linktype,suorce){
    	if(linktype=='1'){
    		queryHostInfos(suorce);
    	}else if(linktype=='2'){
    		queryDeviceInfos(suorce);
    	}
    }
    function queryHostInfos(suorce){
    	$.ajax({
            type: "post",
            url: "/view/class/system/network/query/hostinfos?random=" + Math.random(),
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
            	$("#"+suorce).empty();
        		for(var i=0;i<result.length;i++){
        			$("#"+suorce).append("<option value=\""+result[i].hostid+"\">"+result[i].hostname+"</option>");
        		}
            }
         });
    }
    
    function queryDeviceInfos(suorce){
    	$.ajax({
            type: "post",
            url: "/view/class/system/network/query/deviceinfos?random=" + Math.random(),
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
            	$("#"+suorce).empty();
        		for(var i=0;i<result.length;i++){
        			$("#"+suorce).append("<option value=\""+result[i].device_id+"\">"+result[i].device_name+"</option>");
        		}
            }
         });
    }
    
    function querySourceTypeInfos(sourcetype){
    	$.ajax({
            type: "post",
            url: "/view/class/system/network/query/sourcetypeinfos?random=" + Math.random(),
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
            	$("#"+sourcetype).empty();
        		for(var i=0;i<result.length;i++){
        			$("#"+sourcetype).append("<option value=\""+result[i].code+"\">"+result[i].description+"</option>");
        		}
            }
         });
    }
    
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
        
        var linktype = $("#linktype").val();
        var source = $("#source").val();
        var data = {'linktype':linktype,'source':source};
        $.getJSON("/view/class/system/network/query/infolist?random=" + Math.random(),data, function(result) {
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
            rowdata = rowdata + "<tr><td>"+rowninfo.sourcename+"</td><td>"
            +isNull(rowninfo.port)+"</td><td>"+rowninfo.targetname+"</td><td>"
            +isNull(rowninfo.to_port)+"</td><td>"+isNull(rowninfo.description)+"</td>"
            +"<td>"
            +"<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""+rowninfo.id+"\" >修改</a>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""+rowninfo.id+"\">删除</a>"
            +"</td></tr>";
        }
        $("#hostdiv").empty().append(rowdata);
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
    //新增按钮事件
    function addShow(){
    	querySourceTypeInfos("add_type");
    	var linktype = $("#add_type").val();
    	var source = "add_source";
    	linkTypeInfo(linktype,source);
    	var linktype2 = '2';
    	var source2 = "add_target";
    	linkTypeInfo(linktype2,source2);
		reset("add_div");
		showLayer("add_div",'新增网络关系');
    }
    
    function addInfo(){
    	var type = $("#add_type").val();
    	var source = $("#add_source").val();
    	var port = $("#add_port").val();
    	var target = $("#add_target").val();
    	var to_port = $("#add_to_port").val();
    	var description = $("#add_description").val();
    	if(validType('add',type)|| validSource('add',source) || validPort('add',port) ||validTarget('add',target) ||
    			validToPort('add',to_port) ){
            return;
        }
    	var data = {type:type,source:source,port:port,target:target,to_port:to_port,description:description};
    	loadingwait();
    	$.getJSON("/view/class/system/network/mamage/add/?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }
    
    function validType(type,value){
        if(null == value || value==""){
            $("#"+type+"_type").focus();
            layer.tips('类型不能为空!', '#'+type+'_type',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    function validSource(type,value){
        if(null == value || value==""){
            $("#"+type+"_source").focus();
            layer.tips('本机名称不能为空!', '#'+type+'_source',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    function validPort(type,value){
        if(null == value || value==""){
            $("#"+type+"_port").focus();
            layer.tips('本机端口不能为空!', '#'+type+'_port',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    function validTarget(type,value){
        if(null == value || value==""){
            $("#"+type+"_target").focus();
            layer.tips('对端设备不能为空!', '#'+type+'_target',{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }
    function validToPort(type,value){
        if(null == value || value==""){
            $("#"+type+"_to_port").focus();
            layer.tips('对端端口不能为空!', '#'+type+'_to_port',{ tips: [2, '#EE1A23'] });
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
            var data = {id:id};
            var url = "/view/class/system/network/mamage/delete/?random=" + Math.random();
            $.getJSON(url,data,function(result){
                layerResultAndReload(result);
            })
        });
    }
    
    
    //修改按钮事件
    function modifyShow(id){
        reset("modify_div");
        var data = {id:id};
        //查询网络关系信息
        loadingwait();
        $.getJSON("/view/class/system/network/querySingleinfo?random=" + Math.random(),data,function(result){
        	$("#modify_id").val(id);
        	$("#modify_port").val(result.port);
        	$("#modify_to_port").val(result.to_port);
        	$("#modify_description").val(result.description);
        	$("#modify_type").val(result.type);
        	var linktype = '1';
        	var source = "modify_source";
        	querySourceTypeInfos("modify_type");
        	$("#modify_type option").each(function(){
        		if($(this).attr('value')==result.type){
        			$(this).prop("selected", "true");
        		}
        	});
        	
        	linkTypeInfo(linktype,source);
        	var linktype2 = '2';
        	var source2 = "modify_target";
        	linkTypeInfo(linktype2,source2);
        	
        	$("#modify_source option").each(function(){
        		if($(this).attr('value')==result.source){
        			$(this).prop("selected", "true");
        		}
        	});
        	
            $("#modify_target option").each(function(){
        		if($(this).attr('value')==result.target){
        			$(this).prop("selected", "true");
        		}
        	});
            showLayer("modify_div",'修改网络关系');
        });
    }
    
    //修改确认
    function modifyInfo(){
    	var id = $("#modify_id").val();
    	var type = $("#modify_type").val();
    	var source = $("#modify_source").val();
    	var port = $("#modify_port").val();
    	var target = $("#modify_target").val();
    	var to_port = $("#modify_to_port").val();
    	var description = $("#modify_description").val();
    	if(validType('modify',type)|| validSource('modify',source) || validPort('modify',port) ||validTarget('modify',target) ||
    			validToPort('modify',to_port) ){
            return;
        }
    	var data = {id:id,type:type,source:source,port:port,target:target,to_port:to_port,description:description};
    	loadingwait();
    	$.getJSON("/view/class/system/network/mamage/modify/?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }
    
});