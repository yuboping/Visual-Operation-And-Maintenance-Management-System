<input type="hidden" id="classtype" name="classtype" value="<#if classtype??>${classtype}</#if>"></input>
<input type="hidden" id="leftsearch2" name="leftsearch2" value='123'></input>
<input type="hidden" id="lastClick" name="lastClick" value='<#if lastClick??>${lastClick}</#if>'></input>
<!--begin leftmenu-->
<div class="omc_sidebar">
	<div id="leftmenubar">
	    <div class="omc_sidebar_menu omc-scroll">
	    	<div class="scroller-box " id="leftmenu">
	    	<input style="display:none;" class="mt10 ml10" id="leftsearch" name="leftsearch" type="text" placeholder="查询">
	        </div>
	    </div>
    </div>
</div>
<@bs.useJS ["echarts/echarts.js","lcims/leftmenu.js"]/>
<!--end-leftmenu-->

