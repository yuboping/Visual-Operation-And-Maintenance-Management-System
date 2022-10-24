/**
 * 常用的字符校验工具方法
 */

require.config({
	paths : {
		'jquery' : '/js/jquery/jquery.min',
		'layer' : '/js/layer/layer'
	}
});

define(function(require) {
	"use strict";
	var layer = require('layer');

	/**
	 * 判断字符空值和长度
	 * 
	 * @param id
	 * @param idVal
	 * @param msg
	 * @param length
	 * @returns
	 */
	function checkString(id, idVal, msg, length) {
		if (null != length) {
			if (idVal.length > length) {
				$("#" + id).focus();
				layer.tips(msg, '#' + id, {
					tips : [ 2, '#EE1A23' ]
				});
				return true;
			}
			return false;
		} else {
			if (null == idVal || idVal == "") {
				$("#" + id).focus();
				layer.tips(msg, '#' + id, {
					tips : [ 2, '#EE1A23' ]
				});
				return true;
			}
			return false;
		}
	}

    function Trim(str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    
    function Transference(str) {
        return str.replace("'", "\'");
    }

    //null值处理
    function isNull(data){
    	if(data==null || data=='null'){
    		return '';
    	}else{
    		return data;
    	}
    }
    
    //转义html脚本
    function escapeHTML(str){
        return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&apos;");
    }
    
	return {
		checkString : checkString ,
		Trim : Trim,
		Transference : Transference,
		isNull : isNull,
		escapeHTML:escapeHTML
	};
});