require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer': '/js/layer/layer',
        "laypage": "/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh','checkbox','stringutil' ],
    function($, layer, laypage, resizewh,checkbox,stringutil) {
        var layer_load;

        resizewh.resizeBodyH($("#mainarea"));
        butBindFunction();
        $("#checkboxAll").click(function() {
            var flag = $(this).prop('checked');
            checkbox.checkboxAll(flag,'checkboxAll','areadiv');
        });
        reset("mainarea");
        initChildrenMenu();
        loadingwait();
        loadOptRecord();

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainarea");
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
        $("#detail_ok").click(function() {
            layer.closeAll();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','areadiv');
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
        $("#areadiv :checkbox").click(function(){
            var flag = $(this).prop('checked');
            var value = $(this).val();
            checkbox.checkboxSingle(flag,value);
        });
    }

    // 新增按钮事件
    function addShow() {
        reset("add_div");
        $.getJSON("/view/class/system/soundalarmmanage/query/mdparamlist?type=21&random=" + Math.random(), function(result) {
            $("#add_soundduration").empty();
            $.each(result,function(i,data){
                $("#add_soundduration").append("<option value=\""+data.code+"\">"+data.description+"</option>");
            });
        });
        showLayer("add_div", '新增声音告警信息');
    }

    // 新增确认
    function addInfo() {
        var sound_name = stringutil.Trim($("#add_soundname").val());
        var sound_duration = stringutil.Trim($("#add_soundduration").val());
        var sound_music = stringutil.Trim($("#add_soundmusic").val());
        var description = stringutil.Trim($("#add_description").val());
        if(stringutil.checkString("add_soundname",sound_name,"声音告警名称不能为空!") 
        		|| stringutil.checkString("add_soundname",sound_name,"声音告警名称不能超过65位!",65) 
        		|| stringutil.checkString("add_soundduration",sound_duration,"声音持续时间不能为空!")
        		|| stringutil.checkString("add_soundmusic",sound_music,"音乐不能为空!")
        		|| stringutil.checkString("add_description",description,"描述不能超过100位!",100)){
            return;
        }
        // 校验声音告警名称是否重复
        $.ajax({
            type: "post",
            url: "/view/class/system/soundalarmmanage/query?random=" + Math.random(),
            cache: false,
            async: false, 
            data: {"sound_name":sound_name},
            success: function (result) {
            	var length = result.length;
            	var state = true;
            	for (var i=0;i<length;i++){
            		if(result[i].sound_name==sound_name){
        				state = false;
        			}
        		}
            	if(state){
            		loadingwait();
                    var data = {"sound_name":sound_name,"sound_duration":sound_duration,"sound_music":sound_music,"description":description};
        			$.getJSON("/view/class/system/soundalarmmanage/add?random=" + Math.random(),data, function(result) {
                        layer.closeAll();
                        layerResultAndReload(result);
                    });
            	}else{
            		$("#add_soundname").focus();
                    layer.tips("声音告警名称不能重复", '#add_soundname',{ tips: [2, '#EE1A23'] });
            		return;
            	}
            }
		});
    }
    
    // 详情按钮事件
    function detailShow(soundalarmid){
        reset("detail_div");
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/soundalarmmanage/query?id="+soundalarmid+"&random=" + Math.random(),
            cache: false,
            async: false, 
            success: function (result) {
            	if(result.length>0){
                    var soundalarm = result[0];
                    $("#detail_soundname").val(soundalarm.sound_name);
                    $("#detail_soundduration").val(soundalarm.sound_duration);
                    $("#detail_soundmusic").val(soundalarm.sound_music);
                    $("#detail_description").val(soundalarm.description);
                    showLayerDetail("detail_div",'声音告警详情');
                }
            }
		});
    }

    // 修改按钮事件
    function modifyShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个声音告警!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var soundalarmid = checkboxArray[0];
            reset("modify_div");
            loadingwait();
            var data = {"id":soundalarmid};
            $.ajax({
				type: "post",
				url: "/view/class/system/soundalarmmanage/query/mdparamlist?type=21&random=" + Math.random(),
				cache: false,
				async: false, 
				dataType: "json",
				success: function (result) {
					$("#modify_soundduration").empty();
                    $.each(result,function(i,data){
                        $("#modify_soundduration").append("<option value=\""+data.code+"\">"+data.description+"</option>");
                    });
				}
			});
            $.ajax({
                type: "post",
                url: "/view/class/system/soundalarmmanage/query?random=" + Math.random(),
                data: data,
                cache: false,
                async: false,
                dataType: "json",
                success: function (result) {
                    if(result.length>0){
                        var info = result[0];
                        $("#modify_soundname").val(info.sound_name);
                        $("#modify_soundduration option").each(function(){
							if($(this).attr('value')==info.sound_duration){
								$(this).prop("selected", "true");
							}
						});
                        $("#modify_soundmusic").val(info.sound_music);
                        $("#modify_description").val(info.description);
                        showLayer("modify_div",'修改声音告警信息');
                    }
                }
            });
        }
    }

    // 修改确认
    function modifyInfo() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length != 1){
            layer.msg("修改请只选择一个声音告警!",{
                time:2000,
                skin: 'layer_msg_color_alert'
            });
        }else{
            var soundalarmid = checkboxArray[0];
            var sound_name = stringutil.Trim($("#modify_soundname").val());
	        var sound_duration = stringutil.Trim($("#modify_soundduration").val());
	        var sound_music = stringutil.Trim($("#modify_soundmusic").val());
	        var description = stringutil.Trim($("#modify_description").val());
	        if(stringutil.checkString("modify_soundname",sound_name,"声音告警名称不能为空!") 
	        		|| stringutil.checkString("modify_soundname",sound_name,"声音告警名称不能超过65位!",65) 
	        		|| stringutil.checkString("modify_soundduration",sound_duration,"声音持续时间不能为空!")
	        		|| stringutil.checkString("modify_soundmusic",sound_music,"音乐不能为空!")
	        		|| stringutil.checkString("modify_description",description,"描述不能超过100位!",100)){
	            return;
	        }
            // 校验声音告警名称是否重复
            $.ajax({
                type: "post",
                url: "/view/class/system/soundalarmmanage/query?random=" + Math.random(),
                cache: false,
                async: false, 
                data: {'sound_name':sound_name},
                success: function (result) {
                	var length = result.length;
                	var state = true;
                	if(length == 0 ){
                		state = true;
                	}else{
                		for(var i=0; i<length;i++){
                			if(result[i].sound_name == sound_name){
                				if(result[i].id!=soundalarmid) {
                					state = false;
                    				break;
                				}
                    		}
                		}
                	}
                	if(state){
                		loadingwait();
                        var data = {"id":soundalarmid,"sound_name":sound_name,"sound_duration":sound_duration,"sound_music":sound_music,"description":description};
        				$.getJSON("/view/class/system/soundalarmmanage/modify?random=" + Math.random(),data, function(result) {
                            layer.closeAll();
                            layerResultAndReload(result);
                        });
                	}else{
                		$("#modify_soundname").focus();
                        layer.tips("声音告警名称不能重复", '#modify_soundname',{ tips: [2, '#EE1A23'] });
                		return;
                	}
                }
			});
        }
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

        var sound_name = stringutil.Trim($("#sound_name").val());
        var data = {'sound_name':sound_name};
        $.getJSON("/view/class/system/soundalarmmanage/query?random=" + Math.random(), data, function(result) {
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
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total==0){
                        $("#currnum").empty().text("0 ");
                    }
                    resizewh.resizeBodyH($("#mainarea"));
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
            var id = rowninfo.id;
            var checked = "";
            if(checkbox.isExitArray(id)){
                checked = "checked=\"checked\"";
                k++;
            }
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"
            		+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.sound_name)+"\">"+stringutil.isNull(rowninfo.sound_name)
            		+"</td><td>"+stringutil.isNull(rowninfo.sound_duration)
            		+"</td><td>"+stringutil.isNull(rowninfo.sound_music)
            		+"</td><td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.description)+"\">"+stringutil.isNull(rowninfo.description)+"</td><td>"
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
        $("#soundalarmdiv").empty().append(rowdata);
        $("[name=detail]").each(function(){
            $(this).on('click',function(){
            	detailShow($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("soundalarmdiv");
    }

    function deleteShow() {
        var checkboxArray = checkbox.getReturnArray();
        if(checkboxArray.length == 0){
            layer.msg("请选择一个声音告警信息!",{
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
                var data = {"soundalarmids":checkboxArray};
                var url = "/view/class/system/soundalarmmanage/delete?random=" + Math.random();
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
            area : [ '600px', '300px' ],
            content : $("#" + divid)
        });
    }
    
    function showLayerDetail(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '600px', '300px' ],
            content : $("#"+divid)
        });
    }

    function validName(type, value) {
        if (null == value || value == "") {
            $("#" + type + "_name").focus();
            layer.tips('声音告警名称不能为空!', '#' + type + '_name', {
                tips : [ 2, '#EE1A23' ]
            });
            return true;
        }
        return false;
    }
});