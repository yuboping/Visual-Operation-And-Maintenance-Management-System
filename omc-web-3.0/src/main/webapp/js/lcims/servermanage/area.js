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

        resizewh.resizeBodyH($("#mainarea"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','areadiv');
        });
        reset("mainarea");
        initChildrenMenu();
        // loadingwait();
        // loadOptRecord();

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainarea");
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
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','areadiv');
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
        $("#"+divid+" select").each(function(){
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
    }

    function bindCheckBox(){
        $("#areadiv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
    }

    // 新增按钮事件
    function addShow() {
        reset("add_div");
        showLayer("add_div", '新增属地信息');
    }

    // 新增确认
    function addInfo() {
        var name = stringutil.Trim($("#add_name").val());
        if (validName('add', name)) {
            return;
        }
        loadingwait();
        var data = {name:name};
        $.getJSON("/view/class/system/areamanage/add?random="
                + Math.random(), data, function(result) {
            if(result.opSucc) {
                layer.closeAll();
                layerResultAndReload(result);
            }else{
                if(result.data == "repeat"){
                    layer.close(layer_load);
                    $("#add_name").focus();
                    layer.tips("属地名称不能重复", '#add_name',{ tips: [2, '#EE1A23'] });
                    return;
                }else{
                    layer.closeAll();
                    layerResultAndReload(result);
                }
            }
        });
    }

    // 修改按钮事件
    function modifyShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个属地!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var areano = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {areano : areano, name: ""};
            $.ajax({
                type: "post",
                url: "/view/class/system/areamanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if(result.length>0){
                        var info = result[0];
                        $("#modify_areano").val(areano);
                        $("#modify_name").val(info.name);
                        showLayer("modify_div",'修改属地信息');
                    }
                }
            });
        }
    }

    // 修改确认
    function modifyInfo() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个属地!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var areano = checkboxArray[0];
            var name = stringutil.Trim($("#modify_name").val());
            if (validName('modify', name)) {
                return;
            }
            loadingwait();
            // 校验指标类型名称是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/areamanage/modify?random=" + Math.random(),
                cache: false,
                async: false,
                data: {areano : areano, name : name},
                success: function (result) {
                    if(result.opSucc) {
                        layer.closeAll();
                        layerResultAndReload(result);
                    }else{
                        if(result.data == "repeat"){
                            layer.close(layer_load);
                            $("#modify_name").focus();
                            layer.tips("属地名称不能重复", '#modify_name',{ tips: [2, '#EE1A23'] });
                            return;
                        }else{
                            layer.closeAll();
                            layerResultAndReload(result);
                        }
                    }
                }
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

        var name = stringutil.Trim($("#name").val());
        var data = {
            'name' : name
        };
        $.getJSON("/view/class/system/areamanage/query?random=" + Math.random(), data, function(result) {
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
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            var id = rowninfo.areano;

            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.areano+"\" id=\""+rowninfo.areano+"\" "+checked+" />"
                + "</td><td>" + rowninfo.name + "</td><td>"
                + rowninfo.areano + "</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }
        $("#areadiv").empty().append(rowdata);
        checkbox.bindSingleCheckbox("areadiv");
    }

    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个属地信息!",{
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
                var data = {areaArray:checkboxArray};
                var url = "/view/class/system/areamanage/delete?random=" + Math.random();
                $.getJSON(url,data,function(result){
                    layer.close(layer_load);
                    loadOptRecord();
                    layer.alert(result.message);
                })
            });
        }
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
            area : [ '500px', '210px' ],
            content : $("#" + divid)
        });
    }

    function validName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_name").focus();
            layer.tips('属地名称不能为空!', '#' + type + '_name', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
});