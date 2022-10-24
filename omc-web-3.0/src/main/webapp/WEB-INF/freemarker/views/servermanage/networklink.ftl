<@bs.layout [
    "echarts/echarts.js",
    "echarts3/echarts.min.js",
    "lcims/servermanage/networklink.js",
    "layer/layer.css",
    "dpl.css"
]  true true>
<!--main-->
<div class="omc_main">
	<div>
        <div class="omc_main_tab" >
        	<div class="row-fluid">
                        <div class="span10">
                            <div class="controls">
                            	<select class="input-medium" id="nodeid" name="nodeid" >
                                   <#list nodeList as node>
                                        <option value=${node.nodeid}>${node.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
              </div>
        	<div class="omc_table_box">
			  <div id="networkTiemain" style="width: 3200px;height:750px;"></div>
			</div>
	   </div>
	</div>
</div>
</@bs.layout>