<#-- define bootstrap header -->
<#macro header userCSS=[] >
<head>
    <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="renderer" content="webkit">
	<title>AAA Data Analysis Platform</title>
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<@bs.useJS ["lcims/tool/iscroll.js"]/>
    <#list userCSS as css>
    <link rel="stylesheet" media="screen" href="/css/${css}" >
    </#list>
</head>
</#macro>

<#-- BASIC LAYOUT FOR BOOSTRAP -->
<#macro layout resources=[] istopmenu=false isleftmenu=false>
<!DOCTYPE html>
<html>
    <@header filter(resources,"css") />
<body>
    <#if istopmenu>
        <#include "topmenu.ftl">
    </#if>
    <#if isleftmenu>
        <#include "leftmenu.ftl">
     </#if>
    <#nested>
    <@useJS filter(resources,"js") />
</body>
</html>
</#macro>

<#macro useJS userjs=[]>
    <#list userjs as JS>
    <script type="text/javascript" src="/js/${JS}"></script>
    </#list>
</#macro>

<#macro useCSS usercss=[]>
    <#list usercss as css>
   		<link href="/css/${css}" rel="stylesheet" media="screen">
    </#list>
</#macro>

<#macro metric title chartid class isAlarm chartindex alarmmsg="abc">
        <p>${title}
            <div class="omc_blink dep_red">
                <p class="height25">
                <#if isAlarm>
                    ${alarmmsg}
                </#if>
            </div>
        <div name="siglechartdiv" class="${class}" value="${chartindex}" id="${chartid}" style="height: 220px;"></div>
</#macro>

<#macro metricChart title chartname class isAlarm chartindex alarmmsg="abc">
        <p class="omc_city_tu_title">${title} </p>
            <div class="omc_blink dep_red">
                <p class="height25">
                <#if isAlarm>
                    ${alarmmsg}
                </#if>
                </p>
            </div>
        <div name="siglechartdiv" class="${class}" value="${chartindex}" id="${chartname}" style="height: 220px;margin-top:20px;"></div>
</#macro>


<#macro navigator current menus>
<div class="col-md-1 bootstrap-admin-col-left">
    <ul class="nav navbar-collapse collapse bootstrap-admin-navbar-side">
        <#list menus as menu>
        <#if menu.name == current>
            <li id=${menu.id} class="active">
        <#else>
       		<li id=${menu.id}>
        </#if>
            <a href="javascript:loadPage('${menu.link}')"><@icon "chevron-right"/> ${menu.name?html}</a>
        </li>
        </#list>
    </ul>
</div>
</#macro>

<#--
 * input with label
 *
-->
<#macro input id type label labelsize=2 divided=true required=false  extra... >
<label class="col-lg-${labelsize} control-label" for="${id}">${label}</label>
<#if divided>
<div class="col-lg-${6-labelsize}">
<#else>
<div class="col-lg-${12-labelsize}">
</#if>
<#if required >
<div class="input-group">
</#if>
    <input class="form-control" id="${id}" type="${type}" 
    <#list extra?keys as attr>
    ${attr}="${extra[attr]?html}"
    </#list>
    >
    <#if required >
    <span class="input-group-addon" ><@icon "star"/></span>
    </#if>
</div>
<#if required >
</div>
</#if>
</#macro>

<#macro select id label optionlist listkey listvalue selected="" labelsize=2 divided=true extra... >
<#assign optionsize = optionlist?size >
<label class="col-lg-${labelsize} control-label" for="${id}">${label}</label>
<#if divided>
<div class="col-lg-${6-labelsize}">
<#else>
<div class="col-lg-${12-labelsize}">
</#if>
    <select id="${id}"
    <#if multiple??> multiple </#if>
    <#if disabled??> disabled="disabled"  </#if>
    <#if class??>
    class="${class?html}"
    <#else>
    class="form-control"
    </#if>
    <#list extra?keys as attr>
    ${attr}="${extra[attr]?html}"
    </#list>
    >
        <#if optionsize==0>
        <option value="" selected>无可选项</option>
        <#else>
            <#list optionlist as option>
            <#assign optval = option[listkey]?html >    
            <option value="${optval}" 
                <#if (optval==selected)>
                selected="selected"
                </#if>
            >
            ${option[listvalue]?html}
            </option>
            </#list>    
        </#if>
    </select>
</div>
</#macro>

