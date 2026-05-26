# Dormitory Management System

一个基于 Java Swing 和 MySQL 的学生宿舍管理系统，支持宿舍、学生、学生出入登记、访客出入登记等基础管理功能。

## 功能

- 管理员登录：登录、注册、修改密码、退出登录、注销账号
- 系统概览：统计宿舍、学生、学生出入记录、访客出入记录数量
- 宿舍信息管理：添加、更新、删除、按楼栋查询、列表查看全部宿舍
- 学生信息管理：添加、更新、删除、按姓名查询、列表查看全部学生
- 入住情况管理：查看宿舍入住学生、已入住数量、剩余床位，并校验同宿舍床号不能重复
- 学生出入登记：登记学生进出时间、按学号查询、列表查看全部记录
- 访客出入登记：登记访客信息和进出时间、按电话查询、列表查看全部记录

## 技术栈

- Java 17
- Java Swing
- MySQL 8
- MySQL Connector/J 8.0.33

## 项目结构

```text
src/
  GUI/      Swing 界面
  JDBC/     数据库连接
  System/   实体类和业务管理类
lib/        MySQL JDBC 驱动
```

## 数据库初始化

默认数据库连接配置位于 `src/JDBC/DatabaseConnection.java`：

```java
jdbc:mysql://localhost:3306/dormitorymanagementsystem
user: root
password: 123456
```

初始化库表：

```powershell
mysql -uroot -p123456 --default-character-set=utf8mb4 --execute="source D:/IdeaProjects/DormitoryManagementSystem/dormitorymanagementsystem.sql"
```

插入测试数据：

```powershell
mysql -uroot -p123456 --default-character-set=utf8mb4 --execute="source D:/IdeaProjects/DormitoryManagementSystem/test_data.sql"
```

## 编译与运行

在项目根目录执行：

```powershell
$sources = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib\mysql-connector-j-8.0.33.jar" -d bin $sources
java -cp "bin;lib\mysql-connector-j-8.0.33.jar" GUI.MainFrame
```

也可以直接使用 IntelliJ IDEA 打开项目并运行 `GUI.MainFrame`。

默认管理员账号：

```text
用户名：admin
密码：123456
```

## 测试数据

`test_data.sql` 中包含可重复执行的测试数据：

- 宿舍：A栋、B栋、C栋
- 学生：张三、李四、王五、赵六
- 学生学号：20240001、20240002、20240003、20240004
- 访客电话：13900000001、13900000002、13900000003

## 说明

- “显示所有”功能使用表格弹窗展示数据，便于查看多条记录。
- 日期时间输入使用 Swing 原生 `JSpinner`，不再依赖额外的 jcalendar 包。
- 启动程序后会先进入管理员登录界面，登录后可在“账户”菜单中修改密码、退出登录或注销账号。
