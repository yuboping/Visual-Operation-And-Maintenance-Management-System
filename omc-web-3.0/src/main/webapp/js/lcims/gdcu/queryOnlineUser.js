require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'laydate' : '/js/laydate/laydate',
        'layer':'/js/layer/layer',
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});
require(['jquery','layer','laypage','resizewh','laydate','stringutil'],
	function($,layer,laypage,resizewh,laydate,stringutil) {
	    resizewh.resizeBodyH($("#mainOnlineUser"));
	    $("#querybutton").click(function() {
	    	var querytype = $("#querytype").val();
	        var queryvalue = $("#queryvalue").val();
	    	if(querytype == "2"){
	    		if (!checkIp(queryvalue)) {
		            layer.tips('请输入正确格式的Brasip!', '#querytype',{ tips: [3, '#EE1A23'] });
		        } else {
		        	loadOptRecord();
		        }
	    	}else if(querytype == "1"){
	    		if (queryvalue == null || queryvalue == "") {
		            layer.tips('用户账号不能为空!', '#querytype',{ tips: [3, '#EE1A23'] });
		        } else {
		        	loadOptRecord();
		        }
	    	}
	    });
	    
	    $("#resetbutton").click(function() {
                reset("mainOnlineUser");
            });
//-----------------------------------------以下为自定义方法-------------------------------------------------//            
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
	    
	    function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
        }
	    
	    function loadOptRecord(){
	        var pagecount=0;
	        var page_count = 10;
	        var querytype = $("#querytype").val();
	        var queryvalue = $("#queryvalue").val();
	        loadingwait();
	        var data = {'account':queryvalue,'opernodeid':0};
	        var url = "/gdmainttool/queryUserNode?random=" + Math.random();
	        $.getJSON(url, data, function(result) {
                if(result.returncode == "0"){
                	data = {'querytype':querytype,'queryvalue':queryvalue};
        	        $.getJSON("/gdmainttool/queryOnlineUser?random=" + Math.random(),data, function(result){
        	        	layer.close(layer_load);
        	        	total = result.length;
        	            pagecount=Math.ceil(total/page_count);
        	            $("#querynum").text(total);
        	            laypage({
        	                cont: 'pageinfo', 
        	                skin: '#6AB0F4',
        	                pages: pagecount,
        	                curr: 1, 
        	                skip: true, //是否开启跳页
        	                jump: function(obj, first){ //触发分页后的回调
        	                	startnum = (obj.curr - 1) * page_count + 1;
        	                    endnum = obj.curr * page_count;
        	                    endnum = endnum > total ? total : endnum;
        	                    showTable(result,startnum,endnum);
        	                    $("#currnum").text( startnum + "-" + endnum);
        	                    resizewh.resizeBodyH($("#mainOnlineUser"));
        	                },
        	                groups: page_count //连续显示分页数
        	            });
        	        });
                }else {
                	layerResultAndReload(result);
                }
	        });
	        
	        
	    }
	    
	    function showTable(data){
	    	var rowdata = "";
	    	$("#queryOnlineUser").empty().append("");
            if(data==null||data.length==0){
            	rowdata = rowdata + "<tr><td colspan='10'>无在线用户</td></tr>";
            	$("#queryOnlineUser").empty().append(rowdata);
            	//解除事件
            	butUnbindFunction();
            }else{
            	for(var i=0;i<data.length;i++){
            		var rowninfo = data[i];
            		//'account':account,'nasip':nasip,'userip':userip,'sessionid':sessionid
            		var radio_val = rowninfo.account+"|"+rowninfo.nasip+"|"+rowninfo.userip+"|"+rowninfo.sessionid
                    rowdata = rowdata + "<tr><td style=\"white-space: normal;\"><input type=\"radio\" name=\"user_account\" value=\""+radio_val+"\" /></td>"
                    +"<td style=\"white-space: normal;\">"+rowninfo.account+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.starttime+"</td><td style=\"white-space: normal;\">"+rowninfo.lactiveTime+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.sessionTime+"</td><td style=\"white-space: normal;\">"+rowninfo.status+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.nasip+"</td><td style=\"white-space: normal;\">"+rowninfo.userip+"</td><td style=\"white-space: normal;\">"+rowninfo.mac+"</td><td style='word-break: break-all;'>"
                    +rowninfo.sessionid+"</td><td style=\"white-space: normal;\">" +rowninfo.nasPort+ "</td></tr>";
            	}
            	butBindFunction();
            	$("#queryOnlineUser").empty().append(rowdata);
            }
	    }
	    
	    function butUnbindFunction(){
	    	$("#batchbutton").click(function() {
	    		
	        });
	        $("#deletebutton").click(function() {
	        	
	        });
	    }
	    
	    function butBindFunction(result){
	        $("#batchbutton").click(function() {
	        	batchShow();
	        });
	        $("#deletebutton").click(function() {
	        	deleteShow();
	        });
	    }
	    
	    
	    function batchShow() {
	    	var radio_val= $('input:radio[name="user_account"]:checked').val();
	    	if(radio_val==null){
                alert("请选中一个!");
                return false;
            }else{
            	var params = radio_val.split('|');
            	var data = {'account':params[0],'nasip':params[1],'userip':params[2],'sessionid':params[3]};
            	var layershow = layer.confirm('是否确认下线该用户？', {
    	            closeBtn : 0,
    	            title : '询问',
    	            btn : [ '确认', '取消' ]
    	        }, function() {
    	            layer.closeAll();
    		        loadingwait();
    	    	    var url = "/gdmainttool/kickBRASUser?random=" + Math.random();
    	            $.getJSON(url, data, function(result) {
    	            	layer.close(layershow);
    	                layerResultAndReload(result);
    	            });
    	        });
            }
	    }
	    
	    function deleteShow() {
	        var layershow = layer.confirm('是否确认删除该用户？', {
	            closeBtn : 0,
	            title : '询问',
	            btn : [ '确认', '取消' ]
	        // 按钮
	        }, function() {
	            layer.closeAll();
	            var querytype = $("#querytype").val();
		    var queryvalue = $("#queryvalue").val();
		    loadingwait();
            	var data = {'querytype':querytype,'queryvalue':queryvalue};
	            var url = "/gdmainttool/kickLMUser?random=" + Math.random();
	            $.getJSON(url, data, function(result) {
	            	layer.close(layershow);
	                layerResultAndReload(result);
	            });
	        });
	    }
	    
	 
	    function checkIp(ip) {
	        var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	        return regex.test(ip);
	    }
	    
	    function layerResultAndReload(result){
            layer.close(layer_load);
            if(result.returncode == "0"){
            	loadOptRecord();
                layer.msg(result.message, {
                    time : 2000,
                    skin : 'layer_msg_color_success'
                });
            }else{
            	$("#queryOnlineUser").empty().append("");
            	layer.msg(result.message, {
                    time : 2000,
                    skin : 'layer_msg_color_error'
                });
            }
        }
	    
	    function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '500px', '395px' ],
                content : $("#"+divid)
            });
        }
});