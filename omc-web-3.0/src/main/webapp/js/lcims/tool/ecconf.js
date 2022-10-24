/**
 * 宽带业务分析系统echart图形默认配置
 * 
 */
define(function (require) {
    "use strict";

    var util = require('lcims/tool/util');
    var pieColors = ["#7cb5ec", "#ffa93d", "#1b5995",
        "#6f85bf", "#43cf43", "#7f0caf", "#ec7838"];
    var lineColors = ["#ffa93d", "#54b046"];
    
    /**
     * 
     * 默认的tooltip样式
     * 
     * @param {Object} formatter tooltip信息格式
     */
    function getTooltipOption(formatter) {
        var tooltip = {
            "trigger": "item",
            "textStyle": {
                "color": "#0000ff"
            },
            "backgroundColor": "#fcfcfc",
            "borderColor": "#ccc",
            "borderWidth": 1
        };
        if (formatter) {
            tooltip["formatter"] = formatter;
        };
        return tooltip;
    };

    function getGridInfo() {
        return {
            "x2": 30,
            "y2": 40,
            "x": 60,
            "y": 60,
            "borderWidth": 0
        };

    }
    function getGridInfo2() {
        return {
            "x2": 10,
            "y2": 20,
            "x": 10,
            "y": 30,
            "borderWidth": 0
        };

    }
    
    
    /**
     * 默认的option
     * 
     * @param {String} title 标题
     * @param {Object} tooltip  
     * 
     */
    function getDefaultOption(title, tooltip) {
        return {
            "calculable": false,
            "tooltip": tooltip,
            "title": {
                "text": title || "",
                "textStyle": {
                    "color": "#545454",
                    "fontSize": 16,
                    "fontFamily": "Microsoft Yahei, Arial, Helvetica, sans-serif",
                    "fontWeight": "normal"
                }
            },
        };
    };

    /**
     * 折线图的option
     * 
     * @param {String} title 标题,必需
     * @param {Array} colors 序列颜色,必需
     * @param {Object} tpformatter  tooltip信息格式
     * 
     */
    function getLineOption(title, colors, tpformatter) {
        var tooltip = getTooltipOption(tpformatter);
        var gridinfo = getGridInfo();
        var lineOption = {
            "calculable": false,
            "title": {
                "x": 20,
                "y": 25
            },
            "color": colors || lineColors,
            "tooltip": {
                "trigger": "axis",
            },
            "legend": {
                "x": "right",
                "y": 25,
                "data": []
            },
            "xAxis": [
                {
                    "boundaryGap": false,
                    "type": "category",
                    "splitLine": {
                        "show": false
                    },
                    "axisTick": {
                        "interval": 0
                    },
                    "axisLabel": {
                        "interval": 0
                    },
                    "data": []
                }
            ],
            "yAxis": [
                {
                    "boundaryGap": [
                        0,
                        0.01
                    ],
                    "type": "value",
                    "axisLine": {
                        "show": false
                    },
                    axisLabel : {
                        formatter: '{value}'
                    }
                }
            ],
            "symbol": "none",
            "series": [],
        };
        lineOption.grid = gridinfo;
        util.merge(lineOption, getDefaultOption(title, tooltip));
        return lineOption;
    };
    
    /**
     * 面积图的Option
     * @param {String} title 标题,必需
     * @param {Array} colors 序列颜色,必需
     * @param {Object} tpformatter  tooltip信息格式
     */
    function getAreaLineOption(title, colors, tpformatter) {
        var areaOption = {
            "series": [],
        };
        for (var i = 0; i < colors.length; i++) {
            areaOption.series[i] = {
                "smooth": true,
                "symbol": "none",
                "type": "line",
                "itemStyle": {
                    "normal": {
                        "color": colors[i],
                        "areaStyle": {
                            "type": "default"
                        }
                    }
                },
                "data": []
            };
        };
        util.merge(areaOption, getLineOption(title, colors, tpformatter));
        return areaOption;
    };
    /**
     * 饼图的option
     * @param {String} title 标题
     * @param {Array} colors 序列颜色,默认使用pieColors
     * @param {Object} tpformatter  tooltip信息格式,默认{b}: {c} ({d}%)
     */
    function getPieOption(title, colors, tpformatter) {
        var pieOption = {
            "calculable": false,
            "title": {
                "x": 20,
                "y": 25
            },
            "color": colors || pieColors,
            "legend": {
                "orient": "vertical",
                "x": "60px",
                "y": "center",
                "borderColor": "#716464",
                "borderWidth": 1
            },
            "series": [
                {
                    "center": [
                        "70%",
                        "50%"
                    ],
                    "radius": "85%",
                    "type": "pie",
                    "itemStyle": {
                        "normal": {
                            "label": {
                                "show": true,
                                "position": "inside"
                            },
                            "labelLine": {
                                "show": false
                            }
                        }
                    },
                    "data": [],
                },
            ],
        };

        var tooltip = getTooltipOption(tpformatter || "{b} : {c} ({d}%)");

        util.merge(pieOption, getDefaultOption(title, tooltip));
        return pieOption;
    };
    
    function getNotaxisLineOption(title, colors, tpformatter)
    {
        var lineoption = getLineOption(title, colors, tpformatter);
        lineoption.legend.y=0;
        lineoption.yAxis[0].show=false;
        lineoption.xAxis[0].axisLine={"show":false};
        lineoption.xAxis[0].axisTick={"show":false};
        lineoption.title.y=0;
        lineoption.title.x="center";
        return lineoption;
    }

    /**
     * 地图的option
     * @param {String} maptype 地图省份名 必需
     * @param {String} title 标题 必需
     * @param {Array} colors 序列颜色,必需
     * @param {Object} tpformatter  tooltip信息格式
     */
    function getMapOption(maptype, title, colors, tpformatter) {
        var tooltip = getTooltipOption(tpformatter);
        var mapOption = {
            "backgroundColor": "#fcfcfc",
            "title": {
                "x": 60,
                "y": 325
            },
            "dataRange": {
                "splitNumber": colors.length,
                "color": colors,
                "formatter": "{value} ~ {value2}",
                "x": "60px",
                "y": "350px"
            },
            "series": [
                {
                    "selectedMode": "single",
                    "mapType": maptype,
                    "mapLocation": {
                        "width": "100%",
                        "height": "100%"
                    },
                    "roam": false,
                    "name": title,
                    "type": "map",
                    nameMap: {},
                    "itemStyle": {
                        "normal": {
                            "label": {
                                "show": true,
                                "textStyle": {
                                    "color": "#000000",
                                    "fontSize": 12,
                                    "fontWeight": "bold"
                                }
                            },
                            "borderColor": "#FFFFFF",
                            "borderWidth": 1
                        },
                        "emphasis": {
                            "label": {
                                "show": true,
                                "textStyle": {
                                    "color": "#000000",
                                    "fontWeight": "bold"
                                }
                            },
                            "color": "#ef7c20"
                        }
                    },
                    "data": [],
                }
            ],
        };
        util.merge(mapOption, getDefaultOption(title, tooltip));
        return mapOption;
    };

    function getBarOption(title, colors, tpformatter) {
        var tooltip = getTooltipOption(tpformatter);
        var gridinfo = getGridInfo();
        var barOption = {
            "title": {
                "x": 20,
                "y": 25
            },
            "tooltip": {
                "trigger": 'axis'
            },
            "color": colors,
            "legend": {
                "y": 25,
                "x": "right",
                "data": []
            },
            "calculable": false,
            "xAxis": [
                {
                    "type": 'category',
                    "splitLine": {
                        "show": false
                    },
                    axisLabel:{},
                    "data": []
                }
            ],
            "yAxis": [
                {
                    "type": 'value',
                    "axisLine": {
                        "show": false
                    }
                }
            ],
            "series": [
            ]
        };
        barOption.grid = gridinfo;
        util.merge(barOption, getDefaultOption(title, tooltip));
        return barOption;
    };
    
    function getNotaxisBarOption(title, colors, tpformatter) {
    
        var baroption = getBarOption(title,colors,tpformatter);
        baroption.grid= getGridInfo2();
        baroption.xAxis[0].axisLine={"show":false};
        baroption.xAxis[0].axisTick={"show":false};
        baroption.xAxis[0].axisLabel={"show":true,interval:0,"textStyle":{align:"center"}};
        baroption.xAxis[0].axisLabel.formatter=  function (value){
                                             return value.replace(/(.{5})/g,'$1\n');
                                         }; 
        baroption.yAxis[0].splitLine={"show":false};
        baroption.yAxis[0].show=false;
        return baroption;
    };


    function makelineSymbolForData(data) {
        $.each(data, function (i, point) {
            data[i] = {
                "value": point,
                "symbol": "circle"
            };
        });
    };
    /**
     * 堆积柱状图的option
     * @param {String} title 标题 必需
     * @param {Array} colors 序列颜色,必需
     * @param {Object} tpformatter  tooltip信息格式
     */
    function getCumulateBarOption(title, colors, tpformatter) {
        var tooltip = getTooltipOption(tpformatter);
        var option = {
            "backgroundColor": "#ffffff",
            "color": colors,
            "tooltip": {
                "trigger": "axis",
                "axisPointer": {
                    "type": "shadow"
                }
            },
            "grid": {
                "x2": 20,
                "y2": 40,
                "x": 60,
                "y": 60,
                "borderWidth": 0
            },
            "legend": {
                "x": "right",
                "data": []
            },
            "calculable": false,
            "xAxis": [
                {
                    "splitLine": {
                        "show": false
                    },
                    "type": "value",
                    "data": []
                }
            ],
            "yAxis": [
                {
                    "splitLine": {
                        "show": false
                    },
                    "type": "category",
                    "data": []
                }
            ],
            "series": []
        };
        util.merge(option, getDefaultOption(title, tooltip));
        return option;
    };
    
    /**
     * 首页堆积柱状图的option
     * @param {String} title 标题 必需
     * @param {Array} colors 序列颜色,必需
     * @param {Object} tpformatter  tooltip信息格式
     */
    function getHomeCumulateBarOption(title, colors, tpformatter) {
        var tooltip = getTooltipOption(tpformatter);
        var option = {
            "color": colors,
            "tooltip": {
                "trigger": "axis",
                "axisPointer": {
                    "type": "shadow"
                }
            },
            "grid": {
                "x2": 20,
                "y2": 40,
                "x": 60,
                "y": 60,
                "borderWidth": 0
            },
            "legend": {
                "x": "right",
                "textStyle" : {
                    "color" : "#FFFFFF"
                },
                "data": []
            },
            "xAxis": [
                {
                    "splitLine": {
                        "show": false
                    },
                    "type": "value",
                    "axisLabel" : {
                        "textStyle" : {
                            "color" : "#FFFFFF"
                        }
                    },
                    "data": []
                }
            ],
            "yAxis": [
                {
                    "splitLine": {
                        "show": false
                    },
                    "type": "category",
                    "axisLabel" : {
                        "textStyle" : {
                            "color" : "#FFFFFF"
                        }
                    },
                    "data": []
                }
            ],
            "calculable": false,
            "title": {
                "text": title || "",
                "textStyle": {
                	"fontSize": 14,
                	"color": "#fff"
                }
            },
            "series": []
        };
        return option;
    };
    
    /**
     * 所有options的集合
     */
    return {
        getDefaultOption: getDefaultOption,
        getLineOption: getLineOption,
        getPieOption: getPieOption,
        getMapOption: getMapOption,
        getAreaLineOption: getAreaLineOption,
        makelineSymbolForData: makelineSymbolForData,
        getBarOption: getBarOption,
        getCumulateBarOption: getCumulateBarOption,
        getNotaxisLineOption: getNotaxisLineOption,
        getNotaxisBarOption:getNotaxisBarOption,
        getHomeCumulateBarOption:getHomeCumulateBarOption,
    };
});
