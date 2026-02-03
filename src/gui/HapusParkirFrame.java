package gui;

import dao.BulkDeleteDAO;

import javax.swing.*;
import java.awt.*;

public class HapusParkirFrame extends JFrame {

    public HapusParkirFrame() {

        setTitle("Hapus Data Kendaraan");
        setSize(400, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnHapus = new JButton("HAPUS KENDARAAN SUDAH KELUAR");

        setLayout(new GridLayout(2,1,10,10));
        add(new JLabel("⚠️ Menghapus semua kendaraan yang sudah keluar"));
        add(btnHapus);

        btnHapus.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin hapus semua kendaraan yang sudah keluar?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (ok != JOptionPane.YES_OPTION) return;

            try {
                BulkDeleteDAO dao = new BulkDeleteDAO();
                dao.hapusKendaraanSudahKeluar();

                JOptionPane.showMessageDialog(this,
                        "✅ Data kendaraan sudah keluar berhasil dihapus");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}
