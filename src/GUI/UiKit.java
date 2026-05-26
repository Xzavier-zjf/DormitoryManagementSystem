package GUI;

import javax.swing.*;
import java.awt.*;

public class UiKit {
    private static final Color PAGE_BACKGROUND = new Color(245, 247, 250);
    private static final Color PANEL_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 226, 235);
    private static final Color PRIMARY_COLOR = new Color(44, 92, 160);

    private UiKit() {
    }

    public static void applyGlobalStyle() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("TabbedPane.tabInsets", new Insets(8, 18, 8, 18));
        UIManager.put("Button.margin", new Insets(7, 16, 7, 16));
        UIManager.put("TextField.margin", new Insets(4, 6, 4, 6));
    }

    public static JPanel createPage(String title, String subtitle, JPanel formPanel, JPanel buttonPanel) {
        JPanel page = new JPanel(new BorderLayout(0, 14));
        page.setBackground(PAGE_BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        page.add(createHeader(title, subtitle), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(createCard(formPanel), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PAGE_BACKGROUND);
        scrollPane.setOpaque(false);
        page.add(scrollPane, BorderLayout.CENTER);

        if (buttonPanel != null) {
            page.add(createButtonBar(buttonPanel), BorderLayout.SOUTH);
        }

        return page;
    }

    public static JPanel createHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout(0, 4));
        header.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        titleLabel.setForeground(new Color(32, 43, 57));
        header.add(titleLabel, BorderLayout.NORTH);

        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.PLAIN, 13f));
            subtitleLabel.setForeground(new Color(92, 103, 118));
            header.add(subtitleLabel, BorderLayout.CENTER);
        }

        return header;
    }

    public static JPanel createCard(JPanel content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    public static GridBagConstraints formConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    public static void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int columnPair) {
        int labelColumn = columnPair * 2;
        gbc.gridx = labelColumn;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setHorizontalAlignment(SwingConstants.RIGHT);
        labelComponent.setPreferredSize(new Dimension(100, 28));
        panel.add(labelComponent, gbc);

        gbc.gridx = labelColumn + 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        field.setMinimumSize(new Dimension(170, 32));
        field.setPreferredSize(new Dimension(190, 32));
        panel.add(field, gbc);
    }

    public static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        panel.setOpaque(false);
        return panel;
    }

    public static JPanel createButtonBar(JPanel content) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(PAGE_BACKGROUND);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        wrapper.add(content, BorderLayout.EAST);
        return wrapper;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(new Color(24, 48, 84));
        button.setBackground(new Color(224, 236, 252));
        button.setFocusPainted(false);
        button.setOpaque(true);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }
}
