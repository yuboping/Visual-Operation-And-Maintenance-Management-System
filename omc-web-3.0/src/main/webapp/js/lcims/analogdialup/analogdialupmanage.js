require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer': '/js/layer/layer',
        "laypage": "/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh','checkbox','stringutil' ],
    function($, layer, laypage, resizewh,checkbox,stringutil) {
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
        loadOptRecord();

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
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

    // 新增按钮事件
    function addShow() {
        reset("add_div");
        showLayer("add_div", '新增模拟拨测信息');
    }

    // 新增确认
    function addInfo() {
        var add_host_id = stringutil.Trim($("#add_host_id").val());
        var add_min_rate = stringutil.Trim($("#add_min_rate").val());
        var add_username = stringutil.Trim($("#add_username").val());
        var add_password = stringutil.Trim($("#add_password").val());
        var add_nas_port = stringutil.Trim($("#add_nas_port").val());
        var add_call_from_id = stringutil.Trim($("#add_call_from_id").val());
        var add_call_to_id = stringutil.Trim($("#add_call_to_id").val());
        var add_ext = stringutil.Trim($("#add_ext").val());
        if(stringutil.checkString("add_username",add_username,"账号名称不能为空!")|| 
        		stringutil.checkString('add_password',add_password,"密码不能为空!")){
            return;
        }
        loadingwait();
        var data = {host_id:add_host_id, 
        		min_rate:add_min_rate,
        		username:add_username,
        		password:add_password,
        		nas_port:add_nas_port,
        		call_from_id:add_call_from_id,
        		call_to_id:add_call_to_id,
        		ext:add_ext};
        $.getJSON("/view/class/system/analogdialupmanage/add?random="
                + Math.random(), data, function(result) {
            if(result.opSucc) {
                layer.closeAll();
                layerResultAndReload(result);
            }
        });
    }

    // 修改按钮事件
    function modifyShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个模拟拨测!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var id = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {id : id};
            $.ajax({
                type: "POST",
                url: "/view/class/system/analogdialupmanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if(result.length>0){
                        var info = result[0];
                        $("#modify_host_id").val(info.host_id);
                        $("#modify_min_rate").val(info.min_rate);
                        $("#modify_username").val(info.username);
                        $("#modify_nas_port").val(info.nas_port);
                        $("#modify_call_from_id").val(info.call_from_id);
                        $("#modify_call_to_id").val(info.call_to_id);
                        $("#modify_ext").val(info.ext);
                        showLayer("modify_div",'修改模拟拨测信息');
                    }
                }
            });
        }
    }

    // 修改确认
    function modifyInfo() {
    	var checkboxArray = checkbox.getReturnArray();
    	if(checkboxArray.length != 1){
    		layer.msg("修改请只选择一个模拟拨测!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
    	}else{
    		var id = checkboxArray[0];
    		var modify_host_id = stringutil.Trim($("#modify_host_id").val());
            var modify_min_rate = stringutil.Trim($("#modify_min_rate").val());
            var modify_username = stringutil.Trim($("#modify_username").val());
            var modify_password = stringutil.Trim($("#modify_password").val());
            var modify_nas_port = stringutil.Trim($("#modify_nas_port").val());
            var modify_call_from_id = stringutil.Trim($("#modify_call_from_id").val());
            var modify_call_to_id = stringutil.Trim($("#modify_call_to_id").val());
            var modify_ext = stringutil.Trim($("#modify_ext").val());
            if(stringutil.checkString("modify_username",modify_username,"账号名称不能为空!")|| 
            		stringutil.checkString('modify_password',modify_password,"密码不能为空!")){
                return;
            }
            loadingwait();
            var data = {id:id, 
            		host_id:modify_host_id, 
            		min_rate:modify_min_rate,
            		username:modify_username,
            		password:modify_password,
            		nas_port:modify_nas_port,
            		call_from_id:modify_call_from_id,
            		call_to_id:modify_call_to_id,
            		ext:modify_ext};
            $.getJSON("/view/class/system/analogdialupmanage/modify?random="
                + Math.random(), data, function(result) {
                    layer.closeAll();
                    layerResultAndReload(result);
            });
    	}
    }

    // 查询按钮事件
    function queryOpt() {
        loadingwait();
        loadOptRecord();
    }
    // 加载查询内容
    function loadOptRecord() {
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 10;

        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();

        var host_name = stringutil.Trim($("#host_name").val());
        var host_ip = stringutil.Trim($("#host_ip").val());
        var username = stringutil.Trim($("#username").val());
        var data = {
            'host_name' : host_name,
            'host_ip' : host_ip,
            'username' : username
        };
        $.getJSON("/view/class/system/analogdialupmanage/query?random=" + Math.random(), data, function(result) {
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
                    resizewh.resizeBodyH($("#mainanalogdialup"));
                },
                groups : page_count
            // 连续显示分页数
            });
        });
    }
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
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
                + "</td><td title=\""+stringutil.isNull(rowninfo.min_rate_name)+"\">" + stringutil.isNull(rowninfo.min_rate_name)
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

    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个模拟拨测信息!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            layer.confirm('是否确认删除该批次数据？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] // 按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {analogDialUpIdArray:checkboxArray};
                var url = "/view/class/system/analogdialupmanage/delete?random=" + Math.random();
                $.getJSON(url,data,function(result){
                    layer.close(layer_load);
                    loadOptRecord();
                    layer.alert(result.message);
                })
            });
        }
    }
    
  //详情按钮事件
    function detailShow(analogdialupid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/analogdialupmanage/query?id="+analogdialupid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	if(result.length>0){
                    var analogdialup = result[0];
                    $("#detail_host_name").val(analogdialup.host_name);
                    $("#detail_host_ip").val(analogdialup.host_ip);
                    $("#detail_min_rate_name").val(analogdialup.min_rate_name);
                    $("#detail_username").val(analogdialup.username);
                    $("#detail_nas_port").val(analogdialup.nas_port);
                    $("#detail_call_from_id").val(analogdialup.call_from_id);
                    $("#detail_call_to_id").val(analogdialup.call_to_id);
                    $("#detail_ext").val(analogdialup.ext);
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
            area : [ '620px', '300px' ],
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
            loadOptRecord();
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