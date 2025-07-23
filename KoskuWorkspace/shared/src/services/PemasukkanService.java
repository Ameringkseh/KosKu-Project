package services;

import db.DBHelper;
import models.Pemasukkan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemasukkanService {

    // Menambahkan pemasukkan
    public static void create(Pemasukkan pemasukkan) {
        String sql = "INSERT INTO pemasukkan (sumber, jenis, jumlah, tanggal) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pemasukkan.getSumber());
            pstmt.setString(2, pemasukkan.getJenis());
            pstmt.setInt(3, pemasukkan.getJumlah());
            pstmt.setString(4, pemasukkan.getTanggal());
            pstmt.executeUpdate();
            System.out.println("Pemasukkan berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan pemasukkan: " + e.getMessage());
        }
    }

    // Mengambil semua pemasukkan
    public static List<Pemasukkan> getAllPemasukkan() {
        List<Pemasukkan> pemasukkanList = new ArrayList<>();
        String sql = "SELECT * FROM pemasukkan";

        try (Connection conn = DBHelper.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Pemasukkan pemasukkan = new Pemasukkan(
                        rs.getInt("id"),
                        rs.getString("sumber"),
                        rs.getString("jenis"),
                        rs.getInt("jumlah"),
                        rs.getString("tanggal"),
                        rs.getString("catatan")
                );
                pemasukkanList.add(pemasukkan);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil pemasukkan: " + e.getMessage());
        }
        return pemasukkanList;
    }

    // Mengupdate pemasukkan
    public static boolean update(Pemasukkan p) {
        String sql = "UPDATE pemasukkan SET sumber = ?, jenis = ?, jumlah = ?, tanggal = ?, catatan = ? WHERE id = ?";
        try (Connection conn = DBHelper.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getSumber());
            stmt.setString(2, p.getJenis());
            stmt.setInt(3, p.getJumlah());
            stmt.setString(4, p.getTanggal());
            stmt.setString(5, p.getCatatan());
            stmt.setInt(6, p.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal update pemasukkan: " + e.getMessage());
            return false;
        }
    }


    // Menghapus pemasukkan
    public static boolean deletePemasukkan(int id) {
        String sql = "DELETE FROM pemasukkan WHERE id = ?";

        try (Connection conn = DBHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Pemasukkan berhasil dihapus!");
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal menghapus pemasukkan: " + e.getMessage());
        }
			return false;
    }
    
    public static void readAll() {
        String sql = "SELECT * FROM pemasukkan";
        try (Connection conn = DBHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Daftar Pemasukkan ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Sumber: " + rs.getString("sumber"));
                System.out.println("Jenis: " + rs.getString("jenis"));
                System.out.println("Jumlah: " + rs.getInt("jumlah"));
                System.out.println("Tanggal: " + rs.getString("tanggal"));
                System.out.println("-------------------------");
            }
            
        } catch (Exception e) {
            System.out.println("Gagal mengambil data pemasukkan: " + e.getMessage());
        }
    }
    
    public static List<Pemasukkan> filterByTanggal(String dari, String sampai) {
        List<Pemasukkan> list = new ArrayList<>();
        String sql = "SELECT * FROM pemasukkan WHERE tanggal BETWEEN ? AND ?";

        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dari);
            stmt.setString(2, sampai);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Pemasukkan(
                    rs.getInt("id"),
                    rs.getString("sumber"),
                    rs.getString("jenis"),
                    rs.getInt("jumlah"),
                    rs.getString("tanggal"),
                    rs.getString("catatan")
                ));
            }
        } catch (Exception e) {
            System.out.println("Gagal filter pemasukkan: " + e.getMessage());
        }
        return list;
    }
    
    public static List<Pemasukkan> getAll() {
        return getAllPemasukkan();
    }


}
