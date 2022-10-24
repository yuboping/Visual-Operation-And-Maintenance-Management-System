require.config({
    paths: {
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'config':"/js/echarts/config",
        'jquery': '/js/jquery/jquery.min',
        'packdata':'/js/lcims/tool/packdata',
//        'resizewh':'/js/lcims/resizewh/resizewh'
    }
});


require(['jquery', "packdata",
      'echarts',
      'config',
//      'resizewh',
      'echarts/chart/bar',
      'echarts/chart/line',
      'echarts/chart/gauge',
      'echarts/chart/pie',
      'echarts/chart/map',
      'echarts/chart/tree'
      ],function($, packdata,ec,config) {
		var province = $("#province").val();
        loadData();
        setInterval(loadData,1000*60*5);
//        resizewh.resizeWH($("#homemain"));
    	/*窗口变化时重新设置高度宽度*/
        /*------------------------------------------------以下为自定义方法---------------------------------------------------*/
        function loadData() {
                //各地市在线用户数
                loadMap("area1","/data/monitor/getOnlineDataHomeMap?chart_name=area_online_rate","安徽",
                        "area6","/data/monitor/getRadiusHost");
                //认证总量
                loadLine("area2","/data/monitor/getDataLine?chart_name=radius_auth_all",2);
                //各失败原因占比
                loadRosePie("area3","/data/monitor/getDataRecent?chart_name=radius_auth_fail_reason_rate_total");
                //当前告警数汇总
                loadBarGraph("area4","/view/class/system/alarmquery/query/alarmgraph");
                //认证成功率
                loadOnlineLine("area5","/data/monitor/getDataLine?chart_name=allradius_summary_authen_succrate",2);
        };
        
      	//告警信息柱状图
	    function loadBarGraph(chartid,url){
	        var myChart = ec.init(document.getElementById(chartid));
	
	        $.getJSON(url+"?random="+Math.random(), function(resultdata) {
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
	    }
        
      	//省份热力地图
        function loadMap(chartid,url,provincename,chartid2,url2,provicename2){
            loadAnaMapNMG(chartid,url,provincename);
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
        
        function loadAnaMapNMG(chartid,url,provincename){
           $.getJSON(url+"&random="+Math.random(), function(resultdata) {
                $.get('/js/echarts/util/mapData/rawData/geoJson/an_hui_geo.json', function(geoJson) {
                    var myChart = ec.init(document.getElementById(chartid));
                    var features;
                    try {
                      var geoJsonObj = eval('(' + geoJson + ')');
                      features = geoJsonObj.features;
                     }
                     catch(err){
                         features = geoJson.features;
                     }
                    var geoCoordMap = {};
                    for(var i=0;i<features.length;i++){
                        geoCoordMap[features[i].properties.name]= features[i].properties.cp;
                    }
                    var option = packdata.getAnaMapOption22(provincename,resultdata.data,
                            ["rgba(250,119,237)","rgb(240,249,30)","rgb(46,244,11)","rgb(143,209,232)"],geoCoordMap);
                    myChart.setOption(option);
                });
            });
        }
        
        function loadRelation(chartid,url){
        	$.getJSON(url+"?random="+Math.random(), function(resultdata) {
            	var myChart = ec.init(document.getElementById(chartid));
            	var option;
            	option = packdata.getRelationshipMap(resultdata.data);
            	myChart.setOption(option);
            });
        }
        
        //折线图 loadLine(chartid,cycle,url,true);
        function loadLine(chartid,url,cycle,chartname){
            $.getJSON(url+"&random="+Math.random(), function(resultdata) {
            	var myChart = ec.init(document.getElementById(chartid));
            	
            	var linestyle = [{"width":2,"color":"rgb(0,0,250)"},{"width":2,"color":"rgb(255,0,0)"},{"width":2,"color":"rgb(0,255,0)"}];
            	var areacolor = ["rgba(28,125,250,0.2)","rgba(255,58,47,0.6)","rgba(83,215,105,0.4)"];
                var option = packdata.getHomeLineOption(resultdata,areacolor,cycle,linestyle);
                if(chartname!=null && chartname=="online"){
                	option.grid.x=70;
                	option.legend.show=false;
                }
                myChart.setOption(option);
            });
        };
        
        //折线图
        function loadOnlineLine(chartid,url,cycle){
            $.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid));
                
                var linestyle = [{"width":2,"color":"rgb(0,255,0)"}];
                var areacolor = ["rgba(0,0,0,0)"];
                var option = packdata.getHomeLineOption(resultdata,areacolor,cycle,linestyle);
            	option.grid.x=70;
            	option.legend.show=false;
                myChart.setOption(option);
            });
        };
        
        //饼图
        function loadRosePie(chartid,url){
            $.getJSON(url+"&random="+Math.random(), function(resultdata) {
                var myChart = ec.init(document.getElementById(chartid),"red");
                var precolor = ["#5a0602","#88110c","#a01e18","#c02d26","#cc443e","#cc615c","#da1007","#f8170d","#f94b43"];
                var option = packdata.getRosePieOption("",resultdata,precolor);
                myChart.setOption(option);
            });
        }
    }
);
