require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        'cookie': '/js/jquery/jquery.cookie',
        'session': '/js/jquery/jquerySession',
        'resizewh':'/js/lcims/resizewh/resizewh'
    }
});
require([ "jquery",'cookie','session','resizewh'],function($,cookie,session,resizewh) {
	 loadLeftMenu();
	 var pathname = window.location.pathname;
	 if(pathname.indexOf("module")!=-1){
	     $("#leftsearch").show();
	 }
	 var leftsearch_val = "";
	//添加ID左侧菜单是否需要定位到该位置
	 function addMenuFocus(active){
		if(active == true){
			return " id=\"menufocus\" ";
		}else {
			return "";
		}
	};
	
	 //添加ID左侧子菜单是否需要定位到该位置
	 function addChildMenuFocus(active){
		if(active == true){
			return " id=\"childmenufocus\" ";
		}else {
			return "";
		}
	};
	 
	//判断左侧菜单是否需要active状态
	 function isActive(active){
		if(active == true){
			return " active ";
		}else {
			return "";
		}
	};
	//展示左侧菜单鼠标悬停的值
	 function showTitle(row){
		if(row.dynamic != "0"){
			return row.show_title;
		}else {
			return row.show_name;
		}
	};
	//判断左侧菜单是否时候display状态
	 function isDisplay(row,judgeActive,judgeIsMenu){
		 var display = "display:block";
		if(judgeActive == true){
			if(row.active == false){
				display = "display:none";
			}
		}
		if(judgeIsMenu == true){
			if(row.is_menu != "1"){
				display = "display:none";
			}
		}
		return display;
	};
	
	//根据左侧菜单的key值获取相应的图标icon
	function getMenuIcon(menukey,icon){
	    if(icon!=null && icon!=""){
	        return icon;
	    }else if(menukey == "total"){
			return " icon-zonglan ";
		}else if(menukey.indexOf("city")>=0){
			return " icon-ditu ";
		}else if(menukey.indexOf("node")>=0){
			return " icon-jiedian ";
		}
	};
	
	//判断左侧菜单是否有告警消息，并拼写对应的告警标签
	function mkMenuAlarmNum(alarmurl){
		if(alarmurl == null || alarmurl ==""){
			return "";
		}
		if(parseInt(alarmurl) == 0){
			return "";
		}
		var alarmspan = "<span class=\"message_box\">";
		alarmspan = alarmspan + "<span class=\"menu_message dep_red_bg\"></span>"
		alarmspan = alarmspan + "</span>";
		return alarmspan;
	};
	
	function mkChildMenuInfo(row) {
		var children = row.children;
		if(children == null || children == ""){
			return "";
		}
		var childmenuinfo = "<div class=\"vcon\" style=\""+isDisplay(row,true,true)+"\"><ul class=\"omc_menu_body clearfix\">";
		$.each(children, function(i, child){
			childmenuinfo = childmenuinfo + "<li "+addChildMenuFocus(child.active)+" style=\""+isDisplay(child,false,true)+"\" class=\""+isActive(child.active)+"\"><a  style=\"width:170px\" title='"+showTitle(child)+"' href=\"" + child.url +"\"data-theme=\""+child.show_name+"\"><i class=\"iconfont\"></i>"
			+ child.show_name + mkMenuAlarmNum(child.alarmcount) +  "</a>"  + "</li>";
		});
		childmenuinfo = childmenuinfo +"</div>";
		return childmenuinfo;
	};

	function loadLeftMenu(){
        var lastClick=$("#lastClick").val();
        var classtype=$("#classtype").val();
		var pageUrl=window.location.pathname;
		var session = $.session.get('leftsearch');
		var arr = pageUrl.split('/');
		if(arr[6] == 'total'){
			$.session.clear(); 
		}
		var url = "/system/menu/"+classtype+"?lastClick="+lastClick+"&pageUrl="+pageUrl+"&random="+Math.random();
		// var url = "/system/menu/"+classtype+"?random="+Math.random();
		$.ajax({
            url: url,
            dataType: "json",
            async: false,
            success: function (data) {
            	if(data.length ==0){
            		return;
            	}
            	var leftmenu = "";

                $.each(data, function(i, row) {
    	        	leftmenu = leftmenu + "<div "+addMenuFocus(row.active)+" class=\"vtitle"+isActive(row.active)+"\"><a title='"+showTitle(row)+"' href=\""+row.url+"\">";
    	        	if(row.icon!=null && row.icon!=""){
    	        	    leftmenu = leftmenu + "<i class=\"iconfont icon_menu "+ row.icon +"\"></i>"
    	        	}else{
    	        	    leftmenu = leftmenu + "<i class=\"iconfont icon_menu_no_icon \"></i>"
    	        	}
    	    				
    	        	leftmenu = leftmenu + row.show_name + mkMenuAlarmNum(row.alarmcount) + "</a></div>";
    	        	if(row.children != null && row.children != "") {
    	        		leftmenu = leftmenu + mkChildMenuInfo(row);
    	        	}
    	        });
            	$("#leftmenu").append(leftmenu);
            	findBrasip2(null);
    	        $("#leftsearch").keyup(findBrasip);
    	        /**
    	         * 添加鼠标移出事件 mouseleave
    	         */
                $("#leftsearch").mouseleave(function(){
                    var leftsearch = $("#leftsearch").val();
                    if(leftsearch_val!=leftsearch){
//                        console.log("leftsearch_val!=leftsearch");
                        findBrasip();
                    }
                });
    	        if($(".active") && $(".active").get(0)){
    	        	if($(".active").get(0).tagName == 'DIV'){
            			$(".active").next().show();
            		}else if($(".active").get(0).tagName == 'LI'){
            			$(".active").parent().parent().show();
            		}
    	        }
    	        
            },
            error: function (data) {
                console.log("URL ERROR:["+url+"]");
            }
        });
		resizewh.resizeWH($("#leftmenubar"));
	};
	
	function findBrasip2(brasip){
    	if(null != brasip && brasip != ""){
    		$("#leftsearch").val(brasip);
    		findBrasip();
		}
	}
	
	function findBrasip() {
    	var leftsearch = $("#leftsearch").val();
    	leftsearch_val = $("#leftsearch").val();
    	$("#leftmenu .vtitle").hide();
    	$("#leftmenu .vcon").show();
    	
    	$("#leftmenu a").each(function(){
//    		var tmp = $(this).attr('title');
    		//使用名称不是使用IP搜索
    		var tmp = $(this).html();
    		tmp = tmp.split("<span")[0];
    		tmp = tmp.split("</i>")[1];
    		
    		if(tmp.indexOf(leftsearch)==0){
    			$(this).show();
    			if($(this).parent().get(0).tagName == 'LI'){
    			    $(this).parent().parent().parent().prev().show();
    			    $(this).parent().parent().parent().prev().children("a").show();
    			}else{
    			    $(this).parent().show();
    			}
    		}else{
    		        $(this).hide();
    		}
    	});
    	
    	if(leftsearch.length <=0 ){
    		$.session.clear(); 
    		$("#leftmenu .vcon").hide();
    		$("#leftmenu .vtitle:eq(0)").show();
    		if($(".active").get(0).tagName == 'DIV'){
    			$(".active").next().show();
    		}else if($(".active").get(0).tagName == 'LI'){
    			$(".active").parent().parent().parent().show();
    		}
    	}
    }
});