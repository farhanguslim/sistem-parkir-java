package dao;

import config.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.Statement;

public class DataParkirDAO {

    public ResultSet getAll() throws Exception {
        String sql = """
            SELECT NO_PLAT, JENIS, JAM_MASUK, JAM_KELUAR, BIAYA, STATUS
            FROM V_DATA_PARKIR
            ORDER BY JAM_MASUK DESC
        """;

        Statement st = DatabaseConnection
                .getConnection()
                .createStatement();

        return st.executeQuery(sql);
    }

    // ====== KHUSUS CLI ======
    public void printAllCLI() throws Exception {
        ResultSet rs = getAll();

        System.out.println("NO_PLAT | JENIS | MASUK | KELUAR | BIAYA | STATUS");
        System.out.println("------------------------------------------------");

        while (rs.next()) {
            System.out.println(
                    rs.getString("NO_PLAT") + " | " +
                            rs.getString("JENIS") + " | " +
                            rs.getTimestamp("JAM_MASUK") + " | " +
                            rs.getTimestamp("JAM_KELUAR") + " | " +
                            rs.getInt("BIAYA") + " | " +
                            rs.getString("STATUS")
            );
        }
    }
}
