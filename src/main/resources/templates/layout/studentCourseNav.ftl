<ol class="breadcrumb" style="margin-bottom:0">
    <li><a href="/student">首页</a></li>
    <li><a href="/student">本科生课程管理</a></li>
    <li><a href="/student/course/${courseId}">${courseName}</a></li>
</ol>
<ul class="nav nav-pills" style="margin-top: 15px">
    <#assign navs=["homework","message","question","discussion"]>
    <#assign navNames=["查看作业","查看消息","查看提问","研讨选题"]>
    <#list 0..3 as index>
        <#if index==navId>
            <li role="presentation" class="active">
                <a href="#">
                    ${navNames[index]}
                </a>
            </li>
        <#else >
            <li role="presentation">
                <a href="/student/course/${courseId}/${navs[index]}">
                    ${navNames[index]}
                </a>
            </li>
        </#if>
    </#list>
</ul>