package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private Color normalColor = new Color(200, 230, 255);
    private Color hoverColor = new Color(180, 210, 255);
    private Color clickColor = new Color(150, 200, 255);
    private boolean hovered = false;
    private boolean clicked = false;

    // ✅ Konstruktor yang kamu perlukan
    public RoundedButton(String text, Icon icon) {
        super(text, icon);
        initButton();
    }

    // Optional: Konstruktor lain jika hanya pakai text saja
    public RoundedButton(String text) {
        super(text);
        initButton();
    }

    private void initButton() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setIconTextGap(10);
        setPreferredSize(new Dimension(230, 48));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                hovered = false;
                clicked = false;
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                clicked = true;
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                clicked = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bgColor = normalColor;
        if (clicked) {
            bgColor = clickColor;
        } else if (hovered) {
            bgColor = hoverColor;
        }

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    public void setContentAreaFilled(boolean b) {
        // override agar tidak pakai default fill
    }

    @Override
    protected void paintBorder(Graphics g) {
        // biarkan kosong
    }
}
