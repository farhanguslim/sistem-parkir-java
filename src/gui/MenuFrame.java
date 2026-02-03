package gui;

import dao.DataParkirDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class MenuFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private User user;

    public MenuFrame(User user) {
        this.user = user;

        setTitle("Sistem Parkir - " + user.getRole());
        setSize(1200, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ================= TABLE =================
        String[] kolom = {
                "NO_PLAT", "JENIS", "JAM_MASUK",
                "JAM_KELUAR", "BIAYA", "STATUS"
        };

        model = new DefaultTableModel(kolom, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= PANEL BUTTON =================
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // ===== TOMBOL REFRESH (INI YANG KAMU MAU) =====
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setToolTipText("Refresh Data Parkir");

        // ===== TOMBOL LAIN =====
        JButton btnMasuk = new JButton("Parkir Masuk");
        JButton btnKeluar = new JButton("Parkir Keluar");
        JButton btnUpdate = new JButton("Update Jenis");
        JButton btnRekap = new JButton("Rekap Pendapatan");
        JButton btnExport = new JButton("Export CSV");
        JButton btnBulk = new JButton("Bulk Delete");
        JButton btnHapusPlat = new JButton("Hapus Per Plat");
        JButton btnLogout = new JButton("Logout");

        // ===== TAMBAH KE PANEL =====
        panel.add(btnRefresh);   // ⬅️ REFRESH BENAR-BENAR ADA
        panel.add(btnMasuk);
        panel.add(btnKeluar);

        // ===== ADMIN ONLY =====
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            panel.add(btnUpdate);
            panel.add(btnRekap);
            panel.add(btnExport);
            panel.add(btnBulk);
            panel.add(btnHapusPlat);
        }

        panel.add(btnLogout);

        add(panel, BorderLayout.SOUTH);

        // ================= ACTION =================
        btnRefresh.addActionListener(e -> loadData());

        btnMasuk.addActionListener(e ->
                new ParkirMasukFrame().setVisible(true)
        );

        btnKeluar.addActionListener(e ->
                new ParkirKeluarFrame().setVisible(true)
        );

        btnUpdate.addActionListener(e ->
                new UpdateJenisFrame().setVisible(true)
        );

        btnRekap.addActionListener(e ->
                new ChartPendapatanFrame().setVisible(true)
        );

        btnExport.addActionListener(e ->
                new ExportCSVFrame().setVisible(true)
        );

        btnBulk.addActionListener(e ->
                new BulkDeleteFrame().setVisible(true)
        );

        btnHapusPlat.addActionListener(e ->
                new HapusParkirFrame().setVisible(true)
        );

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        // LOAD DATA AWAL
        loadData();
    }

    // ================= LOAD DATA PARKIR =================
    private void loadData() {
        model.setRowCount(0);

        try {
            DataParkirDAO dao = new DataParkirDAO();
            ResultSet rs = dao.getAll();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("NO_PLAT"),
                        rs.getString("JENIS"),
                        rs.getString("JAM_MASUK"),
                        rs.getString("JAM_KELUAR"),
                        rs.getInt("BIAYA"),
                        rs.getString("STATUS")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
