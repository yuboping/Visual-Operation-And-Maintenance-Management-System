<@bs.layout [
	"echarts3/echarts.min.js",
    "bootstrap/bootstrap.min.css",
    "jquery.mCustomScrollbar.css",
    <#--"lcims/home/jquery.mCustomScrollbar.concat.min.js",-->
    "scroll/jquery.mousewheel.min.js",
    "layer/layer.css",
    "echarts/echarts.css",
    "lcims/develop/home.js"
] true>

<!--内容区域-->
<!--omc_main-->
<div class="omc_main omc_city clearfix " >
<input type="hidden" id="province" name="province" value="${province}"/>
    <div class="clearfix">
        <div class="fl " style="width: 33.3%; margin:10px 0;border-right: 1px solid #394b73;">
            <div id="area1" style="height: 300px; padding: 10px;"></div>
        </div>
        <div class="fl " style="width: 33.3%; margin:10px 0;border-right: 1px solid #394b73;">
            <div id="area2" style="height: 300px; padding: 10px;"></div>
        </div>
        <div class="fl " style="width: 33.3%; margin:10px 0;">
            <div id="area3" style="height: 300px; padding: 10px;"></div>
        </div>
    </div>
    <div class="clearfix" style="border-top: 1px solid #394b73;">

        <div class="fl " style="width: 33.3%; margin:10px 0;border-right: 1px solid #394b73;">
            <div id="area4" style="height: 300px; padding: 10px;"></div>
        </div>
        <div class="fl " style="width: 33.3%; margin:10px 0;border-right: 1px solid #394b73;">
            <div id="area5" style="height: 300px; padding: 10px;"></div>
        </div>
        <div class="fl " style="width: 33.3%; margin:10px 0;">
            <div id="area6" style="height: 300px; padding: 10px;"></div>
        </div>
    </div>
    <div class="clearfix" style="border-top: 1px solid #394b73;">
        <div class="omc_home_Big fl" style="width: 50%; margin:10px 0;border-right: 1px solid #394b73;">
            <div id="area7" style="height: 400px; padding: 10px;"></div>
        </div>
        <div class="omc_home_small fl" style="width: 50%; ">
            <div class="home_new_tab" style="margin:0px 10px;padding-top: 20px;padding-left: 10px;">
                <b style=" font-size: 16px;color: #fff; ">关键警告信息</b>
                <table width="100%" cellspacing="0" cellpadding="0">
                    <thead>
                    <tr>
                    	<th>模块</th>
                        <th>指标</th>
                        <th>告警级别</th>
                        <th>告警目标</th>
                        <th>告警信息</th>
                        <th>告警数量</th>
                    </tr>
                    </thead>
                    <tbody id="alarmhistorydiv"></tbody>
                </table>
            </div>
        </div>
    </div>

</div>


<!--end-omc_main-->

<!--弹层-radius进程状态详情-->
<div class="mt10 display_none" id="detail_div">
    <div class="form-horizontal onlineTools">
       <div style="margin-left: 50px;">
				    <div class="mb10 clearfix">
                         <div class="mb10 clearfix">
                             <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span>条数据 | </div>
                             <div class="fl ml10">第 <span id="currnum" >1-10</span>条数据 </div>
                             <div id = "operate_menu" class="fr" ></div>
                         </div>
                    </div>
					<table style="width:90%" class="table table-bordered table-striped table-head-bordered table-hover  center ">
						<thead>
                            <tr>
                                <th style="width: 80px;">主机IP</th>
								<th style="width: 100px;">主机名</th>
								<th style="width: 200px;">radius进程状态描述</th>
                            </tr>
                        </thead>
						<tbody id="detaildiv"></tbody>
					</table>
					<div style="width:30%" id="pageinfo" class="fr parts_down_page clearfix"></div>
		</div>		
       <div class="row row-fluid mb10">
           <br/>
           <a id="detail_ok" style="margin-left: 320px;" class="button button-primary ml200 detail_ok">关闭</a>
       </div>
    </div>
</div>
<!--弹层-radius进程状态详情-END-->

