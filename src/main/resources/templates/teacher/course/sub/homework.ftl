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
            <div class="panel-heading">平时作业</div>
            <div class="panel-body">
                <form class="form-horizontal"
                      action="/teacher/course/${courseId}/homework" method="get">
                    <div class="form-group">
                        <label for="week" class="control-label col-md-2">周次</label>
                        <div class="col-md-3">
                            <select class="form-control" name="week" id="week">
                                <#list 1..10 as index>
                                    <#if index==week>
                                        <option value="${index}" selected>${index}</option>
                                    <#else >
                                        <option value="${index}">${index}</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary">查看</button>
                        </div>
                    </div>
                </form>
                <hr style="border: 0.5px solid lightgrey">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>提交时间</td>
                        <td>是否逾期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list homeworkList as homework>
                        <tr>
                            <td>${homework.studentId}</td>
                            <td>${homework.createTime?number_to_date}</td>
                            <td>${homework.overdue?string('是','否')}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#homeworkDetail"
                                        data-id="${homework.id}"
                                >批改作业
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
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    //TODO to be tested
    $('#homeworkDetail').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        //TODO adding ajax query here
        $.getJSON("/teacher/course/getHomework/" + id, function (data) {
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
                modal.find("#homework-file").attr("href", "/teacher/course/getHomeworkFile?id=" + id);
            }

        });
    });
</script>
</body>
</html>