require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        "jcookie": "/js/jquery/jquery.cookie",
        "layoututil":'/js/lcims/tool/layoututil'
    }
});

define(function (require) {
    "use strict";
    var $ = require('jquery');
    require('jcookie');
    var layoututil = require('layoututil');
    var g_cmp_expires = 15;
    function openCompare() {
        $('#floatCom').css('display', 'none');
        $('#compare_bar').css('display', 'block');
        initCompareBar();
    };
    function closeCompare() {
        $('#floatCom').css('display', 'block');
        $('#compare_bar').css('display', 'none');
    };
    function get_cmp_expires() {
        var expires = new Date();
        var t = expires.getTime();
        t += g_cmp_expires * 60000;
        expires.setTime(t);
        return expires;
    };
    function open_compare_tip(msg) {
        $('#compare_tip_msg').html(msg);
        //$('#compare_tip').css('display', 'block');
        //$('#compare_bar').css('display','block');
        layoututil.showDlg();
    };
    function closeCompareTip() {
       // $('#compare_tip').css('display', 'none');
       layoututil.cancel();
    };

    function initCompareBar() {
        var compCookie = $.cookie("wz_aaa_compare_new");
        if (compCookie && compCookie != "null") {
            var cookiejson = $.parseJSON(compCookie);         
            //var cookieArr = compCookie.split("||");       
            var compare_select_str = [];
            $.each(cookiejson, function (i, row) {
                var cmp_arr_type = row.type;
                var cmp_arr_grouptype = row.grouptype;
                var cmp_arr_groupvalue = row.groupvalue;
                var cmp_arr_showtitle = row.showtitle;
                var cmp_arr_groupname = row.groupname;

                if (cmp_arr_showtitle.length > 10) {
                    cmp_arr_showtitle = cmp_arr_showtitle.substr(0, 10);
                    cmp_arr_showtitle += '...';
                }
                compare_select_str.push('<li><a title="', cmp_arr_type, ',', cmp_arr_grouptype, ',', cmp_arr_groupvalue, '" class="del fr" href="javascript:void(0);">删除</a>');
                compare_select_str.push('<p>[', cmp_arr_groupname, ']', cmp_arr_showtitle, '</p></li>');
            });
            if (compare_select_str) {
                $('#choose').css('display', 'block');
                $('#compare_select_item_title').css('display', 'block');
                $('#compare_select_item').css('display', 'block');
                $("#compare_select_item").html(compare_select_str.join(""));
                $('#compare_select_item a.del').click(function () {
                    var params = $(this).attr("title").split(',');
                    if (params.length != 3) {
                        return false;
                    }
                    removeCompareItem(params[0], params[1], params[2]);
                });
            }
        }
        else {
            $("#compare_select_item").html('');
            init_compare_title();
        }
    };
    function init_compare_title() {
        var select_house_item = $('#compare_select_item li')
        if (select_house_item.length == 0) {
            //$('#compare_select_item').css('display','none');
            //$('#compare_select_item_title').css('display','none');
            $('#choose').css('display', 'none');
            $('#nochoose').css('display', 'block');
        }
    };
    function removeCompareItem(att_type, attr_grouptype, attr_groupvalue) {
        var compare = $.cookie("wz_aaa_compare_new");
        if (compare && compare != "null") {
            var cookiejson = $.parseJSON(compare);
            $(cookiejson).each(function () {
                var cmp_arr_type = this.type;
                var cmp_arr_grouptype = this.grouptype;
                var cmp_arr_groupvalue = this.groupvalue;

                if (att_type == cmp_arr_type && attr_grouptype == cmp_arr_grouptype && attr_groupvalue == cmp_arr_groupvalue) {
                    cookiejson.splice($.inArray(this, cookiejson), 1);
                }
            });
            if (cookiejson.length == 0) {
                $.cookie("wz_aaa_compare_new", "", { expires: -1 ,path:'/'});
            }
            else {
                $.cookie("wz_aaa_compare_new", JSON.stringify(cookiejson), { expires: 1 ,path:'/'});
            }
        }
        initCompareBar();
        init_compare_title();
    };

    function addCompareItem(attr_type, attr_grouptype, attr_groupvalue, attr_showtitle, attr_groupname) {
        var compare = $.cookie("wz_aaa_compare_new");
        if (compare && compare != "null") {
            var cookiejson = $.parseJSON(compare);
            if (cookiejson.length >= 6) {
                open_compare_tip('亲,最多只能对比6项数据');
                return false;
            }
            var addflag = true;
            var userarray = ["user", "newuser", "expireuser", "activeuser"];
            var bandarray = ["bandwidth"];
            $.each(cookiejson, function (i, row) {
                if (!(($.inArray(attr_type, userarray) >= 0 && $.inArray(row.type, userarray) >= 0) || ($.inArray(attr_type, bandarray) >= 0 && $.inArray(row.type, bandarray) >= 0))) {
                    open_compare_tip('类型不同，无法对比，请选择同类型数据比较');
                    addflag = false;
                    return false;
                }
                if (attr_type == row.type && attr_grouptype == row.grouptype && attr_groupvalue == row.groupvalue) {
                    open_compare_tip('亲,该对比项已添加,无须再添加');
                    addflag = false;
                    return false;
                }
            });
            if (!addflag) {
                return false;
            }
            cookiejson.push({ type: attr_type, grouptype: attr_grouptype, groupvalue: attr_groupvalue, showtitle: attr_showtitle, groupname: attr_groupname });

            $.cookie("wz_aaa_compare_new", JSON.stringify(cookiejson), { expires: 1 ,path:'/'});
        }
        else {
            var adddata = [];
            adddata.push({ type: attr_type, grouptype: attr_grouptype, groupvalue: attr_groupvalue, showtitle: attr_showtitle, groupname: attr_groupname });
            $.cookie("wz_aaa_compare_new", JSON.stringify(adddata), { expires: 1 ,path:'/'});
        }
        initCompareBar();
        $('#nochoose').css('display', 'none');
        $('#compare_bar').css('display', 'block');
    };

    function removeAllItem() {
        $.cookie("wz_aaa_compare_new", "", { expires: -1 ,path:'/'});
        initCompareBar();
    };

    function goCompare(url,formid) {
        /*var compare = $.cookie("wz_aaa_compare_new");
        var cookiejson = $.parseJSON(compare);
        $.postJSON = function (url, data, callback) {
            return $.ajax({
                'type': 'POST',
                'url': url,
                'contentType': 'application/json',
                'data': JSON.stringify(data),
                'dataType': 'json',
                'success': callback
            });
        };
        var callback = function (data) {
            alert(data);
        };
        $.postJSON(url, compare, callback);
        */
        var form=$('#'+formid);
        form.attr('action',url);
        form.submit();
        
    };

    return {
        openCompare: openCompare,
        closeCompare: closeCompare,
        removeCompareItem: removeCompareItem,
        addCompareItem: addCompareItem,
        removeAllItem: removeAllItem,
        closeCompareTip: closeCompareTip,
        goCompare: goCompare
    };
});



