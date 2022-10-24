require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'laydate' : '/js/laydate/laydate',
        'layer':'/js/layer/layer',
        'checkbox': '/js/lcims/tool/checkbox',
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','laydate','stringutil'],
    function($,layer,laypage,resizewh,checkbox,laydate,stringutil) {
        var modulelistvar;
        var initializationalarmlevel;
        var initializationalarmmodes;
        var monitortargetonelevellist;
        var monitortargettwolevellist;
        var monitortargetthreelevellist;
        var monitortargetfourlevellist;

        var layer_load;

		resizewh.resizeBodyH($("#mainhistory"));
        butBindFunction();
        initAlarm();
        initChildrenMenu();
        
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','alarmhistorydiv');
        });

        laydate.render({
            elem: '#startdate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,max: 1
            // ,value: new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
            ,format: 'yyyy-MM-dd HH:mm:ss'
            ,type: 'datetime'
            ,trigger: 'click' //采用click弹出
        });

        laydate.render({
            elem: '#enddate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            // ,value: new Date()
            ,max: 1
            ,format: 'yyyy-MM-dd HH:mm:ss'
            ,type: 'datetime'
            ,trigger: 'click' //采用click弹出
        });

        reset("mainhistory");
        
//----------------------------------以下为自定义方法-------------------------------------------------//
        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
        }
        
        function butBindFunction(){
        $("#querybutton").click(function() {
        	var startDate = $("#startdate").val();
            var endDate = $("#enddate").val();
            if(startDate!=""&&endDate!=""&&startDate>endDate){
            	layer.tips('结束日期不能小于开始日期!', '#enddate',{ tips: [2, '#EE1A23'] });
            }else{
            	loadingwait();
                loadOptRecord(1);
            }
        });

        $("#resetbutton").click(function() {
            reset("mainhistory");
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#querymonitortarget1").empty().append(rowdata);
            $("#querymonitortarget2").empty().append(rowdata);
            $("#querymonitortarget3").empty().append(rowdata);
            $("#querymonitortarget4").empty().append(rowdata);
            $("#querymetric").empty().append(rowdata);
            $("#querymonitortarget2").css('display','none');
            $("#querymonitortarget3").css('display','none');
            $("#querymonitortarget4").css('display','none');
        });
        $("#detail_ok").click(function() {
            layer.closeAll();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','alarmhistorydiv');

        queryinitialization();

    }

    //查询初始化
    function queryinitialization(){
        $.ajaxSettings.async = false;
        $.getJSON("/view/class/system/alarmrulemanage/query/mdalarmlevellist?random=" + Math.random(), function(result) {
            initializationalarmlevel = result;
        });
    }

    //重置页面标签内容
    function reset(divid){
        $("#"+divid+" input[type='text']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" select").each(function(){
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
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
                    if(result[i].url=='confirm'){
                        $("#"+result[i].name).click(function() {
                            confirmShow();
                        });
                    }
                }
            }

        });
    }
    
    //初始化告警方式、告警类型列表信息
    function initAlarm(){
	   	var url = "/view/class/mainttool/realtime/getAlarm";
	    $.getJSON(url+"?random=" + Math.random(), function(result) {
	        $("#query_alarmlevel").empty();
	        $("#query_alarmlevel").append("<option value=\"\">请选择</option>");
	        for(var i = 0 ; i < result.length; i++) {
	        	$("#query_alarmlevel").append("<option value=\""+result[i].alarmlevel+"\">"+result[i].alarmname+"</option>");
	        } 
	    });
        $.getJSON("/view/class/mainttool/realtime/getAlarmTypeList?random=" + Math.random(), function(result) {
            $("#query_alarmtype").empty();
            $("#query_alarmtype").append("<option value=\"\">请选择</option>");
            for(var i = 0 ; i < result.length; i++) {
                $("#query_alarmtype").append("<option value=\""+result[i].CODE+"\">"+result[i].DESCRIPTION+"</option>");
            }
        });
    }

    function bindCheckBox(){
        $("#areadiv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
    }
    
	//加载查询内容
    function loadOptRecord(pageNumber) {
        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();

        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        var alarm_level = $("#query_alarmlevel").val();
        var alarm_type = $("#query_alarmtype").val();

        loadingwait();
        var data = {
            'start_time': startdate, 'end_time': enddate, 'alarm_level': alarm_level, 'alarm_type': alarm_type, 'pageNumber':pageNumber
        };
        $.getJSON("/view/class/mainttool/realtime/query?random=" + Math.random(), data, function (result) {
            layer.close(layer_load);
            resetPage(result);
        });
    }

        //重置分页(跳转分页)
        function resetPage(result) {
            var totalCount = result.totalCount;
            var pageList = result.pageList;
            var start = result.start;
            var end = result.end;
            var pageNumber = result.pageNumber;
            var totalPages = result.totalPages;
            $("#querynum").text(totalCount);
            laypage({
                cont: "pageinfo", //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                pages: totalPages, //通过后台拿到的总页数
                curr: pageNumber, //当前页
                jump: function (obj, first) { //触发分页后的回调
                    showTable(pageList,start,end);
                    $("#currnum").text( start + "-" + end);
                    if(totalCount==0){
                        $("#currnum").empty().text("0 ");
                    }
                    $("#page_curr").val(obj.curr);
                    resizewh.resizeBodyH($("#mainhistory"));
                    if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                        loadOptRecord(obj.curr);
                    }
                }
            });
        }

        //拼接tr
        function showTable(data,startnum,endnum){
            var rowdata = "";
            var k = 0;
            for(var i=0;i<=endnum-startnum;i++){
                var rowninfo = data[i];
                var id = rowninfo.alarm_id;

                var disabled="disabled=\"disabled\"";
                var checked = "checked=\"checked\"";
                if(rowninfo.clear_status != 1){
                    disabled = "";
                    checked = "";
                }
                if(checkbox.isExitArray(id)){
                    checked = "checked=\"checked\"";
                    k++;
                }
                rowdata = rowdata + "<tr>" +
                    "<td><input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.alarm_id+"\" id=\""+rowninfo.alarm_id+"\" "+checked+" "+disabled+" />" +
                    "<td title=\""+rowninfo.alarm_level+"\">"+stringutil.isNull(rowninfo.alarm_level)+"</td>" +
                    "<td title=\""+rowninfo.alarm_type+"\">"+stringutil.isNull(rowninfo.alarm_type)+"</td>" +
                    "<td title=\""+rowninfo.host_name+"\">"+stringutil.isNull(rowninfo.host_name)+"</td>" +
                    "<td title=\""+rowninfo.alarm_msg+"\">"+stringutil.isNull(rowninfo.alarm_msg)+"</td>" +
                    "<td title=\""+rowninfo.create_time+"\">"+stringutil.isNull(rowninfo.create_time)+"</td>";
            }
            var ct = endnum-startnum+1;
            if(k==ct&&k>0){
                //全选框选中
                $("#checkboxAll").prop("checked", true);
            }else{
                $("#checkboxAll").prop("checked", false);
            }
            $("#alarmhistorydiv").empty().append(rowdata);
            checkbox.bindSingleCheckbox("alarmhistorydiv");
        }

        //确认告警信息
        function confirmShow() {
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length == 0){
                layer.msg("请选择一个告警信息!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                layer.confirm('是否确认该批次数据？', {
                    closeBtn:0,
                    title: '询问',
                    btn: ['确认','取消'] // 按钮
                },function(){
                    layer.closeAll();
                    loadingwait();
                    var data = {alarmArray:checkboxArray};
                    var url = "/view/class/mainttool/realtime/confirmAlarm?random=" + Math.random();
                    $.getJSON(url,data,function(result){
                        layer.close(layer_load);
                        layerResultAndReload(result);
                        layer.alert(result.message);
                    })
                });
            }
        }

        // 详情按钮事件
        function detailShow(id){
            reset("detail_div");
            loadingwait();

            loadHisAlarmList(id);
            showLayer("detail_div",'告警历史');
        }

        function loadHisAlarmList(id){
            var data = {alarm_id:id};
            // 分页显示的初始化数据
            var pagecount = 0;
            var page_count = 10;
            $.ajax({
                type: "post",
                url: "/view/class/system/alarmquery/hisalarmlist?random=" + Math.random(),
                data: data,
                dataType: "json",
                cache: false,
                async: false,
                success: function (result) {
                    layer.close(layer_load);
                    total = result.length;
                    pagecount = Math.ceil(total / page_count);
                    $("#hisquerynum").text(total);
                    laypage({
                        cont: 'hispageinfo',
                        skin: '#6AB0F4',
                        pages: pagecount,
                        curr: 1,
                        skip: false, // 是否开启跳页
                        jump: function (obj, first) { // 触发分页后的回调
                            startnum = (obj.curr - 1) * page_count + 1;
                            endnum = obj.curr * page_count;
                            endnum = endnum > total ? total : endnum;
                            showHisTable(result, startnum, endnum);
                            $("#hiscurrnum").text(startnum + "-" + endnum);
                            if (total == 0) {
                                $("#hiscurrnum").empty().text("0 ");
                            }
                        },
                        groups: page_count
                    });
                }
            });

        }

        // 拼接tr
        function showHisTable(data, startnum, endnum) {
            var rowdata = "";
            for(var i=startnum;i<=endnum;i++){
                var rowninfo = data[i-1];

                rowdata = rowdata + "<tr><td title=\""+rowninfo.name+"\">" + rowninfo.name + "</td><td title=\""+rowninfo.metric_name+"\">"
                    +rowninfo.metric_name+"</td><td title=\""+rowninfo.dimension_name+"\">"+rowninfo.dimension_name+"</td><td>"+rowninfo.alarm_level_name+"</td><td title=\""+rowninfo.alarmmsg+"\">"
                    +rowninfo.alarmmsg+"</td><td title=\""+rowninfo.cycle_time+"\">"+rowninfo.cycle_time+"</td></tr>";
            }
            //本页条数
            $("#hisalarmdiv").empty().append(rowdata);
        }

        function layerResultAndReload(result){
            layer.close(layer_load);
            if(result.opSucc){
                var page_curr = $("#page_curr").val();
                loadOptRecord(page_curr);
                layer.msg(result.message,{
                    time:2000,
                    skin: 'layer_msg_color_success'
                });
            }else{
                layer.msg(result.message,{
                    time:2000,
                    skin: 'layer_msg_color_error'
                });
            }
        }

        function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '900px', '595px' ],
                content : $("#"+divid)
            });
        }
});
