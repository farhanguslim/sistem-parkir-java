package dao;

import config.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkirMasukDAO {

    // ================= CEK KENDARAAN MASIH PARKIR =================
    public boolean isMasihParkir(String noPlat) throws Exception {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM PARKIR_MASUK " +
                        "WHERE NO_PLAT = ? " +
                        "AND STATUS = 'MASIH PARKIR'";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, noPlat);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1) > 0;
    }

    // ================= PARKIR MASUK =================
    public void insert(String noPlat, String jenis) throws Exception {

        // 🔒 CEGAH DATA DUPLIKAT (MASIH PARKIR)
        if (isMasihParkir(noPlat)) {
            throw new Exception("Kendaraan ini masih berada di dalam parkiran");
        }

        String sql =
                "INSERT INTO PARKIR_MASUK " +
                        "(ID_MASUK, NO_PLAT, JENIS, JAM_MASUK, STATUS) " +
                        "VALUES (SEQ_PARKIR_MASUK.NEXTVAL, ?, ?, SYSDATE, 'MASIH PARKIR')";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);

        ps.setString(1, noPlat);
        ps.setString(2, jenis);

        ps.executeUpdate();
    }
}
