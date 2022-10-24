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
        
        resizewh.resizeBodyH($("#mainhost"));
        butBindFunction();
        $("#checkboxAll").click(function() {
        	var flag = $(this).prop('checked');
        	checkbox.checkboxAll(flag,'checkboxAll','hostdiv');
        });
        reset("mainhost");
        initChildrenMenu();
        loadingwait();
        loadOptRecord();
        
//----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainhost");
        });
        $("#add_ok").click(function() {
            addInfo();
        });
        $("#add_cancle").click(function() {
            layer.closeAll();
        });
        $("#detail_ok").click(function() {
            layer.closeAll();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','hostdiv');
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
 					}else if(result[i].url=='download'){
 						$("#"+result[i].name).click(function() {
 							location.href ="/view/class/mainttool/autodeploy/downloadMould?random=" + Math.random();
 				        });
 					}else if(result[i].url=='upload'){
 						$("#operate_menu").append("<form id = 'uploadFileForm' style='display:none' enctype='multipart/form-data'> <input id = 'uploadFileInput' type='file' name='file'> </form>");
 						$("#"+result[i].name).click(function() {
 							$("#uploadFileInput").trigger('click');
 							$("#uploadFileInput").change(function(){
 						    	uploadMould();
 						    	$('#uploadFileInput').replaceWith("<input id = 'uploadFileInput' type='file' name='file'>");
 						    });
 				        });
 					}else if(result[i].url=='log'){
 						$("#"+result[i].name).click(function() {
 						  // 发送请求
 				        	$.ajax({
 				        		url: "/view/class/mainttool/autodeploy/ansibleLogRefresh",
 				        		type:'post',
 				        		processData:false,  
 				        		contentType:false,  
 				        		success:function(result){
 				        			logShowLayer("log_div","自动部署实时日志");
 				        		}
 				        	})
 				        });
 					}
     			}
     		}
     	});
    }
    
    function uploadMould() {
    	// 构造formData
    	var formData = new FormData($("#uploadFileForm")[0]);
    	loadingwait();
    	// 发送请求
    	$.ajax({
    		url: "/view/class/mainttool/autodeploy/uploadMould",
    		type:'post',
    		data:formData,
    		processData:false,  
    		contentType:false,  
    		success:function(result){
    			layer.closeAll();
    			addLayerResultAndReload(result);
    		}
    	})
    }
    
    var add_auto_id_no = 1;
    var add_auto_no = 1;
    //新增按钮事件
	function addShow(){
		reset("add_div");
		for ( var i = 1; i <=add_auto_id_no; i++){
			$("#add_"+i).remove();
		}
	    add_auto_id_no = 1;
	    add_auto_no = 1;
	    var add_div = "<div id='add_"+add_auto_id_no+"' class='auto_pop clearfix'> <div class='row  fl'> <label class='control-label'>主机IP</label> <div class='controls'> <input id='add_addr_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>主机名称</label> <div class='controls' > <input id='add_hostname_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>所属节点</label> <div class='controls'> <input id='add_nodename_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>SSH用户名</label> <div class='controls' > <input id='add_ssh_user_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>SSH密码</label> <div class='controls'> <input id='add_ssh_password_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>SSH端口</label> <div class='controls' > <input id='add_ssh_port_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div id='auto_del_"+add_auto_id_no+"' class='auto_del'><i class='iconfont icon-cha'></i></div> </div>";
		$("#add_confirm").before(add_div);
		$(".auto_del").click(function() {
			if(add_auto_no > 1){
				$(this).parent().remove();
				add_auto_no--;
			}
	    });
		showLayer("add_div",'新增自动部署服务器信息');
    }
    
	$("#add_button").click(function() {
		var addinfolist = new Array(); 
		for (var i=1;i<=add_auto_id_no;i++){
    		if($("#add_"+i).length>0){
    			var add_addr = stringutil.Trim($("#add_addr_"+i).val());
    			var add_hostname = stringutil.Trim($("#add_hostname_"+i).val());
    	        var add_nodename = stringutil.Trim($("#add_nodename_"+i).val());
    	        var add_ssh_user = stringutil.Trim($("#add_ssh_user_"+i).val());
    			var add_ssh_password = stringutil.Trim($("#add_ssh_password_"+i).val());
    	        var add_ssh_port = stringutil.Trim($("#add_ssh_port_"+i).val());
    	        if (!checkIp(add_addr)) {
    	        	$("#add_addr_"+i).focus();
    	            layer.tips('请输入正确格式的主机IP!', '#add_addr_'+i,{ tips: [2, '#EE1A23'] });
    	            return;
    	        }
    	        if(stringutil.checkString("add_hostname_"+i,add_hostname,"主机名称不能为空!")|| 
                		stringutil.checkString("add_hostname_"+i,add_hostname,"主机名称不能超过100位!",100) ||
                		stringutil.checkString("add_nodename_"+i,add_nodename,"所属节点不能为空!")|| 
                		stringutil.checkString("add_nodename_"+i,add_nodename,"所属节点不能超过100位!",100) ||
                		stringutil.checkString("add_ssh_user_"+i,add_ssh_user,"SSH用户名不能为空!")|| 
                		stringutil.checkString("add_ssh_user_"+i,add_ssh_user,"SSH用户名不能超过100位!",100) ||
                		stringutil.checkString("add_ssh_password_"+i,add_ssh_password,"SSH密码不能为空!")|| 
                		stringutil.checkString("add_ssh_password_"+i,add_ssh_password,"SSH密码不能超过100位!",100)
                		){
                    return;
                }
    	        if (!isPort(add_ssh_port)) {
    	        	$("#add_ssh_port_"+i).focus();
    	            layer.tips('请输入正确格式的SSH端口!', '#add_ssh_port_'+i,{ tips: [2, '#EE1A23'] });
    	            return;
    	        }
    	        var map = new Map();
    	        map.clear();
    	        map.put("add_addr", add_addr);
    	        map.put("add_hostname", add_hostname);
    	        map.put("add_nodename", add_nodename);
    	        map.put("add_ssh_user", add_ssh_user);
    	        map.put("add_ssh_password", add_ssh_password);
    	        map.put("add_ssh_port", add_ssh_port);
    	        for ( var j = 1; j <=addinfolist.length; j++){
    	            if(addinfolist[j-1][0] == add_addr && i!=j){
    	        		$("#add_addr_"+i).focus();
        	            layer.tips('请输入不重复的主机IP!', '#add_addr_'+i,{ tips: [2, '#EE1A23'] });
        	            return;
    	        	}
    	        }
    	        addinfolist.push(map.values());
        	}
    	}
		var add_div_before = "#add_"+add_auto_id_no;
		add_auto_id_no++;
		add_auto_no++;
		var add_div = "<div id='add_"+add_auto_id_no+"' class='auto_pop clearfix'> <div class='row  fl'> <label class='control-label'>主机IP</label> <div class='controls'> <input id='add_addr_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>主机名称</label> <div class='controls' > <input id='add_hostname_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>所属节点</label> <div class='controls'> <input id='add_nodename_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>SSH用户名</label> <div class='controls' > <input id='add_ssh_user_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>SSH密码</label> <div class='controls'> <input id='add_ssh_password_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div class='row  fl'> <label class='control-label'>SSH端口</label> <div class='controls' > <input id='add_ssh_port_"+add_auto_id_no+"' size='16' class='input-medium' type='text'> </div> </div> <div id='auto_del_"+add_auto_id_no+"' class='auto_del'><i class='iconfont icon-cha'></i></div> </div>";
		$("#add_confirm").before(add_div);
		$(".auto_del").click(function() {
			if(add_auto_no > 1){
				$(this).parent().remove();
				add_auto_no--;
			}
	    });
    });
	
	$(".auto_del").click(function() {
		if(add_auto_no > 1){
			$(this).parent().remove();
			add_auto_no--;
		}
    });
	
    //新增确认
    function addInfo(){
    	var addinfolist = new Array(); 
    	for (var i=1;i<=add_auto_id_no;i++){
    		if($("#add_"+i).length>0){
    			var add_addr = stringutil.Trim($("#add_addr_"+i).val());
    			var add_hostname = stringutil.Trim($("#add_hostname_"+i).val());
    	        var add_nodename = stringutil.Trim($("#add_nodename_"+i).val());
    	        var add_ssh_user = stringutil.Trim($("#add_ssh_user_"+i).val());
    			var add_ssh_password = stringutil.Trim($("#add_ssh_password_"+i).val());
    	        var add_ssh_port = stringutil.Trim($("#add_ssh_port_"+i).val());
    	        if (!checkIp(add_addr)) {
    	        	$("#add_addr_"+i).focus();
    	            layer.tips('请输入正确格式的主机IP!', '#add_addr_'+i,{ tips: [2, '#EE1A23'] });
    	            return;
    	        }
    	        if(stringutil.checkString("add_hostname_"+i,add_hostname,"主机名称不能为空!")|| 
                		stringutil.checkString("add_hostname_"+i,add_hostname,"主机名称不能超过100位!",100) ||
                		stringutil.checkString("add_nodename_"+i,add_nodename,"所属节点不能为空!")|| 
                		stringutil.checkString("add_nodename_"+i,add_nodename,"所属节点不能超过100位!",100) ||
                		stringutil.checkString("add_ssh_user_"+i,add_ssh_user,"SSH用户名不能为空!")|| 
                		stringutil.checkString("add_ssh_user_"+i,add_ssh_user,"SSH用户名不能超过100位!",100) ||
                		stringutil.checkString("add_ssh_password_"+i,add_ssh_password,"SSH密码不能为空!")|| 
                		stringutil.checkString("add_ssh_password_"+i,add_ssh_password,"SSH密码不能超过100位!",100)
                		){
                    return;
                }
    	        if (!isPort(add_ssh_port)) {
    	        	$("#add_ssh_port_"+i).focus();
    	            layer.tips('请输入正确格式的SSH端口!', '#add_ssh_port_'+i,{ tips: [2, '#EE1A23'] });
    	            return;
    	        }
    	        var map = new Map();
    	        map.clear();
    	        map.put("add_addr", add_addr);
    	        map.put("add_hostname", add_hostname);
    	        map.put("add_nodename", add_nodename);
    	        map.put("add_ssh_user", add_ssh_user);
    	        map.put("add_ssh_password", add_ssh_password);
    	        map.put("add_ssh_port", add_ssh_port);
    	        for ( var j = 1; j <=addinfolist.length; j++){
    	            if(addinfolist[j-1][0] == add_addr && i!=j){
    	        		$("#add_addr_"+i).focus();
        	            layer.tips('请输入不重复的主机IP!', '#add_addr_'+i,{ tips: [2, '#EE1A23'] });
        	            return;
    	        	}
    	        }
    	        addinfolist.push(map.values());
        	}
    	}
    	loadingwait();
    	var addinfojson=[];
    	addinfolist.forEach(function(item){
    	    var temp={};
    	    item.forEach(function(value,index){
    	        temp[index]=value;
    	    });
    	    addinfojson.push(temp);
    	})
        var data = {addinfojson:addinfojson,addinfojsonlength:addinfojson.length};
		$.getJSON("/view/class/mainttool/autodeploy/add/?random=" + Math.random(),data, function(result) {
			layer.closeAll();
			addLayerResultAndReload(result);
		});
    }
    
    
    
    function isNull(data){
    	if(data==null || data ==''){
    		return '';
    	}else
    		return data;
    }
    
    function bindCheckBox(){
    	$("#hostdiv :checkbox").click(function(){  
        	var flag = $(this).prop('checked');
        	var value = $(this).val();
        	checkbox.checkboxSingle(flag,value);
        });
    }

    //查询按钮事件
    function queryOpt(){
        var addr = $("#addr").val();
        if(addr != null && addr != ""){
        	var addrress = stringutil.Trim(addr);
            if (!checkIp(addrress) && addrress != "") {
                layer.tips('请输入正确格式的主机IP!', '#addr',{ tips: [2, '#EE1A23'] });
                $("#addr").focus();
                return false;
            }
        }
        loadingwait();
        loadOptRecord();
    }
    //加载查询内容
    function loadOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();
        
        var hostname = stringutil.Trim($("#hostname").val());
        var addr = stringutil.Trim($("#addr").val());
        var hosttype = stringutil.Trim($("#hosttype").val());
        var nodeid = stringutil.Trim($("#nodeid").val());
        var data = {'hostname':hostname,'addr':addr,'hosttype':hosttype,'nodeid':nodeid};
        var host_detail_show = $("#host_detail_show").val();
        $.getJSON("/view/class/system/servermanage/query/infolist?random=" + Math.random(),data, function(result) {
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
                    showTableDetail(result,startnum,endnum);
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total == 0) {
                    	$("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#mainhost"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }

    //拼接tr
    function showTableDetail(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.hostid;
            var checked = "";
            if(checkbox.isExitArray(id)){
            	checked = "checked=\"checked\"";
            	k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.hostid+"\" id=\""+rowninfo.hostid+"\" "+checked+" />"
            + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.addr+"\">" 
            + rowninfo.addr + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.hostname+"\">"
            + rowninfo.hostname + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.nodeidname)+"\">"+ stringutil.isNull(rowninfo.nodeidname) 
            + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.ssh_user)+"\">"+ stringutil.isNull(rowninfo.ssh_user) 
            + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.ssh_port)+"\">"+ stringutil.isNull(rowninfo.ssh_port) 
            + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.deploy_status_name)+"\">"+ stringutil.isNull(rowninfo.deploy_status_name) 
            + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.deploy_des)+"\">"+ stringutil.isNull(rowninfo.deploy_des) 
            + "</td><td>"
            + "<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.hostid+"\">详情</a>"
            + "</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
        	//全选框选中
        	$("#checkboxAll").prop("checked", true);
        }else{
        	$("#checkboxAll").prop("checked", false);
        }
        $("#hostdiv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("hostdiv");
    }
    
     //详情按钮事件
    function detailShow(hostid){
        reset("detail_div");
        var data = {hostid : hostid};
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/servermanage/queryDetail?random=" + Math.random(),
            data: data,
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
            	if(result.length>0){
                    var host = result[0];
                    $("#detail_hostid").val(hostid); 
                    $("#detail_hostname").val(host.hostname);
                    $("#detail_addr").val(host.addr);
                    $("#detail_productname").val(host.productname);
                	$("#detail_nodeid").val(host.nodeidname);
                	$("#detail_hosttype").val(host.hosttypename);
                    $("#detail_host_room").val(host.host_room);
                    $("#detail_rack_num").val(host.rack_num);
                    $("#detail_os").val(host.os);
                    $("#detail_cpu").val(host.cpu);
                    $("#detail_memory").val(host.memory);
                    $("#detail_diskspace").val(host.diskspace);
                    $("#detail_ipv4").val(host.ipv4);
                    $("#detail_ipv6").val(host.ipv6);
                    $("#detail_location").val(host.location);
                    $("#detail_serialnumber").val(host.serialnumber);
                    $("#detail_ssh_user").val(host.ssh_user);
                    $("#detail_ssh_port").val(host.ssh_port);
                    $("#detail_deploy_status_name").val(host.deploy_status_name);
                    $("#detail_deploy_des").val(host.deploy_des);
                    showLayerDetail("detail_div",'服务器主机详情');
                }
            }
            
         });
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
        	// 发送请求
        	$.ajax({
        		url: "/view/class/mainttool/autodeploy/startUpAnsible",
        		type:'post',
        		processData:false,  
        		contentType:false,  
        		success:function(result){
        			logShowLayer("log_div","自动部署实时日志");
        		}
        	})
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
            area : [ '620px', '560px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            shadeClose: true,
            area : [ '730px', '420px' ],
            content : $("#"+divid)
        });
    }
    
    function logShowLayer(divid,title){
    	document.getElementById('msg').innerHTML = '';
    	start();
        layer.open({
        type: 1,
        title: title,
        shadeClose: true,
        shade: 0.8,
        area: ['70%', '70%'],
        skin: 'auto_log_div',
        content: $("#"+divid)
       });
    }
    
    //返回checkbox的选中的id的数组
    function getCheckboxArray(groupCheckbox){
    	var checkboxArray=new Array();
        for(i=0;i<groupCheckbox.length;i++){
            if(groupCheckbox[i].checked){
                var id =groupCheckbox[i].id;
                checkboxArray.push(id);
            }
        }
        return checkboxArray;
    }
    
    //校验IP
    function checkIp(ip) {
        var regex1 = /^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
        var regex2 = '0.0.0.0';
        return regex1.test(ip) && regex2 != ip;
    }
    
    /*端口号校验*/
    function isPort(str)
    {
        var parten=/^(\d)+$/g;
        if(parten.test(str)&&parseInt(str)<=65535&&parseInt(str)>=0){
            return true;
         }else{
            return false;
         }
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
    
    function Map() {
        this.elements = new Array();

        // 获取MAP元素个数
        this.size = function() {
            return this.elements.length;
        }

        // 判断MAP是否为空
        this.isEmpty = function() {
            return (this.elements.length < 1);
        }

        // 删除MAP所有元素
        this.clear = function() {
            this.elements = new Array();
        }

        // 向MAP中增加唯一元素（key, value)
        this.put = function(_key, _value) {
            this.remove(_key);
            this.elements.push({
                        key : _key,
                        value : _value
                    });
        }

        // 向MAP中增加重复元素（key, value)
        this.putRepeat = function(_key, _value) {
            this.elements.push({
                        key : _key,
                        value : _value
                    });
        }

        // 删除指定KEY的元素，成功返回True，失败返回False
        this.remove = function(_key) {
            var bln = false;
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].key == _key) {
                        this.elements.splice(i, 1);
                        return true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        }

        // 获取指定KEY的元素值VALUE，失败返回NULL
        this.get = function(_key) {
            try {
                var result = null;
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].key == _key) {
                        result = this.elements[i].value;
                    }
                }
                return result;
            } catch (e) {
                return null;
            }
        }

        // 设置MAP中指定KEY元素的值VALUE, 失败返回NULL
        this.set = function(_key, _value) {
            try {
                this.remove(_key);
                this.put(_key, _value);
            } catch (e) {
                return null;
            }
        }

        // 获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
        this.element = function(_index) {
            if (_index < 0 || _index >= this.elements.length) {
                return null;
            }
            return this.elements[_index];
        }

        // 判断MAP中是否含有指定KEY的元素
        this.containsKey = function(_key) {
            var bln = false;
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].key == _key) {
                        bln = true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        }

        // 判断MAP中是否含有指定VALUE的元素
        this.containsValue = function(_value) {
            var bln = false;
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].value == _value) {
                        bln = true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        }

        // 获取MAP中所有VALUE的数组（ARRAY）
        this.values = function() {
            var arr = new Array();
            for (i = 0; i < this.elements.length; i++) {
                arr.push(this.elements[i].value);
            }
            return arr;
        }

        // 获取MAP中所有KEY的数组（ARRAY）
        this.keys = function() {
            var arr = new Array();
            for (i = 0; i < this.elements.length; i++) {
                arr.push(this.elements[i].key);
            }
            return arr;
        }
    }
    
    
    var timerId = 1; // 模拟计时器id，唯一性
    var timerObj = {}; // 计时器存储器
    function getData() {
    	setTimeout(function(){
    		// 发送请求
        	$.ajax({
        		url: "/view/class/mainttool/autodeploy/getAnsibleLog",
        		type:'post',
        		processData:false,  
        		contentType:false,  
        		success:function(result){
        			for(var i=0;i<result.length;i++){
        				var logTime = result[i].logTime;
        				var logLine = result[i].logLine;
        				document.getElementById('msg').innerHTML += "<span style='color:#98a712' >" + logTime +"</span>	" +  logLine + "<br/>";
        				if(logLine.indexOf("自动部署结束") != -1 ){
        					stop();
        					loadingwait();
        					loadOptRecord();
            			}
        			}
        			if(result.length>0){
            	        var msg_end = document.getElementById('msg_end');
            	        msg_end.scrollIntoView();
        			}
        		}
        	})
        }, 500);
    }
    // 轮询
    function start () {
      var id = timerId++;
      timerObj[id] = true;
      async function timerFn () {
        if (!timerObj[id]) return
        getData();
        setTimeout(timerFn, 3000);
      }
      timerFn();
    }
    // 暂停
    function stop () {
      timerObj = {};
    }
    
    //删除按钮事件
    function deleteShow() {
    	var checkboxArray = checkbox.getReturnArray();
    	if (checkboxArray.length == 0) {
    		layer.msg("没有选择主机，请选择一个主机!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	} else {
    		layer.confirm('是否确认删除选择的主机数据？', {
	            closeBtn:0,
	            title: '询问',
	            btn: ['确认','取消']
	        },function(){
	            layer.closeAll();
	            loadingwait();
	            var data = {hostidArray:checkboxArray};
	            var url = "/view/class/mainttool/autodeploy/delete/?random=" + Math.random();
	            $.getJSON(url,data,function(result){
	            	if(result.opSucc){
	            		layer.close(layer_load);
	            		logShowLayer("log_div","自动部署实时日志");
	                }else{
	                    layer.msg(result.message,{
	                        time:2000,
	                        skin: 'layer_msg_color_error'
	                    });
	                }
	            })
	        });
    	}
    }
});