<!--弹层-主机连通性详情-->
<div class="mt10 display_none" id="detail_div_connectable">
    <div class="form-horizontal onlineTools">
       <div style="margin-left: 50px;">
				    <div class="mb10 clearfix">
                         <div class="mb10 clearfix">
                             <div class="fl"> 共查询到 <span id="querynum_connectable" class="text-danger"></span>条数据 | </div>
                             <div class="fl ml10">第 <span id="currnum_connectable" >1-10</span>条数据 </div>
                             <div id = "operate_menu" class="fr" ></div>
                         </div>
                    </div>
					<table style="width:90%" class="table table-bordered table-striped table-head-bordered table-hover  center ">
						<thead>
                            <tr>
                                <th style="width: 80px;">主机IP</th>
								<th style="width: 100px;">主机名</th>
								<th style="width: 200px;">主机连通性状态描述</th>
                            </tr>
                        </thead>
						<tbody id="detaildiv_connectable"></tbody>
					</table>
					<div style="width:30%" id="pageinfo_connectable" class="fr parts_down_page clearfix"></div>
	   </div>		
       <div class="row row-fluid mb10">
           <br/>
           <a style="margin-left: 320px;" class="button button-primary ml200 detail_ok">关闭</a>
       </div>
    </div>
</div>
<!--弹层-主机连通性详情-END-->

<!--弹层-主机状态详情-->
<div class="mt10 display_none" id="detail_div_hoststate">
    <div class="form-horizontal onlineTools">
       <div style="margin-left: 50px;">
				    <div class="mb10 clearfix">
                         <div class="mb10 clearfix">
                             <div class="fl"> 共查询到 <span id="querynum_hoststate" class="text-danger"></span>条数据 | </div>
                             <div class="fl ml10">第 <span id="currnum_hoststate" >1-10</span>条数据 </div>
                             <div id = "operate_menu" class="fr" ></div>
                         </div>
                    </div>
					<table style="width:90%" class="table table-bordered table-striped table-head-bordered table-hover  center ">
						<thead>
                            <tr>
                                <th style="width: 100px;">主机IP</th>
								<th style="width: 100px;">主机名</th>
								<th style="width: 100px;">cpu占用率</th>
								<th style="width: 100px;">内存占用率</th>
								<th style="width: 100px;">磁盘利用率</th>
								<th style="width: 400px;">主机状态描述</th>
                            </tr>
                        </thead>
						<tbody id="detaildiv_hoststate"></tbody>
					</table>
					<div style="width:30%" id="pageinfo_hoststate" class="fr parts_down_page clearfix"></div>
	   </div>		
       <div class="row row-fluid mb10">
           <br/>
           <a style="margin-left: 450px;" class="button button-primary ml200 detail_ok">关闭</a>
       </div>
    </div>
</div>
<!--弹层-主机状态详情-END-->

<!--弹层-详情-->
<div class="mt10 display_none" id="detail_div_hisalarm">
	<div class="form-horizontal onlineTools">
		<input id="detail_alarmid" type="hidden">
		<!--搜索结果-->
		<div class="omc_table_box">
			<div class="mb10 clearfix">
				<div class="mb10 clearfix">
					<div class="fl"> 共查询到 <span id="hisquerynum" class="text-danger"></span>条数据 | </div>
					<div class="fl ml10">第 <span id="hiscurrnum" >1-10</span>条数据 </div>
					<div id="operate_menu_his" class="fr">
						<a id="confirm_button" class="button button-small button-primary">确认</a>
                        <#--<a href="#" id="export_button" class="button button-small button-primary">导出</a>-->
                        <a id="detail_hisalarm_ok"  class="button button-small button-primary">关闭</a>
					</div>
				</div>
				<table class="table table-bordered table-striped table-head-bordered table-hover  center ">
					<thead>
					<tr>
						<th style="width:3%"><input type="checkbox" id="checkboxAll" name="checkboxAll" /> </th>
						<th style="width:8%">模块</th>
						<th style="width:9%">指标</th>
						<th style="width:7%">告警级别</th>
						<th style="width:7%">告警目标</th>
						<th style="width:13%">告警信息</th>
						<th style="width:7%">告警数量</th>
						<th style="width:13%">初次告警时间</th>
						<th style="width:13%">最后告警时间</th>
						<th style="width:7%">确认人</th>
						<th style="width:11%">确认时间</th>
					</tr>
					</thead>
					<tbody id="hisalarmdiv"></tbody>
				</table>
				<div id="hispageinfo" class="fr parts_down_page clearfix"></div>
				<div class="ad-page-outer clearfix "></div>
			</div>
			<!--搜索结果-END-->
		</div>
	</div>
</div>
<!--end-详情-->

</@bs.layout>
