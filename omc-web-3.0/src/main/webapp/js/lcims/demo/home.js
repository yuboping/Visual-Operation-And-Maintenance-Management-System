require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        'lcims': "/js/lcims",
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'checkbox': '/js/lcims/tool/checkbox',
        'echarts': "/js/echarts",
        'config':"/js/echarts/config",
        'packdata':'/js/lcims/tool/packdata',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'echarts3': "/js/echarts3/echarts.min",
        'stringutil': '/js/lcims/tool/stringutil',
        'js_scroll':'/js/scroll/jquery.mousewheel.min'
    }
});


require(['jquery',
         'layer',
         'laypage',
         'checkbox',
         'packdata',
         'stringutil',
      'echarts',
      'config',
      'resizewh',
      "echarts3",
      'echarts/chart/bar',
      'echarts/chart/line',
      'echarts/chart/gauge',
      'echarts/chart/pie',
      'echarts/chart/map',
      'echarts/chart/tree'
      ],function($,layer,laypage,checkbox,packdata,stringutil,ec,config,resizewh,echarts) {
        var layer_load;
		var radiusprocessdata; 
	    var radiusanalyticdata; 
	    var hostnetconnectabledata;
	    var hoststatedata;
	    var query_level_name;
		var province = $("#province").val();
        loadData();
        setInterval(loadData,1000*60*5);
        $("body").css("overflow","auto");

        resizewh.resizeBodyH($("#homemain"));
    	/*窗口变化时重新设置高度宽度*/
        /*------------------------------------------------以下为自定义方法---------------------------------------------------*/
        function loadData() {
        	switch(province){
        	case "ahcm":
        		loadMap("area1","/data/monitor/class/city/module/2/item/00/usersonlinenums/homeMap","安徽",
            			"area6","/data/monitor/class/home/Radius");
            	loadLine("area2","/data/monitor/class/service/module/7/item/node/authrequestnum",2);
            	loadRosePie("area3","/data/monitor/class/service/module/7/item/node/authfialnum");
            	loadOnlineLine("area4","/data/monitor/class/city/module/2/item/00/usersonlinenums/byHour",4);
            	loadOnlineLine("area5","/data/monitor/class/service/module/7/item/node/authsuccrate/byHour");
        	  break;
        	case "demo":
                loadOptRecord();
                loadAlarmStatisBarGraph("area6");
        		loadRadiusData();
        		loadRadiusProcessPie("area1",radiusprocessdata.chartInfoList);
            	loadRadiusAnalyticLine("area7",radiusanalyticdata.authenChartInfoList);
            	loadRadiusAnalyticOnlineLine("area3",radiusanalyticdata.authenSuccessRateChartInfoList);
            	loadRadiusAnalyticOnlineLine("area2",radiusanalyticdata.requestsChartInfoList);
            	loadHostNetConnectablePie("area5",hostnetconnectabledata.chartInfoList);
            	loadHostState("area4",hoststatedata.chartInfoList);
            	$("#area1").click(function() {
                	loadRadiusProcessPieShow();
                });
            	$("#area5").click(function() {
            		loadHostNetConnectPieShow();
                });
            	$("#area4").click(function() {
            		loadHostStateShow();
                });

        	  break;
        	default:
        		loadMap("area1","/data/monitor/class/netdevice/module/20/item/00/useronline/homeMap","广东",
            			"area6","/data/monitor/class/home/Radius");
            	loadLine("area2","/data/monitor/class/city/module/16/item/00/authinfo",2);
            	loadRosePie("area3","/data/monitor/class/city/module/16/item/00/authfailreasonnum");
            	loadOnlineLine("area4","/data/monitor/class/netdevice/module/20/item/00/useronline/byHour",4);
            	loadOnlineLine("area5","/data/monitor/class/city/module/16/item/00/authsuccrate/byHour");
        	}
        };
        
        function loadRadiusData() {
        	$.ajaxSettings.async = false;
        	$.getJSON("/data/home/radius/radiusprocess?random="+Math.random(), function(resultdata) {
        		radiusprocessdata = resultdata;
            });
        	$.getJSON("/data/home/radius/radiusanalytic?random="+Math.random(), function(resultdata) {
        		radiusanalyticdata = resultdata;
            });
        	$.getJSON("/data/home/host/netconnectable?random="+Math.random(), function(resultdata) {
        		hostnetconnectabledata = resultdata;
            });
        	$.getJSON("/data/home/host/state?random="+Math.random(), function(resultdata) {
        		hoststatedata = resultdata;
            });
        };

        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','hisalarmdiv');
        });

        $(".detail_ok").click(function() {
            layer.closeAll();
        });


        $("#detail_hisalarm_ok").click(function() {
            layer.closeAll();
        });

        $("#confirm_button").click(function() {
            confirmShow();
        });

        $("#export_button").click(function() {
            exportShow();
        });
        
        //radius解析量数据折线图
        function loadRadiusAnalyticLine(chartid,resultdata,cycle,chartname){
        	var myChart = ec.init(document.getElementById(chartid));
        	var linestyle = [{"width":2,"color":"rgb(0,0,250)"},{"width":2,"color":"rgb(255,0,0)"},{"width":2,"color":"rgb(0,255,0)"}];
        	var areacolor = ["rgba(28,125,250,0.2)","rgba(255,58,47,0.6)","rgba(83,215,105,0.4)"];
            var option = packdata.getHomeLineOption(resultdata,areacolor,cycle,linestyle);
            if(chartname!=null && chartname=="online"){
            	option.grid.x=70;
            	option.legend.show=false;
            }
            myChart.setOption(option);
        };
        
        //认证成功率数据折线图
        function loadRadiusAnalyticOnlineLine(chartid,resultdata,cycle){
        	var myChart = ec.init(document.getElementById(chartid));
            var linestyle = [{"width":2,"color":"rgb(0,255,0)"}];
            var areacolor = ["rgba(0,0,0,0)"];
            var option = packdata.getHomeLineOption(resultdata,areacolor,cycle,linestyle);
        	option.grid.x=70;
        	option.legend.show=false;
            myChart.setOption(option);
        };
        
        /*------------------------------------------------radius进程状态start---------------------------------------------------*/
        
        //radius进程状态南丁格尔玫瑰图
        function loadRadiusProcessPie(chartid,resultdata){
        	if(resultdata[0].group == "0"){
        		$("#"+chartid).text("没有被检测的radius进程!")
        		.css('color','#C94040')
        		.css('font-size','20px')
        		.css('text-align','center')
        		.css('padding-top','100px')
        		.css('font-weight','bold');
        	}else{
        		var myChart = ec.init(document.getElementById(chartid),"red");
                var itemcolor = "#00d5ff";
                var option = packdata.getHomePieOption(resultdata,itemcolor);
                myChart.setOption(option);
        	}
        }
        
        //radius进程状态饼图按钮事件
        function loadRadiusProcessPieShow(){
        	if(radiusprocessdata.radiusProcessData.length>0){
        		detailResetPage(radiusprocessdata.radiusProcessData);
                showLayerDetail("detail_div",'radius进程状态');
            }
        }
        
        //radius进程状态详情重置分页(跳转分页)
        function detailResetPage(result) {
        	//分页显示的初始化数据
            var pagecount=0;
            var page_count = 10;
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
                    detailShowTable(result,startnum,endnum);
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#detail_div"));
                },
                groups: page_count //连续显示分页数
            });
        }
        
        // radius进程状态详情拼接tr
        function detailShowTable(data,startnum,endnum){
            var rowdata = "";
            for(var i=0;i<=endnum-startnum;i++){
                var rowninfo = data[i];
                    rowdata = rowdata + "<tr>"
                    +"<td class='over_ellipsis' style='max-width:80px' title=\""+rowninfo.ADDR+"\">"+rowninfo.ADDR+"</td>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+rowninfo.HOSTNAME+"\">"+rowninfo.HOSTNAME+"</td>"
                    +"<td class='over_ellipsis' style='max-width:200px' title=\""+rowninfo.processMessage+"\">"+rowninfo.processMessage+"</td>"
                    +"</tr>";
            }
            $("#detaildiv").empty().append(rowdata);
        }
        
        //radius进程状态饼图弹出事件
        function showLayerDetail(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '620px', '450px' ],
                content : $("#"+divid)
            });
            $("#"+divid).height(380);
        }
        
        /*------------------------------------------------radius进程状态end---------------------------------------------------*/
        
        /*------------------------------------------------主机连通性start---------------------------------------------------*/
        
        //主机连通性南丁格尔玫瑰图
        function loadHostNetConnectablePie(chartid,resultdata){
        	var myChart = ec.init(document.getElementById(chartid),"red");
        	var itemcolor = "#ffcc00";
            var option = packdata.getHomePieOption(resultdata,itemcolor);
            myChart.setOption(option);
        }
        
        //主机连通性南丁格尔玫瑰图按钮事件
        function loadHostNetConnectPieShow(){
        	if(hostnetconnectabledata.hostNetConnectableList.length>0){
        		detailResetPageHostNetConnect(hostnetconnectabledata.hostNetConnectableList);
        		showLayerDetailHostNetConnect("detail_div_connectable",'主机连通性');
            }
        }
        
        //主机连通性详情重置分页(跳转分页)
        function detailResetPageHostNetConnect(result) {
        	//分页显示的初始化数据
            var pagecount=0;
            var page_count = 10;
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#querynum_connectable").text(total);
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
                    detailShowTableHostNetConnect(result,startnum,endnum);
                    $("#currnum_connectable").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#currnum_connectable").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#detail_div_connectable"));
                },
                groups: page_count //连续显示分页数
            });
        }
        
        // 主机连通性详情拼接tr
        function detailShowTableHostNetConnect(data,startnum,endnum){
            var rowdata = "";
            for(var i=0;i<=endnum-startnum;i++){
                var rowninfo = data[i];
                    rowdata = rowdata + "<tr>"
                    +"<td class='over_ellipsis' style='max-width:80px' title=\""+rowninfo.ATTR1+"\">"+rowninfo.ATTR1+"</td>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.HOSTNAME)+"\">"+stringutil.isNull(rowninfo.HOSTNAME)+"</td>"
                    +"<td class='over_ellipsis' style='max-width:200px' title=\""+rowninfo.message+"\">"+rowninfo.message+"</td>"
                    +"</tr>";
            }
            $("#detaildiv_connectable").empty().append(rowdata);
        }
        
        //主机连通性南丁格尔玫瑰图弹出事件
        function showLayerDetailHostNetConnect(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '620px', '450px' ],
                content : $("#"+divid)
            });
            $("#"+divid).height(380);
        }
        
        /*------------------------------------------------主机连通性end---------------------------------------------------*/
        
        /*------------------------------------------------主机状态start---------------------------------------------------*/
        
        //主机状态堆积柱状图
        function loadHostState(chartid,resultdata){
        	var myChart = ec.init(document.getElementById(chartid),"red");
            var precolor = ["#7CFC00","#C94040"];
            var option = packdata.getHomeCumulateBarOption("主机状态",resultdata,precolor);
            myChart.setOption(option);
        }
        
        //主机状态堆积柱状图按钮事件
        function loadHostStateShow(){
        	if(hoststatedata.hostStateAllHostList.length>0){
        		detailResetPageHostState(hoststatedata.hostStateAllHostList);
        		showLayerDetailHostState("detail_div_hoststate",'主机状态');
            }
        }
        
        //主机状态详情重置分页(跳转分页)
        function detailResetPageHostState(result) {
        	//分页显示的初始化数据
            var pagecount=0;
            var page_count = 10;
            total = result.length;
            pagecount=Math.ceil(total/page_count);
            $("#querynum_hoststate").text(total);
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
                    detailShowTableHostState(result,startnum,endnum);
                    $("#currnum_hoststate").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#currnum_hoststate").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#detail_div_hoststate"));
                },
                groups: page_count //连续显示分页数
            });
        }
        
        // 主机状态详情拼接tr
        function detailShowTableHostState(data,startnum,endnum){
            var rowdata = "";
            for(var i=0;i<=endnum-startnum;i++){
                var rowninfo = data[i];
                    rowdata = rowdata + "<tr>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+rowninfo.ADDR+"\">"+rowninfo.ADDR+"</td>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.HOSTNAME)+"\">"+stringutil.isNull(rowninfo.HOSTNAME)+"</td>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.cpuOccupancyrateValue)+"\">"+stringutil.isNull(rowninfo.cpuOccupancyrateValue)+"</td>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.memoryOccupancyrateValue)+"\">"+stringutil.isNull(rowninfo.memoryOccupancyrateValue)+"</td>"
                    +"<td class='over_ellipsis' style='max-width:100px' title=\""+stringutil.isNull(rowninfo.diskBusyRateValue)+"\">"+stringutil.isNull(rowninfo.diskBusyRateValue)+"</td>"
                    +"<td class='over_ellipsis' style='max-width:400px' title=\""+rowninfo.message+"\">"+rowninfo.message+"</td>"
                    +"</tr>";
            }
            $("#detaildiv_hoststate").empty().append(rowdata);
        }
        
        //主机状态堆积柱状图弹出事件
        function showLayerDetailHostState(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '1000px', '450px' ],
                content : $("#"+divid)
            });
            $("#"+divid).height(380);
        }
        
        /*------------------------------------------------主机状态end---------------------------------------------------*/
        
        //圆环图
        function loadCircle(chartid,url){
            var data=[{"mark":"成功率","value":"0"}];
            $.getJSON(url+"?random="+Math.random(), function(resultdata) {
            	if(resultdata.data[0].data!=null&&resultdata.data[0].data.length!=0){
                    data=resultdata.data[0].data;
                }
                var myChart = ec.init(document.getElementById(chartid));
                var option = packdata.getMonitorCircleOption(resultdata.data[0].title,data,["rgba(0,0,255,0.7)"],"#fff");
                
                //首页显示特殊处理
                option.title.show=true;
                option.series[0].radius=["50%","70%"];
                option.series[0].data[0].itemStyle.normal.color='rgba(255,255,255,0.2)';
                myChart.setOption(option);
            });
        }

        
      //省份热力地图
        function loadMap(chartid,url,provincename,chartid2,url2,provicename2){
        	if(province == "ahcm"){
        		loadAnaMapByAhcm(chartid,url,provincename);
        	}else{
        		loadAnaMap(chartid,url,provincename);
        	}
        	loadRelation(chartid2,url2);
        }
        
        function loadAnaMap(chartid,url,provincename){
        	$.getJSON(url+"?random="+Math.random(), function(resultdata) {
            	var myChart = ec.init(document.getElementById(chartid));
            	var option = packdata.getAnaMapOption(provincename,resultdata.data,
            			["rgba(250,119,237)","rgb(240,249,30)","rgb(46,244,11)","rgb(143,209,232)"]);
            	myChart.setOption(option);
            });
        }	
        
        function loadAnaMapByAhcm(chartid,url,provincename){
        	$.getJSON(url+"?random="+Math.random(), function(resultdata) {
            	var myChart = echarts.init(document.getElementById(chartid));
            	var option = packdata.getAnaMapOptionNew(provincename,resultdata.data);
            	$.get('/js/echarts3/map/anhui.json', function (geoJson) {
            		echarts.registerMap('安徽', geoJson);
            		myChart.setOption(option);
            	});
            });
        }
        
        function loadRelation(chartid,url){
        	$.getJSON(url+"?random="+Math.random(), function(resultdata) {
            	var myChart = ec.init(document.getElementById(chartid));
            	var option;
            	if(province == "ahcm"){
            		option = packdata.getRelationshipMapNew(resultdata.data);
            	}else{
            		option = packdata.getRelationshipMap(resultdata.data);
            	}
            	myChart.setOption(option);
//            	console.log(JSON.stringify(option));
            });
        }
        
        //折线图
        function loadLine(chartid,url,cycle,chartname){
            $.getJSON(url+"?random="+Math.random(), function(resultdata) {
            	var myChart = ec.init(document.getElementById(chartid));
            	
            	var linestyle = [{"width":2,"color":"rgb(0,0,250)"},{"width":2,"color":"rgb(255,0,0)"},{"width":2,"color":"rgb(0,255,0)"}];
            	var areacolor = ["rgba(28,125,250,0.2)","rgba(255,58,47,0.6)","rgba(83,215,105,0.4)"];
                var option = packdata.getHomeLineOption(resultdata.data,areacolor,cycle,linestyle);
                if(chartname!=null && chartname=="online"){
                	option.grid.x=70;
                	option.legend.show=false;
                }
                myChart.setOption(option);
            });
        };
        //折线图
        function loadOnlineLine(chartid,url,cycle){
            $.getJSON(url+"?random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid));
                
                var linestyle = [{"width":2,"color":"rgb(0,255,0)"}];
                var areacolor = ["rgba(0,0,0,0)"];
                var option = packdata.getHomeLineOption(resultdata.data,areacolor,cycle,linestyle);
            	option.grid.x=70;
            	option.legend.show=false;
                myChart.setOption(option);
            });
        };
        
       //饼图
        function loadRosePie(chartid,url){
            $.getJSON(url+"?random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid),"red");
                var precolor = ["#87CEEB","#7CFC00","#7AC5CD","#7FFFD4","#C1FFC1","#CAFF70","#3CB371","#48D1CC","#EE0000"];
//                var precolor = ["#5a0602","#88110c","#a01e18","#c02d26","#cc443e","#cc615c","#da1007","#f8170d","#f94b43"];
                var option = packdata.getRosePieOption("",resultdata.data,precolor);
                myChart.setOption(option);
            });
        }

    //加载查询内容
    function loadOptRecord() {
        var data = {};

        $.getJSON("/view/class/system/alarmquery/querywithindex?random=" + Math.random(), data, function (result) {
            var total = result.length;
            showTable(result,total);
        });
    }

    //拼接tr
    function showTable(data,total){
        var rowdata = "";
        if(total > 0){
            for(var i=1;i<=total;i++){
                var rowninfo = data[i-1];

                rowdata = rowdata + "<tr><td title=\"" + rowninfo.name + "\">" + rowninfo.name + "</td><td title=\"" + rowninfo.metric_name + "\">"
                    + rowninfo.metric_name + "</td><td title=\"" + rowninfo.alarm_level_name + "\">" + rowninfo.alarm_level_name + "</td><td title=\"" + rowninfo.dimension_name + "\">" + rowninfo.dimension_name + "</td><td title=\"" + rowninfo.alarmmsg + "\">"
                    + rowninfo.alarmmsg + "</td><td>" + rowninfo.alarm_num  + "</td></tr>";
            }
        }else {
            rowdata = "暂无数据";
        }
        $("#alarmhistorydiv").empty().append(rowdata);
        $.getScript("/js/lcims/home/jquery.mCustomScrollbar.concat.min.js",function(){
            $("#alarmhistorydiv").mCustomScrollbar({
                scrollButtons: {
                    enable: true
                },
                theme:"dark-2"
            });
        });
    }

    //告警信息折线图
    function loadAlarmStatisBarGraph(chartid){
        var myChart = ec.init(document.getElementById(chartid));

        $.getJSON("/view/class/system/alarmquery/query/alarmgraph?random="+Math.random(), function(resultdata) {
            var option = {
                title : {
                    text: '告警信息统计图',
                    x: 'left',
                    align: 'right',
                    textStyle:{ //设置主标题风格
                    	fontSize:14,
                        color:'#FFFFFF'//设置主标题字体颜色
                    }
                },tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: function (params) {
                        var tar;
                        tar = params[0];
                        return tar.name + ":" + tar.value;
                    }
                },
                xAxis: {
                    type: 'category',
                    data: resultdata.barXdata,
                    axisLabel: {
                        show: true,
                        textStyle:{
                        	color: '#FFFFFF'
                        },
                    }
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#FFFFFF'
                        }
                    }
                },
                series: [{
                    type: 'bar',
                    stack: '总量',
                    itemStyle: {
                        normal: {
                            color: function(params) {
                                var colorList = ['#64BD3D','#EFE42A','#C33531'];
                                return colorList[params.dataIndex]
                            },
                            label: {
                                show: true, //开启显示
                                position: 'top', //在上方显示
                                formatter:function(params) {
                                        if (params.value > 0) {
                                            return params.value;
                                        } else {
                                            return '';
                                        }
                                    },
                                textStyle: { //数值样式
                                    color: 'white',
                                    fontSize: 16
                                }
                            }
                        }
                    },
                    data: resultdata.barYdata
                }]
            };
            myChart.setOption(option);
        });

        myChart.on('click', function (params) {
            alarmDetailShow(params.name);
        });
    }

    // 详情按钮事件
    function alarmDetailShow(id){
        reset("detail_div_hisalarm");

        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();

        loadHisAlarmList(id);
        showLayer("detail_div_hisalarm",'告警历史');
    }

    function loadHisAlarmList(id){
        var data = {alarm_level:id};
        query_level_name = id;
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 10;
        $.ajax({
            type: "post",
            url: "/view/class/system/alarmquery/querywithgraphlist?random=" + Math.random(),
            data: data,
            dataType: "json",
            cache: false,
            async: false,
            success: function (result) {
                layer.close(layer_load);
                total = result.length;
                pagecount = Math.ceil(total / page_count);
                $("#hisquerynum").text(total);
                laypage({
                    cont: 'hispageinfo',
                    skin: '#6AB0F4',
                    pages: pagecount,
                    curr: 1,
                    skip: false, // 是否开启跳页
                    jump: function (obj, first) { // 触发分页后的回调
                        startnum = (obj.curr - 1) * page_count + 1;
                        endnum = obj.curr * page_count;
                        endnum = endnum > total ? total : endnum;
                        showHisTable(result, startnum, endnum);
                        $("#hiscurrnum").text(startnum + "-" + endnum);
                        if (total == 0) {
                            $("#hiscurrnum").empty().text("0 ");
                        }
                    },
                    groups: page_count
                });
            }
        });

    }

    // 拼接tr
    function showHisTable(data, startnum, endnum) {
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];

            var id = rowninfo.alarm_id;

            var disabled="disabled=\"disabled\"";
            var checked = "checked=\"checked\"";
            if(rowninfo.confirm_state != 1){
                disabled = "";
                checked = "";
            }
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
                rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.alarm_id+"\" id=\""+rowninfo.alarm_id+"\" "+checked+" "+disabled+" />"
                + "<td title=\""+rowninfo.name+"\">"+rowninfo.name+"</td><td title=\""+rowninfo.metric_name+"\">"
                +rowninfo.metric_name+"</td><td title=\""+rowninfo.alarm_level_name+"\">"+rowninfo.alarm_level_name+"</td><td title=\""+rowninfo.dimension_name+"\">"+rowninfo.dimension_name+"</td><td title=\""+rowninfo.alarmmsg+"\">"
                +rowninfo.alarmmsg+"</td><td>"+rowninfo.alarm_num+"</a>"+"</td><td title=\""+rowninfo.first_time+"\">"
                +rowninfo.first_time+"</td><td title=\""+rowninfo.last_time+"\">"+rowninfo.last_time+"</td><td>"
                +rowninfo.confirm_name+"</td><td title=\""+rowninfo.confirm_time+"\">"+rowninfo.confirm_time+"</td></tr>";
        }
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }

        //本页条数
        $("#hisalarmdiv").empty().append(rowdata);
        checkbox.bindSingleCheckbox("hisalarmdiv");

    }

    //确认告警信息
    function confirmShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个告警信息!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            layer.confirm('是否确认该批次数据？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] // 按钮
            },function(){
                loadingwait();
                var data = {alarmArray:checkboxArray};
                var url = "/view/class/system/alarmquery/confirmAlarm?random=" + Math.random();
                $.getJSON(url,data,function(result){
                    // layerResultAndReload(result);
                    loadHisAlarmList(query_level_name);
                    layer.alert(result.message);
                })
            });
        }
    }

    //导出Excel
    function exportShow(){
        downloadExcel();
    }

    function downloadExcel(){
        var alarm_level = query_level_name;

        var url = "/view/class/mainttool/alarmhisquery/exportwithlevel?alarm_level="+alarm_level;

        window.open(url+"&random=" + Math.random(),"_blank");
    }

    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '900px', '595px' ],
            content : $("#"+divid)
        });
    }

    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
        });
    }

    function layerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
            loadOptRecord();
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

    }
);
