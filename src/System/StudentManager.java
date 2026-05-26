package System;

import JDBC.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {

    private DormitoryManager dormitoryManager = new DormitoryManager();

    // 添加学生信息
    public void addStudent(String studentNumber, String name, String gender, int age, String department, int grade, String phone, String bedNumber, String feePaid, int dormitoryId) throws SQLException {
        String normalizedBedNumber = validateStudentPlacement(dormitoryId, bedNumber, 0);

        String sql = "INSERT INTO Student (student_number, name, gender, age, department, grade, phone, bed_number, fee_paid, dormitory_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentNumber);
            stmt.setString(2, name);
            stmt.setString(3, gender);
            stmt.setInt(4, age);
            stmt.setString(5, department);
            stmt.setInt(6, grade);
            stmt.setString(7, phone);
            stmt.setString(8, normalizedBedNumber);
            stmt.setString(9, feePaid);
            stmt.setInt(10, dormitoryId);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                conn.commit();  // 显式提交事务
                System.out.println("Student added successfully!");
            } else {
                throw new SQLException("学生添加失败。");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // 打印异常堆栈跟踪
            throw ex;
        }
    }

    // 更新学生信息
    public void updateStudent(Student student) throws SQLException {
        String normalizedBedNumber = validateStudentPlacement(student.getDormitoryId(), student.getBedNumber(), student.getStudentId());

        String sql = "UPDATE Student SET student_number = ?, name = ?, gender = ?, age = ?, department = ?, grade = ?, phone = ?, bed_number = ?, fee_paid = ?, dormitory_id = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getGender());
            stmt.setInt(4, student.getAge());
            stmt.setString(5, student.getDepartment());
            stmt.setInt(6, student.getGrade());
            stmt.setString(7, student.getPhone());
            stmt.setString(8, normalizedBedNumber);
            stmt.setString(9, student.getFeePaid());
            stmt.setInt(10, student.getDormitoryId());
            stmt.setInt(11, student.getStudentId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                conn.commit();  // 显式提交事务
                System.out.println("Student updated successfully!");
            } else {
                throw new SQLException("未找到指定ID的学生。");
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

            if (rowsDeleted > 0) {
                conn.commit();  // 显式提交事务
                System.out.println("Student deleted successfully!");
            } else {
                throw new SQLException("未找到指定ID的学生。");
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
        return students;
    }

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
        return students;
    }

    public List<Student> getStudentsByDormitoryId(int dormitoryId) throws SQLException {
        String sql = "SELECT * FROM Student WHERE dormitory_id = ? ORDER BY bed_number, student_id";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dormitoryId);
            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return students;
    }

    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
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
        return null;
    }

    private String validateStudentPlacement(int dormitoryId, String bedNumber, int excludedStudentId) throws SQLException {
        Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
        if (dormitory == null) {
            throw new SQLException("宿舍ID不存在。");
        }

        String trimmedBedNumber = bedNumber == null ? "" : bedNumber.trim();
        if (trimmedBedNumber.isEmpty()) {
            throw new SQLException("床号不能为空。");
        }

        if (!dormitoryManager.isBedNumberAvailable(dormitoryId, trimmedBedNumber, excludedStudentId)) {
            throw new SQLException("该宿舍的床号已被占用：" + trimmedBedNumber);
        }

        boolean isNewStudent = excludedStudentId == 0;
        boolean isMovingToAnotherDormitory = false;
        if (!isNewStudent) {
            Student existingStudent = getStudentById(excludedStudentId);
            if (existingStudent == null) {
                throw new SQLException("未找到指定ID的学生。");
            }
            isMovingToAnotherDormitory = existingStudent.getDormitoryId() != dormitoryId;
        }

        if ((isNewStudent || isMovingToAnotherDormitory) && dormitoryManager.getRemainingBeds(dormitoryId) <= 0) {
            throw new SQLException("该宿舍已住满，不能继续入住。");
        }

        return trimmedBedNumber;
    }
}
