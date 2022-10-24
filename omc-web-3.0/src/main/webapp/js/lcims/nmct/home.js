require.config({
    paths: {
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'config':"/js/echarts/config",
        'jquery': '/js/jquery/jquery.min',
        'packdata':'/js/lcims/tool/packdata',
        'resizewh':'/js/lcims/resizewh/resizewh'
    }
});


require(['jquery', "packdata",
      'echarts',
      'config',
      'resizewh',
      'echarts/chart/bar',
      'echarts/chart/line',
      'echarts/chart/gauge',
      'echarts/chart/pie',
      'echarts/chart/map',
      'echarts/chart/tree'
      ],function($, packdata,ec,config,resizewh) {
		var province = $("#province").val();
        loadData();
        setInterval(loadData,1000*60*5);
        resizewh.resizeWH($("#homemain"));
    	/*窗口变化时重新设置高度宽度*/
        /*------------------------------------------------以下为自定义方法---------------------------------------------------*/
        function loadData() {
                //各地市在线用户数
                loadMap("area1","/data/monitor/getOnlineDataHomeMap?chart_name=area_online_rate","内蒙古",
                        "area6","/data/monitor/class/home/Radius");
                //认证总量
                loadLine("area2","/data/monitor/getDataLine?chart_name=radius_auth_all",2);
                //各失败原因占比
                loadRosePie("area3","/data/monitor/getDataRecent?chart_name=auth_reject_reason_all");
                //在线用户数汇总
                loadOnlineLine("area4","/data/monitor/getDataLine?chart_name=online_total",4);
                //认证成功率
                loadOnlineLine("area5","/data/monitor/getDataLine?chart_name=auth_sussces_rate_all",2);
        	
        };
        
        
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
                loadAnaMapNMG(chartid,url,provincename);
        	//loadRelation(chartid2,url2);
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
//            url = "/js/lcims/nmct/nm_mapdata.json?chart_name=area_online_rate"
           $.getJSON(url+"&random="+Math.random(), function(resultdata) {
                $.get('/js/echarts/util/mapData/rawData/geoJson/nei_meng_gu_geo.json', function (geoJson) {
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
//                    resultdata.data[0].data=[];
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
            	if(province == "ahcm"){
            		option = packdata.getRelationshipMapNew(resultdata.data);
            	}else{
            		option = packdata.getRelationshipMap(resultdata.data);
            	}
            	myChart.setOption(option);
//            	console.log(JSON.stringify(option));
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
//                var precolor = ["#5a0602","#88110c","#a01e18","#c02d26","#cc443e","#cc615c","#da1007","#f8170d","#f94b43"];
                var option = packdata.getRosePieOption("",resultdata,precolor);
                myChart.setOption(option);
            });
        }
    }
);
