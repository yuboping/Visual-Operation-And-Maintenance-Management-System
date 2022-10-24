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

        resizewh.resizeBodyH($("#mainaisgroup"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','aisgroupdiv');
        });
        reset("mainaisgroup");
        initChildrenMenu();
        loadingwait();
        loadOptRecord();

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainaisgroup");
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

        $("#add_icon").on("change", function() {
            var show_add_class = document.getElementById("add_show_class");
            var icon = $("#add_icon").val();
            show_add_class.setAttribute("class",icon);
        });

        $("#modify_icon").on("change", function() {
            var show_modify_class = document.getElementById("modify_show_class");
            var icon = $("#modify_icon").val();
            show_modify_class.setAttribute("class",icon);
        });

        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','aisgroupdiv');
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
        $("#aisgroupdiv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
    }

    // 新增按钮事件
    function addShow() {
        reset("add_div");
        $("#add_icon").change();
        showLayer("add_div", '新增巡检组信息');
    }

    // 新增确认
    function addInfo() {
        var group_name = stringutil.Trim($("#add_name").val());
        var icon = stringutil.Trim($("#add_icon").val());
        var description = stringutil.Trim($("#add_description").val());
        if (validName('add', group_name)) {
            return;
        }
        loadingwait();
        var data = {group_name:group_name, icon:icon, description:description};
        $.getJSON("/view/class/ais/aisgroupmanage/add?random="
                + Math.random(), data, function(result) {
            if(result.opSucc) {
                layer.closeAll();
                layerResultAndReload(result);
            }else{
                if(result.data == "repeat"){
                    layer.close(layer_load);
                    $("#add_name").focus();
                    layer.tips("巡检组名称不能重复", '#add_name',{ tips: [2, '#EE1A23'] });
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
            layer.msg("修改请只选择一个巡检组!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var group_id = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {group_id : group_id, group_name: ""};
            $.ajax({
                type: "POST",
                url: "/view/class/ais/aisgroupmanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if(result.length>0){
                        var info = result[0];
                        $("#modify_group_id").val(info.group_id);
                        $("#modify_name").val(info.group_name);
                        $("#modify_icon option").each(function(){
                            if($(this).attr('value')==info.icon){
                                $(this).prop("selected", "true");
                            }
                        });
                        $("#modify_icon").change();
                        // $("#modify_icon").val(info.icon);
                        $("#modify_description").val(info.description);
                        showLayer("modify_div",'修改巡检组信息');
                    }
                }
            });
        }
    }

    // 修改确认
    function modifyInfo() {
        var group_id = $("#modify_group_id").val();
        var group_name = stringutil.Trim($("#modify_name").val());
        var icon = stringutil.Trim($("#modify_icon").val());
        var description = stringutil.Trim($("#modify_description").val());
        if (validName('modify', group_name)) {
            return;
        }
        loadingwait();
        // 校验指标类型名称是否重复事件

        var data = {group_id:group_id, group_name:group_name, icon:icon, description:description};

        $.getJSON("/view/class/ais/aisgroupmanage/modify?random="
            + Math.random(), data, function(result) {
            if(result.opSucc) {
                layer.closeAll();
                layerResultAndReload(result);
            }else{
                if(result.data == "repeat"){
                    layer.close(layer_load);
                    $("#modify_name").focus();
                    layer.tips("巡检组名称不能重复", '#modify_name',{ tips: [2, '#EE1A23'] });
                    return;
                }else{
                    layer.closeAll();
                    layerResultAndReload(result);
                }
            }
        });

        // $.ajax({
        //     type: "POST",
        //     url: "/view/class/ais/aisgroupmanage/modify?random=" + Math.random(),
        //     cache: false,
        //     async: false,
        //     data: {group_id:group_id, group_name:group_name, icon:icon, description:description},
        //     success: function (result) {
        //         if(result.opSucc) {
        //             layer.closeAll();
        //             layerResultAndReload(result);
        //         }else{
        //             if(result.data == "repeat"){
        //                 layer.close(layer_load);
        //                 $("#modify_name").focus();
        //                 layer.tips("巡检组名称不能重复", '#modify_name',{ tips: [2, '#EE1A23'] });
        //                 return;
        //             }else{
        //                 layer.closeAll();
        //                 layerResultAndReload(result);
        //             }
        //         }
        //     }
        // });
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

        var group_name = stringutil.Trim($("#group_name").val());
        var data = {
            'group_name' : group_name
        };
        $.getJSON("/view/class/ais/aisgroupmanage/query?random=" + Math.random(), data, function(result) {
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
                    resizewh.resizeBodyH($("#mainaisgroup"));
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
            var id = rowninfo.group_id;

            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata
                + "<tr><td>" +"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.group_id+"\" id=\""+rowninfo.group_id+"\" "+checked+" />"
                + "</td><td title=\""+ rowninfo.group_name +"\">" + rowninfo.group_name
                + "</td><td title=\""+stringutil.isNull(rowninfo.description)+"\">" + stringutil.isNull(rowninfo.description)
                + "</td><td title=\""+stringutil.isNull(rowninfo.icon_name)+"\">" + stringutil.isNull(rowninfo.icon_name)
                + "</td></tr>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }
        $("#aisgroupdiv").empty().append(rowdata);
        checkbox.bindSingleCheckbox("aisgroupdiv");
    }

    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个巡检组信息!",{
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
                var data = {aisGroupArray:checkboxArray};
                var url = "/view/class/ais/aisgroupmanage/delete?random=" + Math.random();
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
            area : [ '550px', '510px' ],
            content : $("#" + divid)
        });
    }

    function validName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_name").focus();
            layer.tips('巡检组名称不能为空!', '#' + type + '_name', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
});