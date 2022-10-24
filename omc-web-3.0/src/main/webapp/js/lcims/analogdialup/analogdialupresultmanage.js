require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'laydate' : '/js/laydate/laydate',
        'layer':'/js/layer/layer',
        "laypage": "/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh','checkbox','laydate','stringutil' ],
    function($, layer, laypage, resizewh,checkbox,laydate,stringutil) {
        var layer_load;

        resizewh.resizeBodyH($("#mainanalogdialup"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','analogdialupdiv');
        });
        reset("mainanalogdialup");
        initChildrenMenu();
        loadingwait();
        loadOptRecord(1);

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
        	var startDate = $("#startdate").val();
            var endDate = $("#enddate").val();
            if(startDate!=""&&endDate!=""&&startDate>endDate){
            	layer.tips('结束日期不能小于开始日期!', '#enddate',{ tips: [2, '#EE1A23'] });
            }else{
            	queryOpt();
            }
        });
        $("#resetbutton").click(function() {
            reset("mainanalogdialup");
        });
        $("#addbutton").click(function() {
            addShow();
        });
        $("#add_ok").click(function() {
            addInfo();
        });
        $("#add_cancle").click(function() {
            layer.closeAll();
        });
        $("#modify_ok").click(function() {
            modifyInfo();
        });
        $("#modify_cancle").click(function() {
            layer.closeAll();
        });
        $("#detail_ok").click(function() {
            layer.closeAll();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','analogdialupdiv');
    }

    laydate.render({
        elem: '#startdate' //指定元素
        ,lang: 'cn'
        // ,calendar: true
        ,max: 0
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
        ,max: 0
        ,format: 'yyyy-MM-dd HH:mm:ss'
        ,type: 'datetime'
        ,trigger: 'click' //采用click弹出
    });
    
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
        $("#"+divid+" input[type='text']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" input[type='password']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" input[type='number']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" select").each(function(){
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
    }

    function bindCheckBox(){
        $("#analogdialupdiv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
    }

    // 查询按钮事件
    function queryOpt() {
        loadingwait();
        loadOptRecord(1);
    }
    // 加载查询内容
    function loadOptRecord(pageNumber) {

        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();

        var host_name = stringutil.Trim($("#host_name").val());
        var host_ip = stringutil.Trim($("#host_ip").val());
        var username = stringutil.Trim($("#username").val());
        var returncode = stringutil.Trim($("#returncode").val());
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        var data = {
            'host_name' : host_name,
            'host_ip' : host_ip,
            'username' : username,
            'returncode' : returncode,
            'startdate' : startdate,
            'enddate' : enddate
            ,pageNumber:pageNumber
        };
        $.getJSON("/view/class/system/analogdialupresultmanage/query?random=" + Math.random(), data, function(result) {
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
                resizewh.resizeBodyH($("#mainanalogdialup"));
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    loadOptRecord(obj.curr);
                }
            }
        });
    }
    
    
    // 拼接tr
    function showTable(data, startnum, endnum) {
    	var rowdata = "";
        //计算选中条数
        var k = 0;
        for(var i=0;i<=endnum-startnum;i++){
        	var rowninfo = data[i];
            var id = rowninfo.id;

            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata
                + "<tr><td>" +"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"
                + "</td><td title=\""+ stringutil.isNull(rowninfo.host_name) +"\">" + stringutil.isNull(rowninfo.host_name)
                + "</td><td title=\""+stringutil.isNull(rowninfo.host_ip)+"\">" + stringutil.isNull(rowninfo.host_ip)
                + "</td><td title=\""+stringutil.isNull(rowninfo.username)+"\">" + stringutil.isNull(rowninfo.username)
                + "</td><td title=\""+stringutil.isNull(rowninfo.dial_up_time)+"\">" + stringutil.isNull(rowninfo.dial_up_time)
                + "</td><td title=\""+stringutil.isNull(rowninfo.returncode_name)+"\">" + stringutil.isNull(rowninfo.returncode_name)
                + "</td><td title=\""+stringutil.isNull(rowninfo.errordesc)+"\">" + stringutil.isNull(rowninfo.errordesc)
                + "</td><td><a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\">详情</a></td></tr>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }
        $("#analogdialupdiv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("analogdialupdiv");
    }
    
  //详情按钮事件
    function detailShow(analogdialupid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/analogdialupresultmanage/query?id="+analogdialupid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	var pageList = result.pageList;
            	if(pageList.length>0){
                    var analogdialup = pageList[0];
                    $("#detail_host_name").val(analogdialup.host_name);
                    $("#detail_host_ip").val(analogdialup.host_ip);
                    $("#detail_min_rate_name").val(analogdialup.min_rate_name);
                    $("#detail_username").val(analogdialup.username);
                    $("#detail_nas_port").val(analogdialup.nas_port);
                    $("#detail_call_from_id").val(analogdialup.call_from_id);
                    $("#detail_call_to_id").val(analogdialup.call_to_id);
                    $("#detail_ext").val(analogdialup.ext);
                    $("#detail_command_name").val(analogdialup.command_name);
                    $("#detail_serialno").val(analogdialup.serialno);
                    $("#detail_returncode").val(analogdialup.returncode);
                    $("#detail_errordesc").val(analogdialup.errordesc);
                    $("#detail_returncode_name").val(analogdialup.returncode_name);
                    $("#detail_calltime").val(analogdialup.calltime);
                    showLayerDetail("detail_div",'模拟拨测详情');
                }
            }
         });
    }
    
    function showLayerDetail(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '620px', '400px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
    
    function layerResultAndReload(result) {
        layer.close(layer_load);
        if (result.opSucc) {
        	var page_curr = $("#page_curr").val();
        	loadOptRecord(page_curr);
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
            area : [ '550px', '510px' ],
            content : $("#" + divid)
        });
    }
});