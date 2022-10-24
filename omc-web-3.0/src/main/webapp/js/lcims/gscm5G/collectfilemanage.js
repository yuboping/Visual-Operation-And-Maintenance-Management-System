require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'laydate' : '/js/laydate/laydate',
        'checkbox': '/js/lcims/tool/checkbox',
        'layer':'/js/layer/layer',
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});

require(['jquery','layer','laypage','resizewh','checkbox','laydate','stringutil'],
    function($,layer,laypage,resizewh,checkbox,laydate,stringutil) {
        var layer_load;
        
        resizewh.resizeBodyH($("#maincollect"));
        butBindFunction();
        $("#checkboxAll").click(function() {
        	var flag = $(this).prop('checked');
        	checkbox.checkboxAll(flag,'checkboxAll','collectdiv');
        });
        
        laydate.render({
            elem: '#collect_time' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,max: 0
            // ,value: new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
            ,format: 'yyyy-MM-dd'
//            ,value: new Date()
            ,value: '2020-06-01'
            ,trigger: 'click' //采用click弹出
        });
        
        reset("maincollect");
        initChildrenMenu();
        // loadingwait();
//         loadOptRecord();
        
//----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("maincollect");
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
        $("#contrast_ok").click(function() {
            layer.closeAll();
        });
        $("#contrast_ok").click(function() {
            layer.closeAll();
        });
        $("#contrastbutton").click(function() {
        	contrastShow();
        });
        //查询页面全选框事件
        checkbox.bindAllCheckbox('checkboxAll','collectdiv');
        
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
    	$("#collectdiv :checkbox").click(function(){  
        	var flag = $(this).prop('checked');
        	var value = $(this).val();
        	checkbox.checkboxSingle(flag,value);
        });
    }
    
    
    
  //下载按钮事件
    function firewallDownload(collectid){
    	location.href ="/view/class/system/collectfilemanage/download?random=" + Math.random();
    	layer.msg("采集状态下载",{
            time:2000,
            skin: 'layer_msg_color_success'
        });
    }
    
    function thirdpartyDownload(collectid){
    	location.href ="/view/class/system/collectfilemanage/download?random=" + Math.random();
    	layer.msg("输出状态下载",{
            time:2000,
            skin: 'layer_msg_color_success'
        });
    }
    
    function isNull(data){
    	if(data==null || data ==''){
    		return '';
    	}else
    		return data;
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
        var page_count = 10;
        $("#checkboxAll").prop("checked", false);
        checkbox.cleanArray();
        
        var host_ip = stringutil.Trim($("#host_ip").val());
        var collect_time = stringutil.Trim($("#collect_time").val());
        var data = {'host_ip':host_ip,'collect_time':collect_time};
        $.getJSON("/view/class/system/collectfilemanage/query?random=" + Math.random(),data, function(result) {
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
                    resizewh.resizeBodyH($("#maincollect"));
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
            rowdata = rowdata + "<tr><td>"+"<input type=\"checkbox\" name = \"checkbox\" value=\""+rowninfo.id+"\" id=\""+rowninfo.id+"\" "+checked+" />"+"</td>" +
            "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.firewall_log_name)+"\">"+stringutil.isNull(rowninfo.firewall_log_name)+"</td>" +
            "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.firewall_log_size)+"\">"+stringutil.isNull(rowninfo.firewall_log_size)+"</td>" +
            "<td class='over_ellipsis' style='max-width:70px' title=\""+stringutil.isNull(rowninfo.firewall_log_state)+"\">"+stringutil.isNull(rowninfo.firewall_log_state)+"</td>";
            if(rowninfo.firewall_log_download == "--"){
            	rowdata = rowdata + "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.firewall_log_download)+"\">"+stringutil.isNull(rowninfo.firewall_log_download)+"</td>"
            }else{
            	rowdata = rowdata + "<td><a class=\"J_delete pr10\" href=\"#\" name='firewall_log_download' id=\""+rowninfo.id+"\">"+rowninfo.firewall_log_download+"</a></td>"
            }
            rowdata = rowdata + "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.thirdparty_log_name)+"\">"+stringutil.isNull(rowninfo.thirdparty_log_name)+"</td>" +
            "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.thirdparty_log_size)+"\">"+stringutil.isNull(rowninfo.thirdparty_log_size)+"</td>" +
            "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.thirdparty_log_state)+"\">"+stringutil.isNull(rowninfo.thirdparty_log_state)+"</td>";
            if(rowninfo.firewall_log_download == "--"){
            	rowdata = rowdata + "<td class='over_ellipsis' style='max-width:120px' title=\""+stringutil.isNull(rowninfo.thirdparty_log_download)+"\">"+stringutil.isNull(rowninfo.thirdparty_log_download)+"</td>"
            }else{
            	rowdata = rowdata + "<td><a class=\"J_delete pr10\" href=\"#\" name='thirdparty_log_download' id=\""+rowninfo.id+"\">"+rowninfo.thirdparty_log_download+"</a></td>"
            };
        }
        //本页条数
        var ct = endnum-startnum+1;
        if(k==ct&&k>0){
        	//全选框选中
        	$("#checkboxAll").prop("checked", true);
        }else{
        	$("#checkboxAll").prop("checked", false);
        }
        $("#collectdiv").empty().append(rowdata);
        $("[name=firewall_log_download]").each(function(){
            $(this).on('click',function(){
            	firewallDownload($(this).attr('id'));
            });
        });
        $("[name=thirdparty_log_download]").each(function(){
            $(this).on('click',function(){
            	thirdpartyDownload($(this).attr('id'));
            });
        });
        checkbox.bindSingleCheckbox("collectdiv");
    }
    
    
    //稽核对比按钮事件
    function contrastShow(){
    	var host_ip = stringutil.Trim($("#host_ip").val());
        var collect_time = stringutil.Trim($("#collect_time").val());
        var data = {'host_ip':host_ip,'collect_time':collect_time};
        loadingwait();
        $.ajax({
            type: "post",
            url: "/view/class/system/collectfilemanage/query/contrast?random=" + Math.random(),
            cache: false,
            async: false, 
            data : data,
            success: function (result) {
            	if(result.length>0){
                    var mdcollectfilediff = result[0];
                    $("#contrast_collect_time").val(mdcollectfilediff.collect_time);
                    $("#contrast_host_ip").val(mdcollectfilediff.host_ip);
                    $("#contrast_firewall_log_num").val(mdcollectfilediff.firewall_log_num);
                    $("#contrast_thirdparty_log_num").val(mdcollectfilediff.thirdparty_log_num);
                    $("#contrast_diff_log_name").val(mdcollectfilediff.diff_log_name);
                    showLayerDetail("contrast_div",'稽核对比结果');
                }else{
                	layer.close(layer_load);
                	layer.msg("无稽核对比结果",{
                        time:2000,
                        skin: 'layer_msg_color_error'
                    });
                }
            }
         });
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
            area : [ '480px', '400px' ],
            content : $("#"+divid)
        });
    }
    
    function showLayer(divid,title) {
        layer.open({
            type : 1,
            title : title,
            closeBtn:0,
            area : [ '620px', '360px' ],
            content : $("#"+divid)
        });
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
});