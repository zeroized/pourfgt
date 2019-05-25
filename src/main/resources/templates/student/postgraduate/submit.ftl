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
        <#include "../../layout/studentPostgraduateNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">提交毕设</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>项目</td>
                        <td>提交日期</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list submissions as submission>
                        <tr>
                            <td>${submission.type}</td>
                            <td>${submission.submitDate}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                <hr style="border: 0.5px solid lightgrey">
                <form class="form-horizontal" enctype="multipart/form-data"
                      action="/student/postgraduate/submitEvent" method="post">
                    <div class="form-group">
                        <label for="type" class="control-label col-md-3">项目</label>
                        <div class="col-md-5">
                            <select class="form-control" name="type" id="type">
                                <option value="0">论文</option>
                                <option value="1">文件</option>
                                <option value="2">文档</option>
                                <option value="3">源码</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="file" class="control-label col-md-3">文件</label>
                        <div class="col-md-5">
                            <input type="file" name="file" id="file">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-3 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">提交</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>