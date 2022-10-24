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
	    resizewh.resizeWH($("#mainqryAccessLog"));
	    butBindFunction();
	    changeDateType();
        reset("mainqryAccessLog");
//-----------------------------------------以下为自定义方法-------------------------------------------------//            
        function butBindFunction(){
	        $("#querybutton").click(function() {
	            var querydate = $("#querydate").val();
	            var account = $("#account").val();
	            if(account == null || account ==""){
					layer.tips('用户账号不能为空!', '#account',{ tips: [2, '#EE1A23'] });
	            }else if(null == querydate || querydate ==""){
	            	layer.tips('查询日期不能为空!', '#querydate',{ tips: [2, '#EE1A23'] }); 
	            }else{
	            	loadingwait();
	                loadOptRecord();
	            }
	        });
	        $("#resetbutton").click(function() {
	            reset("mainqryAccessLog");
	        });
	        
	        $("#queryaccType").bind("change",function(){
	            changeDateType();
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
	    
	    function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
        }
	    function changeDateType(){
	            var queryaccType = $("#queryaccType").val();
	            $("#querydate").val("");
	            $("#querydate").removeAttr("lay-key");
	            var max,format,datetype;
	            if(queryaccType==1){
	                max = 0;
	                format = 'yyyyMMdd';
	                datetype = 'date';
	                console.log(queryaccType+ " 1");
	            }else if(queryaccType==2){
	                max = 1;
                        format = 'yyyyMMdd HHmm';
                        datetype = 'datetime';
	                console.log(queryaccType+ " 2");
	            } else{
	                max = 0;
                        format = 'yyyyMMdd';
                        datetype = 'date';
	                console.log(queryaccType+ " else");
	            }
	            console.log(queryaccType+ " " + max + " " + format);
	            //删除
	            $("#querydate").remove();
	            //新增
	            $("#queryaccType").after(' <input size="30" readonly="readonly" class=" input-medium" style="width: 155px;" id="querydate" name="querydate" type="text">');
	            
	            laydate.render({
                        elem: '#querydate', //指定元素
                        lang: 'cn',
                        calendar: false,
                        max: max,
                        value: new Date(),
                        format: format,
                        type: datetype,
                        trigger: 'click' //采用click弹出
                    });
	        }
	    
	    function loadOptRecord(){
	        var pagecount=0;
	        var page_count = 10;
	        var account = $("#account").val();
	        var querydate = $("#querydate").val();
	        console.log(querydate);
	        loadingwait();
	        var data = {'account':account,'querydate':querydate};
	        $.getJSON("/gdmainttool/qryAccessLog?random=" + Math.random(),data, function(result){
	        	console.log(result);
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
	                    resizewh.resizeWH($("#mainqryAccessLog"));
	                },
	                groups: page_count //连续显示分页数
	            });
	        });
	    }
	    
	    function showTable(data,startnum,endnum){
	    	var rowdata = "";
            if(data==null||data.length==0){
            	rowdata = rowdata + "<tr><td colspan='13'>暂无数据</td></tr>";
            	$("#qryAccessLogdiv").empty().append(rowdata);
            }else{
            	for(var i=startnum;i<=endnum;i++){
                    var rowninfo = data[i-1];
                    if(rowninfo.svctype == '801' || rowninfo.svctype =="PPPoE"){
                    	rowninfo.svctype = "PPPoE";
                    }else {
                    	rowninfo.svctype = "不确定";
                    }
                    if(!isNaN(rowninfo.operatetype)){
                    	findoperatype(rowninfo ,rowninfo.operatetype);
                	}
                    rowdata = rowdata + "<tr><td style=\"white-space: normal;\">"+rowninfo.username+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.domainname+"</td><td style=\"white-space: normal;\">"+rowninfo.logtime+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.svctype+"</td><td style=\"white-space: normal;\">"+rowninfo.operatetype+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.reason+"</td><td style=\"white-space: normal;\">"+rowninfo.nasip+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.nasport+"</td><td style=\"white-space: normal;\">"+rowninfo.porttype+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.frameip+"</td><td style=\"white-space: normal;\">"+rowninfo.callerid+"</td><td style=\"white-space: normal;\">"
                    +rowninfo.calleeid+"</td><td style=\"white-space: normal;\">"+rowninfo.naslocation+"</td></tr>";
                }
            	$("#qryAccessLogdiv").empty().append(rowdata);
            }
	    }
	    
	    function findoperatype(row ,operatetype) {
	    	switch(row.operatetype){
	        	case "1" : row.operatetype = "认证成功"; break;
	        	case "2" : row.operatetype = "认证失败"; break;
	        	case "3" : row.operatetype = "计费开始"; break;
	        	case "4" : row.operatetype = "计费结束"; break;
	        	case "5" : row.operatetype = "KeepAlive"; break;
	        	default : row.operatetype = "NoResponse";
	    	}
		}
	 
	    function checkIp(ip) {
	        var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	        return regex.test(ip);
	    }

});