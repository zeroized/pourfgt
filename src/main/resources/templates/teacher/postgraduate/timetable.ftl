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
            <div class="panel-heading">时间节点</div>
            <div class="panel-body">
                <form class="form-horizontal" action="/teacher/postgraduate/timetable" method="get">
                    <div class="form-group">
                        <label for="year" class="control-label col-md-3">入学年份</label>
                        <div class="col-md-3">
                            <select class="form-control" name="year" id="year">
                                <option value="2019">2019</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="type" class="control-label col-md-3">类型</label>
                        <div class="col-md-3">
                            <select class="form-control" name="type" id="type">
                                <option value="0">学硕</option>
                                <option value="1">专硕</option>
                            </select>
                        </div>
                    </div>
                </form>
                <hr style="border: 0.5px solid lightgrey">
                <table class="table">
                    <thead>
                    <tr>
                        <td>时间</td>
                        <td>事件</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list events as event>
                        <tr>
                            <td>${event.keyDate}</td>
                            <td>${event.event}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                <hr style="border: 0.5px solid lightgrey">
                <form class="form-horizontal"
                      action="/teacher/postgraduate/createTimetable" method="post">
                    <div class="form-group">
                        <label for="event" class="control-label col-md-3">事件</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="event" id="event">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="keyDate" class="control-label col-md-3">时间</label>
                        <div class="col-md-7">
                            <input type="date" class="form-control"
                                   name="keyDate" id="keyDate">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="year" class="control-label col-md-3">入学年份</label>
                        <div class="col-md-7">
                            <select class="form-control" name="year" id="year">
                                <option value="2019">2019</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="type" class="control-label col-md-3">类型</label>
                        <div class="col-md-7">
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