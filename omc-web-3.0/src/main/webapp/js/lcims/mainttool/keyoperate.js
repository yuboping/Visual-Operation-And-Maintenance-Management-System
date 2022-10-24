require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','stringutil'],
    function($,layer,laypage,resizewh,checkbox,stringutil) {
        var layer_load;
        var uuid_ct=0;
        var arr_len=0;
        var intervalProcess;
        resizewh.resizeBodyH($("#mainkeyOperate"));
        butBindFunction();
        reset("mainkeyOperate");
        initChildrenMenu();
        $("#checkboxAll").prop('checked',false);
//        loadingwait();
//        loadOptRecord();
        
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            loadOptRecord();
        });
        $("#resetbutton").click(function() {
            reset("mainkeyOperate");
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','keyOperatediv');
    }
    // 重置页面标签内容
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
    
    function initChildrenMenu() {
        var pageUrl = window.location.pathname;
        $("#operate_menu").empty();
        var url = "/view/class/querychildrenmdmenu";
        $.getJSON(url + "?pageUrl=" + pageUrl + "&random="+ Math.random(),function(result) {
                    if (result != null && result.length > 0) {
                        for (var i = 0; i < result.length; i++) {
                            $("#operate_menu").append('<a href="#" id="'
                               + result[i].name
                               + '" value=\''+result[i].url
                               + '\' class="button button-small button-primary">'
                               + result[i].show_name
                               + '</a> ');
                        }
                        $("#operate_menu a").bind("click",function(){
                            operate($("#"+this.id).attr("value"));
                        });
                    }
          });
    }
    // 一键直通、恢复多主机操作操作
    function operate(type) {
        var arr = checkbox.getReturnArray();
        arr_len = arr.length;
        var message="请选择主机数据";
        if(arr_len==0){
            layer.msg(message,{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
            return;
        }
        if(type==1) {
            message = "是否执行直通操作?"
        } else if(type==2) {
            message = "是否执行恢复操作?"
        } else {
            return;
        }
        layer.confirm(message, {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function() {
            layer.closeAll();
            loadingwait();
            var jsonData = JSON.stringify(arr);
            var data = {operateType:type, ipdatas:jsonData};
            var url = "/mainttool/operateRadiusBusiness";
            freshOperateResult(arr);
            $.ajax({
                type: "post",
                url: url+"?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
//                    layer.close(layer_load);
//                    layer.closeAll();
                    layer.msg(result.message,{
                        time:3000,
                        skin: 'layer_msg_color_success'
                    });
                    $("#uuid").val(result.data);
                    cleanCheckAll2();
                    uuid_ct = 0;
                    intervalProcess = setInterval(refreshOperateResult,1000);
                }
             });
        });
    }
    
    function freshOperateResult(arr) {
        for(var i=0;i<arr.length;i++) {
            var id = replaceTo_(arr[i]);
            $("#rt_"+id).empty().html("执行中");
        }
    }
    
    function refreshOperateResult() {
        var uuid = $("#uuid").val();
        //测试uuid
//        uuid = "ea2bbb1c-1f01-449f-9197-0e4b6faba446";
        if(uuid_ct==arr_len && uuid_ct>0) {
//            cleanCheckAll();
            //终止定时任务
            if(null!=intervalProcess) {
                clearInterval(intervalProcess);
            }
            layer.close(layer_load);
            layer.closeAll();
            layer.msg("操作执行完成",{
                time:3000,
                skin: 'layer_msg_color_success'
            });
            return;
        }
        var url = "/mainttool/queryOperateRadiusBusinessResult";
        var data = {uuid:uuid};
        $.ajax({
            type: "post",
            url: url+"?random=" + Math.random(),
            data: data,
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
                // 数据处理
                if(result.opSucc) {
                    makeRadiusBusinessRsult(result.data);
                } else {
                    uuid_ct=arr_len;
                }
            }
         });
        
    }
    function makeRadiusBusinessRsult(data) {
        uuid_ct = 0;
        if(null!=data) {
            var size = data.length;
            for(var i=0; i<size ; i++) {
                var rowninfo = data[i];
                var id = replaceTo_(rowninfo.host_ip);
                $("#rt_"+id).empty().html(rowninfo.operate_state_name);
                if(rowninfo.operate_state != 2) {
                    uuid_ct++;
                }
            }
        }
    }
    
    // 一键直通、恢复单台主机操作操作
    function operateOne(type, checkId) {
        var arr = [checkId];
        arr_len = arr.length;
        var message="";
        if(type==1) {
            message = "是否执行直通操作?"
        } else if(type==2) {
            message = "是否执行恢复操作?"
        } else {
            return;
        }
        layer.confirm(message, {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function() {
            layer.closeAll();
            loadingwait();
            var jsonData = JSON.stringify(arr);
            var data = {operateType:type, ipdatas:jsonData};
            var url = "/mainttool/operateRadiusBusiness";
            freshOperateResult(arr);
            $.ajax({
                type: "post",
                url: url+"?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                    layer.msg(result.message,{
                        time:3000,
                        skin: 'layer_msg_color_success'
                    });
                    $("#uuid").val(result.data);
                    uuid_ct = 0;
                    intervalProcess = setInterval(refreshOperateResult,1000);
                }
             });
        });
    }
    
    //查询功能
    function loadOptRecord() {
        var nodeid = $("#select_nodeid").val();
        if(stringutil.checkString("select_nodeid",nodeid,"请选择节点!"))
        {
            return;
        }
        var url = "/mainttool/queryRadiusHostInfo";
        var data = {nodeid:nodeid};
        cleanCheckAll();
        loadingwait();
        $.ajax({
            type: "post",
            url: url+"?random=" + Math.random(),
            data: data,
            cache: false,
            async: false, 
            dataType: "json",
            success: function (result) {
                layer.close(layer_load);
                var size = result.length;
                $("#querynum").html(size);
                //组装
                var rowdata = "";
                for(var i=0; i<size ; i++) {
                    var rowninfo = result[i];
                    var checked = "";
                    if(checkbox.isExitArray(rowninfo.addr)){
                        checked = "checked=\"checked\"";
                        k++;
                    }
                    var ipId = replaceTo_(rowninfo.addr);
                    rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" value=\""+rowninfo.addr+"\" id=\""+ipId+"\" "+checked+" />"+"</td>"
                        +"<td title=\""+rowninfo.nodeidname+"\">"+rowninfo.nodeidname+"</td>"
                        +"<td title=\""+rowninfo.addr+"\">"+rowninfo.addr+"</td>"
                        +"<td id=\"rt_"+ipId+"\">未执行</td>"
                        +"<td>"
                        +"<a class=\"J_delete pr10\" href=\"#\" name='operate_straightIn' value=\""+rowninfo.addr+"\" id=\"straightIn_"+ipId+"\">直通</a> "
                        +"<a class=\"J_delete pr10\" href=\"#\" name='operate_recover' value=\""+rowninfo.addr+"\" id=\"recover_"+ipId+"\">恢复</a>"
                        +"</td>"
                }
                $("#keyOperatediv").empty().append(rowdata);
                //复选框增加选中事件
                checkbox.bindSingleCheckbox("keyOperatediv");
                $("[name=operate_straightIn]").each(function(){
                    $(this).on('click',function(){
                        var checkId = $(this).parent().parent().find("input").first().attr('value');
                        operateOne(1,checkId);
                    });
                });
                $("[name=operate_recover]").each(function(){
                    $(this).on('click',function(){
                        var checkId = $(this).parent().parent().find("input").first().attr('value');
                        operateOne(2,checkId);
                    });
                });
            }
        });
    }
    
    function replaceTo_(ip) {
        return ip.replace(/\./g,"_");
    }
    
    function cleanCheckAll() {
        $("#checkboxAll").prop('checked',false);
        checkbox.cleanArray();
    }
    
    function cleanCheckAll2() {
        $("#checkboxAll").prop('checked',false);
        checkbox.cleanArray();
        $("#keyOperatediv input").each(function(){
            $(this).prop('checked',false);
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] // 0.1透明度的白色背景
          });
    }
});