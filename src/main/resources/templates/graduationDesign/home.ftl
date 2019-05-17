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
                <a href="/">
                    <span class="glyphicon glyphicon-globe"></span>&nbsp; 本科课程管理
                </a>
            </li>
            <li role="presentation">
                <a href="/pos">
                    <span class="glyphicon glyphicon-th-large"></span>&nbsp; 本科毕设管理
                </a>
            </li>
            <li role="presentation">
                <a href="#tab3" role="tab" data-toggle="tab">
                    <span class="glyphicon glyphicon-home"></span>&nbsp; 研究生日常管理
                </a>
            </li>
        </ul>
    </div>
    <div class="col-md-10 col-sm-10 col-lg-10 ">
        <ol class="breadcrumb" style="margin-bottom:0">
            <li><a href="home.ftl">首页</a></li>
        </ol>
        <div class="tab-content">
            <div class="active tab-pane" id="tab1" role="tabpanel">
                <ul class="nav nav-pills" role="tablist">
                    <li role="presentation" class="active">
                        <a href="#tab1-1" role="tab" data-toggle="tab">
                            &nbsp; 课程列表
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#tab1-2" role="tab" data-toggle="tab">
                            &nbsp; 新建课程
                        </a>
                    </li>

                </ul>
                <div class="tab-content">
                    <div class="active tab-pane" id="tab1-1" role="tabpanel">
                        <div class="panel panel-default">
                            <div class="panel-heading">课程列表</div>
                            <div class="panel-body">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <td>课程号</td>
                                        <td>课程名</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>0083XXXX</td>
                                        <td><a href="user.html">课程名1</a></td>
                                    </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab1-2" role="tabpanel">
                        <div class="panel panel-default">
                            <div class="panel-heading">新建课程</div>
                            <div class="panel-body">
                                </br></br>
                                <form class="form-horizontal" action="#">
                                    <div class="form-group">
                                        <label for="course-number" class="col-md-5 control-label">课程号：</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" id="course-number"
                                                   style="width:350px;" placeholder="请输入课程号">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="course-name" class="col-md-5 control-label">课程名：</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" id="course-name"
                                                   style="width:350px;" placeholder="请输入课程名">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="time" class="col-md-5 control-label">课程类型：</label>
                                        <div class="col-md-7">
                                            <div class="checkbox">
                                                <label class="checkbox-inline">
                                                    <input type="checkbox" value="">
                                                    研讨
                                                </label>
                                                <label class="checkbox-inline">
                                                    <input type="checkbox" value="">
                                                    上机
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="time" class="col-md-5 control-label">研讨形式：</label>
                                        <div class="col-md-7">
                                            <div class="radio">
                                                <label class="radio-inline">
                                                    <input type="radio" name="radio1" id="" value="" checked>
                                                    单人
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio" name="radio1" id="" value="">
                                                    小组
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputfile" class="col-md-5 control-label">学生名单：</label>
                                        <div class="col-md-7">
                                            <input type="file" id="inputfile">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="select1" class="col-md-5 control-label">给分形式：</label>
                                        <div class="col-md-7">
                                            <select class="form-control" id="select1" style="width:350px;">
                                                <option value="">优-良-中-及格-差</option>
                                                <option value="">A-B-C-D-E</option>
                                                <option value="">分数</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="peacetime-grade" class="col-md-5 control-label">平时成绩占比：</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" id="peacetime-grade"
                                                   style="width:350px;" placeholder="请输入平时成绩占比">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="final-grade" class="col-md-5 control-label">期末成绩占比：</label>
                                        <div class="col-md-7">
                                            <input type="text" class="form-control" id="final-grade"
                                                   style="width:350px;" placeholder="请输入期末成绩占比">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-md-offset-5 col-md-7">
                                            <button type="submit" class="btn btn-primary">确定</button>
                                            &nbsp; &nbsp;
                                            <button type="button" class="btn btn-danger">取消</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>