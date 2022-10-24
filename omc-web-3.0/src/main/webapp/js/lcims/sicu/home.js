require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        'layer' : '/js/layer/layer',
        'topalarmutil': '/js/lcims/tool/topalarmutil'
    }
});


require(['jquery','layer','topalarmutil'
      ],function($,layer,topalarmutil) {
    
    //加载js
    loadJs();
    $("body").css("overflow","auto");
    
    //图表5分钟刷新一次  1000*60*5
    setInterval(refreshData,1000*60*5);
    
    function loadJs(){
        $.getScript("/js/lcims/home/jquery.mCustomScrollbar.concat.min.js",function(){
            refreshData();
        });
    }
    
    function refreshData(){
        loadAlarmInfoData();
        loadHostPerformanceData();
    }
    
    //加载主机性能信息
    function loadHostPerformanceData(){
        //content
        var data= {};
        $.getJSON("/data/home/getHostPerformance?random=" + Math.random(),data, function(result) {
            var total = result.data.length;
            if(total>0){
                var rowdata = "";
                for(var i=0; i<total; i++){
                    var rowninfo = result.data[i];
                    rowdata = rowdata +"<tr>"
                    rowdata = rowdata + "<td title='"+rowninfo.hostname+"'>"+ rowninfo.hostname +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.addr+"'>"+ rowninfo.addr +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.hosttypename+"'>"+ rowninfo.hosttypename +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.nodename+"'>"+ rowninfo.nodename +"</td>";
                    var cpuval = getStr(rowninfo.cpu.mvalue);
                    if(rowninfo.cpu.alarmflag){
                        cpuval = getHostPerformanceAlarmClass(rowninfo.cpu.alarm_level,cpuval);
                    }
                    rowdata = rowdata + "<td>"+ cpuval +"</td>";
                    
                    var memoryval = getStr(rowninfo.memory.mvalue);
                    if(rowninfo.memory.alarmflag){
                        memoryval = getHostPerformanceAlarmClass(rowninfo.memory.alarm_level,memoryval);
                    }
                    rowdata = rowdata + "<td>"+ memoryval +"</td>";
                    
                    var connectableval = getIsNormal(rowninfo.connectable.mvalue);
                    if(rowninfo.connectable.alarmflag){
                        connectableval = getHostPerformanceAlarmClass(rowninfo.connectable.alarm_level,connectableval);
                    }
                    rowdata = rowdata + "<td>"+ connectableval +"</td>";
                    
                    var processval = "正常";
                    if(rowninfo.processlist.length==0){
                        processval = "";
                    }
                    if(rowninfo.process.alarmflag){
                        processval = "异常";
                        processval = getHostPerformanceAlarmClass(rowninfo.process.alarm_level,processval);
                    }
                    rowdata = rowdata + "<td>"+ processval +"</td>";
                    
                    
                    rowdata = rowdata + "</tr>";
                }
//                console.log("hostperformance--query:"+new Date());
                $("#hostperformance").empty().append(rowdata);
                try{
                    $("#hostperformance").mCustomScrollbar({
                        scrollButtons:{
                            enable:true
                        },
                        theme:"light-2"
                    });
                }catch(err){
//                    console.log("hostperformance:"+err);
                }
            }
        });
    }
    
    function getStr(value){
        if(value==null || value==""){
            return "";
        }
        return value;
    }
    
    function getIsNormal(value){
        if(value==null || value==""){
            return "";
        }
        if(value=='1'){
            return "正常";
        }
        return "异常";
    }
    
    function getHostPerformanceAlarmClass(alarm_level,value){
        var val = "";
        if(alarm_level == 1){
            val = "<span class=\"label label-info\">"+value+"</span>";
        }else if(alarm_level == 2){
            val = "<span class=\"label label-success\">"+value+"</span>";
        }else if(alarm_level == 3){
            val = "<span class=\"label label-warning\">"+value+"</span>";
        }else{
            val = "<span class=\"text-danger\">"+value+"</span>";
        }
        return val;
    }
    
    //加载当前最新告警信息
    function loadAlarmInfoData(){
        var data= {};
        $.getJSON("/view/class/system/alarmquery/querywithindex?random=" + Math.random(),data, function(result) {
            var total = result.length;
            //console.log("alarminfoData--query:alarm num is "+total+",query time is "+new Date());
            if(total>0){
                var rowdata = "";
                for(var i=0; i<total; i++){
                    var rowninfo = result[i];
                    var 
                    rowdata = rowdata +'<tr id="del_'+rowninfo.alarm_id+'">'
                    rowdata = rowdata + "<td title='"+rowninfo.dimension_name+"'>"+ rowninfo.dimension_name +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.metric_name+"'>"+ rowninfo.metric_name +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.alarm_level_name+"'>"+ getHostPerformanceAlarmClass(rowninfo.alarm_level,rowninfo.alarm_level_name) +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.alarmmsg+"'>"+ rowninfo.alarmmsg +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.first_time+"'>"+ rowninfo.first_time +"</td>";
                    rowdata = rowdata + "<td title='"+rowninfo.confirm_state_name+"'>"+ rowninfo.confirm_state_name +"</td>";
                    rowdata = rowdata + '<td> <a name="operate_confirm" id="'+rowninfo.alarm_id+'_'+rowninfo.confirm_state+'" class="button btn-primary">确认</a>'+
                    '&nbsp;<a name="operate_delete" id="'+rowninfo.alarm_id+'" class="button btn-primary">删除</a>' +'</td>';
                    rowdata = rowdata +"</tr>"
                }
                //console.log("alarminfoData--query:alarm rowdata is "+rowdata);
                $("#alarminfoData").empty().append(rowdata);
                $("[name=operate_confirm]").each(function(){
                    $(this).on('click',function(){
                        alarmConfirmShow($(this).attr('id'));
                    });
                });
                $("[name=operate_delete]").each(function(){
                    $(this).on('click',function(){
                        alarmDeleteShow($(this).attr('id'));
                    });
                });
                
                try{
                    $("#alarminfoData").mCustomScrollbar({
                        scrollButtons:{
                            enable:true
                        },
                        theme:"light-2"
                    });
                }catch(err){
                    //console.log("alarminfoData:"+err);
                }
            }else{
                $("#alarminfoData").empty();
            }
        });
    }
    
    function getAlarmClass(alarm_level){
        var styleclass = "iconfont dep_red_bg icon-cry";
        if(alarm_level == 1){
            styleclass = "iconfont dep_yellow_bg icon-cry";
        }else if(alarm_level == 2){
            styleclass = "iconfont dep_green_bg icon-cry";
        }else if(alarm_level == 3){
            styleclass = "iconfont dep_red_bg icon-cry";
        }
        return styleclass;
    }
    
    function alarmConfirmShow(id){
        var ids = id.split("_");
        if(ids[1] == 1){
            layer.alert("该条告警信息已确认");
        }else{
            layer.confirm('是否确认', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] //按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var alarmArray = new Array();
                alarmArray[0] = ids[0];
                var data = {alarmArray:alarmArray};
                $.ajax({
                    type: "post",
                    url: "/view/class/system/alarmquery/confirmAlarm?random=" + Math.random(),
                    data: data,
                    cache: false,
                    async: false, 
                    dataType: "json",
                    success: function (result) {
                        layer.close(layer_load);
                        layer.msg(result.message,{
                            time:2000,
                            skin: 'layer_msg_color_success'
                        });
                        alarmConfirmSuccess();
                    }
                });
            });
        }
    }
    function alarmConfirmSuccess(){
        loadAlarmInfoData()
        topalarmutil.alarm();
    }
    function alarmDeleteShow(id){
        layer.confirm('是否删除', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function(){
            layer.closeAll();
            loadingwait();
            var alarmArray = new Array();
            alarmArray[0] = id;
            var data = {alarmArray:alarmArray};
            $.ajax({
                type: "post",
                url: "/view/class/system/alarmquery/deleteAlarm?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                    layer.close(layer_load);
                    layer.msg(result.message,{
                        time:2000,
                        skin: 'layer_msg_color_success'
                    });
                    removeAlarmInfo(id);
                }
            });
        });
    }
    
    function removeAlarmInfo(id){
        $("#del_"+id).remove();
        topalarmutil.alarm();
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
    
    function closewait(){
        layer_load = layer.load(1, {
            shade:0
         });
    }
//    
//    $(window).load(function(){
//    });
    
    }
);
