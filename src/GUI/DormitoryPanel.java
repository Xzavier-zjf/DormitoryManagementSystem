package GUI;

import javax.swing.*;
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

        JButton listAllButton = new JButton("显示所有宿舍");
        listAllButton.addActionListener(e -> {
            try {
                List<Dormitory> allDormitories = dormitoryManager.getAllDormitories();
                if (allDormitories.isEmpty()) {
                    JOptionPane.showMessageDialog(DormitoryPanel.this, "暂无宿舍信息！");
                    return;
                }

                Object[][] rows = new Object[allDormitories.size()][6];
                for (int i = 0; i < allDormitories.size(); i++) {
                    Dormitory dorm = allDormitories.get(i);
                    rows[i] = new Object[]{
                            dorm.getDormitoryId(),
                            dorm.getBuilding(),
                            dorm.getFloor(),
                            dorm.getRoomNumber(),
                            dorm.getBedCount(),
                            dorm.getPrice()
                    };
                }
                TableDialog.show(
                        DormitoryPanel.this,
                        "所有宿舍信息",
                        new String[]{"ID", "楼栋", "楼层", "房间号", "床位数", "单价"},
                        rows
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(DormitoryPanel.this, "数据库查询失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(listAllButton);

    }

}
