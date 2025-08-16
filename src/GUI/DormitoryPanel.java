package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import System.DormitoryManager;
import System.Dormitory;
import System.Student;
import System.StudentManager;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Objects;

public class DormitoryPanel extends JPanel {
    private DormitoryManager dormitoryManager;
    private StudentManager studentManager;
    private JTextField dormitoryIdField;
    private JTextField buildingField;
    private JTextField floorField;
    private JTextField roomNumberField;
    private JTextField bedCountField;
    private JTextField priceField;
    private JTextField searchBuildingField;

    private JTable dormitoryTable;
    private DefaultTableModel tableModel;
    private JFrame dormitoryFrame; // 作为类的成员变量
    private int currentDormitoryId; // 用于存储当前操作的宿舍ID



    public DormitoryPanel(DormitoryManager dormitoryManager, StudentManager studentManager) {
        this.dormitoryManager = dormitoryManager;
        this.studentManager = studentManager;
        this.dormitoryFrame = new JFrame("Dormitory Details"); // 初始化框架
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

        addField(formPanel, "宿舍ID：", dormitoryIdField = new JTextField(15), gbc, 0);
        addField(formPanel, "楼栋：", buildingField = new JTextField(15), gbc, 1);
        addField(formPanel, "楼层：", floorField = new JTextField(15), gbc, 2);
        addField(formPanel, "房间号：", roomNumberField = new JTextField(15), gbc, 3);
        addField(formPanel, "床位数：", bedCountField = new JTextField(15), gbc, 4);
        addField(formPanel, "单价：", priceField = new JTextField(15), gbc, 5);
        addField(formPanel, "按宿舍ID或楼栋查询宿舍：", searchBuildingField = new JTextField(15), gbc, 6);

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
        JButton addButton = new JButton("添加宿舍");
        addButton.addActionListener(e -> addDormitory());
        JButton updateButton = new JButton("更新宿舍");
        updateButton.addActionListener(e -> updateDormitories());
        JButton deleteButton = new JButton("删除宿舍");
        deleteButton.addActionListener(e -> deleteDormitories());
        JButton searchButton = new JButton("查询宿舍");
        searchButton.addActionListener(e -> searchDormitories());
        JButton occupancyButton = new JButton("查询宿舍入住情况");
        occupancyButton.addActionListener(e -> showOccupancyWindow());
        JButton listAllButton = new JButton("显示所有宿舍");
        listAllButton.addActionListener(e -> showDormitoryWindow(false));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(occupancyButton);
        buttonPanel.add(listAllButton);
        return buttonPanel;
    }

    private void addDormitory() {
        try {
            dormitoryManager.addDormitory(
                    buildingField.getText(),
                    Integer.parseInt(floorField.getText()),
                    roomNumberField.getText(),
                    Integer.parseInt(bedCountField.getText()),
                    Double.parseDouble(priceField.getText())
            );
            JOptionPane.showMessageDialog(this, "宿舍添加成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage());
        }
    }

    private void updateDormitories() {
        try {
            int[] selectedRows = dormitoryTable.getSelectedRows();
            if (selectedRows.length > 0) {
                for (int selectedRow : selectedRows) {
                    int dormitoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    Dormitory dormitory = new Dormitory(
                            dormitoryId,
                            tableModel.getValueAt(selectedRow, 1).toString(),
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString()),
                            tableModel.getValueAt(selectedRow, 3).toString(),
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString()),
                            Double.parseDouble(tableModel.getValueAt(selectedRow, 5).toString())
                    );
                    dormitoryManager.updateDormitory(dormitory);
                }
                updateTable(dormitoryManager.getAllDormitories(), tableModel);
                JOptionPane.showMessageDialog(this, "宿舍信息更新成功！");
            } else {
                JOptionPane.showMessageDialog(this, "请选择要更新的宿舍！");
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
        }
    }

