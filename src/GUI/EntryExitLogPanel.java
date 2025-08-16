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
import System.EntryExitLogManager;
import System.EntryExitLog;

public class EntryExitLogPanel extends JPanel {
    private EntryExitLogManager entryExitLogManager;

    private JTextField logIdField;
    private JTextField studentNumberField;
    private JDateChooser entryDateChooser;
    private JDateChooser exitDateChooser;
    private JTextField searchStudentIdField;

    public EntryExitLogPanel(EntryExitLogManager entryExitLogManager) {
        this.entryExitLogManager = entryExitLogManager;
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

        addField(formPanel, "记录ID：", logIdField = new JTextField(15), gbc, 1);
        addField(formPanel, "学生学号：", studentNumberField = new JTextField(15), gbc, 2);
        addField(formPanel, "离校时间：", entryDateChooser = new JDateChooser(), gbc, 3);
        addField(formPanel, "返校时间：", exitDateChooser = new JDateChooser(), gbc, 4);
        addField(formPanel, "按学生学号查询出入记录：", searchStudentIdField = new JTextField(15), gbc, 5);

        return formPanel;
    }

    // 修改 addField 方法，使其接受 JComponent 类型的参数
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
        logButton.addActionListener(this::logEntryExit);
        buttonPanel.add(logButton);

        JButton updateButton = new JButton("更新出入记录");
        updateButton.addActionListener(this::updateEntryExitLog);
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("删除出入记录");
        deleteButton.addActionListener(this::deleteEntryExitLog);
        buttonPanel.add(deleteButton);

        JButton searchButton = new JButton("查询出入记录");
        searchButton.addActionListener(this::searchEntryExitLogs);
        buttonPanel.add(searchButton);

        JButton listAllButton = new JButton("显示所有出入记录");
        listAllButton.addActionListener(this::showAllEntryExitLogs);
        buttonPanel.add(listAllButton);

