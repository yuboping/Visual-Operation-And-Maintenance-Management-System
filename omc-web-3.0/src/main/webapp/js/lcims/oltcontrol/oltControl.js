$(document).ready(function(){
    $('#oltAddressType').multiselect({
        buttonWidth: '240px',
        selectAllText: '全选',
        buttonText: function(options, select) {
            if (options.length === 0) {
                return '请选择...';
            } else if (options.length > 3) {
                return '已选择' + options.length + '条数据 ...';
            } else {
                var labels = [];
                options.each(function() {
                    if ($(this).attr('label') !== undefined) {
                        labels.push($(this).attr('label'));
                    }
                    else {
                        labels.push($(this).html());
                    }
                });
                return labels.join(', ') + '';
            }
        },
        includeSelectAllOption: true,
        disableIfEmpty: true
    });
});

$("#resetbutton").click(function() {
    $('#oltAddressType').multiselect('deselectAll', false);
    $('#oltAddressType').multiselect('updateButtonText');
});

require.config({
    paths: {
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'multiselect':'/js/multiple/bootstrap-multiselect',
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

        resizewh.resizeBodyH($("#mainhistory"));
        butBindFunction();
        initChildrenMenu();

        laydate.render({
            elem: '#date' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,max: 1
            ,value: new Date(new Date().getTime())
            ,format: 'yyyy-MM-dd'
            ,type: 'date'
            ,trigger: 'click' //采用click弹出
        });

//----------------------------------以下为自定义方法-------------------------------------------------//
        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
        }

        function butBindFunction(){
            $("#querybutton").click(function() {
                var date = $("#date").val();
                if (null == date || date == ""){
                    layer.tips('日期不能为空!', '#date',{ tips: [2, '#EE1A23'] });
                } else {
                    loadingwait();
                    loadOptRecord(1);
                }
            });
            $("#resetbutton").click(function() {
                reset("mainhistory");
            });

        }

        //重置页面标签内容
        function reset(divid){
            $("#"+divid+" input[type='text']").each(function(){
                $(this).val('');
            });

            laydate.render({
                elem: '#date' //指定元素
                ,lang: 'cn'
                // ,calendar: true
                ,max: 1
                ,value: new Date(new Date().getTime())
                ,format: 'yyyy-MM-dd'
                ,type: 'date'
                ,trigger: 'click' //采用click弹出
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
                    }
                }

            });
        }

        //加载查询内容
        function loadOptRecord(pageNumber) {

            var search_time = $("#date").val();
            var oltIp = stringutil.Trim($("#oltAddress").val());
            var oltIps = $("#oltAddressType").val();
            if (!checkIp(oltIp) && oltIp != "") {
                layer.close(layer_load);
                layer.tips('请输入正确格式的主机IP!', '#oltAddress',{ tips: [2, '#EE1A23'] });
                $("#oltAddress").focus();
                return;
            }
            if (oltIps != null) {
                oltIps = oltIps.toString();
            }
            if (!(oltIps == null || oltIps == "") && !(oltIp == null || oltIp == "")) {
                layer.close(layer_load);
                $("#oltAddress").focus();
                layer.tips("oltIp地址输入框与下拉框不能同时选择!", '#oltAddress',{ tips: [2, '#EE1A23'] });
                return;
            } else if ((oltIps == null || oltIps == "") && (oltIp == null || oltIp == "")) {
                layer.close(layer_load);
                $("#oltAddress").focus();
                layer.tips("oltIp地址输入框与下拉框不能为空!", '#oltAddress',{ tips: [2, '#EE1A23'] });
                return;
            }
            loadingwait();
            var data = {
                'search_time': search_time, 'oltIps': oltIps,
                'oltIp': oltIp, 'pageNumber':pageNumber

            };
            $.getJSON("/view/class/mainttool/oltcontrol/query?random=" + Math.random(), data, function (result) {
                layer.close(layer_load);
                resetPage(result);
            });
            $.getJSON("/view/class/mainttool/oltcontrol/query/getOltLineData?random=" + Math.random(), data, function (result) {
                lineData(result);
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
                var pageList = data[i];

                rowdata = rowdata + "<tr><td title=\""+pageList.attr1+"\">"+pageList.attr1+"</td><td title=\""+pageList.alarm_level+"\">"
                    +isNull(pageList.alarm_level)+"</td><td title=\""+pageList.alarm_msg+"\">"
                    +isNull(pageList.alarm_msg)+"</td><td title=\""+pageList.alarm_time+"\">"+pageList.alarm_time+"</td></tr>";
            }
            var ct = endnum-startnum+1;
            $("#alarmhistorydiv").empty().append(rowdata);

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

        function isNull(data){
            if(data==null || data =='' || data == "null"){
                return '';
            }else
                return data;
        }

        function lineData(oltLineData) {
            $("#chartArea").empty();
            var myChart = "";
            var noDataLength = 0;
            for (var i = 0; i < oltLineData.length; i++) {
                var seriesData=[];
                if (oltLineData[i].xRateData == null || oltLineData[i].xRateData == "") {
                    noDataLength = noDataLength + 1;
                    continue;
                }
                var infoHtml = "<div id='area" + i + "' style='width: 100%;height:400px;'></div>";
                $("#chartArea").append(infoHtml);
                // 基于准备好的dom，初始化echarts实例
                myChart = ec.init(document.getElementById('area'+i));
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: 'olt在线用户数'
                    },
                    legend : {
                        data : [oltLineData[i].oltIp]
                    },
                    xAxis: {
                        type: 'category',
                        data: oltLineData[i].xRateData
                    },
                    yAxis: {
                        type: 'value'
                    },
                    tooltip : {
                        formatter:function(param){  //自定义提示框的文字
                            return "olt地址：" +" " + param.seriesName +"<br>"
                                +"时间："+param.name+"<br>"
                                +"用户数：" +param.value;
                        }
                    },
                    series : [
                        {
                            name:'olt地址',
                            type:'line',
                            smooth:true,
                            // itemStyle: {
                            //     normal: {
                            //         color:'#bba9df', //改变折线点的颜色
                            //         lineStyle:{
                            //             color:'#bba9df' //改变折线颜色
                            //         },
                            //         areaStyle: {
                            //             type: 'default' //区域颜色
                            //         }
                            //     }
                            // },
                            data:[oltLineData[i].oltIp]
                        }
                    ]
                };
                seriesData.push({'name':oltLineData[i].oltIp,'type':'line','data':oltLineData[i].yRateData});
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
                myChart.setSeries(seriesData);
            }
            if (noDataLength == oltLineData.length) {
                loadNoDataChart();
            }
        }

        function loadNoDataChart() {
            var infoHtml = "<div id='noDataArea' style='width: 100%;height:400px;'></div>";
            $("#chartArea").append(infoHtml);
            var myChart = ec.init(document.getElementById('noDataArea'));
            // 指定图表的配置项和数据
            var option1 = {
                title: {
                    text: 'olt在线用户数'
                },
                legend : {
                    data : []
                },
                xAxis: {
                    type: 'category',
                    data: []
                },
                yAxis: {
                    type: 'value'
                },
                tooltip : {
                    formatter:function(param){  //自定义提示框的文字
                        return "olt地址：" +" " + param.seriesName +"<br>"
                            +"时间："+param.name+"<br>"
                            +"用户数：" +param.value;
                    }
                },
                noDataLoadingOption:{
                    effect:'bubble',
                    effectOption: {
                        effect: {
                            n: 30
                        }
                    },
                    text:'暂无数据',
                    textStyle:{
                        fontSize : 20
                    }
                },

                series : [
                    {
                        name:'olt地址',
                        type:'line',
                        smooth:true,
                        // itemStyle: {
                        //     normal: {
                        //         color:'#bba9df', //改变折线点的颜色
                        //         lineStyle:{
                        //             color:'#bba9df' //改变折线颜色
                        //         },
                        //         areaStyle: {
                        //             type: 'default' //区域颜色
                        //         }
                        //     }
                        // },
                        data:[]
                    }
                ]
            };
            myChart.setOption(option1);
        }

        //校验IP
        function checkIp(ip) {
            var regex1 = /^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
            var regex2 = '0.0.0.0';
            return regex1.test(ip) && regex2 != ip;
        }

    });
