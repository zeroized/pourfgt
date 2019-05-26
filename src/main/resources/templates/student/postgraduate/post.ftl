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
        <#include "../../layout/studentPostgraduateNav.ftl">
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">发布</div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>标题</td>
                        <td>发布日期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list posts as post>
                        <tr>
                            <td>${post.title}</td>
                            <td>${post.createTime?number_to_date}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#postDetail"
                                        data-id="${post.id}"
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
<div class="modal fade" id="postDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
                <h4>提醒日期</h4>
                <p id="post-notification"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#postDetail').on('show.bs.modal', function (event) {
        var modal = $(this);
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        // var courseId=button.data("queryCourse");
        //TODO adding ajax query here
        $.getJSON("/student/postgraduate/getPost?id=" + id, function (data) {
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
                modal.find("#post-file").attr("href", "/teacher/graduation/getPostFile?id=" + id);
            }
            if (!post.hasNotification) {
                modal.find("#post-notification").text("无");
            } else {
                var notifyDate = new Date();
                notifyDate.setTime(post.notifyDate);
                modal.find("#post-notification").text(notifyDate);
            }
        });
    });
</script>
</body>
</html>