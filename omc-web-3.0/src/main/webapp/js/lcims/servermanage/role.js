require.config({
    paths : {
        'lcims' : "/js/lcims",
        'resizewh' : "/js/lcims/resizewh/resizewh",
        'jquery' : '/js/jquery/jquery.min',
        'iscroll' : '/js/lcims/tool/iscroll',
        'layer' : '/js/layer/layer',
        "laypage" : "/js/lcims/tool/laypage"
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh' ],
    function($, layer, laypage, resizewh) {
        var layer_load;
        
        resizewh.resizeWH($("#mainrole"));
        butBindFunction();
        
        loadingwait();
        loadOptRecord();
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
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
    }
    // 重置页面标签内容
    function reset(divid) {
        $("#" + divid + " input[type='text']").each(function() {
            $(this).val('');
        });
    }
    
    // 新增按钮事件
    function addShow() {
        reset("add_div");
        $.getJSON("/view/class/system/rolemanage/query/menulist?random=" + Math.random(), function(result) {
            var temp = "";
            $.each(result,function(i,info){
                temp = temp + "<span class=\"span6\"><input name=\"add_menu\" value=\""+info.type
                +"\" type=\"checkbox\">"+info.busname+"</span>";
            });
            $("#add_menulist").empty().append(temp);
        });
        showLayer("add_div", '新增角色信息');
    }

    // 新增确认
    function addInfo() {
        var name = $("#add_name").val();
        var menulist="";
        $.each($("input[name='add_menu']:checked"),function(i,info){
            if(i==0){
                menulist = $(info).val();
            }else{
                menulist = menulist+","+$(info).val();
            }
        });
        if (validName('add', name)||validMenulist('add', menulist)) {
            return;
        }
        loadingwait();
        var data = {name:name,menulist:menulist};
        $.getJSON("/view/class/system/rolemanage/add/?random=" + Math.random(), data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }

    // 修改按钮事件
    function modifyShow(roleid) {
        reset("modify_div");
        loadingwait();
        var data={roleid:roleid};
        $.getJSON("/view/class/system/rolemanage/query/menulist?random=" + Math.random(),data, function(result) {
            var temp = "";
            $.each(result,function(i,info){
                temp = temp + "<span class=\"span6\"><input name=\"modify_menu\" value=\""+info.type+"\"";
                if(Boolean(info.checkflag)){
                    temp = temp + "checked=\""+info.checkflag+"\"";
                }
                temp = temp + "type=\"checkbox\">"+info.busname+"</span>";
            });
            $("#modify_menulist").empty().append(temp);
        });
        var data = { roleid : roleid};
        $.getJSON("/view/class/system/rolemanage/query/singleinfo?random=" + Math.random(), data, function(result) {
            if (result.length > 0) {
                var info = result[0];
                $("#modify_roleid").val(roleid);
                $("#modify_name").val(info.name);
                showLayer("modify_div", '修改角色信息');
            }
        });
    }

    // 修改确认
    function modifyInfo() {
        var name = $("#modify_name").val();
        var roleid = $("#modify_roleid").val();
        var menulist="";
        $.each($("input[name='modify_menu']:checked"),function(i,info){
            if(i==0){
                menulist = $(info).val();
            }else{
                menulist = menulist+","+$(info).val();
            }
        });
        if (validName('modify', name)||validMenulist('modify', menulist)) {
            return;
        }
        loadingwait();
        var data = {roleid:roleid,name:name,menulist:menulist};
        $.getJSON("/view/class/system/rolemanage/modify/?random=" + Math.random(), data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
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
        var page_count = 12;

        var name = $("#name").val();
        var data = {
            'name' : name
        };
        $.getJSON("/view/class/system/rolemanage/query/infolist?random=" + Math.random(), data, function(result) {
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
                    $("#currnum").text(startnum + "-" + endnum);
                    resizewh.resizeWH($("#mainrole"));
                },
                groups : page_count
            // 连续显示分页数
            });
        });
    }
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        for (var i = startnum; i <= endnum; i++) {
            var rowninfo = data[i - 1];
            rowdata = rowdata + "<tr><td>" + rowninfo.name + "</td><td>" + rowninfo.menulist
                    + "</td><td>" + "<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""
                    + rowninfo.roleid + "\" >修改</a>"
                    + "<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""
                    + rowninfo.roleid + "\">删除</a>" + "</td>";
        }
        $("#nodediv").empty().append(rowdata);
        $("[name=modify]").each(function() {
            $(this).on('click', function() {
                modifyShow($(this).attr('id'));
            });
        });
        $("[name=delete]").each(function() {
            $(this).on('click', function() {
                deleteShow($(this).attr('id'));
            });
        });
    }

    function deleteShow(roleid) {
        var layershow = layer.confirm('是否确认删除该条数据？', {
            closeBtn : 0,
            title : '询问',
            btn : [ '确认', '取消' ]
        // 按钮
        }, function() {
            layer.closeAll();
            loadingwait();
            
            var data = { roleid : roleid };
            var url = "/view/class/system/rolemanage/delete/?random=" + Math.random();
            $.getJSON(url, data, function(result) {
                layer.close(layershow);
                layerResultAndReload(result);
            })
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
            area : [ '500px', '250px' ],
            content : $("#" + divid)
        });
    }

    function validName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_name").focus();
            layer.tips('角色名称不能为空!', '#' + type + '_name', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    function validMenulist(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_menulist").focus();
            layer.tips('权限不能为空!', '#' + type + '_menulist', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
});