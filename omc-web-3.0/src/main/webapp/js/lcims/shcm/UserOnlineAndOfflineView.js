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
        'stringutil': '/js/lcims/tool/stringutil',
        'echarts':"/js/echarts"
    }
});

require(['jquery','layer','laypage','resizewh','laydate','stringutil','echarts','echarts/chart/line'],
    function($,layer,laypage,resizewh,laydate,stringutil,ec) {
	
	Date.prototype.format = function (fmt) {
	    var o = {
	        "M+": this.getMonth() + 1, //月份
	        "d+": this.getDate(), //日
	        "h+": this.getHours(), //小时
	        "m+": this.getMinutes(), //分
	        "s+": this.getSeconds(), //秒
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	        "S": this.getMilliseconds() //毫秒
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	
	var time=(new Date).getTime()-24*60*60*1000;
	var yesterday=new Date(time);
	var s1 = yesterday.format("yyyy-MM-dd");
	var s2 = yesterday.format("yyyy-MM");
	var time2=(new Date).getTime()-24*60*60*1000*15;
	var myDate=new Date(time2);
	var s3=myDate.format("yyyy-MM-dd");
	var queryTime = s3+" ~ "+s1;;
	var startDate="";
	var endDate2="";
	var lastdate = new Date; 
    var lastyear = lastdate.getFullYear();
    var lastmonth = lastdate.getMonth();
    if(lastmonth == 0){
         lastyear = lastyear -1;
         lastmonth = 12; 
    }
   var lastyearmonth="";
	if(lastmonth<10){
		lastyearmonth=lastyear+"-"+"0"+lastmonth
	}else{
		lastyearmonth=lastyear+"-"+lastmonth
	}
	
	
	laydate.render({
	    elem: '#timeCheck'
	    ,range: '~'
	    ,max: s1
	    ,value:s3+" ~ "+s1
	    ,done: function(value, date, endDate){
	    	queryTime=value;
	    	startDate=date;
	    	endDate2=endDate;
	      }
	 });
	//年月范围
    laydate.render({
	  elem: '#monthCheck'
	  ,type: 'month'
	  ,max: s2
	  ,value: new Date()
	  ,range: '~'
	  ,value:lastyearmonth+" ~ "+s2
	  ,done: function(value, date, endDate){
		    queryTime=value;
		    startDate=date;
	    	endDate2=endDate;
	      }
	});
    
    //权限控制操作标签
    function initChildrenMenu(){
        var pageUrl=window.location.pathname;
        var url = "/view/class/querychildrenmdmenu";
        $.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
            if(result!=null && result.length >0 ){
                for(var i=0;i<result.length;i++){
                    $("#operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
                    // 导出绑定事件
                    if(result[i].url=='export'){
                        $("#"+result[i].name).click(function() {
                            exportShow();
                        });
                    }
                }
            }

        });
    }
    
    initChildrenMenu()
    function isshowchart(){
    	if($('#chartdiv').is(':hidden')){
    		$("#chartdiv").show();
    		$("#showchartdiv").html("隐藏图表");
    		$("#showtablediv").css("max-height","270px")
    	}else{
    		$("#chartdiv").hide();
    		$("#showchartdiv").html("显示图表");
    		$("#showtablediv").css("max-height","500px")
    	}
    }
    
    
   // $("#showchartdiv").bind('click',isshowchart);
	
	function timeChange(type){
		var itemValue = $("#query_type").val();
		$("#timeCheck").val("");
		$("#monthCheck").val("");
		if(type != 1){
			queryTime=""
		}
		if(itemValue == '1'){
			$("#timeCheck").show();
			$("#monthCheck").hide();
		}else{
			$("#timeCheck").hide();
			$("#monthCheck").show();
		}
	}
	timeChange(1);
	$('#query_type').bind('change',timeChange);
	
	var datas=[]
	var page_count = 50;
    function query(){
    	var itemValue = $("#query_type").val();//queryTime
    	if(queryTime==""){
    		if(itemValue == '1'){
    			layer.tips('日期不能为空!', '#timeCheck',{ tips: [2, '#EE1A23'] });
    		}else{
    			layer.tips('日期不能为空!', '#monthCheck',{ tips: [2, '#EE1A23'] });
    		}
    		return
    	}
    	if(itemValue == '1'){
    		var start=new Date(parseInt(startDate.year),parseInt(startDate.month) -1,parseInt(startDate.date));
    		var end=new Date(parseInt(endDate2.year),parseInt(endDate2.month) -1,parseInt(endDate2.date));
    		var starttime=start.getTime()+24*60*60*1000*15;//开始日期加15天
    		var endtime=end.getTime();
    		if(starttime < endtime){
    			layer.tips('日期跨度不能超过15天!', '#timeCheck',{ tips: [2, '#EE1A23'] });
    			return
    		}
		}else{
			var start=new Date(parseInt(startDate.year),parseInt(startDate.month) -1+3,1);
    		var end=new Date(parseInt(endDate2.year),parseInt(endDate2.month) -1,1);
    		if(start.getTime() <= end.getTime()){
    			layer.tips('月日期期跨度不能超过3个月!', '#monthCheck',{ tips: [2, '#EE1A23'] });
    			return
    		}
		}
    	var data = {
                'queryType': itemValue,
                'queryTime': queryTime
            };
    	$.getJSON("/view/class/shcmreport/UserOnlineAndOfflineView/query?random=" + Math.random(), data, function (result) {
    		datas=result;
    		$("#querynum").html(datas.length)
    		var total = datas.length;
            var pagecount = Math.ceil(total / page_count)
    		laypage({
                cont : 'pageinfo',
                skin : '#6AB0F4',
                pages : pagecount,
                curr : 1,
                skip : false, // 是否开启跳页
                jump : function(obj, first) {
                	showtable(obj.curr,page_count)
                },
                groups : page_count
                // 连续显示分页数
            });
        });
    }
    function showtable(curr,pagesize){
    	var html=$("#alarmhistorydiv")
		var context=""
		for(var i=pagesize*(curr-1);(i<pagesize*curr && i<datas.length);i++){
			var value=datas[i];
			context+="<tr>"
			context+="<th style=\"width:25%\">"+value.attr4+"</th>";
			context+="<th style=\"width:50%\">"+value.attr1+"</th>";
			context+="<th style=\"width:25%\">"+value.attr2+"</th>";
			context+="</tr>"
		}	
    	if(datas.length==0){
    		context+="<tr>"
    	   context+="<th style=\"width:100%\" colspan=3>暂无数据</th>";
    	   context+="</tr>"
    	}
		html.html(context);
    	
    }
    function quertdata(){
    	$("#chartdiv").show();
    	query()
    	queryChart()
    }
    $('#querybutton').bind('click',quertdata);
    var myChart = ec.init(document.getElementById('userChartShow'));
    function showEchart(chartdata){
    	var xdata=chartdata.rateData
    	var ydata=chartdata.yData
    	var series=[];
    	var title=[];
    	for(var key in ydata){
    		  var so={
  		            name: key,
		            type: 'line',
		            stack: '总量',
		            data: ydata[key]
		        } 
    		  series.push(so)
    		  title.push(key)
    		}
    	var option = {
    		    backgroundColor: '#ffffff',
    		    title: {
    		        text: ''
    		    },
    		    tooltip: {
    		        trigger: 'axis'
    		    },
    		    legend: {
    		        data: title
    		    },
    		    grid: {
    		        left: '3%',
    		        right: '4%',
    		        bottom: '3%',
    		        containLabel: true
    		    },
    		    toolbox: {
    		        feature: {
    		            saveAsImage: {}
    		        }
    		    },
    		    xAxis: {
    		        type: 'category',
    		        boundaryGap: false,
    		        data: xdata,
    		        axisLabel : {
    		        	"formatter": function (value, index) { 
                        	var year=value.substr(0,4)
                        	var qita =value.substr(5,value.length);
                            return qita+"\n"+year
                       }
                    }
    		    },
    		    yAxis: {
    		        type: 'value',
    		        axisLabel : {
    		        	"formatter": function (value, index) { 
                            if (value >= 10000 && value < 10000000) {
                                value = value / 10000 + "万"; 
                            } else if (value >= 10000000) {
                                value = value / 10000000 + "千万"; 
                            } 
                            return value; 
                        }
                    }
    		    },
    		    series: series
    		};


    	// 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option,true);
    }
    
    
    function queryChart(){
    	if(queryTime==""){
    		return
    	}
    	var itemValue = $("#query_type").val();//queryTime
    	if(itemValue == '1'){
    		var start=new Date(parseInt(startDate.year),parseInt(startDate.month) -1,parseInt(startDate.date));
    		var end=new Date(parseInt(endDate2.year),parseInt(endDate2.month) -1,parseInt(endDate2.date));
    		var starttime=start.getTime()+24*60*60*1000*15;//开始日期加15天
    		var endtime=end.getTime();
    		if(starttime < endtime){
    			return
    		}
		}else{
			var start=new Date(parseInt(startDate.year),parseInt(startDate.month) -1+3,1);
    		var end=new Date(parseInt(endDate2.year),parseInt(endDate2.month) -1,1);
    		if(start.getTime() <= end.getTime()){
    			return
    		}
		}
    	
    	var data = {
                'queryType': itemValue,
                'queryTime': queryTime
            };
    	$.getJSON("/view/class/shcmreport/UserOnlineAndOfflineView/queryEchart?random=" + Math.random(), data, function (result) {
    		showEchart(result);
        });
    }
    
  //导出Excel
    function exportShow(){
    	var itemValue = $("#query_type").val();//queryTime
    	if(queryTime==""){
    		if(itemValue == '1'){
    			layer.tips('日期不能为空!', '#timeCheck',{ tips: [2, '#EE1A23'] });
    		}else{
    			layer.tips('日期不能为空!', '#monthCheck',{ tips: [2, '#EE1A23'] });
    		}
    		return
    	}
    	if(itemValue == '1'){
    		var start=new Date(parseInt(startDate.year),parseInt(startDate.month) -1,parseInt(startDate.date));
    		var end=new Date(parseInt(endDate2.year),parseInt(endDate2.month) -1,parseInt(endDate2.date));
    		var starttime=start.getTime()+24*60*60*1000*15;//开始日期加15天
    		var endtime=end.getTime();
    		if(starttime < endtime){
    			return
    		}
		}else{
			var start=new Date(parseInt(startDate.year),parseInt(startDate.month) -1+3,1);
    		var end=new Date(parseInt(endDate2.year),parseInt(endDate2.month) -1,1);
    		if(start.getTime() <= end.getTime()){
    			return
    		}
		}
         var url = "/view/class/shcmreport/UserOnlineAndOfflineView/export?queryType="+itemValue+"&queryTime="+queryTime;
         window.open(url+"&random=" + Math.random(),"_blank");
    }
    
});