        return buttonPanel;
    }

    private void logEntryExit(ActionEvent e) {
        try {
            String studentNumber = studentNumberField.getText();
            Date entryDate = entryDateChooser.getDate();
            Timestamp entryTime = new Timestamp(entryDate.getTime());
            Date exitDate = exitDateChooser.getDate();
            Timestamp exitTime = new Timestamp(exitDate.getTime());
            entryExitLogManager.logEntryExit(studentNumber, entryTime, exitTime);
            JOptionPane.showMessageDialog(this, "学生出入记录添加成功！");
            clearFields();
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "记录失败：" + ex.getMessage());
        }
    }

    private void updateEntryExitLog(ActionEvent e) {
        try {
            int logId = Integer.parseInt(logIdField.getText());
            String studentNumber = studentNumberField.getText();
            Date entryDate = entryDateChooser.getDate();
            Timestamp entryTime = new Timestamp(entryDate.getTime());
            Date exitDate = exitDateChooser.getDate();
            Timestamp exitTime = new Timestamp(exitDate.getTime());
            EntryExitLog log = new EntryExitLog(logId, studentNumber, entryTime, exitTime);
            entryExitLogManager.updateEntryExitLog(log);
            JOptionPane.showMessageDialog(this, "学生出入记录更新成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
        }
    }

    private void deleteEntryExitLog(ActionEvent e) {
        try {
            int logId = Integer.parseInt(logIdField.getText());
            entryExitLogManager.deleteEntryExitLog(logId);
            JOptionPane.showMessageDialog(this, "出入记录删除成功！");
            clearFields();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
        }
    }

    private void searchEntryExitLogs(ActionEvent e) {
        try {
            String studentNumber = searchStudentIdField.getText();
            List<EntryExitLog> logs = entryExitLogManager.getEntryExitLogsByStudentId(studentNumber);
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"记录ID", "学生学号", "离校时间", "返校时间"}, 0);
            updateTable(logs, tableModel);
            showTableWindow(tableModel, "搜索结果", true);
            if (logs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "未找到相关记录！");
            }
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void showAllEntryExitLogs(ActionEvent e) {
        try {
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"记录ID", "学生学号", "离校时间", "返校时间"}, 0);
            listAllEntryExitLogsInTable(tableModel);
            showTableWindow(tableModel, "所有学生出入记录", false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询失败：" + ex.getMessage());
        }
    }

    private void showTableWindow(DefaultTableModel tableModel, String title, boolean showReturnButton) {
        JFrame entryExitFrame = new JFrame(title);
        entryExitFrame.setSize(800, 600);
        entryExitFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JTable entryExitTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(entryExitTable);
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
            JDialog addDialog = new JDialog(entryExitFrame, "添加学生出入登记", true);
            addDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField studentNumberField = new JTextField(20);
            JDateChooser entryDateChooser = new JDateChooser();
            JDateChooser exitDateChooser = new JDateChooser();

            addField(addDialog, "学生学号：", studentNumberField, gbc, 0);
            addField(addDialog, "离校时间：", entryDateChooser, gbc, 1);
            addField(addDialog, "返校时间：", exitDateChooser, gbc, 2);

            JButton submitButton = new JButton("提交");
            submitButton.addActionListener(ev -> {
                String studentNumber = studentNumberField.getText().trim();
                Date entryDate = entryDateChooser.getDate();
                Date exitDate = exitDateChooser.getDate();

                if (studentNumber.isEmpty() || entryDate == null || exitDate == null) {
                    JOptionPane.showMessageDialog(addDialog, "请填写所有字段", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Timestamp entryTime = new Timestamp(entryDate.getTime());
                Timestamp exitTime = new Timestamp(exitDate.getTime());

                try {
                    entryExitLogManager.logEntryExit(studentNumber, entryTime, exitTime);
                    updateTable(entryExitLogManager.getAllEntryExitLogs(), tableModel);
                    JOptionPane.showMessageDialog(addDialog, "学生出入记录添加成功！");
                    addDialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 1;
            gbc.gridy = 3;
            addDialog.add(submitButton, gbc);

            addDialog.pack();
            addDialog.setLocationRelativeTo(entryExitFrame);
            addDialog.setVisible(true);
        });

        updateButton.addActionListener(e -> {
            try {
                int selectedRow = entryExitTable.getSelectedRow();
                if (selectedRow != -1) {
                    int logId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    EntryExitLog log = new EntryExitLog(
                            logId,
                            tableModel.getValueAt(selectedRow, 1).toString(),
                            Timestamp.valueOf(tableModel.getValueAt(selectedRow, 2).toString()),
                            Timestamp.valueOf(tableModel.getValueAt(selectedRow, 3).toString())
                    );
                    entryExitLogManager.updateEntryExitLog(log);
                    updateTable(entryExitLogManager.getAllEntryExitLogs(), tableModel); // Update the table after updating the log
                    JOptionPane.showMessageDialog(entryExitFrame, "学生出入记录更新成功！");
                } else {
                    JOptionPane.showMessageDialog(entryExitFrame, "请选择要更新的记录！");
                }
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(entryExitFrame, "更新失败：" + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int selectedRow = entryExitTable.getSelectedRow();
                if (selectedRow != -1) {
                    int logId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    entryExitLogManager.deleteEntryExitLog(logId);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(entryExitFrame, "学生出入记录删除成功！");
                } else {
                    JOptionPane.showMessageDialog(entryExitFrame, "请选择要删除的记录！");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(entryExitFrame, "删除失败：" + ex.getMessage());
            }
        });

        searchButton.addActionListener(e -> {
            String searchNumber = JOptionPane.showInputDialog(entryExitFrame, "输入要搜索的学生学号：");
            if (searchNumber != null && !searchNumber.trim().isEmpty()) {
                try {
                    List<EntryExitLog> logs = entryExitLogManager.getEntryExitLogsByStudentId(searchNumber);
                    DefaultTableModel searchResultModel = new DefaultTableModel(new Object[]{"记录ID", "学生学号", "离校时间", "返校时间"}, 0);
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
                        entryExitFrame.setVisible(true); // 返回时显示原始窗口
                    });

                    resultButtonPanel.add(returnButton);
                    resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);
                    searchResultFrame.add(resultPanel);
                    searchResultFrame.setVisible(true);
                    searchResultFrame.setLocationRelativeTo(null); // 设置窗口居中

                    entryExitFrame.setVisible(false); // 隐藏原始窗口

                    if (logs.isEmpty()) {
                        JOptionPane.showMessageDialog(searchResultFrame, "未找到相关记录！");
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(entryExitFrame, "查询失败：" + ex.getMessage());
                }
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                updateTable(entryExitLogManager.getAllEntryExitLogs(), tableModel);
                JOptionPane.showMessageDialog(entryExitFrame, "学生出入记录已刷新！");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(entryExitFrame, "刷新失败：" + ex.getMessage());
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
                entryExitFrame.dispose();
                showAllEntryExitLogs(null);
            });
            buttonPanel.add(returnButton);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
        entryExitFrame.add(panel);
        entryExitFrame.setVisible(true);

        // 设置窗口居中
        entryExitFrame.setLocationRelativeTo(null);
    }

    private void addField(JDialog dialog, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }


    private void updateTable(List<EntryExitLog> logs, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (EntryExitLog log : logs) {
            tableModel.addRow(new Object[]{
                    log.getLogId(),
                    log.getStudentNumber(),
                    log.getEntryTime(),
                    log.getExitTime()
            });
        }
    }


    private void listAllEntryExitLogsInTable(DefaultTableModel tableModel) throws SQLException {
        List<EntryExitLog> logs = entryExitLogManager.getAllEntryExitLogs();
        updateTable(logs, tableModel);
    }

    private void clearFields() {
        logIdField.setText("");
        studentNumberField.setText("");
        entryDateChooser.setDate(null);
        exitDateChooser.setDate(null);
        searchStudentIdField.setText("");
    }
}
