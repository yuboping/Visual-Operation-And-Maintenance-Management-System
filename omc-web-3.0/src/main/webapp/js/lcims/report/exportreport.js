require.config({
	paths:{
		'lcims' : "/js/lcims",
        'resizewh' : "/js/lcims/resizewh/resizewh",
        'jquery' : '/js/jquery/jquery.min',
        'iscroll' : '/js/lcims/tool/iscroll',
        'layer' : '/js/layer/layer',
        "daterangepicker": "/js/lcims/tool/daterangepicker",
        "moment": "/js/lcims/tool/moment",
        "laypage" : "/js/lcims/tool/laypage"
	},
    shim: {
        "daterangepicker": {
            deps: ["jquery", "moment"]
        },
        "moment": {}
    }
});

require(['jquery', 'layer', 'laypage', 'resizewh','daterangepicker' ],function($, layer, laypage, resizewh){
	var layer_load;
	butBindFunction();
	setDateForInput();
    loadingwait();
    loadReportInfo(); 
	resizewh.resizeWH($("#main"));
	 
////////////////////////////////////////////////////////////////////
	function butBindFunction(){
		$("#exportbut").click(function(){
			exportExport();
		});
		$("#querybutton").click(function(){
			loadingwait();
			loadReportInfo();
		});
		$("#report_type").change(function(){
			setDateForInput();
		});
    }
	
	function exportExport(){
		var reportid = $("#reportid").val();
        var reporttype=$("#report_type").val();
        var time=$("#startdate").val();
		if(reportid>0){
			location.href ="/view/class/report/report/exportreport/"+reportid+"?reporttype="+reporttype+"&time="+time+"&random=" + Math.random();
		}
	}
	
	function setDateForInput(){
		var reporttype = $("#report_type").val();
		var dataFormat='YYYYMMDD';
		if(reporttype==1){
			dataFormat='YYYYMMDD';
		}else if(reporttype==2){
			dataFormat='YYYYMM';
		}
		var today = moment().format(dataFormat);
	   	$('#startdate').val(today).daterangepicker({
	   		parentEl: $("#startDate_div"),
	   		format : dataFormat,
	   		startDate:today,
	   		showDropdowns: false,
	   		singleDatePicker:true
	   	});
	}
	
	function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
	
	// 加载查询内容
    function loadReportInfo() {
        var reportid = $("#reportid").val();
        var reporttype=$("#report_type").val();
        var time=$("#startdate").val();
        var data = {
            'reportid' : reportid,'reporttype':reporttype,'time':time
        };
        $.getJSON("/view/class/report/query?random=" + Math.random(), data, function(result) {
        	layer.close(layer_load);
        	$("#querynum").text(result.reportinfo.length-1);
            showTable(result.reportinfo);
            resizewh.resizeWH($("#main"));
        });
    }
    
    // 拼接tr
    function showTable(data) {
        var rowdata = "";
        var length = data.length;
        if(length==0){
        	return;
        }
        var fieldsize = data[0].length;
        th_size = 92/fieldsize;
        var temp =temp + "<th style=\"width:8%;word-wrap:break-word; word-break:normal;\">"+data[0][0]+ "</th>";
        for(var i=1;i<fieldsize;i++){
        	temp =temp + "<th style=\"width:"+th_size+"%;word-wrap:break-word; word-break:normal;\">"+data[0][i]+ "</th>";
        }
        temp = "<tr>"+ temp +"</tr>";
        $("#tb_head").empty().append(temp);
        $("#tb_body").empty();
        for(var j=1;j<length;j++){
        	temp = "";
        	for(var i=0;i<fieldsize;i++){
        		temp = temp + "<td style=\"word-wrap:break-word; word-break:normal;\">"+data[j][i]+ "</td>";
            }
        	temp = "<tr>"+ temp +"</tr>";
        	$("#tb_body").append(temp);
        }
    }
});