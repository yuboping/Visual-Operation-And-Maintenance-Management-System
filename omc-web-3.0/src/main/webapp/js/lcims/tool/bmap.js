/**
 * BMap API包装器,异步加载BMap之后返回BMap对象
 * 依赖require/async插件
 */
 require.config({
    baseUrl: '/js',
    paths: {
        "async": "require/async"
    },
});

define(["async!http://api.map.baidu.com/api?v=2.0&ak=XnDdFtImWfhcLSeDUttAS7DK"], function() {
    return BMap;
});