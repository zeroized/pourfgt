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
    <#include "../../layout/studentLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <#include "../../layout/studentGraduationNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">时间节点</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>时间</td>
                        <td>事件</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign semesters=["秋季","冬季","春季","夏季"]>
                    <#list events as event>
                        <tr>
                            <td>${event.keyDate}</td>
                            <td>${event.event}</td>
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