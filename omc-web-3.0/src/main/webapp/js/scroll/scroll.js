require.config({
    baseUrl: '/js',
    paths: {
    	'jquery' : 'jquery/jquery.min',
    	"mousewheel": "scroll/jquery.mousewheel.min",
    	"mCustomScrollbar": "scroll/omc.jquery.mCustomScrollbar.min"
    },
    shim:{
    	'mCustomScrollbar': ['jquery','mousewheel']
    }
});

require(["jquery","mCustomScrollbar"], function ($,mCustomScrollbar) {
	$(".omc—scroll").mCustomScrollbar({
        theme:"minimal"
        // 这里可以根据背景颜色来通过theme选择自定义样式，
    });
});