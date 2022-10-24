/**
 * 常用的工具方法
 */
require.config({
        paths : {
            'jquery': '/js/jquery/jquery.min',
            'cookie': '/js/jquery/jquery.cookie'
        }
});


define(function(require) {
        "use strict";
        var $ = require('jquery');
        var cookie = require('cookie');
        function alarm(){
            $.ajax({
              url: '/data/alarm/navigation?random='+Math.random(),
              dataType: "json",
              async: false,
              success: function (data) {
                   loadAlarm(data);//刷新告警总数及消息面板
                   alarmSound(data);
                   //popAlarm(data);//弹出告警信息
              },
              error: function (data) {
                  console.log(data);
              }
            });
        }
        function loadAlarm(data){
            $("#alarmmessage ul").empty();
            $("#alarm_message_num").empty();
            $("#alarm_message_num").append('0');
            var temp = "";
            if(data.opSucc){
                 $("#alarm_message_num").empty().append(data.data.total);
                 //console.log("top-alarminfoData--query:alarm num is "+data.data.total+",query time is "+new Date());
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
              }else{
                  //console.log("top-alarminfoData--query:alarm num is 0"+",query time is "+new Date());
              }
         };
     function alarmSound(data){
        var alarmsound = $("#alarmsound").val();
        var alarm_sound_type = $("#alarm_sound_type").val();
        if(alarmsound=="true" && alarm_sound_type=="1"){
            //1：sicu类型  当前存在未确认的告警信息，声音一直响
            if(isExitUnConfirmAlarmInfo(data)){
                $("#alarm_music").prop("loop","loop");
                $("#alarm_music").prop("autoplay","autoplay");
                $("#alarm_music").prop("muted","");
            }else{
                $("#alarm_music").prop("loop","");
                $("#alarm_music").prop("autoplay","");
                $("#alarm_music").prop("muted","muted");
            }
        }else if(alarmsound=="true" && alarm_sound_type=="2"){
            //2：默认公共类型：当前只要存在 告警一直响
            if(data.data!=null && data.data.total>0){
                $("#alarm_music").prop("loop","loop");
                $("#alarm_music").prop("autoplay","autoplay");
                $("#alarm_music").prop("muted","");
            }else{
                $("#alarm_music").prop("loop","");
                $("#alarm_music").prop("autoplay","");
                $("#alarm_music").prop("muted","muted");
            }
        }else{
            popAlarm(data, alarmsound, alarm_sound_type);
        }
      }
     
     function isExitUnConfirmAlarmInfo(data){
         var flag = false;
         if(data.data==null || data.data.list==null || data.data.total ==0){
             return flag;
         }
         $.each(data.data.list,function(i,row){
             //未确认
             if(row.confirm_state == 0){
                 flag = true;
                 return flag;
             }
         });
         return flag;
     }
     
      function popAlarm(data, alarmsound, alarm_sound_type){
         if(data==null||data.data==null||data.data.list==null){
              return;
          }
          $("#info-container").empty();
          var alarmtime=data.data.list[0].last_time;
          $.each(data.data.list,function(i,row){
              if(notShow(row.alarm_id,alarmtime)){
                var msg = "<li class=\""
                if(row.alarm_level == 3){
                           msg = msg + "info-error\"";
                }else if(row.alarm_level == 2){
                           msg = msg + "info-warn\"";
                }else if(row.alarm_level == 1){
                  msg = msg + "info-normal\"";
                   }
                msg = msg + "style=\"display: list-item; top: 0px;\"><div class=\"cont\"><i class=\"iconfont ";
                if(row.alarm_level == 1){
                           msg = msg + " icon-cry dep_yellow_bg\"";
                }else if(row.alarm_level == 2){
                    msg = msg + " icon-cry dep_green_bg\"";
                }else if(row.alarm_level == 3){
                           msg = msg + " icon-kulian dep_red_bg\"";
                }
                msg = msg + "></i><div><a href=\"" +row.url+ "\"><span>"+row.alarmmsg+
                "</span></a></p></div><a class=\"dap-close\">&times;</a></li>";
//                setTimeout(function(){
//                    $("#info-container").append($(msg).fadeIn().animate({top:"0"},"slow"));
//                     },200*(i+1)
//                );
                addShowid(row.alarm_id,alarmtime);
                if(alarmsound=="true" && alarm_sound_type=="0"){
                    $("#alarm_music").prop("autoplay","autoplay");
                }
             }
         });
            /*实时告警关闭*/
          $("#info-container").on("click",".dap-close",function(){
              $(this).parents("li").remove();
          })
      };
 
    function notShow(sid,alarmtime){
        var time=$.cookie("alarmtime");
        if(time!=alarmtime){
          $.cookie("alarmsid",null,{path:'/'});
          $.cookie("alarmtime",null,{path:'/'});
          return true;
        }
        var showid=","+$.cookie("alarmsid")+",";
        sid=","+sid+",";
        if(showid.indexOf(sid)<0){
          return true;
        }
        return false;
   }
    function addShowid(sid,alarmtime){
        var showid = (""+$.cookie("alarmsid"))==="null"?"":$.cookie("alarmsid");
        if((","+showid+",").indexOf(","+sid+",")<0){
          showid=showid+","+sid;
          $.cookie("alarmsid",showid,{path:'/'});
          $.cookie("alarmtime",alarmtime,{path:'/'});
        }
    }
        return {
                alarm : alarm
        };
});