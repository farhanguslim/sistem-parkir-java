package gui;

import dao.ParkirKeluarDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class ParkirKeluarFrame extends JFrame {

    private JTextField txtPlat;
    private JLabel lblBiaya;
    private final ParkirKeluarDAO dao = new ParkirKeluarDAO();

    public ParkirKeluarFrame() {
        setTitle("Parkir Keluar");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("No Plat"));
        txtPlat = new JTextField();
        add(txtPlat);

        add(new JLabel("Biaya"));
        lblBiaya = new JLabel("-");
        add(lblBiaya);

        JButton btnCek = new JButton("Cek Biaya");
        JButton btnKeluar = new JButton("Parkir Keluar");

        add(btnCek);
        add(btnKeluar);

        // ===== CEK BIAYA =====
        btnCek.addActionListener(e -> {
            try {
                String plat = txtPlat.getText().trim().toUpperCase();
                int biaya = dao.hitungBiaya(plat);
                lblBiaya.setText("Rp " + biaya);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // ===== PARKIR KELUAR + STRUK =====
        btnKeluar.addActionListener(e -> {
            try {
                String plat = txtPlat.getText().trim().toUpperCase();
                dao.parkirKeluar(plat);

                ResultSet rs = dao.getStruk(plat);
                if (rs.next()) {
                    new StrukParkirFrame(
                            rs.getString("NO_PLAT"),
                            rs.getString("JENIS"),
                            rs.getTimestamp("JAM_MASUK").toString(),
                            rs.getTimestamp("JAM_KELUAR").toString(),
                            rs.getInt("BIAYA")
                    ).setVisible(true);
                }

                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}
