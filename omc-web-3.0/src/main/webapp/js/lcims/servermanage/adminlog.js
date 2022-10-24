require.config({
    paths: {
        'lcims': "/js/lcims",
        'resizewh': "/js/lcims/resizewh/resizewh",
        'jquery': '/js/jquery/jquery.min',
        'iscroll': '/js/lcims/tool/iscroll',
        'layer':'/js/layer/layer',
        "daterangepicker": "/js/lcims/tool/daterangepicker",
        "moment": "/js/lcims/tool/moment",
        "laypage":"/js/lcims/tool/laypage"
    },
    shim: {
        "daterangepicker": {
            deps: ["jquery", "moment"]
        },
        "moment": {}
    }
});

require(['jquery','layer','laypage','resizewh','daterangepicker'],
    function($,layer,laypage,resizewh) {
        var layer_load;
        resizewh.resizeWH($("#mainadminlog"));
        butBindFunction();
       	var today = moment().format('YYYY-MM-DD');
       	$('#startdate').daterangepicker({
       		parentEl: $("#startDate_div"),
       		format : 'YYYY-MM-DD',
       		startDate:today,
       		showDropdowns: false,
       		singleDatePicker:true
       	}, function (start, end, label) {
        });
       	
       	$('#enddate').daterangepicker({
       		parentEl: $("#endDate_div"),
       		format : 'YYYY-MM-DD',
       		startDate:today,
       		showDropdowns: false,
       		singleDatePicker:true
       	}, function (start, end, label) {
        });
        reset("mainadminlog");
        
//----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){

    	$("#funcid").mouseenter(function(){
    		resizewh.releaseWH($("#mainadminlog"));
    	});
    	$("#funcid").mouseleave (function(){
    		resizewh.resizeWH($("#mainadminlog"));
    	});
    	
        $("#querybutton").click(function() {
        	var startDate = $("#startdate").val();
            var endDate = $("#enddate").val();
            if(startDate!=""&&endDate!=""&&startDate>endDate){
            	layer.tips('结束日期不能小于开始日期!', '#enddate',{ tips: [2, '#EE1A23'] });
            }else{
            	loadingwait();
                loadOptRecord();
            }
        });
        $("#resetbutton").click(function() {
            reset("mainadminlog");
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
    
    //加载查询内容
    function loadOptRecord(){
        //分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        var admin = $("#admin").val();
        var funcid = $("#funcid").val();
        var data = {'startdate':startdate,'enddate':enddate,'admin':admin,'funcid':funcid};
        $.getJSON("/view/class/system/operatelogquery/query?random=" + Math.random(),data, function(result) {
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
                    resizewh.resizeWH($("#mainadminlog"));
                },
                groups: page_count //连续显示分页数
            });
        });
    }
    //拼接tr
    function showTable(data,startnum,endnum){
        var rowdata = "";
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            rowdata = rowdata + "<tr><td>"+rowninfo.operatetime+"</td><td>"
            +rowninfo.admin+"</td><td>"+rowninfo.rolename+"</td><td>"
            +rowninfo.areaname+"</td><td>"+rowninfo.ipaddress+"</td><td>"
            +rowninfo.funcname+"</td>";
        }
        $("#adminlogdiv").empty().append(rowdata);
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
    
    //校验IP
    function checkIp(ip) {
        var regex = /^(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(0\d\d|\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        return regex.test(ip);
    }
    
    function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
          });
    }
});