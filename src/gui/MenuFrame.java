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
        setSize(1200, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ================= TABLE =================
        String[] kolom = {
                "NO_PLAT",
                "JENIS",
                "JAM_MASUK",
                "JAM_KELUAR",
                "BIAYA",
                "STATUS"
        };

        model = new DefaultTableModel(kolom, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // ================= BUTTON =================
        JButton btnRefresh = new JButton("Refresh");
        JButton btnMasuk = new JButton("Parkir Masuk");
        JButton btnKeluar = new JButton("Parkir Keluar");
        JButton btnLogout = new JButton("Logout");

        // ===== ADMIN BUTTON =====
        JButton btnUpdateJenis = new JButton("Update Jenis");
        JButton btnHapusPerPlat = new JButton("Hapus Per Plat");
        JButton btnBulkDelete = new JButton("Bulk Delete");
        JButton btnExportCSV = new JButton("Export CSV");
        JButton btnExportTanggal = new JButton("Export Rekap Per Hari");
        JButton btnChart = new JButton("Chart Pendapatan");

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // ================= PETUGAS =================
        panelButton.add(btnRefresh);
        panelButton.add(btnMasuk);
        panelButton.add(btnKeluar);

        // ================= ADMIN =================
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            panelButton.add(btnUpdateJenis);
            panelButton.add(btnHapusPerPlat);
            panelButton.add(btnBulkDelete);
            panelButton.add(btnExportCSV);
            panelButton.add(btnExportTanggal);
            panelButton.add(btnChart);
        }

        panelButton.add(btnLogout);

        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        // ================= ACTION =================
        btnRefresh.addActionListener(e -> loadData());

        btnMasuk.addActionListener(e ->
                new ParkirMasukFrame().setVisible(true)
        );

        btnKeluar.addActionListener(e ->
                new ParkirKeluarFrame().setVisible(true)
        );

        btnUpdateJenis.addActionListener(e ->
                new UpdateJenisFrame().setVisible(true)
        );

        btnHapusPerPlat.addActionListener(e ->
                new HapusParkirFrame().setVisible(true)
        );

        btnBulkDelete.addActionListener(e ->
                new BulkDeleteFrame().setVisible(true)
        );

        btnExportCSV.addActionListener(e ->
                new ExportCSVFrame().setVisible(true)
        );

        btnExportTanggal.addActionListener(e ->
                new ExportRekapTanggalFrame().setVisible(true)
        );

        btnChart.addActionListener(e ->
                new ChartPendapatanFrame().setVisible(true)
        );

        btnLogout.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin logout?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (ok == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // LOAD DATA PERTAMA
        loadData();
    }

    // ================= LOAD DATA =================
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
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
