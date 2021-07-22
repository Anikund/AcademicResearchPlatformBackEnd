###使用方法


为了保证数据jpa的更新方式被设置成了update，如果不怕丢失数据可以在application.properties
里将Jpa方式设置为create（也就是update->create)，防止造成被删除字段仍然保留在数据库中。

先执行单元测试：
src->test->java->com->acdamic...->service.impl里的
PermissionServiceImplTest里面的唯一方法，
该方法执行成功后会添加我们用到的所有权限，也会创建一个具有super超级权限的admin角色，
同时会创建一个超级管理员账号：
username:admin
password:admin

