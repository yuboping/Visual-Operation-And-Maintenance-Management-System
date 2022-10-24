require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        'cookie': '/js/jquery/jquery.cookie',
        'resizewh':'/js/lcims/resizewh/resizewh',
        'topalarmutil': '/js/lcims/tool/topalarmutil'
    }
});

//添加时间
window.onload = function() {
           var show = document.getElementById("show");
           if(show != null) {
                      setInterval(function() {
                                 var time = new Date();
                                 // 程序计时的月从0开始取值后+1
                                 // 获取系统时间
                                 var yyyy = time.getFullYear();
                                 var MM = time.getMonth() + 1;
                                 var dd = time.getDate();
                                 var hh = time.getHours();
                                 var mi = time.getMinutes();
                                 var ss = time.getSeconds();
                                 // 分秒时间是一位数字，在数字前补0。
                                 mi = extra(mi);
                                 ss = extra(ss);
                                 show.innerHTML = yyyy + "-" + MM + "-" + dd + " " + hh + ":" + mi
                                                       + ":" + ss;
                      }, 1000);
           }
           
           // 补位函数。
           function extra(x) {
                      // 如果传入数字小于10，数字前补一位0。
                      if (x < 10) {
                                 return "0" + x;
                      } else {
                                 return x;
                      }
           }
};

require([ "jquery", 'cookie','resizewh','topalarmutil'],function($,cookie,resizewh,topalarmutil) {
           cusset();
           loadTopMenu();
           topalarmutil.alarm();
           resizewh.resizeWH($("#messageMain"));
//           alarm();
           setInterval(topalarmutil.alarm,1000*60*5);
           $(window).resize(function(){
               resizewh.resizeWH($(".omc-scroll"));
           });
           function alarm(){
              $.ajax({
                url: '/data/alarm/navigation?random='+Math.random(),
                dataType: "json",
                async: false,
                success: function (data) {
                     loadAlarm(data);//刷新告警总数及消息面板
                     //popAlarm(data);//弹出告警信息
                },
                error: function (data) {
                    console.log(data);
                }
            });
              resizewh.resizeWH($("#messageMain"));
           };
           
           function loadTopMenu(){
                      var pageUrl=window.location.pathname;
                      $.getJSON('/system/menu/top?pageUrl='+pageUrl+'&random='+Math.random(), function(data) {
                           $.each(data, function(i, row) {
                               if(row.active==false){
                                          $("#firstLevelMenu").append("<li><a href=\""+row.url+"\">"+row.show_name+"</a></li>");
                               }else {
                                          $("#firstLevelMenu").append("<li class=\"header_menu_active\"><a href=\""+row.url+"\">"+row.show_name+"</a></li>");
                                          }
                           });
                       });
           };
           
           function loadAlarm(data){
                      $("#alarmmessage ul").empty();
                      $("#alarm_message_num").empty();
                      $("#alarm_message_num").append('0');
                      var temp = "";
                      if(data.opSucc){
                                 $("#alarm_message_num").empty().append(data.data.total);
            $.each(data.data.list,function(i,row){
                              temp = ("<li class=\"clearfix\"><span class=\"");
                       if(row.alarm_level == 3){
                                  temp = temp + "error mr5\"";
                       }else if(row.alarm_level == 2){
                                  temp = temp + "warn mr5\"";
                       }else if(row.alarm_level == 1){
                                                       temp = temp + "normal mr5\"";
                                            }
                       temp = temp + "></span><span class=\"message_content\"><a href=\"" + row.url + "\">" + row.alarmmsg + "</a></span></li>";
                       $("#alarmmessage ul").append(temp);
                       temp = "";
            });
        }
           };
           
           function cusset(){
               $("#alarmmessage_but").on('click', function(ev){
            ev.preventDefault();
            $("#alarmmessage").toggleClass("message-open");
            if($("#alarmmessage").hasClass("message-open")){
              $("#alarmmessage .message_list").animate({right:"0px"},"slow");
            }else{
               $("#alarmmessage .message_list").animate({right:"-500px"},"slow");
            }
        });
               
               /*告警关闭*/
        $(".message_list .dap-close").on("click",function(){
                   $("#alarmmessage").toggleClass("message-open");
                   $("#alarmmessage .message_list").animate({right:"-500px"},"slow");
        })
    }
});
