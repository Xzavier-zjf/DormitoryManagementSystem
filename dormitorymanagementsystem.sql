CREATE DATABASE IF NOT EXISTS `DormitoryManagementSystem`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `DormitoryManagementSystem`;

-- 管理员表
CREATE TABLE IF NOT EXISTS Admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- 宿舍表
CREATE TABLE IF NOT EXISTS Dormitory (
    dormitory_id INT AUTO_INCREMENT PRIMARY KEY,
    building VARCHAR(50),
    floor INT,
    room_number VARCHAR(10),
    bed_count INT,
    price DECIMAL(10, 2)
);

-- 学生表
CREATE TABLE IF NOT EXISTS Student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE,
    name VARCHAR(50),
    gender ENUM('男', '女'),
    age INT,
    department VARCHAR(50),
    grade INT,
    phone VARCHAR(20),
    bed_number VARCHAR(10),
    fee_paid ENUM('是', '否'),
    dormitory_id INT,
    FOREIGN KEY (dormitory_id) REFERENCES Dormitory(dormitory_id)
);

-- 学生出入登记表
CREATE TABLE IF NOT EXISTS EntryExitLog (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20),
    entry_time DATETIME,
    exit_time DATETIME,
    FOREIGN KEY (student_number) REFERENCES Student(student_number)
);

-- 访客信息表
/*CREATE TABLE IF NOT EXISTS VisitorsInfo (
   visitor_id INT AUTO_INCREMENT PRIMARY KEY,
   visitor_name VARCHAR(50),
   visitor_phone VARCHAR(15),
   visit_purpose VARCHAR(255),
   UNIQUE (visitor_phone)
);*/

-- 访客出入登记表
CREATE TABLE IF NOT EXISTS VisitorEntryExitLog (
    visitor_log_id INT AUTO_INCREMENT PRIMARY KEY,
    visitor_name VARCHAR(50),
    visitor_phone VARCHAR(15),
    visitor_purpose VARCHAR(255),
    entry_timestamp DATETIME,
    exit_timestamp DATETIME
    /*FOREIGN KEY (visitor_phone) REFERENCES VisitorsInfo(visitor_phone)*/
);
