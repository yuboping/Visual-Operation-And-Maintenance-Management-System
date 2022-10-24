require.config({
    paths: {
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'config':"/js/echarts/config",
        'jquery': '/js/jquery/jquery.min',
        'packdata':'/js/lcims/tool/packdata',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'echarts3': "/js/lcims/jscm/echarts.min",
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
	       obj[next.id] ? '' : obj[next.id] = true && item.push(next);
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
    var nodes=[];
    var canvas = document.getElementById('canvas');
    var serverwidth = $('#serverwidth').width();
    var stage = new JTopo.Stage(canvas);
    var scene = new JTopo.Scene(stage);
    scene.alpha = 0;
    canvas.width=serverwidth;
    //整个canvas高度
    var canvasHeight=780;
    //node节点高度增加量
    var x=0;
    //每个主机节点的高度
    var nodeHeight=40+x;
   //每个主机节点的宽度
    var nodeWidth=Math.ceil(nodeHeight*(3/4));
   //root节点宽度
    var rootwidth=Math.ceil(nodeWidth*(5/3));
    //root节点高度
    var rootheight=Math.ceil(nodeWidth*(5/3));
    //相邻主机节点之间的水平间距
    var nodex=1;
    //相邻主机节点之间的垂直间距
    var nodey=10;
    //每个业务之间的垂直间距
    var containery=27;
    //root与node之间的连线长度固定的不能改
    var linklenght=87;
   //图形上方空白高度
    var blanky=20;//30
    //画图实际可以高度
    var heighty=canvasHeight-blanky;
    
    var serverlistheight=0;
    
    // text:显示文本 id:节点id icon:图表名称 x:坐标x y:坐标y width:图片宽度 height:图片高度 ismouse:是否鼠标移动显示文本
    function addNode(text,id, icon, x, y,width,height,ismouse) {
      var node = new JTopo.Node();
      node.setImage('/images/jscm/' + icon + '.png', false);
      node.setSize(width, height);
      node.fontColor = '255,255,255';
      node.textPosition = 'Bottom_Center'
      node.font = 'normal 12px 宋体';
      node.showSelected=false;
      //是否鼠标移动显示
      if(ismouse){
		node.alarmAlpha=1;
		node.mouseover(function () {
		    this.text = text;
		  });
		  node.mouseout(function () {
		    this.text = null;
		  });
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
      }else{
    	  node.setLocation(x, y);
    	  node.text = text;
    	  node.nodeId=id
      }
      scene.add(node);
      return node;
     }
    function addTextNode(text,x,y,width,height){
    	  var textNode = new JTopo.TextNode(text);  // 创建文本节点
    	  textNode.setLocation(x, y);
    	  textNode.setSize(width, height);
    	  textNode.font = 'normal 12px 宋体';
    	  textNode.alpha = 1
    	  textNode.showSelected=false;
    	  textNode.fontColor = '255,255,255';
    	  textNode.zIndex=999
    	  scene.add(textNode);
    }
    function guid() {
        return Number(Math.random().toString().substr(3, 3) + Date.now()).toString(36);
    }

    function addContainer(datas,text,rootId,x,y,width,height) {
        // 流式布局（水平、垂直间隔)
        var flowLayout = JTopo.layout.FlowLayout(nodex, nodey);
        var container2 = new JTopo.Container();
        container2.layout = flowLayout;
        container2.fillColor = '0,51,102';//'255,255,255';
        container2.fontColor = '255,255,255';
        container2.alpha =0.5;
        container2.textPosition = 'Top_Left';
        container2.shadow=true
        container2.shadowOffsetX=3
        container2.shadowOffsetY=4
        
        container2.setBound(x, y, width, height);
        $.each(datas,function(a,data){
        	 var node=addNode(data.ip,rootId,'node',0,0,nodeWidth,nodeHeight,true)
        	 node.id=guid()
        	 node.hostId=data.hostId
        	 node.nodeId=rootId
        	 node.serverName=text
        	 node.hostip=data.ip
        	 node.alarmId=""
        	 //把所有主机节点方到数组里
        	 nodes.push(node)
 	         container2.add(node);
        })
        scene.add(container2);
        
        return container2;
       
      }
    function link(rootNode,node){
    	 var link = new JTopo.FlexionalLink(rootNode, node);
	        link.direction = 'horizontal';
	        link.strokeColor = '102,153,204';
	        link.lineWidth = 1;
	        scene.add(link);
    }
    
    function changdata(sumhang,maxhang){	
	  //node节点高度增加量
    //  var kkk=heighty-(serverlistheight-1)*containery;
      if(sumhang<maxhang*0.3){
    	  x=10;
    	  containery=100;
      }else if(sumhang<maxhang*0.5){
    	  x=10;
      }else if(sumhang>maxhang){
    	  x=-10;
      }
  //    console.log("x="+x)
	  //x=-10;
	  //每个主机节点的高度
	  nodeHeight=40+x;
	  //每个主机节点的宽度
	  nodeWidth=Math.ceil(nodeHeight*(3/4));
	  //root节点宽度
	  rootwidth=Math.ceil(nodeWidth*(5/3));
	  //root节点高度
	  rootheight=Math.ceil(nodeWidth*(5/3));
    }
    function cancasPrint(serverlist){
	  //container 的最大宽度 30:是左边空白 10:是右边空白
	  var w=serverwidth-linklenght-30-rootwidth-10;
//	  console.log('剩余宽度'+w)
	  // 画图总共用了多少高度
	  var h=0;
	  serverlistheight=serverlist.length-1;
	  //container 最多一行放多少个node 一个node 宽30px 高40px计算 node和node直接水平间距1 垂直间距1 一个node实际大小35 44
	  var n=Math.floor(w/(nodeWidth+nodex));
//	  console.log('最多一行放多少个node:'+n)
//	  console.log('最多放多少行node:'+Math.floor((heighty-(serverlist.length-1)*containery)/(nodeHeight+nodey)))
	  var maxhang=Math.floor((heighty-(serverlist.length-1)*containery)/(nodeHeight+nodey));
	  var sumhang=0;
	  $.each(serverlist,function(i,data){
		  var serverx=data.serverType;
		  $.each(serverx,function(j,data1){
			  var hostlist=data1.hostList;
			  var hostl=hostlist.length
			  var hang2=Math.ceil(hostl/n);
			  sumhang=sumhang+hang2;
			  
		  })
	  })
//	  console.log("当前数据需要多少行:"+sumhang);
	  changdata(sumhang,maxhang);
	  //数据改变后,重新计算数据开始
	  n=Math.floor(w/(nodeWidth+nodex));
//	  console.log('从新计算最多一行放多少个node:'+n)
//	  console.log('从新计算最多放多少行node:'+Math.floor((heighty-(serverlist.length-1)*containery)/(nodeHeight+nodey)))
	  w=serverwidth-linklenght-30-rootwidth-10;
	  //数据改变后,重新计算数据结束
	  //一个节点全部展示 占据的高度
	  var m=0;
	  $.each(serverlist,function (i,data){
		  //节点的 节点名称 节点大小 宽高都为50px计算
		  var rootname=data.nodeName;
		  var rootId=data.nodeId;
		  //一个节点下面的全部服务列表
		  var servertype=data.serverType;
		  //一个server用掉多少高度
		  var nodeheight=0;
		  var containers=[]
		  $.each(servertype,function(j,item){
			  //一个container的高度
			  var h2=0;
			  //服务类型显示名称
			  var containername=item.serverType;
			  //服务类型英文名称
			  var servername=item.serverName;
			  //主机列表信息
			  var hostlist=item.hostList;
			  var hostNum=hostlist.length
			  //parseInt
			  var hang=Math.ceil(hostNum/n);
			  //行数*一个主机的高度
			  h2=hang*(nodeHeight+nodey);
	//		  console.log(rootname+":"+containername+":要 "+hang+"行"+"有"+hostNum+":个node;"+"要"+h2+"px高度")
			  var ww=0;
			  if(hang>1){
				  ww=w;
			  }else{
				  ww=hostNum*(nodeWidth+nodex)
			  }
			  addTextNode(containername,30+linklenght+rootwidth,blanky+m-20,30,16);
			  var con=addContainer(hostlist,servername,rootId,30+linklenght+rootwidth, blanky+m,ww,h2);
			  containers.push(con)
			  m=m+h2;
			  //container和container直接的间距
			  m=m+containery;
	//		  console.log("m="+m)
			  nodeheight=nodeheight+h2+containery;
		  })
		  var rooty=h+Math.ceil((nodeheight-containery)/2)//-Math.ceil(rootheight/2)
	//	  console.log("root height:"+rootheight)
		  var rootNode = addNode(rootname,rootId, 'host', 30, rooty,rootwidth,rootheight,false);
		  $.each(containers,function(l,item){
			  link(rootNode,item);
		  })
		  //一个
		  h=h+nodeheight;
//		  console.log("一个rootnode:"+h)
		  
	  })
//	  console.log("用掉的总高度:"+h)
    }
    
    
    function queryServerList(){
    	var url="/data/home/server/serverlist?random="+Math.random()
    	$.ajax({
            url: url,
            type:"get",
            dataType: "json",
            async: true,
            success: function (data) {
            	//console.log(data)
            	cancasPrint(data)
                nodealarm();//alarmnodes.push
            }
        })
 //       console.log("同步测试")
        
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
    	switch(alarmBean.dimensionType) {
    	case '8': //全部主机
    		 alarlHostType(alarmBean)
           break;
        case '9': //单个主机
        	 alarlHostType(alarmBean)
           break;
        case '10': //全部节点,全部主机
        	alarlNodeType(alarmBean)
           break;
        case '11': //单个节点, 
        	alarlNodeType(alarmBean)
           break;
        case '12'://单个节点下面,单个主机,单个业务
        	alarlNodeType(alarmBean)
           break;
        default:
           console.log("告警字段"+alarmBean.dimensionType+"没配")
           break;
    	} 
    }
    //8:全部主机 9:单个主机
   function alarlHostType(alarmBean){
    	if(alarmBean.serverType=="all"){
    		$.each(nodes,function(i,node){
    			if(node.hostId==alarmBean.dimension1){
    				var nodecha=nodechanges(node,alarmBean)
        			alarmnodes.push(nodecha)
        		}
	    	})
    	}else{
    		$.each(nodes,function(i,node){
    			if(node.serverName==alarmBean.serverType
        				&& node.hostId==alarmBean.dimension1){
    				var nodecha=nodechanges(node,alarmBean)
        			alarmnodes.push(nodecha)
        		}
	    	})
    	}
    }
    function nodechanges(node,alarmBean){
    	if(node.alarmId!=""){
    		node.alarmId=node.alarmId+"#"+alarmBean.alarmId
    	}else{
    		node.alarmId=alarmBean.alarmId
    	}
		node.alarm="Warning";
    	return node;
    }
    //12:单个节点下面,单个主机,单个业务 11:单个节点.全部主机 10:全部节点,全部主机
   function alarlNodeType(alarmBean){
    	if(alarmBean.serverType=="all"){
    		$.each(nodes,function(i,node){	
        		if(node.hostId==alarmBean.dimension2){
        			var nodecha=nodechanges(node,alarmBean)
        			alarmnodes.push(nodecha)
        		}
	    	})
    	}else{
    		$.each(nodes,function(i,node){	
        		if(node.nodeId==alarmBean.dimension1
        				&& node.hostId==alarmBean.dimension2
        				&& node.serverName==alarmBean.serverType){
        			var nodecha=nodechanges(node,alarmBean)
        			alarmnodes.push(nodecha)
        		}
	    	})
    	}
	    }
	   //服务器列表 结束
	   //告警刷新方法
	   function alarmRef(){
	   	nodealarm();
	   	alarmList();
	   }
	   setInterval(alarmRef, 1000*60);
   }
);
