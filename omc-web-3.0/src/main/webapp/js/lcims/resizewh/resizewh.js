require.config({
	paths: {
        "jquery": "/js/jquery/jquery.min"
    },
})
define(["jquery"],function($){
	return{
		resizeWH:resizeWH,
		releaseWH:releaseWH,
		resizeBodyH:resizeBodyH
	};
	var myScroll;
	function resizeWH(obj,overflow){
		var bodyH=$(window).height();
		var mainH=bodyH-51;
		if(obj == null && obj ==""){
			return;
		}
		if(overflow == null || overflow == ""){
		    overflow = "hidden";
		}
		obj.height(mainH).css("overflow",overflow);
	  	$.each($(obj).parent().find(".omc-scroll"), function () {
	          myScroll = new IScroll(this, {
	          scrollbars: true,
	          mouseWheel: true,
	          click: false,
	          interactiveScrollbars: true,
	          shrinkScrollbars: 'scale',
	          fadeScrollbars: true,
	          preventDefault: false
	      });
	      //滚动到该元素的位置，第二个参数为时间，第三个第四个参数为偏移量（如果设置这两个参数为true，该元素将会显示在容器的中间）
	      if(exist('menufocus')){
	      	  myScroll.scrollToElement('#menufocus',1000,true,true);
	      }
	      if(exist('childmenufocus')){
		      myScroll.scrollToElement('#childmenufocus',1000,true,true);
		  }
	  });
	}
	
	//判断页面是否有此ID
	function exist(id){
		 if($("#"+id).length>0){
		  return true;
		 }
		 else{
		  return false;
		 } 
	}
	
	function resizeBodyH(obj){
		var bodyH=$(window).height();
		if(obj == null && obj ==""){
			return;
		}
		obj.height(bodyH).css("overflow-y","auto");
	}
	
	function releaseWH(obj){
		if(myScroll!=null){
			myScroll.destroy();
			myScroll = null;
		}
	}
	
});