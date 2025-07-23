package services;

import db.DBHelper;
import models.Pengeluaran;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PengeluaranService {
    public static void create(Pengeluaran pengeluaran) {
        String sql = "INSERT INTO pengeluaran (kategori, jenis, jumlah, tanggal, catatan) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pengeluaran.getKategori());
            pstmt.setString(2, pengeluaran.getJenis());
            pstmt.setInt(3, pengeluaran.getJumlah());
            pstmt.setString(4, pengeluaran.getTanggal());
            pstmt.setString(5, pengeluaran.getCatatan());
            pstmt.executeUpdate();
            System.out.println("Pengeluaran berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan pengeluaran: " + e.getMessage());
        }
    }

    public static List<Pengeluaran> getAll() {
        List<Pengeluaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pengeluaran";
        try (Connection conn = DBHelper.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pengeluaran(
                    rs.getInt("id"),
                    rs.getString("kategori"),
                    rs.getString("jenis"),
                    rs.getInt("jumlah"),
                    rs.getString("tanggal"),
                    rs.getString("catatan")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Gagal ambil data pengeluaran: " + e.getMessage());
        }
        return list;
    }

    public static boolean update(Pengeluaran p) {
        String sql = "UPDATE pengeluaran SET kategori = ?, jenis = ?, jumlah = ?, tanggal = ?, catatan = ? WHERE id = ?";
        try (Connection conn = DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getKategori());
            stmt.setString(2, p.getJenis());
            stmt.setInt(3, p.getJumlah());
            stmt.setString(4, p.getTanggal());
            stmt.setString(5, p.getCatatan());
            stmt.setInt(6, p.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal update pengeluaran: " + e.getMessage());
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM pengeluaran WHERE id = ?";
        try (Connection conn = DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Pengeluaran berhasil dihapus!");
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal hapus pengeluaran: " + e.getMessage());
            return false;
        }
    }

    public static void readAll() {
        String sql = "SELECT * FROM pengeluaran";
        try (Connection conn = DBHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Daftar Pengeluaran ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Kategori: " + rs.getString("kategori"));
                System.out.println("Jenis: " + rs.getString("jenis"));
                System.out.println("Jumlah: " + rs.getInt("jumlah"));
                System.out.println("Tanggal: " + rs.getString("tanggal"));
                System.out.println("Catatan: " + rs.getString("catatan"));
                System.out.println("---------------------------");
            }

        } catch (Exception e) {
            System.out.println("Gagal mengambil data pengeluaran: " + e.getMessage());
        }
    }

    public static List<Pengeluaran> filterByTanggal(String dari, String sampai) {
        List<Pengeluaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pengeluaran WHERE tanggal BETWEEN ? AND ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dari);
            stmt.setString(2, sampai);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Pengeluaran(
                    rs.getInt("id"),
                    rs.getString("kategori"),
                    rs.getString("jenis"),
                    rs.getInt("jumlah"),
                    rs.getString("tanggal"),
                    rs.getString("catatan")
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal filter pengeluaran: " + e.getMessage());
        }
        return list;
    }
}
