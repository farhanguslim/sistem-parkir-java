package gui;

import dao.HapusParkirDAO;

import javax.swing.*;
import java.awt.*;

public class HapusParkirFrame extends JFrame {

    public HapusParkirFrame() {

        setTitle("Hapus Data Parkir (Per Plat)");
        setSize(350, 170);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField txtPlat = new JTextField();
        JButton btnHapus = new JButton("Hapus");

        setLayout(new GridLayout(4, 1, 10, 10));
        setPadding();

        add(new JLabel("No Plat Kendaraan"));
        add(txtPlat);
        add(new JLabel("⚠ Data akan dihapus PERMANEN"));
        add(btnHapus);

        btnHapus.addActionListener(e -> {
            String plat = txtPlat.getText().trim().toUpperCase();

            if (plat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No plat wajib diisi");
                return;
            }

            int ok = JOptionPane.showConfirmDialog(
                    this,
                    "Hapus TOTAL data kendaraan " + plat + "?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (ok != JOptionPane.YES_OPTION) return;

            try {
                HapusParkirDAO dao = new HapusParkirDAO();
                dao.deleteTotalByNoPlat(plat);

                JOptionPane.showMessageDialog(this,
                        "Data kendaraan " + plat + " berhasil dihapus");

                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // ENTER = HAPUS
        getRootPane().setDefaultButton(btnHapus);

        // ESC = BATAL
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
