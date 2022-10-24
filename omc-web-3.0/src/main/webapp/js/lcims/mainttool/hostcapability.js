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

        resizewh.resizeBodyH($("#mainhostcap"));
        butBindFunction();
        reset("mainhostcap");
        initChildrenMenu();
        loadingwait();
        loadOptRecord();
        setInterval(loadOptRecord,1000*60*5);

// ----------------------------------以下为自定义方法-------------------------------------------------//
    function butBindFunction(){
        $("#querybutton").click(function() {
            queryOpt();
        });
        $("#resetbutton").click(function() {
            reset("mainhostcap");
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

    // 查询按钮事件
    function queryOpt() {
        var host_name = stringutil.Trim($("#name").val());
        if(host_name != null && host_name != ""){
            if (!checkIp(host_name) && host_name != "") {
                layer.tips('请输入正确格式的主机IP!', '#name',{ tips: [2, '#EE1A23'] });
                $("#name").focus();
                return;
            }
        }
        loadingwait();
        loadOptRecord();
    }
    // 加载查询内容
    function loadOptRecord() {
        // 分页显示的初始化数据
        var pagecount = 0;
        var page_count = 10;

        var host_name = stringutil.Trim($("#name").val());
        var data = {
            'host_name' : host_name
        };
        $.getJSON("/view/class/mainttool/hostcapabilityquery/query?random=" + Math.random(), data, function(result) {
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
                    resizewh.resizeBodyH($("#mainhostcap"));
                },
                groups : page_count // 连续显示分页数
            });
        });
    }
    // 拼接tr
    function showTable(data, startnum, endnum) {
        var rowdata = "";
        var k = 0;
        for(var i=startnum;i<=endnum;i++){
            var rowninfo = data[i-1];
            rowdata = rowdata + "<tr><td>" + rowninfo.host_name + "</td><td>"
                + rowninfo.ip + "</td><td class='over_ellipsis' >" + isNull(rowninfo.cpu) + "</td><td class='over_ellipsis'>"
                + isNull(rowninfo.cpu_use_percent) + "</td><td class='over_ellipsis'>" + isNull(rowninfo.memory) + "</td><td class='over_ellipsis'>"
                + isNull(rowninfo.memory_use_percent) + "</td><td class='over_ellipsis'>" + isNull(rowninfo.stime) + "</td>";
        }
        $("#hostcapdiv").empty().append(rowdata);
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
            area : [ '500px', '210px' ],
            content : $("#" + divid)
        });
    }

    function isNull(data){
        if(data==null || data =='' || data == "null"){
            return '';
        }else
            return data;
    }

    //校验IP
    function checkIp(ip) {
        var regex1 = /^(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
        var regex2 = '0.0.0.0';
        return regex1.test(ip) && regex2 != ip;
    }

});