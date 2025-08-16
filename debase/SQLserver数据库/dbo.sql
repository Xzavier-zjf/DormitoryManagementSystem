/*
 Navicat Premium Data Transfer

 Source Server         : zzz
 Source Server Type    : SQL Server
 Source Server Version : 16001000
 Source Host           : localhost:1433
 Source Catalog        : dormitorymanagementsystem
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 16001000
 File Encoding         : 65001

 Date: 16/08/2025 18:01:39
*/


-- ----------------------------
-- Table structure for Admin
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Admin]') AND type IN ('U'))
	DROP TABLE [dbo].[Admin]
GO

CREATE TABLE [dbo].[Admin] (
  [admin_id] int  IDENTITY(1,1) NOT NULL,
  [username] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [password] nvarchar(100) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[Admin] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of Admin
-- ----------------------------
SET IDENTITY_INSERT [dbo].[Admin] ON
GO

INSERT INTO [dbo].[Admin] ([admin_id], [username], [password]) VALUES (N'1', N'admin', N'111')
GO

SET IDENTITY_INSERT [dbo].[Admin] OFF
GO


-- ----------------------------
-- Table structure for dormitory
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[dormitory]') AND type IN ('U'))
	DROP TABLE [dbo].[dormitory]
GO

CREATE TABLE [dbo].[dormitory] (
  [dormitory_id] int  IDENTITY(1,1) NOT NULL,
  [building] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [floor] int  NULL,
  [room_number] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
  [bed_count] int  NULL,
  [price] decimal(10,2)  NULL
)
GO

ALTER TABLE [dbo].[dormitory] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of dormitory
-- ----------------------------
SET IDENTITY_INSERT [dbo].[dormitory] ON
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'1', N'c23', N'1', N'118', N'4', N'2300.00')
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'2', N'c23', N'1', N'119', N'4', N'2300.00')
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'3', N'c10', N'1', N'110', N'4', N'2300.00')
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'4', N'c22', N'1', N'101', N'3', N'3000.00')
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'5', N'c11', N'1', N'111', N'4', N'2300.00')
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'6', N'c22', N'1', N'101', N'2', N'5000.00')
GO

INSERT INTO [dbo].[dormitory] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price]) VALUES (N'7', N'c23', N'2', N'201', N'4', N'3000.00')
GO

SET IDENTITY_INSERT [dbo].[dormitory] OFF
GO


-- ----------------------------
-- Table structure for entryexitlog
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[entryexitlog]') AND type IN ('U'))
	DROP TABLE [dbo].[entryexitlog]
GO

CREATE TABLE [dbo].[entryexitlog] (
  [log_id] int  IDENTITY(1,1) NOT NULL,
  [student_number] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [entry_time] datetime2(0)  NULL,
  [exit_time] datetime2(0)  NULL
)
GO

ALTER TABLE [dbo].[entryexitlog] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of entryexitlog
-- ----------------------------
SET IDENTITY_INSERT [dbo].[entryexitlog] ON
GO

INSERT INTO [dbo].[entryexitlog] ([log_id], [student_number], [entry_time], [exit_time]) VALUES (N'1', N'1001', N'2024-06-27 22:34:35', N'2024-06-27 22:34:41')
GO

INSERT INTO [dbo].[entryexitlog] ([log_id], [student_number], [entry_time], [exit_time]) VALUES (N'2', N'1002', N'2024-07-01 13:31:56', N'2024-07-01 13:31:58')
GO

INSERT INTO [dbo].[entryexitlog] ([log_id], [student_number], [entry_time], [exit_time]) VALUES (N'3', N'1003', N'2024-07-03 17:36:22', N'2024-07-03 17:36:25')
GO

INSERT INTO [dbo].[entryexitlog] ([log_id], [student_number], [entry_time], [exit_time]) VALUES (N'7', N'1001', N'2024-07-03 19:09:05', N'2024-07-03 19:09:07')
GO

