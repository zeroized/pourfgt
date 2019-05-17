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
    <script src="/js/echarts.min.js"></script>
    <script src="/js/chart.js"></script>
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
                <a href="/postgraduate" role="tab" data-toggle="tab">
                    <span class="glyphicon glyphicon-home"></span>&nbsp; 研究生日常管理
                </a>
            </li>
        </ul>
    </div>
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <ol class="breadcrumb" style="margin-bottom:0">
            <li><a href="/">首页</a></li>
            <li><a href="/course">本科生课程管理</a></li>
            <li><a href="/course/2">数据结构2</a></li>
        </ol>
        <ul class="nav nav-pills" style="margin-top: 15px">
            <li role="presentation">
                <a href="/course/2/studentList">
                    学生名单
                </a>
            </li>
            <li role="presentation" class="active">
                <a href="/course/2/record">
                    记录成绩
                </a>
            </li>
            <li role="presentation">
                <a href="/course/2/announcement">
                    发布信息
                </a>
            </li>
            <li role="presentation">
                <a href="/course/2/homework">
                    平时作业
                </a>
            </li>
            <li role="presentation">
                <a href="/course/2/problem">
                    学生问题
                </a>
            </li>
            <li role="presentation">
                <a href="/course/2/discussion">
                    发布研讨
                </a>
            </li>
            <li role="presentation">
                <a href="/course/2/weight">
                    修改权重
                </a>
            </li>
        </ul>
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">记录成绩</div>
            <div class="panel-body">
                <form class="form-horizontal" action="#">
                    <div class="form-group">
                        <label for="student-number" class="col-md-3 control-label">学生学号：</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="student-number"
                                   name="studentId" placeholder="请输入学生学号">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="select2" class="col-md-3 control-label">项目：</label>
                        <div class="col-md-7">
                            <select class="form-control" name="name" id="select2">
                                <option value="出勤" selected>出勤</option>
                                <option value="研讨">研讨</option>
                                <option value="上机">上机</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="grade" class="col-md-3 control-label">分数：</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="grade"
                                   name="score" placeholder="请输入学生分数">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-offset-5 col-md-3">
                            <button type="submit" class="btn btn-primary">确定</button>
                        </div>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>
</body>
</html>