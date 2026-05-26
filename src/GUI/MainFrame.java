package GUI;

import javax.swing.*;
import java.awt.*;
import System.AdminLogin;
import System.DormitoryManager;
import System.StudentManager;
import System.EntryExitLogManager;
import System.VisitorEntryExitLogManager;


public class MainFrame extends JFrame {
    private DormitoryManager dormitoryManager;
    private StudentManager studentManager;
    private EntryExitLogManager entryExitLogManager;
    private VisitorEntryExitLogManager visitorEntryExitLogManager;
    private String username;


    public MainFrame() {
        this(null);
    }

    public MainFrame(String username) {
        this.username = username;
        dormitoryManager = new DormitoryManager();
        studentManager = new StudentManager();
        entryExitLogManager = new EntryExitLogManager();
        visitorEntryExitLogManager = new VisitorEntryExitLogManager();

        StudentPanel studentPanel = new StudentPanel(studentManager, dormitoryManager); // 传递两个参数

        setTitle("学生宿舍管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("系统概览", new DashboardPanel(
                dormitoryManager,
                studentManager,
                entryExitLogManager,
                visitorEntryExitLogManager
        ));
        tabbedPane.addTab("宿舍管理", new DormitoryPanel(dormitoryManager));
        tabbedPane.addTab("学生管理", studentPanel);
        tabbedPane.addTab("出入登记管理", new EntryExitLogPanel(entryExitLogManager));
        tabbedPane.addTab("访客出入登记管理", new VisitorEntryExitLogPanel(visitorEntryExitLogManager));

        add(tabbedPane, BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu("账户");

        JMenuItem currentUserItem = new JMenuItem("当前用户：" + (username == null ? "未登录" : username));
        currentUserItem.setEnabled(false);
        accountMenu.add(currentUserItem);

        JMenuItem changePasswordItem = new JMenuItem("修改密码");
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        accountMenu.add(changePasswordItem);

        JMenuItem logoutItem = new JMenuItem("退出登录");
        logoutItem.addActionListener(e -> logout());
        accountMenu.add(logoutItem);

        JMenuItem deleteAccountItem = new JMenuItem("注销账号");
        deleteAccountItem.addActionListener(e -> showDeleteAccountDialog());
        accountMenu.add(deleteAccountItem);

        menuBar.add(accountMenu);
        return menuBar;
    }

    private void showChangePasswordDialog() {
        if (username == null) {
            JOptionPane.showMessageDialog(this, "请先登录后再修改密码。");
            return;
        }

        JDialog dialog = new JDialog(this, "修改密码", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        addField(dialog, "旧密码：", oldPasswordField, gbc, 0);
        addField(dialog, "新密码：", newPasswordField, gbc, 1);
        addField(dialog, "确认新密码：", confirmPasswordField, gbc, 2);

        JButton submitButton = new JButton("修改");
        submitButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "密码不能为空。");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "新密码和确认新密码不一致。");
                return;
            }

            AdminLogin adminLogin = new AdminLogin();
            if (adminLogin.updatePassword(username, oldPassword, newPassword)) {
                JOptionPane.showMessageDialog(dialog, "密码修改成功，请重新登录。");
                dialog.dispose();
                logout();
            } else {
                JOptionPane.showMessageDialog(dialog, "旧密码错误或修改失败。");
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        dialog.add(submitButton, gbc);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showDeleteAccountDialog() {
        if (username == null) {
            JOptionPane.showMessageDialog(this, "请先登录后再注销账号。");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要注销当前账号吗？该操作不可恢复。",
                "确认注销",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        JPasswordField passwordField = new JPasswordField(20);
        int input = JOptionPane.showConfirmDialog(
                this,
                passwordField,
                "请输入当前账号密码",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (input != JOptionPane.OK_OPTION) {
            return;
        }

        AdminLogin adminLogin = new AdminLogin();
        if (adminLogin.deleteAccount(username, new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "账号已注销。");
            logout();
        } else {
            JOptionPane.showMessageDialog(this, "密码错误或注销失败。");
        }
    }

    private void logout() {
        new AdminLoginPanel().setVisible(true);
        dispose();
    }

    private void addField(Container container, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        container.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        container.add(field, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminLoginPanel loginFrame = new AdminLoginPanel();
            loginFrame.setVisible(true);
        });
    }
}
