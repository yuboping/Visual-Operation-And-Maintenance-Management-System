require.config({
	baseUrl : '/js',
	paths : {
		"domReady" : "require/domReady",
		"jquery" : "jquery/jquery.min",
		"bootstrap" : "bootstrap/bootstrap.min",
		"ais" : "lcims/ais/ais",
        'layer': '/js/layer/layer',
        'resizewh' : "lcims/resizewh/resizewh",
		"sprintf" : "lcims/tool/sprintf"
	},
	shim : {
		"bootstrap" : {
			deps : [ "jquery" ]
		}
	}
});

require([ "domReady!", "jquery", "bootstrap", "ais", "layer", "sprintf",'resizewh'], function(
		domReady, $, bs, ais, layer, sprintf,resizewh) {
	"use strict";

    var layer_load;

    loadingInsItem();
	bindFunction();
	resizewh.resizeBodyH($("#mainplan"));
	$(window).resize(function(){
		resizewh.resizeBodyH($("#mainplan"));
	})
	
	// ////////////////////////////////////////
	function bindFunction() {
		$("#schedule_type").on("change", function() {
			var optionVal = $("#schedule_type").val();
			if (optionVal == "1") {
				$("#monthSel").show();
				$("#monthLabel").show();
				$("#daySel").show();
				$("#dayLabel").show();
				$("#weekdaySel").hide();
			}
			if (optionVal == "2") {
				$("#monthSel").hide();
				$("#monthLabel").hide();
				$("#daySel").show();
				$("#dayLabel").show();
				$("#weekdaySel").hide();
			}
			if (optionVal == "3") {
				$("#monthSel").hide();
				$("#monthLabel").hide();
				$("#daySel").hide();
				$("#dayLabel").hide();
				$("#weekdaySel").show();
			}
			if (optionVal == "4") {
				$("#monthSel").hide();
				$("#monthLabel").hide();
				$("#daySel").hide();
				$("#dayLabel").hide();
				$("#weekdaySel").hide();
			}
		});

		// $(".dap-ais-chklist").on('click','.dap-ais-categorybox[data-disable!="true"]', function() {
		// 	ais.categoryboxclick($(this));
		// });
		$("#savebutton").on("click",function() {
			// 校验数据
			var title = $("#title").val();
			if (title.trim() == "") {
				alert("报告标题不能为空");
				return false;
			}
			if (sizeof(title) > 64) {
				console.log(sizeof(title));
				alert("报告标题长度不能超过64个字节");
				return false;
			}
			var schedule_type = $("#schedule_type").val();
			var monthsel = $("#monthSel").val();
			var daysel = $("#daySel").val();
			var hoursel = $("#hourSel").val();
			var minsel = $("#minSel").val();
			var weeksel = $("#weekdaySel").val();
			if (schedule_type == "1") {
				if (monthsel == "" || daysel == "" || hoursel == ""|| minsel == "") {
					alert("时间设置有误！");
					return false;
				}
			} else if (schedule_type == "2") {
				if (daysel == "" || hoursel == "" || minsel == "") {
					alert("时间设置有误！");
					return false;
				}
			} else if (schedule_type == "4") {
				if (hoursel == "" || minsel == "") {
					alert("时间设置有误！");
					return false;
				}
			} else if (schedule_type == "3") {
				if (weeksel == "" || hoursel == "" || minsel == "") {
					alert("时间设置有误！");
					return false;
				}
			}

			$("#timer").val(minsel + " " + hoursel + " " + daysel + " "+ monthsel + " " + weeksel);
			var selgroupids = "";
			objs = $(".dap-ais-chklist").find(".dap-ais-categorybox");
			$.each(objs, function(j, obj) {
				if (!$(obj).hasClass("dap-ais-nochoose")) {
					selgroupids = selgroupids + $(obj).attr("id")+ ",";
				}
			});
			if (selgroupids == "" || selgroupids == "undefined") {
				alert("巡检类别不能为空");
				return false;
			}
            $("#group_ids").val(selgroupids.substring(0, selgroupids.length - 1));
            $("#scheduleform").attr("action","/data/class/ais/schedule/" + type);
            if(type == "edit"){
                $("#scheduleform").submit();
			}else{
            	loadingwait();
				// 校验指标类型名称是否重复事件
				$.ajax({
					type: "post",
					url: "/view/class/ais/aisschedulemanage/vaild?random=" + Math.random(),
					cache: false,
					async: false,
					data: {title:title},
					success: function (result) {
						if(!result.opSucc) {
							layer.close(layer_load);
							if(result.data == "repeat"){
								$("#title").focus();
								layer.tips("巡检计划标题不能重复！", '#title',{ tips: [2, '#EE1A23'] });
								return;
							}
						}else{
							$("#scheduleform").submit();
						}
					}
				});
            }
		});

        $("#closebutton").on("click",function() {
			window.close();
		});

	}

    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
        });
    }
	
	function loadingInsItem(){
		$.getJSON("/data/class/ais/categarylist?random=" + Math.random(), function( resultdata) {
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
						if(catagary.categarydesc == "" || catagary.categarydesc == null){
							catagary.categarydesc = "暂无描述";
						}
						categaryhtml += sprintf.sprintf(categarytext, showmark, catagary.groupid,
								catagary.categarydesc, datadisable, catagary.iconclass,
								catagary.categaryname, catagary.categarydesc);
					});
				}
				// $(".dap-ais-chklist").empty();
				// $(".dap-ais-chklist").append(categaryhtml);
                $("#ais-chklist").empty();
                $("#ais-chklist").html(categaryhtml);
				resizewh.resizeBodyH($("#mainplan"));
			}
		});
	}
	
	var objs;
	var path = window.location.pathname;
	var type = "add";
	if (path.indexOf("/view/class/ais/schedule/editinit") == 0) {
        $(".dap-ais-chklist").on('click','.dap-ais-categorybox[data-disable!="true"]', function() {
            ais.categoryboxclick($(this));
        });

		type = "edit";

        var queryid = path.slice(34);

		$.getJSON("/data/class/ais/schedule/query/" + queryid + "?random=" + Math.random(), function(resultdata) {
			if (resultdata != null) {
				// 初始化数据
				$("#id").val(resultdata.id);
				$("#timer").val(resultdata.timer);
				$("#group_ids").val(resultdata.group_ids);
				$("#title").val(resultdata.title);
				$("#title").attr("readonly", true);
				$("#schedule_type").val(resultdata.schedule_type);
				$("#schedule_type").change();
				var timer = resultdata.timer.split(" ");
				$.each(timer, function(i, value) {
					if (value != "*") {
					    if(value == "?"){
					        value = 1;
                        }
						if (i == 0) {
							$("#minSel").val(value);
						} else if (i == 1) {
							$("#hourSel").val(value);
						} else if (i == 2) {
							$("#daySel").val(value);
						} else if (i == 3) {
							$("#monthSel").val(value);
						} else if (i == 4) {
							$("#weekdaySel").val(value);
						}
					}
				});
				// 巡检类型
				var group_ids = resultdata.group_ids.split(",")
				objs = $(".dap-ais-chklist").find(".dap-ais-categorybox");
				$.each(objs, function(j, obj) {
					$(obj).addClass("dap-ais-nochoose");
				});

				$.each(group_ids, function(i, value) {
					$.each(objs, function(j, obj) {
						if ($(obj).attr("id") == value) {
							$(obj).removeClass("dap-ais-nochoose");
						}
					});
				});
				$("#emails").val(resultdata.emails);
				$("#phones").val(resultdata.phones)
			}
		});
	}else if (path.indexOf("/view/class/ais/schedule/detailinit") == 0) {
        type = "detail";

        var queryid = path.slice(36);

        $.getJSON("/data/class/ais/schedule/query/" + queryid + "?random=" + Math.random(), function(resultdata) {
            if (resultdata != null) {
                // 初始化数据
                $("#id").val(resultdata.id);
                $("#timer").val(resultdata.timer);
                $("#group_ids").val(resultdata.group_ids);
                $("#title").val(resultdata.title);
                $("#title").attr("readonly", true);
                $("#schedule_type").val(resultdata.schedule_type);
                $("#schedule_type").change();
                $("#schedule_type").attr("disabled", true);
                //隐藏保存按钮
                $("#savebutton").attr("style","display:none;");
                var timer = resultdata.timer.split(" ");
                $.each(timer, function(i, value) {
                    if (value != "*") {
                        if (i == 0) {
                            $("#minSel").val(value);
                            $("#minSel").attr("disabled", true);
                        } else if (i == 1) {
                            $("#hourSel").val(value);
                            $("#hourSel").attr("disabled", true);
                        } else if (i == 2) {
                            $("#daySel").val(value);
                            $("#daySel").attr("disabled", true);
                        } else if (i == 3) {
                            $("#monthSel").val(value);
                            $("#monthSel").attr("disabled", true);
                        } else if (i == 4) {
                            $("#weekdaySel").val(value);
                            $("#weekdaySel").attr("disabled", true);
                        }
                    }
                });
                // 巡检类型
                var group_ids = resultdata.group_ids.split(",")
                objs = $(".dap-ais-chklist").find(".dap-ais-categorybox");
                $.each(objs, function(j, obj) {
                    $(obj).addClass("dap-ais-nochoose");
                });

                $.each(group_ids, function(i, value) {
                    $.each(objs, function(j, obj) {
                        if ($(obj).attr("id") == value) {
                            $(obj).removeClass("dap-ais-nochoose");
                        }
                    });
                });
                $(".dap-ais-chklist").onclick =null;

                $("#emails").val(resultdata.emails);
                $("#emails").attr("readonly", true);

                $("#phones").val(resultdata.phones)
                $("#phones").attr("readonly", true);

            }
        });
    }else if(path.indexOf("/view/class/ais/schedule/savefineinit") == 0){
        if (window.opener != null && !window.opener.closed) {
            var savetype = window.opener.document.getElementById("savetype");//获取父窗口中元素，也可以获取父窗体中的值
            savetype.value = "ok";//将子窗体中的值传递到父窗体中去
        }
		// alert("保存成功！");
		window.close();
	}else{
        $(".dap-ais-chklist").on('click','.dap-ais-categorybox[data-disable!="true"]', function() {
            ais.categoryboxclick($(this));
        });
	}

	function sizeof(str) {
		var char = str.replace(/[^\x00-\xff]/g, '**');
		return char.length;
	}
});
