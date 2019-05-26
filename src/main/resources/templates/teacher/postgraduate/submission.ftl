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
                <form class="form-horizontal" action="/teacher/postgraduate/submission" method="get">
                    <div class="form-group">
                        <label for="type" class="control-label col-md-3">学生</label>
                        <div class="col-md-3">
                            <select class="form-control" name="type" id="type">
                                <#list students as student>
                                    <option value="${student.studentId}">${student.studentName}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <button class="btn btn-primary">查看</button>
                        </div>
                    </div>
                </form>
                <hr style="border: 0.5px solid lightgrey">
                <table class="table">
                    <thead>
                    <tr>
                        <td>提交日期</td>
                        <td>类型</td>
                        <td>期望回复日期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign types=["论文","文件","文档","源代码"]>
                    <#list submissions as submission>
                        <tr>
                            <td>${submission.submitDate?number_to_date}</td>
                            <td>${types[submission.type]}</td>
                            <#if submission.notifyDate==-1>
                                <td>无</td>
                            <#else>
                                <td>${submission.notifyDate?number_to_date}</td>
                            </#if>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#submissionDetail"
                                        data-id="${submission.id}"
                                >查看文件
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
<div class="modal fade" id="submissionDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">提交详情</h4>
            </div>
            <div class="modal-body">
                <h4>发布时间</h4>
                <p id="submission-create"></p>
                <h4>附件</h4>
                <a id="submission-file"></a>
                <form class="form-horizontal"
                      action="/teacher/postgraduate/respondSubmission" method="post" id="responseForm">
                    <input type="hidden" name="submissionId" id="submissionId">
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
                <p class="resp" id="submission-response"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#submissionDetail').on('show.bs.modal', function (event) {
        var modal = $(this);
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        modal.find("#submissionId").val(id);
        modal.find(".resp").hide();
        //TODO adding ajax query here
        $.getJSON("/teacher/postgraduate/getSubmission?id=" + id, function (data) {
            var types = ["论文", "文件", "文档", "源代码"];
            var submission = data;
            var createTime = new Date();
            createTime.setTime(submission.submitTime);
            modal.find("#submission-create").text(createTime);
            if (!submission.hasFile) {
                modal.find("#submission-file").text("无附件");
            } else {
                modal.find("#submission-file").text("下载");
                modal.find("#submission-file").attr("href", "/teacher/course/getAnnouncementFile?id=" + id);
            }
            if (submission.hasResponse) {
                modal.find("#responseForm").hide();
                modal.find("#submission-response").text(submission.response);
                modal.find(".resp").show();
            }

        });
    });
</script>
</body>
</html>