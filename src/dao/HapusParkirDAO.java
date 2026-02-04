package dao;

import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class HapusParkirDAO {

    public void deleteTotalByNoPlat(String noPlat) throws Exception {

        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        try {
            // hapus parkir keluar
            PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM PARKIR_KELUAR WHERE ID_MASUK IN " +
                            "(SELECT ID_MASUK FROM PARKIR_MASUK WHERE NO_PLAT = ?)"
            );
            ps1.setString(1, noPlat);
            ps1.executeUpdate();

            // hapus parkir masuk
            PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM PARKIR_MASUK WHERE NO_PLAT = ?"
            );
            ps2.setString(1, noPlat);
            ps2.executeUpdate();

            // hapus kendaraan
            PreparedStatement ps3 = conn.prepareStatement(
                    "DELETE FROM KENDARAAN WHERE NO_PLAT = ?"
            );
            ps3.setString(1, noPlat);
            ps3.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Gagal hapus total kendaraan");
        }
    }
}
