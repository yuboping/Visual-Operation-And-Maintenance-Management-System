define(function (require) {
    var ecconf = require('lcims/tool/ecconf');
    "use strict";
    var precolor = [ "#44ACCF", "#C1DD33", "#2EADA4", "#D2E593", "#7DC9E1",
            "#FCD0BD", "#F39F8F", "#AFA9B3", "#AA6F8B", "#00FA9A", "#87CEFA",
            "#FF7F50", "#A6E930" ], 
            fivemin = [ "00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:35", "00:40", "00:45",
            "00:50", "00:55", "01:00", "01:05", "01:10", "01:15", "01:20", "01:25", "01:30", "01:35", "01:40", "01:45", "01:50", "01:55",
            "02:00", "02:05", "02:10", "02:15", "02:20", "02:25", "02:30", "02:35", "02:40", "02:45", "02:50", "02:55", "03:00", "03:05",
            "03:10", "03:15", "03:20", "03:25", "03:30", "03:35", "03:40", "03:45", "03:50", "03:55", "04:00", "04:05", "04:10", "04:15",
            "04:20", "04:25", "04:30", "04:35", "04:40", "04:45", "04:50", "04:55", "05:00", "05:05", "05:10", "05:15", "05:20", "05:25",
            "05:30", "05:35", "05:40", "05:45", "05:50", "05:55", "06:00", "06:05", "06:10", "06:15", "06:20", "06:25", "06:30", "06:35",
            "06:40", "06:45", "06:50", "06:55", "07:00", "07:05", "07:10", "07:15", "07:20", "07:25", "07:30", "07:35", "07:40", "07:45",
            "07:50", "07:55", "08:00", "08:05", "08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55",
            "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:45", "09:50", "09:55", "10:00", "10:05",
            "10:10", "10:15", "10:20", "10:25", "10:30", "10:35", "10:40", "10:45", "10:50", "10:55", "11:00", "11:05", "11:10", "11:15",
            "11:20", "11:25", "11:30", "11:35", "11:40", "11:45", "11:50", "11:55", "12:00", "12:05", "12:10", "12:15", "12:20", "12:25",
            "12:30", "12:35", "12:40", "12:45", "12:50", "12:55", "13:00", "13:05", "13:10", "13:15", "13:20", "13:25", "13:30", "13:35",
            "13:40", "13:45", "13:50", "13:55", "14:00", "14:05", "14:10", "14:15", "14:20", "14:25", "14:30", "14:35", "14:40", "14:45",
            "14:50", "14:55", "15:00", "15:05", "15:10", "15:15", "15:20", "15:25", "15:30", "15:35", "15:40", "15:45", "15:50", "15:55",
            "16:00", "16:05", "16:10", "16:15", "16:20", "16:25", "16:30", "16:35", "16:40", "16:45", "16:50", "16:55", "17:00", "17:05",
            "17:10", "17:15", "17:20", "17:25", "17:30", "17:35", "17:40", "17:45", "17:50", "17:55", "18:00", "18:05", "18:10", "18:15",
            "18:20", "18:25", "18:30", "18:35", "18:40", "18:45", "18:50", "18:55", "19:00", "19:05", "19:10", "19:15", "19:20", "19:25",
            "19:30", "19:35", "19:40", "19:45", "19:50", "19:55", "20:00", "20:05", "20:10", "20:15", "20:20", "20:25", "20:30", "20:35",
            "20:40", "20:45", "20:50", "20:55", "21:00", "21:05", "21:10", "21:15", "21:20", "21:25", "21:30", "21:35", "21:40", "21:45",
            "21:50", "21:55", "22:00", "22:05", "22:10", "22:15", "22:20",  "22:25", "22:30", "22:35", "22:40", "22:45", "22:50", "22:55",
            "23:00", "23:05", "23:10", "23:15", "23:20", "23:25", "23:30", "23:35", "23:40", "23:45", "23:50", "23:55" ],
            fifteenmin=[ "00:00", "00:15", "00:30", "00:45",
                      "01:00", "01:15", "01:30", "01:45",
                      "02:00", "02:15", "02:30", "02:45",
                      "03:00", "03:15", "03:30", "03:45",
                      "04:00", "04:15", "04:30", "04:45",
                      "05:00", "05:15", "05:30", "05:45",
                      "06:00", "06:15", "06:30", "06:45",
                      "07:00", "07:15", "07:30", "07:45",
                      "08:00", "08:15", "08:30", "08:45",
                      "09:00", "09:15", "09:30", "09:45",
                      "10:00", "10:15", "10:30", "10:45",
                      "11:00", "11:15", "11:30", "11:45",
                      "12:00", "12:15", "12:30", "12:45",
                      "13:00", "13:15", "13:30", "13:45",
                      "14:00", "14:15", "14:30", "14:45",
                      "15:00", "15:15", "15:30", "15:45",
                      "16:00", "16:15", "16:30", "16:45",
                      "17:00", "17:15", "17:30", "17:45",
                      "18:00", "18:15", "18:30", "18:45",
                      "19:00", "19:15", "19:30", "19:45",
                      "20:00", "20:15", "20:30", "20:45",
                      "21:00", "21:15", "21:30", "21:45",
                      "22:00", "22:15", "22:30", "22:45",
                      "23:00", "23:15", "23:30", "23:45" ],
            onehour=["00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00",
                   "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"],
            onemonth=["01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"];
            
    

    return {
        getAreaLineOption : getAreaLineOption,// 面积图
        getCumulateBarOption : getCumulateBarOption,// 堆积柱状图
        getAnaCurveOption : getAnaCurveOption,// 业务分析模块曲线图
        getVerticalbarOption : getVerticalbarOption,// 垂直柱状图
        getMonitorLineOption : getMonitorLineOption,// 系统监控页面折线图
        getMonitorCircleOption : getMonitorCircleOption,// 系统监控页面圆环图
        getMonitorGaugeOption : getMonitorGaugeOption,// 系统监控页面仪表盘
        getMonitorHorizontalBar : getMonitorHorizontalBar,// 系统监控页面水平柱状图
        pushData : pushData,// 往option中填充数据
        getAnaPieOption : getAnaPieOption,// 业务分析模块饼图
        getAnaMapOption : getAnaMapOption,// 业务分析模块省份地图
        getAnaHorizontalBar : getAnaHorizontalBar,// 业务分析页面水平柱状图
        getAnaVerticalBar : getAnaVerticalBar,// 业务分析页面垂直柱状图
        getRelationshipGrap : getRelationshipGrap, // 关系图
        getRosePieOption : getRosePieOption,// 南丁格尔玫瑰图
        getHomeLineOption : getHomeLineOption,// 首页折线图
        getRelationshipMap : getRelationshipMap, // 首页关系拓扑图
        getAnaMapOptionNew : getAnaMapOptionNew, //业务分析模块省份地图new
        getRelationshipMapNew : getRelationshipMapNew, // 首页关系拓扑图new
        getHomeCumulateBarOption : getHomeCumulateBarOption,// 首页堆积柱状图
        getHomePieOption:getHomePieOption,// 首页饼图
        getAnaMapOption22:getAnaMapOption22
    };
    /**
	 * 图表数据填充至option data--数据 series--option中series样式
	 * cuscolor--自定义颜色，若为空使用默认color unit--坐标轴单位 type--Vertical:纵向，Horizontal:横向
	 * 
	 */
    function pushData(data, option, series, cuscolor, unit, type, interval,linestyle,charttype,tenthousand) {
        var color = (cuscolor == null ? precolor : cuscolor);
        var tmp = [],asis=getAsis(data, interval);
        if (type == "Vertical") {
            option.xAxis[0].data=asis;
        } else if (type == "Horizontal") {
            option.yAxis[0].data=asis;
        }
        option.tooltip.formatter = "{b}<br/>";
        var legendSelected = {};
        var flag = false;
        for (var i = 0; i < data.length; i++) {
        	//浙江移动,全失败数量的时候,默认只有“全失败数”是选中状态
        	if(data[i].legend=="总失败数"){
        		flag = true;
        	}else{
        		legendSelected[data[i].legend]=false;
        	}
            option.legend.data.push({
                icon : "bar",
                name : data[i].legend
            });
            if (data.length == 1 || type == "Horizontal") {
                option.tooltip.formatter = "{b}<br/>{a0} | {c0}" + unit + "<br/>";
            } else {
                option.tooltip.formatter = option.tooltip.formatter + "{a" + i + "} | {c" + i + "}" + unit + "<br/>";
            }
            tmp = data[i].data.reverse();
            if (series != null) {
                option.series[i] = series.getSeries();
                option.series[i].name = data[i].legend;
                option.series[i].itemStyle.normal.color = color[i];
            	if(linestyle!=null && linestyle!=""){
            	    option.series[i].itemStyle.normal.areaStyle.color = color[i];
            	    option.series[i].itemStyle.normal.lineStyle = linestyle[i];
            	}
            }
            for (var j = 0; j < asis.length; j++) {
            	if (charttype == "anaverticalbar") {
            		var seriesdatanull = true;
            		for (var x = 0; x < tmp.length; x++) {
            			if(asis[j]==tmp[x].mark){
            				var tmpvalue = tmp[x].value;
            				if (tenthousand) {
            					tmpvalue = tmpvalue / 10000; 
            					tmpvalue / 10000
                				tmpvalue = tmpvalue.toFixed(2);
                            }
                            option.series[i].data.push(tmpvalue);
                            seriesdatanull = false;
                            break;
                        }
            		}
            		if(seriesdatanull){
            			option.series[i].data.push("-");
            		}
            	}else{
            		if(tmp.length>0&&asis[j]==tmp[tmp.length-1].mark){
                        option.series[i].data.push(tmp.pop().value);
                    }else{
                        option.series[i].data.push("-");
                    }
            	}
            }
        }
        if(flag==true){
        	option.legend.selected=legendSelected;
        }
    }
    function getAsis(data, interval){
    	var	asis=mergeData(data);
    	return asis;
    }
    function mergeData(data){
        var tmp=[],asis=[],hash={};
//        data.forEach(function(e){ 
//            tmp=e.data;
//            tmp.forEach(function(value){ 
//                hash[value.mark] = value.mark;
//            });
//        });
        for (var i=0;i<data.length;i++) {
        	tmp=data[i].data;
        	for (var j=0;j<tmp.length;j++) {
        		var value = tmp[j];
        		hash[value.mark] = value.mark;
        	}
        }
        for(var val in hash) { 
            asis.push(val);
        }
        asis.sort();
        return asis;
    }
    function getIntervalTime(data, interval){
        var tmp=[],asis=[];
        var flag = 0;
        if("2"==interval){
            tmp=fivemin;
        }else if("4"==interval){
            tmp=onehour;
        }else if("6"==interval){
            tmp=onemonth;
        }else if("7"==interval){
        	tmp=fifteenmin;
        }else{
        	flag = 1;
        }
        if(flag==0){
        	var max=findMaxTime(data);
            tmp.forEach(function(e){ 
                if(e<=max) {
                    asis.push(e);
                }
            });
        }else{
        	asis = mergeData(data);
        }
        return asis;
    }
    function findMaxTime(data){
        var max="",tmp=[];
        data.forEach(function(e){ 
            tmp=e.data;
            if(tmp!=null&&tmp.length>0&&tmp[tmp.length-1].mark>max){
                max=tmp[tmp.length-1].mark;
            }
        });
        return max;
    }
    /**
	 * 业务分析模块曲线图
	 */
    function getAnaCurveOption(data,legend, cuscolor) {
        var option = ecconf.getLineOption("", []);
        option.grid = {
            "x" : 70,
            "y" : 20,
            "x2" : 30,
            "y2" : 20,
            "borderWidth" : 0
        };
        option.legend.show = legend;
        option.backgroundColor = "";
        option.legend.y = 'top' ;
        option.legend.itemWidth = 10 ;
        option.yAxis[0].axisLine = {
            "show" : true,
            "lineStyle" : {
                "color" : "#cccccc",
                "width" : 1
            }
        };
        option.xAxis[0].axisLine = {
            "show" : false
        };
        option.xAxis[0].axisTick = {
            show : false
        };
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "",
                        "type" : "line",
                        "smooth" : true,
                        "itemStyle" : {
                            "normal" : {
                                "color" : ""
                            }
                        },
                        "data" : []
                    }
                }
            };
        })();
        pushData(data, option, seriesObj, cuscolor, "", "Vertical");
        return option;
    }
    function getAreaLineOption(title, data, cuscolor) {
        var option = ecconf.getAreaLineOption(title, precolor);
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "",
                        "smooth" : true,
                        "symbol" : "none",
                        "type" : "line",
                        "itemStyle" : {
                            "normal" : {
                                "color" : "",
                                "areaStyle" : {
                                    "type" : "default"
                                }
                            }
                        },
                        "data" : []
                    }
                }
            };
        })();
        pushData(data, option, seriesObj, cuscolor, "", "Vertical");
        return option;
    }
    function getCumulateBarOption(title, data, cuscolor) {
        var option = ecconf.getCumulateBarOption(title, precolor, []);
        var tmp = [];
        option.tooltip.formatter = "{b}<br/>";
        option.xAxis[0].axisLabel = {
            "formatter" : "{value}%"
        };
        option.xAxis[0].max = 100;
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "",
                        "type" : "bar",
                        "stack" : "总量",
                        "barMaxWidth" : 35,
                        "itemStyle" : {
                            "normal" : {
                                "color" : "",
                                "areaStyle" : {
                                    "type" : "default"
                                },
                                "label" : {
                                    "show" : true,
                                    "position" : "inside",
                                    "formatter" : "{c}%",
                                    "textStyle" : {
                                        "fontSize" : 11
                                    }
                                }
                            }
                        },
                        "data" : []
                    }
                }
            };
        })();
        pushData(data, option, seriesObj, cuscolor, "%", "Horizontal");
        return option;
    }
    /**
	 * 垂直柱状图
	 */
    function getVerticalbarOption(title, data, legend, cuscolor) {
        var option = ecconf.getBarOption(title, precolor, []);
        var axisLine = {
            "show" : true,
            "lineStyle" : {
                "color" : "#cccccc",
                "width" : 1
            }
        };
        option.backgroundColor = "";
        option.grid = {
            "x" : "50",
            "y" : "20",
            "x2" : "20",
            "y2" : "20",
        };
        option.legend.show = legend;
        option.legend.y = 'top' ;
        option.legend.itemWidth = 10 ;
        option.yAxis[0].axisLine = axisLine;
        option.xAxis[0].axisLine = axisLine;
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "",
                        "type" : "bar",
                        "barMaxWidth" : 50,
                        "itemStyle" : {
                            "normal" : {
                                "color" : ""
                            }
                        },
                        "data" : []
                    }
                }
            };
        })();
        pushData(data, option, seriesObj, cuscolor, "", "Vertical");
        return option;
    }
    function getHomeLineOption(data, cuscolor,interval,linestyle){
        var option= {
                "tooltip" : {
                    "trigger" : "axis"
                },
                "toolbox" : {
                    "show" : false,
                },
                title : {
                	show : true,
                	text: data[0].legend,
                    textStyle:{
                        fontSize:14,
                        color: '#fff'
                    },
                    y:'12'
                },
                "calculable" : false,
                "grid" : {
                    "x" : "55",
                    "y" : "50",
                    "x2" : "20",
                    "y2" : "30",
                },
                "legend": {
                    "show":true,
                    "textStyle": {"fontSize": 14,"color": "#fff"},
                    "y":"12",
                    "data": []
                },
                "xAxis" : [ {
                    "type" : "category",
                    "boundaryGap" : false,
                    "data" : [ ],
                    "axisLabel" : {
                        "textStyle" : {
                            "color" : "#FFFFFF"
                        }
                    }
                } ],
                "yAxis" : [ {
                    "type" : "value",
                    "data" : [ ],
                    "axisLabel" : {
                        "textStyle" : {
                            "color" : "#FFFFFF"
                        }
                    }
                } ],
                "series": []
        };
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "成交",
                        "type" : "line",
                        "smooth" : true,
                        "itemStyle" : {
                            "normal" : {
                                "lineStyle" : {
                                    "width" : 2
                                },
                                "areaStyle" : {
                                    "type" : "default",
                                    "color" : "rgba(174,228,228,0.5)"
                                },
                                "borderColor" : "rgba(133,178,36,0.8)",
                            }
                        },
                        "data" : [ ]
                    }
                }
            };
        })();
        var color = ["rgba(145,194,35,0.8)"];
        var cuscolor = ((cuscolor == null||cuscolor == "") ? color : cuscolor);
        pushData(data, option, seriesObj, cuscolor, "", "Vertical", interval,linestyle);
        return option;
    }
    function getMonitorLineOption( data, cuscolor,interval){
        var option= {
                "tooltip" : {
                    "trigger" : "axis"
                },
                "toolbox" : {
                    "show" : false,
                },
//                dataZoom: {
//                    show: true,
//                    start : 70
//                },
                title : {
                    show : false,
                    text: data[0].title,
                    textStyle:{
                        fontSize:14,
                        color: '#fff'
                    },
                    y:'12'
                },
                "calculable" : false,
                "grid" : {
                    "x" : "50",
                    "y" : "20",
                    "x2" : "20",
                    "y2" : "35",
                },
                "legend": {
                    "show":false,
                    "textStyle": {},
                    "y":"0",
                    "data": []
                },
                "xAxis" : [ {
                    "type" : "category",
                    "boundaryGap" : false,
                    "data" : [ ],
                    "axisLabel" : {
                        "textStyle" : {
                            "color" : "#666",
                        },
                        "formatter": function (value, index) { 
                            var valuechar="";
                            if(value.indexOf("-") != -1){
                            	var values=value.split("-")
                            	valuechar=values[1]+"-"+values[2]+"\n"+values[0]
                            	return valuechar
                            }
                            return value
                       }
                    }
                    
                } ],
                "yAxis" : [ {
                    "type" : "value",
                    "data" : [ ],
                    "axisLabel" : {
                        "margin": 2,
                        "formatter": function (value, index) { 
                            if (value >= 10000 && value < 10000000) {
                                value = value / 10000 + "万"; 
                            } else if (value >= 10000000) {
                                value = value / 10000000 + "千万"; 
                            } 
                            return value; 
                        },
                        "textStyle" : {
                            "color" : "#666"
                        }
                    }
                } ],
                "series": []
        };
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "成交",
                        "type" : "line",
                        "smooth" : true,
                        "itemStyle" : {
                            "normal" : {
                                "lineStyle" : {
                                    "width" : 2
                                },
                                "areaStyle" : {
                                    "type" : "default",
                                    "color" : "rgba(174,228,228,0.5)"
                                },
                                "borderColor" : "rgba(133,178,36,0.8)",
                            }
                        },
                        "data" : [ ]
                    }
                }
            };
        })();
        var color = ["rgba(145,194,35,0.8)"];
        var cuscolor = ((cuscolor == null||cuscolor == "") ? color : cuscolor);
        pushData(data, option, seriesObj, cuscolor, "", "Vertical", interval);
        return option;
    }
    function getMonitorCircleOption(title,data, cuscolor,titlecolor) {
        var color = (cuscolor == null ? precolor : cuscolor);
        var titlecolor = (titlecolor == null ? "#ccc" : titlecolor);
        var option = {
                "calculable" : false,
                "title" : {
                    show : false,
                    text: title,
                    textStyle:{
                        fontSize:14,
                        color: titlecolor
                    },
                    y:'12'
                },
                "series" : [ ]
        };
        var  interval = parseInt(100/data.length);
        var start= parseInt(interval/2);
        for (var i = 0; i < data.length; i++) {
            option.series[i]={
                "type" : "pie",
                "center" : [ (start+i*interval)+"%", "50%" ],
                "radius" : [ "60%", "90%" ],
                "x" : "0%", 
                "itemStyle" : {
                    "normal" : {
                        "color" : color[i],
                        "label" : {
                            "textStyle" : {
                                "baseline" : "top",
                                "color" : "#666",
                                "fontSize" : 16,
                            }
                        }
                    },
                },
                "data" : [ {
                    "name" : "other",
                    "value" : 100-data[i].value,
                    "itemStyle" : {
                        "normal" : {
                            "color" : "#ccc",
                            "label" : {
                                "show" : false
                            },
                            "labelLine" : {
                                "show" : false
                            },
                            "textStyle" : {
                                "baseline" : "top",
                                "color" : "#f00",
                                "fontSize" : 14,
                            }
                        },
                        "emphasis" : {
                            "color" : "rgba(0,0,0,0)"
                        }
                    }
                }, {
                    "name" : data[i].mark,
                    "value" : data[i].value,
                    "itemStyle" : {
                        "normal" : {
                            "label" : {
                                "show" : true,
                                "position" : "center",
                                "formatter" : "{b}:{c}%",
                                "textStyle" : {
                                    "color" : titlecolor,
                                    "baseline" : "bottom"
                                }
                            },
                            "labelLine" : {
                                "show" : false
                            }
                        }
                    }
                } ]
            };
        }
        return option;
    }
    function getMonitorGaugeOption(data, cuscolor,max,min, level){
        var color = (cuscolor == null ? precolor : cuscolor);
        var colortmp=[],format=[],last=0;
        try{
        if(level==null||level==""){
            level=[0.2, 0.8,1];
            for(var i=0;i<level.length;i++){
                var tmp =[];
                tmp.push(level[i]);
                tmp.push(color[i]);
                colortmp.push(tmp);
            }
        }else{
            for(var i=0;i<level.length;i++){
                var tmp =[];
                tmp.push(level[i]/max);
                tmp.push(color[i]);
                colortmp.push(tmp);
            }
        }
        }catch(e){alert(e);}
        var option = {
            "tooltip" : {
                "formatter" : "{a} <br/>{b} : {c}"
            },
            "series" : [ {
                "name" : '业务指标',
                "type" : 'gauge',
                "radius" : "100%",
                "min": min,                    
                "max": max,
                "axisLine": {            // 坐标轴线
                    "lineStyle": {       // 属性lineStyle控制线条样式
                        "width": 20,
                        "color" : colortmp,  
                    }
                },
                "splitLine": {           // 分隔线
                    "length" :20
                },
                "detail" : {
                    "formatter" : '{value}'
                },
                "data" : [ {
                    "value" : data[0].value,
                    "name" : ''
                } ]
            } ]
        };
        return option;
    }
    /**
	 * 监控页面水平柱状图
	 */
    function getMonitorHorizontalBar(title, data, cuscolor) {
        var color = (cuscolor == null ? precolor : cuscolor);
        var option = ecconf.getCumulateBarOption(title, color, []);
        option.grid = {
                "x":85,
                "y":20,
                "x2":40,
                "y2":20,
                "borderWidth":0
        };
        option.backgroundColor = "";
        option.legend.show = false;
        option.calculable=false;
        option.xAxis[0].splitLine = {
                "show" : false
            };
        option.yAxis[0].splitLine = {
            "show" : false
        };
        option.xAxis[0].max = 100;
        var tmp = [];
        for (var i = 0; i < data.length; i++) {
            tmp = data[i].data;
            option.series[i] = {
                    "name":data[i].title,
                    "type":"bar",
                    "barMaxWidth":20,
                    "data":[],
                    "itemStyle": {
                      "normal": {
                          color: function(params) {
                              return color[params.dataIndex]
                          },
                          "label": {
                              "show": true,
                              "position": "right",
                              "formatter": "{c}%"
                          }
                      }
                  }
            };
            for (var j = 0; j < tmp.length; j++) {
                option.yAxis[0].data.push((tmp[j].mark==null?"":tmp[j].mark));
                option.series[i].data.push(tmp[j].value);
            }
        }
        return option;
    }
    /**
	 * 业务分析饼图
	 */
    function getAnaPieOption(title, data, cuscolor) {
    	data = mkData(data);
        var color = (cuscolor == null ? precolor : cuscolor);
        var option = {
            "calculable" : false,
            "title" : {
                "x" : 10,
                "y" : 12,
                "show" : false,
                "text" : data[0].title,
                "textStyle" : {
                    "color" : "#fff",
                    "fontSize" : 14,
// "fontFamily" : "Microsoft Yahei, Arial, Helvetica, sans-serif",
// "fontWeight" : "normal"
                }
            },
            "tooltip" : {
                "trigger": 'item',
                "formatter": "{a} <br/>{b} : {c} ({d}%)",
                "textStyle": {
                    "color": "#0000ff"
                },
                "backgroundColor": "#fcfcfc",
                "borderColor": "#ccc",
                "borderWidth": 1,
                
            },
            "series":[]
        };
        var  interval = parseInt(100/data.length);
        var start= parseInt(interval/2);
        var tmp = [];
        for (var i = 0; i < data.length; i++) {
            tmp = data[i].data;
            option.series[i] = {
                "name" : data[i].title,
                "type" : "pie",
                "radius" : "40%",
                "center" : [ (start+i*interval+8)+"%", "65%" ],
                "itemStyle" : {
                    "normal" : {
                    	"color" : function(params) {
                            return color[params.dataIndex];
                        },
                        label:{ 
                            show: true, 
                            textStyle:{
                            	fontSize:10
                            },
                            formatter: '{b} : {d}%' 
                          }, 
                          labelLine :{show:true} 
                    }
                },
                "data" : []
            };
            for (var j = 0; j < tmp.length; j++) {
                option.series[i].data.push({
                    "value" : tmp[j].value,
                    "name" : tmp[j].mark
                });
            }
        }
        return option;
    }
    
    /**
	 * 南丁格尔玫瑰图
	 */
    function getRosePieOption(title, data, cuscolor) {
        var color = (cuscolor == null ? precolor : cuscolor);
        var option = {
            "calculable" : false,
            "title" : {
                "x" : 10,
                "y" : 12,
                "show" : true,
                "text" : data[0].legend,
                "textStyle" : {
                    "color" : "#fff",
                    "fontSize" : 14
                }
            },
            "tooltip" : {
                "trigger": 'item',
                "textStyle": {
                    "color": "#0000ff"
                },
                "backgroundColor": "#fcfcfc",
                "borderColor": "#ccc",
                "borderWidth": 1,
                "formatter": "{a} <br/>{b} : {c} ({d}%)"
            },
            "series":[]
        };
        var  interval = parseInt(100/data.length);
        var start= parseInt(interval/2);
        var tmp = [];
        for (var i = 0; i < data.length; i++) {
            tmp = data[i].data;
            option.series[i] = {
                "name" : data[i].title,
                "type" : "pie",
                "roseType" : "radius",
                "radius" : "60%",
                "center" : [ (start+i*interval)+"%", "60%" ],
                "itemStyle" : {
                    "normal" : {
                        "label": {
                            "textStyle":{
                              "color":"#EAEAEA"
                            }
                         },
                         "labelLine": {
                             "length":"0.2",
                             "lineStyle": {
                                 "color": "#EAEAEA"
                             }
                         },
                         "color" : function(params) {
                             return color[params.dataIndex];
                         }
                    }
                },
                "data" : []
            };
            for (var j = 0; j < tmp.length; j++) {
                option.series[i].data.push({
                    "value" : tmp[j].value,
                    "name" : tmp[j].mark
                });
            }
        }
        return option;
    }
    
    // 最大值和最小值依次显示
    function mkData(data){
    	data[0].data.sort(function(a,b){
            return b.value-a.value;});
    	var tmp = [];
    	var i = 0;
    	var length = data[0].data.length;
    	var j = length;
    	for(var i=0;i<length;i++){
    		j--;
    		if(i>j){
    			break;
    		}else if(i==j){
    			tmp.push(data[0].data[i]);
    		}else{
    			tmp.push(data[0].data[j]);
    			tmp.push(data[0].data[i]);
    		}
        }
    	data[0].data = tmp;
    	return data;
    }
    
    /**
	 * 首页关系拓扑图
	 */
    function getRelationshipMap(data, cuscolor) {
		var color = (cuscolor == null ? precolor : cuscolor);
		var node=[];
		var selectdata=[];
		var nodeslist = data.nodelist;
		if(nodeslist != null) {
			var child = [];
			for (var i = 0; i < nodeslist.length; i++) {
				var hostlist = nodeslist[i].hostlist;
				if (hostlist != null) {
					var childs = [];
					for(var j=0;j< hostlist.length; j++) {
						if(hostlist[j].alarmnum > 0){
							childs.push({
								"name" : hostlist[j].hostname,
								"symbol": 'circle',
                                "symbolSize": 15,
                                "itemStyle": {
                                    "normal": {
                                        "color": '#ff0000',
                                        "label": {
                                            "show": true,
                                            "position": 'right',
                                            "formatter": "{b}"
                                        },
                                    }
                                }
							});
						}else{
							childs.push({
								"name" : hostlist[j].hostname,
							});
						}
					}
				}
				child.push({
					"name":nodeslist[i].node_name,
					"children" : childs,
				});
			}
			node.push({
				"name" : "radius主机",
				"children" : child
			});
		}
		var option = 
			{
				"title": {
			        "show": true,
			        "text": "radius主机网络拓扑图",
			        "textStyle": {
			            "fontSize": 14,
			            "color": "#fff"
			        },
			        "x" : 'center',
					"y" : 30
			    },
			    "tooltip": {
			        "trigger": "item",
			        "formatter": "{b}"
			    },
			    "calculable": false,
			    "series": [
			        {
		            "name": "树图",
		            "type": "tree",
		            "orient": "horizontal", // vertical horizontal
		            "rootLocation": { // 根节点位置  
		                "x": "20%",
		                "y": "60%"
		            },
		            "roam" : false,
		            "nodePadding": 8,
		            "symbol": "circle",
		            "symbolSize": 8,
		            "itemStyle": {
		                "normal": {
		                    "color": "#f0f91e",
		                    "label": {
		                        "show": true,
		                        "position": "right",
		                        "textStyle": {
		                            "color": "#fff",
		                            "fontSize": 12,
		                            "fontWeight": "normal"
		                        }
		                    },
		                    "lineStyle": {
		                        "color": "#ccc",
		                        "width": 1,
		                        "type": "broken" // 'curve'|'broken'|'solid'|'dotted'|'dashed'
		                    }
		                },
		                "emphasis": {
		                    "label": {
		                        "show": false
		                    },
		                    "borderWidth": 0
		                }
		            },
		            "data": node
				}
		    ]
		};
		return option;
	}
    
	/**
	 * 首页关系拓扑图new
	 */
	function getRelationshipMapNew(data, cuscolor) {
		var color = (cuscolor == null ? precolor : cuscolor);
		var node=[];
		var selectdata=[];
		var nodeslist = data.nodelist;
		if(nodeslist != null) {
			var child = [];
			for (var i = 0; i < nodeslist.length; i++) {
				var hostlist = nodeslist[i].hostlist;
				if (hostlist != null) {
					var childs = [];
					for(var j=0;j< hostlist.length; j++) {
						if(hostlist[j].alarmnum > 0){
							childs.push({
								"name" : hostlist[j].hostname,
								"symbol": 'circle',
                                "symbolSize": 15,
                                "itemStyle": {
                                    "normal": {
                                        "color": '#ff0000',
                                        "label": {
                                            "show": true,
                                            "position": 'right',
                                            "formatter": "{b}"
                                        },
                                    }
                                }
							});
						}else{
							childs.push({
								"name" : hostlist[j].hostname,
							});
						}
					}
				}
				child.push({
					"name":nodeslist[i].name,
					"children" : childs,
				});
			}
			node.push({
				"name" : "radius主机",
				"children" : child
			});
		}
		var option = 
			{
				"title": {
			        "show": true,
			        "text": "radius主机网络拓扑图",
			        "textStyle": {
			            "fontSize": 14,
			            "color": "#fff"
			        },
			        "x" : 'center',
					"y" : 100
			    },
			    "tooltip": {
			        "trigger": "item",
			        "formatter": "{b}"
			    },
			    "calculable": false,
			    "series": [
			        {
		            "name": "树图",
		            "type": "tree",
		            "orient": "horizontal", // vertical horizontal
		            "rootLocation": { // 根节点位置  
		                "x": "30%",
		                "y": "70%"
		            },
		            "roam" : false,
		            "nodePadding": 8,
		            "symbol": "circle",
		            "symbolSize": 8,
		            "itemStyle": {
		                "normal": {
		                    "color": "#f0f91e",
		                    "label": {
		                        "show": true,
		                        "position": "right",
		                        "textStyle": {
		                            "color": "#fff",
		                            "fontSize": 12,
		                            "fontWeight": "normal"
		                        }
		                    },
		                    "lineStyle": {
		                        "color": "#ccc",
		                        "width": 1,
		                        "type": "broken" // 'curve'|'broken'|'solid'|'dotted'|'dashed'
		                    }
		                },
		                "emphasis": {
		                    "label": {
		                        "show": false
		                    },
		                    "borderWidth": 0
		                }
		            },
		            "data": node
				}
		    ]
		};
		return option;
	}
	
	/**
	 * 业务分析地图
	 */
	function getAnaMapOption(province, data, cuscolor) {
		var color = (cuscolor == null ? precolor : cuscolor);
		var geoCoordMap = {
			"清远市" : [ 112.9175, 24.3292 ],
			"韶关市" : [ 113.7964, 24.7028 ],
			"湛江市" : [ 110.3577, 20.9894 ],
			"梅州市" : [ 116.1255, 24.1534 ],
			"河源市" : [ 114.917, 23.9722 ],
			"肇庆市" : [ 112.1265, 23.5822 ],
			"惠州市" : [ 114.6204, 23.1647 ],
			"茂名市" : [ 111.0059, 22.0221 ],
			"江门市" : [ 112.6318, 22.1484 ],
			"阳江市" : [ 111.8298, 22.0715 ],
			"云浮市" : [ 111.7859, 22.8516 ],
			"汕尾市" : [ 115.5762, 23.0438 ],
			"揭阳市" : [ 116.1255, 23.313 ],
			"珠海市" : [ 113.7305, 22.1155 ],
			"佛山市" : [ 112.8955, 23.1097 ],
			"潮州市" : [ 116.7847, 23.8293 ],
			"汕头市" : [ 117.1692, 23.3405 ],
			"深圳市" : [ 114.5435, 22.5439 ],
			"东莞市" : [ 113.8953, 22.901 ],
			"中山市" : [ 113.4229, 22.478 ]
		};
		var selectdata = data[0].data;
		var convertData = [];
		for(var i = 0; i < selectdata.length; i++){
			var geoCoord = geoCoordMap[selectdata[i].mark];
	        if (geoCoord && selectdata[i].alarmNum > 0 && selectdata[i].alarmNum!= null) {
	        	convertData.push({
	        		"name" : selectdata[i].mark,
	        		"value": selectdata[i].value,
	        		"alarmNum":selectdata[i].alarmNum,
	        		"data" : selectdata[i].data
	        	});
	        }
	    }
		var option = {
			"title" : {
				"show" : true,
				"text" : data[0].title,
				"textStyle" : {
					"fontSize" : 14,
					"color" : '#fff'
				},
				"x" : 'left',
				"y" : 12
			},
			"clickable": true,
			"selectedMode": 'single',
			"backgroundColor" : "rgba(0,0,0,0)",
			"dataRange" : {
				"splitNumber" : 4,
				"formatter" : "{value} ~ {value2}",
				"color" : [ "rgb(250,119,227)","rgb(240,249,30)","rgb(46,244,11)","rgb(143,209,232)" ],
				"textStyle" : 
					{
						"color" : '#fff'
					}
			},
			"tooltip" : {  
				"trigger": 'item',  
		        "formatter": function(params) {  
		              var res = params.name+'<br/>';  
			              var myseries = option.series;  
			              for (var i = 0; i < myseries.length; i++) {  
				              res+= myseries[i].name;  
				              for(var j=0;j<myseries[i].data.length;j++){  
					              if(myseries[i].data[j].name==params.name){  
					            	  res+=' : '+myseries[i].data[j].value+'</br>';  
				              }  
			              }         
		              }  
		              return res;  
		        }  
		    },
			"series" : [ 
	            {
            	"name" : data[0].title,
				"type" : "map",
				"selectedMode" : "multiple",
				"mapType" : province,
				"mapLocation" : {
					"width" : "100%",
					"height" : "95%",
					"x" : "center",
					"y" : "center"+20
				},
				"roam" : false,
				"itemStyle" : {
					"normal" : {
						"label" : {
							"show" : true,
							"formatter" : function(params) {
								var res =params.substr(0, 2) + '\n';
								var datas = data[0].data;
								for (var i = 0; i < datas.length; i++) {
									if (datas[i].mark == params) {
										res += datas[i].value +'  ';
									}
								}
								return res;
							},
							"textStyle" : {
								"color" : "#000000",
								"fontSize" : "12",
								"fontWeight" : "normal",
								"baseline" : "top"
							}
						},
						"borderColor" : "#FFFFFF",
						"borderWidth" : 1
					},
					"emphasis": {                 // 也是选中样式
	                    borderWidth:2,
	                    borderColor:'#fff',
	                    color: 'orange',
	                    label: {
	                        show: true,
	                        "formatter" : function(params) {
								var res =params.substr(0, 2) + '\n';
								var datas = data[0].data;
								for (var i = 0; i < datas.length; i++) {
									if (datas[i].mark == params) {
										res += datas[i].value +'  ';
									}
								}
								return res;
							}
	                    }
	                }
				},
				"data" : [],
				"markPoint": {
	                "symbolSize": 5,
	                "itemStyle": {
	                    "normal": {
	                        "borderColor": "#87cefa",
	                        "borderWidth": 1,
	                        "label": {
	                            "show": false
	                        }
	                    },
	                    "emphasis": {
	                        "borderColor": "#1e90ff",
	                        "borderWidth": 5,
	                        "label": {
	                            "show": false
	                        }
	                    }
	                },
	                "data": []
				},
				"geoCoord":geoCoordMap,
			},
			{
				"name" : "",
				"type": "map",
	            "selectedMode": "single",
	            "mapType": province,
	            "tooltip" : {
					"trigger" : "item",
					"formatter": function(params) {  
			              var res = params.name+'<br/>';  
			              var myseries = option.series;  
			              for (var i = 0; i < myseries.length; i++) {   
					              for(var j=0;j<myseries[i].data.length;j++){  
						              if(myseries[i].data[j].name==params.name){ 
						            	  res+='告警数量 : '+myseries[i].data[j].alarmNum+'</br>'; 
						            	  var alarm = myseries[i].data[j].data;
						            	  res+='告警信息 : '+'</br>';
						            	  for(var k=0;k<alarm.length;k++) {
						            		  res+=myseries[i].data[j].data[k].message+'</br>'; 
						            	  }
					              }  
				              }         
			              }  
			              return res;  
			        }
	            },
	            "data": [],
	            "markPoint": {
	                "clickable": true,
	                "symbol": "emptyCircle",
	                "effect": {
	                    "show": true,
	                    "shadowBlur": 0,
	                    "color": "#ff0000"
	                },
	                "itemStyle": {
	                    "normal": {
	                        "label": {
	                            "show": false
	                        }
	                    }
	                },
	                "data": convertData
	            }
			}
			]
		};
		var max = 0, min = 0, tmp = data[0].data;
		for (var i = 0; i < tmp.length; i++) {
			var value = parseInt(tmp[i].value);
			if (i == 0) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
			option.series[0].data.push({
				"name" : tmp[i].mark,
				"value" : tmp[i].value,
				"alarmNum":tmp[i].alarmNum,
				"data":tmp[i].data
			});
		}
		option.dataRange.max = max;
		option.dataRange.min = min;
		return option;
	}
	
	
	
	/**
         * 业务分析地图
         */
        function getAnaMapOption22(province, data, cuscolor, geoCoordMap) {
                var color = (cuscolor == null ? precolor : cuscolor);
                var selectdata = data[0].data;
                var convertData = [];
                for(var i = 0; i < selectdata.length; i++){
                    var geoCoord = geoCoordMap[selectdata[i].mark];
                    if (geoCoord && selectdata[i].alarmNum > 0 && selectdata[i].alarmNum!= null) {
                            convertData.push({
                                    "name" : selectdata[i].mark,
                                    "value": selectdata[i].value,
                                    "alarmNum":selectdata[i].alarmNum,
                                    "data" : selectdata[i].data
                            });
                    }
                }
                var option = {
                        "title" : {
                                "show" : true,
                                "text" : data[0].title,
                                "textStyle" : {
                                        "fontSize" : 14,
                                        "color" : '#fff'
                                },
                                "x" : 'left',
                                "y" : 12
                        },
                        "clickable": true,
                        "selectedMode": 'single',
                        "backgroundColor" : "rgba(0,0,0,0)",
                        "dataRange" : {
                                "splitNumber" : 4,
                                "formatter" : "{value} ~ {value2}",
                                "color" : [ "rgb(250,119,227)","rgb(240,249,30)","rgb(46,244,11)","rgb(143,209,232)" ],
                                "textStyle" : 
                                        {
                                                "color" : '#fff'
                                        }
                        },
                        "tooltip" : {  
                                "trigger": 'item',  
                        "formatter": function(params) {  
                              var res = params.name+'<br/>';  
                                      var myseries = option.series;  
                                      for (var i = 0; i < myseries.length; i++) {  
                                              res+= myseries[i].name;  
                                              for(var j=0;j<myseries[i].data.length;j++){  
                                                      if(myseries[i].data[j].name==params.name){  
                                                          res+=' : '+myseries[i].data[j].value+'</br>';  
                                              }  
                                      }         
                              }  
                              return res;  
                        }  
                    },
                        "series" : [ 
                    {
                "name" : data[0].title,
                                "type" : "map",
                                "selectedMode" : "multiple",
                                "mapType" : province,
                                "mapLocation" : {
                                        "width" : "100%",
                                        "height" : "95%",
                                        "x" : "center",
                                        "y" : "center"+20
                                },
                                "roam" : false,
                                "itemStyle" : {
                                        "normal" : {
                                                "label" : {
                                                        "show" : true,
                                                        "formatter" : function(params) {
                                                            var res =params + '\n';
//                                                            var res =params.name + '\n';    
                                                            var datas = data[0].data;
                                                                for (var i = 0; i < datas.length; i++) {
                                                                        if (datas[i].mark == params.name) {
                                                                                res += datas[i].value +'  ';
                                                                        }
                                                                }
                                                                return res;
                                                        },
                                                        "textStyle" : {
                                                                "color" : "#000000",
                                                                "fontSize" : "12",
                                                                "fontWeight" : "normal",
                                                                "baseline" : "top"
                                                        }
                                                },
                                                "borderColor" : "#FFFFFF",
                                                "borderWidth" : 1
                                        },
                                        "emphasis": {                 // 也是选中样式
                            borderWidth:2,
                            borderColor:'#fff',
                            color: 'orange',
                            label: {
                                show: true,
                                "formatter" : function(params) {
                                                                //var res =params.substr(0, 2) + '\n';
                                                                var res =params + '\n';
                                                                var datas = data[0].data;
                                                                for (var i = 0; i < datas.length; i++) {
                                                                        if (datas[i].mark == params.name) {
                                                                                res += datas[i].value +'  ';
                                                                        }
                                                                }
                                                                return res;
                                                        }
                            }
                        }
                                },
                                "data" : [],
                                "markPoint": {
                        "symbolSize": 5,
                        "itemStyle": {
                            "normal": {
                                "borderColor": "#87cefa",
                                "borderWidth": 1,
                                "label": {
                                    "show": false
                                }
                            },
                            "emphasis": {
                                "borderColor": "#1e90ff",
                                "borderWidth": 5,
                                "label": {
                                    "show": false
                                }
                            }
                        },
                        "data": []
                                },
                                "geoCoord":geoCoordMap,
                        },
                        {
                                "name" : "",
                                "type": "map",
                    "selectedMode": "single",
                    "mapType": province,
                    "tooltip" : {
                                        "trigger" : "item",
                                        "formatter": function(params) {  
                                      var res = params.name+'<br/>';  
                                      var myseries = option.series;  
                                      for (var i = 0; i < myseries.length; i++) {   
                                                      for(var j=0;j<myseries[i].data.length;j++){  
                                                              if(myseries[i].data[j].name==params.name){ 
                                                                  res+='告警数量 : '+myseries[i].data[j].alarmNum+'</br>'; 
                                                                  var alarm = myseries[i].data[j].data;
                                                                  res+='告警信息 : '+'</br>';
                                                                  for(var k=0;k<alarm.length;k++) {
                                                                          res+=myseries[i].data[j].data[k].message+'</br>'; 
                                                                  }
                                                      }  
                                              }         
                                      }  
                                      return res;  
                                }
                    },
                    "data": [],
                    "markPoint": {
                        "clickable": true,
                        "symbol": "emptyCircle",
                        "effect": {
                            "show": true,
                            "shadowBlur": 0,
                            "color": "#ff0000"
                        },
                        "itemStyle": {
                            "normal": {
                                "label": {
                                    "show": false
                                }
                            }
                        },
                        "data": convertData
                    }
                        }
                        ]
                };
                var max = 0, min = 0, tmp = data[0].data;
                for (var i = 0; i < tmp.length; i++) {
                        var value = parseInt(tmp[i].value);
                        if (i == 0) {
                                min = value;
                        }
                        if (value > max) {
                                max = value;
                        }
                        if (value < min) {
                                min = value;
                        }
                        option.series[0].data.push({
                                "name" : tmp[i].mark,
                                "value" : tmp[i].value,
                                "alarmNum":tmp[i].alarmNum,
                                "data":tmp[i].data
                        });
                }
                option.dataRange.max = max;
                option.dataRange.min = min;
                return option;
        }
	
	
	/**
	 * 业务分析地图new
	 */
	function getAnaMapOptionNew(province, data, cuscolor) {
            var color = (cuscolor == null ? precolor : cuscolor);
            var preData = data[0].data;
            var newData = [];
		for(var i = 0; i < preData.length; i++){
			newData.push({
        		"name" : preData[i].mark,
        		"value": preData[i].value
        	});
	    }
		var maxValue = 0, minValue = 0;
		for (var i = 0; i < preData.length; i++) {
			var value = parseInt(preData[i].value);
			if (i == 0) {
				minValue = value;
			}
			if (value > maxValue) {
				maxValue = value;
			}
			if (value < minValue) {
				minValue = value;
			}
		}
		var option = {
            title : {
                "show" : true,
                "text" : data[0].title,
                "textStyle" : {
                        "fontSize" : 14,
                        "color" : '#fff'
                },
                "x" : 'left',
                "y" : 12
            },
            tooltip: {
                trigger: 'item',
                formatter: '{b}<br/>'+data[0].title+'：{c}'
            },
            toolbox: {
                show: false,
                orient: 'vertical',
                left: 'right',
                top: 'center',
                feature: {
                    dataView: {readOnly: false},
                    restore: {},
                    saveAsImage: {}
                }
            },
            visualMap: {
                min: minValue,
                max: maxValue,
                text:['High','Low'],
                realtime: true,
                calculable: true,
                orient: 'horizontal',
                inRange: {
                    color: ['lightskyblue','yellow', 'orangered'],
                    symbolSize: [0, 0]
                }
            },
            series: [
                {
                    name: data[0].title,
                    type: 'map',
                    roam: 'scale',
                    layoutCenter:['45%','50%'],
                    layoutSize: '90%',
                    mapType: province,
                    itemStyle:{
                        normal:{label:{show:true}},
                        emphasis:{label:{show:true}}
                    },
                    data:newData
                }

            ]
        };
		return option;
	}
    /**
	 * 业务分析页面水平柱状图
	 */
    function getAnaHorizontalBar(title, data, cuscolor) {
        var color = (cuscolor == null ? precolor : cuscolor);
        var option = ecconf.getCumulateBarOption(title, color, []);
        option.grid = {
                "x":105,
                "y":10,
                "x2":40,
                "y2":20,
                "borderWidth":0
        };
        option.backgroundColor = "";
        option.legend.show = false;
        option.calculable=false;
        option.xAxis[0].splitLine = {
                "show" : false
            };
        option.yAxis[0].splitLine = {
            "show" : false
        };
        var tmp = [];
        for (var i = 0; i < data.length; i++) {
            tmp = data[i].data;
            option.series[i] = {
                    "name":data[i].title,
                    "type":"bar",
                    "barMaxWidth":10,
                    "data":[],
                    "itemStyle": {
                      "normal": {
                          color: function(params) {
                              return color[0]
                          },
                          "label": {
                              "show": true,
                              "position": "right",
                              "formatter": "{c}"
                          }
                      }
                  }
            };
            for (var j = 0; j < tmp.length; j++) {
                option.yAxis[0].data.push((tmp[j].mark==null?"":tmp[j].mark));
                option.series[i].data.push(tmp[j].value);
            }
        }
        return option;
    }
    
    /**
	 * 业务分析页面垂直柱状图
	 */
    function getAnaVerticalBar(title, data, legend, cuscolor,tenthousand) {
    	var option = ecconf.getBarOption(title, precolor, []);
        var axisLine = {
            "show" : true,
            "lineStyle" : {
                "color" : "#cccccc",
                "width" : 1
            }
        };
        option.backgroundColor = "";
        option.grid = {
            "x" : "60",
            "y" : "20",
            "x2" : "20",
            "y2" : "30",
        };
        option.legend.show = legend;
        option.legend.y = 'top' ;
        option.legend.itemWidth = 10 ;
        option.yAxis[0].axisLine = axisLine;
        option.xAxis[0].axisLine = axisLine;
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "",
                        "type" : "bar",
                        "barMaxWidth" : 50,
                        "itemStyle" : {
                            "normal" : {
                                "color" : ""
                            }
                        },
                        "data" : []
                    }
                }
            };
        })();
        pushData(data, option, seriesObj, cuscolor, "", "Vertical","","","anaverticalbar",tenthousand);
        return option;
    }
    
    // 关系图
    function getRelationshipGrap(title, data, cuscolor){
        var color = (cuscolor == null ? precolor : cuscolor);
        var links_status='正常';
        if(data==null){
            color='red';
            links_status='发生故障';
        }else if(data[0].value=='1'){
            color='black';
            links_status='正常';
        }else{
            color='red';
            links_status='发生故障';
        }
        var option = {
                title : {
                    text: '',
                    subtext: '',
                    x:'right',
                    y:'bottom'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: '{b}'
                },
                legend: {
                    x: 'left',
                    data:['']
                },
                series : [
                    {
                        type:'force',
                        name : "",
                        ribbonType: false,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    textStyle: {
                                        color: '#333'
                                    }
                                },
                                nodeStyle : {
                                    brushType : 'both',
                                    borderColor : 'rgba(255,215,0,0.4)',
                                    borderWidth : 1
                                },
                                linkStyle: {
                                    type: 'curve',
                                    color:color
                                }
                            }
                        },
                        maxRadius : 25,
                        gravity: 1.1,
                        scaling: 1.1,
                        roam: 'move',
                        nodes:[
                            {category:0, name: '服务器', value : 10, label: '服务器\n'},
                            {category:1, name: '目标主机',value : 2}
                        ],
                        links : [
                            {source : '服务器', target : '目标主机', weight : 2, name: links_status}
                        ]
                    }
                ]
            };
        return option;
    }
    
    //首页堆积柱状图
    function getHomeCumulateBarOption(title, data, cuscolor) {
        var option = ecconf.getHomeCumulateBarOption(title, precolor, []);
        var tmp = [];
        option.tooltip.formatter = "{b}<br/>";
        option.xAxis[0].axisLabel = {
            "formatter" : "{value}%"
        };
        option.xAxis[0].max = 100;
        var seriesObj = (function() {
            return {
                getSeries : function() {
                    return {
                        "name" : "",
                        "type" : "bar",
                        "stack" : "总量",
                        "barMaxWidth" : 35,
                        "itemStyle" : {
                            "normal" : {
                                "color" : "",
                                "areaStyle" : {
                                    "type" : "default"
                                },
                                "label" : {
                                    "show" : true,
                                    "position" : "inside",
                                    "formatter" : "{c}%",
                                    "textStyle" : {
                                        "fontSize" : 11
                                    }
                                },
                            }
                        },
                        "data" : []
                    }
                }
            };
        })();
        pushData(data, option, seriesObj, cuscolor, "%", "Horizontal");
        return option;
    }
    
    /**
	 * 首页饼图
	 */
    function getHomePieOption(data, itemcolor) {
    	var option = {
    	        title: {
    	            text: data[0].title,
    	            textStyle: {
    	                fontSize: 16,
    	                color: '#fff'
    	            },
    	            y: '2'
    	        },

    	        tooltip: {
    	            trigger: 'item',
    	            formatter: "{a} <br/>{b} : {c} ({d}%)"
    	        },

    	        visualMap: {
    	            show: false,
    	            min: 80,
    	            max: 600,
    	            inRange: {
    	                colorLightness: [0, 1]
    	            }
    	        },
    	        series: [
    	            {
    	                type: 'pie',
    	                radius: '55%',
    	                center: ['50%', '50%'],
    	                data: [ 
    	                ].sort(function (a, b) {
    	                    return a.value - b.value
    	                }),
    	                roseType: 'angle',
    	                label: {
    	                    normal: {
    	                        textStyle: {
    	                            color: 'rgba(255, 255, 255, 0.6)'
    	                        }
    	                    }
    	                },
    	                labelLine: {
    	                    normal: {
    	                        lineStyle: {
    	                            color: 'rgba(255, 255, 255, 0.6)'
    	                        },
    	                        smooth: 0.2,
    	                        length: 10,
    	                        length2: 20
    	                    }
    	                },
    	                itemStyle: {
    	                    normal: {
    	                        color: itemcolor,
    	                        shadowBlur: 200,
    	                        shadowColor: 'rgba(0, 0, 0, 0.1)'
    	                    }
    	                }
    	            }
    	        ]
    	    };
    	for (var i = 0; i < data[0].data.length; i++) {
    		option.series[0].data[i]={value: data[0].data[i].value, name: data[0].data[i].mark}
        }
        return option;
    }
    
});
