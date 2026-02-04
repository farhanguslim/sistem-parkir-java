package gui;

import dao.ExportCSVDAO;

import javax.swing.*;
import java.awt.*;

public class ExportRekapTanggalFrame extends JFrame {

    public ExportRekapTanggalFrame() {

        setTitle("Export Rekap Per Tanggal");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField txtTahun = new JTextField();
        JTextField txtBulan = new JTextField();
        JTextField txtTanggal = new JTextField();

        JButton btnExport = new JButton("Export");

        setLayout(new GridLayout(5, 2, 10, 10));
        setPadding();

        add(new JLabel("Tahun (YYYY)"));
        add(txtTahun);

        add(new JLabel("Bulan (1-12)"));
        add(txtBulan);

        add(new JLabel("Tanggal (1-31)"));
        add(txtTanggal);

        add(new JLabel());
        add(btnExport);

        btnExport.addActionListener(e -> {
            try {
                int tahun = Integer.parseInt(txtTahun.getText().trim());
                int bulan = Integer.parseInt(txtBulan.getText().trim());
                int tanggal = Integer.parseInt(txtTanggal.getText().trim());

                ExportCSVDAO dao = new ExportCSVDAO();
                dao.exportPerTanggal(tahun, bulan, tanggal);

                JOptionPane.showMessageDialog(this,
                        "Export berhasil\n" +
                                "export/csv/" + tahun + "/" +
                                String.format("%02d", bulan));

                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Input harus angka!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // ENTER = Export
        getRootPane().setDefaultButton(btnExport);

        // ESC = Tutup
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private void setPadding() {
        ((JComponent) getContentPane())
                .setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
}
