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

        laydate.render({
            elem: '#startdate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,max: 0
            ,value: new Date()
            ,format: 'yyyy-MM'
            ,type: 'month'
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
            var type = $("#query_type").val();

            var querybusinesslinkdiv = $("#querybusinesslinkdiv option:selected").attr("value");
            var attr1 = $("#attr1 option:selected").attr("value");
            var attr2 = $("#attr2 option:selected").attr("value");
            if(type == 1){
                if(null == startDate || startDate =="" ){
                    layer.tips('日期不能为空!', '#startdate',{ tips: [2, '#EE1A23'] });
                    return;
                }
            }else{
                if(endDate == null || endDate =="") {
                    layer.tips('日期不能为空!', '#enddate', {tips: [2, '#EE1A23']});
                    return;
                }
            }
            if((null != querybusinesslinkdiv && querybusinesslinkdiv !="" )&& (null == attr1 || attr1 =="")){
                layer.tips('节点信息不能为空!', '#attr1',{ tips: [2, '#EE1A23'] });
            }else if(querybusinesslinkdiv == "/view/class/server/module/device/node/host" && (null == attr2 || attr2 =="" )){
                layer.tips('主机信息不能为空!', '#attr2',{ tips: [2, '#EE1A23'] });
            }else{
            	loadingwait();
                loadOptRecord(1);
            }
        });
        $("#resetbutton").click(function() {
            reset("mainhistory");
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#startdate").css('display','block');
            $("#querymonitortarget1").empty().append(rowdata);
            $("#querymonitortarget2").empty().append(rowdata);
            $("#querymonitortarget3").empty().append(rowdata);
            $("#querymonitortarget4").empty().append(rowdata);
            $("#enddate").empty();
            $("#querymetric").empty().append(rowdata);
            $("#querymonitortarget2").css('display','none');
            $("#querymonitortarget3").css('display','none');
            $("#querymonitortarget4").css('display','none');
            $("#enddate").css('display','none');

            laydate.render({
                elem: '#startdate' //指定元素
                ,lang: 'cn'
                // ,calendar: true
                ,max: 0
                ,value: new Date()
                ,format: 'yyyy-MM'
                ,type: 'month'
                ,trigger: 'click' //采用click弹出
            });

        });
        $("#detail_ok").click(function() {
            layer.closeAll();
        });
        queryinitialization();
        $("#enddate").empty();
        $("#enddate").css('display','none');

    }

    $("#query_type").change(function(){
        var type = $("#query_type").val();
        if(type=="0"){
            $("#enddate").css('display','block');
            $("#startdate").empty();
            $("#startdate").css('display','none');
            laydate.render({
                elem: '#enddate' //指定元素
                ,lang: 'cn'
                // ,calendar: true
                ,value: new Date()
                ,max: 0
                ,format: 'yyyy-MM-dd'
                ,type: 'date'
                ,trigger: 'click' //采用click弹出
            });
        }else{
            $("#startdate").css('display','block');
            $("#enddate").empty();
            $("#enddate").css('display','none');
            laydate.render({
                elem: '#startdate' //指定元素
                ,lang: 'cn'
                // ,calendar: true
                ,max: 0
                ,value: new Date()
                ,format: 'yyyy-MM'
                ,type: 'month'
                ,trigger: 'click' //采用click弹出
            });
        }
    });

    //查询初始化
    function queryinitialization(){
        $.ajaxSettings.async = false;
        $.getJSON("/view/class/system/alarmrulemanage/query/modulelist?random=" + Math.random(), function(result) {
            modulelistvar = result;
        });
        $.getJSON("/view/class/system/alarmrulemanage/query/mdalarmlevellist?random=" + Math.random(), function(result) {
            initializationalarmlevel = result;
        });
        $.getJSON("/view/class/system/alarmrulemanage/query/alarmmodeslist?random=" + Math.random(), function(result) {
            initializationalarmmodes = result;
        });
        //初始化模块
        querymoduleinitialization();
    }

    //初始化模块
    function querymoduleinitialization(){
        $("#querymodule").empty();
        var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        for(var i=0;i<modulelistvar.length;i++){
            rowdata = rowdata + "<option value="+modulelistvar[i].id+">"+modulelistvar[i].show_name+"</option>";
        }
        $("#querymodule").empty().append(rowdata);
        //模块选择事件
        querymodulechange();
    }

    //模块选择事件
    function querymodulechange(){
        $("#querymodule").change(function(){
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#querymonitortarget1").empty().append(rowdata);
            $("#querymonitortarget2").empty().append(rowdata);
            $("#querymonitortarget3").empty().append(rowdata);
            $("#querymonitortarget4").empty().append(rowdata);
            $("#querymonitortarget2").css('display','none');
            $("#querymonitortarget3").css('display','none');
            $("#querymonitortarget4").css('display','none');
            $("#querymetric").empty().append(rowdata);
            var id = $("#querymodule").val();
            if(id!=null && id!=""){
                //初始化监控目标一级下拉列表
                querymonitortargetoneinitialization();
            }
        });
    }

    //初始化监控目标一级下拉列表
    function querymonitortargetoneinitialization(){
        $("#querymonitortarget1").empty();
        var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        var id = $("#querymodule").val();
        var data= {id:id};
        $.ajaxSettings.async = false;
        $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetonelevellist?random=" + Math.random(),data, function(result) {
            monitortargetonelevellist = result;
            for(var i=0;i<monitortargetonelevellist.length;i++){
                rowdata = rowdata + "<option value="+monitortargetonelevellist[i].id+">"+monitortargetonelevellist[i].name+"</option>";
            }
            $("#querymonitortarget1").empty().append(rowdata);
        });
        //监控目标一级下拉列表选择事件
        querymonitortargetonechange();
    }

    //监控目标一级下拉列表选择事件
    function querymonitortargetonechange(){
        $("#querymonitortarget1").change(function(){
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#querymonitortarget2").empty().append(rowdata);
            $("#querymonitortarget3").empty().append(rowdata);
            $("#querymonitortarget4").empty().append(rowdata);
            $("#querymonitortarget2").css('display','none');
            $("#querymonitortarget3").css('display','none');
            $("#querymonitortarget4").css('display','none');
            var id = $("#querymonitortarget1").val();
            if(id!=null && id!=""){
                //初始化监控目标二级下拉列表
                querymonitortargettwoinitialization();
                //初始化监控目标三级下拉列表
                querymonitortargetthreeinitialization();
                //初始化指标下拉列表
                // querymetricinitialization();
            }
        });
    }

    //初始化监控目标二级下拉列表
    function querymonitortargettwoinitialization(){
        $("#querymonitortarget2").empty();
        var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        var id = $("#querymonitortarget1").val();
        for(var i=0;i<monitortargetonelevellist.length;i++){
            if(monitortargetonelevellist[i].id == id){
                var mdMenu = monitortargetonelevellist[i].mdMenu;
                var type = monitortargetonelevellist[i].type;
            }
        }
        var data= {name:mdMenu.name,dynamic:mdMenu.dynamic,type:type};
        $.ajaxSettings.async = false;
        $.getJSON("/view/class/system/alarmrulemanage/query/monitortargettwolevellist?random=" + Math.random(),data, function(result) {
            monitortargettwolevellist = result;
            if(monitortargettwolevellist.length>0){
                $("#querymonitortarget2").css('display','block');
            }else{
                $("#querymonitortarget2").css('display','none');
            }
            for(var i=0;i<monitortargettwolevellist.length;i++){
                rowdata = rowdata + "<option value="+monitortargettwolevellist[i].id+">"+monitortargettwolevellist[i].name+"</option>";
            }
            $("#querymonitortarget2").empty().append(rowdata);
        });
        //监控目标二级下拉列表选择事件
        querymonitortargettwochange();
    }

    //监控目标二级下拉列表选择事件
    function querymonitortargettwochange(){
        $("#querymonitortarget2").change(function(){
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#querymonitortarget4").empty().append(rowdata);
            $("#querymonitortarget4").css('display','none');
            var id = $("#querymonitortarget2").val();
            if(id!=null && id!=""){
                //初始化监控目标四级下拉列表
                querymonitortargetfourinitialization();
                //初始化指标下拉列表
                querymetricinitialization();
            }
        });
    }

    //初始化监控目标三级下拉列表
    function querymonitortargetthreeinitialization(){
        $("#querymonitortarget3").empty();
        var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        var id = $("#querymonitortarget1").val();
        var data= {id:id};
        $.ajaxSettings.async = false;
        $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetthreelevellist?random=" + Math.random(),data, function(result) {
            monitortargetthreelevellist = result;
            if(monitortargetthreelevellist.length>0){
                $("#querymonitortarget3").css('display','block');
            }else{
                $("#querymonitortarget3").css('display','none');
            }
            for(var i=0;i<monitortargetthreelevellist.length;i++){
                rowdata = rowdata + "<option value="+monitortargetthreelevellist[i].id+">"+monitortargetthreelevellist[i].name+"</option>";
            }
            $("#querymonitortarget3").empty().append(rowdata);
        });
        //监控目标三级下拉列表选择事件
        querymonitortargetthreechange();
    }

    //监控目标三级下拉列表选择事件
    function querymonitortargetthreechange(){
        $("#querymonitortarget3").change(function(){
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#querymonitortarget4").empty().append(rowdata);
            $("#querymonitortarget4").css('display','none');
            var id = $("#querymonitortarget3").val();
            if(id!=null && id!=""){
                //初始化监控目标四级下拉列表
                querymonitortargetfourinitialization();
                //初始化指标下拉列表
                // querymetricinitialization();
            }
        });
    }

    //初始化监控目标四级下拉列表
    function querymonitortargetfourinitialization(){
        $("#querymonitortarget4").empty();
        var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        var querymonitortarget1_id = $("#querymonitortarget1").val();
        for(var i=0;i<monitortargetonelevellist.length;i++){
            if(monitortargetonelevellist[i].id == querymonitortarget1_id){
                var monitorTargetOneDynamic = monitortargetonelevellist[i].mdMenu.dynamic;
                var monitorTargetOneType = monitortargetonelevellist[i].type;
            }
        }
        var querymonitortarget2_id = $("#querymonitortarget2").val();
        for(var i=0;i<monitortargettwolevellist.length;i++){
            if(monitortargettwolevellist[i].id == querymonitortarget2_id){
                var monitorTargetTwoId = monitortargettwolevellist[i].id;
            }
        }
        var querymonitortarget3_id = $("#querymonitortarget3").val();
        if(querymonitortarget3_id != null && querymonitortarget3_id != ""){
            for(var i=0;i<monitortargetthreelevellist.length;i++){
                if(monitortargetthreelevellist[i].id == querymonitortarget3_id){
                    var monitorTargetThreeDynamic = monitortargetthreelevellist[i].mdMenu.dynamic;
                    var monitorTargetThreeName = monitortargetthreelevellist[i].mdMenu.name;
                    var monitorTargetThreeType = monitortargetthreelevellist[i].type;
                }
            }
            var data= {monitorTargetOneDynamic:monitorTargetOneDynamic,
                monitorTargetOneType:monitorTargetOneType,
                monitorTargetTwoId:monitorTargetTwoId,
                monitorTargetThreeDynamic:monitorTargetThreeDynamic,
                monitorTargetThreeName:monitorTargetThreeName,
                monitorTargetThreeType:monitorTargetThreeType};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetfourlevellist?random=" + Math.random(),data, function(result) {
                monitortargetfourlevellist = result;
                if(monitortargetfourlevellist.length>0){
                    $("#querymonitortarget4").css('display','block');
                }else{
                    $("#querymonitortarget4").css('display','none');
                }
                for(var i=0;i<monitortargetfourlevellist.length;i++){
                    rowdata = rowdata + "<option value="+monitortargetfourlevellist[i].id+">"+monitortargetfourlevellist[i].name+"</option>";
                }
                $("#querymonitortarget4").empty().append(rowdata);
            });
            //监控目标四级下拉列表选择事件
            querymonitortargetfourchange();
        }else {
            $("#querymonitortarget4").empty().append(rowdata);
        }
    }

    //监控目标四级下拉列表选择事件
    function querymonitortargetfourchange(){
        $("#querymonitortarget4").change(function(){
            var id = $("#querymonitortarget4").val();
            if(id!=null && id!=""){
                //初始化指标下拉列表
                querymetricinitialization();
            }
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
                    // 导出绑定事件
                    if(result[i].url=='export'){
                        $("#"+result[i].name).click(function() {
                            exportShow();
                        });
                    }
                }
            }

        });
    }
    
    //初始化告警方式、告警方式列表信息
    function initAlarm(){
	   	var url = "/view/class/system/alarmquery/getAlarm";
	    $.getJSON(url+"?random=" + Math.random(), function(result) {
	    	$("#query_metricid").empty();
	        $("#query_metricid").append("<option value=\"\">请选择</option>");
	        for(var i = 0 ; i < result[0].length; i++) {
	        	$("#query_metricid").append("<option value=\""+result[0][i].id+"\">"+result[0][i].metric_name+"</option>");
	        }
	        $("#query_alarmlevel").empty();
	        $("#query_alarmlevel").append("<option value=\"\">请选择</option>");
	        for(var i = 0 ; i < result[1].length; i++) {
	        	$("#query_alarmlevel").append("<option value=\""+result[1][i].alarmlevel+"\">"+result[1][i].alarmname+"</option>");
	        } 
	    });
    }
    
	//加载查询内容
    function loadOptRecord(pageNumber) {
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        var metric_id = $("#query_metricid").val();
        var alarm_level = $("#query_alarmlevel").val();
        var querymodule = $("#querymodule").val();
        var query_type = $("#query_type").val();

        if(monitortargetonelevellist != null){
            for(var i=0;i<monitortargetonelevellist.length;i++){
                if(monitortargetonelevellist[i].id == $("#querymonitortarget1").val()){
                    var monitorTargetOneUrl = monitortargetonelevellist[i].mdMenu.url;
                    var monitorTargetOneType = monitortargetonelevellist[i].type;
                    var monitorTargetOneName = monitortargetonelevellist[i].name;
                }
            }
        }
        if(monitortargetthreelevellist != null){
            for(var i=0;i<monitortargetthreelevellist.length;i++){
                if(monitortargetthreelevellist[i].id == $("#querymonitortarget3").val()){
                    var monitorTargetThreeUrl = monitortargetthreelevellist[i].mdMenu.url;
                    var monitorTargetThreeType = monitortargetthreelevellist[i].type;
                    var monitorTargetThreeName = monitortargetthreelevellist[i].name;
                }
            }
        }
        var attr1 = $("#querymonitortarget2").val();
        var attr2 = $("#querymonitortarget4").val();

        loadingwait();
        var data = {
            'start_time': startdate, 'end_time': enddate, 'metric_id': metric_id, 'alarm_level': alarm_level, 'pageNumber':pageNumber,
            'name':querymodule, 'query_type':query_type,
            monitorTargetOneUrl:monitorTargetOneUrl,
            monitorTargetOneType:monitorTargetOneType,
            monitorTargetOneName:monitorTargetOneName,
            monitorTargetThreeUrl:monitorTargetThreeUrl,
            monitorTargetThreeType:monitorTargetThreeType,
            monitorTargetThreeName:monitorTargetThreeName,
            attr1:attr1,
            attr2:attr2
        };
        $.getJSON("/view/class/system/shcmalarmhisquery/query?random=" + Math.random(), data, function (result) {
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

                rowdata = rowdata + "<tr><td title=\""+rowninfo.name+"\">"+rowninfo.name
                +"</td><td title=\""+rowninfo.metric_name+"\">"+rowninfo.metric_name
                +"</td><td title=\""+rowninfo.alarm_level_name+"\">"+rowninfo.alarm_level_name
                +"</td><td title=\""+rowninfo.dimension_name+"\">"+rowninfo.dimension_name
                +"</td><td title=\""+rowninfo.alarmmsg+"\">"+rowninfo.alarmmsg
                +"</td><td title=\""+rowninfo.cycle_time+"\">"+rowninfo.cycle_time
                +"</td><td title=\""+stringutil.isNull(rowninfo.confirm_time)+"\">"+stringutil.isNull(rowninfo.confirm_time)
                +"</td><td title=\""+stringutil.isNull(rowninfo.confirm_name)+"\">"+stringutil.isNull(rowninfo.confirm_name)
                +"</td><td title=\""+stringutil.isNull(rowninfo.clear_time)+"\">"+stringutil.isNull(rowninfo.clear_time)
                +"</td><td>" + "<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\">详情</a>"
                +"</td></tr>";
            }
            $("#alarmhistorydiv").empty().append(rowdata);
            $("[name=detail]").each(function(){
                $(this).on('click',function(){
                	detailShow($(this).attr('id'));
                });
            });
        }
        
        //详情按钮事件
        function detailShow(id){
            reset("detail_div");
            var data = {id : id};
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/shcmalarmhisquery/queryDetail?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                	if(result.length>0){
                        var alarmhis = result[0];
                        $("#detail_id").val(id); 
                        $("#detail_name").val(alarmhis.name);
                        $("#detail_metric_name").val(alarmhis.metric_name);
                        $("#detail_alarm_level_name").val(alarmhis.alarm_level_name);
                    	$("#detail_dimension_name").val(alarmhis.dimension_name);
                    	$("#detail_alarmmsg").val(alarmhis.alarmmsg);
                        $("#detail_cycle_time").val(alarmhis.cycle_time);
                        $("#detail_confirm_time").val(alarmhis.confirm_time);
                        $("#detail_confirm_name").val(alarmhis.confirm_name);
                        $("#detail_clear_time").val(alarmhis.clear_time);
                        $("#detail_alarmtext").val(alarmhis.alarmtext);
                        showLayerDetail("detail_div",'告警历史信息详情');
                    }
                }
                
             });
        }
        
        function showLayerDetail(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '600px', '520px' ],
                content : $("#"+divid)
            });
        }

        //导出Excel
        function exportShow(){
            downloadExcel();
        }

        function downloadExcel(){
            var startdate = $("#startdate").val();
            var enddate = $("#enddate").val();
            var metric_id = $("#query_metricid").val();
            var alarm_level = $("#query_alarmlevel").val();
            var querymodule = $("#querymodule").val();
            var attr1 = $("#querymonitortarget2").val();
            var attr2 = $("#querymonitortarget4").val();
            var query_type = $("#query_type").val();

            var url = "/view/class/system/shcmalarmhisquery/export?metric_id="+metric_id+"&alarm_level="+alarm_level+"&name="
                +querymodule+"&attr1="+attr1+"&attr2="+attr2+"&start_time="+startdate+"&end_time="+enddate+"&query_type="+query_type;
            if(monitortargetonelevellist != null){
                for(var i=0;i<monitortargetonelevellist.length;i++){
                    if(monitortargetonelevellist[i].id == $("#querymonitortarget1").val()){
                        var monitorTargetOneUrl = monitortargetonelevellist[i].mdMenu.url;
                        var monitorTargetOneType = monitortargetonelevellist[i].type;
                        var monitorTargetOneName = monitortargetonelevellist[i].name;

                    }
                }
                url += "&monitorTargetOneUrl="+monitorTargetOneUrl+"&monitorTargetOneType="
                    +monitorTargetOneType+"&monitorTargetOneName="+monitorTargetOneName;
            }
            if(monitortargetthreelevellist != null){
                for(var i=0;i<monitortargetthreelevellist.length;i++){
                    if(monitortargetthreelevellist[i].id == $("#querymonitortarget3").val()){
                        var monitorTargetThreeUrl = monitortargetthreelevellist[i].mdMenu.url;
                        var monitorTargetThreeType = monitortargetthreelevellist[i].type;
                        var monitorTargetThreeName = monitortargetthreelevellist[i].name;
                    }
                }
                url += "&monitorTargetThreeUrl="+monitorTargetThreeUrl+"&monitorTargetThreeType="
                    +monitorTargetThreeType+"&monitorTargetThreeName="+monitorTargetThreeName;
            }


            window.open(url+"&random=" + Math.random(),"_blank");
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
