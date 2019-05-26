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
            <div class="panel-heading">学生消息</div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>提交时间</td>
                        <td>期望回复时间</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list messages as message>
                        <tr>
                            <td>${message.studentId}</td>
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
                                >查看问题
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
                <h4 class="modal-title" id="myModalLabel">信息详情</h4>
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
                <form class="form-horizontal"
                      action="/teacher/postgraduate/respond" method="post" id="responseForm">
                    <input type="hidden" name="messageId" id="messageId">
                    <div class="form-group">
                        <label for="response" class="control-label col-md-3">回复</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control"
                                   name="response" id="response">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-3 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">回复</button>
                        </div>
                    </div>
                </form>
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
        modal.find("#messageId").val(id);
        modal.find(".resp").hide();
        //TODO adding ajax query here
        $.getJSON("/teacher/postgraduate/getMessage?id=" + id, function (data) {
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
                modal.find("#responseForm").hide();
                modal.find("#message-response").text(message.response);
                modal.find(".resp").show();
            } else {
                modal.find("#responseForm").show();
                modal.find(".resp").hide();
            }

        });
    });
</script>
</body>
</html>