    private void deleteDormitories() {
        try {
            int[] selectedRows = dormitoryTable.getSelectedRows();
            if (selectedRows.length > 0) {
                for (int selectedRow : selectedRows) {
                    int dormitoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    dormitoryManager.deleteDormitory(dormitoryId);
                }
                updateTable(dormitoryManager.getAllDormitories(), tableModel);
                JOptionPane.showMessageDialog(this, "宿舍信息删除成功！");
            } else {
                JOptionPane.showMessageDialog(this, "请选择要删除的宿舍！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
        }
    }

    private void searchDormitories() {
        String searchText = searchBuildingField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "搜索框不能为空，请输入宿舍ID或楼栋进行查询！");
            return;
        }
        try {
            tableModel = new DefaultTableModel(new Object[]{"宿舍ID", "楼栋", "楼层", "房间号", "床位数", "单价"}, 0);
            if (searchText.matches("\\d+")) {
                int dormitoryId = Integer.parseInt(searchText);
                Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
                if (dormitory != null) {
                    tableModel.addRow(new Object[]{
                            dormitory.getDormitoryId(),
                            dormitory.getBuilding(),
                            dormitory.getFloor(),
                            dormitory.getRoomNumber(),
                            dormitory.getBedCount(),
                            dormitory.getPrice()
                    });
                    showTableWindow(tableModel, "查询结果", true);
                } else {
                    JOptionPane.showMessageDialog(this, "未找到宿舍ID为 " + searchText + " 的宿舍信息");
                }
            } else {
                List<Dormitory> dormitories = dormitoryManager.getDormitoriesByBuilding(searchText);
                if (!dormitories.isEmpty()) {
                    updateTable(dormitories, tableModel);
                    showTableWindow(tableModel, "查询结果", true);
                } else {
                    JOptionPane.showMessageDialog(this, "未找到楼栋为 " + searchText + " 的宿舍信息");
                }
            }
            searchBuildingField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void showDormitoryWindow(boolean showReturnButton) {
        tableModel = new DefaultTableModel(new Object[]{"宿舍ID", "楼栋", "楼层", "房间号", "床位数", "单价"}, 0);
        listAllDormitoriesInTable(tableModel);
        showTableWindow(tableModel, "所有宿舍信息", showReturnButton);
    }

    private void showTableWindow(DefaultTableModel tableModel, String title, boolean showReturnButton) {
        dormitoryFrame = new JFrame(title);
        dormitoryFrame.setSize(800, 600);
        dormitoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        dormitoryTable = new JTable(tableModel);
        dormitoryTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(dormitoryTable);
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
            JDialog addDialog = new JDialog(dormitoryFrame, "添加宿舍", true);
            addDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField buildingField = new JTextField(20);
            JTextField floorField = new JTextField(20);
            JTextField roomNumberField = new JTextField(20);
            JTextField bedCountField = new JTextField(20);
            JTextField priceField = new JTextField(20);

            addField(addDialog, "楼栋：", buildingField, gbc, 0);
            addField(addDialog, "楼层：", floorField, gbc, 1);
            addField(addDialog, "房间号：", roomNumberField, gbc, 2);
            addField(addDialog, "床位数：", bedCountField, gbc, 3);
            addField(addDialog, "单价：", priceField, gbc, 4);

            JButton submitButton = new JButton("添加");
            submitButton.addActionListener(ev -> {
                String building = buildingField.getText().trim();
                int floor = Integer.parseInt(floorField.getText().trim());
                String roomNumber = roomNumberField.getText().trim();
                int bedCount = Integer.parseInt(bedCountField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                try {
                    dormitoryManager.addDormitory(building, floor, roomNumber, bedCount, price);
                    updateTable(dormitoryManager.getAllDormitories(), tableModel);
                    JOptionPane.showMessageDialog(addDialog, "宿舍添加成功！");
                    addDialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 1;
            gbc.gridy = 5;
            addDialog.add(submitButton, gbc);

            addDialog.pack();
            addDialog.setLocationRelativeTo(dormitoryFrame);
            addDialog.setVisible(true);
        });

        updateButton.addActionListener(e -> updateDormitories());

        deleteButton.addActionListener(e -> deleteDormitories());

        searchButton.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog(dormitoryFrame, "输入要搜索的楼栋或宿舍ID：");
            if (searchText != null && !searchText.trim().isEmpty()) {
                try {
                    List<Dormitory> dormitories;
                    if (searchText.matches("\\d+")) {
                        int dormitoryId = Integer.parseInt(searchText);
                        Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
                        dormitories = dormitory != null ? List.of(dormitory) : List.of();
                    } else {
                        dormitories = dormitoryManager.getDormitoriesByBuilding(searchText);
                    }

                    if (dormitories.isEmpty()) {
                        JOptionPane.showMessageDialog(dormitoryFrame, "未找到相关宿舍信息");
                    } else {
                        DefaultTableModel searchResultModel = new DefaultTableModel(new Object[]{"宿舍ID", "楼栋", "楼层", "房间号", "床位数", "单价"}, 0);
                        updateTable(dormitories, searchResultModel);

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
                            dormitoryFrame.setVisible(true);
                        });

                        resultButtonPanel.add(returnButton);

                        resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);
                        searchResultFrame.add(resultPanel);
                        searchResultFrame.setVisible(true);

                        searchResultFrame.setLocationRelativeTo(null);

                        dormitoryFrame.setVisible(false);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dormitoryFrame, "查询失败：" + ex.getMessage());
                }
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                updateTable(dormitoryManager.getAllDormitories(), tableModel);
                JOptionPane.showMessageDialog(dormitoryFrame, "宿舍信息已刷新！");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dormitoryFrame, "刷新失败：" + ex.getMessage());
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
                dormitoryFrame.dispose();
                showDormitoryWindow(false);
            });
            buttonPanel.add(returnButton);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
        dormitoryFrame.add(panel);
        dormitoryFrame.setVisible(true);

