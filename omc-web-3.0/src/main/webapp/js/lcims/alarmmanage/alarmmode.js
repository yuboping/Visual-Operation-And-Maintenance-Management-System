require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage"
    }
});

require(['jquery','layer','laypage','resizewh'],
    function($,layer,laypage,resizewh) {
        var layer_load;
        
        resizewh.resizeWH($("#mainalarmmode"));
        butBindFunction();
        
        reset("mainalarmmode");
        loadingwait();
        loadOptRecord();
        
//----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainalarmmode");
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
    
    //新增按钮事件
    function addShow(){
        reset("add_div");
        $("#add_modeattr").val('');
        showLayer("add_div",'新增告警方式信息');
    }
    
    //修改按钮事件
    function modifyShow(modeid){
        reset("modify_div");
        var data = {modeid : modeid};
        $.getJSON("/view/class/system/alarmwaymanage/queryAlarmModeInfo?random=" + Math.random(),data, function(result) {
            var host = result[0];
            $("#modify_modeid").val(result.modeid);
            $("#modify_modename").val(result.modename);
            $("#modify_modetype").val(result.modetype);
            $("#modify_modeattr").val(result.modeattr);
            showLayer("modify_div",'修改告警方式信息');
        });
    }
    
    function checkIsNull(tips,id,value){
    	if(value==null||value==""){
    		layer.tips(tips, '#'+id,{ tips: [2, '#EE1A23'] });
    		return true;
    	}
    	return false;
    }
    
    function checkValueLength(tips,id,value,length){
    	if(value.length>length){
    		layer.tips(tips, '#'+id,{ tips: [2, '#EE1A23'] });
    		return true;
    	}
    	return false;
    }
    
    //修改确认
    function modifyInfo(){
    	var modeid = $("#modify_modeid").val();
    	var modename = $("#modify_modename").val();
        var modetype = $("#modify_modetype").val();
        var modeattr = $("#modify_modeattr").val();
        if(checkIsNull('告警方式名称不能为空','modify_modename',modename))
    		return;
        else{
        	if(checkValueLength('告警方式名称超出长度限制,限制长度30','modify_modename',modename,30))
        		return;
        }
        if(checkIsNull('告警方式属性不能为空','modify_modeattr',modeattr))
    		return;
        else{
        	if(checkValueLength('告警方式属性超出长度限制,限制长度250','modify_modeattr',modeattr,250))
        		return;
        }
        var data = {modeid:modeid,modename:modename,modetype:modetype,modeattr:modeattr};
        loadingwait();
        $.getJSON("/view/class/system/alarmwaymanage/modify/?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
    }
    
    //新增确认
    function addInfo(){
        var modename = $("#add_modename").val();
        var modetype = $("#add_modetype").val();
        var modeattr = $("#add_modeattr").val();
        if(checkIsNull('告警方式名称不能为空','add_modename',modename))
    		return;
        else{
        	if(checkValueLength('告警方式名称超出长度限制,限制长度30','add_modename',modename,30))
        		return;
        }
        if(checkIsNull('告警方式属性不能为空','add_modeattr',modeattr))
    		return;
        else{
        	if(checkValueLength('告警方式属性超出长度限制,限制长度250','add_modeattr',modeattr,250))
        		return;
        }
        var data = {modename:modename,modetype:modetype,modeattr:modeattr};
        loadingwait();
        $.getJSON("/view/class/system/alarmwaymanage/add/?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            layerResultAndReload(result);
        });
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
        var page_count = 8;
        
        var modename = $("#modename").val();
        var modetype = $("#modetype").val();
        var data = {'modename':modename,'modetype':modetype};
        $.getJSON("/view/class/system/alarmwaymanage/query?random=" + Math.random(),data, function(result) {
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
                    resizewh.resizeWH($("#mainalarmmode"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            rowdata = rowdata + "<tr><td>"+rowninfo.modename+"</td><td>"
            +rowninfo.modetypename+"</td><td>"+rowninfo.modeattr+"</td><td>"
            +"<a class=\"J_edit pr10\" href=\"#\" name='modify' id=\""+rowninfo.modeid+"\" >修改</a>"
            +"<a class=\"J_delete pr10\" href=\"#\" name='delete' id=\""+rowninfo.modeid+"\">删除</a>"
            +"</td>";
        }
        $("#modediv").empty().append(rowdata);
        $("[name=modify]").each(function(){
            $(this).on('click',function(){
                modifyShow($(this).attr('id'));
            });
        });
        $("[name=delete]").each(function(){
            $(this).on('click',function(){
                deleteShow($(this).attr('id'));
            });
        });
    }
    
    function deleteShow(modeid) {
        layer.confirm('是否确认删除该条数据？', {
            closeBtn:0,
            title: '询问',
            btn: ['确认','取消'] //按钮
        },function(){
            layer.closeAll();
            loadingwait();
            var data = {modeid:modeid};
            var url = "/view/class/system/alarmwaymanage/delete/?random=" + Math.random();
            $.getJSON(url,data,function(result){
                layerResultAndReload(result);
            })
        });
    }
    
    function layerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
            loadOptRecord();
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
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '500px', '345px' ],
            content : $("#"+divid)
        });
    }
    
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
    
});