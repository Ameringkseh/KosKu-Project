package services;

import db.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SaldoService {

    public static int getTotalPemasukkan() {
        String sql = "SELECT SUM(jumlah) FROM pemasukkan";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.getInt(1);
        } catch (Exception e) {
            System.out.println("Gagal hitung pemasukkan: " + e.getMessage());
            return 0;
        }
    }

    public static int getTotalPengeluaran() {
        String sql = "SELECT SUM(jumlah) FROM pengeluaran";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.getInt(1);
        } catch (Exception e) {
            System.out.println("Gagal hitung pengeluaran: " + e.getMessage());
            return 0;
        }
    }

    public static int getSaldoAwal() {
        String sql = "SELECT jumlah FROM saldo_awal WHERE id = 1";
        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt("jumlah");
        } catch (Exception e) {
            System.out.println("Gagal ambil saldo awal: " + e.getMessage());
        }
        return 0;
    }

    public static int getTotalSaldo() {
        return getSaldoAwal() + getTotalPemasukkan() - getTotalPengeluaran();
    }
}
