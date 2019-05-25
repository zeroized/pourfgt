<ol class="breadcrumb" style="margin-bottom:0">
    <li><a href="/">首页</a></li>
    <li><a href="/teacher/postgraduate/list">研究生日常管理</a></li>
</ol>
<ul class="nav nav-pills" style="margin-top: 15px">
    <#assign navs=["list","timetable","post","message","submission"]>
    <#assign navNames=["学生名单","时间节点","信息发布","信息查看","查看文档"]>
    <#list 0..4 as index>
        <#if index==navId>
            <li role="presentation" class="active">
                <a href="#">
                    ${navNames[index]}
                </a>
            </li>
        <#else >
            <li role="presentation">
                <a href="/teacher/postgraduate/${navs[index]}">
                    ${navNames[index]}
                </a>
            </li>
        </#if>
    </#list>
</ul>