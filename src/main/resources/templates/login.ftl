<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#include "layout/resource.ftl">
</head>
<body>
<div class="row">
    <div class="col-md-4 col-md-offset-4">
        <form action="/signIn" method="post">
            <div class="form-group">
                <label for="userId" class="control-label">学号/工号</label>
                <input type="text" class="form-control" name="userId" id="userId">
            </div>
            <div class="form-group">
                <label for="password" class="control-label">密码</label>
                <input type="password" class="form-control" name="password" id="password">
            </div>
            <div class="form-group">
                <div class="col-md-3 col-md-offset-3">
                    <button type="submit" class="btn btn-primary">登陆</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>