package services;

import db.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SaldoAwalService {

    public static void setSaldoAwal(int jumlah) {
        String sql = "INSERT OR REPLACE INTO saldo_awal (id, jumlah) VALUES (1, ?)";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jumlah);
            stmt.executeUpdate();
            System.out.println("Saldo awal berhasil disimpan.");
        } catch (Exception e) {
            System.out.println("Gagal menyimpan saldo awal: " + e.getMessage());
        }
    }

    public static int getSaldoAwal() {
        String sql = "SELECT jumlah FROM saldo_awal WHERE id = 1";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("jumlah");
            }
        } catch (Exception e) {
            System.out.println("Gagal mengambil saldo awal: " + e.getMessage());
        }
        return 0; // default jika belum diatur
    }

    public static void updateSaldoAwal(int jumlah) {
        setSaldoAwal(jumlah); // karena kita pakai INSERT OR REPLACE
    }
}
