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

        resizewh.resizeBodyH($("#mainhost"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','hostdiv');
        });
        reset("mainhost");
        initChildrenMenu();
        // loadingwait();
        // loadOptRecord();

//----------------------------------以下为自定义方法-------------------------------------------------//
        function butBindFunction(){
            $("#querybutton").click(function() {
                queryOpt();
            });
            $("#resetbutton").click(function() {
                reset("mainhost");
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
            checkbox.bindAllCheckbox('checkboxAll','hostdiv');
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

        //详情按钮事件
        function detailShow(hostid){
            reset("detail_div");
            var data = {hostid : hostid};
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/collectormanage/queryDetail?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if(result.length>0){
                        var host = result[0];
                        $("#detail_hostid").val(hostid);
                        $("#detail_hostname").val(host.hostname);
                        $("#detail_addr").val(host.addr);
                        $("#detail_productname").val(host.productname);
                        $("#detail_nodeid").val(host.nodeidname);
                        $("#detail_hosttype").val(host.hosttypename);
                        $("#detail_host_room").val(host.host_room);
                        $("#detail_rack_num").val(host.rack_num);
                        $("#detail_os").val(host.os);
                        $("#detail_cpu").val(host.cpu);
                        $("#detail_memory").val(host.memory);
                        $("#detail_diskspace").val(host.diskspace);
                        $("#detail_ipv4").val(host.ipv4);
                        $("#detail_ipv6").val(host.ipv6);
                        $("#detail_location").val(host.location);
                        $("#detail_serialnumber").val(host.serialnumber);
                        $("#detail_isCollect").val(host.is_collect);
                        if($("#detail_isCollect").val() == 1){
                            $("#detail_isCollect").prop('checked', true)
                        }else{
                            $("#detail_isCollect").prop('checked', false);
                        }
                        showLayerDetail("detail_div",'采集机详情');
                    }
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

        //新增按钮事件
        function addShow(){
            reset("add_div");
            $("#add_hosttype").html("采集机主机");
            $.getJSON("/view/class/system/collectormanage/query/nodelist?random=" + Math.random(), function(result) {
                $("#add_nodeid").empty();
                $.each(result,function(i,data){
                    $("#add_nodeid").append("<option value=\""+data.id+"\">"+data.node_name+"</option>");
                });
            });
            showLayer("add_div",'新增采集机');
        }

        //新增确认
        function addInfo(){
            var hostname = stringutil.Trim($("#add_hostname").val());
            var addr = stringutil.Trim($("#add_addr").val());
            var nodeid = $("#add_nodeid").val();
            var hosttype = $("#add_hosttype").html();
            // var productname = stringutil.Trim($("#add_productname").val());
            var os = stringutil.Trim($("#add_os").val());

            // var host_room = stringutil.Trim($("#add_host_room").val()); //位置
            // var rack_num = stringutil.Trim($("#add_rack_num").val());
            // var cpu = stringutil.Trim($("#add_cpu").val());
            // var memory = stringutil.Trim($("#add_memory").val());
            // var diskspace = stringutil.Trim($("#add_diskspace").val());
            var ipv4 = stringutil.Trim($("#add_ipv4").val());
            var ipv6 = stringutil.Trim($("#add_ipv6").val());
            // var serialnumber = stringutil.Trim($("#add_serialnumber").val());
            // var location = stringutil.Trim($("#add_location").val());
            var isCollect = stringutil.Trim($("#add_isCollect").val());
            if(validHostName('add',hostname)|| validAddr('add',addr) || validNodeId('add',nodeid) || validOS('add',os)){
                return;
            }
            if(stringutil.checkString("add_os",os,"操作系统不能超过100位!",100)
                || stringutil.checkString("add_addr",addr,"采集机IP不能超过100位!", 100)
                || stringutil.checkString("add_hostname",hostname,"采集机名称不能超过64位!",100)) {
                return;
            }
            //根据采集机IP判断是否有重复采集机
            $.ajax({
                type: "post",
                url: "/view/class/system/collectormanage/query?random=" + Math.random(),
                cache: false,
                async: false,
                data: {"addr": addr,"hostname":hostname},
                success: function (result) {
                    var length = result.length;
                    if(length > 0){
                        $("#add_addr").focus();
                        layer.msg("采集机名称或采集机IP不能重复!",{
                            time:2000,
                            skin: 'layer_msg_color_alert'
                        });
                        return;
                    }else{
                        loadingwait();
                        var data = {hostname:hostname,addr:addr,nodeid:nodeid,hosttype:hosttype,os:os,ipv4:ipv4,ipv6:ipv6,is_collect:isCollect};
                        $.getJSON("/view/class/system/collectormanage/add/?random=" + Math.random(),data, function(result) {
                            layer.closeAll();
                            addLayerResultAndReload(result);
                        });
                    }
                }
            });
        }

        //修改按钮事件
        function modifyShow(){
            var checkboxArray = checkbox.getReturnArray();
            if (checkboxArray.length == 0) {
                layer.msg("没有选择采集机，请选择一个采集机!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            } else if (checkboxArray.length > 1) {
                layer.msg("修改采集机只能选择一个!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            } else {
                var hostid = checkboxArray[0];
                reset("modify_div");
                var data = {hostid : hostid};
                $.ajax({
                    type: "post",
                    url: "/view/class/system/collectormanage/query/nodelist?random=" + Math.random(),
                    cache: false,
                    async: false,
                    dataType: "json",
                    success: function (result) {
                        $("#modify_nodeid").empty();
                        $.each(result,function(i,data){
                            $("#modify_nodeid").append("<option value=\""+data.id+"\">"+data.node_name+"</option>");
                        });
                    }
                });
                loadingwait();
                $.ajax({
                    type: "post",
                    url: "/view/class/system/collectormanage/query/singleinfo?random=" + Math.random(),
                    data: data,
                    cache: false,
                    async: false,
                    dataType: "json",
                    success: function (result) {
                        if(result.length>0){
                            var host = result[0];
                            $("#modify_isCollect").val(host.is_collect);
                            if($("#modify_isCollect").val() == 1){
                                $("#modify_isCollect").prop('checked', true)
                            }else{
                                $("#modify_isCollect").prop('checked', false);
                            }
                            $("#modify_hostid").val(hostid);
                            $("#modify_hostname").val(host.hostname);
                            $("#modify_addr").val(host.addr);
                            $("#modify_productname").val(host.productname);
                            $("#modify_nodeid option").each(function(){
                                if($(this).attr('value')==host.nodeid){
                                    $(this).prop("selected", "true");
                                }
                            });
                            $("#modify_host_room").val(host.host_room);
                            $("#modify_rack_num").val(host.rack_num);
                            $("#modify_os").val(host.os);
                            $("#modify_cpu").val(host.cpu);
                            $("#modify_memory").val(host.memory);
                            $("#modify_diskspace").val(host.diskspace);
                            $("#modify_ipv4").val(host.ipv4);
                            $("#modify_ipv6").val(host.ipv6);
                            $("#modify_location").val(host.location);
                            $("#modify_serialnumber").val(host.serialnumber);
                            showLayer("modify_div",'修改采集机');
                        }
                    }
                });
            }
        }

        //修改确认
        function modifyInfo(){
            var hostid = stringutil.Trim($("#modify_hostid").val());
            var hostname = stringutil.Trim($("#modify_hostname").val());
            var addr = stringutil.Trim($("#modify_addr").val());
            var nodeid = $("#modify_nodeid").val();
            var os = stringutil.Trim($("#modify_os").val());

            var ipv4 = stringutil.Trim($("#modify_ipv4").val());
            var ipv6 = stringutil.Trim($("#modify_ipv6").val());
            var isCollect = stringutil.Trim($("#modify_isCollect").val());

            console.log(isCollect);
            if(validOS('modify',os) || validNodeId('modify',nodeid) ||
                validAddr('modify',addr) || validHostName('modify',hostname)){
                return;
            }
            if(stringutil.checkString("modify_os",os,"操作系统不能超过100位!",100) || stringutil.checkString("modify_addr",addr,"采集机IP不能超过100位!", 100)|| stringutil.checkString("modify_hostname",hostname,"采集机名称不能超过64位!",100)) {
                return;
            }
            loadingwait();
            if ($('#modify_isCollect').prop('checked') == false){
                isCollect = 0;
            }
            console.log(isCollect);
            var data= {hostid:hostid,hostname:hostname,addr:addr,nodeid:nodeid,os:os,ipv4:ipv4,ipv6:ipv6,is_collect:isCollect};
            $.getJSON("/view/class/system/collectormanage/modify?random=" + Math.random(),data, function(result) {
                layer.closeAll();
                layerResultAndReload(result);
            });
        }

        //删除按钮事件
        function deleteShow() {
            var checkboxArray = checkbox.getReturnArray();
            if (checkboxArray.length == 0) {
                layer.msg("没有选择采集机，请选择一个采集机!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            } else {
                layer.confirm('是否确认删除选择的采集机数据？', {
                    closeBtn:0,
                    title: '询问',
                    btn: ['确认','取消']
                },function(){
                    layer.closeAll();
                    loadingwait();
                    var data = {hostidArray:checkboxArray};
                    var url = "/view/class/system/collectormanage/delete/?random=" + Math.random();
                    $.getJSON(url,data,function(result){
                        layer.close(layer_load);
                        loadOptRecord();
                        layer.alert(result.message);
                    })
                });
            }
        }

        function isNull(data){
            if(data==null || data ==''){
                return '';
            }else
                return data;
        }

        function bindCheckBox(){
            $("#hostdiv :checkbox").click(function(){
                var flag = $(this).prop('checked');
                var value = $(this).val();
                checkbox.checkboxSingle(flag,value);
            });
        }

        //查询按钮事件
        function queryOpt(){
            var addr = $("#addr").val();
            if(addr != null && addr != ""){
                var addrress = stringutil.Trim(addr);
                if (!checkIp(addrress) && addrress != "") {
                    layer.tips('请输入正确格式的采集机IP!', '#addr',{ tips: [2, '#EE1A23'] });
                    $("#addr").focus();
                    return false;
                }
            }
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

            var hostname = stringutil.Trim($("#hostname").val());
            var addr = stringutil.Trim($("#addr").val());
            // var hosttype = stringutil.Trim($("#hosttype").val());
            var nodeid = stringutil.Trim($("#nodeid").val());
            var data = {'hostname':hostname,'addr':addr,'nodeid':nodeid};
            var host_detail_show = $("#host_detail_show").val();
            $.getJSON("/view/class/system/collectormanage/query/infolist?random=" + Math.random(),data, function(result) {
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
                        showTableDetail(result,startnum,endnum);
                        $("#currnum").text( startnum + "-" + endnum);
                        if(total == 0) {
                            $("#currnum").empty().text("0 ");
                        }
                        resizewh.resizeBodyH($("#mainhost"));
                    },
                    groups: page_count //连续显示分页数
                });
            });
        }

        //拼接tr
        function showTableDetail(data,startnum,endnum){
            var rowdata = "";
            //计算选中条数
            var k = 0;
            for(var i=startnum;i<=endnum;i++){
                var rowninfo = data[i-1];
                var id = rowninfo.hostid;
                var checked = "";
                if(checkbox.isExitArray(id)){
                    checked = "checked=\"checked\"";
                    k++;
                }
                rowdata = rowdata + "<tr>"+
                    "<td><input type=\"checkbox\" name=\"checkbox\" value=\""+rowninfo.hostid+"\" id=\""+rowninfo.hostid+"\" "+checked+" /></td>"
                    + "<td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.addr+"\">" + rowninfo.addr + "</td>"
                    + "<td class='over_ellipsis' style='max-width:120px' title=\""+rowninfo.hostname+"\">" + rowninfo.hostname + "</td>"
                    + "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.os)+"\">" + stringutil.isNull(rowninfo.os) + "</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.nodeidname)+"\">"
                    + stringutil.isNull(rowninfo.nodeidname) + "</td><td>"
                    + "<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.hostid+"\">详情</a>"
                    + "</td>";
            }
            //本页条数
            var ct = endnum-startnum+1;
            if(k==ct&&k>0){
                //全选框选中
                $("#checkboxAll").prop("checked", true);
            }else{
                $("#checkboxAll").prop("checked", false);
            }
            $("#hostdiv").empty().append(rowdata);
            $("[name=detail]").each(function(){
                $(this).on('click',function(){
                    detailShow($(this).attr('id'));
                });
            });
            checkbox.bindSingleCheckbox("hostdiv");
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
                addJumpShow(result.message);
            }else{
                layer.msg(result.message,{
                    time:2000,
                    skin: 'layer_msg_color_error'
                });
            }
        }

        function addJumpShow(operateid) {
            layer.confirm('是否需要跳转到采集机指标配置管理页面？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] //按钮
            },function(){
                window.location.href = "/view/class/system/collectmetricmanage?key=collectmetricmanage&operatetype=2&operateid="+operateid+"&random=" + Math.random();
            });
        }

        function showLayerDetail(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '620px', '560px' ],
                content : $("#"+divid)
            });
        }

        function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '550px', '360px' ],
                content : $("#"+divid)
            });
        }

        function validProductName(type,productname){
            if(null == productname || productname==""){
                $("#"+type+"_productname").focus();
                layer.tips('采集机型号不能为空!', '#'+type+'_productname',{ tips: [2, '#EE1A23'] });
                return true;
            }
            return false;
        }
        function validOS(type,os){
            if(null == os || os==""){
                $("#"+type+"_os").focus();
                layer.tips('操作系统不能为空!', '#'+type+'_os',{ tips: [2, '#EE1A23'] });
                return true;
            }
            return false;
        }

        function validNodeId(type,nodeid){
            if(null == nodeid || nodeid==""){
                $("#"+type+"_nodeid").focus();
                layer.tips('所属节点不能为空!', '#'+type+'_nodeid',{ tips: [2, '#EE1A23'] });
                return true;
            }
            return false;
        }
        function validHostName(type,hostname){
            if(null == hostname || hostname==""){
                $("#"+type+"_hostname").focus();
                layer.tips('采集机名称不能为空!', '#'+type+'_hostname',{ tips: [2, '#EE1A23'] });
                return true;
            }
            return false;
        }
        function validAddr(type,addr){
            if(null == addr || addr==""){
                $("#"+type+"_addr").focus();
                layer.tips('采集机IP不能为空!', '#'+type+'_addr',{ tips: [2, '#EE1A23'] });
                return true;
            }
            if (!checkIp(addr)) {
                $("#"+type+"_addr").focus();
                layer.tips('请输入正确格式的采集机IP!', '#'+type+'_addr',{ tips: [2, '#EE1A23'] });
                return true;
            }
            return false;
        }

        //返回checkbox的选中的id的数组
        function getCheckboxArray(groupCheckbox){
            var checkboxArray=new Array();
            for(i=0;i<groupCheckbox.length;i++){
                if(groupCheckbox[i].checked){
                    var id =groupCheckbox[i].id;
                    checkboxArray.push(id);
                }
            }
            return checkboxArray;
        }

        //校验IP
        function checkIp(ip) {
            var regex1 = /^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
            var regex2 = '0.0.0.0';
            return regex1.test(ip) && regex2 != ip;
        }

        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
        }
    });