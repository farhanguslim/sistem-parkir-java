package dao;

import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RekapPendapatanDAO {

    public int getPendapatanHariIni() {
        int total = 0;

        String sql =
                "SELECT NVL(SUM(BIAYA), 0) AS TOTAL " +
                        "FROM PARKIR_MASUK " +
                        "WHERE STATUS = 'SUDAH KELUAR' " +
                        "AND JAM_KELUAR IS NOT NULL " +
                        "AND TRUNC(JAM_KELUAR) = TRUNC(SYSDATE)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                total = rs.getInt("TOTAL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}
