package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("config/db.properties"));

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String pass = prop.getProperty("db.password");

            return DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            System.out.println("❌ Gagal koneksi database");
            e.printStackTrace();
            return null;
        }
    }
}
