import dao.*;
import model.User;

import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();

        User loggedIn = null;

        // ===== LOGIN CLI =====
        while (loggedIn == null) {
            System.out.println("===== LOGIN =====");
            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            loggedIn = userDAO.login(username, password);

            if (loggedIn == null) {
                System.out.println("❌ Login gagal, coba lagi\n");
            }
        }

        System.out.println("✔ Login berhasil sebagai " + loggedIn.getRole() + "\n");

        // ===== DAO =====
        DataParkirDAO dataDAO = new DataParkirDAO();
        ParkirMasukDAO masukDAO = new ParkirMasukDAO();
        ParkirKeluarDAO keluarDAO = new ParkirKeluarDAO();
        RekapPendapatanDAO rekapDAO = new RekapPendapatanDAO();
        BulkDeleteDAO bulkDAO = new BulkDeleteDAO();

        // ===== MENU =====
        while (true) {
            try {
                System.out.println("\n===== MENU UTAMA =====");
                System.out.println("1. Data Parkir");
                System.out.println("2. Parkir Masuk");
                System.out.println("3. Parkir Keluar");
                System.out.println("4. Rekap Pendapatan Hari Ini");
                System.out.println("5. Export CSV");
                System.out.println("6. Bulk Delete (ADMIN)");
                System.out.println("0. Logout");
                System.out.print("Pilih: ");

                int pilih = Integer.parseInt(sc.nextLine());

                switch (pilih) {

                    case 1 -> {
                        // 🔥 FIX: DATA PARKIR TAMPIL
                        dataDAO.printAllCLI();
                    }

                    case 2 -> {
                        System.out.print("No Plat: ");
                        String plat = sc.nextLine().trim().toUpperCase();

                        System.out.print("Jenis (Motor/Mobil): ");
                        String jenis = sc.nextLine();

                        masukDAO.insert(plat, jenis);
                        System.out.println("✔ Parkir masuk berhasil");
                    }

                    case 3 -> {
                        System.out.print("No Plat: ");
                        String plat = sc.nextLine().trim().toUpperCase();

                        int biaya = keluarDAO.hitungBiaya(plat);
                        System.out.println("Total Biaya: Rp " + biaya);

                        keluarDAO.parkirKeluar(plat);
                        System.out.println("✔ Parkir keluar berhasil");
                    }

                    case 4 -> {
                        int total = rekapDAO.getPendapatanHariIni();
                        System.out.println("Pendapatan hari ini: Rp " + total);
                    }

                    case 5 -> {
                        ExportCSVDAO exportDAO = new ExportCSVDAO();
                        exportDAO.exportDataParkir();
                        exportDAO.exportRekapPendapatan();
                        System.out.println("✔ Export CSV berhasil");
                    }

                    case 6 -> {
                        if (!loggedIn.getRole().equalsIgnoreCase("ADMIN")) {
                            System.out.println("❌ Akses ditolak (ADMIN only)");
                            break;
                        }
                        bulkDAO.deleteAll();
                        System.out.println("🔥 Semua data parkir dihapus");
                    }

                    case 0 -> {
                        System.out.println("Logout...");
                        System.exit(0);
                    }

                    default -> System.out.println("Menu tidak valid");
                }

            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }
}
