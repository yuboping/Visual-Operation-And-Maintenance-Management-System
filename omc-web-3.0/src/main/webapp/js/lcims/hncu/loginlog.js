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

        var layer_load;

		resizewh.resizeBodyH($("#mainhistory"));
        butBindFunction();
        initChildrenMenu();
        document.getElementById("visible_div").style.visibility="hidden";
        laydate.render({
            elem: '#startdate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,max: 1
            ,value: new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
            ,format: 'yyyy-MM-dd HH:mm:ss'
            ,type: 'datetime'
            ,trigger: 'click' //采用click弹出
        });

        laydate.render({
            elem: '#enddate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,value: new Date()
            ,max: 1
            ,format: 'yyyy-MM-dd HH:mm:ss'
            ,type: 'datetime'
            ,trigger: 'click' //采用click弹出
        });
        
        reset("mainhistory");

//----------------------------------以下为自定义方法-------------------------------------------------//
        function loadingwait(){
            layer_load = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
        }
        
        function butBindFunction(){
        $("#querybutton").click(function() {
        	var startDate = $("#startdate").val();
            var endDate = $("#enddate").val();
            var startoutdate = $("#startoutdate").val();
            var endoutdate = $("#endoutdate").val();
            if(null == startDate || startDate =="" ){
            	layer.tips('登陆日期不能为空!', '#startdate',{ tips: [2, '#EE1A23'] }); 
            }else if(endDate == null || endDate ==""){
            	layer.tips('退出日期不能为空!', '#enddate',{ tips: [2, '#EE1A23'] });
            }else if(startDate!=""&&endDate!=""&&startDate>endDate){
            	layer.tips('退出日期不能小于登陆日期!', '#enddate',{ tips: [2, '#EE1A23'] });
            }else {
            	loadingwait();
                loadOptRecord(1);
            }
        });
        $("#resetbutton").click(function() {
            reset("mainhistory");
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

        laydate.render({
            elem: '#startdate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,max: 1
            ,value: new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
            ,format: 'yyyy-MM-dd HH:mm:ss'
            ,type: 'datetime'
            ,trigger: 'click' //采用click弹出
        });

        laydate.render({
            elem: '#enddate' //指定元素
            ,lang: 'cn'
            // ,calendar: true
            ,value: new Date()
            ,max: 1
            ,format: 'yyyy-MM-dd HH:mm:ss'
            ,type: 'datetime'
            ,trigger: 'click' //采用click弹出
        });
        
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
                    // 导出绑定事件
                    if(result[i].url=='export'){
                        $("#"+result[i].name).click(function() {
                            exportShow();
                        });
                    }
                }
            }

        });
    }
    
	//加载查询内容
    function loadOptRecord(pageNumber) {

        var start_time = $("#startdate").val();
        var end_time = $("#enddate").val();
        var admin_account = stringutil.Trim($("#adminAccount").val());
        var login_ip = stringutil.Trim($("#loginIp").val());
        var event_occur_host = stringutil.Trim($("#eventOccurHost").val());

        loadingwait();
        var data = {
            'start_time': start_time, 'end_time': end_time, 'admin_account': admin_account, 'login_ip': login_ip, 
            'event_occur_host': event_occur_host,'pageNumber': pageNumber

        };
        $.getJSON("/view/class/mainttool/loginlog/query?random=" + Math.random(), data, function (result) {
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
                    resizewh.resizeBodyH($("#mainhistory"));
                    if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                        loadOptRecord(obj.curr);
                    }
                }
            });
        }
        
        //导出Excel
        function exportShow(){
            downloadExcel();
        }
        
        function downloadExcel(){
            var start_time = $("#startdate").val();
            var end_time = $("#enddate").val();
            var admin_account = stringutil.Trim($("#adminAccount").val());
            var login_ip = stringutil.Trim($("#loginIp").val());
            var event_occur_host = stringutil.Trim($("#eventOccurHost").val());
            
            var url = "/view/class/mainttool/loginlog/export?start_time="+start_time+"&end_time="+end_time+"&admin_account="+admin_account+"&login_ip="+login_ip+"&event_occur_host="+event_occur_host;
            window.open(url+"&random=" + Math.random(),"_blank");
        }

        //拼接tr
        function showTable(data,startnum,endnum){
            var rowdata = "";
            var k = 0;
            for(var i=0;i<=endnum-startnum;i++){
                var rowninfo = data[i];
                rowdata = rowdata + "<tr><td title=\""+rowninfo.event_occur_host+"\">"+rowninfo.event_occur_host
                +"</td><td title=\""+rowninfo.admin_account+"\">"+isNull(rowninfo.admin_account)
                +"</td><td title=\""+rowninfo.login_ip+"\">"+isNull(rowninfo.login_ip)
                +"</td><td title=\""+rowninfo.login_time+"\">"+rowninfo.login_time
                +"</td><td title=\""+rowninfo.logout_time+"\">"+rowninfo.logout_time
                +"</td><td title=\""+rowninfo.online_time+"\">"+rowninfo.online_time
                +"</td><td title=\""+rowninfo.login_flag+"\">"+rowninfo.login_flag
                +"</td></tr>";
            }
            var ct = endnum-startnum+1;
            $("#alarmhistorydiv").empty().append(rowdata);

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

        function showLayer(divid,title) {
            layer.open({
                type : 1,
                title : title,
                closeBtn:0,
                area : [ '900px', '595px' ],
                content : $("#"+divid)
            });
        }

        function isNull(data){
            if(data==null || data =='' || data == "null"){
                return '';
            }else
                return data;
        }
});
