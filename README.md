# pourfgt
## 运行环境
- mysql8
- jdk8及以上
- chrome浏览器（推荐使用）
*请不要使用ie浏览器，因为没有经过页面效果的测试
## 准备工作
在mysql中创建一个用户并赋予权限
```
mysql> create database pourfgt; -- Create the new database
mysql> create user 'springuser'@'%' identified by 'spring'; -- Creates the user
mysql> grant all on pourfgt.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database
```
## 编码结构
src  
|-main  
|&emsp;|-java 该目录下放所有的java源代码  
|&emsp;|&emsp;|-cn.edu.shu.pourfgt  
|&emsp;|&emsp;&emsp;|-controller 该目录下放所有的前端页面Controller类，按照模块命名，每一个模块放在同一个controller下  
|&emsp;|&emsp;&emsp;|-dataSource 该目录下放所有与数据库相关的类  
|&emsp;|&emsp;&emsp;|&emsp;|-dao 该目录下放所有表的数据访问对象接口   
|&emsp;|&emsp;&emsp;|&emsp;|-entity 该目录下放所有表的实体类   
|&emsp;|&emsp;&emsp;|-util 该目录下放所有的工具类，注意所有的工具方法必须为静态方法  
|&emsp;|&emsp;&emsp;|-PourfgtApplication.java 启动类  
|&emsp;|-resources 该目录下放所有的配置文件和非java代码的内容，包括静态资源文件、页面模板和各种配置文件  
|&emsp; &emsp;|-static 该目录下存放所有的静态资源，如js、css、图片、字体文件，并按照各个分类分别存放  
|&emsp; &emsp;|-templates 该目录下存放所有的页面模板，模板以.ftl结尾，按照3个模块分别放在三个目录下  
|&emsp; &emsp;|-application.properties spring配置文件  
|-test  
|&emsp;|-java 该目录下放所有的测试代码，注意测试代码中环境已经配置好，无需额外加载资源和配置环境  
## 开发注意事项
- 在开发过程中，如果数据库dao层没有调通，或是数据库表结构没有确定，请在resource/application.properties中设置spring.jpa.hibernate.ddl-auto=create，再开始调试
- 在开发过程中，如果数据库部分已经完成，请在resource/application.properties中设置spring.jpa.hibernate.ddl-auto=update，以便数据库查询能够正确地返回结果以供前端页面和业务逻辑调试
- 页面不要使用单页切换tab的方式进行，如果有多个tab，请分开设置不同的文件名，使用href进行页面跳转
- 页面开发可以使用.html文件直接在chrome中打开进行调试，然后复制到.ftl中
- 从html文件转换到.ftl文件时，静态资源的路径需要修改，.ftl加载静态资源使用的是url加载，路径为“/js/...js”，“/css/...css”等  
- 先启动一次项目，然后执行项目根目录下的pourfgt_user.sql文件，即可添加3种不同的登陆用户开始测试，具体用户名和密码见sql文件