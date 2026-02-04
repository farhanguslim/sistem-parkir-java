package dao;

import config.DatabaseConnection;

import java.sql.PreparedStatement;

public class KendaraanDAO {

    // ================= UPDATE JENIS KENDARAAN =================
    public void updateJenis(String noPlat, String jenisBaru) throws Exception {

        String sql =
                "UPDATE PARKIR_MASUK " +
                        "SET JENIS = ? " +
                        "WHERE NO_PLAT = ? " +
                        "AND STATUS = 'MASIH PARKIR'";

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);

        ps.setString(1, jenisBaru);
        ps.setString(2, noPlat);

        int affected = ps.executeUpdate();

        if (affected == 0) {
            throw new Exception(
                    "Kendaraan tidak ditemukan atau sudah keluar"
            );
        }
    }
}
