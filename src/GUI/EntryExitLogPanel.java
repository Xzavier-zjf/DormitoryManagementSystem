package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import System.EntryExitLogManager;
import System.EntryExitLog;
import java.util.Date;


public class EntryExitLogPanel extends JPanel {
    private EntryExitLogManager entryExitLogManager;
    private JTextField studentNumberField;
    private JSpinner entryDateSpinner;
    private JSpinner exitDateSpinner;
    private JTextField searchStudentIdField;

    public EntryExitLogPanel(EntryExitLogManager entryExitLogManager) {
        this.entryExitLogManager = entryExitLogManager;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("学生学号："), gbc);
        gbc.gridx = 1;
        studentNumberField = new JTextField(15);
        formPanel.add(studentNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("进入时间："), gbc);
        gbc.gridx = 1;
        entryDateSpinner = createDateTimeSpinner();
        formPanel.add(entryDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("离开时间："), gbc);
        gbc.gridx = 1;
        exitDateSpinner = createDateTimeSpinner();
        formPanel.add(exitDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("按学生学号查询出入记录："), gbc);
        gbc.gridx = 1;
        searchStudentIdField = new JTextField(15);
        formPanel.add(searchStudentIdField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton logButton = new JButton("记录出入");
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String studentNumber = studentNumberField.getText().trim();
                    if (studentNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(EntryExitLogPanel.this, "请填写学生学号！");
                        return;
                    }

                    Date entryDate = (Date) entryDateSpinner.getValue();
                    Timestamp entryTime = new Timestamp(entryDate.getTime());
                    Date exitDate = (Date) exitDateSpinner.getValue();
                    Timestamp exitTime = new Timestamp(exitDate.getTime());
                    if (exitTime.before(entryTime)) {
                        JOptionPane.showMessageDialog(EntryExitLogPanel.this, "离开时间不能早于进入时间！");
                        return;
                    }

                    entryExitLogManager.logEntryExit(studentNumber, entryTime, exitTime);
                    JOptionPane.showMessageDialog(EntryExitLogPanel.this, "出入记录成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(EntryExitLogPanel.this, "记录失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(logButton);

        JButton searchButton = new JButton("查询出入记录");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String studentNumber = searchStudentIdField.getText();
                    List<EntryExitLog> logs = entryExitLogManager.getEntryExitLogsByStudentId(studentNumber);
                    if (logs.isEmpty()) {
                        JOptionPane.showMessageDialog(EntryExitLogPanel.this, "未找到匹配的出入记录！");
                    } else {
                        StringBuilder result = new StringBuilder();
                        for (EntryExitLog log : logs) {
                            result.append("Log ID: ").append(log.getLogId())
                                    .append(", Student Number: ").append(log.getStudentNumber())
                                    .append(", Entry Time: ").append(log.getEntryTime())
                                    .append(", Exit Time: ").append(log.getExitTime())
                                    .append("\n");
                        }
                        JOptionPane.showMessageDialog(EntryExitLogPanel.this, result.toString());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(EntryExitLogPanel.this, "查询失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(searchButton);

        JButton listAllButton = new JButton("显示所有出入记录");
        listAllButton.addActionListener(e -> {
            try {
                List<EntryExitLog> logs = entryExitLogManager.getAllEntryExitLogs();
                if (logs.isEmpty()) {
                    JOptionPane.showMessageDialog(EntryExitLogPanel.this, "暂无学生出入记录！");
                    return;
                }

                Object[][] rows = new Object[logs.size()][4];
                for (int i = 0; i < logs.size(); i++) {
                    EntryExitLog log = logs.get(i);
                    rows[i] = new Object[]{
                            log.getLogId(),
                            log.getStudentNumber(),
                            log.getEntryTime(),
                            log.getExitTime()
                    };
                }
                TableDialog.show(
                        EntryExitLogPanel.this,
                        "所有学生出入记录",
                        new String[]{"记录ID", "学生学号", "进入时间", "离开时间"},
                        rows
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(EntryExitLogPanel.this, "查询失败：" + ex.getMessage());
            }
        });
        buttonPanel.add(listAllButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JSpinner createDateTimeSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm:ss"));
        return spinner;
    }
}
