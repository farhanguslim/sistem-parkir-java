package gui;

import dao.ParkirKeluarDAO;

import javax.swing.*;
import java.awt.*;

public class ParkirKeluarFrame extends JFrame {

    private JTextField txtPlat;
    private JLabel lblBiaya;
    private JButton btnCek, btnKeluar;

    public ParkirKeluarFrame() {

        setTitle("Parkir Keluar");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(new JLabel("No Plat"));
        txtPlat = new JTextField();
        panel.add(txtPlat);

        panel.add(new JLabel("Biaya"));
        lblBiaya = new JLabel("-");
        panel.add(lblBiaya);

        btnCek = new JButton("Cek Biaya");
        btnKeluar = new JButton("Keluar");
        btnKeluar.setEnabled(false);

        panel.add(btnCek);
        panel.add(btnKeluar);

        add(panel);

        btnCek.addActionListener(e -> cekBiaya());
        btnKeluar.addActionListener(e -> keluar());
    }

    private void cekBiaya() {
        try {
            String plat = txtPlat.getText().trim().toUpperCase();
            ParkirKeluarDAO dao = new ParkirKeluarDAO();
            int biaya = dao.getBiayaByNoPlat(plat);

            lblBiaya.setText("Rp " + biaya);
            btnKeluar.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void keluar() {
        try {
            String plat = txtPlat.getText().trim().toUpperCase();
            ParkirKeluarDAO dao = new ParkirKeluarDAO();
            dao.insertByNoPlat(plat);

            JOptionPane.showMessageDialog(this,
                    "✅ Parkir keluar berhasil");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
