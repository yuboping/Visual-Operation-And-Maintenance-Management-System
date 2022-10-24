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
        var report_type;
        var menu_tree_name;
        
        resizewh.resizeBodyH($("#mainreportpage"));
        butBindFunction();
        reset("mainreportpage");
        initChildrenMenu();
        initQueryCondition();
//        loadingwait();
//        loadReportOptRecord();
        
        
//----------------------------------以下为自定义方法-------------------------------------------------//
        
    function butBindFunction(){
        $("#report_querybutton").click(function() {
        	queryOpt();
        });
        $("#report_resetbutton").click(function() {
            reset("report_div");
            initReportDate(report_type[0].type,false);
        });
    }
    
    function initQueryCondition(){
    	var pageUrl=window.location.pathname;
        var url = "/system/menu/query/menutreename";
        $.ajaxSettings.async = false;
    	$.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
    		menu_tree_name = result.name;
     	});
    	$("#reporttype").empty();
    	var url = "/view/class/report/query/reporttype";
    	$.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
    		report_type = result;
    		if(result!=null && result.length >0 ){
     			for(var i=0;i<result.length;i++){
     				$("#reporttype").append("<option value='"+result[i].type+"'>"+result[i].name+"</option>");
     			}
     			initReportDate(result[0].type,true);
     		}
//    		loadingwait();
//    		loadReportOptRecord();
     	});
    	$("#reporttype").off("change").on("change",function(){
    		var reporttype = $("#reporttype").val();
    		initReportDate(reporttype,false);
        });
    }
    
    function initReportDate(reporttype,isFirst){
    	var curDate = new Date();
    	switch(reporttype)
			{
			case "DAY":
				var newDate=new Date(curDate.setDate(curDate.getDate()-1));
				if(isFirst){
					$("#reportdate_page").val(getFormatDate(newDate));
				}
			    $("#reportdateinput").html('<input size="30" readonly="readonly" class="input-medium" id="reportdate" name="reportdate" type="text">');
				laydate.render({
		            elem: '#reportdate' //指定元素
		            ,max: -1
		            ,value: newDate 
				    ,type: 'date'
		        });
			  break;
			case "WEEK":
		    	var weekFirstDay=new Date(curDate-(curDate.getDay()-1)*86400000);
		    	var iDays = (datedifference(weekFirstDay,curDate)+1)*-1;
		    	var newDate=new Date(weekFirstDay.setDate(weekFirstDay.getDate()-7));
		    	if(isFirst){
		    		$("#reportdate_page").val(getFormatDate(newDate));
		    	}
			    $("#reportdateinput").html('<input size="30" readonly="readonly" class="input-medium" id="reportdate" name="reportdate" type="text">');
				laydate.render({
		            elem: '#reportdate' //指定元素
		            ,max: iDays
		            ,value: newDate
				    ,type: 'date'
		        });
			  break;
			case "MONTH":
				var monthFirstDay=new Date(new Date().getFullYear(), new Date().getMonth(), 1);
		    	var iDays = 0;// (datedifference(monthFirstDay,curDate)+1)*-1;
		    	var newDate=new Date(new Date().getFullYear(), new Date().getMonth()-1, 1);
		    	if(isFirst){
		    		$("#reportdate_page").val(getFormatDate(newDate));
		    	}
			    $("#reportdateinput").html('<input size="30" readonly="readonly" class="input-medium" id="reportdate" name="reportdate" type="text">');				
				laydate.render({
		            elem: '#reportdate' //指定元素
		            ,max: iDays
		            ,value: newDate
				    ,type: 'date'
		        });
			  break;
		    }
    }
    
    //获取date，格式YYYY-MM-DD
    function getFormatDate(date) {
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }
    
    //获取2天之差
    function datedifference(date1, date2) {    
    	var days = date2.getTime() - date1.getTime();
    	var time = parseInt(days / (1000 * 60 * 60 * 24));
        return time
    };
    
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
 		$("#report_operate_menu").empty();
 		$("#report_menu").empty();
    	var url = "/view/class/querychildrenmdmenu";
     	$.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
     		if(result!=null && result.length >0 ){
     			var tagon = true;
     			var reportButton = "";
     			for(var i=0;i<result.length;i++){
     				// 导出绑定事件
         			if(result[i].url=='export'){
         				$("#report_operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
    					$("#"+result[i].name).click(function() {
    						exportShow();
    			        });
    				}
     			}
     		}
     	});
    }
    
    //报表查询按钮事件
    function queryOpt(){
        loadingwait();
        loadReportOptRecord();
    }
    //加载报表查询内容
    function loadReportOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;	
        var page_count = 10;
        
        var reporttype = $("#reporttype option:selected").attr("value");
        $("#reporttype_page").val(reporttype);
        var reportdate = stringutil.Trim($("#reportdate").val());
        if(reportdate == ""){
        	reportdate = $("#reportdate_page").val();
        }else{
        	$("#reportdate_page").val(reportdate);
        }
        var data = {'reportType':reporttype,'date':reportdate};
        $.getJSON("/data/class/report/select/"+menu_tree_name+"?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            var total = result.data.length;
            var thead = result.header;
            var theaddata = "<tr>";
            for(var i=0;i<thead.length;i++){
                theaddata = theaddata + "<th>"+stringutil.isNull(thead[i].showName)+"</th>";
            }
            theaddata = theaddata + "</tr>";
            $("#report_reportpagethead").empty().append(theaddata);
            pagecount=Math.ceil(total/page_count);
            $("#report_querynum").text(total);
            laypage({
                cont: 'report_pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: false, //是否开启跳页
                jump: function(obj, first){ //触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showReportTable(result.data,startnum,endnum);
                    $("#report_currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#report_currnum").empty().text("0 ");
                    }
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接报表tr
    function showReportTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            rowdata = rowdata + "<tr>";
            for(var j=0;j<rowninfo.length;j++){
            	rowdata = rowdata + "<td  class='over_ellipsis' title=\""+stringutil.isNull(rowninfo[j])+"\">"+stringutil.isNull(rowninfo[j])+"</td>";
            }
            rowdata = rowdata + "</tr>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        $("#report_reportpagetbody").empty().append(rowdata);
    }
    
    //导出数据
    function exportShow() {
    	layer.confirm('是否确认导出该批次数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] // 按钮
        },function(){
            layer.closeAll();
            var reporttype = $("#reporttype_page").val();
            var reportdate = $("#reportdate_page").val();
            var url = "/data/class/report/export/"+menu_tree_name+"?reportType="+reporttype+"&date="+reportdate;
            window.open(url+"&random=" + Math.random(),"_blank");
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }     
    
});