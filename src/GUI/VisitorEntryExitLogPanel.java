package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import System.VisitorEntryExitLogManager;
import System.VisitorEntryExitLog;

public class VisitorEntryExitLogPanel extends JPanel {
    private VisitorEntryExitLogManager visitorEntryExitLogManager;
    private JTextField visitorNameField;
    private JTextField visitorPhoneField;
    private JTextField visitorPurposeField;
    private JSpinner entryDateSpinner;
    private JSpinner exitDateSpinner;
    private JTextField searchVisitorIdField;

    public VisitorEntryExitLogPanel(VisitorEntryExitLogManager visitorEntryExitLogManager) {
        this.visitorEntryExitLogManager = visitorEntryExitLogManager;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("访客姓名："), gbc);
        gbc.gridx = 1;
        visitorNameField = new JTextField(15);
        formPanel.add(visitorNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("访客电话："), gbc);
        gbc.gridx = 1;
        visitorPhoneField = new JTextField(15);
        formPanel.add(visitorPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("访问目的："), gbc);
        gbc.gridx = 1;
        visitorPurposeField = new JTextField(15);
        formPanel.add(visitorPurposeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("进入时间："), gbc);
        gbc.gridx = 1;
        entryDateSpinner = createDateTimeSpinner();
        formPanel.add(entryDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("离开时间："), gbc);
        gbc.gridx = 1;
        exitDateSpinner = createDateTimeSpinner();
        formPanel.add(exitDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("按访客电话查询出入记录："), gbc);
        gbc.gridx = 1;
        searchVisitorIdField = new JTextField(15);
        formPanel.add(searchVisitorIdField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton logButton = new JButton("记录出入");
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String visitorName = visitorNameField.getText().trim();
                    String visitorPhone = visitorPhoneField.getText().trim();
                    String visitorPurpose = visitorPurposeField.getText().trim();

                    if (visitorName.isEmpty() || visitorPhone.isEmpty() || visitorPurpose.isEmpty()) {
                        JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "请填写访客姓名、电话和访问目的！");
                        return;
                    }

                    Date entryDate = (Date) entryDateSpinner.getValue();
                    Date exitDate = (Date) exitDateSpinner.getValue();
                    Timestamp entryTime = new Timestamp(entryDate.getTime());
                    Timestamp exitTime = new Timestamp(exitDate.getTime());
                    if (exitTime.before(entryTime)) {
                        JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "离开时间不能早于进入时间！");
                        return;
                    }

                    visitorEntryExitLogManager.logVisitorEntryExit(visitorName, visitorPhone, visitorPurpose, entryTime, exitTime);
                    JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "出入记录成功！");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "记录失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(logButton);

        JButton searchButton = new JButton("查询出入记录");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String visitorPhone = searchVisitorIdField.getText();
                    List<VisitorEntryExitLog> logs = visitorEntryExitLogManager.getEntryExitLogsByVisitorPhone(visitorPhone);
                    if (logs.isEmpty()) {
                        JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "未找到匹配的出入记录！");
                    } else {
                        StringBuilder result = new StringBuilder();
                        for (VisitorEntryExitLog log : logs) {
                            result.append("Log ID: ").append(log.getLogId())
                                    .append(", Visitor Name: ").append(log.getVisitorName())
                                    .append(", Visitor Phone: ").append(log.getVisitorPhone())
                                    .append(", Visitor Purpose: ").append(log.getVisitorPurpose())
                                    .append(", Entry Time: ").append(log.getEntryTime())
                                    .append(", Exit Time: ").append(log.getExitTime())
                                    .append("\n");
                        }
                        JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, result.toString());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "查询失败：" + ex.getMessage());
                }
            }
        });
        buttonPanel.add(searchButton);

        JButton listAllButton = new JButton("显示所有访客出入记录");
        listAllButton.addActionListener(e -> {
            try {
                List<VisitorEntryExitLog> logs = visitorEntryExitLogManager.getAllVisitorEntryExitLogs();
                if (logs.isEmpty()) {
                    JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "暂无访客出入记录！");
                    return;
                }

                Object[][] rows = new Object[logs.size()][6];
                for (int i = 0; i < logs.size(); i++) {
                    VisitorEntryExitLog log = logs.get(i);
                    rows[i] = new Object[]{
                            log.getLogId(),
                            log.getVisitorName(),
                            log.getVisitorPhone(),
                            log.getVisitorPurpose(),
                            log.getEntryTime(),
                            log.getExitTime()
                    };
                }
                TableDialog.show(
                        VisitorEntryExitLogPanel.this,
                        "所有访客出入记录",
                        new String[]{"记录ID", "访客姓名", "访客电话", "访问目的", "进入时间", "离开时间"},
                        rows
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(VisitorEntryExitLogPanel.this, "查询失败：" + ex.getMessage());
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
