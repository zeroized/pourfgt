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
                                <option value="18720001">张三</option>
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
                        <td>时间</td>
                        <td>类型</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list submissions as submission>
                        <tr>
                            <td>${submission.submitDate}</td>
                            <td>${submission.type}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#submissionDetail"
                                        data-id="${submission.id}"
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
<div class="modal fade" id="submissionDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">提交详情</h4>
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
    $('#submissionDetail').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        //TODO adding ajax query here
        $.getJSON("/teacher/course/getQuestion/" + id, function (data) {
            var body = modal.find('.modal-body');
            // var types = ["信息", "资料", "作业"];
            // body.append("<h4>标题</h4>" +
            //     "<p>" + data.title + "</p>");
            // body.append("<h4>内容</h4>" +
            //     "<p>" + data.content + "</p>");
            // var createTime = new Date();
            // createTime.setTime(data.createTime);
            // body.append("<h4>发布时间</h4>" +
            //     "<p>" + createTime + "</p>");
            // body.append("<h4>信息类型</h4>" +
            //     "<p>" + types[data.type] + "</p>");
            // if (data.hasFile) {
            //     body.append("<h4>附件</h4>" +
            //         "<a href=\"/course/getAnnouncementFile/" + id + "\">下载</a>");
            // }
            // if (data.hasDeadline) {
            //     var deadline = new Date();
            //     deadline.setTime(data.deadline);
            //     body.append("<h4>截止日期</h4>" +
            //         "<p>" + deadline + "</p>")
            // }

        });
        var modal = $(this);
        // modal.find('.modal-title').text(studentId + "成绩详细");
        // modal.find('#studentIdInput').val(studentId);
    });
</script>
</body>
</html>