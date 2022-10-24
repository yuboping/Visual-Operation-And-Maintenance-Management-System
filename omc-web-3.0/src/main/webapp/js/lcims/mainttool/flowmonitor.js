require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'layer':'/js/layer/layer',
    }
});
require([ "jquery","resizewh","layer",],function($,resizewh,layer) {
    resizewh.resizeWH($("#mainarea"));
    var timer;//定时器全局定义，方便清除
    var pageUrl=window.location.pathname;
    $('.J_detail').on('click', function(){//点击执行细节
        var task_id=$(this).attr("task_id");
        var task_name=$(this).attr("task_name");
        var serial_num=$(this).attr("serial_num");
        showFlowDetail(task_id,task_name,serial_num,"");
    });
    $('.J_execute').on('click', function(){//点击实时执行
        var task_id=$(this).attr("task_id");
        var task_name=$(this).attr("task_name");
        var serial_num="";
        var message="";
        $.getJSON("/data/mainttool/flowmonitor/excuteFlow?task_id=" + task_id + "&random="+Math.random(), function(data) {
            serial_num = data.serial_num;
            message = data.message;
            showFlowDetail(task_id,task_name,serial_num,message);
        });
        timer = window.setInterval(function(){ 
            showFlowDetail(task_id,task_name,serial_num,message);
        },1000);
    });
    $('.J_edit').on('click', function(){//点击配置
        var task_id=$(this).attr("task_id");
        showEditPage(task_id);// 展示配置页面
    });
    $("#send_type").change(function(){
        var send_type = $('#send_type').val();
        if(send_type == '1'){
            $('label#receive_label').text("邮件地址");
        }else{
            $('label#receive_label').text("手机号码");
        }
    });
    // -------------------------------以下为自定义方法----------------------------------
    function showEditPage(task_id){//配置流程执行时间等
        $.getJSON("/data/mainttool/flowmonitor/query?task_id="+task_id+"&random="+Math.random(), function(data) {
            $('div#task_name').text(data.task_name);
            $('input#cron').val(data.cron);
            $('select#send_type').val(data.send_type);
            if(data.send_type=='1'){
                $('label#receive_label').text("邮件地址");
            }else{
                $('label#receive_label').text("手机号码");
            }
            $('input#receiver').val(data.receiver);
        });
        layer.open({
            type: 1,
            title: '修改配置',
            area: ['600px', '400px'],
            shadeClose: true,
            content: $('#data_edit'),
            btn: ['确认','取消'],
            yes:function(){
                editFlowTask(task_id);
            }
        });
    }
    function showFlowDetail(task_id,task_name,serial_num,message){//执行细节、实时执行页面
        $.getJSON("/data/mainttool/flowmonitor/detail?task_id="+task_id+"&serial_num="+serial_num+"&random="+Math.random(), function(data) {
            appendDetailHtml(data,task_name,message);
        });
    }
    function appendDetailHtml(data,task_name,message){//根据查询结果将div#mainarea下的内容替换为流程执行过程
        var text=[];
        text.push("<div class='omc_main_tab '>");
        text.push(" <div class='clearfix'>")
        text.push("    <h4 class='pl10 fl' style='font-size:16px;font-weight:normal'>" + task_name + "</h4>");
        text.push("    <a id='reback' class='button button-primary mr10 f14 fr' href='" + pageUrl + "'>返回</a>");
        text.push(" </div>")
        text.push("    <div class='omc_table_box'>");
        text.push("        <div class='ml20'>")
        text.push("            <span class='process-tips'>" + message + "</span>");
        text.push("        </div>")
        text.push("        <div class='process-info'>");
        $.each(data, function(i, row) {
            var cStyle = "",textStyle = "",desc = "未执行";
            if(row.isend == "1"){
                clearInterval(timer);
            }
            if(row.result_flag == "SUCCESS"){
                desc = "执行成功";
                cStyle = "c_green";
                textStyle = "text_green";
            }else if(row.result_flag == "FAILED"){
                desc = "执行失败";
                cStyle = "c_red";
                textStyle = "text_red";
            }
            text.push("            <div class='processlist'>");
            text.push("                <span class='circle " + cStyle + "'>·</span>");
            text.push("                <ul class='base_list'>");
            text.push("                    <li>");
            text.push("                        <div class='list_center'>");
            text.push("                            <span class='worker'>" + desc + (row.update_time==null?"":"("+row.update_time+")") + "</span>")
            text.push("                            <span class='identity'>" + (row.result_descript==null?"":row.result_descript)+ "</span>")
            text.push("                            <span class='feedback-time " + textStyle + "'>" + row.node_name + "</span></div>");
            text.push("                    </li>");
            text.push("                </ul>");
            if(i != data.length-1){
                text.push("                <span class='line'></span>");
            }
            text.push("            </div>");
        });
        text.push("        </div>");
        text.push("    </div>");
        text.push("</div>");
        $('div#mainarea').empty().append(text.join(""));
        resizewh.resizeWH($("#mainarea"));
    }
    
    function editFlowTask(task_id){//修改全流程配置
        var cron = $('input#cron').val();
        var send_type = $('select#send_type').val();
        var receiver = $('input#receiver').val();
        if(!validCron('cron',cron) || !validReceiver(send_type,receiver)){
            return;
        }
        var data = {task_id:task_id,cron:cron,send_type:send_type,receiver:receiver};
        $.getJSON("/data/mainttool/flowmonitor/edit?random=" + Math.random(), data, function(result) {
            layer.closeAll();
            if(result.opSucc){
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
        });
    }
    
    function validCron(id,value){//校验执行频率表达式
        if(null == value || value==""){
            $("input#"+id).focus();
            layer.tips('执行频率不能为空!', '#'+id,{ tips: [2, '#EE1A23'] });
            return false;
        }
        var flag = true;
        $.ajaxSettings.async = false;
        $.getJSON("/data/mainttool/flowmonitor/checkcron?cron="+value+"&random="+Math.random(), function(data) {
            if(data){
                flag = true;
            }else{
                $("input#"+id).focus();
                layer.tips('执行频率格式错误!', '#'+id,{ tips: [2, '#EE1A23'] });
                flag = false;
            }
        });
        return flag;
    }
    function validReceiver(send_type,receiver){//邮件地址，手机号码校验
        var email = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); 
        var phone = new RegExp("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$"); 
        if(send_type == "1"){//邮件方式
            if(receiver != "" && !email.test(receiver)){
                $("input#receiver").focus();
                layer.tips('邮件地址错误!', '#receiver',{ tips: [2, '#EE1A23'] });
                return false;
            }
        }else if(send_type == "2"){//短信方式
            if(receiver != "" && !phone.test(receiver)){
                $("input#receiver").focus();
                layer.tips('手机号码错误!', '#receiver',{ tips: [2, '#EE1A23'] });
                return false;
            }
        }
        return true;
    }
});