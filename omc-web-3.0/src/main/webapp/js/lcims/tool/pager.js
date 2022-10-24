/**
 * 定义分页控件,暂时使用kkpager.js代理实现功能
 */

require.config({
    baseUrl: '/js',
    paths: {
        "jquery": "jquery/jquery.min",
        "kkpager": "pager/kkpager"
    },
    shim: {
        "kkpager": {
            deps: ["jquery"],
            exports: "kkpager"
        }
    }
});

define(function (require) {
    'use strict';
    var pager = require('kkpager');
    
    //对pager进行初始化设置
    {
        pager.isShowPrePageBtn = false;
        pager.isShowNextPageBtn = false;
        pager.isShowTotalPage = false;
        pager.isShowCurrPage = false;
        pager.isGoPage = false;
    };

    /**
     * 在{domId}上创建pager对象
     * @param {Number} total - 元素总数
     * @param {callback} onPage - 点击页码执行的回调函数
     * @param {Number} currPage - 当前页码数,默认为1
     * @param {Number} pagesize - 每页元素个数,默认为10
     */
    function createPager(domId, total, onPage, currPage, pagesize) {
        currPage = currPage || 1;
        pagesize = pagesize || 10;
        pager.generPageHtml({
            pno: currPage,
            total: Math.ceil(total / pagesize),
            totalRecords: total,
            mode: 'click',
            click: function (n) {
                this.selectPage(n);
                onPage(n);
                return false;
            },
        }, true);
    }

    return {
        createPager: createPager,
    };
});