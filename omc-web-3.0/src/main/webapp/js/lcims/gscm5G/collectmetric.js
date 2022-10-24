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
        resizewh.resizeBodyH($("#collectmetricmanage"));
        $("#query_div").hide();
        initChildrenMenu();
        butBindFunction();
        
//---------------------以下为自定义方法----------------------------------//
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
                $("#hostmetricadddiv").empty();
                $("#checkboxAddAll").prop('checked',false);
                checkbox.cleanArray();
//                queryOperateModuleChange();
                queryHostMetricInfoChange();
                $("#bind_operateid option").each(function(){
                    if($(this).attr('value')==operateid){
                        $(this).prop("selected", "true");
                    }
                });
                //获取主机指标配置详细信息
                checkbox.cleanArray();
                queryHostMetricInfoChange();
            } else{
                //查询
                $('.tags').first().addClass('tag_on').siblings().removeClass("tag_on");
                //初始化查询条件
//                initQueryCondition();
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
                             if(result[i].url=='bind'){
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
                                 if(result[i].url=='unbind'){
                                     $("#"+result[i].name).click(function() {
                                         updateHostMetric('unbind');
                                     });
                                 }else if(result[i].url=='publish'){
                                     $("#"+result[i].name).click(function() {
                                         updateHostMetric('publish');
                                     });
                                 }else if(result[i].url=='edit'){
                                     $("#"+result[i].name).click(function() {
                                         modifyHostMetricShow();
                                         });
                                 }else if(result[i].url=='delete'){
                                                    $("#"+result[i].name).click(function() {
                                                        deleteHostMetric();
                                                    });
                                                }
                                 
                             }
                         }
                     }
                    initShow();
                }
             });
        }
        
        /**
         * 解绑操作
         * @returns
         */
        function updateHostMetric(type){
            var arr = checkbox.getReturnArray();
            var message="请选择解绑数据";
            if(type=="publish"){
                message = "请选择下发数据";
            }
            if(arr.length==0){
                layer.msg(message,{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
                return;
            }
            var hostMetricids=arr.toString();
            var metricid = $("#query_metricid_page").val();
            message="是否解绑数据？";
            var url = "/view/class/system/collectmetricmanage/unbindHostMetric";
            if(type=="publish"){
                url = "/view/class/system/collectmetricmanage/publishHostMetric";
                message = "是否下发数据？";
            }
            
            layer.confirm(message, {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] //按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {hostMetricids:hostMetricids,metricid:metricid};
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
                        checkbox.cleanArray();
                        layer.closeAll();
                        layer.msg(result.message,{
                            time:2000,
                            skin: 'layer_msg_color_success'
                        });
                        var page_curr = $("#page_curr").val();
                        loadOptRecord(page_curr);
                    }
                 });
            });
        }
        
        function deleteHostMetric(){
            var arr = checkbox.getReturnArray();
            var message="请选择删除数据";
            if(arr.length==0){
               layer.msg(message,{
                 time:2000,
                 skin: 'layer_msg_color_alert'
              });
              return;
            }
            var hostMetricids=arr.toString();
            var metricid = $("#query_metricid_page").val();
            message="是否删除数据？";
            var url = "/view/class/system/collectmetricmanage/deleteHostMetric";
            layer.confirm(message, {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] //按钮
            },function(){
                layer.closeAll();
                loadingwait();
                var data = {hostMetricids:hostMetricids,metricid:metricid};
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
                        checkbox.cleanArray();
                        layer.closeAll();
                        layer.msg(result.message,{
                            time:3000,
                            skin: 'layer_msg_color_success'
                        });
                        var page_curr = $("#page_curr").val();
                        loadOptRecord(page_curr);
                    }
                 });
            });
        }
        
        /**
         * 修改显示
         * @returns
         */
        function modifyHostMetricShow(){
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
            var metricid = $("#query_metricid_page").val();
            var data = {id:id,metricid:metricid};
            
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/collectmetricmanage/query/singleinfoState?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                    layer.close(layer_load);
                    if(result.opSucc){
                        loadingwait();
                        $.ajax({
                            type: "post",
                            url: "/view/class/system/collectmetricmanage/query/singleinfo?random=" + Math.random(),
                            data: data,
                            cache: false,
                            async: false, 
                            dataType: "json",
                            success: function (result) {
                                layer.close(layer_load);
                                if(result!=null && result!=""){
                                    $("#modify_id").val(result.id);
                                    $("#modify_hostname").val(result.hostname);
                                    $("#modify_addr").val(result.addr);
                                    $("#modify_metric_name").val(result.metric_name);
                                    $("#modify_script").val(result.script);
                                    $("#modify_script_param").val(result.script_param);
                                    $("#modify_state option").each(function(){
                                        if($(this).attr('value')==result.state){
                                            $(this).prop("selected", "true");
                                        }
                                    });
                                    $("#modify_cycle_id option").each(function(){
                                        if($(this).attr('value')==result.cycle_id){
                                            $(this).prop("selected", "true");
                                        }
                                    });
                                    $("#modify_script_return_type option").each(function(){
                                        if($(this).attr('value')==result.script_return_type){
                                            $(this).prop("selected", "true");
                                        }
                                    });
                                    
                                    showLayer("modify_div",'修改主机指标配置');
                                }
                            }
                         });
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
            var script = $("#modify_script").val();
            var script_param = $("#modify_script_param").val();
            var cycle_id = $("#modify_cycle_id").val();
            var script_return_type = $("#modify_script_return_type").val();
            
            if(stringutil.checkString('modify_script',script,'采集脚本不能为空!'))
                return;
            //校验采集脚本长度
            if(stringutil.checkString('modify_script',script,'采集脚本长度不能超过100位!',100))
                return;
            //校验采集脚本参数长度
            if(stringutil.checkString('modify_script_param',script_param,'采集脚本参数长度不能超过256位!',256))
                return;
            var metricid = $("#query_metricid_page").val();
            loadingwait();
            var data= {id:id,script:script,script_param:script_param,cycle_id:cycle_id,script_return_type:script_return_type,metricid:metricid};
            $.getJSON("/view/class/system/collectmetricmanage/modify?random=" + Math.random(),data, function(result) {
                layer.closeAll();
                checkbox.cleanArray();
                layerResultAndReload(result);
            });
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
            
            //查询页面全选框事件
            checkbox.bindAllCheckbox('checkboxAll','hostmetricdiv');
            
            //绑定页面全选框事件
            checkbox.bindAllCheckbox('checkboxAddAll','hostmetricadddiv');
            
            //绑定页面选择类型事件
            $("#bind_operatetype").bind("change",function(){
                $("#hostmetricadddiv").empty();
                $("#checkboxAddAll").prop('checked',false);
                //获取操作目标信息
                checkbox.cleanArray();
//                queryOperateModuleChange();
                queryHostMetricInfoChange();
            });
            
            //绑定页面选择项事件
            $("#bind_operateid").bind("change",function(){
                $("#hostmetricadddiv").empty();
                $("#checkboxAddAll").prop('checked',false);
                //获取主机指标配置详细信息
                checkbox.cleanArray();
                queryHostMetricInfoChange();
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
            var id,script,script_param,host_id,metric_id,script_return_type,cycle_id,hostip;
            var _list = new Array();
            for(var i=0;i<arr.length;i++){
                id=arr[i]; 
                //校验采集脚本空
                script=$("#script"+id).val();
                if(stringutil.checkString('script'+id,script,'采集脚本不能为空!'))
                    return;
                //校验采集脚本长度
                if(stringutil.checkString('script'+id,script,'采集脚本长度不能超过100位!',100))
                    return;
                //校验采集脚本参数长度
                script_param=$("#script_param"+id).val();
                if(stringutil.checkString('script_param'+id,script,'脚本参数长度不能超过256位!',256))
                    return;
                host_id=$("#hostid"+id).val();
                hostip=$("#hostip"+id).val();
                metric_id=$("#metricid"+id).val();
                script_return_type=$("#returnType"+id).val();
                cycle_id=$("#cycle"+id).val();
                var hostMetric=new Object();
                hostMetric.host_id=host_id;
                hostMetric.addr=hostip;
                hostMetric.metric_id=metric_id;
                hostMetric.cycle_id=cycle_id;
                hostMetric.script_return_type=script_return_type;
                hostMetric.script=script;
                hostMetric.script_param=script_param;
                _list.push(hostMetric);
            }
            var jsonData = JSON.stringify(_list);
            var operatetype= $("#bind_operatetype").val();
            var url = "/view/class/system/collectmetricmanage/getbindOperate";
             var data = {jsonData:jsonData,operatetype:operatetype};
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
                    $("#hostmetricadddiv").empty();
                    $("#checkboxAddAll").prop('checked',false);
                    //获取主机指标配置详细信息
                    checkbox.cleanArray();
                    queryHostMetricInfoChange();
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
            var url = "/view/class/system/collectmetricmanage/getSourceList";
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
                        if(operatetype=='2'){
                            for(var i=0;i<data.length;i++){
                                $("#bind_operateid").append('<option value="'+data[i].hostid+'">'+data[i].hostname+' '+data[i].addr+'</option>');
                            }
                        }else{
                            for(var i=0;i<data.length;i++){
                                $("#bind_operateid").append('<option value="'+data[i].id+'">'+data[i].metric_name+'</option>');
                            }
                        }
                    }
                }
             });
        }
        
        function queryHostMetricInfoChange(){
            var operatetype= $("#bind_operatetype").val();
            var operateid= $("#bind_operateid").val();
            if(operateid==null || operateid=="")
                return;
            //查询数据
            var url = "/view/class/system/collectmetricmanage/getHostMetricConfigInfo";
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
                    var collcyclelist = result[1];
                    var datalist = result[2];
                    if(datalist!=null){
                        var rowdata = "";
                        var cycleSelect = "";
                        var returnTypeSelect = "";
                        for(var i=0;i<datalist.length;i++){
                            var rowninfo = datalist[i];
                            var id = rowninfo.host_id+""+rowninfo.metric_id;
                            //flag = -1 可编辑
                            var flag = rowninfo.flag;
                            var status="已绑定";
                            var readonly="readonly=\"readonly\"";
                            var disabled="disabled=\"disabled\"";
                            var selectDisabled="disabled=\"disabled\"";
                            var checked = "checked=\"checked\"";
                            if(flag=='-1'){
                                readonly="";
                                disabled = "";
                                checked = "";
                                status="未绑定"
                            }
                            var cycleSelect="<select style=\"width:100%;\" "+selectDisabled+" id=\"cycle"+id+ "\" style=\"width:90px;\""+ ">";
                            for(var j=0;j<collcyclelist.length;j++){
                                if(collcyclelist[j].cycleid==rowninfo.cycle_id){
                                    cycleSelect = cycleSelect+ "<option selected=\"selected\" value=\""+collcyclelist[j].cycleid+"\" > "+collcyclelist[j].cyclename+" </option>"
                                }else{
                                    cycleSelect = cycleSelect+ "<option value=\""+collcyclelist[j].cycleid+"\" > "+collcyclelist[j].cyclename+" </option>"
                                }
                            }
                            cycleSelect = cycleSelect+"</select>";
                            
                            returnTypeSelect="<select style=\"width:100%;\" "+selectDisabled+" id=\"returnType"+id+"\">";
                            for(var k=0;k<returnTypelist.length;k++){
                                if(rowninfo.script_return_type==returnTypelist[k].code){
                                    returnTypeSelect = returnTypeSelect+"<option selected=\"selected\" value=\""+returnTypelist[k].code+"\" > "+returnTypelist[k].description+" </option>"
                                }else{
                                    returnTypeSelect = returnTypeSelect+"<option value=\""+returnTypelist[k].code+"\" > "+returnTypelist[k].description+" </option>"
                                }
                            }
                            var metric_name = rowninfo.metric_name;
                            if(rowninfo.metric_name.length>10){
                                metric_name = metric_name.substring(0,8)+"..";
                            }
                            var hostname = rowninfo.hostname;
                            if(rowninfo.hostname.length>12){
                                hostname = hostname.substring(0,10)+"..";
                            }
                            returnTypeSelect = returnTypeSelect+"</select>";
                            rowdata = rowdata + "<tr><td>"+"<input "+checked+" "+disabled+" type=\"checkbox\" value=\""+id+"\" id=\""+id+"\" />"+"</td>"
                            +"<td title=\""+rowninfo.hostname+"\">"+hostname+"<input type=\"hidden\" id=\"hostid"+id+"\" value=\""+rowninfo.host_id+"\" />"+"</td>"
                            +"<td title=\""+rowninfo.addr+"\" >"+rowninfo.addr+"<input type=\"hidden\" id=\"hostip"+id+"\" value=\""+rowninfo.addr+"\" />"+"</td>"
                            +"<td title=\""+rowninfo.metric_name+"\">"+metric_name+"<input type=\"hidden\" id=\"metricid"+id+"\" value=\""+rowninfo.metric_id+"\" />"+"</td>"
                            +"<td>"+cycleSelect+"</td>"
                            +"<td><input style=\"width:100%;\" id=\"script"+id+"\" type=\"text\" value=\""+stringutil.isNull(rowninfo.script)+"\" maxlength=\"99\" size=\"100\" "+disabled+" /></td>"
                            +"<td><input style=\"width:100%;\" id=\"script_param"+id+"\" type=\"text\" value=\""+stringutil.isNull(rowninfo.script_param)+"\" maxlength=\"99\" size=\"256\" "+disabled+" /></td>"
                            +"<td>"+returnTypeSelect+"</td>"
                            +"<td>"+status+"</td>"
                            +"</tr>";
                        }
                        $("#hostmetricadddiv").append(rowdata);
                        checkbox.bindSingleCheckbox('hostmetricadddiv');
                        resizewh.resizeBodyH($("#collectmetricmanage"));
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
            $("#query_hostid_page").val('-1');
            $("#query_metricid_page").val('-1');
            checkbox.cleanArray();
            loadOptRecord(1);
        }
        
        function resetBindDiv(){
            reset("bind_div");
//            $("#bind_operateid").empty();
            $("#checkboxAddAll").prop("checked", false);
            $("#hostmetricadddiv").empty();
            checkbox.cleanArray();
        }
        
      //加载查询内容
        function loadOptRecord(pageNumber){
            var hostid = $("#query_hostid_page").val();
            var metricid = $("#query_metricid_page").val();
            $("#checkboxAll").prop("checked", false);
            var pagecount=0;
            var page_count = 10;
            loadingwait();
            var data = {'hostid':hostid,'metricid':metricid,'pageNumber':pageNumber};
            $.getJSON("/view/class/system/collectmetricmanage/query?random=" + Math.random(),data, function(result) {
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
                    resizewh.resizeBodyH($("#collectmetricmanage"));
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
                +"<td title=\""+rowninfo.hostname+"\">"+rowninfo.hostname+"</td>"
                +"<td>"+rowninfo.addr+"</td>"
                +"<td title=\""+rowninfo.metric_name+"\">"+rowninfo.metric_name+"</td>"
                +"<td>"+rowninfo.cyclename+"</td>"
                +"<td title=\""+rowninfo.script+"\">"+rowninfo.script+"</td>"
                +"<td>"+rowninfo.statename+"</td>"
                +"<td>"+"<a class=\"J_edit pr10\" href=\"#\" name='detail' id=\""+rowninfo.id+"\" >详情</a>"+"</td>"
                +"</tr>";
            }
            //本页条数
            var ct = endnum-startnum+1;
            if(k==ct&&k>0){
                //全选框选中
                $("#checkboxAll").prop("checked", true);
            }else{
                $("#checkboxAll").prop("checked", false);
            }
            $("#hostmetricdiv").empty().append(rowdata);
            $("[name=detail]").each(function(){
                $(this).on('click',function(){
                    detailShow($(this).attr('id'));
                });
            });
            checkbox.bindSingleCheckbox("hostmetricdiv");
        }  
        //详情
        function detailShow(id){
            reset("detail_div");
            var metricid = $("#query_metricid_page").val();
            var data = {id:id,metricid:metricid};
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/collectmetricmanage/query/singleinfo?random=" + Math.random(),
                data: data,
                cache: false,
                async: false, 
                dataType: "json",
                success: function (result) {
                    layer.close(layer_load);
                    if(result!=null && result!=""){
                        $("#detail_hostname").val(result.hostname);
                        $("#detail_addr").val(result.addr);
                        $("#detail_metric_name").val(result.metric_name);
                        $("#detail_cyclename").val(result.cyclename);
                        $("#detail_returntypename").val(result.returntypename);
                        $("#detail_script").val(result.script);
                        $("#detail_script_param").val(result.script_param);
                        $("#detail_statename").val(result.statename);
                        showLayerDetail("detail_div",'主机指标配置详情');
                    }
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