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
        
        resizewh.resizeWH($("#mainnetdevice"));
        butBindFunction();
        
        reset("mainnetdevice");
        
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainnetdevice");
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
        $("#"+divid+" select").each(function(){
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
    }
    
    // 新增按钮事件
    function addShow() {
        reset("add_div");
        $.getJSON("/view/class/system/netdevicemanage/query/accesstypelist?random=" + Math.random(),function(result){
            $("#add_accesstype").empty();
            $.each(result,function(i,data){
                $("#add_accesstype").append("<option value=\""+data.code+"\">"+data.description+"</option>");
            });
            accesstypeChangeFunction("add");
        });
        $.getJSON("/view/class/system/netdevicemanage/query/nastypelist?random=" + Math.random(),function(result){
            $("#add_nastype").empty();
            $.each(result,function(i,data){
                $("#add_nastype").append("<option value=\""+data.nastype+"\">"+data.nastype+"</option>");
            });
        });
        $.getJSON("/view/class/system/netdevicemanage/query/arealist?random=" + Math.random(),function(result){
            $("#add_areano").empty();
            $.each(result,function(i,data){
                $("#add_areano").append("<option value=\""+data.areano+"\">"+data.name+"</option>");
            });
        });
        showLayer("add_div", '新增节点信息');
    }
    
    function accesstypeChangeFunction(type){
        $("#"+type+"_accesstype").change(function(){
            var accesstype = $("#"+type+"_accesstype").val();
            var data = {'accesstype':accesstype};
            $("#"+type+"_nastype").empty();
            $.getJSON("/view/class/system/netdevicemanage/query/nastypelist?random=" + Math.random(),data,function(result){
                $.each(result,function(i,data){
                    $("#"+type+"_nastype").append("<option value=\""+data.nastype+"\">"+data.nastype+"</option>");
                });
            });
        });
    }

    // 新增确认
    function addInfo() {
        var nasip = $("#add_nasip").val();
        var accesstype = $("#add_accesstype").val();
        var nastype = $("#add_nastype").val();
        var areano = $("#add_areano").val();
        if (validNasip('add', nasip)||validAccesstype('add', accesstype)||validNastype('add', nastype)||validAreano('add', areano)) {
            return;
        }
        var data = {nasip:nasip,accesstype:accesstype,nastype:nastype,areano:areano};
        loadingwait();
        $.getJSON("/view/class/system/netdevicemanage/add/?random=" + Math.random(), data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }

    // 修改按钮事件
    function modifyShow(nasid) {
        reset("modify_div");
        var data = { nasid : nasid};
        loadingwait();
        $.getJSON("/view/class/system/netdevicemanage/query/accesstypelist?random=" + Math.random(),function(result){
            $("#modify_accesstype").empty();
            $.each(result,function(i,data){
                $("#modify_accesstype").append("<option value=\""+data.code+"\">"+data.description+"</option>");
            });
            accesstypeChangeFunction("modify");
        });
        $.getJSON("/view/class/system/netdevicemanage/query/nastypelist?random=" + Math.random(),function(result){
            $("#modify_nastype").empty();
            $.each(result,function(i,data){
                $("#modify_nastype").append("<option value=\""+data.nastype+"\">"+data.nastype+"</option>");
            });
        });
        $.getJSON("/view/class/system/netdevicemanage/query/arealist?random=" + Math.random(),function(result){
            $("#modify_areano").empty();
            $.each(result,function(i,data){
                $("#modify_areano").append("<option value=\""+data.areano+"\">"+data.name+"</option>");
            });
        });
        $.getJSON("/view/class/system/netdevicemanage/query/singleinfo?random=" + Math.random(), data, function(result) {
            if (result.length > 0) {
                var info = result[0];
                $("#modify_nasid").val(nasid);
                $("#modify_nasip").val(info.nasip);
                showLayer("modify_div", '修改设备信息');
            }
        });
    }

    // 修改确认
    function modifyInfo() {
        var nasid = $("#modify_nasid").val();
        var nasip = $("#modify_nasip").val();
        var accesstype = $("#modify_accesstype").val();
        var nastype = $("#modify_nastype").val();
        var areano = $("#modify_areano").val();
        if (validNasip('modify', nasip)||validAccesstype('modify', accesstype)||validNastype('modify', nastype)||validAreano('modify', areano)) {
            return;
        }
        var data = {nasid:nasid,nasip : nasip,accesstype:accesstype,nastype:nastype,areano:areano};
        loadingwait();
        $.getJSON("/view/class/system/netdevicemanage/modify/?random=" + Math.random(), data, function(result) {
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
        var page_count = 8;

        var nasip = $("#nasip").val();
        var accesstype = $("#accesstype").val();
        var nastype = $("#nastype").val();
        var areano = $("#areano").val();
        var data = {'nasip' : nasip,'accesstype':accesstype,'nastype':nastype,'areano':areano};
        $.getJSON("/view/class/system/netdevicemanage/query/infolist?random=" + Math.random(), data, function(result) {
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
                    resizewh.resizeWH($("#mainnetdevice"));
                },
                groups : 5
            // 连续显示分页数
            });
        });
    }
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        for (var i = startnum; i <= endnum; i++) {
            var rowninfo = data[i - 1];
            rowdata = rowdata + "<tr><td>" + rowninfo.nasip + "</td><td>" + rowninfo.accesstypename
                +"</td><td>" + rowninfo.nastype+"</td><td>" + rowninfo.areaname
                    + "</td><td>" + "<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""
                    + rowninfo.nasid + "\" >修改</a>"
                    + "<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""
                    + rowninfo.nasid + "\">删除</a>" + "</td>";
        }
        $("#querydiv").empty().append(rowdata);
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

    function deleteShow(nasid) {
        var layershow = layer.confirm('是否确认删除该条数据？', {
            closeBtn : 0,
            title : '询问',
            btn : [ '确认', '取消' ]
        // 按钮
        }, function() {
            layer.closeAll();
            loadingwait();
            
            var data = { nasid : nasid };
            var url = "/view/class/system/netdevicemanage/delete/?random=" + Math.random();
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
            area : [ '500px', '295px' ],
            content : $("#" + divid)
        });
    }

    function validNasip(type, value) {
        if (null == value || value == "" ||!checkIp(value)) {
            $("#" + type + "_nasip").focus();
            layer.tips('设备IP为空或格式错误!', '#' + type + '_nasip', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function validAccesstype(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_accesstype").focus();
            layer.tips('设备类型不能为空!', '#' + type + '_accesstype', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function validNastype(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_nastype").focus();
            layer.tips('设备型号不能为空!', '#' + type + '_nastype', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function validAreano(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_areano").focus();
            layer.tips('属地不能为空!', '#' + type + '_areano', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function checkIp(ip) {
        var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        return regex.test(ip);
    }
});