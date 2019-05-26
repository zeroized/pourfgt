<!DOCTYPE html>
<html>
<head>
    <title>Shanghai UniversityRender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <#include "../../../layout/resource.ftl">
</head>
<body style="width:100%;height:100%;">
<#include "../../../layout/headerNav.ftl">
<div class="container" style="margin-top: 15px">
    <#include "../../../layout/studentLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <#include "../../../layout/studentCourseNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">平时作业</div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>周次</td>
                        <td>创建时间</td>
                        <td>截止日期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list homeworkList as homework>
                        <tr>
                            <td>${homework.week}</td>
                            <td>${homework.createTime?number_to_date}</td>
                            <td>${homework.deadline}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#homeworkDetail"
                                        data-id="${homework.id}"
                                >查看作业
                                </button>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="homeworkDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">作业详情</h4>
            </div>
            <div class="modal-body">
                <h4>标题</h4>
                <p id="homework-title"></p>
                <h4>内容</h4>
                <p id="homework-content"></p>
                <h4>周次</h4>
                <p id="homework-week"></p>
                <h4>截至周次</h4>
                <p id="homework-deadline"></p>
                <h4>发布时间</h4>
                <p id="homework-create"></p>
                <h4>附件</h4>
                <a id="homework-file"></a>
                <form class="form-horizontal" enctype="multipart/form-data"
                      action="/student/course/doHomework" method="post" id="homeworkForm">
                    <input type="hidden" name="courseDBId" value="${courseId}">
                    <input type="hidden" name="week" id="week">
                    <div class="form-group">
                        <label for="title" class="control-label col-md-3">标题</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="title" id="title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="content" class="control-label col-md-3">内容</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="content" id="content">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="file" class="control-label col-md-3">附件</label>
                        <div class="col-md-7">
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
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#homeworkDetail').on('show.bs.modal', function (event) {
        var modal = $(this);
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        //TODO adding ajax query here
        $.getJSON("/student/course/getPost?id=" + id, function (data) {
            var homework = data;
            var createTime = new Date();
            createTime.setTime(homework.createTime);
            modal.find("#homework-title").text(homework.title);
            modal.find("#homework-content").text(homework.content);
            modal.find("#homework-create").text(createTime);
            if (!homework.hasFile) {
                modal.find("#homework-file").text("无附件");
            } else {
                modal.find("#homework-file").text("下载");
                modal.find("#homework-file").attr("href", "/teacher/course/getPostFile?id=" + id);
            }
            modal.find("#homework-week").text(homework.week);
            modal.find("#homework-deadline").text(homework.deadline);
            modal.find("#week").val(homework.week);

        });
    });
</script>
</body>
</html>