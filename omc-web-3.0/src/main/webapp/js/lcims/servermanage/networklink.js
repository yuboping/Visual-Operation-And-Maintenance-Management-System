require.config({
    paths : {
        'lcims' : "/js/lcims",
        'resizewh' : "/js/lcims/resizewh/resizewh",
        'jquery' : '/js/jquery/jquery.min',
        'iscroll' : '/js/lcims/tool/iscroll',
        'layer' : '/js/layer/layer',
        "laypage" : "/js/lcims/tool/laypage",
        'echarts3': "/js/echarts3/echarts.min"
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh',"echarts3" ],
    function($, layer, laypage, resizewh,echarts3) {
	//初始化查询条件 节点信息
	
	/**
	 * 取第一个节点id，生成关系图
	 * 按照以下层级展现
	 *  内网
		路由
		防火墙
		交换机
		服务器
		交换机
		防火墙
		路由
		公网
	 *
	 */
	$("#nodeid").bind("change",function(){
		var nodeid = $("#nodeid").val();
		loadData(nodeid);
    });
	loadData(null);
	//初始节点
	function loadData(nodeid) {
		resizewh.resizeWH($("#networkTiemain"));
		if(nodeid==null || nodeid==''){
			nodeid = $("#nodeid").val();
		}
		var data = {nodeid:nodeid};
		$.getJSON("/view/class/system/network/initdata"+"?random="+Math.random(),data, function(result) {
        	var data = result.data;
        	var myChart = echarts3.init(document.getElementById('networkTiemain'));
        	myChart.clear();
        	// 指定图表的配置项和数据
    		var nodes = [];
    		var links = [];
    		var legend_data = [];
    		var hostinfo = data[0];
    		//公网设备
    		var networkTieinfos = data[3];
    		var deviceinfo = data[2];
    		
    		// 内网设备 路由、交换机、防火墙处理
    		var intranetDeviceinfos = classificationDeviceData(data[1]);
    		var intranetSwapData=intranetDeviceinfos[0];
    		var intranetFirewallData=intranetDeviceinfos[1];
    		var intranetRouterData=intranetDeviceinfos[2];
    		
    		
    		// 公网网 路由、交换机、防火墙处理
    		var publicDeviceinfos = classificationDeviceData(data[2])
    		var publicSwapData=publicDeviceinfos[0];
    		var publicFirewallData=publicDeviceinfos[1];
    		var publicRouterData=publicDeviceinfos[2];
    		
    		
    		var root_x = 0;
    		var root_y = 300;
    		
    		var litter_temp_top = 10;
    		var litter_temp_left = 10;
    		var center_num = Math.round(hostinfo.length/2);
    		for (var i = 0; i < hostinfo.length; i++) {
    		    var y = root_y;
    		    var x = 0;
  			  	x = root_x + litter_temp_left;
  			  	litter_temp_left += 30;
    			nodes.push({
    				x: x,
    				y: y,
    				symbol: 'image:///images/server.jpg',
    				label: {
    					normal: {
    						position: 'bottom',
    						color: 'black'
    					}
    				},
    				itemStyle: {
    					normal: {
    						color: 'blue'
    					}
    				},
    				name: hostinfo[i].hostname,
    				value: hostinfo[i].hostid
    			});
    		}
    		
    		//交换机 防火墙、路由器
    		swap(root_x,root_y,intranetSwapData,nodes,-60);
    		firewall(root_x,root_y,intranetFirewallData,nodes,-80);
    		router(root_x,root_y,intranetRouterData,nodes,-120);
    		
    		swap(root_x,root_y,publicSwapData,nodes,60);
    		firewall(root_x,root_y,publicFirewallData,nodes,80);
    		router(root_x,root_y,publicRouterData,nodes,120);
    		
    		for(var i=0;i<networkTieinfos.length;i++){
    			links.push({
    				source: networkTieinfos[i].sourcename,
    				target: networkTieinfos[i].targetname
    			});
    		}
    		
    		var option = {
    				title: {
    					text: '网络关系图'
    				},
    				tooltip: {
    					trigger: 'item',
    					formatter: "{b}"
    				},
    				legend: {
    					orient: 'vertical',
    					data: []
    				},
    				series: [{
    					type: 'graph',
    					layout: 'none',
    					roam: true,
    					symbolSize: 18,
    					left: 90,
    					label: {
    						normal: {
    							show: true
    						}
    					},
    					data: nodes,
    					links: links,
    					lineStyle: {
    						normal: {
    							opacity: 0.9,
    							width: 1
    						}
    					}
    				}]
    			};
    	        // 使用刚指定的配置项和数据显示图表。
    	        myChart.setOption(option);
        });
	}
	
	function GetRandomNum(Min,Max)
	{   
		var Range = Max - Min;   
		var Rand = Math.random();   
		return(Min + Math.round(Rand * Range));   
	}   
	
	
	function swap(root_x,root_y,swapData,nodes,right){
		var litter_swap_left = 90;
		var y = 0;
		var x = 0;
		for (var i = 0; i < swapData.length; i++) {
			if(i%2==0){
		      var r1 = GetRandomNum(1,10);
			  y = root_y +right + r1;
			}else{
			  var r2 = GetRandomNum(5,20);
			  y = root_y +right+r2;
			}
//			y = root_y+right;
			var image = 'swap.png';
			x = root_x+litter_swap_left;
			addNodesDevice(x,y,image,swapData[i],nodes);
			litter_swap_left += 80;
		}
	}
	
	
	function firewall(root_x,root_y,firewallData,nodes,right){
		var litter_swap_left = 190;
		var y = 0;
		var x = 0;
		for (var i = 0; i < firewallData.length; i++) {
		    var y = root_y +right;
			y = root_y +90 ;
			var image = 'firewall.jpg';
			x = root_x+litter_swap_left;
			addNodesDevice(x,y,image,firewallData[i],nodes);
			litter_swap_left += 40;
		}
	}
	
	function router(root_x,root_y,routerData,nodes,right){
		var litter_swap_top = 10;
		var litter_swap_left = 190;
		var y = 0;
		var x = 0;
		for (var i = 0; i < routerData.length; i++) {
		    y = root_y +right;
			var image = 'router.png';
			x = root_x+litter_swap_left;
			addNodesDevice(x,y,image,routerData[i],nodes);
			litter_swap_left += 50;
		}
	}
	
	function addNodesDevice(x,y,image,device,nodes){
		nodes.push({
			x: x,
			y: y,
			symbol: 'image:///images/'+image,
			label: {
				normal: {
					position: 'bottom',
					color: 'black'
				}
			},
			itemStyle: {
				normal: {
					color: 'red'
				}
			},
			name: device.device_name,
			value: device.device_id
		});
	}
	
	function classificationDeviceData(deviceinfo){
		// 公网网 路由、交换机、防火墙处理
		var routerData=[];
		var swapData=[];
		var firewallData=[];
		for (var i = 0; i < deviceinfo.length; i++) {
			if(deviceinfo[i].type==1){//交换机
				swapData.push(deviceinfo[i]);
			}else if(deviceinfo[i].type==2){//防火墙
				firewallData.push(deviceinfo[i]);
			}else{//路由器
				routerData.push(deviceinfo[i]);
			}
		}
		var data = new Array(swapData,firewallData,routerData);
		return data;
	}
	
});