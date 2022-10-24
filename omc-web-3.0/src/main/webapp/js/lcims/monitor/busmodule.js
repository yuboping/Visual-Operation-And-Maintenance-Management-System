require.config({
    paths: {
        'jquery': '/js/jquery/jquery.min',
        'resizewh':'/js/lcims/resizewh/resizewh'
    }
});
require([ "jquery",'resizewh'],function($,resizewh) {
	resizewh.resizeWH($("#busmodulediv"));
	//判断左侧菜单是否时候active状态
});