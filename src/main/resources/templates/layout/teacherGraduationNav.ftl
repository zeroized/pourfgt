<ol class="breadcrumb" style="margin-bottom:0">
    <li><a href="/">首页</a></li>
    <li><a href="/teacher/graduation/list">本科生毕业设计管理</a></li>
</ol>
<ul class="nav nav-pills" style="margin-top: 15px">
    <#assign navs=["list","timetable","post","checkMessage","inspection","weight"]>
    <#assign navNames=["学生名单","时间节点","信息发布","信息查看","验收","修改权重"]>
    <#list 0..5 as index>
        <#if index==navId>
            <li role="presentation" class="active">
                <a href="#">
                    ${navNames[index]}
                </a>
            </li>
        <#else >
            <li role="presentation">
                <a href="/teacher/graduation/${navs[index]}?year=${year}&semester=${semester}">
                    ${navNames[index]}
                </a>
            </li>
        </#if>
    </#list>
</ul>