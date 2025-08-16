package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import System.VisitorEntryExitLogManager;
import System.VisitorEntryExitLog;

public class VisitorEntryExitLogPanel extends JPanel {
    private VisitorEntryExitLogManager visitorEntryExitLogManager;
    private JTextField logIdField;
    private JTextField visitorNameField;
    private JTextField visitorPhoneField;
    private JTextField visitorPurposeField;
    private JDateChooser entryDateChooser;
    private JDateChooser exitDateChooser;
    private JTextField searchVisitorPhoneField;
    private JFrame parentFrame;

    public VisitorEntryExitLogPanel(VisitorEntryExitLogManager visitorEntryExitLogManager) {
        this.visitorEntryExitLogManager = visitorEntryExitLogManager;
        setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(formPanel, "记录ID：", logIdField = new JTextField(15), gbc, 0);
        addField(formPanel, "访客姓名：", visitorNameField = new JTextField(15), gbc, 1);
        addField(formPanel, "访客电话：", visitorPhoneField = new JTextField(15), gbc, 2);
        addField(formPanel, "访问目的：", visitorPurposeField = new JTextField(15), gbc, 3);
        addField(formPanel, "进入时间：", entryDateChooser = new JDateChooser(), gbc, 4);
        addField(formPanel, "离开时间：", exitDateChooser = new JDateChooser(), gbc, 5);
        addField(formPanel, "按访客电话查询出入记录：", searchVisitorPhoneField = new JTextField(15), gbc, 6);

        return formPanel;
    }

    private void addField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton logButton = new JButton("添加出入记录");
        logButton.addActionListener(this::logVisitorEntryExit);
        buttonPanel.add(logButton);

        JButton updateButton = new JButton("更新出入记录");
        updateButton.addActionListener(this::updateVisitorEntryExitLog);
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("删除出入记录");
        deleteButton.addActionListener(this::deleteVisitorEntryExitLog);
        buttonPanel.add(deleteButton);

        JButton searchButton = new JButton("查询出入记录");
        searchButton.addActionListener(this::searchVisitorEntryExitLogs);
        buttonPanel.add(searchButton);

        JButton listAllButton = new JButton("显示所有访客出入记录");
        listAllButton.addActionListener(this::showAllVisitorEntryExitLogs);
        buttonPanel.add(listAllButton);

