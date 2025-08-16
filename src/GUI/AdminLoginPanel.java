package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import System.AdminLogin;

public class AdminLoginPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox showPasswordCheckBox;

    public AdminLoginPanel() {
        setTitle("管理员登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Change input language to English (for Windows)
        setInputLanguageToEnglish();

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // 设置窗口居中
        setLocationRelativeTo(null);
    }

    private void setInputLanguageToEnglish() {
        try {
            String command = "powershell.exe \"Set-WinUILanguageOverride -Language en-US\"";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        passwordField.setEchoChar('•');

        addField(formPanel, "用户名：", usernameField, gbc, 0);
        addPasswordField(formPanel, "密码：", passwordField, gbc, 1);

        return formPanel;
    }

    private void addField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addPasswordField(JPanel panel, String label, JPasswordField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);

        showPasswordCheckBox = new JCheckBox("显示密码");
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        gbc.gridx = 2;
        panel.add(showPasswordCheckBox, gbc);
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0); // 显示明文
        } else {
            passwordField.setEchoChar('•'); // 隐藏密码
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        buttonPanel.add(loginButton);

        registerButton = new JButton("注册");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        buttonPanel.add(registerButton);

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        return buttonPanel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AdminLogin adminLogin = new AdminLogin();
        if (adminLogin.login(username, password)) {
            final JDialog dialog = new JDialog(this, "消息", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(new JLabel("登录成功", SwingConstants.CENTER), BorderLayout.CENTER);
            dialog.setSize(200, 100);
            dialog.setLocationRelativeTo(this);

            // 使用Timer设置延时关闭
            Timer timer = new Timer(1000, new ActionListener() { // 延时1秒
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    new MainFrame(username).setVisible(true);
                    dispose();  // 关闭登录界面
                }
            });
            timer.setRepeats(false); // 只执行一次
            timer.start();

            dialog.setVisible(true); // 显示对话框
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码无效", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        JDialog registerDialog = new JDialog(this, "管理员注册", true);
        registerDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        addField(registerDialog, "新用户名：", newUsernameField, gbc, 0);
        addField(registerDialog, "新密码：", newPasswordField, gbc, 1);
        addField(registerDialog, "确认新密码：", confirmPasswordField, gbc, 2);

        JButton submitButton = new JButton("注册");
        submitButton.addActionListener(e -> {
            String newUsername = newUsernameField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (newUsername.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(registerDialog, "密码和确认密码不匹配", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AdminLogin adminLogin = new AdminLogin();
            if (adminLogin.register(newUsername, newPassword)) {
                JOptionPane.showMessageDialog(registerDialog, "注册成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(registerDialog, "用户名已存在或注册失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String newUsername = newUsernameField.getText().trim();
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());

                    if (newUsername.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(registerDialog, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(registerDialog, "密码和确认密码不匹配", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    AdminLogin adminLogin = new AdminLogin();
                    if (adminLogin.register(newUsername, newPassword)) {
                        JOptionPane.showMessageDialog(registerDialog, "注册成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        registerDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(registerDialog, "用户名已存在或注册失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        newUsernameField.addKeyListener(enterKeyListener);
        newPasswordField.addKeyListener(enterKeyListener);
        confirmPasswordField.addKeyListener(enterKeyListener);

        gbc.gridx = 1;
        gbc.gridy = 3;
        registerDialog.add(submitButton, gbc);

        registerDialog.pack();
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setVisible(true);
    }

    private void addField(JDialog dialog, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            AdminLoginPanel loginFrame = new AdminLoginPanel();
            loginFrame.setVisible(true);
        });
    }
}
