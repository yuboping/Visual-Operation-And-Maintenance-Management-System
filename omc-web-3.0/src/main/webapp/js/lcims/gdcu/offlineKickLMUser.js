require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'layer':'/js/layer/layer',
        "daterangepicker": "/js/lcims/tool/daterangepicker",
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage",
        'stringutil': '/js/lcims/tool/stringutil'
    },
    shim: {
        "daterangepicker": {
            deps: ["jquery", "moment"]
        },
        "moment": {}
    }
});
require(['jquery', 'layer', 'daterangepicker', 'laypage', 'resizewh', 'stringutil' ],
function($, layer, daterangepicker,laypage,resizewh,stringutil) {
    var lopen = {},layernames=["#omcOpen","#openLoading","#loadingresult"];
    resizewh.resizeWH($("#mainarea"));
    $("#offlineuser").click(function() {
    	var brasip = stringutil.Trim($("#brasip").val());
        if (!checkIp(brasip)) {
            layer.tips('请输入正确格式的Brasip!', '#brasip',{ tips: [3, '#EE1A23'] });
        } else {
            showLayer();
        }
    });
    $("#submitCommand").click(function() {
        offlineBatchuser();
    });
    
    $("#query").click(function() {
    	loadOptRecord();
    });
//----------------------------------以下为自定义方法-------------------------------------------------//            
    function loadOptRecord(){
    	var pagecount=0;
        var page_count = 10;
        var data = {'opttype':"KickLMUser"};
        $.getJSON("/gdmainttool/batoptrecord/query?random=" + Math.random(),data, function(result) {
			total = result.length;
			pagecount=Math.ceil(total/page_count);
			$("#querynum").text(total);
            laypage({
                cont: 'pageinfo', 
                skin: '#6AB0F4',
                pages: pagecount,
                curr: 1, 
                skip: true, //是否开启跳页
                jump: function(obj, first){ //触发分页后的回调
                	startnum = (obj.curr - 1) * page_count + 1;
                    endnum = obj.curr * page_count;
                    endnum = endnum > total ? total : endnum;
                    showTable(result,startnum,endnum);
                    $("#currnum") .text( startnum + "-" + (endnum > total ? total : endnum));
                    resizewh.resizeWH($("#mainarea"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    
    //拼接tr
    function showTable(data,startnum,endnum){
    	var rowdata=[];
        if(data==null || data.length==0){
            rowdata.push("<tr><td></td><td>暂无数据</td><td></td></tr>");
        }else{
        	for(var i=startnum;i<=endnum;i++){
                var row = data[i-1];
                rowdata.push("<tr>");
                rowdata.push("  <td>" + row.nasip + "</td>");
                rowdata.push("  <td>" + (row.optreason == null ? "" : row.optreason) + "</td>");
                rowdata.push("  <td>" + row.opttime + "</td>");
                rowdata.push("</tr>");
            }
        }
        $("#recordtable").html(rowdata.join(""));
    }
    function showLayer() {
        lopen = layer.open({
            type : 1,
            title : '批量删除在线用户',
            area : [ '500px', '290px' ],
            shadeClose : true,
            content : $('#tool_info'),
            end: function () {
                $("#brasip").val("");
                $("#loadingresult").hide();
                $("#omcOpen").css("display","none");
            }
        });
        $(".omc-loadclose").click(function() {
        	$("#omcOpen").css("display","none");
        	layer.close(lopen);
        });
        $.getJSON("/gdmainttool/checkLimits?brasip=" + stringutil.Trim($("#brasip").val()) + "&random=" + Math.random(), function(result) {
        	if (result.returncode === "0") {
	        	$.getJSON("/gdmainttool/checkBasUser?brasip=" + stringutil.Trim($("#brasip").val()) + "&random=" + Math.random(), function(result) {
	                if (result.returncode === "0") {
	                    lopenByName("#omcOpen",result.total);
	                } else {
	                    lopenByName("#loadingresult","指令发送失败："+result.errordescription,result.returncode);
	                }
	            });
        	}else{
	        	lopenByName("#loadingresult","指令发送失败："+result.message,result.returncode);
	        }
        });
    }
    function offlineBatchuser(){
        var offlineReason=$("#offlineReason").val();
        var brasip = $("#brasip").val();
        offlineReason=offlineReason.replace(/(^\s*)|(\s*$)/g, "");//去掉两端空格
        if(offlineReason==""){
            layer.tips('请输入删除在线用户原因!', '#offlineReason',{ tips: [3, '#EE1A23'] });
        }else{
            lopenByName("#openLoading");
            var data = {'type':2,'brasip':brasip,'offlineReason':offlineReason};
            var url = "/gdmainttool/offlineKickBRASUser?random=" + Math.random();
            $.getJSON(url, data, function(result){
                if (result.returncode === "0") {
                    lopenByName("#loadingresult","指令发送成功！",result.returncode);
                } else {
                    lopenByName("#loadingresult","指令发送失败："+result.errordescription,result.returncode);
                }
            });
        }
    }
    
    function checkIp(ip) {
        var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        var regex2 = '0.0.0.0';
        return regex.test(ip) && regex2 != ip;
    }
    
    function lopenByName(name,msg,returncode){
        $.each(layernames,function(i,row){
            if(row===name){
                $(row).show();
                if("#omcOpen"===name){
                    $("#offlineReason").val("");
                    $("#totalnum").text(msg);
                }else if("#loadingresult"===name){
                    loadresult(msg,returncode);
                }
            }else{
                $(row).hide();
            }
        });
    }
    function loadresult(msg,returncode){
        if(returncode==="0"){
            $("#loadingresult i.iconfont").removeClass("icon-cha text-danger");
            $("#loadingresult i.iconfont").addClass("icon-gou text-success");
            $(".loadingresult_title").text(msg);
        }else{
            $("#loadingresult i.iconfont").removeClass("icon-gou text-success");
            $("#loadingresult i.iconfont").addClass("icon-cha text-danger");
            $(".loadingresult_title").text(msg);
        }
    }
});