<#macro textarea id label rows=3 labelsize=2  extra...>
<label class="col-lg-${labelsize} control-label" for="${id}">${label}</label>
<div class="col-lg-${12-labelsize}" >
<textarea class="form-control" rows="${rows}" 
<#list extra?keys as attr>
${attr}="${extra[attr]?html}"
</#list>
></textarea>
</div>
</#macro>

<#--
 * <@tablist>
 *      <@navtab link="xxx" > TabName </@>
 * </@>
-->
<#macro tablist class="nav nav-tabs">
<ul class="${class}" role="tablist">
<#nested >
</ul>
</#macro>

<#--
 * must wrap this in <@tablist>
 * <@navtab link="hostlist" active=true> TabName </@>
-->
<#macro navtab link tabname="" active=false>
<li role="presentation"
<#if active >class="active" </#if>
><a href="#${link}"  aria-controls="${link}" role="tab" data-toggle="tab">
${tabname}<#nested >
</a></li>
</#macro>

<#macro navbar navlist=[] >
<div class="navbar navbar-default bootstrap-admin-navbar-thin">
    <ol class="breadcrumb bootstrap-admin-breadcrumb">
        <#list navlist as nav>
            <#if nav_has_next>
                <li><a href="${nav.link}">${nav.display}</a></li>
            <#else>
                <li class="active">${nav.display}</li>
            </#if>    
        </#list>
    </ol>
</div>
</#macro>

<#-- 
 * a table use seq element
 * header=['#','First Name','Last Name','Username'] 
 * rowEls=[
 *       ['1','Mark','Otto','@mdo'],
 *       ['2','Jacob','Thornton','@fat'],
 *       ['3','Larry','the Bird','@twitter'],
 *       ['4','Mark','Sillywolf','@gmail']
 * ]
 * hidden=['#','Last Name'] 
 *
 * ----------------------------------------------
 * a table use hash element
 * header=[
 *         {'key':'id','displayName':'#'},
 *         {'key':'name','displayName':'First Name'},
 *         {'key':'lastname' , 'displayName':'Last Name'},
 *         {'key':'username' , 'displayName':'Username'}
 * ] 
 * rowEls=[
 *       {'id':'1' , 'name':'Mark' , 'lastname':'Otto' , 'username':'@mdo'},
 *       {'id':'2' , 'name':'Jacob' , 'lastname':'Thornton' , 'username':'@fat'},
 *       {'id':'3' , 'name':'Larry' , 'lastname':'the Bird' , 'username':'@twitter'},
 *       {'id':'4' , 'name':'Mark' , 'lastname':'Sillywolf' , 'username':'@gmail'}
 * ]
 * hidden=['name','username'] 
 *
 * editable = true|false false as default 
 *   if edittable set true extra actions TD(include edit,confirm,cancle,delete buttons) will be added
 * rowid required if editable is true
 * rowid specify which value from the row to be used as the button id
 -->
<#macro table class="table" header=[] rowEls=[] hidden=[] editable=false rowid=0 >
<#assign isHashRow = header[0]?is_hash  >
<#assign colsize = header?size >
<#assign display = createDisplaySeq(header,hidden,isHashRow) >
 <table class="${class}">
    <thead>
        <tr>
            <#list header as head>
            <#if display[head_index] == "true" >
                <th>${getTHVal(head,isHashRow,"displayName")}</th>
            </#if>
            </#list>
            <#if editable>
                <th>Actions</th>
            </#if>
        </tr>
    </thead>
    <#if rowEls??>
    <tbody>
        <#list rowEls as row>
        <tr>
            <#list 0..(colsize-1) as ind>
            <#if display[ind] == "true" >
                <td>${getTDVal(row header ind isHashRow)}</td>
            </#if>
            </#list>
            <#if editable>
                <td class="actions">
                    <button id="primary-${getTDVal(row header rowid isHashRow)}" class="btn btn-sm btn-primary">
                        <@icon "pencil"/> Edit
                    </button>
                    <button id="confirm-${getTDVal(row header rowid isHashRow)}" class="btn btn-sm btn-success">
                        <@icon "ok-sign" /> Confirm
                    </button>
                    <button id="cancel-${getTDVal(row header rowid isHashRow)}" class="btn btn-sm btn-warning">
                        <@icon "bell" /> Cancel
                    </button>
                    <button id="delete-${getTDVal(row header rowid isHashRow)}" class="btn btn-sm btn-danger">
                        <@icon "trash"/> Delete
                    </button>
                </td>
            </#if> 
        </tr>
        </#list>
    </tbody>
    </#if>
