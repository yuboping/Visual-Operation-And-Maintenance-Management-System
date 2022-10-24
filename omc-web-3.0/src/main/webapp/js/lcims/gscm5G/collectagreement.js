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
        var layer_load;

        resizewh.resizeBodyH($("#mainmetric"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','metricdiv');
        });
        reset("mainmetric");
        initChildrenMenu();
        // loadingwait();
        // loadOptRecord();

//----------------------------------以下为自定义方法-------------------------------------------------//
        function butBindFunction(){
            $("#querybutton").click(function() {
                queryOpt();
            });
            $("#resetbutton").click(function() {
                reset("mainmetric");
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
            checkbox.bindAllCheckbox('checkboxAll','metricdiv');

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
            $("#operate_menu").empty();
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
            $("#metricdiv :checkbox").click(function(){
                var flag = $(this).prop('checked');
                var value = $(this).val();
                checkbox.checkboxSingle(flag,value);
            });
        }

        //新增按钮事件
        function addShow(){
            reset("add_div");
            $.getJSON("/view/class/system/collectagreement/query/mdcollcyclelist?random=" + Math.random(), function(result) {
                $("#add_cycle_id").empty();
                $.each(result,function(i,data){
                    $("#add_cycle_id").append("<option value=\""+data.cycleid+"\">"+data.cyclename+"</option>");
                });
            });
            $.getJSON("/view/class/system/collectagreement/query/mdparamlist?type=7&random=" + Math.random(), function(result) {
                $("#add_script_return_type").empty();
                $.each(result,function(i,data){
                    $("#add_script_return_type").append("<option value=\""+data.code+"\">"+data.description+"</option>");
                });
            });
            $.getJSON("/view/class/system/collectagreement/query/mdmetrictypelist?random=" + Math.random(), function(result) {
                $("#add_protocol_type").empty();
                $.each(result,function(i,data){
                    $("#add_protocol_type").append("<option value=\""+data.id+"\">"+data.metric_type_name+"</option>");
                });
            });
            $("#add_server_type").empty();
            $("#add_server_type").append("<option value=\"\">请选择</option>");
            $.getJSON("/view/class/system/collectagreement/query/servertypelist?random=" + Math.random(), function(result) {
                $.each(result,function(i,data){
                    $("#add_server_type").append("<option value=\""+data.code+"\">"+data.description+"</option>");
                });
            });

            showLayer("add_div",'新增插件');
        }

        //修改按钮事件
        function modifyShow(){
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length != 1){
                layer.msg("修改请只选择一个插件!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                var protocolid = checkboxArray[0];
                reset("modify_div");

                loadingwait();
                var data = {id : protocolid};
                $.ajax({
                    type: "post",
                    url: "/view/class/system/collectagreement/query?random=" + Math.random(),
                    data: data,
                    cache: false,
                    async: false,
                    dataType: "json",
                    success: function (result) {
                        if(result.length>0){
                            var protocol = result[0];
                            $("#modify_protocol_identity").val(protocol.protocol_identity);
                            $("#modify_protocol_name").val(protocol.protocol_name);
                            $("#modify_script").val(protocol.script);
                            $("#modify_script_param").val(protocol.script_param);
                            $("#modify_description").val(protocol.description);
                            $.getJSON("/view/class/system/collectagreement/query/mdcollcyclelist?random=" + Math.random(), function(result) {
                                $("#modify_cycle_id").empty();
                                $.each(result,function(i,data){
                                    $("#modify_cycle_id").append("<option value=\""+data.cycleid+"\">"+data.cyclename+"</option>");
                                });
                                $("#modify_cycle_id").val(protocol.cycle_id);
                            });
                            $.getJSON("/view/class/system/collectagreement/query/mdparamlist?type=7&random=" + Math.random(), function(result) {
                                $("#modify_script_return_type").empty();
                                $.each(result,function(i,data){
                                    $("#modify_script_return_type").append("<option value=\""+data.code+"\">"+data.description+"</option>");
                                });
                                $("#modify_script_return_type").val(protocol.script_return_type);
                            });


                            $.getJSON("/view/class/system/collectagreement/query/mdmetrictypelist?random=" + Math.random(), function(result) {
                                $("#modify_protocol_type").empty();
                                $.each(result,function(i,data){
                                    $("#modify_protocol_type").append("<option value=\""+data.id+"\">"+data.metric_type_name+"</option>");
                                });
                                $("#modify_protocol_type").val(protocol.protocol_type);
                            });
                            $("#modify_server_type").empty();
                            $("#modify_server_type").append("<option value=\"\">请选择</option>");
                            $.getJSON("/view/class/system/collectagreement/query/servertypelist?random=" + Math.random(), function(result) {
                                var server=""
                                $.each(result,function(i,data){
                                    if(data.description==protocol.server_type){
                                        server=data.code
                                    }
                                    $("#modify_server_type").append("<option value=\""+data.code+"\">"+data.description+"</option>");
                                });
                                $("#modify_server_type").val(server);
                            });

                            showLayer("modify_div",'修改插件');
                        }
                    }
                });
            }
        }


        //详情按钮事件
        function detailShow(protocolid){
            reset("detail_div");
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/collectagreement/query?id="+protocolid+"&random=" + Math.random(),
                cache: false,
                async: false,
                success: function (result) {
                    if(result.length>0){
                        var protocol = result[0];
                        $("#detail_protocol_identity").val(protocol.protocol_identity);
                        $("#detail_protocol_name").val(protocol.protocol_name);
                        $("#detail_cycle_id").val(protocol.cyclename);
                        $("#detail_script").val(protocol.script);
                        $("#detail_script_param").val(protocol.script_param);
                        $("#detail_script_return_type").val(protocol.metric_type_name);
                        $("#detail_protocol_type").val(protocol.mdparamdescription);
                        $("#detail_description").val(protocol.description);
                        $("#detail_server_type").val(protocol.server_type);
                        showLayerDetail("detail_div",'插件详情');
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

        //修改确认
        function modifyInfo(){
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length != 1){
                layer.msg("修改请只选择一个插件!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                var protocolid = checkboxArray[0];
                var protocol_identity = stringutil.Trim($("#modify_protocol_identity").val());
                var protocol_name = stringutil.Trim($("#modify_protocol_name").val());
                var cycle_id = $("#modify_cycle_id").val();
                var script = stringutil.Trim($("#modify_script").val());
                var script_param = stringutil.Trim($("#modify_script_param").val());
                var script_return_type = $("#modify_script_return_type").val();
                var protocol_type = $("#modify_protocol_type").val();
                var server_type = $("#modify_server_type").val();
                var description = stringutil.Trim($("#modify_description").val());
                if(stringutil.checkString("modify_protocol_identity",protocol_identity,"插件标识不能为空!")||
                    stringutil.checkString('modify_protocol_identity',protocol_identity,"插件标识不能超过30位!",30) ||
                    stringutil.checkString('modify_protocol_name',protocol_name,"插件名称不能为空!") ||
                    stringutil.checkString('modify_protocol_name',protocol_name,"插件名称不能超过50位!",50) ||
                    stringutil.checkString('modify_cycle_id',cycle_id,"采集周期不能为空!") ||
                    stringutil.checkString('modify_script',script,"采集脚本不能为空!") ||
                    stringutil.checkString('modify_script',script,"采集脚本不能超过100位!",100) ||
                    stringutil.checkString('modify_script_param',script_param,"脚本参数不能超过256位!",256) ||
                    stringutil.checkString('modify_script_return_type',script_return_type,"脚本数据返回格式类型不能为空!") ||
                    stringutil.checkString('modify_protocol_type',protocol_type,"插件类型不能为空!")||
                    stringutil.checkString('modify_description',description,"插件描述不能超过100位!",100)){
                    return;
                }
                var state = true;
                //校验插件标识是否重复事件
                $.ajax({
                    type: "post",
                    url: "/view/class/system/collectagreement/query?random=" + Math.random(),
                    cache: false,
                    async: false,
                    data: {protocol_identity:protocol_identity},
                    success: function (result) {
                        var length = result.length;
                        for (var i=0;i<length;i++){
                            if(result[i].protocol_identity==protocol_identity){
                                if(result[i].id != protocolid){
                                    state = false;
                                    $("#modify_protocol_identity").focus();
                                    layer.tips("插件标识不能重复", '#modify_protocol_identity',{ tips: [2, '#EE1A23'] });
                                    return;
                                }
                            }
                        }
                    }
                });
                if(state){
                    loadingwait();
                    var data= {id:protocolid,protocol_identity:protocol_identity,protocol_name:protocol_name,cycle_id:cycle_id,script:script,script_param:script_param,script_return_type:script_return_type,protocol_type:protocol_type,description:description,server_type:server_type};
                    $.getJSON("/view/class/system/collectagreement/modify?random=" + Math.random(),data, function(result) {
                        layer.closeAll();
                        layerResultAndReload(result);
                    });
                }
            }
        }

        //新增确认
        function addInfo(){
            var protocol_identity = stringutil.Trim($("#add_protocol_identity").val());
            var protocol_name = stringutil.Trim($("#add_protocol_name").val());
            var cycle_id = $("#add_cycle_id").val();
            var script = stringutil.Trim($("#add_script").val());
            var script_param = stringutil.Trim($("#add_script_param").val());
            var script_return_type = $("#add_script_return_type").val();
            var protocol_type = $("#add_protocol_type").val();
            var server_type = $("#add_server_type").val();
            if(server_type == null ){
                server_type = 'null';
            }
            var description = stringutil.Trim($("#add_description").val());
            if(stringutil.checkString("add_protocol_identity",protocol_identity,"插件标识不能为空!")||
                stringutil.checkString('add_protocol_identity',protocol_identity,"插件标识不能超过30位!",30) ||
                stringutil.checkString('add_protocol_name',protocol_name,"插件名称不能为空!") ||
                stringutil.checkString('add_protocol_name',protocol_name,"插件名称不能超过50位!",50) ||
                stringutil.checkString('add_cycle_id',cycle_id,"采集周期不能为空!") ||
                stringutil.checkString('add_script',script,"采集脚本不能为空!") ||
                stringutil.checkString('add_script',script,"采集脚本不能超过100位!",100) ||
                stringutil.checkString('add_script_param',script_param,"脚本参数不能超过256位!",256) ||
                stringutil.checkString('add_script_return_type',script_return_type,"脚本数据返回格式类型不能为空!") ||
                stringutil.checkString('add_protocol_type',protocol_type,"插件类型不能为空!")||
                stringutil.checkString('add_description',description,"插件描述不能超过100位!",100)){
                return;
            }
            //校验插件标识是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/collectagreement/query?random=" + Math.random(),
                cache: false,
                async: false,
                data: {"protocol_identity": protocol_identity},
                success: function (result) {
                    var length = result.length;
                    if(length > 0){
                        $("#add_protocol_identity").focus();
                        layer.tips("插件标识不能重复", '#add_protocol_identity',{ tips: [2, '#EE1A23'] });
                        return;
                    }else{
                        loadingwait();
                        var data= {protocol_identity:protocol_identity,protocol_name:protocol_name,cycle_id:cycle_id,script:script,script_param:script_param,script_return_type:script_return_type,protocol_type:protocol_type,description:description,server_type:server_type};
                        $.getJSON("/view/class/system/collectagreement/add?random=" + Math.random(),data, function(result) {
                            layer.closeAll();
                            addLayerResultAndReload(result);
                        });
                    }
                }
            });
        }

        // function addJumpShow(operateid) {
        //     layer.confirm('是否需要跳转到主机插件配置管理页面？', {
        //         closeBtn:0,
        //         title: '询问',
        //         btn: ['确认','取消'] //按钮
        //     },function(){
        //         window.location.href = "/view/class/system/hostmetricmanage?key=hostmetricmanage&operatetype=1&operateid="+operateid+"&random=" + Math.random();
        //     });
        // }

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

            var protocol_name = stringutil.Trim($("#protocol_name").val());
            var protocol_type = stringutil.Trim($("#protocol_type").val());
            var data = {'protocol_name':protocol_name,'protocol_type':protocol_type};
            $.getJSON("/view/class/system/collectagreement/query?random=" + Math.random(),data, function(result) {
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
                        resizewh.resizeBodyH($("#mainmetric"));
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
            var provincename=$("#provincename").val()
            for(var i=startnum;i<=endnum;i++){
                var rowninfo = data[i-1];
                var id = rowninfo.id;
                var checked = "";

                if(checkbox.isExitArray(id)){
                    checked = "checked=\"checked\"";
                    k++;
                }
                rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"+"</td>"
                    + "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.protocol_identity)+"\">"+stringutil.isNull(rowninfo.protocol_identity)+"</td><td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.protocol_name)+"\">"
                    +stringutil.isNull(rowninfo.protocol_name)+"</td><td>"+stringutil.isNull(rowninfo.cyclename)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.script)+"\">"
                    +stringutil.isNull(rowninfo.script)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.script_param)+"\">"+stringutil.isNull(rowninfo.script_param)+"</td><td>"
                    +stringutil.isNull(rowninfo.metric_type_name)+"</td><td>"+stringutil.isNull(rowninfo.mdparamdescription)+"</td>";
                if(provincename=="jscm"){
                    rowdata+="<td class='over_ellipsis' style='max-width:120px'>"+stringutil.isNull(rowninfo.server_type)+"</td>"
                }
                rowdata+="<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.description)+"\">"
                    +stringutil.isNull(rowninfo.description)+"</td><td>"
                    +"<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\">详情</a>"
                    +"</td>";
            }
            //本页条数
            var ct = endnum-startnum+1;
            if(k==ct&&k>0){
                //全选框选中
                $("#checkboxAll").prop("checked", true);
            }else{
                $("#checkboxAll").prop("checked", false);
            }
            $("#metricdiv").empty().append(rowdata);
            $("[name=detail]").each(function(){
                $(this).on('click',function(){
                    detailShow($(this).attr('id'));
                });
            });
            checkbox.bindSingleCheckbox("metricdiv");
        }



        function deleteShow() {
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length == 0){
                layer.msg("请选择一个插件!",{
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
                    var data = {protocolidArray:checkboxArray};
                    var url = "/view/class/system/collectagreement/delete/?random=" + Math.random();
                    $.getJSON(url,data,function(result){
                        layer.close(layer_load);
                        loadOptRecord();
                        layer.alert(result.message);
                    })
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
                addJumpShow(result.data);
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
                area : [ '620px', '360px' ],
                content : $("#"+divid)
            });
        }

        function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '650px', '630px' ],
                content : $("#"+divid)
            });
        }

        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
        }
    });