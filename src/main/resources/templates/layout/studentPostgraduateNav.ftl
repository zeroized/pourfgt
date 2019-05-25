<ol class="breadcrumb" style="margin-bottom:0">
    <li><a href="/">首页</a></li>
    <li><a href="/student/postgraduate/timetable">研究生日常管理</a></li>
</ol>
<ul class="nav nav-pills" style="margin-top: 15px">
    <#assign navs=["timetable","post","message","submit"]>
    <#assign navNames=["时间节点","查看信息","发送信息","提交文档"]>
    <#list 0..3 as index>
        <#if index==navId>
            <li role="presentation" class="active">
                <a href="#">
                    ${navNames[index]}
                </a>
            </li>
        <#else >
            <li role="presentation">
                <a href="/student/postgraduate/${navs[index]}">
                    ${navNames[index]}
                </a>
            </li>
        </#if>
    </#list>
</ul>