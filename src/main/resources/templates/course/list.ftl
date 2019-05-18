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

    <!-- /.navbar-header -->

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
        </ol>
        <ul class="nav nav-pills" style="margin-top: 15px">
            <li role="presentation" class="active">
                <a href="/course/list">
                    课程列表
                </a>
            </li>
            <li role="presentation">
                <a href="/course/create">
                    新建课程
                </a>
            </li>

        </ul>
        <div class="panel panel-default" style="margin-top: 15px">
            <div class="panel-heading">课程列表</div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <td>课程号</td>
                        <td>课程名</td>
                        <td>学年</td>
                        <td>学期</td>
                        <td>状态</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#assign semesters=["秋季","冬季","春季","夏季"]>
                    <#list courses as course>
                        <#if course.year==currYear &&course.semester==currSemester>
                            <tr>
                                <td>${course.courseId}</td>
                                <td><a href="/course/${course.id}">${course.courseName}</a></td>
                                <td>${course.year}-${course.year+1}</td>
                                <td>${semesters[course.semester]}</td>
                                <td>激活</td>
                            </tr>
                        <#else >
                            <tr>
                                <td>${course.courseId}</td>
                                <td>${course.courseName}</td>
                                <td>${course.year}-${course.year+1}</td>
                                <td>${semesters[course.semester]}</td>
                                <td>已完成</td>
                            </tr>
                        </#if>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>