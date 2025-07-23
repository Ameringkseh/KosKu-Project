package app;

import db.DBHelper;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterFrame extends JFrame {

    private JTextField tfUsername;
    private JPasswordField pfPassword;

    public RegisterFrame() {
        setTitle("Register - KosKu");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Daftar Akun Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 111, 219));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        tfUsername = new JTextField(15);
        gbc.gridx = 1;
        panel.add(tfUsername, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        pfPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(pfPassword, gbc);

        JButton btnRegister = new JButton("Daftar");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBackground(new Color(173, 216, 230)); // biru muda
        btnRegister.setForeground(Color.BLACK);              // Teks hitam agar kontras
        btnRegister.setFocusPainted(false);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);

        // Set default button untuk aksi ENTER
        getRootPane().setDefaultButton(btnRegister);

        JButton btnToLogin = new JButton("Sudah punya akun? Login");
        btnToLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnToLogin.setForeground(new Color(33, 111, 219));
        btnToLogin.setBorderPainted(false);
        btnToLogin.setContentAreaFilled(false);
        btnToLogin.setFocusPainted(false);
        gbc.gridy = 4;
        panel.add(btnToLogin, gbc);

        add(panel);

        btnRegister.addActionListener(e -> register());

        btnToLogin.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void register() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBHelper.connect()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Koneksi database gagal.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
            dispose();
            new LoginFrame().setVisible(true);

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan.", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
