package System;

import JDBC.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentManager {

    private Connection conn;
    private DormitoryManager dormitoryManager;

    public StudentManager(DormitoryManager dormitoryManager) {
        this.dormitoryManager = dormitoryManager;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=dormitorymanagementsystem;trustServerCertificate=true", "sa", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 添加学生信息
    public void addStudent(String studentNumber, String name, String gender, int age, String department, int grade, String phone, String bedNumber, String feePaid, int dormitoryId) throws SQLException {
        String sql = "INSERT INTO Student (student_number, name, gender, age, department, grade, phone, bed_number, fee_paid, dormitory_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
            if (dormitory == null) {
                throw new SQLException("宿舍ID不存在。");
            }

            int bedNumberInt = Integer.parseInt(bedNumber);
            if (bedNumberInt < 1 || bedNumberInt > dormitory.getBedCount()) {
                throw new SQLException("床号无效，必须在1到" + dormitory.getBedCount() + "之间。");
            }

            if (!dormitoryManager.isBedNumberAvailable(dormitoryId, bedNumber)) {
                throw new SQLException("床号已被占用。");
            }

            stmt.setString(1, studentNumber);
            stmt.setString(2, name);
            stmt.setString(3, gender);
            stmt.setInt(4, age);
            stmt.setString(5, department);
            stmt.setInt(6, grade);
            stmt.setString(7, phone);
            stmt.setString(8, bedNumber);
            stmt.setString(9, feePaid);
            stmt.setInt(10, dormitoryId);

            int rowsInserted = stmt.executeUpdate();
            conn.commit();  // 显式提交事务

            if (rowsInserted > 0) {
                System.out.println("Student added successfully!");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // 打印异常堆栈跟踪
            throw ex;  // 重新抛出异常，以便调用者处理
        }
    }

    // 更新学生信息
    public void updateStudent(Student student, boolean ignoreBedCheck) throws SQLException {
        String sql = "UPDATE Student SET student_number = ?, name = ?, gender = ?, age = ?, department = ?, grade = ?, phone = ?, bed_number = ?, fee_paid = ?, dormitory_id = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            Dormitory dormitory = dormitoryManager.getDormitoryById(student.getDormitoryId());
            if (dormitory == null) {
                throw new SQLException("宿舍ID不存在。");
            }

            // 在不忽略床号检查的情况下进行床号检查
            if (!ignoreBedCheck && !student.getBedNumber().isEmpty()) {
                int bedNumberInt = Integer.parseInt(student.getBedNumber());
                if (bedNumberInt < 1 || bedNumberInt > dormitory.getBedCount()) {
                    throw new SQLException("床号无效，必须在1到" + dormitory.getBedCount() + "之间。");
                }

                if (!dormitoryManager.isBedNumberAvailable(student.getDormitoryId(), student.getBedNumber()) && !isSameBed(student)) {
                    throw new SQLException("床号已被占用。");
                }
            }

            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getGender());
            stmt.setInt(4, student.getAge());
            stmt.setString(5, student.getDepartment());
            stmt.setInt(6, student.getGrade());
            stmt.setString(7, student.getPhone());
            stmt.setString(8, student.getBedNumber());
            stmt.setString(9, student.getFeePaid());
            stmt.setInt(10, student.getDormitoryId());
            stmt.setInt(11, student.getStudentId());

            int rowsUpdated = stmt.executeUpdate();
            conn.commit();  // 显式提交事务

            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // 打印异常堆栈跟踪
            throw ex;  // 重新抛出异常，以便调用者处理
        }
    }

    // 删除学生信息
    public void deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM Student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);

            int rowsDeleted = stmt.executeUpdate();
            conn.commit();  // 显式提交事务

            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // 打印异常堆栈跟踪
            throw ex;  // 重新抛出异常，以便调用者处理
        }
    }

    // 根据学生姓名查询学生信息
    public List<Student> getStudentsByName(String name) throws SQLException {
        String sql = "SELECT * FROM Student WHERE name LIKE ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_number"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getInt("grade"),
                            rs.getString("phone"),
                            rs.getString("bed_number"),
                            rs.getString("fee_paid"),
                            rs.getInt("dormitory_id")
                    );
                    students.add(student);
                }
            }
        }

        if (students.isEmpty()) {
            System.out.println("No student found with the given name.");
        } else {
            System.out.println("Student found:");
        }

        return students;
    }

    // 根据学生学号查询学生信息
    public List<Student> getStudentsByNumber(String studentNumber) throws SQLException {
        String sql = "SELECT * FROM Student WHERE student_number = ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_number"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getInt("grade"),
                            rs.getString("phone"),
                            rs.getString("bed_number"),
                            rs.getString("fee_paid"),
                            rs.getInt("dormitory_id")
                    );
                    students.add(student);
                }
            }
        }

        if (students.isEmpty()) {
            System.out.println("No student found with the given number.");
        } else {
            System.out.println("Student found:");
        }

        return students;
    }

    // 查询所有学生信息
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_number"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("department"),
                        rs.getInt("grade"),
                        rs.getString("phone"),
                        rs.getString("bed_number"),
                        rs.getString("fee_paid"),
                        rs.getInt("dormitory_id")
                ));
            }
        }

        if (!students.isEmpty()) {
            System.out.println("Students retrieved successfully.");
        } else {
            System.out.println("No students found in the database.");
        }

        return students;
    }

    // 根据宿舍ID查询学生信息
    public List<Student> getStudentsByDormitoryId(int dormitoryId) throws SQLException {
        String sql = "SELECT * FROM Student WHERE dormitory_id = ?";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_number"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getInt("grade"),
                            rs.getString("phone"),
                            rs.getString("bed_number"),
                            rs.getString("fee_paid"),
                            rs.getInt("dormitory_id")
                    );
                    students.add(student);
                }
            }
        }
        return students;
    }

    // 根据学生ID查询学生信息
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        Student student = null;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_number"),
                            rs.getString("name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getString("department"),
                            rs.getInt("grade"),
                            rs.getString("phone"),
                            rs.getString("bed_number"),
                            rs.getString("fee_paid"),
                            rs.getInt("dormitory_id")
                    );
                }
            }
        }
        return student;
    }

    // 辅助方法：检查更新时床号是否与原床号相同
    private boolean isSameBed(Student student) throws SQLException {
        String sql = "SELECT bed_number FROM Student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, student.getStudentId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1).equals(student.getBedNumber());
                }
            }
        }
        return false;
    }

    // 添加用于交换多个学生床号的方法
    public void swapMultipleStudentBeds(Map<Integer, String> studentBedMap) throws SQLException {
        String sql = "UPDATE Student SET bed_number = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, String> entry : studentBedMap.entrySet()) {
                stmt.setString(1, entry.getValue());
                stmt.setInt(2, entry.getKey());
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();

            System.out.println("学生床号交换成功！");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void swapStudentBeds(int studentId1, int studentId2) throws SQLException {
        // 先获取两个学生的当前床号
        Student student1 = getStudentById(studentId1);
        Student student2 = getStudentById(studentId2);

        // 检查是否在同一宿舍
        if (student1.getDormitoryId() != student2.getDormitoryId()) {
            throw new SQLException("Students are not in the same dormitory.");
        }

        // 交换床号
        String tempBed = student1.getBedNumber();
        student1.setBedNumber(student2.getBedNumber());
        student2.setBedNumber(tempBed);

        // 更新数据库
        updateStudentBedNumber(student1.getStudentId(), student1.getBedNumber());
        updateStudentBedNumber(student2.getStudentId(), student2.getBedNumber());
    }

    private void updateStudentBedNumber(int studentId, String newBedNumber) throws SQLException {
        String sql = "UPDATE Student SET bed_number = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newBedNumber);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
            conn.commit();
        }
    }
}
