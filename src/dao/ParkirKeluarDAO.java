package dao;

import config.DatabaseConnection;

import java.sql.*;

public class ParkirKeluarDAO {

    // ================= HITUNG BIAYA =================
    public int hitungBiaya(String noPlat) throws Exception {

        String sql = """
            SELECT
                CEIL((SYSDATE - JAM_MASUK) * 24) AS JAM,
                JENIS
            FROM PARKIR_MASUK
            WHERE NO_PLAT = ?
            AND STATUS = 'MASIH PARKIR'
        """;

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, noPlat);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Kendaraan tidak ditemukan atau sudah keluar");
        }

        int jam = rs.getInt("JAM");
        String jenis = rs.getString("JENIS");

        int tarif = jenis.equalsIgnoreCase("Motor") ? 2000 : 5000;
        return jam * tarif;
    }

    // ================= PARKIR KELUAR (FIX ORACLE) =================
    public void parkirKeluar(String noPlat) throws Exception {

        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        try {
            // 1️⃣ Ambil ID_MASUK
            PreparedStatement psId = conn.prepareStatement("""
                SELECT ID_MASUK
                FROM PARKIR_MASUK
                WHERE NO_PLAT = ?
                AND STATUS = 'MASIH PARKIR'
            """);
            psId.setString(1, noPlat);

            ResultSet rsId = psId.executeQuery();

            if (!rsId.next()) {
                throw new Exception("Data parkir tidak ditemukan");
            }

            int idMasuk = rsId.getInt("ID_MASUK");

            // 2️⃣ Hitung biaya
            int biaya = hitungBiaya(noPlat);

            // 3️⃣ Insert PARKIR_KELUAR
            PreparedStatement psInsert = conn.prepareStatement("""
                INSERT INTO PARKIR_KELUAR
                (ID_KELUAR, ID_MASUK, JAM_KELUAR, TOTAL_BIAYA)
                VALUES
                (SEQ_PARKIR_KELUAR.NEXTVAL, ?, SYSDATE, ?)
            """);
            psInsert.setInt(1, idMasuk);
            psInsert.setInt(2, biaya);
            psInsert.executeUpdate();

            // 4️⃣ Update PARKIR_MASUK
            PreparedStatement psUpdate = conn.prepareStatement("""
                UPDATE PARKIR_MASUK
                SET JAM_KELUAR = SYSDATE,
                    BIAYA = ?,
                    STATUS = 'SUDAH KELUAR'
                WHERE ID_MASUK = ?
            """);
            psUpdate.setInt(1, biaya);
            psUpdate.setInt(2, idMasuk);
            psUpdate.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }

    // ================= DATA STRUK =================
    public ResultSet getStruk(String noPlat) throws Exception {

        String sql = """
            SELECT NO_PLAT, JENIS, JAM_MASUK, JAM_KELUAR, BIAYA
            FROM PARKIR_MASUK
            WHERE NO_PLAT = ?
            AND STATUS = 'SUDAH KELUAR'
            ORDER BY JAM_KELUAR DESC
        """;

        PreparedStatement ps =
                DatabaseConnection.getConnection().prepareStatement(sql);
        ps.setString(1, noPlat);

        return ps.executeQuery();
    }
}
