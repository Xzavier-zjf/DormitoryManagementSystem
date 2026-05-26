package GUI;

import System.DormitoryManager;
import System.EntryExitLogManager;
import System.StudentManager;
import System.VisitorEntryExitLogManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends JPanel {
    private final DormitoryManager dormitoryManager;
    private final StudentManager studentManager;
    private final EntryExitLogManager entryExitLogManager;
    private final VisitorEntryExitLogManager visitorEntryExitLogManager;
    private final JLabel dormitoryCountLabel = new JLabel("-");
    private final JLabel studentCountLabel = new JLabel("-");
    private final JLabel entryExitCountLabel = new JLabel("-");
    private final JLabel visitorLogCountLabel = new JLabel("-");
    private final JLabel refreshTimeLabel = new JLabel("-");

    public DashboardPanel(
            DormitoryManager dormitoryManager,
            StudentManager studentManager,
            EntryExitLogManager entryExitLogManager,
            VisitorEntryExitLogManager visitorEntryExitLogManager
    ) {
        this.dormitoryManager = dormitoryManager;
        this.studentManager = studentManager;
        this.entryExitLogManager = entryExitLogManager;
        this.visitorEntryExitLogManager = visitorEntryExitLogManager;

        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(UiKit.createHeader("系统概览", "快速查看宿舍、学生、出入登记和访客记录规模。"), BorderLayout.NORTH);
        add(createSummaryPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        refresh();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 12, 12));
        panel.setOpaque(false);
        panel.add(createSummaryCard("宿舍总数", dormitoryCountLabel));
        panel.add(createSummaryCard("学生总数", studentCountLabel));
        panel.add(createSummaryCard("学生出入记录", entryExitCountLabel));
        panel.add(createSummaryCard("访客出入记录", visitorLogCountLabel));
        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.PLAIN, 15f));
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 30f));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JButton refreshButton = UiKit.secondaryButton("刷新统计");
        refreshButton.addActionListener(e -> refresh());
        panel.add(refreshTimeLabel, BorderLayout.WEST);
        panel.add(refreshButton, BorderLayout.EAST);
        return panel;
    }

    private void refresh() {
        try {
            dormitoryCountLabel.setText(String.valueOf(dormitoryManager.getAllDormitories().size()));
            studentCountLabel.setText(String.valueOf(studentManager.getAllStudents().size()));
            entryExitCountLabel.setText(String.valueOf(entryExitLogManager.getAllEntryExitLogs().size()));
            visitorLogCountLabel.setText(String.valueOf(visitorEntryExitLogManager.getAllVisitorEntryExitLogs().size()));
            refreshTimeLabel.setText("更新时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "统计信息刷新失败：" + ex.getMessage());
        }
    }
}
