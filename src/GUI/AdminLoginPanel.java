package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import System.AdminLogin;

public class AdminLoginPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox showPasswordCheckBox;

    public AdminLoginPanel() {
        UiKit.applyGlobalStyle();
        setTitle("管理员登录");
        setSize(460, 320);
        setMinimumSize(new Dimension(420, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        AdminLogin adminLogin = new AdminLogin();
        if (!adminLogin.ensureAdminTable()) {
            JOptionPane.showMessageDialog(
                    this,
                    "管理员表初始化失败，请检查 MySQL 服务和数据库连接配置。",
                    "数据库错误",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        JPanel contentPanel = new JPanel(new BorderLayout(0, 14));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));
        contentPanel.add(UiKit.createHeader("管理员登录", "登录后进入学生宿舍管理系统。"), BorderLayout.NORTH);
        formPanel.setOpaque(false);
        JPanel formCard = createLoginCard(formPanel);
        buttonPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formCard);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(UiKit.createButtonBar(buttonPanel), BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // 设置窗口居中
        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        passwordField.setEchoChar('•');

        addField(formPanel, "用户名：", usernameField, gbc, 0);
        addPasswordField(formPanel, "密码：", passwordField, gbc, 1);

        return formPanel;
    }

    private JPanel createLoginCard(JPanel formPanel) {
        JPanel card = UiKit.createCard(formPanel);
        card.setPreferredSize(new Dimension(360, 128));
        card.setMaximumSize(new Dimension(360, 128));
        return card;
    }

    private void addField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelComponent, gbc);
        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(190, 32));
        panel.add(field, gbc);
    }

    private void addPasswordField(JPanel panel, String label, JPasswordField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelComponent, gbc);
        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(190, 32));
        panel.add(field, gbc);

        showPasswordCheckBox = new JCheckBox("显示密码");
        showPasswordCheckBox.setOpaque(false);
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
        JPanel buttonPanel = UiKit.createButtonPanel();

        loginButton = UiKit.primaryButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        buttonPanel.add(loginButton);

        registerButton = UiKit.secondaryButton("注册");
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
        registerDialog.setLayout(new BorderLayout(0, 14));
        registerDialog.getContentPane().setBackground(new Color(245, 247, 250));
        ((JComponent) registerDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        registerDialog.add(UiKit.createHeader("管理员注册", "创建新的系统管理员账号。"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        addField(formPanel, "新用户名：", newUsernameField, gbc, 0);
        addField(formPanel, "新密码：", newPasswordField, gbc, 1);
        addField(formPanel, "确认密码：", confirmPasswordField, gbc, 2);

        registerDialog.add(UiKit.createCard(formPanel), BorderLayout.CENTER);

        JPanel buttonPanel = UiKit.createButtonPanel();
        JButton submitButton = UiKit.primaryButton("注册");
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

        buttonPanel.add(submitButton);
        registerDialog.add(UiKit.createButtonBar(buttonPanel), BorderLayout.SOUTH);

        registerDialog.setSize(440, 330);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            AdminLoginPanel loginFrame = new AdminLoginPanel();
            loginFrame.setVisible(true);
        });
    }
}
