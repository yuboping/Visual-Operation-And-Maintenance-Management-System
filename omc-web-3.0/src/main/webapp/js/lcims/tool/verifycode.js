/**
 * 验证码校验
 */
define(function (require) {
    "use strict";

    var len = 4;
    var self = {};
	
	/**
	 * 校验结果
	 * @param {boolean} pass 校验成功失败标识
	 * @param {String} message 校验失败信息
	 */
    function ValidResult(pass, message) {
    	this.pass=pass||false;
        this.message=message||"Invalid VerifyCode !";
    };

    var SUCCESS = new ValidResult(true,'OK!');
	
	/**
	 * 校验验证码
	 * @param {string} pwd 待校验的验证码
	 * @return {ValidResult} 校验结果 
	 */
    self.valid = function (code) {
        if (!code) {
            return new ValidResult(false, "请输入验证码");
        }
        if (code.length != 4) {
            return new ValidResult(false, "验证码错误!");
        }
        return SUCCESS;
    };

    return self;
});