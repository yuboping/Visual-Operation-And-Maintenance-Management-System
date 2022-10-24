/**
 * 常用的工具方法
 */
define(function (require) {
    "use strict";
    // 用于处理merge时无法遍历Date等对象的问题
    var BUILTIN_OBJECT = {
        '[object Function]': 1,
        '[object RegExp]': 1,
        '[object Date]': 1,
        '[object Error]': 1,
        '[object CanvasGradient]': 1
    };
    
    var objToString = Object.prototype.toString;
    
    function isDom(obj) {
        return obj && obj.nodeType === 1
            && typeof (obj.nodeName) == 'string';
    }

    function mergeItem(target, source, key, overwrite) {
        if (source.hasOwnProperty(key)) {
            var targetProp = target[key];
            if (typeof targetProp == 'object'
                && !BUILTIN_OBJECT[objToString.call(targetProp)]
            // 是否为 dom 对象
                && !isDom(targetProp)
                ) {
                // 如果需要递归覆盖，就递归调用merge
                merge(
                    target[key],
                    source[key],
                    overwrite
                    );
            }
            else if (overwrite || !(key in target)) {
                // 否则只处理overwrite为true，或者在目标对象中没有此属性的情况
                target[key] = source[key];
            }
        }
    }

    /**
     * 合并源对象的属性到目标对象
     * modify from Tangram
     * @param {*} target 目标对象
     * @param {*} source 源对象
     * @param {boolean} overwrite 是否覆盖
     */
    function merge(target, source, overwrite) {
        for (var i in source) {
            mergeItem(target, source, i, overwrite);
        }

        return target;
    }

    /**
     * 将模板使用变量展开
     * @param {String} -  templet 模板字符串 %s为占位符
     * @param {Array} - values 变量值数组,顺序与占位符保持一致
     * @return {String} - 替换之后的字符串
     */
    function expandTemplet(templet, values) {
        var i = 0,
            len = values.length;
        return templet.replace(/%s/g, function () {
            return (i < len) ? values[i++] : "";
        });
    }
    
    return {
        merge: merge,
        expandTemplet:expandTemplet
    };
});