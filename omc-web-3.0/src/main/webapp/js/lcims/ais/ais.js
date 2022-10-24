define(['jquery'], function ($) {
    //公共部分方法
    function categoryboxclick(event)
    {
       
            if (event.hasClass("dap-ais-nochoose")) {
                event.removeClass("dap-ais-nochoose");
            }
            else {
                event.addClass("dap-ais-nochoose");
            }
       
    }
    
    /*全选*/
    function checkOrNot(all, cellCheck) {
        var cellEle = $("#" + cellCheck).find("input[type='checkbox']");
        cellEle.each(function (index) {
            if ($("#" + all).attr("checked") == "checked") {
                flag = 1;
                $("#" + all).removeAttr("checked");
                $("#" + all).next().find(".swi_check>.checked").removeAttr("style");
                $("#" + ce).removeAttr("checked");
                $(this).next().find(".swi_check>.checked").removeAttr("style");
            } else {
                flag = 0;
                $("#" + all).attr("checked", "checked");
                $("#" + all).next().find(".swi_check>.checked").css("left", "2px");
                $(this).attr("checked", "checked");
                $(this).next().find(".swi_check>.checked").css("left", "2px")
            }
        })
    };

    function check(checkId, all) {
        if ($("#" + checkId).attr("checked") == "checked") {
            $("#" + checkId).next().find(".swi_check>.checked").removeAttr("style");
            $("#" + all).removeAttr("checked");
            flag = 1;

        } else {
            $("#" + all).attr("checked", "checked");
            $("#" + checkId).next().find(".swi_check>.checked").css("left", "2px");
            flag = 0;

        }

    };

    function ischeckAll(boxId) {
        var chkEle = $("#" + boxId).find("li").find("input[type='checkbox']:checked");
        if (chkEle.length == $("#" + boxId).find('li').length) {
            flag = 0;
        } else {
            flag = 1;
        }
    };

    function show(boxId) {
        $("#" + boxId).show();
    }

    function hide(boxId) {
        $("#" + boxId).hide();
    }

    function fadeIn(boxId) {
        $("#" + boxId).fadeIn();
    }

    /*正在巡检动画*/
    function scaleX(num) {
        $("#chkDoing" + num).animate({width: "80px"}, 500);
    }

    function addWarn(obj) {
        if(!obj.hasClass("warn"))
        {
            obj.addClass("warn");
        }
    }

    /*滚动条滚动到指定位置*/
    function scrollToP(obj) {
        $("html,body").animate({scrollTop: obj.offset().top}, 1000)
    }

    /*异常项计算*/
    function addCell(n) {
        var categoryobj = $('.dap-ais-mainarea-bg').find("#categoryList"+n+" ul");
        var warnEle = categoryobj.find("li").find(".check-warning-num");
        var warnNum = 0;
        warnEle.each(function () {
            warnNum = warnNum + parseInt($(this).text());
        })
        var cellNum = categoryobj.find("li").length;
        var infoHtml = "异常" + "<span class='c-orange'>" + warnNum + "项</span>"

        // var totalInfo=totalNum+"个异常项";
        if (warnNum > 0) {
            $("#info" + n).html(infoHtml);
            // $("#totalInfo").html(totalInfo);

        }
    }

    function total() {
        var totalNum = 0;
        $(".check-warning-num").each(function () {
            totalNum = totalNum + parseInt($(this).text());
            var totalInfo = totalNum + "个异常项";
            $("#totalInfo").html(totalInfo);
        })

    }

    return {
        categoryboxclick:categoryboxclick,
        checkOrNot: checkOrNot,
        check: check,
        ischeckAll: ischeckAll,
        scaleX: scaleX,
        addWarn: addWarn,
        scrollToP: scrollToP,
        show: show,
        hide: hide,
        fadeIn: fadeIn,
        addCell: addCell,
        total: total,
    }
})