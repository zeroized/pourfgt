<ol class="breadcrumb" style="margin-bottom:0">
    <li><a href="/">首页</a></li>
    <li><a href="/teacher/course">本科生课程管理</a></li>
    <li><a href="/teacher/course/${courseId}">${courseName}</a></li>
</ol>
<ul class="nav nav-pills" style="margin-top: 15px">
    <#assign navs=["studentList","announcement","homework","question","discussion","weight"]>
    <#assign navNames=["学生名单","发布","平时作业","学生问题","研讨管理","修改权重"]>
    <#list 0..5 as index>
        <#if index==navId>
            <li role="presentation" class="active">
                <a href="#">
                    ${navNames[index]}
                </a>
            </li>
        <#else >
            <li role="presentation">
                <a href="/teacher/course/${courseId}/${navs[index]}">
                    ${navNames[index]}
                </a>
            </li>
        </#if>
    </#list>
</ul>