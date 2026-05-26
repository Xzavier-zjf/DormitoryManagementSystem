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

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("宿舍ID："), gbc);
        gbc.gridx = 1;
        dormitoryIdField = new JTextField(15);
        formPanel.add(dormitoryIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("楼栋："), gbc);
        gbc.gridx = 1;
        buildingField = new JTextField(15);
        formPanel.add(buildingField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("楼层："), gbc);
        gbc.gridx = 1;
        floorField = new JTextField(15);
        formPanel.add(floorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("房间号："), gbc);
        gbc.gridx = 1;
        roomNumberField = new JTextField(15);
        formPanel.add(roomNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("床位数："), gbc);
        gbc.gridx = 1;
        bedCountField = new JTextField(15);
        formPanel.add(bedCountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("单价："), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(15);
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("按楼栋查询宿舍："), gbc);
        gbc.gridx = 1;
        searchBuildingField = new JTextField(15);
        formPanel.add(searchBuildingField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("添加宿舍");
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

        JButton updateButton = new JButton("更新宿舍");
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

        JButton deleteButton = new JButton("删除宿舍");
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

        JButton searchButton = new JButton("查询宿舍");
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

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        JButton clearButton = new JButton("清空表单");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        JButton listAllButton = new JButton("显示所有宿舍");
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

    private String getCell(DefaultTableModel tableModel, int row, int column) {
        Object value = tableModel.getValueAt(row, column);
        return value == null ? "" : value.toString().trim();
    }
}
