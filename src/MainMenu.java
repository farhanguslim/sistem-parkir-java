import dao.*;
import model.User;

import java.sql.ResultSet;
import java.util.Scanner;

public class MainMenu {

    // ================= HELPER =================
    static int inputMenu(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Masukkan angka yang valid");
            }
        }
    }

    static String inputPlat(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            String plat = sc.nextLine().trim().toUpperCase().replaceAll("\\s+", " ");
            if (plat.length() >= 3) return plat;
            System.out.println("❌ Plat tidak valid");
        }
    }

    static String inputJenis(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            String jenis = sc.nextLine().trim();
            if (jenis.equalsIgnoreCase("motor") || jenis.equalsIgnoreCase("mobil"))
                return jenis;
            System.out.println("❌ Jenis harus Motor atau Mobil");
        }
    }

    static boolean konfirmasi(Scanner sc, String pesan) {
        System.out.print(pesan + " (ya/tidak): ");
        return sc.nextLine().equalsIgnoreCase("ya");
    }

    static boolean isAdmin(User u) {
        return u.getRole().equalsIgnoreCase("ADMIN");
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        UserDAO userDAO = new UserDAO();
        KendaraanDAO kendaraanDAO = new KendaraanDAO();
        ParkirMasukDAO masukDAO = new ParkirMasukDAO();
        ParkirKeluarDAO keluarDAO = new ParkirKeluarDAO();
        DataParkirDAO dataDAO = new DataParkirDAO();
        RekapPendapatanDAO rekapDAO = new RekapPendapatanDAO();
        ExportCSVDAO exportDAO = new ExportCSVDAO();
        BulkDeleteDAO bulkDAO = new BulkDeleteDAO();

        // ===== LOOP LOGIN =====
        while (true) {

            User user = null;
            System.out.println("\n===== LOGIN SISTEM PARKIR =====");

            while (user == null) {
                try {
                    System.out.print("Username: ");
                    String u = sc.nextLine();
                    System.out.print("Password: ");
                    String p = sc.nextLine();

                    user = userDAO.login(u, p);
                    if (user == null) {
                        System.out.println("❌ Login gagal");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ " + e.getMessage());
                }
            }

            System.out.println("✅ Login sebagai " + user.getRole());

            int pilih;
            do {
                System.out.println("\n===== MENU UTAMA =====");
                System.out.println("1. Data Parkir");
                System.out.println("2. Parkir Masuk");
                System.out.println("3. Parkir Keluar");

                if (isAdmin(user)) {
                    System.out.println("4. Update Jenis Kendaraan");
                    System.out.println("5. Rekap Pendapatan");
                    System.out.println("6. Export CSV");
                    System.out.println("7. Bulk Delete (Massal)");
                    System.out.println("8. Hapus Data Parkir (Per Plat)");
                }

                System.out.println("0. Logout");
                pilih = inputMenu(sc, "Pilih: ");

                try {
                    switch (pilih) {

                        // ===== DATA PARKIR =====
                        case 1 -> {
                            ResultSet rs = dataDAO.getAll();
                            System.out.println(
                                    "NO_PLAT | JENIS | JAM_MASUK | JAM_KELUAR | BIAYA | STATUS");
                            while (rs.next()) {
                                System.out.println(
                                        rs.getString("NO_PLAT") + " | " +
                                                rs.getString("JENIS") + " | " +
                                                rs.getString("JAM_MASUK") + " | " +
                                                rs.getString("JAM_KELUAR") + " | " +
                                                rs.getInt("BIAYA") + " | " +
                                                rs.getString("STATUS")
                                );
                            }
                        }

                        // ===== PARKIR MASUK =====
                        case 2 -> {
                            String platMasuk = inputPlat(sc, "No Plat: ");
                            String jenis = inputJenis(sc, "Jenis (Motor/Mobil): ");

                            try {
                                if (kendaraanDAO.exists(platMasuk))
                                    kendaraanDAO.updateJenis(platMasuk, jenis);
                                else
                                    kendaraanDAO.insert(platMasuk, jenis);

                                masukDAO.insert(platMasuk);
                                System.out.println("✅ Parkir masuk berhasil");

                            } catch (Exception e) {
                                if (e.getMessage() != null && e.getMessage().contains("20001")) {
                                    System.out.println("❌ Kendaraan masih parkir, silakan parkir keluar dulu.");
                                } else {
                                    System.out.println("⚠️ " + e.getMessage());
                                }
                            }
                        }

                        // ===== PARKIR KELUAR =====
                        case 3 -> {
                            String platKeluar = inputPlat(sc, "No Plat: ");

                            String jenis = keluarDAO.getJenisByNoPlat(platKeluar);
                            int biaya = keluarDAO.getBiayaByNoPlat(platKeluar);

                            System.out.println("Jenis Kendaraan : " + jenis);
                            System.out.println("Biaya           : Rp " + biaya);

                            if (konfirmasi(sc, "Sudah dibayar")) {
                                keluarDAO.insertByNoPlat(platKeluar);
                                System.out.println("✅ Parkir keluar berhasil");
                            }
                        }

                        // ===== UPDATE JENIS =====
                        case 4 -> {
                            if (isAdmin(user)) {
                                kendaraanDAO.updateJenis(
                                        inputPlat(sc, "No Plat: "),
                                        inputJenis(sc, "Jenis baru: ")
                                );
                                System.out.println("✅ Jenis kendaraan berhasil diupdate");
                            }
                        }

                        // ===== REKAP =====
                        case 5 -> {
                            if (isAdmin(user)) {
                                rekapDAO.rekapHariIni();
                            }
                        }

                        // ===== EXPORT CSV =====
                        case 6 -> {
                            if (isAdmin(user)) {
                                exportDAO.exportDataParkir();
                                exportDAO.exportRekapPendapatan();
                            }
                        }

                        // ===== BULK DELETE =====
                        case 7 -> {
                            if (isAdmin(user) &&
                                    konfirmasi(sc, "Hapus semua kendaraan yang sudah keluar")) {

                                bulkDAO.hapusKendaraanSudahKeluar();
                                System.out.println("🧹 Data massal berhasil dihapus");
                            }
                        }

                        // ===== HAPUS PER PLAT =====
                        case 8 -> {
                            if (isAdmin(user)) {
                                String plat = inputPlat(sc, "No Plat: ");
                                if (konfirmasi(sc, "Hapus TOTAL data parkir " + plat)) {
                                    bulkDAO.deleteTotalByNoPlat(plat);
                                    System.out.println("🗑️ Data parkir " + plat + " berhasil dihapus");
                                }
                            }
                        }

                        case 0 -> System.out.println("👋 Logout");
                    }

                } catch (Exception e) {
                    System.out.println("⚠️ " + e.getMessage());
                }

            } while (pilih != 0);
        }
    }
}