SET IDENTITY_INSERT [dbo].[entryexitlog] OFF
GO


-- ----------------------------
-- Table structure for NotFullyOccupiedDormitories
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[NotFullyOccupiedDormitories]') AND type IN ('U'))
	DROP TABLE [dbo].[NotFullyOccupiedDormitories]
GO

CREATE TABLE [dbo].[NotFullyOccupiedDormitories] (
  [dormitory_id] int  NULL,
  [building] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [floor] int  NULL,
  [room_number] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
  [bed_count] int  NULL,
  [price] decimal(10,2)  NULL,
  [current_occupancy] int  NULL,
  [remaining_beds] int  NULL
)
GO

ALTER TABLE [dbo].[NotFullyOccupiedDormitories] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of NotFullyOccupiedDormitories
-- ----------------------------
INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'1', N'c23', N'1', N'118', N'4', N'2300.00', N'3', N'1')
GO

INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'2', N'c23', N'1', N'119', N'4', N'2300.00', N'1', N'3')
GO

INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'3', N'c10', N'1', N'110', N'4', N'2300.00', N'1', N'3')
GO

INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'4', N'c22', N'1', N'101', N'3', N'3000.00', N'0', N'3')
GO

INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'5', N'c11', N'1', N'111', N'4', N'2300.00', N'0', N'4')
GO

INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'6', N'c22', N'1', N'101', N'2', N'7000.00', N'0', N'2')
GO

INSERT INTO [dbo].[NotFullyOccupiedDormitories] ([dormitory_id], [building], [floor], [room_number], [bed_count], [price], [current_occupancy], [remaining_beds]) VALUES (N'7', N'c23', N'2', N'201', N'4', N'2300.00', N'1', N'3')
GO


-- ----------------------------
-- Table structure for student
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[student]') AND type IN ('U'))
	DROP TABLE [dbo].[student]
GO

CREATE TABLE [dbo].[student] (
  [student_id] int  IDENTITY(1,1) NOT NULL,
  [student_number] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [name] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [gender] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [age] int  NULL,
  [department] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [grade] int  NULL,
  [phone] nvarchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [bed_number] nvarchar(10) COLLATE Chinese_PRC_CI_AS  NULL,
  [fee_paid] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [dormitory_id] int  NULL
)
GO

ALTER TABLE [dbo].[student] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of student
-- ----------------------------
SET IDENTITY_INSERT [dbo].[student] ON
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'3', N'1002', N'李四', N'男', N'20', N'计算机工程学院', N'2022', N'145465', N'2', N'是', N'1')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'4', N'1001', N'张三', N'男', N'20', N'计算机工程学院', N'2022', N'125624', N'1', N'是', N'1')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'5', N'1003', N'王五', N'男', N'20', N'计算机工程学院', N'2022', N'135675', N'2', N'是', N'2')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'6', N'1004', N'刘芳', N'女', N'20', N'计算机工程学院', N'2022', N'146235', N'1', N'是', N'3')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'7', N'1005', N'郭杨', N'男', N'20', N'计算机工程学院', N'2022', N'1241561', N'3', N'是', N'1')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'8', N'1006', N'岳飞', N'男', N'20', N'大数据学院', N'2022', N'151651', N'1', N'是', N'7')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'9', N'1008', N'王零', N'男', N'20', N'计算机工程需要', N'2022', N'14165165', N'4', N'是', N'1')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'11', N'1009', N'安星', N'男', N'20', N'计算机工程学院', N'2022', N'131515', N'1', N'是', N'2')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'12', N'1010', N'西野', N'男', N'19', N'计算机工程学院', N'2022', N'124165', N'3', N'是', N'2')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'14', N'1011', N'黄昏', N'男', N'19', N'计算机工程学院', N'2022', N'124151', N'4', N'是', N'2')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'15', N'1012', N'玛德', N'男', N'20', N'计算机工程学院', N'2022', N'1515151', N'1', N'是', N'4')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'16', N'1013', N'德莎', N'男', N'20', N'计算机工程学院', N'2022', N'1356416', N'2', N'是', N'4')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'18', N'1014', N'荣威', N'男', N'20', N'计算机工程学院', N'2021', N'141513', N'2', N'是', N'3')
GO

