<@bs.layout [
    "lcims/gdcu/onlineUserStats.js",
    "layer/layer.css",
    "laydate/laydate.js",
    "dpl.css",
    "bootstrap/bootstrap.min.css"
]  true true>
<!--main-->
<div class="omc_main">
   <div class="omc-scroll" id="mainonlineUserStats" >
        <div>
        <!--查询条件-->
        <div class="omc_main_tab" >
           <div class="form-horizontal onlineTools">
           <div class="row row-fluid">
           		<div class="span10">
                    <label class="control-label">Brasip</label>
                    <div class="controls">
                        <input size="16" id="brasip" class="input-medium" type="text">
                    </div>
                </div>
                <div class="span10">
                    <label class="control-label">业务类型</label>
                    <div class="controls">
                        <select class="input-medium" id="brasTpye">
                        	<option value="">请选择</option>
	                        <option value="1">wlan业务</option>
	                	 	<option value="2">宽带业务</option>
                        </select>
                    </div>
                </div>
                <div class="span4">
                    <a href="#" id="querybutton" class="button button-primary">查询</a>
                    <a href="#" id="resetbutton" class="button ml10">重置</a>
                </div>
            </div>
            </div>
			<!--查询条件-END-->
            
            <!--搜索结果-->
                <div class="omc_table_box">
                    <div class="mb10 clearfix">
                    	<div class="mb10 clearfix">
                    		<div class="fl"> 用户在线总数为 <span id="onlineUserNum" class="text-danger"></span>条数据</div>
                    	</div>
	                    <div class="mb10 clearfix">
	                        <div class="fl"> 共查询到 <span id="querynum" class="text-danger"></span>条数据 | </div>
	                        <div class="fl ml10">第 <span id="currnum" >1-10</span>条数据 </div>
	                     </div>
                    </div>
                    <table class="table table-bordered table-striped table-head-bordered table-hover  center ">
                        <thead>
                            <tr>
                                <th>BrasIp地址</th>
                                <th>所在区域</th>
                                <th>设备类型</th>
                                <th>业务类型</th>
                                <th>用户在线数</th>
                            </tr>
                        </thead>
                        <tbody id="onlineUserStatsdiv"> </tbody>
                    </table>
                    <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                    <div class="ad-page-outer clearfix "></div>
                </div>
                <!--搜索结果-END-->
               <div/>
            </div>
        </div>
    </div>
</div>
<!--end-main-->
</@bs.layout>             