        dormitoryFrame.setLocationRelativeTo(null);
    }

    private void addField(JDialog dialog, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

    private void updateTable(List<Dormitory> dormitories, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Dormitory dormitory : dormitories) {
            tableModel.addRow(new Object[]{
                    dormitory.getDormitoryId(),
                    dormitory.getBuilding(),
                    dormitory.getFloor(),
                    dormitory.getRoomNumber(),
                    dormitory.getBedCount(),
                    dormitory.getPrice()
            });
        }
    }

    private void listAllDormitoriesInTable(DefaultTableModel tableModel) {
        try {
            List<Dormitory> dormitories = dormitoryManager.getAllDormitories();
            updateTable(dormitories, tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void clearFields() {
        dormitoryIdField.setText("");
        buildingField.setText("");
        floorField.setText("");
        roomNumberField.setText("");
        bedCountField.setText("");
        priceField.setText("");
        searchBuildingField.setText("");
    }

    private void showOccupancyWindow() {
        dormitoryFrame.setSize(800, 600);
        dormitoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String dormitoryIdStr = JOptionPane.showInputDialog(this, "输入宿舍ID：");
        if (dormitoryIdStr == null || dormitoryIdStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "宿舍ID不能为空！");
            return;
        }


        try {
            int dormitoryId = Integer.parseInt(dormitoryIdStr.trim());


            // 检查宿舍是否存在
            if (!isDormitoryExist(dormitoryId)) {
                JOptionPane.showMessageDialog(this, "该宿舍未创建！");
                return;
            }


            List<Student> students = studentManager.getStudentsByDormitoryId(dormitoryId);

            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this, "该宿舍暂无学生入住！");
                return;
            }

            // 创建宿舍入住情况窗口
            JFrame occupancyFrame = new JFrame("宿舍入住情况");
            occupancyFrame.setSize(800, 600);
            occupancyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout());
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"学生ID", "学号", "姓名", "性别", "年龄", "系别", "年级", "电话", "床号", "是否缴费", "宿舍ID"}, 0); // Ensure dormitory ID column is added here
            JTable studentTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(studentTable);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);

            // 底部面板包含按钮和剩余床位数标签
            JPanel bottomPanel = new JPanel(new BorderLayout());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JLabel remainingBedsLabel = new JLabel();
            updateRemainingBedsLabel(remainingBedsLabel, dormitoryId);

            JButton addButton = new JButton("添加学生");
            addButton.addActionListener(e -> openAddStudentDialog(dormitoryId, occupancyFrame, tableModel, remainingBedsLabel));
            buttonPanel.add(addButton);

            JButton updateButton = new JButton("更新学生");
            updateButton.addActionListener(e -> updateStudents(studentTable, tableModel, remainingBedsLabel));
            buttonPanel.add(updateButton);

            JButton deleteButton = new JButton("删除学生");
            deleteButton.addActionListener(e -> deleteStudents(studentTable, tableModel, dormitoryId, remainingBedsLabel));
            buttonPanel.add(deleteButton);

            JButton swapBedsButton = new JButton("交换床号");
            swapBedsButton.addActionListener(e -> openSwapBedsDialog(occupancyFrame, tableModel, remainingBedsLabel)); // Pass the correct JFrame here
            buttonPanel.add(swapBedsButton);

            JButton refreshButton = new JButton("刷新学生");
            refreshButton.addActionListener(e -> refreshOccupancyTable(dormitoryId, tableModel, remainingBedsLabel));
            buttonPanel.add(refreshButton);

            bottomPanel.add(buttonPanel, BorderLayout.CENTER);
            bottomPanel.add(remainingBedsLabel, BorderLayout.SOUTH);

            panel.add(bottomPanel, BorderLayout.SOUTH);
            occupancyFrame.add(panel);
            occupancyFrame.setVisible(true);
            occupancyFrame.setLocationRelativeTo(null);

            // 初始刷新入住情况表格和剩余床位数
            refreshOccupancyTable(dormitoryId, tableModel, remainingBedsLabel);

        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }


    public boolean isDormitoryExist(int dormitoryId) {
        DormitoryManager dormitoryManager = new DormitoryManager();
        try {
            Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
            return dormitory != null; // 如果找到了宿舍，则返回 true，否则返回 false
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // 查询过程中出现异常，返回 false
        }
    }


    private void openAddStudentDialog(int dormitoryId, JFrame parentFrame, DefaultTableModel tableModel, JLabel remainingBedsLabel) {
        JDialog addDialog = new JDialog(parentFrame, "添加学生", true);
        addDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Field labels and fields setup
        JTextField studentNumberField = new JTextField(20);
        JTextField studentNameField = new JTextField(20);
        JTextField studentGenderField = new JTextField(20);
        JTextField studentAgeField = new JTextField(20);
        JTextField studentDepartmentField = new JTextField(20);
        JTextField studentGradeField = new JTextField(20);
        JTextField studentPhoneField = new JTextField(20);
        JTextField studentBedField = new JTextField(20);
        JTextField studentPaymentField = new JTextField(20);

        // Add fields and labels to the dialog
        int y = 0;
        addField(addDialog, "学号：", studentNumberField, gbc, y++);
        addField(addDialog, "姓名：", studentNameField, gbc, y++);
        addField(addDialog, "性别：", studentGenderField, gbc, y++);
        addField(addDialog, "年龄：", studentAgeField, gbc, y++);
        addField(addDialog, "系别：", studentDepartmentField, gbc, y++);
        addField(addDialog, "年级：", studentGradeField, gbc, y++);
        addField(addDialog, "电话：", studentPhoneField, gbc, y++);
        addField(addDialog, "床号：", studentBedField, gbc, y++);
        addField(addDialog, "是否缴费：", studentPaymentField, gbc, y++);

        // Add the submit button in a new row
        JButton submitButton = new JButton("添加");
        submitButton.addActionListener(ev -> {
            try {
                String bedNumber = studentBedField.getText().trim();
                if (bedNumber.isEmpty()) {
                    throw new SQLException("床号不能为空。");
                }

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

                studentManager.addStudent(
                        studentNumberField.getText().trim(),
                        studentNameField.getText().trim(),
                        studentGenderField.getText().trim(),
                        Integer.parseInt(studentAgeField.getText().trim()),
                        studentDepartmentField.getText().trim(),
                        Integer.parseInt(studentGradeField.getText().trim()),
                        studentPhoneField.getText().trim(),
                        bedNumber,
                        studentPaymentField.getText().trim(),
                        dormitoryId
                );
                refreshOccupancyTable(dormitoryId, tableModel, remainingBedsLabel);
                JOptionPane.showMessageDialog(addDialog, "学生添加成功！");
                addDialog.dispose();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2; // Make the button span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        addDialog.add(submitButton, gbc);

        addDialog.pack();
        addDialog.setLocationRelativeTo(parentFrame);
        addDialog.setVisible(true);
    }

    private void addField(JDialog dialog, String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(field, gbc);
    }


    private void openSwapBedsDialog(JFrame parentFrame, DefaultTableModel tableModel, JLabel remainingBedsLabel) {
        JDialog swapDialog = new JDialog(parentFrame, "交换床号", true);
        swapDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField studentIdField1 = new JTextField(20);
        JTextField studentIdField2 = new JTextField(20);

        addField(swapDialog, "学生ID 1：", studentIdField1, gbc, 0);
        addField(swapDialog, "学生ID 2：", studentIdField2, gbc, 1);

        JButton submitButton = new JButton("交换");
        submitButton.addActionListener(ev -> swapBedsAction(studentIdField1, studentIdField2, swapDialog, tableModel, remainingBedsLabel));

        // Adding key listener to trigger swap action on Enter key press
        studentIdField2.addActionListener(ev -> swapBedsAction(studentIdField1, studentIdField2, swapDialog, tableModel, remainingBedsLabel));

        gbc.gridx = 1;
        gbc.gridy = 2;
        swapDialog.add(submitButton, gbc);

        swapDialog.pack();
        swapDialog.setLocationRelativeTo(parentFrame);
        swapDialog.setVisible(true);
    }

    private void swapBedsAction(JTextField studentIdField1, JTextField studentIdField2, JDialog swapDialog, DefaultTableModel tableModel, JLabel remainingBedsLabel) {
        try {
            int studentId1 = Integer.parseInt(studentIdField1.getText().trim());
            int studentId2 = Integer.parseInt(studentIdField2.getText().trim());

            if (!areStudentsInSameDormitory(studentId1, studentId2)) {
                JOptionPane.showMessageDialog(swapDialog, "交换失败：学生不在同一个宿舍", "错误", JOptionPane.ERROR_MESSAGE);
                studentIdField1.setText("");
                studentIdField2.setText("");
                return;
            }

            int oldDormitoryId = studentManager.getStudentById(studentId1).getDormitoryId();
            studentManager.swapStudentBeds(studentId1, studentId2);
            JOptionPane.showMessageDialog(swapDialog, "床号交换成功！");

            // Refresh both old and new dormitory tables
            refreshOccupancyTable(oldDormitoryId, tableModel, remainingBedsLabel);
            int newDormitoryId = studentManager.getStudentById(studentId2).getDormitoryId();
            refreshOccupancyTable(newDormitoryId, tableModel, remainingBedsLabel);

            swapDialog.dispose();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(swapDialog, "交换失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }


    private boolean areStudentsInSameDormitory(int studentId1, int studentId2) throws SQLException {
        int dormitoryId1 = studentManager.getStudentById(studentId1).getDormitoryId();
        int dormitoryId2 = studentManager.getStudentById(studentId2).getDormitoryId();
        return dormitoryId1 == dormitoryId2;
    }

    public int getCurrentDormitoryId() {
        return this.currentDormitoryId; // Make sure this is updated appropriately in your logic
    }

    private void updateStudents(JTable studentTable, DefaultTableModel tableModel, JLabel remainingBedsLabel) {
        try {
            int[] selectedRows = studentTable.getSelectedRows();
            if (selectedRows.length > 0) {
                int oldDormitoryId = getValueAsInt(tableModel, selectedRows[0], 10, 0);
                for (int selectedRow : selectedRows) {
                    int studentId = getValueAsInt(tableModel, selectedRow, 0, -1);
                    String studentNumber = getValueAsString(tableModel, selectedRow, 1, "");
                    String name = getValueAsString(tableModel, selectedRow, 2, "");
                    String gender = getValueAsString(tableModel, selectedRow, 3, "");
                    int age = getValueAsInt(tableModel, selectedRow, 4, 0);
                    String department = getValueAsString(tableModel, selectedRow, 5, "");
                    int grade = getValueAsInt(tableModel, selectedRow, 6, 0);
                    String phone = getValueAsString(tableModel, selectedRow, 7, "");
                    String bedNumber = getValueAsString(tableModel, selectedRow, 8, "");
                    String feePaid = getValueAsString(tableModel, selectedRow, 9, "");
                    int newDormitoryId = getValueAsInt(tableModel, selectedRow, 10, 0);

                    Student student = new Student(
                            studentId,
                            studentNumber,
                            name,
                            gender,
                            age,
                            department,
                            grade,
                            phone,
                            bedNumber,
                            feePaid,
                            newDormitoryId
                    );

                    // Check if dormitory ID has changed
                    if (newDormitoryId != oldDormitoryId) {
                        // Check if the new dormitory exists
                        Dormitory newDormitory = dormitoryManager.getDormitoryById(newDormitoryId);
                        if (newDormitory == null) {
                            JOptionPane.showMessageDialog(null, "宿舍ID " + newDormitoryId + " 不存在！");
                            return;
                        }

                        // Move the student to the new dormitory
                        studentManager.updateStudent(student, false);
                    } else {
                        // Update the student without moving dormitory
                        studentManager.updateStudent(student, true);
                    }
                }

                // Refresh both old and new dormitory tables
                refreshOccupancyTable(oldDormitoryId, tableModel, remainingBedsLabel);
                if (selectedRows.length > 0) {
                    int newDormitoryId = getValueAsInt(tableModel, selectedRows[0], 10, 0);
                    refreshOccupancyTable(newDormitoryId, tableModel, remainingBedsLabel);
                }

                JOptionPane.showMessageDialog(null, "学生信息更新成功！");
            } else {
                JOptionPane.showMessageDialog(null, "请选择要更新的学生！");
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "更新失败：" + ex.getMessage());
        }
    }

    private String getValueAsString(DefaultTableModel tableModel, int row, int column, String defaultValue) {
        Object value = tableModel.getValueAt(row, column);
        return value == null ? defaultValue : value.toString();
    }


    private boolean isValidBedNumber(String bedNumber, int dormitoryId) throws SQLException {
        int bedNum = Integer.parseInt(bedNumber);
        Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
        if (bedNum < 1 || bedNum > dormitory.getBedCount()) return false;

        // Check if the bed is already occupied, excluding the current student
        return dormitoryManager.isBedNumberAvailable(dormitoryId, bedNumber);
    }

    private int getValueAsInt(DefaultTableModel tableModel, int row, int column, int defaultValue) {
        try {
            Object value = tableModel.getValueAt(row, column);
            if (value == null) {
                return defaultValue;
            }
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void deleteStudents(JTable studentTable, DefaultTableModel tableModel, int dormitoryId, JLabel remainingBedsLabel) {
        try {
            int[] selectedRows = studentTable.getSelectedRows();
            if (selectedRows.length > 0) {
                for (int selectedRow : selectedRows) {
                    int studentId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    studentManager.deleteStudent(studentId);
                    tableModel.removeRow(selectedRow);
                }
                refreshOccupancyTable(dormitoryId, tableModel, remainingBedsLabel);
                JOptionPane.showMessageDialog(null, "学生信息删除成功！");
            } else {
                JOptionPane.showMessageDialog(null, "请选择要删除的学生！");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "删除失败：" + ex.getMessage());
        }
    }

    private void refreshOccupancyTable(int dormitoryId, DefaultTableModel tableModel, JLabel remainingBedsLabel) {
        try {
            List<Student> students = studentManager.getStudentsByDormitoryId(dormitoryId);

            // Sort students by bed number
            students.sort((s1, s2) -> {
                String bedNumber1 = s1.getBedNumber();
                String bedNumber2 = s2.getBedNumber();
                if (bedNumber1.isEmpty()) bedNumber1 = "0";
                if (bedNumber2.isEmpty()) bedNumber2 = "0";
                return Integer.compare(Integer.parseInt(bedNumber1), Integer.parseInt(bedNumber2));
            });

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

            if (remainingBedsLabel != null) {
                updateRemainingBedsLabel(remainingBedsLabel, dormitoryId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "刷新失败：" + ex.getMessage());
        }
    }

    private void updateRemainingBedsLabel(JLabel label, int dormitoryId) {
        try {
            int remainingBeds = dormitoryManager.getRemainingBeds(dormitoryId);
            label.setText("剩余床位数: " + remainingBeds);
        } catch (SQLException ex) {
            label.setText("获取剩余床位数失败: " + ex.getMessage());
        }
    }
}
