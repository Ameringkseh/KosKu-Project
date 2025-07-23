package app;

import models.Pengeluaran;
import services.PengeluaranService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TabelPengeluaranGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private boolean[] hapusVisible;
    private boolean[] editMode;

    public TabelPengeluaranGUI() {
        setTitle("Daftar Pengeluaran");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] kolom = {"ID", "Sumber", "Jenis", "Jumlah", "Tanggal", "Catatan", "Aksi", "Hapus"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editMode[row] && column >= 1 && column <= 5;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(200, 200, 200));
        table.setBackground(new Color(250, 250, 250));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(220, 220, 220));
        header.setForeground(Color.BLACK);
        header.setOpaque(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        String[] jenisPilihan = {"pengeluaran", "makan", "transport", "belanja", "lainnya"};
        JComboBox<String> jenisCombo = new JComboBox<>(jenisPilihan);
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jenisCombo));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadData();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int col = table.getSelectedColumn();
                int row = table.getSelectedRow();

                if (col == 6) {
                    if (!editMode[row]) {
                        editMode[row] = true;
                        hapusVisible[row] = true;
                        tableModel.setValueAt("✔ Simpan", row, 6);
                        table.repaint();
                    } else {
                        try {
                            int id = (int) tableModel.getValueAt(row, 0);
                            String sumber = tableModel.getValueAt(row, 1).toString();
                            String jenis = tableModel.getValueAt(row, 2).toString();
                            int jumlah = Integer.parseInt(tableModel.getValueAt(row, 3).toString());
                            String tanggal = tableModel.getValueAt(row, 4).toString();
                            String catatan = tableModel.getValueAt(row, 5).toString();

                            Pengeluaran p = new Pengeluaran(id, sumber, jenis, jumlah, tanggal, catatan);
                            PengeluaranService.update(p);

                            editMode[row] = false;
                            hapusVisible[row] = false;
                            tableModel.setValueAt("✎ Edit", row, 6);
                            table.repaint();

                            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Gagal update: " + ex.getMessage());
                        }
                    }
                } else if (col == 7 && hapusVisible[row]) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int id = (int) tableModel.getValueAt(row, 0);
                        PengeluaranService.delete(id);
                        tableModel.removeRow(row);

                        boolean[] newHapusVisible = new boolean[hapusVisible.length - 1];
                        boolean[] newEditMode = new boolean[editMode.length - 1];
                        for (int i = 0, j = 0; i < hapusVisible.length; i++) {
                            if (i != row) {
                                newHapusVisible[j] = hapusVisible[i];
                                newEditMode[j] = editMode[i];
                                j++;
                            }
                        }
                        hapusVisible = newHapusVisible;
                        editMode = newEditMode;

                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    }
                }
            }
        });

        setVisible(true);
    }

    private void loadData() {
        List<Pengeluaran> list = PengeluaranService.getAll();
        hapusVisible = new boolean[list.size()];
        editMode = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Pengeluaran p = list.get(i);
            Object[] row = {
                p.getId(),
                p.getKategori(),
                p.getJenis(),
                p.getJumlah(),
                p.getTanggal(),
                p.getCatatan(),
                "✎ Edit",
                ""
            };
            tableModel.addRow(row);
            hapusVisible[i] = false;
            editMode[i] = false;
        }

        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                if (hapusVisible[row]) {
                    label.setText("🗑 Hapus");
                    label.setForeground(Color.RED);
                } else {
                    label.setText("");
                }
                return label;
            }
        });

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                label.setText(value.toString());
                label.setForeground(new Color(0, 102, 204));
                return label;
            }
        });
    }
}
