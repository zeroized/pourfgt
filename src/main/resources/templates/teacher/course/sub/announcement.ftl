<!DOCTYPE html>
<html>
<head>
    <title>Shanghai UniversityRender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#include "../../../layout/resource.ftl">
</head>
<body style="width:100%;height:100%;">
<#include "../../../layout/headerNav.ftl">

<div class="container" style="margin-top: 15px">
    <#include "../../../layout/teacherLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <#include "../../../layout/teacherCourseNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">发布</div>
            <div class="panel-body">
                <form class="form-horizontal" enctype="multipart/form-data"
                      action="/teacher/course/post/${courseId}" method="post">
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
                        <label for="type" class="control-label col-md-3">类型</label>
                        <div class="col-md-7">
                            <select class="form-control" name="type" id="type">
                                <option value="0" selected>通知</option>
                                <option value="1">资料</option>
                                <option value="2">作业</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="file" class="control-label col-md-3">附件</label>
                        <div class="col-md-7">
                            <input type="file" name="file" id="file">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="week" class="control-label col-md-3">周次</label>
                        <div class="col-md-3">
                            <div class="input-group">
                                <span class="input-group-addon">第</span>
                                <input type="text" class="form-control"
                                       name="week" id="week">
                                <span class="input-group-addon">周</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="deadline" class="control-label col-md-3">截止周次</label>
                        <div class="col-md-3">
                            <div class="input-group">
                                <span class="input-group-addon">第</span>
                                <input type="text" class="form-control"
                                       name="deadline" id="deadline">
                                <span class="input-group-addon">周</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-2 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">发布</button>
                        </div>
                    </div>
                </form>
                <hr style="border: 0.5px solid lightgrey">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>标题</td>
                        <td>发布日期</td>
                        <td>类型</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign types=["通知","资料","作业","研讨"]>
                    <#list announcements as announcement>
                        <tr>
                            <td>${announcement.title}</td>
                            <td>${announcement.createTime?number_to_date}</td>
                            <td>${types[announcement.type]}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#announcementDetail"
                                        data-id="${announcement.id}"
                                >查看详情
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
<div class="modal fade" id="announcementDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">发布详情</h4>
            </div>
            <div class="modal-body">
                <h4>标题</h4>
                <p id="post-title"></p>
                <h4>类型</h4>
                <p id="post-type"></p>
                <h4>内容</h4>
                <p id="post-content"></p>
                <h4>发布时间</h4>
                <p id="post-create"></p>
                <h4>附件</h4>
                <a id="post-file"></a>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#announcementDetail').on('show.bs.modal', function (event) {
        var modal = $(this);
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        // var courseId=button.data("queryCourse");
        //TODO adding ajax query here
        $.getJSON("/teacher/course/getPost?id=" + id, function (data) {
            var types = ["通知", "资料", "作业", "研讨"];
            var post = data;
            var createTime = new Date();
            createTime.setTime(post.createTime);
            modal.find("#post-title").text(post.title);
            modal.find("#post-content").text(post.content);
            modal.find("#post-create").text(createTime);
            modal.find("#post-type").text(types[post.type]);
            if (!post.hasFile) {
                modal.find("#post-file").text("无附件");
            } else {
                modal.find("#post-file").text("下载");
                modal.find("#post-file").attr("href", "/teacher/course/getAnnouncementFile?id=" + id);
            }

        });
    });
</script>
</body>
</html>