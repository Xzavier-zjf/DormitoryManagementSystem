package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import System.*;

public class StudentPanel extends JPanel {
    private StudentManager studentManager;
    private DormitoryManager dormitoryManager;

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
    private JTextField searchNameField;

    public StudentPanel(StudentManager studentManager, DormitoryManager dormitoryManager) {
        this.studentManager = studentManager;
        this.dormitoryManager = dormitoryManager;
        setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(formPanel, "学号：", studentNumberField = new JTextField(15), gbc, 0);
        addField(formPanel, "姓名：", nameField = new JTextField(15), gbc, 1);
        addField(formPanel, "性别：", genderField = new JTextField(15), gbc, 2);
        addField(formPanel, "年龄：", ageField = new JTextField(15), gbc, 3);
        addField(formPanel, "系别：", departmentField = new JTextField(15), gbc, 4);
        addField(formPanel, "年级：", gradeField = new JTextField(15), gbc, 5);
        addField(formPanel, "电话：", phoneField = new JTextField(15), gbc, 6);
        addField(formPanel, "床号：", bednumField = new JTextField(15), gbc, 7);
        addField(formPanel, "是否缴费：", feepaidField = new JTextField(15), gbc, 8);
        addField(formPanel, "宿舍ID：", dormitoryIdField = new JTextField(15), gbc, 9);
        addField(formPanel, "按学生学号或姓名查询学生：", searchNameField = new JTextField(15), gbc, 10);

        return formPanel;
    }

    private void addField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("添加学生");
        addButton.addActionListener(e -> addStudent());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("更新学生");
        updateButton.addActionListener(e -> updateStudents());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("删除学生");
        deleteButton.addActionListener(e -> deleteStudents());
        buttonPanel.add(deleteButton);

        JButton searchStudentButton = new JButton("查询学生");
        searchStudentButton.addActionListener(e -> searchStudents());
        buttonPanel.add(searchStudentButton);

        JButton searchDormButton = new JButton("查询未住满学生的宿舍");
        searchDormButton.addActionListener(e -> openQueryDormitoryWindow());
        buttonPanel.add(searchDormButton);

        JButton listAllButton = new JButton("显示所有学生");
        listAllButton.addActionListener(e -> showStudentWindow(false));
        buttonPanel.add(listAllButton);

