package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import System.DormitoryManager;
import System.*;


public class DormitoryPanel extends JPanel {
    private DormitoryManager dormitoryManager;
    private JTextField dormitoryIdField;
    private JTextField buildingField;
    private JTextField floorField;
    private JTextField roomNumberField;
    private JTextField bedCountField;
    private JTextField priceField;
    private JTextField searchBuildingField; // 添加搜索字段

    public DormitoryPanel(DormitoryManager dormitoryManager) {
        this.dormitoryManager = dormitoryManager;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = UiKit.formConstraints();

        dormitoryIdField = new JTextField(15);
        UiKit.addFormField(formPanel, "宿舍ID：", dormitoryIdField, gbc, 0, 0);

        buildingField = new JTextField(15);
        UiKit.addFormField(formPanel, "楼栋：", buildingField, gbc, 0, 1);

        floorField = new JTextField(15);
        UiKit.addFormField(formPanel, "楼层：", floorField, gbc, 1, 0);

        roomNumberField = new JTextField(15);
        UiKit.addFormField(formPanel, "房间号：", roomNumberField, gbc, 1, 1);

        bedCountField = new JTextField(15);
        UiKit.addFormField(formPanel, "床位数：", bedCountField, gbc, 2, 0);

        priceField = new JTextField(15);
        UiKit.addFormField(formPanel, "单价：", priceField, gbc, 2, 1);

        searchBuildingField = new JTextField(15);
        UiKit.addFormField(formPanel, "按楼栋查询：", searchBuildingField, gbc, 3, 0);

        JPanel buttonPanel = UiKit.createButtonPanel();

        JButton addButton = UiKit.primaryButton("添加宿舍");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dormitoryManager.addDormitory(
                            buildingField.getText(),
                            Integer.parseInt(floorField.getText()),
                            roomNumberField.getText(),
                            Integer.parseInt(bedCountField.getText()),
                            Double.parseDouble(priceField.getText())
                    );
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "宿舍添加成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "添加失败：" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "输入的数字格式错误：" + ex.getMessage());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "操作失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = UiKit.secondaryButton("更新宿舍");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Dormitory dormitory = new Dormitory(
                            Integer.parseInt(dormitoryIdField.getText()),
                            buildingField.getText(),
                            Integer.parseInt(floorField.getText()),
                            roomNumberField.getText(),
                            Integer.parseInt(bedCountField.getText()),
                            Double.parseDouble(priceField.getText())
                    );
                    dormitoryManager.updateDormitory(dormitory);
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "宿舍更新成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "更新失败：" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "输入的数字格式错误：" + ex.getMessage());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "操作失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = UiKit.secondaryButton("删除宿舍");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int dormitoryId = Integer.parseInt(dormitoryIdField.getText());
                    dormitoryManager.deleteDormitory(dormitoryId);
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "宿舍删除成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "删除失败：" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "输入的数字格式错误：" + ex.getMessage());
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "操作失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton searchButton = UiKit.secondaryButton("查询宿舍");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<Dormitory> dormitories = dormitoryManager.getDormitoriesByBuilding(searchBuildingField.getText());
                    if (dormitories.isEmpty()) {
                        JOptionPane.showMessageDialog(DormitoryPanel.this, "未找到匹配的宿舍！");
                    } else {
                        StringBuilder result = new StringBuilder();
                        for (Dormitory dormitory : dormitories) {
                            result.append("ID: ").append(dormitory.getDormitoryId())
                                    .append(", 楼栋: ").append(dormitory.getBuilding())
                                    .append(", 楼层: ").append(dormitory.getFloor())
                                    .append(", 房间号: ").append(dormitory.getRoomNumber())
                                    .append(", 床位数: ").append(dormitory.getBedCount())
                                    .append(", 单价: ").append(dormitory.getPrice())
                                    .append("\n");
                        }
                        JOptionPane.showMessageDialog(DormitoryPanel.this, result.toString());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "查询失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(searchButton);

        add(UiKit.createPage("宿舍管理", "维护宿舍基础信息，查看入住情况和剩余床位。", formPanel, buttonPanel), BorderLayout.CENTER);

        JButton clearButton = UiKit.secondaryButton("清空表单");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        JButton occupancyButton = UiKit.secondaryButton("入住情况");
        occupancyButton.addActionListener(e -> showOccupancyWindow());
        buttonPanel.add(occupancyButton);

        JButton listAllButton = UiKit.secondaryButton("显示所有宿舍");
        listAllButton.addActionListener(e -> showDormitoryTableWindow());
        buttonPanel.add(listAllButton);

    }

    private void showDormitoryTableWindow() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "宿舍信息管理", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(8, 8));
        dialog.setSize(780, 460);
        dialog.setLocationRelativeTo(this);

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"ID", "楼栋", "楼层", "房间号", "床位数", "单价"},
                0
        );
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(i == 0 ? 60 : 120);
        }

        refreshDormitoryTable(tableModel);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addRowButton = new JButton("新增空行");
        addRowButton.addActionListener(e -> tableModel.addRow(new Object[]{"", "", "", "", "", ""}));
        buttonPanel.add(addRowButton);

        JButton saveButton = new JButton("保存选中/全部");
        saveButton.addActionListener(e -> saveDormitoryRows(table, tableModel));
        buttonPanel.add(saveButton);

        JButton deleteButton = new JButton("删除选中");
        deleteButton.addActionListener(e -> deleteSelectedDormitories(table, tableModel));
        buttonPanel.add(deleteButton);

        JButton loadButton = new JButton("载入到表单");
        loadButton.addActionListener(e -> loadSelectedDormitoryToForm(table, tableModel));
        buttonPanel.add(loadButton);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> refreshDormitoryTable(tableModel));
        buttonPanel.add(refreshButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void refreshDormitoryTable(DefaultTableModel tableModel) {
        try {
            tableModel.setRowCount(0);
            for (Dormitory dormitory : dormitoryManager.getAllDormitories()) {
                tableModel.addRow(new Object[]{
                        dormitory.getDormitoryId(),
                        dormitory.getBuilding(),
                        dormitory.getFloor(),
                        dormitory.getRoomNumber(),
                        dormitory.getBedCount(),
                        dormitory.getPrice()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "刷新失败：" + ex.getMessage());
        }
    }

    private void saveDormitoryRows(JTable table, DefaultTableModel tableModel) {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            selectedRows = new int[tableModel.getRowCount()];
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                selectedRows[i] = i;
            }
        }

        try {
            for (int row : selectedRows) {
                String id = getCell(tableModel, row, 0);
                String building = getCell(tableModel, row, 1);
                int floor = Integer.parseInt(getCell(tableModel, row, 2));
                String roomNumber = getCell(tableModel, row, 3);
                int bedCount = Integer.parseInt(getCell(tableModel, row, 4));
                double price = Double.parseDouble(getCell(tableModel, row, 5));

                if (id.isEmpty()) {
                    dormitoryManager.addDormitory(building, floor, roomNumber, bedCount, price);
                } else {
                    dormitoryManager.updateDormitory(new Dormitory(
                            Integer.parseInt(id),
                            building,
                            floor,
                            roomNumber,
                            bedCount,
                            price
                    ));
                }
            }
            refreshDormitoryTable(tableModel);
            JOptionPane.showMessageDialog(this, "宿舍信息保存成功！");
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "保存失败：" + ex.getMessage());
        }
    }

    private void deleteSelectedDormitories(JTable table, DefaultTableModel tableModel) {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的宿舍。");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定删除选中的宿舍吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            for (int selectedRow : selectedRows) {
                String id = getCell(tableModel, selectedRow, 0);
                if (!id.isEmpty()) {
                    dormitoryManager.deleteDormitory(Integer.parseInt(id));
                }
            }
            refreshDormitoryTable(tableModel);
            JOptionPane.showMessageDialog(this, "删除成功！");
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
        }
    }

    private void loadSelectedDormitoryToForm(JTable table, DefaultTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请选择一个宿舍。");
            return;
        }
        dormitoryIdField.setText(getCell(tableModel, selectedRow, 0));
        buildingField.setText(getCell(tableModel, selectedRow, 1));
        floorField.setText(getCell(tableModel, selectedRow, 2));
        roomNumberField.setText(getCell(tableModel, selectedRow, 3));
        bedCountField.setText(getCell(tableModel, selectedRow, 4));
        priceField.setText(getCell(tableModel, selectedRow, 5));
    }

    private void clearForm() {
        dormitoryIdField.setText("");
        buildingField.setText("");
        floorField.setText("");
        roomNumberField.setText("");
        bedCountField.setText("");
        priceField.setText("");
        searchBuildingField.setText("");
    }

    private void showOccupancyWindow() {
        String idText = dormitoryIdField.getText().trim();
        if (idText.isEmpty()) {
            idText = JOptionPane.showInputDialog(this, "请输入宿舍ID：", "查看入住情况", JOptionPane.QUESTION_MESSAGE);
        }
        if (idText == null || idText.trim().isEmpty()) {
            return;
        }

        try {
            int dormitoryId = Integer.parseInt(idText.trim());
            Dormitory dormitory = dormitoryManager.getDormitoryById(dormitoryId);
            if (dormitory == null) {
                JOptionPane.showMessageDialog(this, "未找到该宿舍。");
                return;
            }

            List<Student> students = new StudentManager().getStudentsByDormitoryId(dormitoryId);
            int occupiedBeds = dormitoryManager.getOccupiedBedCount(dormitoryId);
            int remainingBeds = dormitoryManager.getRemainingBeds(dormitoryId);

            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "宿舍入住情况", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setLayout(new BorderLayout(8, 8));
            dialog.setSize(960, 520);
            dialog.setLocationRelativeTo(this);

            JLabel summaryLabel = new JLabel(String.format(
                    "宿舍：%s %s楼 %s室    床位数：%d    已入住：%d    剩余床位：%d",
                    dormitory.getBuilding(),
                    dormitory.getFloor(),
                    dormitory.getRoomNumber(),
                    dormitory.getBedCount(),
                    occupiedBeds,
                    remainingBeds
            ));
            summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 12, 4, 12));
            dialog.add(summaryLabel, BorderLayout.NORTH);

            DefaultTableModel tableModel = new DefaultTableModel(
                    new String[]{"学生ID", "学号", "姓名", "性别", "年龄", "系别", "年级", "电话", "床号", "是否缴费"},
                    0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
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
                        student.getFeePaid()
                });
            }

            JTable table = new JTable(tableModel);
            table.setRowHeight(24);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(i == 0 ? 80 : 110);
            }
            dialog.add(new JScrollPane(table), BorderLayout.CENTER);

            JButton closeButton = new JButton("关闭");
            closeButton.addActionListener(e -> dialog.dispose());
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.add(closeButton);
            dialog.add(bottomPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "查询入住情况失败：" + ex.getMessage());
        }
    }

    private String getCell(DefaultTableModel tableModel, int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return value == null ? "" : value.toString().trim();
    }
}
