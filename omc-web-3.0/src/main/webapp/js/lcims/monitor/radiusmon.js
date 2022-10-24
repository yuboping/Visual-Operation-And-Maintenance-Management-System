require.config({
    paths: {
        "domReady": "/js/require/domReady",
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'jquery': '/js/jquery/jquery.min',
        'cookie': '/js/jquery/jquery.cookie',
        'layer': '/js/lcims/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'packdata':'/js/lcims/tool/packdata',
        'layer':'/js/layer/layer',
        'daterangepicker':'/js/lcims/tool/daterangepicker_multicycle',
        'moment': '/js/lcims/tool/moment',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'js_scroll':'/js/scroll/jquery.mousewheel.min'
    },
    shim: {
        "daterangepicker": {
            deps: ["jquery", "moment"]
        },
        "moment": {}
    }
});
require([ "domReady", 'jquery', "packdata",
      'echarts',
      'layer','laypage',
      'daterangepicker',
      'cookie',
      'resizewh',
      'echarts/chart/bar',
      'echarts/chart/line',
      'echarts/chart/gauge',
      'echarts/chart/force',
      'echarts/chart/pie'
      ],function(domReady, $, packdata, ec,layer,laypage,daterangepicker,cookie,resizewh) {
        
        cusset();
        
        $.each($(".omc-scroll"), function () {
            var myScroll = new IScroll(this, {
                scrollbars: true,
                mouseWheel: true,
                click: false,
                interactiveScrollbars: true,
                shrinkScrollbars: 'scale',
                fadeScrollbars: true
            });
        });
        
        loadAllData();
        resizewh.resizeWH($("#mainarea"),"auto");
        $("#dataStyle").bind("change",function(){
            drawDateInput();
        });
        
        //图表5分钟刷新一次 
        setInterval(loadAllData,1000*60*5);
       
    	/*窗口变化时重新设置高度宽度*/
        /*------------------------------------------------以下为自定义方法---------------------------------------------------*/

        //获取页面上的所有指标，根据指标加载数据
        function loadAllData(){
        		//TODO 加载指标信息
        	$("input[name='chart_in_maindiv']").each(function(){
        		var data = $(this).attr("value");
        		var chartinfo = eval('('+data + ')');
        		loadData(chartinfo);
        	});
        };
        
        //添加时间控件
        function drawDateInputInLayer(){
        	var dateInput = "<input type=\"text\" style=\"width: 200px\" name=\"layer_date_input\" id=\"layer_date_input\"" +
        			" class=\"form-control ml40\"></input>";
        	$("#layer_date_div").empty().append(dateInput);
        	
        	//弹出层时间控件
        	var minDate = moment().add('days',-7);
        	var today = moment().format('YYYY-MM-DD');
        	$('#layer_date_input').val(today);
        	$('#layer_date_input').daterangepicker({
        		parentEl: $("#layer_date_div"),
        		format : 'YYYY-MM-DD',
        		startDate:today,
        		maxDate:today,
        		showDropdowns: false,
        		minDate:minDate,
        		dateLimit:'7',
        		singleDatePicker:true
        	}, function (start, end, label) {
            });
        }
        

        function drawDateInput(min){
        	var dataStyle = $("#dataStyle").val();
        	var today = moment().format('YYYY-MM-DD');
        	$('#enddate').val('');
        	if(null==dataStyle||dataStyle==""){
        		$('#startdate').daterangepicker({
               		parentEl: $("#startDate_div"),
               		format : 'YYYY-MM-DD',
               		startDate:today,
               		maxDate:today,
               		showDropdowns: false,
               		singleDatePicker:true
               	}, function (start, end, label) {
                });
        		$("#end_div").hide();
        	}else if(dataStyle=="queryhour"){
        		//按小时查询
        		$('#startdate').daterangepicker({
               		parentEl: $("#startDate_div"),
               		format : 'YYYY-MM-DD',
               		startDate:today,
               		maxDate:today,
               		showDropdowns: false,
               		singleDatePicker:true
               	}, function (start, end, label) {
                });
               	$('#enddate').daterangepicker({
               		parentEl: $("#endDate_div"),
               		format : 'YYYY-MM-DD',
               		startDate:today,
               		maxDate:today,
               		showDropdowns: false,
               		singleDatePicker:true
               	}, function (start, end, label) {
                });
               	$("#end_div").show();
        	}else if(dataStyle=="queryday"){
        		//按天查询
        		var today = moment().format('YYYY-MM-DD');
        		$('#startdate').daterangepicker({
               		parentEl: $("#startDate_div"),
               		format : 'YYYY-MM-DD',
               		startDate:today,
               		maxDate:today,
               		showDropdowns: false,
               		singleDatePicker:true
               	}, function (start, end, label) {
                });
               	$('#enddate').daterangepicker({
               		parentEl: $("#endDate_div"),
               		format : 'YYYY-MM-DD',
               		startDate:today,
               		maxDate:today,
               		showDropdowns: false,
               		singleDatePicker:true
               	}, function (start, end, label) {
                });
               	$("#end_div").show();
        	}else if(dataStyle=="querymin"){
        		var increment = 5;
        		switch(min) {
                case '1min':
                	increment = 1;
                   break;
                default:
                	increment = 5;
                }
        		//按分钟查询
        		var today = moment().format('YYYY-MM-DD HH:mm:ss');
        		$('#startdate').daterangepicker({
               		parentEl: $("#startDate_div"),
               		format : 'YYYY-MM-DD HH:mm',
               		startDate:today,
               		maxDate:today,
               		showDropdowns: true,
               		autoUpdateInput: true, 
               		singleDatePicker:true,
                    timePicker: true,
                    timePicker12Hour: false,
                    timePickerIncrement:increment
               	}, function (start, end, label) {
                });
        		$("#end_div").hide();
        	}
        }
        
        //弹出层画图标
        function layerchartinfo(data) {
        	$("#startDate_label").html("开始日期");
        	//drawDateInputInLayer();
        	//TODO
        	var chartinfo = eval('('+data + ')');
        	if(chartinfo.chart_type!='line' && chartinfo.chart_type!='tableline' 
        		&& chartinfo.chart_type!='pielayer'&& chartinfo.chart_type!='tablelayer'){
        		return;
        	}
//        	var page_history_more_flag = $("#page_history_more_flag").val();
        	var cycle = 0;
        	switch(chartinfo.scope) {
            case 'recent':
               $("#dataStyle").empty().append("<option value=\"querymin\">按分钟统计</option>");
               if(chart_title = '当天各认证失败原因占比'){
            	   	$("#dataStyle").empty().append("<option value=\"querymin\">按天统计</option>");
            	   	$("#startDate_label").html("截至时间");
           	   }
               var today = moment().format('YYYY-MM-DD HH:mm');
	        	$('#startdate').val(today);
	        	drawDateInput(chartinfo.chart_interval);
	        	var url = chartinfo.data_url+"&queryDate="+today+":00&random="+Math.random();
               break;
            default:
            	$("#dataStyle").empty().append("<option value=\"\">原始数据</option>");
	        	if("false"=="true"){
	            	$("#dataStyle").append("<option value=\"queryhour\">按小时统计</option>");
	            	$("#dataStyle").append("<option value=\"queryday\">按天统计</option>");
	        	}
	        	
	        	var today = moment().format('YYYY-MM-DD');
	        	$('#startdate').val(today);
	        	drawDateInput();
	        	var url = chartinfo.data_url+"&queryDate="+today+"&random="+Math.random();
            } 
        	
        	
        	$("#layer_hidden_chartinfo").attr("value",data);
        	//修改样式高度
        	var layer_div_H=$(window).height()*0.6;
        	var layer_div_W=$(window).width()*0.95;
//        	$("#layer_div").css('width',layer_div_W);
        	$("#layer_chartinfo_div").css('width',layer_div_W);
        	$("#layer_chartinfo_div").css('height',layer_div_H);
        	//特殊处理
    		loadLineInLayer("layer_chartinfo_div",cycle,url,true,chartinfo);
        	layer.open({
                type: 1,
                title: chartinfo.chart_title,
                area: [layer_div_W, layer_div_H],
                shadeClose: true,
                content: $("#layer_div")
        	});
        };
        
        function cusset(){
        	//添加ESC监控,按ESC的时候关闭layer弹层
        	$(document).keyup(function(event){
           		 if(event.keyCode == 27) {
       				 layer.closeAll();
           		 }
       		});
        	//添加弹出框触发事件
        	$('div[name="siglechartdiv"]').each(function(){
        		$(this).dblclick(function(){
        			var index = $(this).attr("value");
        			var chartinfo = $("#chart_in_maindiv_"+index).val();
        			$("#layer_div").attr("value",chartinfo);
        			layerchartinfo(chartinfo);
    			});
        	});
        	
        	$("#layer_but_serach").click(function(){
        		var data = $("#layer_hidden_chartinfo").attr("value");
        		var chartinfo = eval('('+data + ')');
        		var dataStyle = $("#dataStyle").val();
        		var queryDate = $('#startdate').val();
        		var startdate = $('#startdate').val();
        		var enddate = $('#enddate').val();
        		var cycle = chartinfo.cycle;
        		if(null!=dataStyle && ""!=dataStyle){
        			cycle=null;
        		}
        		var url = chartinfo.data_url+"&queryDate="+queryDate+"&dataStyle="+dataStyle+"&startdate="+startdate+"&enddate="+enddate+"&random="+Math.random();
        		if(chartinfo.scope=='recent'){
        			url = chartinfo.data_url+"&queryDate="+queryDate+":00&dataStyle="+dataStyle+"&startdate="+startdate+":00&enddate="+enddate+"&random="+Math.random();
            	}
        		loadLineInLayer("layer_chartinfo_div",cycle,url,false,chartinfo);
        	});
        	
        	$("#layer_but_export").click(function(){
        		var data = $("#layer_hidden_chartinfo").attr("value");
        		var chartinfo = eval('('+data + ')');
        		var dataStyle = $("#dataStyle").val();
        		var queryDate = $('#startdate').val();
        		var startdate = $('#startdate').val();
        		var enddate = $('#enddate').val();
        		var cycle = chartinfo.cycle;
        		if(null!=dataStyle && ""!=dataStyle){
        			cycle=null;
        		}
        		var url = chartinfo.data_url.replace("getDataLine","exportDataLine")+"&queryDate="+queryDate+"&dataStyle="+dataStyle+"&startdate="+startdate+"&enddate="+enddate+"&random="+Math.random();
        		window.open(url);
        	});
        };
        
        function loadData(chartinfo,chartid) {
        	//pie：饼图，line：折线图，map：地图，gauge：仪表盘，circle：环形图，table：表格，verticalbar：垂直柱状图，horizontalbar：横向柱状图
        	if(chartid == null || chartid==""){
    		   //特殊处理 弹出框加载效果图
    		   chartid = chartinfo.chart_name;
        	}
        	url = chartinfo.data_url;
        	var cycle = 0;
        	if(chartinfo.chart_type=="line" || chartinfo.chart_type=="tableline"){
              loadLine(chartid,cycle,url);
          	}else if(chartinfo.chart_type=="circle"){
              loadCircle(chartid,url);
            }else if(chartinfo.chart_type=="pie" || chartinfo.chart_type=="pielayer"){
                loadPie(chartid,url);
            }else if(chartinfo.chart_type=="gauge"){
            	loadGauge(chartid,url);
            }else if(chartinfo.chart_type=="verticalbar"){
                loadVerticalbar(chartid,url);
            }else if(chartinfo.chart_type=="map"){
                loadGrap(chartid,url);
            }else if(chartinfo.chart_type=="table" || chartinfo.chart_type=="tablelayer"){
                loadTable(chartid,url);
            }else if(chartinfo.chart_type=="ntpd"){
                loadNtpd(chartid,url);
            }else if(chartinfo.chart_type=="switch"){
                loadSwitch(chartid,url);
            }else if(chartinfo.chart_type=="lineprocess"){
            	loadLine(chartid,cycle,url,true);
            }else if(chartinfo.chart_type=="tableinfo"){
            	loadTableinfo(chartid,url);
            }else if(chartinfo.chart_type=="horizontalbar"){
            	loadHorizontalbar(chartid,url);
            }else if(chartinfo.chart_type=="grap"){
                loadGrap(chartid,url);
            }else if(chartinfo.chart_type=="hostinfo"){
                loadHostinfo(chartid,url);
            }else if(chartinfo.chart_type=="anaverticalbar"){
            	loadAnaVerticalbar(chartid,url,false);
            }else if(chartinfo.chart_type=="anaverticalbartenthousand"){
            	loadAnaVerticalbar(chartid,url,true);
            }
        };
        
        //圆环图
        function loadCircle(chartid,url){
            var data=[{"mark":"回应率","value":"0"}];
            $.getJSON(url+"&random="+Math.random(), function(resultdata) {
            	if(resultdata[0].data!=null&&resultdata[0].data.length!=0){
                    data=resultdata[0].data;
                }
                var myChart = ec.init(document.getElementById(chartid));
                myChart.setOption(packdata.getMonitorCircleOption("",data,["#91C7AE"]));
            });
        }
        
        //折线图,layer弹出层查询历史时调用
        function loadLineInLayer(chartid,cycle,url,clearFlag,chartinfo){
        	var myChart = ec.init(document.getElementById(chartid));
        	if(clearFlag){
        		myChart.clear();
        	}
            $.getJSON(url, function(resultdata) {
            	switch(chartinfo.chart_type) {
                case 'pielayer':
                	var option = packdata.getAnaPieOption("",resultdata);
                	myChart.setOption(option);
                   break;
                case 'tablelayer':
                	var resultdataremovalhead=new Array()
            		$.each(resultdata , function(q, x) {
            			if(q!=0){
            				resultdataremovalhead[q-1]=x;
            			}
            		});
                    	var table=[];
                            table.push("<table class='omc_city_tab2 table table-bordered table-striped table-head-bordered table-hover  center ' cellpadding='0' cellspacing='0' >");
                            table.push("<thead>");
                            table.push("<tr>");
                            $.each(resultdata[0].data , function(z, r) {
                                 table.push("<th>"+r.value+"</th>");
                            });
                            table.push("</tr>");
                            table.push("</thead>");
                            table.push("<tbody id='tbodydiv"+chartid+"'>");
                            table.push("</tbody>");
                            table.push("</table>");
                            $("#"+chartid).empty().append(table.join(""));
                            if(resultdataremovalhead!=null && resultdataremovalhead.length > 0){
                                var total = resultdataremovalhead.length;
                                var rowdata = "";
                                for(var j=0;j<total;j++){
                                    var rowninfo = resultdataremovalhead[j].data;
                                    rowdata = rowdata + "<tr>";
                                    $.each(rowninfo, function(i, row) {
                                        rowdata = rowdata + "<td title = '"+row.value+"'>"+row.value+"</td>";
                                    });
                                    rowdata = rowdata + "</tr>";
                                }
                                $("#tbodydiv"+chartid).empty().append(rowdata);
                                if(total<=5) {
                                    $(chartid).removeClass("boxh540");
                                    $(chartid).addClass("boxh265");
                                }else{
                                    $(chartid).removeClass("boxh265");
                                    $(chartid).addClass("boxh540");
                                    $.getScript("/js/lcims/home/jquery.mCustomScrollbar.concat.min.js",function(){
                                        $("#tbodydiv"+chartid).mCustomScrollbar({
                                            scrollButtons: {
                                                enable: true
                                            },
                                            theme:"dark-2"
                                        });
                                    });
                                    
                                }
                            }else{
                                $("#tbodydiv"+chartid).empty().append("<p id = 'nodata"+chartid+"'>暂无数据</p>");
                            }
                   break;
                default:
                	var option = packdata.getMonitorLineOption(resultdata,"",cycle);
	                if(resultdata!=null&&resultdata.length>1){
	            		option.legend.show=true;
	            	}
	                console.log(JSON.stringify(option));
	                myChart.setOption(option);
                }
            });
        };
        
        //折线图
        function loadLine(chartid,cycle,url,legendflag){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
            	var myChart = ec.init(document.getElementById(chartid));
                var option = packdata.getMonitorLineOption(resultdata,"",cycle);
                if(resultdata!=null&&resultdata.length>1){
            		option.legend.show=true;
            	}
                if(legendflag!=null&&legendflag==true){
                	option.legend.show=true;
                }
                myChart.setOption(option);
            });
        };
        
        //饼图
        function loadPie(chartid,url){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
        		var myChart = ec.init(document.getElementById(chartid));
                var option = packdata.getAnaPieOption("",resultdata);
                myChart.setOption(option);
            });
        }
        
        //仪表盘
        function loadGauge(chartid,url,ext) {
            var data=[{"mark":"仪表盘","value":"0"}];
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
            	if(resultdata[0].data!=null&&resultdata[0].data.length!=0){
                    data=resultdata[0].data;
                }
                var myChart = ec.init(document.getElementById(chartid));
                var datarate = Math.floor(data[0].value/100);
                if(datarate==1){
                	datarate = 0;
                }
                var max=(datarate+1)*100,min=0;
                var j = 3;//仪表盘的等份
                var maxexcept = Math.floor(max/j);
                var level=new Array()
                for (var i=0;i<(j-1);i++){
                	level[i]=maxexcept*(i+1);
                }
                level[j-1]=max;
                if(ext!=null){
                    var tmp=ext.split(";");
                    for(var i=0;i<tmp.length;i++){
                        var value=tmp[i].split("=");
                        if(value[0]=="max"){
                            max=parseInt(value[1]);
                        }else if(value[0]=="level"){
                            level=$.parseJSON(value[1]);
                        }
                    }
                }
                myChart.setOption(packdata.getMonitorGaugeOption(data,[ '#91C7AE', '#63869E','#C23531'],max,min,level));
            });
        }
        
        //仪表盘_NTPD
        function loadNtpd(chartid,url,ext) {
            var data=[{"mark":"时差","value":"0"}];
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
                if(resultdata[0].data!=null&&resultdata[0].data.length!=0){
                    data=resultdata[0].data;
                }
                var myChart = ec.init(document.getElementById(chartid));
                var max=10,min=-10;
                myChart.setOption(packdata.getMonitorGaugeOption(data,['#C23531', '#63869E','#C23531'],max,min,null));
            });
        }
        
        //正常异常(yncu日志告警标识)
        function loadSwitch(chartid,url,ext) {
            var data=[{"mark":"日志告警标识","value":"1"}];
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
                if(resultdata[0].data!=null&&resultdata[0].data.length!=0){
                    data=resultdata[0].data;
                }
                var img;
                if(Number(data[0].value)==0){
                    img = "<img src=\"/images/right.png\">"
                }else{
                    //日志异常，显示告警 0
                    img = "<img src=\"/images/error.png\">"
                }
                $("#"+chartid).html(img);
            });
        }
        
        //垂直柱状图
        function loadVerticalbar(chartid,url){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid));
            	var option = packdata.getVerticalbarOption("",resultdata,true);
                myChart.setOption(option);
            });
        }
        
        //横向柱状图
        function loadHorizontalbar(chartid,url){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid));
            	var option = packdata.getAnaHorizontalBar("",resultdata,true);
                myChart.setOption(option);
            });
        }
        
        //业务分析页面垂直柱状图
        function loadAnaVerticalbar(chartid,url,tenthousand){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid));
            	var option = packdata.getAnaVerticalBar("",resultdata,true,"",tenthousand);
                myChart.setOption(option);
            });
        }
        
        function loadGrap(chartid,url){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var data = null;
                if(resultdata[0].data==null || resultdata[0].data.length==0){
                    data = [{"mark":"default","value":"-1"}];
                }else{
                    data = resultdata[0].data;
                }
                var myChart = ec.init(document.getElementById(chartid));
                var option = packdata.getRelationshipGrap("",data,true);
                myChart.setOption(option);
            });
        }
        
        //两列动态表格
        function loadTable(chartid,url){
        	$.getJSON(url+"&random="+Math.random(), function(resultdata) {
        		var resultdataremovalhead=new Array()
        		$.each(resultdata , function(q, x) {
        			if(q!=0){
        				resultdataremovalhead[q-1]=x;
        			}
        		});
                	var table=[];
                        table.push("<table class='omc_city_tab2 table table-bordered table-striped table-head-bordered table-hover  center ' cellpadding='0' cellspacing='0' >");
                        table.push("<thead>");
                        table.push("<tr>");
                        $.each(resultdata[0].data , function(z, r) {
                             table.push("<th>"+r.value+"</th>");
                        });
                        table.push("</tr>");
                        table.push("</thead>");
                        table.push("<tbody id='tbodydiv"+chartid+"'>");
                        table.push("</tbody>");
                        table.push("</table>");
                        $("#"+chartid).empty().append(table.join(""));
                        if(resultdataremovalhead!=null && resultdataremovalhead.length > 0){
                            var total = resultdataremovalhead.length;
                            var rowdata = "";
                            for(var j=0;j<total;j++){
                                var rowninfo = resultdataremovalhead[j].data;
                                rowdata = rowdata + "<tr>";
                                $.each(rowninfo, function(i, row) {
                                    rowdata = rowdata + "<td title = '"+row.value+"'>"+row.value+"</td>";
                                });
                                rowdata = rowdata + "</tr>";
                            }
                            $("#tbodydiv"+chartid).empty().append(rowdata);
                            if(total<=5) {
                                $("#serviceinfo_"+chartid).removeClass("boxh540");
                                $("#serviceinfo_"+chartid).addClass("boxh265");
                            }else{
                                $("#serviceinfo_"+chartid).removeClass("boxh265");
                                $("#serviceinfo_"+chartid).addClass("boxh540");
                                $.getScript("/js/lcims/home/jquery.mCustomScrollbar.concat.min.js",function(){
                                    $("#tbodydiv"+chartid).mCustomScrollbar({
                                        scrollButtons: {
                                            enable: true
                                        },
                                        theme:"dark-2"
                                    });
                                });
                                
                            }
                        }else{
                            $("#tbodydiv"+chartid).empty().append("<p id = 'nodata"+chartid+"'>暂无数据</p>");
                        }
            });
        }
        
        function loadHostinfo(chartid,url){
            $.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var hostDiv=[];
                hostDiv.push("<div class='fwqinfo clearfix'>");
                hostDiv.push("<div class='fl'><img src='/images/fwq.png' width='80'></div>");
                hostDiv.push("<div class='ml50 fl'>");
                
                hostDiv.push("<div class='c666'>主机名称：<b>"+resultdata.hostname+"</b></div>");
                hostDiv.push("<div class='c666'>主机IP：<b>"+resultdata.addr+"</b></div>");
                hostDiv.push("<div class='c666'>主机类型：<b>"+resultdata.hosttypename+"</b></div>");
                hostDiv.push("<div class='c666'>操作系统：<b>"+getHostInfoStr(resultdata.os)+"</b></div>");
                hostDiv.push("<div class='c666'>主机CPU：<b>"+getHostInfoStr(resultdata.cpu)+"</b></div>");
                hostDiv.push("<div class='c666'>主机内存：<b>"+getHostInfoStr(resultdata.memory)+"</b></div>");
                hostDiv.push("</div>");
                hostDiv.push("</div>");
                $("#"+chartid).empty().append(hostDiv.join(""));
            });
        }
        
         function getHostInfoStr(value){
            if(value==null || value=="" || value == "null"){
                return "未知";
            }
            return value;
        }
        
    }
);