        return buttonPanel;
    }

    private void addStudent() {
        try {
            studentManager.addStudent(
                    studentNumberField.getText(),
                    nameField.getText(),
                    genderField.getText(),
                    Integer.parseInt(ageField.getText()),
                    departmentField.getText(),
                    Integer.parseInt(gradeField.getText()),
                    phoneField.getText(),
                    bednumField.getText(),
                    feepaidField.getText(),
                    Integer.parseInt(dormitoryIdField.getText())
            );
            JOptionPane.showMessageDialog(this, "学生添加成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage());
        }
    }

    private void updateStudents() {
        try {
            List<Student> students = studentManager.getStudentsByNumber(studentNumberField.getText());
            for (Student student : students) {
                student.setName(nameField.getText());
                student.setGender(genderField.getText());
                student.setAge(Integer.parseInt(ageField.getText()));
                student.setDepartment(departmentField.getText());
                student.setGrade(Integer.parseInt(gradeField.getText()));
                student.setPhone(phoneField.getText());
                student.setBedNumber(bednumField.getText());
                student.setFeePaid(feepaidField.getText());
                student.setDormitoryId(Integer.parseInt(dormitoryIdField.getText()));
                studentManager.updateStudent(student, false); // Ensure the correct method is called with both parameters
            }
            JOptionPane.showMessageDialog(this, "学生更新成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
        }
    }



    private void deleteStudents() {
        try {
            List<Student> students = studentManager.getStudentsByNumber(studentNumberField.getText());
            for (Student student : students) {
                studentManager.deleteStudent(student.getStudentId());
            }
            JOptionPane.showMessageDialog(this, "学生删除成功！");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
        }
    }

    private void searchStudents() {
        String searchText = searchNameField.getText().trim();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "搜索框不能为空，请输入学号或姓名进行查询！");
            return;
        }

        try {
            List<Student> students;
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"学生ID", "学号", "姓名", "性别", "年龄", "系别", "年级", "电话", "床号", "是否缴费", "宿舍ID"}, 0);

            if (searchText.matches("\\d+")) {
                students = studentManager.getStudentsByNumber(searchText);
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "未找到学号为 " + searchText + " 的学生信息");
                } else {
                    updateTable(students, tableModel);
                    showTableWindow(tableModel, "查询结果", true);
                }
            } else {
                students = studentManager.getStudentsByName(searchText);
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "未找到姓名为 " + searchText + " 的学生信息");
                } else {
                    updateTable(students, tableModel);
                    showTableWindow(tableModel, "查询结果", true);
                }
            }

            searchNameField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void updateDormitoryTable(List<Dormitory> dormitories, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Dormitory dormitory : dormitories) {
            tableModel.addRow(new Object[]{
                    dormitory.getDormitoryId(),
                    dormitory.getBuilding(),
                    dormitory.getFloor(),
                    dormitory.getRoomNumber(),
                    dormitory.getBedCount(),
                    dormitory.getRemainingBeds(),
                    dormitory.getPrice()
            });
        }
    }

    private void showStudentWindow(boolean showReturnButton) {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"学生ID", "学号", "姓名", "性别", "年龄", "系别", "年级", "电话", "床号", "是否缴费", "宿舍ID"}, 0);
        listAllStudentsInTable(tableModel);
        showTableWindow(tableModel, "所有学生信息", showReturnButton);
    }

    private void showTableWindow(DefaultTableModel tableModel, String title, boolean showReturnButton) {
        JFrame studentFrame = new JFrame(title);
        studentFrame.setSize(800, 600);
        studentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JTable studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // 支持多选
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("添加");
        JButton updateButton = new JButton("更新");
        JButton deleteButton = new JButton("删除");
        JButton searchButton = new JButton("搜索");
        JButton refreshButton = new JButton("刷新");

        addButton.addActionListener(e -> {
            JDialog addDialog = new JDialog(studentFrame, "添加学生", true);
            addDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField studentNumberField = new JTextField(20);
            JTextField studentNameField = new JTextField(20);
            JTextField studentGenderField = new JTextField(20);
            JTextField studentAgeField = new JTextField(20);
            JTextField studentDepartmentField = new JTextField(20);
            JTextField studentGradeField = new JTextField(20);
            JTextField studentPhoneField = new JTextField(20);
            JTextField studentBedField = new JTextField(20);
            JTextField studentPaymentField = new JTextField(20);
            JTextField studentDormitoryIdField = new JTextField(20);

            addField(addDialog, "学号：", studentNumberField, gbc, 0);
            addField(addDialog, "姓名：", studentNameField, gbc, 1);
            addField(addDialog, "性别：", studentGenderField, gbc, 2);
            addField(addDialog, "年龄：", studentAgeField, gbc, 3);
            addField(addDialog, "系别：", studentDepartmentField, gbc, 4);
            addField(addDialog, "年级：", studentGradeField, gbc, 5);
            addField(addDialog, "电话：", studentPhoneField, gbc, 6);
            addField(addDialog, "床号：", studentBedField, gbc, 7);
            addField(addDialog, "是否缴费：", studentPaymentField, gbc, 8);
            addField(addDialog, "宿舍ID：", studentDormitoryIdField, gbc, 9);

            JButton submitButton = new JButton("添加");
            submitButton.addActionListener(ev -> {
                String studentNumber = studentNumberField.getText().trim();
                String studentName = studentNameField.getText().trim();
                String studentGender = studentGenderField.getText().trim();
                int studentAge = Integer.parseInt(studentAgeField.getText().trim());
                String studentDepartment = studentDepartmentField.getText().trim();
                int studentGrade = Integer.parseInt(studentGradeField.getText().trim());
                String studentPhone = studentPhoneField.getText().trim();
                String studentBed = studentBedField.getText().trim();
                String studentPayment = studentPaymentField.getText().trim();
                int studentDormitoryId = Integer.parseInt(studentDormitoryIdField.getText().trim());

                try {
                    studentManager.addStudent(studentNumber, studentName, studentGender, studentAge,
                            studentDepartment, studentGrade, studentPhone, studentBed,
                            studentPayment, studentDormitoryId);
                    updateTable(studentManager.getAllStudents(), tableModel);
                    JOptionPane.showMessageDialog(addDialog, "学生添加成功！");
                    addDialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 1;
            gbc.gridy = 10;
            addDialog.add(submitButton, gbc);

            addDialog.pack();
            addDialog.setLocationRelativeTo(studentFrame);
            addDialog.setVisible(true);
        });

        updateButton.addActionListener(e -> {
            try {
                int[] selectedRows = studentTable.getSelectedRows();
                for (int selectedRow : selectedRows) {
                    int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    Student student = new Student(
                            studentId,
                            tableModel.getValueAt(selectedRow, 1).toString(),
                            tableModel.getValueAt(selectedRow, 2).toString(),
                            tableModel.getValueAt(selectedRow, 3).toString(),
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString()),
                            tableModel.getValueAt(selectedRow, 5).toString(),
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 6).toString()),
                            tableModel.getValueAt(selectedRow, 7).toString(),
                            tableModel.getValueAt(selectedRow, 8).toString(),
                            tableModel.getValueAt(selectedRow, 9).toString(),
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 10).toString())
                    );
                    studentManager.updateStudent(student, false); // 传递第二个参数
                }
                updateTable(studentManager.getAllStudents(), tableModel);
                JOptionPane.showMessageDialog(studentFrame, "学生信息更新成功！");
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(studentFrame, "更新失败：" + ex.getMessage());
            }
        });


        deleteButton.addActionListener(e -> {
            try {
                int[] selectedRows = studentTable.getSelectedRows();
                for (int selectedRow : selectedRows) {
                    int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    studentManager.deleteStudent(studentId);
                    tableModel.removeRow(selectedRow);
                }
                JOptionPane.showMessageDialog(studentFrame, "学生信息删除成功！");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(studentFrame, "删除失败：" + ex.getMessage());
            }
        });

        searchButton.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog(studentFrame, "输入要搜索的学生姓名或学号：");
            if (searchText != null && !searchText.trim().isEmpty()) {
                try {
                    List<Student> students = searchText.matches("\\d+")
                            ? studentManager.getStudentsByNumber(searchText)
                            : studentManager.getStudentsByName(searchText);

                    if (students.isEmpty()) {
                        JOptionPane.showMessageDialog(studentFrame, "未找到相关学生信息");
                    } else {
                        DefaultTableModel searchResultModel = new DefaultTableModel(
                                new Object[]{"学生ID", "学号", "姓名", "性别", "年龄", "系别", "年级", "电话", "床号", "是否缴费", "宿舍ID"},
                                0
                        );
                        updateTable(students, searchResultModel);

                        JFrame searchResultFrame = new JFrame("搜索结果");
                        searchResultFrame.setSize(800, 600);
                        searchResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel resultPanel = new JPanel(new BorderLayout());
                        JTable resultTable = new JTable(searchResultModel);
                        JScrollPane resultScrollPane = new JScrollPane(resultTable);
                        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

                        JPanel resultButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                        JButton returnButton = new JButton("返回");

                        returnButton.addActionListener(event -> {
                            searchResultFrame.dispose();
                            studentFrame.setVisible(true);
                        });

                        resultButtonPanel.add(returnButton);
                        resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);
                        searchResultFrame.add(resultPanel);
                        searchResultFrame.setVisible(true);
                        searchResultFrame.setLocationRelativeTo(null);

                        studentFrame.setVisible(false);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(studentFrame, "查询失败：" + ex.getMessage());
                }
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                updateTable(studentManager.getAllStudents(), tableModel);
                JOptionPane.showMessageDialog(studentFrame, "学生信息已刷新！");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(studentFrame, "刷新失败：" + ex.getMessage());
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);

        if (showReturnButton) {
            JButton returnButton = new JButton("返回");
            returnButton.addActionListener(e -> {
                studentFrame.dispose();
                showStudentWindow(false);
            });
            buttonPanel.add(returnButton);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
        studentFrame.add(panel);
        studentFrame.setVisible(true);
        studentFrame.setLocationRelativeTo(null);
    }

    private void addField(JDialog dialog, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

    private void updateTable(List<Student> students, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
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
            });
        }
    }

    private void listAllStudentsInTable(DefaultTableModel tableModel) {
        try {
            List<Student> students = studentManager.getAllStudents();
            updateTable(students, tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void clearFields() {
        studentNumberField.setText("");
        nameField.setText("");
        genderField.setText("");
        ageField.setText("");
        departmentField.setText("");
        gradeField.setText("");
        phoneField.setText("");
        bednumField.setText("");
        feepaidField.setText("");
        dormitoryIdField.setText("");
        searchNameField.setText("");
    }

    private void openQueryDormitoryWindow() {
        JFrame frame = new JFrame("查询未住满学生的宿舍");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"宿舍ID", "楼栋", "楼层", "房间号", "床位数", "剩余床位数", "单价"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        refreshDormitories(tableModel); // 初始显示未住满的宿舍

        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addStudentButton = new JButton("添加学生");
        addStudentButton.addActionListener(e -> openAddStudentDialog(frame, tableModel));
        functionPanel.add(addStudentButton);

        JButton searchDormButton = new JButton("搜索宿舍");
        searchDormButton.addActionListener(e -> searchDormitoryById(tableModel, frame));
        functionPanel.add(searchDormButton);

        JButton refreshDormButton = new JButton("刷新宿舍");
        functionPanel.add(refreshDormButton);

        mainPanel.add(functionPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        refreshDormButton.addActionListener(e -> {
            if (table.getSelectedRow() != -1) {
                int dormitoryId = (Integer) tableModel.getValueAt(table.getSelectedRow(), 0);
                refreshDormitoryById(tableModel, dormitoryId);
            } else {
                refreshDormitories(tableModel);
            }
        });
    }

    private void openAddStudentDialog(JFrame parentFrame, DefaultTableModel tableModel) {
        JDialog addDialog = new JDialog(parentFrame, "添加学生", true);
        addDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField studentNumberField = new JTextField(20);
        JTextField studentNameField = new JTextField(20);
        JTextField studentGenderField = new JTextField(20);
        JTextField studentAgeField = new JTextField(20);
        JTextField studentDepartmentField = new JTextField(20);
        JTextField studentGradeField = new JTextField(20);
        JTextField studentPhoneField = new JTextField(20);
        JTextField studentBedField = new JTextField(20);
        JTextField studentPaymentField = new JTextField(20);
        JTextField studentDormitoryIdField = new JTextField(20);

        addField(addDialog, "学号：", studentNumberField, gbc, 0);
        addField(addDialog, "姓名：", studentNameField, gbc, 1);
        addField(addDialog, "性别：", studentGenderField, gbc, 2);
        addField(addDialog, "年龄：", studentAgeField, gbc, 3);
        addField(addDialog, "系别：", studentDepartmentField, gbc, 4);
        addField(addDialog, "年级：", studentGradeField, gbc, 5);
        addField(addDialog, "电话：", studentPhoneField, gbc, 6);
        addField(addDialog, "床号：", studentBedField, gbc, 7);
        addField(addDialog, "是否缴费：", studentPaymentField, gbc, 8);
        addField(addDialog, "宿舍ID：", studentDormitoryIdField, gbc, 9);

        JButton submitButton = new JButton("添加");
        submitButton.addActionListener(ev -> {
            String studentNumber = studentNumberField.getText().trim();
            String studentName = studentNameField.getText().trim();
            String studentGender = studentGenderField.getText().trim();
            int studentAge = Integer.parseInt(studentAgeField.getText().trim());
            String studentDepartment = studentDepartmentField.getText().trim();
            int studentGrade = Integer.parseInt(studentGradeField.getText().trim());
            String studentPhone = studentPhoneField.getText().trim();
            String studentBed = studentBedField.getText().trim();
            String studentPayment = studentPaymentField.getText().trim();
            int studentDormitoryId = Integer.parseInt(studentDormitoryIdField.getText().trim());

            try {
                int remainingBeds = dormitoryManager.getRemainingBeds(studentDormitoryId);
                if (remainingBeds <= 0) {
                    JOptionPane.showMessageDialog(addDialog, "该宿舍已住满", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    studentManager.addStudent(studentNumber, studentName, studentGender, studentAge,
                            studentDepartment, studentGrade, studentPhone, studentBed,
                            studentPayment, studentDormitoryId);
                    updateDormitoryTable(dormitoryManager.getDormitoriesNotFullyOccupied(), tableModel);
                    JOptionPane.showMessageDialog(addDialog, "学生添加成功！");
                    addDialog.dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 10;
        addDialog.add(submitButton, gbc);

        addDialog.pack();
        addDialog.setLocationRelativeTo(parentFrame);
        addDialog.setVisible(true);
    }

    private void searchDormitoryById(DefaultTableModel tableModel, JFrame parentFrame) {
        String searchText = JOptionPane.showInputDialog(parentFrame, "输入你想要的未住满学生的宿舍ID：");
        if (searchText == null) {
            // 用户点击了取消按钮
            return;
        }
        searchText = searchText.trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "宿舍ID不能为空，请重新输入！");
            return;
        }

        try {
            int dormitoryId = Integer.parseInt(searchText);
            Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
            if (dormitory != null) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                        dormitory.getDormitoryId(),
                        dormitory.getBuilding(),
                        dormitory.getFloor(),
                        dormitory.getRoomNumber(),
                        dormitory.getBedCount(),
                        dormitoryManager.getRemainingBeds(dormitoryId),
                        dormitory.getPrice()
                });
                if (dormitoryManager.getRemainingBeds(dormitoryId) == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "该宿舍已住满");
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "未找到宿舍ID为 " + searchText + " 的宿舍信息");
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "查询失败：" + ex.getMessage());
        }
    }


    private void refreshDormitories(DefaultTableModel tableModel) {
        try {
            List<Dormitory> dormitories = dormitoryManager.getDormitoriesNotFullyOccupied();
            updateDormitoryTable(dormitories, tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "刷新失败：" + ex.getMessage());
        }
    }

    private void refreshDormitoryById(DefaultTableModel tableModel, int dormitoryId) {
        try {
            Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
            if (dormitory != null) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                        dormitory.getDormitoryId(),
                        dormitory.getBuilding(),
                        dormitory.getFloor(),
                        dormitory.getRoomNumber(),
                        dormitory.getBedCount(),
                        dormitoryManager.getRemainingBeds(dormitoryId),
                        dormitory.getPrice()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "刷新失败：" + ex.getMessage());
        }
    }
}
