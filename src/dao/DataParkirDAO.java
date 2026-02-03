package dao;

import config.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataParkirDAO {

    public ResultSet getAll() throws Exception {
        String sql =
                "SELECT M.NO_PLAT, K.JENIS, M.JAM_MASUK, " +
                        "P.JAM_KELUAR, P.TOTAL_BIAYA AS BIAYA, " +
                        "CASE WHEN P.ID_KELUAR IS NULL THEN 'MASIH PARKIR' ELSE 'SUDAH KELUAR' END STATUS " +
                        "FROM PARKIR_MASUK M " +
                        "JOIN KENDARAAN K ON M.NO_PLAT = K.NO_PLAT " +
                        "LEFT JOIN PARKIR_KELUAR P ON M.ID_MASUK = P.ID_MASUK " +
                        "ORDER BY M.JAM_MASUK DESC";

        Statement st = DatabaseConnection.getConnection().createStatement();
        return st.executeQuery(sql);
    }
}