        return buttonPanel;
    }

    private void logVisitorEntryExit(ActionEvent e) {
        try {
            String visitorName = visitorNameField.getText();
            String visitorPhone = visitorPhoneField.getText();
            String visitorPurpose = visitorPurposeField.getText();

            Date entryDate = entryDateChooser.getDate();
            Date exitDate = exitDateChooser.getDate();

            if (entryDate == null || exitDate == null) {
                JOptionPane.showMessageDialog(this, "请填写所有日期！");
                return;
            }

            Timestamp entryTime = new Timestamp(entryDate.getTime());
            Timestamp exitTime = new Timestamp(exitDate.getTime());

            visitorEntryExitLogManager.logVisitorEntryExit(visitorName, visitorPhone, visitorPurpose, entryTime, exitTime);
            JOptionPane.showMessageDialog(this, "出入记录添加成功！");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "记录失败：" + ex.getMessage());
        }
    }


    private void updateVisitorEntryExitLog(ActionEvent e) {
        try {
            int logId = Integer.parseInt(logIdField.getText());
            String visitorName = visitorNameField.getText();
            String visitorPhone = visitorPhoneField.getText();
            String visitorPurpose = visitorPurposeField.getText();
            Date entryDate = entryDateChooser.getDate();
            Timestamp entryTime = new Timestamp(entryDate.getTime());
            Date exitDate = exitDateChooser.getDate();
            Timestamp exitTime = new Timestamp(exitDate.getTime());

            VisitorEntryExitLog log = new VisitorEntryExitLog(logId, visitorName, visitorPhone, visitorPurpose, entryTime, exitTime);
            visitorEntryExitLogManager.updateVisitorEntryExitLog(log);
            JOptionPane.showMessageDialog(this, "出入记录更新成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
        }
    }

    private void deleteVisitorEntryExitLog(ActionEvent e) {
        try {
            int logId = Integer.parseInt(logIdField.getText());
            visitorEntryExitLogManager.deleteVisitorEntryExitLog(logId);
            JOptionPane.showMessageDialog(this, "出入记录删除成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
        }
    }


    private void searchVisitorEntryExitLogs(ActionEvent e) {
        try {
            String visitorPhone = searchVisitorPhoneField.getText();
            List<VisitorEntryExitLog> logs = visitorEntryExitLogManager.getEntryExitLogsByVisitorPhone(visitorPhone);
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"记录ID", "访客姓名", "访客电话", "访问目的", "进入时间", "离开时间"}, 0);
            updateTable(logs, tableModel);
            showTableWindow(tableModel, "搜索结果", true);

            if (logs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "未找到相关记录！");
            }

            searchVisitorPhoneField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }


    private void showAllVisitorEntryExitLogs(ActionEvent e) {
        try {
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"记录ID", "访客姓名", "访客电话", "访问目的", "进入时间", "离开时间"}, 0);
            listAllVisitorEntryExitLogsInTable(tableModel);
            showTableWindow(tableModel, "所有访客出入记录", false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void showTableWindow(DefaultTableModel tableModel, String title, boolean showReturnButton) {
        JFrame visitorEntryExitFrame = new JFrame(title);
        visitorEntryExitFrame.setSize(800, 600);
        visitorEntryExitFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JTable visitorEntryExitTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(visitorEntryExitTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> {
            JDialog addDialog = new JDialog(visitorEntryExitFrame, "添加访客出入记录", true);
            addDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField visitorNameField = new JTextField(20);
            JTextField visitorPhoneField = new JTextField(20);
            JTextField visitorPurposeField = new JTextField(20);
            JDateChooser entryDateChooser = new JDateChooser();
            JDateChooser exitDateChooser = new JDateChooser();

            addField(addDialog, "访客姓名：", visitorNameField, gbc, 0);
            addField(addDialog, "访客电话：", visitorPhoneField, gbc, 1);
            addField(addDialog, "访问目的：", visitorPurposeField, gbc, 2);
            addField(addDialog, "进入时间：", entryDateChooser, gbc, 3);
            addField(addDialog, "离开时间：", exitDateChooser, gbc, 4);

            JButton submitButton = new JButton("提交");
            submitButton.addActionListener(ev -> {
                String visitorName = visitorNameField.getText().trim();
                String visitorPhone = visitorPhoneField.getText().trim();
                String visitorPurpose = visitorPurposeField.getText().trim();
                Date entryDate = entryDateChooser.getDate();
                Date exitDate = exitDateChooser.getDate();

                if (visitorName.isEmpty() || visitorPhone.isEmpty() || visitorPurpose.isEmpty() || entryDate == null || exitDate == null) {
                    JOptionPane.showMessageDialog(addDialog, "请填写所有字段", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Timestamp entryTime = new Timestamp(entryDate.getTime());
                Timestamp exitTime = new Timestamp(exitDate.getTime());

                try {
                    visitorEntryExitLogManager.logVisitorEntryExit(visitorName, visitorPhone, visitorPurpose, entryTime, exitTime);
                    updateTable(visitorEntryExitLogManager.getAllVisitorEntryExitLogs(), tableModel);
                    JOptionPane.showMessageDialog(addDialog, "访客出入记录添加成功！");
                    addDialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 1;
            gbc.gridy = 5;
            addDialog.add(submitButton, gbc);

            addDialog.pack();
            addDialog.setLocationRelativeTo(visitorEntryExitFrame);
            addDialog.setVisible(true);
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("更新");
        updateButton.addActionListener(e -> {
            try {
                int selectedRow = visitorEntryExitTable.getSelectedRow();
                if (selectedRow != -1) {
                    int logId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    String visitorName = (String) tableModel.getValueAt(selectedRow, 1);
                    String visitorPhone = (String) tableModel.getValueAt(selectedRow, 2);
                    String visitorPurpose = (String) tableModel.getValueAt(selectedRow, 3);
                    Timestamp entryTime = (Timestamp) tableModel.getValueAt(selectedRow, 4);
                    Timestamp exitTime = (Timestamp) tableModel.getValueAt(selectedRow, 5);

                    VisitorEntryExitLog log = new VisitorEntryExitLog(logId, visitorName, visitorPhone, visitorPurpose, entryTime, exitTime);
                    visitorEntryExitLogManager.updateVisitorEntryExitLog(log);
                    updateTable(visitorEntryExitLogManager.getAllVisitorEntryExitLogs(), tableModel); // Update the table after updating the log
                    JOptionPane.showMessageDialog(visitorEntryExitFrame, "访客出入记录更新成功！");
                } else {
                    JOptionPane.showMessageDialog(visitorEntryExitFrame, "请选择要更新的记录！");
                }
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(visitorEntryExitFrame, "更新失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> {
            try {
                int selectedRow = visitorEntryExitTable.getSelectedRow();
                if (selectedRow != -1) {
                    int logId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    visitorEntryExitLogManager.deleteVisitorEntryExitLog(logId);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(visitorEntryExitFrame, "访客出入记录删除成功！");
                } else {
                    JOptionPane.showMessageDialog(visitorEntryExitFrame, "请选择要删除的记录！");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(visitorEntryExitFrame, "删除失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(deleteButton);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> {
            String visitorPhone = JOptionPane.showInputDialog(visitorEntryExitFrame, "输入要搜索的访客电话：");
            if (visitorPhone != null && !visitorPhone.trim().isEmpty()) {
                try {
                    List<VisitorEntryExitLog> logs = visitorEntryExitLogManager.getEntryExitLogsByVisitorPhone(visitorPhone);
                    DefaultTableModel searchResultModel = new DefaultTableModel(new Object[]{"记录ID", "访客姓名", "访客电话", "访问目的", "进入时间", "离开时间"}, 0);
                    updateTable(logs, searchResultModel);

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
                        visitorEntryExitFrame.setVisible(true); // 显示原始窗口
                    });

                    resultButtonPanel.add(returnButton);
                    resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);
                    searchResultFrame.add(resultPanel);
                    searchResultFrame.setVisible(true);

                    searchResultFrame.setLocationRelativeTo(null); // 设置窗口居中

                    visitorEntryExitFrame.setVisible(false); // 隐藏原始窗口

                    if (logs.isEmpty()) {
                        JOptionPane.showMessageDialog(searchResultFrame, "未找到相关记录！");
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(visitorEntryExitFrame, "查询失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(searchButton);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> {
            try {
                List<VisitorEntryExitLog> logs = visitorEntryExitLogManager.getAllVisitorEntryExitLogs();
                updateTable(logs, tableModel);
                JOptionPane.showMessageDialog(visitorEntryExitFrame, "访客出入记录已刷新！");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(visitorEntryExitFrame, "刷新失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(refreshButton);

        if (showReturnButton) {
            JButton returnButton = new JButton("返回");
            returnButton.addActionListener(e -> {
                visitorEntryExitFrame.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                } else {
                    showAllVisitorEntryExitLogs(null); // Return to the main entry-exit log list if parentFrame is null
                }
            });
            buttonPanel.add(returnButton);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
        visitorEntryExitFrame.add(panel);
        visitorEntryExitFrame.setVisible(true);

        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }

        // 设置窗口居中
        visitorEntryExitFrame.setLocationRelativeTo(null);
    }

    private void addField(JDialog dialog, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }


    private void listAllVisitorEntryExitLogsInTable(DefaultTableModel tableModel) throws SQLException {
        List<VisitorEntryExitLog> logs = visitorEntryExitLogManager.getAllVisitorEntryExitLogs();
        updateTable(logs, tableModel);
    }

    private void updateTable(List<VisitorEntryExitLog> logs, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (VisitorEntryExitLog log : logs) {
            tableModel.addRow(new Object[]{
                    log.getLogId(),
                    log.getVisitorName(),
                    log.getVisitorPhone(),
                    log.getVisitorPurpose(),
                    log.getEntryTime(),
                    log.getExitTime()
            });
        }
    }

    private void clearFields() {
        logIdField.setText("");
        visitorNameField.setText("");
        visitorPhoneField.setText("");
        visitorPurposeField.setText("");
        entryDateChooser.setDate(null);
        exitDateChooser.setDate(null);
        searchVisitorPhoneField.setText("");
    }
}
