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
            <div class="panel-heading">学生名单</div>
            <div class="panel-body">
                <form action="/teacher/graduation/updateWeight" method="post">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <td>项目</td>
                            <td>权重</td>
                        </tr>
                        </thead>
                        <tbody>
                        <#--                        <#list events as event>-->
                        <#--                            <tr>-->
                        <#--                                <td>${event.name}</td>-->
                        <#--                                <td>-->
                        <#--                                    <input type="text" name="ratio" value="${event.ratio}">-->
                        <#--                                    <input type="hidden" name="id" value="${event.id}">-->
                        <#--                                </td>-->
                        <#--                            </tr>-->
                        <#--                        </#list>-->
                        </tbody>
                    </table>
                    <div class="form-group">
                        <div class="col-md-2 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">确认修改</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>