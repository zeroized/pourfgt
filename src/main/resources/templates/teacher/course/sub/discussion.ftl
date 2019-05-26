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
    <#include "../../../layout/teacherLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <#include "../../../layout/teacherCourseNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">发布</div>
            <div class="panel-body">
                <form class="form-horizontal" enctype="multipart/form-data"
                      action="/teacher/course/addDiscussion/${courseId}" method="post">
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
                    <input type="hidden" name="type" value="3">
                    <input type="hidden" name="forward" value="discussion">
                    <div class="form-group">
                        <label for="file" class="control-label col-md-3">附件</label>
                        <div class="col-md-7">
                            <input type="file" name="file" id="file">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="week" class="control-label col-md-3">研讨周次</label>
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
                        <label for="deadline" class="control-label col-md-3">选题截至</label>
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
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list discussions as discussion>
                        <tr>
                            <td>${discussion.title}</td>
                            <td>${discussion.createTime?number_to_date}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#discussionDetail"
                                        data-id="${discussion.id}"
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
<div class="modal fade" id="discussionDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">发布详情</h4>
            </div>
            <div class="modal-body">
                <h4>标题</h4>
                <p id="discussion-title"></p>
                <h4>内容</h4>
                <p id="discussion-content"></p>
                <h4>发布时间</h4>
                <p id="discussion-create"></p>
                <h4>截止时间</h4>
                <p id="discussion-deadline"></p>
                <h4>附件</h4>
                <a id="discussion-file"></a>
                <h4>选题状态</h4>
                <p id="discussion-select"></p>
                <form class="form-horizontal" id="scoreForm"
                      action="/teacher/course/scoreDiscussion" method="post">
                    <input type="hidden" name="courseDBId" id="courseDBId" value="${courseId}">
                    <input type="hidden" name="discussionId" id="discussionId">
                    <div class="form-group">
                        <label for="score" class="control-label col-md-3">分数</label>
                        <div class="col-md-3">
                            <input type="text" class="form-control" name="score" id="score">
                        </div>
                        <div class="col-md-3">
                            <button type="submit" class="btn btn-primary">打分</button>
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
    $('#discussionDetail').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        var modal = $(this);
        modal.find("#discussionId").val(id);
        $.getJSON("/teacher/course/getDiscussion?id=" + id + "&courseId=${courseId}", function (data) {
            var body = modal.find('.modal-body');
            var students = data.students;
            var selected = data.selected;
            var discussion = data.discussion;
            var createTime = new Date();
            createTime.setTime(discussion.createTime);
            modal.find("#discussion-title").text(discussion.title);
            modal.find("#discussion-content").text(discussion.content);
            modal.find("#discussion-create").text(createTime);
            modal.find("#discussion-deadline").text(discussion.deadline);
            if (!discussion.hasFile) {
                modal.find("#discussion-file").text("无附件");
            } else {
                modal.find("#discussion-file").text("下载");
                modal.find("#discussion-file").attr("href", "/teacher/course/getAnnouncementFile?id=" + id);
            }
            if (!selected) {
                modal.find("#discussion-select").text("尚未被选");
                modal.find("#scoreForm").hide();
            } else {
                modal.find("#discussion-select").text("已选");
                modal.find("#scoreForm").show();
            }
        });
    });
</script>
</body>
</html>