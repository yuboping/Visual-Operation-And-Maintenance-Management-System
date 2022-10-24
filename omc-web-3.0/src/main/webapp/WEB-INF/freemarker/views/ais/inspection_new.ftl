<@bs.layout [
"bootstrap/bootstrap.min.css",
"bootstrap/font-awesome.min.css",
"ais/font-awesome.min.css",
"ais/icon-style.css",
 "daterangepicker/daterangepicker.css",
"ais/ais.css"
] true true>


<div class="omc_main omc_ais" >
    <div  id="maincheckhome" class="omc-scroll">
            <!--搜索条件-->
       <div class=" dap-ais-mainarea">
           <div class="dap-ais-mainarea-bg">
               <!--实时巡检-->
               <div class="row">
                   <div class="dap-ais-ttl">
                       <h2 class="pull-left">实时巡检</h2>
                       <div class="pull-right">
                           <button class="dap-ais-btn big" id="dap-ais-btn">开始巡检</button>
                       </div>
                   </div>
                   <!--巡检类型START-->
                   <div class="dap-ais-chklist" id="ais-chklist"></div>
                   <#include "chkgroup.ftl"/>
                   <!--巡检类型END-->
               </div>

               <!--表格展示-->
               <div id="ais-table">
                   <div style="display: flex;margin-bottom: 10px;">
                       <div style="width: 10%;">
                           <select class="form-control" id="AisSelect">
                               <option value="0">全部</option>
                               <option value="1">正常</option>
                               <option value="2">异常</option>
                           </select>
                       </div>
                       <div style="margin-left: 10px;">
                           <button type="button" value="" name="search" id="searchAis" style="height: 34px;width: 34px;font-size: 14px;">
                               <span class="glyphicon glyphicon-search"></span>
                           </button>
                       </div>
                   </div>
                   <table class="table table-bordered">
                       <thead style="font-size: 14px;">
                       <tr>
                           <th>作业内容</th>
                           <th>执行时间</th>
                           <th>检查结果</th>
                           <th>指标值</th>
                           <th>检查项</th>
                           <th>备注</th>
                       </tr>
                       </thead>
                       <tbody id="aistbody"></tbody>
                   </table>
               </div>

               <!--定期巡检-->
               <div class="row" style="margin-top: 20px;">
                   <div class="dap-ais-ttl">
                       <h2 class="pull-left" style="padding-left:0">定期巡检</h2>
                       <div class="dap-ais-line"></div>
                   </div>
                   <!--巡检计划START-->
                   
                   <!-- 巡检计划模块-->
                    <script type="text/x-my-template" id="schedule">
                        <tr>
                            <td >%s</td>
                        </tr>
                    </script>
                   
                   <div class="dap-ais-plan-box">
                       <div class="dap-ais-plan-ttl">巡检计划</div>
                       <!--无巡检计划-->
                       <div id="noplan" class="dap-ais-noplan" style="display:none">
                           <p>您还没有设置任何巡检计划</p>
                           <p>请设置定期巡检</p>
                       </div>
                       <!--有巡检计划-->
                       <div id="hasplan">
                           <div class="dap-ais-plan-next-time">
                               <p class="ttl">下一次巡检时间</p>
                               <div class="time-box">
                                   <div class="col-md-6" style="padding-left:0; padding-right:0">
                                    <div class="time">12:00</div>
                                   </div>
                                   <div class="col-md-6 date"><span id="showdate"></span><br/><span class="weekday"></span></div>
                               </div>
                           </div><div class="clearfix"></div>
                           <table class="checktable  dap-ais-plan-list" style="margin-top:20px;">
                                <tbody  id="schdulelist">
                                </tbody>
                           </table>
                       </div>
                   </div>
                   <!--巡检计划END-->
                   <!--历史报告START-->
                   
                   <!-- 巡检报告模块 -->
                    <script type="text/x-my-template" id="report">
                        <tr>
                          <td><a href="#">%s</a></td>
                          <td>%s</td>
                          <td><a href="%s" class="dap-ais-plan-icon"><i class="fa fa-download"></i></a></td>
                        </tr>
                    </script>
                   
                   <div class="dap-ais-history">
                       <!-- <div class="pull-left dap-ais-history-ttl">历史巡检报告</div> -->
                           <fieldset>
                               <div class="control-group">
                                   <div class="clearfix">
                                       <div class="input-prepend input-group pull-left">
                                           <span class="add-on input-group-addon"><i class="glyphicon glyphicon-calendar fa fa-calendar"></i></span>
                                           <!--搜索-->
                                           <div id="date_div">
                                                <input type="hidden" id="begintime" name="begintime" value=""/>
                                                <input type="hidden" id="endtime" name="endtime" value=""/>
                                                <input type="text" readonly style="width: 200px" name="reservation" id="reservation" class="form-control" value="2014-5-21 - 2014-6-21" /> 
                                            </div>
                                           <div class="dap-ais-searchbox">
                                               <input class="form-control" type="text" style="" value="" name="searchkey" id="searchkey" placeholder="请输入关键字" />
                                               <button type="button" value="" name="search" id="search" class="fa fa-search dap-ais-searbtn"></button>
                                           </div>
                                       </div>
                                   </div>
                               </div>
                           </fieldset>
                       <table class="checktable  dap-ais-plan-list">
                           <thead>
                           <tr>
                               <th>巡检报告名称</th>
                               <th>时间</th>
                               <th>操作</th>
                           </tr>
                           </thead>
                           <tbody id="reporttbody">
                           </tbody>
                       </table>
                       <div id="pageinfo" class="fr parts_down_page clearfix"></div>
                       <div class="ad-page-outer clearfix "></div>
                       <!--分页START-->
                       <div class="dap-ais-pagination" >
                           <ul class="pagination">
                           </ul>
                       </div>
                       <!--分页END-->
                   </div>
                   <!--历史报告END-->
               </div>
               <div class="clearfix" style="height:40px; width: 100%; "></div>
           </div>
       </div>
            <!--搜索结果-END-->
        <!--main_index-END-->
    </div>
</div>

</@bs.layout>
<@bs.useJS ["lcims/ais/checkhome.js"]/>