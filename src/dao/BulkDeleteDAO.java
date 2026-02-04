package dao;

import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BulkDeleteDAO {

    /**
     * 1️⃣ Hapus SEMUA data parkir (dipakai MainMenu)
     */
    public void deleteAll() {
        String sql = "DELETE FROM parkir_masuk";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2️⃣ Hapus kendaraan yang SUDAH KELUAR saja
     * (dipakai BulkDeleteFrame)
     */
    public void hapusKendaraanSudahKeluar() {
        String sql = """
            DELETE FROM parkir_masuk
            WHERE status = 'SUDAH KELUAR'
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 3️⃣ Reset semua data (alias hapus total)
     * (dipakai BulkDeleteFrame)
     */
    public void resetSemuaData() {
        deleteAll();
    }
}
