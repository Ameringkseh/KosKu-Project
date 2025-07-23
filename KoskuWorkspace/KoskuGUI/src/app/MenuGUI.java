package app;

import models.Pemasukkan;
import models.Pengeluaran;
import services.PemasukkanService;
import services.PengeluaranService;
import services.SaldoAwalService;
import services.SaldoService;
import app.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;

import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class MenuGUI extends JFrame {
    private String username;
    private JLabel saldoLabel;
    private JLabel saldoAwalLabel;
    private JPanel chartPanelContainer;
    private JComboBox<String> jenisCombo;
    private JComboBox<String> periodeCombo;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;

    public MenuGUI(String username) {
        this.username = username;
        setTitle("KosKu - Menu Utama");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(4, 1));
        headerPanel.setBackground(new Color(220, 240, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Selamat datang, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        saldoAwalLabel = new JLabel();
        saldoAwalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        saldoAwalLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelSaldoTitle = new JLabel("Saldo Saat Ini");
        labelSaldoTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelSaldoTitle.setHorizontalAlignment(SwingConstants.CENTER);

        saldoLabel = new JLabel();
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        saldoLabel.setForeground(new Color(0, 153, 51));
        saldoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        updateSaldoLabel();

        headerPanel.add(welcomeLabel);
        headerPanel.add(saldoAwalLabel);
        headerPanel.add(labelSaldoTitle);
        headerPanel.add(saldoLabel);

        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // GRAFIK
        add(new DashboardGrafikPanel(), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 12, 12));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.setBackground(new Color(240, 250, 255));

        JButton btnPemasukkan = IconUtil.createButton("Tambah Pemasukkan", "add.png", 32, 32);
        JButton btnListPemasukkan = IconUtil.createButton("Tampilkan Pemasukkan", "table.png", 32, 32);
        JButton btnPengeluaran = IconUtil.createButton("Tambah Pengeluaran", "add.png", 32, 32);
        JButton btnListPengeluaran = IconUtil.createButton("Tampilkan Pengeluaran", "table.png", 32, 32);
        JButton btnUbahSaldo = IconUtil.createButton("Ubah Saldo Awal", "change.png", 32, 32);
        JButton btnTotalSaldo = IconUtil.createButton("Lihat Rincian Saldo", "view.png", 32, 32);
        JButton btnFilter = IconUtil.createButton("Filter Transaksi", "filter.png", 32, 32);
        JButton btnKeluar = IconUtil.createButton("Keluar", "exit.png", 32, 32);


        buttonPanel.add(btnPemasukkan);
        buttonPanel.add(btnListPemasukkan);
        buttonPanel.add(btnPengeluaran);
        buttonPanel.add(btnListPengeluaran);
        buttonPanel.add(btnUbahSaldo);
        buttonPanel.add(btnTotalSaldo);
        buttonPanel.add(btnFilter);
        buttonPanel.add(btnKeluar);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        btnPemasukkan.addActionListener((ActionEvent e) -> {
            JTextField sumber = new JTextField();
            JComboBox<String> jenis = new JComboBox<>(new String[]{"pemasukkan", "pengeluaran"});

            JPanel jumlahPanel = new JPanel(new BorderLayout());
            JLabel rpLabel = new JLabel("Rp ");
            JTextField jumlahField = new JTextField();
            jumlahPanel.add(rpLabel, BorderLayout.WEST);
            jumlahPanel.add(jumlahField, BorderLayout.CENTER);

            JDateChooser dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("yyyy-MM-dd");

            JTextField catatan = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
            inputPanel.add(new JLabel("Sumber:")); inputPanel.add(sumber);
            inputPanel.add(new JLabel("Jenis:")); inputPanel.add(jenis);
            inputPanel.add(new JLabel("Jumlah:")); inputPanel.add(jumlahPanel);
            inputPanel.add(new JLabel("Tanggal:")); inputPanel.add(dateChooser);
            inputPanel.add(new JLabel("Catatan:")); inputPanel.add(catatan);

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Tambah Pemasukkan", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String sumberText = sumber.getText().trim();
                    String jenisText = jenis.getSelectedItem().toString();
                    int jumlah = Integer.parseInt(jumlahField.getText().trim());
                    Date selectedDate = dateChooser.getDate();

                    if (selectedDate == null) {
                        throw new Exception("Tanggal belum dipilih.");
                    }

                    String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                    String catatanText = catatan.getText().trim();

                    Pemasukkan p = new Pemasukkan(0, sumberText, jenisText, jumlah, tanggal, catatanText);
                    PemasukkanService.create(p);
                    JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
                    updateSaldoLabel();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Input tidak valid: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnListPemasukkan.addActionListener((ActionEvent e) -> new TabelPemasukkanGUI());

        btnPengeluaran.addActionListener((ActionEvent e) -> {
            JTextField kategori = new JTextField();
            String[] jenisOptions = {"pengeluaran", "makan", "transport", "belanja", "lainnya"};
            JComboBox<String> jenisCombo = new JComboBox<>(jenisOptions);

            JPanel jumlahPanel = new JPanel(new BorderLayout());
            JLabel rpLabel = new JLabel("Rp ");
            JTextField jumlah = new JTextField();
            jumlahPanel.add(rpLabel, BorderLayout.WEST);
            jumlahPanel.add(jumlah, BorderLayout.CENTER);

            JDateChooser dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("yyyy-MM-dd");

            JTextField catatan = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(5, 2));
            inputPanel.add(new JLabel("Kategori:")); inputPanel.add(kategori);
            inputPanel.add(new JLabel("Jenis:")); inputPanel.add(jenisCombo);
            inputPanel.add(new JLabel("Jumlah:")); inputPanel.add(jumlahPanel);
            inputPanel.add(new JLabel("Tanggal:")); inputPanel.add(dateChooser);
            inputPanel.add(new JLabel("Catatan:")); inputPanel.add(catatan);

            int result = JOptionPane.showConfirmDialog(null, inputPanel, "Tambah Pengeluaran", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String kategoriText = kategori.getText();
                    String jenis = jenisCombo.getSelectedItem().toString();
                    int jumlahValue = Integer.parseInt(jumlah.getText().replaceAll("[^\\d]", ""));
                    Date selectedDate = dateChooser.getDate();

                    if (selectedDate == null) {
                        JOptionPane.showMessageDialog(null, "Tanggal belum dipilih.");
                        return;
                    }

                    String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                    String catatanText = catatan.getText();

                    Pengeluaran p = new Pengeluaran(0, kategoriText, jenis, jumlahValue, tanggal, catatanText);
                    PengeluaranService.create(p);
                    JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
                    updateSaldoLabel();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Jumlah harus berupa angka.");
                }
            }
        });

        btnListPengeluaran.addActionListener((ActionEvent e) -> new TabelPengeluaranGUI());


        btnUbahSaldo.addActionListener((ActionEvent e) -> {
            String input = JOptionPane.showInputDialog("Masukkan saldo awal baru:");
            if (input != null && !input.isBlank()) {
                int saldo = Integer.parseInt(input);
                SaldoAwalService.updateSaldoAwal(saldo);
                JOptionPane.showMessageDialog(null, "Saldo awal berhasil diperbarui!");
                updateSaldoLabel();
            }
        });

        btnTotalSaldo.addActionListener((ActionEvent e) -> {
            int saldoAwal = SaldoService.getSaldoAwal();
            int totalMasuk = SaldoService.getTotalPemasukkan();
            int totalKeluar = SaldoService.getTotalPengeluaran();
            int saldo = SaldoService.getTotalSaldo();
            String info = String.format("Saldo Awal: Rp %s\nTotal Pemasukkan: Rp %s\nTotal Pengeluaran: Rp %s\nTotal Saldo Saat Ini: Rp %s",
                    formatRupiah(saldoAwal), formatRupiah(totalMasuk), formatRupiah(totalKeluar), formatRupiah(saldo));
            JOptionPane.showMessageDialog(null, info);
        });

        btnFilter.addActionListener((ActionEvent e) -> {
            JDateChooser dateChooserAwal = new JDateChooser();
            dateChooserAwal.setDateFormatString("yyyy-MM-dd");

            JDateChooser dateChooserAkhir = new JDateChooser();
            dateChooserAkhir.setDateFormatString("yyyy-MM-dd");

            JComboBox<String> comboJenis = new JComboBox<>(new String[]{"Pengeluaran", "Pemasukkan"});
            JComboBox<String> comboSortir = new JComboBox<>(new String[]{
                "Tanggal Terbaru", "Tanggal Terlama", "Jumlah Terbesar", "Jumlah Terkecil"
            });

            JPanel filterPanel = new JPanel(new GridLayout(4, 2, 5, 5));
            filterPanel.add(new JLabel("Tanggal Awal:")); filterPanel.add(dateChooserAwal);
            filterPanel.add(new JLabel("Tanggal Akhir:")); filterPanel.add(dateChooserAkhir);
            filterPanel.add(new JLabel("Jenis:")); filterPanel.add(comboJenis);
            filterPanel.add(new JLabel("Urutkan Berdasarkan:")); filterPanel.add(comboSortir);

            int result = JOptionPane.showConfirmDialog(null, filterPanel, "Filter Transaksi", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Date awalDate = dateChooserAwal.getDate();
                Date akhirDate = dateChooserAkhir.getDate();

                if (awalDate == null || akhirDate == null) {
                    JOptionPane.showMessageDialog(null, "Tanggal awal dan akhir harus dipilih!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String awal = sdf.format(awalDate);
                String akhir = sdf.format(akhirDate);
                String jenis = (String) comboJenis.getSelectedItem();
                String sortir = (String) comboSortir.getSelectedItem();

                String[] kolom;
                Object[][] data;

                if ("Pengeluaran".equals(jenis)) {
                    List<Pengeluaran> list = PengeluaranService.filterByTanggal(awal, akhir);
                    // === SORTIR ===
                    list.sort((a, b) -> {
                        switch (sortir) {
                            case "Tanggal Terbaru":
                                return b.getTanggal().compareTo(a.getTanggal());
                            case "Tanggal Terlama":
                                return a.getTanggal().compareTo(b.getTanggal());
                            case "Jumlah Terbesar":
                                return Integer.compare(b.getJumlah(), a.getJumlah());
                            case "Jumlah Terkecil":
                                return Integer.compare(a.getJumlah(), b.getJumlah());
                            default:
                                return 0;
                        }
                    });

                    kolom = new String[]{"Kategori", "Jenis", "Jumlah", "Tanggal"};
                    data = new Object[list.size()][4];
                    for (int i = 0; i < list.size(); i++) {
                        Pengeluaran p = list.get(i);
                        data[i][0] = p.getKategori();
                        data[i][1] = p.getJenis();
                        data[i][2] = formatRupiah(p.getJumlah());
                        data[i][3] = p.getTanggal();
                    }

                } else {
                    List<Pemasukkan> list = PemasukkanService.filterByTanggal(awal, akhir);
                    // === SORTIR ===
                    list.sort((a, b) -> {
                        switch (sortir) {
                            case "Tanggal Terbaru":
                                return b.getTanggal().compareTo(a.getTanggal());
                            case "Tanggal Terlama":
                                return a.getTanggal().compareTo(b.getTanggal());
                            case "Jumlah Terbesar":
                                return Integer.compare(b.getJumlah(), a.getJumlah());
                            case "Jumlah Terkecil":
                                return Integer.compare(a.getJumlah(), b.getJumlah());
                            default:
                                return 0;
                        }
                    });

                    kolom = new String[]{"Sumber", "Jenis", "Jumlah", "Tanggal"};
                    data = new Object[list.size()][4];
                    for (int i = 0; i < list.size(); i++) {
                        Pemasukkan p = list.get(i);
                        data[i][0] = p.getSumber();
                        data[i][1] = p.getJenis();
                        data[i][2] = formatRupiah(p.getJumlah());
                        data[i][3] = p.getTanggal();
                    }
                }

                JTable table = new JTable(data, kolom);
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);

                if (data.length == 0) {
                    JOptionPane.showMessageDialog(null, "Tidak ada data ditemukan.");
                } else {
                    JOptionPane.showMessageDialog(null, scrollPane, "Hasil Filter " + jenis, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        btnKeluar.addActionListener((ActionEvent e) -> {
            dispose();      
            JOptionPane.showMessageDialog(null, "Terima kasih telah menggunakan KosKu!");
        });
        btnUbahSaldo.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Masukkan saldo awal baru:");
            if (input != null && !input.isBlank()) {
                int saldo = Integer.parseInt(input);
                SaldoAwalService.updateSaldoAwal(saldo);
                JOptionPane.showMessageDialog(null, "Saldo awal berhasil diperbarui!");
                updateSaldoLabel();
                updateChart();
            }
        });
        btnTotalSaldo.addActionListener(e -> {
            int saldoAwal = SaldoService.getSaldoAwal();
            int totalMasuk = SaldoService.getTotalPemasukkan();
            int totalKeluar = SaldoService.getTotalPengeluaran();
            int saldo = SaldoService.getTotalSaldo();
            String info = String.format("Saldo Awal: Rp %s\nTotal Pemasukkan: Rp %s\nTotal Pengeluaran: Rp %s\nTotal Saldo Saat Ini: Rp %s",
                    formatRupiah(saldoAwal), formatRupiah(totalMasuk), formatRupiah(totalKeluar), formatRupiah(saldo));
            JOptionPane.showMessageDialog(null, info);
        });

        setVisible(true);
    }

    private void updateSaldoLabel() {
        int saldo = SaldoService.getTotalSaldo();
        int saldoAwal = SaldoAwalService.getSaldoAwal();
        saldoLabel.setText("Rp " + formatRupiah(saldo));
        saldoAwalLabel.setText("Saldo Awal: Rp " + formatRupiah(saldoAwal));
    }

    private String formatRupiah(int amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    private JButton createButton(String text, Icon icon) {
        JButton btn = new JButton(text, icon);
        btn.setBackground(new Color(200, 230, 255));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    private void updateChart() {
        String jenis = jenisCombo.getSelectedItem().toString();
        String periode = periodeCombo.getSelectedItem().toString();
        Date start = startDateChooser.getDate();
        Date end = endDateChooser.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            if (jenis.equals("Pemasukkan")) {
                List<Pemasukkan> list = PemasukkanService.getAll();
                for (Pemasukkan p : list) {
                    dataset.addValue(p.getJumlah(), "Jumlah", p.getTanggal());
                }
            } else {
                List<Pengeluaran> list = PengeluaranService.getAll();
                for (Pengeluaran p : list) {
                    dataset.addValue(p.getJumlah(), "Jumlah", p.getTanggal());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Grafik " + jenis,
                "Tanggal",
                "Jumlah",
                dataset
        );

        chartPanelContainer.removeAll();
        chartPanelContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }
    
    private JButton createButton(String text, String iconFileName) {
        ImageIcon icon = IconUtil.getIcon(iconFileName, 20, 20); // otomatis resize
        JButton btn = new JButton(text, icon);
        btn.setBackground(new Color(200, 230, 255));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setIconTextGap(10); // jarak teks dengan icon
        btn.setHorizontalAlignment(SwingConstants.LEFT); // icon di kiri
        return btn;
    }

}
