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
			var metricidlist,cyclelist;
			var modetypelist=null;
            resizewh.resizeWH($("#mainalarmrule"));
            $("#add_alarmmode").hide();
            $("#modify_alarmmode").hide();
            initAlarmMode();
            var classtype = $('.tags').first().attr("id");
            $('.tags').first().addClass('tag_on').siblings().removeClass("tag_on");
            loadOptRecord(classtype);
            //$("#classtype_param").val("service");
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
            $('.tags').click(function(){
                $(this).addClass('tag_on').siblings().removeClass("tag_on");
                var classtype = $(this).attr("id");
                $("#classtype_param").val(classtype);
                loadOptRecord(classtype);
            });
            $("#add_moduleid").bind("change",function(){
            	$("#add_metricid").empty();
            	addMetricidChange();
            	$("#add_monitortarget1").val("");
        		$("#add_monitortarget_area").css("display","none");
        		$("#add_monitortarget2").css("display","none");
            });
            $("#add_monitortarget1").bind("change",function(){
            	addselectMetricid();
            });
            $("#add_monitortarget2").bind("change",function(){
            	selectSingleMetricid();
            });
            $("#add_monitortarget_area").bind("change",function(){
            	addselectBas();
            	selectSingleMetricid();
            });
            $("#add_metricid").bind("change",function(){
            	addMetricidChange();
            })
