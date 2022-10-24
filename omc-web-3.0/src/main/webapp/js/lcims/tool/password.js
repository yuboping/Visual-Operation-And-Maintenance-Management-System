/**
 * 密码校验
 */
define(function (require) {
    "use strict";

    var min_len = 4;
    var max_len = 12;
    var self = {};
	
	/**
	 * 校验结果
	 * @param {boolean} pass 校验成功失败标识
	 * @param {String} message 校验失败信息
	 */
    function ValidResult(pass, message) {
    	this.pass=pass||false;
        this.message=message||"Invalid Password !";
    };

    var SUCCESS = new ValidResult(true,'OK!');
	
	/**
	 * 校验密码
	 * @param {string} pwd 待校验的密码
	 * @return {ValidResult} 校验结果 
	 */
    self.valid = function (pwd) {
        if (!pwd) {
            return new ValidResult(false, "请输入密码");
        }
        if (pwd.length < min_len || pwd.length > max_len) {
            return new ValidResult(false, "密码长度必须在" + min_len + "~" + max_len + "位之间");
        }
        return SUCCESS;
    };

    return self;
});