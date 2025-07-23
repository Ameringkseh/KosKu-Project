package app;

import services.AuthService;
import services.PemasukkanService;
import services.PengeluaranService;
import services.SaldoAwalService;
import services.SaldoService;
import models.Pemasukkan;
import models.Pengeluaran;

import java.util.List;
import java.util.Scanner;

public class MenuUtamaConsole {
    private static Scanner scanner = new Scanner(System.in);

    public static void tampilkan(String username) {
        System.out.println("Selamat datang, " + username + "!");
        menuSetelahLogin();  // Langsung ke menu utama
    }

    private static void filterTransaksi() {
        System.out.print("Masukkan tanggal awal (yyyy-mm-dd): ");
        String tglAwal = scanner.nextLine();

        System.out.print("Masukkan tanggal akhir (yyyy-mm-dd): ");
        String tglAkhir = scanner.nextLine();

        System.out.println("Pilih jenis transaksi:");
        System.out.println("1. Pengeluaran");
        System.out.println("2. Pemasukkan");
        System.out.print("Pilihan: ");
        int jenis = Integer.parseInt(scanner.nextLine());

        if (jenis == 1) {
            List<Pengeluaran> hasil = PengeluaranService.filterByTanggal(tglAwal, tglAkhir);
            System.out.println("\nHasil Filter Pengeluaran:");
            for (Pengeluaran p : hasil) {
                System.out.printf("ID: %d | Kategori: %s | Jenis: %s | Jumlah: %d | Tanggal: %s\n",
                    p.getId(), p.getKategori(), p.getJenis(), p.getJumlah(), p.getTanggal());
            }
        } else if (jenis == 2) {
            List<Pemasukkan> hasil = PemasukkanService.filterByTanggal(tglAwal, tglAkhir);
            System.out.println("\nHasil Filter Pemasukkan:");
            for (Pemasukkan p : hasil) {
                System.out.printf("ID: %d | Sumber: %s | Jenis: %s | Jumlah: %d | Tanggal: %s\n",
                    p.getId(), p.getSumber(), p.getJenis(), p.getJumlah(), p.getTanggal());
            }
        } else {
            System.out.println("Pilihan tidak valid.");
        }

        menuSetelahLogin();
    }

    public static void menuSetelahLogin() {
        while (true) {
            System.out.println("\n=== Menu Utama ===");
            System.out.println("1. Tambah Pemasukkan");
            System.out.println("2. Tampilkan Pemasukkan");
            System.out.println("3. Tambah Pengeluaran");
            System.out.println("4. Tampilkan Pengeluaran");
            System.out.println("5. Tampilkan Saldo Awal");
            System.out.println("6. Ubah Saldo Awal");
            System.out.println("7. Lihat Total Saldo");
            System.out.println("8. Filter Transaksi");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilih = scanner.nextInt();
            scanner.nextLine();

            switch (pilih) {
                case 1 -> {
                    System.out.print("Sumber: ");
                    String sumber = scanner.nextLine();
                    System.out.print("Jenis: ");
                    String jenis = scanner.nextLine();
                    System.out.print("Jumlah: ");
                    int jumlah = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Tanggal (yyyy-MM-dd): ");
                    String tanggal = scanner.nextLine();
                    System.out.print("Catatan: ");
                    String catatan = scanner.nextLine();

                    Pemasukkan pemasukkan = new Pemasukkan(0, sumber, jenis, jumlah, tanggal, catatan);
                    PemasukkanService.create(pemasukkan);
                }
                case 2 -> PemasukkanService.readAll();
                case 3 -> {
                    System.out.print("Kategori: ");
                    String kategori = scanner.nextLine();
                    System.out.print("Jenis: ");
                    String jenis = scanner.nextLine();
                    System.out.print("Jumlah: ");
                    int jumlah = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Tanggal (yyyy-MM-dd): ");
                    String tanggal = scanner.nextLine();
                    System.out.print("Catatan: ");
                    String catatan = scanner.nextLine();

                    Pengeluaran pengeluaran = new Pengeluaran(0, kategori, jenis, jumlah, tanggal, catatan);
                    PengeluaranService.create(pengeluaran);
                }
                case 4 -> PengeluaranService.readAll();
                case 5 -> {
                    int saldo = SaldoAwalService.getSaldoAwal();
                    System.out.println("Saldo Awal: " + saldo);
                }
                case 6 -> {
                    System.out.print("Masukkan saldo awal baru: ");
                    int saldoBaru = Integer.parseInt(scanner.nextLine());
                    SaldoAwalService.updateSaldoAwal(saldoBaru);
                }
                case 7 -> {
                    int saldoAwal = SaldoService.getSaldoAwal();
                    int totalPemasukkan = SaldoService.getTotalPemasukkan();
                    int totalPengeluaran = SaldoService.getTotalPengeluaran();
                    int saldo = SaldoService.getTotalSaldo();

                    System.out.println("\n--- Ringkasan Keuangan ---");
                    System.out.println("Saldo Awal       : " + saldoAwal);
                    System.out.println("Total Pemasukkan : " + totalPemasukkan);
                    System.out.println("Total Pengeluaran: " + totalPengeluaran);
                    System.out.println("Total Saldo Saat Ini: " + saldo);
                    System.out.println("--------------------------\n");
                }
                case 8 -> filterTransaksi();
                case 0 -> {
                    System.out.println("Keluar...");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid");
            }
        }
    }
}
