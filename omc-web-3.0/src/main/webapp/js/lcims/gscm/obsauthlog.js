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
	var obsloginurl;
	var obsreporturl;
	report();
	
	function report() {
		$.ajaxSettings.async = false;
		$.getJSON("/view/class/mainttool/obsauthlog/obsloginurl?random=" + Math.random(), function(result) {
			obsloginurl = result.message;
        });
		$.getJSON("/view/class/mainttool/obsauthlog/obsreporturl?random=" + Math.random(), function(result) {
			obsreporturl = result.message;
        });
		var newwindow=window.open(obsloginurl,'登陆OBS统计', 'height=10, width=100, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');		
		loadingwait();
		setTimeout(function(){
			newwindow.close();
//			$('#iframeobsreporturl').attr('src',obsreporturl);
			window.open(obsreporturl);	
			layer.close(layer_load);
	    },1000)
    }
	
	function loadingwait(){
        layer_load = layer.load(1, {
            shade: [0.1,'#fff'] // 0.1透明度的白色背景
          });
    }
});
