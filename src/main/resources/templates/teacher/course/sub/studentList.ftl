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
            <div class="panel-heading">学生名单</div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>姓名</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list students as student>
                        <tr>
                            <td>${student.studentId}</td>
                            <td>${student.studentName}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#scoreDetail"
                                        data-student="${student.studentId}"
                                >查看成绩/提交成绩
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
<div class="modal fade" id="scoreDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">成绩详情</h4>
            </div>
            <div class="modal-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>项目名</td>
                        <td>得分</td>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
                <hr style="border: 0.5px solid lightgrey">
                <form class="form-horizontal" action="/teacher/course/record/${courseId}" method="post">
                    <input type="hidden" name="studentId" id="studentIdInput">
                    <div class="form-group">
                        <label for="eventName" class="col-md-2 control-label">项目名</label>
                        <div class="col-md-6">
                            <select id="eventName" class="form-control" name="eventName">
                                <option value="出勤">出勤</option>
                                <option value="上机">上机</option>
                                <option value="研讨">研讨</option>
                                <option value="期末">期末</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="score" class="col-md-2 control-label">得分</label>
                        <div class="col-md-6">
                            <input id="score" type="text" class="form-control" name="score">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-2 col-md-offset-3">
                            <button type="submit" class="btn btn-primary">提交成绩</button>
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
    $('#scoreDetail').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var studentId = button.data("student");
        var modal = $(this);
        modal.find('tbody').empty();
        // var courseId=button.data("queryCourse");
        //TODO adding ajax query here
        $.getJSON("/teacher/course/${courseId}/getScore/" + studentId, function (data) {
            data.forEach(function (datum) {
                modal.find('tbody').append("" +
                    "<tr>" +
                    "   <td>" + datum.eventName + "</td>" +
                    "   <td>" + datum.score + "</td>" +
                    "</tr>")
            })
        });
        modal.find('#studentIdInput').val(studentId);
    });
</script>
</body>
</html>