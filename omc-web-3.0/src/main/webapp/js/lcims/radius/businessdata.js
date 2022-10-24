require.config({
    paths: {
        "domReady": "/js/require/domReady",
        'lcims': "/js/lcims",
        'echarts': "/js/echarts",
        'jquery': '/js/jquery/jquery.min',
        'cookie': '/js/jquery/jquery.cookie',
        'layer': '/js/lcims/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'packdata':'/js/lcims/tool/packdata',
        'layer':'/js/layer/layer',
        'daterangepicker':'/js/lcims/tool/daterangepicker_multicycle',
        'moment': '/js/lcims/tool/moment',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'js_scroll':'/js/scroll/jquery.mousewheel.min'
    },
    shim: {
        "daterangepicker": {
            deps: ["jquery", "moment"]
        },
        "moment": {}
    }
});
require([ "domReady", 'jquery', "packdata",
        'echarts',
        'layer','laypage',
        'daterangepicker',
        'cookie',
        'resizewh',
        'echarts/chart/bar',
        'echarts/chart/line',
        'echarts/chart/gauge',
        'echarts/chart/force',
        'echarts/chart/pie'
    ],function(domReady, $, packdata, ec,layer,laypage,daterangepicker,cookie,resizewh) {

        loadData();
        /*------------------------------------------------以下为自定义方法---------------------------------------------------*/

        function loadData() {
            // var hostId = $("#hostId").val();
            // $.getJSON("/view/class/businessdata/radius/getRadiusData?hostId=" + hostId + "&random=" + Math.random(), function (result) {
            //     lineData(result);
            // });
            $.getJSON("/view/class/businessdata/radius/getRadiusData?random=" + Math.random(), function (result) {
                lineData(result);
            });
        }

        function lineData(radiusLineData) {
            $("#chartArea").empty();
            var myChart = "";
            for (var i = 0; i < radiusLineData.length; i++) {
                var seriesData=[];
                var legendData=[];
                var infoHtml = "<div id='area" + i + "' style='width: 100%;height:400px;'></div>";
                $("#chartArea").append(infoHtml);

                myChart = ec.init(document.getElementById('area' + i));
                // 指定图表的配置项和数据
                var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    title: {
                        text : radiusLineData[i].name
                    },
                    legend : {
                        data: [],
                        x:'55%',
                        y:'1%'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: radiusLineData[i].radiusChartVoList[0].xRateData
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: []
                };

                var arr = radiusLineData[i];
                var arrList = arr.radiusChartVoList;
                for (var j = 0; j < arrList.length; j++){
                    seriesData.push({name:arrList[j].dataName, type:'line', data:arrList[j].yRateData});
                    legendData.push(arrList[j].dataName);
                }
                option.legend.data=legendData;
                myChart.setOption(option);
                myChart.setSeries(seriesData);
            }
        }
    }
);
