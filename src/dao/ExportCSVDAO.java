package dao;

import config.DatabaseConnection;

import java.io.FileWriter;
import java.sql.*;

public class ExportCSVDAO {

    // ================= EXPORT DATA PARKIR =================
    public void exportDataParkir() throws Exception {

        String sql =
                "SELECT M.NO_PLAT, K.JENIS, M.JAM_MASUK, " +
                        "P.JAM_KELUAR, P.TOTAL_BIAYA AS BIAYA, " +
                        "CASE " +
                        " WHEN P.ID_KELUAR IS NULL THEN 'MASIH PARKIR' " +
                        " ELSE 'SUDAH KELUAR' " +
                        "END AS STATUS " +
                        "FROM PARKIR_MASUK M " +
                        "JOIN KENDARAAN K ON M.NO_PLAT = K.NO_PLAT " +
                        "LEFT JOIN PARKIR_KELUAR P ON M.ID_MASUK = P.ID_MASUK " +
                        "ORDER BY M.JAM_MASUK DESC";

        Statement st = DatabaseConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        FileWriter fw = new FileWriter("data_parkir.csv");

        // HEADER
        fw.append("NO_PLAT,JENIS,JAM_MASUK,JAM_KELUAR,BIAYA,STATUS\n");

        while (rs.next()) {
            fw.append(rs.getString("NO_PLAT")).append(",");
            fw.append(rs.getString("JENIS")).append(",");
            fw.append(rs.getString("JAM_MASUK")).append(",");
            fw.append(rs.getString("JAM_KELUAR")).append(",");
            fw.append(String.valueOf(rs.getInt("BIAYA"))).append(",");
            fw.append(rs.getString("STATUS")).append("\n");
        }

        fw.flush();
        fw.close();

        System.out.println("✅ data_parkir.csv berhasil dibuat");
    }

    // ================= EXPORT REKAP PENDAPATAN =================
    public void exportRekapPendapatan() throws Exception {

        String sql =
                "SELECT TRUNC(JAM_KELUAR) AS TANGGAL, " +
                        "COUNT(*) AS JUMLAH_KENDARAAN, " +
                        "SUM(TOTAL_BIAYA) AS TOTAL_PENDAPATAN " +
                        "FROM PARKIR_KELUAR " +
                        "GROUP BY TRUNC(JAM_KELUAR) " +
                        "ORDER BY TANGGAL";

        Statement st = DatabaseConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        FileWriter fw = new FileWriter("rekap_pendapatan.csv");

        // HEADER
        fw.append("TANGGAL,JUMLAH_KENDARAAN,TOTAL_PENDAPATAN\n");

        while (rs.next()) {
            fw.append(rs.getDate("TANGGAL").toString()).append(",");
            fw.append(String.valueOf(rs.getInt("JUMLAH_KENDARAAN"))).append(",");
            fw.append(String.valueOf(rs.getInt("TOTAL_PENDAPATAN"))).append("\n");
        }

        fw.flush();
        fw.close();

        System.out.println("✅ rekap_pendapatan.csv berhasil dibuat");
    }
}
