package app;

import java.util.Scanner;
import services.AuthService;
import db.DBHelper;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DBHelper.initDatabase(); // Inisialisasi DB dan tabel-tabel

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n=== MENU LOGIN ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1" -> {
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    if (AuthService.login(username, password)) {
                        System.out.println("Login berhasil.");
                        app.MenuUtamaConsole.tampilkan(username); // langsung ke menu utama
                    } else {
                        System.out.println("Login gagal. Username atau password salah.");
                    }
                }

                case "2" -> {
                    System.out.print("Username baru: ");
                    String newUser = scanner.nextLine();
                    System.out.print("Password baru: ");
                    String newPass = scanner.nextLine();

                    if (AuthService.register(newUser, newPass)) {
                        System.out.println("Registrasi berhasil. Silakan login.");
                    } else {
                        System.out.println("Registrasi gagal. Username mungkin sudah digunakan.");
                    }
                }

                case "0" -> {
                    System.out.println("Keluar dari aplikasi.");
                    isRunning = false;
                }

                default -> System.out.println("Pilihan tidak valid.");
            }
        }

        scanner.close();
    }
}
