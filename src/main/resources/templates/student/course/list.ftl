<!DOCTYPE html>
<html>
<head>
    <title>Shanghai UniversityRender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <#include "../../layout/resource.ftl">

</head>
<body style="width:100%;height:100%;">
<#include "../../layout/headerNav.ftl">

<div class="container" style="margin-top: 15px">
    <#include "../../layout/leftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <ol class="breadcrumb" style="margin-bottom:0">
            <li><a href="/">首页</a></li>
            <li><a href="/teacher/course">本科生课程管理</a></li>
        </ol>
        <ul class="nav nav-pills" style="margin-top: 15px">
            <li role="presentation" class="active">
                <a href="/teacher/course/list">
                    课程列表
                </a>
            </li>
            <li role="presentation">
                <a href="/teacher/course/create">
                    新建课程
                </a>
            </li>

        </ul>
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">课程列表</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>课程号</td>
                        <td>课程名</td>
                        <td>学年</td>
                        <td>学期</td>
                        <td>状态</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign semesters=["秋季","冬季","春季","夏季"]>
                    <#list courses as course>
                        <#if course.year==currYear &&course.semester==currSemester>
                            <tr>
                                <td>${course.courseId}</td>
                                <td><a href="/teacher/course/${course.id}">${course.courseName}</a></td>
                                <td>${course.year}-${course.year+1}</td>
                                <td>${semesters[course.semester]}</td>
                                <td>激活</td>
                            </tr>
                        <#else >
                            <tr>
                                <td>${course.courseId}</td>
                                <td>${course.courseName}</td>
                                <td>${course.year}-${course.year+1}</td>
                                <td>${semesters[course.semester]}</td>
                                <td>已完成</td>
                            </tr>
                        </#if>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>