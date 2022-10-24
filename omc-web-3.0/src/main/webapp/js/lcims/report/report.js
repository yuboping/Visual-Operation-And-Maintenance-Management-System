require.config({
	paths:{
		 'lcims' : "/js/lcims",
	        'resizewh' : "/js/lcims/resizewh/resizewh",
	        'jquery' : '/js/jquery/jquery.min',
	        'iscroll' : '/js/lcims/tool/iscroll',
	        'layer' : '/js/layer/layer',
	        "laypage" : "/js/lcims/tool/laypage"
	}
});

require(['jquery', 'layer', 'laypage', 'resizewh' ],function($, layer, laypage, resizewh){
	var layer_load;
	butBindFunction();
    loadingwait();
    loadOptRecord(); 
	resizewh.resizeWH($("#main"));
	 
////////////////////////////////////////////////////////////////////
	function butBindFunction(){
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
        $("#query_ok").click(function() {
            layer.closeAll();
        });
        $("#modify_ok").click(function() {
            modifyInfo();
        });
        $("#modify_cancle").click(function() {
            layer.closeAll();
        });
        $("#add_but_matchsql").click(function(){
        	matchSql("add");
        });
        $("#add_but_reset").click(function(){
        	resetmatch("add");
        });
        $("#modify_but_matchsql").click(function(){
        	matchSql("modify");
        });
        $("#modify_but_reset").click(function(){
        	resetmatch("modify");
        });
        $("#resetbutton").click(function(){
        	$("#reportname").val("");
        });
    }
	function resetmatch(type){
		$("#"+type+"_fieldinfo_div").empty();
    	$("#"+type+"_data_info_div input[type='text']").each(function(i,input){
    		$(input).removeAttr("disabled");
    	});
    	$("#"+type+"_data_info_div select").each(function(i,input){
    		$(input).removeAttr("disabled");
    	});
    	$("#"+type+"_data_info_div textarea").each(function(i,input){
    		$(input).removeAttr("disabled");
    	});
	}
	
	 // 新增按钮事件
    function addShow() {
        reset("add_div");
        $.getJSON("/view/class/system/reportmanage/query/datasourcelist?random="+ Math.random(),function(result){
        	//加载数据源
        	var tmp="";
    		$.each(result,function(i,info){
        		tmp = tmp + "<option name='datasource' value=\""+info.key+"\">"+info.value+"</option>";
        	});
    		$("#add_datasourceid").empty().append(tmp);
    		//弹出输入框
    		layer.open({
                type : 1,
                title : '新增报表信息',
                closeBtn : 0,
                btn: ['确认','取消'],
                btnAlign:'c',
                area : [ '750px', '480px' ],
                content : $("#add_div"),
                yes:function(){
                	addInfo();
                },
                btn2:function(){
                	layer.closeAll();
                }
            });
        });
    }
    
    // 新增确认
    function addInfo() {
        var datasourceid= $("#add_datasourceid").val();
        var report_sql_day = $("#add_report_sql_day").val();
        var report_sql_mon = $("#add_report_sql_mon").val();
        var report_name = $("#add_report_name").val();
        var report_timefield = $("#add_report_timefield").val();
//        var report_type = getReportType('add');
        var fieldinfo = getFieldInfo('add');
        if (validReportName('add', report_name)) {
            return;
        }
        if (validDataSource('add', datasourceid)) {
            return;
        }
        if (validReportSql('add', report_sql_day,report_sql_mon)) {
            return;
        }
//        if (validReportType('add', report_type)) {
//            return;
//        }
        if (validFieldInfo('add', fieldinfo)) {
            return;
        }
        loadingwait();
        var data = {
    		'datasourceid':datasourceid,
    		'fieldinfo':fieldinfo,
    		'report_sql_day':report_sql_day,
    		'report_sql_mon':report_sql_mon,
//    		'report_type':report_type,
    		'report_timefield':report_timefield,
    		'report_name':report_name
		};
    	$.getJSON("/view/class/system/reportmanage/manage/add?random="+ Math.random(),data,function(result){
    		 layer.closeAll();
             layerResultAndReload(result);
        });
    }
    
    function deleteShow(reportid) {
        var layershow = layer.confirm('是否删除该条数据？', {
            closeBtn : 0,
            title : '询问',
            btn : [ '确认', '取消' ]
        // 按钮
        }, function() {
            layer.closeAll();
            loadingwait();
            
            var data = { reportid : reportid };
            var url = "/view/class/system/reportmanage/manage/delete?random=" + Math.random();
            $.getJSON(url, data, function(result) {
                layer.close(layershow);
                layerResultAndReload(result);
            })
        });
    }
    
    function layerResultAndReload(result) {
        layer.close(layer_load);
        if (result.opSucc) {
            loadOptRecord();
            layer.msg(result.message, {
                time : 2000,
                skin : 'layer_msg_color_success'
            });
        } else {
            layer.msg(result.message, {
                time : 2000,
                skin : 'layer_msg_color_error'
            });
        }
    }
    
    function matchSql(type) {
    	var datasourceid= $("#"+type+"_datasourceid").val();
        var report_sql_day = $("#"+type+"_report_sql_day").val();
        var report_sql_mon = $("#"+type+"_report_sql_mon").val();
        var report_timefield = $("#"+type+"_report_timefield").val();
        var report_name = $("#"+type+"_report_name").val();
        if (validReportName(type, report_name)) {
            return;
        }
        if (validDataSource(type, datasourceid)) {
            return;
        }
        if (validReportSql(type, report_sql_day,report_sql_mon)) {
            return;
        }
        var data = {datasourceid:datasourceid,report_sql_day:report_sql_day,report_sql_mon:report_sql_mon,report_timefield:report_timefield};
        $.getJSON("/view/class/system/reportmanage/matchsql?random="+ Math.random(), data, function(result) {
                if(result.opSucc){
                	var fieldInfo = mkFieldInfo(result.data);
                	$("#"+type+"_fieldinfo_div").empty().append(fieldInfo);
                	
                	$("#"+type+"_data_info_div input[type='text']").each(function(i,input){
                		$(input).attr("disabled","true");
                	});
                	$("#"+type+"_data_info_div select").each(function(i,input){
                		$(input).attr("disabled","true");
                	});
                	$("#"+type+"_data_info_div textarea").each(function(i,input){
                		$(input).attr("disabled","true");
                	});
                }else{
                	layer.msg(result.message, {
                        time : 2000,
                        skin : 'layer_msg_color_error'
                    });
                }
        });
    }
    
    function mkFieldInfo(data){
    	var size = data.length;
    	var tmp = "";
    	for(var i=0;i<size;i++){
    		if(i%2==0){
    			tmp = tmp +"<div class=\"row row-fluid\">";
    			tmp = tmp + mkDivForFieldInfo(data[i]);
    		}else{
    			tmp = tmp + mkDivForFieldInfo(data[i]);
    			tmp = tmp +"</div>";
    		}
    	}
    	if(size%2 !=0){
    		tmp = tmp +"</div>";
    	}
    	return tmp;
    }
    
    function mkDivForFieldInfo(info){
    	return "<div class=\"span12\"><label class=\"control-label\">"+info+":</label><div class=\"controls\"><input  class=\"input-medium\" " +
		"field_name=\""+info+"\" name=\"fieldshowname\" type=\"text\"></div></div>";
    }

    // 修改按钮事件
    function modifyShow(reportid) {
        reset("modify_div");
        var data = { reportid : reportid};
        loadingwait();
        $.getJSON("/view/class/system/reportmanage/query/datasourcelist?random="+ Math.random(),data,function(result){
        	var tmp="";
    		$.each(result,function(i,info){
        		tmp = tmp + "<option name='datasource' value=\""+info.key+"\"";
        		if(info.checkflag){
        			tmp = tmp +"selected =\"selected\" ";
        		}
        		tmp = tmp + ">"+info.value+"</option>";
        	});
    		$("#modify_datasourceid").empty().append(tmp);
        });
        $.getJSON("/view/class/system/reportmanage/query/singleinfo?random=" + Math.random(), data, function(result) {
            if (result.length > 0) {
                var info = result[0];
                $("#modify_report_id").val(reportid);
                $("#modify_report_name").val(info.reportname);
                $("#modify_report_sql_day").val(info.reportsqlday);
                $("#modify_report_sql_mon").val(info.reportsqlmon);
                $("#modify_report_timefield").val(info.timefield);
                $("#modify_fieldinfo_div").empty();
              //弹出输入框
        		layer.open({
                    type : 1,
                    title : '修改报表信息',
                    closeBtn : 0,
                    btn: ['确认','取消'],
                    btnAlign:'c',
                    area : [ '750px', '480px' ],
                    content : $("#modify_div"),
                    yes:function(){
                    	modifyInfo();
                    },
                    btn2:function(){
                    	layer.closeAll();
                    }
                });
            }
        });
    }

    function modifyInfo() {
        var datasourceid= $("#modify_datasourceid").val();
        var report_id = $("#modify_report_id").val();
        var report_sql_day = $("#modify_report_sql_day").val();
        var report_sql_mon = $("#modify_report_sql_mon").val();
        var report_name = $("#modify_report_name").val();
        var report_timefield = $("#modify_report_timefield").val();
        var fieldinfo = getFieldInfo('modify');
        if (validReportName('modify', report_name)) {
            return;
        }
        if (validDataSource('modify', datasourceid)) {
            return;
        }
        if (validReportSql('modify', report_sql_day,report_sql_mon)) {
            return;
        }
        if (validFieldInfo('modify', fieldinfo)) {
            return;
        }
        loadingwait();
        var data = {
    		'report_id':report_id,
        	'datasourceid':datasourceid,
    		'fieldinfo':fieldinfo,
    		'report_sql_day':report_sql_day,
    		'report_sql_mon':report_sql_mon,
    		'report_timefield':report_timefield,
    		'report_name':report_name
		};
    	$.getJSON("/view/class/system/reportmanage/manage/modify?random="+ Math.random(),data,function(result){
    		 layer.closeAll();
             layerResultAndReload(result);
        });
    }
    
    // 查询按钮事件
    function queryOpt() {
        loadingwait();
        loadOptRecord();
    }
	
	function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
	
	// 加载查询内容
    function loadOptRecord() {
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 12;

        var reportname = $("#reportname").val();
        var data = {
            'reportname' : reportname
        };
        $.getJSON("/view/class/system/reportmanage/query/infolist?random=" + Math.random(), data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount = Math.ceil(total / page_count);
            $("#querynum").text(total);
            laypage({
                cont : 'pageinfo',
                skin : '#6AB0F4',
                pages : pagecount,
                curr : 1,
                skip : false, // 是否开启跳页
                jump : function(obj, first) { // 触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showTable(result, startnum, endnum);
                    $("#currnum").text(startnum + "-" + endnum);
                    resizewh.resizeWH($("#main"));
                },
                groups : page_count
            // 连续显示分页数
            });
        });
    }
    
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        for (var i = startnum; i <= endnum; i++) {
            var rowninfo = data[i - 1];
            rowdata = rowdata + "<tr><td>" + rowninfo.reportname + "</td><td>" + rowninfo.datasourcename + "</td><td>" + rowninfo.reporttype
        	+ "</td><td>" + rowninfo.reportsqlday+ "</td><td>"+ rowninfo.reportsqlmon + "</td><td>"+ "<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""
                + rowninfo.reportid + "\" >修改</a>"+ "<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""
                + rowninfo.reportid + "\">删除</a>" + "</td>";
        }
        $("#infodiv").empty().append(rowdata);
        $("[name=modify]").each(function() {
            $(this).on('click', function() {
                modifyShow($(this).attr('id'));
            });
        });
        $("[name=delete]").each(function() {
            $(this).on('click', function() {
                deleteShow($(this).attr('id'));
            });
        });
    }
    
    // 重置页面标签内容
    function reset(divid) {
        $("#" + divid + " input[type='text']").each(function() {
            $(this).val('');
        });
        $("#" + divid + " textarea").each(function() {
            $(this).val('');
        });
        $("#" + divid + " :disabled").each(function() {
            $(this).attr('disabled',false);
        });
        $("#" + divid + " #add_fieldinfo_div").empty();
        $("input[type='checkbox']").removeAttr("checked");
    }
    
    function validDataSource(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_datasourceid").focus();
            layer.tips('数据源不能为空!', '#' + type + '_datasourceid', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    function validReportName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_report_name").focus();
            layer.tips('报表名称不能为空!', '#' + type + '_report_name', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    function validReportSql(type, value1,value2) {
        if ((null == value1 || value1 == "")&&(null == value2 || value2 == "")) {
            $("#" + type + "_report_sql_day").focus();
            layer.tips('报表SQL不能为空!', '#' + type + '_report_sql_day', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function validRoleid(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_roleid").focus();
            layer.tips('角色不能为空!', '#' + type + '_roleid', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
//    function getReportType(type){
//    	var tmp="";
//    	$("input[name='" + type +"_report_type']:checked").each(function(i,info){
//    		if(i==0){
//    			tmp=$(info).val();
//    		}else{
//    			tmp=tmp+","+$(info).val();
//    		}
//    	});
//    	return tmp;
//    }
//    function validReportType(type, value) {
//    	var result=false;
//    	if(value==null ||value.length==0){
//    		layer.tips('报表周期不能为空!',$("#"+type+"_report_type_div"), {
//                tips : [ 2, '#EE1A23' ]
//            });
//    		return true;
//    	}
//        return result;
//    }
    
    function validFieldInfo(type, value) {
    	var result=false;
    	var fieldinfo = $("#" + type +"_fieldinfo_div input[name='fieldshowname']");
    	if(fieldinfo==null ||fieldinfo.length==0){
    		layer.tips('报表列名不能为空!',$("#"+type+"_fieldinfo_div"), {
                tips : [ 2, '#EE1A23' ]
            });
    		return true;
    	}
    	$.each(fieldinfo,function(i,info){
    		var tmp = $(info).val();
    		if (null == tmp || tmp == "") {
                $(info).focus();
                layer.tips('字段不能为空!',$(info), {
                    tips : [ 2, '#EE1A23' ]
                });
                result = true;
                return false;//跳出each循环，return true的话会继续each循环
            }
    	});
        return result;
    }
    
    function getFieldInfo(type){
    	var tmp="";
    	$("#" + type +"_fieldinfo_div input[name='fieldshowname']").each(function(i,info){
    		if(i==0){
    			tmp=$(info).attr("field_name")+":"+$(info).val();
    		}else{
    			tmp=tmp+","+$(info).attr("field_name")+":"+$(info).val();
    		}
    	});
    	return tmp;
    }
});