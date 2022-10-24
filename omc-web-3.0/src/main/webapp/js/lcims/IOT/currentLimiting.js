require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil',
        'select2': '/js/select2/select2.min'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','stringutil','select2'],
    function($,layer,laypage,resizewh,checkbox,stringutil,select2) {
        var layer_load;
        var apnName;

        resizewh.resizeBodyH($("#main"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','apndiv');
        });
        reset("main");
        initChildrenMenu();
        // loadingwait();
        // loadOptRecord();
        apnNameList();

//----------------------------------以下为自定义方法-------------------------------------------------//
        function butBindFunction(){
            $("#querybutton").click(function() {
                queryOpt();
            });
            $("#resetbutton").click(function() {
                reset("main");
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
            checkbox.bindAllCheckbox('checkboxAll','apndiv');

        }

        //初始化下拉列表
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

        function initChildrenMenu(){
            var pageUrl=window.location.pathname;
            $("#opOPEerate_menu").empty();
            var url = "/view/class/querychildrenmdmenu";
            $.getJSON(url+"?pageUrl="+pageUrl+"&random=" + Math.random(), function(result) {
                if(result!=null && result.length >0 ){
                    for(var i=0;i<result.length;i++){
                        $("#operate_menu").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary">'+result[i].show_name+'</a> ');
                        //新增、修改、删除绑定事件
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

        function bindCheckBox(){
            $("#apndiv :checkbox").click(function(){
                var flag = $(this).prop('checked');
                var value = $(this).val();
                checkbox.checkboxSingle(flag,value);
            });
        }

        //判断变量是否存在于数组中
        function IsInArray(arr,val) {
            var testStr=',' + arr.join(",") + ",";
            return testStr.indexOf("," + val + ",") != -1;
        }

        //新增按钮事件
        function addShow(){
            reset("add_div");
            $("#add_apn_id").val(null).trigger("change");   //清除选中项
            showLayer("add_div",'新增限流规则');
        }

        //新增确认
        function addInfo(){
            var apn_id = stringutil.Trim($("#add_apn_id").val());
            var limit_cycle = stringutil.Trim($("#add_limit_cycle").val());
            var auth_value = stringutil.Trim($("#add_auth_value").val());
            var log_value = $("#add_log_value").val();
            var record_value = $("#add_record_value").val();

            if(
                stringutil.checkString('add_apn_id',apn_id,"apn名称不能为空!") ||
                stringutil.checkString('add_limit_cycle',limit_cycle,"限流周期不能为空!") ||
                stringutil.checkString('add_auth_value',auth_value,"认证限流阀值不能为空!") ||
                stringutil.checkString('add_log_value',log_value,"日志限流阀值不能为空!") ||
                stringutil.checkString('add_record_value',record_value,"记录次数限流阀值不能为空!")){
                return;
            }
            if (limit_cycle > 4294967296){
                $("#add_limit_cycle").focus();
                layer.tips("限流周期长度超过", '#add_limit_cycle', {
                    tips : [ 2, '#EE1A23' ]
                });
                return;
            }
            if (!(auth_value >= -2147483648 && auth_value <= 2147483647)){
                $("#add_auth_value").focus();
                layer.tips("认证限流阀值需在-2147483648 - 2147483647范围内", '#add_auth_value', {
                    tips : [ 2, '#EE1A23' ]
                });
                return;
            }
            if (!(log_value >= -2147483648 && log_value <= 2147483647)){
                $("#add_log_value").focus();
                layer.tips("日志限流阀值需在-2147483648 - 2147483647范围内", '#add_log_value', {
                    tips : [ 2, '#EE1A23' ]
                });
                return;
            }
            if (!(record_value >= -2147483648 && record_value <= 2147483647)){
                $("#add_record_value").focus();
                layer.tips("每日限流阀值需在-2147483648 - 2147483647范围内", '#add_record_value', {
                    tips : [ 2, '#EE1A23' ]
                });
                return;
            }
            
            if ((parseInt(auth_value) >= parseInt(log_value) || parseInt(log_value) >= parseInt(record_value))){
                $("#add_auth_value").focus();
                layer.tips("认证限流阀值、日志限流阀值和记录次数限流阀值需满足递增关系，即认证限流阀值<日志限流阀值<记录次数限流阀值", '#add_auth_value', {
                    tips : [ 2, '#EE1A23' ]
                });
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
                    day_value:0
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
                                            day_value:0
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

        //修改按钮事件
        function modifyShow(){
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length != 1){
                layer.msg("修改请只选择一个apn!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                var apnid = checkboxArray[0];
                reset("modify_div");
                var data = {apnId : apnid};
                $.ajax({
                    type: "post",
                    url: "/view/class/system/currentlimiting/query?random=" + Math.random(),
                    data: data,
                    cache: false,
                    async: false,
                    dataType: "json",
                    success: function (result) {
                        if(result.length>0){
                            var apn = result[0];
                            $("#modify_apn_id").val(apn.apnId);
                            $("#modify_limit_cycle").val(apn.limit_cycle);
                            $("#modify_auth_value").val(apn.auth_value);
                            $("#modify_log_value").val(apn.log_value);
                            $("#modify_record_value").val(apn.record_value);
                            showLayer("modify_div",'修改apn');
                        }
                    }
                });
            }
        }

        //修改确认
        function modifyInfo(){
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length != 1){
                layer.msg("修改请只选择一个apn!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                var ruleId = checkboxArray[0];
                var apn_id = stringutil.Trim($("#modify_apn_id").val());
                var limit_cycle = stringutil.Trim($("#modify_limit_cycle").val());
                var auth_value = stringutil.Trim($("#modify_auth_value").val());
                var log_value = $("#modify_log_value").val();
                var record_value = $("#modify_record_value").val();

                if(
                    stringutil.checkString('modify_apn_id',apn_id,"apn名称不能为空!") ||
                    stringutil.checkString('modify_limit_cycle',limit_cycle,"限流周期不能为空!") ||
                    stringutil.checkString('modify_auth_value',auth_value,"认证限流阀值不能为空!") ||
                    stringutil.checkString('modify_log_value',log_value,"日志限流阀值不能为空!") ||
                    stringutil.checkString('modify_record_value',record_value,"记录次数限流阀值不能为空!")){
                    return;
                }
                if (limit_cycle > 4294967296){
                    $("#modify_limit_cycle").focus();
                    layer.tips("限流周期长度超过", '#modify_limit_cycle', {
                        tips : [ 2, '#EE1A23' ]
                    });
                    return;
                }
                if (!(auth_value >= -2147483648 && auth_value <= 2147483647)){
                    $("#modify_auth_value").focus();
                    layer.tips("认证限流阀值需在-2147483648 - 2147483647范围内", '#modify_auth_value', {
                        tips : [ 2, '#EE1A23' ]
                    });
                    return;
                }
                if (!(log_value >= -2147483648 && log_value <= 2147483647)){
                    $("#modify_log_value").focus();
                    layer.tips("日志限流阀值需在-2147483648 - 2147483647范围内", '#modify_log_value', {
                        tips : [ 2, '#EE1A23' ]
                    });
                    return;
                }
                if (!(record_value >= -2147483648 && record_value <= 2147483647)){
                    $("#modify_record_value").focus();
                    layer.tips("每日限流阀值需在-2147483648 - 2147483647范围内", '#modify_record_value', {
                        tips : [ 2, '#EE1A23' ]
                    });
                    return;
                }
                
                if ((parseInt(auth_value) >= parseInt(log_value) || parseInt(log_value) >= parseInt(record_value))){
                    $("#modify_auth_value").focus();
                    layer.tips("认证限流阀值、日志限流阀值和记录次数限流阀值需满足递增关系，即认证限流阀值<日志限流阀值<记录次数限流阀值", '#modify_auth_value', {
                        tips : [ 2, '#EE1A23' ]
                    });
                    return;
                }

                loadingwait();
                $.ajax({
                    type: "post",
                    url: "/view/class/system/currentlimiting/modifyApn?random=" + Math.random(),
                    cache: false,
                    async: false,
                    data:{
                        // ruleId:ruleId,
                        apnId:apn_id,
                        limit_cycle:limit_cycle,
                        auth_value:auth_value,
                        log_value:log_value,
                        record_value:record_value,
                        day_value:0
                    },
                    success:function (result) {
                        var data = result;
                        if (data.ret == "0"){
                            var dataModel = {
                                // ruleId:ruleId,
                                apnId:apn_id,
                                limit_cycle:limit_cycle,
                                auth_value:auth_value,
                                log_value:log_value,
                                record_value:record_value,
                                day_value:0
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

        //详情按钮事件
        function detailShow(apnid){
            reset("detail_div");
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/currentlimiting/query?ruleId="+apnid+"&random=" + Math.random(),
                cache: false,
                async: false,
                success: function (result) {
                    if(result.length>0){
                        var apn = result[0];
                        $("#detail_apn_id").val(apn.apn);
                        $("#detail_limit_cycle").val(apn.limit_cycle);
                        $("#detail_auth_value").val(apn.auth_value);
                        $("#detail_log_value").val(apn.log_value);
                        $("#detail_record_value").val(apn.record_value);
                        showLayerDetail("detail_div",'apn详情');
                    }
                }
            });
        }

        function isNull(data){
            if(data==null || data ==''){
                return '';
            }else
                return data;
        }

        //查询按钮事件
        function queryOpt(){
            loadingwait();
            loadOptRecord();
        }
        //加载查询内容
        function loadOptRecord(){
            //分页显示的初始化数据
            var pagecount=0;
            var page_count = 10;
            $("#checkboxAll").prop("checked", false);
            checkbox.cleanArray();

            var apn_name = stringutil.Trim($("#apn_name").val());
            var data = {'apn':apn_name};
            $.getJSON("/view/class/system/currentlimiting/query?random=" + Math.random(),data, function(result) {
                layer.close(layer_load);
                total = result.length;
                pagecount=Math.ceil(total/page_count);
                $("#querynum").text(total);
                laypage({
                    cont: 'pageinfo',
                    skin: '#6AB0F4',
                    pages: pagecount,
                    curr: 1,
                    skip: false, //是否开启跳页
                    jump: function(obj, first){ //触发分页后的回调
                        startnum = (obj.curr - 1) * page_count + 1;
                        endnum = obj.curr * page_count;
                        endnum = endnum > total ? total : endnum;
                        showTable(result,startnum,endnum);
                        $("#currnum").text( startnum + "-" + endnum);
                        if(total==0){
                            $("#currnum").empty().text("0 ");
                        }
                        resizewh.resizeBodyH($("#main"));
                    },
                    groups: page_count //连续显示分页数
                });
            });
        }
        //拼接tr
        function showTable(data,startnum,endnum){
            var rowdata = "";
            //计算选中条数
            var k = 0;
            for(var i=startnum;i<=endnum;i++){
                var rowninfo = data[i-1];
                var id = rowninfo.ruleId;
                var checked = "";

                if(checkbox.isExitArray(id)){
                    checked = "checked=\"checked\"";
                    k++;
                }
                rowdata = rowdata + "<tr>" +
                    "<td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.apnId+"\" id=\""+rowninfo.apnId+"\" "+checked+" />"+"</td>" +
                    "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.apn)+"\">"+stringutil.isNull(rowninfo.apn)+"</td>" +
                    "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.limit_cycle)+"\">" +stringutil.isNull(rowninfo.limit_cycle)+"</td>" +
                    "<td>"+stringutil.isNull(rowninfo.auth_value)+"</td>" +
                    "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.log_value)+"\">" +stringutil.isNull(rowninfo.log_value)+"</td>" +
                    "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.record_value)+"\">"+stringutil.isNull(rowninfo.record_value)+"</td>" +
                    "<td><a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.ruleId+"\">详情</a></td>" +
                    "</tr>"
            }
            //本页条数
            var ct = endnum-startnum+1;
            if(k==ct&&k>0){
                //全选框选中
                $("#checkboxAll").prop("checked", true);
            }else{
                $("#checkboxAll").prop("checked", false);
            }
            $("#apndiv").empty().append(rowdata);
            $("[name=detail]").each(function(){
                $(this).on('click',function(){
                    detailShow($(this).attr('id'));
                });
            });
            checkbox.bindSingleCheckbox("apndiv");
        }

        //删除
        function deleteShow() {
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length == 0){
                layer.msg("请选择至少一个apn!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                layer.confirm('是否确认删除该批次数据？', {
                    closeBtn:0,
                    title: '询问',
                    btn: ['确认','取消'] //按钮
                },function(){
                    layer.closeAll();
                    loadingwait();
                    // var ruleid = checkboxArray[0];
                    // var data = {ruleId : ruleid};
                    $.ajax({
                        type: "post",
                        url: "/view/class/system/currentlimiting/cancelApn?random=" + Math.random(),
                        cache: false,
                        async: false,
                        data:{apnArray : checkboxArray},
                        success:function (result) {
                            var data = result;
                            if (data.ret == "0"){
                                var data = {apnIdArray:checkboxArray};
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
            }
        }

        function layerResultAndReload(result){
            layer.close(layer_load);
            if(result.opSucc){
                queryOpt();
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

        function addLayerResultAndReload(result){
            layer.close(layer_load);
            if(result.opSucc){
                queryOpt();
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

        function showLayerDetail(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '610px', '250px' ],
                content : $("#"+divid)
            });
        }

        function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '560px', '340px' ],
                content : $("#"+divid)
            });
        }

        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
        }
    });