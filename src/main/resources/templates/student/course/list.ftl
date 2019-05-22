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
    <#include "../../layout/teacherLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <ol class="breadcrumb" style="margin-bottom:0">
            <li><a href="/">首页</a></li>
            <li><a href="/student">本科生课程管理</a></li>
        </ol>
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">课程列表</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>课程号</td>
                        <td>得分</td>
                        <td>状态</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign semesters=["秋季","冬季","春季","夏季"]>
                    <#list courses as course>
                        <tr>
                            <td><a href="/student/course/${course.attachedId}">${course.attachedId}</a></td>
                            <td>${course.score}</td>
                            <td>激活</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>