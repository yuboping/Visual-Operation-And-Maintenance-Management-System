require.config({
    paths : {
        'lcims' : "/js/lcims",
        'resizewh' : "/js/lcims/resizewh/resizewh",
        'jquery' : '/js/jquery/jquery.min',
        'iscroll' : '/js/lcims/tool/iscroll',
        'layer' : '/js/layer/layer',
        "laypage" : "/js/lcims/tool/laypage"
    }
});

require([ 'jquery', 'layer', 'laypage', 'resizewh' ],
    function($, layer, laypage, resizewh) {
        var layer_load;
        
        resizewh.resizeWH($("#mainhostperformance"));
        loadOptRecord();
// ----------------------------------以下为自定义方法-------------------------------------------------//
    function loadOptRecord(){
    	//分页显示的初始化数据
        var pagecount=0;
        var page_count = 10;
        var menuId = $("#menuId").val();
    	var data= {};
    	loadingwait();
		$.getJSON("/data/home/getHostPerformance?random=" + Math.random(),data, function(result) {
			layer.close(layer_load);
	        total = result.data.length;
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
                    showTable(result.data,startnum,endnum);
                    $("#currnum").text( startnum + "-" + endnum);
                    if(total==0){
                    	$("#currnum").empty().text("0");
                    }
                    resizewh.resizeWH($("#mainhostperformance"));
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
            rowdata = rowdata +"<tr>"
                +"<td title=\""+rowninfo.hostname+"\">"+rowninfo.hostname+"</td>"
                +"<td title=\""+rowninfo.addr+"\">"+rowninfo.addr+"</td>"
                +"<td title=\""+getStr(rowninfo.nodename)+"\">"+getStr(rowninfo.nodename)+"</td>";
            var cpuval = getStr(rowninfo.cpu.mvalue);
            if(rowninfo.cpu.alarmflag){
                cpuval = "<span class=\"text-danger\">"+cpuval+"</span>";
            }
            rowdata = rowdata + "<td>"+cpuval+"</td>";
            var memoryval = getStr(rowninfo.memory.mvalue);
            if(rowninfo.memory.alarmflag){
                memoryval = "<span class=\"text-danger\">"+memoryval+"</span>";
            }
            rowdata = rowdata + "<td>"+memoryval+"</td>";
            var connectableval = getIsNormal(rowninfo.connectable.mvalue);
            if(rowninfo.connectable.alarmflag){
                connectableval = "异常";
            }
            rowdata = rowdata + "<td>"+connectableval+"</td>";
            //处理进程信息
            var processData = rowninfo.processlist;
            var processval = "";
            for(var a=0;a<processData.length;a++){
                if(processData[a].alarmflag){
                    processval = processval+processData[a].itemname+"："+"异常<br/>";
                }else{
                    processval = processval+processData[a].itemname+"："+"正常<br/>";
                }
            }
            rowdata = rowdata + "<td>"+processval+"</td>";
            rowdata = rowdata + "</tr>";
        }
        //本页条数
        var ct = endnum-startnum+1;
        $("#hostperformancediv").empty().append(rowdata);
    }
    function getStr(value){
        if(value==null || value==""){
            return "";
        }
        return value;
    }
    function getIsNormal(value){
        if(value==null || value==""){
            return "";
        }
        if(value=='1'){
            return "正常";
        }
        return "异常";
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