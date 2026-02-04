package dao;

import config.DatabaseConnection;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExportCSVDAO {

    private static final String BASE_DIR = "export/csv";

    // ================== UTIL ==================
    private void ensureDir(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // =====================================================
    // =============== METHOD LAMA (WAJIB ADA) ===============
    // =====================================================

    // Dipakai MainMenu & ExportCSVFrame
    public void exportDataParkir() throws Exception {

        File dir = new File(BASE_DIR);
        ensureDir(dir);

        String sql =
                "SELECT NO_PLAT, JENIS, JAM_MASUK, JAM_KELUAR, BIAYA, STATUS " +
                        "FROM V_DATA_PARKIR ORDER BY JAM_MASUK DESC";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        FileWriter fw = new FileWriter(new File(dir, "data_parkir.csv"));
        fw.append("NO_PLAT,JENIS,JAM_MASUK,JAM_KELUAR,BIAYA,STATUS\n");

        while (rs.next()) {
            fw.append(rs.getString("NO_PLAT")).append(",");
            fw.append(rs.getString("JENIS")).append(",");
            fw.append(String.valueOf(rs.getTimestamp("JAM_MASUK"))).append(",");
            fw.append(String.valueOf(rs.getTimestamp("JAM_KELUAR"))).append(",");
            fw.append(String.valueOf(rs.getInt("BIAYA"))).append(",");
            fw.append(rs.getString("STATUS")).append("\n");
        }

        fw.close();
    }

    // Dipakai MainMenu & ExportCSVFrame
    public void exportRekapPendapatan() throws Exception {

        File dir = new File(BASE_DIR);
        ensureDir(dir);

        String sql =
                "SELECT TRUNC(JAM_KELUAR) AS TANGGAL, " +
                        "SUM(TOTAL_BIAYA) AS TOTAL_PENDAPATAN " +
                        "FROM PARKIR_KELUAR " +
                        "GROUP BY TRUNC(JAM_KELUAR) " +
                        "ORDER BY TANGGAL";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        FileWriter fw = new FileWriter(new File(dir, "rekap_pendapatan.csv"));
        fw.append("TANGGAL,TOTAL_PENDAPATAN\n");

        while (rs.next()) {
            fw.append(rs.getDate("TANGGAL").toString()).append(",");
            fw.append(String.valueOf(rs.getInt("TOTAL_PENDAPATAN"))).append("\n");
        }

        fw.close();
    }

    // =====================================================
    // =============== FITUR BARU (DINAMIS) =================
    // =====================================================

    // export/csv/2026/02/2026-02-03.csv
    public void exportPerTanggal(int tahun, int bulan, int tanggal) throws Exception {

        File dir = new File(
                BASE_DIR + "/" + tahun + "/" + String.format("%02d", bulan)
        );
        ensureDir(dir);

        String sql =
                "SELECT NO_PLAT, JENIS, JAM_MASUK, JAM_KELUAR, BIAYA, STATUS " +
                        "FROM V_DATA_PARKIR " +
                        "WHERE EXTRACT(YEAR FROM JAM_MASUK) = ? " +
                        "AND EXTRACT(MONTH FROM JAM_MASUK) = ? " +
                        "AND EXTRACT(DAY FROM JAM_MASUK) = ? " +
                        "ORDER BY JAM_MASUK";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, tahun);
        ps.setInt(2, bulan);
        ps.setInt(3, tanggal);

        ResultSet rs = ps.executeQuery();

        String fileName = tahun + "-" +
                String.format("%02d", bulan) + "-" +
                String.format("%02d", tanggal) + ".csv";

        FileWriter fw = new FileWriter(new File(dir, fileName));
        fw.append("NO_PLAT,JENIS,JAM_MASUK,JAM_KELUAR,BIAYA,STATUS\n");

        while (rs.next()) {
            fw.append(rs.getString("NO_PLAT")).append(",");
            fw.append(rs.getString("JENIS")).append(",");
            fw.append(String.valueOf(rs.getTimestamp("JAM_MASUK"))).append(",");
            fw.append(String.valueOf(rs.getTimestamp("JAM_KELUAR"))).append(",");
            fw.append(String.valueOf(rs.getInt("BIAYA"))).append(",");
            fw.append(rs.getString("STATUS")).append("\n");
        }

        fw.close();
    }

    // export/csv/2026/02/rekap_2026-02.csv
    public void exportPerBulan(int tahun, int bulan) throws Exception {

        File dir = new File(
                BASE_DIR + "/" + tahun + "/" + String.format("%02d", bulan)
        );
        ensureDir(dir);

        String sql =
                "SELECT TRUNC(JAM_KELUAR) AS TANGGAL, " +
                        "SUM(TOTAL_BIAYA) AS TOTAL " +
                        "FROM PARKIR_KELUAR " +
                        "WHERE EXTRACT(YEAR FROM JAM_KELUAR) = ? " +
                        "AND EXTRACT(MONTH FROM JAM_KELUAR) = ? " +
                        "GROUP BY TRUNC(JAM_KELUAR) " +
                        "ORDER BY TANGGAL";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, tahun);
        ps.setInt(2, bulan);

        ResultSet rs = ps.executeQuery();

        FileWriter fw = new FileWriter(
                new File(dir, "rekap_" + tahun + "-" + String.format("%02d", bulan) + ".csv")
        );

        fw.append("TANGGAL,TOTAL_PENDAPATAN\n");

        while (rs.next()) {
            fw.append(rs.getDate("TANGGAL").toString()).append(",");
            fw.append(String.valueOf(rs.getInt("TOTAL"))).append("\n");
        }

        fw.close();
    }
}
