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
        var layer_load,layer_div;
        
        resizewh.resizeWH($("#mainadmin"));
        butBindFunction();
        
        reset("mainadmin");
        loadingwait();
        loadOptRecord();
// ----------------------------------以下为自定义方法-------------------------------------------------//
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
        $("#add_areanolist_div").hide();
        $("#add_nodelist_div").hide();
        $("#add_modulelist_div").hide();
        $.getJSON("/view/class/system/adminmanage/query/arealist?random="+Math.random(),function(result){
        	var temp = "";
            $.each(result,function(i,info){
            	if(info.areano != null && info.areano != ""){
            		temp = temp + "<span class=\"span2\"><input name=\"add_areanolist_check\" value=\""+info.areano
                    +"\" type=\"checkbox\">"+info.name+"</span>";
            	}
            });
            $("#add_areanolist").empty().append(temp);
        });
        $.getJSON("/view/class/system/adminmanage/query/nodelist?random="+Math.random(),function(result){
            var temp = "";
            $.each(result,function(i,info){
                temp = temp + "<span class=\"span3\"><input name=\"add_nodelist_check\" value=\""+info.nodeid
                +"\" type=\"checkbox\">"+info.name+"</span>";
            });
            $("#add_nodelist").empty().append(temp);
        });
        $.getJSON("/view/class/system/adminmanage/query/area?random="+Math.random(),function(result){
            $.each(result,function(i,data){
                $("#add_areaid").append("<option value=\""+data.areano+"\">"+data.name+"</option>");
            });
        });
        $.getJSON("/view/class/system/adminmanage/query/rolelist?random="+Math.random(),function(result){
            $("#add_roleid").empty().append("<option value=\"\">请选择</option>");
            $.each(result,function(i,data){
                $("#add_roleid").append("<option menulist=\""+ data.menulist +"\" value=\""+data.roleid+"\">"+data.name+"</option>");
            });
            unbindRoleChangeFunction("add");
            bindRoleChangeFunction("add");
        });
        showLayer("add_div", '新增管理员信息');
        $("#layui-layer"+layer_div).css("height","300px");
        $("#layui-layer"+layer_div+" .layui-layer-content").css("height","250px");
    }
    
    function unbindRoleChangeFunction(type){
    	$("#"+type+"_roleid").unbind();
    }
    
    function bindRoleChangeFunction(type){
        $("#"+type+"_roleid").change(function(){
            var roleid=$("#"+type+"_roleid").val();
            var menulist = $("#"+type+"_roleid").find("option:selected").attr("menulist");
            if(roleid==0){//超级管理员
                $("input[name='"+type+"_areanolist_check']:checked").each(function(){
                    $(this).attr('checked',false);
                });
                $("input[name='"+type+"_nodelist_check']:checked").each(function(){
                    $(this).attr('checked',false);
                });
                $("input[name='"+type+"_modulelist_check']:checked").each(function(){
                    $(this).attr('checked',false);
                });
                $("#"+type+"_areanolist_div").hide();
                $("#"+type+"_nodelist_div").hide();
                $("#"+type+"_modulelist_div").hide();
                var height = "300px";
                $("#layui-layer"+layer_div).css("height",height);
                $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
            }else{
        	  mkRoleModuleList(roleid,type);
              var height = "470px";
              $("#layui-layer"+layer_div).css("height",height);
              $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
              mkNodeList(menulist,type);
              mkAreaList(menulist,type);
          }
        });
    }
    
    function mkNodeList(menulist,type){
    	if(menulist.indexOf("service")> -1 ){
      	  var content = $("#"+type+"_nodelist").html();
      	  if(content == null || content.length == 0){
      		  $("#"+type+"_nodelist_div").hide();
      	  }else{
      		  $("input[name='"+type+"_nodelist_check']:checked").each(function(){
                    $(this).attr('checked',false);
                });
      		  $("#"+type+"_nodelist_div").show();
      	  }
  	  }else{
  		  $("#"+type+"_nodelist_div").hide();
  	  }
    }
    
    function mkAreaList(menulist,type){
    	if(menulist.indexOf("city")> -1 || menulist.indexOf("netdevice")> -1 ){
  		  var content = $("#"+type+"_areanolist").html();
  		  if(content == null || content.length == 0){
      		  $("#"+type+"_areanolist").hide();
      	  }else{
      		  $("input[name='"+type+"_areanolist_check']:checked").each(function(){
                    $(this).attr('checked',false);
                });
      		  $("#"+type+"_areanolist_div").show();
      	  }
  	  }else{
  		  $("#"+type+"_areanolist_div").hide();
  	  }
    }
    
    function mkRoleModuleList(roleid,type){
        var data = {roleid:roleid};
        $("#"+type+"_modulelist").empty();
        $.getJSON("/view/class/system/adminmanage/query/moduleList?random="+Math.random(),data,function(result){
        	var temp = "";
            $.each(result,function(i,info){
                temp = temp + "<span class=\"span5\"><input name=\""+type+"_modulelist_check\" value=\""+info.key
                +"\" type=\"checkbox\">"+info.value+"</span>";
            });
            $("#"+type+"_modulelist").append(temp);
        });
        $("#"+type+"_modulelist_div").show();
    }
    // 新增确认
    function addInfo() {
        var admin = $("#add_admin").val();
        var pwd = $("#add_pwd").val();
        var pwd_s = $("#add_pwd_s").val();
        var roleid = $("#add_roleid").val();
        var name = $("#add_name").val();
        var areano = $("#add_areaid").val();
        var contactphone = $("#add_contactphone").val();
        var areanolist="",modulelist="",nodelist="";
        $("input[name='add_areanolist_check']:checked").each(function(){
             areanolist=areanolist+","+$(this).val();
        });
        $("input[name='add_nodelist_check']:checked").each(function(){
            nodelist=nodelist+","+$(this).val();
        });
        $("input[name='add_modulelist_check']:checked").each(function(i){
            modulelist=modulelist+","+$(this).val();
        });
        areanolist=areanolist+",";
        modulelist=modulelist+",";
        nodelist=nodelist+",";
        if (validAdmin('add', admin)||validPwd('add',pwd,pwd_s)||validRoleid('add',roleid)) {
            return;
        }
        var data = {admin:admin,pwd:pwd,pwd_s:pwd_s,roleid:roleid,name:name,contactphone:contactphone,areanolist:areanolist,modulelist:modulelist,nodelist:nodelist,areano:areano};
        loadingwait();
        $.getJSON("/view/class/system/adminmanage/add/?random=" + Math.random(), data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }

    // 修改按钮事件
    function modifyShow(admin) {
        reset("modify_div");
        var data = { admin : admin};
        $.getJSON("/view/class/system/adminmanage/query/checkRole?random=" + Math.random(), data, function(result) {
        	var msg = result[0];
        	if(msg.opSucc) {
        		loadingwait();
                $.getJSON("/view/class/system/adminmanage/query/singleinfo?random=" + Math.random(), data, function(result) {
                	if (result.length > 0) {
                        var info = result[0];
                        $("#modify_admin").val(info.admin);
                        $("#modify_contactphone").val(info.contactphone);
                        $("#modify_nodelist_div").hide();
                        $("#modify_areanolist_div").hide();
                        $("#modify_modulelist_div").hide();
                        //属地
                        $.each(info.arealistforcheck,function(i,info){
                            var temp = "<option value=\""+info.key + "\"";
                            if(Boolean(info.checkflag)){
                                temp = temp + " selected = \"selected\"";
                            }
                            temp = temp + ">"+info.value+"</option>";
                            $("#modify_areaid").append(temp);
                        });
                        //角色
                        $("#modify_roleid").empty().append("<option value=\"\">请选择</option>");
                        $.each(info.rolelistforcheck,function(i,info){
                            var temp = "<option value=\""+info.key + "\"";
                            if(Boolean(info.checkflag)){
                                temp = temp + " selected = \"selected\"";
                            }
                            temp = temp + ">"+info.value+"</option>";
                            $("#modify_roleid").append(temp);
                        });
                        unbindRoleChangeFunction("modify");
                        bindRoleChangeFunction("modify");
                        //可管理城市
                        var temp = "";
                        $.each(info.areanolistforcheck,function(i,info){
                        	if(info != null){
                        		temp = temp + "<span class=\"span2\"><input name=\"modify_areanolist_check\" value=\""+info.key+"\"";
                                if(Boolean(info.checkflag)){
                                    temp = temp + "checked='checked' ";
                                }
                                temp = temp + "type=\"checkbox\">"+info.value+"</span>";
                        	}
                        });
                        $("#modify_areanolist").empty().append(temp);
                        //可管理模块
                        temp = "";
                        $.each(info.modulelistforcheck,function(i,info){
                            temp = temp + "<span class=\"span5\"><input name=\"modify_modulelist_check\" value=\""+info.key+"\"";
                            if(Boolean(info.checkflag)){
                                temp = temp + "checked='checked' ";
                            }
                            temp = temp + "type=\"checkbox\">"+info.value+"</span>";
                        });
                        $("#modify_modulelist").empty().append(temp);
                        
                        //可管理节点
                        temp = "";
                        $.each(info.nodelistforcheck,function(i,info){
                            temp = temp + "<span class=\"span2\"><input name=\"modify_nodelist_check\" value=\""+info.key+"\"";
                            if(Boolean(info.checkflag)){
                                temp = temp + "checked='checked' ";
                            }
                            temp = temp + "type=\"checkbox\">"+info.value+"</span>";
                        });
                        $("#modify_nodelist").empty().append(temp);
                        
                        showLayer("modify_div", '修改管理员信息');
                        if(info.roleid==0){
                            var height = "300px";
                            $("#layui-layer"+layer_div).css("height",height);
                            $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
                        }else{
                        	  $("#modify_modulelist_div").show();
                              $("#modify_nodelist_div").show();
                              $("#modify_areanolist_div").show();
                              modifyContentList("nodelist","modify");
                              modifyContentList("areanolist","modify");
                              var height = "470px";
                              $("#layui-layer"+layer_div).css("height",height);
                              $("#layui-layer"+layer_div+" .layui-layer-content").css("height",height);
                          }
                    }
                });
        	}else {
            	layerResultAndReload(msg);
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
        var admin = $("#modify_admin").val();
        var pwd = $("#modify_pwd").val();
        var pwd_s = $("#modify_pwd_s").val();
        var areano = $("#modify_areaid").val();
        var roleid = $("#modify_roleid").val();
        var name = $("#modify_name").val();
        var contactphone = $("#modify_contactphone").val();
        var areanolist="",nodelist="",modulelist="";
        $("input[name='modify_areanolist_check']:checked").each(function(){
            areanolist=areanolist+","+$(this).val();
        });
        $("input[name='modify_nodelist_check']:checked").each(function(){
            nodelist=nodelist+","+$(this).val();
        });
        $("input[name='modify_modulelist_check']:checked").each(function(){
             modulelist=modulelist+","+$(this).val();
        });
        areanolist=areanolist+",";
        modulelist=modulelist+",";
        nodelist=nodelist+",";
        if (validAdmin('modify', admin)||validPwd('modify',pwd,pwd_s)||validRoleid('modify',roleid)) {
            return;
        }
        var data = {admin:admin,pwd:pwd,pwd_s:pwd_s,roleid:roleid,areano:areano,name:name,contactphone:contactphone,areanolist:areanolist,modulelist:modulelist,nodelist:nodelist};
        loadingwait();
        $.getJSON("/view/class/system/adminmanage/modify/?random=" + Math.random(), data, function(result) {
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
        var admin = $("#admin").val();
        var roleid = $("#roleid").val();
        var areano = $("#areano").val();
        var nodeid = $("#nodeid").val();
        var data = {'admin':admin,'roleid':roleid,'areano':areano,'nodeid':nodeid};
        $.getJSON("/view/class/system/adminmanage/query/infolist?random=" + Math.random(), data, function(result) {
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
                    resizewh.resizeWH($("#mainadmin"));
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
            rowdata = rowdata + "<tr><td>" + rowninfo.admin + "</td><td>" + rowninfo.rolename + "</td><td>" 
                + rowninfo.areanamelist + "</td><td>" + rowninfo.nodenamelist + "</td><td>" + rowninfo.name 
                + "</td><td>" + rowninfo.contactphone + "</td><td>" + "<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\"" 
                + rowninfo.admin + "\" >修改</a>" + "<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""
                + rowninfo.admin + "\">删除</a>" + "</td>";
        }
        $("#queryinfodiv").empty().append(rowdata);
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

    function deleteShow(admin) {
        var layershow = layer.confirm('是否确认删除该条数据？', {
            closeBtn : 0,
            title : '询问',
            btn : [ '确认', '取消' ]
        // 按钮
        }, function() {
            layer.closeAll();
            loadingwait();
            var data = { admin : admin };
            var url = "/view/class/system/adminmanage/delete/?random=" + Math.random();
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
        layer_div = layer.open({
            type : 1,
            title : title,
            closeBtn : 0,
            area : [ '900px', '460px'],
            content : $("#" + divid)
        });
    }

    function validAdmin(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_admin").focus();
            layer.tips('管理员不能为空!', '#' + type + '_admin', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    function validRoleid(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_roleid").focus();
            layer.tips('角色不能为空!', '#' + type + '_roleid', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    function validPwd(type, pwd,pwd_s) {
        if (null == pwd || pwd == "") {
            return false;
        }
        if (pwd.length<4) {
            $("#" + type + "_pwd").focus();
            layer.tips('密码长度不够!', '#' + type + '_pwd', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        if (null == pwd_s || pwd_s == "") {
            $("#" + type + "_pwd_s").focus();
            layer.tips('确认密码不能为空!', '#' + type + '_pwd_s', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        if(pwd!=pwd_s){
            $("#" + type + "_pwd").focus();
            layer.tips('密码和确认密码不一致!', '#' + type + '_pwd', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
    
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainadmin");
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
});