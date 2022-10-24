require.config({
    paths: {
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'config':"/js/echarts/config",
        'jquery': '/js/jquery/jquery.min',
        'packdata':'/js/lcims/tool/packdata',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'echarts3': "/js/lcims/hncu/echarts.min",
    }
});
require(['jquery', "packdata",
      'echarts',
      'config',
      'resizewh',
      "echarts3"
      ],function($, packdata,ec,config,resizewh,echarts) {
	
	var alarmLevel1=0;
	var alarmLevel2=0;
	var alarmLevel3=0;
	//告警级别数量显示方法
	function alarmLevel(){
		$("#alarmlevel3").html(alarmLevel3);
		$("#alarmlevel2").html(alarmLevel2);
		$("#alarmlevel1").html(alarmLevel1);
	}
	
	//存放所有告警节点
    var alarmnodes=[];
    //告警闪烁方法
    function nodealarmss(){
    	if(alarmnodes==[] || alarmnodes.length==0){
    		return
    	}
    	var obj = {};
    	var alaemnodea = alarmnodes.reduce(function(item, next) {
	       obj[next.nodeId] ? '' : obj[next.nodeId] = true && item.push(next);
	       return item;
	    }, []);
    	$("#alarmhost").html(alaemnodea.length)
    	$.each(alaemnodea,function(i,node){
    		if(node.alarm==null){
    			node.alarm="Warning"
    		}else{
    			node.alarm=null
    		}
    	})
    }
   nodealarmss()
   setInterval(nodealarmss, 900);
	
	//告警信息列表
	function alarmList(){
		alarmLevel1=0;
		alarmLevel2=0;
		alarmLevel3=0;
		var url="/data/home/host/gscm/queryAlarmByAlarmlevel?random="+Math.random()
		$.ajax({
	        url: url,
	        type:"get",
	        dataType: "json",
	        async: true,
	        success: function (data) {
	        	var table="";
	    		var maintable=$("#maintable")
	    		$.each(data,function(i,alarmbean){
	    			table+="<tr style=\"\">";
	    			table+="<td style=\"text-align: left;margin:5px 10px;\">";
	    			if(alarmbean.alarmLevel=='3'){
	    				alarmLevel3++;
		    			table+="<span title=\"严重\" class=\"layui-badge-dot\" style=\"background: #EF164E;\"></span>";
	    			}else if(alarmbean.alarmLevel=='2'){
	    				alarmLevel2++
		    			table+="<span title=\"警告\" class=\"layui-badge-dot\" style=\"background: #F26A6B;\"></span>";
	    			}else{
	    				alarmLevel1++;
		    			table+="<span title=\"普通\" class=\"layui-badge-dot\" style=\"background: #F29905;\"></span>";
	    			}
	    			table+="<a href=\""+alarmbean.url+"\" title=\""+alarmbean.alarmMsg+"\" >    "+alarmbean.alarmMsg+"</a>"
	    			table+="</td>"
	    		    table+="</tr>"
	        	})
	        	maintable.html(table)
	        	alarmLevel();
	        }
	    })
	}
	alarmList();
	//设备总数
	function hostNumber(){
		var url="/data/home/host/gscm/mdHostNumber?random="+Math.random()
		$.ajax({
	        url: url,
	        type:"get",
	        dataType: "json",
	        async: true,
	        success: function (data) {
	        	$("#hostnumber").html(data)
	        }
	    })
	}
	hostNumber()
	
    //服务器列表 开始
    //存放所以host节点
    var canvas = document.getElementById('canvas');
    var serverwidth = $('#serverwidth').width();
    var stage = new JTopo.Stage(canvas);
    var scene = new JTopo.Scene(stage);
    scene.alpha = 0;
    canvas.width=serverwidth;
    // text:显示文本 id:节点id icon:图表名称 x:坐标x y:坐标y width:图片宽度 height:图片高度 ismouse:是否鼠标移动显示文本
    function addNode(text,id, icon, x, y,width,height,ismouse) {
      var node = new JTopo.Node();
      node.setImage('/images/hncu/' + icon + '.png', false);
      node.setSize(width, height);
      node.fontColor = '255,255,255';
      node.textPosition = 'Bottom_Center'
      node.font = 'normal 12px 宋体';
      node.showSelected=false;
      //是否鼠标移动显示
      if(ismouse){
		node.alarmAlpha=1;
		if(icon=="host"){
			node.mouseover(function () {
		    this.text = text;
		  });
		  node.mouseout(function () {
		    this.text = null;
		  });
		}
	   node.click(function(){
		  if(this.alarmId==null || this.alarmId==""){
			  return false;
		  }
		  var url="/data/home//host/gscm/homeAlarmQueryById?random="+Math.random()
    	  $.ajax({
            url: url,
            type:"get",
            dataType: "json",
            async: true,
            data:{alarmId:this.alarmId},
            success: function (data) {
            	var table="<div style=\"margin-top: 10px;\">";
				  table+="	<div class=\"layui-form-item\">"
				  table+="		<label class=\"layui-form-label\">告警主机</label>"
				  table+="		<div class=\"layui-input-block\">"
				  table+="		<input type=\"text\" name=\"\" disabled style=\"width:95%\"  value=\""+node.hostip+"\" autocomplete=\"off\" class=\"layui-input\">"
				  table+="		</div>"
				  table+="	</div>"
            	$.each(data,function(i,alarmBean){
            		table+="	<div class=\"layui-form-item\">"
  					  table+="		<label class=\"layui-form-label\">告警信息</label>"
  					  table+="		<div class=\"layui-input-block\">"
  					  table+="		<textarea placeholder=\"请输入内容\" disabled style=\"width:95%\" class=\"layui-textarea\">"+alarmBean.alarmMsg+"</textarea>"
  					  table+="		</div>"
  					  table+="	</div>"
  					  table+="	<div class=\"layui-form-item\" style=\"text-align:center;\">"
  					  table+="		<a href=\""+alarmBean.url+"\" class=\"layui-btn layui-btn-sm layui-btn-normal\">跳转查看详情</a>"
  					  table+="	</div>"
            	})
            	table+="</div>"
        		layer.open({
				  title:'告警详情信息',
				  type: 1, 
				  anim: 0,
				  area: ['500px', '350px'],
				  content: table //这里content是一个普通的String
				});
            }
        })
	  })
      }
	  node.setLocation(x, y);
	  if(icon!="host"){
		  node.text = text;
	  }
	  node.nodeId=id
      scene.add(node);
      return node;
     }
    // 开始节点,结束节点,是否使用二次折线
    function link(rootNode,node,linktype){
    	var link = ""
        if(linktype){
        	link = new JTopo.FlexionalLink(rootNode, node);
        	link.shadow = false;
        	link.offsetGap=100;//折线长度
        }else{
        	link = new JTopo.Link(rootNode, node);
        }
        link.strokeColor = '102,153,204';
        link.lineWidth = 1;
        scene.add(link);
    }
    
    var hostnode=[]
    var nodeleft=""
    var noderight=""
    function cancasPrint(data){
    	var firewall=data.firewall
    	var switchs=data.switchs
    	//画防火墙
    	// text:显示文本 id:节点id icon:图表名称 x:坐标x y:坐标y width:图片宽度 height:图片高度 ismouse:是否鼠标移动显示文本
    	$.each(firewall,function(i,node){
    		if(i==0){
    			// console.log(Math.floor(serverwidth*25%))
    			nodeleft=addNode(node.hostname,node.hostid, "f",Math.floor(serverwidth*0.15) , 100,30,50,true)
    			nodeleft.hostip=node.addr
    			hostnode.push(nodeleft)
    		}else{
    			 noderight=addNode(node.hostname,node.hostid, "f", Math.floor(serverwidth*0.85), 100,30,50,true)
    			 noderight.hostip=node.addr
    			 hostnode.push(noderight)
    		}
    	})
    	//将防火墙连线
    	link(noderight,nodeleft,false)
    	var node254=""
    	var node251=""
    	var node250=""
		var node254s=[]
    	var node251s=[]
    	var node250s=[]
    	//画交换机
    	$.each(switchs,function(i,node){
    		if(node.addr == "192.169.0.254"){
    			node254=addNode(node.hostname,node.hostid, "j",Math.floor(serverwidth*0.50)-12 , 300,60,20,true)
    			node254.hostip=node.addr
    			hostnode.push(node254)
    			node254s=node.host
    		}else if(node.addr == "192.169.0.251"){
    			node251=addNode(node.hostname,node.hostid, "j",Math.floor(serverwidth*0.15)-12 , 300,60,20,true)
    			node251.hostip=node.addr
    			hostnode.push(node251)
    			node251s=node.host
    		}else if(node.addr == "192.169.0.250"){
    			node250=addNode(node.hostname,node.hostid, "j",Math.floor(serverwidth*0.85)-12 , 300,60,20,true)
    			node250.hostip=node.addr
    			hostnode.push(node250)
    			node250s=node.host
    		}
    	})
    	//防火墙和交换机连线
    	link(noderight,node250,false)
    	link(nodeleft,node251,false)
    	//交换机连线
    	link(node251,node254,false)
    	link(node250,node254,false)
    	//画最左边主机
    	var x=25; //主机间隔
    	var userwidth=15;//主机用掉的宽度
    	var hostheight=520
    	$.each(node251s,function(j,node2){
			 var host=addNode(node2.hostname,node2.hostid, "host", x*(j+1)+userwidth, hostheight,20,50,true)
			 host.hostip=node2.addr
			 hostnode.push(host)
			 link(node251,host,true)
		 })
		 userwidth += x*(node251s.length)+40
		 var fznode=addNode("负载均衡器","251", "fz",userwidth, hostheight,50,20,true)
		 userwidth +=x
		 link(node251,fznode,true)
    	//交换机下面主机间隔
    	userwidth +=x+15+15
    	//画中间主机
    	$.each(node254s,function(j,node2){
			 var host=addNode(node2.hostname,node2.hostid, "host", x*(j+1)+userwidth, hostheight,20,50,true)
			 host.hostip=node2.addr
			 hostnode.push(host)
			 link(node254,host,true)
		 })
		 userwidth += x*(node254s.length)+40
//		 var fznode=addNode("负载均衡器","254", "fz",userwidth , hostheight,50,20,true)
//		 userwidth +=x
//		 link(node254,fznode,true)
    	//交换机下面主机间隔
//    	userwidth +=x+15
    	//画右边主机
    	$.each(node250s,function(j,node2){
			 var host=addNode(node2.hostname,node2.hostid, "host", x*(j+1)+userwidth, hostheight,20,50,true)
			 host.hostip=node2.addr
			 hostnode.push(host)
			 link(node250,host,true)
		 })
		 userwidth += x*(node250s.length)+40
		 var fznode=addNode("负载均衡器","250", "fz",userwidth, hostheight,50,20,true)
		 link(node250,fznode,true)
    }
    
    function queryServerList(){
    	var url="/data/home//hncu/topuquery?random="+Math.random()
    	$.ajax({
            url: url,
            type:"get",
            dataType: "json",
            async: true,
            success: function (data) {
            	cancasPrint(data)
                nodealarm();//alarmnodes.push
            }
        })
        
    }
    queryServerList()
    //告警开发 alarmnodes=[];
    function nodealarm(){
    	$.each(alarmnodes,function(i,node){
    		node.alarmId=""
    		node.url=""
			node.alarmMsg=""
			node.alarmLevel=""
			node.alarm=null;
    	})
    	alarmnodes=[]
		var url="/data/home//host/gscm/homeAlarmQuery?random="+Math.random()
    	$.ajax({
            url: url,
            type:"get",
            dataType: "json",
            async: true,
            success: function (data) {
            	$.each(data,function(i,alarmBean){
            		alarmDoDown(alarmBean)
            	})
            }
        })
    }
    function alarmDoDown(alarmBean){
    	alarlHostType(alarmBean)
    }
    //只告警主机
   function alarlHostType(alarmBean){
		$.each(hostnode,function(i,node){
			if(node.nodeId==alarmBean.dimension1 || node.nodeId==alarmBean.dimension2){
				var nodecha=nodechanges(node,alarmBean)
    			alarmnodes.push(nodecha)
    		}
    	})
    }
    function nodechanges(node,alarmBean){
    	if(node.alarmId!=""){//多个告警用#连接告警id
    		node.alarmId=node.alarmId+"#"+alarmBean.alarmId
    	}else{
    		node.alarmId=alarmBean.alarmId
    	}
		node.alarm="Warning";
    	return node;
    }
   //告警刷新方法
   function alarmRef(){
	   	nodealarm();
	   	alarmList();
   }
   setInterval(alarmRef, 1000*60);
   
   }
);
