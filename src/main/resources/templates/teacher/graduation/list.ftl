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
        <#include "../../layout/teacherGraduationNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">课程列表</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>姓名</td>
                        <td>标题</td>
                        <td>学年</td>
                        <td>学期</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign semesters=["秋季","冬季","春季","夏季"]>
                    <#list works as work>
                        <tr>
                            <td>${work.studentId}</td>
                            <td>${work.studentName}</td>
                            <td>${work.title}</td>
                            <td>${work.year}-${work.year+1}</td>
                            <td>${semesters[work.semester]}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                <hr style="border: 0.5px solid lightgrey">
                <form class="form-horizontal"
                      action="/teacher/graduation/addWork" method="post">
                    <div class="form-group">
                        <label for="studentId" class="control-label col-md-3">学号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="studentId" id="studentId">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="studentName" class="control-label col-md-3">姓名</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="studentName" id="studentName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="title" class="control-label col-md-3">毕业设计题目</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="title" id="title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="control-label col-md-3">描述</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="description" id="description">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-2 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">添加</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>