//----------------------------------以下为自定义方法-------------------------------------------------//
            
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
            
            //初始化告警方式、告警方式列表信息
             function initAlarmMode(){
            	var url = "/view/class/system/alarmmanage/getAlarmMode";
             	$.getJSON(url+"?random=" + Math.random(), function(result) {
             		if(result[0]!=null&&result[0].length>0){
             			modetypelist=result[0];
             			$("#add_alarmmode_controls").empty();
             			$("#modify_alarmmode_controls").empty();
             			var addselectid='';
             			var adddivstr='';
             			var modifyselectid='';
             			var modifydivstr='';
             			for(var i=0;i<result[0].length;i++){
             				addselectid="add_"+result[0][i].code;
             				modifyselectid="modify_"+result[0][i].code;
             				adddivstr='<span class="control-label" style="text-align: left;width: 60px;padding-top: 0px;font-style:normal;">'+result[0][i].description+'</span>'
             						+'<div class="controls" style="margin-left: 50px;">'
             						+'<select class="input-large" style="width: 150px;" id="'+addselectid+'">'+'<option value="">请选择</option>';
             				
             				modifydivstr='<span class="control-label" style="text-align: left;width: 60px;padding-top: 0px;font-style:normal;">'+result[0][i].description+'</span>'
     						+'<div class="controls" style="margin-left: 50px;">'
     						+'<select class="input-large" style="width: 150px;" id="'+modifyselectid+'">'+'<option value="">请选择</option>';
             				
             				
             				for(var j=0;j<result[1].length;j++){
             					if(result[0][i].code==result[1][j].modetype){
             						adddivstr=adddivstr+'<option value="'+result[1][j].modeid+'">'+result[1][j].modename+'</option>';
             						modifydivstr=modifydivstr+'<option value="'+result[1][j].modeid+'">'+result[1][j].modename+'</option>';
             					}
             				}
             				adddivstr=adddivstr+'</select>'+'</div>';
             				$("#add_alarmmode_controls").append(adddivstr);
             				$("#modify_alarmmode_controls").append(modifydivstr);
             			}
             			$("#add_alarmmode").show();
             			$("#modify_alarmmode").show();
             		}else{
             			$("#add_alarmmode").remove();
                        $("#modify_alarmmode").remove();
             		}
             	});
             }
            
            //新增按钮事件
            function addShow(){
                reset("add_div");
                var classtype = $("#classtype_param").val();
                var url = "/view/class/system/alarmmanage/queryModules?classtype="+classtype;
                $.getJSON(url+"&random=" + Math.random(), function(result) {
                    $("#add_moduleid").empty();
                    $.each(result,function(i,data){
                        $("#add_moduleid").append("<option value=\""+data.moduleid+"\">"+data.modulename+"</option>");
                    });
                });
                $("#add_monitortarget1").empty();
                $("#add_monitortarget2").css("display","none");
         		$("#add_monitortarget2").empty();
         		$("#add_monitortarget_area").empty();
         		$("#add_monitortarget_area").css("display","none");
         		$("#add_metricid").empty();
         		addMetricidChange();
                if(classtype.match('service')=='service'){
                	$("#add_monitortarget1").append("<option value=\"\">请选择</option>");
                	$("#add_monitortarget1").append("<option value=\"1\">概览</option>");
                	$("#add_monitortarget1").append("<option value=\"2\">全部节点</option>");
                	$("#add_monitortarget1").append("<option value=\"3\">单个节点</option>");
                	$("#add_monitortarget1").append("<option value=\"4\">全部主机</option>");
                	$("#add_monitortarget1").append("<option value=\"5\">单个主机</option>");
                }else if(classtype.match('netdevice')=='netdevice'){
                	$("#add_monitortarget1").append("<option value=\"\">请选择</option>");
                	$("#add_monitortarget1").append("<option value=\"1\">概览</option>");
                	$("#add_monitortarget1").append("<option value=\"2\">全部地市</option>");
                	$("#add_monitortarget1").append("<option value=\"3\">单个地市</option>");
                	$("#add_monitortarget1").append("<option value=\"4\">全部BAS</option>");
                	$("#add_monitortarget1").append("<option value=\"5\">单个BAS</option>");
                }else{
                	$("#add_monitortarget1").append("<option value=\"\">请选择</option>");
                	$("#add_monitortarget1").append("<option value=\"1\">概览</option>");
                	$("#add_monitortarget1").append("<option value=\"2\">全部地市</option>");
                	$("#add_monitortarget1").append("<option value=\"3\">单个地市</option>");
                }
                showLayer("add_div",'新增告警规则');
            }
            function addMetricidChange(){
            	var metricid= $("#add_metricid").val();
            	$("#add_metricid_attr").hide();
            	if(metricid==null || metricid==""){
            	}else{
            		$.ajax({
            			type:"POST",
            			data:{"metricid":metricid},
            			url:"/view/class/system/alarmmanage/getMetricAttrById",
            			dateType:"JSON",
            			success:function(result){
            				if(result!=null&&result!=""){
            					$("#add_metricid_attr").empty().show();
            					for(var i=0;i<result.length;i++){
                        			$("#add_metricid_attr").append("<option value=\""+result[i].code+"\">"+result[i].description+"</option>");
                        		}
            				}
            			},
            			error:function(a,b,c){
            				console.log(a);
            				console.log(b);
            				console.log(c);
            			}
            		});
            	}
            }
            function addselectMetricid(){
            	var monitortarget = $("#add_monitortarget1").val();
            	var classtype = $("#classtype_param").val();
            	var url = "/view/class/system/alarmmanage/"+classtype+"/"+$("#add_moduleid").val()+"/"+monitortarget;
            	$("#add_monitortarget2").empty();
            	$("#add_monitortarget_area").empty();
            	$("#add_monitortarget_area").css("display","none");
            	if(monitortarget==""){
            		$("#add_metricid").empty();
            		$("#add_monitortarget_area").css("display","none");
            		$("#add_monitortarget2").css("display","none");
            	}
            	$.ajax({
                    type: "post",
                    url: url+"?random=" + Math.random(),
                    cache: false,
                    async: false, 
                    dataType: "json",
                    success: function (result) {
                    	if(monitortarget==""){
                			$("#add_monitortarget_area").css("display","none");
                     		$("#add_monitortarget2").empty();
                     	}else if(monitortarget=="1"||monitortarget=="2"||monitortarget=="4"){
                     		$("#add_monitortarget2").css("display","none");
                     		$("#add_monitortarget2").empty();
                     	}else if(monitortarget=="3"){
                     		$("#add_monitortarget2").css("display","");
                     		if(classtype.match('service')=='service'){
                     			//单个节点
                     			for(var i=0;i<result.data[0].length;i++){
                        			$("#add_monitortarget2").append("<option value=\""+result.data[0][i].nodeid+"\">"+result.data[0][i].name+"</option>");
                        		}
                     		}else{
                     			//单个地市
                     			for(var i=0;i<result.data[0].length;i++){
                        			$("#add_monitortarget2").append("<option value=\""+result.data[0][i].areano+"\">"+result.data[0][i].name+"</option>");
                        		}
                     		}
                     	}else if(monitortarget=="5"){
                     		$("#add_monitortarget2").css("display","");
                     		if(classtype.match('service')=='service'){
                     			//单个主机
                     			for(var i=0;i<result.data[0].length;i++){
                        			$("#add_monitortarget2").append("<option value=\""+result.data[0][i].hostid+"\">"+result.data[0][i].hostname+"</option>");
                        		}
                     		}else{
                     			//单个BAS 14 
                     			$("#add_monitortarget_area").css("display","");
                     			for(var j=0;j<result.data[2].length;j++){
                        			$("#add_monitortarget_area").append("<option value=\""+result.data[2][j].areano+"\">"+result.data[2][j].name+"</option>");
                        		}
                     			for(var i=0;i<result.data[0].length;i++){
                        			$("#add_monitortarget2").append("<option value=\""+result.data[0][i].nasid+"\">"+result.data[0][i].nasip+"</option>");
                        		}
                     		}
                     	}
                		$("#add_metricid").empty();
                		if(null!=result.data[1]){
                			for(var i=0;i<result.data[1].length;i++){
                    			$("#add_metricid").append("<option value=\""+result.data[1][i].metricid+"\">"+result.data[1][i].description+"</option>");
                    		}
                			addMetricidChange();
                		}
                		metricidlist = result.data[3];
                		cyclelist = result.data[4];
                    }
                });
            }
            
            function selectSingleMetricid(){
            	var monitortarget = $("#add_monitortarget1").val();
            	var classtype = $("#classtype_param").val();
            	var monitorSingleTarget=$("#add_monitortarget2").val();
            	var url = "/view/class/system/alarmmanage/"+classtype+"/"+$("#add_moduleid").val()+"/"+monitortarget+"/"+monitorSingleTarget;
            	$("#add_metricid").empty();
            	$.ajax({
                    type: "post",
                    url: url+"?random=" + Math.random(),
                    cache: false,
                    async: false, 
                    dataType: "json",
                    success: function (result) {
                    	$("#add_metricid").empty();
                		if(null!=result.data){
                			for(var i=0;i<result.data[0].length;i++){
                    			$("#add_metricid").append("<option value=\""+result.data[0][i].metricid+"\">"+result.data[0][i].description+"</option>");
                    		}
                			addMetricidChange();
                		}
                		metricidlist = result.data[1];
                		cyclelist = result.data[2];
                    }
                });
            }
            
            function addselectBas(){
            	$("#add_monitortarget2").empty();
            	var url = "/view/class/system/alarmmanage/"+$("#add_moduleid").val()+"/"+$("#add_monitortarget_area").val();
            	$.getJSON(url+"?random=" + Math.random(), function(result) {
            		for(var j=0;j<result.length;j++){
            			$("#add_monitortarget2").append("<option value=\""+result[j].nasid+"\">"+result[j].nasip+"</option>");
            		}
            	});
            }
            
            //修改按钮事件
            function modifyShow(ruleid){
            	reset("modify_div");
            	$("#modify_ruleid").val(ruleid);
                var classtype = $("#classtype_param").val();
                var url = "/view/class/system/alarmmanage/queryAlarmRuleInfo?ruleid="+ruleid;
                $.ajax({
                    type: "post",
                    url: url+"&random=" + Math.random(),
                    cache: false,
                    async: false, 
                    dataType: "json",
                    success: function (result) {
	                	$("#modify_moduleid").val(result.modulename);
	                	$("#modify_monitortarget2").val(result.itemname);
	                	$("#modify_metricid").val(result.metricname);
	                	if(result.attributename!=null && result.attributename!=""){
	                		$("#modify_attribute").val(result.attributename);
	                		$("#modify_attribute").show();
	                	}else{
	                		$("#modify_attribute").val("").hide();
	                	}
	                	
	                	$("#modify_alarmexpr").val(result.alarmexpr);
	                	$("#modify_alarmmsg").val(result.alarmmsg);
	                	
	                	$("#modify_alarmlevel option").removeAttr("selected");
	                	$("#modify_alarmlevel option").each(function(){
	                		if($(this).attr('value')==result.alarmlevel){
	                			$(this).prop("selected", "true");
	                		}
	                	});
	                	var modes = result.modes;
	                	if(modes!=null&&modes!=""&&modetypelist!=null&&modetypelist!=""){
	                		var modelist = modes.split(',');
	                		for(var i=0;i<modetypelist.length;i++){
	                			$("#modify_"+modetypelist[i].code+" option").removeAttr("selected");
	                		}
	                		for(var i=0;i<modelist.length;i++){
	                			var mode = modelist[i];
	                			for(var j=0;j<modetypelist.length;j++){
		                			$("#modify_"+modetypelist[j].code+" option").each(function(){
		    	                		if($(this).attr('value')==mode){
		    	                			$(this).prop("selected", "true");
		    	                		}
		    	                	});
		                		}
	                		};
	                	}
                     }
                 });
                showLayer("modify_div",'修改告警规则');
            }
            
            //修改确认
            function modifyInfo(){
            	var alarmexpr = $("#modify_alarmexpr").val();
            	var alarmmsg = $("#modify_alarmmsg").val();
            	var alarmlevel = $("#modify_alarmlevel").val();
            	var ruleid = $("#modify_ruleid").val();
            	if(checkInfo('告警规则不能为空','modify','alarmexpr',alarmexpr))
            		return;
            	if(checkInfo('告警规则不能为空','modify','alarmexpr',alarmexpr))
            		return;
            	if(checkAlarmexpr('告警规则不合法','modify','alarmexpr',alarmexpr))
            		return;
            	if(checkInfo('告警信息不能为空','modify','alarmmsg',alarmmsg))
            		return;
            	var modes = getAlarmType('modify',modetypelist);
            	var data = {ruleid:ruleid,alarmexpr:alarmexpr,alarmlevel:alarmlevel,modes:modes,alarmmsg:alarmmsg};
            	loadingwait();
            	$.getJSON("/view/class/system/alarmmanage/modify/?random=" + Math.random(),data, function(result) {
//	               	 if(!result.opSucc){
//	               		 layer.alert(""+result.message);
//	               		 closewait();
//	               	 }else{
//	               		 layer.closeAll();
//	               		 layerMsgAndReloadSelect(result)
//	               	 }
	               	layer.closeAll();
	               	layerMsgAndReloadSelect(result);
                });
            }
            
            //新增确认
            function addInfo(){
            	var itemtype = $("#add_monitortarget1").val();
            	var metricid = $("#add_metricid").val();
            	var cycleid;
            	for(var i=0;i<metricidlist.length;i++){
            		if(metricid==metricidlist[i]){
            			cycleid=cyclelist[i];
            		}
            	}
            	var alarmlevel = $("#add_alarmlevel").val();
            	var monitortarget1 = $("#add_monitortarget1").val();
            	var alarmexpr = $("#add_alarmexpr").val();
            	var alarmmsg = $("#add_alarmmsg").val();
            	if(checkInfo('请选择监控目标','add','monitortarget1',monitortarget1))
            		return;
            	if(checkInfo('告警规则不能为空','add','alarmexpr',alarmexpr))
            		return;
            	if(checkAlarmexpr('告警规则不合法','add','alarmexpr',alarmexpr))
            		return;
            	if(checkInfo('告警信息不能为空','add','alarmmsg',alarmmsg))
            		return;
            	var modes = getAlarmType('add',modetypelist);
            	var classtype = $("#classtype_param").val();
            	var moduleid = $("#add_moduleid").val();
            	var item = $("#add_monitortarget2").val();
            	if(item==null||item==""){
            		item = "";
            	}
            	var metricidattr = $("#add_metricid_attr").val();
            	loadingwait();
            	var data = {classtype:classtype,moduleid:moduleid,item:item,itemtype:itemtype,metricid:metricid,alarmexpr:alarmexpr,alarmlevel:alarmlevel,modes:modes,alarmmsg:alarmmsg,cycleid:cycleid,metricidattr:metricidattr};
                 $.getJSON("/view/class/system/alarmmanage/add/?random=" + Math.random(),data, function(result) {
//                	 if(!result.opSucc){
//                		 closewait();
//                		 layer.alert(""+result.message);
//                	 }else{
//                		 layer.closeAll();
//                		 layerMsgAndReloadSelect(result);
//                	 }
                	 layer.closeAll();
            		 layerMsgAndReloadSelect(result);
                 });
            }
            
            function checkInfo(tips,type,id,value){
            	if(value==null||value==""){
            		layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
            		return true;
            	}
            	return false;
            }
            
            function checkAlarmexpr(tips,type,id,value){
            	var check = true;
            	value.indexOf('newest')
            	value.indexOf('avgindays')
            	if(value.indexOf('newest')!=-1||value.indexOf('avgindays')!=-1||value.indexOf('pre5min')!=-1||value.indexOf('yescurrent')!=-1||value.indexOf('allin12hour')!=-1||value.indexOf('collect')!=-1){
            		check = false;
            	}else{
            		layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
            		return check;
            	}
            	value = value.replace(/newest/g,1).replace(/avgindays/g,1).replace(/pre5min/g,1).replace(/yescurrent/g,1).replace(/allin12hour/g,1).replace(/collect/g,1);
            	value = $.trim(value);
            	value = value.replace(/\|\|/g," OR ").replace(/&&/g," AND ");
            	value = value.replace(/Math.abs/g,"1*")
            	var url = "/view/class/system/alarmmanage/checkalarmexpr";
            	
            	$.ajax({
                    type: "post",
                    url: url+"?alarmexpr="+value+"&random=" + Math.random(),
                    cache: false,
                    async: false, 
                    dataType: "json",
                    success: function (result) {
	                	if(!result.opSucc){
	             			//tips = result.message;
	                		layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
	                 		check = true;
	                 	}else{
	                 		check=false;
	                 	}
                     }
                 });
            	return check;
            }
            
            //拼接告警方式
            function getAlarmType(type,modetypelist){
            	var alartype="";
            	if(modetypelist!=null&&modetypelist.length>0){
            		for(var i=0;i<modetypelist.length;i++){
            			var alarmmode = $("#"+type+"_"+modetypelist[i].code).val();
            			if(alarmmode!=null && alarmmode!=''){
            				alartype = alartype + alarmmode + ",";
            			}
            		}
            		alartype = alartype.substring(0,alartype.length-1);
            	}
            	return alartype;
            }
            
            //加载查询内容
            function loadOptRecord(classtype){
                //分页显示的初始化数据
                var pagecount=0;
                var page_count = 8;
                var data = {'classtype':classtype};
                $.getJSON("/view/class/system/alarmmanage/query?random=" + Math.random(),data, function(result) {
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
                            resizewh.resizeWH($("#mainalarmrule"));
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
                    
                    rowdata = rowdata + "<tr><td>"+rowninfo.modulename+"</td><td>"
                    +rowninfo.metricname+"</td><td>"+rowninfo.itemname+"</td><td>"
                    +rowninfo.alarmexpr+"</td><td>"+rowninfo.alarmmsg+"</td>"+"<td>"
                    +"<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""+rowninfo.ruleid+"\" >修改</a>"
                    +"<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""+rowninfo.ruleid+"\">删除</a>"
                    +"</td>";
                }
                $("#alarmdiv").empty().append(rowdata);
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
            
            function deleteShow(ruleid) {
                var layershow = layer.confirm('是否确认删除该条数据？', {
                    closeBtn:0,
                    title: '询问',
                    btn: ['确认','取消'] //按钮
                },function(){
                    var data = {ruleid:ruleid};
                    var url = "/view/class/system/alarmmanage/delete/?random=" + Math.random();
                    $.getJSON(url,data,function(result){
                        layer.close(layershow);
                        layerMsgAndReloadSelect(result);
                    })
                });
            }
            
            function layerMsgAndReloadSelect(result){
                if(result.opSucc){
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
                loadOptRecord($("#classtype_param").val());
            }
            
            function showLayer(divid,title) {
                layer.closeAll();
                layer.open({
                    type : 1,
                    title : title,
                    closeBtn : 0,
                    area : [ '630px', '545px' ],
                    content : $("#"+divid)
                });
            }
            
            function loadingwait(){
                layer_load = layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                  });
            }
            function closewait(){
            	layer_load = layer.load(1, {
                    shade:0
                 });
            }
            //校验IP
            function checkIp(ip) {
                var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
                return regex.test(ip);
            }
});