require.config({
	paths : {
		'jquery' : '/js/jquery/jquery.min',
		'daterangepicker' : '/js/lcims/tool/daterangepicker',
		'moment' : '/js/lcims/tool/moment',
		'resizewh' : "/js/lcims/resizewh/resizewh",
		'layer':'/js/layer/layer',
		"ais":"/js/lcims/ais/ais",
        "laypage":"/js/lcims/tool/laypage",
		"sprintf" : "/js/lcims/tool/sprintf"
	},
	shim : {
		'daterangepicker' : {
			deps : [ 'jquery', 'moment' ]
		},
		'moment' : {}
	}
});
require([ "jquery", 'daterangepicker','resizewh', 'sprintf','layer','laypage','ais'], function($,daterangepicker,resizewh, sprintf,layer,laypage,ais) {
	// 实施巡检模块加载
	loadingInsItem();
	// 巡检计划加载
	loadingInsSchedule();
	initData();
	bindFunction();
	doseach();
	resizewh.resizeWH($("#maincheckhome"));

	var notice = "<div style='font-size: 22px;text-align: center;margin: 85px auto;'>暂无数据</div>";
	$("#aistbody").html(notice);
	if ($("#aistbody").html(notice)){
		$("#searchAis").attr({"disabled":"disabled"});
		$("#searchAis").css({"cursor":"not-allowed"});
	}

	function loadingInsItem() {
		$.getJSON("/data/class/ais/categarylist?random=" + Math.random(),
				function(resultdata) {
					if (resultdata.opSucc) {
						var chklist = resultdata.data;
						var categaryhtml = "";
						var categarytext = $('#chkcategary')[0].text;
						if (chklist != null && chklist != "") {
							$.each(chklist, function(i, catagary) {
								var showmark = "";
								var datadisable = "";
								if (catagary.isdisable) {
									showmark = "dap-ais-nochoose";
									datadisable = "true";
								}
								categaryhtml += sprintf.sprintf(categarytext,
										showmark, catagary.groupid,
										datadisable, catagary.iconclass,
										catagary.categaryname,
										catagary.categarydesc,catagary.categarydesc);
							});
						}
						$("#ais-chklist").empty();
						$("#ais-chklist").html(categaryhtml);
						resizewh.resizeWH($("#maincheckhome"));
					}
				});
	}
	function loadingInsSchedule() {
		$.getJSON('/data/class/ais/schedules?random=' + Math.random(),function(resultdata) {
			if (resultdata.opSucc) {
				$("#noplan").hide();
				$("#hasplan").show();
				
				var nexttimer = resultdata.data.nexttime;
				if (nexttimer != "") {
					var timer = nexttimer.split(" ");
					if (timer.length == 3) {
						$(".dap-ais-plan-next-time #showdate").empty().html(timer[0]);
						$(".dap-ais-plan-next-time .time").empty().html(timer[1]);
						$(".dap-ais-plan-next-time .weekday").empty().html(timer[2]);
					}
				}
				var schedulelist = resultdata.data.schedule;
				var schedulehtml = "";
				var scheduletext = $('#schedule')[0].text;
				if (schedulelist != null && schedulelist != "") {
					$.each(schedulelist, function(i, item) {
						schedulehtml += sprintf.sprintf(scheduletext,cutString(item.title, 40,"..."), item.id);
					});
				}
				$("#schdulelist").empty();
				$("#schdulelist").html(schedulehtml);
				resizewh.resizeWH($("#maincheckhome"));
			} else {
				$("#noplan").show();
				$("#hasplan").hide();
			}
		});
	}
	var report_resizewh = false;
	function doseach(currentpage) {
	        var pagesize = 8;
	        
	        if(currentpage==null||currentpage==""){
	        	currentpage=1;
	        }
			var reservation = $("#reservation").val();
	    	var times = reservation.split(" - ");
	    	var begintime=times[0];
	    	var endtime = times[1];
	    	var searchkey = $("#searchkey").val();
	    	var data={
	    			'begintime':begintime,
	    			'endtime':endtime,
	    			'currentpage':currentpage,
	    			'searchkey':searchkey,
	    			'pagesize':pagesize
	    	}
	        $.getJSON("/data/class/ais/report?random=" + Math.random(),data, function(result) {
	        	var total=result.data.totalcount;
	        	var currentpage=result.data.currentpage;
	        	laypage({
	                cont: 'pageinfo', 
	                skin: '#6AB0F4',
	                pages: Math.ceil(total/pagesize),
	                curr: currentpage, 
	                skip: false, //是否开启跳页
	                jump: function(obj, first){ //触发分页后的回调
	                    if(Boolean(first)){
	                    	showTable(result.data.reportlist);
	                    	if(currentpage==1 && report_resizewh==false){
	                    	    report_resizewh = true;
	                    	    resizewh.resizeWH($("#maincheckhome"));
	                    	}
	                    }else {
	                    	doseach(obj.curr);
	                    }
	                },
	                groups: 6 //连续显示分页数
	            });
	    	});
	}
	//拼接tr
    function showTable(data){
    	var reporthtml = "";
    	var reporttext = $('#report')[0].text;
    	$(data).each(function(i,report){
    		 reporthtml += sprintf.sprintf(reporttext, report.title,report.showcreatetime,"/export/ais/regularReport?reportId="+report.id);
    	})
        $("#reporttbody").empty();
        $("#reporttbody").html(reporthtml);
    }

    function cutString(str, len, suffix) {
        if (!str)
            return "";
        if (len <= 0)
            return "";
        if (!suffix)
            suffix = "";
        var templen = 0;
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 255) {
                templen += 2;
            } else {
                templen++
            }
            if (templen == len) {
                return str.substring(0, i + 1) + suffix;
            } else if (templen > len) {
                return str.substring(0, i) + suffix;
            }
        }
        return str;
    }
    
	function initData() {
		// 时间选择
		var formatedate = function(date) {
			var datetime = date.getFullYear()
					+ "-"// "年"
					+ ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1))
					+ "-"// "月"
					+ (date.getDate() < 10 ? "0" + date.getDate() : date.getDate());
			return datetime;
		};
		var addNDays = function(date, n) {
			var time = date.getTime();
			var newTime = time + n * 24 * 60 * 60 * 1000;
			return new Date(newTime);
		};
		var today = formatedate(new Date());
		var weekday = formatedate(addNDays(new Date(), -7));
		
		$('#reservation').val(weekday + ' - ' + today).daterangepicker({
			parentEl : $("#date_div")
		}, function() {
			doseach();
		});
		
		$('#searchkey').val("");
	}
	
	function bindFunction(){
		$(".dap-ais-chklist").on('click','.dap-ais-categorybox[data-disable!="true"]',function(){ais.categoryboxclick($(this));});

		$('#searchkey').keydown(function(event){
	    	if (event.keyCode == 13) {
	    		doseach();
	    	}
	    });
		$('#search').click(function(){
    		doseach();
	    });
		
		 $("#dap-ais-btn").on('click',function(){
            var selgroupids="";
            var objs= $(".dap-ais-chklist").find(".dap-ais-categorybox");
            $.each(objs,function(j,obj){
				if(!$(obj).hasClass("dap-ais-nochoose")){
				    selgroupids = selgroupids+$(obj).attr("attr")+",";
				}
			});
			if(selgroupids==""||selgroupids=="undefined") {
				alert("巡检项不能为空");
				return false;
			}
            selgroupids = selgroupids.substring(0,selgroupids.length-1);
            window.open("/data/class/ais/check?groupids="+selgroupids);
            getAisTable();
            //var radiusgroupids = selgroupids.split(",");
            //window.open("/data/class/ais/radiusReport?groupids="+radiusgroupids[0]);
		 });
	}

	function getAisTable(){
		var selgroupids="";
		var objs= $(".dap-ais-chklist").find(".dap-ais-categorybox");
		var selectData = $("#AisSelect").val();
		console.log("selectData",selectData);
		$.each(objs,function(j,obj){
			if(!$(obj).hasClass("dap-ais-nochoose")){
				selgroupids = selgroupids+$(obj).attr("attr")+",";
			}
		});
		selgroupids = selgroupids.substring(0,selgroupids.length-1);

		$.getJSON("/data/class/ais/checkTable?groupids="+selgroupids+"&selectData="+selectData, function(result) {
			showAisTable(result);
			if (result){
				$("#searchAis").attr("disabled",false);
				$("#searchAis").css({"cursor":"pointer"});
				$('#searchAis').click(function(){
					getAisTable();
				});
			}

		});
	}
	//拼接tr
	function showAisTable(data){
		console.log(data);
		var aishtml = "";
		if(data.length > 0){
			for(var i=0;i<=data.length-1;i++){

				if (data[i].value == null || data[i].value == ""){
					data[i].value = "-";
				}

				if (data[i].checking == null){
					data[i].checking = "-";
				}

				if (data[i].result == "异常"){
					aishtml = aishtml + "<tr style='color:red;'>" +
						"<td title=\"" + data[i].content + "\">" + data[i].content + "</td>" +
						"<td title=\"" + data[i].time + "\">"+ data[i].time + "</td>" +
						"<td title=\"" + data[i].result + "\">" + data[i].result + "</td>" +
						"<td title=\"" + data[i].value + "\">" + data[i].value + "</td>" +
						"<td title=\"" + data[i].ip + "\">" + data[i].ip + "</td>" +
						"<td title=\"" + data[i].checking + "\">" + data[i].checking + "</td>" +
						"</tr>";
				}else {
					aishtml = aishtml + "<tr>" +
						"<td title=\"" + data[i].content + "\">" + data[i].content + "</td>" +
						"<td title=\"" + data[i].time + "\">"+ data[i].time + "</td>" +
						"<td title=\"" + data[i].result + "\">" + data[i].result + "</td>" +
						"<td title=\"" + data[i].value + "\">" + data[i].value + "</td>" +
						"<td title=\"" + data[i].ip + "\">" + data[i].ip + "</td>" +
						"<td title=\"" + data[i].checking + "\">" + data[i].checking + "</td>" +
						"</tr>";
				}
			}
		}else {
			aishtml = "<div style='font-size: 22px;text-align: center;margin: 85px auto;'>暂无数据</div>";
		}
		$("#aistbody").empty();
		$("#aistbody").html(aishtml);
	}
});