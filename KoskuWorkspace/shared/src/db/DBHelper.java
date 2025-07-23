package db;

import java.sql.*;

public class DBHelper {
    private static final String DB_PATH = "jdbc:sqlite:../shared/KosKu.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_PATH);
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }

    public static void initDatabase() {
        String sqlPengeluaran = "CREATE TABLE IF NOT EXISTS pengeluaran (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kategori TEXT," +
                "jenis TEXT," +
                "jumlah INTEGER," +
                "tanggal TEXT)" +
                "catatan TEXT";

        String sqlPemasukkan = "CREATE TABLE IF NOT EXISTS pemasukkan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sumber TEXT," +
                "jenis TEXT," +
                "jumlah INTEGER," +
                "tanggal TEXT)" +
                "catatan TEXT";

        String sqlSaldoAwal = "CREATE TABLE IF NOT EXISTS saldo_awal (" +
                "id INTEGER PRIMARY KEY CHECK(id = 1)," +
                "jumlah INTEGER)";

        String sqlUser = "CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)";

        try (Connection conn = connect()) {
            if (conn == null) {
                System.out.println("Gagal: koneksi null saat init database");
                return;
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlPengeluaran);
                stmt.execute(sqlPemasukkan);
                stmt.execute(sqlSaldoAwal);
                stmt.execute(sqlUser);

                // Tambahkan kolom catatan jika belum ada
                alterTableIfNeeded(conn, "pengeluaran");
                alterTableIfNeeded(conn, "pemasukkan");
            }

        } catch (Exception e) {
            System.out.println("Gagal membuat tabel: " + e.getMessage());
        }
    }

    // Tambahkan kolom 'catatan' jika belum ada di pengeluaran / pemasukkan
    private static void alterTableIfNeeded(Connection conn, String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")");
        boolean hasCatatan = false;

        while (rs.next()) {
            if ("catatan".equalsIgnoreCase(rs.getString("name"))) {
                hasCatatan = true;
                break;
            }
        }

        if (!hasCatatan) {
            stmt.execute("ALTER TABLE " + tableName + " ADD COLUMN catatan TEXT");
            System.out.println("Kolom catatan ditambahkan ke tabel: " + tableName);
        }
    }
    
    public static void resetUserTable() {
        String sql = "DELETE FROM user";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Semua akun pengguna berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Gagal menghapus akun: " + e.getMessage());
        }
    }

}
