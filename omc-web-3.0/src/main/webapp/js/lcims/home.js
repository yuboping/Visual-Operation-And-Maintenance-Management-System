require.config({
    paths: {
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'config':"/js/echarts/config",
        'jquery': '/js/jquery/jquery.min',
        'packdata':'/js/lcims/tool/packdata',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'echarts3': "/js/echarts3/echarts.min",
    }
});


require(['jquery', "packdata",
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
      ],function($, packdata,ec,config,resizewh,echarts) {
		var province = $("#province").val();
        loadData();
        setInterval(loadData,1000*60*5);
        resizewh.resizeWH($("#homemain"));
    	/*窗口变化时重新设置高度宽度*/
        /*------------------------------------------------以下为自定义方法---------------------------------------------------*/
        function loadData() {
        	if(province == "ahcm"){
        		loadMap("area1","/data/monitor/class/city/module/2/item/00/usersonlinenums/homeMap","安徽",
            			"area6","/data/monitor/class/home/Radius");
            	loadLine("area2","/data/monitor/class/service/module/7/item/node/authrequestnum",2);
            	loadRosePie("area3","/data/monitor/class/service/module/7/item/node/authfialnum");
            	loadOnlineLine("area4","/data/monitor/class/city/module/2/item/00/usersonlinenums/byHour",4);
            	loadOnlineLine("area5","/data/monitor/class/service/module/7/item/node/authsuccrate/byHour");
        	}else{
        		loadMap("area1","/data/monitor/class/netdevice/module/20/item/00/useronline/homeMap","广东",
            			"area6","/data/monitor/class/home/Radius");
            	loadLine("area2","/data/monitor/class/city/module/16/item/00/authinfo",2);
            	loadRosePie("area3","/data/monitor/class/city/module/16/item/00/authfailreasonnum");
            	loadOnlineLine("area4","/data/monitor/class/netdevice/module/20/item/00/useronline/byHour",4);
            	loadOnlineLine("area5","/data/monitor/class/city/module/16/item/00/authsuccrate/byHour");
        	}
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
                var precolor = ["#5a0602","#88110c","#a01e18","#c02d26","#cc443e","#cc615c","#da1007","#f8170d","#f94b43"];
//                var precolor = ["#5a0602","#88110c","#a01e18","#c02d26","#cc443e","#cc615c","#da1007","#f8170d","#f94b43"];
                var option = packdata.getRosePieOption("",resultdata.data,precolor);
                myChart.setOption(option);
            });
        }
    }
);
