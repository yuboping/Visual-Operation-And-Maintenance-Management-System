require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil',
        'select2': '/js/select2/select2.min'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','stringutil','select2'],
    function($,layer,laypage,resizewh,checkbox,stringutil,select2) {
        var modulelistvar;
        var initializationmetricvar;
        var initializationalarmlevel;
        var initializationalarmmodes;
        var monitortargetonelevellist;
        var monitortargettwolevellist;
        var monitortargetthreelevellist;
        var monitortargetfourlevellist;
        var addmonitortargetonelevellist;
        var addmonitortargettwolevellist;
        var addmonitortargetthreelevellist;
        var addmonitortargetfourlevellist;
        var addinitializationchartsvar;
        var addinitializationmetricvar;
        var modifymonitortargetonelevellist;
        var modifymonitortargettwolevellist;
        var modifymonitortargetthreelevellist;
        var modifymonitortargetfourlevellist;
        var modifyinitializationchartsvar;
        var modifyinitializationmetricvar;
        var layer_load;
        var initializationalarmtype;
        var select2querymonitortarget2 = false;
        var select2addmonitortarget2 = false;
        
        resizewh.resizeBodyH($("#mainalarmrule"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','alarmrulediv');
        });
        reset("mainalarmrule");
        initChildrenMenu();
        // loadingwait();
        // queryOpt();
        
        
//----------------------------------以下为自定义方法-------------------------------------------------//
        function butBindFunction(){
            $("#querybutton").click(function() {
                queryOpt();
            });
            $("#resetbutton").click(function() {
            	resetbuttonclick();
            });
            $("#add_ok").click(function() {
            	if(select2querymonitortarget2 || select2addmonitortarget2){
            		resetbuttonclick();
            	}
                addInfo();
            });
            $("#add_cancle").click(function() {
            	if(select2querymonitortarget2 || select2addmonitortarget2){
            		resetbuttonclick();
            	}
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
            checkbox.bindAllCheckbox('checkboxAll','alarmrulediv');
            //查询初始化
            queryinitialization();
            //初始化告警级别
            queryinitializationalarmlevel("add");
            queryinitializationalarmlevel("modify");
            //初始化告警方式
            queryinitializationalarmmodes("add");
            queryinitializationalarmmodes("modify");
            //初始化告警类型
            queryinitializationalarmtype("add");
            queryinitializationalarmtype("modify");
        }

        function resetbuttonclick(){
        	reset("mainalarmrule");
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#querymonitortarget1").empty().append(rowdata);
            $("#querymonitortarget2").empty().append(rowdata);
            $("#querymonitortarget3").empty().append(rowdata);
            $("#querymonitortarget4").empty().append(rowdata);
            $("#querymonitortarget2").css('display','none');
            $(".select2-selection").css('display','none');
            $("#querymonitortarget3").css('display','none');
            $("#querymonitortarget4").css('display','none');
            if(select2querymonitortarget2){
                $('#querymonitortarget2').data('select2').destroy();
                select2querymonitortarget2 = false;
        	}
            if(select2addmonitortarget2){
                $('#addmonitortarget2').data('select2').destroy();
                select2addmonitortarget2 = false;
        	}
        }
        
        //生成告警级别
        function queryinitializationalarmlevel(type){
            $('#'+type+'_alarm_rule').bind('input propertychange', function() {
                var alarm_rule = $(this).val();
                modifyalarmrule(type, alarm_rule);
            });
        }

        //修改告警规则
        function modifyalarmrule(type,alarm_rule){
            $("#"+type+"alarmlevel").empty();
            var arr=alarm_rule.split('#');
            var rowdata = "";
            if(arr.length>3){
                layer.tips('告警区间不得大于3', '#'+type+'_alarm_rule',{ tips: [2, '#EE1A23'] });
            }else{
                if(arr.length>0){
                    rowdata = rowdata + "<td style=\"text-align:right;padding-left:10px;\">告警级别:</td>";
                    for(i=0;i<arr.length;i++){
                        rowdata = rowdata + "<td style=\"padding-left:10px;width:25%;float:left;\"><select class=\"\" style=\"width: 100px;\" id=\""+type+"_alarm_level_"+(i+1)+"\">";
                        for(var j=0;j<initializationalarmlevel.length;j++){
                            rowdata = rowdata + "<option value="+initializationalarmlevel[j].alarmlevel+">"+initializationalarmlevel[j].alarmname+"</option>";
                        }
                        rowdata = rowdata + "</select> <span class=\"text-danger\">*</span></td>";
                    }
                    $("#"+type+"alarmlevel").empty().append(rowdata);
                }
            }
        }

        //生成告警方式
        function queryinitializationalarmmodes(type){
            var rowdata = "";
            rowdata = rowdata + "<td style=\"padding-left:10px;\">告警方式:</td> <td style=\"padding-left:10px;float:left;width:80%;\">";
            for(i=0;i<initializationalarmmodes.length;i++){
                rowdata = rowdata + "<div style=\"padding-left:10px;float:left;width:100%;\"> <div style=\"padding-left:10px;width:20%;float:left;\">"+initializationalarmmodes[i].DESCRIPTION+":</div> <div style=\"padding-left:10px;width:50%;float:left;\"><select class=\"\" style=\"width: 130px;\" id=\""+type+"_modes_"+(i+1)+"\">  <option value=\"\" selected=\"true\">请选择</option>";
                for(var j=0;j<initializationalarmmodes[i].mdAlarmModeList.length;j++){
                    rowdata = rowdata + "<option value="+initializationalarmmodes[i].mdAlarmModeList[j].modeid+">"+initializationalarmmodes[i].mdAlarmModeList[j].modename+"</option>";
                }
                rowdata = rowdata + "</select></div> </div>";
            }
            rowdata = rowdata + "</td>";
            $("#"+type+"alarmmodes").empty().append(rowdata);
        }
        
        //生成告警类型
        function queryinitializationalarmtype(type){
        	var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
        	for(i=0;i<initializationalarmtype.length;i++){
        		rowdata = rowdata + "<option value="+initializationalarmtype[i].CODE+">"+initializationalarmtype[i].DESCRIPTION+"</option>";
        		}
        	$("#"+type+"_alarm_type").empty().append(rowdata);
           
        }

        //查询初始化
        function queryinitialization(){
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/modulelist?random=" + Math.random(), function(result) {
                modulelistvar = result;
            });
            $.getJSON("/view/class/system/alarmrulemanage/query/mdalarmlevellist?random=" + Math.random(), function(result) {
                initializationalarmlevel = result;
            });
            $.getJSON("/view/class/system/alarmrulemanage/query/alarmmodeslist?random=" + Math.random(), function(result) {
                initializationalarmmodes = result;
            });
            $.getJSON("/view/class/system/alarmrulemanage/query/alarmtypelist?random=" + Math.random(), function(result) {
                initializationalarmtype = result;
            });
            //初始化模块
            querymoduleinitialization();
            //初始化指标下拉列表
            querymetricinitialization();
        }

        //初始化模块
        function querymoduleinitialization(){
            $("#querymodule").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            for(var i=0;i<modulelistvar.length;i++){
                rowdata = rowdata + "<option value="+modulelistvar[i].id+">"+modulelistvar[i].show_name+"</option>";
            }
            $("#querymodule").empty().append(rowdata);
            //模块选择事件
            querymodulechange();
        }

        //模块选择事件
        function querymodulechange(){
            $("#querymodule").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#querymonitortarget1").empty().append(rowdata);
                $("#querymonitortarget2").empty().append(rowdata);
                $("#querymonitortarget3").empty().append(rowdata);
                $("#querymonitortarget4").empty().append(rowdata);
                $("#querymonitortarget2").css('display','none');
                $(".select2-selection").css('display','none');
                $("#querymonitortarget3").css('display','none');
                $("#querymonitortarget4").css('display','none');
                var id = $("#querymodule").val();
                if(id!=null && id!=""){
                    //初始化监控目标一级下拉列表
                    querymonitortargetoneinitialization();
                }
            });
        }

        //初始化监控目标一级下拉列表
        function querymonitortargetoneinitialization(){
            $("#querymonitortarget1").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#querymodule").val();
            var data= {id:id};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetonelevellist?random=" + Math.random(),data, function(result) {
                monitortargetonelevellist = result;
                for(var i=0;i<monitortargetonelevellist.length;i++){
                    rowdata = rowdata + "<option value="+monitortargetonelevellist[i].id+">"+monitortargetonelevellist[i].name+"</option>";
                }
                $("#querymonitortarget1").empty().append(rowdata);
            });
            //监控目标一级下拉列表选择事件
            querymonitortargetonechange();
        }

        //监控目标一级下拉列表选择事件
        function querymonitortargetonechange(){
            $("#querymonitortarget1").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#querymonitortarget2").empty().append(rowdata);
                $("#querymonitortarget3").empty().append(rowdata);
                $("#querymonitortarget4").empty().append(rowdata);
                $("#querymonitortarget2").css('display','none');
                $(".select2-selection").css('display','none');
                $("#querymonitortarget3").css('display','none');
                $("#querymonitortarget4").css('display','none');
                var id = $("#querymonitortarget1").val();
                if(id!=null && id!=""){
                    //初始化监控目标二级下拉列表
                    querymonitortargettwoinitialization();
                    //初始化监控目标三级下拉列表
                    querymonitortargetthreeinitialization();
                }
            });
        }

        //初始化监控目标二级下拉列表
        function querymonitortargettwoinitialization(){
            $("#querymonitortarget2").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#querymonitortarget1").val();
            for(var i=0;i<monitortargetonelevellist.length;i++){
                if(monitortargetonelevellist[i].id == id){
                    var mdMenu = monitortargetonelevellist[i].mdMenu;
                    var type = monitortargetonelevellist[i].type;
                }
            }
            var data= {name:mdMenu.name,dynamic:mdMenu.dynamic,type:type};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargettwolevellist?random=" + Math.random(),data, function(result) {
                monitortargettwolevellist = result;
                if(monitortargettwolevellist.length>0){
                    $("#querymonitortarget2").css('display','block');
                }else{
                    $("#querymonitortarget2").css('display','none');
                }
                for(var i=0;i<monitortargettwolevellist.length;i++){
                    rowdata = rowdata + "<option value="+monitortargettwolevellist[i].id+">"+monitortargettwolevellist[i].name+"</option>";
                }
                $("#querymonitortarget2").empty().append(rowdata);
            });
            //监控目标二级下拉列表选择事件
            querymonitortargettwochange();
            //select2插件引用
            if(mdMenu.dynamic=="apn" && type == "2"){
            	$('#querymonitortarget2').select2();
            	select2querymonitortarget2 = true;
            }else{
            	if(select2querymonitortarget2){
                	$('#querymonitortarget2').data('select2').destroy();
                	select2querymonitortarget2 = false;
            	}
            }
            if(monitortargettwolevellist.length>0){
                $(".select2-selection").css('display','block');
            }else{
                $(".select2-selection").css('display','none');
            }
        }

        //监控目标二级下拉列表选择事件
        function querymonitortargettwochange(){
            $("#querymonitortarget2").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#querymonitortarget4").empty().append(rowdata);
                $("#querymonitortarget4").css('display','none');
                var id = $("#querymonitortarget2").val();
                if(id!=null && id!=""){
                    //初始化监控目标四级下拉列表
                    querymonitortargetfourinitialization();
                }
            });
        }

        //初始化监控目标三级下拉列表
        function querymonitortargetthreeinitialization(){
            $("#querymonitortarget3").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#querymonitortarget1").val();
            var data= {id:id};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetthreelevellist?random=" + Math.random(),data, function(result) {
                monitortargetthreelevellist = result;
                if(monitortargetthreelevellist.length>0){
                    $("#querymonitortarget3").css('display','block');
                }else{
                    $("#querymonitortarget3").css('display','none');
                }
                for(var i=0;i<monitortargetthreelevellist.length;i++){
                    rowdata = rowdata + "<option value="+monitortargetthreelevellist[i].id+">"+monitortargetthreelevellist[i].name+"</option>";
                }
                $("#querymonitortarget3").empty().append(rowdata);
            });
            //监控目标三级下拉列表选择事件
            querymonitortargetthreechange();
        }

        //监控目标三级下拉列表选择事件
        function querymonitortargetthreechange(){
            $("#querymonitortarget3").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#querymonitortarget4").empty().append(rowdata);
                $("#querymonitortarget4").css('display','none');
                var id = $("#querymonitortarget3").val();
                if(id!=null && id!=""){
                    //初始化监控目标四级下拉列表
                    querymonitortargetfourinitialization();
                }
            });
        }

        //初始化监控目标四级下拉列表
        function querymonitortargetfourinitialization(){
            $("#querymonitortarget4").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var querymonitortarget1_id = $("#querymonitortarget1").val();
            for(var i=0;i<monitortargetonelevellist.length;i++){
                if(monitortargetonelevellist[i].id == querymonitortarget1_id){
                    var monitorTargetOneDynamic = monitortargetonelevellist[i].mdMenu.dynamic;
                    var monitorTargetOneType = monitortargetonelevellist[i].type;
                }
            }
            var querymonitortarget2_id = $("#querymonitortarget2").val();
            for(var i=0;i<monitortargettwolevellist.length;i++){
                if(monitortargettwolevellist[i].id == querymonitortarget2_id){
                    var monitorTargetTwoId = monitortargettwolevellist[i].id;
                }
            }
            var querymonitortarget3_id = $("#querymonitortarget3").val();
            if(querymonitortarget3_id != null && querymonitortarget3_id != ""){
                for(var i=0;i<monitortargetthreelevellist.length;i++){
                    if(monitortargetthreelevellist[i].id == querymonitortarget3_id){
                        var monitorTargetThreeDynamic = monitortargetthreelevellist[i].mdMenu.dynamic;
                        var monitorTargetThreeName = monitortargetthreelevellist[i].mdMenu.name;
                        var monitorTargetThreeType = monitortargetthreelevellist[i].type;
                    }
                }
                var data= {monitorTargetOneDynamic:monitorTargetOneDynamic,
                    monitorTargetOneType:monitorTargetOneType,
                    monitorTargetTwoId:monitorTargetTwoId,
                    monitorTargetThreeDynamic:monitorTargetThreeDynamic,
                    monitorTargetThreeName:monitorTargetThreeName,
                    monitorTargetThreeType:monitorTargetThreeType};
                $.ajaxSettings.async = false;
                $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetfourlevellist?random=" + Math.random(),data, function(result) {
                    monitortargetfourlevellist = result;
                    if(monitortargetfourlevellist.length>0){
                        $("#querymonitortarget4").css('display','block');
                    }else{
                        $("#querymonitortarget4").css('display','none');
                    }
                    for(var i=0;i<monitortargetfourlevellist.length;i++){
                        rowdata = rowdata + "<option value="+monitortargetfourlevellist[i].id+">"+monitortargetfourlevellist[i].name+"</option>";
                    }
                    $("#querymonitortarget4").empty().append(rowdata);
                });
                //监控目标四级下拉列表选择事件
                querymonitortargetfourchange();
            }else {
                $("#querymonitortarget4").empty().append(rowdata);
            }
        }

        //监控目标四级下拉列表选择事件
        function querymonitortargetfourchange(){
            $("#querymonitortarget4").off("change").on("change",function(){
                var id = $("#querymonitortarget4").val();
            });
        }

        //初始化指标下拉列表
        function querymetricinitialization(){
            $("#querymetric").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/allmetriclist?random=" + Math.random(), function(result) {
                initializationmetricvar = result;
                if(initializationmetricvar.length>0){
                    for(var x=0;x<initializationmetricvar.length;x++){
                        rowdata = rowdata + "<option value="+initializationmetricvar[x].id+">"+initializationmetricvar[x].metric_name+"</option>";
                    }
                }
                $("#querymetric").empty().append(rowdata);
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
                        }else if(result[i].url=='refresh'){
                            $("#"+result[i].name).click(function() {
                                refreshShow();
                            });
                        }
                    }
                }

            });
        }

        function bindCheckBox(){
            $("#alarmrulediv :checkbox").click(function(){
                var flag = $(this).prop('checked');
                var value = $(this).val();
                checkbox.checkboxSingle(flag,value);
            });
        }

        //新增按钮事件
        function addShow(){
            reset("add_div");
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#addmonitortarget1").empty().append(rowdata);
            $("#addmonitortarget2").empty().append(rowdata);
            $("#addmonitortarget3").empty().append(rowdata);
            $("#addmonitortarget4").empty().append(rowdata);
            $("#addcharts").empty().append(rowdata);
            $("#addmetric").empty().append(rowdata);
            $("#addmonitortarget2").css('display','none');
            $(".select2-selection").css('display','none');
            $("#addmonitortarget3").css('display','none');
            $("#addmonitortarget4").css('display','none');
            $("#addalarmlevel").empty();
            //初始化模块
            addmoduleinitialization();
            showLayer("add_div",'新增告警规则');
        }

        //初始化模块
        function addmoduleinitialization(){
            $("#addmodule").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            for(var i=0;i<modulelistvar.length;i++){
                rowdata = rowdata + "<option value="+modulelistvar[i].id+">"+modulelistvar[i].show_name+"</option>";
            }
            $("#addmodule").empty().append(rowdata);
            //模块选择事件
            addmodulechange();
        }

        //模块选择事件
        function addmodulechange(){
            $("#addmodule").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#addmonitortarget1").empty().append(rowdata);
                $("#addmonitortarget2").empty().append(rowdata);
                $("#addmonitortarget3").empty().append(rowdata);
                $("#addmonitortarget4").empty().append(rowdata);
                $("#addmonitortarget2").css('display','none');
                $(".select2-selection").css('display','none');
                $("#addmonitortarget3").css('display','none');
                $("#addmonitortarget4").css('display','none');
                $("#addcharts").empty().append(rowdata);
                $("#addmetric").empty().append(rowdata);
                var id = $("#addmodule").val();
                if(id!=null && id!=""){
                    //初始化监控目标一级下拉列表
                    addmonitortargetoneinitialization();
                }
            });
        }

        //初始化监控目标一级下拉列表
        function addmonitortargetoneinitialization(){
            $("#addmonitortarget1").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#addmodule").val();
            var data= {id:id};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetonelevellist?random=" + Math.random(),data, function(result) {
                addmonitortargetonelevellist = result;
                for(var i=0;i<addmonitortargetonelevellist.length;i++){
                    rowdata = rowdata + "<option value="+addmonitortargetonelevellist[i].id+">"+addmonitortargetonelevellist[i].name+"</option>";
                }
                $("#addmonitortarget1").empty().append(rowdata);
            });
            //监控目标一级下拉列表选择事件
            addmonitortargetonechange();
        }

        //监控目标一级下拉列表选择事件
        function addmonitortargetonechange(){
            $("#addmonitortarget1").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#addmonitortarget2").empty().append(rowdata);
                $("#addmonitortarget3").empty().append(rowdata);
                $("#addmonitortarget4").empty().append(rowdata);
                $("#addmonitortarget2").css('display','none');
                $(".select2-selection").css('display','none');
                $("#addmonitortarget3").css('display','none');
                $("#addmonitortarget4").css('display','none');
                var id = $("#addmonitortarget1").val();
                if(id!=null && id!=""){
                    //初始化监控目标二级下拉列表
                    addmonitortargettwoinitialization();
                    //初始化监控目标三级下拉列表
                    addmonitortargetthreeinitialization();
                }
                //初始化图表下拉列表
                addchartsinitialization();
            });
        }

        //初始化监控目标二级下拉列表
        function addmonitortargettwoinitialization(){
            $("#addmonitortarget2").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#addmonitortarget1").val();
            for(var i=0;i<addmonitortargetonelevellist.length;i++){
                if(addmonitortargetonelevellist[i].id == id){
                    var mdMenu = addmonitortargetonelevellist[i].mdMenu;
                    var type = addmonitortargetonelevellist[i].type;
                }
            }
            var data= {name:mdMenu.name,dynamic:mdMenu.dynamic,type:type};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargettwolevellist?random=" + Math.random(),data, function(result) {
                addmonitortargettwolevellist = result;
                if(addmonitortargettwolevellist.length>0){
                    $("#addmonitortarget2").css('display','block');
                }else{
                    $("#addmonitortarget2").css('display','none');
                }
                for(var i=0;i<addmonitortargettwolevellist.length;i++){
                    rowdata = rowdata + "<option value="+addmonitortargettwolevellist[i].id+">"+addmonitortargettwolevellist[i].name+"</option>";
                }
                $("#addmonitortarget2").empty().append(rowdata);
            });
            //监控目标二级下拉列表选择事件
            addmonitortargettwochange();
            //select2插件引用
            if(mdMenu.dynamic=="apn" && type == "2"){
            	$('#addmonitortarget2').select2({dropdownParent:$('#add_div')});
            	select2addmonitortarget2 = true;
            }else{
            	if(select2addmonitortarget2){
                	$('#addmonitortarget2').data('select2').destroy();
                	select2addmonitortarget2 = false;
            	}
            }
            if(addmonitortargettwolevellist.length>0){
                $(".select2-selection").css('display','block');
            }else{
                $(".select2-selection").css('display','none');
            }
        }

        //监控目标二级下拉列表选择事件
        function addmonitortargettwochange(){
            $("#addmonitortarget2").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#addmonitortarget4").empty().append(rowdata);
                $("#addmonitortarget4").css('display','none');
                var id = $("#addmonitortarget2").val();
                if(id!=null && id!=""){
                    //初始化监控目标四级下拉列表
                    addmonitortargetfourinitialization();
                }
                //初始化图表下拉列表
                addchartsinitialization();
            });
        }

        //初始化监控目标三级下拉列表
        function addmonitortargetthreeinitialization(){
            $("#addmonitortarget3").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#addmonitortarget1").val();
            var data= {id:id};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetthreelevellist?random=" + Math.random(),data, function(result) {
                addmonitortargetthreelevellist = result;
                if(addmonitortargetthreelevellist.length>0){
                    $("#addmonitortarget3").css('display','block');
                }else{
                    $("#addmonitortarget3").css('display','none');
                }
                for(var i=0;i<addmonitortargetthreelevellist.length;i++){
                    rowdata = rowdata + "<option value="+addmonitortargetthreelevellist[i].id+">"+addmonitortargetthreelevellist[i].name+"</option>";
                }
                $("#addmonitortarget3").empty().append(rowdata);
            });
            //监控目标三级下拉列表选择事件
            addmonitortargetthreechange();
        }

        //监控目标三级下拉列表选择事件
        function addmonitortargetthreechange(){
            $("#addmonitortarget3").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#addmonitortarget4").empty().append(rowdata);
                $("#addmonitortarget4").css('display','none');
                var id = $("#addmonitortarget3").val();
                if(id!=null && id!=""){
                    //初始化监控目标四级下拉列表
                    addmonitortargetfourinitialization();
                }
                //初始化图表下拉列表
                addchartsinitialization();
            });
        }

        //初始化监控目标四级下拉列表
        function addmonitortargetfourinitialization(){
            $("#addmonitortarget4").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var addmonitortarget1_id = $("#addmonitortarget1").val();
            for(var i=0;i<addmonitortargetonelevellist.length;i++){
                if(addmonitortargetonelevellist[i].id == addmonitortarget1_id){
                    var monitorTargetOneDynamic = addmonitortargetonelevellist[i].mdMenu.dynamic;
                    var monitorTargetOneType = addmonitortargetonelevellist[i].type;
                }
            }
            var addmonitortarget2_id = $("#addmonitortarget2").val();
            for(var i=0;i<addmonitortargettwolevellist.length;i++){
                if(addmonitortargettwolevellist[i].id == addmonitortarget2_id){
                    var monitorTargetTwoId = addmonitortargettwolevellist[i].id;
                }
            }
            var addmonitortarget3_id = $("#addmonitortarget3").val();
            if(addmonitortarget3_id != null && addmonitortarget3_id != ""){
                for(var i=0;i<addmonitortargetthreelevellist.length;i++){
                    if(addmonitortargetthreelevellist[i].id == addmonitortarget3_id){
                        var monitorTargetThreeDynamic = addmonitortargetthreelevellist[i].mdMenu.dynamic;
                        var monitorTargetThreeName = addmonitortargetthreelevellist[i].mdMenu.name;
                        var monitorTargetThreeType = addmonitortargetthreelevellist[i].type;
                    }
                }
                var data= {monitorTargetOneDynamic:monitorTargetOneDynamic,
                    monitorTargetOneType:monitorTargetOneType,
                    monitorTargetTwoId:monitorTargetTwoId,
                    monitorTargetThreeDynamic:monitorTargetThreeDynamic,
                    monitorTargetThreeName:monitorTargetThreeName,
                    monitorTargetThreeType:monitorTargetThreeType};
                $.ajaxSettings.async = false;
                $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetfourlevellist?random=" + Math.random(),data, function(result) {
                    addmonitortargetfourlevellist = result;
                    if(addmonitortargetfourlevellist.length>0){
                        $("#addmonitortarget4").css('display','block');
                    }else{
                        $("#addmonitortarget4").css('display','none');
                    }
                    for(var i=0;i<addmonitortargetfourlevellist.length;i++){
                        rowdata = rowdata + "<option value="+addmonitortargetfourlevellist[i].id+">"+addmonitortargetfourlevellist[i].name+"</option>";
                    }
                    $("#addmonitortarget4").empty().append(rowdata);
                });
                //监控目标四级下拉列表选择事件
                addmonitortargetfourchange();
            }else {
                $("#addmonitortarget4").empty().append(rowdata);
            }
        }

        //监控目标四级下拉列表选择事件
        function addmonitortargetfourchange(){
            $("#addmonitortarget4").off("change").on("change",function(){
                //初始化图表下拉列表
                addchartsinitialization();
            });
        }

        //初始化图表下拉列表
        function addchartsinitialization(){
            $("#addcharts").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#addmetric").empty().append(rowdata);
            $("#addmetricattr").empty().append(rowdata);
            if(addmonitortargetonelevellist != null){
                for(var i=0;i<addmonitortargetonelevellist.length;i++){
                    if(addmonitortargetonelevellist[i].id == $("#addmonitortarget1").val()){
                        var monitorTargetOneUrl = addmonitortargetonelevellist[i].mdMenu.url;
                        var monitorTargetOneType = addmonitortargetonelevellist[i].type;
                        var monitorTargetOneName = addmonitortargetonelevellist[i].name;
                    }
                }
            }
            if(addmonitortargetthreelevellist != null){
                for(var i=0;i<addmonitortargetthreelevellist.length;i++){
                    if(addmonitortargetthreelevellist[i].id == $("#addmonitortarget3").val()){
                        var monitorTargetThreeUrl = addmonitortargetthreelevellist[i].mdMenu.url;
                        var monitorTargetThreeType = addmonitortargetthreelevellist[i].type;
                        var monitorTargetThreeName = addmonitortargetthreelevellist[i].name;
                    }
                }
            }
            var attr1 = $("#addmonitortarget2").val();
            var attr2 = $("#addmonitortarget4").val();
            var data= {monitorTargetOneUrl:monitorTargetOneUrl,
                monitorTargetOneType:monitorTargetOneType,
                monitorTargetOneName:monitorTargetOneName,
                monitorTargetThreeUrl:monitorTargetThreeUrl,
                monitorTargetThreeType:monitorTargetThreeType,
                monitorTargetThreeName:monitorTargetThreeName,
                attr1:attr1,
                attr2:attr2};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/chartslist?random=" + Math.random(),data, function(result) {
                addinitializationchartsvar = result;
                if(addinitializationchartsvar.length>0){
                    for(var x=0;x<addinitializationchartsvar.length;x++){
                        rowdata = rowdata + "<option value="+addinitializationchartsvar[x].CHART_NAME+">"+addinitializationchartsvar[x].CHART_TITLE+"</option>";
                    }
                }
                $("#addcharts").empty().append(rowdata);
            });
            //图表下拉列表选择事件
            addchartschange();
        }

        //图表下拉列表选择事件
        function addchartschange(){
            $("#addcharts").off("change").on("change",function(){
                //初始化指标下拉列表
                addmetricinitialization();
            });
        }

        //初始化指标下拉列表
        function addmetricinitialization(){
            $("#addmetric").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var addcharts = $("#addcharts").val();
            for(var x=0;x<addinitializationchartsvar.length;x++){
                if(addinitializationchartsvar[x].CHART_NAME == addcharts){
                    addinitializationmetricvar = addinitializationchartsvar[x].mdMetricList;
                    if(addinitializationmetricvar.length>0){
                        for(var y=0;y<addinitializationmetricvar.length;y++){
                            rowdata = rowdata + "<option value="+addinitializationmetricvar[y].id+">"+addinitializationmetricvar[y].metric_name+"</option>";
                        }
                    }
                    $("#addmetric").empty().append(rowdata);
                }
            }
            //指标下拉列表选择事件
            addmetricchange();
        }

        //指标下拉列表选择事件
        function addmetricchange(){
            $("#addmetric").off("change").on("change",function(){
                //初始化指标维度下拉列表
                addmetricattrinitialization();
            });
        }

        //初始化指标维度下拉列表
        function addmetricattrinitialization(){
            $("#addmetricattr").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var addmetric = $("#addmetric").val();
            for(var x=0;x<addinitializationmetricvar.length;x++){
                if(addinitializationmetricvar[x].id == addmetric){
                    var length = addinitializationmetricvar[x].metric_attr.length;
                    if(length>0){
                        for(var y=0;y<addinitializationmetricvar[x].metric_attr.length;y++){
                            rowdata = rowdata + "<option value="+addinitializationmetricvar[x].metric_attr[y]+">"+addinitializationmetricvar[x].metric_attr[y]+"</option>";
                        }
                    }
                }
            }
            $("#addmetricattr").empty().append(rowdata);
        }

        //修改按钮事件
        function modifyShow(){
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length != 1){
                layer.msg("修改请只选择一个告警规则!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                reset("modify_div");
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#modifymonitortarget1").empty().append(rowdata);
                $("#modifymonitortarget2").empty().append(rowdata);
                $("#modifymonitortarget3").empty().append(rowdata);
                $("#modifymonitortarget4").empty().append(rowdata);
                $("#modifycharts").empty().append(rowdata);
                $("#modifymetric").empty().append(rowdata);
                $("#modifymonitortarget2").css('display','none');
                $("#modifymonitortarget3").css('display','none');
                $("#modifymonitortarget4").css('display','none');
                $("#modifyalarmlevel").empty();
                //初始化模块
                modifymoduleinitialization();
                loadingwait();
                var alarmruleid = checkboxArray[0];
                var data = {rule_id : alarmruleid};
                $.ajaxSettings.async = false;
                $.getJSON("/view/class/system/alarmrulemanage/query?random=" + Math.random(),data, function(result) {
                    if(result.length>0){
                        var alarmrule = result[0];
                        $("#modifymodule").val(alarmrule.module);
                        $("#modifymodule").trigger("change");
                        $("#modifymonitortarget1").val(alarmrule.monitortarget1);
                        $("#modifymonitortarget1").trigger("change");
                        $("#modifymonitortarget2").val(alarmrule.dimension1);
                        $("#modifymonitortarget2").trigger("change");
                        $("#modifymonitortarget3").val(alarmrule.monitortarget3);
                        $("#modifymonitortarget3").trigger("change");
                        $("#modifymonitortarget4").val(alarmrule.dimension2);
                        $("#modifymonitortarget4").trigger("change");
                        $("#modifycharts").val(alarmrule.chart_name);
                        $("#modifycharts").trigger("change");
                        $("#modifymetric").val(alarmrule.metric_id);
                        $("#modifymetric").trigger("change");
                        $("#modifymetricattr").val(alarmrule.attr);
                        $("#modify_alarm_rule").val(alarmrule.alarm_rule);
                        $("#modify_report_rule").val(alarmrule.report_rule);
                        modifyalarmrule("modify", alarmrule.alarm_rule);
                        $("#modify_alarmmsg").val(alarmrule.alarmmsg);
                        $("#modify_alarm_type").val(alarmrule.alarm_type);
                        $("#modify_alarm_type").trigger("change");
                        var alarm_level = alarmrule.alarm_level;
                        var arr = alarm_level.split("#");
                        for(i=0;i<arr.length;i++){
                            $("#modify_alarm_level_"+(i+1)).val(arr[i]);
                        }
                        var alarm_modes = alarmrule.modes;
                        var modesarr = new Array();
                        if(alarm_modes != null){
                            modesarr = alarm_modes.split("#");
                        }
                        for(i=0;i<initializationalarmmodes.length;i++){
                            for(var j=0;j<initializationalarmmodes[i].mdAlarmModeList.length;j++){
                                for(z=0;z<modesarr.length;z++){
                                    if(modesarr[z]==initializationalarmmodes[i].mdAlarmModeList[j].modeid){
                                        $("#modify_modes_"+(i+1)).val(modesarr[z]);
                                    }
                                }
                            }
                        }
                        $("#modifymodule").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifymonitortarget1").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifymonitortarget2").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifymonitortarget3").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifymonitortarget4").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifycharts").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifymetric").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        $("#modifymetricattr").attr("disabled","disabled").css("background-color","#EEEEEE;");
                        showLayer("modify_div",'修改告警规则');
                    }
                });
            }
        }

        //初始化模块
        function modifymoduleinitialization(){
            $("#modifymodule").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            for(var i=0;i<modulelistvar.length;i++){
                rowdata = rowdata + "<option value="+modulelistvar[i].id+">"+modulelistvar[i].show_name+"</option>";
            }
            $("#modifymodule").empty().append(rowdata);
            //模块选择事件
            modifymodulechange();
        }

        //模块选择事件
        function modifymodulechange(){
            $("#modifymodule").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#modifymonitortarget1").empty().append(rowdata);
                $("#modifymonitortarget2").empty().append(rowdata);
                $("#modifymonitortarget3").empty().append(rowdata);
                $("#modifymonitortarget4").empty().append(rowdata);
                $("#modifymonitortarget2").css('display','none');
                $("#modifymonitortarget3").css('display','none');
                $("#modifymonitortarget4").css('display','none');
                $("#modifycharts").empty().append(rowdata);
                $("#modifymetric").empty().append(rowdata);
                var id = $("#modifymodule").val();
                if(id!=null && id!=""){
                    //初始化监控目标一级下拉列表
                    modifymonitortargetoneinitialization();
                }
            });
        }

        //初始化监控目标一级下拉列表
        function modifymonitortargetoneinitialization(){
            $("#modifymonitortarget1").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#modifymodule").val();
            var data= {id:id};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetonelevellist?random=" + Math.random(),data, function(result) {
                modifymonitortargetonelevellist = result;
                for(var i=0;i<modifymonitortargetonelevellist.length;i++){
                    rowdata = rowdata + "<option value="+modifymonitortargetonelevellist[i].id+">"+modifymonitortargetonelevellist[i].name+"</option>";
                }
                $("#modifymonitortarget1").empty().append(rowdata);
            });
            //监控目标一级下拉列表选择事件
            modifymonitortargetonechange();
        }

        //监控目标一级下拉列表选择事件
        function modifymonitortargetonechange(){
            $("#modifymonitortarget1").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#modifymonitortarget2").empty().append(rowdata);
                $("#modifymonitortarget3").empty().append(rowdata);
                $("#modifymonitortarget4").empty().append(rowdata);
                $("#modifymonitortarget2").css('display','none');
                $("#modifymonitortarget3").css('display','none');
                $("#modifymonitortarget4").css('display','none');
                var id = $("#modifymonitortarget1").val();
                if(id!=null && id!=""){
                    //初始化监控目标二级下拉列表
                    modifymonitortargettwoinitialization();
                    //初始化监控目标三级下拉列表
                    modifymonitortargetthreeinitialization();
                }
                //初始化图表下拉列表
                modifychartsinitialization();
            });
        }

        //初始化监控目标二级下拉列表
        function modifymonitortargettwoinitialization(){
            $("#modifymonitortarget2").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#modifymonitortarget1").val();
            for(var i=0;i<modifymonitortargetonelevellist.length;i++){
                if(modifymonitortargetonelevellist[i].id == id){
                    var mdMenu = modifymonitortargetonelevellist[i].mdMenu;
                    var type = modifymonitortargetonelevellist[i].type;
                }
            }
            var data= {name:mdMenu.name,dynamic:mdMenu.dynamic,type:type};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargettwolevellist?random=" + Math.random(),data, function(result) {
                modifymonitortargettwolevellist = result;
                if(modifymonitortargettwolevellist.length>0){
                    $("#modifymonitortarget2").css('display','block');
                }else{
                    $("#modifymonitortarget2").css('display','none');
                }
                for(var i=0;i<modifymonitortargettwolevellist.length;i++){
                    rowdata = rowdata + "<option value="+modifymonitortargettwolevellist[i].id+">"+modifymonitortargettwolevellist[i].name+"</option>";
                }
                $("#modifymonitortarget2").empty().append(rowdata);
            });
            //监控目标二级下拉列表选择事件
            modifymonitortargettwochange();
        }

        //监控目标二级下拉列表选择事件
        function modifymonitortargettwochange(){
            $("#modifymonitortarget2").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#modifymonitortarget4").empty().append(rowdata);
                $("#modifymonitortarget4").css('display','none');
                var id = $("#modifymonitortarget2").val();
                if(id!=null && id!=""){
                    //初始化监控目标四级下拉列表
                    modifymonitortargetfourinitialization();
                }
                //初始化图表下拉列表
                modifychartsinitialization();
            });
        }

        //初始化监控目标三级下拉列表
        function modifymonitortargetthreeinitialization(){
            $("#modifymonitortarget3").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var id = $("#modifymonitortarget1").val();
            var data= {id:id};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetthreelevellist?random=" + Math.random(),data, function(result) {
                modifymonitortargetthreelevellist = result;
                if(modifymonitortargetthreelevellist.length>0){
                    $("#modifymonitortarget3").css('display','block');
                }else{
                    $("#modifymonitortarget3").css('display','none');
                }
                for(var i=0;i<modifymonitortargetthreelevellist.length;i++){
                    rowdata = rowdata + "<option value="+modifymonitortargetthreelevellist[i].id+">"+modifymonitortargetthreelevellist[i].name+"</option>";
                }
                $("#modifymonitortarget3").empty().append(rowdata);
            });
            //监控目标三级下拉列表选择事件
            modifymonitortargetthreechange();
        }

        //监控目标三级下拉列表选择事件
        function modifymonitortargetthreechange(){
            $("#modifymonitortarget3").off("change").on("change",function(){
                var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
                $("#modifymonitortarget4").empty().append(rowdata);
                $("#modifymonitortarget4").css('display','none');
                var id = $("#modifymonitortarget3").val();
                if(id!=null && id!=""){
                    //初始化监控目标四级下拉列表
                    modifymonitortargetfourinitialization();
                }
                //初始化图表下拉列表
                modifychartsinitialization();
            });
        }

        //初始化监控目标四级下拉列表
        function modifymonitortargetfourinitialization(){
            $("#modifymonitortarget4").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            var modifymonitortarget1_id = $("#modifymonitortarget1").val();
            for(var i=0;i<modifymonitortargetonelevellist.length;i++){
                if(modifymonitortargetonelevellist[i].id == modifymonitortarget1_id){
                    var monitorTargetOneDynamic = modifymonitortargetonelevellist[i].mdMenu.dynamic;
                    var monitorTargetOneType = modifymonitortargetonelevellist[i].type;
                }
            }
            var modifymonitortarget2_id = $("#modifymonitortarget2").val();
            for(var i=0;i<modifymonitortargettwolevellist.length;i++){
                if(modifymonitortargettwolevellist[i].id == modifymonitortarget2_id){
                    var monitorTargetTwoId = modifymonitortargettwolevellist[i].id;
                }
            }
            var modifymonitortarget3_id = $("#modifymonitortarget3").val();
            if(modifymonitortarget3_id != null && modifymonitortarget3_id != ""){
                for(var i=0;i<modifymonitortargetthreelevellist.length;i++){
                    if(modifymonitortargetthreelevellist[i].id == modifymonitortarget3_id){
                        var monitorTargetThreeDynamic = modifymonitortargetthreelevellist[i].mdMenu.dynamic;
                        var monitorTargetThreeName = modifymonitortargetthreelevellist[i].mdMenu.name;
                        var monitorTargetThreeType = modifymonitortargetthreelevellist[i].type;
                    }
                }
                var data= {monitorTargetOneDynamic:monitorTargetOneDynamic,
                    monitorTargetOneType:monitorTargetOneType,
                    monitorTargetTwoId:monitorTargetTwoId,
                    monitorTargetThreeDynamic:monitorTargetThreeDynamic,
                    monitorTargetThreeName:monitorTargetThreeName,
                    monitorTargetThreeType:monitorTargetThreeType};
                $.ajaxSettings.async = false;
                $.getJSON("/view/class/system/alarmrulemanage/query/monitortargetfourlevellist?random=" + Math.random(),data, function(result) {
                    modifymonitortargetfourlevellist = result;
                    if(modifymonitortargetfourlevellist.length>0){
                        $("#modifymonitortarget4").css('display','block');
                    }else{
                        $("#modifymonitortarget4").css('display','none');
                    }
                    for(var i=0;i<modifymonitortargetfourlevellist.length;i++){
                        rowdata = rowdata + "<option value="+modifymonitortargetfourlevellist[i].id+">"+modifymonitortargetfourlevellist[i].name+"</option>";
                    }
                    $("#modifymonitortarget4").empty().append(rowdata);
                });
                //监控目标四级下拉列表选择事件
                modifymonitortargetfourchange();
            }else {
                $("#modifymonitortarget4").empty().append(rowdata);
            }
        }

        //监控目标四级下拉列表选择事件
        function modifymonitortargetfourchange(){
            $("#modifymonitortarget4").off("change").on("change",function(){
                //初始化图表下拉列表
                modifychartsinitialization();
            });
        }

        //初始化图表下拉列表
        function modifychartsinitialization(){
            $("#modifycharts").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            $("#modifymetric").empty().append(rowdata);
            $("#modifymetricattr").empty().append(rowdata);
            if(modifymonitortargetonelevellist != null){
                for(var i=0;i<modifymonitortargetonelevellist.length;i++){
                    if(modifymonitortargetonelevellist[i].id == $("#modifymonitortarget1").val()){
                        var monitorTargetOneUrl = modifymonitortargetonelevellist[i].mdMenu.url;
                        var monitorTargetOneType = modifymonitortargetonelevellist[i].type;
                        var monitorTargetOneName = modifymonitortargetonelevellist[i].name;
                    }
                }
            }
            if(modifymonitortargetthreelevellist != null){
                for(var i=0;i<modifymonitortargetthreelevellist.length;i++){
                    if(modifymonitortargetthreelevellist[i].id == $("#modifymonitortarget3").val()){
                        var monitorTargetThreeUrl = modifymonitortargetthreelevellist[i].mdMenu.url;
                        var monitorTargetThreeType = modifymonitortargetthreelevellist[i].type;
                        var monitorTargetThreeName = modifymonitortargetthreelevellist[i].name;
                    }
                }
            }
            var attr1 = $("#modifymonitortarget2").val();
            var attr2 = $("#modifymonitortarget4").val();
            var data= {monitorTargetOneUrl:monitorTargetOneUrl,
                monitorTargetOneType:monitorTargetOneType,
                monitorTargetOneName:monitorTargetOneName,
                monitorTargetThreeUrl:monitorTargetThreeUrl,
                monitorTargetThreeType:monitorTargetThreeType,
                monitorTargetThreeName:monitorTargetThreeName,
                attr1:attr1,
                attr2:attr2};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/chartslist?random=" + Math.random(),data, function(result) {
                modifyinitializationchartsvar = result;
                if(modifyinitializationchartsvar.length>0){
                    for(var x=0;x<modifyinitializationchartsvar.length;x++){
                        rowdata = rowdata + "<option value="+modifyinitializationchartsvar[x].CHART_NAME+">"+modifyinitializationchartsvar[x].CHART_TITLE+"</option>";
                    }
                }
                $("#modifycharts").empty().append(rowdata);
            });
            //图表下拉列表选择事件
            modifychartschange();
        }

        //图表下拉列表选择事件
        function modifychartschange(){
            $("#modifycharts").off("change").on("change",function(){
                //初始化指标下拉列表
                modifymetricinitialization();
            });
        }

        //初始化指标下拉列表
        function modifymetricinitialization(){
            $("#modifymetric").empty();
            var rowdata = "<option value=\"\" selected=\"true\" >请选择</option>";
            if(modifymonitortargetonelevellist != null){
                for(var i=0;i<modifymonitortargetonelevellist.length;i++){
                    if(modifymonitortargetonelevellist[i].id == $("#modifymonitortarget1").val()){
                        var monitorTargetOneUrl = modifymonitortargetonelevellist[i].mdMenu.url;
                        var monitorTargetOneType = modifymonitortargetonelevellist[i].type;
                        var monitorTargetOneName = modifymonitortargetonelevellist[i].name;
                    }
                }
            }
            if(modifymonitortargetthreelevellist != null){
                for(var i=0;i<modifymonitortargetthreelevellist.length;i++){
                    if(modifymonitortargetthreelevellist[i].id == $("#modifymonitortarget3").val()){
                        var monitorTargetThreeUrl = modifymonitortargetthreelevellist[i].mdMenu.url;
                        var monitorTargetThreeType = modifymonitortargetthreelevellist[i].type;
                        var monitorTargetThreeName = modifymonitortargetthreelevellist[i].name;
                    }
                }
            }
            var attr1 = $("#modifymonitortarget2").val();
            var attr2 = $("#modifymonitortarget4").val();
            var data= {monitorTargetOneUrl:monitorTargetOneUrl,
                monitorTargetOneType:monitorTargetOneType,
                monitorTargetOneName:monitorTargetOneName,
                monitorTargetThreeUrl:monitorTargetThreeUrl,
                monitorTargetThreeType:monitorTargetThreeType,
                monitorTargetThreeName:monitorTargetThreeName,
                attr1:attr1,
                attr2:attr2};
            $.ajaxSettings.async = false;
            $.getJSON("/view/class/system/alarmrulemanage/query/metriclist?random=" + Math.random(),data, function(result) {
                modifyinitializationmetricvar = result;
                if(modifyinitializationmetricvar.length>0){
                    for(var x=0;x<modifyinitializationmetricvar.length;x++){
                        rowdata = rowdata + "<option value="+modifyinitializationmetricvar[x].id+">"+modifyinitializationmetricvar[x].metric_name+"</option>";
                    }
                }
                $("#modifymetric").empty().append(rowdata);
            });
        }

        //详情按钮事件
        function detailShow(alarmruleid){
            reset("detail_div");
            loadingwait();
            $.ajax({
                type: "post",
                url: "/view/class/system/alarmrulemanage/query?rule_id="+alarmruleid+"&random=" + Math.random(),
                cache: false,
                async: false,
                success: function (result) {
                    if(result.length>0){
                        var alarmrule = result[0];
                        $("#detail_module_name").val(alarmrule.module_name);
                        $("#detail_charts_title").val(alarmrule.chart_title);
                        $("#detail_metric_name").val(alarmrule.metric_name);
                        $("#detail_attr").val(alarmrule.attr);
                        $("#detail_alarm_level_name").val(alarmrule.alarm_level_name);
                        $("#detail_alarm_rule").val(alarmrule.alarm_rule);
                        $("#detail_modes_name").val(alarmrule.modes_name);
                        $("#detail_alarmmsg").val(alarmrule.alarmmsg);
                        $("#detail_dimension1_name").val(alarmrule.dimension1_name);
                        $("#detail_dimension2_name").val(alarmrule.dimension2_name);
                        $("#detail_dimension_type_name").val(alarmrule.dimension_type_name);
                        $("#detail_report_rule").val(alarmrule.report_rule);
                        $("#detail_alarm_type").val(alarmrule.alarm_type_name);
                        showLayerDetail("detail_div",'告警规则详情');
                    }
                }
            });
        }

        //修改确认
        function modifyInfo(){
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length != 1){
                layer.msg("修改请只选择一个告警规则!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                var alarmruleid = checkboxArray[0];
                var modifymodule = $("#modifymodule option:selected").attr("value");
                var modifymonitortarget1 = $("#modifymonitortarget1 option:selected").attr("value");
                var modifymonitortarget2 = $("#modifymonitortarget2 option:selected").attr("value");
                var modifymonitortarget3 = $("#modifymonitortarget3 option:selected").attr("value");
                var modifymonitortarget4 = $("#modifymonitortarget4 option:selected").attr("value");
                var modifycharts = $("#modifycharts option:selected").attr("value");
                var modifymetric = $("#modifymetric option:selected").attr("value");
                var modifymetricattr = $("#modifymetricattr option:selected").attr("value");
                var modify_alarm_rule = stringutil.Trim($("#modify_alarm_rule").val());
                var modify_alarmmsg = stringutil.Trim($("#modify_alarmmsg").val());
                var report_rule = stringutil.Trim($("#modify_report_rule").val());
                var modify_alarm_type = $("#modify_alarm_type option:selected").attr("value");
                if(stringutil.checkString("modify_alarm_rule",modify_alarm_rule,"告警规则不能为空!")||
                    stringutil.checkString('modify_alarm_rule',modify_alarm_rule,"告警规则不能超过200位!",200) ||
                    checkAlarmInterval('告警区间不得大于3','modify','alarm_rule',modify_alarm_rule) ||
                    stringutil.checkString('modify_alarmmsg',modify_alarmmsg,"告警信息不能为空!") ||
                    stringutil.checkString('modify_alarmmsg',modify_alarmmsg,"告警信息不能超过100位!",100) ||
                    checkAlarmInterval('告警区间不得大于3','modify','alarmmsg',modify_alarm_rule,modify_alarmmsg)){
                    return;
                }
                if(stringutil.checkString("modify_report_rule",report_rule,"上报规则不能为空!")||
                    stringutil.checkString('modify_report_rule',report_rule,"上报规则不能超过100位!",100) ||
                    checkReportInterval("modify","report_rule",report_rule)){
                    return;
                }
                var arr = new Array();
                if($("#modify_alarm_level_1").val() != null){
                    arr.push($("#modify_alarm_level_1").val());
                }
                if($("#modify_alarm_level_2").val() != null){
                    arr.push($("#modify_alarm_level_2").val());
                }
                if($("#modify_alarm_level_3").val() != null){
                    arr.push($("#modify_alarm_level_3").val());
                }
                var modify_alarm_level = splicingDelimiter(arr, "#");
                var modesarr = new Array();
                for(i=0;i<initializationalarmmodes.length;i++){
                    var modes = $("#modify_modes_"+(i+1)).val();
                    if(modes != null && modes != ""){
                        modesarr.push(modes);
                    }
                }
                var modify_modes = splicingDelimiter(modesarr, "#");
                for(var i=0;i<modifymonitortargetonelevellist.length;i++){
                    if(modifymonitortargetonelevellist[i].id == $("#modifymonitortarget1").val()){
                        var monitorTargetOneUrl = modifymonitortargetonelevellist[i].mdMenu.url;
                        var monitorTargetOneMdMenuName = modifymonitortargetonelevellist[i].mdMenu.name;
                        var monitorTargetOneDimensionType = modifymonitortargetonelevellist[i].dimensiontype;
                        var monitorTargetOneType = modifymonitortargetonelevellist[i].type;
                        var monitorTargetOneName = modifymonitortargetonelevellist[i].name;
                    }
                }
                for(var i=0;i<modifymonitortargetthreelevellist.length;i++){
                    if(modifymonitortargetthreelevellist[i].id == $("#modifymonitortarget3").val()){
                        var monitorTargetThreeUrl = modifymonitortargetthreelevellist[i].mdMenu.url;
                        var monitorTargetThreeMdMenuName = modifymonitortargetthreelevellist[i].mdMenu.name;
                        var monitorTargetThreeDimensionType = modifymonitortargetthreelevellist[i].dimensiontype;
                        var monitorTargetThreeType = modifymonitortargetthreelevellist[i].type;
                        var monitorTargetThreeName = modifymonitortargetthreelevellist[i].name;
                    }
                }
                var attr1 = $("#modifymonitortarget2").val();
                var attr2 = $("#modifymonitortarget4").val();
                loadingwait();
                var data= {monitorTargetOneUrl:monitorTargetOneUrl,
                    monitorTargetOneType:monitorTargetOneType,
                    monitorTargetOneName:monitorTargetOneName,
                    monitorTargetOneMdMenuName:monitorTargetOneMdMenuName,
                    monitorTargetOneDimensionType:monitorTargetOneDimensionType,
                    monitorTargetThreeUrl:monitorTargetThreeUrl,
                    monitorTargetThreeType:monitorTargetThreeType,
                    monitorTargetThreeName:monitorTargetThreeName,
                    monitorTargetThreeMdMenuName:monitorTargetThreeMdMenuName,
                    monitorTargetThreeDimensionType:monitorTargetThreeDimensionType,
                    attr1:attr1,
                    attr2:attr2,
                    chart_name:modifycharts,
                    report_rule:report_rule,
                    alarm_type:modify_alarm_type,
                    rule_id:alarmruleid,metric_id:modifymetric,attr:modifymetricattr,alarm_rule:modify_alarm_rule,alarm_level:modify_alarm_level,modes:modify_modes,alarmmsg:modify_alarmmsg};
                $.getJSON("/view/class/system/alarmrulemanage/modify?random=" + Math.random(),data, function(result) {
                    layer.closeAll();
                    layerResultAndReload(result);
                });
            }
        }

        //新增确认
        function addInfo(){
            var addmodule = $("#addmodule option:selected").attr("value");
            var addmonitortarget1 = $("#addmonitortarget1 option:selected").attr("value");
            var addmonitortarget2 = $("#addmonitortarget2 option:selected").attr("value");
            var addmonitortarget3 = $("#addmonitortarget3 option:selected").attr("value");
            var addmonitortarget4 = $("#addmonitortarget4 option:selected").attr("value");
            var addcharts = $("#addcharts option:selected").attr("value");
            var addmetric = $("#addmetric option:selected").attr("value");
            var addmetricattr = $("#addmetricattr option:selected").text();
            var add_alarm_rule = stringutil.Trim($("#add_alarm_rule").val());
            var add_alarmmsg = stringutil.Trim($("#add_alarmmsg").val());
            var report_rule = stringutil.Trim($("#add_report_rule").val());
            var add_alarm_type = $("#add_alarm_type option:selected").attr("value");
            if(addmetricattr == '请选择'){
                addmetricattr = '';
            }
            if((addmodule != null && stringutil.checkString("addmodule",addmodule,"请选择模块!")) ||
                (addmonitortarget1 != null && stringutil.checkString('addmonitortarget1',addmonitortarget1,"请选择监控目标!"))){
                return;
            }
            for(var i=0;i<addmonitortargetonelevellist.length;i++){
                if(addmonitortargetonelevellist[i].id == $("#addmonitortarget1").val()){
                    var monitorTargetOneUrl = addmonitortargetonelevellist[i].mdMenu.url;
                    var monitorTargetOneMdMenuName = addmonitortargetonelevellist[i].mdMenu.name;
                    var monitorTargetOneDimensionType = addmonitortargetonelevellist[i].dimensiontype;
                    var monitorTargetOneType = addmonitortargetonelevellist[i].type;
                    var monitorTargetOneName = addmonitortargetonelevellist[i].name;
                }
            }
            for(var i=0;i<addmonitortargetthreelevellist.length;i++){
                if(addmonitortargetthreelevellist[i].id == $("#addmonitortarget3").val()){
                    var monitorTargetThreeUrl = addmonitortargetthreelevellist[i].mdMenu.url;
                    var monitorTargetThreeMdMenuName = addmonitortargetthreelevellist[i].mdMenu.name;
                    var monitorTargetThreeDimensionType = addmonitortargetthreelevellist[i].dimensiontype;
                    var monitorTargetThreeType = addmonitortargetthreelevellist[i].type;
                    var monitorTargetThreeName = addmonitortargetthreelevellist[i].name;
                }
            }
            var attr1 = $("#addmonitortarget2").val();
            var attr2 = $("#addmonitortarget4").val();
            if(monitorTargetOneType == "2"){
                if(attr1 == null || attr1 == ""){
                    if(stringutil.checkString('addmonitortarget2',addmonitortarget2,"请选择监控目标!")){
                        return;
                    }
                }
            }
            if(monitorTargetThreeType == "2"){
                if(attr2 == null || attr2 == ""){
                    if(stringutil.checkString('addmonitortarget4',addmonitortarget4,"请选择监控目标!")){
                        return;
                    }
                }
            }
            if((addcharts != null && stringutil.checkString('addcharts',addcharts,"请选择图表!")) ||
                (addmetric != null && stringutil.checkString('addmetric',addmetric,"请选择指标!")) ||
                stringutil.checkString("add_alarm_rule",add_alarm_rule,"告警规则不能为空!")||
                stringutil.checkString('add_alarm_rule',add_alarm_rule,"告警规则不能超过200位!",200) ||
                checkAlarmInterval('告警区间不得大于3','add','alarm_rule',add_alarm_rule) ||
                stringutil.checkString('add_alarmmsg',add_alarmmsg,"告警信息不能为空!") ||
                stringutil.checkString('add_alarmmsg',add_alarmmsg,"告警信息不能超过100位!",100) ||
                checkAlarmInterval('告警区间不得大于3','add','alarmmsg',add_alarm_rule,add_alarmmsg)){
                return;
            }
            if(stringutil.checkString("add_report_rule",report_rule,"上报规则不能为空!")||
                stringutil.checkString('add_report_rule',report_rule,"上报规则不能超过100位!",100) ||
                checkReportInterval("add","report_rule",report_rule)){
                return;
            }
            var arr = new Array();
            if($("#add_alarm_level_1").val() != null){
                arr.push($("#add_alarm_level_1").val());
            }
            if($("#add_alarm_level_2").val() != null){
                arr.push($("#add_alarm_level_2").val());
            }
            if($("#add_alarm_level_3").val() != null){
                arr.push($("#add_alarm_level_3").val());
            }
            var add_alarm_level = splicingDelimiter(arr, "#");
            var modesarr = new Array();
            for(i=0;i<initializationalarmmodes.length;i++){
                var modes = $("#add_modes_"+(i+1)).val();
                if(modes != null && modes != ""){
                    modesarr.push(modes);
                }
            }
            var add_modes = splicingDelimiter(modesarr, "#");
            var data= {monitorTargetOneUrl:monitorTargetOneUrl,
                monitorTargetOneType:monitorTargetOneType,
                monitorTargetOneName:monitorTargetOneName,
                monitorTargetOneMdMenuName:monitorTargetOneMdMenuName,
                monitorTargetOneDimensionType:monitorTargetOneDimensionType,
                monitorTargetThreeUrl:monitorTargetThreeUrl,
                monitorTargetThreeType:monitorTargetThreeType,
                monitorTargetThreeName:monitorTargetThreeName,
                monitorTargetThreeMdMenuName:monitorTargetThreeMdMenuName,
                monitorTargetThreeDimensionType:monitorTargetThreeDimensionType,
                attr1:attr1,
                attr2:attr2,
                chart_name:addcharts,
                metric_id:addmetric,
                attr:addmetricattr,
                alarm_rule:add_alarm_rule,
                alarm_level:add_alarm_level,
                modes:add_modes,
                report_rule:report_rule,
                alarm_type:add_alarm_type,
                alarmmsg:add_alarmmsg,
                mdMenuId:addmodule};
            //告警规则是否重复事件
            $.ajax({
                type: "post",
                url: "/view/class/system/alarmrulemanage/query?random=" + Math.random(),
                cache: false,
                async: false,
                data: data,
                success: function (result) {
                	//var length = result.length;
                	var length = 0; //去除告警规则重复校验
                    if(length > 0){
                        $("#addmodule").focus();
                        layer.tips("告警规则不能重复", '#addmodule',{ tips: [2, '#EE1A23'] });
                        return;
                    }else{
                        loadingwait();
                        var data= {monitorTargetOneUrl:monitorTargetOneUrl,
                            monitorTargetOneType:monitorTargetOneType,
                            monitorTargetOneName:monitorTargetOneName,
                            monitorTargetOneMdMenuName:monitorTargetOneMdMenuName,
                            monitorTargetOneDimensionType:monitorTargetOneDimensionType,
                            monitorTargetThreeUrl:monitorTargetThreeUrl,
                            monitorTargetThreeType:monitorTargetThreeType,
                            monitorTargetThreeName:monitorTargetThreeName,
                            monitorTargetThreeMdMenuName:monitorTargetThreeMdMenuName,
                            monitorTargetThreeDimensionType:monitorTargetThreeDimensionType,
                            attr1:attr1,
                            attr2:attr2,
                            chart_name:addcharts,
                            metric_id:addmetric,
                            attr:addmetricattr,
                            alarm_rule:add_alarm_rule,
                            report_rule:report_rule,
                            alarm_level:add_alarm_level,
                            modes:add_modes,
                            alarm_type:add_alarm_type,
                            alarmmsg:add_alarmmsg};
                        $.getJSON("/view/class/system/alarmrulemanage/add?random=" + Math.random(),data, function(result) {
                            layer.closeAll();
                            addLayerResultAndReload(result);
                        });
                    }
                }
            });
        }

        //数组用分隔符拼接字符串
        function splicingDelimiter(arr,delimiter) {
            var s = "";
            if(arr.length>0){
                for(i=0;i<arr.length;i++){
                    s = s + delimiter + arr[i];
                }
                return s.substring(1);
            }else{
                return s;
            }
        }

        function addJumpShow(operateid) {
            layer.confirm('是否需要跳转到主机告警规则配置管理页面？', {
                closeBtn:0,
                title: '询问',
                btn: ['确认','取消'] //按钮
            },function(){
                window.location.href = "/view/class/system/hostalarmrulemanage?key=hostalarmrulemanage&operatetype=1&operateid="+operateid+"&random=" + Math.random();
            });
        }

        //查询按钮事件
        function queryOpt(){
            loadOptRecord();
        }

        //获取完整url
        function getWholeUrl(url,attr1,attr2){
            var wholeUrl;
            for(var i=0;i<initializationvar.length;i++){
                if(initializationvar[i].URL == url){
                    var isDynamicAttr1;
                    var isDynamicAttr2;
                    for(var j=0;j<initializationvar[i].dynamicList.length;j++){
                        var attr1id = initializationvar[i].dynamicList[j].id;
                        if(attr1id == attr1){
                            isDynamicAttr1 = initializationvar[i].dynamicList[j].isDynamic;
                            for(var k=0;k<initializationvar[i].dynamicList[j].list.length;k++){
                                var attr2id = initializationvar[i].dynamicList[j].list[k].id;
                                if(attr2id == attr2){
                                    isDynamicAttr2 = initializationvar[i].dynamicList[j].list[k].isDynamic;
                                    break;
                                }
                            }
                        }
                    }
                    if(isDynamicAttr1){
                        if(isDynamicAttr2){
                            wholeUrl = url + "--/" + attr1 + "/" + attr2;
                        }else{
                            wholeUrl = url + "--/" + attr1;
                        }
                    }else{
                        wholeUrl = url;
                    }
                }
            }
            return wholeUrl;
        }

        //加载查询内容
        function loadOptRecord(){
            //分页显示的初始化数据
            var pagecount=0;
            var page_count = 10;
            $("#checkboxAll").prop("checked", false);
            checkbox.cleanArray();
            var metric = $("#querymetric option:selected").attr("value");
            if(monitortargetonelevellist != null){
                for(var i=0;i<monitortargetonelevellist.length;i++){
                    if(monitortargetonelevellist[i].id == $("#querymonitortarget1").val()){
                        var monitorTargetOneUrl = monitortargetonelevellist[i].mdMenu.url;
                        var monitorTargetOneType = monitortargetonelevellist[i].type;
                        var monitorTargetOneName = monitortargetonelevellist[i].name;
                    }
                }
            }
            if(monitortargetthreelevellist != null){
                for(var i=0;i<monitortargetthreelevellist.length;i++){
                    if(monitortargetthreelevellist[i].id == $("#querymonitortarget3").val()){
                        var monitorTargetThreeUrl = monitortargetthreelevellist[i].mdMenu.url;
                        var monitorTargetThreeType = monitortargetthreelevellist[i].type;
                        var monitorTargetThreeName = monitortargetthreelevellist[i].name;
                    }
                }
            }
            var attr1 = $("#querymonitortarget2").val();
            var attr2 = $("#querymonitortarget4").val();
            var querymodule = $("#querymodule").val();
            loadingwait();
            var data = {monitorTargetOneUrl:monitorTargetOneUrl,
                monitorTargetOneType:monitorTargetOneType,
                monitorTargetOneName:monitorTargetOneName,
                monitorTargetThreeUrl:monitorTargetThreeUrl,
                monitorTargetThreeType:monitorTargetThreeType,
                monitorTargetThreeName:monitorTargetThreeName,
                attr1:attr1,
                attr2:attr2,
                metric_id:metric,
                mdMenuId:querymodule};
            $.getJSON("/view/class/system/alarmrulemanage/query?random=" + Math.random(),data, function(result) {
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
                        resizewh.resizeBodyH($("#mainalarmrule"));
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
            for(var i=startnum;i<=endnum;i++){
                var rowninfo = data[i-1];
                var id = rowninfo.rule_id;
                var checked = "";
                if(checkbox.isExitArray(id)){
                    checked = "checked=\"checked\"";
                    k++;
                }
                rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.rule_id+"\" id=\""+rowninfo.rule_id+"\" "+checked+" />"+"</td>"
                    + "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.module_name)+"\">"+stringutil.isNull(rowninfo.module_name)+"</td><td title=\""+stringutil.isNull(rowninfo.dimension1_name)+"\">"+stringutil.isNull(rowninfo.dimension1_name)
                    +"</td><td title=\""+stringutil.isNull(rowninfo.dimension2_name)+"\">"+stringutil.isNull(rowninfo.dimension2_name)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.dimension_type_name)+"\">"+stringutil.isNull(rowninfo.dimension_type_name)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.chart_title)+"\">"+stringutil.isNull(rowninfo.chart_title)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.metric_name)+"\">"+stringutil.isNull(rowninfo.metric_name)+"</td>"
                    +"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.alarm_level_name)+"\">"+stringutil.isNull(rowninfo.alarm_level_name)+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.alarm_rule)+"\">"
                    +stringutil.escapeHTML(stringutil.isNull(rowninfo.alarm_rule))+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.alarmmsg)+"\">"+stringutil.isNull(rowninfo.alarmmsg)+"</td><td>"
                    +"<a class=\"J_delete pr10\" href=\"#\" name='detail' id=\""+rowninfo.rule_id+"\">详情</a>"
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
            $("#alarmrulediv").empty().append(rowdata);
            $("[name=detail]").each(function(){
                $(this).on('click',function(){
                    detailShow($(this).attr('id'));
                });
            });
            checkbox.bindSingleCheckbox("alarmrulediv");
        }

        //删除操作
        function deleteShow() {
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length == 0){
                layer.msg("请选择一个告警规则!",{
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
                    var data = {ruleidArray:checkboxArray};
                    var url = "/view/class/system/alarmrulemanage/delete/?random=" + Math.random();
                    $.getJSON(url,data,function(result){
                        layer.close(layer_load);
                        loadOptRecord();
                        layer.alert(result.message);
                    })
                });
            }
        }

        //刷新操作
        function refreshShow() {
            var checkboxArray = checkbox.getReturnArray();
            if(checkboxArray.length == 0){
                layer.msg("请选择一个告警规则!",{
                    time:2000,
                    skin: 'layer_msg_color_alert'
                });
            }else{
                layer.confirm('是否确认刷新该批次数据？', {
                    closeBtn:0,
                    title: '询问',
                    btn: ['确认','取消'] //按钮
                },function(){
                    layer.closeAll();
                    loadingwait();
                    var data = {ruleidArray:checkboxArray};
                    var url = "/view/class/system/alarmrulemanage/refresh/?random=" + Math.random();
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
//        	addJumpShow(result.data);
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

        function showLayerDetail(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '620px', '450px' ],
                content : $("#"+divid)
            });
        }

        function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '620px', '450px' ],
                content : $("#"+divid)
            });
        }

        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });
        }

        //替换全部字符串中的特殊字符
        function replaceSpecial(newvalue,char,repchar){
            if(newvalue.indexOf(char) != -1){
                newvalue = newvalue.replace(char,repchar);
                newvalue = replaceSpecial(newvalue,char,repchar);
            }
            return newvalue;
        }

        function checkAlarmexpr(tips,type,id,value){
            var check = true;
            if(value.indexOf('newest')!=-1||value.indexOf('lastest')!=-1||value.indexOf('yescurrent')!=-1||value.indexOf('collect')!=-1||value.indexOf('alarm_lastval')!=-1){
                var newvalue = value.replace(new RegExp("newest","g"),parseInt(Math.random()*1000+1))
                    .replace(new RegExp("lastest","g"),parseInt(Math.random()*1000+1))
                    .replace(new RegExp("yescurrent","g"),parseInt(Math.random()*1000+1))
                    .replace(new RegExp("collect","g"),parseInt(Math.random()*1000+1))
                    .replace(new RegExp("alarm_lastval","g"),parseInt(Math.random()*1000+1))
                    .replace(new RegExp("==","g"),"+")
                    .replace(new RegExp("!=","g"),"+")
                    .replace(new RegExp("<=","g"),"+")
                    .replace(new RegExp(">=","g"),"+")
                    .replace(new RegExp("<","g"),"+")
                    .replace(new RegExp(">","g"),"+")
                    .replace(new RegExp("&&","g"),"+");
                newvalue = replaceSpecial(newvalue,"||","+");
                try  {
                    var r = eval(newvalue);
                    if(r==null||r==false||isNaN(parseInt(r))||newvalue.indexOf("|") != -1){
                        layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                        return check;
                    }
                }
                catch(exception) {
                    layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                    return check;}
                check = false;
            }else{
                layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                return check;
            }
            return check;
        }

        function checkAlarmInterval(tips,type,id,value,value2){
            var check = true;
            var arr=value.split('#');
            var n=arr.length-1;
            if(n>2){
                layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                return check;
            }else{
                if("alarm_rule" == id){
                    for(var i=0;i<arr.length;i++){
                        check = checkAlarmexpr('告警规则不合法',type,id,arr[i]);
                        if(check){
                            return check;
                        }
                    }
                }if("alarmmsg" == id){
                    var m=(value2.split('#')).length-1;
                    if(m!=n){
                        layer.tips('告警规则和信息的区间不一致', '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                        return check;
                    }
                }
                check = false;
            }
            return check;
        }

        function checkReportInterval(type,id,value){
            var check = true;
            var arr=value.split('#');

            for(var i=0;i<arr.length;i++){
                check = checkReportexpr('上报规则不合法',type,id,arr[i]);
                if(check){
                    return check;
                }
            }
            check = false;

            return check;
        }

        function checkReportexpr(tips,type,id,value){
            var check = true;
            if(value.indexOf('alarmNum')!=-1){
                var newvalue = value.replace(new RegExp("alarmNum","g"),parseInt(Math.random()*1000+1))
                    .replace(new RegExp("==","g"),"+")
                    .replace(new RegExp("!=","g"),"+")
                    .replace(new RegExp("<=","g"),"+")
                    .replace(new RegExp(">=","g"),"+")
                    .replace(new RegExp("<","g"),"+")
                    .replace(new RegExp(">","g"),"+")
                    .replace(new RegExp("%","g"),"+")
                    .replace(new RegExp("&&","g"),"+");
                newvalue = replaceSpecial(newvalue,"||","+");
                try  {
                    var r = eval(newvalue);
                    if(r==null||r==false||isNaN(parseInt(r))||newvalue.indexOf("|") != -1){
                        layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                        return check;
                    }
                }
                catch(exception) {
                    layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                    return check;}
                check = false;
            }else{
                layer.tips(tips, '#'+type+'_'+id,{ tips: [2, '#EE1A23'] });
                return check;
            }
            return check;
        }
    });