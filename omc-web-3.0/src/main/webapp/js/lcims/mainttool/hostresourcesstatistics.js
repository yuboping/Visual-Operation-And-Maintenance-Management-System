require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        'laypage':'/js/lcims/tool/laypage',
        'laydate' : '/js/laydate/laydate',
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','laydate','stringutil'],
    function($,layer,laypage,resizewh,checkbox,laydate,stringutil) {
        var layer_load;
        
        resizewh.resizeBodyH($("#mainhostresourcesstatistics"));
        butBindFunction();
        laydate.render({
            elem: '#dayreportdate' //指定元素
            ,value: new Date()
        });
        laydate.render({
            elem: '#weekreportdate' //指定元素
            ,value: new Date()
        });
        laydate.render({
            elem: '#monthreportdate' //指定元素
            ,value: new Date()
            ,type: 'month'
            ,format: 'yyyyMM'
        });
        reset("mainhostresourcesstatistics");
        initChildrenMenu();
        loadingwait();
        loadDayReportOptRecord();
        
        
//----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#dayreport_querybutton").click(function() {
        	dayreportQueryOpt();
        });
        $("#dayreport_resetbutton").click(function() {
            reset("dayreport_div");
        });
        $("#weekreport_querybutton").click(function() {
        	weekreportQueryOpt();
        });
        $("#weekreport_resetbutton").click(function() {
            reset("weekreport_div");
        });
        $("#monthreport_querybutton").click(function() {
        	monthreportQueryOpt();
        });
        $("#monthreport_resetbutton").click(function() {
            reset("monthreport_div");
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
    
    function initChildrenMenu(){
    	var pageUrl=window.location.pathname;
 		$("#dayreport_operate_menu").empty();
 		$("#weekreport_operate_menu").empty();
 		$("#monthreport_operate_menu").empty();
 		$("#report_menu").empty();
    	var url = "/view/class/querychildrenmdmenu";
     	$.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
     		if(result!=null && result.length >0 ){
     			var tagon = true;
     			var reportButton = "";
     			for(var i=0;i<result.length;i++){
     			    // 日报表、周报表、月报表绑定事件
     				if(result[i].url=='dayreport'){
     					var tagonclass = "";
     					if(tagon){
     						tagon = false;
     						tagonclass = "-left tag_on";
     						reportButton = "dayreport";
     					}
         				$("#report_menu").append('<a href="#" id="'+result[i].name+'" class="tags border'+tagonclass+'">'+result[i].show_name+'</a> ');
    					$("#"+result[i].name).click(function() {
    						dayreport();
    					});
    				}
    				if(result[i].url=='weekreport'){
    					var tagonclass = "";
     					if(tagon){
     						tagon = false;
     						tagonclass = "-left tag_on";
     						reportButton = "weekreport";
     					}
             			$("#report_menu").append('<a href="#" id="'+result[i].name+'" class="tags border'+tagonclass+'">'+result[i].show_name+'</a> ');
        				$("#"+result[i].name).click(function() {
        					weekreport();
        				});
    				}
        			if(result[i].url=='monthreport'){
        				var tagonclass = "";
     					if(tagon){
     						tagon = false;
     						tagonclass = "-left tag_on";
     						reportButton = "monthreport";
     					}
                 		$("#report_menu").append('<a href="#" id="'+result[i].name+'" class="tags border'+tagonclass+'">'+result[i].show_name+'</a> ');
            			$("#"+result[i].name).click(function() {
            				monthreport();
            			  });
        			}
     				// 导出日、周、月绑定事件
         			if(result[i].url=='exportday'){
         				$("#dayreport_operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
    					$("#"+result[i].name).click(function() {
    						exportdayShow();
    			        });
    				}else if(result[i].url=='exportweek'){
    					$("#weekreport_operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> '); 
    					$("#"+result[i].name).click(function() {
    						exportweekShow();
    			        });
    				}else if(result[i].url=='exportmonth'){
    					$("#monthreport_operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
    					$("#"+result[i].name).click(function() {
    						exportmonthShow();
    			        });
    				}
     			}
     			switch(reportButton)
     			{
     			case "dayreport":
     				dayreport();
     			  break;
     			case "weekreport":
     				weekreport();
     			  break;
     			case "monthreport":
     				monthreport();
     			  break;
     		    }
     		}
     	});
    }
    
    //日报表按钮事件
    function dayreport(){
    	$("#hostresourcesstatistics_dayreport").addClass('tag_on')
    	$("#hostresourcesstatistics_weekreport").removeClass("tag_on");
    	$("#hostresourcesstatistics_monthreport").removeClass("tag_on");
    	$("#weekreport_div").hide();
    	$("#monthreport_div").hide();
        $("#dayreport_div").show();
    }
    
    //周报表按钮事件
    function weekreport(){
    	$("#hostresourcesstatistics_weekreport").addClass('tag_on')
    	$("#hostresourcesstatistics_dayreport").removeClass("tag_on");
    	$("#hostresourcesstatistics_monthreport").removeClass("tag_on");
    	$("#dayreport_div").hide();
    	$("#monthreport_div").hide();
        $("#weekreport_div").show();
    }
    
    //月报表按钮事件
    function monthreport(){
    	$("#hostresourcesstatistics_monthreport").addClass('tag_on')
    	$("#hostresourcesstatistics_dayreport").removeClass("tag_on");
    	$("#hostresourcesstatistics_weekreport").removeClass("tag_on");
    	$("#dayreport_div").hide();
    	$("#weekreport_div").hide();
        $("#monthreport_div").show();
    }
    
    //日报表查询按钮事件
    function dayreportQueryOpt(){
        loadingwait();
        loadDayReportOptRecord();
    }
    //加载日报表查询内容
    function loadDayReportOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        var dayreportaddr = stringutil.Trim($("#dayreportaddr").val());
        $("#dayreportaddr_page").val(dayreportaddr);
        var dayreportdate = stringutil.Trim($("#dayreportdate").val());
        $("#dayreportdate_page").val(dayreportdate);
        var data = {'addr':dayreportaddr,'date':dayreportdate};
        $.getJSON("/view/class/mainttool/hostresourcesstatistics/query/dayreport?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#dayreport_querynum").text(total);
            laypage({
                cont: 'dayreport_pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: false, //是否开启跳页
                jump: function(obj, first){ //触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showDayReportTable(result,startnum,endnum);
                    $("#dayreport_currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#dayreport_currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#dayreport_div"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接日报表tr
    function showDayReportTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.id;
            rowdata = rowdata + "<tr><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.STIME)+"\">"+stringutil.isNull(rowninfo.STIME)+"</td><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.CPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.CPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MEMORYMVALUE)+"</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        $("#dayreport_hostresourcesstatisticsdiv").empty().append(rowdata);
    }
    
    //周报表查询按钮事件
    function weekreportQueryOpt(){
        loadingwait();
        loadWeekReportOptRecord();
    }
    //加载周报表查询内容
    function loadWeekReportOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        
        var weekreportaddr = stringutil.Trim($("#weekreportaddr").val());
        $("#weekreportaddr_page").val(weekreportaddr);
        var weekreportdate = stringutil.Trim($("#weekreportdate").val());
        $("#weekreportdate_page").val(weekreportdate);
        var data = {'addr':weekreportaddr,'date':weekreportdate};
        $.getJSON("/view/class/mainttool/hostresourcesstatistics/query/weekreport?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#weekreport_querynum").text(total);
            laypage({
                cont: 'weekreport_pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: false, //是否开启跳页
                jump: function(obj, first){ //触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showWeekReportTable(result,startnum,endnum);
                    $("#weekreport_currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#weekreport_currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#weekreport_div"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接周报表tr
    function showWeekReportTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.id;
            rowdata = rowdata + "<tr><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.HOSTNAME)+"\">"+stringutil.isNull(rowninfo.HOSTNAME)+
            "</td><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.ADDR)+"\">"+stringutil.isNull(rowninfo.ADDR)+"</td><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.AVGCPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.AVGCPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MINCPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MINCPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MAXCPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MAXCPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.AVGMEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.AVGMEMORYMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MINMEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MINMEMORYMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MAXMEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MAXMEMORYMVALUE)+"</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        $("#weekreport_hostresourcesstatisticsdiv").empty().append(rowdata);
    }
    
    
    //月报表查询按钮事件
    function monthreportQueryOpt(){
        loadingwait();
        loadMonthReportOptRecord();
    }
    //加载月报表查询内容
    function loadMonthReportOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        
        var monthreportaddr = stringutil.Trim($("#monthreportaddr").val());
        $("#monthreportaddr_page").val(monthreportaddr);
        var monthreportdate = stringutil.Trim($("#monthreportdate").val());
        $("#monthreportdate_page").val(monthreportdate);
        var data = {'addr':monthreportaddr,'date':monthreportdate};
        $.getJSON("/view/class/mainttool/hostresourcesstatistics/query/monthreport?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#monthreport_querynum").text(total);
            laypage({
                cont: 'monthreport_pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: false, //是否开启跳页
                jump: function(obj, first){ //触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showMonthReportTable(result,startnum,endnum);
                    $("#monthreport_currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#monthreport_currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#monthreport_div"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接月报表tr
    function showMonthReportTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.id;
            rowdata = rowdata + "<tr><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.HOSTNAME)+"\">"+stringutil.isNull(rowninfo.HOSTNAME)+
            "</td><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.ADDR)+"\">"+stringutil.isNull(rowninfo.ADDR)+"</td><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.AVGCPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.AVGCPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MINCPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MINCPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MAXCPUMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MAXCPUMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.AVGMEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.AVGMEMORYMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MINMEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MINMEMORYMVALUE)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.MAXMEMORYMVALUE)+"\">"
            +stringutil.isNull(rowninfo.MAXMEMORYMVALUE)+"</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        $("#monthreport_hostresourcesstatisticsdiv").empty().append(rowdata);
    }
    
    //导出日数据
    function exportdayShow() {
    	layer.confirm('是否确认导出该批次数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            layer.closeAll();
            var dayreportaddr = $("#dayreportaddr_page").val();
            var dayreportdate = $("#dayreportdate_page").val();
            var url = "/view/class/mainttool/hostresourcesstatistics/export?type=day&addr="+dayreportaddr+"&date="+dayreportdate;
            window.open(url+"&random=" + Math.random(),"_blank");
        });
    }
    
    //导出周数据
    function exportweekShow() {
    	layer.confirm('是否确认导出该批次数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            layer.closeAll();
            var weekreportaddr = $("#weekreportaddr_page").val();
            var weekreportdate = $("#weekreportdate_page").val();
            var url = "/view/class/mainttool/hostresourcesstatistics/export?type=week&addr="+weekreportaddr+"&date="+weekreportdate;
            window.open(url+"&random=" + Math.random(),"_blank");
        });
    }
    
    //导出月数据
    function exportmonthShow() {
    	layer.confirm('是否确认导出该批次数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            layer.closeAll();
            var monthreportaddr = $("#monthreportaddr_page").val();
            var monthreportdate = $("#monthreportdate_page").val();
            var url = "/view/class/mainttool/hostresourcesstatistics/export?type=month&addr="+monthreportaddr+"&date="+monthreportdate;
            window.open(url+"&random=" + Math.random(),"_blank");
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
        	queryOpt();
        	addJumpShow(result.data);
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
            area : [ '620px', '360px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
    
});