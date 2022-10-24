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
        'stringutil': '/js/lcims/tool/stringutil',
        "echarts":'/js/echarts'
    }
});

require(['jquery','layer','laypage','resizewh','laydate','stringutil','echarts','echarts/chart/line'],
    function($,layer,laypage,resizewh,laydate,stringutil,ec) {
        var layer_load;
        var dayDate = $("#dayDate").val();
        var monthDate = $("#monthDate").val();
		resizewh.resizeBodyH($("#mainhistory"));
        butBindFunction();
        initChildrenMenu();

        laydate.render({
            elem: '#hourdate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,value: new Date()
            ,max: 0
            ,format: 'yyyy-MM-dd'
            ,type: 'date'
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
            var hourdate;
            var daydate;
            var monthdate;

            var query_type = $("#query_type").val();
            if(query_type=="2"){
                monthdate = $("#monthdate").val();
                if(null == monthdate || monthdate =="" ){
                    layer.tips('日期不能为空!', '#monthdate',{ tips: [2, '#EE1A23'] });
                    return;
                }
            } else if (query_type =="1"){
                daydate = $("#daydate").val();
                if(null == daydate || daydate =="" ){
                    layer.tips('日期不能为空!', '#daydate',{ tips: [2, '#EE1A23'] });
                    return;
                }
            } else {
                hourdate = $("#hourdate").val();
                if(null == hourdate || hourdate =="" ){
                    layer.tips('日期不能为空!', '#hourdate',{ tips: [2, '#EE1A23'] });
                    return;
                }
            }

            loadingwait();
            loadOptRecord();
        });

        $("#resetbutton").click(function() {
            reset("mainhistory");
            $("#hourdate").css('display','block');
            $("#daydate").empty();
            $("#daydate").css('display','none');
            $("#monthdate").empty();
            $("#monthdate").css('display','none');

            laydate.render({
                elem: '#hourdate' //指定元素
                ,lang: 'cn'
                // ,calendar: true
                ,value: new Date()
                ,max: 0
                ,format: 'yyyy-MM-dd'
                ,type: 'date'
                ,trigger: 'click' //采用click弹出
            });

        });

        $("#daydate").empty();
        $("#daydate").css('display','none');
        $("#monthdate").empty();
        $("#monthdate").css('display','none');
    }

    $("#query_type").change(function(){
        var type = $("#query_type").val();
        if(type=="2"){
            $("#monthdate").css('display','block');
            $("#daydate").empty();
            $("#daydate").css('display','none');
            $("#hourdate").empty();
            $("#hourdate").css('display','none');
            laydate.render({
                elem: '#monthdate' //指定元素
                ,lang: 'cn'
                ,max: 0
                ,value: monthDate
                ,format: 'yyyy-MM'
                ,type: 'month'
                ,trigger: 'click' //采用click弹出
                ,range: true
            });
        }else if(type=="1"){
            $("#daydate").css('display','block');
            $("#monthdate").empty();
            $("#monthdate").css('display','none');
            $("#hourdate").empty();
            $("#hourdate").css('display','none');
            laydate.render({
                elem: '#daydate' //指定元素
                ,lang: 'cn'
                ,max: 0
                ,value: dayDate
                ,format: 'yyyy-MM-dd'
                ,type: 'date'
                ,trigger: 'click' //采用click弹出
                ,range: true
            });
        } else {
            $("#hourdate").css('display','block');
            $("#daydate").empty();
            $("#daydate").css('display','none');
            $("#monthdate").empty();
            $("#monthdate").css('display','none');

            laydate.render({
                elem: '#hourdate' //指定元素
                ,lang: 'cn'
                ,max: 0
                ,value: new Date()
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
        var startdate;
        var enddate;
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 12;
        var query_type = $("#query_type").val();
        if(query_type=="2"){
            var dateArray = $("#monthdate").val().split(" - ");
            startdate = dateArray[0];
            enddate = dateArray[1];
        } else if (query_type =="1"){
            var dateArray = $("#daydate").val().split(" - ");
            startdate = dateArray[0];
            enddate = dateArray[1];
        } else {
            enddate = $("#hourdate").val();
        }

        loadingwait();
        var data = {
            'startDate': startdate, 'endDate': enddate, 'queryType':query_type
        };
        $.getJSON("/view/class/shcmreport/onlineuserstatictic/query?random=" + Math.random(), data, function (result) {
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
        $.getJSON("/view/class/shcmreport/onlineuserstatictic/queryChart?random=" + Math.random(), data, function (result) {
            lineData(result);
        });
    }

    //拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];

            rowdata = rowdata + "<tr><td title=\""+rowninfo.stime+"\">"+rowninfo.stime+"</td><td>"
                +rowninfo.mvalue+"</td></tr>";
        }
        $("#alarmhistorydiv").empty().append(rowdata);
    }

    //导出Excel
    function exportShow(){
        downloadExcel();
    }

    function downloadExcel(){
        var startdate;
        var enddate;
        var query_type = $("#query_type").val();
        if(query_type=="2"){
            var dateArray = $("#monthdate").val().split(" - ");
            startdate = dateArray[0];
            enddate = dateArray[1];
        } else if (query_type =="1"){
            var dateArray = $("#daydate").val().split(" - ");
            startdate = dateArray[0];
            enddate = dateArray[1];
        } else {
            enddate = $("#hourdate").val();
        }
        var url = "/view/class/shcmreport/onlineuserstatictic/export?startDate="+startdate+"&endDate="+enddate+"&queryType="+query_type;
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

    function lineData(onlineUserData) {
        $("#chartArea").empty();
        var infoHtml = "<div id='area1' style='width: 100%;height:400px;'></div>";
        $("#chartArea").append(infoHtml);
        // 基于准备好的dom，初始化echarts实例
        var myChart = ec.init(document.getElementById('area1'));
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: onlineUserData.title
            },
            xAxis: {
                type: 'category',
                data: onlineUserData.xRateData
            },
            yAxis: {
                type: 'value'
            },
            tooltip : {
                formatter:function(param){  //自定义提示框的文字
                    return "时间："+param.name+"<br>"
                        +"用户数：" +param.value;
                }
            },
            series : [
                {
                    name:'在线用户情况分析折线图',
                    type:'line',
                    smooth:true,
                    data: onlineUserData.yRateData
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
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
