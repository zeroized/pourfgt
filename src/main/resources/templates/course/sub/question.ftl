<!DOCTYPE html>
<html>
<head>
    <title>Shanghai UniversityRender</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="/js/jquery-3.3.1.js"></script>
    <!-- 包括所有已编译的插件 -->
    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/bootstrap-tab.js"></script>
</head>
<body style="width:100%;height:100%;">
<nav class="navbar navbar-default navbar-static-top" role="navigation"
     style="margin-bottom: 0">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target=".navbar-collapse">
                <span class="sr-only">导航栏开关<!-- 导航栏开关--> </span>
                <span class="icon-bar"></span> <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">
                <span>
                    <img src="/img/logo-navbar.png" style="width:200px;height:45px"/>
                </span>
            </a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">用户<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#">修改密码</a></li>
                    <li role="separator" class="divider"></li>
                    <li><a href="#">注销</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">提醒<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#">x个待回复问题</a></li>
                    <li><a href="#">x个待查看论文</a></li>
                    <li><a href="#">x个新提交的平时作业</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <ul class="nav navbar-top-links navbar-right">

        <li class="dropdown ark-TODO">
            <a class="dropdown-toggle"
               data-toggle="dropdown" href="#">
                <i class="fa fa-tasks fa-fw"></i>
                <i class="fa fa-caret-down"></i>
            </a>
        </li>


    </ul>
</nav>

<div class="container" style="margin-top: 15px">
    <div class="col-md-2 col-sm-2 col-lg-2">
        <ul class=" nav nav-pills nav-stacked">
            <li role="presentation" class="active">
                <a href="#">
                    <span class="glyphicon glyphicon-globe"></span>&nbsp; 本科课程管理
                </a>
            </li>
            <li role="presentation">
                <a href="/graduationDesign">
                    <span class="glyphicon glyphicon-th-large"></span>&nbsp; 本科毕设管理
                </a>
            </li>
            <li role="presentation">
                <a href="/postgraduate">
                    <span class="glyphicon glyphicon-home"></span>&nbsp; 研究生日常管理
                </a>
            </li>
        </ul>
    </div>
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <ol class="breadcrumb" style="margin-bottom:0">
            <li><a href="/">首页</a></li>
            <li><a href="/course">本科生课程管理</a></li>
            <li><a href="/course/${courseId}">数据结构2</a></li>
        </ol>
        <ul class="nav nav-pills" style="margin-top: 15px">
            <li role="presentation">
                <a href="/course/${courseId}/studentList">
                    学生名单
                </a>
            </li>
            <li role="presentation">
                <a href="/course/${courseId}/announcement">
                    发布
                </a>
            </li>
            <li role="presentation">
                <a href="/course/${courseId}/homework">
                    平时作业
                </a>
            </li>
            <li role="presentation" class="active">
                <a href="/course/${courseId}/question">
                    学生问题
                </a>
            </li>
            <li role="presentation">
                <a href="/course/${courseId}/discussion">
                    研讨管理
                </a>
            </li>
            <li role="presentation">
                <a href="/course/${courseId}/weight">
                    修改权重
                </a>
            </li>
        </ul>
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">平时作业</div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <td>学号</td>
                        <td>提交时间</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list questions as question>
                        <tr>
                            <td>${question.studentId}</td>
                            <td>${question.createTime}</td>
                            <td>
                                <button class="btn btn-primary"
                                        data-toggle="modal" data-target="#questionDetail"
                                        data-id="${question.id}"
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
<div class="modal fade" id="questionDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">作业详情</h4>
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
    $('#questionDetail').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data("id");
        //TODO adding ajax query here
        $.getJSON("/course/getQuestion/" + id, function (data) {
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