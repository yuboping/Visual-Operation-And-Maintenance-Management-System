require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer': '/js/layer/layer',
        "laypage": "/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil',
        "domReady" : "/js/require/domReady",
        "bootstrap" : "/js/bootstrap/bootstrap.min",
        "ais" : "/js/lcims/ais/ais",
        "sprintf" : "/js/lcims/tool/sprintf"
    },
    shim : {
        "bootstrap" : {
            deps : [ "jquery" ]
        }
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh','checkbox','stringutil', "domReady!", "bootstrap", "ais", "sprintf" ],
    function($, layer, laypage, resizewh,checkbox,stringutil, domReady, bs, ais, sprintf) {
        // "use strict";
        var layer_load;
        var type;
        resizewh.resizeBodyH($("#mainaisschedule"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','aisschedulediv');
        });
        reset("mainaisschedule");
        initChildrenMenu();
        loadingwait();
        loadOptRecord();
        loadingInsItem();

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainaisschedule");
        });
        $("#addbutton").click(function() {
            addShow();
        });
        $("#savebutton").click(function() {
            saveInfo();
        });
        $("#closebutton").click(function() {
            layer.closeAll();
        });

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

        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','aisschedulediv');
    }

    //权限控制操作标签
    function initChildrenMenu(){
    	var pageUrl=window.location.pathname;
        $("#operate_menu").empty();
        var url = "/view/class/querychildrenmdmenu";
        $.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
            if(result!=null && result.length >0 ){
                for(var i=0;i<result.length;i++){
                    $("#operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
                    // 新增、修改、删除绑定事件
                    if(result[i].url=='add'){
                        $("#"+result[i].name).click(function() {
                            addShow();
                        });
                    }else if(result[i].url=='edit'){
                        $("#"+result[i].name).click(function() {
                            modifyShow();
                        });
                    }else if(result[i].url=='delete'){
                        $("#"+result[i].name).click(function() {
                            deleteShow();
                        });
                    }
                }
            }

        });
    }

    // 重置页面标签内容
    function reset(divid){
        loadingInsItem();
        $("#"+divid+" input[type='text']").each(function(){
            $(this).val('');
            $(this).attr("disabled", false);
            $(this).attr("readonly", false);
        });
        $("#"+divid+" button[type='button']").each(function(){
            $(this).show();
        });
        $("#"+divid+" select").each(function(){
            $(this).attr("disabled", false);
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
        $("#schedule_type").change();
        $(".dap-ais-chklist").off('click');

    }

    function bindCheckBox(){
        $("#aisschedulediv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
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
                $("#ais-chklist").empty();
                $("#ais-chklist").html(categaryhtml);
                resizewh.resizeBodyH($("#mainplan"));
            }
        });
    }

    // 新增按钮事件
    function addShow() {
        type = "add";
        reset("add_div");
        $(".dap-ais-chklist").on('click','.dap-ais-categorybox[data-disable!="true"]', function() {
            ais.categoryboxclick($(this));
        });
        showLayer("add_div",'新增巡检计划');
        // var openUrl = "/view/class/ais/schedule/addinit";//弹出窗口的url
        // var iWidth= 1200; //弹出窗口的宽度;
        // var iHeight= 800; //弹出窗口的高度;
        // var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
        // var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
        // var windowObj = window.open(openUrl,"新增巡检计划","height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft +",toolbar=no, menubar=no,location=no, status=no");
        // var loop = setInterval(function() {
        //     if(windowObj  != null && windowObj.closed) {
        //         clearInterval(loop);
        //         var savetype = $("#savetype").val();
        //         if(savetype == "ok"){
        //             alert("新增成功!");
        //         }
        //         $("#savetype").val("");
        //         //do something 在这里执行回调
        //         loadOptRecord();
        //     }
        // }, 800);
    }

    // 新增确认
    function saveInfo() {
        var objs;
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
        var id = $("#id").val();
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
        var timer = $("#timer").val();
        var group_ids = $("#group_ids").val();
        var emails = $("#emails").val();
        var phones = $("#phones").val();

        loadingwait();
        // 校验指标类型名称是否重复事件
        var data = {id : id, schedule_type : schedule_type, title : title, timer : timer, group_ids : group_ids, emails : emails, phones : phones};
        $.getJSON("/view/class/ais/aisschedulemanage/"+ type +"?random="
            + Math.random(), data, function(result) {
            if(result.opSucc) {
                layer.closeAll();
                layerResultAndReload(result);
            }else{
                if(result.data == "repeat"){
                    layer.close(layer_load);
                    $("#title").focus();
                    layer.tips("巡检计划标题不能重复", '#title',{ tips: [2, '#EE1A23'] });
                    return;
                }else{
                    layer.closeAll();
                    layerResultAndReload(result);
                }
            }
        });

        // $.ajax({
        //     type: "POST",
        //     url: "/view/class/ais/aisschedulemanage/"+ type +"?random=" + Math.random(),
        //     cache: false,
        //     async: false,
        //     data: {id : id, schedule_type : schedule_type, title : title, timer : timer, group_ids : group_ids, emails : emails, phones : phones},
        //     success: function (result) {
        //         if(result.opSucc) {
        //             layer.closeAll();
        //             layerResultAndReload(result);
        //         }else{
        //             if(result.data == "repeat"){
        //                 layer.close(layer_load);
        //                 $("#title").focus();
        //                 layer.tips("巡检计划标题不能重复", '#title',{ tips: [2, '#EE1A23'] });
        //                 return;
        //             }else{
        //                 layer.closeAll();
        //                 layerResultAndReload(result);
        //             }
        //         }
        //     }
        // });
    }


        // 修改按钮事件
    function modifyShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个巡检计划!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var group_id = checkboxArray[0];
            type = "modify";
            reset("add_div");
            $(".dap-ais-chklist").on('click','.dap-ais-categorybox[data-disable!="true"]', function() {
                ais.categoryboxclick($(this));
            });
            loadingwait();
            $.getJSON("/data/class/ais/schedule/query/" + group_id + "?random=" + Math.random(), function(resultdata) {
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
            showLayer("add_div",'修改巡检计划');
            // var openUrl = "/view/class/ais/schedule/editinit/"+group_id;//弹出窗口的url
            // var iWidth= 1200; //弹出窗口的宽度;
            // var iHeight= 800; //弹出窗口的高度;
            // var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
            // var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
            // var windowObj = window.open(openUrl,"修改巡检计划","height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft +",toolbar=no, menubar=no,location=no, status=no");
            // var loop = setInterval(function() {
            //     if(windowObj  != null && windowObj.closed) {
            //         clearInterval(loop);
            //         var savetype = $("#savetype").val();
            //         if(savetype == "ok"){
            //             alert("修改成功");
            //         }
            //         $("#savetype").val("");
            //         //do something 在这里执行回调
            //         loadOptRecord();
            //     }
            // }, 800);
        }
    }

    // 查询按钮事件
    function queryOpt() {
        loadingwait();
        loadOptRecord();
    }
    // 加载查询内容
    function loadOptRecord() {
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 10;

        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();

        var schedule_name = stringutil.Trim($("#schedule_name").val());
        var data = {
            'title' : schedule_name
        };
        $.getJSON("/view/class/ais/aisschedulemanage/query?random=" + Math.random(), data, function(result) {
            layer.close(layer_load);
            total = result.length;
            pagecount = Math.ceil(total / page_count);
            $("#querynum").text(total);
            laypage({
                cont : 'pageinfo',
                skin : '#6AB0F4',
                pages : pagecount,
                curr : 1,
                skip : false, // 是否开启跳页
                jump : function(obj, first) { // 触发分页后的回调
                    startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showTable(result, startnum, endnum);
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total==0){
                        $("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#mainaisschedule"));
                },
                groups : page_count
            // 连续显示分页数
            });
        });
    }
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.id;

            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata
                + "<tr><td>" +"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"
                + "</td><td title=\""+ rowninfo.title +"\">" + rowninfo.title
                + "</td><td title=\""+stringutil.isNull(rowninfo.group_ids)+"\">" + stringutil.isNull(rowninfo.group_ids)
                + "</td><td title=\""+stringutil.isNull(rowninfo.timer)+"\">" + stringutil.isNull(rowninfo.timer)
                // + "</td><td title=\""+stringutil.isNull(rowninfo.emails)+"\">" + stringutil.isNull(rowninfo.emails)
                + "</td><td>" + "<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\">详情</a>"
                + "</td></tr>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }
        $("#aisschedulediv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
                detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("aisschedulediv");
    }

    //详情按钮事件
    function detailShow(group_id){
        reset("add_div");
        loadingwait();
        $.getJSON("/data/class/ais/schedule/query/" + group_id + "?random=" + Math.random(), function(resultdata) {
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

                $("#emails").val(resultdata.emails);
                $("#emails").attr("readonly", true);

                $("#phones").val(resultdata.phones)
                $("#phones").attr("readonly", true);

            }
        });
        showLayer("add_div",'查看巡检计划详情');
        // var openUrl = "/view/class/ais/schedule/detailinit/"+group_id;//弹出窗口的url
        // var iWidth= 1200; //弹出窗口的宽度;
        // var iHeight= 800; //弹出窗口的高度;
        // var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
        // var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
        // var windowObj = window.open(openUrl,"巡检计划详情","height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft +",toolbar=no, menubar=no,location=no, status=no");
        // var loop = setInterval(function() {
        //     if(windowObj  != null && windowObj.closed) {
        //         clearInterval(loop);
        //         var savetype = $("#savetype").val();
        //         if(savetype == "ok"){
        //             // alert("保存成功");
        //             $("#savetype").val("");
        //         }
        //         //do something 在这里执行回调
        //         loadOptRecord();
        //     }
        // }, 800);
    }

    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个巡检计划!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            layer.confirm('是否确认删除该批次数据？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] // 按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {aisScheduleArray:checkboxArray};
                var url = "/view/class/ais/aisschedulemanage/delete?random=" + Math.random();
                $.getJSON(url,data,function(result){
                    layer.close(layer_load);
                    loadOptRecord();
                    layer.alert(result.message);
                })
            });
        }
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
    
    function layerResultAndReload(result) {
        layer.close(layer_load);
        if (result.opSucc) {
            loadOptRecord();
            layer.msg(result.message, {
                time : 2000,
                skin : 'layer_msg_color_success'
            });
        } else {
            layer.msg(result.message, {
                time : 2000,
                skin : 'layer_msg_color_error'
            });
        }
    }

    function showLayer(divid, title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn : 0,
            area : [ '1000px', '720px' ],
            scrollbar: false,
            content : $("#" + divid)
        });
    }

    function validName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_name").focus();
            layer.tips('巡检组名称不能为空!', '#' + type + '_name', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }

    function sizeof(str) {
        var char = str.replace(/[^\x00-\xff]/g, '**');
        return char.length;
    }
});