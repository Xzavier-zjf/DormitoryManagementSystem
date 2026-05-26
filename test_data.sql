CREATE DATABASE IF NOT EXISTS `DormitoryManagementSystem`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `DormitoryManagementSystem`;

INSERT INTO Dormitory (dormitory_id, building, floor, room_number, bed_count, price) VALUES
    (1, 'A栋', 1, '101', 4, 1200.00),
    (2, 'A栋', 2, '205', 6, 1000.00),
    (3, 'B栋', 3, '302', 4, 1350.00),
    (4, 'C栋', 5, '508', 8, 850.00)
ON DUPLICATE KEY UPDATE
    building = VALUES(building),
    floor = VALUES(floor),
    room_number = VALUES(room_number),
    bed_count = VALUES(bed_count),
    price = VALUES(price);

INSERT INTO Student (
    student_id,
    student_number,
    name,
    gender,
    age,
    department,
    grade,
    phone,
    bed_number,
    fee_paid,
    dormitory_id
) VALUES
    (1, '20240001', '张三', '男', 20, '计算机系', 2022, '13800000001', '1号床', '是', 1),
    (2, '20240002', '李四', '女', 19, '软件工程系', 2023, '13800000002', '2号床', '否', 2),
    (3, '20240003', '王五', '男', 21, '网络工程系', 2021, '13800000003', '3号床', '是', 3),
    (4, '20240004', '赵六', '女', 20, '信息管理系', 2022, '13800000004', '4号床', '是', 4)
ON DUPLICATE KEY UPDATE
    student_number = VALUES(student_number),
    name = VALUES(name),
    gender = VALUES(gender),
    age = VALUES(age),
    department = VALUES(department),
    grade = VALUES(grade),
    phone = VALUES(phone),
    bed_number = VALUES(bed_number),
    fee_paid = VALUES(fee_paid),
    dormitory_id = VALUES(dormitory_id);

INSERT INTO EntryExitLog (log_id, student_number, entry_time, exit_time) VALUES
    (1, '20240001', '2026-05-20 08:10:00', '2026-05-20 18:30:00'),
    (2, '20240002', '2026-05-21 07:55:00', '2026-05-21 21:10:00'),
    (3, '20240003', '2026-05-22 09:20:00', '2026-05-22 17:45:00'),
    (4, '20240001', '2026-05-23 08:05:00', '2026-05-23 20:00:00')
ON DUPLICATE KEY UPDATE
    student_number = VALUES(student_number),
    entry_time = VALUES(entry_time),
    exit_time = VALUES(exit_time);

INSERT INTO VisitorEntryExitLog (
    visitor_log_id,
    visitor_name,
    visitor_phone,
    visitor_purpose,
    entry_timestamp,
    exit_timestamp
) VALUES
    (1, '陈老师', '13900000001', '检查宿舍卫生', '2026-05-20 10:00:00', '2026-05-20 10:45:00'),
    (2, '刘先生', '13900000002', '探访学生张三', '2026-05-21 15:20:00', '2026-05-21 16:10:00'),
    (3, '黄女士', '13900000003', '维修登记', '2026-05-22 09:30:00', '2026-05-22 11:00:00')
ON DUPLICATE KEY UPDATE
    visitor_name = VALUES(visitor_name),
    visitor_phone = VALUES(visitor_phone),
    visitor_purpose = VALUES(visitor_purpose),
    entry_timestamp = VALUES(entry_timestamp),
    exit_timestamp = VALUES(exit_timestamp);
