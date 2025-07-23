package app;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import services.PemasukkanService;
import services.PengeluaranService;
import models.Pemasukkan;
import models.Pengeluaran;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;

public class DashboardGrafikPanel extends JPanel {
    private JComboBox<String> tipeCombo;
    private JComboBox<String> rangeCombo;
    private JPanel chartContainer;

    public DashboardGrafikPanel() {
        setLayout(new BorderLayout());

        // Panel kontrol
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(235, 245, 255));

        tipeCombo = new JComboBox<>(new String[]{"Pemasukkan", "Pengeluaran"});
        rangeCombo = new JComboBox<>(new String[]{"Mingguan", "Bulanan", "Tahunan"});

        JButton btnTampilkan = new JButton("Tampilkan Grafik");
        btnTampilkan.setBackground(new Color(100, 160, 255));
        btnTampilkan.setForeground(Color.BLACK);
        btnTampilkan.setFont(new Font("Segoe UI", Font.BOLD, 14));

        controlPanel.add(new JLabel("Jenis:"));
        controlPanel.add(tipeCombo);
        controlPanel.add(new JLabel("Rentang:"));
        controlPanel.add(rangeCombo);
        controlPanel.add(btnTampilkan);

        chartContainer = new JPanel(new BorderLayout());

        add(controlPanel, BorderLayout.NORTH);
        add(chartContainer, BorderLayout.CENTER);

        btnTampilkan.addActionListener(this::updateChart);
        updateChart(null); // tampilkan default saat awal
    }

    private void updateChart(ActionEvent e) {
        String tipe = (String) tipeCombo.getSelectedItem();
        String rentang = (String) rangeCombo.getSelectedItem();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> dataMap = new TreeMap<>();

        try {
            if ("Pemasukkan".equals(tipe)) {
                List<Pemasukkan> list = PemasukkanService.getAll();
                for (Pemasukkan p : list) {
                    String key = getFormattedKey(p.getTanggal(), rentang);
                    dataMap.put(key, dataMap.getOrDefault(key, 0) + p.getJumlah());
                }
            } else {
                List<Pengeluaran> list = PengeluaranService.getAll();
                for (Pengeluaran p : list) {
                    String key = getFormattedKey(p.getTanggal(), rentang);
                    dataMap.put(key, dataMap.getOrDefault(key, 0) + p.getJumlah());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data grafik: " + ex.getMessage());
            return;
        }

        for (String key : dataMap.keySet()) {
            dataset.addValue(dataMap.get(key), tipe, key);
        }

        String chartTitle = String.format("Grafik %s (%s)", tipe, rentang);
        JFreeChart chart = ChartFactory.createBarChart(
                chartTitle,
                "Waktu",
                "Jumlah (Rp)",
                dataset
        );

        // Kostumisasi chart
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, tipe.equals("Pemasukkan") ? new Color(0, 153, 76) : new Color(255, 102, 102));
        renderer.setMaximumBarWidth(0.08);

        chartContainer.removeAll();
        chartContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    // Parsing tanggal menggunakan java.time
    private LocalDate parseDate(String tanggal) {
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        );

        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(tanggal, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        return null;
    }

    // Membuat key label yang rapi untuk grafik
    private String getFormattedKey(String tanggal, String rentang) {
        LocalDate date = parseDate(tanggal);
        if (date == null) return "Format Error";

        switch (rentang) {
            case "Mingguan":
                WeekFields wf = WeekFields.of(new Locale("id", "ID"));
                int minggu = date.get(wf.weekOfWeekBasedYear());
                int tahunMinggu = date.get(wf.weekBasedYear());
                return "Minggu " + minggu + " " + tahunMinggu;

            case "Bulanan":
                DateTimeFormatter bulanFormatter =
                        DateTimeFormatter.ofPattern("MMM yyyy", new Locale("id", "ID"));
                return date.format(bulanFormatter);

            case "Tahunan":
                return String.valueOf(date.getYear());

            default:
                return "Unknown";
        }
    }
}