INSERT INTO [dbo].[student] ([student_id], [student_number], [name], [gender], [age], [department], [grade], [phone], [bed_number], [fee_paid], [dormitory_id]) VALUES (N'19', N'1015', N'李楠', N'男', N'19', N'计算机工程学院', N'2022', N'1461677', N'4', N'是', N'3')
GO

SET IDENTITY_INSERT [dbo].[student] OFF
GO


-- ----------------------------
-- Table structure for visitorentryexitlog
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[visitorentryexitlog]') AND type IN ('U'))
	DROP TABLE [dbo].[visitorentryexitlog]
GO

CREATE TABLE [dbo].[visitorentryexitlog] (
  [visitor_log_id] int  IDENTITY(1,1) NOT NULL,
  [visitor_name] nvarchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [visitor_phone] nvarchar(15) COLLATE Chinese_PRC_CI_AS  NULL,
  [visitor_purpose] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [entry_timestamp] datetime2(0)  NULL,
  [exit_timestamp] datetime2(0)  NULL
)
GO

ALTER TABLE [dbo].[visitorentryexitlog] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Records of visitorentryexitlog
-- ----------------------------
SET IDENTITY_INSERT [dbo].[visitorentryexitlog] ON
GO

INSERT INTO [dbo].[visitorentryexitlog] ([visitor_log_id], [visitor_name], [visitor_phone], [visitor_purpose], [entry_timestamp], [exit_timestamp]) VALUES (N'1', N'张五', N'123566', N'接送学生', N'2024-06-20 20:18:29', N'2024-06-20 20:18:58')
GO

INSERT INTO [dbo].[visitorentryexitlog] ([visitor_log_id], [visitor_name], [visitor_phone], [visitor_purpose], [entry_timestamp], [exit_timestamp]) VALUES (N'2', N'刘晶', N'12345689', N'维修宿舍空调', N'2024-06-27 21:25:52', N'2024-06-27 21:25:55')
GO

INSERT INTO [dbo].[visitorentryexitlog] ([visitor_log_id], [visitor_name], [visitor_phone], [visitor_purpose], [entry_timestamp], [exit_timestamp]) VALUES (N'3', N'莫健', N'14516166', N'派送物品', N'2024-06-27 00:00:00', N'2024-06-27 21:53:36')
GO

INSERT INTO [dbo].[visitorentryexitlog] ([visitor_log_id], [visitor_name], [visitor_phone], [visitor_purpose], [entry_timestamp], [exit_timestamp]) VALUES (N'4', N'徐宽', N'124151', N'帮学生拿行李', N'2024-06-30 20:28:58', N'2024-06-30 20:29:07')
GO

INSERT INTO [dbo].[visitorentryexitlog] ([visitor_log_id], [visitor_name], [visitor_phone], [visitor_purpose], [entry_timestamp], [exit_timestamp]) VALUES (N'5', N'叶尘', N'1426235', N'探望学生', N'2024-06-30 20:41:49', N'2024-06-30 20:41:55')
GO

INSERT INTO [dbo].[visitorentryexitlog] ([visitor_log_id], [visitor_name], [visitor_phone], [visitor_purpose], [entry_timestamp], [exit_timestamp]) VALUES (N'6', N'李凡', N'154161', N'送学生看病', N'2024-07-01 13:37:44', N'2024-07-01 13:38:19')
GO

SET IDENTITY_INSERT [dbo].[visitorentryexitlog] OFF
GO


-- ----------------------------
-- View structure for View_NotFullyOccupiedDormitories
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[View_NotFullyOccupiedDormitories]') AND type IN ('V'))
	DROP VIEW [dbo].[View_NotFullyOccupiedDormitories]
