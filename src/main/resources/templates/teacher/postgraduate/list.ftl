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
        <#include "../../layout/teacherPostgraduateNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">学生名单</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>姓名</td>
                        <td>入学年份</td>
                        <td>类型</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list students as student>
                        <tr>
                            <td>${student.studentId}</td>
                            <td>${student.studentName}</td>
                            <td>${student.year}</td>
                            <td>${student.type}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                <hr style="border: 0.5px solid lightgrey">
                <form class="form-horizontal"
                      action="/teacher/postgraduate/addStudent" method="post">
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
                        <label for="year" class="control-label col-md-3">入学年份</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="year" id="year">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="type" class="control-label col-md-3">类型</label>
                        <div class="col-md-4">
                            <select class="form-control" name="type" id="type">
                                <option value="0">学硕</option>
                                <option value="1">专硕</option>
                            </select>
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