/**
 * 地图展示页面的辅助类,提供地图展示页面的一些效果函数
 * 将效果/样式从业务中剥离
 */
require.config({
     baseUrl: '/js',
     paths: {
          "jquery": "jquery/jquery.min",
          "lcims": "lcims",
          "jscroll": "jquery/jquery.mCustomScrollbar.concat.min",
          "jmodal": "jquery/jquery.modal",
          "BMap": "lcims/tool/bmap",
          "bmapInfoBox": "bmap/InfoBox_min"
     },
     shim: {
          "jscroll": ["jquery"],
          "jmodal": ["jquery"],
          "bmapInfoBox": ["BMap"]
     }
});

define([
     "jquery",
     "lcims/tool/util",
     "BMap",
     "bmapInfoBox",
     "jscroll",
     "jmodal",
], function ($, util, BMap) {
     
          /** 海量点样式 */
          var massPointStyle = {
               size: BMAP_POINT_SIZE_SMALL || 3,
               shape: BMAP_POINT_SHAPE_WATERDROP || 1,
               color: '#F9917B'
          };
          /** 自定义信息框样式 */
          var infoBoxStyle = {
               boxStyle: {
                    margin: "25px 0px",
                    width: "300px",
                    height: "197px",
                    border: "1px solid #ddd"
               },      //标题
               closeIconMargin: "10px 10px 0px 10px",// 关闭按钮的margin
               closeIconUrl: "/images/close.png",
               enableAutoPan: true,     //是否启动自动平移功能
               align: INFOBOX_AT_TOP //基于哪个位置进行定位，取值为[INFOBOX_AT_TOP,INFOBOX_AT_BOTTOM]
          };
          
          /**
           * 获取html模板
           */
          function _getTemplet(templetId) {
               return $("#" + templetId)[0].text;
          }
          
          /**
           * 创建地图的infobox代码
           * @param {BMap.Map} map - 地图对象
           * @param {Array} data - 替换数据
           * @return {String} infobox的Html代码
           */
          function createInfoBox(map,data) {
               var temp = _getTemplet("infobox_templet");
               var html = util.expandTemplet(temp, data);
               return new BMapLib.InfoBox(map, html, infoBoxStyle);
          }
          
          /**
           * 创建排序列表的代码
           * @param {Number} ind - 数据序号[0..10]
           * @param {Array} data - 替换数据
           * @return {String} ranklist的Html代码
           */
          function createRankList(ind, data) {
               var temp = _getTemplet("ranklist_templet");
               var realData = [ind, ind, ind].concat(data);
               return util.expandTemplet(temp, realData);
          }
          
          function createMarker(ind,point){
                var imageUrl = "/images/point" + ind + ".png";//自定义图标
                var myIcon = new BMap.Icon(imageUrl, new BMap.Size(30, 38));
                return new BMap.Marker(point, { icon: myIcon });//初始化marker
          }
          
          /**
           * 为百度地图标记点附加infoBox对象,并添加mouseover,click,mouseout事件
           * @param {Number} ind - 标记点索引 
           * @param {Marker} marker - 百度地图的标记点对象
           * @param {infobox} infobox - 百度地图的InfoBox对象
           * 
           */
          function attachInfoBox(ind,marker,infobox){
               marker.addEventListener('mouseover', function (e) {
                    infobox.open(e.target);
                    //变更图标样式
                    $("#no" + ind).removeClass( "no-" + ind).addClass( "no-1" + ind );
                    $("#listli" + ind).addClass("listli");
                });
                marker.addEventListener('click', function (e) {
                    infobox.open(e.target);
                    //变更图标样式
                    $("#contentDiv").mCustomScrollbar("scrollTo", "#listli" + ind, {
                        scrollInertia: 1000
                    });
                });
                marker.addEventListener('mouseout', function (e) {
                    infobox.close(e.target);
                    $("#no" + ind).removeClass( "no-1" + ind).addClass( "no-" + ind );
                    $("#listli" + ind).removeClass("listli");
                });
          }
          
          /**
           * 设置自定义滚动条样式
           */
          function initScrollBar() {
               $(".scrollcontent").mCustomScrollbar({
                    theme: "dark",
                    autoHideScrollbar: true,
                    autoExpandScrollbar: true
               });
          }
     
          /**
           * 将地图及内容显示区自动扩展到全屏
           */
          function expandMapHeight() {
               var windowHeight = $(window).height();
               var searchHeight = $(".search").outerHeight(true);
               var crumbsHeight = $(".crumbs").outerHeight(true);
               var contentHeight = windowHeight - searchHeight - crumbsHeight;
               //设置高度
               $("#allmap").height(contentHeight);
               $("#contentDiv").height(contentHeight);
          }

          /**
           * 创建过渡动画对象
           * @param {Object} trans 过渡动画关联的对象标识
           * @return {Object} {trigger:Function,clear:Function} 过渡动画对象,trigger函数触发动画
           */
          function initTransition(trans) {
               var trigger = trans.trigger || "#modalLink";
               var close = trans.close;
               $(trigger).modal({
                    trigger: trigger,
                    olay: 'div.overlay_community',
                    modals: 'div.modalClass',
                    animationEffect: 'fadeIn',
                    animationSpeed: 400,
                    moveModalSpeed: 'slow',
                    background: '7F7F7F',
                    opacity: 0.7,
                    openOnLoad: false,
                    docClose: false,
                    closeByEscape: false,
                    moveOnScroll: true,
                    resizeWindow: false,
                    close: close
               });
               return {
                    trigger: function () {
                         $(trigger).click();
                    },
                    clear: function () {
                         $(close).click();
                    }
               };
          }


          return {
               massPointStyle: massPointStyle,
               initScrollBar: initScrollBar,
               expandMapHeight: expandMapHeight,
               initTransition: initTransition,
               createRankList: createRankList,
               createInfoBox: createInfoBox,
               attachInfoBox:attachInfoBox,
               createMarker:createMarker
          };
     });
