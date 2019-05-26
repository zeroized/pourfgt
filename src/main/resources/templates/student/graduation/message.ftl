<!DOCTYPE html>
<html>
<head>
    <title>Shanghai UniversityRender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#include "../../layout/resource.ftl">
</head>
<body style="width:100%;height:100%;">
<#include "../../layout/headerNav.ftl">

<div class="container" style="margin-top: 15px">
    <#include "../../layout/studentLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <#include "../../layout/studentGraduationNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">发送消息</div>
            <div class="panel-body">
                <form class="form-horizontal" enctype="multipart/form-data"
                      action="/student/graduation/addMessage" method="post">
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
                        <label for="notifyDate" class="control-label col-md-3">期望回复日期</label>
                        <div class="col-md-7">
                            <input type="date" class="form-control" name="notifyDate" id="notifyDate">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-2 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">提交</button>
                        </div>
                    </div>
                </form>
                <hr style="border: 0.5px solid lightgrey">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>标题</td>
                        <td>发布日期</td>
                        <td>期望回复日期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list messages as message>
                        <tr>
                            <td>${message.studentId}</td>
                            <td>${message.title}</td>
                            <td>${message.createTime?number_to_date}</td>
                            <#if !message.hasNotification>
                                <td>无</td>
                            <#else>
                                <td>${message.notifyDate?number_to_date}</td>
                            </#if>

                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#messageDetail"
                                        data-id="${message.id}"
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
<div class="modal fade" id="messageDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">发布详情</h4>
            </div>
            <div class="modal-body">
                <h4>标题</h4>
                <p id="message-title"></p>
                <h4>内容</h4>
                <p id="message-content"></p>
                <h4>发布时间</h4>
                <p id="message-create"></p>
                <h4>附件</h4>
                <a id="message-file"></a>
                <h4 class="resp">回复</h4>
                <p class="resp" id="message-response"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#messageDetail').on('show.bs.modal', function (event) {
        var modal = $(this);
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        // var courseId=button.data("queryCourse");
        //TODO adding ajax query here
        $.getJSON("/student/graduation/getMessage?id=" + id, function (data) {
            var message = data;
            var createTime = new Date();
            createTime.setTime(message.createTime);
            modal.find("#message-title").text(message.title);
            modal.find("#message-content").text(message.content);
            modal.find("#message-create").text(createTime);
            if (!message.hasFile) {
                modal.find("#message-file").text("无附件");
            } else {
                modal.find("#message-file").text("下载");
                modal.find("#message-file").attr("href", "/teacher/course/getAnnouncementFile?id=" + id);
            }
            if (message.hasResponse) {
                modal.find("#message-response").text(message.response);
            } else {
                modal.find("#message-response").text("暂无回复");
            }
        });
    });
</script>
</body>
</html>