<!DOCTYPE html>
<html>
<head>
    <title>Shanghai UniversityRender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <#include "../../../layout/resource.ftl">
</head>
<body style="width:100%;height:100%;">
<script>
    var body = $("body");
    body.on('click', ".addCourseGradeGroup", function () {
        var $content = "";
        $content += '<a href="javascript:;" class="delCourseGradeGroup" >';
        $content += '<span class="glyphicon glyphicon-minus"></span>';
        $content += '</a>';
        $(this).parent().append($content);

        this.remove();

        var formGroup =
            "<div class=\"form-group\">\n" +
            "   <label class=\"col-md-1 col-md-offset-2 control-label\">项目：</label>\n" +
            "   <div class=\"col-md-2\">\n" +
            "       <input type=\"text\" class=\"form-control\" name=\"name\">\n" +
            "   </div>\n" +
            "   <label class=\"col-md-1 control-label\">比例：</label>\n" +
            "   <div class=\"col-md-2\">\n" +
            "       <input type=\"text\" class=\"form-control\" name=\"ratio\">\n" +
            "   </div>\n" +
            "   <div class=\"col-md-1\">\n" +
            "       <a href=\"javascript:;\" class=\"addCourseGradeGroup\">\n" +
            "           <span class=\"glyphicon glyphicon-plus\"></span>\n" +
            "       </a>\n" +
            "   </div>\n" +
            "</div>";

        $("#courseGradeGroup").append(formGroup);
    });
    body.on("click", ".delCourseGradeGroup", function () {
        $(this).parent().parent().remove();
    })
</script>
<#include "../../../layout/headerNav.ftl">
<div class="container" style="margin-top: 15px">
    <#include "../../../layout/studentLeftNav.ftl">
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <#include "../../../layout/studentCourseNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">研讨选题</div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>研讨题</td>
                        <td>研讨周次</td>
                        <td>选题截止周次</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list discussions as discussion>
                        <tr>
                            <td>${discussion.title}</td>
                            <td>${discussion.week}</td>
                            <td>${discussion.deadline}</td>
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
                <form class="form-horizontal"
                      action="/student/course/selectDiscussion" method="post" id="homeworkForm">
                    <input type="hidden" name="courseDBId" value="${courseId}">
                    <input type="hidden" name="discussionId" id="discussionId">
                    <div id="courseGradeGroup">
                        <div class="form-group">
                            <label class="col-md-1 col-md-offset-2 control-label">学号</label>
                            <div class="col-md-2">
                                <input type="text" class="form-control"
                                       name="studentId">
                            </div>
                            <label class="col-md-1 control-label">权重</label>
                            <div class="col-md-2">
                                <input type="text" class="form-control"
                                       name="ratio">
                            </div>
                            <div class="col-md-1">
                                <a href="javascript:" class="addCourseGradeGroup">
                                    <span class="glyphicon glyphicon-plus"></span>
                                </a>
                            </div>
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
    $('#discussionDetail').on('show.bs.modal', function (event) {
        var modal = $(this);
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        modal.find("#discussionId").val(id);
        $.getJSON("/student/course/getDiscussion?id=" + id + "&courseId=${courseId}", function (data) {
            var discussion = data;
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
        });
    });
</script>
</body>
</html>