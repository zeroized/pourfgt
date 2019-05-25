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
                            <td>${message.createTime}</td>
                            <td>${message.notifyDate}</td>
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
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#messageDetail').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        //TODO adding ajax query here
        $.getJSON("/teacher/course/getQuestion/" + id, function (data) {
            var body = modal.find('.modal-body');

        });
        var modal = $(this);
        // modal.find('.modal-title').text(studentId + "成绩详细");
        // modal.find('#studentIdInput').val(studentId);
    });
</script>
</body>
</html>