package dao;

import config.DatabaseConnection;
import java.sql.*;

public class ParkirKeluarDAO {

    public String getJenisByNoPlat(String noPlat) throws Exception {
        String sql = "SELECT JENIS FROM KENDARAAN WHERE NO_PLAT = ?";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, noPlat);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getString("JENIS");
        throw new Exception("Kendaraan tidak ditemukan");
    }

    public int getBiayaByNoPlat(String noPlat) throws Exception {
        String sql =
                "SELECT FN_HITUNG_TARIF(M.JAM_MASUK, SYSDATE, K.JENIS) AS BIAYA " +
                        "FROM PARKIR_MASUK M " +
                        "JOIN KENDARAAN K ON M.NO_PLAT = K.NO_PLAT " +
                        "LEFT JOIN PARKIR_KELUAR P ON M.ID_MASUK = P.ID_MASUK " +
                        "WHERE M.NO_PLAT = ? AND P.ID_KELUAR IS NULL";

        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, noPlat);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("BIAYA");
        throw new Exception("Kendaraan tidak sedang parkir");
    }

    public void insertByNoPlat(String noPlat) throws Exception {
        String sql =
                "INSERT INTO PARKIR_KELUAR (ID_KELUAR, ID_MASUK, JAM_KELUAR, TOTAL_BIAYA) " +
                        "SELECT SEQ_PARKIR_KELUAR.NEXTVAL, M.ID_MASUK, SYSDATE, " +
                        "FN_HITUNG_TARIF(M.JAM_MASUK, SYSDATE, K.JENIS) " +
                        "FROM PARKIR_MASUK M " +
                        "JOIN KENDARAAN K ON M.NO_PLAT = K.NO_PLAT " +
                        "LEFT JOIN PARKIR_KELUAR P ON M.ID_MASUK = P.ID_MASUK " +
                        "WHERE M.NO_PLAT = ? AND P.ID_KELUAR IS NULL";

        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, noPlat);
        ps.executeUpdate();
    }
}
