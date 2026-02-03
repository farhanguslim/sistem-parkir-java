package dao;

import config.DatabaseConnection;
import java.sql.*;

public class BulkDeleteDAO {

    // ================= HAPUS TOTAL PER PLAT =================
    public void deleteTotalByNoPlat(String noPlat) throws Exception {

        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);

        try {
            PreparedStatement ps1 = c.prepareStatement(
                    "DELETE FROM PARKIR_KELUAR WHERE ID_MASUK IN " +
                            "(SELECT ID_MASUK FROM PARKIR_MASUK WHERE NO_PLAT = ?)"
            );
            ps1.setString(1, noPlat);
            ps1.executeUpdate();

            PreparedStatement ps2 = c.prepareStatement(
                    "DELETE FROM PARKIR_MASUK WHERE NO_PLAT = ?"
            );
            ps2.setString(1, noPlat);
            ps2.executeUpdate();

            PreparedStatement ps3 = c.prepareStatement(
                    "DELETE FROM KENDARAAN WHERE NO_PLAT = ?"
            );
            ps3.setString(1, noPlat);
            ps3.executeUpdate();

            c.commit();

        } catch (Exception e) {
            c.rollback();
            throw e;
        }
    }

    // ================= HAPUS MASSAL KENDARAAN SUDAH KELUAR =================
    public void hapusKendaraanSudahKeluar() throws Exception {

        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);

        try (Statement st = c.createStatement()) {

            st.executeUpdate("DELETE FROM PARKIR_KELUAR");

            st.executeUpdate(
                    "DELETE FROM PARKIR_MASUK " +
                            "WHERE ID_MASUK NOT IN (SELECT ID_MASUK FROM PARKIR_KELUAR)"
            );

            st.executeUpdate(
                    "DELETE FROM KENDARAAN " +
                            "WHERE NO_PLAT NOT IN (SELECT NO_PLAT FROM PARKIR_MASUK)"
            );

            c.commit();

        } catch (Exception e) {
            c.rollback();
            throw e;
        }
    }

    // ================= RESET SEMUA DATA =================
    public void resetSemuaData() throws Exception {

        Connection c = DatabaseConnection.getConnection();
        Statement st = c.createStatement();

        st.executeUpdate("DELETE FROM PARKIR_KELUAR");
        st.executeUpdate("DELETE FROM PARKIR_MASUK");
        st.executeUpdate("DELETE FROM KENDARAAN");
    }
}
