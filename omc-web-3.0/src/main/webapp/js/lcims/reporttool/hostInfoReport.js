require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'layer':'/js/layer/layer',
        "daterangepicker": "/js/lcims/tool/daterangepicker",
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage"
    },
    shim: {
        "daterangepicker": {
            deps: ["jquery", "moment"]
        },
        "moment": {}
    }
});
require(['jquery', 'layer', 'daterangepicker', 'laypage', 'resizewh' ],
	function($, layer, daterangepicker,laypage,resizewh) {
	    resizewh.resizeWH($("#mainqryHostInfoReport"));
	    butBindFunction();
	    showDayTime();
	    var year = moment().format('YYYY');
	    initYear(year);
	    var yesterday = moment().subtract(1, 'days').format('YYYY-MM-DD');
    	$('#querydate').daterangepicker({
    		parentEl: $("#queryDate_div"),
    		format : 'YYYY-MM-DD',
    		startDate: yesterday,
    		maxDate: yesterday,
    		showDropdowns: false,
    		singleDatePicker:true
    	}, function (start, end, label) {
    	});
        reset("mainqryHostInfoReport");
//-----------------------------------------以下为自定义方法-------------------------------------------------//            
        function butBindFunction(){
	        $("#querybutton").click(function() {
	        	var querytype = $("#querytype").val();
	        	var querydate = "";
	        	if(querytype=='1'){
		        	querydate = $("#querydate").val();
		        }else{
		        	querydate = $("#queryYear").val()+"-"+$("#queryMonth").val();
		        }
	            if(null == querydate || querydate ==""){
	            	layer.tips('查询日期不能为空!', '#querydate',{ tips: [2, '#EE1A23'] }); 
	            }else{
	            	loadingwait();
	                loadOptRecord();
	            }
	        });
	        $("#resetbutton").click(function() {
	            reset("mainqryHostInfoReport");
	            showDayTime();
	        });
	        
	        $("#querytype").bind("change",function(){
	        	var querytype=$("#querytype").val();
	        	if(querytype=='1'){
	        		showDayTime();
	        	}else{
	        		showMonthTime();
	        	}
            });
	        $("#exportbutton").click(function(){
	        	var querytype = $("#querytype").val();
	        	var querydate = "";
	        	if(querytype=='1'){
		        	querydate = $("#querydate").val();
		        }else{
		        	querydate = $("#queryYear").val()+"-"+$("#queryMonth").val();
		        }
	            if(null == querydate || querydate ==""){
	            	layer.tips('查询日期不能为空!', '#querydate',{ tips: [2, '#EE1A23'] }); 
	            }else{
	            	exportExport();
	            }
			});
        }
        
        function initYear(year){
        	var option = "";
        	for(var i=0;i<5;i++){
        		option = option + "<option value='"+(year-i)+"'>"+(year-i)+"</option>";
        	}
        	$("#queryYear").empty().append(option);
        }
        function showMonthTime(){
        	$("#day_div").hide();
        	$("#month_div").show();
        }
        
        function showDayTime(){
        	$("#day_div").show();
        	$("#month_div").hide();
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
	    
	    function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
        }
	    
	    function loadOptRecord(){
	        var pagecount=0;
	        var page_count = 20;
	        var querytype = $("#querytype").val();
	        var querydate = "";
	        if(querytype=='1'){
	        	querydate = $("#querydate").val();
	        }else{
	        	querydate = $("#queryYear").val()+"-"+$("#queryMonth").val();
	        }
	        var reportId = $("#reportId").val();
	        console.log(querytype);
	        loadingwait();
	        var data = {'querytype':querytype,'querydate':querydate,'reportId':reportId};
	        $.getJSON("/view/class/reporttool/getData?random=" + Math.random(),data, function(result){
	        	console.log(result);
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
	                    resizewh.resizeWH($("#mainqryHostInfoReport"));
	                },
	                groups: page_count //连续显示分页数
	            });
	        });
	    }
	    
	    function showTable(data,startnum,endnum){
	    	var rowdata = "";
            if(data==null||data.length==0){
            	rowdata = rowdata + "<tr><td colspan='9'>暂无数据</td></tr>";
            	$("#qryHostInfoReport").empty().append(rowdata);
            }else{
            	for(var i=startnum;i<=endnum;i++){
                    var rowninfo = data[i-1];
                    rowdata = rowdata + "<tr><td>"+rowninfo[0]+"</td><td>"
                    +rowninfo[1]+"</td><td>"+rowninfo[2]+"</td><td>"
                    +rowninfo[3]+"</td><td>"+rowninfo[4]+"</td><td>"
                    +rowninfo[5]+"</td><td>"+rowninfo[6]+"</td><td>"
                    +rowninfo[7]+"</td><td>"+rowninfo[8]+"</td><td>"
                    +rowninfo[9]+"</td><td>"+rowninfo[10]+"</td>"
                    +"</tr>";
                }
            	$("#qryHostInfoReport").empty().append(rowdata);
            }
	    }
	    
	    function exportExport(){
	    	var querytype=$("#querytype").val();
	        var querydate = "";
	        if(querytype=='1'){
	        	querydate = $("#querydate").val();
	        }else{
	        	querydate = $("#queryYear").val()+"-"+$("#queryMonth").val();
	        }
	        var reportId = $("#reportId").val();
			window.open("/view/class/reporttool/exportData?querydate="+querydate+"&querytype="+querytype+"&reportId="+reportId+"&random=" + Math.random(),"_blank");
		}
});