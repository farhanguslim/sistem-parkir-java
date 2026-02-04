package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            Properties props = new Properties();

            // Ambil db.properties dari classpath
            InputStream is = DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream("config/db.properties");

            if (is == null) {
                throw new RuntimeException("File db.properties tidak ditemukan di classpath");
            }

            props.load(is);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            System.out.println("❌ Gagal koneksi database");
            e.printStackTrace();
            return null;
        }
    }
}