</table>
</#macro>

<#-- 
 * used for alert info 
 * class = success|info|warning|danger default : info
 * dismiss = true
 -->
<#macro alert class="info"  dismiss=true >
<div class="alert alert-${class}">
<#if dismiss>
<a class="close" data-dismiss="alert">&times;</a>
</#if>
<#nested >
</div>
</#macro>

<#-- 
 * used for label 
 * class = default|primary|success|info|warning|danger default : info
 -->
<#macro label class="info" >
<span class="label label-${class}">
<#nested >
</span>
</#macro>

<#-- 
 * used for button 
 * class = default|primary|success|info|warning|danger|link default : info
 -->
<#macro button type class="info" >
<button type="${type}" class="btn btn-${class}"
<#if disabled??> disabled="disabled"</#if>
>
<#nested >
</button>
</#macro>

<#macro icon name>
<span class="glyphicon glyphicon-${name}"></span>
</#macro>

<#macro searchbar options>
<div class="col-lg-3">
</div>
<!-- 切换城市-->
<div class="col-lg-1">
    <div class="dropdown" >
        <p>全省</p><a href="#" class="dropdown-toggle" data-hover="dropdown"><em>[切换城市]</em></a>                       
        <div class="dropdown-menu">
        <#assign rownum=4>
        <#list options as option>
        <#if (option_index  % rownum) == 0 >
        <dl><dd>
        </#if>
        <a href="#" style="font-weight:bold">${option}</a>
        <#if ((option_index+1)  % rownum) == 0 || !option_has_next >
        </dd></dl>
        </#if>
        </#list>
        </div>
    </div>
</div>
<!--search-->
<div class="col-lg-5">
    <div class="input-group">
        <input type="text" class="form-control" placeholder="请输入关键字（小区名）">
        <span class="input-group-btn">
        <button class="btn btn-primary" type="button">搜索</button>
        </span>
    </div>
</div>
<div class="col-lg-3">
</div>  
</#macro>

<#-- 
 * create a seq with String element "true" or "false"
 * the return seq.size == header.size
 * if header[ind]["key"] contains by hidden then return seq[ind] == "false"
 * else seq[ind] == true
-->
<#function createDisplaySeq header hidden isHashRow=false>
    <#assign boolstr>
        <#list header as head>
            <#if hidden?seq_contains(getTHVal(head,isHashRow,"key")) >
                false 
            <#else>
                true 
            </#if>
        </#list>
    </#assign>
    <#return boolstr?word_list>
</#function>

<#-- 
 * if isHashRow == true  return header[key]
 * else return header
-->
<#function getTHVal head isHashRow=false key="key">
    <#if isHashRow >
         <#return head[key] >
    <#else>
         <#return head>
    </#if>
</#function>

<#-- 
 * if isHashRow == true  return row[head[ind]["key"]]]
 * else return row[ind]
-->
<#function getTDVal row header ind isHashRow=false>
    <#if isHashRow >
         <#return row[(header[ind]["key"])]!(' -- ') >
    <#else>
         <#return row[ind]!(' -- ')>
    </#if>
</#function>

<#-- 
 * create a seq from String sequence that every elements end with suffix
-->
<#function filter oriseq suffix>
    <#assign seqstr>
        <#list oriseq as el>
            <#if el?ends_with(suffix)>
                ${el}
            </#if>
        </#list>
    </#assign>
    <#if seqstr??>
        <#return seqstr?word_list>
    </#if>
    <#return []>
</#function>

<#macro showlayout >
<style type="text/css">
    .hideDlg
    {
        height:129px;width:368px;
        display:none;
    }
    .showDlg
    {
        background-color:#ffffdd;
        border-width:3px;
        border-style:solid;
        height:129px;width:368px;
        position: absolute; 
        display:block;
        overflow-y: auto;
        z-index:33;
    }
    .showDeck {
        display:block;
        top:0px;
        left:0px;
        margin:0px;
        padding:0px;
        width:100%;
        height:100%;
        position:absolute;
        z-index:33;
        background:#cccccc;
    }
    .hideDeck 
    {
        display:none;
    }
</style>
<div style="opacity: 0.4;" class="hideDeck" id="deck"></div>   
<div id="messageBox" class="hideDlg" style="" >
    <#nested >
</div>
</#macro>