GO

CREATE VIEW [dbo].[View_NotFullyOccupiedDormitories] AS SELECT d.dormitory_id, d.building, d.floor, d.room_number, d.bed_count, d.price, 
       (SELECT COUNT(*) FROM student WHERE dormitory_id = d.dormitory_id) AS current_occupancy,
       (d.bed_count - (SELECT COUNT(*) FROM student WHERE dormitory_id = d.dormitory_id)) AS remaining_beds
FROM dormitory d
WHERE (SELECT COUNT(*) FROM student WHERE dormitory_id = d.dormitory_id) < d.bed_count;
GO


-- ----------------------------
-- Auto increment value for Admin
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[Admin]', RESEED, 5)
GO


-- ----------------------------
-- Uniques structure for table Admin
-- ----------------------------
ALTER TABLE [dbo].[Admin] ADD CONSTRAINT [UQ__Admin__F3DBC572491D6DC3] UNIQUE NONCLUSTERED ([username] ASC)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Admin
-- ----------------------------
ALTER TABLE [dbo].[Admin] ADD CONSTRAINT [PK__Admin__43AA4141E570EB82] PRIMARY KEY CLUSTERED ([admin_id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for dormitory
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[dormitory]', RESEED, 7)
GO


-- ----------------------------
-- Primary Key structure for table dormitory
-- ----------------------------
ALTER TABLE [dbo].[dormitory] ADD CONSTRAINT [PK__dormitor__DB201439068421FC] PRIMARY KEY CLUSTERED ([dormitory_id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for entryexitlog
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[entryexitlog]', RESEED, 7)
GO


-- ----------------------------
-- Indexes structure for table entryexitlog
-- ----------------------------
CREATE NONCLUSTERED INDEX [student_number]
ON [dbo].[entryexitlog] (
  [student_number] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table entryexitlog
-- ----------------------------
ALTER TABLE [dbo].[entryexitlog] ADD CONSTRAINT [PK__entryexi__9E2397E04A678570] PRIMARY KEY CLUSTERED ([log_id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for student
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[student]', RESEED, 19)
GO


-- ----------------------------
-- Indexes structure for table student
-- ----------------------------
CREATE UNIQUE NONCLUSTERED INDEX [student_number]
ON [dbo].[student] (
  [student_number] ASC
)
GO

CREATE NONCLUSTERED INDEX [dormitory_id]
ON [dbo].[student] (
  [dormitory_id] ASC
)
GO


-- ----------------------------
-- Primary Key structure for table student
-- ----------------------------
ALTER TABLE [dbo].[student] ADD CONSTRAINT [PK__student__2A33069ADE65ECC9] PRIMARY KEY CLUSTERED ([student_id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Auto increment value for visitorentryexitlog
-- ----------------------------
DBCC CHECKIDENT ('[dbo].[visitorentryexitlog]', RESEED, 6)
GO


-- ----------------------------
-- Primary Key structure for table visitorentryexitlog
-- ----------------------------
ALTER TABLE [dbo].[visitorentryexitlog] ADD CONSTRAINT [PK__visitore__4D161546E99A2849] PRIMARY KEY CLUSTERED ([visitor_log_id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Foreign Keys structure for table entryexitlog
-- ----------------------------
ALTER TABLE [dbo].[entryexitlog] ADD CONSTRAINT [entryexitlog_ibfk_1] FOREIGN KEY ([student_number]) REFERENCES [dbo].[student] ([student_number]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO


-- ----------------------------
-- Foreign Keys structure for table student
-- ----------------------------
ALTER TABLE [dbo].[student] ADD CONSTRAINT [student_ibfk_1] FOREIGN KEY ([dormitory_id]) REFERENCES [dbo].[dormitory] ([dormitory_id]) ON DELETE NO ACTION ON UPDATE NO ACTION
GO

