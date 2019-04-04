require.config({

	baseUrl:'js/base',

	paths:{
		'jquery':'jquery',
		'expand-mobile' : 'http://127.0.0.1:8080/redirection1/web/res/js/mobile/expand-mobile',
		'biz-mobile': 'http://127.0.0.1:8080/redirection1/web/biz/js/common/biz-mobile',
		
		'domReady' : 'http://127.0.0.1:8080/redirection1/res/js/base/require-domReady',

		'jcl' : 'http://127.0.0.1:8080/redirection1/res/js/base/jcl',

		'zepto' : 'http://127.0.0.1:8080/redirection1/res/js/base/zepto',

		'iScroll' : 'http://127.0.0.1:8080/redirection1/res/js/base/iscroll',

		'iScroll5' : 'http://127.0.0.1:8080/redirection1/res/js/base/iscroll5',

		'Hammer' : 'http://127.0.0.1:8080/redirection1/res/js/base/hammer',

		'FastClick' : 'http://127.0.0.1:8080/redirection1/res/js/base/fastclick',

		'o' : 'http://127.0.0.1:8080/redirection1/res/js/frame/o',

		//'oEvent' : 'http://127.0.0.1:8080/redirection1/res/js/frame/o-event',

		'oInput' : 'http://127.0.0.1:8080/redirection1/res/js/frame/o-input',

		'tap' : 'http://127.0.0.1:8080/redirection1/res/js/frame/tap',

		'browserTool' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/browser-toolkit',

		'clientTool' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/client-toolkit',

		'mobileBrowser' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/mobile-browser',

		'mobileClient' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/mobile-client',

		'wadeMobile' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/wade-mobile',
//		这里同时会引入expand-mobile和biz-mobile
		'mobile' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/mobile',

		'base64' : 'http://127.0.0.1:8080/redirection1/res/js/mobile/base64',

		'chart' : 'http://127.0.0.1:8080/redirection1/res/js/ui/chart',

		'wmWebUI' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-webui',

		'wmTab' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-tab',

		'wmTabbar' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-tabbar',

		'wmPopup' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-popup',

		'wmBase' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-base',

		'wmAnimate' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-animate',

		'wmCss3animate' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-css3animate',

		'wmTouchLayer' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-touchLayer.js',

		'wmUI' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-ui',

		'wmNavBar' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-navbar',

		'wmToolTip' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-tooltip',

		'wmSwitch' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-switch',

		'wmSlider' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-slider',

		'wmProgress' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-progress',

		'wmSegment' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-segment',

		'wmDialog' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-dialog',

		'wmDialog2' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-dialog2',

		'wmDropmenu' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-dropmenu',

		'wmRefresh' : 'http://127.0.0.1:8080/redirection1/res/js/ui/wm-refresh',

		'handlebars' : 'http://127.0.0.1:8080/redirection1/res/js/base/handlebars/handlebars',

		'ipuUI' : 'http://127.0.0.1:8080/redirection1/res/js/ipuui/js/ipuUI',

		'ipuUI2' : 'http://127.0.0.1:8080/redirection1/res/js/ipuui/js/ipuUI2',

		'common' : 'http://127.0.0.1:8080/redirection1/biz/js/common/common',

		'util' : 'http://127.0.0.1:8080/redirection1/biz/js/common/util',

		'notice':'http://127.0.0.1:8080/redirection1/biz/js/homePage/notice',

		'mobscroll' : 'http://127.0.0.1:8080/redirection1/biz/js/base/mobiscroll.core-2.6.2',

		'customUtil':'http://127.0.0.1:8080/redirection1/biz/js/util/customUtil',

		'artTemplate':'http://127.0.0.1:8080/redirection1/biz/js/art-template/template-web',

		'share':'http://127.0.0.1:8080/redirection1/biz/js/common/share'
		
	},
	deps: [],
	callback: function(){
	},
//	非AMD规范的js输出对象
	shim: {
		handlebars: {  
			deps:[],
			exports: 'Handlebars'  
		}  
	},
//	设置超时时间,默认7秒
	waitSeconds:7/*,
//缓存
urlArgs: "bust=" + (new Date()).getTime()*/
});
