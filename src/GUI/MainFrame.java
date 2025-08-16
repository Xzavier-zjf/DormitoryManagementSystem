package GUI;

import javax.swing.*;
import System.DormitoryManager;
import System.StudentManager;
import System.EntryExitLogManager;
import System.VisitorEntryExitLogManager;
import System.AdminLogin;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private DormitoryManager dormitoryManager;
    private StudentManager studentManager;
    private EntryExitLogManager entryExitLogManager;
    private VisitorEntryExitLogManager visitorEntryExitLogManager;
    private String username;

    public MainFrame(String username) {
        this.username = username;
        dormitoryManager = new DormitoryManager();
        studentManager = new StudentManager(dormitoryManager);
        entryExitLogManager = new EntryExitLogManager();
        visitorEntryExitLogManager = new VisitorEntryExitLogManager();

        setTitle("学生宿舍管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("宿舍管理", new DormitoryPanel(dormitoryManager, studentManager));
        tabbedPane.addTab("学生管理", new StudentPanel(studentManager, dormitoryManager));
        tabbedPane.addTab("学生出入登记管理", new EntryExitLogPanel(entryExitLogManager));
        tabbedPane.addTab("访客出入登记管理", new VisitorEntryExitLogPanel(visitorEntryExitLogManager));

        add(tabbedPane, BorderLayout.CENTER);

        // 添加修改密码和注销功能
        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu("账户");

        JMenuItem changePasswordItem = new JMenuItem("修改密码");
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        accountMenu.add(changePasswordItem);

        JMenuItem logoutItem = new JMenuItem("注销账号");
        logoutItem.addActionListener(e -> showLogoutDialog());
        accountMenu.add(logoutItem);

        menuBar.add(accountMenu);
        setJMenuBar(menuBar);

        // 设置窗口居中
        setLocationRelativeTo(null);
    }

    private void showChangePasswordDialog() {
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
        submitButton.addActionListener(e -> submitPasswordChange(dialog, oldPasswordField, newPasswordField, confirmPasswordField));

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitPasswordChange(dialog, oldPasswordField, newPasswordField, confirmPasswordField);
                }
            }
        };

        oldPasswordField.addKeyListener(enterKeyListener);
        newPasswordField.addKeyListener(enterKeyListener);
        confirmPasswordField.addKeyListener(enterKeyListener);

        gbc.gridx = 1;
        gbc.gridy = 3;
        dialog.add(submitButton, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void submitPasswordChange(JDialog dialog, JPasswordField oldPasswordField, JPasswordField newPasswordField, JPasswordField confirmPasswordField) {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(dialog, "新密码和确认新密码不匹配", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AdminLogin adminLogin = new AdminLogin();
        if (adminLogin.updatePassword(username, oldPassword, newPassword)) {
            JOptionPane.showMessageDialog(dialog, "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, "旧密码错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLogoutDialog() {
        JDialog dialog = new JDialog(this, "注销账号", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        addField(dialog, "用户名：", usernameField, gbc, 0);
        addField(dialog, "密码：", passwordField, gbc, 1);

        JButton submitButton = new JButton("注销");
        submitButton.addActionListener(e -> submitLogout(dialog, usernameField, passwordField));

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitLogout(dialog, usernameField, passwordField);
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        gbc.gridx = 1;
        gbc.gridy = 2;
        dialog.add(submitButton, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void submitLogout(JDialog dialog, JTextField usernameField, JPasswordField passwordField) {
        String targetUsername = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        AdminLogin adminLogin = new AdminLogin();
        if (adminLogin.deleteAccount(targetUsername, password)) {
            JOptionPane.showMessageDialog(dialog, "账号注销成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();

            if (targetUsername.equals(this.username)) {
                new AdminLoginPanel().setVisible(true);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addField(Container container, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        container.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        container.add(field, gbc);
    }
}
