package app;

import javax.swing.*;
import db.DBHelper;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainGUI {
    public static void main(String[] args) {
        // Set Look and Feel ke sistem OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Gagal set look and feel: " + e.getMessage());
        }

        // Tampilkan GUI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("KosKu - Aplikasi Keuangan Anak Kos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            // Panel utama
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(new Color(245, 250, 255));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Judul
            JLabel titleLabel = new JLabel("KosKu", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            titleLabel.setForeground(new Color(33, 111, 219));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            gbc.gridy = 0;
            mainPanel.add(titleLabel, gbc);

            // Deskripsi
            JLabel descLabel = new JLabel("Manajemen Keuangan Praktis untuk Anak Kos", SwingConstants.CENTER);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            descLabel.setForeground(new Color(80, 80, 80));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            gbc.gridy = 1;
            mainPanel.add(descLabel, gbc);

            // Spacer
            gbc.gridy = 2;
            mainPanel.add(Box.createVerticalStrut(20), gbc);

         // Tombol "Mulai"
            JButton startButton = new JButton("Mulai");
            startButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            startButton.setBackground(new Color(173, 216, 230)); // Warna biru muda
            startButton.setForeground(Color.BLACK);              // Teks hitam agar kontras
            startButton.setFocusPainted(false);
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            gbc.gridy = 3;
            mainPanel.add(startButton, gbc);


            // Tombol "Reset Akun"
            JButton btnReset = new JButton("Reset Akun");
            btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnReset.setForeground(Color.RED);
            btnReset.setFocusPainted(false);
            btnReset.setBorderPainted(false);
            btnReset.setContentAreaFilled(false);
            btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
            gbc.gridy = 4;
            mainPanel.add(btnReset, gbc);

            // Event tombol
            startButton.addActionListener(e -> {
                frame.dispose(); // Tutup jendela utama
                new RegisterFrame().setVisible(true); // Buka jendela registrasi
            });

            btnReset.addActionListener((ActionEvent e) -> {
                int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin hapus semua akun?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DBHelper.resetUserTable();
                    JOptionPane.showMessageDialog(null, "Semua akun telah dihapus.");
                }
            });

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
