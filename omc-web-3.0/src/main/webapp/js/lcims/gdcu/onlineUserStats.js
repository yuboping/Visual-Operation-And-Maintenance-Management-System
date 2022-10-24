require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'laydate' : '/js/laydate/laydate',
        'layer':'/js/layer/layer',
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    }
});
require(['jquery','layer','laypage','resizewh','laydate','stringutil'],
        function($,layer,laypage,resizewh,laydate,stringutil) {
	    resizewh.resizeBodyH($("#mainonlineUserStats"));
	    //loadOptRecord();
	    $("#querybutton").click(function() {
	        if (!checkIp($("#brasip").val())) {
	            layer.tips('请输入正确格式的Brasip!', '#brasip',{ tips: [3, '#EE1A23'] });
	        } else {
	        	loadOptRecord();
	        }
	    });
	    
	    $("#resetbutton").click(function() {
            reset("mainonlineUserStats");
        });
//-----------------------------------------以下为自定义方法-------------------------------------------------//            
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
	    
	    function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
        }
	    
	    function loadOptRecord(){
	        var pagecount=0;
	        var page_count = 10;
	        var brasip = $("#brasip").val();
	        var brasTpye = $("#brasTpye").val();
	        loadingwait();
	        var data = {'brasip':brasip,'brasTpye':brasTpye};
	        $.getJSON("/gdmainttool/onlineBasUser?random=" + Math.random(),data, function(result){
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
	                    resizewh.resizeBodyH($("#mainonlineUserStats"));
	                },
	                groups: page_count //连续显示分页数
	            });
	        });
	        $.getJSON("/gdmainttool/onlineUserStats?random=" + Math.random(), function(result) {
	        	if(result.num != "" && result.num != null) {
	        		$("#onlineUserNum").html(result.num);
	        	}
            });
	    }
	    
	    function showTable(data,startnum,endnum){
	    	var rowdata = "";
            if(data==null||data.length==0){
            	rowdata = rowdata + "<tr><td colspan='6'>暂无数据</td></tr>";
            	$("#onlineUserStatsdiv").empty().append(rowdata);
            }else{
            	for(var i=startnum;i<=endnum;i++){
                    var rowninfo = data[i-1];
                    console.log(rowninfo);
                    if(rowninfo.areaname == null || rowninfo.areaname ==""){
                    	rowninfo.areaname = "未知区域";
                    }
                    if(rowninfo.brasTpye == null || rowninfo.brasTpye ==""){
                    	rowninfo.brasTpye = "- -";
                    }
                    if(rowninfo.businessName == "1"){
                    	rowninfo.businessName = "wlan业务";
                    }else {
                    	rowninfo.businessName = "宽带业务";
                    }
                    rowdata = rowdata + "<tr><td>"+rowninfo.brasIp+"</td><td>"
                    +rowninfo.areaname+"</td><td>"+rowninfo.brasTpye+"</td><td>"
                    +rowninfo.businessName+"</td><td>"+rowninfo.onlineNum+"</td></tr>";
                }
            	$("#onlineUserStatsdiv").empty().append(rowdata);
            }
	    }
	 
	    function checkIp(ip) {
	        if(null==ip || ""==ip){
	            return true;
	        }
	        var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	        return regex.test(ip);
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
                area : [ '500px', '395px' ],
                content : $("#"+divid)
            });
        }
});
