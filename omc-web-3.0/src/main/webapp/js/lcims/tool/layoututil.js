require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min'
    }
});

define(function (require) {
    "use strict";
    var $ = require('jquery');

    function showDlg() {
        //显示遮盖的层
        var objDeck = $("#deck");
        objDeck.attr('class', 'showDeck');
        objDeck.css('filter', 'alpha(opacity=50)');
        objDeck.css('opacity', '40/100');
        objDeck.css('MozOpacity', '40/100');
        objDeck.css('width',(window.pageXOffset + window.innerWidth) + "px");
        objDeck.css('height',(Math.max(document.body.scrollHeight, document.documentElement.scrollHeight)) + "px");
        //显示遮盖的层end
        $(window).scroll(function () {
            windowScrollUpload(this);
        });
        //改变样式
        $('#messageBox').attr('class', 'showDlg');
        
        //调整位置至居中
        adjustLocation();

    };

    function cancel() {
        $('#messageBox').attr('class', 'hideDlg');
        $('#deck').attr('class', 'hideDeck');
    };

    function windowScrollUpload(obj) {
        $('#deck > div').eq(0).css({
            'top': $(obj).scrollTop() + 'px'
        });

        $('#deck > div').eq(1).css({
            'top': ($(obj).scrollTop() < 200 ? 200 : $(obj).scrollTop()) + 'px'
        });
    };

    function adjustLocation() {
        var obox = $('#messageBox');
        var w = 368;
        var h = 129;
        var oLeft, oTop;

        if (window.innerWidth) {
            oLeft = window.pageXOffset + (window.innerWidth - w) / 2 + "px";
            oTop = window.pageYOffset + (window.innerHeight - h) / 2 + "px";
        }
        else {
            var dde = document.documentElement;
            oLeft = dde.scrollLeft + (dde.offsetWidth - w) / 2 + "px";
            oTop = dde.scrollTop + (dde.offsetHeight - h) / 2 + "px";
        }

        obox.css('left', oLeft);
        obox.css('top', oTop);
    };

    return {
        showDlg: showDlg,
        cancel: cancel,

    };

});