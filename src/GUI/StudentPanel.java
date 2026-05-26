package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import System.*;


public class StudentPanel extends JPanel {
    private StudentManager studentManager;
    private DormitoryManager dormitoryManager; // 添加 DormitoryManager 属性
    private JTextField studentIdField;
    private JTextField studentNumberField;
    private JTextField nameField;
    private JTextField genderField;
    private JTextField ageField;
    private JTextField departmentField;
    private JTextField gradeField;
    private JTextField phoneField;

    private JTextField bednumField;

    private JTextField feepaidField;

    private JTextField dormitoryIdField;
    private JTextField searchNameField; // 添加搜索字段

    public StudentPanel(StudentManager studentManager, DormitoryManager dormitoryManager) {
        this.studentManager = studentManager;
        this.dormitoryManager = dormitoryManager;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("学生ID："), gbc);
        gbc.gridx = 1;
        studentIdField = new JTextField(15);
        formPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("学号："), gbc);
        gbc.gridx = 1;
        studentNumberField = new JTextField(15);
        formPanel.add(studentNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("姓名："), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("性别："), gbc);
        gbc.gridx = 1;
        genderField = new JTextField(15);
        formPanel.add(genderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("年龄："), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(15);
        formPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("系别："), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(15);
        formPanel.add(departmentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("年级："), gbc);
        gbc.gridx = 1;
        gradeField = new JTextField(15);
        formPanel.add(gradeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("电话："), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("床号："), gbc);
        gbc.gridx = 1;
        bednumField = new JTextField(15);
        formPanel.add(bednumField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("是否缴费："), gbc);
        gbc.gridx = 1;
        feepaidField = new JTextField(15);
        formPanel.add(feepaidField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("宿舍ID："), gbc);
        gbc.gridx = 1;
        dormitoryIdField = new JTextField(15);
        formPanel.add(dormitoryIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("按姓名查询学生："), gbc);
        gbc.gridx = 1;
        searchNameField = new JTextField(15);
        formPanel.add(searchNameField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("添加学生");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    studentManager.addStudent(
                            studentNumberField.getText(),
                            nameField.getText(),
                            genderField.getText(),
                            Integer.parseInt(ageField.getText()),
                            departmentField.getText(),
                            Integer.parseInt(gradeField.getText()),
                            phoneField.getText(),
                            bednumField.getText(), // 新增床号
                            feepaidField.getText(), // 新增是否缴费状态
                            Integer.parseInt(dormitoryIdField.getText())
                    );
                    JOptionPane.showMessageDialog(StudentPanel.this, "学生添加成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "添加失败：" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "输入的数字格式错误：" + ex.getMessage());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "操作失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("更新学生");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Student student = new Student(
                            Integer.parseInt(studentIdField.getText()),
                            studentNumberField.getText(),
                            nameField.getText(),
                            genderField.getText(),
                            Integer.parseInt(ageField.getText()),
                            departmentField.getText(),
                            Integer.parseInt(gradeField.getText()),
                            phoneField.getText(),
                            bednumField.getText(), // 新增床号
                            feepaidField.getText(), // 新增是否缴费状态
                            Integer.parseInt(dormitoryIdField.getText())
                    );
                    studentManager.updateStudent(student);
                    JOptionPane.showMessageDialog(StudentPanel.this, "学生更新成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "更新失败：" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "输入的数字格式错误：" + ex.getMessage());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "操作失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("删除学生");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int studentId = Integer.parseInt(studentIdField.getText());
                    studentManager.deleteStudent(studentId);
                    JOptionPane.showMessageDialog(StudentPanel.this, "学生删除成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "删除失败：" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "输入的数字格式错误：" + ex.getMessage());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "操作失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton searchButton = new JButton("查询学生");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Student> students = studentManager.getStudentsByName(searchNameField.getText());
                    if (students.isEmpty()) {
                        JOptionPane.showMessageDialog(StudentPanel.this, "未找到匹配的学生！");
                    } else {
                        StringBuilder result = new StringBuilder();
                        for (Student student : students) {
                            result.append("ID: ").append(student.getStudentId())
                                    .append(", 学号: ").append(student.getStudentNumber())
                                    .append(", 姓名: ").append(student.getName())
                                    .append(", 性别: ").append(student.getGender())
                                    .append(", 年龄: ").append(student.getAge())
                                    .append(", 系别: ").append(student.getDepartment())
                                    .append(", 年级: ").append(student.getGrade())
                                    .append(", 电话: ").append(student.getPhone())
                                    .append(", 床号: ").append(student.getBedNumber())
                                    .append(", 是否缴费: ").append(student.getFeePaid())
                                    .append(", 宿舍ID: ").append(student.getDormitoryId())
                                    .append("\n");
                        }
                        JOptionPane.showMessageDialog(StudentPanel.this, result.toString());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "查询失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(searchButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        JButton listAllButton = new JButton("显示所有学生");
        listAllButton.addActionListener(e -> {
            try {
                List<Student> students = studentManager.getAllStudents();
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(StudentPanel.this, "暂无学生信息！");
                    return;
                }

                Object[][] rows = new Object[students.size()][11];
                for (int i = 0; i < students.size(); i++) {
                    Student student = students.get(i);
                    rows[i] = new Object[]{
                            student.getStudentId(),
                            student.getStudentNumber(),
                            student.getName(),
                            student.getGender(),
                            student.getAge(),
                            student.getDepartment(),
                            student.getGrade(),
                            student.getPhone(),
                            student.getBedNumber(),
                            student.getFeePaid(),
                            student.getDormitoryId()
                    };
                }
                TableDialog.show(
                        StudentPanel.this,
                        "所有学生信息",
                        new String[]{"ID", "学号", "姓名", "性别", "年龄", "系别", "年级", "电话", "床号", "是否缴费", "宿舍ID"},
                        rows
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(StudentPanel.this, "查询失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(listAllButton);
    }

}
