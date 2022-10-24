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
        'stringutil': '/js/lcims/tool/stringutil',
        'select2': '/js/select2/select2.min'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','laydate','stringutil','select2'],
    function($,layer,laypage,resizewh,checkbox,laydate,stringutil,select2) {
        var modulelistvar;
        var initializationalarmlevel;
        var initializationalarmmodes;
        var monitortargetonelevellist;
        var monitortargettwolevellist;
        var monitortargetthreelevellist;
        var monitortargetfourlevellist;

        var layer_load;
        var ruleId;
        var apnName;

		resizewh.resizeBodyH($("#mainhistory"));
        butBindFunction();
        initAlarm();
        initChildrenMenu();
        apnNameList();

        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','alarmhistorydiv');
        });

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

            $("#modify_ok").click(function() {
                modifyInfo();
            });
            $("#modify_cancle").click(function() {
                layer.closeAll();
            });
            $("#add_ok").click(function() {
                addInfo();
            });
            $("#add_cancle").click(function() {
                layer.closeAll();
            });
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

        queryinitialization();
        $("#enddate").empty();
        $("#enddate").css('display','none');

        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','alarmhistorydiv');

    }

    function bindCheckBox(){
        $("#alarmhistorydiv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
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

    //初始化指标下拉列表
    function apnNameList(){
        $("#add_apn_id").empty();
        var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        $.ajaxSettings.async = false;
        $.getJSON("/view/class/system/currentlimiting/queryApnRecord?random=" + Math.random(), function(result) {
            apnName = result;
            if(apnName.length>0){
                for(var x=0;x<apnName.length;x++){
                    rowdata = rowdata + "<option value="+apnName[x].apnid+">"+apnName[x].apn+"</option>";
                }
            }
            $("#add_apn_id").empty().append(rowdata);
            $("#modify_apn_id").empty().append(rowdata);
            $('#add_apn_id').select2({dropdownParent:$('#add_div')});
        });
    }

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
                    //限流按钮
                    if(result[i].url=='limit'){
                        $("#"+result[i].name).click(function() {
                            limitShow();
                        });
                    }
                    //取消限流按钮
                    if(result[i].url=='cancellimit'){
                        $("#"+result[i].name).click(function() {
                            deleteShow();
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
        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();
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
        $.getJSON("/view/class/system/alarmhisquery/query?random=" + Math.random(), data, function (result) {
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
            var id = rowninfo.id;
            var checked = "";

            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }

            rowdata = rowdata + "<tr>" +
                "<td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"+"</td>" +
                "<td title=\""+rowninfo.name+"\">"+rowninfo.name+"</td><td title=\""+rowninfo.metric_name+"\">" +rowninfo.metric_name+"</td>" +
                "<td title=\""+rowninfo.alarm_level_name+"\">"+rowninfo.alarm_level_name+"</td>" +
                "<td title=\""+rowninfo.dimension_name+"\">"+rowninfo.dimension_name+"</td>" +
                "<td title=\""+rowninfo.alarmmsg+"\">" +rowninfo.alarmmsg+"</td>" +
                "<td title=\""+rowninfo.cycle_time+"\">"+rowninfo.cycle_time+"</td>" +
                "<td title=\""+stringutil.isNull(rowninfo.cycle_time)+"\">"+stringutil.isNull(rowninfo.metric_original)+"</td>" +
                "<td title=\""+stringutil.isNull(rowninfo.confirm_time)+"\">" +stringutil.isNull(rowninfo.confirm_time)+"</td>" +
                "<td title=\""+stringutil.isNull(rowninfo.confirm_name)+"\">"+stringutil.isNull(rowninfo.confirm_name)+"</td>" +
                "<td title=\""+stringutil.isNull(rowninfo.clear_time)+"\">"+stringutil.isNull(rowninfo.clear_time)+"</td>" +
                "</tr>";
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

    //导出Excel
    function exportShow(){
        downloadExcel();
    }

    //判断变量是否存在于数组中
    function IsInArray(arr,val) {
        var testStr=',' + arr.join(",") + ",";
        return testStr.indexOf("," + val + ",") != -1;
    }

    //限流按钮
    function limitShow(){
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个apn!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var id = checkboxArray[0];
            reset("modify_div");
            reset("add_div");
            $("#add_apn_id").val(null).trigger("change");   //清除选中项

            var data = {id : id};
            var url = "/view/class/system/alarmhisquery/query?random=" + Math.random();
            $.getJSON(url,data,function(result){
                var metric_id = result.pageList[0].metric_id;
                var apn_id = result.pageList[0].dimension1;
                var data = {"apnId":apn_id};
                if (metric_id == "7c15400914c54182a6be26fb153fea2e"){
                    $.getJSON("/view/class/system/currentlimiting/query?random=" + Math.random(),data, function(result) {
                        if(result.length > 0){
                            ruleId = result[0].ruleId;
                            var apn = result[0];
                            $("#modify_apn_id").val(apn.apnId);
                            $("#modify_limit_cycle").val(apn.limit_cycle);
                            $("#modify_auth_value").val(apn.auth_value);
                            $("#modify_log_value").val(apn.log_value);
                            $("#modify_record_value").val(apn.record_value);
                            $("#modify_day_value").val(apn.day_value);
                            showLayer("modify_div",'修改apn');
                        }else {
                            showLayer("add_div",'新增apn');
                        }
                    });
                }else {
                    layer.msg("该条数据不可以限流!",{
                        time:2000,
                        skin: 'layer_msg_color_alert'
                    });
                }
            });
        }
    }

    //限流按钮修改确认
    function modifyInfo(){
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个apn!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var apn_id = stringutil.Trim($("#modify_apn_id").val());
            var limit_cycle = stringutil.Trim($("#modify_limit_cycle").val());
            var auth_value = stringutil.Trim($("#modify_auth_value").val());
            var log_value = $("#modify_log_value").val();
            var record_value = $("#modify_record_value").val();
            var day_value = $("#modify_day_value").val();
            if(
                stringutil.checkString('modify_apn_id',apn_id,"apn名称不能为空!") ||
                stringutil.checkString('modify_limit_cycle',limit_cycle,"限流周期不能为空!") ||
                stringutil.checkString('modify_auth_value',auth_value,"认证限流阀值不能为空!") ||
                stringutil.checkString('modify_log_value',log_value,"日志限流阀值不能为空!") ||
                stringutil.checkString('modify_record_value',record_value,"记录次数限流阀值不能为空!") ||
                stringutil.checkString('modify_day_value',day_value,"每日限流阀值不能为空!")){
                return;
            }
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/currentlimiting/modifyApn?random=" + Math.random(),
                cache: false,
                async: false,
                data:{
                    ruleId:ruleId,
                    apnId:apn_id,
                    limit_cycle:limit_cycle,
                    auth_value:auth_value,
                    log_value:log_value,
                    record_value:record_value,
                    day_value:day_value
                },
                success:function (result) {
                    var data = result;
                    if (data.ret == "0"){
                        var dataModel = {
                            ruleId:ruleId,
                            apnId:apn_id,
                            limit_cycle:limit_cycle,
                            auth_value:auth_value,
                            log_value:log_value,
                            record_value:record_value,
                            day_value:day_value
                        };
                        $.getJSON("/view/class/system/currentlimiting/modify?random=" + Math.random(),dataModel, function(result) {
                            layer.closeAll();
                            layerResultAndReload(result);
                        });
                    }else {
                        layer.closeAll();
                        layer.msg(data.desc,{
                            time:2000,
                            skin: 'layer_msg_color_error'
                        });
                    }
                }
            });
        }
    }

    //限流按钮新增确认
    function addInfo(){
        var apn_id = stringutil.Trim($("#add_apn_id").val());
        var limit_cycle = stringutil.Trim($("#add_limit_cycle").val());
        var auth_value = stringutil.Trim($("#add_auth_value").val());
        var log_value = $("#add_log_value").val();
        var record_value = $("#add_record_value").val();
        var day_value = $("#add_day_value").val();

        if(
            stringutil.checkString('add_apn_id',apn_id,"apn名称不能为空!") ||
            stringutil.checkString('add_limit_cycle',limit_cycle,"限流周期不能为空!") ||
            stringutil.checkString('add_auth_value',auth_value,"认证限流阀值不能为空!") ||
            stringutil.checkString('add_log_value',log_value,"日志限流阀值不能为空!") ||
            stringutil.checkString('add_record_value',record_value,"记录次数限流阀值不能为空!") ||
            stringutil.checkString('add_day_value',day_value,"每日限流阀值不能为空!")){
            return;
        }
        $.ajax({
            type: "post",
            url: "/view/class/system/currentlimiting/addApn?random=" + Math.random(),
            cache: false,
            async: false,
            data:{
                apnId:apn_id,
                limit_cycle:limit_cycle,
                auth_value:auth_value,
                log_value:log_value,
                record_value:record_value,
                day_value:day_value
            },
            success:function (result) {
                var data = result;
                if (data.ret == "0"){
                    //校验apn名称是否重复
                    $.ajax({
                        type: "post",
                        url: "/view/class/system/currentlimiting/query?random=" + Math.random(),
                        cache: false,
                        async: false,
                        data: {"apnId": apn_id},
                        success: function (result) {
                            var length = result.length;
                            if(length > 0){
                                $("#add_apn_id").focus();
                                layer.tips("apn不能重复", '#add_apn_id',{ tips: [2, '#EE1A23'] });
                                return;
                            }else{
                                loadingwait();
                                $.ajax({
                                    type: "post",
                                    url: "/view/class/system/currentlimiting/add?random=" + Math.random(),
                                    cache: false,
                                    async: false,
                                    data:{
                                        apnId:apn_id,
                                        limit_cycle:limit_cycle,
                                        auth_value:auth_value,
                                        log_value:log_value,
                                        record_value:record_value,
                                        day_value:day_value
                                    },
                                    success:function (result) {
                                        layer.closeAll();
                                        addLayerResultAndReload(result);
                                    }
                                });
                                return;
                            }
                        }
                    });
                }else {
                    layer.closeAll();
                    layer.msg(data.desc,{
                        time:2000,
                        skin: 'layer_msg_color_error'
                    });
                }
            }
        });
    }

    //取消限流
    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择至少一个apn!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var id = checkboxArray[0];
            var data = {id : id};
            var url = "/view/class/system/alarmhisquery/query?random=" + Math.random();
            $.getJSON(url,data,function(result){
                var metric_id = result.pageList[0].metric_id;
                var apn_id = result.pageList[0].dimension1;
                var data = {"apnId":apn_id};
                //先查询告警历史表，判断metric_id是不是"7c15400914c54182a6be26fb153fea2e"
                if (metric_id == "7c15400914c54182a6be26fb153fea2e"){
                    //如果是的话，根据dimension1字段查询规则表的数据
                    $.getJSON("/view/class/system/currentlimiting/query?random=" + Math.random(),data, function(result) {
                        if(result.length > 0){
                            var apn = result[0];
                            var arr = [];
                            arr.push(apn.apnId);
                            layer.confirm('是否确认删除该批次数据？', {
                                closeBtn:0,
                                title: '询问',
                                btn: ['确认','取消'] //按钮
                            },function(){
                                layer.closeAll();
                                loadingwait();
                                $.ajax({
                                    type: "post",
                                    url: "/view/class/system/currentlimiting/cancelApn?random=" + Math.random(),
                                    cache: false,
                                    async: false,
                                    data:{apnArray : arr},
                                    success:function (result) {
                                        var data = result;
                                        if (data.ret == "0"){
                                            var data = {apnIdArray:arr};
                                            var url = "/view/class/system/currentlimiting/delete/?random=" + Math.random();
                                            $.getJSON(url,data,function(result){
                                                layer.close(layer_load);
                                                loadOptRecord();
                                                layer.alert(result.message);
                                            })
                                        }else {
                                            layer.close(layer_load);
                                            layer.msg(data.desc,{
                                                time:2000,
                                                skin: 'layer_msg_color_error'
                                            });
                                        }
                                    }
                                });
                            });
                        }else {
                            layer.msg("该条数据没有限流规则!",{
                                time:2000,
                                skin: 'layer_msg_color_alert'
                            });
                        }
                    });
                }else {
                    layer.msg("该条数据不可以取消限流!",{
                        time:2000,
                        skin: 'layer_msg_color_alert'
                    });
                }
            });
        }
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

        var url = "/view/class/system/alarmhisquery/export?metric_id="+metric_id+"&alarm_level="+alarm_level+"&name="
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
            area : [ '560px', '250px' ],
            content : $("#"+divid)
        });
    }

    function addLayerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
            loadOptRecord();
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
});
