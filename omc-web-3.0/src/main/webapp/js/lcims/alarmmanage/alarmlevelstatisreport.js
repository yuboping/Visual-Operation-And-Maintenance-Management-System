require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'laydate' : '/js/laydate/laydate',
        'layer':'/js/layer/layer',
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require(['jquery','layer','laypage','resizewh','laydate','stringutil'],
    function($,layer,laypage,resizewh,laydate,stringutil) {
        var layer_load;

		resizewh.resizeBodyH($("#mainhistory"));
        butBindFunction();
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

            if(type == 2){
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

            loadingwait();
            loadOptRecord();
        });

        $("#resetbutton").click(function() {
            reset("mainhistory");
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

        });

        $("#enddate").empty();
        $("#enddate").css('display','none');
    }

    $("#query_type").change(function(){
        var type = $("#query_type").val();
        if(type=="2"){
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
        }else{
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
        }
    });

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
    
	//加载查询内容
    function loadOptRecord() {
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 12;

        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        var query_type = $("#query_type").val();

        loadingwait();
        var data = {
            'start_time': startdate, 'end_time': enddate, 'query_type':query_type
        };
        $.getJSON("/view/class/mainttool/alarmstatisquery/query?random=" + Math.random(), data, function (result) {
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
                    resizewh.resizeBodyH($("#mainarea"));
                },
                groups : page_count
                // 连续显示分页数
            });
        });
    }

    //拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];

            rowdata = rowdata + "<tr><td title=\""+rowninfo.alarm_time+"\">"+rowninfo.alarm_time+"</td><td>"
                +rowninfo.alarm_level_normal+"</td><td>"+rowninfo.alarm_level_warn+"</td><td>"
                +rowninfo.alarm_level_serious+"</td></tr>";
        }
        $("#alarmhistorydiv").empty().append(rowdata);
    }

    //导出Excel
    function exportShow(){
        downloadExcel();
    }

    function downloadExcel(){
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        var query_type = $("#query_type").val();

        var url = "/view/class/mainttool/alarmstatisquery/export?start_time="+startdate+"&end_time="+enddate+"&query_type="+query_type;
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
