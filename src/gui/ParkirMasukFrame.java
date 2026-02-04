package gui;

import dao.ParkirMasukDAO;

import javax.swing.*;
import java.awt.*;

public class ParkirMasukFrame extends JFrame {

    private JTextField txtPlat;
    private JRadioButton rbMotor, rbMobil;
    private JButton btnSimpan, btnBatal;

    public ParkirMasukFrame() {

        setTitle("Parkir Masuk");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== PANEL =====
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("No Plat"));
        txtPlat = new JTextField();
        panel.add(txtPlat);

        panel.add(new JLabel("Jenis Kendaraan"));
        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT));

        rbMotor = new JRadioButton("Motor");
        rbMobil = new JRadioButton("Mobil");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMotor);
        bg.add(rbMobil);

        panelRadio.add(rbMotor);
        panelRadio.add(rbMobil);

        panel.add(panelRadio);

        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");

        panel.add(btnSimpan);
        panel.add(btnBatal);

        add(panel);

        // ===== ACTION =====
        btnSimpan.addActionListener(e -> simpan());
        btnBatal.addActionListener(e -> dispose());

        // ENTER = SIMPAN
        getRootPane().setDefaultButton(btnSimpan);
    }

    private void simpan() {

        String noPlat = txtPlat.getText()
                .trim()
                .toUpperCase()
                .replaceAll("\\s+", " ");

        String jenis = null;
        if (rbMotor.isSelected()) {
            jenis = "Motor";
        } else if (rbMobil.isSelected()) {
            jenis = "Mobil";
        }

        // ===== VALIDASI =====
        if (noPlat.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No plat wajib diisi",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jenis == null) {
            JOptionPane.showMessageDialog(this,
                    "Pilih jenis kendaraan (Motor / Mobil)",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ParkirMasukDAO dao = new ParkirMasukDAO();
            dao.insert(noPlat, jenis);

            JOptionPane.showMessageDialog(this,
                    "✅ Parkir masuk berhasil");
            dispose();

        } catch (Exception e) {

            if (e.getMessage().contains("Kendaraan masih berada")) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Kendaraan masih berada di dalam parkir",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
