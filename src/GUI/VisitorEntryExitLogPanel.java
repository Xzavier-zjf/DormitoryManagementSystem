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
        setBackground(new Color(245, 247, 250));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = UiKit.formConstraints();

        visitorNameField = new JTextField(15);
        UiKit.addFormField(formPanel, "访客姓名：", visitorNameField, gbc, 0, 0);

        visitorPhoneField = new JTextField(15);
        UiKit.addFormField(formPanel, "访客电话：", visitorPhoneField, gbc, 0, 1);

        visitorPurposeField = new JTextField(15);
        UiKit.addFormField(formPanel, "访问目的：", visitorPurposeField, gbc, 1, 0);

        entryDateSpinner = createDateTimeSpinner();
        UiKit.addFormField(formPanel, "进入时间：", entryDateSpinner, gbc, 1, 1);

        exitDateSpinner = createDateTimeSpinner();
        UiKit.addFormField(formPanel, "离开时间：", exitDateSpinner, gbc, 2, 0);

        searchVisitorIdField = new JTextField(15);
        UiKit.addFormField(formPanel, "按电话查询：", searchVisitorIdField, gbc, 2, 1);

        JPanel buttonPanel = UiKit.createButtonPanel();

        JButton logButton = UiKit.primaryButton("记录出入");
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

        JButton searchButton = UiKit.secondaryButton("查询出入记录");
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

        JButton listAllButton = UiKit.secondaryButton("显示所有访客出入记录");
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

        add(UiKit.createPage("访客出入登记", "记录访客来访目的和进出时间，便于追溯访问记录。", formPanel, buttonPanel), BorderLayout.CENTER);
    }

    private JSpinner createDateTimeSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm:ss"));
        return spinner;
    }
}
