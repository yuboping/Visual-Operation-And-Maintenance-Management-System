require.config({
    paths : {
        'lcims' : "/js/lcims",
        'resizewh' : "/js/lcims/resizewh/resizewh",
        'jquery' : '/js/jquery/jquery.min',
        'jstree' : '/js/jstree/jstree.min',
        'iscroll' : '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer' : '/js/layer/layer',
        "laypage" : "/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require([ 'jquery',  'layer', 'laypage', 'resizewh','checkbox','stringutil','jstree'],
    function($, layer, laypage, resizewh,checkbox,stringutil, jstree) {
        var layer_load,layer_div;

        resizewh.resizeBodyH($("#mainrole"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','rolediv');
        });
        reset("mainrole");
        initChildrenMenu();
        // loadingwait();
        // loadOptRecord();

// ----------------------------------Jstree实现方法-------------------------------------------------//

        // 选择的时候调用的方法
        // $('#add_jstree').on("changed.jstree", function (e, data) {
        //      alert(data.selected);
        // });

// ----------------------------------以下为自定义方法-------------------------------------------------//

        function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainrole");
        });
        $("#addbutton").click(function() {
            addShow();
        });
        $("#add_ok").click(function() {
            addInfo();
        });
        $("#add_cancle").click(function() {
            layer.closeAll();
            hideDiv("add");
        });
        $("#modify_ok").click(function() {
            modifyInfo();
        });
        $("#modify_cancle").click(function() {
            layer.closeAll();
            hideDiv("modify");
        });
        $("#detail_ok").click(function() {
            layer.closeAll();
            hideDiv("detail");
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','rolediv');
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
        $("#rolediv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
    }

    // 新增按钮事件
    function addShow() {
        reset("add_div");
        $("#add_areanolist_div").hide();
        $("#add_nodelist_div").hide();
        $("#add_jstree_div").hide();
        $.getJSON("/view/class/system/rolemanage/query/arealist?random="+Math.random(),function(result){
            var temp = "";
            $.each(result,function(i,info){
                if(info.areano != null && info.areano != ""){
                    if(info.disabled == "0"){
                        if(info.name.length < 6) {
                            temp = temp + "<span class=\"span3\" style=\"color:#A9A9A9\"><input name=\"add_areanolist_check\" disabled=\"disabled\" value=\""+info.areano
                                +"\" type=\"checkbox\">"+info.name+"</span>";
                        }else if(info.name.length < 12){
                            temp = temp + "<span class=\"span6\" style=\"color:#A9A9A9\"><input name=\"add_areanolist_check\" disabled=\"disabled\" value=\""+info.areano
                                +"\" type=\"checkbox\">"+info.name+"</span>";
                        }else{
                            temp = temp + "<span class=\"span9\" style=\"color:#A9A9A9\"><input name=\"add_areanolist_check\" disabled=\"disabled\" value=\""+info.areano
                                +"\" type=\"checkbox\">"+info.name+"</span>";
                        }
                    }else{
                        if(info.name.length < 6) {
                            temp = temp + "<span class=\"span3\"><input name=\"add_areanolist_check\" value=\""+info.areano
                                +"\" type=\"checkbox\">"+info.name+"</span>";
                        }else if(info.name.length < 12){
                            temp = temp + "<span class=\"span6\"><input name=\"add_areanolist_check\" value=\""+info.areano
                                +"\" type=\"checkbox\">"+info.name+"</span>";
                        }else{
                            temp = temp + "<span class=\"span9\"><input name=\"add_areanolist_check\" value=\""+info.areano
                                +"\" type=\"checkbox\">"+info.name+"</span>";
                        }
                    }

                }
            });
            $("#add_areanolist").empty().append(temp);
        });
        $.getJSON("/view/class/system/rolemanage/query/nodelist?random="+Math.random(),function(result){
            var temp = "";
            $.each(result,function(i,info){
                if(info.disabled == "0") {
                    if (info.node_name.length < 6) {
                        temp = temp + "<span class=\"span3\" style=\"color:#A9A9A9\"><input name=\"add_nodelist_check\" disabled=\"disabled\" value=\"" + info.id
                            + "\" type=\"checkbox\">" + info.node_name + "</span>";
                    } else if (info.node_name.length < 12) {
                        temp = temp + "<span class=\"span6\" style=\"color:#A9A9A9\"><input name=\"add_nodelist_check\" disabled=\"disabled\" value=\"" + info.id
                            + "\" type=\"checkbox\">" + info.node_name + "</span>";
                    } else {
                        temp = temp + "<span class=\"span9\" style=\"color:#A9A9A9\"><input name=\"add_nodelist_check\" disabled=\"disabled\" value=\"" + info.id
                            + "\" type=\"checkbox\">" + info.node_name + "</span>";
                    }
                }else{
                    if (info.node_name.length < 6) {
                        temp = temp + "<span class=\"span3\"><input name=\"add_nodelist_check\" value=\"" + info.id
                            + "\" type=\"checkbox\">" + info.node_name + "</span>";
                    } else if (info.node_name.length < 12) {
                        temp = temp + "<span class=\"span6\"><input name=\"add_nodelist_check\" value=\"" + info.id
                            + "\" type=\"checkbox\">" + info.node_name + "</span>";
                    } else {
                        temp = temp + "<span class=\"span9\"><input name=\"add_nodelist_check\" value=\"" + info.id
                            + "\" type=\"checkbox\">" + info.node_name + "</span>";
                    }
                }
            });
            $("#add_nodelist").empty().append(temp);
        });
        $.getJSON("/view/class/system/rolemanage/query/paramList?random="+Math.random(),function(result){
            var temp = "";
            $.each(result,function(i,info){
                temp = temp + "<span class=\"span3\"><input name=\"add_paramlist_check\" value=\""+info.code
                    +"\" type=\"checkbox\">"+info.description+"</span>";
            });
            $("#add_roleid").empty().append(temp);
            unbindRoleChangeFunction("add");
            bindRoleChangeFunction("add");
        });
        //一般data从后台返回，调用这个方法显示视图
        $.getJSON("/view/class/system/rolemanage/query/menuList?random="+Math.random(),function(result){
            //展示树div
            mkRoleModuleList("add");
            $('#add_jstree').jstree({'plugins':["wholerow","checkbox"],
                'checkbox' : {
                    "undetermined" : true,
                    "three_state" : true,
                    "cascade_to_disabled" : false
                }, "core" : {
                    "multiple" : true,
                    'data' : result
                }
            });
            $("#add_jstree").on("loaded.jstree", function (event, data) {
                // 展开所有节点
                $('#add_jstree').jstree('open_all');
                // 展开指定节点
                // data.instance.open_node([1,10]);     // 单个节点 （1 是顶层的id）
            });
        });

        showLayer("add_div", '新增角色信息');
        $("#layui-layer"+layer_div).css("height","300px");
        $("#layui-layer"+layer_div+" .layui-layer-content").css("height","250px");
    }

    function unbindRoleChangeFunction(type){
        $("#"+type+"_roleid").unbind();
    }

    function bindRoleChangeFunction(type){
        $("#"+type+"_roleid").change(function(){
            var paramlist="";
            var roleid=$("#"+type+"_roleid").val();
            $("input[name='"+type+"_paramlist_check']:checked").each(function(){
                paramlist=paramlist+","+$(this).val();
            });
            var height = "470px";
            $("#layui-layer"+layer_div).css("height",height);
            $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
            mkNodeList(paramlist,type);
            mkAreaList(paramlist,type);
        });
    }

    function mkNodeList(paramlist,type){
        if(paramlist.indexOf("node")> -1 ){
            var content = $("#"+type+"_nodelist").html();
            if(content == null || content.length == 0){
                $("#"+type+"_nodelist_div").hide();
            }else{
                // $("input[name='"+type+"_nodelist_check']:checked").each(function(){
                //     $(this).attr('checked',false);
                // });
                $("#"+type+"_nodelist_div").show();
            }
        }else{
            $("#"+type+"_nodelist_div").hide();
        }
    }

    function mkAreaList(paramlist,type){
        if(paramlist.indexOf("area")> -1 ){
            var content = $("#"+type+"_areanolist").html();
            if(content == null || content.length == 0){
                $("#"+type+"_areanolist").hide();
            }else{
                // $("input[name='"+type+"_areanolist_check']:checked").each(function(){
                //     $(this).attr('checked',false);
                // });
                $("#"+type+"_areanolist_div").show();
            }
        }else{
            $("#"+type+"_areanolist_div").hide();
        }
    }

    function mkRoleModuleList(type){
        $("#"+type+"_jstree_div").show();
    }

    // 新增确认
    function addInfo() {
        var rolename = stringutil.Trim($("#add_rolename").val());
        var description = stringutil.Trim($("#add_description").val());
        if (validName('add', rolename)) {
            return;
        }
        var areanolist="",nodelist="";
        $("input[name='add_paramlist_check']:checked").each(function(){
            if($(this).val() == "area"){
                $("input[name='add_areanolist_check']:checked").each(function(){
                    areanolist=areanolist+","+$(this).val();
                });
            }
            if($(this).val() == "node"){
                $("input[name='add_nodelist_check']:checked").each(function(){
                    nodelist=nodelist+","+$(this).val();
                });
            }
        });

        //get_selected(true)时获取数组全部值
        var menulist = $('#add_jstree').jstree(true).get_selected();
        var menulist2 = $('#add_jstree').jstree(true).get_undetermined();

        areanolist=areanolist.substring(1,areanolist.length);
        nodelist=nodelist.substring(1,nodelist.length);
        var data = {name:rolename,description:description,areanolist:areanolist,menulist:menulist,menulist2:menulist2,nodelist:nodelist};
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/rolemanage/add?random=" + Math.random(),
            data: data,
            cache: false,
            async: false,
            dataType: "json",
            success: function (result) {
                if(result.opSucc) {
                    layer.closeAll();
                    hideDiv("add");
                    layerResultAndReload(result);
                }else{
                    if(result.data == "repeat"){
                        layer.close(layer_load);
                        $("#add_rolename").focus();
                        layer.tips("角色名称不能重复", '#add_rolename',{ tips: [2, '#EE1A23'] });
                        return;
                    }else {
                        layer.closeAll();
                        hideDiv("add");
                        layerResultAndReload(result);
                    }
                }
            }
        });
    }

    // 修改按钮事件
    function modifyShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个角色信息!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
            return;
        }
        var roleid = checkboxArray[0];
        reset("modify_div");
        var data = { roleid : roleid, querytype:"1"};
        loadingwait();
        $.getJSON("/view/class/system/rolemanage/querymodifyinfo?random=" + Math.random(), data, function(result) {
            if(result.opSucc) {
                var info = result.data;
                $("#modify_id").val(info.roleinfo.roleid);
                $("#modify_rolename").val(info.roleinfo.name);
                $("#modify_description").val(info.roleinfo.description);
                $("#modify_nodelist_div").hide();
                $("#modify_areanolist_div").hide();
                $("#modify_jstree_div").hide();
                //可管理城市
                var temp = "";
                $.each(info.arealist,function(i,info){
                    if(info != null){
                        if(info.disabled == "0"){
                            if(info.name.length < 6) {
                                temp = temp + "<span class=\"span3\" style=\"color:#A9A9A9\"><input name=\"modify_areanolist_check\" disabled=\"disabled\" value=\"" + info.areano + "\"";
                            }else if(info.name.length < 12){
                                temp = temp + "<span class=\"span6\" style=\"color:#A9A9A9\"><input name=\"modify_areanolist_check\" disabled=\"disabled\" value=\"" + info.areano + "\"";
                            }else{
                                temp = temp + "<span class=\"span9\" style=\"color:#A9A9A9\"><input name=\"modify_areanolist_check\" disabled=\"disabled\" value=\"" + info.areano + "\"";
                            }
                        }else{
                            if(info.name.length < 6) {
                                temp = temp + "<span class=\"span3\"><input name=\"modify_areanolist_check\" value=\"" + info.areano + "\"";
                            }else if(info.name.length < 12){
                                temp = temp + "<span class=\"span6\"><input name=\"modify_areanolist_check\" value=\"" + info.areano + "\"";
                            }else{
                                temp = temp + "<span class=\"span9\"><input name=\"modify_areanolist_check\" value=\"" + info.areano + "\"";
                            }
                        }

                        if (info.checked == "1") {
                            temp = temp + "checked='checked' ";
                        }
                        temp = temp + "type=\"checkbox\">" + info.name + "</span>";

                    }
                });
                $("#modify_areanolist").empty().append(temp);
                //可管理节点
                temp = "";
                $.each(info.nodelist,function(i,info){
                    if(info.disabled == "0"){
                        if(info.node_name.length < 6) {
                            temp = temp + "<span class=\"span3\" style=\"color:#A9A9A9\"><input name=\"modify_nodelist_check\" disabled=\"disabled\" value=\"" + info.id + "\"";
                        }else if(info.node_name.length < 12){
                            temp = temp + "<span class=\"span6\" style=\"color:#A9A9A9\"><input name=\"modify_nodelist_check\" disabled=\"disabled\" value=\"" + info.id + "\"";
                        }else{
                            temp = temp + "<span class=\"span9\" style=\"color:#A9A9A9\"><input name=\"modify_nodelist_check\" disabled=\"disabled\" value=\"" + info.id + "\"";
                        }
                    }else{
                        if(info.node_name.length < 6) {
                            temp = temp + "<span class=\"span3\"><input name=\"modify_nodelist_check\" value=\"" + info.id + "\"";
                        }else if(info.node_name.length < 12){
                            temp = temp + "<span class=\"span6\"><input name=\"modify_nodelist_check\" value=\"" + info.id + "\"";
                        }else{
                            temp = temp + "<span class=\"span9\"><input name=\"modify_nodelist_check\" value=\"" + info.id + "\"";
                        }

                    }


                    if (info.checked == "1") {
                        temp = temp + "checked='checked' ";
                    }
                    temp = temp + "type=\"checkbox\">" + info.node_name + "</span>";

                });
                $("#modify_nodelist").empty().append(temp);
                //参数
                temp = "";
                $.each(info.paramlist,function(i,info){
                    if(info != null){
                        temp = temp + "<span class=\"span3\"><input name=\"modify_paramlist_check\" value=\""+info.code+"\"";
                        if(info.count > 0){
                            temp = temp + "checked='checked' ";
                            if(info.code == "area"){
                                modifyContentList("areanolist","modify");
                            }else{
                                modifyContentList("nodelist","modify");
                            }
                        }
                        temp = temp + "type=\"checkbox\">"+info.description+"</span>";
                    }
                });
                $("#modify_roleid").empty().append(temp);
                unbindRoleChangeFunction("modify");
                bindRoleChangeFunction("modify");
                //树结构展示
                $('#modify_jstree').jstree({'plugins':["wholerow","checkbox"] ,
                    'checkbox' : {
                        "undetermined" : true,
                        "three_state" : true,
                        // "cascade" : 'undetermined,up,down',
                        "cascade_to_disabled" : false
                    }, "core" : {
                        "multiple" : true,
                        'data' : info.menulist
                    }
                });
                $("#modify_jstree").on("loaded.jstree", function (event, data) {
                    // 展开所有节点
                    $('#modify_jstree').jstree('open_all');
                    // 展开指定节点
                    // data.instance.open_node([1,10]);     // 单个节点 （1 是顶层的id）
                });
                showLayer("modify_div", '修改角色信息');
                $("#modify_jstree_div").show();
                var height = "470px";
                $("#layui-layer"+layer_div).css("height",height);
                $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
            }else{
                layerResultAndReload(result);
            }
        });
    }

    function modifyContentList(source,type){
        var content = $("#"+type+"_"+source+"_div").html();
        if(content == null || content.length == 0){
            $("#"+type+"_"+source+"_div").hide();
        }else{
            $("#"+type+"_"+source+"_div").show();
        }
    }

    // 修改确认
    function modifyInfo() {
        var roleid = $("#modify_id").val();
        var name = stringutil.Trim($("#modify_rolename").val());
        var description = stringutil.Trim($("#modify_description").val());
        var areanolist="",nodelist="";
        $("input[name='modify_paramlist_check']:checked").each(function(){
            if($(this).val() == "area"){
                $("input[name='modify_areanolist_check']:checked").each(function(){
                    areanolist=areanolist+","+$(this).val();
                });
            }
            if($(this).val() == "node"){
                $("input[name='modify_nodelist_check']:checked").each(function(){
                    nodelist=nodelist+","+$(this).val();
                });
            }
        });

        //get_selected(true)时获取数组全部值
        var menulist = $('#modify_jstree').jstree(true).get_selected();
        var menulist2 = $('#modify_jstree').jstree(true).get_undetermined();
        areanolist=areanolist.substring(1,areanolist.length);
        nodelist=nodelist.substring(1,nodelist.length);
        if (validName('modify', name)) {
            return;
        }
        var data = {roleid:roleid,name:name,description:description,areanolist:areanolist,nodelist:nodelist,menulist:menulist,menulist2:menulist2};
        loadingwait();
        $.getJSON("/view/class/system/rolemanage/modify/?random=" + Math.random(), data, function(result) {
            if(result.opSucc) {
                layer.closeAll();
                hideDiv("modify");
                layerResultAndReload(result);
            }else{
                if(result.data == "repeat"){
                    layer.close(layer_load);
                    $("#modify_rolename").focus();
                    layer.tips("角色名称不能重复", '#modify_rolename',{ tips: [2, '#EE1A23'] });
                    return;
                }else{
                    layer.closeAll();
                    hideDiv("modify");
                    layerResultAndReload(result);
                }
            }
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
        var page_count = 10;

        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();

        var name = stringutil.Trim($("#name").val());
        var data = {
            'name' : name
        };
        $.getJSON("/view/class/system/rolemanage/query?random=" + Math.random(), data, function(result) {
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
                    if(total==0){
                        $("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#mainrole"));
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
            var id = rowninfo.roleid;

            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.roleid+"\" id=\""+rowninfo.roleid+"\" "+checked+" />"
                + "</td><td>" + rowninfo.name + "</td>"
                +"<td>"+"<a class=\"J_edit pr10\" href=\"#\" name='detail' id=\""+rowninfo.roleid+"\" >详情</a>"+"</td>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }
        $("#rolediv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
                detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("rolediv");
    }

    // 详情按钮事件
    function detailShow(id){
        reset("detail_div");
        var data = {roleid:id};
        loadingwait();
        $.getJSON("/view/class/system/rolemanage/querydetailinfo?random=" + Math.random(), data, function(result) {
            if(result.opSucc) {
                var info = result.data;
                $("#detail_id").val(info.roleinfo.roleid);
                $("#detail_rolename").val(info.roleinfo.name);
                $("#detail_description").val(info.roleinfo.description);
                $("#detail_nodelist_div").hide();
                $("#detail_areanolist_div").hide();
                $("#detail_jstree_div").hide();
                //参数
                var temp = "";
                $.each(info.paramlist,function(i,info){
                    if(info != null){
                        temp = temp + "<span class=\"span3\"><input name=\"detail_paramlist_check\" disabled=\"disabled\" value=\""+info.code+"\"";
                        if(info.count > 0){
                            temp = temp + "checked='checked' ";
                            if(info.code == "area"){
                                modifyContentList("areanolist","detail");
                            }else{
                                modifyContentList("nodelist","detail");
                            }
                        }
                        temp = temp + "type=\"checkbox\">"+info.description+"</span>";
                    }
                });
                $("#detail_roleid").empty().append(temp);
                unbindRoleChangeFunction("detail");
                bindRoleChangeFunction("detail");
                //可管理城市
                temp = "";
                $.each(info.arealist,function(i,info){
                    if(info != null){
                        if(info.name.length < 6) {
                            temp = temp + "<span class=\"span3\"><input name=\"detail_areanolist_check\" disabled=\"disabled\" value=\"" + info.areano + "\"";
                        }else if(info.name.length < 12){
                            temp = temp + "<span class=\"span6\"><input name=\"detail_areanolist_check\" disabled=\"disabled\" value=\"" + info.areano + "\"";
                        }else{
                            temp = temp + "<span class=\"span9\"><input name=\"detail_areanolist_check\" disabled=\"disabled\" value=\"" + info.areano + "\"";
                        }
                        if(info.checked == "1"){
                            temp = temp + "checked='checked' ";
                        }
                        temp = temp + "type=\"checkbox\">"+info.name+"</span>";
                    }
                });
                $("#detail_areanolist").empty().append(temp);
                //可管理节点
                temp = "";
                $.each(info.nodelist,function(i,info){
                    if(info.node_name.length < 6) {
                        temp = temp + "<span class=\"span3\"><input name=\"detail_nodelist_check\" disabled=\"disabled\" value=\"" + info.id + "\"";
                    }else if(info.node_name.length < 12){
                        temp = temp + "<span class=\"span6\"><input name=\"detail_nodelist_check\" disabled=\"disabled\" value=\"" + info.id + "\"";
                    }else{
                        temp = temp + "<span class=\"span9\"><input name=\"detail_nodelist_check\" disabled=\"disabled\" value=\"" + info.id + "\"";
                    }
                    if(info.checked == "1"){
                        temp = temp + "checked='checked' ";
                    }
                    temp = temp + "type=\"checkbox\">"+info.node_name+"</span>";
                });
                $("#detail_nodelist").empty().append(temp);
                //树结构展示
                if(info.menulist == null || info.menulist == ""){
                    $("#detail_jstree_div").hide();
                }else{
                    $('#detail_jstree').jstree({'plugins':["wholerow","checkbox"] ,
                        'checkbox' : {
                            "undetermined" : true,
                            "three_state" : true
                        } , "core" : {
                            "multiple" : true,
                            'data' : info.menulist
                        }
                    });
                    $("#detail_jstree").on("loaded.jstree", function (event, data) {
                        // 展开所有节点
                        $('#detail_jstree').jstree('open_all');
                        // 展开指定节点
                        // data.instance.open_node([1,10]);     // 单个节点 （1 是顶层的id）
                    });
                    $("#detail_jstree_div").show();
                }


                showLayer("detail_div", '查看角色信息');
                var height = "470px";
                $("#layui-layer"+layer_div).css("height",height);
                $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
            }else{
                layerResultAndReload(result);
            }
        });
    }

    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择角色!",{
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
                var data = {roleArray:checkboxArray};
                var url = "/view/class/system/rolemanage/delete?random=" + Math.random();
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
            area : [ '900px', '600px' ],
            content : $("#" + divid)
        });
    }

    function validName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_rolename").focus();
            layer.tips('角色名称不能为空!', '#' + type + '_rolename', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function destoryTree(type) {
        $("#" + type + "_jstree").jstree("destroy");
    }

    function hideDiv(type) {
        $("#" + type + "_div").hide();
        destoryTree(type);
    }
});