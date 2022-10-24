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
        resizewh.resizeBodyH($("#hostprocessmanage"));
        $("#query_div").hide();
        initChildrenMenu();
        butBindFunction();
        resetQueryDiv();

//----------------------------------以下为自定义方法-------------------------------------------------//
    //重置页面标签内容
    function reset(divid){
        $("#"+divid+" input[type='text']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" input[type='hidden']").each(function(){
            $(this).val('');
        });
        $("#"+divid+" select").each(function(){
            var temp = $(this).get(0).options[0];
            if(temp !=null){
                temp.selected=true;
            }
        });
    }

    function initShow(){
        var operatetype = $("#operatetype").val();
        var operateid = $("#operateid").val();
        if (operatetype=="1" || operatetype=="2"){//绑定-初始化指标
            $('.tags').last().addClass('tag_on').siblings().removeClass("tag_on");
            $("#query_div").hide();
            $("#bind_div").show();
            $("#bind_operatetype option").each(function(){
                if($(this).attr('value')==operatetype){
                    $(this).prop("selected", "true");
                }
            });
            $("#hostprocessrepdiv").empty();
            $("#checkboxAddAll").prop('checked',false);
            checkbox.cleanArray();
            queryOperateModuleChange();
            $("#bind_operateid option").each(function(){
                if($(this).attr('value')==operateid){
                    $(this).prop("selected", "true");
                }
            });
            //获取主机指标配置详细信息
            checkbox.cleanArray();
            queryHostProcessInfoChange();
        } else{
            //查询
            $('.tags').first().addClass('tag_on').siblings().removeClass("tag_on");
            //初始化查询条件
//            	initQueryCondition();
            $("#query_div").show();
            $("#bind_div").hide();

        }
    }

    function initChildrenMenu(){
    	var pageUrl=window.location.pathname;
        $("#operate_menu_1").empty();
        $("#operate_menu_1").append('<a href="#" id="queryDiv" class="tags border-left" onclick="">查询</a>');
        $("#queryDiv").click(function() {
            $("#query_div").show();
            $("#bind_div").hide();
            $(this).addClass('tag_on').siblings().removeClass("tag_on");
            loadOptRecord(1);
            //绑定页面重置
            resetBindDiv();
        });
        $("#operate_menu_2").empty();
        var url = "/view/class/querychildrenmdmenu";
        var data = {pageUrl:pageUrl};
        $.ajax({
            type: "post",
            url: url+"?pageUrl="+pageUrl+"&random=" + Math.random(),
            data: data,
            cache: false,
            async: true,
            dataType: "json",
            success: function (result) {
                if(result!=null && result.length >0 ){
                    for(var i=0;i<result.length;i++){
                        if(result[i].url=="bind"){
                            $("#operate_menu_1").append('<a href="#" id="'+result[i].name+'" class="tags border-right">'+result[i].show_name+'</a>');
                            //绑定绑定事件
                            $("#"+result[i].name).click(function() {
                                $("#query_div").hide();
                                $("#bind_div").show();
                                //查询页面重置
                                resetQueryDiv();
                                $(this).addClass('tag_on').siblings().removeClass("tag_on");
                            });
                        }else{
                            $("#operate_menu_2").append('<a href="#" id="'+result[i].name+'" class="button button-small button-primary" >'+result[i].show_name+'</a> ');
                            //解绑、下发、修改绑定事件
                            if(result[i].url=='edit'){
                                $("#"+result[i].name).click(function() {
                                    modifyHostProcessShow();
                                });
                            }else if(result[i].url=='delete'){
                                $("#"+result[i].name).click(function() {
                                    deleteShow();
                                });
                            }else if(result[i].url=='startex'){
                                $("#"+result[i].name).click(function() {
                                    excuteShow(1);
                                });
                            }else if(result[i].url=='stopex'){
                                $("#"+result[i].name).click(function() {
                                    excuteShow(2);
                                });
                            }else if(result[i].url=='autostartex'){
                                $("#"+result[i].name).click(function() {
                                	autostartexShow();
                                });
                            }
                        }
                    }
                }
                initShow();
            }
        });
    }

    function autostartexShow() {
    	reset("autostartex_div");
    	$("#autostartex_date").val('2020-06-06 06:00:00');
    	showLayerAutostartex("autostartex_div",'自动启动');
    }
    
    /**
     * 修改显示
     * @returns
     */
    function modifyHostProcessShow(){
        reset("modify_div");
        var arr = checkbox.getReturnArray();
        if(arr.length==0){
            layer.msg("请选择修改数据",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
            return;
        }
        if(arr.length>1){
            layer.msg("只能选择一条数据修改",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
            return;
        }
        var id = arr[0];
        var data = {id:id};

        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/hostprocessmanage/query/singleHostProcessInfo?random=" + Math.random(),
            data: data,
            cache: false,
            async: false,
            dataType: "json",
            success: function (result) {
                layer.close(layer_load);
                if(result.opSucc){
                    loadingwait();
                    if(result.data!=null && result.data!=""){
                        $("#modify_id").val(result.data.id);
                        $("#modify_host_id").val(result.data.host_ip);
                        $("#modify_process_id").val(result.data.process_id);
                        $("#modify_process_key").val(result.data.process_key);
                        $("#modify_start_script").val(result.data.start_script);
                        $("#modify_stop_script").val(result.data.stop_script);
                        $("#modify_description").val(result.data.description);

                        showLayer("modify_div",'修改主机进程关联关系');
                    }
                }else{
                    layer.msg(result.message,{
                        time:2000,
                        skin: 'layer_msg_color_alert'
                    });
                }
            }
        });

    }

    //修改确认
    function modifyInfo(){
        var id = $("#modify_id").val();
        var process_key = stringutil.Trim($("#modify_process_key").val());
        var start_script = stringutil.Trim($("#modify_start_script").val());
        var stop_script = stringutil.Trim($("#modify_stop_script").val());
        var description = stringutil.Trim($("#modify_description").val());

        //进程标志不能为空
        if(stringutil.checkString('modify_process_key',process_key,'进程标志不能为空!'))
            return;
        if(stringutil.checkString('modify_start_script',start_script,'启动脚本不能为空!'))
            return;
        if(stringutil.checkString('modify_stop_script',stop_script,'停止脚本不能为空!'))
            return;
        //校验采集脚本长度
        if(stringutil.checkString('modify_start_script',start_script,'启动脚本长度不能超过100位!',100))
            return;
        if(stringutil.checkString('modify_stop_script',stop_script,'停止脚本长度不能超过100位!',100))
            return;
        //描述长度
        if(stringutil.checkString('modify_description',description,'描述不能超过100位!',100))
            return;
        loadingwait();
        var data= {id:id,process_key:process_key,start_script:start_script,stop_script:stop_script,description:description};
        $.getJSON("/view/class/system/hostprocessmanage/modify?random=" + Math.random(),data, function(result) {
            layer.closeAll();
            checkbox.cleanArray();
            layerResultAndReload(result);
        });
    }

    //删除数据
    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个主机进程关联信息!",{
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
                var data = {hostProcessArray:checkboxArray};
                var url = "/view/class/system/hostprocessmanage/delete?random=" + Math.random();
                $.getJSON(url,data,function(result){
                    layerResultAndReload(result);
                })
            });
        }
    }

    //执行脚本
    function excuteShow(script_type) {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个主机进程关联信息!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            layer.confirm('是否确认执行该批次脚本？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] // 按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {hostProcessArray:checkboxArray,script_type:script_type};
                var url = "/view/class/system/hostprocessmanage/execute?random=" + Math.random();
                $.getJSON(url,data,function(result){
                    $("#idlist").val(result.data);
                    //清空checkbox
                    checkbox.cleanArray();
                    layerResultAndReload(result);
                    result.message = "脚本执行状态刷新成功!";
                    setTimeout(function(){resetIdlist(result);},10000);
                })
            });
        }
    }

    function resetIdlist(result){
        layerResultAndReload(result);
        $("#idlist").val("");
    }

    function layerResultAndReload(result){
        layer.close(layer_load);
        if(result.opSucc){
            var page_curr = $("#page_curr").val();
            loadOptRecord(page_curr);
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

    function butBindFunction(){
        //查询页面查询按钮事件
        $("#querybutton").click(function() {
            var hostid = $("#query_hostid").val();
            var metricid = $("#query_metricid").val();
            $("#query_metricid_page").val(metricid);
            $("#query_hostid_page").val(hostid);
            checkbox.cleanArray();
            loadOptRecord(1);
        });

        //查询页面重置按钮事件
        $("#resetbutton").click(function() {
            reset("query_div");
        });

        //详情ok键事件
        $("#detail_ok").click(function() {
            layer.closeAll();
        });

        //自动启动ok键事件
        $("#autostartex_ok").click(function() {
            layer.closeAll();
        });
        
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','hostprocessdiv');

        //绑定页面全选框事件
        checkbox.bindAllCheckbox('checkboxAddAll','hostprocessrepdiv');

        //绑定页面选择类型事件
        $("#bind_operatetype").bind("change",function(){
            $("#hostprocessrepdiv").empty();
            $("#checkboxAddAll").prop('checked',false);
            //获取操作目标信息
            checkbox.cleanArray();
            queryOperateModuleChange();
        });

        //绑定页面选择项事件
        $("#bind_operateid").bind("change",function(){
            $("#hostprocessrepdiv").empty();
            $("#checkboxAddAll").prop('checked',false);
            //获取主机指标配置详细信息
            checkbox.cleanArray();
            queryHostProcessInfoChange();
        });

        //绑定页面提交按钮事件
        $("#bindbutton").click(function() {
            //绑定数据提交
            bindDataSubmit();
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
    }

    /**
     *  采集：cycle+id
     脚本返回类型：returnType+id
     主机id：hostid+id
     主机ip: hostip+id
     指标id：metricid+id
     采集脚本：script+id
     脚本参数：script_param+id
     * @returns
     */
    function bindDataSubmit(){
        var arr = checkbox.getReturnArray();
        if(arr==null || arr.length==0){
            layer.msg("请选择绑定数据",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
            return;
        }
        //校验是否符合规范
        var id,start_script,host_id,process_id,process_key,stop_script,description;
        var _list = new Array();
        for(var i=0;i<arr.length;i++){
            id=arr[i];
            //校验采集脚本空
            start_script=stringutil.Trim($("#start_script"+id).val());
            stop_script=stringutil.Trim($("#stop_script"+id).val());
            if(stringutil.checkString('start_script'+id,start_script,'启动脚本不能为空!'))
                return;
            //校验采集脚本长度
            if(stringutil.checkString('start_script'+id,start_script,'启动脚本长度不能超过100位!',100))
                return;
            if(stringutil.checkString('stop_script'+id,stop_script,'停止脚本不能为空!'))
                return;
            //校验采集脚本长度
            if(stringutil.checkString('stop_script'+id,stop_script,'停止脚本长度不能超过100位!',100))
                return;
            //校验描述参数长度
            description=stringutil.Trim($("#description"+id).val());
            if(stringutil.checkString('description'+id,description,'描述长度不能超过100位!',100))
                return;
            host_id=$("#hostid"+id).val();
            process_key=stringutil.Trim($("#processkey"+id).val());
            process_id=$("#processid"+id).val();
            var hostProcess=new Object();
            hostProcess.host_id=host_id;
            hostProcess.process_key=process_key;
            hostProcess.process_id=process_id;
            hostProcess.stop_script=stop_script;
            hostProcess.start_script=start_script;
            hostProcess.description=description;
            _list.push(hostProcess);
        }
        var jsonData = JSON.stringify(_list);
        var operatetype='1';
        var data = {operatetype:operatetype};
        var url = "/view/class/system/hostprocessmanage/getbindOperate";
        var data = {jsonData:jsonData};
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
                $("#hostprocessrepdiv").empty();
                $("#checkboxAddAll").prop('checked',false);
                //获取主机指标配置详细信息
                checkbox.cleanArray();
                queryHostProcessInfoChange();
            }
        });
    }


    function queryOperateModuleChange(){
        var operatetype= $("#bind_operatetype").val();
        $("#bind_operateid").empty();
        $("#bind_operateid").append('<option value="">请选择</option>');
        if(operatetype==""||operatetype==""){
            return;
        }
        var url = "/view/class/system/hostprocessmanage/getSourceList";
        var data = {operatetype:operatetype};
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
                if(result!=null && result.length>1){
                    var operatetype = result[0];
                    var data = result[1];
                    if(operatetype=='1'){
                        for(var i=0;i<data.length;i++){
                            $("#bind_operateid").append('<option value="'+data[i].hostid+'">'+data[i].hostname+' '+data[i].addr+'</option>');
                        }
                    }else{
                        for(var i=0;i<data.length;i++){
                            $("#bind_operateid").append('<option value="'+data[i].process_id+'">'+data[i].process_name+'</option>');
                        }
                    }
                }
            }
        });
    }

    function queryHostProcessInfoChange(){
        var operatetype= $("#bind_operatetype").val();
        var operateid= $("#bind_operateid").val();
        if(operateid==null || operateid=="")
            return;
        //查询数据
        var url = "/view/class/system/hostprocessmanage/getHostProcessConfigInfo";
        var data = {operatetype:operatetype,operateid:operateid};
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
                var returnTypelist = result[0];
                var datalist = result[1];
                if(datalist!=null){
                    var rowdata = "";
                    var returnTypeSelect = "";
                    for(var i=0;i<datalist.length;i++){
                        var rowninfo = datalist[i];
                        var id = rowninfo.host_id+""+rowninfo.process_id;
                        //flag = -1 可编辑
                        var flag = rowninfo.flag;
                        var readonly="readonly=\"readonly\"";
                        var disabled="disabled=\"disabled\"";
                        var checked = "checked=\"checked\"";
                        if(flag=='-1'){
                            readonly="";
                            disabled = "";
                            checked = "";
                        }

                        // returnTypeSelect="<select "+disabled+" id=\"returnType"+id+"\">";
                        // for(var k=0;k<returnTypelist.length;k++){
                        //     if(rowninfo.type==returnTypelist[k].code){
                        //         returnTypeSelect = returnTypeSelect+"<option selected=\"selected\" value=\""+returnTypelist[k].code+"\" > "+returnTypelist[k].description+" </option>"
                        //     }else{
                        //         returnTypeSelect = returnTypeSelect+"<option value=\""+returnTypelist[k].code+"\" > "+returnTypelist[k].description+" </option>"
                        //     }
                        // }
                        returnTypeSelect = returnTypeSelect+"</select>";
                        rowdata = rowdata + "<tr><td>"+"<input "+checked+" "+disabled+" type=\"checkbox\" value=\""+id+"\" id=\""+id+"\" />"+"</td>"
                            +"<td>"+rowninfo.hostname+"<input type=\"hidden\" id=\"hostid"+id+"\" value=\""+rowninfo.host_id+"\" />"+"</td>"
                            +"<td>"+rowninfo.addr+"<input type=\"hidden\" id=\"hostip"+id+"\" value=\""+rowninfo.addr+"\" />"+"</td>"
                            +"<td>"+rowninfo.process_name+"<input type=\"hidden\" id=\"processid"+id+"\" value=\""+rowninfo.process_id+"\" />"+"</td>"
                            +"<td>"+rowninfo.process_key+"<input type=\"hidden\" id=\"processkey"+id+"\" value=\""+rowninfo.process_key+"\" />"+"</td>"
                            +"<td><input id=\"start_script"+id+"\" type=\"text\" value=\""+rowninfo.start_script+"\" maxlength=\"100\" size=\"100\" "+disabled+" /></td>"
                            +"<td><input id=\"stop_script"+id+"\" type=\"text\" value=\""+rowninfo.stop_script+"\" maxlength=\"100\" size=\"100\" "+disabled+" /></td>"
                            // +"<td>"+returnTypeSelect+"</td>"
                            +"<td><input id=\"description"+id+"\" type=\"text\" value=\""+rowninfo.description+"\" maxlength=\"100\" size=\"100\" "+disabled+" /></td>"
                            +"</tr>";
                    }
                    $("#hostprocessrepdiv").append(rowdata);
                    checkbox.bindSingleCheckbox('hostprocessrepdiv');
                    resizewh.resizeBodyH($("#hostprocessmanage"));
                }
            }
        });
    }

    function checkInfo(tips,type,id,value){
        if(value==null||value==""){
            layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
            return true;
        }
        return false;
    }

    function resetQueryDiv(){
        reset("query_div");
        var hostid = $("#query_hostid").val();
        var metricid = $("#query_metricid").val();
        $("#query_metricid_page").val(metricid);
        $("#query_hostid_page").val(hostid);
        checkbox.cleanArray();
        loadOptRecord(1);    }

    function resetBindDiv(){
        reset("bind_div");
        $("#bind_operateid").empty();
        $("#checkboxAddAll").prop("checked", false);
        $("#hostprocessrepdiv").empty();
        checkbox.cleanArray();
    }


    //加载查询内容
    function loadOptRecord(pageNumber){
        //分页显示的初始化数据
        var hostid = $("#query_hostid_page").val();
        var metricid = $("#query_metricid_page").val();
        $("#checkboxAll").prop("checked", false);
        var pagecount=0;
        var page_count = 10;
        var idlist =$("#idlist").val();
        loadingwait();
        var data = {'host_id':hostid,'process_id':metricid,'start_script': idlist,'pageNumber':pageNumber};
        $.getJSON("/view/class/system/hostprocessmanage/query?random=" + Math.random(),data, function(result) {
            layer.close(layer_load);
            resetPage(result);
        });
    }

    //重置分页(跳转分页)
    function resetPage(result) {
        var totalCount = result.totalCount;
        var pageList = result.pageList;
        var start = result.start;
        var end = result.end;
        var pageNumber = result.pageNumber;
        var totalPages = result.totalPages;
        $("#querynum").text(totalCount);
        laypage({
            cont: "pageinfo", //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: totalPages, //通过后台拿到的总页数
            curr: pageNumber, //当前页
            jump: function (obj, first) { //触发分页后的回调
                showTable(pageList,start,end);
                $("#currnum").text( start + "-" + end);
                if(totalCount==0){
                    $("#currnum").empty().text("0 ");
                }
                $("#page_curr").val(obj.curr);
                resizewh.resizeBodyH($("#hostprocessmanage"));
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    loadOptRecord(obj.curr);
                }
            }
        });
    }

    //拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        //计算选中条数
        var k = 0;
        for(var i=0;i<=endnum-startnum;i++){
            var rowninfo = data[i];
            var id = rowninfo.id;
            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"+"</td>"
                +"<td>"+rowninfo.hostname+"</td>"
                +"<td>"+rowninfo.host_ip+"</td>"
                +"<td>"+rowninfo.process_name+"</td>"
                +"<td title=\""+rowninfo.process_key+"\">"+rowninfo.process_key+"</td>"
                +"<td title=\""+rowninfo.start_script+"\">"+rowninfo.start_script+"</td>"
                +"<td title=\""+rowninfo.stop_script+"\">"+rowninfo.stop_script+"</td>"
                +"<td title=\""+rowninfo.description+"\">"+rowninfo.description+"</td>";
                 if(rowninfo.flag == 1){
                     rowdata+="<td>"+"<a class=\"J_edit pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\" >"+rowninfo.operate_state+"</a>"+"</td>";
                 }else {
                     rowdata+="<td>"+"<a class=\"J_edit pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\" >查看</a>"+"</td>";
                 }
            rowdata+="</tr>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
            //全选框选中
            $("#checkboxAll").prop("checked", true);
        }else{
            $("#checkboxAll").prop("checked", false);
        }
        $("#hostprocessdiv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
                detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("hostprocessdiv");
    }

    // 详情按钮事件
    function detailShow(id){
        reset("detail_div");
        var data = {id:id};
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/hostprocessmanage/query/processoperateinfo?random=" + Math.random(),
            data: data,
            dataType: "json",
            cache: false,
            async: false,
            success: function (result) {
                layer.close(layer_load);
                if(result!=null && result!=""){
                    $("#host_ip").val(result.host_ip);
                    $("#process_name").val(result.process_name);
                    $("#operate_state option").each(function(){
                        if($(this).attr('value')==result.operate_state){
                            $(this).prop("selected", "true");
                        }
                    });
                    $("#script_type option").each(function(){
                        if($(this).attr('value')==result.script_type){
                            $(this).prop("selected", "true");
                        }
                    });
                    $("#operate_result").val(result.operate_result);
                    $("#create_time").val(result.create_time);
                    $("#show_script").val(result.process_script);
                    showLayerDetail("detail_div",'操作详情');
                }else {
                    layer.msg("该主机未执行过此进程，无数据!",{
                        time:2000,
                        skin: 'layer_msg_color_alert'
                    });
                }
            },error: function () {
                layer.close(layer_load);
                layer.msg("该主机未执行过此进程，无数据!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }
        });
    }

    function showLayerDetail(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '580px', '320px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayerAutostartex(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '580px', '180px' ],
            content : $("#"+divid)
        });
    }

    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '580px', '320px' ],
            content : $("#"+divid)
        });
    }

    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
        });
    }

    function closewait(){
        layer_load = layer.load(1, {
            shade:0
        });
    }

});